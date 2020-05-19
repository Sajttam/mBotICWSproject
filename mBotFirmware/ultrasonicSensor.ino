/*
 * ultraSonic:
<<<<<<< Updated upstream
 * reads 
 * from sensor, checks if there is obstacles within 15 cm
 * if obstacle send BT MSG
 */
void ultraSonic(){
  dist = ultrasensor.distanceCm();
  if(dist >= 0 && dist < 15 && dist != olddist){
    olddist = dist;
    //sendJsonMSG();
  }
=======
 * reads from sensor,
 * save value, to be sent with next update msg
 */
void ultraSonic(){
  dist = ultrasensor.distanceCm();
>>>>>>> Stashed changes
}
