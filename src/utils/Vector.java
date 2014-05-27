package utils;
public class Vector {
	private double[] vec;

	/*
	 * create the zero vector of dimension n
	 */
	public Vector(int n) {
		vec = new double[n];
	}

	/*
	 * create a vector from an array (array is copied)
	 */
	public Vector(double data[]) {
		vec = new double[data.length];
		for (int i = 0; i < vec.length; ++i) {
			vec[i] = data[i];
		}
	}

	/*
	 * create vector from vararg list (values are copied)
	 */
	public Vector(double d, double... data) {
		vec = new double[data.length + 1];
		vec[0] = d;
		for (int i = 1; i < vec.length; ++i) {
			vec[i] = data[i - 1];
		}
	}

	/*
	 * dimension of the vector
	 */
	public int length() {
		return vec.length;
	}

	/*
	 * return x_i
	 */
	public double x(int i) {
		return vec[i];
	}

	/*
	 * vector addition
	 */
	public Vector plus(Vector that) {
		Vector temp = new Vector(vec);
		for (int i = 0; i < temp.vec.length; ++i) {
			temp.vec[i] += that.vec[i];
		}
		return temp;
	}

	/*
	 * vector subtraction
	 */
	public Vector minus(Vector that) {
		Vector temp = new Vector(vec);
		for (int i = 0; i < temp.vec.length; ++i) {
			temp.vec[i] -= that.vec[i];
		}
		return temp;
	}

	/*
	 * product scalar * vector
	 */
	public Vector times(double factor) {
		Vector temp = new Vector(vec);
		for (int i = 0; i < temp.vec.length; ++i)
			temp.vec[i] *= factor;
		return temp;
	}

	/*
	 * dotproduct
	 */
	public double dot(Vector that) {
		double ret = 0.0;
		for (int i = 0; i < vec.length; ++i)
			ret += vec[i] * that.vec[i];
		return ret;
	}

	/*
	 * norm
	 */
	public double magnitude() {
		double square = 0.0;
		for (double x : vec)
			square += Math.pow(x, 2);
		return Math.sqrt(square);
	}

	/*
	 * crossproduct
	 */
	public Vector cross(Vector that) {
		Vector temp = new Vector(vec);
		for (int i = 0; i < temp.vec.length; ++i) {
			temp.vec[i] = vec[(i + 1) % temp.vec.length]
					* that.vec[(i + 2) % temp.vec.length]
					- vec[(i + 2) % temp.vec.length]
					* that.vec[(i + 1) % temp.vec.length];
		}
		return temp;
	}

	/*
	 * Euclidian distance
	 */
	public double distanceTo(Vector that) {
		double ret = 0.0;
		for (int i = 0; i < vec.length; ++i) {
			ret += Math.pow(vec[i] - that.vec[i], 2);
		}
		return Math.sqrt(ret);

	}

	/*
	 * return the corresponding unit vector
	 */
	public Vector direction() {
		double t = 0.0;
		for (int i = 0; i < vec.length; ++i)
			t += Math.pow(vec[i], 2);
		Vector temp = new Vector(vec);
		for (int i = 0; i < vec.length; ++i)
			temp.vec[i] /= t;
		return temp;
	}

	public void print() {
		System.out.println("---");
		for (double x : vec)
			System.out.println(x);
		System.out.println("---");
	}

}
