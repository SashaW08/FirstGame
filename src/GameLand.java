import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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

    /**
     * STEP 0: declare
     **/
    public MainCharacter planeNew;
    public Background sky;
    public Background skyflip;
    public Background sky2;
    public Background skyflip2;
    public OtherCharacters[] BirdArray;
    public OtherCharacters[] Gift1Array;
    public OtherCharacters[] Gift2Array;
    public OtherCharacters[] Gift3Array;
    public Image gift1;
    public Image gift2;
    public Image gift3;
    public Image planeNewPic;
    public Image skyPic;
    public Image skyPicFlip;
    public Image skyPic2;
    public Image skyPicFlip2;
    public Image birdpic;
    public int randomY;
    public int randomX;
    public Image quizBackground; // add this with the other images
    public SoundFile correct;
    public SoundFile incorrect;
    public SoundFile music;
    public int giftDifficulty;


    public boolean planeNewisintersectingbird;
    public boolean planeNewintersectinggift1;
    public boolean planeNewintersectinggift2;
    public boolean planeNewintersectinggift3;

    public boolean startScreen = true;
    public boolean isPlaying = false;
    public boolean gameOver = false;
    public boolean isOnQuiz = false;
    ///ADDED?????
    public boolean quizStarted = false;

    public long startTime;
    public long currentTime;
    public long elapsedTime;

    public String gQuestion;
    public String gAnswer;
    public String choiceA;
    public String choiceB;
    public String choiceC;
    public String choiceD;
    public String userAnswer;
    public int score;


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
        QuizGame();
        if (gQuestion ==null){
            System.out.println("Question is null");
        }else{
            System.out.println("Question set.");
        }
        setUpGraphics(); //this calls the setUpGraphics() method
        mouseControls();
        sky = new Background(0, 0, -4, 0, WIDTH, HEIGHT);
        skyflip = new Background(1000, 0, -4, 0, WIDTH, HEIGHT);
        sky2 = new Background(2000, 0, -4, 0, WIDTH, HEIGHT);
        skyflip2 = new Background(3000, 0, -4, 0, WIDTH, HEIGHT);


        skyPic = Toolkit.getDefaultToolkit().getImage("Sky Background 2.png");
        skyPicFlip = Toolkit.getDefaultToolkit().getImage("Sky Background 2 (flip).png");
        skyPic2 = Toolkit.getDefaultToolkit().getImage("Sky Background 2.png");
        skyPicFlip2 = Toolkit.getDefaultToolkit().getImage("Sky Background 2 (flip).png");
        birdpic = Toolkit.getDefaultToolkit().getImage("pre_flip_bird-removebg-preview.png");
        planeNewPic = Toolkit.getDefaultToolkit().getImage("plane(4).png");
        quizBackground = Toolkit.getDefaultToolkit().getImage("qzs.png");
        Image bgImage = skyPic;
        gift1 = Toolkit.getDefaultToolkit().getImage("gift.png");
        gift2 = Toolkit.getDefaultToolkit().getImage("greengift.png");
        gift3 = Toolkit.getDefaultToolkit().getImage("rainbowgift.png");

        correct = new SoundFile("correct.wav");
        incorrect = new SoundFile("wrong.wav");
        music = new SoundFile("elevatormusic.wav");


        //for each object that has a picture, load in images as well

    }// GameLand()

    public void mouseControls(){canvas.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int clickX = e.getX();
            int clickY = e.getY();
            System.out.println("Mouse clicked at: (" + clickX + ", " + clickY + ")");

            //quiz choice
            if (isOnQuiz && isPlaying) {
                if (clickX >= 35 && clickX <= 470 && clickY >= 420 && clickY <= 510) {
                    userAnswer = choiceA;
                    System.out.println("Clicked choice A: " + choiceA);
                    System.out.println(userAnswer);
                } else if (clickX >= 533 && clickX <= 970 && clickY >= 420 && clickY <= 510) {
                    System.out.println("Clicked choice B: " + choiceB);
                    userAnswer = choiceB;
                    System.out.println(userAnswer);
                } else if (clickX >= 35 && clickX <= 470 && clickY >= 535 && clickY <= 630) {
                    System.out.println("Clicked choice C: " + choiceC);
                    userAnswer = choiceC;
                    System.out.println(userAnswer);
                } else if (clickX >= 533 && clickX <= 970 && clickY >= 535 && clickY <= 630) {
                    System.out.println("Clicked choice D: " + choiceD);
                    userAnswer = choiceD;
                    System.out.println(userAnswer);
                }
            }
            if (userAnswer == gAnswer) {
                score += 10*giftDifficulty;
                System.out.println("correct!");


            } else {
                score -= 5*giftDifficulty;
                System.out.println("wrong...");

            }
            quizStarted = false;
            isOnQuiz = false;
            isPlaying = true;
        }

    });}

//********************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever using a while loop
        while (true) { ///IF STATEMENT?????????
            timer();
            render();  // paint the graphics
            pause(20); // sleep for 20 ms
            if (isOnQuiz == false) {
                moveThings();  //move all the game objects
                collisions();
            } else {
                if (quizStarted == false) {
//                    QuizGame();
                    quizStarted = true;
                }
                ///QUIZGOESHERE??????    background screen and a place to displau qyestions multiple choice
            }


        }
    }

    public void QuizGame() {
        try {
            String apiEasy = "https://opentdb.com/api.php?amount=10&category=27&difficulty=easy&type=multiple";
//            String apiMed = "https://opentdb.com/api.php?amount=15&category=27&difficulty=medium&type=multiple";
//            String apiHard = "https://opentdb.com/api.php?amount=1&category=27&difficulty=hard&type=multiple";
//            String apiUrl = apiEasy;
            URL url = new URL(apiEasy);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseStr = response.toString();

            // Decode basic HTML entities
            responseStr = responseStr.replace("&quot;", "\"").replace("&#039;", "'");

            // Extract question
            String question = responseStr.split("\"question\":\"")[1].split("\",")[0];

            // Extract correct answer
            String correct = responseStr.split("\"correct_answer\":\"")[1].split("\",")[0];

            // Extract incorrect answers
            String incorrectRaw = responseStr.split("\"incorrect_answers\":\\[")[1].split("]")[0];
            incorrectRaw = incorrectRaw.replace("\"", "");
            String[] incorrectArray = incorrectRaw.split(",");

            String incorrect1 = incorrectArray.length > 0 ? incorrectArray[0].trim() : "";
            String incorrect2 = incorrectArray.length > 1 ? incorrectArray[1].trim() : "";
            String incorrect3 = incorrectArray.length > 2 ? incorrectArray[2].trim() : "";

            // Print results
            gQuestion = question;
            System.out.println("Question: test"+question);

            gAnswer = correct;
            List<String> randomanswer = new ArrayList<String>();
            randomanswer.add(incorrect1);
            randomanswer.add(incorrect2);
            randomanswer.add(incorrect3);
            randomanswer.add(correct);

            // Shuffle the list randomly
            Collections.shuffle(randomanswer);

            // Print each one out with a letter label
            choiceD = randomanswer.get(3);
            choiceC = randomanswer.get(2);
            choiceB = randomanswer.get(1);
            choiceA = randomanswer.get(0);

            System.out.println("Question: " + question);
            System.out.println("Correct Answer: " + correct);
            System.out.println("Incorrect Answer 1: " + incorrect1);
            System.out.println("Incorrect Answer 2: " + incorrect2);
            System.out.println("Incorrect Answer 3: " + incorrect3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("Arial", Font.BOLD, 30));


        if (startScreen == true) {
            g.drawImage(skyPic, sky.xpos, sky.ypos, sky.width, sky.height, null);
            g.drawImage(skyPicFlip, skyflip.xpos, skyflip.ypos, skyflip.width, skyflip.height, null);
            g.drawImage(skyPic2, sky2.xpos, sky2.ypos, sky2.width, sky2.height, null);
            g.drawImage(skyPicFlip2, skyflip2.xpos, skyflip2.ypos, skyflip2.width, skyflip2.height, null);
            g.drawString("Press space bar to start!", 300, 300);
        }


        /**STEP 4: draw the images**/
        if (isPlaying == true) {
            if (isOnQuiz == true) {//?????
                g.drawImage(quizBackground, 0, 0, WIDTH, HEIGHT, null);
                if (gQuestion.length() > 58) {
                    g.drawString(gQuestion.substring(0, 58), 50, 240);
                    g.drawString(gQuestion.substring(58, gQuestion.length() - 1), 50, 280);
                } else {
                    g.drawString(gQuestion, 50, 260);
                }
                g.drawString(choiceA, 125, 480);
                g.drawString(choiceB, 600, 480);
                g.drawString(choiceC, 125, 600);
                g.drawString(choiceD, 600, 600);

            } else {//?????
                g.drawImage(skyPic, sky.xpos, sky.ypos, sky.width, sky.height, null);
                g.drawImage(skyPicFlip, skyflip.xpos, skyflip.ypos, skyflip.width, skyflip.height, null);
                g.drawImage(skyPic2, sky2.xpos, sky2.ypos, sky2.width, sky2.height, null);
                g.drawImage(skyPicFlip2, skyflip2.xpos, skyflip2.ypos, skyflip2.width, skyflip2.height, null);
            }


            if (planeNew != null && isOnQuiz == false) {
                g.drawImage(planeNewPic, planeNew.xpos, planeNew.ypos, planeNew.width, planeNew.height, null);
            }

            if (BirdArray != null) {
                for (int i = 0; i < BirdArray.length; i++) {
                    g.drawImage(birdpic, BirdArray[i].xpos, BirdArray[i].ypos, BirdArray[i].width, BirdArray[i].height, null);
                }
            }
            if (Gift1Array != null) {
                for (int i = 0; i < Gift1Array.length; i++) {
                    g.drawImage(gift1, Gift1Array[i].xpos, Gift1Array[i].ypos, Gift1Array[i].width, Gift1Array[i].height, null);
                }
            }
            if (Gift2Array != null) {
                for (int i = 0; i < Gift2Array.length; i++) {
                    g.drawImage(gift2, Gift2Array[i].xpos, Gift2Array[i].ypos, Gift2Array[i].width, Gift2Array[i].height, null);
                }
            }
            if (Gift3Array != null) {
                for (int i = 0; i < Gift3Array.length; i++) {
                    g.drawImage(gift3, Gift3Array[i].xpos, Gift3Array[i].ypos, Gift3Array[i].width, Gift3Array[i].height, null);
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
        g.drawString("Score: "+String.valueOf(score),860,675);

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
        if(planeNewintersectinggift1){
            isOnQuiz=false;
        }

        if(planeNewintersectinggift2){
            isOnQuiz=false;
        }

        if(planeNewintersectinggift3){
            isOnQuiz=false;
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
        if(Gift1Array!=null) {
            for (int i = 0; i < Gift1Array.length; i++) {
                if (elapsedTime == 10) {
                    startTime = System.currentTimeMillis();//this should reset elapsed time to 0
                    Gift1Array[i].dx = Gift1Array[i].dx - 1;
                }
                Gift1Array[i].movebirds();
            }
        }
        if(Gift2Array!=null) {
            for (int i = 0; i < Gift2Array.length; i++) {
                if (elapsedTime == 10) {
                    startTime = System.currentTimeMillis();//this should reset elapsed time to 0
                    Gift2Array[i].dx = Gift2Array[i].dx - 1;
                }
                Gift2Array[i].movebirds();
            }
        }
        if(Gift3Array!=null) {
            for (int i = 0; i < Gift3Array.length; i++) {
                if (elapsedTime == 10) {
                    startTime = System.currentTimeMillis();//this should reset elapsed time to 0
                    Gift3Array[i].dx = Gift3Array[i].dx - 1;
                }
                Gift3Array[i].movebirds();
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

        if(planeNew!=null && Gift1Array!=null) {
            for (int i = 0; i < Gift1Array.length; i++) {
                if (planeNew.rec.intersects(Gift1Array[i].rec) && planeNewintersectinggift1 == false) {
                    planeNewintersectinggift1 = true;
                    giftDifficulty=1;
                    isOnQuiz=true;
                    quizStarted=true;


                }


                if (planeNew.rec.intersects(Gift1Array[i].rec) == false) {
                    planeNewintersectinggift1 = false;
                }
            }
        }
        if(planeNew!=null && Gift2Array!=null) {
            for (int i = 0; i < Gift2Array.length; i++) {
                if (planeNew.rec.intersects(Gift2Array[i].rec) && planeNewintersectinggift2 == false) {
                    planeNewintersectinggift2 = true;
                    giftDifficulty=2;
                    isOnQuiz=true;
                    quizStarted=true;

                }
                if (planeNew.rec.intersects(Gift2Array[i].rec) == false) {
                    planeNewintersectinggift2 = false;
                }
            }
        }
        if(planeNew!=null && Gift3Array!=null) {
            for (int i = 0; i < Gift3Array.length; i++) {
                if (planeNew.rec.intersects(Gift3Array[i].rec) && planeNewintersectinggift3 == false) {
                    planeNewintersectinggift3 = true;
                    isOnQuiz=true;
                    giftDifficulty=3;
                    quizStarted=true;

                }
                if (planeNew.rec.intersects(Gift3Array[i].rec) == false) {
                    planeNewintersectinggift3 = false;
                }
            }
        }
        //???????

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
            Gift1Array= new  OtherCharacters[75];
            Gift2Array = new  OtherCharacters[75];
            Gift3Array = new OtherCharacters[75];

            for(int i=0; i<BirdArray.length; i++){
                randomY = (int)(Math.random()*700);
                randomX=(int)(Math.random()*200000+200);
                BirdArray[i] = new OtherCharacters(randomX+1000,randomY,-7,0,125,125);
            }

            for(int i=0; i<Gift1Array.length; i++){
                randomY = (int)(Math.random()*700);
                randomX=(int)(Math.random()*200000+200);
                Gift1Array[i] = new OtherCharacters(randomX+1000,randomY,-7,0,125,125);
            }
            for(int i=0; i<Gift2Array.length; i++){
                randomY = (int)(Math.random()*700);
                randomX=(int)(Math.random()*200000+200);
                Gift2Array[i] = new OtherCharacters(randomX+1000,randomY,-7,0,125,125);
            }
            for(int i=0; i<Gift3Array.length; i++){
                randomY = (int)(Math.random()*700);
                randomX=(int)(Math.random()*200000+200);
                Gift3Array[i] = new OtherCharacters(randomX+1000,randomY,-7,0,125,125);
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