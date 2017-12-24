package Program;

import java.util.ArrayList;
import java.util.Random;

public class Selection {

	// 選擇機制
	public ArrayList<Integer>[] selection(ArrayList<Integer>[] chromosome, double[] fitness) {
		int listSize = chromosome.length;
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] selectPool = new ArrayList[listSize];
		Random random = new Random();
		for (int i=0; i<listSize; i++) {
			int randomNumber1 = random.nextInt(listSize);
			int randomNumber2 = random.nextInt(listSize);
			while (randomNumber1 == randomNumber2) {
				randomNumber2 = random.nextInt(listSize);
			}
			selectPool[i] = selectChromosome(chromosome[randomNumber1], fitness[randomNumber1], 
											chromosome[randomNumber2], fitness[randomNumber2]);
		}
		return selectPool;
	}
	

 	// 選擇染色體，比較兩個染色體之適應值，選擇適應值較佳之染色體
 	private ArrayList<Integer> selectChromosome(ArrayList<Integer> chromosome1, double fitness1, 
 												ArrayList<Integer> chromosome2, double fitness2 ) {
 		if (fitness1 > fitness2) {
 			return chromosome1;
 		}
 		else {
 			return chromosome2;
		}
 	}
 	
}
