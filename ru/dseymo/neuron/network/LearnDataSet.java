package ru.dseymo.neuron.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class LearnDataSet implements Iterable<double[][]> {
	
	public static LearnDataSet parseFromStr(String str) {
		
		String strData = str.split("data>")[1];
		
		HashMap<double[], double[]> data = new HashMap<double[], double[]>();
		for(String _str: strData.split(";")) {
			
			String strInput = _str.split(":")[0].substring(1, _str.split(":")[0].length()-1),
				   strOutput = _str.split(":")[1].substring(1, _str.split(":")[1].length()-1);
			
			double[] input = new double[strInput.split(", ").length],
					 output = new double[strOutput.split(", ").length];
			for(int i = 0; i < input.length; i++)
				input[i] = Double.parseDouble(strInput.split(", ")[i]);
			for(int i = 0; i < output.length; i++)
				output[i] = Double.parseDouble(strOutput.split(", ")[i]);
			
			data.put(input, output);
			
		}
				
		
		LearnDataSet lds = new LearnDataSet(data);
		lds.iterations = Integer.parseInt(str.split("iterations>")[1].split("learning_rate>")[0]);
		lds.learning_rate = Double.parseDouble(str.split("learning_rate>")[1].split("data>")[0]);
		
		return lds;
		
	}
	
	public static LearnDataSet loadFromFile(File file) {
		
		if(!file.exists()) return null;
		
		LearnDataSet lds = new LearnDataSet();
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(file), 8192);
			
			int typeData = 0;
			String line;
			while((line = reader.readLine()) != null) {
				
				if(line.startsWith("iterations")) {
					
					typeData = 0;
					continue;
					
				} else if(line.startsWith("learning_rate")) {
					
					typeData = 1;
					continue;
					
				} else if(line.startsWith("data")) {
					
					typeData = 2;
					continue;
					
				}
				
				switch (typeData) {
					case 0:
						lds.iterations = Integer.parseInt(line);
					break;
					
					case 1:
						lds.learning_rate = Double.parseDouble(line);
					break;
					
					case 2:
						String strInput = line.split(":")[0].substring(1, line.split(":")[0].length()-1),
							   strOutput = line.split(":")[1].substring(1, line.split(":")[1].length()-1);
						
						double[] input = new double[strInput.split(", ").length],
								 output = new double[strOutput.split(", ").length];
						for(int i = 0; i < input.length; i++)
							input[i] = Double.parseDouble(strInput.split(", ")[i]);
						for(int i = 0; i < output.length; i++)
							output[i] = Double.parseDouble(strOutput.split(", ")[i]);
						
						lds.addData(input, output);
					break;
					
				}
				
			}
			
			reader.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return lds;
		
	}
	

	private HashMap<double[], double[]> data = new HashMap<double[], double[]>();
	public int iterations = 1;
	public double learning_rate = 0.5;
	
	public LearnDataSet(HashMap<double[], double[]> data) {
		
		this.data = data;
		
	}
	
	public LearnDataSet() {}
	
	public LearnDataSet addData(double[] input, double[] output) {
		
		data.put(input, output);
		
		return this;
		
	}
	
	public HashMap<double[], double[]> getData() {
		
		return data;
		
	}
	
	public void saveToFile(File file) {
		
		try {
			
			if(file.exists()) {
				
				file.delete();
				file.createNewFile();
				
			}
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(file), 8192);
			
			writer.append("iterations");
			writer.newLine();
			writer.append(iterations + "");
			writer.newLine();
			
			writer.append("learning_rate");
			writer.newLine();
			writer.append(learning_rate + "");
			writer.newLine();
			
			writer.append("data");
			writer.newLine();
			for(double[] in: this.data.keySet()) {
				
				writer.append(Arrays.toString(in) + ":" + Arrays.toString(this.data.get(in)));
				writer.newLine();
				
			}
			
			writer.close();
			
		} catch (Exception e) {}
		
	}
	
	@Override
	public String toString() {
		
		String data = "";
		for(double[] in: this.data.keySet())
			data += Arrays.toString(in) + ":" + Arrays.toString(this.data.get(in)) + ";";
		
		return "learnDataSet@" + 
							"iterations>" + iterations +
							"learning_rate>" + learning_rate +
							"data>" + data;
		
	}
	
	@Override
	public Iterator<double[][]> iterator() {
		
		return new Iterator<double[][]>() {
			
			int i = 0;

			@Override
			public boolean hasNext() {
				
				return i < data.size();
				
			}

			@Override
			public double[][] next() {
				
				double[] input = (double[])data.keySet().toArray()[i],
						 output = (double[])data.values().toArray()[i];
				
				i++;
				
				return new double[][] {input, output};
				
			}
			
		};
		
	}
	
}
