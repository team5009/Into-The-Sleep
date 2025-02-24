package org.firstinspires.ftc.teamcode.components

import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class My_Color_Sensor (private val instance: LinearOpMode) {

    val CS = instance.hardwareMap.get(Color_Sensor::class.java, "CS")

    fun sensor() : String{
        val hsvValues = FloatArray(3)
        val colors = (CS as NormalizedColorSensor).getNormalizedColors()
        Color.colorToHSV(colors.toColor(), hsvValues)

        if(hsvValues[0] > 5.0 && hsvValues[0] < 20.0){
            return "RED"
        }else if(hsvValues[0] > 60.0 && hsvValues[0] < 90.0){
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