package math;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;


public class Mth {
    private static final int BIG_ENOUGH_INT = 1024;
    private static final float BIG_ENOUGH_FLOAT = 1024.0F;
    private static final long UUID_VERSION = 61440L;
    private static final long UUID_VERSION_TYPE_4 = 16384L;
    private static final long UUID_VARIANT = -4611686018427387904L;
    private static final long UUID_VARIANT_2 = Long.MIN_VALUE;
    public static final float PI = (float) Math.PI;
    public static final float HALF_PI = (float) (Math.PI / 2);
    public static final float TWO_PI = (float) (Math.PI * 2);
    public static final float DEG_TO_RAD = (float) (Math.PI / 180.0);
    public static final float RAD_TO_DEG = 180.0F / (float) Math.PI;
    public static final float EPSILON = 1.0E-5F;
    public static final float SQRT_OF_TWO = sqrt(2.0F);
    private static final float SIN_SCALE = 10430.378F;
    private static final float[] SIN = (float[]) make(new float[65536], fs -> {
        for (int ix = 0; ix < fs.length; ++ix) {
            fs[ix] = (float) Math.sin((double) ix * Math.PI * 2.0 / 65536.0);
        }
    });
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
            0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
    };
    private static final double ONE_SIXTH = 0.16666666666666666;
    private static final int FRAC_EXP = 8;
    private static final int LUT_SIZE = 257;
    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
    private static final double[] ASIN_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];

    public static <T> T make(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

    public Mth() {
    }

    public static float sin(float value) {
        return SIN[(int) (value * 10430.378F) & 65535];
    }

    public static float cos(float value) {
        return SIN[(int) (value * 10430.378F + 16384.0F) & 65535];
    }

    public static float sqrt(float value) {
        return (float) Math.sqrt((double) value);
    }

    public static int floor(float value) {
        int i = (int) value;
        return value < (float) i ? i - 1 : i;
    }

    public static int fastFloor(double value) {
        return (int) (value + 1024.0) - 1024;
    }

    public static int floor(double value) {
        int i = (int) value;
        return value < (double) i ? i - 1 : i;
    }

    public static long lfloor(double value) {
        long l = (long) value;
        return value < (double) l ? l - 1L : l;
    }

    public static int absFloor(double value) {
        return (int) (value >= 0.0 ? value : -value + 1.0);
    }

    public static float abs(float value) {
        return Math.abs(value);
    }

    public static int abs(int value) {
        return Math.abs(value);
    }

    public static int ceil(float value) {
        int i = (int) value;
        return value > (float) i ? i + 1 : i;
    }

    public static int ceil(double value) {
        int i = (int) value;
        return value > (double) i ? i + 1 : i;
    }

    public static byte clamp(byte value, byte min, byte max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static double clampedLerp(double start, double end, double delta) {
        if (delta < 0.0) {
            return start;
        } else {
            return delta > 1.0 ? end : lerp(delta, start, end);
        }
    }

    public static float clampedLerp(float start, float end, float delta) {
        if (delta < 0.0F) {
            return start;
        } else {
            return delta > 1.0F ? end : lerp(delta, start, end);
        }
    }

    public static double absMax(double x, double y) {
        if (x < 0.0) {
            x = -x;
        }

        if (y < 0.0) {
            y = -y;
        }

        return x > y ? x : y;
    }

    public static int intFloorDiv(int x, int y) {
        return Math.floorDiv(x, y);
    }


    public static double average(long[] values) {
        long l = 0L;

        for (long m : values) {
            l += m;
        }

        return (double) l / (double) values.length;
    }

    public static boolean equal(float x, float y) {
        return Math.abs(y - x) < 1.0E-5F;
    }

    public static boolean equal(double x, double y) {
        return Math.abs(y - x) < 1.0E-5F;
    }

    public static int positiveModulo(int x, int y) {
        return Math.floorMod(x, y);
    }

    public static float positiveModulo(float numerator, float denominator) {
        return (numerator % denominator + denominator) % denominator;
    }

    public static double positiveModulo(double numerator, double denominator) {
        return (numerator % denominator + denominator) % denominator;
    }

    public static int wrapDegrees(int angle) {
        int i = angle % 360;
        if (i >= 180) {
            i -= 360;
        }

        if (i < -180) {
            i += 360;
        }

        return i;
    }

    public static float wrapDegrees(float value) {
        float f = value % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static double wrapDegrees(double value) {
        double d = value % 360.0;
        if (d >= 180.0) {
            d -= 360.0;
        }

        if (d < -180.0) {
            d += 360.0;
        }

        return d;
    }

    public static float degreesDifference(float start, float end) {
        return wrapDegrees(end - start);
    }

    public static float degreesDifferenceAbs(float start, float end) {
        return abs(degreesDifference(start, end));
    }

    public static float rotateIfNecessary(float rotationToAdjust, float actualRotation, float maxDifference) {
        float f = degreesDifference(rotationToAdjust, actualRotation);
        float g = clamp(f, -maxDifference, maxDifference);
        return actualRotation - g;
    }

    public static float approach(float value, float limit, float stepSize) {
        stepSize = abs(stepSize);
        return value < limit ? clamp(value + stepSize, value, limit) : clamp(value - stepSize, limit, value);
    }

    public static float approachDegrees(float angle, float limit, float stepSize) {
        float f = degreesDifference(angle, limit);
        return approach(angle, angle + f, stepSize);
    }


    public static double getDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Throwable var4) {
            return defaultValue;
        }
    }

    public static double getDouble(String value, double defaultValue, double max) {
        return Math.max(max, getDouble(value, defaultValue));
    }

    public static int smallestEncompassingPowerOfTwo(int value) {
        int i = value - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }

    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int ceillog2(int value) {
        value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) value * 125613361L >> 27) & 31];
    }

    public static int log2(int value) {
        return ceillog2(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static int color(float r, float g, float b) {
        return color(floor(r * 255.0F), floor(g * 255.0F), floor(b * 255.0F));
    }

    public static int color(int r, int g, int b) {
        int i = (r << 8) + g;
        return (i << 8) + b;
    }

    public static int colorMultiply(int firstColor, int secondColor) {
        int i = (firstColor & 0xFF0000) >> 16;
        int j = (secondColor & 0xFF0000) >> 16;
        int k = (firstColor & 0xFF00) >> 8;
        int l = (secondColor & 0xFF00) >> 8;
        int m = (firstColor & 0xFF) >> 0;
        int n = (secondColor & 0xFF) >> 0;
        int o = (int) ((float) i * (float) j / 255.0F);
        int p = (int) ((float) k * (float) l / 255.0F);
        int q = (int) ((float) m * (float) n / 255.0F);
        return firstColor & 0xFF000000 | o << 16 | p << 8 | q;
    }

    public static int colorMultiply(int color, float red, float green, float blue) {
        int i = (color & 0xFF0000) >> 16;
        int j = (color & 0xFF00) >> 8;
        int k = (color & 0xFF) >> 0;
        int l = (int) ((float) i * red);
        int m = (int) ((float) j * green);
        int n = (int) ((float) k * blue);
        return color & 0xFF000000 | l << 16 | m << 8 | n;
    }

    public static float frac(float number) {
        return number - (float) floor(number);
    }

    public static double frac(double number) {
        return number - (double) lfloor(number);
    }

    public static long getSeed(int x, int y, int z) {
        long l = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }


    public static double inverseLerp(double delta, double start, double end) {
        return (delta - start) / (end - start);
    }

    public static float inverseLerp(float delta, float start, float end) {
        return (delta - start) / (end - start);
    }

    public static double atan2(double y, double x) {
        double d = x * x + y * y;
        if (Double.isNaN(d)) {
            return Double.NaN;
        } else {
            boolean bl = y < 0.0;
            if (bl) {
                y = -y;
            }

            boolean bl2 = x < 0.0;
            if (bl2) {
                x = -x;
            }

            boolean bl3 = y > x;
            if (bl3) {
                double e = x;
                x = y;
                y = e;
            }

            double e = fastInvSqrt(d);
            x *= e;
            y *= e;
            double f = FRAC_BIAS + y;
            int i = (int) Double.doubleToRawLongBits(f);
            double g = ASIN_TAB[i];
            double h = COS_TAB[i];
            double j = f - FRAC_BIAS;
            double k = y * h - x * j;
            double l = (6.0 + k * k) * k * 0.16666666666666666;
            double m = g + l;
            if (bl3) {
                m = (Math.PI / 2) - m;
            }

            if (bl2) {
                m = Math.PI - m;
            }

            if (bl) {
                m = -m;
            }

            return m;
        }
    }

    public static float fastInvSqrt(float number) {
        float f = 0.5F * number;
        int i = Float.floatToIntBits(number);
        i = 1597463007 - (i >> 1);
        number = Float.intBitsToFloat(i);
        return number * (1.5F - f * number * number);
    }

    public static double fastInvSqrt(double number) {
        double d = 0.5 * number;
        long l = Double.doubleToRawLongBits(number);
        l = 6910469410427058090L - (l >> 1);
        number = Double.longBitsToDouble(l);
        return number * (1.5 - d * number * number);
    }

    public static float fastInvCubeRoot(float number) {
        int i = Float.floatToIntBits(number);
        i = 1419967116 - i / 3;
        float f = Float.intBitsToFloat(i);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * number);
        return 0.6666667F * f + 1.0F / (3.0F * f * f * number);
    }

    public static int hsvToRgb(float hue, float saturation, float value) {
        int i = (int) (hue * 6.0F) % 6;
        float f = hue * 6.0F - (float) i;
        float g = value * (1.0F - saturation);
        float h = value * (1.0F - f * saturation);
        float j = value * (1.0F - (1.0F - f) * saturation);
        float k;
        float l;
        float m;
        switch (i) {
            case 0:
                k = value;
                l = j;
                m = g;
                break;
            case 1:
                k = h;
                l = value;
                m = g;
                break;
            case 2:
                k = g;
                l = value;
                m = j;
                break;
            case 3:
                k = g;
                l = h;
                m = value;
                break;
            case 4:
                k = j;
                l = g;
                m = value;
                break;
            case 5:
                k = value;
                l = g;
                m = h;
                break;
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }

        int n = clamp((int) (k * 255.0F), 0, 255);
        int o = clamp((int) (l * 255.0F), 0, 255);
        int p = clamp((int) (m * 255.0F), 0, 255);
        return n << 16 | o << 8 | p;
    }

    public static int murmurHash3Mixer(int i) {
        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        return i ^ i >>> 16;
    }

    public static long murmurHash3Mixer(long l) {
        l ^= l >>> 33;
        l *= -49064778989728563L;
        l ^= l >>> 33;
        l *= -4265267296055464877L;
        return l ^ l >>> 33;
    }

    public static double[] cumulativeSum(double... values) {
        double d = 0.0;

        for (double e : values) {
            d += e;
        }

        for (int i = 0; i < values.length; ++i) {
            values[i] /= d;
        }

        for (int i = 0; i < values.length; ++i) {
            values[i] += i == 0 ? 0.0 : values[i - 1];
        }

        return values;
    }


    public static double[] binNormalDistribution(double d, double e, double f, int i, int j) {
        double[] ds = new double[j - i + 1];
        int k = 0;

        for (int l = i; l <= j; ++l) {
            ds[k] = Math.max(0.0, d * StrictMath.exp(-((double) l - f) * ((double) l - f) / (2.0 * e * e)));
            ++k;
        }

        return ds;
    }

    public static double[] binBiModalNormalDistribution(double d, double e, double f, double g, double h, double i, int j, int k) {
        double[] ds = new double[k - j + 1];
        int l = 0;

        for (int m = j; m <= k; ++m) {
            ds[l] = Math.max(
                    0.0,
                    d * StrictMath.exp(-((double) m - f) * ((double) m - f) / (2.0 * e * e)) + g * StrictMath.exp(-((double) m - i) * ((double) m - i) / (2.0 * h * h))
            );
            ++l;
        }

        return ds;
    }

    public static double[] binLogDistribution(double d, double e, int i, int j) {
        double[] ds = new double[j - i + 1];
        int k = 0;

        for (int l = i; l <= j; ++l) {
            ds[k] = Math.max(d * StrictMath.log((double) l) + e, 0.0);
            ++k;
        }

        return ds;
    }

    public static int binarySearch(int min, int max, IntPredicate isTargetBeforeOrAt) {
        int i = max - min;

        while (i > 0) {
            int j = i / 2;
            int k = min + j;
            if (isTargetBeforeOrAt.test(k)) {
                i = j;
            } else {
                min = k + 1;
                i -= j + 1;
            }
        }

        return min;
    }

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static double lerp2(double d, double e, double f, double g, double h, double i) {
        return lerp(e, lerp(d, f, g), lerp(d, h, i));
    }

    public static double lerp3(double d, double e, double f, double g, double h, double i, double j, double k, double l, double m, double n) {
        return lerp(f, lerp2(d, e, g, h, i, j), lerp2(d, e, k, l, m, n));
    }

    public static float catmullrom(float f, float g, float h, float i, float j) {
        return 0.5F * (2.0F * h + (i - g) * f + (2.0F * g - 5.0F * h + 4.0F * i - j) * f * f + (3.0F * h - g - 3.0F * i + j) * f * f * f);
    }

    public static double smoothstep(double d) {
        return d * d * d * (d * (d * 6.0 - 15.0) + 10.0);
    }

    public static double smoothstepDerivative(double d) {
        return 30.0 * d * d * (d - 1.0) * (d - 1.0);
    }

    public static int sign(double x) {
        if (x == 0.0) {
            return 0;
        } else {
            return x > 0.0 ? 1 : -1;
        }
    }

    public static float rotLerp(float delta, float start, float end) {
        return start + delta * wrapDegrees(end - start);
    }

    public static float diffuseLight(float f, float g, float h) {
        return Math.min(f * f * 0.6F + g * g * ((3.0F + g) / 4.0F) + h * h * 0.8F, 1.0F);
    }

    @Deprecated
    public static float rotlerp(float start, float end, float delta) {
        float f = end - start;

        while (f < -180.0F) {
            f += 360.0F;
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return start + delta * f;
    }

    @Deprecated
    public static float rotWrap(double value) {
        while (value >= 180.0) {
            value -= 360.0;
        }

        while (value < -180.0) {
            value += 360.0;
        }

        return (float) value;
    }

    public static float triangleWave(float f, float g) {
        return (Math.abs(f % g - g * 0.5F) - g * 0.25F) / (g * 0.25F);
    }

    public static float square(float value) {
        return value * value;
    }

    public static double square(double value) {
        return value * value;
    }

    public static int square(int value) {
        return value * value;
    }

    public static long square(long value) {
        return value * value;
    }

    public static float cube(float f) {
        return f * f * f;
    }

    public static double clampedMap(double d, double e, double f, double g, double h) {
        return clampedLerp(g, h, inverseLerp(d, e, f));
    }

    public static float clampedMap(float f, float g, float h, float i, float j) {
        return clampedLerp(i, j, inverseLerp(f, g, h));
    }

    public static double map(double d, double e, double f, double g, double h) {
        return lerp(inverseLerp(d, e, f), g, h);
    }

    public static float map(float f, float g, float h, float i, float j) {
        return lerp(inverseLerp(f, g, h), i, j);
    }


    public static int roundToward(int value, int factor) {
        return positiveCeilDiv(value, factor) * factor;
    }

    public static int positiveCeilDiv(int x, int y) {
        return -Math.floorDiv(-x, y);
    }

    public static double lengthSquared(double xDistance, double yDistance) {
        return xDistance * xDistance + yDistance * yDistance;
    }

    public static double length(double xDistance, double yDistance) {
        return Math.sqrt(lengthSquared(xDistance, yDistance));
    }

    public static double lengthSquared(double xDistance, double yDistance, double zDistance) {
        return xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;
    }

    public static double length(double xDistance, double yDistance, double zDistance) {
        return Math.sqrt(lengthSquared(xDistance, yDistance, zDistance));
    }

    public static int quantize(double value, int factor) {
        return floor(value / (double) factor) * factor;
    }

    public static IntStream outFromOrigin(int i, int j, int k) {
        return outFromOrigin(i, j, k, 1);
    }

    public static IntStream outFromOrigin(int i, int j, int k, int l) {
        if (j > k) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "upperbound %d expected to be > lowerBound %d", k, j));
        } else if (l < 1) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "steps expected to be >= 1, was %d", l));
        } else {
            return i >= j && i <= k ? IntStream.iterate(i, lx -> {
                int m = Math.abs(i - lx);
                return i - m >= j || i + m <= k;
            }, m -> {
                boolean bl = m <= i;
                int n = Math.abs(i - m);
                boolean bl2 = i + n + l <= k;
                if (!bl || !bl2) {
                    int o = i - n - (bl ? l : 0);
                    if (o >= j) {
                        return o;
                    }
                }

                return i + n + l;
            }) : IntStream.empty();
        }
    }

    static {
        for (int i = 0; i < 257; ++i) {
            double d = (double) i / 256.0;
            double e = Math.asin(d);
            COS_TAB[i] = Math.cos(e);
            ASIN_TAB[i] = e;
        }
    }
}

