#include <Servo.h>

Servo myservo;

void setup(){
    Serial.begin(9600);
    myservo.attach(9);
}

int pos = 90;

void loop(){
  
  if(myservo.read() != pos){  
    myservo.write(pos);
    delay(2000);  
  }
      if( Serial.available() ){
          byte empfang = Serial.read();
          if(empfang == 49){
            pos = pos + 5;
            Serial.println(empfang,DEC);
          }else if(empfang == 48){
            pos = pos - 5;
            Serial.println(empfang,DEC); 
          }
          //
      }
}
