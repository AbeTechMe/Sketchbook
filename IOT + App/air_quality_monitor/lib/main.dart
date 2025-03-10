import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mqtt_client/mqtt_client.dart';
import 'package:mqtt_client/mqtt_server_client.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:logger/logger.dart';

// Inicialização do logger para registro de eventos no aplicativo
final logger = Logger(
  printer: PrettyPrinter(methodCount: 0, lineLength: 80),
);

void main() {
  // Ponto de entrada do aplicativo Flutter
  runApp(
    // Utilizando ChangeNotifierProvider para gerenciar o estado global do app
    ChangeNotifierProvider(
      create: (_) => AirQualityModel(),
      child: const MyApp(),
    ),
  );
}

// Modelo para gerenciar os dados do sensor de qualidade do ar
class AirQualityModel extends ChangeNotifier {
  Map<String, dynamic> _sensorData = {"ppm": 0.0};
  bool _isConnected = false;
  String _connectionStatus = "Desconectado";

  Map<String, dynamic> get sensorData => _sensorData;
  bool get isConnected => _isConnected;
  String get connectionStatus => _connectionStatus;

  void updateSensorData(Map<String, dynamic> data) {
    _sensorData = data;
    notifyListeners();
  }

  void setConnectionStatus(bool connected, String status) {
    _isConnected = connected;
    _connectionStatus = status;
    notifyListeners();
  }

  // Método para interpretar a qualidade do ar com base no valor de PPM
  String getAirQualityStatus() {
    final ppm = _sensorData["ppm"] as double;
    if (ppm < 9) return "Excelente";
    if (ppm < 50) return "Boa";
    if (ppm < 100) return "Moderada";
    if (ppm < 300) return "Ruim";
    return "Perigosa";
  }

  // Método para determinar a cor que representa a qualidade do ar
  Color getStatusColor() {
    final ppm = _sensorData["ppm"] as double;
    if (ppm < 9) return Colors.green;
    if (ppm < 50) return Colors.lightGreen;
    if (ppm < 100) return Colors.yellow;
    if (ppm < 300) return Colors.orange;
    return Colors.red;
  }
}

// Widget principal do aplicativo
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false, // Remove o banner de debug
      title: 'Monitor de Qualidade do Ar',
      theme: ThemeData(
        // Definição do tema do aplicativo
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.blue,
          brightness: Brightness.light,
        ),
        useMaterial3: true, // Usando Material Design 3
        fontFamily: 'Roboto',
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.transparent,
          elevation: 0,
          centerTitle: true,
          foregroundColor: Colors.blue,
        ),
        cardTheme: CardTheme(
          elevation: 8,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        ),
      ),
      darkTheme: ThemeData(
        // Tema escuro do aplicativo
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.blue,
          brightness: Brightness.dark,
        ),
        useMaterial3: true,
        fontFamily: 'Roboto',
        cardTheme: CardTheme(
          elevation: 8,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        ),
      ),
      themeMode: ThemeMode.system, // Segue o tema do sistema
      home: const SensorScreen(),
    );
  }
}

// Tela principal do sensor utilizando Hooks para gerenciamento de estado local
class SensorScreen extends HookWidget {
  const SensorScreen({super.key});

  // Configurações do broker MQTT
  final String broker = "broker.emqx.io"; // Servidor MQTT público para teste
  final int port = 1883; // Porta padrão para MQTT sem TLS
  final String clientId = "flutter_air_monitor"; // Identificador único do cliente
  final String topic = "esp32/air_quality"; // Tópico para inscrição

  @override
  Widget build(BuildContext context) {
    // Usando hooks para memorizar o cliente MQTT e evitar recriação
    final client = useMemoized(() => MqttServerClient(broker, clientId), []);

    // Efeito que executa na montagem do widget
    useEffect(() {
      // Conecta ao broker MQTT quando o widget é inicializado
      _connectMQTT(client, context);

      // Função de limpeza executada na desmontagem do widget
      return () {
        logger.i("Desconectando do broker MQTT");
        client.disconnect();
      };
    }, []);

    // Obtém o modelo de dados do Provider
    final model = Provider.of<AirQualityModel>(context);

    return Scaffold(
      // AppBar com design moderno e transparente
      appBar: AppBar(
        title: const Text(
          "Qualidade do Ar",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        actions: [
          // Indicador de status de conexão
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              children: [
                Icon(
                  model.isConnected ? Icons.wifi : Icons.wifi_off,
                  color: model.isConnected ? Colors.green : Colors.red,
                ),
                const SizedBox(width: 8),
                Text(model.connectionStatus,
                    style: TextStyle(
                      color: model.isConnected ? Colors.green : Colors.red,
                      fontSize: 12,
                    )),
              ],
            ),
          ),
        ],
      ),
      // Corpo principal com layout responsivo
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: Column(
              children: [
                // Card principal com informações do sensor
                _buildMainSensorCard(context, model),

                const SizedBox(height: 24),

                // Informações adicionais sobre o dispositivo
                _buildDeviceInfoCard(context),

                const SizedBox(height: 24),

                // Card com informações sobre qualidade do ar
                _buildAirQualityInfoCard(context),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // Card principal com a leitura do sensor
  Widget _buildMainSensorCard(BuildContext context, AirQualityModel model) {
    final ppm = model.sensorData["ppm"] as double;
    final quality = model.getAirQualityStatus();
    final statusColor = model.getStatusColor();

    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Ícone animado que muda de cor conforme a qualidade do ar
            TweenAnimationBuilder<double>(
              tween: Tween<double>(begin: 0, end: ppm),
              duration: const Duration(seconds: 1),
              builder: (context, value, child) {
                return Column(
                  children: [
                    Icon(
                      CupertinoIcons.wind,
                      size: 80,
                      color: statusColor,
                    ),
                    const SizedBox(height: 16),
                    Text(
                      "Monóxido de Carbono (CO)",
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 24),
                    Text(
                      "${value.toStringAsFixed(1)} ppm",
                      style: Theme.of(context).textTheme.headlineLarge?.copyWith(
                        fontWeight: FontWeight.bold,
                        color: statusColor,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      decoration: BoxDecoration(
                        color: statusColor.withOpacity(0.2),
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: Text(
                        "Qualidade: $quality",
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          color: statusColor,
                        ),
                      ),
                    ),
                  ],
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  // Card com informações sobre o dispositivo
  Widget _buildDeviceInfoCard(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "Informações do Dispositivo",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            _buildInfoRow(context, "Sensor", "MQ-7 (Monóxido de Carbono)"),
            _buildInfoRow(context, "Dispositivo", "ESP32"),
            _buildInfoRow(context, "Localização", "Sala Principal"),
            _buildInfoRow(context, "Última Atualização", "Agora"),
          ],
        ),
      ),
    );
  }

  // Linha de informação para os cards
  Widget _buildInfoRow(BuildContext context, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            label,
            style: TextStyle(
              color: Theme.of(context).colorScheme.secondary,
              fontWeight: FontWeight.w500,
            ),
          ),
          Text(
            value,
            style: const TextStyle(fontWeight: FontWeight.bold),
          ),
        ],
      ),
    );
  }

  // Card com informações sobre qualidade do ar
  Widget _buildAirQualityInfoCard(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "Níveis de CO e Significado",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            _buildQualityRow(context, "0 - 9 ppm", "Excelente", Colors.green),
            _buildQualityRow(context, "9 - 50 ppm", "Boa", Colors.lightGreen),
            _buildQualityRow(context, "50 - 100 ppm", "Moderada", Colors.yellow),
            _buildQualityRow(context, "100 - 300 ppm", "Ruim", Colors.orange),
            _buildQualityRow(context, "> 300 ppm", "Perigosa", Colors.red),
          ],
        ),
      ),
    );
  }

  // Linha de informação para o card de qualidade do ar
  Widget _buildQualityRow(BuildContext context, String range, String quality, Color color) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          Container(
            width: 16,
            height: 16,
            decoration: BoxDecoration(
              color: color,
              shape: BoxShape.circle,
            ),
          ),
          const SizedBox(width: 12),
          Text(
            range,
            style: const TextStyle(fontWeight: FontWeight.w500),
          ),
          const Spacer(),
          Text(
            quality,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: color,
            ),
          ),
        ],
      ),
    );
  }

  // Método para conectar ao broker MQTT
  Future<void> _connectMQTT(
      MqttServerClient client, BuildContext context) async {
    final model = Provider.of<AirQualityModel>(context, listen: false);

    // Configuração do cliente MQTT
    client.logging(on: true); // Ativa logs para debug
    client.port = port;
    client.keepAlivePeriod = 60; // Período para manter conexão ativa

    // Callbacks para eventos de conexão
    client.onConnected = () {
      logger.i("MQTT Conectado ao broker $broker");
      model.setConnectionStatus(true, "Conectado");
    };

    client.onDisconnected = () {
      logger.w("MQTT Desconectado");
      model.setConnectionStatus(false, "Desconectado");
    };

    client.onSubscribed = (String topic) {
      logger.i("Inscrito no tópico: $topic");
    };

    // Mensagem de conexão MQTT
    final connMessage = MqttConnectMessage()
        .withClientIdentifier(clientId) // ID único do cliente
        .startClean() // Inicia uma sessão limpa
        .withWillQos(MqttQos.atMostOnce); // Qualidade de serviço
    client.connectionMessage = connMessage;

    try {
      // Tentativa de conexão ao broker MQTT
      logger.i("Conectando ao broker MQTT: $broker:$port");
      await client.connect();
    } catch (e) {
      logger.e("Erro na conexão MQTT: $e");
      model.setConnectionStatus(false, "Erro: $e");
      client.disconnect();
      return;
    }

    // Verifica se o cliente está conectado
    if (client.connectionStatus!.state == MqttConnectionState.connected) {
      logger.i("Cliente conectado");

      // Inscreve-se no tópico para receber dados
      client.subscribe(topic, MqttQos.atMostOnce);

      // Configura listener para mensagens recebidas
      client.updates!.listen((List<MqttReceivedMessage<MqttMessage?>>? messages) {
        if (messages == null) return;

        final MqttPublishMessage recMessage = messages[0].payload as MqttPublishMessage;

        // Converte bytes da mensagem para string
        final String payload = MqttPublishPayload.bytesToStringAsString(
            recMessage.payload.message
        );

        logger.d("Mensagem recebida: $payload");

        try {
          // Decodifica o JSON recebido
          final data = jsonDecode(payload);
          model.updateSensorData(data);
        } catch (e) {
          logger.e("Erro ao decodificar JSON: $e");
        }
      });
    } else {
      logger.e("Cliente desconectado");
      model.setConnectionStatus(false, "Falha na conexão");
      client.disconnect();
    }
  }
}