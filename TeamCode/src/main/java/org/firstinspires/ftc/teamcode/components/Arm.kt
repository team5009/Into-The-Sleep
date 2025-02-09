package org.firstinspires.ftc.teamcode.components

import androidx.appcompat.widget.SearchView
import ca.helios5009.hyperion.core.PIDFController
import ca.helios5009.hyperion.core.ProportionalController
import ca.helios5009.hyperion.hardware.HyperionMotor
import ca.helios5009.hyperion.misc.constants.Position
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.auto.PID_Tuning_F
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

class Arm(private val instance:LinearOpMode) {

    val front_extension = 20.0
    val back_extension = -30.0
    val reference_angle = 3/4*Math.PI
    val safety = 0.1

    //val pid_gear = PIDFController(0.8, 0.0, 0.0, 0.0)
    val pid_gear = ProportionalController(0.072, 0.27, 0.4, 0.0, false)
    val pid_slide = ProportionalController(0.8,0.3,1.0,0.0, false)//gain was 0.7

    val gear_degrees_ticks = (100*384.5)/16.0/360.0
    val gear_ratio = 10.0/14.0
    val slide_inches_ticks = 384.5*25.4/120.0 //* gear_ratio

    val gear = HyperionMotor(instance.hardwareMap, "GEAR")
    val slide = HyperionMotor(instance.hardwareMap, "SLIDE")
    val left_wrist = instance.hardwareMap.get(Servo::class.java, "Left_Wrist")
    val right_wrist = instance.hardwareMap.get(Servo::class.java, "Right_Wrist")
    val intake_1 = instance.hardwareMap.get(CRServo::class.java, "Intake_1")
    val intake_2 = instance.hardwareMap.get(CRServo::class.java,"Intake_2")

    var gear_state = false
    var slide_state = false

    init {
        slide.motor.direction = DcMotorSimple.Direction.REVERSE
        gear.motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun init_motors(){
        slide.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slide.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        gear.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        gear.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        slide_target.set(0.0)
        gear_target.set(0.0)

        //wrist_servos(0.25,0.25)
        intake_1.power = 0.0
        intake_2.power = 0.0
    }
    fun init_teleOp(){
        gear_target.set(35.0)
        slide_target.set(1.0)
        intake_1.power = 0.0
        intake_2.power = 0.0
    }
    fun go_to_target(gear_is_on:Boolean = true, slide_is_on:Boolean = true){
        val gearOutput = pid_gear.update(Range.clip(gear_target.get(),0.0, 130.0)*gear_degrees_ticks - gear.position)
        //val gearOutput = pid_gear.calculate(gear.getPosition().toDouble(),Range.clip(gear_target.get(),0.0, 130.0)*gear_degrees_ticks)
        val gear_angle = gear.position/gear_degrees_ticks
        val slideOutput = if(gear_angle > 50.0){
            pid_slide.update(min(front_extension, slide_target.get()) - slide.position / slide_inches_ticks)
        }
        else if(gear_angle < 0.0){
            pid_slide.update(min(abs(back_extension), slide_target.get()) - slide.position / slide_inches_ticks)
        }
        else{
            pid_slide.update(min(slide_target.get(), abs(back_extension/ cos(reference_angle - Math.toRadians(gear_angle)))) - slide.position / slide_inches_ticks)
        }

        instance.telemetry.addData("Error Slide", slideOutput)
        instance.telemetry.addData("slide_target", slide_target.get())
        instance.telemetry.addData("Error Gear", gearOutput)
        instance.telemetry.addData("thingy", abs(back_extension/ cos(reference_angle - Math.toRadians(gear_angle))))
        instance.telemetry.addData("sqrt", sqrt(abs(slideOutput)) * sign(slideOutput))
        if(gear_is_on && !gear_state) {
            gear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            gear_state = true
        } else if(!gear_is_on && gear_state) {
            gear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            gear_state = false
        }

        if(slide_is_on && !slide_state) {
            slide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            slide_state = true
        } else if (!slide_is_on && slide_state) {
            slide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            slide_state = false
        }

        if(gear_is_on) {
            if (gear.position / gear_degrees_ticks < 20.0) {
                gear.power = abs(gearOutput).pow(1.0 / 2.0) * sign(gearOutput) * 0.9
            } else if (gearOutput > 0) {
                gear.power = abs(gearOutput).pow(3.0 / 1.0) * sign(gearOutput) * 0.4
            } else {
                gear.power = abs(gearOutput).pow(1.0 / 2.0) * sign(gearOutput) * 1.0
            }
            //gear.setPower(abs(gearOutput).pow(5.0/7.0) * sign(gearOutput) * 0.8)
        } else {
            gear.power = 0.0
        }
        if(slide_is_on) {
            slide.power = abs(slideOutput).pow(4.0/7.0) * sign(slideOutput)
        } else {
            slide.power = 0.0
        }
    }

    fun wrist_servos(left: Double, right: Double){
        left_wrist.position = min(left + safety, 0.9)
        right_wrist.position = min(right + safety, 0.9)
    }

    fun intake_servos(power: Double){
        intake_1.power = power
        intake_2.power = -power
    }

    enum class ArmState {
        CRUISE,
        LOW_BASKET,
        HIGH_BASKET,
        LOW_CHAMBER,
        LOW_CHAMBER_SCORE,
        HIGH_CHAMBER,
        HIGH_CHAMBER_SCORE,
        WALL_PICKUP,
        SUBMERSIBLE,
        ENDED
    }
    companion object {
        val gear_target = AtomicReference(0.0)
        val slide_target = AtomicReference(0.0)
        val grav = AtomicReference(false)
        val free_slide = AtomicReference(false)
    }
}
