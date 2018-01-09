package com.agh.wfiis.piase.inz.utils;

import android.animation.ArgbEvaluator;
import android.util.Log;

/**
 * Created by piase on 2017-12-20.
 */

public class ColorGenerator {

    private static Integer START_COLOR;
    private static double START_THRESHOLD;
    private static Integer END_COLOR;
    private static  double END_THRESHOLD;

    private static void setThresholdColors(Double measurement) {
        for (ColorValue colorValue : ColorValue.values()) {

            if (measurement > ColorValue.VERY_BAD.getThreshold()) {
                START_COLOR = ColorValue.VERY_BAD.getColor();
                START_THRESHOLD = ColorValue.VERY_BAD.getThreshold();
                END_COLOR = ColorValue.VERY_BAD.getColor();
                END_THRESHOLD = ColorValue.VERY_BAD.getThreshold();

                break;
            }

            if (measurement < colorValue.getThreshold()) {
                int index = colorValue.ordinal();
                if (index >= 1) {
                    START_COLOR = ColorValue.values()[index - 1].getColor();
                    START_THRESHOLD = ColorValue.values()[index - 1].getThreshold();
                    END_COLOR = colorValue.getColor();
                    END_THRESHOLD = colorValue.getThreshold();

                } else {
                    END_COLOR = colorValue.getColor();
                    END_THRESHOLD = colorValue.getThreshold();
                    START_COLOR = END_COLOR;
                    START_THRESHOLD = END_THRESHOLD;
                }

                break;
            }
        }

        Log.i("thresholds:", "" + START_THRESHOLD + " " + END_THRESHOLD);

    }

    private static float calculatePercentage(Double measurement) {
        if (START_THRESHOLD == END_THRESHOLD) {
            return 1.0f;
        }
        return (float) ((measurement - START_THRESHOLD) / (END_THRESHOLD - START_THRESHOLD));
    }

    public static String getMeasurementColor(Double measurement){
        setThresholdColors(measurement);
        float percentage = calculatePercentage(measurement);
        Log.i("percentage:", "" + percentage);
        Object evaluate = new ArgbEvaluator().evaluate(percentage, START_COLOR, END_COLOR);

        String measurementColor = String.format("#%06X", 0xFFFFFF &  (int)evaluate);
        Log.i("measurementColor:", "" + measurementColor);

        return measurementColor;
    }
}
