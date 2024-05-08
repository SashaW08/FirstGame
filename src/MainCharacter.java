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

    public boolean upPressed;
    public boolean downPressed;
    public Rectangle rec;


    public MainCharacter(int pxpos, int pypos, int pdx, int pdy, int pwidth, int pheight){
        xpos=pxpos;
        ypos=pypos;
        dx=pdx;
        dy=pdy;
        width=pwidth;
        height=pheight;
        isAlive=true;
        rec=new Rectangle(xpos+45,ypos+25,width-90,height-80);
    }

    public void move(){

    if (upPressed == true && ypos>-15) {
        dy = -7;
    } else if (downPressed == true && ypos<600) {
        dy = 7;
    } else {
        dy = 0;
    }

        if(ypos<0){
            //make it so the up button doesn't work until within range again
        }
        if(ypos>550){
            //make it so the down button doesn't work until within range again
        }


        xpos=xpos+dx;
        ypos=ypos+dy;
        rec=new Rectangle(xpos+45,ypos+25,width-90,height-80);

    }


}
