package com.project.ChessPieces;

import java.util.ArrayList;

import com.project.BoardController.Location;
import com.project.Main.Main;

public class DistanceCalculator {

	public DistanceCalculator() {}
		
	private ArrayList<Location> locations = new ArrayList<Location>();
	
	int[][] offsets = {
	        {1, 0},
	        {0, 1},
	        {-1, 0},
	        {0, -1},
	        {1, 1},
	        {-1, 1},
	        {-1, -1},
	        {1, -1},
	       		        
	    };
	
	public ArrayList<Location> findPossiblePaths(IChessPiece startPiece, int checkAmount) {
		locations.clear();

		Location from = startPiece.getLocation();
		
		// Initial check for attacking pieces
		for(int x = 0; x != 8; x++) {
			int xOffset = offsets[x][0];
			int zOffset = offsets[x][1];
			Location loc = new Location(from.getX() + xOffset, from.getZ() + zOffset);
			if(Main.getBoardController().isLocationOnBoard(loc)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(loc);
				if(piece != null) {
					if(!piece.getTeamType().equals(startPiece.getTeamType())) {
						addToList(startPiece.getLocation(), loc, checkAmount);
					}
				}
			}
		}
		
		recersivePath(startPiece, startPiece.getLocation(), checkAmount, 0);
		return locations;
	}
	
	
	// On start the currentPathAmount = 0
	private void recersivePath(IChessPiece startPiece, Location from, int checkAmount, int currentPathAmount) {
		if(currentPathAmount >= checkAmount) {
			return;
		}
		
		for(int x = 0; x != 8; x++) {
			int xOffset = offsets[x][0];
			int zOffset = offsets[x][1];
			Location loc = new Location(from.getX() + xOffset, from.getZ() + zOffset);
			if(Main.getBoardController().isLocationOnBoard(loc)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(loc);
				if(piece != null) {
					if(!piece.getTeamType().equals(startPiece.getTeamType())) {
						// It's an enemy piece
						// addToList(startPiece.getLocation(), loc, checkAmount);
						// recersivePath(startPiece, loc, checkAmount, (currentPathAmount + 1));
					}
				}else {
					if(!loc.equals(from)) {
						addToList(startPiece.getLocation(), loc, checkAmount);
						recersivePath(startPiece, loc, checkAmount, (currentPathAmount + 1));
						
					}
				}
			}
		}
	}
	
	// Return 1 is in range, return -1 if not
	private int getDistance(Location from, Location to, int checkAmount) {
		if(!Main.getBoardController().isLocationOnBoard(to) || !Main.getBoardController().isLocationOnBoard(from)) {
			return -1;
		}
		
		int xDistance = Math.abs(from.getX() - to.getX());
		int zDistance = Math.abs(from.getZ() - to.getZ());
		
		if(xDistance <= checkAmount && zDistance <= checkAmount) {
			return 1;
		}
		
		return -1;
	}
	
	
	private void addToList(Location from, Location to, int checkAmount) {
		if(!locations.contains(to)) {
			if(getDistance(from, to, checkAmount) == 1) {
				locations.add(to);
			}
		}
	}
	
	
}