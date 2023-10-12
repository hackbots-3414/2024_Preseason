package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase{
private WPI_TalonFX motorFrontLeft = new WPI_TalonFX(10);
private WPI_TalonFX motorBackLeft = new WPI_TalonFX(11);
private WPI_TalonFX motorFrontRight = new WPI_TalonFX(13);
private WPI_TalonFX motorBackRight = new WPI_TalonFX(14);
private DifferentialDrive differentialDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);

public Drivetrain() {
    motorBackLeft.follow(motorFrontLeft);
    motorBackRight.follow(motorFrontRight);

    motorFrontLeft.setInverted(true);
    motorFrontRight.setInverted(true);
}

@Override
public void periodic() {
motorFrontLeft.feed();
motorFrontLeft.feed();
motorBackLeft.feed();
motorBackRight.feed();
}

public void drive(double xSpeed, double zRotation) {
    differentialDrive.arcadeDrive(xSpeed, zRotation);
    }
}
