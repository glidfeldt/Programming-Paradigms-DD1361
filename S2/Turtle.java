import java.lang.*;
import java.math.*;
import java.text.*;

public class Turtle {
    private double x,y,angle;
    private String color;
    private boolean activated;


    public Turtle(){
        this.x=0;
        this.y=0;
        this.angle=0;
        this.color="#0000FF";
        this.activated=false;
    }

    public void activate(boolean type){
        this.activated=type;
    }

    public void setColor(String color){
        this.color=color;
    }

    public void turnLeft(double angle){
        this.angle+=angle;
    }

    public void turnRight(double angle){
        this.angle-=angle;
    }

    public void moveForw(int units){
        double prevX = this.x;
        double prevY = this.y;
        setNewPos(units);
        if(activated){
            print(prevX, prevY);
        }
    }

    public void moveBack(int units){
        double prevX = this.x;
        double prevY = this.y;
        setNewPos(units*-1);
        if(activated){
            print(prevX, prevY);
        }
    }

    public void setNewPos(int units){
        this.x=x+units*Math.cos(Math.PI*Math.toRadians(angle)/Math.toRadians(180));
        this.y=y+units*Math.sin(Math.PI*Math.toRadians(angle)/Math.toRadians(180));
    }

    public void print(double prevX, double prevY){
        //DecimalFormat df = new DecimalFormat("0.0000");
        //System.out.println(this.color+" "+df.format(prevX)+" "+df.format(prevY)
               // +" "+df.format(this.x)+" "+df.format(this.y));
        System.out.println(this.color+" "+prevX+" "+prevY +" "+this.x+" "+this.y);
    }


}
