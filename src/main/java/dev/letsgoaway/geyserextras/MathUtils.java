package dev.letsgoaway.geyserextras;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;

// Most of these are copied from GeyserMC source code lmao.
public class MathUtils {

    /**
     * Retrieve the base attribute value with all modifiers applied.
     * <a href="https://minecraft.wiki/w/Attribute#Modifiers">See here</a>
     * @param attribute The attribute to calculate the total value.
     * @return The finished attribute with all modifiers applied.
     */
    public static double calculateAttribute(WrapperPlayServerUpdateAttributes.Property attribute) {
        double base = attribute.getValue();
        for (WrapperPlayServerUpdateAttributes.PropertyModifier modifier : attribute.getModifiers()) {
            if (modifier.getOperation() == WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.ADDITION) {
                base += modifier.getAmount();
            }
        }
        double value = base;
        for (WrapperPlayServerUpdateAttributes.PropertyModifier modifier : attribute.getModifiers()) {
            if (modifier.getOperation() == WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_BASE) {
                value += base * modifier.getAmount();
            }
        }
        for (WrapperPlayServerUpdateAttributes.PropertyModifier modifier : attribute.getModifiers()) {
            if (modifier.getOperation() == WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_TOTAL) {
                value *= 1.0D + modifier.getAmount();
            }
        }
        return value;
    }
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
     * @param low The low bound of the clamp
     * @param high The high bound of the clamp
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

}
