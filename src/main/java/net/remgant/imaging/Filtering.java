package net.remgant.imaging;

import net.remgant.util.SquareArray2;
import net.remgant.util.TwoDimensionArray;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Filtering extends FFTLandscape {

    public static void main(String args[]) throws IOException {
        new Filtering().run(1L, 256);
    }

    Random random;

    public void run(long seed, int size) throws IOException {
        random = new Random(seed);
        BufferedImage image;
        BufferedImage realImage;
        BufferedImage imaginaryImage;
        Graphics2D g;
        File imageFile;
        int n = size;
        Complex array[][] = createStepFunction(n);
        TwoDimensionArray<Complex> a = new SquareArray2<Complex>(Complex.class, array);
        System.out.println("Orignal");
        realImage = drawImage(a, NumberType.REAL, ColorType.GRAYSCALE);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("filtering-0-original.png");
        ImageIO.write(image, "png", imageFile);


        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        for (int i = 0; i < a.getHeight(); i++) {
            java.util.List<Complex> r = a.getRow(i);
            Complex result[] = fft.transform(r.toArray(new Complex[n]), TransformType.FORWARD);
            a.setRow(i, result);
        }
        for (int i = 0; i < a.getWidth(); i++) {
            java.util.List<Complex> r = a.getColumn(i);
            Complex result[] = fft.transform(r.toArray(new Complex[n]), TransformType.FORWARD);
            a.setColumn(i, result);
        }
        System.out.println("After FFT forward");
        realImage = drawImage(a, NumberType.REAL, ColorType.COLOR);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("filtering-1-forward.png");
        ImageIO.write(image, "png", imageFile);


        for (int i = 0; i < a.getWidth(); i++) {
            for (int j = 0; j < a.getHeight(); j++) {
                Complex c = a.get(i, j);
                if (i < n/4 && j < n/4)
                    c = new Complex(0.0,0.0);
                a.put(i, j, c);
            }
        }

        System.out.println("After filtering");
        realImage = drawImage(a, NumberType.REAL, ColorType.COLOR);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("filtering-2-filtered.png");
        ImageIO.write(image, "png", imageFile);

        for (int i = 0; i < a.getWidth(); i++) {
            java.util.List<Complex> r = a.getColumn(i);
            Complex result[] = fft.transform(r.toArray(new Complex[n]), TransformType.INVERSE);
            a.setColumn(i, result);
        }
        for (int i = 0; i < a.getHeight(); i++) {
            java.util.List<Complex> r = a.getRow(i);
            Complex result[] = fft.transform(r.toArray(new Complex[n]), TransformType.INVERSE);
            a.setRow(i, result);
        }

        System.out.println("after FFT inverse");
        realImage = drawImage(a, NumberType.REAL, ColorType.GRAYSCALE);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("filtering-3-inverse.png");
        ImageIO.write(image, "png", imageFile);
    }

    Complex[][] createRandom(int n) {
        Complex array[][] = new Complex[n][];
        for (int i = 0; i < n; i++) {
            array[i] = new Complex[n];
            for (int j = 0; j < n; j++) {
                double re = random.nextDouble() * (random.nextBoolean() ? 1.0 : -1.0);
                double im = 0.0;
                array[i][j] = new Complex(re, im);
            }
        }
        return array;
    }

    Complex[][] createStepFunction(int n) {
        Complex array[][] = new Complex[n][];
         for (int i = 0; i < n; i++) {
             array[i] = new Complex[n];
             for (int j = 0; j < n; j++) {
                 double re;
                 if (i < n/2 && j < n/2 || i >= n/2 && j >= n/2)
                    re = 0.0;
                 else
                    re = 1.0;
                 double im = 0.0;
                 array[i][j] = new Complex(re, im);
             }
         }
         return array;
    }

    Complex[][] createRampFunction(int n) {
        Complex array[][] = new Complex[n][];
               for (int i = 0; i < n; i++) {
                   array[i] = new Complex[n];
                   for (int j = 0; j < n; j++) {
                       double re;
//                       if (i < n/2 && j < n/2 || i >= n/2 && j >= n/2)
                          re = 1.0 * ((double)((i+j)/2)/(double)(n));
//                       else
//                          re = 1.0;
                       double im = 0.0;
                       array[i][j] = new Complex(re, im);
                   }
               }
               return array;
    }

    Complex[][] createSineWave(int n) {
            Complex array[][] = new Complex[n][];
                   for (int i = 0; i < n; i++) {
                       array[i] = new Complex[n];
                       for (int j = 0; j < n; j++) {
                           double re;
                              re = Math.sin(Math.PI*2.0*((double)((i+j)/2)/(double)(n)));
                           double im = 0.0;
                           array[i][j] = new Complex(re, im);
                       }
                   }
                   return array;
        }
}
