package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import graphics.Canvas;

import percolation.PercolationCanvas;

import ui.PercolationEditorPanel;


/**
 * Main class for the program.
 * @author David Dinkevich
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	public static final String APP_NAME;
	
	static {
		APP_NAME = "Percolation Simulator";		
	}
	
	private boolean windowCreated = false;
	
	private Canvas canvas;
	private PercolationEditorPanel editorPanel;
	
	public MainWindow() {
		super(APP_NAME);
	}
	
	public void createWindow(Dimension size) {		
		if (windowCreated)
			throw new IllegalStateException("Cannot create the window twice");

		windowCreated = true;

//		setIconImage(makeIcon());
		URL iconURL = MainWindow.class.getResource("/app_icon.png");
		setIconImage(new ImageIcon(iconURL).getImage());

		setLayout(new BorderLayout());
		
		JPanel canvasPanel = new JPanel(new BorderLayout());
		canvasPanel.setPreferredSize(size);
		
		utils.Dimension canvasSize = new utils.Dimension(size.width, size.height);
		canvas = new PercolationCanvas(this, canvasSize);

		canvasPanel.add(canvas, BorderLayout.CENTER);
		add(canvasPanel, BorderLayout.CENTER);
		canvas.init();
		
		editorPanel = new PercolationEditorPanel(getSize(), null);
		add(editorPanel, BorderLayout.WEST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(1010, 720);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	// http://www.java2s.com/Tutorial/Java/0240__Swing/SpecifyingWindowDecorations.htm
	protected static Image makeIcon() {
		final int width = 20, height = 20;
		
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Draw into it.
		Graphics g = bi.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("P", 6, 16);

		// Clean up.
		g.dispose();

		return bi;
	}

	public PercolationEditorPanel getPercolationEditorPanel() {
		return editorPanel;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainWindow win = new MainWindow();
			win.createWindow(new Dimension(700, 700));
			// Message displaying of estimated *p
			JOptionPane.showMessageDialog(win, "On a 9x9x9 block in which p "
					+ "(the probability that a site will be open) is 20%,"
					+ " percolation occurs an average of 1% of the time.", 
					"Estimated *p", JOptionPane.INFORMATION_MESSAGE
			);
		});
	}
	
}
