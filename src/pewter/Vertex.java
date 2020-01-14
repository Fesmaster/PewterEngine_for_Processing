package pewter;
import processing.core.*;

public class Vertex extends Vec2 {
	public float u;
	public float v;
	public Vertex(float x, float y, float u, float v){
		super(x, y);
		this.u = u;
		this.v = v;
	}
	public Vertex(PVector a, float u, float v){
		super(a);
		this.u = u;
		this.v = v;
	}
	public Vertex(Vec2 a, float u, float v){
		super(a);
		this.u = u;
		this.v = v;
	}
	public Vertex(PVector a, PVector b){
		super(a);
		this.u = b.x;
		this.v = b.y;
	}
	public Vertex(Vec2 a, Vec2 b){
		super(a);
		this.u = b.x;
		this.v = b.y;
	}
	public Vertex(PVector a, Vec2 b){
		super(a);
		this.u = b.x;
		this.v = b.y;
	}
	public Vertex(Vec2 a, PVector b){
		super(a);
		this.u = b.x;
		this.v = b.y;
	}
	public Vertex(Vertex a){
		this.x = a.x;
		this.y = a.y;
		this.u = a.u;
		this.v = a.v;
	}
	//IMPURE set functions
	Vertex set(float x, float y, float u, float v) {
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
		return this;
	}
	public Vertex set(PVector o, float u, float v) {
		this.x = o.x;
		this.y = o.y;
		this.u = u;
		this.v = v;
		return this;
	}
	public Vertex set(Vec2 o, float u, float v) {
		this.x = o.x;
		this.y = o.y;
		this.u = u;
		this.v = v;
		return this;
	}
	public Vertex set(PVector a,PVector b) {
		this.x = a.x;
		this.y = a.y;
		this.u = b.x;
		this.v = b.y;
		return this;
	}
	public Vertex set(Vec2 a,PVector b) {
		this.x = a.x;
		this.y = a.y;
		this.u = b.x;
		this.v = b.y;
		return this;
	}
	public Vertex set(PVector a,Vec2 b) {
		this.x = a.x;
		this.y = a.y;
		this.u = b.x;
		this.v = b.y;
		return this;
	}
	public Vertex set(Vec2 a,Vec2 b) {
		this.x = a.x;
		this.y = a.y;
		this.u = b.x;
		this.v = b.y;
		return this;
	}
	public Vertex set(Vertex o) {
		this.x = o.x;
		this.y = o.y;
		this.u = o.u;
		this.v = o.v;
		return this;
	}
	//Override Math functions to have some that return Vertex objects
	//U and V are preserved, and there are no static versions
	//Add
	public Vertex add(float a, float b) {
		return super.add(a, b).vert(this.u, this.v);
	}
	public Vertex add(float a) {
		return super.add(a).vert(this.u, this.v);
	}
	public Vertex add(Vec2 a) {
		return super.add(a).vert(this.u, this.v);
	}
	public Vertex addS(float a, float b) {
		return this.set(super.add(a, b).vert(this.u, this.v));
	}
	public Vertex addS(float a) {
		return this.set(super.add(a).vert(this.u, this.v));
	}
	public Vertex addS(Vec2 a) {
		return this.set(super.add(a).vert(this.u, this.v));
	}
	//Sub
	public Vertex sub(float a, float b) {
		return super.sub(a, b).vert(this.u, this.v);
	}
	public Vertex sub(float a) {
		return super.sub(a).vert(this.u, this.v);
	}
	public Vertex sub(Vec2 a) {
		return super.sub(a).vert(this.u, this.v);
	}
	public Vertex subS(float a, float b) {
		return this.set(super.sub(a, b).vert(this.u, this.v));
	}
	public Vertex subS(float a) {
		return this.set(super.sub(a).vert(this.u, this.v));
	}
	public Vertex subS(Vec2 a) {
		return this.set(super.sub(a).vert(this.u, this.v));
	}
	//Mult
	public Vertex mult(float a, float b) {
		return super.mult(a, b).vert(this.u, this.v);
	}
	public Vertex mult(float a) {
		return super.mult(a).vert(this.u, this.v);
	}
	public Vertex mult(Vec2 a) {
		return super.mult(a).vert(this.u, this.v);
	}
	public Vertex multS(float a, float b) {
		return this.set(super.mult(a, b).vert(this.u, this.v));
	}
	public Vertex multS(float a) {
		return this.set(super.mult(a).vert(this.u, this.v));
	}
	public Vertex multS(Vec2 a) {
		return this.set(super.mult(a).vert(this.u, this.v));
	}
	//Div
	public Vertex div(float a, float b) {
		return super.div(a, b).vert(this.u, this.v);
	}
	public Vertex div(float a) {
		return super.div(a).vert(this.u, this.v);
	}
	public Vertex div(Vec2 a) {
		return super.div(a).vert(this.u, this.v);
	}
	public Vertex divS(float a, float b) {
		return this.set(super.div(a, b).vert(this.u, this.v));
	}
	public Vertex divS(float a) {
		return this.set(super.div(a).vert(this.u, this.v));
	}
	public Vertex divS(Vec2 a) {
		return this.set(super.div(a).vert(this.u, this.v));
	}
	//copy
	public Vertex copy() {
		return new Vertex(this);
	}
}
