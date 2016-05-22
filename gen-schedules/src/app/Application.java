package app;

import algorithm.Solver;
import algorithm.approximate.ApproximateOpenShopCMax;
import algorithm.genetic.GeneticOpenShopCMax;
import problem.Problem;
import problem.Schedule;

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

    protected JTextArea infoArea = new JTextArea();

	public Application() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(800, 600));
		setLayout(new BorderLayout(10,10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10,10));
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(new JLabel("  Input parameters:  "), BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.CENTER);

		inputArea.setPreferredSize(new Dimension(400, 150));
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

        JButton apply = new JButton("Apply");
        apply.setPreferredSize(new Dimension(150, 30));
        apply.addActionListener(new ApplyButtonListener());
        inputPanel.add(apply, BorderLayout.SOUTH);

        JPanel infoPanel = new JPanel();
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        infoArea.setEditable(false);
        infoArea.setVisible(true);
        infoArea.setTabSize(2);
        clearInfoArea();
        infoPanel.add(infoArea);

        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

		img.setIcon(ImageManager.getImage(null));
        img.setPreferredSize(new Dimension(ImageManager.getImageWidth(), ImageManager.getImageHeight()));
		add(img, BorderLayout.EAST);

		pack();

        // FIXME: for debug. remove in release
        inputArea.setText("3 3 1 2 3 4 5 6 7 8 9");
	}

    protected void clearInfoArea() {
        infoArea.setText("");
    }

    protected void addInfo(String s) {
        String text = infoArea.getText();
        if (!text.isEmpty()) {
            text += "\n";
        }
        infoArea.setText(text + s);
    }

    public class ApplyButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            clearInfoArea();
            Scanner sc = new Scanner(inputArea.getText());
            Problem problem = Problem.read(sc);

            addInfo(problem.toString());

            Solver solver = null;
            if (approxButton.isSelected()) {
                addInfo("Approximate algorithm:");
                solver = new ApproximateOpenShopCMax(problem);
            } else if (geneticButton.isSelected()) {
                System.out.println("Genetic");
                solver = new GeneticOpenShopCMax(problem, 0, 1, 1);
            } else {
                System.out.println("ERROR!");
            }

            if (solver != null) {
                Schedule schedule = solver.generateSchedule();
                img.setIcon(ImageManager.getImage(schedule));
                addInfo(schedule.toString());
                addInfo("Quality:" + ((double)schedule.getTime()) / problem.getLowerBorderOfSolution());
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
