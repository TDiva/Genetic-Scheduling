package app;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import problem.Schedule;

public class ImageManager {
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static ImageIcon getImage(Schedule schedule) {
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		
		
		return new ImageIcon(img);
	}

}
