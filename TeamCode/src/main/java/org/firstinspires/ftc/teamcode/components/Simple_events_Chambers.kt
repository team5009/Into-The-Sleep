package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.Color_Sensor
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import org.firstinspires.ftc.teamcode.components.Selector
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events.AutoStates
import java.util.concurrent.atomic.AtomicReference

class Simple_events_Chambers (instance:LinearOpMode, private val s : Selector, private val arm : Arm_v2) {
    val listener = EventListener()
    val states = AtomicReference(ChamberStates.START)
    val color = My_Color_Sensor(instance)
    val time_out = ElapsedTime()
    var first = true
    init {
        arm.init_auto()
        val alliance = if(s.alliance_name == Selector.alliance.RED){
            "RED"
        }else{
            "BLUE"
        }
        listener.addListener("start_sample") {
            arm.wrist_servos(0.08, 0.08)
            Arm_v2.gear_target.set(0.0)
            delay(500)
            Arm_v2.grav.set(true)
            arm.go_to_target()
            Arm_v2.grav.set(false)
            while (instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target(gear_is_on = !Arm_v2.grav.get())
            }
            Arm_v2.grav.set(false)
            "started"
        }
        listener.addListener("set_first") {
            Arm_v2.gear_target.set(60.0)
            delay(500)
            Arm_v2.gear_target.set(15.0)
            delay(500)
            Arm_v2.slide_target.set(13.0)
            arm.intake_servos(-0.4)
            while(instance.opModeIsActive() && color.dist() < 1.5){
                delay(10)
            }
            arm.intake_servos(0.0)
            arm.wrist_servos(0.4, 0.4)
            "drop_arm"
        }
        listener.addListener("drop_arm") {
            if(instance.opModeIsActive() && states.get() != ChamberStates.CLIP_READY){
                delay(100)
            }
            Arm_v2.grav.set(true)
            Arm_v2.slide_target.set(6.0)
            delay(800)
            arm.intake_servos(-1.0)
            while(instance.opModeIsActive() && color.dist() > 3.0){
                delay(10)
            }
            delay(200)
            Arm_v2.grav.set(false)
            states.set(ChamberStates.CLIPPED)
            "picked_up"
        }
        listener.addListener("push") {
            states.set(ChamberStates.PUSH)
            Arm_v2.gear_target.set(0.0)
            Arm_v2.slide_target.set(6.0)
            arm.sweeper(0.25)
            "un_push"
        }
        listener.addListener("half_push"){
            states.set(ChamberStates.PUSH)
            Arm_v2.gear_target.set(70.0)
            Arm_v2.slide_target.set(8.0)
            arm.sweeper(0.5)
            while (states.get() == ChamberStates.PUSHED){
                delay(10)
            }
            "push"
        }
        listener.addListener("un_push") {
            delay(300)
            while (states.get() == ChamberStates.PUSHED){
                delay(10)
            }
            arm.sweeper(0.0)
            delay(500)
            arm.sweeper(0.75)
            "un_pushed"
        }
        listener.addListener("set_pick_up"){
            Arm_v2.gear_target.set(70.0)
            Arm_v2.slide_target.set(8.0)
            arm.wrist_servos(0.45, 0.45)
            ""
        }
        listener.addListener("pick_up") {
            time_out.reset()
            while(instance.opModeIsActive() && states.get() != ChamberStates.PICKUP_READY && time_out.milliseconds() < 1000.0){
                delay(100)
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
            } while (instance.opModeIsActive() && color.dist() > 3.0 && color.sensor() == alliance)
            delay(100)
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(40.0)
            delay(300)
            states.set(ChamberStates.PICKUP)
            Arm_v2.gear_target.set(0.0)
            Arm_v2.slide_target.set(6.0)
            arm.intake_servos(-0.4)
            while(instance.opModeIsActive() && color.dist() < 1.5){
                delay(10)
            }
            arm.intake_servos(0.0)
            "picked_up"
        }
        listener.addListener("set_gear") {
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(-4.0)
            Arm_v2.slide_target.set(10.0)
            arm.intake_servos(-0.4)
            while(instance.opModeIsActive() && color.dist() < 1.5){
                delay(10)
            }
            arm.intake_servos(0.0)
            arm.wrist_servos(0.08, 0.08)
            while(states.get() == ChamberStates.CLIP_READY){
                delay(10)
            }
            Arm_v2.gear_target.set(-13.0)
            delay(500)
            "drop_arm"
        }
        listener.addListener("lift_down") {
            Arm_v2.gear_target.set(0.0)
            Arm_v2.slide_target.set(7.0)
            "_"
        }
        listener.addListener("ascend") {
            states.set(ChamberStates.PARK)
            Arm_v2.gear_target.set(0.0)
            Arm_v2.slide_target.set(7.0)
            "_"
        }
    }
    enum class ChamberStates{
        START,
        READY,
        CLIP_READY,
        CLIPPED,
        PUSH,
        PUSHED,
        PICKUP,
        PICKUP_READY,
        PARK
    }

}