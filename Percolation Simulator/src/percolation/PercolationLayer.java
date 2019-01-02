package percolation;

import graphics.Layer3D;
import graphics.SimpleCamera3D;

import main.MainWindow;

import utils.Dimension;
import utils.Dimension3;
import utils.Vec2;
import utils.Vec3;


public class PercolationLayer extends Layer3D {
	
	private MainWindow mainWindow;

	private PercolationChunk chunk;
		
	private Vec3 defaultRotation;
	private boolean returnToDefRot;

	public PercolationLayer(MainWindow window) {
		super(
				window.getCanvas(), 
				Vec2.ZERO, 
				new Dimension(window.getCanvas().width, window.getCanvas().width)
		);
		mainWindow = window;
	}
	
	public void init() {
		super.init();
		
		// Start at a tilt
		defaultRotation = new Vec3(-0.5f, 2f, 0.0f);
		
		setCamera(new SimpleCamera3D(this));

		setScale(new Vec3(0.7f, 0.7f, 0.7f));
		
		// CHUNK
		chunk = new PercolationChunk(Vec3.ZERO, new Dimension3(9f), 
				new Dimension3(50), 0.4f);
		chunk.init();
		chunk.randomizeBlockTypes();
		chunk.populateTopRowWithWater();
		
		getDrawables().add(chunk);
				
		setRotation(Vec3.mult(defaultRotation, 2f));
		returnToDefRot = true; // Initial animation
		
//		setBackground(Brush.LIGHT_BLUE);
		setBackground(50, 150, 255, 255f);
		
		// Updates UI panels
		mainWindow.getPercolationEditorPanel().setPercolationChunk(chunk);
	}
	
	@Override
	public void beforeObjectsDrawn() {
		makeLights();
		
		super.beforeObjectsDrawn();
				
		getGraphics().pushMatrix();
				
		returnToDefRotAnim(); // Animate back to default rotation
		
		getGraphics().popMatrix();
	}
	
	@Override
	public void keyPressed() {
		super.keyPressed();
		
		if (getParentCanvas().keysAreDown(' ')) {
			returnToDefRot = !returnToDefRot;
		}
	}
	
	private void makeLights() {
		final int directionalLightBrightness = 255;
		final int ambientLightBrightness = 200-30;
		
		getGraphics().directionalLight(
				directionalLightBrightness,
				directionalLightBrightness,
				directionalLightBrightness,
				// Location
				0f, 0f, 1f
		);
		getGraphics().ambientLight(
				ambientLightBrightness,
				ambientLightBrightness, 
				ambientLightBrightness
		);
	}
	
	private void returnToDefRotAnim() {
		if (returnToDefRot) {	
			setRotation(Vec3.lerp(getRotation(), defaultRotation, 0.27f));
			
			// Check if we're done
			Vec3 diff = Vec3.sub(getRotation(), defaultRotation);
			
			returnToDefRot = diff.getMag() >= 0.003f;
		}
	}
	
	public PercolationChunk getPercolationChunk() {
		return chunk;
	}
	
}
