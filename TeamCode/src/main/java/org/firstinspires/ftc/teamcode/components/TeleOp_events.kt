package org.firstinspires.ftc.teamcode.components

import ca.helios5009.hyperion.hardware.HyperionMotor
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events_Chambers.ChamberStates
import java.util.concurrent.atomic.AtomicReference


class TeleOp_events (instance: LinearOpMode) {
    val listener = EventListener()

    val arm = Arm_v2(instance)

    val arm_r = HyperionMotor(instance.hardwareMap, "SL")
    val arm_l = HyperionMotor(instance.hardwareMap, "SR")
    val gear_l = HyperionMotor(instance.hardwareMap, "GL")
    val gear_r = HyperionMotor(instance.hardwareMap, "GR")

    init{
        arm_l.motor.direction = DcMotorSimple.Direction.FORWARD
        arm_r.motor.direction = DcMotorSimple.Direction.REVERSE
        gear_l.motor.direction = DcMotorSimple.Direction.REVERSE
        gear_r.motor.direction = DcMotorSimple.Direction.FORWARD

        listener.addListener("Hang"){

            arm_l.power = -1.0
            arm_r.power = -1.0
            delay(2000)
            gear_l.power = -1.0
            gear_r.power = -1.0
            "Hung"
        }
    }
}
