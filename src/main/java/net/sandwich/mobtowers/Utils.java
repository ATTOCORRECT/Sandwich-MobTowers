package net.sandwich.mobtowers;

import org.joml.Vector3f;

public class Utils {
	
	public static float decayAngle(float a, float b, float decay, float delta) {
		float difference = (float)Math.IEEEremainder(b - a, 2 * Math.PI);
		float decayedDifference = difference * (float)Math.exp(-decay * delta);
		float result = (b + decayedDifference) % (2 * (float)Math.PI);
		result += result < 0 ? (2 * (float)Math.PI) : 0;
		return result;
	}

	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	public static Vector3f lerp(Vector3f a, Vector3f b, float t) {
		return a.add((b.sub(a)).mul(t));
	}

	public static float lerpAngle(float a, float b, float t) {
		float difference = (float)Math.IEEEremainder(b - a, 2 * Math.PI);
		return a + difference * t;
	}

	public static float easing(float p1, float p2, float t) {
		return bezier(0, p1, p2, 1, t);
	}


	public static float bezier(float p0, float p1, float p2, float p3, float t) {
		return 
			p0 * (-(t * t * t) + 3 * (t * t) - 3 * t + 1) +
			p1 * (3 * (t * t * t) - 6 * (t * t) + 3 * t) +
			p2 * (-3 * (t * t * t) + 3 * (t * t)) +
			p3 * (t * t * t);
	}
	
}
