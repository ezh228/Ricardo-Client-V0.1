package ru.dseymo.neuron.network;

import java.util.ArrayList;
import java.util.HashMap;

public class Neuron {
	
	private String name;
	private NeuronType type;
	public double value = 0;
	private double signal = 0;
	public double error = 0;
	public HashMap<Neuron, Double> input = new HashMap<Neuron, Double>();
	public ArrayList<Neuron> output = new ArrayList<Neuron>();
	
	public Neuron(String name, NeuronType type) {
		
		this.name = name;
		this.type = type;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public NeuronType getType() {
		
		return type;
		
	}
	
	public double getSignal() {
		
		return signal;
		
	}
	
	public Neuron connTo(Neuron neuron, double w) {
		
		if(output.contains(neuron)) return this;
		
		output.add(neuron);
		neuron.input.put(this, w);
		
		return this;
		
	}
	
	public Neuron disconnFrom(Neuron neuron) {
		
		if(!output.contains(neuron)) return this;
		
		output.remove(neuron);
		neuron.input.remove(this);
		
		return this;
		
	}
	
	public void firing() {
		
		signal = type.activate(value);
		
		for(Neuron neuron: output)
			neuron.value += signal*neuron.input.get(this);
		
	}
	
	public void reset() {
		
		value = 0;
		signal = 0;
		error = 0;
		
	}
	
}
