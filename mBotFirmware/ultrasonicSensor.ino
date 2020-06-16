/*
 * ultraSonic:
 * reads from sensor,
 * save value, to be sent with next update msg
 * 
 */
void ultraSonic(){
  dist = ultrasensor.distanceCm();
}
