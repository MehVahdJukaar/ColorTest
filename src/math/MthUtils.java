package math;

public class MthUtils {

    public static float[] polarToCartesian(float a, float r) {
        float x = r * Mth.cos(a);
        float y = r * Mth.sin(a);
        return new float[]{x, y};
    }

    public static float signedAngleDiff(double to, double from) {
        float x1 = Mth.cos((float) to);
        float y1 = Mth.sin((float) to);
        float x2 = Mth.cos((float) from);
        float y2 = Mth.sin((float) from);
        return (float) Mth.atan2(x1 * y1 - y1 * x2, x1 * x2 + y1 * y2);
    }


    /**
     * Takes angles from 0 to 1
     *
     * @return mean angle
     */
    public static float averageAngles(Float... angles) {
        float x = 0, y = 0;
        for (float a : angles) {
            x += Mth.cos((float) (a * Math.PI * 2));
            y += Mth.sin((float) (a * Math.PI * 2));
        }
        return (float) (Mth.atan2(y, x) / (Math.PI * 2));
    }

    public static double wrapRad(double pValue) {
        double p = Math.PI * 2;
        double d0 = pValue % p;
        if (d0 >= Math.PI) {
            d0 -= p;
        }

        if (d0 < -Math.PI) {
            d0 += p;
        }

        return d0;
    }

    public static float wrapRad(float pValue) {
        float p = (float) (Math.PI * 2);
        float d0 = pValue % p;
        if (d0 >= Math.PI) {
            d0 -= p;
        }

        if (d0 < -Math.PI) {
            d0 += p;
        }

        return d0;
    }

    /**
     * @param rand a rng
     * @param max  maximum value. Has to be >0
     * @param bias positive getValues skew the average towards 0 (has to be from 0 to infinity).
     *             negative toward max (has to be from 0 to negative infinity). Values <= -1 are invalid.
     *             Setting it to 0 is equivalent to rand.nextFloat()*max.
     *             bias = 1 is slightly skewed towards 0 with average 0.38*max
     * @return a number between 0 and max
     * The bias parameters control how much the average is skewed toward 0 or max
     */

    /**
     * Golden ratio
     */
    public static final float PHI = (float) (1 + (Math.sqrt(5d) - 1) / 2f);

}
