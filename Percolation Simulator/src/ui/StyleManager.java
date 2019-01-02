package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public final class StyleManager {
	
	public static final Color PANEL_BACKGROUND;
	public static final Font HEADER_FONT;
	public static final Font PLAIN_FONT;
	
	static {
		PANEL_BACKGROUND = new Color(238, 238, 238);
		HEADER_FONT = new Font("Arial", Font.BOLD, 12);
		PLAIN_FONT = new Font("Arial", Font.ITALIC, 12);
	}

	private StyleManager() {}
	
//	public static JLabel newLabel(String txt) {
//		JLabel label = new SpecialLabel(txt);
//		label.setFont(new Font("Dialog", Font.ITALIC, 12));
//		return label;
//	}
//	
//	public static JLabel newHeaderLabel(String txt) {
//		JLabel label = new SpecialLabel(txt);
//		label.setFont(new Font("Dialog", Font.BOLD, 12));
//		return label;		
//	}
	
	public static NumberOnlyTextField newNumberOnlyTextField() {
		NumberOnlyTextField field = new NumberOnlyTextField();
		field.setColumns(60);
		field.setHorizontalAlignment(JTextField.CENTER);
		return field;
	}
	
	public static JButton newButton(String text) {
		JButton button = new SpecialButton(text);
		button.setBackground(new Color(220, 220, 220));
		button.setForeground(new Color(50, 50, 50));
		return button;
	}

	public static JCheckBox newCheckBox(String string, boolean checked) {
		JCheckBox box = new JCheckBox(string, checked);
		box.setRolloverEnabled(false);
		return box;
	}
	
	public static JToggleButton newToggleButton(String str, boolean activated) {
		JToggleButton toggle = new JToggleButton(str, activated);
		toggle.setBackground(new Color(220, 220, 220));
		toggle.setForeground(new Color(50, 50, 50));
		return toggle;
	}
	
	public static JSlider newSlider(int min, int max, int value) {
		JSlider slider = new JSlider(min, max, value);
		return slider;
	}
	
	public static JTextArea newTextArea(String text) {
		JTextArea area = new SpecialTextArea(text);
		area.setEditable(false);
		area.setRows(1);
		area.setColumns(6);
		area.setBackground(PANEL_BACKGROUND);
		area.setFont(PLAIN_FONT);
		return area;
	}
	
	public static JTextArea newHeaderTextArea(String text) {
		JTextArea area = newTextArea(text);
		area.setFont(HEADER_FONT);
		return area;
	}
	
	public static JPanel newPanel(LayoutManager lay) {
		JPanel panel = new JPanel(lay);
		panel.setBackground(PANEL_BACKGROUND);
		return panel;
	}
	
	public static JPanel newPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(PANEL_BACKGROUND);
		return panel;
	}
	
	@SafeVarargs
	public static <T> JComboBox<T> newComboBox(T... items) {
		JComboBox<T> box = new JComboBox<>(items);
		box.setBackground(Color.WHITE);
		return box;
	}
	
	@SuppressWarnings("serial")
	public static class SpecialButton extends JButton {
		public SpecialButton(String str) {
			super(str);
		}
		
		@Override
		public void revalidate() {
			// Do nothing--this prevents a bug that causes other components
			// in the window to resize
		}

	}
	
	@SuppressWarnings("serial")
	public static class SpecialTextArea extends JTextArea {
		public SpecialTextArea(String str) {
			super(str);
		}
		
		@Override
		public void revalidate() {
			// Do nothing--this prevents a bug that causes other components
			// in the window to resize
		}

	}


}
