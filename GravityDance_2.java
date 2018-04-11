import processing.core.*; 
/*import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; */

public class GravityDance_2 extends PApplet {

private static final long serialVersionUID = 1L;
	
float gravity = 0.3f;
float damping = 0.99f;
float repulsion = 0.3f;
particle[] Z = new particle[32];


public void setup() {
  smooth();
  size(500, 500, P2D); 
  background(0);
  //frameRate(60);

  // initialize particles randomly   
  for (int i = 0; i < Z.length; i++) {
    Z[i] = new particle(random(width), random(height), random(1) - 0.5f, random(1) - 0.5f, 16 + random(32));
  }
}

public void draw() {
  // clear  
  background(0);

  // display particles
  for (int i = 0; i < Z.length; i++) {
    for (int j = i + 1; j < Z.length; j++) {
      Z[i].connect(Z[j]);
    }
    Z[i].display();  
  }
  
  // update particle position
  for (int i = 0; i < Z.length; i++) {
    for (int j = 0; j < Z.length; j++) {
      if (j != i)
        Z[i].gravitate(Z[j]);
    }
    if (mousePressed == true) {
       if (mouseButton == LEFT) {
         Z[i].gravitate(new particle(mouseX, mouseY, 0, 0, 10000));
       }
    }
    Z[i].update();
    Z[i].dampen();
  }
}

class particle {

  float x;
  float y;
  float px;
  float py;
  float vx;
  float vy;
  float mass;
  float forcex;
  float forcey;

  particle(float dx, float dy, float Vx, float Vy, float M) {
    x = dx;
    y = dy;
    px = dx;
    py = dy;
    vx = Vx;
    vy = Vy;
    mass = M;
    forcex = 0;
    forcey = 0;
  }
   
  // calculate the gravitational force contribution by particle Z
  public void gravitate(particle Z) {
    float F, r2;
    r2 = sq(x - Z.x) + sq(y - Z.y);
    if (r2 != 0) {
      F = mass*Z.mass*gravity;
      if (r2 < 100)
        F = 10 * (1 - r2/100); //  - mass*Z.mass*repulsion*100/r2;
      F /= r2 * sqrt(r2);
      forcex += F * (Z.x - x);
      forcey += F * (Z.y - y);
    }
  }

  public void update() {
    // update particle speed
    vx += forcex / mass;
    vy += forcey / mass;
    
    if (vx > 32) {
      vx = 32;
    }
    else if (vx < -32) {
      vx = -32;
    }
    if (vy > 32) {
      vy = 32;
    }
    else if (vy < -32) {
      vy = -32;
    }
    
    // update particle position
    x += vx;
    y += vy;
    
    // check for boundaries
    if (x < 0) {
      x = 0;
      vx = -vx;
    } else if (x > width) {
      x = width;
      vx = - vx;
    } 
    if (y < 0) {
      y = 0;
      vy = -vy;
    } else if (y > height) {
      y = height;
      vy = -vy;
    }
    
    // reset forces   
    forcex = 0;
    forcey = 0;
    px = x;
    py = y;
  }
  
  public void dampen() {
    vx *= damping;
    vy *= damping;
  }
   
  public void display() {
    float trans = 16+144*atan(sqrt(sq(vx)+sq(vy)))/HALF_PI;
    stroke(255, 255, 255, 92);
    fill(125, 175, 255, trans);
    ellipse(x, y, mass*1.5f, mass*1.5f);
    fill(255, 255, 0);
    noStroke();
    ellipse(x, y, mass / 4, mass / 4);
  }

  public void connect(particle Z) {
    stroke(200, 200, 200, 64);
    line(x, y, Z.x, Z.y);
  }

}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "GravityDance_2" });
  }
}
