package pewter;

//stoopid math lib for who knows what reason..................

public class PMath {
	static final float Clamp(float min, float max, float value) {
		if ((value <= max)&&(value >= min)) {
			return value;
		} else if (value > max) {
			return max;
		} else {
			return min;
		}
	}
	//
	static final float Mean(float a, float b) {
		return (a+b)/2;
	}
	static final float Lerp(float a, float b, float alpha) {
		if ((alpha >=0)&&(alpha <= 1)) {
			return a*alpha+b*(1-alpha);
		}
		return a;
	}
	static final float EPSILON = Float.MIN_VALUE*Float.MIN_VALUE; //A TINY value slightly larger than Float.MIN_VALUE
	
	static final boolean Equals(float a, float b) {
		return Math.abs(a-b)<=PMath.EPSILON;
	}
}
