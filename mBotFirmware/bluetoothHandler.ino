
void bluetoothreceiver() {
  if (bluetooth.available() > 0) // Send data only when you receive data:
  {
    data = bluetooth.read();
    switch(currentBTstate){
      case BTSTATE_LISTENING_ID:
        btMSGcounter = 0;   
        switch(data){
          case BTSTATE_LISTENING_ON_OFF:
            currentBTstate = BTSTATE_LISTENING_ON_OFF;
          break;
          case BTSTATE_LISTENING_KEYCOM:
            currentBTstate = BTSTATE_LISTENING_KEYCOM;
          break;
          case BTSTATE_LISTENING_CORRMSG:
            currentBTstate = BTSTATE_LISTENING_CORRMSG;
          break;
        }
      break;
      case BTSTATE_LISTENING_ON_OFF:
        if(data == '\n'){
          currentBTstate = BTSTATE_LISTENING_ID;
          btMSG[btMSGcounter] = data;
          int valMSG = translateBTMSG();
            
          switch(valMSG){
            case 100:
              rgb.setColor(0,0,0);
              rgb.setColor(185,155,63);
              rgb.show();
              reset();
              canRun(1);
              break;
            case 0:
              rgb.setColor(0,0,0);
              rgb.setColor(20,0,0);
              rgb.show();
              canRun(0);
              break;
          }
        }else{
          btMSG[btMSGcounter] = data;
          btMSGcounter++;
        }
      break;
      case BTSTATE_LISTENING_KEYCOM:
        if(data == '\n'){
          currentBTstate = BTSTATE_LISTENING_ID;
        }else{
          keyCom(data);
        }
      break;
      case BTSTATE_LISTENING_CORRMSG:
         if(data == '\n'){
            currentBTstate = BTSTATE_LISTENING_ID;
            btMSG[btMSGcounter] = data;
            int valMSG = translateBTMSG();
            motorBTControll(valMSG);
        }else{
             btMSG[btMSGcounter] = data;
             btMSGcounter++;
        }
      break;
      default: // do nothing used to reset before new massage incase of transimission fault.
      break;
    }
  }
}

int translateBTMSG(){
  int value = 0;
  for( int i = 0; i < 15; i++){
    if(btMSG[i] == '\n'){
      return value;
    }
    else{
      int addValue = btMSG[i] - 48;
      value = value*(10)+addValue;
    }
  }
  return value;
}

void keyCom(char data){
  switch (data) {
          case BT_OPS:
            stopMotors();
            canMove = false;
            rgb.setColor(0, 0, 0);
            rgb.show();
            break;
          case BT_UPP:
            canMove = true;
            rgb.setColor(0, 0, 0);
            rgb.setColor(10, 10, 0);
            rgb.show();
            break;
          case BT_WEST:
            canMove = true;
            rgb.setColor(0, 0, 0);
            rgb.setColor(2, 10, 10, 0);
            rgb.show();
            break;
          case BT_EAST:
            canMove = true;
            rgb.setColor(0, 0, 0);
            rgb.setColor(1, 10, 10, 0);
            rgb.show();
            break;
          case BT_DOWN:
            canMove = true;
            rgb.setColor(0, 0, 0);
            rgb.setColor(10, 0, 0);
            rgb.show();
            break;
          case BT_0:
            rgb.setColor(0, 0, 0); //Switch off lamps
            rgb.show();
            break;
          case BT_1:
            rgb.setColor(10, 10, 10); //Set both lamps to white color with brightnes 10
            rgb.show(); //Show command to lamps
            break;
        }
}


void motorBTControll(int correctionValue){
  if(correctionValue > 100){// turn RIGHT
     if(correctionValue >200){
     }
     leftValue = -100;
     rightValue = (200-correctionValue);
  }else if(correctionValue < 100){// turn LEFT
     if(correctionValue < 0){
     }
      leftValue = -correctionValue;
      rightValue = 100;
  }else{ // = 100 STRIGHT
    rightValue = 100;
    leftValue = -100;
  }
   //drive();
}



void BTmessage(int pos, int state ) {
  bluetooth.print(123);
  bluetooth.print(',');
  bluetooth.print(pos);
  bluetooth.print(',');
  bluetooth.print(state);
  bluetooth.print('\n');
}
