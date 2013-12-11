package sysan.labs;

import sysan.labs.polynomials.*;

import java.io.PrintWriter;
import java.util.List;

import org.la4j.LinearAlgebra;
import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;

import sysan.labs.polynomials.Polynomial;

public class Usages {	
	
	private static double eps = 1e-7;
	
	private static Polynomial polynomial = new ShiftedChebyshev();
	private static Polynomial polynomialForMult = new Legendre(); 
	
	private static double compareVectors(Vector first, Vector second){
		if (first.length() != second.length()){
			throw new IllegalArgumentException("Vectors dimensions does not match! ");
		}
			
		Vector tmp = first.add(second.multiply(-1.0));
		for (int i = 0; i < tmp.length(); i++){
			if (tmp.get(i) < 0){
				tmp.set(i, tmp.get(i) * -1.0);
			}
		}
		
		return tmp.max();
	}
	
	private static double psi(Vector lambdas, double x){
		double result = 0;
		for (int i = 0; i < lambdas.length(); i++){
			result += lambdas.get(i) * polynomial.value(i, x); 
		}
		return result;
	}
	
	private static double fi(Vector components, Matrix lambdas, Vector x){
		if (components.length() != x.length()){
			throw new IllegalArgumentException("Error while calculating function fi: vector " +
					"a and x dimensions does not mathes!");
		}
		double result = 0;
		for (int i = 0; i < components.length(); i++){
			result += components.get(i) * psi(lambdas.getRow(i), x.get(i)); 
		}
		return result;
	}
	
	private static double fiForMult(Vector components, Matrix lambdas, Vector x){
		if (components.length() != x.length()){
			throw new IllegalArgumentException("Error while calculating function fi: vector " +
					"a and x dimensions does not mathes!");
		}
		double result = 0;
		for (int i = 0; i < components.length(); i++){
			result += components.get(i) * Math.log(1 + psiForMult(lambdas.getRow(i), x.get(i))); 
		}
		return Math.exp(result) - 1;
	}
	
	private static double psiForMult(Vector lambdas, double x){
		double result = 0;
		for (int i = 0; i < lambdas.length(); i++){
			result += lambdas.get(i) * Math.log((1 + polynomialForMult.value(i, x))); 
		}
		return Math.exp(result) - 1;
	}
	
	
		
	/**
	 * Calculates values of coefficients for approximating  
	 * of our functions on last stage. Approximates function with sum
	 * of Shifted Chebyshev polynomials {@link Usages#ShiftedChebyshev}
	 * @param X - matrix with columns of X values
	 * @param vector - right part of system 
	 * @param precise - how much Chebyshev polynomials to take 
	 * @return lambdas - vector of coefficients
	 */
	public static Matrix calculateLambdas(Matrix X, Vector vector, int precise)
    {
    	Matrix lambdasMatrix = LinearAlgebra.BASIC2D_FACTORY.createMatrix(vector.length(), X.columns() * precise);    	
    	for (int i = 0; i < lambdasMatrix.rows(); i++){
			for (int j = 0; j < lambdasMatrix.columns(); j++){				
				double x = X.get(i, j / precise);
				lambdasMatrix.set(i, j, polynomial.value(j % precise, x));				
			}			
		}
    	    	
    	try{    
    		//Vector lambdas = lambdasMatrix//    		
    		Vector init = LinearAlgebra.BASIC1D_FACTORY.createVector(lambdasMatrix.columns());
    		Vector lambdas = NonlinearConjugateGradientMethod.solve(lambdasMatrix, vector, init, eps);
    		Vector check = lambdasMatrix.multiply(lambdas);    		
    		System.out.println("Lambdas inaccuracy: " + compareVectors(check, vector));
    		Matrix result = LinearAlgebra.BASIC2D_FACTORY.createMatrix(X.columns(), precise);
    		for (int i = 0; i < X.columns(); i++){
    			result.setRow(i, lambdas.slice(i * precise, (i + 1) * precise));
    		}
        	return result;      		 	
    	}
    	catch (Exception ex){    		
    		System.out.println(ex.getMessage());
    		return null;
    	}    	    	    
    }
	
	/**
	 * Calculates values of coefficients for approximating  
	 * of our functions on last stage. Approximates function with sum
	 * of Shifted Chebyshev polynomials {@link Usages#ShiftedChebyshev}
	 * @param X - matrix with columns of X values
	 * @param vector - right part of system 
	 * @param precise - how much Chebyshev polynomials to take 
	 * @return lambdas - vector of coefficients
	 */
	public static Matrix calculateLambdasForMult(Matrix X, Vector vector, int precise)
    {
    	Matrix lambdasMatrix = LinearAlgebra.BASIC2D_FACTORY.createMatrix(vector.length(), X.columns() * precise);    	
    	for (int i = 0; i < lambdasMatrix.rows(); i++){
			for (int j = 0; j < lambdasMatrix.columns(); j++){				
				double x = X.get(i, j / precise);
				double value = Math.log(1 + polynomialForMult.value(j % precise, x));
				lambdasMatrix.set(i, j, value);				
			}			
		}
    	    	
    	try{    
    		//Vector lambdas = lambdasMatrix.withSolver(SOLVER).solve(vector);
    		Vector init = LinearAlgebra.BASIC1D_FACTORY.createVector(lambdasMatrix.columns());
    		Vector lambdas = NonlinearConjugateGradientMethod.solve(lambdasMatrix, vector, init, eps);
    		System.out.println(lambdasMatrix);
    		Vector check = lambdasMatrix.multiply(lambdas);    		
    		System.out.println("Lambdas inaccuracy: " + compareVectors(check, vector));
    		Matrix result = LinearAlgebra.BASIC2D_FACTORY.createMatrix(X.columns(), precise);
    		for (int i = 0; i < X.columns(); i++){
    			result.setRow(i, lambdas.slice(i * precise, (i + 1) * precise));
    		}
        	return result;      		 	
    	}
    	catch (Exception ex){    		
    		System.out.println(ex.getMessage());
    		return null;
    	}    	    	    
    }
	
	/**
	 * Calculates values of coefficients for approximating  
	 * of our functions on middle stage. Approximates function with sum
	 * of function psi (based on lambdas)
	 * @param X
	 * @param lambdas
	 * @param vector
	 * @return
	 */
	public static Vector calculateComponents(Matrix X, Matrix lambdas, Vector vector){
		Matrix components = LinearAlgebra.BASIC2D_FACTORY.createMatrix(vector.length(), X.columns());
		 
		for (int i = 0; i < components.rows(); i++){
			for (int j = 0; j < X.columns(); j++){
				components.set(i, j, psi(lambdas.getRow(j), X.getColumn(j).get(i)));
			}
		}
		
		try{
			//Vector result = components.withSolver(SOLVER).solve(vector);
			Vector init = LinearAlgebra.BASIC1D_FACTORY.createVector(components.columns());
			Vector result = NonlinearConjugateGradientMethod.solve(components, vector, init, eps);
			Vector check = components.multiply(result);
			System.out.println("Components searching inaccuracy: " + compareVectors(check, vector));
			return result;
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}				
	}
	
	/**
	 * Calculates values of coefficients for approximating  
	 * of our functions on middle stage. Approximates function with sum
	 * of function psi (based on lambdas)
	 * @param X
	 * @param lambdas
	 * @param vector
	 * @return
	 */
	public static Vector calculateComponentsForMult(Matrix X, Matrix lambdas, Vector vector){
		Matrix components = LinearAlgebra.BASIC2D_FACTORY.createMatrix(vector.length(), X.columns());
		for (int i = 0; i < components.rows(); i++){
			for (int j = 0; j < X.columns(); j++){
				components.set(i, j, Math.log( 1 + psiForMult(lambdas.getRow(j), X.getColumn(j).get(i))));
			}
		}
		
		try{
			//Vector result = components.withSolver(SOLVER).solve(vector);
			Vector init = LinearAlgebra.BASIC1D_FACTORY.createVector(components.columns());
			Vector result = NonlinearConjugateGradientMethod.solve(components, vector, init, eps);
			Vector check = components.multiply(result);
			System.out.println("Components searching inaccuracy: " + compareVectors(check, vector));
			return result;
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}				
	}
	
	/**
	 * Calculates final coefficients for function. Approximates with sum of functions fi 
	 * (based on psi). Write result to filePath.
	 * @param components
	 * @param lambdas
	 * @param X
	 * @param vector
	 * @param filePath
	 * @return
	 */
	public static Vector calculateFinal(List<Vector> components, List<Matrix> lambdas, List<Matrix> X, Vector vector, String filePath){
		Matrix fnl = LinearAlgebra.BASIC2D_FACTORY.createMatrix(vector.length(), components.size());
		for (int i = 0; i < fnl.rows(); i++){
			for (int j = 0; j < fnl.columns(); j++){
				double x = fi(components.get(j), lambdas.get(j), X.get(j).getRow(i));
				fnl.set(i, j, x);
			}
		}
		
		try{
			//Vector result = fnl.withSolver(SOLVER).solve(vector);
			Vector init = LinearAlgebra.BASIC1D_FACTORY.createVector(fnl.columns());
			Vector result = NonlinearConjugateGradientMethod.solve(fnl, vector, init, eps);			
						
			Vector check = LinearAlgebra.BASIC1D_FACTORY.createVector(vector.length());
			for (int i = 0; i < check.length(); i++){
				double sum = 0;
				for (int j = 0; j < components.size(); j++){
					sum += result.get(j) * fi(components.get(j), lambdas.get(j), X.get(j).getRow(i));
				}
				check.set(i, sum);
			}
			
			System.out.println("Final searching inaccurancy: " + compareVectors(check, vector));
			
			Matrix out = LinearAlgebra.BASIC2D_FACTORY.createMatrix(check.length(), 2);
			out.setColumn(0, vector);
			out.setColumn(1, check);
			PrintWriter outFile = null;
			try{
				outFile = new PrintWriter(filePath);
				outFile.write(out.toString().replaceAll("  ", ", ").replaceAll(" -", ",-"));			
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			finally{
				outFile.close();
			}
			
			
			return result;
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}				
	}
	
	/**
	 * Calculates final coefficients for function. Approximates with sum of functions fi 
	 * (based on psi). Write result to filePath.
	 * @param components
	 * @param lambdas
	 * @param X
	 * @param vector
	 * @param filePath
	 * @return
	 */
	public static Vector calculateFinalForMult(List<Vector> components, List<Matrix> lambdas, List<Matrix> X, Vector vector, String filePath){
		Matrix fnl = LinearAlgebra.BASIC2D_FACTORY.createMatrix(vector.length(), components.size());
		for (int i = 0; i < fnl.rows(); i++){
			for (int j = 0; j < fnl.columns(); j++){
				double x = Math.log(1 + fiForMult(components.get(j), lambdas.get(j), X.get(j).getRow(i)));
				fnl.set(i, j, x);
			}
		}
		
		try{
			//Vector result = fnl.withSolver(SOLVER).solve(vector);
			Vector init = LinearAlgebra.BASIC1D_FACTORY.createVector(fnl.columns());
			Vector result = NonlinearConjugateGradientMethod.solve(fnl, vector, init, eps);
			Vector check = fnl.multiply(result);					
			
			System.out.println("Final searching inaccurancy: " + compareVectors(check, vector));
			
			Matrix out = LinearAlgebra.BASIC2D_FACTORY.createMatrix(check.length(), 2);
			out.setColumn(0, vector);
			out.setColumn(1, check);
			PrintWriter outFile = null;
			try{
				outFile = new PrintWriter(filePath);
				outFile.write(out.toString().replaceAll("  ", ", ").replaceAll(" -", ",-"));			
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			finally{
				outFile.close();
			}
			
			
			return result;
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}				
	}
		
	
}
