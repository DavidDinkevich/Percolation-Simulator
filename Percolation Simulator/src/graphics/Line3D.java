package graphics;

import utils.Vec3;

public class Line3D extends GraphicsObject3D {

	private Vec3.Mutable point2;
	
	public Line3D(Vec3 p1, Vec3 p2, Brush brush) {
		super(p1, brush);
		init(p2);
	}
	
	public Line3D(Vec3 p1, Vec3 p2) {
		super(p1);
		init(p2);
	}
	
	public Line3D() {
		init(Vec3.ZERO);
	}
	
	private void init(Vec3 p2) {
		point2 = new Vec3.Mutable(p2);
	}
	
	@Override
	public void draw(Layer lay) {
		super.draw(lay);
		
		Layer3D lay3d = (Layer3D) lay;
		lay3d.line(getLoc(), getPoint2());
	}
	
	public Vec3 getPoint2() {
		return point2;
	}
	
	public void setPoint2(Vec3 pt) {
		point2.set(pt);
	}

}
