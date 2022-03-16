package com.project.Render;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.project.BoardController.GameType;
import com.project.BoardController.Location;
import com.project.ChessPieces.BishopPiece;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.KingPiece;
import com.project.ChessPieces.KnightPiece;
import com.project.ChessPieces.PawnPiece;
import com.project.ChessPieces.QueenPiece;
import com.project.ChessPieces.RookPiece;
import com.project.Delegation.DelegationControl;
import com.project.Main.Main;
import com.project.TeamController.Team;
import com.project.TeamController.TeamType;

// Object that holds all the textures of the piece

// Also adds movement handler for the pieces


public class PiecesTexture {

	private String textureLocation; // The texture name
	
	private JLabel pieceLabel; // Gets the piece label (the piece icon you see on the board)
	
	// When the label is pressed, this will allow us to get the pressed piece easier
	private IChessPiece manipulatedPiece;
	
	private TeamType team; // The team the piece belongs too
	
	// Default constructor to create a texture of the piece
	public PiecesTexture(IChessPiece piece, int x, int z, TeamType teamType) {
		team = teamType;
		manipulatedPiece = piece;
		pieceLabel = new JLabel("");
		
		CenterPointManager point = new CenterPointManager();
		Location location = point.centerPointAlgorithm(x, z);

		String link = "";
		if(teamType.equals(TeamType.WHITE)) {
		if(piece instanceof BishopPiece) {
			link = "white_bishop.png";
		}else if(piece instanceof KingPiece) {
			link = "white_king.png";
		}else if(piece instanceof KnightPiece) {
			link = "white_knight.png";
		}else if(piece instanceof PawnPiece) {
			link = "white_pawn.png";
		}else if(piece instanceof QueenPiece) {
			link = "white_queen.png";
		}else if(piece instanceof RookPiece) {
			link = "white_rook.png";
		}
			
		}else {
			if(piece instanceof BishopPiece) {
				link = "black_bishop.png";
			}else if(piece instanceof KingPiece) {
				link = "black_king.png";
			}else if(piece instanceof KnightPiece) {
				link = "black_knight.png";
			}else if(piece instanceof PawnPiece) {
				link = "black_pawn.png";
			}else if(piece instanceof QueenPiece) {
				link = "black_queen.png";
			}else if(piece instanceof RookPiece) {
				link = "black_rook.png";
			}
		}
		
		textureLocation = link;
		pieceLabel.setIcon(new ImageIcon(getClass().getResource("resources/" + link)));
		pieceLabel.setBounds(location.getX(), location.getZ(), point.getDimensionOfPiece(), point.getDimensionOfPiece());
		
		pieceLabel.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	// Check if make sure it's the right team attempting to move
		    	if(Main.getBoardController().hasGameEnded()) {
		    		Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
		    		return;
		    	}
		    	
		    	if(!Main.getBoardController().getCurrentPlayerToMove().equals(team)) return;
		    	Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
		    	// Both players are AI, so don't let anyone touch anything
		    	if(Main.getBoardController().getCurrentGameType().equals(GameType.AI_VS_AI)) {
		    		System.out.println("Error: AI Is supposed to move, player input denied.");
		    		return;
		    	}
		    	
		    	// Black is AI don't let anyone move black unless it's the AI
		    	if(Main.getBoardController().getCurrentGameType().equals(GameType.PLAYER_VS_AI)) {
		    		if(Main.getBoardController().getCurrentPlayerToMove().equals(TeamType.BLACK)) {
		    			System.out.println("Error: AI Is supposed to move, player input denied.");
		    			return;
		    		}
		    	}
		    	
		    	
		    	Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
		    	
		    	Team teamObj = null;
		    	// Before checking if this can run, check if piece can move by commander
		    	
		    	// TODO COMMANDER LOGIC
		    	// Check if piece is allowed to move (is commander still alive)
		    	if(team.equals(TeamType.BLACK)) {
		    		teamObj = Main.getBoardController().getTeam1();
		    	}else {
		    		teamObj = Main.getBoardController().getTeam2();
		    	}
		    
		    	// Get the commander for the piece moving
		    	IChessPiece commander = teamObj.getCommanderLogic().getCommanderForPiece(manipulatedPiece);
		    	if(commander != null) {
		    		System.out.println("Commander is: " + commander.getLocation().getX() + ", " + commander.getLocation().getZ() + " - " + commander.getClass().toString());
		    	}else {
		    		System.out.println("Commander is: null");
		    	}
		    	
		    	
		    	if(e.isShiftDown()) {
		    		if(manipulatedPiece instanceof BishopPiece) {
		    			System.out.println("Opening Delegation Command Center");
		    			new DelegationControl(manipulatedPiece);
		    			return;
		    		}else {
		    			// It's another piece
		    			// Check to make sure it's a king piece
		    			
		    			if(Main.getBoardController().getDelegationController() != null) {
		    				if(initiallyBelongsToKing(manipulatedPiece)) { // Is a piece that can be delegated
		    					if(currentlyBelongsToKing(piece)) { // Check to make sure the piece is allowed to be delegated
		    						// Delegate the piece
		    						Main.getBoardController().getDelegationController().delegationPerformed(manipulatedPiece);
		    					}else {
		    						Main.getBoardController().getDelegationController().delegationPerformed(manipulatedPiece);
		    					}
		    					
		    				}else {
		    					Main.getBoardController().getDelegationController().setInformation("This piece is not allowed to be delegated.");
		    					System.out.println("This piece is not allowed to be delegated.");
		    				}
		    			}
		    			return;
		    		}
		    	}
		    	
		    	if(Main.getBoardController().getDelegationController() != null) {
		    		return;
		    	}
		    	
		    	// Check if the commander has moved yet
		    	if(!teamObj.getCommanderLogic().hasCommanderPerformedMove(commander) && commander != null) {
		    		if(!teamObj.getCommanderLogic().hasAlreadyMoved(piece)) {
		    			Main.getBoardController().getNextMoveRenderer().renderForPiece(manipulatedPiece);
		    		}
		    	}
		    	
		    }  
		}); 
		
		Main.getBoardController().getBoardObject().getFrame().add(pieceLabel);
	}
	
	private boolean initiallyBelongsToKing(IChessPiece piece) {
		if(piece instanceof QueenPiece || piece instanceof RookPiece || piece instanceof PawnPiece) {
			if(piece instanceof PawnPiece && (piece.getStartLocation().getX() == 3 || piece.getStartLocation().getX() == 4)) {
				return true;
			}
			
			if(piece instanceof QueenPiece || piece instanceof RookPiece) {
				return true;
			}
			
		}
		
		return false;
	}
	
	private boolean currentlyBelongsToKing(IChessPiece piece) {
		Team teamObj = null;
    	if(team.equals(TeamType.BLACK)) {
    		teamObj = Main.getBoardController().getTeam1();
    	}else {
    		teamObj = Main.getBoardController().getTeam2();
    	}
		
		for(IChessPiece pieces : teamObj.getChessPieces()) {
			if(pieces instanceof BishopPiece) {
				BishopPiece bishop = (BishopPiece)pieces;
				ArrayList<IChessPiece> delegated = bishop.getDelegatedPieces();
				if(delegated.contains(piece)) { // Currently in control by a bishop
					return false;
				}
				
			}
		}
		
		return true;
	}
	
	// Returns the texture name
	public String getTextureLocation() {
		return textureLocation;
	}
	
	public String getPieceTextureName() {
		return textureLocation.replace("com.project.ChessPieces.", "").replace(".png", "").replace("black_", "").replace("white_", "");
	}
	
	// Returns the piece texture label itself (the icon you see on the board)
	public JLabel getPieceLabel() {
		return pieceLabel;
	}
	
	// Removes the texture of the piece. DO NOT USE THIS. Only used for the destroy method in each pieces code.
	public void removeTextureFromBoard() {
		Main.getBoardController().getBoardObject().getFrame().remove(pieceLabel);
		Main.getBoardController().getBoardObject().getFrame().repaint();
	}
	
	// Moves the piece to specified location.
	public void moveTextureTo(Location location) {
		CenterPointManager point = new CenterPointManager();
		Location locale = point.centerPointAlgorithm(location.getX(), location.getZ());
		pieceLabel.setLocation(locale.getX(), locale.getZ());
		//Main.getBoardController().getBoardObject().getFrame().repaint();
	}
	
}