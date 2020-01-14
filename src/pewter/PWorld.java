package pewter;
import processing.core.*;
import java.util.*;

public class PWorld {
	protected ArrayList<gameObj>objects = new ArrayList<gameObj>();
	protected PApplet POwner;
	protected Vec2 Gravity = new Vec2();
	private int layercount = 1;
	private ArrayList<ArrayList<Integer>> layers = new ArrayList<ArrayList<Integer>>();
	private int to_remove = 0;
	public PWorld(PApplet o){
		POwner = o;
		setLayers(1);
	}
	public void load() {
		//to be implemented in world subclasses
	}
	public void unload() {
		//to be implemented in world subclasses
	}
	public PApplet getPApplet() {
		return POwner;
	}
	public void setLayers(int l) {
		layercount = l;
		layers.clear();
		for (int i = 0;i<layercount;i++) {
			layers.add(new ArrayList<Integer>());
		}
	}
	final public int addGameObject(gameObj o) {
		objects.add(o);
		return objects.size()-1;
	}
	final void update() {
		/*
		 * THIS IS THE SUPER BIG MAIN IMPORTATNT FUNCTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */
		
		//fist, clean up old data
		to_remove = 0;
		//Pewter.log("PWorld: Update begin");
		//clear the layers array, so that its not ... broken..
		for (int i=0;i<layers.size();i++) {
			layers.get(i).clear();
		}
		
		//second, collect collisions
		physicsManager.getSingleton().collect_collisions();
		
		//third, tick all objects
		//Pewter.log("PWorld: objectListSize = "+objects.size());
		for (int i=0;i<objects.size();i++) {
			objects.get(i).indexUpdate(i);
			objects.get(i).tick();
			layers.get(objects.get(i).layer).add(i); //sort the objects into their layer count
		}
		
		//forth, update the physicsManager (fires overlap events and collision events, and does collision math)
		physicsManager.getSingleton().update();
		
		//fith, begin drawing
		//screen transforms before draw
		POwner.pushMatrix();
		POwner.scale(Pewter.scaleFactor, -Pewter.scaleFactor);
		Vec2 campos = Camera.getPos();
		POwner.translate(campos.x, campos.y);
		POwner.rotate(Camera.getAngle());
		//and here the draw code is implemented.
		//this is not confusing at all
		/*
		 * so, there are two arraylists
		 * the first, layers, is an arraylists if arraylists of ints
		 * the seconds is an arraylist of gameObjs
		 * the final result if layst.get().get() is an index into gameObjs, sortin gthe objects by layer and creating order.
		 * this is so that they can be drawn in the correct order!
		 */
		Pewter.log("PWorld: layers size = "+layers.size());
		for (int l=0;l<layers.size();l++) {
			//Pewter.log("PWorld: layer ("+l+") size: "+layers.get(l).size());
			for (int i=0;i<layers.get(l).size();i++) {
				//Pewter.log("Pworld: held Index: "+layers.get(l).get(i));
				objects.get(layers.get(l).get(i)).drawInternal();
			}
		}
		POwner.popMatrix();
		//sixth the wrold's draw function
		this.draw();
		
		//seventh, remove all destroyed objects
		for (int i=0;i<to_remove+1;i++) {
			objects.remove(null);
		}
		//Pewter.log("PWorld: Update ended");
	}
	void killObj(int index) {
		objects.set(index, null);
		to_remove++;
	}
	
	public void draw() {
		//to be implemented in world subclass
	}
	public void setGravity(Vec2 g) {
		Gravity.set(g);
	}
	public Vec2 getGravity() {
		return Gravity;
	}
}
