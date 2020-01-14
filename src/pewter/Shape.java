package pewter;
//import pewter.Vec2;
import java.util.*;

import processing.core.PApplet;

/*
 * This is basically a Mesh class for 3d games. It ODES NOT contain render code, it just stores the verts and stuff. 
 */

public class Shape {
	public float radius = 0;
	public AABB AABB = new AABB();
	public boolean is_circle = true;
	public ArrayList<Vertex> verts = new ArrayList<Vertex>();
	public ArrayList<Vec2> normals = new ArrayList<Vec2>();
	public PApplet POwner = null;
	public gameObj owner = null;
	public Shape(gameObj o){
		this.owner = o;
		POwner = o.POwner;
	}
	public Shape(Shape o){
		radius = o.radius;
		AABB = o.AABB.copy();
		is_circle = o.is_circle;
		this.verts.clear();
		for (int i=0;i<o.verts.size();i++) {
			verts.add(o.verts.get(i).copy());
		}
		this.normals.clear();
		for (int i=0;i<o.normals.size();i++) {
			normals.add(o.normals.get(i).copy());
		}
		POwner = o.POwner;
		owner = o.owner;
	}
	public Shape copy() {
		return new Shape(this);
	}
	public void set(Shape o) {
		radius = o.radius;
		AABB = o.AABB.copy();
		is_circle = o.is_circle;
		this.verts.clear();
		for (int i=0;i<o.verts.size();i++) {
			verts.add(o.verts.get(i).copy());
		}
		this.normals.clear();
		for (int i=0;i<o.normals.size();i++) {
			normals.add(o.normals.get(i).copy());
		}
		//POwner = o.POwner;
		//owner = o.owner;
	}
	public boolean validate() {
		//this is a complex function to ensure that we have a *good* shape and its not broken
		if (verts.size() != normals.size()) {
			return false;
		}
		if (POwner == null) {
			return false;
		}
		if (owner == null) {
			return false;
		}
		if ((is_circle)&&(radius <= 0)) {
			return false;
		}
		
		return true;
	}
	//calculate and store the axis-alligned bounding box for this shape, and, if it has an owner, use that owner's scale and rotation in the calculation
	public AABB calcAABB() {
		//Pewter.log("Shape: calcAABB run: circle state: "+is_circle);
		if (!(is_circle)) {
			Pewter.log("Shape. calcAABB poly version run");
			AABB ab = new AABB();
			Vec2 scale = new Vec2(1, 1);
			float r = 0;
			if (owner != null) {
				scale.set(owner.getScale());
				r = owner.getRot();
			}
			for (int i = 0;i<verts.size();i++) {
				//Pewter.log("Shape. calcAABB: eachVertex");
				Vec2 v = verts.get(i).copy();
				if (owner != null) {
					v.set(v.mult(scale));
					v.set(v.rotateR(r));
				}
				if (v.x < ab.min.x) {
					ab.min.x = v.x;
				}else if (v.x > ab.max.x) {
					ab.max.x = v.x;
				}
				if (v.y < ab.min.y) {
					ab.min.y = v.y;
				}else if (v.y > ab.max.y) {
					ab.max.y = v.y;
				}
			}
			//AABB = ab.copy();
			if (owner != null) {
				ab.addPos(owner.getPos());
			}
			return ab.copy();
		} else {
			//Pewter.log("Shape. calcAABB circle version run");
			AABB ab = new AABB();
			ab.min.set(-radius, -radius);
			ab.max.set(radius, radius);
			if (owner != null) {
				ab.addPos(owner.getPos());
				Vec2 s = owner.getScale();
				ab.min.set(ab.min.mult(s));
				ab.max.set(ab.max.mult(s));
			}
			//Pewter.log("Shape: calcAABB: min: "+ab.min.toString()+" max: "+ab.max.toString());
			return ab.copy();
		}
	}
	
	//this function centers the verts so that local 0, 0 is the COM
	public void center() {
		if (!is_circle) {
			Vec2 offset = COM().invt();
			for (int i = 0;i<verts.size();i++) {
				verts.get(i).set(verts.get(i).add(offset));
			}
		}
	}
	private Vec2 COM() {
		if (!is_circle) {
			Vec2 COM = new Vec2();
			for (int i = 0;i<verts.size();i++) {
				COM = COM.add(verts.get(i));
			}
			COM.div(verts.size());
			return COM;
		} else {
			return new Vec2(0, 0);
		}
	}
	public static Shape RECT(gameObj o, Vec2 size) {
		Shape s = new Shape(o);
		s.verts.add(new Vertex(-size.x/2, size.y/2, 0, 0)); //ul
		s.normals.add(new Vec2(-1, 0)); //left face
		s.verts.add(new Vertex(-size.x/2, -size.y/2, 0, 1)); //ll
		s.normals.add(new Vec2(0, -1)); //bottom face
		s.verts.add(new Vertex(size.x/2, -size.y/2, 1, 1)); //lr
		s.normals.add(new Vec2(1, 0)); //right face
		s.verts.add(new Vertex(size.x/2, size.y/2, 1, 0)); //ur
		s.normals.add(new Vec2(0, 1)); //top face
		s.center();
		s.AABB = s.calcAABB(); //no owner, so there will be no rotating and scaleing. this AABB is local.
		s.is_circle = false;
		return s.copy();
	}
	public static Shape CIRCLE(gameObj o, float r) {
		Shape s = new Shape(o);
		s.is_circle = true;
		s.radius = r;
		//s.center();
		s.AABB = s.calcAABB();
		return s.copy();
	}
}
