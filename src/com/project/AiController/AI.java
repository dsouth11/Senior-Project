package com.project.AiController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.project.BoardController.Location;
import com.project.ChessPieces.BishopPiece;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.KingPiece;
import com.project.ChessPieces.KnightPiece;
import com.project.ChessPieces.ProbabilityController;
import com.project.Main.Main;

public class AI {

	private AIController controller;
	private CommonAIFunctions commonFunctionsController;
	
	private int[][] offsets = {
	        {1, 0},
	        {0, 1},
	        {-1, 0},
	        {0, -1},
	        {1, 1},
	        {-1, 1},
	        {-1, -1},
	        {1, -1},
	       		        
	    };
	
	
	public AI(AIController controller) {
		this.controller = controller;
		this.commonFunctionsController = new CommonAIFunctions(controller);
		run();
	}
	
	// Look for an attack with no movement first 
	
	// (Only if 2 or more moves left) Look to move to the highest value with the highest probability for success and do hierarchy for who will take the piece (archer, pawn, queen, bishop, king)
	// KNIGHT Look to move to the highest value with the highest probability for success and do hierarchy for who will take the piece
	
	// In danger check if == 1 move left (move the piece out of the way, if it can't move to safety, attack the enemy piece)
	// Random move
	
	
	
	// Refinements needed:
	// Make it so the king doesn't attempt to take out pieces until necessary
	// Make it so bishops and kings don't try and do things themselves if not necessary.  Maybe if there is above a certain amount of pieces left on the team
	//   (16 pieces on the board), maybe bishop/king only attack if there is less than 10 pieces on the board still
	// Add better probablity sets
	// I don't think AI is following all the rules all the time - Slow the AI down and check this out
	
	
	private void run() {
		int randomNumber = new ProbabilityController().getRandomNumber(false);
		
		ArrayList<PieceInformation> information = commonFunctionsController.getAllPossibleMoves();
		
		
		if(attackKingCheck(information, randomNumber)) return;
		
		if (attackWithNoMovesAI(information, randomNumber)) return;
		
		if ((controller.getTeam().getCommanderLogic().getMaxMoves() - controller.getTeam().getAmountOfMovesDone() <= 2) || 
				(controller.getTeam().getChessPieces().size() < 10 && (controller.getTeam().getCommanderLogic().getMaxMoves() - controller.getTeam().getAmountOfMovesDone() <= 1))) {
			if (attackLookForHighestValueAI(information, randomNumber)) return;
		}
		
		if (attackLookForHighestValueKnightOnlyAI(information, randomNumber)) return;
		
		// Finished in AI class, work on last method in commonAiFunctions
		if (controller.getTeam().getCommanderLogic().getMaxMoves() - controller.getTeam().getAmountOfMovesDone() <= 1) {
			if(inDangerCheckAI(information, randomNumber)) return;
		}
		
		if(inDangerCheckAI(information, randomNumber)) return;
		
		// Finished
		if (randomMoveAI(information, randomNumber)) return;
		
		Main.getBoardController().setNextPlayerToMove();

		Main.getBoardController().getTeam1().getCommanderLogic().reset();
		Main.getBoardController().getTeam2().getCommanderLogic().reset();
		
		System.out.println("How did we get here?");
	}
	
	private boolean attackKingCheck(ArrayList<PieceInformation> information, int randomNumber) {
		HashMap<IChessPiece, ArrayList<PieceInformation>> canAttackWithNoMovement = commonFunctionsController.generateMoveHashes(information).get(0);
		
		if(canAttackWithNoMovement == null || canAttackWithNoMovement.isEmpty()) return false;
		
		PieceInformation highestMove = null;
		
		int highestProb = 0;
		
		for(IChessPiece piece : canAttackWithNoMovement.keySet()) {
			if(checkIfPieceHasCommander(piece)) {
				ArrayList<PieceInformation> movements = canAttackWithNoMovement.get(piece);
				for(PieceInformation info : movements) {
					if(info.getPieceAtMoveToLocation() instanceof KingPiece) {
						if(info.getKillProbablitySize() > highestProb) {
							highestProb = info.getKillProbablitySize();
							highestMove = info;
						}
					}
				}
			}
			
		}
		
		if(highestMove == null) return false;
		
		if((new ProbabilityController().getProbablity(highestMove.getPiece(), highestMove.getPieceAtMoveToLocation())).length > 2) {
			movePiece(highestMove, highestMove.getLocation(), randomNumber);
			return true;
		}
		
		return false;
	}
	
	private boolean attackWithNoMovesAI(ArrayList<PieceInformation> information, int randomNumber) {
		HashMap<IChessPiece, ArrayList<PieceInformation>> canAttackWithNoMovement = commonFunctionsController.generateMoveHashes(information).get(0);
		
		if(canAttackWithNoMovement == null || canAttackWithNoMovement.isEmpty()) return false;
		
		PieceInformation highestMove = null;
		
		int highestProb = 0;
		
		for(IChessPiece piece : canAttackWithNoMovement.keySet()) {
			if(checkIfPieceHasCommander(piece)) {
				ArrayList<PieceInformation> movements = canAttackWithNoMovement.get(piece);
				for(PieceInformation info : movements) {
					if(info.getKillProbablitySize() > highestProb) {
						highestProb = info.getKillProbablitySize();
						highestMove = info;
					}
				}
			}
			
		}
		
		if(highestMove == null) return false;
		
		movePiece(highestMove, highestMove.getLocation(), randomNumber);
		
		return true;
	}
	
	private boolean attackLookForHighestValueAI(ArrayList<PieceInformation> information, int randomNumber) {
		ArrayList<SimulatedMove> simulatedMoves = new ArrayList<SimulatedMove>();
		
		for(IChessPiece teamPiece : controller.getTeam().getChessPieces()) {
			if(checkIfPieceHasCommander(teamPiece)) {
				if(!(teamPiece instanceof KnightPiece)) {
					for(Location moveTo : teamPiece.getPossibleMoves()) {
						// There must be no piece at this location
						if(Main.getBoardController().getPieceAtLocation(moveTo) == null) {
							for(int x = 0; x != 8; x++) {
								int xOffset = offsets[x][0];
								int zOffset = offsets[x][1];
								Location loc = new Location(moveTo.getX() + xOffset, moveTo.getZ() + zOffset);
								if(Main.getBoardController().isLocationOnBoard(loc)) {
									IChessPiece pieceAtLocation = Main.getBoardController().getPieceAtLocation(loc);
									if(pieceAtLocation != null) {
										if(!pieceAtLocation.getTeamType().equals(teamPiece.getTeamType())) {
											// We have a valid simulated move
											simulatedMoves.add(new SimulatedMove(teamPiece, moveTo, pieceAtLocation));
										}
									}
									
								}
							}
							
							
						}
						
					}
									
				}
			}
			
		}
		
		if(simulatedMoves.isEmpty()) return false;
		
		SimulatedMove highestMove = null;
		
		// TODO Maybe add more logic here
		
		for(SimulatedMove move : simulatedMoves) {
			if(highestMove == null) {
				if(Main.getBoardController().getPieceAtLocation(move.getMoveToLocation()) == null) {
					highestMove = move;
				}
			}
			
			if(move.getProbablitySize() > highestMove.getProbablitySize()) {
				if(Main.getBoardController().getPieceAtLocation(move.getMoveToLocation()) == null) {
					highestMove = move;
				}
			}
			
			
		}
		
		
		if(highestMove == null) return false;
		
		// Perform the move
		
		movePieceNotAttacking(highestMove.getMoveToLocation(), highestMove.getPiece());
		
		return true;
	}
	
	private boolean attackLookForHighestValueKnightOnlyAI(ArrayList<PieceInformation> information, int randomNumber) {
		ArrayList<SimulatedMove> simulatedMoves = new ArrayList<SimulatedMove>();
		
		for(IChessPiece teamPiece : controller.getTeam().getChessPieces()) {
			if(checkIfPieceHasCommander(teamPiece)) {
				if(teamPiece instanceof KnightPiece) {
					for(Location moveTo : teamPiece.getPossibleMoves()) {
						// There must be no piece at this location
						if(Main.getBoardController().getPieceAtLocation(moveTo) == null) {
							for(int x = 0; x != 8; x++) {
								int xOffset = offsets[x][0];
								int zOffset = offsets[x][1];
								Location loc = new Location(moveTo.getX() + xOffset, moveTo.getZ() + zOffset);
								if(Main.getBoardController().isLocationOnBoard(loc)) {
									IChessPiece pieceAtLocation = Main.getBoardController().getPieceAtLocation(loc);
									if(pieceAtLocation != null) {
										if(!pieceAtLocation.getTeamType().equals(teamPiece.getTeamType())) {
											// We have a valid simulated move
											simulatedMoves.add(new SimulatedMove(teamPiece, moveTo, pieceAtLocation));
										}
									}
									
								}
							}
							
							
						}
						
					}
									
				}
			}
			
			
		}
		
		if(simulatedMoves.isEmpty()) return false;
		
		SimulatedMove highestMove = null;
		
		
		// TODO Maybe add more logic here
		
		for(SimulatedMove move : simulatedMoves) {
			if(highestMove == null) {
				if(Main.getBoardController().getPieceAtLocation(move.getMoveToLocation()) == null) {
					highestMove = move;
				}
			}
			
			if(move.getProbablitySize() > highestMove.getProbablitySize()) {
				if(Main.getBoardController().getPieceAtLocation(move.getMoveToLocation()) == null) {
					highestMove = move;
				}
			}
			
			
		}
		
		
		if(highestMove == null) return false;
		
		// Perform the move
		
		// Move the piece to the location
		movePieceNotAttacking(highestMove.getMoveToLocation(), highestMove.getPiece());
		
		// Knight special move
		knightSpecialMove(highestMove, randomNumber);
		
		return true;
	}
	
	private boolean inDangerCheckAI(ArrayList<PieceInformation> information, int randomNumber) {
		ArrayList<ThreatenedPiece> threats = commonFunctionsController.getThreatenedPieces();
		ThreatenedPiece highestThreatened = null;
		
		Location moveTo = null;
		
		if(threats == null) {
			return false;
		}
		
		for(ThreatenedPiece piece : threats) {
			if(checkIfPieceHasCommander(piece.getThreatenedPiece())) {
				// King is threatened
				if(piece.getThreatenedPiece() instanceof KingPiece) {
					Location safeSpot = commonFunctionsController.moveToSafeLocation(piece.getThreatenedPiece());
					if(safeSpot != null) {
						moveTo = safeSpot;
						highestThreatened = piece;
						break;
					}
				}
				
				// Bishop threatened
				if(piece.getThreatenedPiece() instanceof BishopPiece) {
					Location safeSpot = commonFunctionsController.moveToSafeLocation(piece.getThreatenedPiece());
					if(safeSpot != null) {
					if(highestThreatened == null) {
						highestThreatened = piece;
						moveTo = safeSpot;
					}else {
						if(highestThreatened.getThreatenedPiece() instanceof KnightPiece) {
							highestThreatened = piece;
							moveTo = safeSpot;
						}
					}
						
					}
				}
				Location safeSpot = commonFunctionsController.moveToSafeLocation(piece.getThreatenedPiece());
				if(safeSpot != null) {
				if(piece.getThreatenedPiece() instanceof KnightPiece) {
					if(highestThreatened == null) {
						highestThreatened = piece;
						moveTo = safeSpot;
					}else {
						if(!(highestThreatened.getThreatenedPiece() instanceof BishopPiece)) {
							highestThreatened = piece;
							moveTo = safeSpot;
						}
					}
				}
				}
			}
			
			
		}
		
		// We don't have any kings/bishops that can be killed on our team
		if(highestThreatened == null || moveTo == null) return false;
		
		movePieceNotAttacking(moveTo, highestThreatened.getThreatenedPiece());
		
		return true;
				
	}

	@SuppressWarnings("unchecked")
	private boolean randomMoveAI(ArrayList<PieceInformation> information, int randomNumber) {
		HashMap<IChessPiece, ArrayList<PieceInformation>> canMoveWithoutAttack = commonFunctionsController.generateMoveHashes(information).get(1);
		
		if(canMoveWithoutAttack == null || canMoveWithoutAttack.isEmpty()) return false;
		
		ArrayList<PieceInformation> safeMovements = new ArrayList<PieceInformation>();
		
		for(IChessPiece piece : canMoveWithoutAttack.keySet()) {
			if(checkIfPieceHasCommander(piece)) {
				ArrayList<PieceInformation> data = canMoveWithoutAttack.get(piece);
				for(PieceInformation info : data) {
					boolean completelySafe = true;
					// Check all surrounding pieces to the movement site and see if there are pieces there
					for(int x = 0; x != 8; x++) {
						int xOffset = offsets[x][0];
						int zOffset = offsets[x][1];
						Location loc = new Location(info.getLocation().getX() + xOffset, info.getLocation().getZ() + zOffset);
						if(Main.getBoardController().isLocationOnBoard(loc)) {
							IChessPiece atLocation = Main.getBoardController().getPieceAtLocation(loc);
							if(atLocation != null) {
								if(!atLocation.getTeamType().equals(piece.getTeamType())) {
									completelySafe = false;
								}
							}
						}
					}
					
					// If completely safe
					if(completelySafe) {
						safeMovements.add(info);
					}
					
					
				}
			}

		}
		
		if(safeMovements == null || safeMovements.isEmpty()) return false;
		
		ArrayList<PieceInformation> checks = (ArrayList<PieceInformation>) safeMovements.clone();
		
		if(safeMovements.size() > 1) {
			for(PieceInformation nextMove : safeMovements) {
				if(nextMove.getPiece() instanceof KingPiece) {
					checks.remove(nextMove);
				}
			}
		}
		
		safeMovements = checks;
		
		// Pick a random value from the array list
		 int rnd = new Random().nextInt(safeMovements.size());
		 PieceInformation moveMe = safeMovements.get(rnd);
		 
		 movePieceNotAttacking(moveMe.getLocation(), moveMe.getPiece());
		
		return true;
	}
	
	private void knightSpecialMove(SimulatedMove simulatedMove, int randomNumber) {
		randomNumber = randomNumber - 1;
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Main.getBoardController().getLogs().addLog(simulatedMove.getPiece().getTeamType() + " " + simulatedMove.getPiece().getTexture().getPieceTextureName() + " at location (" + simulatedMove.getPiece().getLocation().getX() + 
				", " + simulatedMove.getPiece().getLocation().getZ() + ") performed special move");
		
		Main.getBoardController().getLogs().addLog("Rolled random number: " + randomNumber);
		if(canDoMove(randomNumber, simulatedMove.getProbablity())) {
			controller.getBoardController().removePieceFromBoard(simulatedMove.getEnemyPiece());
			controller.getBoardController().movePieceOnBoard(simulatedMove.getPiece(), simulatedMove.getEnemyPiece().getLocation());
			System.out.println("Knight Special: Moved piece: " + simulatedMove.getPiece().getTexture().getTextureLocation() + ", random number: " + randomNumber);
			Main.getBoardController().getLogs().addLog("Knight special move success!");
		}else {
			Main.getBoardController().getLogs().addLog("Knight special move failed!");
			Main.getBoardController().getLogs().addLog("Failed to move: Knight " + getKillProbablityToString(simulatedMove.getProbablity()) + ", Rolled: " + randomNumber);
			System.out.println("Knight Special: Unable to move piece: " + simulatedMove.getPiece().getTexture().getTextureLocation() + ", random number: " + randomNumber + 
					", (" + simulatedMove.getEnemyPiece().getLocation().getX() + ", " + simulatedMove.getEnemyPiece().getLocation().getZ() + ")");
		}
	}
	
	private void movePiece(PieceInformation bestMove, Location moveLocation, int randomNumber) {
		IChessPiece commander = controller.getTeam().getCommanderLogic().getCommanderForPiece(bestMove.getPiece());
		controller.getTeam().getCommanderLogic().move(commander);
		
		IChessPiece pieceAtMoveLocation = controller.getBoardController().getPieceAtLocation(bestMove.getLocation());
		
		if(pieceAtMoveLocation == null) {
			controller.getBoardController().movePieceOnBoard(bestMove.getPiece(), bestMove.getLocation());
			return;
		}
		
		Main.getBoardController().getLogs().addLog("Rolled random number: " + randomNumber);
		if(canDoMove(randomNumber, bestMove.getKillProbality())) {
			controller.getBoardController().removePieceFromBoard(pieceAtMoveLocation);
			controller.getBoardController().movePieceOnBoard(bestMove.getPiece(), moveLocation);
		}else {
			Main.getBoardController().getLogs().addLog("Failed to move: " + bestMove.getPiece().getTexture().getPieceTextureName() + " " + getKillProbablityToString(bestMove.getKillProbality()) + ", rolled number: " + randomNumber);
		}
	}
	
	private boolean checkIfPieceHasCommander(IChessPiece piece) {
		IChessPiece commander = controller.getTeam().getCommanderLogic().getCommanderForPiece(piece);
		
		if(commander == null) {
			return false;
		}
		
		return true;
	}
	
	private void movePieceNotAttacking(Location moveLocation, IChessPiece movedPiece) {
		if(Main.getBoardController().getPieceAtLocation(moveLocation) != null) {
			System.out.println("Error! In Movement AI");
			return;
		}
		
		IChessPiece commander = controller.getTeam().getCommanderLogic().getCommanderForPiece(movedPiece);
		controller.getTeam().getCommanderLogic().move(commander);
		
		controller.getBoardController().movePieceOnBoard(movedPiece, moveLocation);
		System.out.println("Moved piece: " + movedPiece.getTexture().getPieceTextureName());
		
	}
	
	private boolean canDoMove(int randomNumber, int[] probablity) {		
		if(probablity == null) {
			System.out.println("Null Probability");
			return true;
		}
		
		System.out.print("Needed Probablity : {");
		for(int x : probablity) {
			System.out.print(x + ",");
		}
		
		System.out.println("}");
		
		for(int x : probablity) {
			if(x == randomNumber) {
				return true;	
			}
		}
		return false;
	}
	
	
	private String getKillProbablityToString(int[] prob) {
		
		String t = "{";
		
		for(int x = 0; x != prob.length; x++) {
			t = t + prob[x] + ", ";
		}
		
		t = t + "}";
		
		return t;
	}
}
