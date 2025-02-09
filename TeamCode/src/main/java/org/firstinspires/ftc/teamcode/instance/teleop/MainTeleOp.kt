package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target
import kotlin.math.abs

@TeleOp(name = "Main")
class MainTeleOp: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		motors.setPowerRatio(1.0)

		val timer = ElapsedTime()
		var ended = false
		var started = false
		var drive = 0.0

		val controls = TeleOp_GamePads(this)

		while (opModeInInit()) {
			telemetry.addData(
				"arm error: ",
				controls.arm.pid_slide.calculate(slide_target.get() - controls.arm.slide_r.position / controls.arm.slide_inches_ticks)
			)
			telemetry.addData("slide pos", controls.arm.slide_height())
			telemetry.addData("gear pos", controls.arm.gear_r.position / controls.arm.gear_degrees_ticks)
			telemetry.update()
		}
		//controls.hang_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
		//controls.hang_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
		waitForStart()
		timer.reset()
		controls.arm.init_teleOp()


		CoroutineScope(Dispatchers.Default).launch {
			while(opModeIsActive()) {
				controls.game_pad_1()
			}
		}
		CoroutineScope(Dispatchers.Default).launch {
			while(opModeIsActive()) {
				controls.game_pad_2()
			}
		}

		while (opModeIsActive()) {
//			if(controls.hang_arm.position > 4000.0 && !ended && started){
//				controls.hang_arm.setPowerWithTol(0.0)
//				ended = true
//			}
//			if(timer.seconds() > 10.0 && !ended && !started){
//				controls.hang_arm.setPowerWithTol(1.0)
//				started = true
//			}
			drive = -gamepad1.left_stick_y.toDouble()
			if(gamepad2.dpad_up && abs(drive) < 0.2){
				drive = 0.6
			}else if(gamepad2.dpad_down && abs(drive) < 0.2){
				drive = -0.6
			}
			val strafe = gamepad1.left_stick_x.toDouble()
			val rotate = gamepad1.right_stick_x.toDouble()
			motors.gamepadMove(drive, strafe, rotate)
			controls.arm.go_to_target(!Arm_v2.grav.get(), !Arm_v2.free_slide.get())
			telemetry.addData("State", controls.armState)
			telemetry.addData("gear", controls.arm.gear_angle())
			telemetry.addData("R", controls.arm.right_wrist.position)
			telemetry.addData("L", controls.arm.left_wrist.position)
			//telemetry.addData("Hang encorder", controls.hang_arm.position)
			telemetry.update()
		}
	}
}

