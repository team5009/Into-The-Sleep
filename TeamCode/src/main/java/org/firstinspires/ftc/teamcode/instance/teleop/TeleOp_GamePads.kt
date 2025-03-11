package org.firstinspires.ftc.teamcode.instances.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.components.Arm_v2
import org.firstinspires.ftc.teamcode.components.TeleOp_events

class TeleOp_GamePads (private val instance: LinearOpMode) {

    val eventListener = TeleOp_events(instance)

    private val gamepad1 = instance.gamepad1
    private val gamepad2 = instance.gamepad2

    val arm = Arm_v2(instance)

    var offset = 0.0

    var armState = Arm_v2.ArmState.ENDED
    val cruise_timer = ElapsedTime()
    var y_pressed = false
    var a1_pressed = false
    var a2_pressed = false
    var b1_pressed = false
    var b_pressed = false
    var x_pressed = false
    var manual_slide = false
    var manual_wrist = false
    var dpad_up = false
    var end_game_mode = false
    var ps_pressed = false

    var slide = 0.0

    fun game_pad_1() {
        if (gamepad1.circle && armState == Arm_v2.ArmState.SUBMERSIBLE && !b1_pressed) {
            Arm_v2.grav.set(true)
            b1_pressed = true
        } else if (gamepad1.circle) {
            arm.wrist_servos(0.4, 0.4)
        } else if (!gamepad1.circle && b1_pressed) {
            if (armState == Arm_v2.ArmState.WALL_PICKUP) {
                Arm_v2.grav.set(false)
                armState = Arm_v2.ArmState.SUBMERSIBLE
            }
            b1_pressed = false
        }
        if(gamepad1.cross && !this.x_pressed && armState == Arm_v2.ArmState.SUBMERSIBLE){
            Arm_v2.grav.set(true)
            arm.wrist_servos(0.45, 0.45)
            this.x_pressed = true
        }else if(!gamepad1.cross && armState == Arm_v2.ArmState.SUBMERSIBLE  && this.x_pressed){
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(60.0)
            Arm_v2.slide_target.set(9.0)
            this.x_pressed = false
        }

        // HIGH CHAMBER CLIP
        if (gamepad1.triangle) {
            Arm_v2.gear_target.set(-9.0 + offset)
            Arm_v2.slide_target.set(3.0)
            armState = Arm_v2.ArmState.CRUISE
        }

        // HANG
        if (gamepad1.dpad_down){
            Arm_v2.gear_target.set(-20.0)
            Arm_v2.slide_target.set(12.0)
            arm.wrist_servos(0.45, 0.45)
        }
/*
        if(gamepad1.ps){
            end_game_mode = true
            Arm_v2.grav.set(true)
            while (instance.opModeIsActive() && end_game_mode) {
                if (gamepad1.right_bumper) {
                    arm.gear_r.power = -1.0
                    arm.gear_l.power = -1.0
                } else if (gamepad1.left_bumper) {
                    arm.gear_r.power = 1.0
                    arm.gear_l.power = 1.0
                } else {
                    arm.gear_r.power = 0.0
                    arm.gear_l.power = 0.0
                }
                if (gamepad1.dpad_up && !dpad_up) {
                    arm.slide_r.power = -1.0
                    arm.slide_l.power = -1.0
                } else if (gamepad1.cross){
                    arm.slide_r.power = 0.0
                    arm.slide_l.power = 0.0
                }
                if(gamepad2.start){
                    end_game_mode = false
                    arm.gear_r.power = 0.0
                    arm.gear_l.power = 0.0
                    arm.slide_r.power = 0.0
                    arm.slide_l.power = 0.0
                }
            }
        }else if(gamepad1.ps && ps_pressed){
            end_game_mode = false
            Arm_v2.grav.set(false)
        }


         ARM DOWN
        if (armState == Arm_v2.ArmState.SUBMERSIBLE && gamepad1.a && !gamepad1.start && !a1_pressed) {
            Arm_v2.grav.set(true)
            arm.wrist_servos(0.45, 0.45)
            a1_pressed = true
        } else if (gamepad1.cross && !gamepad1.start && !a1_pressed) {
            Arm_v2.grav.set(true)
            a1_pressed = true
        } else if (!gamepad1.cross && !gamepad1.start && armState == Arm_v2.ArmState.SUBMERSIBLE && a1_pressed) {
            Arm_v2.grav.set(false)
            a1_pressed = false
        } else if (!gamepad1.cross && a1_pressed && !gamepad1.start) {
            a1_pressed = false
            Arm_v2.grav.set(false)
        }
        if(gamepad1.triangle && armState == Arm_v2.ArmState.SUBMERSIBLE){
            arm.wrist_servos(0.1, 0.1 )
        }

         MANUAL GEAR MOVEMENT
        if (gamepad1.right_bumper && !Arm_v2.grav.get()) {
            //Arm_v2.slide_target.set(0.7)
            arm.slide_l.power = 0.9
            arm.slide_r.power = 0.9
            //Arm_v2.gear_target.set(Range.clip(Arm_v2.gear_target.get() + 0.1, 20.0, 125.0))
        } else if (gamepad1.left_bumper && !Arm_v2.grav.get()) {
            //Arm_v2.gear_target.set(Range.clip(Arm_v2.gear_target.get() - 0.1, 20.0, 125.0))
            //Arm_v2.slide_target.set(-0.7)
            arm.slide_r.power = -0.9
            arm.slide_l.power = -0.9
        }else {
            //Arm_v2.slide_target.set(0.0)
            arm.slide_r.power = 0.0
            arm.slide_l.power = 0.0

        }
*/
    }

    fun game_pad_2() {
        // NET ZONE
        if (gamepad2.y && !y_pressed) {
            if (armState == Arm_v2.ArmState.LOW_BASKET) {
                //high basket
                arm.wrist_servos(0.1, 0.1)
                Arm_v2.gear_target.set(-9.0 + offset)
                Arm_v2.slide_target.set(24.0)
                Arm_v2.grav.set(false)

                manual_slide = true
                armState = Arm_v2.ArmState.HIGH_BASKET
            } else if (armState == Arm_v2.ArmState.CRUISE) {
                //low basket
                Arm_v2.gear_target.set(-9.0 + offset)
                Arm_v2.slide_target.set(8.0)
                Arm_v2.grav.set(false)
                arm.wrist_servos(0.1, 0.1)
                manual_slide = false
                armState = Arm_v2.ArmState.LOW_BASKET
            } else {
                //no slide
                if(armState != Arm_v2.ArmState.HIGH_BASKET) {
                    cruise_timer.reset()
                    Arm_v2.grav.set(false)
                } else {
                    Arm_v2.grav.set(false)
                }
                Arm_v2.gear_target.set(-5.0 + offset)
                Arm_v2.slide_target.set(3.0)
                armState = Arm_v2.ArmState.CRUISE
            }
            y_pressed = true
        } else if (!gamepad2.y && y_pressed) {
            if (armState == Arm_v2.ArmState.CRUISE && arm.gear_r.position / arm.gear_degrees_ticks < 30.0) {
                Arm_v2.free_slide.set(true)
                y_pressed = false
            } else if(armState != Arm_v2.ArmState.CRUISE) {
                Arm_v2.free_slide.set(false)
                y_pressed = false
            }
        }

        // PICK UP
        if (gamepad2.a && !gamepad2.start && !a2_pressed && armState != Arm_v2.ArmState.HIGH_BASKET) {
            //sample pick up from submersible
            Arm_v2.gear_target.set(70.0 + offset)
            Arm_v2.slide_target.set(11.0)
            arm.wrist_servos(0.45, 0.45)
            manual_slide = true
            manual_wrist = true
            Arm_v2.free_slide.set(false)
            Arm_v2.grav.set(false)
            armState = Arm_v2.ArmState.SUBMERSIBLE
            a2_pressed = true
            a1_pressed = true
        } else if (!gamepad2.a && a2_pressed) {
            a2_pressed = false
        }

        // Push the samples
        if(gamepad2.dpad_down){
            arm.sweeper(0.0)
        }else if(gamepad2.dpad_up){
            arm.sweeper(0.5)
        }else{
            arm.sweeper(0.75)
        }

        // INTAKE SERVOS
        if (gamepad2.right_bumper) {
            arm.intake_servos(1.0)  //outake
        } else if (gamepad2.left_bumper) {
            arm.intake_servos(-1.0)
        } else {
            arm.intake_servos(0.0)
        }
        //CHAMBERS
        /*if (gamepad2.x && !x_pressed && armState != Arm_v2.ArmState.HIGH_BASKET) {
            //high chamber for specimens
            if (armState == Arm_v2.ArmState.HIGH_CHAMBER) {
                //high chamber scoring
                Arm_v2.gear_target.set(65.0 + offset)
                Arm_v2.slide_target.set(1.0)
                armState = Arm_v2.ArmState.HIGH_CHAMBER_SCORE
            } else {
                //high chamber
                Arm_v2.gear_target.set(55.0 + offset)
                Arm_v2.slide_target.set(8.5)
                arm.wrist_servos(0.4, 0.4)
                armState = Arm_v2.ArmState.HIGH_CHAMBER
            }
            Arm_v2.free_slide.set(false)
            Arm_v2.grav.set(false)
            x_pressed = true
        } else if (!gamepad2.x && x_pressed) {
            x_pressed = false
        }*/

         //LOW CHAMBERS
        /*if (gamepad2.b && !gamepad2.start && !b_pressed && armState != Arm_v2.ArmState.HIGH_BASKET) {
            //high chamber for specimens
            if (armState == Arm_v2.ArmState.LOW_CHAMBER) {
                //low chamber score
                Arm_v2.gear_target.set(100.0 + offset)
                Arm_v2.slide_target.set(0.4)
                Arm_v2.ArmState.LOW_CHAMBER_SCORE
            } else {
                //low chamber
                Arm_v2.gear_target.set(95.0 + offset)
                Arm_v2.slide_target.set(5.0)
                arm.wrist_servos(0.45, 0.45)
                armState = Arm_v2.ArmState.LOW_CHAMBER
            }
            Arm_v2.free_slide.set(false)
            Arm_v2.grav.set(false)
            b_pressed = true
        } else if (!gamepad2.b && b_pressed) {
            b_pressed = false
        }*/



        // WRIST MOVEMENT
        /*if (gamepad2.dpad_up && manual_wrist) {
            if (arm.right_wrist.position != 1.0 && arm.left_wrist.position != 1.0 && arm.right_wrist.position + arm.left_wrist.position < 1.0) {
                arm.right_wrist.position += 0.01
                arm.left_wrist.position += 0.01

            } else if (gamepad2.dpad_down && manual_wrist) {
                if (arm.right_wrist.position != 0.0 && arm.left_wrist.position != 0.0 && arm.right_wrist.position + arm.left_wrist.position > 0.0) {
                    arm.right_wrist.position -= 0.01
                    arm.left_wrist.position -= 0.01
                }
            }
        }*/
        /*if (gamepad2.dpad_left && manual_wrist) {
            if(arm.right_wrist.position != 1.0 && arm.left_wrist.position != 0.0) {
                arm.right_wrist.position += 0.01
                arm.left_wrist.position -= 0.01
            }
        } else if (gamepad2.dpad_right && manual_wrist) {
            if(arm.right_wrist.position != 0.0 && arm.left_wrist.position != 1.0) {
                arm.right_wrist.position -= 0.01
                arm.left_wrist.position += 0.01
            }
        }*/
    }

}