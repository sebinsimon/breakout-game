// The View class creates and manages the GUI for the application.
// It doesn't know anything about the game itself, it just displays
// the current state of the Model, and handles user input

// We import lots of JavaFX libraries (we may not use them all, but it
// saves us having to thinkabout them if we add new code)
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * <h2>View handles the interface of the breakout game.</h2>
 * <p>View update the GUI when it talks with Model class. width and height is our breakout game's
 * window size. GameObj variables such as bat, ball, bricks and score is used to create its 
 * object and colour it. 
 * @param width        fix the width of game window.
 * @param height       fix the height of game window.
 * @param Pane         create layout for the game.
 * @param Canvas       it helps to draw objects inside the canvas.
 * @param Controller   it link to the class Controller to communicate each other.
 * @param Model        View class is communicating with Model class.
 * @param bat          create bat object.
 * @param ball         create ball object.
 * @param bricks       create brick object.
 * @param score        create score for the game. 
 */
public class View implements EventHandler<KeyEvent>
{ 
    // variables for components of the user interface
    protected int width;       // width of window
    protected int height;      // height of window

    // user interface objects
    public Pane pane;       // basic layout pane
    private Canvas canvas;   // canvas to draw game on
    private Label infoText;  // info at top of screen

    // The other parts of the model-view-controller setup
    public Controller controller;
    public Model model;

    private GameObj   bat;            // The bat
    private GameObj   ball;           // The ball
    private GameObj[] bricks;         // The bricks
    private int       score =  0;     // The score

    /**
     * View method control the width and height of the game window. <b>w</b> is initialised with
     * the width value and <b>h</b> is initialised with height value
     */
    // constructor method - we get told the width and height of the window
    public View(int w, int h)
    {
        Debug.trace("View::<constructor>");
        width = w;
        height = h;
    }

    /**
     * Start method uses javafx.stage which help to create canvas, inside that canvas we draw 
     * our object. We use css to style the label which shows the score when the ball hit the 
     * brick. inforText.setTranslateX and Y used to set the label on a side of the window.
     */
    // start is called from the Main class, to start the GUI up
    public void start(Stage window) 
    {
        // breakout is basically one big drawing canvas, and all the objects are
        // drawn on it as rectangles, except for the ball and text at the 
        //top - this is a label which sits 'in front of' the canvas.
        
        // Note that it is important to create control objects (Pane, Label,Canvas etc) 
        // here not in the constructor (or as initialisations to instance variables),
        // to make sure everything is initialised in the right order
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane
        
        // infoText box for the score - a label which we position in front of
        // the canvas (by adding it to the pane after the canvas)
        infoText = new Label("BreakOut: Score = " + score);
        infoText.setTranslateX(50);  // these commands setthe position of the text box
        infoText.setTranslateY(10);  // (measuring from the top left corner)
        pane.getChildren().add(infoText);  // add label to the pane

        // Make a new JavaFX Scene, containing the complete GUI
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file

        // Add an event handler for key presses. By using 'this' (which means 'this 
        // view object itself') we tell JavaFX to call the 'handle' method (below)
        // whenever a key is pressed
        scene.setOnKeyPressed(this);

        // put the scene in the window and display it
        window.setScene(scene);
        window.show();
    }

    /**
     * handle method wait user to press a key then it pass the information to the controller 
     * class which execute the program. handle method use 
     * javafx.event.EventHandler<javafx.scene.input.KeyEvent> import to get the user interaction.
     */
    // Event handler for key presses - it just passes the event to the controller
    public void handle(KeyEvent event)
    {
        // send the event to the controller
        controller.userKeyInteraction( event );
    }

    /**
     * This is the method to display our object like ball, bat and bricks. To display bricks
     * we have to use for loop inside of a for loop. When ball hit a brick score will be updated.
     * @param gc              Graphics Context.
     * @param setFill         Change the colour of canvas.
     * @param fillRect        Change the shape of the object into rectangle shape.
     * @param displayBall     Display the ball
     * @param displayGameObj  Display the bat
     */
    // drawing the game image
    public void drawPicture()
    {
        // the game loop is running 'in the background' so we have
        // add the following line to make sure it doesn't change
        // the model in the middle of us updating the image
        synchronized ( model ) 
         {
            // get the 'paint brush' to pdraw on the canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // clear the whole canvas to GREY
            gc.setFill( Color.GREY );
            gc.fillRect( 0, 0, width, height );
            
            // draw the bat and ball
            displayBall( gc, ball );      // Display the Ball
            displayGameObj( gc, bat  );   // Display the Bat
            
      

            // *[2]****************************************************[2]*
            // * Display the bricks that make up the game                 *
            // * Fill in code to display bricks from the brick array      *
            // * Remember only a visible brick is to be displayed         *
            // ************************************************************
             for (GameObj brick: bricks) {
                for (int i = 0; i < bricks.length; i++) {
                
                     if (brick.visible) {
                      displayGameObj(gc, brick);
                     }
                 }      
            }
            
            
            
            // update the score
            infoText.setText("BreakOut: Score = " + score);
       }
    }

    /**
     * displayGameObj method is used to create brick and bat object from 
     * javafx.scene.canvas.GraphicsContext. gc.setFill( go.colour ) will change the colour of 
     * the object and gc.fillRect will shape the object to rectangle. 
     * @param gc        Graphics Context.
     * @param go        Game object.
     * @param setFill   it give colour to the object.
     * @param fillRect  it give shape to bat and bricks.
     */
    // Display a game object - it is just a rectangle on the canvas
    public void displayGameObj( GraphicsContext gc, GameObj go )
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }

    /**
     * displayBall method is used to create our ball which have same code as displayGameObj
     * expect one code we use to get the shape of an object. We use fillOval instead of fillRect.
     * @param gc       Graphics Context.
     * @param go       Game object.
     * @param setFill  Fill colour into the object.
     * @param fillOval this give the ball oval shape.
     */
    //Display ball object in oval shape.
     public void displayBall (GraphicsContext gc, GameObj go) {
        gc.setFill(go.colour);
        gc.fillOval(go.topX, go.topY, go.width, go.height);
    }

    /**
     * update method is used to update any new progress on model class. This will update the GUI.
     * 
     */
    // This is how the Model talks to the View
    // This method gets called BY THE MODEL, whenever the model changes
    // It has to do whatever is required to update the GUI to show the new game position
    public void update()
    {
        // Get from the model the ball, bat, bricks & score
        ball    = model.getBall();                                      // Ball
        bricks  = model.getBricks().toArray(new GameObj[0]);            // Bricks
        bat     = model.getBat();                                       // Bat
        score   = model.getScore();                                     // Score
        //Debug.trace("Update");
        drawPicture();                                                  // Re draw game
       
    }
    
}
