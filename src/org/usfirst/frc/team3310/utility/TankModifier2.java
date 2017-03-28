package org.usfirst.frc.team3310.utility;

import jaci.pathfinder.Trajectory.Segment;

public class TankModifier2 {
	
	private Segment[] left_traj;
    private Segment[] right_traj;
	
	public void modify(Segment[] original, double wheelbase_width) {
	    double w = wheelbase_width / 2;
	    
	    left_traj = new Segment[original.length];
	    right_traj = new Segment[original.length];
	    
	    for (int i = 0; i < original.length; i++) {
	        Segment seg = original[i];
	        Segment left = new Segment(seg.dt, seg.x, seg.y, seg.position, seg.velocity, seg.acceleration, seg.jerk, seg.heading);
	        Segment right = new Segment(seg.dt, seg.x, seg.y, seg.position, seg.velocity, seg.acceleration, seg.jerk, seg.heading);
	        
	        double cos_angle = Math.cos(seg.heading);
	        double sin_angle = Math.sin(seg.heading);
	        
	        left.x = seg.x - (w * sin_angle);
	        left.y = seg.y + (w * cos_angle);
	        
	        if (i > 0) {
	            Segment last = left_traj[i - 1];
	            double distance = Math.sqrt(
	                (left.x - last.x) * (left.x - last.x)
	                + (left.y - last.y) * (left.y - last.y)
	            );
	            
	            left.position = last.position + distance;
	            left.velocity = distance / seg.dt;
	            left.acceleration = (left.velocity - last.velocity) / seg.dt;
	            left.jerk = (left.acceleration - last.acceleration) / seg.dt;
	        }
	        
	        right.x = seg.x + (w * sin_angle);
	        right.y = seg.y - (w * cos_angle);
	        
	        if (i > 0) {
	            Segment last = right_traj[i - 1];
	            double distance = Math.sqrt(
	                (right.x - last.x) * (right.x - last.x)
	                + (right.y - last.y) * (right.y - last.y)
	            );
	            
	            right.position = last.position + distance;
	            right.velocity = distance / seg.dt;
	            right.acceleration = (right.velocity - last.velocity) / seg.dt;
	            right.jerk = (right.acceleration - last.acceleration) / seg.dt;
	        }
	        
	        left_traj[i] = left;
	        right_traj[i] = right;
	    }	    
	}
	
	public Segment[] getLeftSegments() {
		return left_traj;
	}
	
	public Segment[] getRightSegments() {
		return right_traj;
	}
}
