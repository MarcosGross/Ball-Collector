package com.example.camera_activity;

import org.opencv.core.Scalar;

public class Ball {
	private int xPos;
	private int yPos;
	private Colours type;
	private Scalar hsvMin;
	private Scalar hsvMax;
	private Scalar colour;
	static public enum Colours {
		NONE(""), RED1("Red1"), GREEN("Green"), BLUE("Blue"), RED2("Red2");
		
		private final String name;
		
		Colours(String colourName){
			name = colourName;
		}
		
		public String toString(){
			return name;
		}
	}; 
	
	public Ball(){
		xPos = 0;
		yPos = 0;
		type = Colours.NONE;
		int hMin = 0;
		int sMin = 0;
		int vMin = 0;
		int hMax = 0;
		int sMax = 0;
		int vMax = 0;
		setHsvMin(new Scalar(hMin, sMin, vMin));
		setHsvMax(new Scalar(hMax, sMax, vMax));
		setColour(new Scalar(0, 0, 255));
	}

	public Ball(Colours name){
		setType(name);
		int hMin = 0;
		int sMin = 0;
		int vMin = 0;
		int hMax = 0;
		int sMax = 0;
		int vMax = 0;

		switch (name) {
			case RED1:
				hMin = 165;
				sMin = 0;
				vMin = 0;
				hMax = 179;
				sMax = 256;
				vMax = 256;
				setColour(new Scalar(0, 0, 255));
				break;
			case RED2:
				hMin = 0;
				sMin = 0;
				vMin = 0;
				hMax = 10;
				sMax = 256;
				vMax = 256;
				setColour(new Scalar(0, 0, 255));
				break;
				
			case GREEN:
				hMin = 58;
				sMin = 234;
				vMin = 0;
				hMax = 72;
				sMax = 256;
				vMax = 256;
				setColour(new Scalar(0, 255, 0));
				break;
				
			case BLUE:
				hMin = 115;
				sMin = 150;
				vMin = 0;
				hMax = 125;
				sMax = 256;
				vMax = 256;
				setColour(new Scalar(255, 0, 0));
				break;
	
			default:
				xPos = 0;
				yPos = 0;
				hsvMax = new Scalar(0, 0, 0);
				hsvMin = new Scalar(0, 0, 0);
				colour = new Scalar(0, 0, 0);
				break;
		}

		setHsvMin(new Scalar(hMin, sMin, vMin));
		setHsvMax(new Scalar(hMax, sMax, vMax));
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	public Colours getType() {
		return type;
	}

	public void setType(Colours type) {
		this.type = type;
	}

	public Scalar getHsvMin() {
		return hsvMin;
	}

	public void setHsvMin(Scalar hsvMin) {
		this.hsvMin = hsvMin;
	}

	public Scalar getHsvMax() {
		return hsvMax;
	}

	public void setHsvMax(Scalar hsvMax) {
		this.hsvMax = hsvMax;
	}

	public Scalar getColour() {
		return colour;
	}

	public void setColour(Scalar colour) {
		this.colour = colour;
	}
	
}
