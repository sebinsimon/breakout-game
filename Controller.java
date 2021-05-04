// The breakout controller converts key presses from the user (received by the View object)
// into commands for the game (in the Model object)

// we need to use on JavaFX class
import javafx.scene.input.KeyEvent;
/**
 * Controller class is the mediator between the user and the game. It listen to the key commands 
 * from the user and pass it to the View and Model objects. 
 */
public class Controller
{
  // instance variables for the two other components of the MVC model  
  public Model model;
  public View  view;
 

  /**
   * This method is created to print debugging messages. 
   */
  // we don't really need a constructor method, but include one to print a 
  // debugging message if required
  public Controller()
  {
    Debug.trace("Controller::<constructor>");
  }

  /**
   * This is the main method for takig key commands from the user and act accordingly. This
   * method communicate with the View model to update the game window and with Model then only
   * the game work smoothly. 
   */
  // This is how the View talks to the Controller
  // AND how the Controller talks to the Model
  // This method is called by the View to respond to key presses in the GUI
  // The controller's job is to decide what to do. In this case it converts
  // the keypresses into commands which are run in the model
  public void userKeyInteraction(KeyEvent event )
  {
    // print a debugging message to show a key has been pressed
    Debug.trace("Controller::userKeyInteraction: keyCode = " + event.getCode() );
    
    // KeyEvent objects have a method getCode which tells us which key has been pressed.
    // KeyEvent also provides variables LEFT, RIGHT, F, N, S (etc) which are the codes
    // for individual keys. So you can add keys here just by using ther name (which you
    // can find out by googling 'JavaFX KeyCode')
    switch ( event.getCode() )             
    {
      case LEFT:                     // Left Arrow
        model.moveBat( -4);          // move bat left
        break;
      case RIGHT:                    // Right arrow
        model.moveBat( +4 );         // Move bat right
        break;
      case F :
        // Very fast ball movement
        model.setFast(true);
        break;
      case N :
        // Normal speed ball movement
        model.setFast(false);
        break;
      case S :
        // stop the game
        model.setGameState("finished");
        break;
    }
  }
}
