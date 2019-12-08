package net.remgant.imaging;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Illustrator {
    enum NumberType {REAL, IMAGINARY}

    public static void main(String args[]) throws IOException {
        new Illustrator().run("step-function", 256);
    }

    Complex[] createSineWave(int n) {
        Complex[] array = new Complex[n];
        for (int i = 0; i < array.length; i++) {
            double re = Math.sin((double) i / (double) n * 2.0 * Math.PI);
            double im = 0.0;
            array[i] = new Complex(re, im);
        }
        return array;
    }

    Complex[] createAddedSine(int n) {
        Complex[] array = new Complex[n];
        for (int i = 0; i < array.length; i++) {
            double d = Math.sin((double) i / (double) n * 2.0 * Math.PI);
            d += (1.0/3.0) * Math.sin((double) i / (double) n * 6.0 * Math.PI);
            d += (1.0/9.0) * Math.sin((double) i / (double) n * 18.0 * Math.PI);
            double re = d;
            double im = 0.0;
            array[i] = new Complex(re, im);
        }
        return array;
    }

    Complex[] createStepFunction(int n) {
        Complex[] array = new Complex[n];
        for (int i = 0; i < array.length; i++) {
            double re;
            if (i < n / 2)
                re = 1.0;
            else
                re = 0.0;
            double im = 0.0;
            array[i] = new Complex(re, im);
        }
        return array;
    }

    public void run(String type, int n) throws IOException {
        Complex[] array = createAddedSine(n);

        BufferedImage image = drawTimeDomain(array, NumberType.REAL);

        File imageFile = new File(String.format("%s-0-original.png", type));
        ImageIO.write(image, "png", imageFile);

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        array = fft.transform(array, TransformType.FORWARD);
        image = drawTimeDomain(array, NumberType.REAL);

        imageFile = new File(String.format("%s-1-transformed.png", type));
        ImageIO.write(image, "png", imageFile);

        for (int i = 0; i < n; i++) {
            if (i < n / 3 || i >= (2 * n)/3)
                array[i] = new Complex(0.0, 0.0);
        }
        image = drawTimeDomain(array, NumberType.REAL);

        imageFile = new File(String.format("%s-2-filtered.png", type));
        ImageIO.write(image, "png", imageFile);

        array = fft.transform(array, TransformType.INVERSE);

        image = drawTimeDomain(array, NumberType.REAL);
        imageFile = new File(String.format("%s-3-restored.png", type));
        ImageIO.write(image, "png", imageFile);
    }

    BufferedImage drawTimeDomain(Complex[] array, NumberType numberType) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            double d;
            if (numberType == NumberType.REAL)
                d = array[i].getReal();
            else
                d = array[i].getImaginary();
            if (d < min)
                min = d;
            if (d > max)
                max = d;
        }
        double range = max - min;
        double scale = (double) array.length / range;
        System.out.printf("min = %f, max = %f, range = %f, scale = %f%n", min, max, range, scale);
        Area a = new Area();
        for (int i = 0; i < array.length; i++) {
            double y;
            if (numberType == NumberType.REAL)
                y = array[i].getReal();
            else
                y = array[i].getImaginary();
            double x = (double) i;
            Shape s = new Rectangle2D.Double(x, y * scale, 1.0, 1.0);
            a.add(new Area(s));
        }
        Rectangle2D bounds = a.getBounds2D();
        a.transform(AffineTransform.getTranslateInstance(0.0, -bounds.getY()));
        a.transform(AffineTransform.getScaleInstance(1.0, -1.0));
        a.transform(AffineTransform.getTranslateInstance(0.0, bounds.getHeight()));
        System.out.println(a.getBounds2D());
        bounds = a.getBounds2D();
        BufferedImage image = new BufferedImage(array.length, array.length + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fill(bounds);
        g.setColor(Color.RED);
        g.fill(a);
        return image;
    }
}
