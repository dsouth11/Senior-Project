package com.project.AiController;

import java.util.ArrayList;

import com.project.ChessPieces.IChessPiece;

public class ThreatenedPiece {

	private IChessPiece threatendPiece;
	
	private ArrayList<IChessPiece> threats;
	
	public ThreatenedPiece(IChessPiece threatenedPiece) {
		this.threatendPiece = threatenedPiece;
		threats = new ArrayList<IChessPiece>();
	}
	
	public IChessPiece getThreatenedPiece() {
		return threatendPiece;
	}
	
	public ArrayList<IChessPiece> getPiecesThreatening() {
		return threats;
	}
	
	public void addThreat(IChessPiece threat) {
		if(!threats.contains(threat) && !threat.getTeamType().equals(threatendPiece.getTeamType())) {
			threats.add(threat);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ThreatenedPiece) {
			ThreatenedPiece threat = (ThreatenedPiece) obj;
			if(threat.getThreatenedPiece().equals(threatendPiece)) {
				return true;
			}
		}
		return false;
	}
	
}