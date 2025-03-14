package org.firstinspires.ftc.teamcode.instance.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.My_Color_Sensor
import org.firstinspires.ftc.teamcode.components.Selector

@Autonomous(name = "Menu", group = "Autos")
class Menu : LinearOpMode() {
    override fun runOpMode() {
        val dashboard = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, dashboard.telemetry)
        val arm = Arm_v2(this)
        val s = Selector(this)
        val color = My_Color_Sensor(this)
        val timer = ElapsedTime()
        while(opModeInInit() && Selector.selectors.entries[s.selector] != Selector.selectors.DONE) {
            s.select()
            s.scroll()
            arm.manual_slide(gamepad1)
            telemetry.addData("Which alliance: ", Selector.alliance.entries[s.alliance_index])
            telemetry.addData("Alliance: ", s.alliance_name)
            telemetry.addData("Path: ", Selector.paths.entries[s.path_index])
            telemetry.addData("Selected Path: ", s.path_name)
            telemetry.addData("Position: ", Selector.positions.entries[s.positions_index])
            telemetry.addData("Selected Position: ", s.positions_names)
            telemetry.addData("Is Silver: ",Selector.silver.entries[s.silver_index])
            telemetry.addData("Silver is: ", s.is_silver)
            telemetry.addData("Delay(ms): ", s.delay)
            telemetry.addData("Selector: ", Selector.selectors.entries[s.selector])
            arm.angles[arm.closest_angle(arm.gear_angle())]?.let {
                telemetry.addData("Min angles", it)
            }
            telemetry.update()
        }
        arm.init_auto()
//        while(opModeInInit()) {
////            arm.go_to_target()
//            if(gamepad1.triangle){
//                arm.gear_l.power = 0.4
//                arm.gear_r.power = 0.4
//            }else if(gamepad1.cross){
//                arm.gear_l.power = -0.4
//                arm.gear_r.power = -0.4
//            }else{
//                if(arm.gear_angle() < -35.0) {
//                    arm.gear_l.power = 0.06
//                    arm.gear_r.power = 0.06
//                }else {
//                    arm.gear_l.power = 0.0
//                    arm.gear_r.power = 0.0
//                }
//            }
//        }

        waitForStart()
//        arm.gear_r.power = 0.5
//        arm.gear_l.power = 0.5
        arm.wrist_servos(0.0,0.0)
        timer.reset()
        sleep(s.delay)

        if(s.path_name == Selector.paths.AUTO_SAMPLE) {
            Auto_Sample_Quals(this, arm).run(timer, s)
        } else if(s.path_name == Selector.paths.AUTO_CHAMBER) {
            Auto_Chamber_Champ(this, arm).run(timer, s)
        }
    }

}
