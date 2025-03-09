package org.firstinspires.ftc.teamcode.instance.auto

import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import org.firstinspires.ftc.teamcode.components.Selector
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events

class Auto_Sample_Quals (private val instance : LinearOpMode, private val arm : Arm_v2) {
    fun run(timer: ElapsedTime, s:Selector) {
        val eventListener = Simple_events(instance, s, arm)
        val bot = Robot(instance, eventListener.listener, true)
        val linearadjust = 1.0
        bot.path.start(Point(11.0, 110.0 * linearadjust, "start_sample").setDeg(0.0))//start
        .segment(Point(21.0,110.0 * linearadjust, "set_gear").setTolerance(4.0)
                ,Point(20.5,125.5 * linearadjust, "arm_up").setTolerance(5.0).setDeg(-45.0)
                ,Point(22.5,128.0 * linearadjust).setDeg(-45.0)
        )
        eventListener.state.set(Simple_events.AutoStates.DROP_READY)
        while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PLACE){
            bot.path.wait(100.0)
        }
        //SILVER 5 SAMPLE
        if(s.is_silver == Selector.silver.YES){
            bot.path.segment(
                Point(21.5, 94.5 * linearadjust, "lift_down").setTolerance(3.0).setDeg(-105.0),
                Point(18.0, 88.0 * linearadjust).setDeg(-105.0)
            )
            eventListener.state.set(Simple_events.AutoStates.PICKUP_READY)
            while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PICKUP){
                bot.path.wait(100.0)
            }
            bot.path.segment(Point(28.5,100.5 * linearadjust, "arm_up").setTolerance(5.0).setDeg(-45.0)
                ,Point(21.5,125.5 * linearadjust).setTolerance(5.0).setDeg(-45.0)
                ,Point(22.5,128.0 * linearadjust).setDeg(-45.0)
            )
            eventListener.state.set(Simple_events.AutoStates.DROP_READY)
            while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PLACE){
                bot.path.wait(100.0)
            }
        }
//      //FIRST SAMPLE
        bot.path.segment(Point(27.5, 116.0, "lift_down").setTolerance(4.0).setDeg(-5.0)
                ,Point(24.5, 119.5 * linearadjust)
        )
        eventListener.state.set(Simple_events.AutoStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PICKUP){
            bot.path.wait(100.0)
        }
        //.segment(Point(19.0,124.0 * linearadjust,-80.0, "drop_done"))
        //.wait("picked_up") //pickup 1
        bot.path.segment(Point(21.5,122.5 * linearadjust, "arm_up").setTolerance(5.0).setDeg(-38.0)
            ,Point(19.5,125.0 * linearadjust).setDeg(-38.0)
        )
        eventListener.state.set(Simple_events.AutoStates.DROP_READY)
        while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PLACE){
            bot.path.wait(100.0)
        }
        //SECOND SAMPLE
        bot.path.segment(Point(26.5, 132.0 * linearadjust, "lift_down").setTolerance(4.0).setDeg(-10.0)
                ,Point(24.5, 128.0 * linearadjust).setDeg(0.0)
        )
        eventListener.state.set(Simple_events.AutoStates.PICKUP_READY)
        while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PICKUP){
            bot.path.wait(100.0)
        }
        //.segment(Point(19.0,124.0 * linearadjust,-80.0, "drop_done"))
        //.wait("picked_up") //pickup 2
        bot.path.segment(Point(21.5,122.5 * linearadjust, "arm_up").setTolerance(5.0).setDeg(-38.0)
            ,Point(19.5,125.0 * linearadjust).setDeg(-38.0)
        )
        eventListener.state.set(Simple_events.AutoStates.DROP_READY)
        while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PLACE){
            bot.path.wait(100.0)
        }
        //THIRD SAMPLE
        if(timer.seconds() < 27.0) {
            bot.path.segment(Point(29.5,133.0 * linearadjust, "lift_down").setTolerance(5.0).setDeg(10.0)
                        ,Point(28.0,128.0 * linearadjust).setDeg(30.0)
            )
            eventListener.state.set(Simple_events.AutoStates.PICKUP_READY)
            while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PICKUP){
                bot.path.wait(100.0)
            }
                //.wait("_picked_up") //pickup 3
            bot.path.segment(Point(21.5,122.5 * linearadjust, "arm_up").setTolerance(5.0).setDeg(-38.0)
                ,Point(19.5,125.0 * linearadjust).setDeg(-38.0)
            )
            eventListener.state.set(Simple_events.AutoStates.DROP_READY)
            while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PLACE){
                bot.path.wait(100.0)
            }
            //.wait("_up_arm")
            //.wait("_dropped", "_drop_sample")
        }
        eventListener.state.set(Simple_events.AutoStates.PARK)
        if(s.is_silver == Selector.silver.NO && timer.seconds() < 26.0){
            bot.path.segment(
             Point(62.0,115.0 * linearadjust, "lift_down_final").setTolerance(12.0).setDeg(-80.0)
            ,Point(60.0, 93.0 * linearadjust, "sub_pick_up").setTolerance(8.0).setDeg(-80.0)
            ,Point(60.0, 98.0 * linearadjust).setDeg(-80.0))
            eventListener.state.set(Simple_events.AutoStates.SUB_PICK)
            while (instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PICKUP){
                bot.path.wait(100.0)
            }
            bot.path.segment(Point(17.5,122.5 * linearadjust, "arm_up").setTolerance(4.0).setDeg(-45.0)
                ,Point(17.5,123.0 * linearadjust).setDeg(-45.0)
            )
            eventListener.state.set(Simple_events.AutoStates.DROP_READY)
            while(instance.opModeIsActive() && eventListener.state.get() != Simple_events.AutoStates.PLACE){
                bot.path.wait(100.0)
            }
        }
        if(timer.seconds() < 28){
            bot.path.segment(
                Point(62.0,115.0 * linearadjust, "lift_down_final").setTolerance(12.0).setDeg(-80.0)
                ,Point(60.0, 93.0 * linearadjust, "ascend").setTolerance(8.0).setDeg(-80.0)
                ,Point(60.0, 98.0 * linearadjust).setDeg(-80.0)
            )
        }else {
            bot.path.wait("_", "lift_down_!time")
        }
        eventListener.state.set(Simple_events.AutoStates.SUB_PICK)
        bot.path.end()
    }
}