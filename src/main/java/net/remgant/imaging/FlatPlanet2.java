package net.remgant.imaging;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.function.Function;

public class FlatPlanet2 {
    public static void main(String args[]) throws IOException {
        new FlatPlanet2().run(1L, 600);
    }

    public void run(long seed, int size) throws IOException {
        float[][] map = new float[size][];
        for (int i = 0; i < size; i++) {
            map[i] = new float[size];
            for (int j = 0; j < size; j++) {
                map[i][j] = 0.5f;
            }
        }

        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            int a = random.nextInt() % size;
            int b = random.nextInt() % size;
            boolean z = random.nextBoolean();
            double angle = random.nextDouble() * 2.0 * Math.PI;
            double slope = Math.atan(angle) * (random.nextBoolean() ? 1.0 : -1.0);
            //            double slope = (double) size / (double) (a - b);
            // double yInt = -(double) size * slope;
            double yInt = random.nextDouble() * size * (random.nextBoolean() ? 1.0 : -1.0);

            Function<Double, Double> f = x -> slope * x + yInt;
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++)
                    if (f.apply((double) j) < (double) k) {
                        if (z)
                            map[j][k] *= 1.01;
                        else
                            map[j][k] *= 0.99;
                    } else {
                        if (z)
                            map[j][k] *= 0.99;
                        else
                            map[j][k] *= 1.01;
                    }
            }
            if (i % 100 == 0 || i == 10 || i == 20) {
                BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        float brightness = map[j][k];
                        if (brightness < 0.0)
                            brightness = 0.0f;
                        if (brightness > 1.0)
                            brightness = 1.0f;
                        Color c = Color.getHSBColor(0.5f, 1.0f, brightness);
                        g.setColor(c);
                        g.fill(new Rectangle2D.Double(j, k, 1.0, 1.0));
                    }
                }
                File imageFile = new File(String.format("flat-planet-%d.png", i));
                ImageIO.write(image, "png", imageFile);
            }

        }
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                float brightness = map[j][k];
                if (brightness < 0.0)
                    brightness = 0.0f;
                if (brightness > 1.0)
                    brightness = 1.0f;
                Color c = Color.getHSBColor(0.5f, 1.0f, brightness);
                g.setColor(c);
                g.fill(new Rectangle2D.Double(j, k, 1.0, 1.0));
            }
        }
        File imageFile = new File(String.format("flat-plane.png"));
        ImageIO.write(image, "png", imageFile);
    }
}
