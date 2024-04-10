import java.awt.*;

public class Background {
    public int xpos;        //the x position
    public int ypos;       //the y position
    public int dx;               //the speed of the hero in the x direction
    public int dy;               //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;      //a boolean to determine is the hero is alive or not


    public Background(int pxpos, int pypos, int pdx, int pdy, int pwidth, int pheight){
        xpos=pxpos;
        ypos=pypos;
        dx=pdx;
        dy=pdy;
        width=pwidth;
        height=pheight;
        isAlive=true;
    }

    public void wrappingMove(){


        if(xpos<-1000){
            xpos=3000;
        }
        xpos = xpos + dx;
        ypos = ypos + dy;

    }



}
