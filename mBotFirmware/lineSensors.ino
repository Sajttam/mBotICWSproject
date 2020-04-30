
//Sensor 4
void driveAfterSolid(){
  int state = line.readSensors();
  switch(state)
  {
    case S1_IN_S2_IN:
      rgb.setColor(0,0,0);
      rgb.show();
      LineFollowFlag = 10;
      break;

    case S1_OUT_S2_IN:
      if(LineFollowFlag > 1){ // Turn Left
        LineFollowFlag--;
      }
      rgb.setColor(0,0,0);
      rgb.setColor(1,0,10,0);
      rgb.show();
      break;

    case S1_IN_S2_OUT:
      if(LineFollowFlag < 20){  // Turn Right
        LineFollowFlag++;
      }
      rgb.setColor(0,0,0);
      rgb.setColor(2,0,10,0);
      rgb.show();
      break;

    case S1_OUT_S2_OUT:
      canMove = false;
      rgb.setColor(0,0,0);
      rgb.setColor(10,0,0);
      rgb.show();
      LineFollowFlag == 10;
      stopMotors();
      break;
  }
    leftValue = -100;
    rightValue = 100;
    drive();
    if(LineFollowFlag < 10)
    {
     leftValue = -20;
     rightValue = 100;
     drive();
    }
    if(LineFollowFlag > 10)
    {
     leftValue = -100;
     rightValue = 20;
     drive();
    }
}

void lineCounter(){
  int state;
  state = dashed.readSensors();
  if(currentLineState == LINESTATE_INSIDE  && state == S1_OUT_S2_OUT){
    dashedLinesCounter++;
    currentLineState = LINESTATE_OUTSIDE;
  }else if(currentLineState == LINESTATE_OUTSIDE && state == S1_IN_S2_IN){
    currentLineState = LINESTATE_INSIDE;
  }
  BTmessage(dashedLinesCounter,currentLineState);
}
