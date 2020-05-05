

#include <Wire.h>
#include <SoftwareSerial.h>
#include "MeMCore.h"
#include <string.h>
#include <Arduino_JSON.h>

int systemStepCounter = 0;
int SPEEDUPMSG = 75;

MeRGBLed rgb(0, 16);  /* parameter description: port, slot, led number */
MeDCMotor MotorL(M1);
MeDCMotor MotorR(M2);

MeBluetooth bluetooth(PORT_5);
MeLineFollower line(PORT_2);
MeLineFollower dashed(PORT_4);
MeUltrasonicSensor ultrasensor(PORT_3);

// States for lineFollowerSensor
#define LINESTATE_OUTSIDE         0
#define LINESTATE_INSIDE          1
#define LINESTATE_HALFINSIDE_10   2
#define LINESTATE_HALFINSIDE_01   3
#define LINESTATE_HALFOUTSIDE_10  4
#define LINESTATE_HALFOUTSIDE_01  5
#define LINESTATE_OUT_OF_LINE     6

// FOR LINESENSORS
int currentLineState = 0;
int dashedLinesCounter = 0;
int LineFollowFlag = 10;
bool canMove = false;
bool on_Line = true;

//UltraSonic sensor
int dist = 0;
int olddist = 0;

//States for bluetooth
#define BTSTATE_LISTENING 0
#define BTSTATE_ACTION 1

bool connectedToBT = false;
int currentBTstate = BTSTATE_LISTENING;
char btMSG[100];               // MSG for BT communication
int btMSGcounter = 0;
char data = 0;                //Variable for storing received data

// DRIVE VALUES
int leftValue = -100;
int rightValue = 100;
int basMotorSpeed = 20;

/*
 *setup: 
 *runs on bootup,
 *sets bluetooth connection
 *resets LEDs
 */
void setup() {
  //Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
  bluetooth.begin(115200);//The factory default baud rate is 115200
  if ((rgb.getPort() != PORT_7) || rgb.getSlot() != SLOT2) {
    rgb.reset(PORT_7, SLOT2);;
  }
  rgb.setColor(0, 0, 0);
  rgb.show();
}

/*
 * resetMbot:
 * reset values to standard values
 */
void resetMbot(){
  leftValue = -100;
  rightValue = 100;
  basMotorSpeed= 20;
  canMove = false;
  on_Line = true;
  currentLineState = 0;
  dashedLinesCounter = 0;
  LineFollowFlag = 10;
  dist = 0;
}

/*
 * loop:
 * Program loop
 */
void loop() {
  systemStepCounter++;
  bluetoothReceiver();// Listen and react to bluetooth messages
  lineFollower(); 
  lineCounter();
  ultraSonic();
  if((systemStepCounter >= 150) && connectedToBT){
    sendJsonMSG();
    systemStepCounter = 0;
  }
  delay(1);
}
