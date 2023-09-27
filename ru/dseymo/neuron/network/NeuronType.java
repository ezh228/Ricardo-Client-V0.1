package ru.dseymo.neuron.network;

public enum NeuronType {

	INPUT,
	HIDDEN,
	OUTPUT,
	BIAS;
	//MEMORY();
	
	public double activate(double x) {
		
		double value = x;
//		if(x > 1) value = 1 + 0.01 * (x - 1);
//		else if(x < 0) value = 0.01 * x;
		
		switch(this) {
			case INPUT: value = x; break;
			case BIAS: value = 1; break;
	
			default: value = 1 / (1 + Math.pow(2.718, -x)); break;
			
		}
		
		return value;
		
	}
	
	public double devived(double x) {
		
//		double value = 1;
//		if(x < 0 || x > 1) value = 0.01;
//		
//		return value;
		return x * (1 - x);
		
	}
	
}
