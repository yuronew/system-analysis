package sysan.labs;

import org.la4j.matrix.*;
import org.la4j.vector.*;

public class NonlinearConjugateGradientMethod {
	private static int maxIterations = 10000;
		
	private static double form(Matrix A, Vector x, Vector b){
		return Math.pow(A.multiply(x).add(b.multiply(-1.0)).norm(), 2);
	}
	
	private static Vector formGradient(Matrix A, Vector x, Vector b){
		return A.transpose().multiply(A.multiply(x).add(b.multiply(-1.0))).multiply(2);
	}
	
	private static Vector alphaLineSearch(Matrix A, Vector b, Vector x, Vector s){
		int k = 0;
		
		Vector newX = x.copy();
		Vector oldX = x.copy();
		double newF = form(A, x, b);
		double oldF = form(A, x, b);
		double alpha = 0.00001;
		
		do{
			k++;
			oldX = newX;
			newX = oldX.add(s.multiply(alpha));
			oldF = newF;
			newF = form(A, newX, b);
			
		}while((newF < oldF) && (k < maxIterations));
		
		return  oldX;
		
	}
	
	public static Vector solve(Matrix A, Vector b, Vector initial, double accourancy){		
		Vector oldX = initial.copy();				
		Vector oldGradient = formGradient(A, initial, b).multiply(-1.0);
		Vector newX = alphaLineSearch(A, b, oldX, oldGradient);
		Vector newGradient = formGradient(A, initial, b).multiply(-1.0);
		Vector direction = newGradient.copy();
		
		int k = 0;
		double beta = 1;
		while ((k++ < maxIterations) && (formGradient(A, oldX, b).norm() > accourancy)){			
			newGradient = formGradient(A, newX, b).multiply(-1.0);
			beta = Math.pow(formGradient(A, newX, b).norm(), 2) / Math.pow(formGradient(A, oldX, b).norm(), 2);
			direction = newGradient.add(direction.multiply(beta));
			oldX = newX.copy();
			newX = alphaLineSearch(A, b, oldX, direction);		
		}
		
		return oldX;
		
	}
}
