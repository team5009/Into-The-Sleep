package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

public class Java_Color_Sensor {
    public NormalizedColorSensor get(String name, HardwareMap hardwareMap) {
        return  hardwareMap.get(NormalizedColorSensor.class, name);
    }
}
