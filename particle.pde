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
  void gravitate(particle Z) {
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

  void update() {
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
  
  void dampen() {
    vx *= damping;
    vy *= damping;
  }
   
  void display() {
    float trans = 16+144*atan(sqrt(sq(vx)+sq(vy)))/HALF_PI;
    stroke(255, 255, 255, 92);
    fill(125, 175, 255, trans);
    ellipse(x, y, mass*1.5, mass*1.5);
    fill(255, 255, 0);
    noStroke();
    ellipse(x, y, mass / 4, mass / 4);
  }

  void connect(particle Z) {
    stroke(200, 200, 200, 64);
    line(x, y, Z.x, Z.y);
  }

}
