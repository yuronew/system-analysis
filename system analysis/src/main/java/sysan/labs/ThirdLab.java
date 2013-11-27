package sysan.labs;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.la4j.LinearAlgebra;
import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.Vector;

public class ThirdLab {
	
	private static int precise = 6;
	
	public static void main(String[] args){
		List<Vector> x = new ArrayList<Vector>();
    	List<Vector> y = new ArrayList<Vector>();    	
    	try
    	{    		
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/y1.csv"))).toColumnVector());    	
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/y2.csv"))).toColumnVector());
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/y3.csv"))).toColumnVector());
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/y4.csv"))).toColumnVector());   		
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x11.csv"))).toColumnVector());    		
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x12.csv"))).toColumnVector());    		
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x21.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x22.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x31.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x32.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab3/x33.csv"))).toColumnVector());    		
    	}
    	catch (Exception ex)
    	{
    		System.out.print("Cannot load file: " + ex.getMessage());    		
    		return;
    	}    	
		    	
    	int n = y.get(0).length();
		Matrix Y = LinearAlgebra.BASIC2D_FACTORY.createMatrix(n, y.size());
		Matrix X = LinearAlgebra.BASIC2D_FACTORY.createMatrix(n, x.size());		
		
		for(Vector vector:x){			
			X.setColumn(x.indexOf(vector), vector.add(-vector.min()).divide(vector.max()-vector.min()));
		}
		
		for(Vector vector:y){			
			Y.setColumn(y.indexOf(vector), vector.add(-vector.min()).divide(vector.max()-vector.min()));
		}				
		
		Vector averageY = LinearAlgebra.DENSE_FACTORY.createVector(n);		
		for (int k = 0; k < averageY.length(); k++)
		{
			averageY.set(k, Math.log(((Y.maxInRow(k) + Y.minInRow(k)) / 2) + 1));			
		}
				
		Matrix lambdas = Usages.calculateLambdasForMult(X,averageY, precise);
		System.out.println(lambdas);		
		Matrix lambdas1 = lambdas.slice(0, 0, 2, lambdas.columns());
		Matrix lambdas2 = lambdas.slice(2, 0, 4, lambdas.columns());
		Matrix lambdas3 = lambdas.slice(4, 0, 7, lambdas.columns());
		Vector components1 = Usages.calculateComponentsForMult(X.slice(0, 0, n, 2), lambdas1, Y.getColumn(0).add(1.0));
		Vector components2 = Usages.calculateComponentsForMult(X.slice(0, 2, n, 4), lambdas2, Y.getColumn(1).add(1.0));
		Vector components3 = Usages.calculateComponentsForMult(X.slice(0, 4, n, 7), lambdas3, Y.getColumn(2).add(1.0));
		System.out.println(components1);
		System.out.println(components2);
		System.out.println(components3);
		List<Vector> components = new ArrayList<Vector>();		
		components.add(components1);
		components.add(components2);
		components.add(components3);
		
		List<Matrix> lambdasList = new ArrayList<Matrix>();
		lambdasList.add(lambdas1);
		lambdasList.add(lambdas2);
		lambdasList.add(lambdas3);
	
		Vector result1 = Usages.calculateFinalForMult(components, lambdasList, X, Y.getColumn(0).add(1.0), "resources/lab2/Result3[0].csv");			
		Vector result2 = Usages.calculateFinalForMult(components, lambdasList, X, Y.getColumn(1).add(1.0), "resources/lab2/Result3[1].csv");
		Vector result3 = Usages.calculateFinalForMult(components, lambdasList, X, Y.getColumn(2).add(1.0), "resources/lab2/Result3[2].csv");
		
		System.out.println(result1);
		System.out.println(result2);
		System.out.println(result3);	
	}
}
