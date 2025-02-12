package org.firstinspires.ftc.teamcode.components

import ca.helios5009.hyperion.hardware.HyperionMotor
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events_Chambers.ChamberStates
import java.util.concurrent.atomic.AtomicReference


class TeleOp_events (instance: LinearOpMode, private val armv2 : Arm_v2) {
    val listener = EventListener()
    val arm_r = HyperionMotor(instance.hardwareMap, "SL")
    val arm_l = HyperionMotor(instance.hardwareMap, "SR")
    val gear_l = HyperionMotor(instance.hardwareMap, "GL")
    val gear_r = HyperionMotor(instance.hardwareMap, "GR")

    init{
        listener.addListener("Hang"){
            arm_l.motor.direction = DcMotorSimple.Direction.FORWARD
            arm_r.motor.direction = DcMotorSimple.Direction.REVERSE
            arm_l.power = -1.0
            arm_r.power = -1.0
            gear_l.power = -1.0
            gear_r.power = -1.0
            delay(1000)
            gear_l.power = 1.0
            gear_r.power = 1.0
            delay(1000)
            gear_l.power = -1.0
            gear_r.power = -1.0
            "Hung"
        }
    }
}