package ui;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import ui.PercolationEditorPanel.SubPanel;

import utils.Dimension3;


@SuppressWarnings("serial")
public class PercolationChunkSizeEditor extends SubPanel 
implements NumberChangeListener {

	private NumberOnlyTextField sizeWidthField;
	private NumberOnlyTextField sizeHeightField;
	private NumberOnlyTextField sizeDepthField;
	
	private JTextArea blockConfigBoxTextArea;
	private JComboBox<String> blockConfigBox;
	private Map<String, Dimension3> blockConfigBoxValues;
	
	private JSlider gapWidthSlider;
	private JSlider gapHeightSlider;
	private JSlider gapDepthSlider;
	private JTextArea gapWidthTextArea;
	private JTextArea gapHeightTextArea;
	private JTextArea gapDepthTextArea;
	
	private JButton updateButton;
	private boolean chunkGapsChanged;
	
	public PercolationChunkSizeEditor(PercolationEditorPanel editorPanel) {
		super(editorPanel);
		
		setBackground(StyleManager.PANEL_BACKGROUND);
		
		MigLayout lay = new MigLayout();
		setLayout(lay);
		lay.setRowConstraints("[]3[]7[]0[]");
		
		// Width, height, and depth text boxes
		sizeWidthField = newNumberOnlyTextField();
		sizeHeightField = newNumberOnlyTextField();
		sizeDepthField = newNumberOnlyTextField();
				
		add(StyleManager.newHeaderTextArea("Chunk Size"), "center, span, wrap");
		add(StyleManager.newTextArea("Width"));
		add(sizeWidthField, "span 2");
		add(StyleManager.newTextArea("Height"));
		add(sizeHeightField, "span 2");
		add(StyleManager.newTextArea("Depth"));
		add(sizeDepthField, "span 2, wrap");
		
		add(makeUpdateButtonPanel(), "span, wrap");

		add(makeGapsPanel(), "span, center, wrap");
	}
	
	private JPanel makeUpdateButtonPanel() {
		blockConfigBoxTextArea = StyleManager.newHeaderTextArea("Common:");
		blockConfigBoxTextArea.setColumns(3);
		
		// Create combo box values
		blockConfigBoxValues = new LinkedHashMap<>();
		blockConfigBoxValues.put("9 x 9 x 9", new Dimension3(9f));
		blockConfigBoxValues.put("20 x 3 x 20", new Dimension3(20f, 3f, 20f));
		blockConfigBoxValues.put("20 x 20 x 1", new Dimension3(20f, 20f, 1f));
		blockConfigBoxValues.put("100 x 100 x 1", new Dimension3(100f, 100f, 1f));
		
		// Create combo box
		blockConfigBox = StyleManager.newComboBox();
		// Center Strings in the combo box
		JLabel renderer = (JLabel) blockConfigBox.getRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);

		// Load keys into combo box
		for (String key : blockConfigBoxValues.keySet())
			blockConfigBox.addItem(key);
		
		blockConfigBox.addActionListener(e -> {
			String selected = (String) blockConfigBox.getSelectedItem();
			Dimension3 size = blockConfigBoxValues.get(selected);
			sizeWidthField.setText("" + (int) size.getWidth());
			sizeHeightField.setText("" + (int) size.getHeight());
			sizeDepthField.setText("" + (int) size.getDepth());
			resizeChunk();
		});
				
		// Update button
		updateButton = StyleManager.newButton("Update");
		updateButton.setEnabled(false);
		updateButton.addActionListener(e -> { updateChunk(); });
		
		MigLayout lay = new MigLayout();
		lay.setColumnConstraints("[]6[]30[]");
		JPanel panel = StyleManager.newPanel(lay);
		panel.add(blockConfigBoxTextArea, "left");
		panel.add(blockConfigBox, "left");
		panel.add(updateButton, "right, east");
		return panel;
	}
	
	private JPanel makeGapsPanel() {	
		// LABELS
		gapWidthTextArea = StyleManager.newTextArea("");
		gapHeightTextArea = StyleManager.newTextArea("");
		gapDepthTextArea = StyleManager.newTextArea("");
				
		// GAPS: width, height, depth sliders
		gapWidthSlider = newSlider();
		gapHeightSlider = newSlider();
		gapDepthSlider = newSlider();
				
		MigLayout lay = new MigLayout();
		lay.setRowConstraints("[]0[]");
		lay.setColumnConstraints("[]7[]0[]");
		JPanel panel = StyleManager.newPanel(lay);
		panel.add(StyleManager.newHeaderTextArea("Gap"), "span, center, wrap");
		panel.add(StyleManager.newHeaderTextArea("Width:"));
		panel.add(gapWidthTextArea);
		panel.add(gapWidthSlider, "span, wrap");
		panel.add(StyleManager.newHeaderTextArea("Height:"));
		panel.add(gapHeightTextArea);
		panel.add(gapHeightSlider, "span, wrap");
		panel.add(StyleManager.newHeaderTextArea("Depth:"));
		panel.add(gapDepthTextArea);
		panel.add(gapDepthSlider, "span, wrap");
		
		return panel;
	}
	
	/**
	 * A NumberOnlyTextField with settings that all TextFields in this
	 * panel have (convenience)
	 */
	private NumberOnlyTextField newNumberOnlyTextField() {
		
		NumberOnlyTextField field = StyleManager.newNumberOnlyTextField();
		field.addActionListener(e -> { updateChunk(); });
		field.getNumberChangeListeners().add(this);

		field.setAllowDecimals(false);
		field.setAllowNegatives(false);
		
		// Disallow 0 for all chunk size fields (not a valid dimension)
		field.getDisallowedChars().add('0');

		return field;
		
	}
	
	/**
	 * A JSlider with settings that all JSliders in this panel have
	 * (convenience)
	 */
	private JSlider newSlider() {
		JSlider slider = StyleManager.newSlider(0, 200, 0);
		slider.addChangeListener(e -> {
			chunkGapsChanged = true;
			updateChunk();
		});
		return slider;
	}
	
	/**
	 * Returns true if one of the given {@link JTextField}s are empty.
	 */
	private boolean textFieldIsEmpty(JTextField... fields) {
		for (JTextField field : fields) {
			if (field.getText().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void numberChanged(NumberOnlyTextField field, float oldNum, float newNum) {
		// When a number field is modified, enable the update button
		updateButton.setEnabled(true);
	}
	
	private void updateChunk() {
		if (!pchunk.isInitialized())
			return;
		
		// Do nothing if one of the fields is empty
		if (textFieldIsEmpty(sizeWidthField, sizeHeightField, sizeDepthField)) {			
			return;
		}

		// Determine which fields have been modified, and see if we need
		// to update the chunk's size, gaps, or both
		
		if (updateButton.isEnabled()) {
			// Resize the chunk
			resizeChunk();
		}
		if (chunkGapsChanged)
			resetGaps();
		
		// Disable update button (we're now up to date)
		updateButton.setEnabled(false);
		
	}
	
	private void resizeChunk() {
		pchunk.reset();
		
		final int sx = sizeWidthField.getNumberInt();
		final int sy = sizeHeightField.getNumberInt();
		final int sz = sizeDepthField.getNumberInt();
		
		pchunk.setNumBlocks(new Dimension3(sx, sy, sz));
		pchunk.init();
		pchunk.randomizeBlockTypes();
		pchunk.populateTopRowWithWater();
		
		// Update info
		editorPanel.updatePercolationChunkInfo();
		
		// Update
		editorPanel.updateAll();
		
		// Enable/disable sliders
		gapWidthSlider.setEnabled(sx > 1);
		gapHeightSlider.setEnabled(sy > 1);
		gapDepthSlider.setEnabled(sz > 1);
	}
	
	private void resetGaps() {
		// Update label
		gapWidthTextArea.setText("" + gapWidthSlider.getValue());
		gapHeightTextArea.setText("" + gapHeightSlider.getValue());
		gapDepthTextArea.setText("" + gapDepthSlider.getValue());
					
		// Update chunk
		pchunk.setSpacing(new Dimension3(
			gapWidthSlider.getValue(),
			gapHeightSlider.getValue(),
			gapDepthSlider.getValue()
		));
	}
	
	@Override
	public void update() {
		Dimension3 size = pchunk.getNumBlocks();
				
		sizeWidthField.setText("" + (int) size.getWidth());
		sizeHeightField.setText("" + (int) size.getHeight());
		sizeDepthField.setText("" + (int) size.getDepth());
		
		Dimension3 gap = pchunk.getSpacing();

		gapWidthSlider.setValue((int) gap.getWidth());
		gapWidthTextArea.setText("" + (int) gap.getWidth());
		gapHeightSlider.setValue((int) gap.getHeight());
		gapHeightTextArea.setText("" + (int) gap.getHeight());
		gapDepthSlider.setValue((int) gap.getDepth());
		gapDepthTextArea.setText("" + (int) gap.getDepth());
	}
	
}
