package Program;

import java.util.ArrayList;
import java.util.Random;

import Values.Data;
import Values.Relation;

public class MutiMechanism {

	private Data data = Data.getInstance();
	private Optimization optimization = new Optimization();
	
	// 多重機制
	@SuppressWarnings("unchecked")
	public ArrayList<Integer>[] mutiMechanism(ArrayList<Integer>[] chromosome, double[] fitness) {
		Random random = new Random();
		int listSize = chromosome.length/2;
		int i=0;
		int chromosomeAmount = 7;
		double rate = 0;
		double candidateRate = 0.8;
		double swapRate = 0.1;
		double optRate = 0.1;
		ArrayList<Integer>[] explorer = new ArrayList[listSize];
		ArrayList<Integer>[] selectChromosome = null;
		double[] selectFitness = null;
		while (i<listSize) {
			rate = random.nextDouble();
			int max = listSize;
			if (rate >=0 && rate < candidateRate) {
				chromosomeAmount = (random.nextInt(3)+1) * 3;
			}
			if (rate >=candidateRate && rate < candidateRate + swapRate) {
				chromosomeAmount = 5;
			}
			if (rate >=1-optRate && rate < 1) {
				// M:5
				// L:2
				chromosomeAmount = 2;
			}
			
			if (i+chromosomeAmount<listSize) 
				max = i+chromosomeAmount;
			else 
				max = listSize;
			selectChromosome = new ArrayList[max-i];
			selectFitness = new double[max-i];
			for (int j=i; j<max; j++) {
				selectChromosome[j-i] = new ArrayList<Integer>();
				selectChromosome[j-i] = chromosome[j];
				selectFitness[j-i] = fitness[j];
			}
			
			if (rate >=0 && rate < candidateRate) {
				// 候選人機制
				selectChromosome = candidate(selectChromosome, selectFitness, i, max);
			}
			if (rate >= candidateRate && rate < candidateRate + swapRate) {
				// 單點交換
				selectChromosome = chromosomePointSwap(selectChromosome);
			}
			if (rate >=1-optRate && rate < 1) {
				// 二元優化法
				for (int j=0; j<selectChromosome.length; j++) {
					selectChromosome[j] = optimization.twoOptimization(selectChromosome[j]);
				}
			}
			
			for (int j=i; j<max; j++) {
				explorer[j] = selectChromosome[j-i];
			}
			i += chromosomeAmount;
		}
		this.data = null;
		this.optimization = null;
		return explorer;
	}

	// 染色體單點交換
	private ArrayList<Integer>[] chromosomePointSwap(ArrayList<Integer>[] chromosome) {
		for (int i=0; i<chromosome.length; i++) {
			for (int j=0; j<5; j++) {
				chromosome[i] = pointSwap(chromosome[i]);
			}
		}
		return chromosome;
	}
	
	// 候選人機制
	private ArrayList<Integer>[] candidate(ArrayList<Integer>[] chromosome, double[] fitness, int min, int max) {
		Relation[] relation = new Relation[data.total];
		// 找出所有點的關係
		relation = getPointRelation(relation, chromosome, fitness);
		// 產生探險者
		return createExplorer(relation, chromosome);
	}
	
	// 候選人機制-找尋點之間關係
	private Relation[] getPointRelation(Relation[] relation, ArrayList<Integer>[] chromosome, double[] fitness) {
		for (int i=0; i<chromosome.length; i++) {
			int geneSize = chromosome[i].size();
			for (int j=0; j<geneSize; j++) {
				int nowPoint = chromosome[i].get(j);
				if (relation[nowPoint] == null) { // MainPoint為空
					relation[nowPoint] = new Relation();
					relation[nowPoint].setMainPoint(chromosome[i].get(j)); // 點j輸入
				}
				
				if (j==geneSize-1) { // 最後一個點，與第一個點的關係
					int nextPoint = chromosome[i].get(0);
					if (relation[nowPoint].isPointContained(nextPoint)) { // 已包含染色體內第0個點
						if (relation[nowPoint].getPointFitness(nextPoint) < fitness[i]) // 比較適應值，若現有適應值小於新的適應值則取代
							relation[nowPoint].replacePointFitness(nextPoint, fitness[i]); // 取代染色體內第0個點之適應值
					} else { // 不包含第0個點
						relation[nowPoint].setPoint(nextPoint); // 加入點0
						relation[nowPoint].setFitness(fitness[i]); // 加入點0適應值
					}
				} else {
					int nextPoint = chromosome[i].get(j+1);
					if (relation[nowPoint].isPointContained(nextPoint)) { // 已包含染色體內第j+1個點
						if (relation[nowPoint].getPointFitness(nextPoint) < fitness[i]) // 比較適應值，若現有適應值小於新的適應值則取代
							relation[nowPoint].replacePointFitness(nextPoint, fitness[i]); // 取代染色體內第j+1個點之適應值
					} else { // 不包含第j+1個點
						relation[nowPoint].setPoint(nextPoint); // 加入點j
						relation[nowPoint].setFitness(fitness[i]); // 加入點j適應值
					}
				}
			}
		}
		return relation;
	}
	
	// 候選人機制-產生探險者
	private ArrayList<Integer>[] createExplorer(Relation[] relations, ArrayList<Integer>[] chromosome) {
		int size = chromosome.length;
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] explorer = new ArrayList[size];
		for (int i=0;i<size; i++) {
			for (int j=0;j<chromosome[i].size(); j++) {
				if (j == 0) {
					explorer[i] = new ArrayList<Integer>();
					explorer[i].add(chromosome[i].get(j));
				} else {
					explorer[i].add(getExplorerPoint(relations, explorer[i].get(j-1), explorer[i]));
				}
			}
		}
		return explorer;
	}
	
	// 候選人機制-取得探險者基因
	private int getExplorerPoint(Relation[] relation, int lastPoint, ArrayList<Integer> alreadyPoint) {
		boolean run = true;
		ArrayList<Integer> candidatePoint = relation[lastPoint].getAllPoint();
		ArrayList<Double> candidateFitness =relation[lastPoint].getAllFitness();
		
		while (run) {
			run = false;
			for (int i=1; i<candidatePoint.size(); i++) {
				if (candidateFitness.get(i-1) < candidateFitness.get(i)) {
					int oldPoint = candidatePoint.get(i-1);
					int nowPoint = candidatePoint.get(i);
					double oldFitness = candidateFitness.get(i-1);
					double nowFitness = candidateFitness.get(i);
					candidatePoint.remove(i-1);
					candidatePoint.add(i-1, nowPoint);
					candidateFitness.remove(i-1);
					candidateFitness.add(i-1, nowFitness);
					
					candidatePoint.remove(i);
					candidatePoint.add(i, oldPoint);
					candidateFitness.remove(i);
					candidateFitness.add(i, oldFitness);
					run = true;
				}
			}
		}
		
		int returnPoint = -1;
		for (int i=0; i<candidatePoint.size(); i++) {
			if (!alreadyPoint.contains(candidatePoint.get(i))) {
				returnPoint = candidatePoint.get(i);
				break;
			}
		}
		
		return returnPoint;
	}

 	// 隨機選擇兩點進行交換
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
