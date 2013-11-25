package sysan.labs;

import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;
import org.la4j.*;
import org.la4j.vector.Vector;

public class TEST {
	public static void main (String args[]){
		Matrix a = new Basic2DMatrix(new double[][] {
				   { 1.0, 2.0, 4.0 },
				   { 4.0, 5.0, 6.0 },
				   { 7.0, 8.0, 9.0 }
				});

				// A right hand side vector, which is simple dense vector
				Vector b = new BasicVector(new double[] {
				   1.0, 2.0, 3.0
				});
				
				// We will use standard Forward-Back Substitution method,
				// which is based on LU decomposition and can be used with square systems
				LinearSystemSolver solver = a.withSolver(LinearAlgebra.FORWARD_BACK_SUBSTITUTION);
				// The 'x' vector will be sparse
				System.out.print(a.multiply(solver.solve(b)));
	}
}
