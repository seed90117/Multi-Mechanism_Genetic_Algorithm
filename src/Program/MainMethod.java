package Program;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import Values.BestChromosome;
import Values.Data;
import Values.Parameter;

public class MainMethod {
	
	private static double INITIALSELECT = 0.85;
	
	private Data data = Data.getInstance();
	private Parameter parameter = Parameter.getInstance();
	private MutiMechanism mutiMechanism;
	private Selection selection;
	private Crossover crossover;
	private Mutation mutation;
	
	@SuppressWarnings("unchecked")
	private ArrayList<Integer>[] chromosome = new ArrayList[parameter.getChromosome()];
	private double[] distance = new double[parameter.getChromosome()];
	private double[] fitness = new double[parameter.getChromosome()];
	private boolean isInteger = false;
	
	// 演算法主程式
	public BestChromosome mainProgram(boolean isInteger) {
		this.isInteger = isInteger;
		// 初始化染色體
		double generationDistance = this.initialChromosome();
		for (int i=0; i<parameter.getGeneration(); i++) {
			ArrayList<Integer>[] pool;
			// 依照適應值排序
			this.sortChromosome();
			// 多重機制，產生探險者染色體，計算探險者適應值
			mutiMechanism = new MutiMechanism();
			this.addExplorer(this.mutiMechanism.mutiMechanism(this.chromosome, this.fitness));
//			parameter.setMemoryUsage();
			// 選擇機制，選擇較佳的染色體放入池中
			selection = new Selection();
			pool = this.selection.selection(this.chromosome, this.fitness);
//			parameter.setMemoryUsage();
			// 交配機制，交配染色體
			crossover = new Crossover();
			pool = this.crossover.crossover(pool);
//			parameter.setMemoryUsage();
			// 突變機制
			mutation = new Mutation();
			pool = this.mutation.mutation(pool);
//			parameter.setMemoryUsage();
			// 新的染色體放回母池，並計算適應值
			this.chromosome = pool;
			double tmpDistance = this.caculateDistance();
			changeOptRate(tmpDistance, generationDistance);
			generationDistance = tmpDistance;
//			parameter.setMemoryUsage();
		}
		return getBest();
	}
	
	// 初始化染色體
	private double initialChromosome() {
		Random random = new Random();
		ArrayList<Integer> point = getDataSetList();
		for (int i=0; i<parameter.getChromosome(); i++){
			this.chromosome[i] = new ArrayList<Integer>();
			if (random.nextDouble() < INITIALSELECT)
				this.chromosome[i].addAll(createChromosome(point));
			else
				this.chromosome[i].addAll(greedyChromosome());
		}
		point.clear();
		return caculateDistance();
	}
	
	private ArrayList<Integer> getDataSetList() {
		ArrayList<Integer> point = new ArrayList<>();
		for (int i=0; i<data.total; i++) {
			point.add(i);
		}
		return point;
	}
	
	private ArrayList<Integer> greedyChromosome() {
		Random random = new Random();
		ArrayList<Integer> newChromosome = new ArrayList<Integer>();
		for (int i=0; i<this.data.total; i++) {
			if (i==0)
				newChromosome.add(random.nextInt(this.data.total));
			else {
				int pointA = newChromosome.get(i-1);
				int shortPoint = -1;
				double shortDistance = -1;
				for (int j=0; j<this.data.total; j++) {
					if (!newChromosome.contains(j)) {
						double tmpDistance = Point.distance(this.data.x[pointA], this.data.y[pointA],
								this.data.x[j], this.data.y[j]);
						if (shortPoint == -1 || shortDistance > tmpDistance) {
							shortPoint = j;
							shortDistance = tmpDistance;
						}
					}
				}
				newChromosome.add(shortPoint);
			}
		}
		return newChromosome;
	}
	
	// 產生染色體
	private ArrayList<Integer> createChromosome(ArrayList<Integer> point) {
		Random random = new Random();
		boolean run = true;
		ArrayList<Integer> newChromosome = new ArrayList<Integer>();
		ArrayList<Integer> allPoint = new ArrayList<Integer>();
		allPoint.addAll(point);
		while (run) {
			int tmp = random.nextInt(allPoint.size());
			newChromosome.add(allPoint.get(tmp));
			allPoint.remove(tmp);
			if (allPoint.size() <= 0)
				run = false;
		}
		return newChromosome;
	}
	
	// 計算所有染色體總距離
	private double caculateDistance() {
		double totalDistance = 0;
		for (int i=0; i<this.parameter.getChromosome(); i++){
			double tmpDistance = 0;
			int size = this.chromosome[i].size();
			for (int j=0; j<size; j++){
				int pointA = this.chromosome[i].get(j);
				int pointB = 0;
				if (j == size-1) {
					pointB = this.chromosome[i].get(0);
				}
				if (j < size-1) {
					pointB = this.chromosome[i].get(j+1);
				}
				if (this.isInteger)
					tmpDistance += (double)Math.round(Point.distance(
							this.data.x[pointA], this.data.y[pointA], 
							this.data.x[pointB], this.data.y[pointB]));
				else
					tmpDistance += Point.distance(
							this.data.x[pointA], this.data.y[pointA], 
							this.data.x[pointB], this.data.y[pointB]);
			}
			totalDistance += tmpDistance;
			this.distance[i] = tmpDistance;
			this.fitness[i] = 1/tmpDistance;
		}
		return totalDistance/this.parameter.getChromosome();
	}
	
	// 計算探險者染色體總距離
	private void caculateExplorerDistance() {
		for (int i=this.parameter.getChromosome()/2; i<this.parameter.getChromosome(); i++){
			double tmpDistance = 0;
			int size = this.chromosome[i].size();
			for (int j=0; j<size; j++){
				int pointA = this.chromosome[i].get(j);
				int pointB = 0;
				if (j == size-1) {
					pointB = this.chromosome[i].get(0);
				}
				
				if (j > 0 && j < size-1) {
					pointB = this.chromosome[i].get(j+1);
				}
				if (this.isInteger)
					tmpDistance += (double)Math.round(Point.distance(
							this.data.x[pointA], this.data.y[pointA], 
							this.data.x[pointB], this.data.y[pointB]));
				else
					tmpDistance += Point.distance(
							this.data.x[pointA], this.data.y[pointA], 
							this.data.x[pointB], this.data.y[pointB]);
			}
			this.distance[i] = tmpDistance;
			this.fitness[i] = 1/tmpDistance;
		}
	}

	// 依照適應值排序染色體
	private void sortChromosome() {
		boolean run = true;
		while (run) {
			run = false;
			for (int i=0; i<this.parameter.getChromosome(); i++){
				if (i>0 && this.distance[i] < this.distance[i-1]){
					ArrayList<Integer> tmpChromosome = new ArrayList<Integer>();
					double tmpDistance, tmpFitness;
					tmpDistance = this.distance[i];
					this.distance[i] = this.distance[i-1];
					this.distance[i-1] = tmpDistance;
					
					tmpFitness = this.fitness[i];
					this.fitness[i] = this.fitness[i-1];
					this.fitness[i-1] = tmpFitness;
					
					tmpChromosome = this.chromosome[i];
					this.chromosome[i] = this.chromosome[i-1];
					this.chromosome[i-1] = tmpChromosome;
					
					run = true;
				}
			}
		}
	}
	
	// 加入探險者染色體
	private void addExplorer(ArrayList<Integer>[] explorer) {
		for (int i=this.parameter.getChromosome()/2; i<this.parameter.getChromosome(); i++){
			for (int j=0; j<this.chromosome[i].size(); j++){
				this.chromosome[i] = explorer[i-this.parameter.getChromosome()/2];
			}
		}
		this.caculateExplorerDistance();
	}
	
	private void changeOptRate(double newDistance, double oldDistance) {
		double optRate = parameter.getOptRate();
		double optReduce = parameter.getOptReduce();
//		double optAdd = parameter.getOptAdd();
		if (newDistance < oldDistance) {
			// 降低OPT rate
			if (parameter.getMinOptRate() <= optRate*optReduce) {
				parameter.setOptRate(optRate*optReduce);
			} else {
				parameter.setOptRate(parameter.getMinOptRate());
			}
		}	
//		} else {
//			if (optRate*optAdd >= 1)
//	 			parameter.setOptRate(1);
//			else {
//				parameter.setOptRate(optRate*optAdd);
//			}
//		}
 	}
	
	// 找出最佳解
	private BestChromosome getBest() {
		BestChromosome bestChromosome = new BestChromosome();
		int size = this.parameter.getChromosome();
		int best = 0;
		for (int i=0; i<size; i++) {
			if (this.fitness[best] < this.fitness[i])
				best = i;
		}
		bestChromosome.chromosome = this.chromosome[best];
		bestChromosome.distance = this.distance[best];
		bestChromosome.fitness = this.fitness[best];
		this.clearAll();
		return bestChromosome;
	}
	
	private void clearAll() {
		this.data = null;
		this.parameter = null;
		this.mutiMechanism = null;
		this.selection = null;
		this.crossover = null;
		this.mutation = null;
		this.chromosome = null;
		this.distance = null;
		this.fitness = null;
	}
}
