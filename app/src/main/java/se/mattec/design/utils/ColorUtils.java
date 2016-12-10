package se.mattec.design.utils;

import android.graphics.Color;

public class ColorUtils
{

    public static int mixColors(int fromColor, int toColor, float ratio)
    {
        int fromRed = Color.red(fromColor);
        int toRed = Color.red(toColor);
        int diffRed = toRed - fromRed;

        int fromGreen = Color.green(fromColor);
        int toGreen = Color.green(toColor);
        int diffGreen = toGreen - fromGreen;

        int fromBlue = Color.blue(fromColor);
        int toBlue = Color.blue(toColor);
        int diffBlue = toBlue - fromBlue;

        int fromAlpha = Color.alpha(fromColor);
        int toAlpha = Color.alpha(toColor);
        int diffAlpha = toAlpha - fromAlpha;

        return Color.argb(
                (int) (fromAlpha + diffAlpha * ratio),
                (int) (fromRed + diffRed * ratio),
                (int) (fromGreen + diffGreen * ratio),
                (int) (fromBlue + diffBlue * ratio));
    }

}
