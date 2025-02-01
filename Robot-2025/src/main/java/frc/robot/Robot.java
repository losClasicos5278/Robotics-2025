// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import java.security.PrivateKey;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private DigitalInput limitSwitch;
  private XboxController controller;
  private CANSparkMax armMotor;
  private CANSparkMax intakeMotor;

    private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    controller = new XboxController(0);
    armMotor = new CANSparkMax(1, MotorType.kBrushless);
    intakeMotor = new CANSparkMax(2, MotorType.kBrushless);
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    System.out.println("teleopInit");
    limitSwitch = new DigitalInput (0);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    boolean buttonY = controller.getYbutton();
    boolean buttonA = controller.getAbutton();
    boolean buttonB = controller.getBbutton();

    setArmMotorValue(buttonY, buttonA);
    setIntakeMotorValue(buttonB);
  }
  
  public void setArmMotorValue (boolean buttonY, boolean buttonA) {
    if (buttonY) {
      armMotor.set(1.0);
    } else if (buttonA) {
      armMotor.set(-1.0);
    } else {
      armMotor.set(0.0);
    }
  }  

  public void setIntakeMotorValue(boolean buttonB) {
    if (buttonB) {
      intakeMotor.set(1.0);
    } else {
      intakeMotor.set(-1.0);
    }
  }
  //   boolean isPressed = limitSwitch.get();
  //   //System.out.println("teleopPeriodic");
  //   if (isPressed){
  //     //System.out.println("Pressed");
  //   }
  //   else {
  //     System.out.println("Not Pressed");
  //   }
  // }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    armMotor.stopMotor();
    intakeMotor.stopMotor();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
