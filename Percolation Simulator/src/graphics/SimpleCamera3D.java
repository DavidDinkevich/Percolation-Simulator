package graphics;

import processing.event.MouseEvent;

import utils.Utils;
import utils.Vec3;

public class SimpleCamera3D implements Camera {
	
	private Layer3D layer;

	public SimpleCamera3D(Layer3D layer) {
		this.layer = layer;
	}

	@Override
	public void mouseDragged() {
		Canvas canvas = layer.getParentCanvas();
		final float dx = canvas.pmouseX - canvas.mouseX;
		final float dy = canvas.pmouseY - canvas.mouseY;
		final float angleX = Utils.map(dx, 0f, 800f, 0f, Utils.TWO_PI);
		final float angleY = Utils.map(dy, 0f, 800f, 0f, Utils.TWO_PI);
		
		Vec3 rot = new Vec3(
				layer.getRotation().getX() + angleY,
				layer.getRotation().getY() + angleX,
				layer.getRotation().getZ()
		);
		layer.setRotation(rot);		
	}

	@Override
	public void mouseWheel(MouseEvent e) {
		final float scrollDir = (float)e.getCount();
		final float newScale = scrollDir/100f;	
		
		// Change scale
		layer.setScale(Vec3.add(layer.getScale(), newScale));
	}

	@Override
	public void keyPressed() {}

	@Override
	public void mousePressed() {}

	@Override
	public Layer getLayer() {
		return layer;
	}
	
}
