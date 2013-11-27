/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANJaguar.NeutralMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a basic program to control our preseason robot using a Logitech
 * Driving Force GT wheel and pedals.
 */
public class DFGTChassis extends IterativeRobot {
    DrivingForceGT dfgt;
    CANJaguar frontLeft, rearLeft, frontRight, rearRight;
    RobotDrive drive;
    Compressor compressor;
    Solenoid leftShifter, rightShifter;
    NeutralMode currentNeutralMode;
    double gearRatio = 2.56; //possibly 4:1, check to be sure
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        dfgt = new DrivingForceGT(1,2);
        currentNeutralMode = NeutralMode.kCoast;
        try
        {
            frontLeft = new CANJaguar(1);
            frontLeft.configNeutralMode(currentNeutralMode);
            frontLeft.configEncoderCodesPerRev(360);
            rearLeft = new CANJaguar(2);
            rearLeft.configNeutralMode(currentNeutralMode);
            frontRight = new CANJaguar(3);
            frontRight.configNeutralMode(currentNeutralMode);
            frontRight.configEncoderCodesPerRev(360);
            rearRight = new CANJaguar(4);
            rearRight.configNeutralMode(currentNeutralMode);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        compressor = new Compressor(0,0);
        leftShifter = new Solenoid(0);
        rightShifter = new Solenoid(0);
        leftShifter.set(false);
        rightShifter.set(false);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        dfgt.update(); //Has to be called first thing here
        
        //Shift actual gears (solenoids) on robot if necessary
        if(dfgt.getCurrentGear() == 2 && !leftShifter.get())
        {
            leftShifter.set(true);
            rightShifter.set(true);
        }
        else if(dfgt.getCurrentGear() < 2 && leftShifter.get())
        {
            leftShifter.set(false);
            rightShifter.set(false);
        }
        
        
        //Throttle value based on gear.  Uses shifting and "reverse" gear
        double throttleValue = dfgt.getThrottle() * (dfgt.getCurrentGear() / Math.abs(dfgt.getCurrentGear()));
        
        //Assume if brake is pressed, we actually want to brake
        if(dfgt.getBrake() > 0.1)
        {
            drive.drive(0, 0);
            if(currentNeutralMode != NeutralMode.kBrake)
            {
                currentNeutralMode = NeutralMode.kBrake;
                try {
                    frontLeft.configNeutralMode(currentNeutralMode);
                    frontRight.configNeutralMode(currentNeutralMode);
                    rearLeft.configNeutralMode(currentNeutralMode);
                    rearRight.configNeutralMode(currentNeutralMode);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else
        {
            if(currentNeutralMode != NeutralMode.kCoast)
            {
                currentNeutralMode = NeutralMode.kCoast;
                try {
                    frontLeft.configNeutralMode(currentNeutralMode);
                    frontRight.configNeutralMode(currentNeutralMode);
                    rearLeft.configNeutralMode(currentNeutralMode);
                    rearRight.configNeutralMode(currentNeutralMode);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
            drive.arcadeDrive(throttleValue, dfgt.getSteeringValue());
        }
        
        //SmartDashboard outputs
        SmartDashboard.putNumber("Current Gear", dfgt.getCurrentGear());
        try {
            //TODO: See if encoder is before or after gearing.  This is assuming before
            //TODO: Also see what motor rotation speed : robot speed ratio is
            SmartDashboard.putNumber("Current RPM", frontRight.getSpeed());
            SmartDashboard.putNumber("Current Speed", frontRight.getSpeed() * (dfgt.getCurrentGear() == 2 ? gearRatio : 1));
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
