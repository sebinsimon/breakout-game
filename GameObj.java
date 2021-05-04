// An object in the game, represented as a rectangle, with a position,
// a size, a colour and a direction of movement.

// Watch out for the different spellings of Color/colour - the class uses American
// spelling, but we have chosen to use British spelling for the instance variable!

// import Athe JavaFX Color class
import javafx.scene.paint.Color;
/**
 * Class GameObj uses different methods for the movement of the bat and ball. 
 * In the GameObj we use height, width, position X and Y, direction X and Y and colour.
 * @param topX    to get the position of top left. 
 * @param topY    to get the position of top right corner.
 * @param width   fix the width of object in the game.
 * @param height  fix the height of object in the game.
 * @param Color   to give colour to the objects in the game.
 * @param dirX    to move to left side.
 * @param dirY    to move to right side.
 */
public class GameObj
{
    // state variables for a game object
    protected boolean visible  = true;     // Can be seen on the screen (change to false when the brick gets hit)
    protected int topX   = 0;              // Position - top left corner X
    protected int topY   = 0;              // position - top left corner Y
    protected int width  = 0;              // Width of object
    protected int height = 0;              // Height of object
    protected Color colour;                // Colour of object
    protected int   dirX   = 1;            // Direction X (1, 0 or -1)
    protected int   dirY   = 1;            // Direction Y (1, 0 or -1)

    /**
     * GameObj method use different variable like position, width and height for the objects
     * in our game.
     * @param topX    is used as x for the position on top left corner. 
     * @param topY    is used as y for the position on top right corner.
     * @param width   is used as w for the width of an object.
     * @param height  is used as h for the height of the object. 
     * @param colour  is used as c which gives colour to our objects like bricks, bat and ball.
     * 
     */
    protected GameObj( int x, int y, int w, int h, Color c )
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = c;
    }

    /**
     * moveX method use to move only on one direction which is left
     * @param 
     */
    // move in x axis
    protected void moveX( int units )
    {
        topX += units * dirX;
    }

    /**
     * moveY method helps to  move the object to the right side of the game window
     */
    // move in y axis
    protected void moveY( int units )
    {
        topY += units * dirY;
    }

    /**
     * In order to change with movement of an object we have to create new method called
     * changeDirectionX(). It helps the object to change the direction only to left.
     */
    // change direction of movement in x axis (-1, 0 or +1)
    protected void changeDirectionX()
    {
        dirX = -dirX;
    }

    /**
     * This method help the bat or the ball to change position from X axis to Y axis. This 
     * method only change to right side of the game window.
     */
    // change direction of movement in y axis (-1, 0 or +1)
    protected void changeDirectionY()
    {
        dirY = -dirY;
    }

    /**
     * This is a boolean value, it will detect collision when the ball hit the bricks. 
     */
    // Detect collision between this object and the argument object
    // It's easiest to work out if they do NOT overlap, and then
    // return the opposite
    protected boolean hitBy( GameObj obj )
    {
        boolean separate =  
            topX >= obj.topX+obj.width     ||    // '||' means 'or'
            topX+width <= obj.topX         ||
            topY >= obj.topY+obj.height    ||
            topY+height <= obj.topY ;
        
        // use ! to return the opposite result - hitBy is 'not separate')
        return(! separate);  
          
    }

    /**
     * This method helps bat not going off the screen. It returns the value of topX.
     * @returns The value of topX 
     */
    //return the value of topX, which is left 
    protected int getDirX() {
        return topX;
    }

}
