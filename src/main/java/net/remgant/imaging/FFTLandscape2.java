package net.remgant.imaging;

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

public class FFTLandscape2 {
    enum ColorType {GRAYSCALE, COLOR, COLOR16, PHYSICAL}

    enum NumberType {REAL, IMAGINARY}

    public static void main(String args[]) throws IOException {
        FFTLandscape2 fftLandScape = new FFTLandscape2();
        for (int i = 0; i < 1; i++)
            fftLandScape.run((long) i, 2048);
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

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        for (int i = 0; i < array.length; i++) {
            Complex row[] = getRowAsArray(array,i);
            Complex result[] = fft.transform(row, TransformType.FORWARD);
            setRowFromArray(array, i, result);
        }
        for (int i = 0; i < array[0].length; i++) {
            Complex col[] = getColumnAsArray(array,i);
            Complex result[] = fft.transform(col, TransformType.FORWARD);
            setColumnFromArray(array, i, result);
        }

        double power = 2.8;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                Complex c =array[i][j];
                double ii = (double) (i - n / 2) / (double) n;
                double jj = (double) (j - n / 2) / (double) n;
                double f = Math.sqrt(ii * ii + jj * jj);
                f = f < 1.0 / (double) n ? 1.0 / (double) n : f;
                c = c.multiply(1.0 / Math.pow(f, power));
                array[i][j] = c;
            }
        }

        for (int i = 0; i < array.length; i++) {
                        Complex col[] = getColumnAsArray(array,i);
                        Complex result[] = fft.transform(col, TransformType.INVERSE);
                        setColumnFromArray(array, i, result);
                    }
        for (int i = 0; i < array[0].length; i++) {
                  Complex row[] = getRowAsArray(array,i);
                  Complex result[] = fft.transform(row, TransformType.INVERSE);
                  setRowFromArray(array, i, result);
              }


        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                Complex c = array[i][j];
                double re = c.getReal() * ((i + j) % 2 == 0 ? 1.0 : -1.0);
                double im = c.getImaginary() * ((i + j) % 2 == 0 ? -1.0 : 1.0);
                array[i][j] = new Complex(re,im);
            }
        }

        ColorType[] ct = new ColorType[]{ColorType.COLOR, ColorType.COLOR16, ColorType.GRAYSCALE, ColorType.PHYSICAL};
        String suffix[] = new String[]{"color", "c16", "bw", "phys"};
        for (int i = 0; i < ct.length; i++) {
            image = drawImage(array, NumberType.REAL, ct[i]);
            imageFile = new File(String.format("image-%d-%d-%s.png", size, seed, suffix[i]));
            ImageIO.write(image, "png", imageFile);
            System.out.printf("wrote %s%n", imageFile.getName());
        }
    }

    private void setColumnFromArray(Complex[][] target, int idx, Complex[] source) {
        for (int i=0; i<target.length; i++)
                   target[i][idx] = source[i];
    }

    private void setRowFromArray(Complex[][] target, int idx, Complex[] source) {
        System.arraycopy(source, 0, target[idx], 0, target.length);
    }

    private Complex[] getColumnAsArray(Complex[][] source, int idx) {
        Complex[] target = new Complex[source[0].length];
            for (int i=0; i<target.length; i++)
                target[i] = source[i][idx];
            return target;
    }

    private Complex[] getRowAsArray(Complex[][] source, int idx) {
        Complex[] target = new Complex[source.length];
        for (int i=0; i<target.length; i++)
            target[i] = source[idx][i];
        return target;
    }

    BufferedImage drawImage(Complex array[][], NumberType numberType, ColorType colorType) {
        BufferedImage image = new BufferedImage(array.length, array[0].length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                double d;
                if (numberType == NumberType.REAL)
                    d = array[i][j].getReal();
                else
                    d = array[i][j].getImaginary();
                if (d > max)
                    max = d;
                if (d < min)
                    min = d;
            }
        }

        double scale = max - min;
        System.out.printf("max=%f, min=%f, scale=%f%n", max, min, scale);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[j].length; j++) {
                double d;
                if (numberType == NumberType.REAL)
                    d = array[i][j].getReal();
                else
                    d = array[i][j].getImaginary();

                double x = (d - min) / scale;
                Color c;
                if (colorType == ColorType.GRAYSCALE) {
                    int a = (int) (x * 255.0);
                    if (a < 0)
                        a = 0;
                    if (a > 255)
                        a = 255;
                    c = new Color(a, a, a);
                } else if (colorType == ColorType.COLOR) {
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
                g.draw(new Rectangle2D.Double(i, j, 1.0, 1.0));
            }
        }
        return image;
    }

    static Color[] physioMap = new Color[]{
            new Color(45, 217, 81),
            new Color(18, 94, 13),
            new Color(226, 237, 42),
            new Color(147, 155, 25),
            new Color(225, 159, 32),
            new Color(229, 97, 0),
            new Color(100, 50, 16),
            new Color(240, 240, 240),
    };
}
