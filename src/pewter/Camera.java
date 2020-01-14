package pewter;
/*
 * this class holds positional and rotational transforms for the camera viewing the world. It is a singleton
 */
public class Camera {
	static private Vec2 pos = new Vec2();
	static private float angle = 0;
	static public void setPos(Vec2 p) {
		pos.set(p);
	}
	static public void setAngle(float a) {
		angle = a;
	}
	static public Vec2 getPos() {
		return pos.copy();
	}
	static public float getAngle() {
		return angle;
	}
	private Camera() {} //so it cannot be constructed
}
