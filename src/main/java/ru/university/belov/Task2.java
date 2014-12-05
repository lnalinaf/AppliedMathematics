package ru.university.belov;

import java.util.Arrays;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jblas.*;


public class Task2 extends Application {
	static int n = 1000;
	static int x0 = 2;
	static double X0 = 0;
	static double Xmax = 20;
	static double kappa = 5;
	static double h = (Xmax - X0) / (n + 1);
	private final static LinkedList<XYChart.Series<Number, Number>> lines = new LinkedList<>();

	public static double morze(double x){
		return kappa*kappa*(Math.exp(-2*(x-x0)) - 2*Math.exp(-(x-x0)));
	}

	public static void main(String[] args) {
		DoubleMatrix matrixA = new DoubleMatrix(n,n);
		for(int i = 0; i < n; i++ ){
			if(i == 0){
				matrixA.put(i, i, 2d / (h * h) );
				matrixA.put(i, i + 1, -1d / (h * h));
				continue;
			}
			if(i == n - 1){
				matrixA.put(i, i, 2d / (h * h));
				matrixA.put(i, i - 1, -1d / (h * h));
				break;
			}
			matrixA.put(i, i - 1, -1d / (h * h));
			matrixA.put(i, i, 2d / (h * h));
			matrixA.put(i, i + 1, -1d / (h * h));

		}
		DoubleMatrix matrixB = new DoubleMatrix(n,n);
		for(int i = 0; i < n; i++ ){
			if (i == 0){
				matrixB.put(i, i, 10d/12d);
				matrixB.put(i, i + 1, 1d / 12d);
				continue;
			}
			if (i == n - 1){
				matrixB.put(i, i, 10d / 12d);
				matrixB.put(i, i - 1, 1d / 12d);
				break;
			}
			matrixB.put(i, i - 1, 1d / 12d);
			matrixB.put(i, i, 10d / 12d);
			matrixB.put(i, i + 1, 1d / 12d);

		}
		DoubleMatrix matrixV = new DoubleMatrix(n,n);
		for(int i = 0; i < n; i++ ){
			matrixV.put(i, i , morze(h*i));

		}

		//System.out.println(Task1.matrixToString(matrixA));
		DoubleMatrix res = new DoubleMatrix(n,n);
		res.copy(matrixV);
		res = res.add(Solve.pinv(matrixB).mmul(matrixA));
		double[] eigenValues = Eigen.symmetricEigenvalues(res).toArray();
		Arrays.sort(eigenValues);

		System.out.println(Arrays.toString(eigenValues));
		System.out.println(Arrays.toString(energy()));


		double[] eigenVector = Eigen.symmetricEigenvectors(res)[0].getColumn(0).toArray();
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
		for (int i = 0; i < eigenVector.length; i++)
			series.getData().add(new XYChart.Data<>((Number)(i * h), (Number)(eigenVector[i])));
		lines.add(series);
		Application.launch(args);

	}
	static double[] energy(){
		double[] en = new double[5];
		for(int i = 0; i < 5; i++){
			en[i] = -(kappa - i - 0.5)*(kappa - i - 0.5);
		}
		return en;
	}

	@Override
	public void start(Stage stage) throws Exception {
		StackPane root = new StackPane();
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();

		LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

		chart.setHorizontalGridLinesVisible(true);
		chart.setVerticalGridLinesVisible(true);
		chart.getData().addAll(lines);
		root.getChildren().add(chart);
		stage.setTitle("График функции");
		stage.setScene(new Scene(root, 700, 500));
		stage.show();

	}
//	public static String matrixToString(DoubleMatrix matrix) {
//		return matrix.toString("%f", "[", "]", "|", ";\n");
//	}

}
