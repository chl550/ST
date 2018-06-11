
#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <string.h>
static const int RXPin = 4, TXPin = 3;
static const uint32_t GPSBaud = 9600;

// The TinyGPS++ object
TinyGPSPlus gps;

// The serial connection to the GPS device
SoftwareSerial ss(3,4);
// Serial connection to the Bluetooth module
SoftwareSerial EEBlue(10, 11);
int counter;
char buf[16];
char buf2[16];
char dest[64] = {0};
int sensorValue = 0;
float c = millis();
void setup() {
  //Serial.begin(9600);
  EEBlue.begin(9600);
  //Serial.println("Bluetooth module open, use pasword 1234 to connect");
  ss.begin(GPSBaud);
  counter = 0;
  pinMode(2, INPUT);
  pinMode(9, OUTPUT);
}

void loop() {
  // This sketch displays information every time a new sentence is correctly encoded.
  
  sensorValue = digitalRead(2);
  float b = millis();
  /*
  if (sensorValue == 1) {
    counter = counter + 1;

  }
  if (counter > 1000) {
    b = millis();
    counter = 0;
    EEBlue.write("!");
  }
  if (b - c == 20000 && counter < 1000) {
    b = millis();
    counter = 0;
    c = millis();
  }*/
  //check if we receive a dollar sign signal
  
  if (ss.available() > 0) {
    gps.encode(ss.read());
    if (gps.location.isUpdated()) {
      
      float latitude = gps.location.lat();
      float longitude = gps.location.lng();
      //float latitude = 32.17898;
      //float longitude = -117.28138;
      dtostrf(latitude, 6, 6, buf);
      ss.end();
      strcat(dest, buf);
      strcat(dest, " ");
      dtostrf(longitude, 6, 5, buf2);
      strcat(dest, buf2);
      strcat(dest, "#");
      strcat(dest, "\n");

      EEBlue.begin(9600);
      EEBlue.write(dest);
      for ( int i = 0; i < sizeof(dest);  ++i ) {
        dest[i] = (char)0;
      }
      
    }
    
  }
  EEBlue.write(EEBlue.available());

  while (EEBlue.available() > 0) {
    if (EEBlue.read() == "%") {
      EEBlue.write("alert");
    }
    else if (EEBlue.read() == "v") {
      ss.begin(9600);
      break;
    }
      
  }
 
    c = millis();
    
    
    



  //send signals over to Android




}
