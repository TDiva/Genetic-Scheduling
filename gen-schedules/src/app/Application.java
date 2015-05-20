package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Application extends JFrame {
	private static final long serialVersionUID = 8711822820148841971L;

	JTextArea inputArea = new JTextArea();

	JLabel img = new JLabel();

	public Application() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(800, 600));
		setLayout(new BorderLayout());

		inputArea.setPreferredSize(new Dimension(300, HEIGHT));
		inputArea.setEditable(true);
		add(inputArea, BorderLayout.WEST);

		JPanel propPanel = new JPanel();
		propPanel.setPreferredSize(new Dimension(300, HEIGHT));
		add(propPanel, BorderLayout.CENTER);
		propPanel.add(new JLabel("hello"));

		img.setIcon(ImageManager.getImage(null));
		add(img, BorderLayout.EAST);

		JPanel btmPanel = new JPanel();
		add(btmPanel, BorderLayout.SOUTH);
		btmPanel.setLayout(new FlowLayout());
		JButton apply = new JButton("Apply");
		apply.setPreferredSize(new Dimension(150, 30));
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Banzai!!!");
			}
		});
		btmPanel.add(apply, BorderLayout.SOUTH);

		pack();
	}

	public static void main(String[] args) {
		Application app = new Application();
		app.setVisible(true);
	}
}
