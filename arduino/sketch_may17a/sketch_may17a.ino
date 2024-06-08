#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>  // Thêm thư viện MQTT
#include <ArduinoJson.h>

#define DHTPIN A4         // Chân kết nối cảm biến DHT11
#define DHTTYPE DHT11      // DHT 11
#define MQ4_PIN A6         // Chân kết nối cảm biến MQ4
#define MQ3_PIN A7        // Chân kết nối cảm biến MQ3
#define BUZZER_PIN A5    // chân còi chip

// Cấu hình mạng WiFi
const char* ssid = "Tang 3";
const char* password = "1234567890";

// Cấu hình MQTT Broker
const char* mqtt_server = "192.168.1.190";  // Địa chỉ MQTT Broker
const int mqtt_port = 1883;

float lastTemp = 0;
float lastHumi = 0;
int lastMq3 = 0;
int lastMq4 = 0;

DHT dht(DHTPIN, DHTTYPE);
LiquidCrystal_I2C lcd(0x27, 16, 2);
WiFiClient espClient;
PubSubClient client(espClient);

void setup_wifi() {
  delay(10);
  // Connect WiFi
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* payload, unsigned int length) {
  // Handle message arrived
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("arduinoClient")) {
      Serial.println("connected");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void setup() {
  Serial.begin(9600);

  // Initialize LCD
  lcd.init();
  lcd.backlight();
  pinMode(BUZZER_PIN, OUTPUT);
  // Connect to WiFi
  setup_wifi();

  // Set MQTT server and callback function
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

  dht.begin();
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();

  // Read analog values of MQ3 and MQ4
  int mq4Value = analogRead(MQ4_PIN);
  int mq3Value = analogRead(MQ3_PIN);

  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("C:");
  lcd.print(temperature, 1); // One digit after the decimal point
  lcd.print("C H:");
  lcd.print(humidity, 1); // One digit after the decimal point
  lcd.setCursor(0, 1);
  lcd.print("Q4:");
  lcd.print(mq4Value);
  lcd.print(" Q3:");
  lcd.print(mq3Value);
  if (mq4Value > 1000 || mq3Value>1500 ) {
    // Turn on the buzzer
    digitalWrite(BUZZER_PIN, HIGH);
    delay(200); // Delay for sound effect
    // Turn off the buzzer
    digitalWrite(BUZZER_PIN, LOW);
    delay(200);
  }
  else{
    digitalWrite(BUZZER_PIN, HIGH);
  }
  // Send data via MQTT
  if (client.connected()) {
    if(abs(lastTemp - temperature) > 2
    || abs(lastHumi - humidity) > 2
    || abs(lastMq3 - mq3Value) > 200
    || abs(lastMq4 - mq4Value) > 200
    ){
        // Create JSON payload
      StaticJsonDocument<200> doc;
      doc["temperature"] = temperature;
      doc["humidity"] = humidity;
      doc["mq4"] = mq4Value;
      doc["mq3"] = mq3Value;

      String jsonString;
      serializeJson(doc, jsonString);

      char topic[50];
      snprintf(topic, 50, "sensor/data");
      client.publish(topic, jsonString.c_str());

      lastTemp = temperature;
      lastHumi = humidity;
      lastMq3 = mq3Value;
      lastMq4 = mq4Value;
    }
  }

  delay(1000); // Wait 5 seconds before sending data again
}