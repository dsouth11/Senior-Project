package com.project.BoardController;

// Object that defines a simple location variable
// Is related to the grid of the board
// (0,0) -> (7,7) points
public class Location {

	// Starting at the lowest corner (0,0)
	// Up to the top corner which will be (7,7)
	
	private int x, z;
	public Location() {}
	
	public Location(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setNewLocation(Location location) {
		this.x = location.getX();
		this.z = location.getZ();
	}
	
	@Override
	public boolean equals(Object loc1) {
		if(loc1 instanceof Location) {
			Location loc = (Location)loc1;
			if(x == loc.getX() && z == loc.getZ()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + z + ")";
	}
	
}