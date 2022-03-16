package com.project.ChessPieces;

import java.util.ArrayList;

import com.project.BoardController.Location;
import com.project.Main.Main;
import com.project.Render.PiecesTexture;
import com.project.TeamController.TeamType;

public class BishopPiece implements IChessPiece{

	private Location location;
	
	private PiecesTexture texture;
	
	private boolean isAlive; // Is set when the piece dies
	
	private boolean hasMovedOnce;
	
	private TeamType team;
	
	private Location startLocation;
	
	private ArrayList<IChessPiece> delegated;
	
	public BishopPiece(Location location, TeamType team) {
		this.team = team;
		isAlive = true;
		hasMovedOnce = false;
		this.location = location;
		texture = new PiecesTexture(this, getLocation().getX(), getLocation().getZ(), team);
		startLocation = location;
		this.delegated = new ArrayList<IChessPiece>();
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
	public boolean isAlive() {
		return isAlive;
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

	// Gets the texture and other features of the piece for board rendering
	@Override
	public PiecesTexture getTexture() {
		return texture;
	}
	
	public TeamType getTeamType() {
		return team;
	}
	
	public ArrayList<IChessPiece> getDelegatedPieces(){
		return delegated;
	}
	
	public void removeDelegatedPiece(IChessPiece piece) {
		if(delegated.contains(piece)) {
			delegated.remove(piece);
		}
		
	}
	
	public void addDelegatedPiece(IChessPiece piece) {
		if(!delegated.contains(piece)) {
			delegated.add(piece);
		}
	}

	@Override
	public ArrayList<Location> getPossibleMoves() {
		ArrayList<Location> locationsToMove = new ArrayList<Location>();
		if(team.equals(TeamType.WHITE)) {
			Location loc = new Location(getLocation().getX(), getLocation().getZ() + 1);
			if(Main.getBoardController().getPieceAtLocation(loc) == null) {
				if(Main.getBoardController().isLocationOnBoard(loc)) {
					locationsToMove.add(loc);
				}
			}else {
				// A piece is there
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(loc);
				if(piece.getTeamType().equals(TeamType.BLACK)) {
					locationsToMove.add(loc);
				}
			}
			
			// Diagnonals
			Location diag1 = new Location(getLocation().getX() + 1, getLocation().getZ() + 1);
			if(Main.getBoardController().isLocationOnBoard(diag1)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(diag1);
				if(piece != null) {
					if(Main.getBoardController().isLocationOnBoard(diag1)) {
						if(Main.getBoardController().getTeamPieceBelongsTo(piece) != Main.getBoardController().getTeamPieceBelongsTo(Main.getBoardController().getPieceAtLocation(getLocation()))) {
							locationsToMove.add(diag1);
						}
					}
				}
			}
			
			Location diag2 = new Location(getLocation().getX() - 1, getLocation().getZ() + 1);
			if(Main.getBoardController().isLocationOnBoard(diag2)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(diag2);
				if(Main.getBoardController().isLocationOnBoard(diag2)) {
					if(piece != null) {
						if(Main.getBoardController().getTeamPieceBelongsTo(piece) != Main.getBoardController().getTeamPieceBelongsTo(Main.getBoardController().getPieceAtLocation(getLocation()))) {
							locationsToMove.add(diag2);
						}
					}
				}
			}
		}else {
			Location loc = new Location(getLocation().getX(), getLocation().getZ() - 1);
			if(Main.getBoardController().getPieceAtLocation(loc) == null) {
				if(Main.getBoardController().isLocationOnBoard(loc)) {
					locationsToMove.add(loc);
				}
			}else {
				// A piece is there
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(loc);
				if(piece.getTeamType().equals(TeamType.WHITE)) {
					locationsToMove.add(loc);
				}
			}
			
			// Diagnonals
			Location diag1 = new Location(getLocation().getX() - 1, getLocation().getZ() - 1);
			if(Main.getBoardController().isLocationOnBoard(diag1)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(diag1);
				if(piece != null) {
					if(Main.getBoardController().getTeamPieceBelongsTo(piece) != Main.getBoardController().getTeamPieceBelongsTo(Main.getBoardController().getPieceAtLocation(getLocation()))) {
						locationsToMove.add(diag1);
					}
				}
			}
			
			Location diag2 = new Location(getLocation().getX() + 1, getLocation().getZ() - 1);
			if(Main.getBoardController().isLocationOnBoard(diag2)) {
				IChessPiece piece = Main.getBoardController().getPieceAtLocation(diag2);
				if(piece != null) {
					if(Main.getBoardController().getTeamPieceBelongsTo(piece) != Main.getBoardController().getTeamPieceBelongsTo(Main.getBoardController().getPieceAtLocation(getLocation()))) {
						if(Main.getBoardController().isLocationOnBoard(diag2)) {
							locationsToMove.add(diag2);
						}
					}
				}
			}
		}
		
		
		return locationsToMove;
	}
	
}