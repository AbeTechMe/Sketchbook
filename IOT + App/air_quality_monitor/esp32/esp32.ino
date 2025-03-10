#include <WiFi.h>
#include <PubSubClient.h>
#include <MQUnifiedsensor.h>

// Configurações de WiFi e MQTT
const char* WIFI_SSID = "your_wifi";
const char* WIFI_PASSWORD = "your_password";
const char* MQTT_BROKER = "broker.emqx.io";
const int MQTT_PORT = 1883;
const char* MQTT_TOPIC = "esp32/air_quality";
const char* MQTT_CLIENT_ID = "esp32_air_monitor";

// Configurações do Sensor MQ-7
#define SENSOR_PLACA "ESP-32"
#define SENSOR_VOLTAGE_RESOLUTION 3.3
#define SENSOR_ANALOG_PIN 32
#define SENSOR_TYPE "MQ-7"
#define SENSOR_ADC_RESOLUTION 12
#define SENSOR_CLEAN_AIR_RATIO 27.5
#define SENSOR_PWM_PIN 5

// Intervalos de tempo para ciclos de aquecimento e leitura
const unsigned long HEATING_HIGH_DURATION = 60 * 1000;  // 60 segundos em 5V
const unsigned long HEATING_LOW_DURATION = 90 * 1000;   // 90 segundos em 1.4V
const unsigned long PUBLISH_INTERVAL = 5000;            // 5 segundos entre publicações

// Objetos globais
MQUnifiedsensor MQ7(SENSOR_PLACA, SENSOR_VOLTAGE_RESOLUTION, SENSOR_ADC_RESOLUTION, SENSOR_ANALOG_PIN, SENSOR_TYPE);
WiFiClient espClient;
PubSubClient mqttClient(espClient);

unsigned long lastPublishTime = 0;
unsigned long heatingStartTime = 0;
bool isHighVoltageHeating = true;

void setupWiFi() {
  Serial.println("\nConectando ao WiFi...");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("\nWiFi conectado!");
  Serial.print("Endereço IP: ");
  Serial.println(WiFi.localIP());
}

void reconnectMQTT() {
  while (!mqttClient.connected()) {
    Serial.print("Tentando conectar ao broker MQTT... ");
    
    if (mqttClient.connect(MQTT_CLIENT_ID)) {
      Serial.println("Conectado!");
    } else {
      Serial.print("Falha. Código: ");
      Serial.println(mqttClient.state());
      delay(5000);
    }
  }
}

void calibrateSensor() {
  Serial.print("Calibrando sensor MQ-7... ");
  
  float calcR0 = 0;
  for(int i = 1; i <= 10; i++) {
    MQ7.update();
    calcR0 += MQ7.calibrate(SENSOR_CLEAN_AIR_RATIO);
    Serial.print(".");
  }
  
  MQ7.setR0(calcR0 / 10);
  Serial.println(" Concluído!");

  // Verificações de erro na calibração
  if (isinf(calcR0)) {
    Serial.println("Erro: Circuito aberto! Verifique o cabeamento.");
    while(1);
  }
  
  if (calcR0 == 0) {
    Serial.println("Erro: Curto-circuito no pino analógico!");
    while(1);
  }
}

void setupSensor() {
  // Configuração do modelo matemático de PPM
  MQ7.setRegressionMethod(1); // _PPM = a * ratio^b
  MQ7.setA(99.042);
  MQ7.setB(-1.518);

  // Inicialização do sensor
  MQ7.init();
  pinMode(SENSOR_PWM_PIN, OUTPUT);

  // Calibração
  calibrateSensor();
}

void setup() {
  Serial.begin(115200);
  
  // Configuração WiFi e MQTT
  setupWiFi();
  mqttClient.setServer(MQTT_BROKER, MQTT_PORT);

  // Configuração do sensor
  setupSensor();

  // Marca o início do aquecimento
  heatingStartTime = millis();
}

void loop() {
  // Garante conexão MQTT
  if (!mqttClient.connected()) {
    reconnectMQTT();
  }
  mqttClient.loop();

  unsigned long currentTime = millis();

  // Ciclo de aquecimento e leitura do sensor MQ-7
  if (isHighVoltageHeating) {
    analogWrite(SENSOR_PWM_PIN, 255); // 5V
    
    if (currentTime - heatingStartTime >= HEATING_HIGH_DURATION) {
      isHighVoltageHeating = false;
      heatingStartTime = currentTime;
    }
  } else {
    analogWrite(SENSOR_PWM_PIN, 20); // 1.4V
    
    if (currentTime - heatingStartTime >= HEATING_LOW_DURATION) {
      isHighVoltageHeating = true;
      heatingStartTime = currentTime;
    }
  }

  // Leitura e publicação a cada 5 segundos
  if (currentTime - lastPublishTime >= PUBLISH_INTERVAL) {
    lastPublishTime = currentTime;

    MQ7.update();
    float ppm = MQ7.readSensor();

    // Criar JSON com dados
    String jsonPayload = "{\"sensor\":\"MQ-7\",\"ppm\":" + String(ppm) + 
                         ",\"heating\":" + String(isHighVoltageHeating ? 5 : 1.4) + "}";

    Serial.print("Publicando: ");
    Serial.println(jsonPayload);

    // Publicar no tópico MQTT
    mqttClient.publish(MQTT_TOPIC, jsonPayload.c_str());
  }
}