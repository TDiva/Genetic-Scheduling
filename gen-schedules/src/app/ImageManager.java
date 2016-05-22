package app;

import problem.Machine;
import problem.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageManager {

    public static int getImageWidth() {
        return WIDTH + 2 * MARGIN;
    }

    public static int getImageHeight() {
        return HEIGHT + 2 * MARGIN;
    }

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    public static final int MARGIN = 25;

    public static class Point {
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;
    }

    public static Point getImageCoordinate(long time, int machine, double xCoeff, double yCoeff) {
        int px = (int) (time * xCoeff + MARGIN);
        int py = (int) (machine * yCoeff + MARGIN);
        return new Point(px, py);
    }

    public static void drawAxes(Graphics2D g, double xCoeff, double yCoeff) {
        g.setColor(Color.BLACK);
        g.drawLine(MARGIN-1, HEIGHT + MARGIN, WIDTH + MARGIN-1, HEIGHT + MARGIN);
        g.drawLine(MARGIN-1, MARGIN, MARGIN-1, HEIGHT + MARGIN);

        long maxX = (long) (WIDTH / xCoeff + 0.5) + 1;

        int numQ = WIDTH / 50;
        int deltaX = numQ > maxX ? 1 : (int) (maxX / numQ);

        for (int i = 0; i < maxX; i += deltaX) {
            int pos = (int) (i * xCoeff + MARGIN);
            g.drawLine(pos, HEIGHT + MARGIN - 3, pos, HEIGHT + MARGIN + 3);
            g.drawString(String.valueOf(i), pos - 5, HEIGHT + (int) (MARGIN * 1.7));
        }

    }

    public static final Color[] AVAILABLE_COLORS = {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.CYAN,
            Color.ORANGE,
            Color.LIGHT_GRAY,
            Color.YELLOW,
            Color.PINK,
            Color.DARK_GRAY,
    };

    public static void drawOperation(Graphics2D g, double xCoeff, double yCoeff, int machine, int job, long timeStart, int length) {
        if (length == 0)
            return;
        Point start = getImageCoordinate(timeStart, machine + 1, xCoeff, yCoeff);
        Point end = getImageCoordinate(timeStart + length, machine + 1, xCoeff, yCoeff);

        int jobHeight = (int) (yCoeff / 3);

        int width = end.x - start.x;
        int height = jobHeight * 2;

        if (job < AVAILABLE_COLORS.length) {
            g.setColor(AVAILABLE_COLORS[job]);
        } else {
            Random r = new Random(System.currentTimeMillis());
            g.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        }

        g.fillRect(start.x, start.y - jobHeight, width, height);
        g.setColor(Color.BLACK);

        if (width < 40) {
            g.drawString("J" + job, start.x, start.y);
        } else {
            g.drawString("J" + job, start.x + width / 2 - 5, start.y);
        }


    }

    public static ImageIcon getImage(Schedule schedule) {
        BufferedImage img = new BufferedImage(WIDTH + 2 * MARGIN, HEIGHT + 2 * MARGIN, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH + 2 * MARGIN, HEIGHT + 2 * MARGIN);


        if (schedule == null) {
            drawAxes(g, 1, 1);
            return new ImageIcon(img);
        }

        double xCoeff = ((double) (WIDTH)) / schedule.getTime();
        double yCoeff = ((double) (HEIGHT)) / (schedule.getMachines().size() + 1);

        drawAxes(g, xCoeff, yCoeff);
        g.setFont(new Font("Arial", 3, 16));

        for (int m = 0; m < schedule.getMachines().size(); m++) {
            Point p = getImageCoordinate(0, m+1, xCoeff, yCoeff);
            g.drawString("M" + m, 0, p.y);
        }

        for (Machine m : schedule.getMachines()) {
            m.getSchedule().forEach((time, job) -> {
                drawOperation(g, xCoeff, yCoeff, m.getIndex(), job.getIndex(), time, job.getOperationLength(m.getIndex()));
            });
        }

        return new ImageIcon(img);
    }

}
