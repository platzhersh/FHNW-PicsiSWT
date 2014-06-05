package utils;

/**
 * Helper class for generating various matrices.
 * It also provides various operations for matrices.
 * @author rt
 *
 */
public class Matrix {

	private double[][] mat = null;

	/**
	 * Creates a matrix filled with 0 with dimensions m * n.
	 * @param m Number of rows.
	 * @param n Number of columns.
	 */
	public Matrix(int m, int n) {
		mat = new double[m][n];
	}

	/**
	 * Creates a Matrix from a 2D array.
	 * @param data 2D array with values.
	 */
	public Matrix(double[][] data) {
		mat = new double[data.length][data[0].length];
		for (int i = 0; i < data.length; ++i) {
			for (int j = 0; j < data[i].length; ++j) {
				mat[i][j] = data[i][j];
			}
		}
	}

	/**
	 * Returns number of rows in the matrix.
	 * @return Number of rows.
	 */
	public int nRows() {
		return mat.length;
	}

	/**
	 * Returns number of columns in the matrix.
	 * @return Number of columns.
	 */
	public int nCols() {
		return mat[0].length;
	}

	/**
	 * Returns the specified element of the matrix.
	 * @param i Row number.
	 * @param j Column number.
	 * @return Value in the specified cell.
	 */
	public double el(int i, int j) {
		return mat[i][j];
	}

	/**
	 * Adds matrix to another matrix.
	 * @param that Matrix to add.
	 * @return Summed matrix.
	 */
	public Matrix plus(Matrix that) {
		Matrix temp = new Matrix(mat.length, mat[0].length);
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[i].length; ++j) {
				temp.mat[i][j] = mat[i][j] + that.mat[i][j];
			}
		}
		return temp;
	}

	/**
	 * Subtracts matrix to another matrix.
	 * @param that matrix to subtract.
	 * @return Difference matrix.
	 */
	public Matrix minus(Matrix that) {
		Matrix temp = new Matrix(mat.length, mat[0].length);
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[i].length; ++j) {
				temp.mat[i][j] = mat[i][j] - that.mat[i][j];
			}
		}
		return temp;
	}

	/**
	 * Multiplies Matrix with a scalar value.
	 * @param factor Scalar value.
	 * @return Product Matrix.
	 */
	public Matrix times(double factor) {
		Matrix temp = new Matrix(mat.length, mat[0].length);
		for (int i = 0; i < mat.length; ++i) {
			for (int j = 0; j < mat[i].length; ++j) {
				temp.mat[i][j] = mat[i][j] + factor;
			}
		}
		return temp;
	}

	/**
	 * Multiplies Matrix * Matrix.
	 * @param that Matrix to multiply.
	 * @return Product Matrix.
	 */
	public Matrix times(Matrix that) {
		Matrix temp = new Matrix(mat.length, that.mat[0].length);
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < that.mat[i].length; j++) {
				for (int k = 0; k < that.mat.length; k++) {
					temp.mat[i][j] += mat[i][k] * that.mat[k][j];
				}
			}
		}
		return temp;
	}

	/**
	 * Multiplies Matrix * Vector.
	 * @param that Vector to multiply.
	 * @return Product Vector.
	 */
	public Vector times(Vector that) {
		double[] res = new double[mat.length];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				res[i] += mat[i][j] * that.x(j);
			}
		}
		return new Vector(res);
	}

	/**
	 * Transposes a matrix.
	 * @return Transposed matrix.
	 */
	private Matrix transpose() {
		double[][] trans = new double[mat[0].length][mat.length];
		for (int i = 0; i < mat[0].length; ++i) {
			for (int j = 0; j < mat.length; ++j) {
				trans[i][j] = mat[j][i];
			}
		}
		return new Matrix(trans);
	}

	/**
	 * Returns an identity matrix.
	 * @param n Number of rows/columns.
	 * @return Identity matrix.
	 */
	static private Matrix identity(int n) {
		Matrix temp = new Matrix(n, n);
		for (int i = 0; i < n; ++i)
			temp.mat[i][i] = 1;
		return temp;
	}

	/**
	 * Returns a rotation matrix.
	 * @param phi Rotation degree.
	 * @return Translation matrix.
	 */
	static public Matrix rotation(double phi) {
		double c = Math.cos(Math.toRadians(phi));
		double s = Math.sin(Math.toRadians(phi));
		double[][] rot = {
				{ c,-s, 0 },
				{ s, c, 0 },
				{ 0, 0, 1 } };
		return new Matrix(rot);
	}

	/**
	 * Returns a scale matrix.
	 * Scale <1 to zoom in. Scale >1 to zoom out.
	 * @param scale Scale factor.
	 * @return Scale matrix.
	 */
	static public Matrix scale(double scale) {
		Matrix ret = Matrix.identity(3);
		ret.mat[0][0] = scale;
		ret.mat[1][1] = scale;
		return ret;
	}

	/**
	 * Returns a translation matrix.
	 * @param a1 Move in x direction.
	 * @param a2 Move in y direction.
	 * @return Translation matrix.
	 */
	static public Matrix translation(double a1, double a2) {
		Matrix temp = Matrix.identity(3);
		temp.mat[0][2] = a1;
		temp.mat[1][2] = a2;
		return temp;
	}

	/**
	 * Prints this Matrix to console.
	 */
	public void print() {
		for (double[] i : mat) {
			for (double j : i) {
				System.out.print(j + "\t");
			}
			System.out.println();
		}
	}

	/** 
	 * Prints this Matrix to console with rounded values.
	 * @param round Number of digits to round.
	 */
	public void print(int round) {
		for (double[] i : mat) {
			for (double j : i) {
				System.out.println(Math.round((float) j) + "\t");
			}
			System.out.println();
		}
	}
}
