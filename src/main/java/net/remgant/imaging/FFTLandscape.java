package net.remgant.imaging;

import net.remgant.util.SquareArray2;
import net.remgant.util.TwoDimensionArray;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class FFTLandscape {

    enum ColorType {GRAYSCALE,COLOR, COLOR16,PHYSICAL}
    enum NumberType {REAL,IMAGINARY}
    public static void main(String args[]) throws IOException {
        FFTLandscape fftLandScape = new FFTLandscape();
        for (int i=0; i<1; i++)
            fftLandScape.run((long)i,2048);
    }

    public void run(long seed, int size) throws IOException {
        Random random = new Random(seed);
        BufferedImage image;
        File imageFile;
        int n = size;
        Complex array[][] = new Complex[n][];
        for (int i = 0; i < n; i++) {
            array[i] = new Complex[n];
            for (int j = 0; j < n; j++) {
                double re = random.nextDouble() * (random.nextBoolean() ? 1.0 : -1.0);
                double im = 0.0;
                array[i][j] = new Complex(re, im);
            }
        }
        TwoDimensionArray<Complex> a = new SquareArray2<Complex>(Complex.class, array);
//        System.out.println("Orignal");
//        BufferedImage image = drawImage(a, NumberType.REAL, ColorType.COLOR);
//        File imageFile = new File("image-0r.png");
//        ImageIO.write(image, "png", imageFile);
//        image = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
//        imageFile = new File("image-0i.png");
//        ImageIO.write(image, "png", imageFile);


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
//        System.out.println("After FFT forward");
//        image = drawImage(a, NumberType.REAL, ColorType.COLOR);
//        imageFile = new File("image-1r.png");
//        ImageIO.write(image, "png", imageFile);
//        image = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
//        imageFile = new File("image-1i.png");
//        ImageIO.write(image, "png", imageFile);

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

//        System.out.println("after filtering");
//        image = drawImage(a, NumberType.REAL, ColorType.COLOR);
//        imageFile = new File("image-2r.png");
//        ImageIO.write(image, "png", imageFile);
//        image = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
//        imageFile = new File("image-2i.png");
//        ImageIO.write(image, "png", imageFile);

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

//        System.out.println("after FFT inverse");
//        image = drawImage(a, NumberType.REAL, ColorType.COLOR);
//        imageFile = new File("image-3r.png");
//        ImageIO.write(image, "png", imageFile);
//        image = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
//        imageFile = new File("image-3i.png");
//        ImageIO.write(image, "png", imageFile);

        for (int i = 0; i < a.getWidth(); i++) {
            for (int j = 0; j < a.getHeight(); j++) {
                Complex c = a.get(i, j);
                double re = c.getReal() * ((i + j) % 2 == 0 ? 1.0 : -1.0);
                double im = c.getImaginary() * ((i + j) % 2 == 0 ? -1.0 : 1.0);
                a.put(i, j, new Complex(re, im));
            }
        }

//        System.out.println("after post process");
        ColorType[] ct = new ColorType[]{ColorType.COLOR,ColorType.COLOR16,ColorType.GRAYSCALE,ColorType.PHYSICAL};
        String suffix[] = new String[]{"color","c16","bw","phys"};
        for (int i=0; i<ct.length; i++) {
            image = drawImage(a, NumberType.REAL, ct[i]);
            imageFile = new File(String.format("image-%d-%d-%s.png",size,seed,suffix[i]));
            ImageIO.write(image, "png", imageFile);
            System.out.printf("wrote %s%n",imageFile.getName());
        }

//        image = drawImage(a, NumberType.IMAGINARY, ColorType.COLOR);
//        imageFile = new File("image-4i.png");
//        ImageIO.write(image, "png", imageFile);
    }

    BufferedImage drawImage(TwoDimensionArray<Complex> array, NumberType numberType, ColorType colorType) {
        BufferedImage image = new BufferedImage(array.getWidth(), array.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.getWidth(); i++) {
            for (int j = 0; j < array.getHeight(); j++) {
                double d;
                if (numberType == NumberType.REAL)
                    d = array.get(i, j).getReal();
                else
                    d = array.get(i, j).getImaginary();
                if (d > max)
                    max = d;
                if (d < min)
                    min = d;
            }
        }

        double scale = max - min;
        System.out.printf("max=%f, min=%f, scale=%f%n", max, min, scale);
        for (int i = 0; i < array.getWidth(); i++) {
            for (int j = 0; j < array.getHeight(); j++) {
                double d;
                if (numberType == NumberType.REAL)
                    d = array.get(i, j).getReal();
                else
                    d = array.get(i, j).getImaginary();

                double x = (d -min) / scale;
                Color c;
                if (colorType == ColorType.GRAYSCALE) {
                    int a = (int) (x * 255.0);
                    if (a < 0)
                        a = 0;
                    if (a > 255)
                        a = 255;
                    c = new Color(a, a, a);
                } else if (colorType == ColorType.COLOR){
                    c = Color.getHSBColor((float) x, 1.0f, 1.0f);
                } else if (colorType == ColorType.COLOR16) {
                    x = Math.ceil(x * 16.0) / 16.0;
                    c = Color.getHSBColor((float) x, 1.0f, 1.0f);
                } else {
                    int p = (int) Math.ceil(x * 8.0) - 1;
                    if (p < 0)
                        p = 0;
                    c = physioMap[p];
                }
                g.setColor(c);
//                System.out.printf("d=%g, scale=%g, x=%g, i=%d, j=%d color=%s%n",d,scale,x,i,j,c);
                g.draw(new Rectangle2D.Double(i, j, 1.0, 1.0));
            }
        }
        return image;
    }
    static Color[] physioMap = new Color[] {
            new Color(45,217,81),
            new Color(18,94,13),
            new Color(226,237,42),
            new Color(147,155,25),
            new Color(225,159,32),
            new Color(229,97,0),
            new Color(100,50,16),
            new Color(240,240,240),
    };
}
