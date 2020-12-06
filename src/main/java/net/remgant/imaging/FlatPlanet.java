package net.remgant.imaging;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.function.Function;

public class FlatPlanet {

    public static void main(String args[]) throws IOException {
        new FlatPlanet().run(1L, 600);
    }

    public void run(long seed, int size) throws IOException {
        int[][] map = new int[size][];
        for (int i = 0; i < size; i++) {
            map[i] = new int[size];
            for (int j = 0; j < size; j++) {
                map[i][j] = 128;
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
                            map[j][k] += 2;
                        else
                            map[j][k] -= 2;
                    } else {
                        if (z)
                            map[j][k] -= 2;
                        else
                            map[j][k] += 2;
                    }
            }
            if (i % 100 == 0 || i == 10 || i == 20) {
                BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        Color c;
                        if (map[j][k] < 0)
                            c = Color.BLACK;
                        else if (map[j][k] > 255)
                            c = Color.WHITE;
                        else
                            c = new Color(map[j][k], map[j][k], map[j][k]);
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
                Color c;
                if (map[j][k] < 0)
                    c = Color.BLACK;
                else if (map[j][k] > 255)
                    c = Color.WHITE;
                else
                    c = new Color(map[j][k], map[j][k], map[j][k]);
                g.setColor(c);
                g.fill(new Rectangle2D.Double(j, k, 1.0, 1.0));
            }
        }
        File imageFile = new File(String.format("flat-plane.png"));
        ImageIO.write(image, "png", imageFile);
    }
}

