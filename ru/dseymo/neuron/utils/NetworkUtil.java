package ru.dseymo.neuron.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class NetworkUtil {
	
	public static double[] imageToData(BufferedImage image, int widthStep, int heightStep) {
		
		int numInput = (int)((image.getWidth() / widthStep * 2 - 1) * (image.getHeight() / heightStep * 2 - 1));
		
		double[] data = new double[numInput*3];
		int x = -1,
			y = 0;
		
		int widthStep_2 = widthStep/2,
			heightStep_2 = widthStep/2;
		for(int i = 0; i < data.length; i++) {
			
			x++;
			
			if(x*widthStep_2 + widthStep > image.getWidth()) {
				
				x = 0;
				y++;
				
			}
			
			int xFrom = x*widthStep_2,
				yFrom = y*heightStep_2,
				xTo = x*widthStep_2 + widthStep,
				yTo = y*heightStep_2 + heightStep;
			
			double result = 0;
			for(int _x = xFrom; _x < xTo; _x++)
				for(int _y = yFrom; _y < yTo; _y++) {
					
					Color color = new Color(image.getRGB(_x, _y));
					result += ((double)color.getBlue()) / 255.0;
					
				}
			
			data[i] = result / ((xTo-xFrom) * (yTo-yFrom));
			i++;
			
			result = 0;
			for(int _x = xFrom; _x < xTo; _x++)
				for(int _y = yFrom; _y < yTo; _y++) {
					
					Color color = new Color(image.getRGB(_x, _y));
					result += ((double)color.getRed()) / 255.0;
					
				}
			
			data[i] = result / ((xTo-xFrom) * (yTo-yFrom));
			i++;
			
			result = 0;
			for(int _x = xFrom; _x < xTo; _x++)
				for(int _y = yFrom; _y < yTo; _y++) {
					
					Color color = new Color(image.getRGB(_x, _y));
					result += ((double)color.getGreen()) / 255.0;
					
				}
			
			data[i] = result / ((xTo-xFrom) * (yTo-yFrom));
			
		}
		
		return data;
		
	}
	
	
//	public static double[] imageToData(BufferedImage image, int widthStep, int heightStep) {
//		
//		int numInput = (image.getWidth()/widthStep) * (image.getHeight()/heightStep);
//		
//		double[] data = new double[numInput*3];
//		int x = -1,
//			y = 0;
//		
//		for(int i = 0; i < data.length; i++) {
//			
//			x++;
//			
//			if(x*widthStep + 1 > image.getWidth()) {
//				
//				x = 0;
//				y++;
//				
//			}
//			
//			int xFrom = x*widthStep,
//				yFrom = y*heightStep,
//				xTo = x*widthStep + widthStep,
//				yTo = y*heightStep + heightStep;
//			double result = 0;
//			for(int _x = xFrom; _x < xTo; _x++)
//				for(int _y = yFrom; _y < yTo; _y++) {
//					
//					Color color = new Color(image.getRGB(_x, _y));
//					result += ((double)color.getBlue()) / 255.0;
//					
//				}
//			
//			data[i] = result / ((xTo-xFrom) * (yTo-yFrom));
//			i++;
//			
//			result = 0;
//			for(int _x = xFrom; _x < xTo; _x++)
//				for(int _y = yFrom; _y < yTo; _y++) {
//					
//					Color color = new Color(image.getRGB(_x, _y));
//					result += ((double)color.getRed()) / 255.0;
//					
//				}
//			
//			data[i] = result / ((xTo-xFrom) * (yTo-yFrom));
//			i++;
//			
//			result = 0;
//			for(int _x = xFrom; _x < xTo; _x++)
//				for(int _y = yFrom; _y < yTo; _y++) {
//					
//					Color color = new Color(image.getRGB(_x, _y));
//					result += ((double)color.getGreen()) / 255.0;
//					
//				}
//			
//			data[i] = result / ((xTo-xFrom) * (yTo-yFrom));
//			
//		}
//		
//		return data;
//		
//	}
	
}
