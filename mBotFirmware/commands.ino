/*
 * stopMotors:
 * Set motorvalus to 0, stop mBot
 */
void stopMotors() {
  MotorL.run(0);
  MotorR.run(0);
}

/*
 * canRun: canRun (0,1)
 * sets the canMove, after input
 */
void canRun(int canRun){
  if(canRun == 0 && canMove == true ){
    canMove = false;
    setLeftLedColor(10,0,0);
  }
  if(canRun == 1 && canMove == false ){
    canMove = true;
    turnOffLeftLed();
  }
  drive();
}

/*
 * setLedColor: red , green , blue
 * Takes input rgb and sets both LEDs to color
 */
void setLedColor(int r, int g, int b) {
  rgb.setColor(0,0,0);
  rgb.setColor(r,g,b);
  rgb.show();
}
/*
 * turnOffLed 
 * Turns off all leds
 */
void turnOffLed(){
  rgb.setColor(0,0,0);
  rgb.show();
}
/*
 * turnOffLeftLed 
 * Turns off left led
 */
void turnOffLeftLed(){
  rgb.setColor(1,0,0,0);
  rgb.show();
}
/*
 * turnOffRightLed 
 * Turns off right led
 */
void turnOffRightLed(){
  rgb.setColor(2,0,0,0);
  rgb.show();
}
/*
 * setLeftLedColor: red , green , blue
 * Takes input rgb and sets left LED to color
 */
void setLeftLedColor(int r, int g, int b) {
  rgb.setColor(0,0,0);
  rgb.setColor(1,r,g,b);
  rgb.show();
}

/*
 * setRightLedColor: red , green , blue
 * Takes input rgb and sets right LED to color
 */
void setRightLedColor(int r, int g, int b) {
  rgb.setColor(0,0,0);
  rgb.setColor(2,r,g,b);
  rgb.show();
}

void motorCorrectionControll(int correctionValue){
  /*if(correctionValue > 100){// turn RIGHT
     leftValue = -100;
     rightValue = (200-correctionValue);
  }else if(correctionValue < 100){// turn LEFT
    leftValue = -correctionValue;
    rightValue = 100;
  }else{ // = 100 STRIGHT
    rightValue = 100;
    leftValue = -100;
  }
  if(on_Line){
    setLedColor(0,0,20);
    drive();
  }*/
}
/*
 * forward:
 * Drives forward
 */
void forward(){
  leftValue = -100;
  rightValue = 100;
  drive();
}

/*
 * drive:
 * Sets motorspeeds and checks if it is allowed to move
 */
void drive(){
  if(canMove){
    MotorL.run(-basMotorSpeed+leftValue);
    MotorR.run(basMotorSpeed+rightValue);
  }else{
    stopMotors();
  }
}
