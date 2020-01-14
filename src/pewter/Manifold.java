package pewter;

class Manifold {
	public physicsComponent A = null;
	public physicsComponent B = null;
	public int contactCount = 0;
	public Vec2[] contacts = new Vec2[2];
	public Vec2 normal = new Vec2();
	public float penetration = 0;
	
	public float mE = 0;
	public float mDF = 0;
	public float mSF = 0;
	
	Manifold(physicsComponent a, physicsComponent b){
		A = a;
		B = b;
		//these bits so that there is not a crash if things go wrong............as they ineviabley will
		contacts[0] = new Vec2();
		contacts[1] = new Vec2();
	}
	
	void invAB() {
		physicsComponent t = A;
		A = B;
		B = t;
	}
	
	void Solve() {
		//Pewter.log("Manifold: Solve: run");
		//this is where a collision is detected. This gets complex quickly
		collisionPrimativeType AT = A.primativeType;
		collisionPrimativeType BT = B.primativeType;
		//Pewter.log("Manifold: Sove: types: "+ AT+" and "+ BT);
		switch(AT){
			case CIRCLE : {
				switch(BT) {
					case CIRCLE: {
						CircleVsCircle();
						break;
					}
					case RECT : {
						invAB();
						AABBvsCircle();
						invAB();
						normal.set(normal.invt());
						break;
					}
					case POLY : {
						invAB();
						PolyVsCircle();
						invAB();
						normal.set(normal.invt());
						break;
					}
					default: {break;}
				}
				break;
			}
			case RECT : {
				switch(BT) {
					case CIRCLE: {
						AABBvsCircle();
						break;
					}
					case RECT : {
						AABBvsAABB();
						break;
					}
					case POLY : {
						invAB();
						PolyVsAABB();
						invAB();
						normal.set(normal.invt());
						break;
					}
					default: {break;}
				}
				break;
			}
			case POLY : {
				switch(BT) {
					case CIRCLE: {
						PolyVsCircle();
						break;
					}
					case RECT : {
						PolyVsAABB();
						break;
					}
					case POLY : {
						PolyVsPoly();
						break;
					}
					default: {break;}
				}
				break;
			}
			default: {break;}
		}
	}
	
	
	boolean shouldApplyPhysics() {
		if ((A.owner.moveType == movementType.DYNAMIC)||(B.owner.moveType == movementType.DYNAMIC)) { //if one or the other body is dynamic
			if((A.overlapType == collisionType.COLLIDE_ALL)&&(A.overlapType == collisionType.COLLIDE_ALL)) { //if they are declared as collide_all
				if ((A.owner.layer == B.owner.layer)||((A.collideAllLayers)||(B.collideAllLayers))) { //if they are on the same layer OR one of them has collideAllLayers true
					return true;
				}
			}
		}
		return false;
	}
	boolean shouldApplyOverlap() {
		if((A.overlapType != collisionType.NONE)&&(A.overlapType != collisionType.NONE)) { //if they are declared as overlapable
			if ((A.owner.layer == B.owner.layer)||((A.overlapAllLayers)||(B.overlapAllLayers)||(A.collideAllLayers)||(B.collideAllLayers))) { //if they are on the same layer OR one of them has collideAllLayer or overlapAllLayers true
				return true;
			}
		}
		return false;
	}
	
	
	@SuppressWarnings("unused")
	void applyPhysicsCollision() {
		//early outs
		if (!((A.owner.moveType == movementType.DYNAMIC)||(B.owner.moveType == movementType.DYNAMIC))) {
			Pewter.log("Manifold: Physics early out: Non-dynamic overlap detected");
			return; //non-dynamic movements in both
		}
		if (PMath.Equals(A.imass+B.imass, 0.0f)) {
			Pewter.log("Manifold: Physics early out: zero mass detected");
			return; //infinite mass in both, why this would happen is unknown
		}
		//calculate restution, etc;
		calcValues();
	
		//do stuff
		for (int i=0;i<contactCount;i++) {
			Pewter.log("Manifold: contact being solved: "+i);
			//calc radii from COM to contact
			Vec2 ra = contacts[i].sub(A.owner.getPos());
			Vec2 rb = contacts[i].sub(B.owner.getPos());
			
			//Relatve Velocity
			Vec2 rv = B.vel.add(Vec2.cross(B.velAngle, rb)).sub(A.vel).sub(Vec2.cross(A.velAngle, ra));
			
			//relative vel along normal
			float contactVel = Vec2.dot(rv,  normal);
			
			//do nothing if velocities are seperating
			if (contactVel > 0) {
				continue;
			}
			
			float raCrossN = Vec2.cross(ra, normal);
			float rbCrossN = Vec2.cross(rb, normal);
			float invMassSum = A.imass+B.imass+(raCrossN*raCrossN*A.iinerta) + (rbCrossN*rbCrossN*B.iinerta);
			
			//Calculate impulse scalar
			float j = -(1.0f+mE)*contactVel;
			j /= invMassSum;
			j /= (float)contactCount;
			
			//Apply Impulse
			Vec2 impulse = normal.mult(j);
			Pewter.log("Manifold: Impulse magnitude: "+impulse.length_sq());
			if (A.owner.moveType == movementType.DYNAMIC){
				A.collisionImpulse(impulse.invt(), ra);
			}
			if (A.owner.moveType == movementType.DYNAMIC){
				B.collisionImpulse(impulse, rb);
			}
			
			//Friction Impulse Calculations
			
			rv = B.vel.add(Vec2.cross(B.velAngle, rb)).sub(A.vel).sub(Vec2.cross(A.velAngle, ra));
			
			Vec2 t = rv.sub(normal.mult(Vec2.dot(rv, normal)));
			t.normalize();
			
			//j tangent magnitude
			float jt = -Vec2.dot(rv, t);
			jt /= invMassSum;
			jt /= (float)contactCount;
			
			//Don't apply tiny friction impulses
			if (PMath.Equals(jt, 0.0f)) {
				continue;
			}
			
			//Coulumb's Law
			//ie, choose friction to use
			Vec2 tangentImpulse = new Vec2();
			if (Math.abs(jt) < j*mSF) {
				tangentImpulse = t.mult(jt);
			} else {
				tangentImpulse = t.mult(-j).mult(mDF);
			}
			
			//Apply friction impulse
			
			if (A.owner.moveType == movementType.DYNAMIC){
				A.collisionImpulse(tangentImpulse.invt(), ra);
			}
			if (B.owner.moveType == movementType.DYNAMIC){
				B.collisionImpulse(tangentImpulse, rb);
			}
			
		}
		
		//anti-sinking math
		PositionalCorrection();
	}
	
	private void calcValues() {
		//initialize in example
		//this is where mResitiution, mDF, and mSF are calculated
		mE = Math.min(A.restitution, B.restitution);
		mSF = (float)Math.sqrt(A.staticFriction*B.staticFriction);
		mDF = (float)Math.sqrt(A.dynamicFriction*B.dynamicFriction);
		
		//check to see if we are performing a resting collision or not
		for (int i=0;i<contactCount;i++) {
			Vec2 ra = contacts[i].sub(A.owner.getPos());
			Vec2 rb = contacts[i].sub(B.owner.getPos());
			//long ling of code incoming!
			//basically, this is how much they are overlapping
			Vec2 rv = B.vel.add(Vec2.cross(B.velAngle, rb)).sub(A.vel).sub(Vec2.cross(A.velAngle, ra));
			if(A.gravity||B.gravity) {
				//gravity is involved..so..
				if(rv.length_sq() < Pewter.get_gameInstance().get_loaded_world().getGravity().mult((float)Pewter.getWorldDeltaSeconds()).length_sq()+PMath.EPSILON) {
					mE = 0.0f;
				}
			}else {
				if (rv.length_sq() < PMath.EPSILON*PMath.EPSILON) {
					mE = 0.0f;
				}
			}
		}
	}
	
	//this is a basic positional shunting to prevent sinking
	private void PositionalCorrection() {
		float k_slop = 0.05f; // Penetration allowance
		float percent = 0.6f; // Penetration percentage to correct
		Vec2 correction = normal.mult(Math.max(penetration-k_slop, 0.0f)/(A.imass+B.imass)).mult(percent);
		if (A.owner.moveType == movementType.DYNAMIC){
			A.owner.setPos(A.owner.getPos().sub(correction));
		}
		if (B.owner.moveType == movementType.DYNAMIC){
			B.owner.setPos(B.owner.getPos().add(correction));
		}
	}
	
	/*
	 * -------------------------------------------------------------------------------------------------------------
	 * SPECIFIC TYPES OF COLLISION DETECTION
	 * -------------------------------------------------------------------------------------------------------------
	 */
	private void CircleVsCircle() {
		//Pewter.log("Manifold: Solve: Circle Vs Circle");
		//Vector from A to B
		Vec2 n = Vec2.sub(B.owner.getPos(), A.owner.getPos());
		
		//combined Radius
		float r = A.shape.radius+B.shape.radius;
		//r*=r; //square r, for speedy distance check
		if (n.length_sq() > r*r) {
			contactCount = 0;
			return; //NO COLLISION. SLATE MANIFOLD FOR DESTRUCTION!!!!
		}
		//now compute the actual manifold
		float d = n.length();
		contactCount = 1;
		if (d != 0) {//objects are in different locations(common)
			contacts[0] = A.owner.getPos().add(n.div(2)); //the mid-point between the colliding circles
			penetration = r-d;
			normal.set(n.div(d));//use distance since sqrt() function already performed for it. save some time
		} else {//objects are directly on top of each other
			contacts[0] = A.owner.getPos();
			penetration = A.shape.radius;
			normal.set(1, 0);
		}
	}
	
	
	private void AABBvsAABB() {
		//Pewter.log("Manifold: Solve: AABB Vs AABB");
		contactCount = 0;
		Vec2 n = Vec2.sub(B.owner.getPos(), A.owner.getPos());
		AABB aBox = A.shape.calcAABB();
		AABB bBox = B.shape.calcAABB();
		
		//calc extents on x axis
		float aExtent = (aBox.max.x-aBox.min.x)/2;
		float bExtent = (bBox.max.x-bBox.min.x)/2;
		
		float xOverlap = aExtent+bExtent-Math.abs(n.x);
		
		//SAT test in x axis
		if (xOverlap >0) {
			//calc extents on y axis
			aExtent = (aBox.max.y-aBox.min.y)/2;
			bExtent = (bBox.max.y-bBox.min.y)/2;
			float yOverlap = aExtent+bExtent-Math.abs(n.y);
			//SAT test in y axis
			if (yOverlap >0) {
				//yay! we have a accurate collision
				//determine which face's normal to use
				if(xOverlap > yOverlap) {
					if (n.x < 0) {
						normal.set(-1, 0);
					} else {
						normal.set(1,0);
					}
					penetration = xOverlap;
					contactCount = 1;
					contacts[0] = A.owner.getPos().add(xOverlap, 0);
				}else {
					if (n.y < 0) {
						normal.set(0,-1);
					} else {
						normal.set(0,1);
					}
					penetration = yOverlap;
					contactCount = 1;
					contacts[0] = A.owner.getPos().add(0, yOverlap);
				}
			}
		}
	}
	
	private void AABBvsCircle() {
		//Pewter.log("Manifold: Solve: AABB Vs Circle");
		contactCount = 0;
		Vec2 n = Vec2.sub(B.owner.getPos(), A.owner.getPos());
		Vec2 closest = n.copy();
		
		AABB ab = A.shape.calcAABB();
		float xExtent = (ab.max.x-ab.min.x)/2;
		float yExtent = (ab.max.y-ab.min.y)/2;
		//clamp closest to be on the edge of the AABB
		closest.x = PMath.Clamp(-xExtent, xExtent, closest.x);
		closest.y = PMath.Clamp(-yExtent, yExtent, closest.y);
		
		boolean inside = false;
		
		if (n.equals(closest)) {
			//Circle is inside the AABB, so we nees to clamp the circle's center to the closest edge
			inside = true;
			
			//find the closest axis
			if(Math.abs(n.x)> Math.abs(n.y)) {
				if (closest.x >0) {
					closest.x = xExtent;
				}else {
					closest.x = -xExtent;
				}
			} else {
				if (closest.y >0) {
					closest.y = yExtent;
				}else {
					closest.y = -yExtent;
				}
			}
		}
		
		Vec2 nor = n.sub(closest);
		float d = nor.length_sq();
		float r = B.shape.radius;
		
		if(d>r*r) {
			contactCount = 0;
			return; //NO COLLISION. SLATE MANIFOLD FOR DESTRUCTION!!!!
		}
		contactCount = 1;
		//avoid sqrt till needed
		d = (float)Math.sqrt(d);
		if(inside) {
			normal = n.invt();
			penetration = r-d;
			contacts[0] = A.owner.getPos().add(closest);
		}else {
			normal = n.copy();
			penetration = r-d;
			contacts[0] = A.owner.getPos().add(closest);
		}
	}
	
	//NOT FULLY IMPLEMENTED YET!!!!!!!!!!!!!!!!!!!!
	private void PolyVsCircle() {
		Pewter.log("Manifold: Solve: Poly Vs Circle");
		AABBvsCircle();
	}
	private void PolyVsAABB() {
		Pewter.log("Manifold: Solve: Poly Vs AABB");
		AABBvsAABB();
	}
	private void PolyVsPoly() {
		Pewter.log("Manifold: Solve: Poly Vs Poly");
		AABBvsAABB();
	}
	
}
