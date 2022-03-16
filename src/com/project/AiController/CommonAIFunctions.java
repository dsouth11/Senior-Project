package com.project.AiController;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.BoardController.Location;
import com.project.ChessPieces.BishopPiece;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.KingPiece;
import com.project.ChessPieces.KnightPiece;
import com.project.ChessPieces.QueenPiece;
import com.project.Main.Main;

public class CommonAIFunctions {

	private AIController controller;
	public CommonAIFunctions(AIController controller) {
		this.controller = controller;
	}
	
	// Return location the piece can move too in order to take the king
	// Returns null if the piece cannot take the king
	public Location canPieceTakeKing(IChessPiece piece) {
		
		// Get the kings location of the other team
		IChessPiece enemyKing = null;
		for(IChessPiece king : controller.getEnemyTeam().getChessPieces()) {
			if(king instanceof KingPiece) {
				enemyKing = king;
				break;
			}
		}
		// Error check
		if(enemyKing == null) return null;
		
		for(Location possibleMoves : piece.getPossibleMoves()) {
			if(enemyKing.getLocation().equals(possibleMoves)) {
				return possibleMoves;
			}
		}
		
		return null;
	}
	
	// Return the location a piece can move to inorder to take out a bishop
	// Returns null if the piece can't take out a bishop
	public Location canPieceTakeBishop(IChessPiece piece) {
		
		// Get the kings location of the other team
		IChessPiece enemyKing = null;
		for(IChessPiece king : controller.getEnemyTeam().getChessPieces()) {
			if(king instanceof BishopPiece) {
				enemyKing = king;
				break;
			}
		}
		
		// Error check
		if(enemyKing == null) return null;

		for(Location possibleMoves : piece.getPossibleMoves()) {
			if(enemyKing.getLocation().equals(possibleMoves)) {
				return possibleMoves;
			}
		}
		
		return null;
	}
	
	// Return the location a piece can move to inorder to take out a knight
	// Returns null if the piece can't take out a knight
	public Location canPieceTakeKnight(IChessPiece piece) {

		// Get the kings location of the other team
		IChessPiece enemyKing = null;
		for(IChessPiece king : controller.getEnemyTeam().getChessPieces()) {
			if(king instanceof KnightPiece) {
				enemyKing = king;
				break;
			}
		}
		
		// Error check
		if(enemyKing == null) return null;
		
		for(Location possibleMoves : piece.getPossibleMoves()) {
			if(enemyKing.getLocation().equals(possibleMoves)) {
				return possibleMoves;
			}
		}
		
		return null;
	}
	
	// Return the location a piece can move to inorder to take out a knight
	// Returns null if the piece can't take out a knight
	public Location canPieceTakeQueen(IChessPiece piece) {

		// Get the kings location of the other team
		IChessPiece enemyKing = null;
		for(IChessPiece king : controller.getEnemyTeam().getChessPieces()) {
			if(king instanceof QueenPiece) {
				enemyKing = king;
				break;
			}
		}
		
		// Error check
		if(enemyKing == null) return null;
		
		for(Location possibleMoves : piece.getPossibleMoves()) {
			if(enemyKing.getLocation().equals(possibleMoves)) {
				return possibleMoves;
			}
		}
		
		return null;
	}
	
	
	// Return all possible moves for all the teams pieces
	// Will never return null. Impossible
	public ArrayList<PieceInformation> getAllPossibleMoves() {
		ArrayList<PieceInformation> pieceInformation = new ArrayList<PieceInformation>();
		for(IChessPiece piece : controller.getTeam().getChessPieces()) {
				for(Location loc : piece.getPossibleMoves()) {
					// Check if commander is dead
					IChessPiece commander = controller.getTeam().getCommanderLogic().getCommanderForPiece(piece);
					if(commander != null) {
						IChessPiece pieceAtLocation = controller.getBoardController().getPieceAtLocation(loc);
						if(pieceAtLocation != null && !pieceAtLocation.getTeamType().equals(piece.getTeamType())) {
							pieceInformation.add(new PieceInformation(piece, loc, true, pieceAtLocation));
						}else {
							pieceInformation.add(new PieceInformation(piece, loc, false, pieceAtLocation));
						}
					}
				}
			
		}
		return pieceInformation;
	}
	
	// Function that will return an arraylist of all pieces who can move to a specific location
	public ArrayList<IChessPiece> findPiecesToMoveToLocation(Location location) {
		ArrayList<IChessPiece> pieces = new ArrayList<IChessPiece>();
		
		for(IChessPiece onBoard : controller.getTeam().getChessPieces()) {
			ArrayList<Location> locations = onBoard.getPossibleMoves();
			if(locations.contains(location) && !pieces.contains(onBoard)) {
				pieces.add(onBoard);
			}
			
		}
		
		return null;
	}
	
	
	// Function that will return an arraylist of all pieces who can move to a specific location
	public ArrayList<IChessPiece> findPiecesToMoveToLocationEnemy(Location location) {
		ArrayList<IChessPiece> pieces = new ArrayList<IChessPiece>();
		
		for(IChessPiece onBoard : controller.getEnemyTeam().getChessPieces()) {
			ArrayList<Location> locations = onBoard.getPossibleMoves();
			if(locations.contains(location) && !pieces.contains(onBoard)) {
				pieces.add(onBoard);
			}
			
		}
		
		return null;
	}
	
	
	// Returns all threatened pieces of the team
	// A threatened piece means that it's possible for it to be taken out/killed
	public ArrayList<ThreatenedPiece> getThreatenedPieces() {
		ArrayList<ThreatenedPiece> threatenedPieces = new ArrayList<ThreatenedPiece>();
		
		// Get all enemy pieces
		for(IChessPiece enemyPieces : controller.getEnemyTeam().getChessPieces()) {
			ArrayList<Location> moves = enemyPieces.getPossibleMoves();
			for(Location loc : moves) {
				if(Main.getBoardController().getPieceAtLocation(loc) != null) { // It take take out a piece
					boolean found = false;
					ThreatenedPiece piece = new ThreatenedPiece(Main.getBoardController().getPieceAtLocation(loc));
					
					if(piece.getThreatenedPiece() instanceof KingPiece || piece.getThreatenedPiece() instanceof BishopPiece 
							|| piece.getThreatenedPiece() instanceof KnightPiece) {
						for(ThreatenedPiece threat : threatenedPieces) {
							if(piece.equals(threat)) {
								found = true;
								threat.addThreat(enemyPieces);
								break;
							}
						}
						if(!found) {
							threatenedPieces.add(piece);
						}
					}
					
				}
			}
		}
		
		if(threatenedPieces.size() == 0) {
			return null;
		}
		
		return threatenedPieces;
		
	}
	
	// Get the pieces who can take out a specific piece
	public ArrayList<IChessPiece> getPieceWhoCanKill(IChessPiece piece) {
		ArrayList<IChessPiece> piecesToReturn = new ArrayList<IChessPiece>();
		for(IChessPiece pieces : controller.getTeam().getChessPieces()) {
			if(!pieces.getTeamType().equals(piece.getTeamType())) {
				ArrayList<Location> toMove = pieces.getPossibleMoves();
				if(toMove.contains(piece.getLocation())) {
					piecesToReturn.add(pieces);
				}
			}
			
		}
		
		return piecesToReturn;
		
	}
	
	public ArrayList<HashMap<IChessPiece, ArrayList<PieceInformation>>> generateMoveHashes(ArrayList<PieceInformation> information) {
		
		HashMap<IChessPiece, ArrayList<PieceInformation>> piecesThatCanAttackHash = new HashMap<IChessPiece, ArrayList<PieceInformation>>();
		
		HashMap<IChessPiece, ArrayList<PieceInformation>> piecesThatCannotAttackHash = new HashMap<IChessPiece, ArrayList<PieceInformation>>();
		
		for(PieceInformation piece : information) {
			if(piece.canKillPiece()) {
				if(piecesThatCanAttackHash.containsKey(piece.getPiece())) {
					ArrayList<PieceInformation> temp = piecesThatCanAttackHash.get(piece.getPiece());
					if(!temp.contains(piece)) {
						temp.add(piece);
						piecesThatCanAttackHash.put(piece.getPiece(), temp);
					}
				}else {
					ArrayList<PieceInformation> temp = new ArrayList<PieceInformation>();
					temp.add(piece);
					piecesThatCanAttackHash.put(piece.getPiece(), temp);
					
				}
				
			}else {
				if(piecesThatCannotAttackHash.containsKey(piece.getPiece())) {
					ArrayList<PieceInformation> temp = piecesThatCannotAttackHash.get(piece.getPiece());
					if(!temp.contains(piece)) {
						temp.add(piece);
						piecesThatCannotAttackHash.put(piece.getPiece(), temp);
					}
				}else {
					ArrayList<PieceInformation> temp = new ArrayList<PieceInformation>();
					temp.add(piece);
					piecesThatCannotAttackHash.put(piece.getPiece(), temp);
				}
			}
			
		}
		ArrayList<HashMap<IChessPiece, ArrayList<PieceInformation>>> map = new ArrayList<HashMap<IChessPiece, ArrayList<PieceInformation>>>();
		map.add(piecesThatCanAttackHash);
		map.add(piecesThatCannotAttackHash);
		
		return map;
	}

	
	public Location moveToSafeLocation(IChessPiece piece) {
		// TODO Safe spot check
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
		
		ArrayList<Location> moveTo = piece.getPossibleMoves();
		
		if(moveTo == null || moveTo.isEmpty()) return null;
		
		for(Location locations : moveTo) {
			boolean isSafe = true;
			if(Main.getBoardController().getPieceAtLocation(locations) == null) {
				for(int x = 0; x != 8; x++) {
					int xOffset = offsets[x][0];
					int zOffset = offsets[x][1];
					Location loc = new Location(locations.getX() + xOffset, locations.getZ() + zOffset);
					if(Main.getBoardController().isLocationOnBoard(loc)) {
						IChessPiece atLocation = Main.getBoardController().getPieceAtLocation(loc);
						if(atLocation != null) {
							if(!atLocation.getTeamType().equals(piece.getTeamType())) {
								isSafe = false;
								break;
							}
						}
					}
				}
				if(isSafe) {
					return locations;
				}
			}
			
		}
		
		
		return null;
	}
	
}