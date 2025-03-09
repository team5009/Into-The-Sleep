package org.firstinspires.ftc.teamcode.components

import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class My_Color_Sensor (private val instance: LinearOpMode) {

    var CS : NormalizedColorSensor
    init {
        CS = Java_Color_Sensor().get("CS", instance.hardwareMap)
    }
    fun sensor() : String{
        val hsvValues = FloatArray(3)
        val colors = CS.getNormalizedColors()
        Color.colorToHSV(colors.toColor(), hsvValues)

        if(hsvValues[0] > 5.0 && hsvValues[0] < 30.0){
            return "RED"
        }else if(hsvValues[0] > 55.0 && hsvValues[0] < 95.0){
            return "YELLOW"
        }else if(hsvValues[0] > 210.0 && hsvValues[0] < 250.0){
            return "BLUE"
        }else {
            return "NOTHING"
        }
    }
    fun dist() : Double{
        return (CS as DistanceSensor).getDistance(DistanceUnit.CM)
    }
}