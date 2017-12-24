package Program;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Values.Parameter;

public class Crossover {

	private Parameter parameter = Parameter.getInstance();
	
	// 交配機制
	public ArrayList<Integer>[] crossover(ArrayList<Integer>[] selectPool) {
		Set<Integer> isCrossover = new HashSet<Integer>();
		Random random = new Random();
		int listSize = selectPool.length;
		while (isCrossover.size() != listSize) {
			int crossoverNumber1 = random.nextInt(listSize);
			int crossoverNumber2 = random.nextInt(listSize);
			while (!isCrossover.add(crossoverNumber1)) {
				crossoverNumber1 = random.nextInt(listSize);
			}
			while (!isCrossover.add(crossoverNumber2)) {
				crossoverNumber2 = random.nextInt(listSize);
			}
			if (parameter.getCossoverRate() >= random.nextDouble()) {
				ArrayList<Integer>[] tmp = swapChromosome(selectPool[crossoverNumber1], selectPool[crossoverNumber2]);
				selectPool[crossoverNumber1] = tmp[0];
				selectPool[crossoverNumber2] = tmp[1];
			}
		}
		this.parameter = null;
		return selectPool;
	}

 	// 多點互換，交換染色體內基因
 	private ArrayList<Integer>[] swapChromosome(ArrayList<Integer> chromosome1, ArrayList<Integer> chromosome2) {
 		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] finish = new ArrayList[2];
 		ArrayList<Integer> chromosomeOne = new ArrayList<Integer>();
 		ArrayList<Integer> chromosomeTwo = new ArrayList<Integer>();
 		Random random = new Random();
 		int size = chromosome1.size();
 		int max = random.nextInt(size);
 		int min = random.nextInt(size);
 		while (max == min) {
 			min = random.nextInt(size);
 		}
 		if (max < min) {
 			int tmp = max;
 			max = min;
 			min = tmp;
 		}
 		finish[0] = new ArrayList<Integer>();
 		finish[1] = new ArrayList<Integer>();
 		for (int j=0; j<size; j++) {
 			if (j>=min && j<=max) {
 				// 欲交配染色體
 				chromosomeOne.add(chromosome1.get(j));
 				chromosomeTwo.add(chromosome2.get(j));
 			} else {
 				// 未交配染色體
 				finish[0].add(chromosome1.get(j));
 				finish[1].add(chromosome2.get(j));
 			}
 		}
 		finish[0] = geneReplace(finish[0], chromosomeTwo, chromosomeOne);
 		finish[1] = geneReplace(finish[1], chromosomeOne, chromosomeTwo);
 		finish[0].addAll(min, chromosomeTwo);
 		finish[1].addAll(min, chromosomeOne);
 		return finish;
 	}
 	
 	// 取代基因，將選擇將配基因與未交配基因做比對，把重複點替換掉
 	private ArrayList<Integer> geneReplace(ArrayList<Integer> chromosome, ArrayList<Integer> newPoint, ArrayList<Integer> oldPoint) {
 		for (int i=0; i<newPoint.size(); i++) {
 			if (chromosome.contains(newPoint.get(i))) {
 				int position = chromosome.indexOf(newPoint.get(i));
 				
 				for (int j=0; j<oldPoint.size(); j++) {
 	 	 			if (!newPoint.contains(oldPoint.get(j)) && !chromosome.contains(oldPoint.get(j))) {
 	 	 				chromosome.remove(position);
 	 	 				chromosome.add(position, oldPoint.get(j));
 	 	 				break;
 	 	 			}
 	 	 		}
 			}
 		}
 		return chromosome;
 	}
 	
}
