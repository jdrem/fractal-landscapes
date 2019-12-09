package net.remgant.imaging;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.function.Function;

public class FlatPlanet3 {
    public static void main(String args[]) throws IOException {
        new FlatPlanet3().run(1L, 600);
    }

    public void run(long seed, int size) throws IOException {
        float[][] map = new float[size][];
        for (int i = 0; i < size; i++) {
            map[i] = new float[size];
            for (int j = 0; j < size; j++) {
                map[i][j] = 0.5f;
            }
        }

        Random random = new Random(seed);

        for (int i = 0; i < 10000; i++) {
            int a = random.nextInt(4);
            int b = a;
            while (b == a)
                b = random.nextInt(4);
            Point2D p1 = new Point2D.Double();
            Point2D p2 = new Point2D.Double();
            switch (a) {
                case 0: {
                    p1 = new Point2D.Double(0, random.nextDouble() * (double) size);
                    switch (b) {
                        case 1:
                            p2 = new Point2D.Double(random.nextDouble() * (double) size, (double) size);
                            break;
                        case 2:
                            p2 = new Point2D.Double((double) size, random.nextDouble() * (double) size);
                            break;
                        case 3:
                            p2 = new Point2D.Double(random.nextDouble() * (double) size, 0.0);
                            break;
                    }
                    break;
                }
                case 1: {
                    p1 = new Point2D.Double(random.nextDouble() * (double) size, (double) size);
                    switch (b) {
                        case 0:
                            p2 = new Point2D.Double(0, random.nextDouble() * (double) size);
                            break;
                        case 2:
                            p2 = new Point2D.Double((double) size, random.nextDouble() * (double) size);
                            break;
                        case 3:
                            p2 = new Point2D.Double(random.nextDouble() * (double) size, 0.0);
                            break;

                    }
                    break;
                }
                case 2: {
                    p1 = new Point2D.Double((double) size, random.nextDouble() * (double) size);
                    switch (b) {
                        case 0:
                            p2 = new Point2D.Double(0, random.nextDouble() * (double) size);
                            break;
                        case 1:
                            p2 = new Point2D.Double(random.nextDouble() * (double) size, (double) size);
                            break;
                        case 3:
                            p2 = new Point2D.Double(random.nextDouble() * (double) size, 0.0);
                            break;
                    }
                    break;
                }
                case 3: {
                    p1 = new Point2D.Double(random.nextDouble() * (double) size, 0.0);
                    switch (b) {
                        case 0:
                            p2 = new Point2D.Double(0, random.nextDouble() * (double) size);
                            break;
                        case 1:
                            p2 = new Point2D.Double(random.nextDouble() * (double) size, (double) size);
                            break;
                        case 2:
                            p2 = new Point2D.Double(0.0, random.nextDouble() * (double) size);
                            break;
                    }
                    break;
                }
            }

            double slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
            double yInt = p1.getY() - slope * p2.getX();

            Function<Double, Double> f = x -> slope * x + yInt;
            boolean z = random.nextBoolean();
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {

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
            }
            if (i % 1000 == 0 || i == 10 || i == 20) {
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
