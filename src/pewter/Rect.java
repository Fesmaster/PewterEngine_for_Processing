package pewter;
import processing.core.*;
import java.util.*;

public class Rect extends gameObj {
	public int c;
	public Rect(Vec2 a, Vec2 b){
		super(a);
		shape.set(Shape.RECT(this, b));
		c = POwner.color(255);
	}
	public Rect(float x, float y, float w, float h){
		super(x, y);
		shape.set(Shape.RECT(this, new Vec2(w, h)));
		c = POwner.color(255);
	}
	public void draw() {
		ArrayList<Vertex> verts = shape.verts;
		Pewter.debug("Rect: verts len:"+verts.size());
		POwner.fill(c);
		//POwner.noStroke();
		POwner.beginShape(POwner.QUADS);
		for(int i=0;i<verts.size();i++) {
			Vertex v = verts.get(i);
			POwner.vertex(v.x, v.y, v.u, v.v);
		}
		POwner.endShape(POwner.CLOSE);
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
