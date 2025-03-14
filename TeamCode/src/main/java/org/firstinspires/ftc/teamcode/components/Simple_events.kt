package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import org.firstinspires.ftc.teamcode.components.Selector
import java.util.concurrent.atomic.AtomicReference

class Simple_events (instance:LinearOpMode, private val s : Selector, private val arm : Arm_v2) {
    val listener = EventListener()
    val state = AtomicReference(AutoStates.START)
    val color = My_Color_Sensor(instance)
    val time_out = ElapsedTime()
    var first = true
    init {
        val alliance = if(s.alliance_name == Selector.alliance.RED){
            "RED"
        }else{
            "BLUE"
        }
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
            Arm_v2.slide_target.set(23.0)
            arm.park_auto(0.4)
            null
        }
        listener.addListener("arm_up") {
            arm.intake_servos(0.0)
            Arm_v2.gear_target.set(-9.0)
            Arm_v2.slide_target.set(24.0)
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
            if(first == true){
                delay(500)
            }
            //delay(100)
            arm.intake_servos(-1.0)
            //delay(500)
            while (instance.opModeIsActive() && color.dist() < 1.5){
                delay(10)
            }
            delay(260)
            arm.wrist_servos(0.45, 0.45)
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
            Arm_v2.slide_target.set(6.0)
            "pick_up"
        }
        listener.addListener("lift_down_!time") {
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(0.0)
            Arm_v2.slide_target.set(6.0)
            "_"
        }
        listener.addListener("pick_up") {
            time_out.reset()
            while(instance.opModeIsActive() && state.get() != AutoStates.PICKUP_READY && time_out.milliseconds() < 1000.0){
                delay(100)
            }
            if(first == true && s.is_silver == Selector.silver.YES){
                delay(800)
                first = false
            }
            Arm_v2.grav.set(true)
            //arm.wrist_servos(0.45,0.45)
            arm.intake_servos(1.0)
            //delay(900)
            time_out.reset()
            do {
                while (instance.opModeIsActive() && color.dist() > 2.5 && time_out.milliseconds() < 900.0){
                    delay(10)
                }
            } while (instance.opModeIsActive() && color.dist() > 3.0 && time_out.milliseconds() < 900.0 && (color.sensor() == "YELLOW" || color.sensor() == alliance))
            delay(100)
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(40.0)
            delay(300)
            state.set(AutoStates.PICKUP)
            arm.intake_servos(0.0)
            "picked_up"
        }
        listener.addListener("ascend") {
            state.set(AutoStates.PARK)
            arm.park_auto(0.5)
            Arm_v2.gear_target.set(20.0)
            Arm_v2.slide_target.set(8.0)
            "_"
        }
        listener.addListener("sub_lift_down"){
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(65.0)
            Arm_v2.slide_target.set(7.0)
            "_"
        }
        listener.addListener("sub_pick_up") {
            Arm_v2.grav.set(false)
            while (instance.opModeIsActive() && state.get() != AutoStates.SUB_PICK_READY){
                delay(10)
            }
            arm.sweeper(0.45)
            delay(500)
            arm.sweeper(0.75)
            arm.intake_servos(1.0)
            Arm_v2.grav.set(true)
            time_out.reset()
            do {
                while (instance.opModeIsActive() && color.dist() > 2.5 && time_out.milliseconds() < 900.0){
                    delay(10)
                }
            } while (instance.opModeIsActive()
                && color.dist() > 2.5
                && time_out.milliseconds() > 200.0
                && (color.sensor() == "YELLOW" || color.sensor() == alliance)
            )
            //TRY AGAIN
            if(color.sensor() != "YELLOW" || color.sensor() != alliance) {
                arm.intake_servos(-1.0)
                Arm_v2.grav.set(false)
                delay(1000)
                arm.intake_servos(1.0)
                Arm_v2.grav.set(true)
                time_out.reset()
                do {
                    while (instance.opModeIsActive() && color.dist() > 2.5 && time_out.milliseconds() < 900.0) {
                        delay(10)
                    }
                } while (instance.opModeIsActive()
                    && color.dist() > 2.5
                    && time_out.milliseconds() > 200.0
                    && (color.sensor() == "YELLOW" || color.sensor() == alliance)
                )
                if(color.sensor() != "YELLOW" || color.sensor() != alliance){
                    arm.intake_servos(-1.0)
                    Arm_v2.grav.set(false)
                }
            }
            arm.intake_servos(0.0)
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(65.0)
            state.set(AutoStates.SUB_PICK)
            "sub_picked_up"
        }
    }
    enum class AutoStates {
        START,
        PLACE,
        DROP_READY,
        PICKUP,
        PICKUP_READY,
        PARK,
        SUB_PICK,
        SUB_PICK_READY,
        NOT_ALLIANCE
    }
}