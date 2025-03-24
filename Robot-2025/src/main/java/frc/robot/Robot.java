package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private double goalArmPosition = 0;
  //To do: Adjust positions to real start. These positions are when the arm is directly up.
  private final double armHighPosition = 50;//TODO: find real position
  private final double armLowPosition = 10;//TODO: find real position
  private final double armIntakePosition = 5;//TODO: find real position
  private final double armClimbPosition = 90;//TODO: find real position
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    m_robotContainer.resetGyro();

    m_robotContainer.climberConfig();
    configureAButton();
    configureYButton();
    configureXButton();
    configureBButton();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    SmartDashboard.putBoolean("Motor 1 Forward Limit Reached", m_robotContainer.armMotorLimit.isPressed());
    SmartDashboard.putBoolean("Motor 1 Reverse Limit Reached", m_robotContainer.armMotorLimitReverse.isPressed());
    SmartDashboard.putBoolean("Motor 2 Forward Limit Reached", m_robotContainer.armMotor2Limit.isPressed());
    SmartDashboard.putBoolean("Motor 2 Reverse Limit Reached", m_robotContainer.armMotor2LimitReverse.isPressed());
    SmartDashboard.putNumber("Arm 1 Position", m_robotContainer.encoder.getPosition());
    SmartDashboard.putNumber("Arm 2 Position", m_robotContainer.encoder2.getPosition());
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    m_robotContainer.armMotor.stopMotor();
    m_robotContainer.armMotor2.stopMotor();
    m_robotContainer.intakeMotor.stopMotor();
    m_robotContainer.intakeMotor2.stopMotor();
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_robotContainer.climberConfig();
  }


  private void configureAButton() {
    new JoystickButton(m_robotContainer.m_driverController, Button.kA.value)
      .whileTrue(
        //new LogCommand ("Joystick Pressed")
      new InstantCommand(() -> goalArmPosition = armLowPosition));
        
  }

private void configureYButton() {
    new JoystickButton(m_robotContainer.m_driverController, Button.kY.value)
      .whileTrue(
        //new LogCommand ("Joystick Pressed")
      new InstantCommand(() -> goalArmPosition = armClimbPosition));
      
  }
private void configureXButton() {
    new JoystickButton(m_robotContainer.m_driverController, Button.kX.value)
      .whileTrue(
        //new LogCommand ("Joystick Pressed")
      new InstantCommand(() -> goalArmPosition = armHighPosition));
        
  }
  private void configureBButton() {
    new JoystickButton(m_robotContainer.m_driverController, Button.kB.value)
      .whileTrue(
        //new LogCommand ("Joystick Pressed")
      new InstantCommand(() -> goalArmPosition = armIntakePosition));
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    boolean buttonY = m_robotContainer.m_driverController.getYButton();
    boolean buttonA = m_robotContainer.m_driverController.getAButton();
    boolean buttonB = m_robotContainer.m_driverController.getBButton();
    boolean buttonX = m_robotContainer.m_driverController.getXButton();
    boolean buttonLeftBumper = m_robotContainer.m_driverController.getLeftBumperButton();
    boolean buttonRightBumper = m_robotContainer.m_driverController.getRightBumperButton();
    double buttonRightTrigger = m_robotContainer.m_driverController.getRightTriggerAxis();
    // m_robotContainer.climberConfig();
    System.out.println(goalArmPosition);
    // setArmMotorsValue(buttonY, buttonA);
    setArmMotorsValue(isUpPressed(), isDownPressed());
    setIntakeMotorsValue(buttonRightBumper, buttonLeftBumper, buttonRightTrigger);
    // setArmPosition();
    //setIntakeMotorsValue(buttonB, buttonX, buttonLeftBumper);
  }

  private boolean isDownPressed(){

    int pov = m_robotContainer.m_driverController.getPOV();
    return pov == 180; 
  }
private boolean isUpPressed(){

    int pov = m_robotContainer.m_driverController.getPOV();
    return pov == 0; 
  }

  private void setArmPosition() {
    double realPosition = m_robotContainer.encoder2.getPosition();
    if(realPosition > goalArmPosition) {
      m_robotContainer.armMotor.set(0.3);
      m_robotContainer.armMotor2.set(-0.3);

    } else if(realPosition  < goalArmPosition) {
      m_robotContainer.armMotor.set(0.3);
      m_robotContainer.armMotor2.set(-0.3);

    } else {
      m_robotContainer.armMotor.set(0.01);
      m_robotContainer.armMotor2.set(-0.01);
    }

  }



  public void setArmMotorsValue (boolean isUpButtonPressed, boolean isDownButtonPressed) {
    if (isUpButtonPressed) {
      m_robotContainer.armMotor.set(0.3);
      m_robotContainer.armMotor2.set(-0.3);
    } else if (isDownButtonPressed) {
      m_robotContainer.armMotor.set(-0.3);
      m_robotContainer.armMotor2.set(0.3);
    } else {
      m_robotContainer.armMotor.set(0.0);
      m_robotContainer.armMotor2.set(0.0);
    }
  }

  public void setIntakeMotorsValue(boolean isOutakeButtonPressed, boolean isIntakeButtonPressed, double rightTriggerValue) {
    if (rightTriggerValue > 0.1) {
        // Only activate one motor when the left bumper is held
        m_robotContainer.intakeMotor.set(-0.6);
        m_robotContainer.intakeMotor2.set(0.0);  // Set the second motor to 0 to avoid reversing direction
    } else if (isIntakeButtonPressed) {
        // Both motors run for intake action
        m_robotContainer.intakeMotor.set(0.6);
        m_robotContainer.intakeMotor2.set(-0.6);
    } else if (isOutakeButtonPressed) {
        // Both motors run for outtake action
        m_robotContainer.intakeMotor.set(-0.6);
        m_robotContainer.intakeMotor2.set(0.6);
    } else {
        // Motors are stopped if no button is pressed
        m_robotContainer.intakeMotor.set(0.0);
        m_robotContainer.intakeMotor2.set(0.0);
    }
}


  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}