🌬️ Monitor de Qualidade do Ar com ESP32 e Flutter 📱
Monitoramento em tempo real da qualidade do ar com ESP32 e Flutter! O ESP32 coleta dados do sensor MQ-7 e os envia via MQTT para um app Flutter, que exibe a concentração de monóxido de carbono (CO) de forma intuitiva.

🛠️ Componentes do Projeto
🧰 Hardware
ESP32 → Coleta os dados e publica no broker MQTT.
Sensor MQ-7 → Mede a concentração de CO no ar.
WiFi → Envia os dados para o broker MQTT.
📱 Software
ESP32 (esp32.ino) → Configura o sensor, lê os dados e publica no MQTT.
Flutter (main.dart) → Conecta-se ao MQTT e exibe as informações.
📋 Funcionalidades
ESP32
✅ Leitura do sensor MQ-7
🔥 Ciclo de aquecimento automático
📡 Publicação dos dados via MQTT

Flutter
📶 Conexão com o broker MQTT
🎨 Interface gráfica intuitiva
🟢🔴 Status da qualidade do ar baseado na concentração de CO

🚀 Como Executar
🔧 Configuração do ESP32
1️⃣ Conecte o sensor MQ-7 ao ESP32 conforme o código esp32.ino.
2️⃣ Configure o WiFi substituindo your_wifi e your_password.
3️⃣ Carregue o código no ESP32 via Arduino IDE ou PlatformIO.

📲 Configuração do Flutter
1️⃣ Clone o repositório:

bash
Copiar
Editar
git clone https://github.com/seu-repo
2️⃣ Instale as dependências:

bash
Copiar
Editar
flutter pub get
3️⃣ Execute o app:

bash
Copiar
Editar
flutter run
📊 Escala de Qualidade do Ar
CO (ppm)	Qualidade
0 - 9	🌿 Excelente
9 - 50	🌱 Boa
50 - 100	🌕 Moderada
100 - 300	🌧️ Ruim
> 300	🔴 Perigosa
📄 Estrutura do Código
ESP32 (esp32.ino)
🔹 Conexão WiFi + MQTT
🔹 Leitura do sensor e ciclo de aquecimento
🔹 Publicação no tópico MQTT

Flutter (main.dart)
🔹 Conexão ao MQTT
🔹 Exibição gráfica dos dados
🔹 Gerenciamento de estado com Provider

📚 Dependências
ESP32
WiFi.h → Conexão WiFi
PubSubClient.h → Comunicação MQTT
MQUnifiedsensor.h → Leitura do MQ-7
Flutter
mqtt_client → Comunicação MQTT
flutter_hooks → Gerenciamento de estado
provider → Estado global
logger → Log do aplicativo
📷 Capturas de Tela
(Adicione prints do app aqui! 📸)

