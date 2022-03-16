package com.project.AiController;

import com.project.BoardController.Location;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.ProbabilityController;

public class PieceInformation {

	private boolean canKill; // If there is a piece at that location
	private IChessPiece piece; // Piece to move
	private Location location; // Location to move too
	private IChessPiece pieceAtMoveLocation;
	private int[] killProbablity = null; // The probablity to take the piece
	
	public PieceInformation(IChessPiece piece, Location location, boolean canKill, IChessPiece pieceAtLocation) {
		this.piece = piece;
		this.canKill = canKill;
		if(canKill) {
			killProbablity = new ProbabilityController().getProbablity(piece, pieceAtLocation);
		}else {
			killProbablity = null;
		}
		this.location = location;
		
		this.pieceAtMoveLocation = pieceAtLocation;
	}
	
	public IChessPiece getPiece() {
		return piece;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public IChessPiece getPieceAtMoveToLocation() {
		return pieceAtMoveLocation;
	}
	
	public boolean canKillPiece() {
		return canKill;
	}

	public int[] getKillProbality() {
		return killProbablity;
	}
	
	public int getKillProbablitySize() {
		if(getKillProbality() == null) {
			return 0;
		}
		return getKillProbality().length;
	}
	
}