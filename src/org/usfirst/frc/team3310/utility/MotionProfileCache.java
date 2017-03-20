package org.usfirst.frc.team3310.utility;

import java.util.Hashtable;

public class MotionProfileCache {

	private Hashtable<String, MotionProfileBoxCar> cache;
	private static MotionProfileCache instance;
	
	private MotionProfileCache() {
		cache = new Hashtable<String, MotionProfileBoxCar>();
	}
	
	public void addMP(String key, double startDistance, double targetDistance, double maxVelocity, double itp, double t1, double t2) {
		MotionProfileBoxCar mp = new MotionProfileBoxCar(startDistance, targetDistance, maxVelocity, itp, t1, t2);
		this.addMP(key, mp);
	}
	
	public void addMP(String key, MotionProfileBoxCar mp) {
		cache.put(key, mp);
	}
	
	public MotionProfileBoxCar getMP(String key) {
		return cache.get(key);
	}
	
	public static MotionProfileCache getInstance() {
		if(instance == null) {
			instance = new MotionProfileCache();
		}
		return instance;
	}
	
	public void release() {
		instance = null;
	}
}
