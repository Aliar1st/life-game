package loc.aliar.view;

import loc.aliar.controller.Controller;
import loc.aliar.model.game.Game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Window extends JFrame {

    private static final String TITLE = "BacteriaLife";
    private static final int MIN_WIDTH = 600;
    private static final int MIN_HEIGHT = 400;

    private JSpinner widthProp;
    private JSpinner heightProp;
    private JSpinner ageProp;
    private JSpinner closenessProp;
    private JSpinner lonelinessProp;
    private JSpinner stepCountProp;
    private JSpinner stepDelayProp;
    private JSpinner cellSizeProp;
    private JCheckBox isExpandedProp;

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton clearButton = new JButton("Clear");
    private final JButton exitButton = new JButton("Exit");

    private final JLabel stepsLabel = new JLabel();

    private final JScrollPane scrollPane = new JScrollPane();
    private final Canvas canvas;
    private final Controller controller;

    public Window(Controller controller) {
        super(TITLE);

        this.controller = controller;
        canvas = new Canvas(controller);

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createInputs();
        createGUI();
        setupInputListeners();
        setupButtonListeners();
        setupGameListener();
    }

    private void createGUI() {
        Container container = getContentPane();

        scrollPane.setViewportView(canvas);

        container.add(paramsGroup(), BorderLayout.WEST);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(bottomGroup(), BorderLayout.SOUTH);
    }

    private JPanel paramsGroup() {
        JPanel paramsGroup = new JPanel();
        paramsGroup.setBorder(new EmptyBorder(5, 5, 5, 5));
        paramsGroup.setLayout(new BoxLayout(paramsGroup, BoxLayout.Y_AXIS));

        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel closenessLabel = new JLabel("Closeness:");
        JLabel lonelinessLabel = new JLabel("Loneliness:");
        JLabel stepDelayLabel = new JLabel("Step delay:");
        JLabel cellSizeLabel = new JLabel("Cell size:");
        JLabel stepCountLabel = new JLabel("Step:");

        paramsGroup.add(widthLabel);
        paramsGroup.add(widthProp);
        paramsGroup.add(heightLabel);
        paramsGroup.add(heightProp);
        paramsGroup.add(ageLabel);
        paramsGroup.add(ageProp);
        paramsGroup.add(closenessLabel);
        paramsGroup.add(closenessProp);
        paramsGroup.add(lonelinessLabel);
        paramsGroup.add(lonelinessProp);
        paramsGroup.add(stepCountLabel);
        paramsGroup.add(stepCountProp);
        paramsGroup.add(stepDelayLabel);
        paramsGroup.add(stepDelayProp);
        paramsGroup.add(cellSizeLabel);
        paramsGroup.add(cellSizeProp);
        paramsGroup.add(isExpandedProp);

        return paramsGroup;
    }

    private JPanel bottomGroup() {
        JPanel bottomGroup = new JPanel();
        bottomGroup.setLayout(new BoxLayout(bottomGroup, BoxLayout.X_AXIS));

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonGroup.add(startButton);
        buttonGroup.add(stopButton);
        buttonGroup.add(clearButton);
        buttonGroup.add(exitButton);

        JPanel stepsGroup = new JPanel();
        stepsGroup.setLayout(new FlowLayout(FlowLayout.LEFT));
        stepsGroup.setBorder(new EmptyBorder(5, 5, 5 , 5));
        stepsGroup.add(new JLabel("Step:"));
        stepsGroup.add(stepsLabel);

        bottomGroup.add(stepsGroup);
        bottomGroup.add(buttonGroup);

        return bottomGroup;
    }

    private void createInputs() {
        SpinnerModel widthSM = new SpinnerNumberModel(
                controller.getWidth(), 5, 500, 5
        );
        SpinnerModel heightSM = new SpinnerNumberModel(
                controller.getHeight(), 5, 500, 5
        );
        SpinnerModel ageSM = new SpinnerNumberModel(
                controller.getAge(), 0, 200, 5
        );
        SpinnerModel closenessSM = new SpinnerNumberModel(
                controller.getCloseness(), 0, 8, 1
        );
        SpinnerModel lonelinessSM = new SpinnerNumberModel(
                controller.getLoneliness(), 0, 8, 1
        );
        SpinnerModel stepCountSM = new SpinnerNumberModel(
                controller.getStepCount(), 1, 5, 1
        );
        SpinnerModel stepDelaySM = new SpinnerNumberModel(
                controller.getStepDelay(), 0, 10_000, 100
        );
        SpinnerModel cellSizeSM = new SpinnerNumberModel(
                canvas.getCellSide(), 2, 50, 2
        );

        widthProp = new JSpinner(widthSM);
        heightProp = new JSpinner(heightSM);
        ageProp = new JSpinner(ageSM);
        closenessProp = new JSpinner(closenessSM);
        lonelinessProp = new JSpinner(lonelinessSM);
        stepCountProp = new JSpinner(stepCountSM);
        stepDelayProp = new JSpinner(stepDelaySM);
        cellSizeProp = new JSpinner(cellSizeSM);
        isExpandedProp = new JCheckBox("Is expanded", controller.isExpanded());
    }

    private void setupInputListeners() {
        widthProp.addChangeListener(e -> {
            controller.setWidth((int) widthProp.getValue());
            scrollPane.doLayout();
            canvas.repaint();
        });
        heightProp.addChangeListener(e -> {
            controller.setHeight((int) heightProp.getValue());
            scrollPane.doLayout();
            canvas.repaint();
        });
        ageProp.addChangeListener(e -> controller.setAge((int) ageProp.getValue()));
        closenessProp.addChangeListener(e -> controller.setCloseness((int) closenessProp.getValue()));
        lonelinessProp.addChangeListener(e -> controller.setLoneliness((int) lonelinessProp.getValue()));
        stepCountProp.addChangeListener(e -> controller.setStepCount((int) stepCountProp.getValue()));
        stepDelayProp.addChangeListener(e -> controller.setStepDelay((int) stepDelayProp.getValue()));
        cellSizeProp.addChangeListener(e -> canvas.setCellSide((int) cellSizeProp.getValue()));
        isExpandedProp.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setExpanded(isExpandedProp.isSelected());
            }
        });
    }

    private void setupButtonListeners() {
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.start();
            }
        });
        stopButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stop();
            }
        });
        clearButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
        exitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void setupGameListener() {
        controller.setGameListener(new Game.GameListener() {
            @Override
            public void onStart() {
                canvas.repaint();

                widthProp.setEnabled(false);
                heightProp.setEnabled(false);
                clearButton.setEnabled(false);
                startButton.setEnabled(false);
                startButton.setText("Running...");
            }

            @Override
            public void onStep(int step) {
                canvas.repaint();
                stepsLabel.setText(step + " / " + controller.getAge());
            }

            @Override
            public void onStop() {
                canvas.repaint();

                widthProp.setEnabled(true);
                heightProp.setEnabled(true);
                clearButton.setEnabled(true);
                startButton.setEnabled(true);
                startButton.setText("Start");
            }

            @Override
            public void onClear() {
                canvas.repaint();
            }

            @Override
            public void onFieldChange(int width, int height) {
                widthProp.setValue(width);
                heightProp.setValue(height);
            }
        });
    }
}
