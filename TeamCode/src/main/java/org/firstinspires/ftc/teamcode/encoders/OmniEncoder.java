package org.firstinspires.ftc.teamcode.encoders;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.dependencies.RobotParameters;

public class OmniEncoder {
    LinearOpMode linearOp;
    DcMotorEx motorTop;
    DcMotorEx motorBottom;
    DcMotorEx motorLeft;
    DcMotorEx motorRight;
    DcMotorEx[] motorList;

    double topSpd = 1d;
    double bottomSpd = 1d;
    double leftSpd = 1d;
    double rightSpd = 1d;

    public OmniEncoder(LinearOpMode linearOp){
        this.linearOp = linearOp;
        HardwareMap hardwareMap = linearOp.hardwareMap;
        motorTop = (DcMotorEx) hardwareMap.dcMotor.get("Front");
        motorBottom = (DcMotorEx) hardwareMap.dcMotor.get("Back");
        motorLeft = (DcMotorEx) hardwareMap.dcMotor.get("Left");
        motorRight = (DcMotorEx) hardwareMap.dcMotor.get("Right");
        motorList = new DcMotorEx[]{motorTop, motorBottom, motorLeft, motorRight};
    }

    public void setPower(double p){
        for (DcMotorEx m : motorList){
            m.setPower(p);
        }
    }

    public void setMode(DcMotor.RunMode mode){
        for (DcMotorEx m : motorList){
            m.setMode(mode);
        }
    }

    public void setTarget(int t){
        for (DcMotorEx m : motorList){
            m.setTargetPosition(t);
        }
    }

    public void setTarget(int t, int b, int l, int r){
        motorTop.setTargetPosition(t);
        motorBottom.setTargetPosition(b);
        motorLeft.setTargetPosition(l);
        motorRight.setTargetPosition(r);
    }

    public void setPower(double pT, double pB, double pL, double pR){
        motorTop.setPower(pT);
        motorBottom.setPower(pB);
        motorLeft.setPower(pL);
        motorRight.setPower(pR);
    }

    public void move(double moveInches, double strafeInches, double power){
        // pos is up and right. neg is down and left
        double x = Math.abs(moveInches) / moveInches * power;
        double y = Math.abs(strafeInches) / strafeInches * power;

        // motor powers
        double rx = 0.0;
        double topP = x;
        double bottomP = -x;
        double leftP = y;
        double rightP = -y;

        // motor ticks
        int xT = inToTicks(moveInches);
        int yT = inToTicks(strafeInches);
        int topT = xT;
        int bottomT = -xT;
        int leftT = yT;
        int rightT = -yT;

        // calculate speed multipliers
//        topSpd = ((double) motorTop.getTargetPosition() - motorTop.getCurrentPosition()) / (motorTop.getTargetPosition());
//        bottomSpd = ((double) motorBottom.getTargetPosition() - motorBottom.getCurrentPosition()) / (motorBottom.getTargetPosition());
//        leftSpd = ((double) motorLeft.getTargetPosition() - motorLeft.getCurrentPosition()) / (motorLeft.getTargetPosition());
//        rightSpd = ((double) motorRight.getTargetPosition() - motorRight.getCurrentPosition()) / (motorRight.getTargetPosition());

        setPower(topP*topSpd,
                bottomP*bottomSpd,
                leftP*leftSpd,
                rightP*rightSpd);

        setTarget(topT, bottomT, leftT, rightT);

        // do not run more methods until target is reached (motors will stop being busy)
        if (motorsAreBusy() && linearOp.opModeIsActive()){
            Thread.yield();
        }

//        setPower(0);
//        this.linearOp.sleep(100L);

    }

    public void rotate(double degrees, double power){
        // pos -> CW, neg -> CCW
        double p = Math.abs(degrees) / degrees * power;

        double in  = degToIn(degrees);
        int t = inToTicks(in);

        setPower(p);
        setTarget(t);

        // do not run more methods until target is reached (motors will stop being busy)
        if (motorsAreBusy() && linearOp.opModeIsActive()){
            Thread.yield();
        }
    }

    public void move(double moveInches, double strafeInches){
        move(moveInches,strafeInches,1);
    }

    public int inToTicks(double inches){
        return (int) (inches / RobotParameters.wheelCircumference) * RobotParameters.ticksPerRevolution;
    }

    public double degToIn(double degrees){
        return (RobotParameters.wheelBaseCircumference) * (degrees/360);
    }

    public boolean motorsAreBusy() {
        boolean busy = true;
        for (DcMotor motor : motorList) {
            if (!motor.isBusy()) {
                busy = false;
                break;
            }
        }
        return busy;
    }
}
