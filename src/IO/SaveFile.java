package IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Values.BestChromosome;

public class SaveFile {

	private BufferedWriter bw;
	private static String WINDOWS = ".\\";
	private static String MAC = ".//";
	private static String FILENAME = "Muti-Mechanism Genetic Algorithm - TSP test data";
	private static String FILETYPE = ".txt";
	
	public void saveFile(String dataSetName,BestChromosome bestChromosome, boolean isMac) {
		String savePath = "";
		if (isMac)
			savePath = SaveFile.MAC;
		else
			savePath = SaveFile.WINDOWS;
		savePath += SaveFile.FILENAME + SaveFile.FILETYPE;
		
		try {
			this.bw = new BufferedWriter(new FileWriter(savePath, true));
			
			this.bw.write("DataSet:" + ";" + dataSetName);
			this.bw.newLine();
			
			this.bw.write("Running Time:" + ";" + bestChromosome.time);
			this.bw.newLine();
			
			this.bw.write("Best Distance:" + ";" + bestChromosome.distance);
			this.bw.newLine();
			
			this.bw.write("Best Fitness:" + ";" + bestChromosome.fitness);
			this.bw.newLine();
			
			this.bw.write("Avg Time:" + ";" + bestChromosome.avgTime);
			this.bw.newLine();
			
			this.bw.write("Avg Distance:" + ";" + bestChromosome.avgDistance);
			this.bw.newLine();
			
			this.bw.write("Best Path:" + ";" + getArrayListToString(bestChromosome.chromosome));
			this.bw.newLine();
			this.bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveFile(double soultion, double time, boolean isMac) {
		String savePath = "";
		if (isMac)
			savePath = SaveFile.MAC;
		else
			savePath = SaveFile.WINDOWS;
		savePath += SaveFile.FILENAME + SaveFile.FILETYPE;
		
		try {
			this.bw = new BufferedWriter(new FileWriter(savePath, true));
			
			this.bw.write("Running Time:" + ";" + time);
			this.bw.newLine();
			
			this.bw.write("Best Distance:" + ";" + soultion);
			this.bw.newLine();
			
			this.bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getArrayListToString(ArrayList<Integer> arrayList) {
		String output = "";
		for (int i=0; i<arrayList.size(); i++) {
			output += arrayList.get(i);
			if (i != arrayList.size()-1)
				output += ",";
		}
		return output;
	}
}
