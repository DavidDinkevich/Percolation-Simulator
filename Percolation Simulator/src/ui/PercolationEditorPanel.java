package ui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import percolation.PercolationChunk;
import percolation.PercolationChunkInfo;

@SuppressWarnings("serial")
public class PercolationEditorPanel extends JPanel {

	private PercolationChunk pchunk;
	
	private PercolationChunkSizeEditor sizeEditorPanel;
//	private PercolationChunkStatsPanel statsPanel;
	private PercolationChunkStatsPanel2 statsPanel2;
	private PercolationPlayerPanel playerPanel;
	
	private PercolationChunkInfo initialInfo, finalInfo;
	
	public PercolationEditorPanel(Dimension parentSize, PercolationChunk pc) {
		pchunk = pc;
		
		initialInfo = new PercolationChunkInfo();
		finalInfo = new PercolationChunkInfo();
	
		Dimension prefSize = new Dimension(310, parentSize.height);
		setPreferredSize(prefSize);
		setBackground(StyleManager.PANEL_BACKGROUND);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(6, 3, 6, 3));
		
		// PercolationChunkSizeEditor
		
		sizeEditorPanel = new PercolationChunkSizeEditor(this);
		sizeEditorPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Chunk"),
				BorderFactory.createLoweredSoftBevelBorder()
		));
		
		// PercolationPlayerPanel
		
		playerPanel = new PercolationPlayerPanel(this);
		playerPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Player"),
				BorderFactory.createLoweredSoftBevelBorder()
		));
				
		// PercolationChunkStatsPanel
		
//		statsPanel = new PercolationChunkStatsPanel(this);
//		statsPanel.setBorder(BorderFactory.createCompoundBorder(
//				BorderFactory.createTitledBorder("Stats"),
//				BorderFactory.createLoweredSoftBevelBorder()
//		));
		
		statsPanel2 = new PercolationChunkStatsPanel2(this);
		statsPanel2.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Stats"),
				BorderFactory.createLoweredSoftBevelBorder()
		));
		
		add(sizeEditorPanel);		
		add(playerPanel);
		add(statsPanel2);
		
		// Initial update
		updateAll();
		
	}
	
	public PercolationChunk getPercolationChunk() {
		return pchunk;
	}
	
	public void setPercolationChunk(PercolationChunk chunk) {
		pchunk = chunk;
		
		sizeEditorPanel.setPercolationChunk(pchunk);
		playerPanel.setPercolationChunk(pchunk);
		statsPanel2.setPercolationChunk(pchunk);
		
		// Update info
		updatePercolationChunkInfo();
		
		updateAll();
	}
	
	public void updateAll() {
		if (pchunk != null && pchunk.isInitialized()) {
			sizeEditorPanel.update();
//			statsPanel.update();
			playerPanel.update();
			statsPanel2.update();
		}
	}
	
	public void updatePercolationChunkInfo() {
		if (!pchunk.isInitialized())
			throw new IllegalStateException("PercolationChunk not initialized");
		
		// INITIAL INFO
		initialInfo.set(pchunk);

		// FINAL INFO
		
		// Fast forward until the end of the percolation process, record it,
		// and then go back to the current step
		final int currStep = pchunk.getCurrentStep();
		while (pchunk.stepForward() > 0);
		finalInfo.set(pchunk);
		while (pchunk.getCurrentStep() > currStep) {
			pchunk.stepBack();
		}
	}
	
	public PercolationChunkInfo getInitialPercolationChunkInfo() {
		return initialInfo;
	}
	
	public PercolationChunkInfo getFinalPercolationChunkInfo() {
		return finalInfo;
	}

	// Package private
	static abstract class SubPanel extends JPanel {
		
		protected PercolationEditorPanel editorPanel;
		protected PercolationChunk pchunk;
		
		public SubPanel(PercolationEditorPanel editorPanel) {
			this.editorPanel = editorPanel;
			pchunk = editorPanel.pchunk;
		}
		
		public abstract void update();
		
		public void setPercolationChunk(PercolationChunk chunk) {
			pchunk = chunk;
		}
		
		public PercolationChunk getPercolationChunk() {
			return pchunk;
		}

		public PercolationEditorPanel getPercolationEditorPanel() {
			return editorPanel;
		}

		public void setPercolationEditorPanel(PercolationEditorPanel editorPanel) {
			this.editorPanel = editorPanel;
		}
		
	}
	
}
