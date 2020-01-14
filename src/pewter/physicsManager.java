package pewter;
import java.util.*;
import processing.core.*;

public class physicsManager {
	private ArrayList<physicsComponent> bodies = new ArrayList<physicsComponent>();
	private ArrayList<Manifold> collisions = new ArrayList<Manifold>();
	private static physicsManager singleInstance =null;
	private int to_remove = 0;
	private physicsManager() {
		
	}
	public static physicsManager getSingleton() {
		if (singleInstance == null) {
			singleInstance = new physicsManager();
		}
		return singleInstance;
	}
	final int addPhysicsBody(physicsComponent a) {
		bodies.add(a);
		return bodies.size()-1;
	}
	
	//this is run pre-tick so that all collisions are ready for tick()
	void collect_collisions() {
		//Pewter.log("Physics: Collisions Collection Began");
		int false_manifolds = 0;
		collisions.clear();
		//First, collision Checks
		for (int i=0;i<bodies.size();i++) {
			physicsComponent A = bodies.get(i);
			//Pewter.log("SHAPE CHECK A: "+A.shape.is_circle);
			if (A != null) {
				A.indexUpdate(i);//perfrom a quickie index update here.
				for (int j = i+1;j<bodies.size();j++) {
					physicsComponent B = bodies.get(j);
					//Pewter.log("SHAPE CHECK B: "+B.shape.is_circle);
					if (B != null) {
						if (checkAABBs(A.shape.calcAABB(), B.shape.calcAABB())) {
							//ok, so these objects are likeply to be colliding. make a manifold for them
							Manifold m = new Manifold(A, B);
							//Pewter.log("Physics: Collisions: Manifold Made");
							if (m.shouldApplyOverlap()) { //well, they should not actually even have an overlap check, even though they may be visibly overlapping. This is the easiest way to check for this
								//Pewter.log("Physics: Collisions: Manifold Added");
								collisions.add(m);
							}
						}
					}
				}
			}
		}
		//perform higher level checks on manifolds
		for (int i=0;i< collisions.size();i++) {
			Manifold m = collisions.get(i);
			m.Solve();
			if (m.contactCount == 0) {
				//Pewter.log("Physics: Collisions: Manifold removed: false count: "+ false_manifolds+1);
				collisions.set(i,  null); //clear out that collision
				false_manifolds++;
			}
		}
		//clean up the collisions array so that it has no null values
		for (int i=0;i<false_manifolds+1;i++) {
			collisions.remove(null);
		}
		Pewter.log("Physics: Collisions Collection Ended. Collisions: "+collisions.size());
	}
	
	void update() {
		Pewter.log("Physics: Update Began");
		to_remove = 0;
		//manifold stuff for collisions
		//fire overlap events
		for (int i=0;i< collisions.size();i++) {
			Manifold m = collisions.get(i);
			Pewter.log("Physics: Update: Applying InternalEventContact");
			m.A.InternalEventContact(m.B);
			m.B.InternalEventContact(m.A);
			//debug!!!
			drawVector(m.A.owner.getPos(), m.normal);
		}
		//do physics collisions
		for (int i=0;i< collisions.size();i++) {
			Manifold m = collisions.get(i);
			if (m.shouldApplyPhysics()) {
				//stuff------------------------------------------------------------------------------------------
				Pewter.log("Physics: Update: Applying Physics Collision");
				m.applyPhysicsCollision();
			}
		}
		
		//tick physics components
		for (int i=0;i<bodies.size();i++) {
			Pewter.log("Physics: Update: Applying Physics Tick");
			bodies.get(i).tick();
		}
		for (int i=0;i<to_remove+1;i++) {
			bodies.remove(null);
		}
		Pewter.log("Physics: Update Ended");
	}
	/*
	 * cheap and dirty check to see if two objects are overlapping from their AABB's for a high-pass collision check. 
	 * This will still world on circles as they have an AABB and this is simpler than making a seperate function for  circle vs circle and circle vs AABB
	 */
	boolean checkAABBs(AABB a, AABB b) {
		//Pewter.log("Collision: Check AABBs: A: min: "+a.min.toString()+" max: "+a.max.toString()+" B: min: "+b.min.toString()+" max: "+b.max.toString());
		if ((a.max.x < b.min.x)||(a.min.x > b.max.x)) {return false;}
		if ((a.max.y < b.min.y)||(a.min.y > b.max.y)) {return false;}
		return true;
	}
	void killBody(int index) {
		bodies.set(index, null);
		to_remove++;
	}
	void killAllBodies() {
		bodies.clear();
	}
	void drawVector(Vec2 pos, Vec2 normal) {
		if (Pewter.enableDebugDrawing) {
			//Vec2 poss = Pewter.toScreen(pos);
			PApplet p = Pewter.getSingleton().POwner;
			p.stroke(0, 255, 0);
			p.line(pos.x,  pos.y, pos.x+normal.x*100, pos.y+normal.y*100);
		}
	}
}
	
/*
 * Some smaller, collision-only classes here
 */




