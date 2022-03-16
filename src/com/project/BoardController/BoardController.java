package com.project.BoardController;

import java.awt.EventQueue;

import javax.swing.JLabel;

import com.project.AiController.AIController;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.KingPiece;
import com.project.Delegation.DelegationControl;
import com.project.Logs.Logs;
import com.project.Main.Main;
import com.project.Render.Board;
import com.project.Render.KnightSpeicalMove;
import com.project.Render.NewGameWindow;
import com.project.Render.NextMoveRenderer;
import com.project.TeamController.Team;
import com.project.TeamController.TeamType;

// Object that controllers the board and keeps the information like current player, current game type, the board object, and team information.
// Also has information related getting the location of a piece back on the grid and other game related functions.
public class BoardController {

	private GameType currentGameType; // The current game type, either: PLAYER_VS_PLAYER, PLAYER_VS_AI, or AI_VS_AI
	
	private Team team1; // Assume team 1 is black and on top
	private Team team2; // Assume team 2 is white and on bottom
	
	private TeamType currentPlayer; // The current team that is allowed to move BLACK or WHITE
	
	private Board board; // The board object
	
	private int moveCount; //Integer to count moves
	
	public boolean moved;  //Boolean that will be set to true once both teams have moved
	
	private NextMoveRenderer nextMoveRenderer; // Renderer that renders the next squares and determines different movesets the pieces can or can't do
	
	private boolean gameEnded; // true is game is over, as in someone one or is a tie
	
	private KnightSpeicalMove knightSpecialMoveGUI;
	
	private Logs logs; // Log controller

	public DelegationControl delegationController; // Delegation controller
	
	/**
	 * Default constructor that starts the game with the specified game type: PLAYER_VS_PLAYER, PLAYER_VS_AI, AI_VS_AI or SQL_MULTIPLAYER
	 * @param GameType gameType
	 */
	public BoardController(GameType gameType) {
		delegationController = null;
		knightSpecialMoveGUI = null;
		this.gameEnded = false;
		this.currentGameType = gameType;
		nextMoveRenderer = new NextMoveRenderer(); // Enable the nextMoveRenderer
		currentPlayer = TeamType.WHITE; // White always goes first. It's in the rules.
		board = new Board(); // Create the new board
		
		// Needed to prevent error of board not being created yet
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setupLogs();
				team1 = new Team(TeamType.BLACK); // Create the black team
				team2 = new Team(TeamType.WHITE); // Create the white team
			}
		});
	}
	
	/**
	 * @return Team 1 object
	 * Contains information about team color, pieces, etc.
	 */
	public Team getTeam1() {
		return team1;
	}
	
	/**
	 * @return Team 2 object.
	 * Contains information about team color, pieces, etc.
	 */
	public Team getTeam2() {
		return team2;
	}
	
	/**
	 * @param location
	 * @return piece at specified location, null if no piece exists
	 */
	public IChessPiece getPieceAtLocation(Location location) {
		for(IChessPiece piece : getTeam1().getChessPieces()) {
			if(piece.getLocation().getX() == location.getX() && piece.getLocation().getZ() == location.getZ()) {
				return piece;
			}
		}
		
		for(IChessPiece piece : getTeam2().getChessPieces()) {
			if(piece.getLocation().getX() == location.getX() && piece.getLocation().getZ() == location.getZ()) {
				return piece;
			}
		}
		return null;
	}
	
	// Returns the team the piece belongs to
	// Will return null if the piece somehow doesn't belong to a specific team
	public Team getTeamPieceBelongsTo(IChessPiece piece) {
		
		if(team1.getChessPieces().contains(piece)) {
			return team1;
		}
		
		if(team2.getChessPieces().contains(piece)) {
			return team2;
		}
		
		return null;
	}
	
	// Checks if a certain location is on the board or not
	public boolean isLocationOnBoard(Location location) {
		if(location.getX() >= 0 && location.getZ() >= 0 && location.getX() <= 7 && location.getZ() <= 7) {
			return true;
		}
		return false;
	}
	
	// Remove a piece from the board, will also remove it from the team it belongs too
	public void removePieceFromBoard(IChessPiece piece) {
		Team team = getTeamPieceBelongsTo(piece);
		if(team != null) {
			Main.getBoardController().getLogs().addLog(piece.getTeamType().toString() + " " + piece.getTexture().getPieceTextureName() + " at location: (" + piece.getLocation().getX() + ", " + piece.getLocation().getZ() + ") was destroyed!");
			piece.getTexture().removeTextureFromBoard();
			team.removePiece(piece);
		}else {
			System.out.println("Error: Unable to remove piece");
		}
	}
	
	// Moves the piece on the board to the specified location
	// Clears the next move renderer once complete
	public void movePieceOnBoard(IChessPiece piece, Location newLocation) {
		Location original = piece.getLocation();
		getNextMoveRenderer().clearCurrentRender();
		piece.getTexture().moveTextureTo(newLocation);
		moveCount++;
		if(moveCount > 1)
			moved = true;
		piece.setLocation(newLocation);
		
		Team t = null;
		if(piece.getTeamType().equals(TeamType.BLACK)) {
			getTeam1().addMove(); // Black is considered team1 but technically moves second (bad on my part)
			t = getTeam1();
		}else {
			getTeam2().addMove();
			t = getTeam2();
		}
		
		t.getCommanderLogic().addMovedThisTurn(piece);
		
		
		Main.getBoardController().getLogs().addLog(piece.getTeamType().toString() + " Moved " + piece.getTexture().getPieceTextureName() + ": (" + original.getX() + ", " + original.getZ() + ") -> (" + newLocation.getX() + ", " + newLocation.getZ() + ")");
		getBoardObject().getFrame().repaint();
	}
	
	// Returns the board object
	public Board getBoardObject() {
		return board;
	}
	
	// Returns the nextMoveRenderer object
	public NextMoveRenderer getNextMoveRenderer() {
		return nextMoveRenderer;
	}

	// Returns the current player who is allowed to move
	public TeamType getCurrentPlayerToMove() {
		return currentPlayer;
	}
	
	// Returns current game type
	public GameType getCurrentGameType() {
		return currentGameType;
	}
	
	// Set the next player to move
	public void setNextPlayerToMove() {
		getBoardObject().getFrame().repaint();
		Main.getBoardController().getTeam1().getCommanderLogic().reset();
		Main.getBoardController().getTeam2().getCommanderLogic().reset();
		
		if(currentPlayer.equals(TeamType.WHITE)) {
			currentPlayer = TeamType.BLACK;
		}else {
			currentPlayer = TeamType.WHITE;
		}
		
		Main.getBoardController().getLogs().addLog("Current player moving now: " + currentPlayer.toString());
		
		
		// This game needs an AI in it
		if(Main.getBoardController().getCurrentGameType().equals(GameType.PLAYER_VS_AI) || Main.getBoardController().getCurrentGameType().equals(GameType.AI_VS_AI)) {
			// Run the AI move
			AIController controller = Main.getAIController().getAIControllerForTeam(currentPlayer);
			if(controller != null) {
				controller.runMove();
			}
		}
	}
	
	// Check if the game is finished
	public void checkForGameFinished() {
		
		if(gameEnded) return;
		
		getBoardObject().getFrame().repaint();
		// Locate the king piece for each team
		
		boolean team1HasKing = false;
		boolean team2HasKing = false;
		
		for (IChessPiece piece : getTeam1().getChessPieces()) {
			if(piece instanceof KingPiece) {
				// We found a king piece for this team, so this team is still alive
				team1HasKing = true;
			}
		}
		
		
		for (IChessPiece piece : getTeam2().getChessPieces()) {
			if(piece instanceof KingPiece) {
				// We found a king piece for this team, so this team is still alive
				team2HasKing = true;
			}
		}
		
		if(team2HasKing && team1HasKing) {
			// Both teams have just the king left somehow
			if(getTeam1().getChessPieces().size() == 1 && getTeam2().getChessPieces().size() == 1) {
				endGame(EndGameStatus.TIE);
				return;
			}
		}
		
		if(team1HasKing && !team2HasKing) {
			// Team 1 won
			endGame(EndGameStatus.TEAM1_WON);
			return;
		}
		
		if(team2HasKing && !team1HasKing) {
			// Team 2 won
			endGame(EndGameStatus.TEAM2_WON);
			return;
		}
		
		// Check if a certain team only has the king left
		
		if(team1HasKing && getTeam1().getChessPieces().size() == 1 && getTeam2().getChessPieces().size() >= 2) {
			endGame(EndGameStatus.TEAM2_WON);
			return;
		}
		
		if(team2HasKing && getTeam2().getChessPieces().size() == 1 && getTeam1().getChessPieces().size() >= 2) {
			endGame(EndGameStatus.TEAM1_WON);
			return;
		}
		
	}
	
	// End the game with specified end game status
	public void endGame(EndGameStatus status) {
		new NewGameWindow();
		gameEnded = true;
		
		if(status.equals(EndGameStatus.TIE)) {
			Main.getNotificationHandler().sendNotificationMessage("Game Over","It was a tie! No one won the game!");
			Main.getBoardController().getLogs().addLog("Game Over! It was a tie! No one won the game!");
		}else {
			if(status.equals(EndGameStatus.TEAM1_WON)) {
				Main.getNotificationHandler().sendNotificationMessage("Game Over", "Black Team won the game!");
				Main.getBoardController().getLogs().addLog("Game Over! Black Team won the game!");
			}else {
				Main.getNotificationHandler().sendNotificationMessage("Game Over", "White Team won the game!");
				Main.getBoardController().getLogs().addLog("Game Over! White Team won the game!");
			}
		}
	}
	
	public boolean hasGameEnded() {
		return gameEnded;
	}
	
	public KnightSpeicalMove getKnightSpecialMoveGUI() {
		return knightSpecialMoveGUI;
	}
	
	public void setKnightSpecialMoveGUI(KnightSpeicalMove move) {
		this.knightSpecialMoveGUI = move;
	}
	
	public void resetKnightSpecialMoveGUI() {
		this.knightSpecialMoveGUI = null;
	}
	
	public Logs getLogs() {
		return logs;
	}
	
	public DelegationControl getDelegationController() {
		return delegationController;
	}
	
	private void setupLogs() {
		this.logs = new Logs();
	}
	
	
}