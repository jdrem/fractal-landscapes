package net.remgant.util;

import org.junit.Test;

import java.awt.*;

public class ColorTest {

    @Test
    public void test1() {
        Color c = Color.getHSBColor(0.5f, 1.0f, 1.0f);
        System.out.println(c);
        float[] hsb = Color.RGBtoHSB(128, 128, 128, null);
        System.out.printf("%f %f %f%n",hsb[0], hsb[1], hsb[2]);
    }
}
