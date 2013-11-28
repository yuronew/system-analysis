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
	
	private static int precise = 10;
	
	public static void main(String[] args){
		List<Vector> x = new ArrayList<Vector>();
    	List<Vector> y = new ArrayList<Vector>();    
    	int x1 = 2;
    	int x2 = 2;
    	int x3 = 3;
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
		
		for(int i = 0; i < Y.columns(); i++){
			
			Matrix lambdas = Usages.calculateLambdasForMult(X,Y.getColumn(i), precise);
			System.out.println(lambdas);		
			Matrix lambdas1 = lambdas.slice(0, 0, x1, lambdas.columns());
			Matrix lambdas2 = lambdas.slice(x1, 0, x1 + x2, lambdas.columns());
			Matrix lambdas3 = lambdas.slice(x1 + x2, 0, x1 + x2 + x3, lambdas.columns());
			
			Vector components = Usages.calculateComponentsForMult(X, lambdas, Y.getColumn(i));		
			List<Vector> components1 = new ArrayList<Vector>();
			
			components1.add(components.slice(0, x1));
			components1.add(components.slice(x1, x1 + x2));
			components1.add(components.slice(x1 + x2, x1 + x2 + x3));
					
			List<Matrix> lambdasList = new ArrayList<Matrix>();
			lambdasList.add(lambdas1);
			lambdasList.add(lambdas2);
			lambdasList.add(lambdas3);
			
			List<Matrix> xList= new ArrayList<Matrix>();
			xList.add(X.slice(0, 0, n, x1));
			xList.add(X.slice(0, x1, n, x1 + x2));
			xList.add(X.slice(0, x1 + x2, n, x1 + x2 + x3));
		
			Vector result = Usages.calculateFinalForMult(components1, lambdasList, xList, 
					Y.getColumn(i), "resources/lab3/Result[" + i +"].csv");			
		
			System.out.println(result);	
		}
	}
}
