package app;

import problem.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageManager {

    public static int getImageWidth() {
        return WIDTH + 2*MARGIN;
    }

    public static int getImageHeight() {
        return HEIGHT + 2*MARGIN;
    }
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;

    public static final int MARGIN = 25;

    public static class Point{
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;
    }

    public static Point getImageCoordinate(long time, int machine, double xCoeff, double yCoeff) {
        int px = (int)(time * xCoeff + MARGIN);
        int py = (int)(machine * yCoeff + MARGIN);
        return new Point(px, py);
    }

    public static void drawAxes(Graphics2D g, double xCoeff, double yCoeff) {
        g.setColor(Color.BLACK);
        g.drawLine(MARGIN, HEIGHT+MARGIN, WIDTH+MARGIN, HEIGHT+MARGIN);
        g.drawLine(MARGIN, MARGIN, MARGIN, HEIGHT+MARGIN);

        long maxX = (long)(WIDTH / xCoeff + 0.5) + 1;

        int numQ = WIDTH / 50;
        int deltaX = (int)(maxX / numQ);

        for (int i=0; i< maxX; i+= deltaX) {
            int pos = (int)(i*xCoeff+MARGIN);
            g.drawLine(pos, HEIGHT+MARGIN-3, pos, HEIGHT+MARGIN+3);
            g.drawString(String.valueOf(i), pos-5, HEIGHT+(int)(MARGIN*1.7));
        }
    }

	public static ImageIcon getImage(Schedule schedule) {
		BufferedImage img = new BufferedImage(WIDTH+2*MARGIN, HEIGHT+2*MARGIN, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)img.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH + 2 * MARGIN, HEIGHT + 2 * MARGIN);

        if (schedule == null) {
            drawAxes(g, 1, 1);
            return new ImageIcon(img);
        }

        double xCoeff = ((double)(WIDTH)) / schedule.getTime();
        double yCoeff = ((double)(WIDTH)) / schedule.getMachines().size();

        drawAxes(g, xCoeff, yCoeff);
        g.setColor(Color.RED);
        g.drawString(schedule.toString(), MARGIN, HEIGHT/2);
		
		return new ImageIcon(img);
	}

}
