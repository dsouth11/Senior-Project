package com.project.ChessPieces;

import java.util.ArrayList;

import com.project.BoardController.Location;
import com.project.Main.Main;
import com.project.Render.PiecesTexture;
import com.project.TeamController.TeamType;


public class KingPiece implements IChessPiece{

	private Location location;
	private PiecesTexture texture;
	
	private boolean isAlive;
	
	private boolean hasMovedOnce;
	
	private TeamType team;
	
	private Location startLocation;
	
	public KingPiece(Location location, TeamType team) {
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
	
	
	@Override
	public ArrayList<Location> getPossibleMoves() {
		DistanceCalculator cal = new DistanceCalculator();

		return cal.findPossiblePaths(this, 3);
	}

}