package org.firstinspires.ftc.teamcode.components

import ca.helios5009.hyperion.core.PIDFController
import ca.helios5009.hyperion.hardware.HyperionMotor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.PidGear
import org.firstinspires.ftc.teamcode.PidSlide
import java.util.HashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.reflect.typeOf

class Arm_v2(private val instance: LinearOpMode) {

    val safety = 0.0

    val pid_gear = PIDFController(PidGear.kP, PidGear.kI, PidGear.kD, PidGear.kF)
    val pid_slide = PIDFController(PidSlide.kP, PidSlide.kI, PidSlide.kD, PidSlide.kF)

    val wrist_position = 5.5/3.3*360.0
    val gear_degrees_ticks = (150.0*384.5)/19.0/360.0
    var cur_gear_target  = 0.0
    val distance_limit = 10.0
    val gear_ratio = 10.0/14.0
    val slide_inches_ticks = 384.5*25.4/30.0/ PI
    var cur_slide_target = 0.0
    val fow_ext = 10.0
    val back_ext = 15.0

    val gear_r = HyperionMotor(instance.hardwareMap, "GR")
    val gear_l = HyperionMotor(instance.hardwareMap, "GL")
    val slide_l = HyperionMotor(instance.hardwareMap, "SR")
    val slide_r = HyperionMotor(instance.hardwareMap, "SL")

    val CS = instance.hardwareMap.get(Color_Sensor::class.java, "CS")

    val left_wrist = instance.hardwareMap.get(Servo::class.java, "Left_Wrist")
    val right_wrist = instance.hardwareMap.get(Servo::class.java, "Right_Wrist")
    val right_wrist_encoder = instance.hardwareMap.get(AnalogInput::class.java, "RWE")
    val left_wrist_encoder = instance.hardwareMap.get(AnalogInput::class.java, "LWE")
    val intake_1 = instance.hardwareMap.get(CRServo::class.java, "Intake_1")
    val intake_2 = instance.hardwareMap.get(CRServo::class.java,"Intake_2")

    val angles = HashMap<Double, Double>()
    init {
        gear_r.motor.direction = DcMotorSimple.Direction.FORWARD
        gear_l.motor.direction = DcMotorSimple.Direction.REVERSE
        slide_l.motor.direction = DcMotorSimple.Direction.REVERSE
        slide_r.motor.direction = DcMotorSimple.Direction.FORWARD

        for (i in -30..30) {
            val rad : Double = (i*5.0) * PI / 180.0
            if (i >= 4.0) {
                angles[i * 5.0] = fow_ext + abs( cos(rad) * fow_ext)
            } else if (i <= -4.0) {
                angles[i * 5.0] = back_ext + abs(cos(rad) * back_ext)
            } else {
                angles[i * 5.0] = 300.0
            }
        }
    }
    fun manual_slide(gamepad: Gamepad) {
        if(gamepad.left_bumper){
            slide_r.power = -0.9
            slide_l.power = -0.9
        } else if(gamepad.right_bumper){
            slide_r.power = 0.9
            slide_l.power = 0.9
        } else {
            slide_r.power = 0.0
            slide_l.power = 0.0
        }
    }
    fun init_auto() {
        slide_r.resetEncoder()
        slide_l.resetEncoder()
        gear_l.resetEncoder()
        gear_r.resetEncoder()

        gear_l.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        gear_r.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        val gear_start_pos = -35.0

        slide_target.set(0.0)
        gear_target.set(gear_start_pos)
        cur_gear_target = gear_start_pos

        wrist_servos(0.05,0.05)
        intake_1.power = 0.0
        intake_2.power = 0.0
    }
    fun init_teleOp() {
        gear_target.set(0.0)
        cur_gear_target = gear_target.get()
        slide_target.set(0.0)
        intake_1.power = 0.0
        intake_2.power = 0.0
    }
    fun wrist_pos() : Double {
        return wrist_position
    }
    fun slide_height() : Double {
        return slide_l.position.toDouble()/slide_inches_ticks
    }
    fun gear_angle() : Double {
        return gear_r.position.toDouble()/gear_degrees_ticks
    }

    fun closest_angle(angle : Double):Double {
        if(angle > 120.0) {
            return 120.0
        }else if(angle < -120.0){
            return -120.0
        }
        return 5.0 * (round(angle / 5.0))
    }

    fun go_to_target(gear_is_on:Boolean = true, slide_is_on:Boolean = true) {
        var gear_output = 0.0
        var slide_output = 0.0

        angles[closest_angle(gear_target.get())]?.let {
            slide_target.set(min(slide_target.get(), it))
        }

        gear_output = pid_gear.directCalculate(gear_target.get() - gear_angle())
        slide_output = pid_slide.directCalculate(slide_target.get() - slide_height())

        angles[closest_angle(gear_angle())]?.let {
            if(slide_target.get() > slide_height() && slide_height() > it) {
                slide_output = 0.0
            }else if(slide_height() > it){
                gear_output = 0.0
            }
        }

        instance.telemetry.addData("Error Slide", slide_target.get() - slide_height())

//        slide_output *= if(slide_output > 0.4) {
//            slide_l.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
//            0.0
//        } else if(slide_output < -0.6) {
//            slide_l.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//            0.8
//        } else {
//            slide_l.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//            0.5
//        }
//
        gear_output +=
            if(gear_angle() < -20.0){
                0.0
            } else if(gear_angle() > 20.0) {
                0.0
            } else {
                0.0
            }


        val gear_offset = (gear_r.position - gear_l.position).toDouble() / 200.0
        val slide_offset = (slide_l.position - slide_r.position).toDouble() / 200.0

//        gear_r.setPowerWithTol(gear_output - gear_offset)
//        gear_l.setPowerWithTol(gear_output + gear_offset)
//        slide_l.setPowerWithTol(slide_output - slide_offset)
//        slide_r.setPowerWithTol(slide_output + slide_offset)
        if(gear_is_on){
            if(gear_r.zeroPowerBehavior == DcMotor.ZeroPowerBehavior.FLOAT || gear_l.zeroPowerBehavior == DcMotor.ZeroPowerBehavior.FLOAT) {
                gear_r.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
                gear_l.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            }
            gear_r.setPowerWithTol(gear_output)
            gear_l.setPowerWithTol(gear_output)
        }else {
            gear_r.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            gear_l.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            gear_l.power = 0.0
            gear_r.power = 0.0
        }

        slide_l.setPowerWithTol(slide_output)
        slide_r.setPowerWithTol(slide_output)


        instance.telemetry.addData("Error Gear", gear_output)
        instance.telemetry.addData("Error slide", slide_output)
        instance.telemetry.addData("cur_slide_target", cur_slide_target)
        instance.telemetry.addData("cur_gear_target", cur_gear_target)

    }
    fun wrist_servos(left: Double, right: Double){
        left_wrist.position = min(left + safety, 1.0)
        right_wrist.position = min(right + safety, 1.0)
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