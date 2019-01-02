package graphics;

import utils.Vec3;

public class Text3D extends GraphicsObject3D {

	private String text;
	private float textSize;
	
	public Text3D(Vec3 loc, Brush brush, String text, float textSize) {
		super(loc, brush);
		init(text, textSize);
	}
	
	public Text3D(Vec3 loc, String text, float textSize) {
		super(loc);
		init(text, textSize);
	}
	
	public Text3D(Vec3 loc, String text) {
		super(loc);
		init(text, 20);
	}
	
	public Text3D() {
		init("", 20);
	}
	
	private void init(String text, float textSize) {
		this.text = text;
		this.textSize = Math.abs(textSize);
	}
	
	
	@Override
	public void draw(Layer lay) {
		super.draw(lay);
		
		Layer3D lay3d = (Layer3D) lay;
		lay3d.text(getLoc(), text, textSize);
	}
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = Math.abs(textSize);
	}
	
	

}



















