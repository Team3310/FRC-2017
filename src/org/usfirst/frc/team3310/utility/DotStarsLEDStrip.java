package org.usfirst.frc.team3310.utility;

import java.util.Arrays;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

/**
 * @author gerthcm modified by bselle
 *	Datasheet - https://www.adafruit.com/datasheets/APA102.pdf
 */
public class DotStarsLEDStrip {
	
	// Constant values for fixed things in the serial data stream
    private static final byte[] startFrame = {(byte)0, (byte)0, (byte)0, (byte)0};
    private static final byte[] endFrame = {(byte)1, (byte)1, (byte)1, (byte)1}; 
	
	// Offsets within the stream
    private static final int globalOffset = 0;
    private static final int blueOffset = 1;
    private static final int greenOffset = 2;
    private static final int redOffset = 3;	
    private static final int bytesPerLED = 4;
    private static final int led0Offset = startFrame.length;
    private int endFrameOffset; //dependent on number of LED's
	
	// SPI coms object from FRC
    private SPI spi;
    private static final int SPI_CLK_RATE = 512000; //Total guess at max clock rate - 512KHz is called out in an example in the datasheet, so I used that
    private static final int CHUNK_SIZE = 127; // we can only TX 127 bytes at a time
	
	// Color Buffer - all bytes to send out from the 
    private static final int ledMaxVal = 255;
    private byte[] ledBuffer;
    private int num_leds;
	
	/**
	 * Constructor for led strip class
	 * @param numLEDs - number of LED's in the total strip.
	 */
	public DotStarsLEDStrip(int numLEDs, Port port) {
		// Number of bytes in color buffer needed - each LED has 4 bytes (1 brightness, then 1 for RGB each),
		// plus the start and end frame.
		num_leds = numLEDs;
		int num_bytes_for_strip = 4*numLEDs + startFrame.length + endFrame.length;
		endFrameOffset = 4*numLEDs + startFrame.length;
		
		// Initialize color buffer
		ledBuffer = new byte[num_bytes_for_strip];
		
		// Write in the start/end buffers
		for (int i = 0; i < startFrame.length; i++) {
			ledBuffer[i] = startFrame[i];
		}
		for (int i = 0; i < endFrame.length; i++) {
			ledBuffer[i+endFrameOffset] = endFrame[i];
		}
		
		// Initialize SPI coms on the onboard, chip-select 0. No chip select used, though.
		spi = new SPI(port);
		spi.setMSBFirst();
		spi.setClockActiveLow();
		spi.setClockRate(SPI_CLK_RATE); 
		spi.setSampleDataOnFalling();
		
		clearColorBuffer();
	}
	
	public int updateColors() {
		int ret_val = 0;

		// Chunk the TX'es into smaller size, otherwise we get -1 returned from
		// spi.write. This is undocumented, and was found by experimentation.
		for (int offset = 0; offset < ledBuffer.length; offset = offset + CHUNK_SIZE) {
			int start_index = offset;
			int end_index = Math.min(offset + CHUNK_SIZE, ledBuffer.length);
			int size = end_index - start_index;
			byte[] tx_array = Arrays.copyOfRange(ledBuffer, start_index, end_index);
			ret_val = spi.write(tx_array, size); 
		}

		return ret_val;
	}
	        
	/**
	 * Clears all contents in the color buffer. This turns off all LED's. Be sure to call the updateColors() class 
	 * some time after this one to actually send the commanded colors to the actual strip.
	 */
	
	public void clearColorBuffer(){
		for(int i = 0; i < num_leds; i++){
			setLEDColor(i,0,0,0);
		}

    	return;
	}
	
	public void clearColorBuffer(int startIndex, int endIndex){
		if (startIndex < 0) startIndex = 0;
		if (endIndex > num_leds) endIndex = num_leds;
		for(int i = startIndex; i < endIndex; i++){
			setLEDColor(i,0,0,0);
		}

    	return;
	}
	
	public void setLEDColors(double r, double g, double b){
		for(int i = 0; i < num_leds; i++){
			setLEDColor(i,r,g,b);
		}

    	return;
	}
	
	public void setLEDColors(int startIndex, int endIndex, double r, double g, double b){
		if (startIndex < 0) startIndex = 0;
		if (endIndex > num_leds) endIndex = num_leds;
		for(int i = startIndex; i < endIndex; i++){
			setLEDColor(i,r,g,b);
		}

    	return;
	}
	
	public int getNumLeds() {
		return num_leds;
	}
		
	/**
	 * sets a particular LED in the string to a certain color
	 * @param index - index in the LED to set. 0 is the furthest from the roboRIO, N is the closest.
	 * @param r - red value for the color. Provide as a double in the range of 0 (off) to 1 (full on)
	 * @param g - green value for the color. Provide as a double in the range of 0 (off) to 1 (full on)
	 * @param b - blue value for the color. Provide as a double in the range of 0 (off) to 1 (full on)
	 */
	
	public void setLEDColor(int index, double r, double g, double b){
		
		ledBuffer[index*bytesPerLED + led0Offset + globalOffset] = (byte)-1;
		ledBuffer[index*bytesPerLED + led0Offset + blueOffset] = convDoubletoByte(b);
		ledBuffer[index*bytesPerLED + led0Offset + greenOffset] = convDoubletoByte(g);
		ledBuffer[index*bytesPerLED + led0Offset + redOffset] = convDoubletoByte(r);

    	return;
	}
	
	
	/**
	 * convert a double in the range 0-1 to a byte of value 0x00 to 0xFF. This normalizes the full range of 
	 * the LED brightness to the 0-1 range, hiding the implementation from the users.
	 * @param in
	 * @return
	 */
	private byte convDoubletoByte(double in){
		//Constrain the input to the defined [0,1] input range
		in = Math.min(Math.max(in, 0.0), 1.0);
		//Scale and round
		in = Math.round(in * ledMaxVal);
		//Stupid offsetting b/c java doesn't support unsigned operations
		//This is 2's complement sign conversion. If you don't know what that
		//means, please don't touch this logic.
		if(in > (ledMaxVal+1)/2)
			in = in - (ledMaxVal + 1);
		
		return (byte)in;
	}
}
