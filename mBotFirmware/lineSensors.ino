/*
 * lineCounter: 
 * counts dashedlines, changes states and updates positions
 * sets systemStepCounter += 75; to update state
 */
void lineCounter() {
  int sensorState;
  sensorState = dashed.readSensors();
  switch (currentLineState)
  {
    case LINESTATE_OUTSIDE:
      switch (sensorState) {
        case S1_OUT_S2_IN:
          currentLineState = LINESTATE_HALFINSIDE_01;
          //sendJsonMSG();
          break;
        case S1_IN_S2_OUT:
          currentLineState = LINESTATE_HALFINSIDE_10;
          //sendJsonMSG();
          break;
        case S1_IN_S2_IN:
          currentLineState = LINESTATE_INSIDE;
          //sendJsonMSG();
          break;
      }
      break;
    case LINESTATE_INSIDE:
      switch (sensorState) {
        case S1_OUT_S2_IN:
          currentLineState = LINESTATE_HALFOUTSIDE_01;
          //sendJsonMSG();
          break;
        case S1_IN_S2_OUT:
          currentLineState = LINESTATE_HALFOUTSIDE_10;
         //sendJsonMSG();
          break;
        case S1_OUT_S2_OUT:
          currentLineState = LINESTATE_OUTSIDE;
          dashedLinesCounter++;
          //sendJsonMSG();
          systemStepCounter += SPEEDUPMSG;
          break;
      }
      break;
    case LINESTATE_HALFINSIDE_10:
      if (sensorState == S1_IN_S2_IN) {
        currentLineState = LINESTATE_INSIDE;
        //sendJsonMSG();
      }
      break;
    case LINESTATE_HALFINSIDE_01:
      if (sensorState == S1_IN_S2_IN) {
        currentLineState = LINESTATE_INSIDE;
        //sendJsonMSG();
      }
      break;
    case LINESTATE_HALFOUTSIDE_10:
      if (sensorState == S1_OUT_S2_OUT) {
        currentLineState = LINESTATE_OUTSIDE;
        dashedLinesCounter++;
        //sendJsonMSG();
        systemStepCounter += SPEEDUPMSG;
      }
      break;
    case LINESTATE_HALFOUTSIDE_01:
      if (sensorState == S1_OUT_S2_OUT) {
        currentLineState = LINESTATE_OUTSIDE;
        dashedLinesCounter++;
        //sendJsonMSG();
        systemStepCounter += SPEEDUPMSG;
      }
      break;
  }
}

/*
 * lineFollower:
 * reads from sensor and adapting to follow line
 */
void lineFollower(){
  int state = line.readSensors();
  switch(state)
  {
    case S1_IN_S2_IN: //ONLINE
      on_Line = true;
      LineFollowFlag = 10;
      forward();
      break;

    case S1_OUT_S2_IN: // Turn Left
      if(LineFollowFlag > 1){ 
        LineFollowFlag--;
      }
      on_Line = false;
      forward();    
      break;

    case S1_IN_S2_OUT: // Turn Right
      if(LineFollowFlag < 20){  
        LineFollowFlag++;
      }
      on_Line = false;
      forward();
      break;

    case S1_OUT_S2_OUT: //OFFLINE
      //canMove = false;
      //stopMotors();
      break;
  }
  
  if(LineFollowFlag < 10) {
    leftValue = -10;
    // old value -20,100
    rightValue = 110;
    drive();
  }
  
  if(LineFollowFlag > 10) {
    leftValue = -110;
    //old value -100,20
    rightValue = 10;
    drive();
  }
}
