package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import kotlin.math.abs

@TeleOp(name = "Wrist test")
class Wrist_positions_test: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		motors.setPowerRatio(1.0)
		val arm = Arm_v2(this)
		val s = My_Color_Sensor(this)
		var left = 0.0
		var right = 0.0
		var drive = 0.0
		val half_way = 180.0
		arm.slide_r.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		arm.slide_l.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		arm.slide_r.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		arm.slide_l.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		arm.gear_r.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		arm.gear_l.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		arm.gear_r.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		arm.gear_l.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		telemetry.addData("PIDF calc", arm.pid_gear.directCalculate(arm.gear_r.position.toDouble()))
		waitForStart()
		while (opModeIsActive()) {

			drive = -gamepad1.left_stick_y.toDouble()

			val strafe = gamepad1.left_stick_x.toDouble()
			val rotate = gamepad1.right_stick_x.toDouble()
			motors.gamepadMove(drive, strafe, rotate)

			if(gamepad1.triangle) {
				arm.slide_r.power = 1.0
				arm.slide_l.power = 1.0
			}else if(gamepad1.cross){
				arm.slide_r.power = -1.0
				arm.slide_l.power = -1.0
			}else {
				arm.slide_r.power = 0.0
				arm.slide_l.power = 0.0
			}
			if(gamepad1.square) {
				arm.gear_l.power = 1.0
				arm.gear_r.power = 1.0
			}else if(gamepad1.circle){
				arm.gear_r.power = -1.0
				arm.gear_l.power = -1.0
			}else {
				arm.gear_l.power = 0.0
				arm.gear_r.power = 0.0
			}
			arm.wrist_servos(left, right)
			arm.gear_angle()
			if (gamepad1.dpad_up){
				if(right != left){
					left = 0.25
					right = 0.25
				}else if(right + left < 1.0 && right < 0.5 && left < 0.5) {
					right += 0.01
					left += 0.01
				}
			}
			if(gamepad1.dpad_down) {
				if(right != left){
					left = 0.25
					right = 0.25
				}else if(right + left > 0.0 && right > 0.0 && left > 0.0){
					right -= 0.01
					left -= 0.01
				}
			}
//			if (gamepad1.dpad_left) {
//				if(right + left <= 1.10 && right < 1.0 && left > 0.0) {
//					right += 0.01
//					left -= 0.01
//				}
//			}
//			if (gamepad1.dpad_right) {
//				if(right + left <= 1.10 && left < 1.0 && right > 0.0) {
//					right -= 0.01
//					left += 0.01
//				}
//			}
			if (gamepad1.right_bumper) {
				arm.intake_servos(1.0)  //outtake
			} else if (gamepad1.left_bumper) {
				arm.intake_servos(-1.0)
			} else {
				arm.intake_servos(0.0)
			}
//
			telemetry.addData("right", right)
			telemetry.addData("left", left)
			telemetry.addData("Gear angle", arm.gear_angle())
			telemetry.addData("distance", s.dist())
			telemetry.addData("color", s.sensor())
			telemetry.update()
		}
	}
}
