package sysan.labs;

import org.la4j.matrix.*;
import org.la4j.vector.*;

import static java.lang.Math.*;

public class NonlinearConjugateGradientMethod {
	private static int maxIterations = 10000;
	
	private static Vector formGradient(Matrix A, Vector x){
		return A.transpose().multiply(A.multiply(x).add(x.multiply(-1.0))).multiply(2);
	}
	
	public static Vector solve(Matrix A, Vector b, Vector initial, double accourancy){
		Vector newX = initial.copy();
		Vector oldX = initial.copy();
		Vector oldP = formGradient(A, initial).multiply(-1.0);
		Vector newP;
		int k = 0;
		double beta = 1;
		while ((k++ < maxIterations) && (formGradient(A, oldX).norm() > accourancy)){			
			newP = formGradient(A, oldX).multiply(-1.0).add(oldP.multiply(beta));
			beta = Math.pow(formGradient(A, newX).norm(), 2) / Math.pow(formGradient(A, oldX).norm(), 2); 
			newX = oldX.add(vector)
		}
		
	}
}
