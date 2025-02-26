package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.Color_Sensor
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import java.util.concurrent.atomic.AtomicReference

class Simple_events_Chambers (instance:LinearOpMode, private val arm : Arm_v2) {
    val listener = EventListener()
    val color = My_Color_Sensor(instance)
    val CS = instance.hardwareMap.get(Color_Sensor::class.java, "CS")
    val states = AtomicReference(ChamberStates.START)
    init {
        arm.init_auto()

        listener.addListener("start_sample") {
            arm.wrist_servos(0.96, 0.96)
            Arm_v2.gear_target.set(0.0)
            Arm_v2.grav.set(true)
            arm.go_to_target()
            Arm_v2.grav.set(false)
            while (instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target(gear_is_on = !Arm_v2.grav.get())
            }
            Arm_v2.grav.set(false)
            states.set(ChamberStates.READY)
            "started"
        }
        listener.addListener("set_gear") {
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(-9.0)
            Arm_v2.slide_target.set(8.0)
            arm.wrist_servos(0.34, 0.34)
            "drop_arm"
        }
        listener.addListener("drop_arm") {
            if(instance.opModeIsActive() && states.get() != ChamberStates.CLIP_READY){
                delay(100)
            }
            Arm_v2.gear_target.set(-9.0)
            Arm_v2.slide_target.set(3.0)
            delay(500)
            arm.intake_servos(1.0)
            delay(700)
            Arm_v2.grav.set(false)
            states.set(ChamberStates.CLIPPED)
            "picked_up"
        }
        listener.addListener("push") {
            Arm_v2.gear_target.set(90.0)
            Arm_v2.slide_target.set(7.0)
            arm.wrist_servos(0.96, 0.96)
            "pushing"
        }

        listener.addListener("chamber_up") {
            arm.wrist_servos(0.4,0.4)
            //arm.intake_servos(-0.2)
            Arm_v2.slide_target.set(7.0)
            Arm_v2.gear_target.set(58.0)
            delay(1200)
            Arm_v2.grav.set(true)
            //arm.intake_servos(0.0)
            "chamber_ready"
        }

        listener.addListener("first_score_chamber") {
            Arm_v2.slide_target.set(Arm_v2.slide_target.get() -1.5)
            arm.wrist_servos(0.5,0.5)
            delay(700)
            Arm_v2.slide_target.set(3.0)
            delay(1200)
            /*while(arm.slide.getPosition() / arm.slide_inches_ticks > 4.0) {
                delay(50)
            }*/
            Arm_v2.grav.set(false)
            Arm_v2.slide_target.set(1.0)
            arm.intake_servos(-1.0)
            delay(600)
            arm.intake_servos(0.0)
            "first_score"
        }

        listener.addListener("score_chamber") {
            arm.intake_servos(-0.2)
            Arm_v2.slide_target.set(Arm_v2.slide_target.get() - 5.0)
            delay(800)
            /*while(arm.slide.getPosition() / arm.slide_inches_ticks > 2.0) {
                delay(50)
            }*/
            arm.intake_servos(0.8)
            "score"
        }

        listener.addListener("arm_off") {
            Arm_v2.slide_target.set(4.0)
            Arm_v2.gear_target.set(45.0)
            delay(1000)
            arm.intake_servos(0.2)
            delay(800)
            arm.intake_servos(0.0)
            "disarmed"
        }

        listener.addListener("ready_arm") {
            arm.wrist_servos(0.15, 0.15)
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(60.0)
            delay(1200)
            Arm_v2.gear_target.set(75.0)
            Arm_v2.slide_target.set(2.0)
            while(arm.gear_r.position / arm.gear_degrees_ticks < 65.0) {
                delay(50)
            }
            "arm_hover"
        }

        listener.addListener("_drop_arm") {
            arm.intake_servos(0.8)
            Arm_v2.grav.set(true)
            delay(1500)
            arm.intake_servos(0.0)
            Arm_v2.grav.set(false)
            delay(200)
            "picked_up"
        }

        listener.addListener("arm_up") {
            arm.intake_servos(0.0)
            arm.wrist_servos(0.45,0.45)
            "up_arm"
        }
        listener.addListener("drop") {
            Arm_v2.gear_target.set(55.0)
            delay(1000)
            //arm.intake_servos(-1.0)
            //delay(1000)
            "dropping"
        }
        listener.addListener("drop_done") {
            Arm_v2.gear_target.set(57.0)
            delay(1000)
            Arm_v2.slide_target.set(4.0)
            arm.intake_servos(-0.3)
            Arm_v2.grav.set(true)
            delay(2500)
            arm.intake_servos(-0.8)
            Arm_v2.gear_target.set(47.0)
            arm.intake_servos(0.0)
            Arm_v2.grav.set(false)
            "ahh"
        }
        listener.addListener("lift_down") {
            Arm_v2.gear_target.set(40.0)
            Arm_v2.slide_target.set(3.0)
            "_"
        }
        listener.addListener("ascend") {
            Arm_v2.gear_target.set(55.0)
            "_"
        }
    }
    enum class ChamberStates{
        START,
        READY,
        CLIP_READY,
        CLIPPED,
        PUSH,
        PICKUP,
        PICKUP_READY,
        PARK
    }

}