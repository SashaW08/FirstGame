import java.awt.*;

/**For the characters that move on their own**/

public class OtherCharacters {

    public int xpos;        //the x position
    public int ypos;       //the y position
    public int dx;               //the speed of the hero in the x direction
    public int dy;               //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;      //a boolean to determine is the hero is alive or not
    public Rectangle rec;


    public OtherCharacters(int pxpos, int pypos, int pdx, int pdy, int pwidth, int pheight){
        xpos=pxpos;
        ypos=pypos;
        dx=pdx;
        dy=pdy;
        width=pwidth;
        height=pheight;
        isAlive=true;
        rec=new Rectangle(xpos+20,ypos+20,width-40,height-40);
    }

    public void movebirds(){

        xpos=xpos+dx;
        ypos=ypos+dy;
        rec=new Rectangle(xpos+20,ypos+20,width-40,height-40);
    }


}
