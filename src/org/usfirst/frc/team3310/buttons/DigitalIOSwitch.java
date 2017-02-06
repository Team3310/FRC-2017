package org.usfirst.frc.team3310.buttons;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * @author rhhs
 */
public class DigitalIOSwitch extends Button {
	DigitalInput digitalInput;

	public DigitalIOSwitch(int channel){
		digitalInput = new DigitalInput(channel);
	}

	public boolean get() {
		//System.out.println("Switch = " + digitalInput.get());           
		return digitalInput.get();
	}
}