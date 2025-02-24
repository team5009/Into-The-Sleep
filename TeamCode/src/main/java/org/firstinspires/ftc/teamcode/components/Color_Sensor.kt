package org.firstinspires.ftc.teamcode.components

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.Light
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.SwitchableLight
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit


@TeleOp(name = "CS", group = "Sensor")
class Color_Sensor : LinearOpMode() {
    /** The colorSensor field will contain a reference to our color sensor hardware object  */
    var colorSensor: NormalizedColorSensor? = null

    /** The relativeLayout field is used to aid in providing interesting visual feedback
     * in this sample application; you probably *don't* need this when you use a color sensor on your
     * robot. Note that you won't see anything change on the Driver Station, only on the Robot Controller.  */
    var relativeLayout: View? = null

    /*
   * The runOpMode() method is the root of this OpMode, as it is in all LinearOpModes.
   * Our implementation here, though is a bit unusual: we've decided to put all the actual work
   * in the runSample() method rather than directly in runOpMode() itself. The reason we do that is
   * that in this sample we're changing the background color of the robot controller screen as the
   * OpMode runs, and we want to be able to *guarantee* that we restore it to something reasonable
   * and palatable when the OpMode ends. The simplest way to do that is to use a try...finally
   * block around the main, core logic, and an easy way to make that all clear was to separate
   * the former from the latter in separate methods.
   */
    override fun runOpMode() {
        // Get a reference to the RelativeLayout so we can later change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.

        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier(
            "RelativeLayout",
            "id",
            hardwareMap.appContext.packageName
        )
        relativeLayout = (hardwareMap.appContext as Activity).findViewById(relativeLayoutId)

        try {
            runSample() // actually execute the sample
        } finally {
            // On the way out, *guarantee* that the background is reasonable. It doesn't actually start off
            // as pure white, but it's too much work to dig out what actually was used, and this is good
            // enough to at least make the screen reasonable again.
            // Set the panel back to the default color
        }
    }

    protected fun runSample() {
        // You can give the sensor a gain value, will be multiplied by the sensor's raw value before the
        // normalized color values are calculated. Color sensors (especially the REV Color Sensor V3)
        // can give very low values (depending on the lighting conditions), which only use a small part
        // of the 0-1 range that is available for the red, green, and blue values. In brighter conditions,
        // you should use a smaller gain than in dark conditions. If your gain is too high, all of the
        // colors will report at or near 1, and you won't be able to determine what color you are
        // actually looking at. For this reason, it's better to err on the side of a lower gain
        // (but always greater than  or equal to 1).
        var gain = 2f

        // Once per loop, we will update this hsvValues array. The first element (0) will contain the
        // hue, the second element (1) will contain the saturation, and the third element (2) will
        // contain the value. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
        // for an explanation of HSV color.
        val hsvValues = FloatArray(3)

        // xButtonPreviouslyPressed and xButtonCurrentlyPressed keep track of the previous and current
        // state of the X button on the gamepad
        var xButtonPreviouslyPressed = false
        var xButtonCurrentlyPressed = false

        // Get a reference to our sensor object. It's recommended to use NormalizedColorSensor over
        // ColorSensor, because NormalizedColorSensor consistently gives values between 0 and 1, while
        // the values you get from ColorSensor are dependent on the specific sensor you're using.
        colorSensor = hardwareMap.get(NormalizedColorSensor::class.java, "CS")

        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (colorSensor is SwitchableLight) {
            (colorSensor as SwitchableLight).enableLight(false)
        }

        // Wait for the start button to be pressed.
        waitForStart()

        // Loop until we are asked to stop
        while (opModeIsActive()) {
            // Explain basic gain information via telemetry
            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n")
            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n")

            // Update the gain value if either of the A or B gamepad buttons is being held
            if (gamepad1.a) {
                // Only increase the gain by a small amount, since this loop will occur multiple times per second.
                gain += 0.005.toFloat()
            } else if (gamepad1.b && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
                gain -= 0.005.toFloat()
            }

            // Show the gain value via telemetry
            telemetry.addData("Gain", gain)

            // Tell the sensor our desired gain value (normally you would do this during initialization,
            // not during the loop)
            (colorSensor as NormalizedColorSensor).setGain(gain)

            // Check the status of the X button on the gamepad
            xButtonCurrentlyPressed = gamepad1.x

            // If the button state is different than what it was, then act
            if (gamepad1.x) {
                // If the button is (now) down, then toggle the light
                        val light = colorSensor as SwitchableLight
                        light.enableLight(!light.isLightOn)
            }
            xButtonPreviouslyPressed = xButtonCurrentlyPressed

            // Get the normalized colors from the sensor
            val colors = (colorSensor as NormalizedColorSensor).getNormalizedColors()

            /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
       * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
       * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
       * for an explanation of HSV color. */

            // Update the hsvValues array by passing it to Color.colorToHSV()
            Color.colorToHSV(colors.toColor(), hsvValues)

            telemetry.addLine()
                .addData("Red", "${colors.red}")
                .addData("Green", "${colors.green}")
                .addData("Blue", "${colors.blue}")
            telemetry.addLine()
                .addData("Hue", "${hsvValues[0]}")
                .addData("Saturation", "${hsvValues[1]}")
                .addData("Value", "${hsvValues[2]}")
            telemetry.addData("Alpha", "${colors.alpha}")

            /* If this color sensor also has a distance sensor, display the measured distance.
       * Note that the reported distance is only useful at very close range, and is impacted by
       * ambient light and surface reflectivity. */
            if (colorSensor is DistanceSensor) {
                telemetry.addData(
                    "Distance (cm)",
                    "${(colorSensor as DistanceSensor).getDistance(DistanceUnit.CM)}"

                )
            }


            telemetry.update()

            // Change the Robot Controller's background color to match the color detected by the color sensor.
            relativeLayout!!.post {
                relativeLayout!!.setBackgroundColor(
                    Color.HSVToColor(
                        hsvValues
                    )
                )
            }
        }
    }
}