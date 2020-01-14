package pewter;

public class physicsComponent {
	private int Index = -1;
	
	Vec2 vel = new Vec2();			//linear velocity
	Vec2 forces = new Vec2();		//linear force
	float velAngle = 0;				//angular velocity
	float torque = 0; 				//angular force
	public float linearDampening = 0.1f;	//linear dampening
	public float anglularDampening = 0.1f;	//angular dampening
	
	gameObj owner = null;
	Shape shape = null;
	
	float mass = 1.0f;				//Mass. pretty straightforward
	float imass = 1.0f;				//inverse mass 1/mass
	float inerta = 1.0f;				//inertia. used for angular stuff
	float iinerta = 1.0f;				//1/inertia
	float restitution = 0.1f;		//bouncyness. 0 = no bounce, 1 = full momentium maintained
	float staticFriction  = 0.5f;	//friction when low speed/still
	float dynamicFriction = 0.1f;	//friction when moving. less than staticFriction
	
	public boolean collideAllLayers = false;	//should have physics collision with objects on all layers?
	public boolean overlapAllLayers = false;	//should generate overlap events for objects on all layers?
	
	//some phsycis settings
	public boolean gravity = false;
	public collisionType overlapType = collisionType.OVERLAP_ALL; 
	public collisionPrimativeType primativeType = collisionPrimativeType.RECT;
	
	physicsComponent(gameObj o){
		owner = o;
		shape = o.shape;
		Index = physicsManager.getSingleton().addPhysicsBody(this);
	}
	/*
	 * tick() 
	 * this function is called every step by the physicsManager
	 */
	void tick() {
		//why are these separate? so that code is easier to read!
		integrateForce();
		integrateVel();
	}
	void integrateForce() {
		if (owner.moveType == movementType.DYNAMIC) {
			float dt = (float)Pewter.getWorldDeltaSeconds();
			//positional velocity
			vel.addS(forces.mult(imass).mult(dt));
			//vel.addS(forces.mult(imass));
			if (gravity) { //respect gravity settings
				vel.addS(Pewter.get_gameInstance().get_loaded_world().getGravity().mult(dt));
			}
			//angular velocity
			if (!owner.lock_rot) {
				velAngle +=(torque*(iinerta)*dt);
			}
			//now, implement dampening(universal friction)
			vel.multS(1.0f-linearDampening);
			if (!owner.lock_rot) {
				velAngle = velAngle*(1.0f-anglularDampening);
			}
		}
		//clear forces
		forces.set(0, 0);
		torque = 0;
	}
	void integrateVel() {
		if (owner.moveType != movementType.STATIC) {
			float dt = (float)Pewter.getWorldDeltaSeconds();
			//Position update
			owner.setPos(owner.getPos().add(vel.mult(dt)));
			//Pewter.log(vel.mult(dt).toString());
			if (!owner.lock_rot) {
				owner.setRot(owner.getRot()+(velAngle*dt));
			}
		}
		
	}
	void setVelocity(Vec2 v, boolean add) {
		if(add) {
			vel.set(vel.add(v));
		}else {
			vel.set(v);
		}
	}
	Vec2 getVelocity() {
		return vel.copy();
	}
	
	//this is important
	void kill() {
		physicsManager.getSingleton().killBody(Index);
	}
	void indexUpdate(int i) {
		Index = i;
	}
	
	void InternalEventContact(physicsComponent o) {
		owner.Overlap(o.owner);
	}
	void setMass(float m) {
		mass = m;
		if (m == 0) {
			imass = 0;
		}else {
			imass = 1/m;
		}
	}
	void addForce(Vec2 f) {
		forces.addS(f);
	}
	
	void applyImpulse(Vec2 impulse, Vec2 contactVector) {
		if (owner.moveType == movementType.DYNAMIC) {
			vel.addS(impulse.mult(imass));
			if (!owner.lock_rot) {
				velAngle+= iinerta*Vec2.cross(contactVector, impulse);
			}
		}
	}
	void collisionImpulse(Vec2 impulse, Vec2 contactVector) {
		if (owner.moveType == movementType.DYNAMIC) {
			vel.addS(impulse.mult(imass));
			if (!owner.lock_rot) {
				velAngle += iinerta*Vec2.cross(contactVector, impulse);
			}
		}
	}
}
