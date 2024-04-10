import java.awt.*;

/**For the character that the player controls**/

public class MainCharacter {
    public int xpos;        //the x position
    public int ypos;       //the y position
    public int dx;               //the speed of the hero in the x direction
    public int dy;               //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;      //a boolean to determine is the hero is alive or not
    public Rectangle rec;

    public boolean upPressed;
    public boolean downPressed;
    public Rectangle mrec;


    public MainCharacter(int pxpos, int pypos, int pdx, int pdy, int pwidth, int pheight){
        xpos=pxpos;
        ypos=pypos;
        dx=pdx;
        dy=pdy;
        width=pwidth;
        height=pheight;
        isAlive=true;
        mrec=new Rectangle(xpos,ypos,width,height);
    }

    public void move(){

        if(upPressed==true){
            dy=-4;
        }else if(downPressed==true){
            dy=4;
        }else{
            dy=0;
        }

        xpos=xpos+dx;
        ypos=ypos+dy;
        mrec=new Rectangle(xpos,ypos,width,height);
    }


}
