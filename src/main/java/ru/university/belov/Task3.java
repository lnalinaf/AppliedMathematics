package ru.university.belov;

import java.util.Arrays;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jblas.Solve;


public class Task3 extends Application {
	public static int n = 1000;

	static double X0 = 0;
	static double Xmax = 50;

	 public static double h = (Xmax - X0) / (n + 1);
	private final static LinkedList<XYChart.Series<Number, Number>> lines = new LinkedList<>();

	public static double coloumb(double x){
		return (-2d / x);
	}

	public static void main(String[] args) {

		DoubleMatrix matrixA = new DoubleMatrix(n, n);
		double cons = 3d / h / h;
		for(int i = 0; i < n; i++ ){

			if(i == 0){
				matrixA.put(i, i, cons + coloumb((i+1)*h));
				matrixA.put(i, i + 1, -cons / 2d + (coloumb((i+1)*h))/4d);
				continue;
			}
			if(i == n - 1){
				matrixA.put(i, i, cons + coloumb((i+1)*h));
				matrixA.put(i, i - 1, -cons / 2d + (coloumb((i+1)*h))/4d);
				break;
			}
			matrixA.put(i, i - 1, -cons / 2d + (coloumb((i+1)*h))/4d);
			matrixA.put(i, i, cons + coloumb((i+1)*h));
			matrixA.put(i, i + 1, -cons / 2d + (coloumb((i+1)*h))/4d);

		}

		DoubleMatrix matrixB = new DoubleMatrix(n, n);
		for(int i = 0; i < n; i++ ){

			if(i == 0){
				matrixB.put(i, i, 1d);
				matrixB.put(i, i + 1, 1d / 4d);
				continue;
			}
			if(i == n - 1){
				matrixB.put(i, i,1d);
				matrixB.put(i, i - 1, 1d / 4d);
				break;
			}
			matrixB.put(i, i - 1, 1d / 4d);
			matrixB.put(i, i, 1d);
			matrixB.put(i, i + 1, 1d / 4d);

		}
		DoubleMatrix res = new DoubleMatrix(n, n);

		res = res.add(Solve.pinv(matrixB).mmul(matrixA));
		double[] eigenValues = Eigen.symmetricEigenvalues(res).toArray();
		Arrays.sort(eigenValues);
		//System.out.println(matrixToString(matrixA));


		System.out.println(Arrays.toString(eigenValues));
		System.out.println(Arrays.toString(energy()));
		//System.out.println(Arrays.toString(lines.toArray()));


		double[] eigenVector0 = Eigen.symmetricEigenvectors(matrixA)[0].getColumn(0).toArray();
		XYChart.Series<Number, Number> series0 = new XYChart.Series<>();
		for (int i = 0; i < eigenVector0.length; i++)
			series0.getData().add(new XYChart.Data<>((Number)(i * h), (Number)(eigenVector0[i])));
		lines.add(series0);

		double[] eigenVector1 = Eigen.symmetricEigenvectors(matrixA)[0].getColumn(1).toArray();
		XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
		for (int i = 0; i < eigenVector1.length; i++)
			series1.getData().add(new XYChart.Data<>((Number)(i * h), (Number)(eigenVector1[i])));
		lines.add(series1);

		double[] eigenVector2 = Eigen.symmetricEigenvectors(matrixA)[0].getColumn(2).toArray();
		XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
		for (int i = 0; i < eigenVector2.length; i++)
			series2.getData().add(new XYChart.Data<>((Number)(i * h), (Number)(eigenVector2[i])));
		lines.add(series2);

		Application.launch(args);

	}
	static double[] energy(){
		double[] en = new double[5];
		for(int i = 1; i < 5; i++){
			en[i - 1] = -(1d / (double)i / (double)i);
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
		stage.setTitle("Собственные функции");
		stage.setScene(new Scene(root, 700, 500));
		stage.show();

	}
	public static String matrixToString(DoubleMatrix matrix) {
		return matrix.toString("%f", "[", "]", "|", ";\n");
	}

}
