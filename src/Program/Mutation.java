package Program;

import java.util.ArrayList;
import java.util.Random;

import Values.Parameter;

public class Mutation {

	private Parameter parameter = Parameter.getInstance();
	private Optimization optimization = new Optimization();
	
	// 突變機制
	public ArrayList<Integer>[] mutation(ArrayList<Integer>[] chromosome) {
		Random random = new Random();
		double rate = parameter.getOptRate();
		int listSize = chromosome.length;
		for (int i=0; i<listSize; i++) {
			double ran = random.nextDouble();
			if (parameter.getMutationRateC() >= ran && i < listSize/2)
				chromosome[i] = pointSwap(chromosome[i]);
			if (parameter.getMutationRateE() >= ran && i >= listSize/2)
				chromosome[i] = pointSwap(chromosome[i]);
			ran = random.nextDouble();
			if (rate >= ran) {
				chromosome[i] = optimization.twoOptimization(chromosome[i]);
			}
		}
		this.parameter = null;
		this.optimization = null;
		return chromosome;
	}

 	// 兩點交換，隨機選擇兩點進行交換
 	private ArrayList<Integer> pointSwap(ArrayList<Integer> chromosome) {
		Random random = new Random();
		int size = chromosome.size();
		int position1 = random.nextInt(size);
		int position2 = random.nextInt(size);
		while (position1 == position2) {
			position1 = random.nextInt(size);
			position2 = random.nextInt(size);
		}
		int tmp1 = chromosome.get(position1);
		int tmp2 = chromosome.get(position2);
		chromosome.remove(position1);
		chromosome.add(position1, tmp2);
		chromosome.remove(position2);
		chromosome.add(position2, tmp1);
		return chromosome;
	}
 	
}
