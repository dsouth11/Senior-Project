package com.project.AiController;

import com.project.BoardController.Location;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.ProbabilityController;

public class SimulatedMove {

	private IChessPiece piece;   // The piece that would move
	private Location location;   // The location the piece could move too
	private IChessPiece attack;  // The ememy piece that could be attacked
	private int[] probablity;    // Probablity of success that the attack will work
	
	public SimulatedMove(IChessPiece piece, Location location, IChessPiece attack) {
		this.piece = piece;
		this.location = location;
		this.attack = attack;
		this.probablity = new ProbabilityController().getProbablity(piece, attack);
	}
	
	public IChessPiece getPiece() {
		return piece;
	}
	
	public IChessPiece getEnemyPiece() {
		return attack;
	}
	
	public Location getMoveToLocation() {
		return location;
	}
	
	public int[] getProbablity() {
		return probablity;
	}
	
	public int getProbablitySize() {
		return probablity.length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SimulatedMove) {
			SimulatedMove move = (SimulatedMove)obj;
			if(move.getEnemyPiece().equals(this.attack) && move.getPiece().equals(this.piece)) {
				return true;
			}
		}
		
		return false;
	}
}
