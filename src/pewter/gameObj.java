package pewter;
import java.util.*;

//import java.util.*;
import processing.core.*;

public class gameObj {
	protected int Index = -1;
	protected PWorld worldContext;
	protected PApplet POwner;
	protected physicsComponent physicsController;
	protected Shape shape;
	protected movementType moveType = movementType.STATIONARY;
	
	public int layer = 0;
	public boolean lock_rot = false;
	
	//private data avalable only through getter and setter functions
	private Vec2 pos = new Vec2();
	private Vec2 scale = new Vec2(1, 1);
	private float rotation = 0;
	
	
	public gameObj(Vec2 p){
		this();
		pos = p.copy();
	}
	public gameObj(PVector p){
		this();
		pos = new Vec2(p);
	}
	public gameObj(float x, float y){
		this();
		pos = new Vec2(x, y);
	}
	
	gameObj(){
		this.worldContext = Pewter.get_gameInstance().get_loaded_world();
		Index = worldContext.addGameObject(this);
		this.POwner = worldContext.getPApplet();
		this.shape = new Shape(this);
		this.physicsController = new physicsComponent(this);
	}
	public final void setPos(Vec2 p) {
		if (moveType != movementType.STATIC) { 
			pos.set(p);
		} else {
			PApplet.println("ERROR: Pewter: Cannot set Position of Static gameObj");
		}
	}
	public final void setScale(Vec2 s) {
		if (moveType != movementType.STATIC) {
			scale.set(s);
		} else {
			PApplet.println("ERROR: Pewter: Cannot set Scale of Static gameObj");
		}
	}
	public final void setRot(float a) {
		if (moveType != movementType.STATIC) {
			rotation = a;
		} else {
			PApplet.println("ERROR: Pewter: Cannot set Rotation of Static gameObj");
		}
	}
	public final Vec2 getPos() {
		return pos.copy();
	}
	public final Vec2 getScale() {
		return scale.copy();
	}
	public final float getRot() {
		return rotation;
	}
	
	//velocity and other physics stuff that are just pass-throughs to the physicsComponent
	public final Vec2 getVel() {
		return physicsController.getVelocity();
	}
	public final void setVel(Vec2 v) {
		physicsController.setVelocity(v, false);
	}
	public final void setVel(Vec2 v, boolean add) {
		physicsController.setVelocity(v,  add);
	}
	
	//other helper functions
	public final float getScreenPixelSize() { //helper function to get the size of a single pixel on the screen
		return 1/(Pewter.getPixelsPerUnit()*Math.min(scale.x, scale.y));
	}
	public final ArrayList<Vertex> getShapeVerts(){
		ArrayList<Vertex> r = new ArrayList<Vertex>();
		for (int i=0;i<this.shape.verts.size();i++) {
			r.add(this.shape.verts.get(i).copy());
		}
		return r;
	}
	
	
	void drawInternal() { //This allows 0, 0 to be the origin of the object in its draw function, and the screen appropreately rotated and scaled.
		POwner.pushMatrix();
		POwner.translate(pos.x, pos.y);
		POwner.rotate(rotation);
		POwner.scale(scale.x, scale.y);
		draw();
		POwner.popMatrix();
	}
	
	
	//These functions are overriden by the end user
	public void tick() {
		
	}
	public void draw() {
		
	}
	public void Overlap(gameObj other) {
		
	}
	
	
	
	// another important function!
	public void kill() {
		physicsController.kill();
		worldContext.killObj(Index);
	}
	void indexUpdate(int i) {
		Index = i;
	}
}
