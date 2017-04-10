package org.usfirst.frc.team3310.utility;

import com.ctre.CANTalon;

public class CANTalonEncoder extends CANTalon
{
	private double encoderTicksToWorld;
	private boolean isRight = true;
	
	public CANTalonEncoder(int deviceNumber, double encoderTicksToWorld, FeedbackDevice feedbackDevice) {
		this(deviceNumber, encoderTicksToWorld, false, feedbackDevice);
	}

	public CANTalonEncoder(int deviceNumber, double encoderTicksToWorld, boolean isRight, FeedbackDevice feedbackDevice) {
		super(deviceNumber);
		this.setFeedbackDevice(feedbackDevice);
		this.encoderTicksToWorld = encoderTicksToWorld;
		this.isRight = isRight;
	}

    public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public double convertEncoderTicksToWorld(double encoderTicks) {
    	return encoderTicks / encoderTicksToWorld;
    }

    public double convertEncoderWorldToTicks(double worldValue) {
    	return worldValue * encoderTicksToWorld;
    }
    
    public void setWorld(double worldValue) {
    	this.set(convertEncoderWorldToTicks(worldValue));
    }
    
    public void setPositionWorld(double worldValue) {
    	this.setPosition(convertEncoderWorldToTicks(worldValue));
    }
    
    public double getPositionWorld() {
    	return convertEncoderTicksToWorld(this.getPosition());
    }
    
    public void setVelocityWorld(double worldValue) {
    	this.set(convertEncoderWorldToTicks(worldValue) * 0.1);
    }
    
    public double getVelocityWorld() {
    	return convertEncoderTicksToWorld(this.getSpeed() / 0.1);
    }
}