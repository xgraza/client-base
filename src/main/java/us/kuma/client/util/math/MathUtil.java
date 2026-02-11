/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.util.math;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_DOWN;

public class MathUtil
{
    public static double round(double value, int scale)
    {
        return new BigDecimal(value).setScale(scale, HALF_DOWN).doubleValue();
    }
}
