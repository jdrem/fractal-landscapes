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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jdr
 * Date: 12/21/13
 * Time: 6:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class FFTIllustrator extends FFTLandscape {

    public static void main(String args[]) throws IOException {
        FFTIllustrator fftIllustrator = new FFTIllustrator();
        fftIllustrator.run(1L, 512);
    }

    public void run(long seed, int size) throws IOException {
        Random random = new Random(seed);
        BufferedImage image;
        BufferedImage realImage;
        BufferedImage imaginaryImage;
        Graphics2D g;
        File imageFile;
        int n = size;
        Complex array[][] = new Complex[n][];
        for (int i = 0; i < n; i++) {
            array[i] = new Complex[n];
            for (int j = 0; j < n; j++) {
                double re = random.nextGaussian();
                double im = 0.0;
                array[i][j] = new Complex(re, im);
            }
        }
        TwoDimensionArray<Complex> a = new SquareArray2<Complex>(Complex.class, array);
        System.out.println("Orignal");
        realImage = drawImage(a, NumberType.REAL, ColorType.COLOR);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("image-0.png");
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
        imageFile = new File("image-1.png");
        ImageIO.write(image, "png", imageFile);

        double power = 2.8;
        for (int i = 0; i < a.getWidth(); i++) {
            for (int j = 0; j < a.getHeight(); j++) {
                Complex c = a.get(i, j);
                double ii = (double) (i - n / 2) / (double) n;
                double jj = (double) (j - n / 2) / (double) n;
                double f = Math.sqrt(ii * ii + jj * jj);
                f = f < 1.0 / (double) n ? 1.0 / (double) n : f;
                c = c.multiply(1.0 / Math.pow(f, power));
                a.put(i, j, c);
            }
        }

        System.out.println("after filtering");
        realImage = drawImage(a, NumberType.REAL, ColorType.COLOR);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("image-2.png");
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
        realImage = drawImage(a, NumberType.REAL, ColorType.COLOR);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("image-3.png");
        ImageIO.write(image, "png", imageFile);

        for (int i = 0; i < a.getWidth(); i++) {
            for (int j = 0; j < a.getHeight(); j++) {
                Complex c = a.get(i, j);
                double re = c.getReal() * ((i + j) % 2 == 0 ? 1.0 : -1.0);
                double im = c.getImaginary() * ((i + j) % 2 == 0 ? -1.0 : 1.0);
                a.put(i, j, new Complex(re, im));
            }
        }

        System.out.println("after post process");


        realImage = drawImage(a, NumberType.REAL, ColorType.COLOR);
        imaginaryImage = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
        image = new BufferedImage(2 * n, n, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.drawImage(realImage, AffineTransform.getTranslateInstance(0.0, 0.0), null);
        g.drawImage(imaginaryImage, AffineTransform.getTranslateInstance((double) n, 0.0), null);
        imageFile = new File("image-4.png");
        ImageIO.write(image, "png", imageFile);
    }
}