
void stopMotors() {
  MotorL.run(0);
  MotorR.run(0);
}

void canRun(int canRun){
    if(canRun == 0 && canMove == true ){
    canMove = false;
  }
  if(canRun == 1 && canMove == false ){
    canMove = true;
  }
  drive();
}

void drive(){
  if(canMove){
    MotorL.run(-basMotorSpeed+leftValue);
    MotorR.run(basMotorSpeed+rightValue);
  }else{
    stopMotors();
  }
}
