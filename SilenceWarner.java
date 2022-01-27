/* autogenerated by Processing revision 1277 on 2022-01-27 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import processing.sound.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class SilenceWarner extends PApplet {

//Silence Warner made By Neige


Table table;

final int marge = 6;
final int fontSize = 20;
float threshold = 0.1f;
Amplitude amp1;
Amplitude amp2;
AudioIn in1;
AudioIn in2;
boolean silence;
boolean gateLocked = true;
float gatedAmp;
int silenceTime = 0;
int silenceStart;
boolean stress = false;

 public void setup() {
  
  table = new Table();
  table.addColumn("date");
  table.addColumn("hour");
  
  rectMode(CORNERS);
  surface.setResizable(true);
  /* size commented out by preprocessor */;
  textSize(fontSize);
  
  // Create an Input stream which is routed into the Amplitude analyzer
  amp1 = new Amplitude(this);
  amp2 = new Amplitude(this);
  
  in1 = new AudioIn(this, 0);
  in2 = new AudioIn(this, 1);
  
  in1.start();
  in2.start();
  
  amp1.input(in1);
  amp2.input(in2);
}      
      

 public void draw() {
  
  background(40);
  
  gate();
  silenceCount();
  indicateurs();
  vuMetre();
  silenceList();
  
  
  //println(amp1.analyze());
  //println(amp2.analyze());
  //background(amp1.analyze()*255);
  
}
 public void silenceList() {

  if (silenceTime >= 8000) {
    
    if (stress == true) {
      stress = true;
    }else{
      stress = true;
      
      TableRow newRow = table.addRow();
      newRow.setString("date",year() + "-" + month() + "-" + day());
      newRow.setString("hour",hour() + ":" + minute() + ":" + second());
      saveTable(table, "Silence Logs copier et ne pas ouvrir.csv");
    }
  }else{
    stress = false;
  }
}
 public boolean detectSilence() {

  if (gate() == 0) {
    
    if (silence == true) {
    silence = true;
    }else{
      silence = true;
      silenceStart = millis();
    }
  } else {
    silence = false;
    stress = false;
    silenceStart = millis();
  }
      
  return silence;

}
 public void mouseWheel(MouseEvent event) {
  
  float wheel = event.getCount();
  
  if (gateLocked == false) {
    
      if   (wheel >=0) {
      threshold = constrain(threshold-(0.01f),0,1);
    } else {
      threshold = constrain(threshold+(0.01f),0,1);
    }
  }else{
    threshold = threshold+0;
  }
}

 public float gate() {

  float gatedAmp = constrain(((amp1.analyze() + amp2.analyze()))*2-threshold,0,1);
  
  return gatedAmp;

}
 public void indicateurs() {
  
  PImage lockedIcon = loadImage("lockedIcon.png");
  PImage unlockedIcon = loadImage("unlockedIcon.png");
 
  noStroke();
  fill(60);
  
  //rectangle blanc antenne
  rect(marge,marge,marge+134,marge*3+fontSize,marge);
  //rectangle gate
  rect(width-marge*3-100,marge*3+fontSize,width-marge, marge,marge);
  //rectangle VU metre
  rect(marge,marge*4+fontSize,marge*3+20,height-marge,marge);
  //rectangle décompte
  if (silenceTime<4000){    
    fill(0xFF7AC676);
  }else if (4000<= silenceTime && silenceTime<6000){
    fill(0xFFFAF558);
  }else if (6000 <= silenceTime && silenceTime <8000){
    fill(0xFFFFB758);
  }else if (8000 <= silenceTime && silenceTime <10000){
    fill(0xFFFF4D4D);  
  }else if (10000 < silenceTime && (silenceTime/100)%2 == 0){
    fill(0xFFFF4D4D);
  }else if (10000 < silenceTime && (silenceTime/100)%2 == 1){
    fill(60);
  }
  
  rect(marge*4+20,marge*4+fontSize,width-marge,height-marge,marge);
  
  //textes
  fill(255);
  textSize(fontSize);
  textAlign(LEFT);
  //texte threshold
  text("Threshold", width-marge-100,marge*2+fontSize );
  //texte blanc antenne
  if (detectSilence() == true){
    text("blanc antenne",marge*2,marge*2+fontSize);
  }else{
    text("son",marge*2,marge*2+fontSize);
  }
  //texte décompte
  textAlign(CENTER);
  textSize(256);
  fill(0);
  text(silenceTime/1000,((marge*4+20)+(width-marge))/2,((marge*4+fontSize)+(height-marge))/2+96);
  if (4000<= silenceTime){
    textSize(40);
    text("BLANC ANTENNE", ((marge*4+20)+(width-marge))/2,((marge*4+fontSize)+(height-marge))/4);
  }
  
  //icône GateLock
  if (gateLocked){
    image(lockedIcon,width-marge*2-132,marge);
  }else{
    image(unlockedIcon,width-marge*2-132,marge);
  }
  stroke(255);
  strokeWeight(2);
  line(marge,(height-marge*2) - (threshold)*(height-(marge*7+fontSize)), marge*3+20,(height-marge*2) - (threshold)*(height-(marge*7+fontSize)));
}
 public void mouseClicked() {
    if ((mouseX >= width-(marge*3+182)) && (mouseX <=width-(marge*2+150)) && (mouseY >= marge) && (mouseY <= marge+32)) {
    
    gateLocked = !gateLocked;
  }
}
 public void silenceCount(){
 if (detectSilence() == false) {
   silenceTime = 0;
 }else{
   silenceTime = millis()-silenceStart;
 }
}
 public void vuMetre() {
 
 
 fill(0xFF36B782);
 noStroke();
 rect(marge*2,height-marge*2,marge*2+9,(height-marge*2) - (2*amp1.analyze())*(height-(marge*7+fontSize)));
 rect(marge*2+11,height-marge*2,marge*2+20,(height-marge*2) - (2*amp2.analyze())*(height-(marge*7+fontSize)));

}


  public void settings() { size(364, 364); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SilenceWarner" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
