/*
 * readJsonFormat: message 
 * Json parser, parses message and sets data accordingly
 */
void readJsonFormat(char *message) {
  JSONVar jsonInput = JSON.parse(message);
  if(JSON.typeof(jsonInput) == "undefined") {
    return;
  }
  if(jsonInput.hasOwnProperty("drive")) {
    if (((bool)jsonInput["drive"]) == true) {
      canRun(1);
    } else {
      canRun(0);
    }
  }
  if(jsonInput.hasOwnProperty("bspeed")) {
    basMotorSpeed = (int)jsonInput["bspeed"];
  }
  if(jsonInput.hasOwnProperty("corrvalue")) {
    // IF CORRECTION SPEED NEEDS TO BE IMPLEMENTED
  }
  if(jsonInput.hasOwnProperty("ping")){
      confirmMSG(message);
      //sendJsonMSG(currentLineState,dashedLinesCounter);
  }
  if(jsonInput.hasOwnProperty("exit")){
    if(((bool)jsonInput["exit"]) == true){
      resetMbot(); 
    }
  }
  if(jsonInput.hasOwnProperty("setPos")){
    dashedLinesCounter = (int)jsonInput["setPos"];
  }
  if(jsonInput.hasOwnProperty("connected")){
    if((bool)jsonInput["connected"] == true){
      connectedToBT = true;
      setLedColor(10,10,10);
    }
    confirmMSG(message);
  }
  
}
