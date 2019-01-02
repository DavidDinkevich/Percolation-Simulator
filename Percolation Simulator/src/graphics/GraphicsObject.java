package graphics;

import utils.Vec2;

public abstract class GraphicsObject implements Drawable {
	
	private Vec2.Mutable loc, veloc, accel;
	private Vec2.Mutable rotation;
	private Brush.Builder brush;
	
	public GraphicsObject(Vec2 loc, Brush brush) {
		this.loc = new Vec2.Mutable(loc);
		this.brush = new Brush.Builder(brush);
		veloc = new Vec2.Mutable();
		accel = new Vec2.Mutable();
		rotation = new Vec2.Mutable();
	}
	
	public GraphicsObject(Vec2 loc) {
		this(loc, new Brush());
	}
	
	public GraphicsObject() {
		this(Vec2.ZERO);
	}
	
	@Override
	public void draw(Layer lay) {
		lay.setBrush(brush);
	}
	
	public void update(Layer3D lay) {
		veloc.add(accel);
		loc.add(veloc);
		veloc.mult(0f);
	}
	
	public Vec2.Mutable getLoc() {
		return loc;
	}
	
	public void setLoc(Vec2 loc) {
		this.loc.set(loc);
	}

	public Vec2 getVelocity() {
		return veloc;
	}
	
	public void setVelocity(Vec2 veloc) {
		this.veloc.set(veloc);
	}

	public Vec2 getAcceleration() {
		return accel;
	}
	
	public void setAcceleration(Vec2 accel) {
		this.accel.set(accel);
	}
	
	public Brush.Builder getBrush() {
		return brush;
	}
	
	public void setBrush(Brush newBrush) {
		brush.set(newBrush);
	}
	
	public Vec2 getRotation() {
		return rotation;
	}
	
	public void setRotation(Vec2 rot) {
		rotation.set(rot);
	}

}
