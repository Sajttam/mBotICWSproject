

#include <Wire.h>
#include <SoftwareSerial.h>
#include "MeMCore.h"
#include <string.h>

int potPin = 0;    // select the input pin for the potentiometer
int val = 0;

MeRGBLed rgb(0, 16);  /* parameter description: port, slot, led number */
MeDCMotor MotorL(M1);
MeDCMotor MotorR(M2);

MeBluetooth bluetooth(PORT_5);
MeLineFollower line(PORT_4);
MeLineFollower dashed(PORT_2);


#define BT_A    65
#define BT_B    66
#define BT_C    67
#define BT_D    68
#define BT_E    69
#define BT_F    70
#define BT_OPS  35
#define BT_UPP  94
#define BT_WEST 60
#define BT_EAST 62
#define BT_DOWN 118
#define BT_0    48
#define BT_1    49
#define BT_2    50
#define BT_3    51
#define BT_4    52
#define BT_5    53
#define BT_6    54
#define BT_7    55
#define BT_8    56
#define BT_9    57

// States for lineFollowerSensor
#define LINESTATE_OUTSIDE         0
#define LINESTATE_INSIDE          1
#define LINESTATE_HALFINSIDE_10   2
#define LINESTATE_HALFINSIDE_01   3
#define LINESTATE_HALFOUTSIDE_10  4
#define LINESTATE_HALFOUTSIDE_01  5
#define LINESTATE_SOLID_LEFTOUT   6 
#define LINESTATE_SOLID_RIGHTOUT  7

int currentLineState = 0;
int dashedLinesCounter = 0;
int LineFollowFlag = 10;
bool canMove = false;

//States for bluetooth
#define BTSTATE_LISTENING_ID 0
#define BTSTATE_LISTENING_ON_OFF 1
#define BTSTATE_LISTENING_KEYCOM 2
#define BTSTATE_LISTENING_CORRMSG 3

int currentBTstate = 0;
char btMSG[16];               // MSG for BT communication
int btMSGcounter = 0;
char data = 0;                //Variable for storing received data

// DRIVE VALUES
int leftValue = -100;
int rightValue = 100;
int moveSpeed = 125;
int basMotorSpeed= 25;

void setup() {
  Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
  bluetooth.begin(115200);    //The factory default baud rate is 115200
  if ((rgb.getPort() != PORT_7) || rgb.getSlot() != SLOT2) {
    rgb.reset(PORT_7, SLOT2);;
  }
  rgb.setColor(0, 0, 0);
  rgb.show();
  reset();
}

void reset(){
  leftValue = -100;
  rightValue = 100;
  moveSpeed = 125;
  basMotorSpeed= 25;
  canMove = false;
  currentLineState = 0;
  dashedLinesCounter = 0;
  LineFollowFlag = 10;
}



void loop() {
  bluetoothreceiver(); // Listen and react to bluetooth messages
  driveAfterSolid(); 
  lineCounter();
  delay(1);
}
