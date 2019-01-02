package graphics;

import utils.Vec3;

public abstract class GraphicsObject3D implements Drawable {
	
	private Vec3.Mutable loc, veloc, accel;
	private Vec3.Mutable rotation;
	private Brush.Builder brush;
	
	public GraphicsObject3D(Vec3 loc, Brush brush) {
		this.loc = new Vec3.Mutable(loc);
		this.brush = new Brush.Builder(brush);
		veloc = new Vec3.Mutable();
		accel = new Vec3.Mutable();
		rotation = new Vec3.Mutable();
	}
	
	public GraphicsObject3D(Vec3 loc) {
		this(loc, new Brush());
	}
	
	public GraphicsObject3D() {
		this(Vec3.ZERO);
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
	
	public Vec3.Mutable getLoc() {
		return loc;
	}
	
	public void setLoc(Vec3 loc) {
		this.loc.set(loc);
	}

	public Vec3 getVelocity() {
		return veloc;
	}
	
	public void setVelocity(Vec3 veloc) {
		this.veloc.set(veloc);
	}

	public Vec3 getAcceleration() {
		return accel;
	}
	
	public void setAcceleration(Vec3 accel) {
		this.accel.set(accel);
	}
	
	public Brush.Builder getBrush() {
		return brush;
	}
	
	public void setBrush(Brush newBrush) {
		brush.set(newBrush);
	}
	
	public Vec3 getRotation() {
		return rotation;
	}
	
	public void setRotation(Vec3 rot) {
		rotation.set(rot);
	}

}
