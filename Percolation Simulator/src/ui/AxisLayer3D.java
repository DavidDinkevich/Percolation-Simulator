package ui;

import java.util.Arrays;

import graphics.Brush;
import graphics.Canvas;
import graphics.Layer3D;
import graphics.Line3D;
import graphics.Text3D;

import utils.Dimension;
import utils.Vec2;
import utils.Vec3;

public class AxisLayer3D extends Layer3D {
	
	private Layer3D parentLayer;
	private Line3D xLine, yLine, zLine;
	private Text3D xLabel, yLabel, zLabel;
	
	private int xAxisCol, yAxisCol, zAxisCol;

	public AxisLayer3D(Canvas parentCanvas, Layer3D parentLayer) {
		super(parentCanvas, new Vec2(15f, 30f), new Dimension(150f, 180f));
		
		this.parentLayer = parentLayer;
		
		xAxisCol = Brush.RED;
		yAxisCol = Brush.GREEN;
		zAxisCol = Brush.BLUE;
	}
	
	@Override
	public void init() {
		super.init();
		
		setTransparent(true);
		
		Brush.Builder builder = new Brush.Builder();
		builder.setStrokeWeight(2f);
		builder.setFill(Brush.RED);
		
		/*
		 * AXIS LABELS
		 */
		
		// Draw x y z labels
		final float gapX = 30f;
		final float labelY = getSize().getHeight() / -2.2f;
		final float textSize = 20f;
		
		// Labels won't be added to drawables because we don't want them
		// to move, rotate, or scale
		
		builder.setFill(xAxisCol);
		xLabel = new Text3D(new Vec3(-gapX, labelY, 0f), builder, "x", textSize);
		builder.setFill(yAxisCol);
		yLabel = new Text3D(new Vec3(0f, labelY, 0f), builder, "y", textSize);
		builder.setFill(zAxisCol);
		zLabel = new Text3D(new Vec3(gapX, labelY, 0f), builder, "z", textSize);

		/*
		 * AXIS LINES
		 */
		
		// Move down a bit
		setTranslation(new Vec3(0f, labelY * 0.2f, 0f));
		
		final float len = getSize().getWidth() * 0.4f;
		
		builder.setStroke(xAxisCol);
		xLine = new Line3D(Vec3.ZERO, new Vec3(len, 0f, 0f), builder);
		builder.setStroke(yAxisCol);
		yLine = new Line3D(Vec3.ZERO, new Vec3(0f, len, 0f), builder);
		builder.setStroke(zAxisCol);
		zLine = new Line3D(Vec3.ZERO, new Vec3(0f, 0f, len), builder);
				
		getDrawables().addAll(Arrays.asList(xLine, yLine, zLine));
	}
	
	@Override
	public void beforeObjectsDrawn() {
		// Draw labels
		getGraphics().pushMatrix();
		translate(getTranslation());
		getGraphics().scale(1f, -1f, 1f); // Flip y axis so chars not upside-down
		xLabel.draw(this);
		yLabel.draw(this);
		zLabel.draw(this);
		getGraphics().popMatrix();

		// Copy rotation of parent Layer3D
		setRotation(parentLayer.getRotation());
		
		super.beforeObjectsDrawn();
	}

	public void setAxisColors(int x, int y, int z) {
		xLine.getBrush().setStroke(x);
		xLabel.getBrush().setFill(x);
		yLine.getBrush().setStroke(y);
		yLabel.getBrush().setFill(y);
		zLine.getBrush().setStroke(z);
		zLabel.getBrush().setFill(z);
	}

	public Layer3D getParentLayer() {
		return parentLayer;
	}

	public void setParentLayer(Layer3D parentLayer) {
		this.parentLayer = parentLayer;
	}

	public Line3D getXAxisLine() {
		return xLine;
	}

	public Line3D getYAxisLine() {
		return yLine;
	}

	public Line3D getZAxisLine() {
		return zLine;
	}

}
