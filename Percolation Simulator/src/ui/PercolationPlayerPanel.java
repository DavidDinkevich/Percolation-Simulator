package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;

import percolation.PercolationChunkInfo;
import percolation.PercolationChunkInfo.PROPERTY;

import ui.PercolationEditorPanel.SubPanel;


@SuppressWarnings("serial")
public class PercolationPlayerPanel extends SubPanel
implements NumberChangeListener {
	
	private JTextArea stepLabel, stepValueLabel, blocksAddedLabel;
	private JButton stepBackButton, stepForwardButton, restartButton, 
					finishButton;
	private JButton timerPauseButton;
	private JToggleButton timerBackButton, timerForwardButton;
	private JTextArea delayTextArea;
	private NumberOnlyTextField delayField;
	
	private ImageIcon playIcon, pauseIcon, restartIcon;
	
	private JPanel timerPanel;
	
	private Timer timer;
	private boolean timerIsPaused;
	
	/** How many blocks were added in the last step. */
	private int blocksAddedLast;
	
	public PercolationPlayerPanel(PercolationEditorPanel editorPanel) {
		super(editorPanel);
		
		setBackground(StyleManager.PANEL_BACKGROUND);
		
//		playIcon = new ImageIcon("play.png");
		URL playURL = PercolationPlayerPanel.class.getResource("/play.png");
		playIcon = new ImageIcon(playURL);

//		pauseIcon = new ImageIcon("images/pause.png");
		URL pauseURL = PercolationPlayerPanel.class.getResource("/pause.png");
		pauseIcon = new ImageIcon(pauseURL);
		
//		restartIcon = new ImageIcon("images/restart.png");
		URL restartURL = PercolationPlayerPanel.class.getResource("/restart.png");
		restartIcon = new ImageIcon(restartURL);
				
		timerIsPaused = true;
		makeTimer();
				
		MigLayout lay = new MigLayout();
		setLayout(lay);
		lay.setRowConstraints("7");
		
		stepLabel = StyleManager.newHeaderTextArea("Step:");
		stepValueLabel = StyleManager.newTextArea("x/y");
		
		blocksAddedLabel = StyleManager.newTextArea("");
		blocksAddedLabel.setColumns(12);
		
		add(stepLabel);
		add(stepValueLabel);
		add(blocksAddedLabel, "right, span 2, wrap");
		add(makeStepButtonPanel(), "center, span, wrap");
		add(makeTimerPanel(), "span");
	
	}

	@Override
	public void update() {
		PercolationChunkInfo finalInfo = editorPanel.getFinalPercolationChunkInfo();

		// Update step label
		final int totalSteps = (int) finalInfo.getProperty(PROPERTY.CURRENT_STEP);
		final int currStep = pchunk.getCurrentStep();
		stepValueLabel.setText(currStep + " / " + totalSteps);
		
		// Update blocks added label
		if (blocksAddedLast >= 0) {
			blocksAddedLabel.setForeground(new Color(0, 170, 0));
			blocksAddedLabel.setText(blocksAddedLast + " blocks added!");
		} else {
			blocksAddedLabel.setForeground(Color.RED);
			blocksAddedLabel.setText(-blocksAddedLast + " blocks removed!");
		}
		
		// Update buttons
		stepForwardButton.setEnabled(!finishedPercolation());
		stepBackButton.setEnabled(pchunk.getCurrentStep() > 0);

		// Update restart button
		restartButton.setEnabled(pchunk.getCurrentStep() > 0);
		
		handleTimerPauseButtonState();
				
		// timerPauseButton tells us if we've finished percolating
		// forward or backward
		if (!timerPauseButton.isEnabled()) {
			// Stop the timer
			timerIsPaused = true;
			timer.stop();
			// Reset image of pause button
			timerPauseButton.setIcon(playIcon);
			// Enable delay field
			delayField.setEnabled(true);
		}
	}
	
	@Override
	public void numberChanged(NumberOnlyTextField field, float oldNum, float newNum) {
		// When delay field changes
		timer.setDelay(field.getNumberInt());
	}
	
	private boolean finishedPercolation() {
		PercolationChunkInfo finalInfo = editorPanel.getFinalPercolationChunkInfo();
		final int totalSteps = (int) finalInfo.getProperty(PROPERTY.CURRENT_STEP);
		final int currStep = pchunk.getCurrentStep();
		
		return currStep == totalSteps;
	}
	
	private int stepForward() {
		blocksAddedLast = pchunk.stepForward();

		// Update other panels
		editorPanel.updateAll();
		return blocksAddedLast;
	}
	
	private int stepBack() {
		final int numRemoved = pchunk.stepBack();
		blocksAddedLast = -numRemoved; // Negate
		// Update other panels
		editorPanel.updateAll();
		return numRemoved;
	}
	
	private void handleTimerPauseButtonState() {
		timerPauseButton.setEnabled(
			// If timer is going backward
			(timerBackButton.isSelected() && pchunk.getCurrentStep() > 0) 
			||
			// If timer is going forward
			(timerForwardButton.isSelected() && !finishedPercolation())
		);
		
		// Also update finishButton, which is enabled/disabled
		// at the same time as the timerPauseButton
		finishButton.setEnabled(timerPauseButton.isEnabled());
	}
	
	private void makeTimer() {
		timer = new Timer(1000, null);
		timer.setInitialDelay(0);
		timer.addActionListener(e -> {
			if (timerForwardButton.isSelected()) {
				stepForward();
			} else {
				stepBack();
			}
		});
	}
	
	private JPanel makeStepButtonPanel() {
		JPanel buttonPanel = StyleManager.newPanel();
		stepBackButton = StyleManager.newButton("Step Back");
		stepForwardButton = StyleManager.newButton("Step Forward");
		restartButton = StyleManager.newButton("");
		restartButton.setIcon(restartIcon);
		restartButton.setPreferredSize(new Dimension(50, 27));
		restartButton.setToolTipText("Restart Simulation");
		
		buttonPanel.add(stepBackButton, "span 1, 2");
		buttonPanel.add(stepForwardButton);
		buttonPanel.add(restartButton);
		
		stepForwardButton.addActionListener(e -> {
			stepForward();
		});
		
		stepBackButton.addActionListener(e -> {
			stepBack();
		});
		
		restartButton.addActionListener(e -> {
			pchunk.clearWater();
			pchunk.populateTopRowWithWater();
			editorPanel.updateAll();
		});
		
		return buttonPanel;
	}
	
	private JPanel makeTimerPanel() {
		MigLayout timerPanelLay = new MigLayout();
		timerPanelLay.setRowConstraints("[]5[]");
		timerPanel = StyleManager.newPanel(timerPanelLay);
//		timerPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		timerPanel.setBorder(BorderFactory.createTitledBorder("Timer Controls"));
		
		// Delay label and field
		delayTextArea = StyleManager.newHeaderTextArea("Delay (ms):");
		delayField = StyleManager.newNumberOnlyTextField();
		delayField.setAllowNegatives(false); // No such thing as negative time (yet)
		delayField.setAllowDecimals(false);
		delayField.setText("" + timer.getDelay());
		delayField.getNumberChangeListeners().add(this);
		
		// Timer back and forward buttons
		timerBackButton = StyleManager.newToggleButton("Backward", false);
		timerBackButton.addActionListener(e -> {
			handleTimerPauseButtonState();
		});
		
		timerForwardButton = StyleManager.newToggleButton("Forward", true);
		timerForwardButton.addActionListener(e -> {
			handleTimerPauseButtonState();
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(timerBackButton);
		group.add(timerForwardButton);

		finishButton = StyleManager.newButton("Finish");
		finishButton.addActionListener(e -> {
			if (timerForwardButton.isSelected())
				while (stepForward() > 0);
			else
				while (stepBack() > 0);
		});
		
		// Timer pause button
		timerPauseButton = StyleManager.newButton("");
		timerPauseButton.setIcon(playIcon);
		timerPauseButton.setMaximumSize(new Dimension(50, 30));
		timerPauseButton.addActionListener(e -> {
			if (!timerIsPaused) {
				timerIsPaused = true;
				// Update icon
				timerPauseButton.setIcon(playIcon);
				// Update timer
				timer.stop();
				// Enable delay textfield
				delayField.setEnabled(true);
			} else {
				timerIsPaused = false;
				// Update pause button icon
				timerPauseButton.setIcon(pauseIcon);
				// Update timer
				timer.start();
				// Disable delay textfield
				delayField.setEnabled(false);
			}
		});
		
		timerPanel.add(delayTextArea);
		timerPanel.add(delayField, "span, wrap");
		timerPanel.add(timerBackButton);
		timerPanel.add(timerPauseButton);
		timerPanel.add(timerForwardButton, "wrap");
		timerPanel.add(finishButton, "cell 0 2, center, span");
				
		return timerPanel;
	}

}
