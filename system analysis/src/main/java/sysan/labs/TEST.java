package sysan.labs;

import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;
import org.la4j.*;
import org.la4j.LinearAlgebra.SolverFactory;
import org.la4j.vector.Vector;

import org.apache.commons.math3.optim.SimplePointChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.*;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer.Formula;

public class TEST {
	public static void main (String args[]){
//		
//		Matrix a = new Basic2DMatrix(new double[][] {
//				   { 16.0, 3.0 },
//				   { 7.0, -11.0},				   
//				});
//
//				
//				Vector b = new BasicVector(new double[] {
//				   11.0, 13.0
//				});
//				
//				GaussSeidelSolver solver = new GaussSeidelSolver();
//				
//				Vector result = solver.solve(a, b, new BasicVector(2), 0.0001);
//				Vector result2 = a.withSolver(SolverFactory.GAUSSIAN).solve(b);
//				
//				System.out.println(result);
//				System.out.println(a.multiply(result));
//				System.out.println(result2);
//				System.out.println(a.multiply(result2));
		
		Matrix a = new Basic2DMatrix(new double[][] {
				   { 1.0, 2.0, 3.0 },
				   { 4.0, 5.0, 6.0 },
				   { 7.0, 8.0, 9.0 }
				});

				// A right hand side vector, which is simple dense vector
				Vector b = new BasicVector(new double[] {
				   1.0, 2.0, 3.0
				});
		NonLinearConjugateGradientOptimizer optimizer = 
				new NonLinearConjugateGradientOptimizer(Formula.FLETCHER_REEVES, );
		
	}
}
