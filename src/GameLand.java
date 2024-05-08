import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;

//*******************************************************************************
// Class Definition Section

public class GameLand implements Runnable, KeyListener {

    //Variable Declaration Section
    //Declare the variables used in the program
    //You can set their initial values here if you want

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    /**STEP 0: declare**/
    public MainCharacter planeNew;
    public Background sky;
    public Background skyflip;
    public Background sky2;
    public Background skyflip2;
    public OtherCharacters[] BirdArray;
    public Image planeNewPic;
    public Image skyPic;
    public Image skyPicFlip;
    public Image skyPic2;
    public Image skyPicFlip2;
    public Image birdpic;
    public int randomY;
    public int randomX;

    public boolean planeNewisintersectingbird;

    public boolean startScreen=true;
    public boolean isPlaying=false;
    public boolean gameOver=false;

    public long startTime;
    public long currentTime;
    public long elapsedTime;
    public boolean speedUp;


    // Main method definition: PSVM
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        GameLand ex = new GameLand();   //creates a new instance of the game and tells GameLand() method to run
        new Thread(ex).start();       //creates a thread & starts up the code in the run( ) method
    }

    // Constructor Method
    // This has no return type and has the same name as the class
    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public GameLand() {
        setUpGraphics(); //this calls the setUpGraphics() method

        sky = new Background(0,0,-4,0,WIDTH,HEIGHT);
        skyflip = new Background(1000,0,-4,0,WIDTH,HEIGHT);
        sky2 = new Background(2000,0,-4,0,WIDTH,HEIGHT);
        skyflip2 = new Background(3000,0,-4,0,WIDTH,HEIGHT);


        skyPic=Toolkit.getDefaultToolkit().getImage("Sky Background 2.png");
        skyPicFlip=Toolkit.getDefaultToolkit().getImage("Sky Background 2 (flip).png");
        skyPic2=Toolkit.getDefaultToolkit().getImage("Sky Background 2.png");
        skyPicFlip2=Toolkit.getDefaultToolkit().getImage("Sky Background 2 (flip).png");
        birdpic=Toolkit.getDefaultToolkit().getImage("pre_flip_bird-removebg-preview.png");
        planeNewPic=Toolkit.getDefaultToolkit().getImage("plane(4).png");

        //for each object that has a picture, load in images as well

    }// GameLand()

//*******************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever using a while loop
        while (true) {
            moveThings();  //move all the game objects
            collisions();
            timer();
            render();  // paint the graphics
            pause(20); // sleep for 20 ms
        }
    }

    //paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        if(startScreen==true){
            g.drawImage(skyPic,sky.xpos, sky.ypos, sky.width, sky.height, null);
            g.drawImage(skyPicFlip, skyflip.xpos, skyflip.ypos, skyflip.width, skyflip.height, null);
            g.drawImage(skyPic2, sky2.xpos, sky2.ypos, sky2.width, sky2.height, null);
            g.drawImage(skyPicFlip2, skyflip2.xpos, skyflip2.ypos, skyflip2.width, skyflip2.height, null);
            g.drawString("Press space bar to start!",300,300);
        }

        /**STEP 4: draw the images**/

        if(isPlaying==true) {

            g.drawImage(skyPic, sky.xpos, sky.ypos, sky.width, sky.height, null);
            g.drawImage(skyPicFlip, skyflip.xpos, skyflip.ypos, skyflip.width, skyflip.height, null);
            g.drawImage(skyPic2, sky2.xpos, sky2.ypos, sky2.width, sky2.height, null);
            g.drawImage(skyPicFlip2, skyflip2.xpos, skyflip2.ypos, skyflip2.width, skyflip2.height, null);

            if(planeNew!=null) {
                g.drawImage(planeNewPic, planeNew.xpos, planeNew.ypos, planeNew.width, planeNew.height, null);
            }

            if(BirdArray!=null) {

                for (int i = 0; i < BirdArray.length; i++) {
                    g.drawImage(birdpic, BirdArray[i].xpos, BirdArray[i].ypos, BirdArray[i].width, BirdArray[i].height, null);
                }

            }
        }

        if(gameOver==true){
            g.drawImage(skyPic,sky.xpos, sky.ypos, sky.width, sky.height, null);
            g.drawImage(skyPicFlip, skyflip.xpos, skyflip.ypos, skyflip.width, skyflip.height, null);
            g.drawImage(skyPic2, sky2.xpos, sky2.ypos, sky2.width, sky2.height, null);
            g.drawImage(skyPicFlip2, skyflip2.xpos, skyflip2.ypos, skyflip2.width, skyflip2.height, null);
            g.drawString("Game Over",400,300);
        }

        g.dispose();
        bufferStrategy.show();
    }

    public void moveThings() {
        //call the move() method code from your object class

        sky.wrappingMove();
        skyflip.wrappingMove();
        sky2.wrappingMove();
        skyflip2.wrappingMove();
        if(planeNew!=null) {
            planeNew.move();
        }
        if(BirdArray!=null) {
            for (int i = 0; i < BirdArray.length; i++) {
                if (elapsedTime == 10) {
                    startTime = System.currentTimeMillis();//this should reset elapsed time to 0
                    BirdArray[i].dx = BirdArray[i].dx - 1;
                }
                BirdArray[i].movebirds();
            }
        }

    }

    public void timer(){
        //get the current time
        currentTime = System.currentTimeMillis();
        //calculate the elapsed time, convert it to seconds and cast as an int
        elapsedTime=(int)((currentTime-startTime)*.001); // *.001 to convert to seconds
       // System.out.println(elapsedTime);
    }

    public void collisions(){

        if(planeNew!=null && BirdArray!=null) {

            for (int i = 0; i < BirdArray.length; i++) {
                if (planeNew.rec.intersects(BirdArray[i].rec) && planeNewisintersectingbird == false) {
                    planeNewisintersectingbird = true;
                    isPlaying = false;
                    gameOver = true;
                }

                if (planeNew.rec.intersects(BirdArray[i].rec) == false) {
                    planeNewisintersectingbird = false;
                }
            }
        }

    }


    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Game Land");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        canvas.addKeyListener(this);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        int keyCode = e.getKeyCode();

        if(keyCode==40) //down
            {
                planeNew.downPressed=true;
            }

        if(keyCode==38) //up
        {
            planeNew.upPressed=true;
        }

        if(keyCode==32){
            startScreen=false;
            isPlaying=true;
            startTime=System.currentTimeMillis();

            planeNew = new MainCharacter(5,5,0,0,200,150);

            BirdArray = new OtherCharacters[500];

            for(int i=0; i<BirdArray.length; i++){
                randomY = (int)(Math.random()*700);
                randomX=(int)(Math.random()*200000+200);
                BirdArray[i] = new OtherCharacters(randomX+1000,randomY,-7,0,125,125);
            }


        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        char key=e.getKeyChar();
        int keyCode=e.getKeyCode();
        if(keyCode==40){
            planeNew.downPressed=false;
        }

        if(keyCode==38){
            planeNew.upPressed=false;
        }

    }
}