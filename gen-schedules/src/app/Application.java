package app;

import algorithm.Solver;
import algorithm.approximate.ApproximateOpenShopCMax;
import algorithm.brute.BruteOpenShop;
import algorithm.genetic.GeneticOpenShopCMax;
import algorithm.genetic.core.crossover.CrossoverManager;
import algorithm.genetic.core.crossover.selection.ParentingManager;
import algorithm.genetic.core.makespan.MakespanManager;
import algorithm.genetic.core.mutation.MutationManager;
import algorithm.genetic.core.selection.SelectionManager;
import problem.Problem;
import problem.Schedule;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Application extends JFrame {
    private static final long serialVersionUID = 8711822820148841971L;

    protected JTextArea inputArea = new JTextArea();

    protected JLabel img = new JLabel();

    protected JRadioButton approxButton = new JRadioButton("Approximate");
    protected JRadioButton geneticButton = new JRadioButton("Genetic");
    protected JRadioButton bruteButton = new JRadioButton("Brute");

    protected JTextField sizeOfPopulationF = new JTextField("20");
    protected JTextField mutationF = new JTextField("0.05");
    protected JTextField iterationsF = new JTextField("100");

    protected JRadioButton simple = new JRadioButton("Simple Scheduling");
    protected JRadioButton modified = new JRadioButton("Optimized Scheduling");

    protected JPanel geneticParamsPanel = new JPanel();

    protected JTextArea infoArea = new JTextArea();

    protected Schedule curSolution;

    protected int bruteLimitOnOperations = 10;

    public Application() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(new JLabel("  Input parameters:  "), BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        inputArea.setPreferredSize(new Dimension(400, 150));
        inputArea.setEditable(true);
        inputPanel.add(inputArea, BorderLayout.NORTH);

        approxButton.setSelected(true);

        ButtonGroup selectAlg = new ButtonGroup();
        selectAlg.add(bruteButton);
        selectAlg.add(approxButton);
        selectAlg.add(geneticButton);

        JPanel butAlgPanel = new JPanel();
        butAlgPanel.setLayout(new BorderLayout());
        JPanel selectAlgPanel = new JPanel();
        selectAlgPanel.add(bruteButton);
        selectAlgPanel.add(approxButton);
        selectAlgPanel.add(geneticButton);
        butAlgPanel.add(selectAlgPanel, BorderLayout.NORTH);

        geneticParamsPanel.setLayout(new GridLayout(4,1));
        butAlgPanel.add(geneticParamsPanel, BorderLayout.CENTER);

        JPanel sop = new JPanel();
        sop.add(new JLabel("Size of population: "));
        sizeOfPopulationF.setPreferredSize(new Dimension(50, 20));
        sop.add(sizeOfPopulationF);
        geneticParamsPanel.add(sop);

        sop = new JPanel();
        sop.add(new JLabel("Mutstion prob.: "));
        mutationF.setPreferredSize(new Dimension(50, 20));
        sop.add(mutationF);
        geneticParamsPanel.add(sop);

        sop = new JPanel();
        sop.add(new JLabel("Iterations: "));
        iterationsF.setPreferredSize(new Dimension(50, 20));
        sop.add(iterationsF);
        geneticParamsPanel.add(sop);
        geneticParamsPanel.setVisible(false);

        sop = new JPanel();
        sop.setLayout(new FlowLayout());
        ButtonGroup s1 = new ButtonGroup();
        simple.setSelected(true);
        sop.add(simple);
        s1.add(simple);
        sop.add(modified);
        s1.add(modified);
        geneticParamsPanel.add(sop);

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
        JScrollPane scroll = new JScrollPane(infoArea);
        scroll.setPreferredSize(new Dimension(400, 250));
        clearInfoArea();
        infoPanel.add(scroll);

        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        img.setIcon(ImageManager.getImage(null));
        img.setPreferredSize(new Dimension(ImageManager.getImageWidth(), ImageManager.getImageHeight()));
        add(img, BorderLayout.EAST);

        MenuBar mainMenu = new MenuBar();
        Menu progremMenu = new Menu("Problem");
        Menu solutionMenu = new Menu("Solution");
        mainMenu.add(progremMenu);
        mainMenu.add(solutionMenu);

        MenuItem loadPr = new MenuItem("Load from file");
        MenuItem savePr = new MenuItem("Save to file");
        progremMenu.add(loadPr);
        progremMenu.add(savePr);

        MenuItem saveSolText = new MenuItem("Save to txt");
        MenuItem saveSolIng = new MenuItem("Save image");
        solutionMenu.add(saveSolText);
        solutionMenu.add(saveSolIng);

        MenuItem settings = new MenuItem("Brute limit");
        Menu m = new Menu("Settings");
        m.add(settings);
        mainMenu.add(m);

        setMenuBar(mainMenu);

        pack();
        setResizable(false);

        // FIXME: for debug. remove in release
        inputArea.setText("3 3 1 2 3 4 5 6 7 8 9");

        geneticButton.addActionListener((e) -> geneticParamsPanel.setVisible(true));
        approxButton.addActionListener((e) -> geneticParamsPanel.setVisible(false));
        bruteButton.addActionListener((e) -> geneticParamsPanel.setVisible(false));

        loadPr.addActionListener((e) -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text file", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                StringBuffer sb = new StringBuffer();
                try (Scanner sc = new Scanner(f)) {
                    while (sc.hasNextLine()) {
                        sb.append(sc.nextLine());
                        sb.append("\n");
                    }
                    inputArea.setText(sb.toString());
                } catch (FileNotFoundException ex) {
                    clearInfoArea();
                    addInfo("File " + f.getName() + " not found");
                }
            }
        });

        savePr.addActionListener((e) -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text file", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                f = setExtension(f, "txt");
                StringBuffer sb = new StringBuffer();
                try (PrintWriter sc = new PrintWriter(f)) {
                    sc.write(inputArea.getText());
                } catch (FileNotFoundException ex) {
                    clearInfoArea();
                    addInfo("File " + f.getName() + " not found");
                }
            }
        });

        saveSolText.addActionListener((e) -> {
            if (curSolution == null) {
                clearInfoArea();
                addInfo("No solution was found");
                return;
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text file", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                f = setExtension(f, "txt");
                StringBuffer sb = new StringBuffer();
                try (PrintWriter sc = new PrintWriter(f)) {
                    sc.write(curSolution.toString());
                } catch (FileNotFoundException ex) {
                    clearInfoArea();
                    addInfo("File " + f.getName() + " not found");
                }
            }
        });

        saveSolIng.addActionListener((e) -> {
            if (curSolution == null) {
                clearInfoArea();
                addInfo("No solution was found");
                return;
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image JPG", "jpg");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                f = setExtension(f, "jpg");
                StringBuffer sb = new StringBuffer();
                try (FileOutputStream sc = new FileOutputStream(f)) {
                    ImageIO.write(ImageManager.getBufferedImage(curSolution), "jpg", sc);
                } catch (FileNotFoundException ex) {
                    clearInfoArea();
                    addInfo("File " + f.getName() + " not found");
                } catch (IOException e1) {
                    clearInfoArea();
                    addInfo("Error while saving image: " + e1.getMessage());
                }
            }
        });

        settings.addActionListener((e) -> {
            String limit =  JOptionPane.showInputDialog(
                    this,
                    "Brute algorithm limit on operations:",
                    String.valueOf(bruteLimitOnOperations));
            if (limit != null) {
                bruteLimitOnOperations = Integer.valueOf(limit);
            }
        });
    }

    protected File setExtension(File f, String ext) {
        if (f.getName().endsWith("." + ext)) {
            return f;
        }
        String newName = f.getAbsolutePath() + "." + ext;
        return new File(newName);
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
            if (bruteButton.isSelected()) {
                addInfo("Brute algorithm:");
                if (problem.getNumberOfMachines() * problem.getNumberOfJobs() > bruteLimitOnOperations) {
                    addInfo("Cannot proceed: \nnumber of operations exceed max = " + bruteLimitOnOperations);
                    addInfo("Solution will take too much time. \nTry approximate of genetic algorithm");
                    return;
                }
                solver = new BruteOpenShop(problem);
            } else if (approxButton.isSelected()) {
                addInfo("Approximate algorithm:");
                solver = new ApproximateOpenShopCMax(problem);
            } else if (geneticButton.isSelected()) {
                addInfo("Genetic algorithm:");
                MakespanManager.MakespanManagerType makespanManager = null;
                if (simple.isSelected()) {
                    makespanManager = MakespanManager.MakespanManagerType.OPEN_SHOP_SIMPLE;
                    addInfo("Simple scheduling");
                } else if (modified.isSelected()) {
                    makespanManager = MakespanManager.MakespanManagerType.OPEN_SHOP_MODIFIED;
                    addInfo("Optimized scheduling");
                } else {
                    addInfo("ERROR: select scheduling type");
                    return;
                }

                int sizeOfPopulation = Integer.valueOf(sizeOfPopulationF.getText());
                addInfo("Size of population: " + sizeOfPopulation);
                double mutation = Double.valueOf(mutationF.getText());
                addInfo(String.format("Mutation probability: %.2f", mutation));
                int iterrations = Integer.valueOf(iterationsF.getText());
                addInfo("Iterations: " + iterrations);

                solver = new GeneticOpenShopCMax(
                        problem,
                        makespanManager,
                        ParentingManager.ParentingManagerType.CROSSOVER_WHEEL,
                        CrossoverManager.CrossoverManagerType.RANDOM_CROSSOVER,
                        MutationManager.MutationManagerType.SWAP_MUTATION,
                        mutation,
                        SelectionManager.SelectionManagerType.ELITE_SELECTION,
                        sizeOfPopulation,
                        iterrations,
                        0);
            } else {
                System.out.println("ERROR!");
            }

            if (solver != null) {
                curSolution = solver.generateSchedule();
                img.setIcon(ImageManager.getImage(curSolution));
                addInfo(curSolution.toString());
                addInfo("Quality:" + ((double) curSolution.getTime()) / problem.getLowerBorderOfSolution());
            }
        }


    }


    public static void main(String[] args) {
        Application app = new Application();
        app.setVisible(true);
    }
}
