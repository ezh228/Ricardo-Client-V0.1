package ru.dseymo.neuron.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Network {
	
	public static Network parseFromStr(String str) {
		
		String strNeurons = str.split("neurons>")[1].split("connects>")[0];
		
		Network network = new Network();
		
		for(int i = 0; i < Integer.parseInt(str.split("layers>")[1].split("neurons>")[0]); i++)
			network.layers.add(new Layer());
		
		for(String strNeuron: strNeurons.split(";"))
			network.createN(Integer.parseInt(strNeuron.split(",")[1]), NeuronType.valueOf(strNeuron.split(":")[1].split(",")[0]), strNeuron.split(":")[0]);
		
		String connects = str.split("connects>")[1];
		for(String connect: connects.split(";")) {
			
			Neuron _neuron = network.getNeuron(connect.split("-")[0]);
			Neuron neuron = network.getNeuron(connect.split("-")[1].split("=")[0]);
			
			_neuron.connTo(neuron, Double.parseDouble(connect.split("=")[1]));
			
		}
		
		return network;
		
	}
	
	public static Network loadFromFile(File file) {
		
		if(!file.exists()) return null;
		
		Network network = new Network();
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(file), 8192);
			
			int typeData = 0;
			String line;
			while((line = reader.readLine()) != null) {
				
				if(line.startsWith("layers")) {
					
					typeData = 0;
					continue;
					
				} else if(line.startsWith("neurons")) {
					
					typeData = 1;
					continue;
					
				} else if(line.startsWith("connects")) {
					
					typeData = 2;
					continue;
					
				}
				
				switch (typeData) {
					case 0:
						for(int i = 0; i < Integer.parseInt(line); i++)
							network.layers.add(new Layer());
					break;
					
					case 1:
						network.createN(Integer.parseInt(line.split(",")[1]), NeuronType.valueOf(line.split(":")[1].split(",")[0]), line.split(":")[0]);
					break;
					
					case 2:
						Neuron _neuron = network.getNeuron(line.split("-")[0]);
						Neuron neuron = network.getNeuron(line.split("-")[1].split("=")[0]);
						
						_neuron.connTo(neuron, Double.parseDouble(line.split("=")[1]));
					break;
					
				}
				
			}
			
			reader.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return network;
		
	}
	

	public ArrayList<Layer> layers = new ArrayList<Layer>();
	public boolean logs = false;
	
	public Network(Layer... layers) {
		
		for(Layer layer: layers)
			this.layers.add(layer);
		
	}
	
	public Network(int...layers) {
		
		for(int i = 0; i < layers.length; i++)
			this.layers.add(new Layer());
		
		for(int i = 0; i < layers[0]; i++)
			createN(0, NeuronType.INPUT, "0i" + i);
		
		int i;
		for(i = 1; i < layers.length-1; i++)
			for(int i2 = 0; i2 < layers[i]; i2++) {
				
				Neuron neuron = createN(i, NeuronType.HIDDEN, i + "i" + i2);
				for(Neuron _neuron: this.layers.get(i-1).neurons)
					_neuron.connTo(neuron, Math.random()-0.5);
				
			}
		
		for(int i2 = 0; i2 < layers[layers.length-1]; i2++) {
			
			Neuron neuron = createN(i, NeuronType.OUTPUT, i + "o" + i2);
			for(Neuron _neuron: this.layers.get(i-1).neurons)
				_neuron.connTo(neuron, Math.random()-0.5);
			
		}
		
	}
	
	public Network() {}
	
	public ArrayList<Neuron> getAllNeurons() {
		
		ArrayList<Neuron> neurons = new ArrayList<Neuron>();
		for(Layer layer: layers)
			for(Neuron neuron: layer.neurons)
				neurons.add(neuron);
		
		return neurons;
		
	}
	
	public Neuron getNeuron(String name) {
		
		for(Layer layer: layers)
			for(Neuron neuron: layer.neurons)
				if(neuron.getName().equals(name))
					return neuron;
		
		return null;
		
	}
	
	public Neuron createN(int layerNum, NeuronType type, String name) {
		
		String oldName = name;
		name = name.replaceAll(",", "");
		name = name.replaceAll(":", "");
		name = name.replaceAll(";", "");
		name = name.replaceAll(">", "");
		if(!name.equals(oldName))
			System.out.println("Invalid character in neuron name");
		
		ArrayList<Neuron> neurons = getAllNeurons();
		for(Neuron neuron: neurons)
			if(neuron.getName().equals(name)) return null;
		
		Neuron neuron = new Neuron(name, type);
		layers.get(layerNum).neurons.add(neuron);
		
		return neuron;
		
	}
	
	public Thread train(LearnDataSet dataSet) {
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				if(logs) System.out.println("Train started");
				
				double lr = dataSet.learning_rate;
				int iters = dataSet.iterations;
				
				ArrayList<Neuron> neurons = getAllNeurons();
				ArrayList<Neuron> outputs = new ArrayList<Neuron>();
				ArrayList<Neuron> inputs = new ArrayList<Neuron>();
				ArrayList<Neuron> hiddens = new ArrayList<Neuron>();
				for(Neuron neuron: neurons)
					if(neuron.getType() == NeuronType.OUTPUT) outputs.add(neuron);
					else if(neuron.getType() == NeuronType.HIDDEN) hiddens.add(neuron);
					else if(neuron.getType() == NeuronType.INPUT) inputs.add(neuron);
				Collections.reverse(hiddens);
				
				for(int iter = 0; iter < iters; iter++) {
					double errorIter = 0;
					for(double[][] data: dataSet) {
						
						double[] input = data[0];
						double[] correctAnsw = data[1];
						for(int i = 0; i < input.length; i++)
							if(input[i] == 1) input[i] -= 0.01;
							else if(input[i] == 0) input[i] += 0.01;
						for(int i = 0; i < correctAnsw.length; i++)
							if(correctAnsw[i] == 1) correctAnsw[i] -= 0.01;
							else if(correctAnsw[i] == 0) correctAnsw[i] += 0.01;
						
						for(Neuron neuron: inputs)
							neuron.value = input[inputs.indexOf(neuron)];
						
						for(Neuron neuron: neurons)
							neuron.firing();
						
						for(Neuron output: outputs) {
							
							double error = findErrorOutput(output, correctAnsw[outputs.indexOf(output)]);
							errorIter += error;
							if(logs) System.out.println(((double)((int)(error*10000)))/10000);
							
						}
						for(Neuron hidden: hiddens)
							findError(hidden);
						
						for(Neuron neuron: neurons)
							changeWeight(neuron, lr);
						
						for(Neuron neuron: neurons)
							neuron.reset();
						
					}
					
					if(logs) System.out.println((((double)((int)(errorIter*1000)))/1000) + " - iter: " + (iter+1));
					
				}
				
				if(logs) System.out.println("Train ended");
				
			}
			
		});
		
		thread.start();
		
		return thread;
		
	}
	
	private void changeWeight(Neuron neuron, double learning_rate) {
		
		for(Neuron in: neuron.input.keySet()) {
			
			double w = neuron.input.get(in);
			w += learning_rate * neuron.error * neuron.getType().devived(neuron.getSignal()) * in.getSignal();
			neuron.input.replace(in, w);
			
		}
		
	}
	
	private double findErrorOutput(Neuron neuron, double answ) {
		
		neuron.error = answ - neuron.getSignal();
		return Math.pow(neuron.error, 2);
		
	}
	
	private double findError(Neuron neuron) {
		
		for(Neuron out: neuron.output)
			neuron.error += out.error * out.input.get(neuron);
		
		return Math.pow(neuron.error, 2);
		
	}
	
	public void saveToFile(File file) {
		
		try {
			
			if(file.exists()) {
				
				file.delete();
				file.createNewFile();
				
			}
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(file), 8192);
			
			writer.append("layers");
			writer.newLine();
			writer.append(layers.size() + "");
			writer.newLine();
			
			writer.append("neurons");
			writer.newLine();
			for(int i = 0; i < layers.size(); i++)
				for(Neuron neuron: layers.get(i).neurons) {
					
					writer.append(neuron.getName() + ":" + neuron.getType().name() + "," + i);
					writer.newLine();
					
				}
			
			writer.append("connects");
			writer.newLine();
			ArrayList<Neuron> neurons = getAllNeurons();
			for(Neuron neuron: neurons) {
				
				ArrayList<Neuron> output = neuron.output;
				for(int i = 0; i < output.size(); i++) {
					
					writer.append(neuron.getName() + "-" + output.get(i).getName() + "=" + ((double)((int)(output.get(i).input.get(neuron)*100000)))/100000);
					writer.newLine();
					
				}
			}
			
			writer.close();
			
		} catch (Exception e) {}
		
	}
	
	
	public double[] query(double[] input) {
		
		ArrayList<Neuron> neurons = getAllNeurons();
		ArrayList<Neuron> inputs = new ArrayList<Neuron>();
		ArrayList<Neuron> outputs = new ArrayList<Neuron>();
		for(Neuron neuron: neurons)
			if(neuron.getType() == NeuronType.INPUT) inputs.add(neuron);
			else if(neuron.getType() == NeuronType.OUTPUT) outputs.add(neuron);
		for(int i = 0; i < input.length; i++)
			if(input[i] == 1) input[i] -= 0.01;
			else if(input[i] == 0) input[i] += 0.01;
		
		for(Neuron neuron: inputs)
			neuron.value = input[inputs.indexOf(neuron)];
		
		for(Neuron neuron: neurons)
			neuron.firing();
		
		double[] output = new double[outputs.size()];
		for(Neuron neuron: outputs)
			output[outputs.indexOf(neuron)] = neuron.getSignal();
		
		for(Neuron neuron: neurons)
			neuron.reset();
		
		return output;
		
	}
	
	@Override
	public String toString() {
		
		String strNeurons = "";
		for(int i = 0; i < layers.size(); i++)
			for(Neuron neuron: layers.get(i).neurons)
				strNeurons += neuron.getName() + ":" + neuron.getType().name() + "," + i + ";";
		
		String connects = "";
		ArrayList<Neuron> neurons = getAllNeurons();
		for(Neuron neuron: neurons) {
			
			ArrayList<Neuron> output = neuron.output;
			for(int i = 0; i < output.size(); i++)
				connects += neuron.getName() + "-" + output.get(i).getName() + "=" + ((double)((int)(output.get(i).input.get(neuron)*100000)))/100000 + ";";
					
		}
		
		return "network@" +
						"layers>" + layers.size() +
						"neurons>" + strNeurons +
						"connects>" + connects;
		
	}
	
}
