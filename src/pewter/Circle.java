package pewter;

public class Circle extends gameObj{
	int c;
	public Circle(Vec2 p, float r) {
		super(p);
		shape.set(Shape.CIRCLE(this, r));
		c = POwner.color(255);
	}
	public Circle(float x, float y, float r) {
		super(x, y);
		shape.set(Shape.CIRCLE(this, r));
		c = POwner.color(255);
	}
	public void draw() {
		POwner.noStroke();
		POwner.fill(c);
		POwner.circle(0,  0, shape.radius*2);
		if(Pewter.enableDebugDrawing) {
			POwner.noFill();
			POwner.stroke(255, 0, 0);
			POwner.strokeWeight(getScreenPixelSize());
			AABB ab = shape.calcAABB();
			Vec2 p = getPos();
			POwner.pushMatrix();
			POwner.rotate(-getRot());
			POwner.rect(ab.min.x-p.x, ab.min.y-p.y, ab.max.x-ab.min.x, ab.max.y-ab.min.y);
			POwner.popMatrix();
			POwner.noStroke();
			POwner.fill(255, 0, 0);
			POwner.circle(0, 0, getScreenPixelSize()*5);
		}
	}
}
