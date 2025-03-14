package org.firstinspires.ftc.teamcode.instance.auto

import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.Selector
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events_Chambers
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events_Chambers.ChamberStates

class Auto_Chamber_Champ (private val instance : LinearOpMode, private val arm : Arm_v2) {

    fun run(timer: ElapsedTime, s : Selector) {
        val eventListener = Simple_events_Chambers(instance, s, arm)
        val bot = Robot(instance, eventListener.listener, true)
        val linearadjust = 1.0
        // START
        bot.path.start(Point(11.0, 63.0 * linearadjust, "start_sample").setDeg(0.0))
        .wait(500.0)
        bot.path.segment(
            Point(26.0, 63.0 * linearadjust, "set_first").setTolerance(2.0),
            Point(31.0, 63.0 * linearadjust)
            )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED) {
            bot.path.wait(100.0)
        }
        //PUSH 1
        bot.path.segment(
            Point(23.0, 36.0).setTolerance(5.0).setDeg(-90.0),
            Point(50.0, 36.0 * linearadjust).setTolerance(4.0).setDeg(-90.0),
            Point(55.0, 25.0 * linearadjust, "_half_push").setTolerance(4.0).setDeg(-90.0),
            Point(22.0, 27.0 * linearadjust).setDeg(-70.0)
        )
        eventListener.states.set(ChamberStates.PUSHED)

        //PICKUP 1
        bot.path.segment(
            Point(22.0, 24.0, "set_pick_up").setTolerance(4.0).setDeg(-130.0),
            Point(22.0, 24.0 * linearadjust, "pick_up").setDeg(-130.0)
        )
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(33.0, 66.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(36.0, 66.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        //PICKUP 2
        bot.path.segment(
            Point(28.0, 24.0, "set_pick_up").setTolerance(5.0).setDeg(-179.0),
            Point(28.0, 26.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(33.0, 69.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(36.0, 69.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        //PICKUP 3
        bot.path.segment(
            Point(28.0, 24.0, "set_pick_up").setTolerance(5.0).setDeg(-179.0),
            Point(28.0, 26.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(33.0, 72.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(36.0, 72.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        /*
        //PICKUP 4
        bot.path.segment(
            Point(31.0, 20.0).setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 22.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(31.0, 82.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 78.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }*/
        if(timer.seconds() < 26.5 && s.is_silver == Selector.silver.YES) {
            // PICKUP YELLOW
            bot.path.segment(
                Point(31.0, 20.0).setTolerance(4.0).setDeg(-179.0),
                Point(31.0, 22.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
            )
            eventListener.states.set(ChamberStates.PICKUP_READY)
            while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
                instance.sleep(100)
            }
            bot.path.segment(Point(28.5,90.5 * linearadjust, "set_gear_yellow").setTolerance(5.0).setDeg(-90.0)
                ,Point(21.5,130.5 * linearadjust, "raise_yellow").setTolerance(5.0).setDeg(-45.0)
                ,Point(22.5,128.0 * linearadjust).setDeg(-45.0)
            )
            eventListener.states.set(ChamberStates.CLIP_READY)
            while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
                instance.sleep(100)
            }
        } else if (timer.seconds() < 28.5) {
            bot.path.segment(
                Point(31.0, 20.0).setTolerance(4.0).setDeg(-179.0),
                Point(31.0, 22.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
            )
            eventListener.states.set(ChamberStates.PICKUP_READY)
            while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
                instance.sleep(100)
            }
        }
        //PARK
        bot.path.segment(
            Point(20.0, 36.0, "ascend")
        )
    }
}