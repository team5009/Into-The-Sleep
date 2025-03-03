package org.firstinspires.ftc.teamcode.components

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class Selector(private val instance : LinearOpMode){

    var a_pressed = false
    var b_pressed = false
    var d_pad_pressed = false

    var delay = 0L

    var selector = 0
    val path_length = paths.entries.lastIndex + 1
    var path_index = 0
    var path_name = paths.AUTO_SAMPLE

    val positions_length = positions.entries.lastIndex + 1
    var positions_index = 0
    var positions_names = positions.CLOSE

    val silver_length = silver.entries.lastIndex + 1
    var silver_index = 0
    var is_silver = silver.YES

    fun select() {
        if(instance.gamepad1.a && !a_pressed){
            if(selectors.entries[selector] == selectors.PATH_NAME) {
                path_name = paths.entries[path_index]
            }
            a_pressed = true
            selector += 1
        } else if(!instance.gamepad1.a && a_pressed){
            a_pressed = false
        }
        if(instance.gamepad1.b && !b_pressed){
            b_pressed = true
            selector -= 1
        } else if(!instance.gamepad1.b && b_pressed){
            b_pressed = false
        }
    }

    fun scroll() {
        if(selectors.entries[selector] == selectors.PATH_NAME){
            if(instance.gamepad1.dpad_down && !d_pad_pressed){
                d_pad_pressed = true
                path_index = (path_index + 1) % path_length
            } else if(instance.gamepad1.dpad_up && !d_pad_pressed){
                d_pad_pressed = true
                path_index = (path_index - 1) % path_length
            } else if(!instance.gamepad1.dpad_up && !instance.gamepad1.dpad_down && d_pad_pressed) {
                d_pad_pressed = false
            }
        } else if(selectors.entries[selector] == selectors.POSITION){
            if(instance.gamepad1.dpad_down && !d_pad_pressed){
                d_pad_pressed = true
                positions_index = (positions_index + 1) % positions_length
            }else if(instance.gamepad1.dpad_up && !d_pad_pressed){
                d_pad_pressed = true
                positions_index = (positions_length - 1) % positions_length
            }
        }else if(selectors.entries[selector] == selectors.JOINT_SILVER){
            if(instance.gamepad1.dpad_down && !d_pad_pressed){
                d_pad_pressed = true
                silver_index = (silver_index + 1) % silver_length
            }else if(instance.gamepad1.dpad_up && !d_pad_pressed){
                d_pad_pressed = true
                silver_index = (silver_index - 1) % silver_length
            }
        } else if(selectors.entries[selector] == selectors.DELAY) {
            if(instance.gamepad1.dpad_down && !d_pad_pressed){
                d_pad_pressed = true
                delay += 500
            } else if(instance.gamepad1.dpad_up && !d_pad_pressed && delay > 0){
                d_pad_pressed = true
                delay -= 500
            } else if(!instance.gamepad1.dpad_up && !instance.gamepad1.dpad_down && d_pad_pressed) {
                d_pad_pressed = false
            }
        }
    }

    enum class paths {
        AUTO_CHAMBER,
        AUTO_SAMPLE
    }
    enum class positions {
        CLOSE,
        MIDDLE,
        FAR
    }
    enum class silver {
        YES,
        NO
    }
    enum class selectors {
        PATH_NAME,
        POSITION,
        JOINT_SILVER,
        DELAY,
        READY,
        DONE
    }
}