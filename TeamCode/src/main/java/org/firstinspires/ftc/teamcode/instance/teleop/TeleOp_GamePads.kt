package org.firstinspires.ftc.teamcode.instances.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.components.Arm_v2

class TeleOp_GamePads (private val instance: LinearOpMode) {

    private val gamepad1 = instance.gamepad1
    private val gamepad2 = instance.gamepad2

    val arm = Arm_v2(instance)

    var offset = 0.0

    var armState = Arm_v2.ArmState.ENDED
    val cruise_timer = ElapsedTime()
    var y_pressed = false
    var x_pressed = false
    var a1_pressed = false
    var a2_pressed = false
    var b1_pressed = false
    var b_pressed = false
    var ps1_pressed = false
    var manual_slide = false
    var manual_wrist = false


    fun game_pad_1() {
        if (gamepad1.circle && armState == Arm_v2.ArmState.SUBMERSIBLE && !b1_pressed) {
            Arm_v2.grav.set(true)
            b1_pressed = true
        } else if (gamepad1.circle) {
            arm.wrist_servos(0.8, 0.8)
        } else if (!gamepad1.circle && b1_pressed) {
            if (armState == Arm_v2.ArmState.WALL_PICKUP) {
                Arm_v2.grav.set(false)
                armState = Arm_v2.ArmState.SUBMERSIBLE
            }
            b1_pressed = false
        }

//        if(gamepad1.cross){
//            Arm_v2.grav.set(true)
//        }else{
//            Arm_v2.grav.set(false)
//        }
//        if(gamepad1.square && armState == Arm_v2.ArmState.SUBMERSIBLE){
//            Arm_v2.gear_target.set(60.0)
//            Arm_v2.slide_target.set(7.0)
//        }
        if(gamepad1.cross){
            Arm_v2.gear_target.set(-9.0)
            Arm_v2.slide_target.set(4.0)
        }
        if(gamepad1.ps && !ps1_pressed && armState == Arm_v2.ArmState.SUBMERSIBLE){
            Arm_v2.grav.set(true)
            arm.wrist_servos(0.8, 0.8)
            ps1_pressed = true
        }else if(!gamepad1.ps && armState == Arm_v2.ArmState.SUBMERSIBLE  && ps1_pressed){
            Arm_v2.grav.set(false)
            Arm_v2.gear_target.set(60.0)
            Arm_v2.slide_target.set(7.0)
            ps1_pressed = false
        }

        if(gamepad1.square) {
            arm.wrist_servos(0.5,0.5)
        }

        // HIGH CHAMBER CLIP
        if (gamepad1.triangle) {
            Arm_v2.gear_target.set(-9.0 + offset)
            Arm_v2.slide_target.set(3.0)
            armState = Arm_v2.ArmState.CRUISE
        }

        // ARM DOWN
//        if (armState == Arm_v2.ArmState.SUBMERSIBLE && gamepad1.a && !gamepad1.start && !a1_pressed) {
//            Arm_v2.grav.set(true)
//            arm.wrist_servos(0.45, 0.45)
//            a1_pressed = true
//        } else if (gamepad1.cross && !gamepad1.start && !a1_pressed) {
//            Arm_v2.grav.set(true)
//            a1_pressed = true
//        } else if (!gamepad1.cross && !gamepad1.start && armState == Arm_v2.ArmState.SUBMERSIBLE && a1_pressed) {
//            Arm_v2.grav.set(false)
//            a1_pressed = false
//        } else if (!gamepad1.cross && a1_pressed && !gamepad1.start) {
//            a1_pressed = false
//            Arm_v2.grav.set(false)
//        }
//        if(gamepad1.triangle && armState == Arm_v2.ArmState.SUBMERSIBLE){
//            arm.wrist_servos(0.1, 0.1 )
//        }

        // MANUAL GEAR MOVEMENT
//        if (gamepad1.right_bumper && !Arm_v2.grav.get()) {
//            //Arm_v2.slide_target.set(0.7)
//            arm.slide_l.power = 0.9
//            arm.slide_r.power = 0.9
//            //Arm_v2.gear_target.set(Range.clip(Arm_v2.gear_target.get() + 0.1, 20.0, 125.0))
//        } else if (gamepad1.left_bumper && !Arm_v2.grav.get()) {
//            //Arm_v2.gear_target.set(Range.clip(Arm_v2.gear_target.get() - 0.1, 20.0, 125.0))
//            //Arm_v2.slide_target.set(-0.7)
//            arm.slide_r.power = -0.9
//            arm.slide_l.power = -0.9
//        }else {
//            //Arm_v2.slide_target.set(0.0)
//            arm.slide_r.power = 0.0
//            arm.slide_l.power = 0.0
//
//        }
    }

    fun game_pad_2() {
        // NET ZONE
        if (gamepad2.y && !y_pressed) {
            if (armState == Arm_v2.ArmState.LOW_BASKET) {
                //high basket
                Arm_v2.gear_target.set(-9.0 + offset)
                Arm_v2.slide_target.set(24.0)
                Arm_v2.grav.set(false)
                arm.wrist_servos(0.34, 0.34)
                manual_slide = true
                armState = Arm_v2.ArmState.HIGH_BASKET
            } else if (armState == Arm_v2.ArmState.CRUISE) {
                //low basket
                Arm_v2.gear_target.set(-9.0 + offset)
                Arm_v2.slide_target.set(8.0)
                Arm_v2.grav.set(false)
                arm.wrist_servos(0.34, 0.34)
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
            Arm_v2.slide_target.set(7.0)
            arm.wrist_servos(0.8, 0.8)
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

        // CHAMBERS
//        if (gamepad2.x && !x_pressed && armState != Arm_v2.ArmState.HIGH_BASKET) {
//            //high chamber for specimens
//            if (armState == Arm_v2.ArmState.HIGH_CHAMBER) {
//                //high chamber scoring
//                Arm_v2.gear_target.set(65.0 + offset)
//                Arm_v2.slide_target.set(1.0)
//                armState = Arm_v2.ArmState.HIGH_CHAMBER_SCORE
//            } else {
//                //high chamber
//                Arm_v2.gear_target.set(55.0 + offset)
//                Arm_v2.slide_target.set(8.5)
//                arm.wrist_servos(0.4, 0.4)
//                armState = Arm_v2.ArmState.HIGH_CHAMBER
//            }
//            Arm_v2.free_slide.set(false)
//            Arm_v2.grav.set(false)
//            x_pressed = true
//        } else if (!gamepad2.x && x_pressed) {
//            x_pressed = false
//        }

        // LOW CHAMBERS
//        if (gamepad2.b && !gamepad2.start && !b_pressed && armState != Arm_v2.ArmState.HIGH_BASKET) {
//            //high chamber for specimens
//            if (armState == Arm_v2.ArmState.LOW_CHAMBER) {
//                //low chamber score
//                Arm_v2.gear_target.set(100.0 + offset)
//                Arm_v2.slide_target.set(0.4)
//                Arm_v2.ArmState.LOW_CHAMBER_SCORE
//            } else {
//                //low chamber
//                Arm_v2.gear_target.set(95.0 + offset)
//                Arm_v2.slide_target.set(5.0)
//                arm.wrist_servos(0.45, 0.45)
//                armState = Arm_v2.ArmState.LOW_CHAMBER
//            }
//            Arm_v2.free_slide.set(false)
//            Arm_v2.grav.set(false)
//            b_pressed = true
//        } else if (!gamepad2.b && b_pressed) {
//            b_pressed = false
//        }

        // HANGING
        /*if(gamepad2.left_stick_y > 0.5) {
            hang_arm.setPowerWithTol(-1.0)  //lift robot
        } else if(gamepad2.left_stick_y < -0.5) {
            hang_arm.setPowerWithTol(0.6)   //raise hook
        } else {
            hang_arm.setPowerWithTol(0.0)
        }*/

        // INTAKE SERVOS
        if (gamepad2.right_bumper) {
            arm.intake_servos(-1.0)  //outake
        } else if (gamepad2.left_bumper) {
            arm.intake_servos(1.0)
        } else {
            arm.intake_servos(0.0)
        }

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