package com.project.Render;

import com.project.BoardController.Location;

// Manager that lets you locate the center point of each grid point 
public class CenterPointManager {

	private static int defaultDimension = 72; // Default height and width for the pieces; default: 62
	
	private int x = 41; // Default: 50
	private int z = 594; // Default: 600
	
	private int index = 79; // Default 79
	
	// This class is responsible for finding the center point for each chess square
	public CenterPointManager() {}
	
	
	// Input is x and z coord for the grid plain
	// Output is a location object that has the center point for that square as x, z
	public Location centerPointAlgorithm(int x, int z) {
		// Start center point at (0,0) is (50, 600);
	
		return new Location(this.x + (index * x), this.z - (index * z + 5));
	}
	
	// Input is Location object
	// Output is a location object that has the center point for that square as x, z
	public Location centerPointAlgorithm(Location loc) {
		return new Location(this.x + (index * loc.getX()), this.z - (index * loc.getZ()));
	}
	
	// Returns default height and width of pieces
	public int getDimensionOfPiece() {
		return defaultDimension;
	}
	
	
	public Location getRollLabelLocation(int x, int z) {
		Location loc = centerPointAlgorithm(x, z);
		int locX = loc.getX();
		int locZ = loc.getZ();
		
		return new Location(locX + 50, locZ + 50);
		
		
	}
	
}