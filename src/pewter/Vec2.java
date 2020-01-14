/*
 * Vector Class for Internal Use.
 * pretty basic, but able to convert between PVectors (Processing) and Vec2 (JBox2d) 
 * also, this class is PURE, for the most part. ONLY set() functions are impure
 * 
 * while this class is called Vec2f, indicating float values, they are actually doubles, for better data storage.
 * This is compressed back to a float during conversions to other vector types
 * this is done because automatic casting from float to double is available, but there is no automatic casting from double to float.
 * It should be super easy to put data in and take it out of this vector class, that is why all functions that return a float-like value 
 * have the default return a double and a special copy return a float
 */
package pewter;
import processing.core.*;

public class Vec2 {
	public float x;
	public float y;
	public Vec2() {
		this.x = 0;
		this.y = 0;
	}
	public Vec2(float a, float b){
		this.x = a;
		this.y = b;
	}
	public Vec2 (PVector o) {
		this.x = o.x;
		this.y = o.y;
	}
	public Vec2(Vec2 o) {
		this.x = o.x;
		this.y = o.y;
	}
	public final PVector PVector() {
		return new PVector(this.x, this.y);
	}
	public String toString() {
		return "("+this.x+", "+this.y+")";
	}
	//ABS
	public Vec2 abs() {
		return new Vec2(Math.abs(this.x), Math.abs(this.y));
	}
	public static Vec2 abs(Vec2 a) {
		return new Vec2(Math.abs(a.x), Math.abs(a.y));
	}
	//DOT
	public float dot(Vec2 o) {
		return this.x*o.x+this.y*o.y;
	}
	public static float dot(Vec2 a, Vec2 b) {
		return a.x*b.x+a.y*b.y;
	}
	//CROSS
	public float cross(Vec2 o) {
		return this.x*o.y-this.y*o.x;
	}
	public static float cross(Vec2 a, Vec2 b) {
		return a.x*b.y-a.y*b.x;
	}
	public Vec2 cross(float s) {
		return new Vec2(s*this.y, -s*this.x);
	}
	public static Vec2 cross(Vec2 a, float s) {
		return new Vec2(s*a.y, -s*a.x);
	}
	public static Vec2 cross(float s, Vec2 a) {
		return new Vec2(-s*a.y, s*a.x);
	}
	//ADD
	public Vec2 add(Vec2 o) {
		return new Vec2(this.x+o.x, this.y+o.y);
	}
	public Vec2 add(float o) {
		return new Vec2(this.x+o, this.y+o);
	}
	public Vec2 add(float a, float b) {
		return new Vec2(this.x+a, this.y+b);
	}
	
	public Vec2 addS(Vec2 o) {
		this.x+=o.x;
		this.y+=o.y;
		return this;
	}
	public Vec2 addS(float o) {
		this.x+=o;
		this.y+=o;
		return this;
	}
	public Vec2 addS(float a, float b) {
		this.x+=a;
		this.y+=b;
		return this;
	}
	
	public static Vec2 add(Vec2 a, Vec2 b) {
		return new Vec2(a.x+b.x, a.y+b.y);
	}
	public static Vec2 add(Vec2 a, float o) {
		return new Vec2(a.x+o, a.y+o);
	}
	public static Vec2 add(Vec2 o, float a, float b) {
		return new Vec2(o.x+a, o.y+b);
	}
	//SUB
	public Vec2 sub(Vec2 o) {
		return new Vec2(this.x-o.x, this.y-o.y);
	}
	public Vec2 sub(float o) {
		return new Vec2(this.x-o, this.y-o);
	}
	public Vec2 sub(float a, float b) {
		return new Vec2(this.x-a, this.y-b);
	}
	
	public Vec2 subS(Vec2 o) {
		this.x-=o.x;
		this.y-=o.y;
		return this;
	}
	public Vec2 subS(float o) {
		this.x-=o;
		this.y-=o;
		return this;
	}
	public Vec2 subS(float a, float b) {
		this.x-=a;
		this.y-=b;
		return this;
	}
	
	public static Vec2 sub(Vec2 a, Vec2 b) {
		return new Vec2(a.x-b.x, a.y-b.y);
	}
	public static Vec2 sub(Vec2 a, float o) {
		return new Vec2(a.x-o, a.y-o);
	}
	public static Vec2 sub(Vec2 o, float a, float b) {
		return new Vec2(o.x-a, o.y-b);
	}
	//MULT
	public Vec2 mult(Vec2 o) {
		return new Vec2(this.x*o.x, this.y*o.y);
	}
	public Vec2 mult(float o) {
		return new Vec2(this.x*o, this.y*o);
	}
	public Vec2 mult(float a, float b) {
		return new Vec2(this.x*a, this.y*b);
	}
	
	public Vec2 multS(Vec2 o) {
		this.x*=o.x;
		this.y*=o.y;
		return this;
	}
	public Vec2 multS(float o) {
		this.x*=o;
		this.y*=o;
		return this;
	}
	public Vec2 multS(float a, float b) {
		this.x*=a;
		this.y*=b;
		return this;
	}
	
	public static Vec2 mult(Vec2 a, Vec2 b) {
		return new Vec2(a.x*b.x, a.y*b.y);
	}
	public static Vec2 mult(Vec2 a, float o) {
		return new Vec2(a.x*o, a.y*o);
	}
	public static Vec2 mult(Vec2 o, float a, float b) {
		return new Vec2(o.x*a, o.y*b);
	}
	//DIV
	public Vec2 div(Vec2 o) {
		return new Vec2(this.x/o.x, this.y/o.y);
	}
	public Vec2 div(float o) {
		return new Vec2(this.x/o, this.y/o);
	}
	public Vec2 div(float a, float b) {
		return new Vec2(this.x/a, this.y/b);
	}
	
	public Vec2 divS(Vec2 o) {
		this.x/=o.x;
		this.y/=o.y;
		return this;
	}
	public Vec2 divS(float o) {
		this.x/=o;
		this.y/=o;
		return this;
	}
	public Vec2 divS(float a, float b) {
		this.x/=a;
		this.y/=b;
		return this;
	}
	
	public static Vec2 div(Vec2 a, Vec2 b) {
		return new Vec2(a.x/b.x, a.y/b.y);
	}
	public static Vec2 div(Vec2 a, float o) {
		return new Vec2(a.x/o, a.y/o);
	}
	public static Vec2 div(Vec2 o, float a, float b) {
		return new Vec2(o.x/a, o.y/b);
	}
	//COPY
	public Vec2 copy() {
		return new Vec2(this);
	}
	//LENGTH
	public float length() {
		return (float)(Math.sqrt(x*x+y*y));
	}
	public float length_sq() {
		return (x*x+y*y);
	}
	public static float distance(Vec2 a, Vec2 b) {
		return (float) Math.sqrt((b.x-a.x)*(b.x-a.x)+(b.y*a.y)*(b.y-a.y));
	}
	public static float distance_sq(Vec2 a, Vec2 b) {
		return (b.x-a.x)*(b.x-a.x)+(b.y*a.y)*(b.y-a.y);
	}
	//MAG SETTING ()
	public Vec2 setMag(float m) {
		float oldm = this.length();
		return new Vec2(this.x*m/oldm, this.y*m/oldm);
	}
	public Vec2 normalize() {
		float oldm = this.length();
		return new Vec2(this.x*1/oldm, this.y*1/oldm);
	}
	//SET (IMPURE!!!!)
	public Vec2 set(float a, float b) {
		this.x = a;
		this.y = b;
		return this;
	}
	public Vec2 set(PVector o) {
		this.x = o.x;
		this.y = o.y;
		return this;
	}
	public Vec2 set(Vec2 o) {
		this.x = o.x;
		this.y = o.y;
		return this;
	}
	//Matrix Multiplication and rotation stuff
	public Vec2 matmul(float[][] matrix) {
		if ((matrix.length != 2)||(matrix[0].length != 2)) {
			throw new IndexOutOfBoundsException("ERROR Vec2f matmul: supplied matrix must have a length of [2][2]");
		}
		return new Vec2(this.x*matrix[0][0]+this.y*matrix[0][1], this.x*matrix[1][0]+this.y*matrix[1][1]);
	}
	public Vec2 rotateR(float theta) {
		float[][]r = {
					{(float) Math.cos(theta), (float) -Math.sin(theta)},
					{(float) Math.sin(theta), (float) Math.cos(theta)}
				};
		return this.matmul(r);
	}
	public Vec2 rotateD(float theta) {
		return this.rotateR((float)Math.toRadians(theta));
	}
	//invert function (this*-1)
	public Vec2 invt() {
		return this.mult(-1);
	}
	
	//Make a vertex from this Vec2
	public Vertex vert(float u, float v) {
		return new Vertex(this, u, v);
	}
	public Vertex vert(PVector a) {
		return new Vertex(this, a);
	}
	public Vertex vert(Vec2 a) {
		return new Vertex(this, a);
	}
}