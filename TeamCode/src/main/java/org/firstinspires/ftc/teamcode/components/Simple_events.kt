package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.Color_Sensor
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import java.util.concurrent.atomic.AtomicReference

class Simple_events (instance:LinearOpMode, private val arm : Arm_v2) {
    val listener = EventListener()
    val state = AtomicReference(AutoStates.START)
    var color = My_Color_Sensor(instance)
    val CS = instance.hardwareMap.get(Color_Sensor::class.java, "CS")
    init {
        listener.addListener("start_sample") {
            arm.wrist_servos(0.25,0.25)
            Arm_v2.gear_target.set(0.0)
            Arm_v2.grav.set(true)
            arm.go_to_target()
            Arm_v2.grav.set(false)
            while(instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target(gear_is_on = !Arm_v2.grav.get())
            }
            Arm_v2.grav.set(false)
            "started"
        }
        listener.addListener("set_gear") {
            Arm_v2.gear_target.set(0.0)
            arm.wrist_servos(0.25,0.25)
            while(arm.gear_angle() < -30.0){
                delay(10)
            }
            Arm_v2.slide_target.set(20.0)
            null
        }
        listener.addListener("arm_up") {
            arm.intake_servos(0.0)
            Arm_v2.gear_target.set(-9.0)
            Arm_v2.slide_target.set(23.0)
            delay(1000)
            while(arm.slide_height() < 19.0){
                delay(50)
            }
            arm.wrist_servos(0.25, 0.25)
            "drop_sample"
        }
        listener.addListener("drop_sample") {
            while(instance.opModeIsActive() && state.get() != AutoStates.DROP_READY){
                delay(100)
            }
            arm.wrist_servos(0.08, 0.08)
            delay(300)
            arm.intake_servos(1.0)
            while (instance.opModeIsActive() && color.dist() < 2.5){
                delay(10)
            }
            arm.wrist_servos(0.5, 0.5)
            delay(200)
            arm.intake_servos(0.0)
            Arm_v2.slide_target.set(7.0)
            delay(1300)
            state.set(AutoStates.PLACE)
            Arm_v2.gear_target.set(40.0)
            delay(500)
            "dropped"
        }
        listener.addListener("lift_down") {
            Arm_v2.gear_target.set(85.0)
            delay(500)
            Arm_v2.grav.set(false)
            //Arm_v2.gear_target.set(100.0)
            "pick_up"
        }
        listener.addListener("lift_down_final") {
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(20.0)
            Arm_v2.slide_target.set(10.0)
            "pick_up"
        }
        listener.addListener("pick_up") {
            while(instance.opModeIsActive() && state.get() != AutoStates.PICKUP_READY){
                delay(100)
            }
            Arm_v2.grav.set(true)
            //arm.wrist_servos(0.45,0.45)
            arm.intake_servos(-1.0)
            while (instance.opModeIsActive() && color.dist() > 2.5){
                delay(10)
            }
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(40.0)
            delay(300)
            state.set(AutoStates.PICKUP)
            arm.intake_servos(0.0)
            "picked_up"
        }
        listener.addListener("ascend") {
            state.set(AutoStates.PARK)
            Arm_v2.gear_target.set(20.0)
            delay(1000)
            Arm_v2.grav.set(true)
            "_"
        }
    }
    enum class AutoStates {
        START,
        PLACE,
        DROP_READY,
        PICKUP,
        PICKUP_READY,
        PARK
    }
}