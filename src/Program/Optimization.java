package Program;

import java.awt.Point;
import java.util.ArrayList;

import Values.Data;

public class Optimization {

	private Data data = Data.getInstance();

 	// 二元優化法
 	public ArrayList<Integer> twoOptimization(ArrayList<Integer> chromosome) {
 		int size = chromosome.size();
 		for (int i=0; i<size-2; i++) {
			for (int j=i+2; j<size-1; j++) {
				if (isCross(chromosome.get(i), chromosome.get(i+1), chromosome.get(j), chromosome.get(j+1))) {
					chromosome = sortGene(chromosome, i+1, j);
				}
			}
		}
 		return chromosome;
	}

 	// 判斷是否交叉
 	private boolean isCross(int pointA, int pointB, int pointC, int pointD) {
		double lineAB = Point.distance(data.x[pointA], data.y[pointA], data.x[pointB], data.y[pointB]);
		double lineCD = Point.distance(data.x[pointC], data.y[pointC], data.x[pointD], data.y[pointD]);
		double lineAB_CD = lineAB + lineCD;
		double lineAC = Point.distance(data.x[pointA], data.y[pointA], data.x[pointC], data.y[pointC]);
		double lineBD = Point.distance(data.x[pointB], data.y[pointB], data.x[pointD], data.y[pointD]);
		double lineAC_BD = lineAC + lineBD;
		if (lineAB_CD < lineAC_BD)
			return false;
		else
			return true;
	}
 	
 	// 交換點並重新排序
 	private ArrayList<Integer> sortGene(ArrayList<Integer> chromosome, int min, int max) {
 		ArrayList<Integer> finish = new ArrayList<Integer>();
 		ArrayList<Integer> tmp = new ArrayList<Integer>();
 		int size = chromosome.size();
 		for (int i=0; i<size; i++) {
 			if (i>= min && i<=max)
 				tmp.add(0,chromosome.get(i));
 			else
				finish.add(chromosome.get(i));
 		}
 		finish.addAll(min, tmp);
 		return finish;
 	}
}
