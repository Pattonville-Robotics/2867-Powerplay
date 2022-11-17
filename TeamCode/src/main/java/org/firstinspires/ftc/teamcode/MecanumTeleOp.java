package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.encoders.ClawEncoder;
import org.firstinspires.ftc.teamcode.encoders.LinearSlideEncoder;
import org.firstinspires.ftc.teamcode.encoders.MecanumEncoder;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        GamepadEx controller1 = new GamepadEx(gamepad1);
        GamepadEx controller2 = new GamepadEx(gamepad2);
        final LinearSlideEncoder sEncoder = new LinearSlideEncoder(this);
        final MecanumEncoder mEncoder = new MecanumEncoder(this);
        final ClawEncoder cEncoder = new ClawEncoder(this);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -controller1.getLeftY(); // Remember, this is reversed!
            double x = controller1.getLeftX() * 1.1; // Counteract imperfect strafing
            double rx = controller1.getRightX();
            /*
            Denominator is the largest motor power (absolute value) or 1
            This ensures all the powers maintain the same ratio, but only when
            at least one is out of the range [-1, 1]
            */
            // Mecanum movement
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            mEncoder.setPower(frontLeftPower, backLeftPower, frontRightPower, backRightPower);

            // Linear slide speed
            float LT = gamepad2.left_trigger;
            float slideSpeed = (LT == 0) ? 1 : LT;

            // Linear slide movement
            ButtonReader a2 = new ButtonReader(controller2, GamepadKeys.Button.A);
            ButtonReader x2 = new ButtonReader(controller2, GamepadKeys.Button.X);
            ButtonReader y2 = new ButtonReader(controller2, GamepadKeys.Button.Y);
            GamepadButton LB = new GamepadButton(controller2, GamepadKeys.Button.LEFT_BUMPER);

            if (a2.wasJustPressed()) {
                sEncoder.setHeight(LinearSlideEncoder.LinearPosition.ONE, slideSpeed);
            }
            if (x2.wasJustPressed()) {
                sEncoder.setHeight(LinearSlideEncoder.LinearPosition.TWO, slideSpeed);
            }
            if (y2.wasJustPressed()) {
                sEncoder.setHeight(LinearSlideEncoder.LinearPosition.THREE, slideSpeed);
            }
            if (a2.wasJustPressed() && LB.get()) {  // LB held and A pressed
                sEncoder.setHeight(LinearSlideEncoder.LinearPosition.ZERO, slideSpeed);
            }

            // Claw
            ButtonReader b2 = new ButtonReader(controller2, GamepadKeys.Button.B);
            if (b2.wasJustPressed()) {
                cEncoder.toggleClaw();
            }
        }
    }
}