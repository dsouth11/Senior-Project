package com.project.ChessPieces;

import java.util.ArrayList;

import com.project.BoardController.Location;
import com.project.Main.Main;
import com.project.Render.PiecesTexture;
import com.project.TeamController.TeamType;

public class KnightPiece implements IChessPiece{

	private Location location;
	
	private PiecesTexture texture;
	
	private boolean isAlive;
	
	private boolean hasMovedOnce;
	
	private Location startLocation;
	
	private TeamType team;
	
	private boolean canDoSpecialMove;
	
	private boolean movedFirst = false;
	
	public KnightPiece(Location location, TeamType team) {
		this.canDoSpecialMove = false;
		this.team = team;
		isAlive = true;
		hasMovedOnce = false;
		this.location = location;
		texture = new PiecesTexture(this, getLocation().getX(), getLocation().getZ(), team);
		startLocation = location;
	}
	
	@Override
	public boolean isAlive() {
		return isAlive;
	}
	
	@Override
	public boolean hasMovedAlready() {
		return hasMovedOnce;
	}

	@Override
	public void setHasMovedOnce() {
		this.hasMovedOnce = true;
	}
	
	@Override
	public void destroyPiece() {
		this.isAlive = false;
		getTexture().removeTextureFromBoard();
		Main.getBoardController().removePieceFromBoard(this);
	}

	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public Location getStartLocation() {
		return startLocation;
	}

	@Override
	public void setLocation(int x, int z) {
		location = new Location(x, z);
		setHasMovedOnce();
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
		setHasMovedOnce();
	}
	
	@Override
	public PiecesTexture getTexture() {
		return texture;
	}
	
	public TeamType getTeamType() {
		return team;
	}
	
	public boolean canDoSpecialMove() {
		return canDoSpecialMove;
	}
	
	public void allowSpecialMove() {
		System.out.println("1");
		this.canDoSpecialMove = true;
		movedFirst = true;
	}
	
	public void specialMovePerformed() {
		System.out.println("2");
		this.canDoSpecialMove = false;
	}
	
	public boolean movedFirst() {
		return movedFirst;
	}
	
	public void resetMovedFirst() {
		System.out.println("3");
		movedFirst = false;
	}
	
	@Override
	public ArrayList<Location> getPossibleMoves() {
		DistanceCalculator cal = new DistanceCalculator();

		return cal.findPossiblePaths(this, 5);
	}
	
	public ArrayList<Location> getAttackLocations() {
		ArrayList<Location> attackLocations = new ArrayList<Location>();
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
		
		for(int x = 0; x != 8; x++) {
			int xOffset = offsets[x][0];
			int zOffset = offsets[x][1];
			Location loc = new Location(this.getLocation().getX() + xOffset, this.getLocation().getZ() + zOffset);
			if(Main.getBoardController().isLocationOnBoard(loc)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(loc);
				if(piece != null) {
					if(!piece.getTeamType().equals(this.getTeamType())) {
						attackLocations.add(loc);
					}
				}
			}
		}
		return attackLocations;
		
	}
	
}