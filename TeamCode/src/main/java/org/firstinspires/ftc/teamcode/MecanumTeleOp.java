package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name= "MecanumTeleOp")
//@Disabled
public class MecanumTeleOp extends OpMode {
    // motors
    DcMotor fl_Wheel;
    DcMotor bl_Wheel;
    DcMotor fr_Wheel;
    DcMotor br_Wheel;
    DcMotor placing_slide;
    DcMotor climbing_slide;
    Servo arm_servo;
    Servo claw_servo;
    //private Limelight3A limelight;
    //TouchSensor magnetic_limit;

    @Override
    public void init() {
        fl_Wheel = hardwareMap.get(DcMotor.class, "fl_motor");
        bl_Wheel = hardwareMap.get(DcMotor.class, "bl_motor");
        fr_Wheel = hardwareMap.get(DcMotor.class, "fr_motor");
        br_Wheel = hardwareMap.get(DcMotor.class, "br_motor");
        placing_slide = hardwareMap.get(DcMotor.class, "placing_motor");
        climbing_slide = hardwareMap.get(DcMotor.class, "climbing_motor");
        arm_servo = hardwareMap.get(Servo.class, "arm");
        claw_servo = hardwareMap.get(Servo.class, "claw");
        //limelight = hardwareMap.get(Limelight3A.class, "Limelight 3A");
        //magnetic_limit = hardwareMap.get(TouchSensor.class, "magnetic_limit");

        fr_Wheel.setDirection(DcMotor.Direction.REVERSE);
        fl_Wheel.setDirection(DcMotor.Direction.FORWARD);
        br_Wheel.setDirection(DcMotor.Direction.REVERSE);
        bl_Wheel.setDirection(DcMotor.Direction.REVERSE);

        fr_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        placing_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        climbing_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.setMsTransmissionInterval(11);
        //limelight.pipelineSwitch(0);
        //limelight.start();

        //arm_servo.setPosition(0.16);
        //claw_servo.setPosition(0.66);


    }

    double placing_Position = 0;
    boolean initialize = true;

    public void loop() {

        if (initialize) {
            arm_servo.setPosition(0);
            claw_servo.setPosition(0.5);
            initialize = false;
        }

        //testing a change for github
        // encoders
        double fl_Position = fl_Wheel.getCurrentPosition();
        double bl_Position = bl_Wheel.getCurrentPosition();
        double fr_Position = fr_Wheel.getCurrentPosition();
        double br_Position = br_Wheel.getCurrentPosition();
        //double placing_Position = br_Wheel.getCurrentPosition();
        double arm_Position = arm_servo.getPosition();
        double claw_Position = claw_servo.getPosition();

        telemetry.addData("Front Left Wheel Pos", fl_Position);
        telemetry.addData("Back Left Wheel Pos", bl_Position);
        telemetry.addData("Front Right Wheel Pos", fr_Position);
        telemetry.addData("Back Right Wheel Pos", br_Position);
        //telemetry.addData("Placing Slide Pos", placing_Position);
        //telemetry.addData("Magnetic Limit Boolean", magnetic_limit.isPressed());
        telemetry.addData("Arm Pos", arm_Position);
        telemetry.addData("Claw Pos", claw_Position);


        /*
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            telemetry.addData("April Tag X Pos", result.getTx());
            telemetry.addData("April Tag Y Pos", result.getTy());
            telemetry.addData("April Tag Area %", result.getTa());
            telemetry.addData("Limelight Pipeline", result.getPipelineIndex());
        }
        */

        //pipeline 0 = chamber side
        //pipeline 1 = basket side
        //pipeline 2 = observation side



        // wheel movement
        double left_x = gamepad1.left_stick_x;
        double left_y = gamepad1.left_stick_y;
        double joystick_turn = gamepad1.right_stick_x;
        if (gamepad1.left_stick_button) {
            left_x = (gamepad1.left_stick_x / 2);
            left_y = (gamepad1.left_stick_y / 2);
        }
        if (gamepad1.right_stick_button) {
            joystick_turn = (gamepad1.right_stick_x / 2);
        }

        double joystick_direction = -1 * Math.atan2(left_y, left_x);
        double joystick_magnitude = Math.sqrt((left_x * left_x) + (left_y * left_y));

        double left_x2 = gamepad2.left_stick_x / 2;
        double left_y2 = gamepad2.left_stick_y / 2;
        double joystick_turn2 = gamepad2.right_stick_x / 2;
        double joystick_direction2 = -1 * Math.atan2(left_y2, left_x2) / 2;
        double joystick_magnitude2 = Math.sqrt((left_x2 * left_x2) + (left_y2 * left_y2)) / 2;

        // setting power of wheels based on joystick data
        /*
        fr_Wheel.setPower((-1 * Math.sin(joystick_direction + (0.25 * Math.PI)) * joystick_magnitude + joystick_turn) / 2);
        fl_Wheel.setPower((1 * Math.sin(joystick_direction + (0.25 * Math.PI)) * joystick_magnitude + joystick_turn) / 2);
        br_Wheel.setPower((-1 * Math.sin(joystick_direction - (0.25 * Math.PI)) * joystick_magnitude + joystick_turn) / 2);
        bl_Wheel.setPower((1 * Math.sin(joystick_direction - (0.25 * Math.PI)) * joystick_magnitude + joystick_turn) / 2);
        */

        fr_Wheel.setPower(1.024 * (-1 * Math.sin((joystick_direction + joystick_direction2) - (0.25 * Math.PI)) * (joystick_magnitude + joystick_magnitude2) + (joystick_turn + joystick_turn2)) / 2);
        br_Wheel.setPower(1.000 * (1 * Math.sin((joystick_direction + joystick_direction2) + (0.25 * Math.PI)) * (joystick_magnitude + joystick_magnitude2) - (joystick_turn + joystick_turn2)) / 2);
        fl_Wheel.setPower(1.094 * (-1 * Math.sin((joystick_direction + joystick_direction2) + (0.25 * Math.PI)) * (joystick_magnitude + joystick_magnitude2) - (joystick_turn + joystick_turn2)) / 2);
        bl_Wheel.setPower(0.989 * (1 * Math.sin((joystick_direction + joystick_direction2) - (0.25 * Math.PI)) * (joystick_magnitude + joystick_magnitude2) + (joystick_turn + joystick_turn2)) / 2);

        fr_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // slide movement
        double right;
        double left;
        double right2;
        double left2;

        right = gamepad1.right_trigger;
        left = gamepad1.left_trigger;
        right2 = gamepad2.right_trigger;
        left2 = gamepad2.left_trigger;

        placing_slide.setPower(right + right2 - left - left2);
        climbing_slide.setPower(-1 * (right + right2 - left - left2));
        telemetry.addData("Placing Slide Pos", placing_Position);


        // arm movement
        final double arm_speed = 0.002;
        if (gamepad2.dpad_down) {
            arm_Position += arm_speed;
        } else if (gamepad2.dpad_up) {
            arm_Position -= arm_speed;
        }


        // claw movement
        final double claw_speed = 0.003;
        if ((gamepad2.x || gamepad2.a) && (claw_Position <= 0.6) && (claw_Position >= 0.16)) {
            if (gamepad2.x) {
                claw_Position += claw_speed;
            } else if (gamepad2.a) {
                claw_Position -= claw_speed;
            }
        } else if ((gamepad2.x || gamepad2.a) && claw_Position > 0.6) {
            claw_Position = 0.6;
        } else if ((gamepad2.x || gamepad2.a) && claw_Position < 0.16) {
            claw_Position = 0.16;
        }

        arm_servo.setPosition(arm_Position);
        claw_servo.setPosition(claw_Position);

        telemetry.update();
    }
}

//bleh