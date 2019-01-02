package ui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import percolation.PercolationBlock.BLOCK_TYPE;

import ui.PercolationEditorPanel.SubPanel;

import utils.Dimension3;
import utils.Utils;

import static utils.Utils.round;

import static percolation.PercolationChunkUtils.getNumBlocks;
import static percolation.PercolationChunkUtils.getPercolatedWaterBlocks;

import static ui.StyleManager.newHeaderTextArea;
import static ui.StyleManager.newTextArea;


@SuppressWarnings("serial")
public class PercolationChunkStatsPanel extends SubPanel
implements NumberChangeListener {
	
	private NumberOnlyTextField pTextField;
	private JTextArea totalBlocksValTextArea;
	private JTextArea closedBlocksValTextArea;
	private JTextArea openBlocksValTextArea;
	private JTextArea waterBlocksValTextArea;
	private JTextArea percValueTextArea;
	private JButton updateButton;
	private JButton randomizeButton;
	
	public PercolationChunkStatsPanel(PercolationEditorPanel editorPanel) {
		super(editorPanel);
		
		setBackground(StyleManager.PANEL_BACKGROUND);
		
		MigLayout lay = new MigLayout();
		setLayout(lay);
		lay.setColumnConstraints("[]5[]3[]5[]");
		
				
		JTextArea percTextArea = newHeaderTextArea("Percolation:");
		percValueTextArea = newTextArea("");
		percValueTextArea.setColumns(11);
		
		JTextArea totalBlocksTextArea = newHeaderTextArea("Total:");		
		totalBlocksValTextArea = newTextArea("");
		
		JTextArea closedBlocksTextArea = newHeaderTextArea("Closed:");
		closedBlocksValTextArea = newTextArea("");
		closedBlocksValTextArea.setColumns(11);
		
		JTextArea openBlocksTextArea = newHeaderTextArea("Open:");
		openBlocksValTextArea = newTextArea("");
		openBlocksValTextArea.setColumns(11);
		
		JTextArea waterBlocksTextArea = newHeaderTextArea("Water:");
		waterBlocksValTextArea = newTextArea("");
		waterBlocksValTextArea.setColumns(11);
		
		add(makePPanel(), "span 2");
		add(closedBlocksTextArea);
		add(closedBlocksValTextArea, "right, wrap");
		add(percTextArea);
		add(percValueTextArea);
		add(openBlocksTextArea, "cell 2 1");
		add(openBlocksValTextArea, "span, right, wrap");
		add(totalBlocksTextArea);
		add(totalBlocksValTextArea);
		add(waterBlocksTextArea, "right, cell 2 2");
		add(waterBlocksValTextArea, "wrap");
		add(makeBottomButtonsPanel(), "center, span");
	}

	@Override
	public void update() {
		// Update pTextField
		final float p = pchunk.getP();
		pTextField.setText("" + p);
		
		// Update percolation label
		Dimension3 psize = pchunk.getNumBlocks();
		final int faceArea = (int) (psize.getWidth() * psize.getDepth());
		final float numPercedBlocks = getPercolatedWaterBlocks(pchunk).size();
		final float percentPerced = numPercedBlocks / faceArea * 100f;
		final float roundedPercent =  Utils.round(percentPerced, 1);
		final int roundedNumPercedBlocks = (int) numPercedBlocks;
		percValueTextArea.setText(
				roundedNumPercedBlocks + " (" + roundedPercent + "%)");
		
		// Update total blocks label
		final int numBlocks = pchunk.getBlocks().size();
		totalBlocksValTextArea.setText("" + numBlocks);
		
		// Update closedBlocksValTextArea, openBlocksValTextArea,
		// waterBlocksValTextArea
		
		final int numClosed = getNumBlocks(pchunk, BLOCK_TYPE.CLOSED).size();
		final int numOpen = getNumBlocks(pchunk, BLOCK_TYPE.OPEN).size();
		final int numWater = numBlocks - numClosed - numOpen; // Faster
		
		final float closedPercent = round((float) numClosed / numBlocks * 100f, 1);
		final float openPercent = round((float) numOpen / numBlocks * 100f, 1);
		final float waterPercent = round((float) numWater / numBlocks * 100f, 1);
				
		closedBlocksValTextArea.setText(numClosed + " (" + closedPercent + "%)");
		openBlocksValTextArea.setText(numOpen + " (" + openPercent + "%)");
		waterBlocksValTextArea.setText(numWater + " (" + waterPercent + "%)");
	}

	@Override
	public void numberChanged(NumberOnlyTextField field, float oldNum, float newNum) {
		// If there have been updates to the PTextField, then enable
		// the update button
		updateButton.setEnabled(true);
	}
	
	private JPanel makePPanel() {
		JTextArea pTextArea = newHeaderTextArea("P:");
		pTextArea.setColumns(1);
		NumberOnlyTextField pTextField = makePTextField();
		
		MigLayout lay = new MigLayout();
		lay.setColumnConstraints("[]10[]");

		JPanel pPanel = StyleManager.newPanel(lay);
		pPanel.add(pTextArea, "west");
		pPanel.add(pTextField, "east");
		
		return pPanel;
	}
	
	private NumberOnlyTextField makePTextField() {
		
		pTextField = StyleManager.newNumberOnlyTextField();
		pTextField.setColumns(10);
		pTextField.setAllowNegatives(false);
		pTextField.getNumberChangeListeners().add(this);
		// Reset P on Enter
		pTextField.addActionListener(getResetPActionListener());

		return pTextField;
		
	}
	
	private JButton makeUpdateButton() {
		updateButton = StyleManager.newButton("Update");
		updateButton.setEnabled(false);
		// Reset P
		updateButton.addActionListener(getResetPActionListener());
		return updateButton;
	}
	
	private JButton makeRandomizeButton() {
		randomizeButton = StyleManager.newButton("Randomize");
		
		randomizeButton.addActionListener(e -> {
			pchunk.randomizeBlockTypes();
			pchunk.populateTopRowWithWater();
			editorPanel.updateAll();
		});
		
		return randomizeButton;
	}
	
	private JPanel makeBottomButtonsPanel() {
		MigLayout lay = new MigLayout();
		lay.setColumnConstraints("[]10[]");

		JPanel bottomButtonsPanel = StyleManager.newPanel(lay);
		bottomButtonsPanel.add(makeUpdateButton(), "west");
		bottomButtonsPanel.add(makeRandomizeButton(), "east");

		return bottomButtonsPanel;
	}
	
	private ActionListener getResetPActionListener() {
		return e -> {
			// Don't want to reset P if we don't have to
			// (updateButton being disabled means that P in text field
			// is the same as the actual P)
			if (!updateButton.isEnabled()) {
				return;
			}
			
			final float newP = pTextField.getNumberFloat();
			pchunk.setP(newP);
			
			// Randomize
			randomizeButton.doClick(0);
			
			// Disable update button
			updateButton.setEnabled(false);
			
			// Update other panels
			editorPanel.updateAll();
		};
	}
	
}
