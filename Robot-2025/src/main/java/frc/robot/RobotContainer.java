// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import frc.robot.Constants.AccessoryConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.List;


import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.RelativeEncoder;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();

  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  SparkMax armMotor = new SparkMax(AccessoryConstants.leftArmMotorCanId, MotorType.kBrushless);
  SparkMax armMotor2 = new SparkMax(AccessoryConstants.rightArmMotorCanId, MotorType.kBrushless);

  SparkMax intakeMotor = new SparkMax(AccessoryConstants.leftIntakeMotorCanId, MotorType.kBrushed);
  SparkMax intakeMotor2 = new SparkMax(AccessoryConstants.rightIntakeMotorCanId, MotorType.kBrushed);

  SparkLimitSwitch armMotorLimit = armMotor.getForwardLimitSwitch();
  SparkLimitSwitch armMotor2Limit = armMotor2.getForwardLimitSwitch();

  SparkLimitSwitch armMotorLimitReverse = armMotor.getReverseLimitSwitch();
  SparkLimitSwitch armMotor2LimitReverse = armMotor2.getReverseLimitSwitch();

  RelativeEncoder encoder = armMotor.getEncoder();
  RelativeEncoder encoder2 = armMotor2.getEncoder();
  

  public void climberConfig() {
    SparkMaxConfig armMotorConfig1 = new SparkMaxConfig();
    SparkMaxConfig armMotorConfig2 = new SparkMaxConfig();

    // armMotor.configure(armMotorConfig1.idleMode(IdleMode.kBrake), ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    // armMotor2.configure(armMotorConfig2.idleMode(IdleMode.kBrake), ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    // setting limits here
    // SparkLimitSwitch armMotorLimit = armMotor.getForwardLimitSwitch();
    // SparkLimitSwitch armMotor2Limit = armMotor2.getForwardLimitSwitch();

    // RelativeEncoder encoder = armMotor.getEncoder();
    // RelativeEncoder encoder2 = armMotor2.getEncoder();

    armMotorConfig1.limitSwitch
    .forwardLimitSwitchType(Type.kNormallyOpen)
    .forwardLimitSwitchEnabled(true)
    .reverseLimitSwitchType(Type.kNormallyOpen)
    .reverseLimitSwitchEnabled(true);

    armMotorConfig1.softLimit
    .forwardSoftLimit(10)
    .forwardSoftLimitEnabled(true)
    .reverseSoftLimit(-10)
    .reverseSoftLimitEnabled(true);

    armMotorConfig2.limitSwitch
    .forwardLimitSwitchType(Type.kNormallyOpen)
    .forwardLimitSwitchEnabled(true)
    .reverseLimitSwitchType(Type.kNormallyOpen)
    .reverseLimitSwitchEnabled(true);


    armMotorConfig2.softLimit
    .forwardSoftLimit(50)
    .forwardSoftLimitEnabled(true)
    .reverseSoftLimit(-50)
    .reverseSoftLimitEnabled(true);

    armMotorConfig1.idleMode(IdleMode.kBrake);
    armMotorConfig2.idleMode(IdleMode.kBrake);

    armMotor.configure(armMotorConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    armMotor2.configure(armMotorConfig2, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);


    encoder.setPosition(0);
    encoder2.setPosition(0);
}

// public void climberLimits() {
//     SparkLimitSwitch armMotorLimit = armMotor.getForwardLimitSwitch();
//     SparkLimitSwitch armMotor2Limit = armMotor2.getForwardLimitSwitch();

//     RelativeEncoder encoder = armMotor.getEncoder();
//     RelativeEncoder encoder1 = armMotor2.getEncoder();


// }


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // Configure default commands
    m_robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> m_robotDrive.drive(
                -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
                true),
            m_robotDrive));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_driverController, Button.kR1.value)
        .whileTrue(
            //new LogCommand("Joystick Pressed")
        new RunCommand(
            () -> m_robotDrive.setX(),
            m_robotDrive)
            );
  }


  public class LogCommand extends Command {
    private String message = "";
    public LogCommand(String message){
        this.message = message;
        System.out.println(message);
    } 
    @Override
    public void execute() {
        System.out.println(message);
    }

  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(
        AutoConstants.kMaxSpeedMetersPerSecond,
        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(DriveConstants.kDriveKinematics);

    // An example trajectory to follow. All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),
        config);

    var thetaController = new ProfiledPIDController(
        AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        exampleTrajectory,
        m_robotDrive::getPose, // Functional interface to feed supplier
        DriveConstants.kDriveKinematics,

        // Position controllers
        new PIDController(AutoConstants.kPXController, 0, 0),
        new PIDController(AutoConstants.kPYController, 0, 0),
        thetaController,
        m_robotDrive::setModuleStates,
        m_robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, true));
  }

  public void resetGyro() {
    m_robotDrive.zeroHeading();
  }
}
