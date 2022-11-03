package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LinearSlideEncoder {
    LinearOpMode linearOp;
    DcMotor motorLinearSlide;
    private float height;

    public LinearSlideEncoder (LinearOpMode linearOp){
        this.linearOp = linearOp;
        HardwareMap hardwareMap = linearOp.hardwareMap;
        motorLinearSlide = hardwareMap.dcMotor.get("motorLinearSlide"); // not sure what the id is
        motorLinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public float getHeight(){
        return (height);
    }

    public void changeHeight(float inches) throws InterruptedException{
        float time = 0.5f * Math.abs(inches);

        // 1 or -1 if inches in pos. or neg.
        float dir = inches/(Math.abs(inches));

        motorLinearSlide.setPower(1*dir);
        linearOp.wait( (long) (1000 * time) );
        motorLinearSlide.setPower(0);
        height += inches;
    }

    public void setHeight(float inches) throws InterruptedException{
        float target = height - inches;
        changeHeight(target);
    }

}
