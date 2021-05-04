// The model represents all the actual content and functionality of the game
// For Breakout, it manages all the game objects that the View needs
// (the bat, ball, bricks, and the score), provides methods to allow the Controller
// to move the bat (and a couple of other functions - change the speed or stop 
// the game), and runs a background process (a 'thread') that moves the ball 
// every 20 milliseconds and checks for collisions 

import javafx.scene.paint.*;
import javafx.application.Platform;
import java.util.ArrayList;
/**
 * <h1>Model class have all the values of bricks, brick width and height, ball movement and bat
 * position. </h1>
 * <p>private final int B and M is the border around the edge of breakout game and height
 * of menu bar. Inside the model class there are different variables which helps to position 
 * brick and bat at correct position and when ball hit the wall it detect it hit a wall and need 
 * to change the position, if the ball hit bottom then user get -200 point as penalty and if ball 
 * hit the bat then it will change the position to topX or topY. </p>
 * @param B                  Border around the edge of the panel.
 * @param M                  Height of menu at the top.
 * @param BALL_SIZE          Ball size
 * @param BRICK_WIDTH        Brick width 
 * @param BRICK_HEIGHT       Brick height 
 * @param BRICK_SEPARATION   Separate each bricks
 * @param BRICK_X_OFFSET     To get X axis for bricks
 * @param BAT_MOVE           Whenever user keypress bat move a fixed distance
 * @param BALL_MOVE          Units to move the ball
 * @param HIT_BRICK          Score when the ball hit brick
 * @param HIT_BOTTOM         Penalty score when the ball hit bottom
 */
public class Model 
{
    
    // First,a collection of useful values for calculating sizes and layouts etc.
    private final int B                 = 6;      // Border round the edge of the panel
    private final int M                 = 40;     // Height of menu bar space at the top

    private int BALL_SIZE         = 30;     // Ball size
    private int BRICK_WIDTH       = 50;     // Brick width
    private int BRICK_HEIGHT      = 30;      // Brick height
    private int BRICK_SEPARATION  = 2;       // Gap between each bricks 
    private int BRICK_X_OFFSET    = 10;      // Brick x axis
    

    public int BAT_MOVE       = 5;      // Distance to move bat on each keypress
    public int BALL_MOVE      = 4;      // Units to move the ball on each step

    private final int HIT_BRICK      = 50;      // Score for hitting a brick
    private final int HIT_BOTTOM     = -200;    // Score (penalty) for hitting the bottom of 
                                                // the screen

    // The other parts of the model-view-controller setup
    View view;
    Controller controller;

    // The game 'model' - these represent the state of the game
    // and are used by the View to display it
    public GameObj ball;                         // The ball
    public ArrayList<GameObj> bricks;            // The bricks
    public GameObj bat;                          // The bat
    public int score = 0;                        // The score

    // variables that control the game 
    public String gameState = "running";// Set to "finished" to end the game
    public boolean fast = false;        // Set true to make the ball go faster

    // initialisation parameters for the model
    private final int width;                   // Width of game
    private final int height;                  // Height of game

    /**
     * This method help how big the game window is.
     */
    // CONSTRUCTOR - needs to know how big the window will be
    public Model( int w, int h )
    {
        Debug.trace("Model::<constructor>");  
        width = w; 
        height = h;


    }

    
    // Animating the game
    // The game is animated by using a 'thread'. Threads allow the program to do 
    // two (or more) things at the same time. In this case the main program is
    // doing the usual thing (View waits for input, sends it to Controller,
    // Controller sends to Model, Model updates), but a second thread runs in 
    // a loop, updating the position of the ball, checking if it hits anything
    // (and changing direction if it does) and then telling the View the Model 
    // changed.
    
    // When we use more than one thread, we have to take care that they don't
    // interfere with each other (for example, one thread changing the value of 
    // a variable at the same time the other is reading it). We do this by 
    // SYNCHRONIZING methods. For any object, only one synchronized method can
    // be running at a time - if another thread tries to run the same or another
    // synchronized method on the same object, it will stop and wait for the
    // first one to finish.

    /**
     * This method start the game, initialiseGame() which contain all the object we needed to 
     * run the game and it creates a thread to run runGame.
     */
    // Start the animation thread
    public void startGame()
    {
        initialiseGame();                           // set the initial game state
        Thread t = new Thread( this::runGame );     // create a thread running the runGame method
        t.setDaemon(true);                          // Tell system this thread can die when it finishes
        t.start();                                  // Start the thread running
    }   

    /**
     * This method reset everything inside the game and start from the begining if we restart
     * the game. Ball and bat size and width should initialise in this method also the colour of the object. In order
     * get different layer of bricks we have to use for loop inside a for loop. First for loop 
     * will cover how many columns we need, inside that for loop we create another for loop 
     * which initialise how many rows needed. 
     * @param ball            create object size, X and Y axis, height and width, and colour
     * @param bat             create object size, X and Y axis, height and width, and colour
     * @param bricks          create ArrayList
     * @param WALL_TOP        how far down the screen from the top
     * @param BRICK_COLUMN    how many columns needed
     * @param NUM_BRICK       number of bricks in a row
     */
    // Initialise the game - reset the score and create the game objects 
    public void initialiseGame()
    {       
        score = 0;
        ball   = new GameObj(width/2, height/2, BALL_SIZE, BALL_SIZE, Color.RED );
        bat    = new GameObj(width/2, height - BRICK_HEIGHT*3/2, BRICK_WIDTH*3, 
            BRICK_HEIGHT/4, Color.BLACK);
        bricks = new ArrayList<>();
       
        
          
        int WALL_TOP = 100;                     // how far down the screen the wall starts
        int BRICK_COLUMN = 4;                   // how many layer of bricks 
        
        int NUM_BRICKS = 11;                    // how many bricks fit on screen
        
    
        for (int i=0; i < BRICK_COLUMN; i++) {
            int y = WALL_TOP + (i * (BRICK_HEIGHT + BRICK_SEPARATION));
            
            for (int j = 0; j < NUM_BRICKS; j++) {
                int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEPARATION));
            bricks.add(new GameObj(x, y, BRICK_WIDTH, BRICK_HEIGHT, Color.YELLOW));
            
         
        }
        
    }

      
    }


    /**
     * runGame method is the main animation loop for the breakout game. It use updateGame() 
     * which update the game state, modelChanged() will refresh the screen to update the 
     * movement of the object and Thread.sleep(getFast() ? 10: 20) is used to wait for
     * milliseconds.
     */
    // The main animation loop
    public void runGame()
    {
        try
        {
            Debug.trace("Model::runGame: Game starting"); 
            // set game true - game will stop if it is set to "finished"
            setGameState("running");
            while (!getGameState().equals("finished"))
            {
                updateGame();                        // update the game state
                modelChanged();                      // Model changed - refresh screen
                Thread.sleep( getFast() ? 10 : 20 ); // wait a few milliseconds
            }
            Debug.trace("Model::runGame: Game finished"); 
        } catch (Exception e) 
        { 
            Debug.error("Model::runAsSeparateThread error: " + e.getMessage() );
        }
    }

    /**
     * This method is used to update the game then only the object in the game feels like moving. In order
     * to move the ball and it need to detect whether it is hit a brick, bottom, bat or sidewall. When the
     * ball hit the brick it needs to be invisible so for loop is used to get the result.
     * @param x   current position of ball
     * @param y   current position of ball
     */
    // updating the game - this happens about 50 times a second to give the impression of movement
    public synchronized void updateGame()
    {
        // move the ball one step (the ball knows which direction it is moving in)
        ball.moveX(BALL_MOVE);                      
        ball.moveY(BALL_MOVE);
        // get the current ball possition (top left corner)
        int x = ball.topX;  
        int y = ball.topY;
        // Deal with possible edge of board hit
        if (x >= width - B - BALL_SIZE)  ball.changeDirectionX();
        if (x <= 0 + B)  ball.changeDirectionX();
        if (y >= height - B - BALL_SIZE)  // Bottom
        { 
            ball.changeDirectionY(); 
            addToScore( HIT_BOTTOM );     // score penalty for hitting the bottom of the screen
        }
        if (y <= 0 + M)  ball.changeDirectionY();

       // check whether ball has hit a (visible) brick
        boolean hit = false;
        
        
        
        
        // *[3]******************************************************[3]*
        // * Fill in code to check if a visible brick has been hit      *
        // * The ball has no effect on an invisible brick               *
        // * If a brick has been hit, change its 'visible' setting to   *
        // * false so that it will 'disappear'                          * 
      // **************************************************************
          
         for (GameObj brick: bricks) {
            if (brick.visible && brick.hitBy(ball)) {
                hit = true;
                brick.visible = false;      // set the brick invisible
                addToScore( HIT_BRICK );    // add to score for hitting a brick 
            }
        }
        

        if (hit) {
            ball.changeDirectionY();
        }
        
        // check whether ball has hit the bat
        if ( ball.hitBy(bat) ) {
            ball.changeDirectionY();
        }
    }

    /**
     * This method is used to call update in View whenever the Model get updated then it will update ball, 
     * bricks, bat and score in the update method. 
     */
    // This is how the Model talks to the View
    // Whenever the Model changes, this method calls the update method in
    // the View. It needs to run in the JavaFX event thread, and Platform.runLater 
    // is a utility that makes sure this happens even if called from the
    // runGame thread
    public synchronized void modelChanged()
    {
        Platform.runLater(view::update);
    }
    
    
    // Methods for accessing and updating values
    // these are all synchronized so that the can be called by the main thread 
    // or the animation thread safely

    /**
     * This method change game state to running or finished. setGameState is also called from controller to
     * get the game state.
     * @param value       to change game state
     */
    // Change game state - set to "running" or "finished"
    public synchronized void setGameState(String value)
    {  
        gameState = value;
    }

    /**
     * This method return game running state.
     */
    // Return game running state
    public synchronized String getGameState()
    {  
        return gameState;
    }

    /**
     * This method is used to change speed of the game. It use boolean value, false is for normal speed and
     * true is for fast. This method is called on controller class to use different speed for ball.
     * @param value       to change ball movement from normal to fast
     */
    // Change game speed - false is normal speed, true is fast
    public synchronized void setFast(Boolean value)
    {  
        fast = value;
    }

    /**
     * This is a boolean value which is called on runGame method in Model to set the speed of the thread.
     */
    // Return game speed - false is normal speed, true is fast
    public synchronized Boolean getFast()
    {  
        return(fast);
    }

    /**
     * This method return the bat object 
     */
    // Return bat object
    public synchronized GameObj getBat()
    {
        return(bat);
    }

    /**
     * This method return ball object using getBall()
     */
    // return ball object
    public synchronized GameObj getBall()
    {
        return(ball);
    }

    /**
     * This method return brick object using ArrayList because we used ArrayList to create
     * different layer of bricks in intitialiseGame() method.
     */
    // return bricks
    public synchronized ArrayList<GameObj> getBricks()
    {
        return(bricks);
    }

    /**
     * This method return the current score when the ball hits a brick. 
     */
    // return score
    public synchronized int getScore()
    {
        return(score);
    }

    /**
     * This method update the score when the ball hit another brick. It keeps updating whenever
     * the ball hits the bricks. 
     * @param n       update the score value 
     */
     // update the score
    public synchronized void addToScore(int n)    
    {
        score += n;        
    }

    /**
     * This method helps the user to move the bat to the left and right. The bat will go off the
     * screen if the user move towards the side of the window. In order to prevent it, we use if
     * and else if statement by creating two more int variables. 
     * @param direction   to get the direction of the bat
     * @param MIN_X       to prevent bat going off the screen to the left side
     * @param MAX_X       to prevent bat going off the screen to the right side
     */
    // move the bat one step - -1 is left, +1 is right
    public synchronized void moveBat( int direction )
    {        
        int dist = direction * BAT_MOVE;
        int MIN_X = 10;
        int MAX_X = 440;

        if (dist > 0 && bat.getDirX() < MAX_X) {
            bat.moveX(dist);
        }
        else if (dist < 0 && bat.getDirX() > MIN_X) {
            bat.moveX(dist);
        }

      Debug.trace( "Model::moveBat: Move bat = " + dist );
    }
}   
    