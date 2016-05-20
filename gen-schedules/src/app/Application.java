package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Application extends JFrame {
	private static final long serialVersionUID = 8711822820148841971L;

	protected JTextArea inputArea = new JTextArea();

	protected JLabel img = new JLabel();

    protected JRadioButton approxButton = new JRadioButton("Approximate");
    protected JRadioButton geneticButton = new JRadioButton("Genetic");

	public Application() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(800, 600));

        BorderLayout bL = new BorderLayout();
        bL.setHgap(10);
        bL.setVgap(10);
		setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(bL);
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(new JLabel("  Input parameters:  "), BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        mainPanel.add(inputPanel, BorderLayout.CENTER);

		inputArea.setPreferredSize(new Dimension(500, 400));
		inputArea.setEditable(true);
		inputPanel.add(inputArea, BorderLayout.NORTH);

        approxButton.setSelected(true);
        geneticButton.addActionListener(new SelectGeneticAlgListener());

        ButtonGroup selectAlg = new ButtonGroup();
        selectAlg.add(approxButton);
        selectAlg.add(geneticButton);

        JPanel butAlgPanel = new JPanel();
        butAlgPanel.setLayout(new FlowLayout());
        butAlgPanel.add(approxButton);
        butAlgPanel.add(geneticButton);
        inputPanel.add(butAlgPanel,
                BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JButton apply = new JButton("Apply");
        apply.setPreferredSize(new Dimension(150, 30));
        apply.addActionListener(new ApplyButtonListener());
        bottomPanel.add(apply, BorderLayout.SOUTH);

		img.setIcon(ImageManager.getImage(null));
        img.setPreferredSize(new Dimension(ImageManager.getImageWidth(), ImageManager.getImageHeight()));
		add(img, BorderLayout.EAST);

		pack();
	}

    public class ApplyButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Scanner sc = new Scanner(inputArea.getText());

            while (sc.hasNext()) {
                System.out.println(sc.nextLine() + "->");
            }
            if (approxButton.isSelected()) {
                System.out.println("Approx");
            } else if (geneticButton.isSelected()) {
                System.out.println("Genetic");
            } else {
                System.out.println("ERROR!");
            }
        }
    }

    public class SelectGeneticAlgListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Genetic options shoulf be shown");
        }
    }


	public static void main(String[] args) {
		Application app = new Application();
		app.setVisible(true);
	}
}
