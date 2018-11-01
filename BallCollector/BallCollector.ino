#include <Servo.h>

#define TURN_TIME 330

Servo flipservo;
Servo phoneservo;

String bluetoothRead, Str_x, Str_y, Str_p;
int x ;
int y ;
String command;
int length;

//Motor 1
int pinAIN1 = 9; //Direction
int pinAIN2 = 8; //Direction
int pinPWMA = 11; //Speed

//Motor 2
int pinBIN1 = 7; //Direction
int pinBIN2 = 6; //Direction
int pinPWMB = 10; //Speed

//Standby
int pinSTBY = 12;

//Constants to help remember the parameters
static boolean turnCW = 0;  //for motorDrive function
static boolean turnCCW = 1; //for motorDrive function
static boolean motor1 = 0;  //for motorDrive, motorStop, motorBrake functions
static boolean motor2 = 1;  //for motorDrive, motorStop, motorBrake functions

//Direction
bool forward;
void setup() {
  Serial.begin(9600); 
   flipservo.attach(3);
    // Initially the servo must be stopped 
    flipservo.write(92);
    phoneservo.attach(5);
  
   pinMode(pinPWMA, OUTPUT);
  pinMode(pinAIN1, OUTPUT);
  pinMode(pinAIN2, OUTPUT);

  pinMode(pinPWMB, OUTPUT);
  pinMode(pinBIN1, OUTPUT);
  pinMode(pinBIN2, OUTPUT);

  pinMode(pinSTBY, OUTPUT);
  phoneservo.write(1);

  forward = 1;
  

}

void loop() {

  char commandbuffer[10];
  if(Serial.available())
  {
    
     delay(10);
  int i=0;
     while( Serial.available() && i< 9) {
        commandbuffer[i++] = Serial.read();
  
      
     }
     commandbuffer[i++]='\0';
     bluetoothRead = (char*)commandbuffer;
     length = bluetoothRead.length();
  
 
     
     if(bluetoothRead.substring(0, 1).equals("x"))
     {
      
       Str_x = bluetoothRead.substring(1, length-1);
       x = Str_x.toInt(); 
       
       Str_p = bluetoothRead.substring(length - 1, length);
       command = Str_p;
       Serial.println(command);
             
      Stop();
      

    if(command.equals("d"))//d for dipose
    {
      Stop();
      // flip container
      // Start turning clockwise
    flipservo.write(85);
    // Go on turning for the right duration
    delay(2.3*TURN_TIME);
    // Stop turning
    flipservo.write(92);
    delay(100);
    //TURN CCW
    flipservo.write(100);
    delay(2.3*TURN_TIME);
    //STOP
    flipservo.write(92);
   
  turnback();
      }
 if(command.equals("f"))//forward
   {
    if(x < 270 )
    {
         Left();                
     }
                
      if(x > 370)
       {
           Right();                
        } 
         if(x > 270 && x < 370)
         {
                if(forward)  
                  Forward(); 
                else
                  Back();
           }
                  
      } 
if(command.equals("r"))//rotate
{
  turn180();  
         
} 

if(command.equals("s"))//stop
{
     Stop();
}
      }
  }
}
   
void turn180() 
{
  //turn servo motor of the phone 180
  phoneservo.write(179);
  forward=0;
}
 void turnback()
 {
  //turn servo phone back to 0
  phoneservo.write(1);
  forward=1;
  }

void motorDrive(boolean motorNumber, boolean motorDirection, int motorSpeed)
{
  boolean pinIn1; 
  if (motorDirection == turnCW)
    pinIn1 = HIGH;
  else
    pinIn1 = LOW;

  if(motorNumber == motor1)
  {
    digitalWrite(pinAIN1, pinIn1);
    digitalWrite(pinAIN2, !pinIn1);
    analogWrite(pinPWMA, motorSpeed);
  }
  else
  {
    digitalWrite(pinBIN1, pinIn1);
    digitalWrite(pinBIN2, !pinIn1);  
    analogWrite(pinPWMB, motorSpeed);
  }
  digitalWrite(pinSTBY, HIGH);

}
void Left(){
  motorDrive(motor1, turnCW, 255);
  motorDrive(motor2, turnCCW, 255);  
}


void Right(){
  
  motorDrive(motor1, turnCCW, 255);
  motorDrive(motor2, turnCW, 255);
  
}


void Forward(){
  motorDrive(motor1, turnCCW, 255);
  motorDrive(motor2, turnCCW, 255);
}


void Back(){
 motorDrive(motor1, turnCW, 255);
  motorDrive(motor2, turnCW, 255);
}


void Stop(){ 
  motorDrive(motor1, turnCW, 0);
  motorDrive(motor2, turnCW, 0);
}
