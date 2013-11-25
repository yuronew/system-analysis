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
			X.setColumn(x.indexOf(vector), vector);
		}
		
		for(Vector vector:y){			
			Y.setColumn(y.indexOf(vector), vector);
		}				
		
		System.out.println(X);
		System.out.println(Y);
	}
}
