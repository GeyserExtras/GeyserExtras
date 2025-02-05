package dev.letsgoaway.geyserextras;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector4f;

// Most of these are copied from GeyserMC source code lmao.
public class MathUtils {
    public static double restrain(double x, double max) {
        if (x < 0d)
            return 0d;
        return Math.min(x, max);
    }

    /**
     * Round the given float to the next whole number
     *
     * @param floatNumber Float to round
     * @return Rounded number
     */
    public static int ceil(float floatNumber) {
        int truncated = (int) floatNumber;
        return floatNumber > truncated ? truncated + 1 : truncated;
    }

    /**
     * If number is greater than the max, set it to max, and if number is lower than low, set it to low.
     *
     * @param num number to calculate
     * @param min the lowest value the number can be
     * @param max the greatest value the number can be
     * @return - min if num is lower than min <br>
     * - max if num is greater than max <br>
     * - num otherwise
     */
    public static double constrain(double num, double min, double max) {
        if (num > max) {
            num = max;
        }

        if (num < min) {
            num = min;
        }

        return num;
    }

    /**
     * If number is greater than the max, set it to max, and if number is lower than low, set it to low.
     *
     * @param num number to calculate
     * @param min the lowest value the number can be
     * @param max the greatest value the number can be
     * @return - min if num is lower than min <br>
     * - max if num is greater than max <br>
     * - num otherwise
     */
    public static int constrain(int num, int min, int max) {
        if (num > max) {
            num = max;
        }

        if (num < min) {
            num = min;
        }

        return num;
    }

    /**
     * Clamps the value between the low and high boundaries
     * Copied from {@link org.cloudburstmc.math.GenericMath} with floats instead.
     *
     * @param value The value to clamp
     * @param low   The low bound of the clamp
     * @param high  The high bound of the clamp
     * @return the clamped value
     */
    public static float clamp(float value, float low, float high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    public static float clampOne(float value) {
        return clamp(value, -1, 1);
    }


    public static Vector3f toEuler(Vector4f q) {
        Vector3f v;
        float sqw = q.getW() * q.getW();
        float sqx = q.getX() * q.getX();
        float sqy = q.getY() * q.getY();
        float sqz = q.getZ() * q.getZ();
        float unit = sqx + sqy + sqz + sqw;

        float test = q.getX() * q.getY() + q.getZ() * q.getW();
        if (test > 0.499 * unit) { // singularity at north pole
            v = Vector3f.from(0.0, 2 * Math.atan2(q.getX(), q.getW()), Math.PI / 2);
        } else if (test < -0.499 * unit) { // singularity at south pole
            v = Vector3f.from(0.0, -2 * Math.atan2(q.getX(), q.getW()), -(Math.PI / 2));
        } else {
            v = Vector3f.from(Math.atan2(2 * q.getX() * q.getW() - 2 * q.getY() * q.getZ(), -sqx + sqy - sqz + sqw), Math.atan2(2 * q.getY() * q.getW() - 2 * q.getX() * q.getZ(), sqx - sqy - sqz + sqw), Math.asin(2 * test / unit));
        }
        return v;
    }
}
