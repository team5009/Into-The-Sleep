package org.firstinspires.ftc.teamcode.instances.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.components.Arm_v2

@TeleOp(name = "Wrist test")
class Wrist_positions_test: LinearOpMode() {
	override fun runOpMode() {
		val arm = Arm_v2(this)
		var left = 0.5
		var right = 0.5
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
			if(gamepad1.triangle) {
				arm.slide_r.power = 0.5
				arm.slide_l.power = 0.5
			}else if(gamepad1.cross){
				arm.slide_r.power = -0.5
				arm.slide_l.power = -0.5
			}else {
				arm.slide_r.power = 0.0
				arm.slide_l.power = 0.0
			}
			arm.wrist_servos(left, right)
			arm.gear_angle()
			if (gamepad1.dpad_up){
				if(right != 1.0 && left != 1.0 && right + left < 2.0) {
					right += 0.01
					left += 0.01
				}
			} else if(gamepad1.dpad_down) {
				if(right != 0.0 && left != 0.0 && right + left >0.0){
					right -= 0.01
					left -= 0.01
				}
			}
			if (gamepad1.right_bumper) {
				arm.intake_servos(1.0)  //outake
			} else if (gamepad1.left_bumper) {
				arm.intake_servos(-1.0)
			} else {
				arm.intake_servos(0.0)
			}
//			if (gamepad1.dpad_left) {
//				if(right != 1.0 && left != 0.0) {
//					right += 0.01
//					left -= 0.01
//				}
//			} else if (gamepad1.dpad_right) {
//				if(right != 0.0 && left != 1.0) {
//					right -= 0.01
//					left += 0.01
//				}
//			}
			telemetry.addData("right", right)
			telemetry.addData("left", left)
			telemetry.addData("Gear angle", arm.gear_angle())
			telemetry.update()
		}
	}
}
