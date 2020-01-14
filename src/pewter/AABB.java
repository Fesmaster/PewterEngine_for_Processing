package pewter;

public class AABB{
	public Vec2 min = new Vec2();
	public Vec2 max = new Vec2();
	public AABB copy() {
		AABB r = new AABB();
		r.min = this.min.copy();
		r.max = this.max.copy();
		return r;
	}
	//snap the AABB to be at 0, 0
	public void zero() {
		Vec2 offset = origin().invt();
		min.set(min.add(offset));
		max.set(max.add(offset));
	}
	public Vec2 origin() {
		return new Vec2(max.x-min.x, max.y-min.y);
	}
	public void addPos(Vec2 p) {
		min.set(min.add(p));
		max.set(max.add(p));
	}
}
