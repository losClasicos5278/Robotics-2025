package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.*;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

//Unused. keeping just cause I don't want to delete someone elses work.
public class Climber extends SubsystemBase {
    SparkMax armMotor;
    SparkMax armMotor2;
    SparkMaxConfig armMotorConfig1;
    SparkMaxConfig armMotorConfig2;
  
    public Climber() {
      armMotor = new SparkMax(11, MotorType.kBrushed);
      armMotor2 = new SparkMax(10, MotorType.kBrushed);
  
      armMotorConfig1 = new SparkMaxConfig();
      armMotorConfig2 = new SparkMaxConfig();
  
      armMotor.configure(armMotorConfig1.
        inverted(true).
        idleMode(IdleMode.kBrake), 
        ResetMode.kNoResetSafeParameters, 
        PersistMode.kPersistParameters);
  
      armMotor2.configure(armMotorConfig2.
        inverted(true).
        idleMode(IdleMode.kBrake), 
        ResetMode.kNoResetSafeParameters, 
        PersistMode.kPersistParameters);
  
        }
    }
    