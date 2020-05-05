/*
 * bluetoothReceiver:
 * listens to BT and reads MSG.
 * Sends completed msg to parser
 */
void bluetoothReceiver(){
 if(currentBTstate == BTSTATE_LISTENING){
    while(bluetooth.available() > 0){
      data = bluetooth.read();
      if(data == '\n'){
        currentBTstate = BTSTATE_ACTION;
        break;
      }else{
        setRightLedColor(0,0,10);
        btMSG[btMSGcounter] = data;
        btMSGcounter++;
      }
    }
  }else if(currentBTstate == BTSTATE_ACTION){
    readJsonFormat(btMSG);
    //bluetooth.print(btMSG);
    turnOffRightLed();
    currentBTstate = BTSTATE_LISTENING;
    btMSGcounter = 0;
    data = 0;
    memset(btMSG,0,100);
  }
}

/*
 * sendJsonMSG:
 * send MSG containg all important data over BT
 * "{state:currentLineState,pos:dashedLinesCounter,lp:leftValue,rp:rightValue,bspeed:basMotorSpeed,dist:dist}"
 */
void sendJsonMSG(){  
  bluetooth.print("{\"state\":");
  bluetooth.print(currentLineState);
  bluetooth.print(",\"pos\":");
  bluetooth.print(dashedLinesCounter);
  bluetooth.print(",\"lp\":");
  bluetooth.print(leftValue);
  bluetooth.print(",\"rp\":");
  bluetooth.print(rightValue);
  bluetooth.print(",\"bspeed\":");
  bluetooth.print(basMotorSpeed);
  bluetooth.print(",\"dist\":");
  bluetooth.print(dist);
  bluetooth.print(",\"driving\":");
  if(canMove == 1){
     bluetooth.print("true");
  }else{
     bluetooth.print("false");
  }
  bluetooth.print("}");
}

void confirmMSG(char *message){
   bluetooth.print(message);
}
