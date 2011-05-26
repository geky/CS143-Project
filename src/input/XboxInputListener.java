package input;
/**
 * @author Tim Mikeladze
 * Gives access to XboxControllerInput.exe output
 */

import java.awt.geom.Point2D;
import java.io.*;
import java.util.StringTokenizer;

public class XboxInputListener {
    String line;
    StringTokenizer tokenizer;
    //strings to hold output
    String button_string, leftStick_string, rightStick_string,leftTrigger_string, rightTrigger_string;
    //Point2D holds stick coordinates with Double precision
    Point2D leftStick, rightStick;
    //holds button keycode
    int button;
    //holds trigger state
    double rightTrigger, leftTrigger;
    Process p;
    BufferedReader input;

    public XboxInputListener() throws IOException {
        //player is alive
        startInputListener();
    }
    /**
     * Not sure what to do with this, current prints debug messages
     */
    public void update() {
        if(getRightTriggerState() >= .2) {
            InputRouter.sendAction(InputRouter.Interaction.SHOOT_PRIMARY);
        }
        if(getLeftTriggerState() >= .2){
            InputRouter.sendAction(InputRouter.Interaction.SHOOT_SECONDARY);
        }

        if(getButtonState() == 5) {
            InputRouter.sendAction(InputRouter.Interaction.ROLL_RIGHT);
        }
        if(getButtonState() == 6) {
            InputRouter.sendAction(InputRouter.Interaction.ROLL_LEFT);
        }

        if(getLeftStickState().getY() > 0 && getLeftStickState().getY() < 1) {
            InputRouter.sendAction(InputRouter.Interaction.FORWARD);
        }
        if(getLeftStickState().getY() < 0 && getLeftStickState().getY() < -1) {
            InputRouter.sendAction(InputRouter.Interaction.BACK);
        }

        if(getRightStickState().getY() < 0 && getRightStickState().getY() < -1) {
            InputRouter.sendAction(InputRouter.Interaction.PITCH_UP);
        }
        if(getRightStickState().getY() > 0 && getRightStickState().getY() < 1 ) {
            InputRouter.sendAction(InputRouter.Interaction.PITCH_DOWN);
        }

        System.out.println("Button " + getButtonState());
        System.out.println("LeftStick " + getLeftStickState());
        System.out.println("RightStick " + getRightStickState());
        System.out.println("LeftTrigger " + getLeftTriggerState());
        System.out.println("RightTrigger " + getRightTriggerState());
    }
    
    /**
     * Starts the InputListener
     * @throws IOException 
     * @throws IOException
     */
    public void startInputListener() throws IOException {

        if (isWindows() == true) {
            Process p = null;
            try {
                p = Runtime.getRuntime().exec("XboxControllerInput.exe");
            } catch (IOException e) {
                System.out.println("Couldn't start Xbox Controller Drivers, make sure you have the drivers and the xna runtime installed");
                e.printStackTrace();
            }
            input =new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null)
            {
                tokenizer = new StringTokenizer(line, ",");
                button_string = tokenizer.nextToken();
                leftStick_string = tokenizer.nextToken();
                rightStick_string = tokenizer.nextToken();
                leftTrigger_string = tokenizer.nextToken();
                rightTrigger_string = tokenizer.nextToken();
                update();

            }
        }

        else {
            System.out.println("No Linux or Apple support, blame Microsoft");
        }

    }
    /**
     * Gets coordinates of right stick
     * @return rightStick coordinates
     */
    public Point2D getRightStickState() {
        //splits the rightStick_string and assigns values
        String[] xy = SplitReplace(rightStick_string);
        double x = Double.parseDouble(xy[0]);
        double y = Double.parseDouble(xy[1]);
        //rounds to 10^-2 precision
        x = round(x);
        y = round(y);
        //creates a new Point2D
        rightStick = new Point2D.Double(x, y);
        return rightStick;
    }
    /**
     * Gets coordinates of left stick
     * @return leftStick coordinates
     */
    public Point2D getLeftStickState() {
        String[] xy = SplitReplace(leftStick_string);
        double x = Double.parseDouble(xy[0]);
        double y = Double.parseDouble(xy[1]);
        x = round(x);
        y = round(y);
        leftStick = new Point2D.Double(x, y);
        return leftStick;
    }
    /**
     * Gets Right Trigger position
     * @return rightTrigger position
     */
    public double getRightTriggerState() {
        //parses and rounds
        rightTrigger = round(Double.parseDouble(rightTrigger_string));
        return rightTrigger;
    }
    /**
     * Gets Left Trigger position
     * @return leftTrigger position
     */
    public double getLeftTriggerState() {
        leftTrigger = round(Double.parseDouble(leftTrigger_string));
        return leftTrigger;
    }
    /**
     * Gets the currently pressed Button
     * @return button
     */
    public int getButtonState() {
        button = Integer.parseInt(button_string);
        return button;
    }
    /**
     * Stops the InputListener and clears the Controller
     * @throws IOException
     */
    public void stopInputListener() throws IOException {
        input.close();
        clearControllerState();
    }
    /**
     * Clears Controller
     */
    public void clearControllerState() {
        line = null;
    }
    /**
     * Splits and Replaces strings
     * @param data
     * @return values
     */
    private String[] SplitReplace(String data) {
        String[] values = data.split("");
        data = data.replace("{", "");
        data = data.replace(":", "");
        data = data.replace("X", "");
        data = data.replace("Y", "");
        data = data.replace(":", "");
        data = data.replace("}", "");

        values = data.split(" ");
        return values;
    }
    /**
     * Rounds double to 10^-2 precision
     * @param d
     * @return
     */
    private double round(double d) {
        int temp=(int)((d*Math.pow(10,2)));
        return (((double)temp)/Math.pow(10,2));
    }
    /**
     * Checks if Windows
     * @return
     */
    public static boolean isWindows(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "win" ) >= 0);
    }
    /**
     * Checks if Mac
     * @return
     */
    public static boolean isMac(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "mac" ) >= 0);
    }
    /**
     * Checks if Unix/Linux
     * @return
     */
    public static boolean isUnix(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);

    }

    public static void main(String []args) throws IOException {
        new XboxInputListener();
    }
}
