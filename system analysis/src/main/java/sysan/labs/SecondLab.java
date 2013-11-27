package sysan.labs;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.la4j.LinearAlgebra;
import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.Vector;

public class SecondLab {
	
	private static int precise = 7;
	
	public static void  main (String[] args){
		List<Vector> x = new ArrayList<Vector>();
    	List<Vector> y = new ArrayList<Vector>();    	
    	try
    	{    		
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/y1.csv"))).toColumnVector());    	
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/y2.csv"))).toColumnVector());
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/y3.csv"))).toColumnVector());
    		y.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/y4.csv"))).toColumnVector());   		
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x11.csv"))).toColumnVector());    		
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x12.csv"))).toColumnVector());    		
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x21.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x22.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x31.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x32.csv"))).toColumnVector());
    		x.add(new Basic2DMatrix(Matrices.asSymbolSeparatedSource(new FileInputStream("resources/lab2/x33.csv"))).toColumnVector());    		
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
			averageY.set(k, (Y.maxInRow(k) + Y.minInRow(k)) / 2);			
		}
				
		Matrix lambdas = Usages.calculateLambdas(X,averageY, precise);
		System.out.println(lambdas);		
		Matrix lambdas1 = lambdas.slice(0, 0, 2, lambdas.columns());
		Matrix lambdas2 = lambdas.slice(2, 0, 4, lambdas.columns());
		Matrix lambdas3 = lambdas.slice(4, 0, 7, lambdas.columns());
		Vector components1 = Usages.calculateComponents(X.slice(0, 0, n, 2), lambdas1, Y.getColumn(0));
		Vector components2 = Usages.calculateComponents(X.slice(0, 2, n, 4), lambdas2, Y.getColumn(1));
		Vector components3 = Usages.calculateComponents(X.slice(0, 4, n, 7), lambdas3, Y.getColumn(2));
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
	
		Vector result1 = Usages.calculateFinal(components, lambdasList, X, Y.getColumn(0), "resources/lab2/Result[0].csv");			
		Vector result2 = Usages.calculateFinal(components, lambdasList, X, Y.getColumn(1), "resources/lab2/Result[1].csv");
		Vector result3 = Usages.calculateFinal(components, lambdasList, X, Y.getColumn(2), "resources/lab2/Result[2].csv");
		
		System.out.println(result1);
		System.out.println(result2);
		System.out.println(result3);	
		
		
	}

}
