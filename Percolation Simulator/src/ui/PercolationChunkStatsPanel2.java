package ui;

import static ui.StyleManager.newHeaderTextArea;

import static utils.Utils.round;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import percolation.PercolationBlock.BLOCK_TYPE;
import percolation.PercolationChunkInfo.PROPERTY;
import percolation.PercolationChunkInfo;

import ui.PercolationEditorPanel.SubPanel;
import utils.Dimension3;
import utils.Utils;


@SuppressWarnings("serial")
public class PercolationChunkStatsPanel2 extends SubPanel
implements NumberChangeListener {
	
	private NumberOnlyTextField pTextField;
	private JButton updateButton;
	private JButton randomizeButton;
	
	private JTable dataTable;
	private CustomTableModel tableModel;

	public PercolationChunkStatsPanel2(PercolationEditorPanel panel) {
		super(panel);
		
		setBackground(StyleManager.PANEL_BACKGROUND);
				
		MigLayout lay = new MigLayout();
		setLayout(lay);
		lay.setColumnConstraints("[]5[]3[]5[]");

		add(makePPanel());
		add(makeUpdateButton());
		add(makeRandomizeButton(), "wrap");
		add(new JScrollPane(makeDataTable()), "span");
	}
	
	@Override
	public void update() {
		// Update pTextField
		final float p = pchunk.getP();
		pTextField.setText("" + p);
		
		// UPDATE TABLE COLUMNS
				
		// Update the "Current" column
		PercolationChunkInfo currentInfo = new PercolationChunkInfo(pchunk);
		updateColumn(1, currentInfo);
		
		// Update "Initial" column
		PercolationChunkInfo initInfo = editorPanel.getInitialPercolationChunkInfo();
		updateColumn(2, initInfo);
		
		// Update "Final" column
		PercolationChunkInfo finalInfo = editorPanel.getFinalPercolationChunkInfo();
		updateColumn(3, finalInfo);
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
		pTextField.setColumns(20);
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
			// Update
			editorPanel.updatePercolationChunkInfo();
			editorPanel.updateAll();
		});
		
		return randomizeButton;
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

	
	/**
	 * Convenience method for {@link #updateColumn(int, PercolationChunkInfo)}
	 */
	private String formatBlockComposition(
			PercolationChunkInfo info, BLOCK_TYPE type, PROPERTY prop) {

		final float numBlocks = info.getProperty(PROPERTY.TOTAL_BLOCKS);
		final float numType = info.getProperty(prop);
		final float percent = round(numType / numBlocks * 100f, 1);

		String str = (int) numType + " (" + percent + "%)";
		
		return str;
	
	}
	
	/**
	 * Convenience method for {@link #updateColumn(int, PercolationChunkInfo)}
	 */
	private String formatPercolation(PercolationChunkInfo info) {
		
		Dimension3 totalBlocks = pchunk.getNumBlocks();
		final int faceArea = (int) (totalBlocks.getWidth() * totalBlocks.getDepth());
		final float numPerced = info.getProperty(PROPERTY.PERCOLATED_BLOCKS);
		final float percentPerced = numPerced / faceArea * 100f;
		final float roundedPercent =  Utils.round(percentPerced, 1);
		
		return (int) numPerced + " (" + roundedPercent + "%)";
	}
	
	private void updateColumn(int colIndex, PercolationChunkInfo info) {
		// Total num blocks
		String numBlocksText = "" + (int) info.getProperty(PROPERTY.TOTAL_BLOCKS);
		
		// Closed, open, and water blocks
		String closedBlocksText = formatBlockComposition(info, 
				BLOCK_TYPE.CLOSED, PROPERTY.CLOSED_BLOCKS);
		String openBlocksText = formatBlockComposition(info, 
				BLOCK_TYPE.OPEN, PROPERTY.OPEN_BLOCKS);
		String waterBlocksText = formatBlockComposition(info, 
				BLOCK_TYPE.WATER, PROPERTY.WATER_BLOCKS);

		// Determine percolation
		String percText = formatPercolation(info);
		
		String[] column = {
				// Total blocks
				numBlocksText,
				// Closed blocks
				closedBlocksText,
				// Open blocks
				openBlocksText,
				// Water blocks
				waterBlocksText,
				// Percolation
				percText
		};
				
		for (int i = 0; i < column.length; i++) {
			dataTable.setValueAt(column[i], i, colIndex);
		}
	}

	
	private JTable makeDataTable() {
		tableModel = new CustomTableModel();
		dataTable = new JTable(tableModel);
		// Prevents reordering
		dataTable.getTableHeader().setReorderingAllowed(false);
		
		String[] colTitles = { "", "Current", "Initial", "Final" };
				
		for (int i = 0; i < dataTable.getColumnCount(); i++) {
			TableColumnModel model = dataTable.getColumnModel();
			TableColumn col = model.getColumn(i);
			col.setHeaderValue(colTitles[i]);		
			col.setCellRenderer(new CustomColumnCellRenderer());
		}

		return dataTable;
	}
	
	private static class CustomTableModel extends AbstractTableModel {
		
		private Object[][] data;
		
		public CustomTableModel() {
			data = new Object[][] {
					{ "Total", "", "", "" },
					{ "Closed", "", "", "" },
					{ "Open", "", "", "" },
					{ "Water", "", "", "" },
					{ "Percolation", "", "", "" }
			};
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}
		
		@Override
		public void setValueAt(Object obj, int row, int col) {
			data[row][col] = obj;
			fireTableCellUpdated(row, col);
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public boolean isCellEditable(int x, int y) {
			return false;
		}
	}
	
	// https://stackoverflow.com/questions/5673430/java-jtable-change-cell-color
	public class CustomColumnCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, 
				Object value, boolean isSelected, boolean hasFocus,
				int row, int col) {

			// Cells are by default rendered as a JLabel, and this table
			// is no exception
			JLabel label = (JLabel) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, col);

			// Special color for the first column, as it is also a header
			if (col == 0) {
				label.setBackground(StyleManager.PANEL_BACKGROUND);
			}
			
			// Bottom right cell is final percolation. If it is 0, then paint
			// the cell red
			else if (col == table.getColumnCount()-1 && row == table.getRowCount()-1) {
				String text = label.getText();
				if (!text.isEmpty()) {
					// Extract number in between of parenthesis. This is the percent
					// of percolation
					String betweenParenthesis = text.substring(
							text.indexOf("(") + 1, text.indexOf("%)")
					);
					
					final float num = Float.parseFloat(betweenParenthesis);
					
					if (num == 0f) {
						label.setBackground(Color.RED);
						label.setForeground(Color.WHITE);
					}
					else if (num >= 50f){
						label.setBackground(Color.GREEN);
						label.setForeground(Color.BLACK);
					}
					else {
						label.setBackground(Color.YELLOW);
						label.setForeground(Color.BLACK);
					}
				}
			}
			else {
				label.setBackground(Color.WHITE);
				label.setForeground(Color.BLACK);
			}

			// Return the JLabel which renders the cell.
			return label;
		}
	}
	
//	private class CustomTableHeader extends JTableHeader {
//		@Override
//		public void columnMoved(TableColumnModelEvent e) {
//			// - 1 to ignore 
//			final int fromIndex = e.getFromIndex();
//			final int toIndex = e.getToIndex();
//			
//			// Get the value that was originally at the toIndex
//			final int origToIndexVal = columnIndices[toIndex];
//			// Set the toIndex to the fromIndex
//			columnIndices[toIndex] = columnIndices[fromIndex];
//			columnIndices[fromIndex] = origToIndexVal;
//			
//			System.out.println(fromIndex + "-" + columnIndices[fromIndex] + ", " + toIndex + "-" + columnIndices[toIndex] );
//		}	
//	}
	
}
