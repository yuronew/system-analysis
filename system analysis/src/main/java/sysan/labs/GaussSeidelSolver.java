package sysan.labs;

import org.la4j.LinearAlgebra;
import org.la4j.matrix.*;
import org.la4j.vector.*;

public class GaussSeidelSolver {
	
	private static int maxIterations = 100000;
	
	/**
	 * I took this implementation here: http://p.quinput.eu/websvn/filedetails.php?repname=jal&path=%2Ftrunk%2Fsrc%2Fjatcoregpl%2Falg%2Fsolver%2FGaussSeidel.java&rev=53
	 * http://p.quinput.eu/websvn/filedetails.php?repname=jal&path=%2Ftrunk%2Fsrc%2Fjatcoregpl%2Fmatvec%2Fdata%2FVectorN.java
	 * @param A
	 * @param b
	 * @param initial
	 * @param accourancy
	 * @return
	 */
	public static Vector solve(Matrix A, Vector b, Vector initial, double accourancy){
		int  k = 0;
		double delta = 1.;
		
		Vector newX = LinearAlgebra.BASIC1D_FACTORY.createVector(initial);
		Vector oldX = LinearAlgebra.BASIC1D_FACTORY.createVector(initial);
		
		while ((k < maxIterations) &&(accourancy < delta)){
			k++;
						
			for (int i = 0; i < initial.length(); i++){
				double sum = 0;
				for (int j = 0; j < initial.length(); j++){
					if (j == i){
						sum += A.get(i, j) * newX.get(j);
					}						
				}
				newX.set(i, (- sum + b.get(i)) / A.get(i, i));
			}
			
			//delta = Math.abs(newX.norm())
		}
		
		return newX;
	}

}
