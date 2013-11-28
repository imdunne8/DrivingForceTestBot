/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author dunnen
 */
public class DrivingForceGT extends Joystick{
    
    static final int axisRotation = 1;
    static final int axisThrottle = 2;
    static final int axisBrake = 3;
    static final int axisHatLR = 5;
    static final int axisHatUD = 6;
    static final int buttonX = 1;
    static final int buttonSquare = 2;
    static final int buttonTriangle = 4;
    static final int buttonCircle = 3;
    static final int buttonShiftUp = 5;
    static final int buttonShiftDown = 6;
    static final int buttonR2 = 7;
    static final int buttonL2 = 8;
    static final int buttonR3 = 11;
    static final int buttonL3 = 12;
    static final int buttonSelect = 9;
    static final int buttonStart = 10;
    
    private boolean isUpshiftPressed = false;
    private boolean wasUpshiftPressed = false;
    private boolean isDownshiftPressed = false;
    private boolean wasDownshiftPressed = false;
    private int currentGear = 1;
    private int numGears = 1;
    
    /**
     * Initializes joystick object and sets number of gears
     * @param port Port that joystick is set to in Driver Station
     * @param numGears number of gears the robot has, not including reverse
     */
    public DrivingForceGT(int port, int numGears)
    {
        super(port);
        this.numGears = numGears;
    }
    
    /**
     * This should be called one time per loop of the program, at the beginning 
     * of the loop.  Used to ensure shift events only happen one time each and 
     * gear is set correctly
     */
    public void update()
    {
        wasUpshiftPressed = isUpshiftPressed;
        wasDownshiftPressed = isDownshiftPressed;
        isUpshiftPressed = getRawButton(buttonShiftUp);
        isDownshiftPressed = getRawButton(buttonShiftDown);
        if(isUpshiftPressed && !wasUpshiftPressed && currentGear < numGears)
        {
            if(currentGear == -1)
                currentGear++; //don't use 0 as a gear (between reverse and 1)
            currentGear++;
        }
        if(isDownshiftPressed && !wasDownshiftPressed && currentGear > -1)
        {
            if(currentGear == 1)
                currentGear--; //don't use 0 as a gear (between reverse and 1)
            currentGear--;
        }
    }
    
    /**
     * Gets the value of the steering wheel input. Fully CCW is -1, fully CW is 1
     * @return Value in range of -1:1, where 0 is center
     */
    public double getSteeringValue()
    {
        return getRawAxis(axisRotation);
    }
    
    /**
     * Throttle pedal value where no throttle is 0, full throttle is 1
     * @return Throttle value in range of 0:1
     */
    public double getThrottle()
    {
        return getRawAxis(axisThrottle) / -2 + 0.5;
    }
    
    /**
     * Brake pedal value where no brake is 0, full brake is 1
     * @return Brake value in range of 0:1
     */
    public double getBrake()
    {
        return getRawAxis(axisBrake) / -2 + 0.5;
    }
    
    /**
     * Reports hat button as a boolean
     * @return True if hat up button is pressed
     */
    public boolean getHatUp()
    {
        return getRawAxis(axisHatUD) < 0;
    }
    
    /**
     * Reports hat button as a boolean
     * @return True if hat down button is pressed
     */
    public boolean getHatDown()
    {
        return getRawAxis(axisHatUD) > 0;
    }
    
    /**
     * Reports hat button as a boolean
     * @return True if hat left button is pressed
     */
    public boolean getHatLeft()
    {
        return getRawAxis(axisHatLR) < 0;
    }
    
    /**
     * Reports hat button as a boolean
     * @return True if hat right button is pressed
     */
    public boolean getHatRight()
    {
        return getRawAxis(axisHatLR) > 0;
    }
    
    /**
     * Value of wheel button X
     * @return true if X is pressed
     */
    public boolean getButtonX()
    {
        return getRawButton(buttonX);
    }
    
    /**
     * Value of wheel button Circle (O)
     * @return true if Circle is pressed
     */
    public boolean getButtonCircle()
    {
        return getRawButton(buttonCircle);
    }
    
    /**
     * Value of wheel button Square
     * @return true if Square is pressed
     */
    public boolean getButtonSquare()
    {
        return getRawButton(buttonSquare);
    }
    
    /**
     * Value of wheel button Triangle
     * @return true if Triangle is pressed
     */
    public boolean getButtonTriangle()
    {
        return getRawButton(buttonTriangle);
    }
    
    /**
     * Value of wheel button R2
     * @return true if R2 is pressed
     */
    public boolean getButtonR2()
    {
        return getRawButton(buttonR2);
    }
    
    /**
     * Value of wheel button L2
     * @return true if L2 is pressed
     */
    public boolean getButtonL2()
    {
        return getRawButton(buttonL2);
    }
    
    /**
     * Value of wheel button R3
     * @return true if R3 is pressed
     */
    public boolean getButtonR3()
    {
        return getRawButton(buttonR3);
    }
    
    /**
     * Value of wheel button L3
     * @return true if L3 is pressed
     */
    public boolean getButtonL3()
    {
        return getRawButton(buttonL3);
    }
    
    /**
     * Value of wheel button Start
     * @return true if Start is pressed
     */
    public boolean getButtonStart()
    {
        return getRawButton(buttonStart);
    }
    
    /**
     * Value of wheel button Select
     * @return true if Select is pressed
     */
    public boolean getButtonSelect()
    {
        return getRawButton(buttonSelect);
    }
    
    /**
     * Returns true only ONE TIME for each shift event.  Regardless of how long the
     * button is held, the upshift button will have to be released before this will
     * report true again
     * @return true one time for each upshift event, upon the button press
     */
    public boolean getShiftUp()
    {
        return isUpshiftPressed && !wasUpshiftPressed;
    }
    
    /**
     * Returns true only ONE TIME for each shift event.  Regardless of how long the
     * button is held, the downshift button will have to be released before this will
     * report true again
     * @return true one time for each downshift event, upon the button press
     */
    public boolean getShiftDown()
    {
        return isDownshiftPressed && !wasDownshiftPressed;
    }
    
    /**
     * Returns current "gear" selection, in range of -1,1:numGears
     * @return current gear selection
     */
    public int getCurrentGear()
    {
        return currentGear;
    }
}
