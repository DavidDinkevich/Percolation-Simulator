package percolation;

import graphics.Canvas;

import main.MainWindow;

import ui.AxisLayer3D;

import utils.Dimension;


@SuppressWarnings("serial")
public class PercolationCanvas extends Canvas {

	private MainWindow mainWindow;
	
	private PercolationLayer percLay;
	private AxisLayer3D axisLayer;
	
	public PercolationCanvas(MainWindow mainWindow, Dimension size) {
		super(size);
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void setup() {
		super.setup();
		
		// Add PercolationLayer
		percLay = new PercolationLayer(mainWindow);
		getLayers().add(percLay);
		percLay.init();
		
		// Add axis PGraphics
		axisLayer = new AxisLayer3D(this, percLay);
		getLayers().add(axisLayer);
		axisLayer.init();
	}

}
