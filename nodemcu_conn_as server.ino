#include <ESP8266WiFi.h>

const char* ssid = "rtj";
const char* password = "shd123456";
WiFiServer server(80);

void printWiFiStatus();

void setup(void) {
  Serial.begin(9600);
  WiFi.begin(ssid, password);

  // Configure GPIO2 as OUTPUT.
  pinMode(2, OUTPUT);

  // Start TCP server.
  server.begin();
}

void loop(void) {
  // Check if module is still connected to WiFi.
  if (WiFi.status() != WL_CONNECTED) {
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
    }
    // Print the new IP to Serial.
    printWiFiStatus();
  }

  WiFiClient client = server.available();

  if (client) {
    Serial.println("Client connected.");
    
    while (client.connected()) {
      if (client.available()) {
        digitalWrite(2, HIGH);
        char command = client.read();
        Serial.println(command);
        
      }
    }
    Serial.println("Client disconnected.");
    client.stop();
  }
}

void printWiFiStatus() {
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}
