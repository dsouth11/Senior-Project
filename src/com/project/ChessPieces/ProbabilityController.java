package com.project.ChessPieces;

import java.util.Random;

import com.project.Main.Main;

public class ProbabilityController {

	public ProbabilityController() {}

	
	// Return the probabilty
	public int[] getProbablity(IChessPiece attacker, IChessPiece attacking) {
	
		if(attacker instanceof KingPiece) {
			if(attacking instanceof KingPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof QueenPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof KnightPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof BishopPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof RookPiece) {
				return new int[] {6, 5};
			}else {
				return new int[] {6, 5, 4, 3, 2, 1};
			}
			
		}else if(attacker instanceof QueenPiece) {
			if(attacking instanceof KingPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof QueenPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof KnightPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof BishopPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof RookPiece) {
				return new int[] {6, 5};
			}else {
				return new int[] {6, 5, 4, 3, 2};
			}
			
		}else if(attacker instanceof KnightPiece) {
			if(attacking instanceof KingPiece) {
				return new int[] {6};
			}else if(attacking instanceof QueenPiece) {
				return new int[] {6};
			}else if(attacking instanceof KnightPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof BishopPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof RookPiece) {
				return new int[] {6, 5};
			}else {
				return new int[] {6, 5, 4, 3, 2};
			}
			
		}else if(attacker instanceof BishopPiece) {
			if(attacking instanceof KingPiece) {
				return new int[] {6, 5};
			}else if(attacking instanceof QueenPiece) {
				return new int[] {6, 5};
			}else if(attacking instanceof KnightPiece) {
				return new int[] {6, 5};
			}else if(attacking instanceof BishopPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof RookPiece) {
				return new int[] {6, 5};
			}else {
				return new int[] {6, 5, 4, 3};
			}
		}else if(attacker instanceof RookPiece) {
			if(attacking instanceof KingPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof QueenPiece) {
				return new int[] {6, 5, 4};
			}else if(attacking instanceof KnightPiece) {
				return new int[] {6, 5};
			}else if(attacking instanceof BishopPiece) {
				return new int[] {6, 5};
			}else if(attacking instanceof RookPiece) {
				return new int[] {6};
			}else {
				return new int[] {6, 5};
			}
		}else { // Pawn piece
			if(attacking instanceof KingPiece) {
				return new int[] {6};
			}else if(attacking instanceof QueenPiece) {
				return new int[] {6};
			}else if(attacking instanceof KnightPiece) {
				return new int[] {6};
			}else if(attacking instanceof BishopPiece) {
				return new int[] {6, 5};
			}else if(attacking instanceof RookPiece) {
				return new int[] {6};
			}else {
				return new int[] {6, 5, 4};
			}
		}
	}
	
	public int getRandomNumber(boolean display) {
		Random rand = new Random();
		int r = rand.nextInt(6) + 1;

		if(display) {
			Main.getBoardController().getLogs().addLog("Rolled random number: " + r);
		}
		
		return r;
		
	}
	
}