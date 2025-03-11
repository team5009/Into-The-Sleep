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
        bot.path.segment(Point(36.0, 63.0 * linearadjust, "set_first"))
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED) {
            bot.path.wait(100.0)
        }
        /*//PUSH 1
          bot.path.segment(
            Point(32.0, 30.0).setTolerance(4.0).setDeg(-90.0),
            Point(40.0, 30.0 * linearadjust,).setTolerance(4.0).setDeg(-90.0),
            Point(48.0, 30.0 * linearadjust, "push").setTolerance(4.0).setDeg(-90.0),
            Point(9.0, 26.0 * linearadjust).setDeg(-110.0)
        )
        eventListener.states.set(ChamberStates.PUSHED)
        //PUSH 2
        bot.path.segment(
            Point(40.0, 33.0 * linearadjust).setTolerance(4.0).setDeg(-90.0),
            Point(48.0, 15.0 * linearadjust, "push").setTolerance(6.0).setDeg(-90.0),
            Point(9.0, 29.0 * linearadjust).setTolerance(6.0).setDeg(-110.0),
        )
        eventListener.states.set(ChamberStates.PUSHED)
        //PUSH 3
        bot.path.segment(
            Point(52.0, 20.0).setDeg(-179.0),
            Point(52.0, 6.0, "half_push").setTolerance(8.0).setDeg(-179.0),
            Point(24.0, 25.0).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.PUSHED)
        bot.path.wait(500.0)*/
        //PUSH 1
        bot.path.segment(
            Point(32.0, 34.0).setTolerance(4.0).setDeg(-90.0),
            Point(52.0, 26.0 * linearadjust).setTolerance(4.0).setDeg(-90.0),
            Point(61.0, 20.0 * linearadjust, "half_push").setTolerance(4.0).setDeg(-90.0),
            Point(9.0, 20.0 * linearadjust).setDeg(-110.0)
        )
        eventListener.states.set(ChamberStates.PUSHED)
        //PUSH 2
        /*bot.path.segment(
            Point(52.0, 25.0 * linearadjust).setTolerance(4.0).setDeg(-90.0),
            Point(61.0, 15.0 * linearadjust).setTolerance(6.0).setDeg(-90.0),
            Point(9.0, 10.0 * linearadjust).setTolerance(6.0).setDeg(-110.0),
            Point(26.0, 22.0 * linearadjust, "set_pick_up").setTolerance(6.0).setDeg(-179.0)
        )*/
        eventListener.states.set(ChamberStates.PUSHED)

        //PICKUP 1
        bot.path.segment(
            Point(31.0, 24.0).setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 18.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        bot.path.wait(1000.0)
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(31.0, 70.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-170.0),
            Point(31.0, 66.0 * linearadjust).setDeg(-170.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        //PICKUP 2
        bot.path.segment(
            Point(31.0, 18.0, "set_pick_up").setTolerance(5.0).setDeg(-179.0),
            Point(31.0, 22.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(31.0, 74.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 70.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        //PICKUP 3
        bot.path.segment(
            Point(31.0, 18.0, "set_pick_up").setTolerance(5.0).setDeg(-179.0),
            Point(31.0, 22.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(31.0, 78.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 74.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        //PICKUP 4
        bot.path.segment(
            Point(31.0, 18.0).setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 22.0 * linearadjust, "pick_up").setTolerance(4.0).setDeg(-179.0)
        )
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.PICKUP_READY){
            instance.sleep(100)
        }
        bot.path.segment(
            Point(31.0, 82.0 * linearadjust, "set_gear").setTolerance(4.0).setDeg(-179.0),
            Point(31.0, 78.0 * linearadjust).setDeg(-179.0)
        )
        eventListener.states.set(ChamberStates.CLIP_READY)
        while(instance.opModeIsActive() && eventListener.states.get() != ChamberStates.CLIPPED){
            instance.sleep(100)
        }
        //PARK
        bot.path.segment(
            Point(20.0, 36.0, "ascend")
        )
    }
}