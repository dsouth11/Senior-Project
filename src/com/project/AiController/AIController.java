package com.project.AiController;

import javax.swing.JFrame;

import com.project.BoardController.BoardController;
import com.project.Main.Main;
import com.project.TeamController.Team;

// Object that handles the AI for the specified team
public class AIController {
	
	private Team team; // Gets the team the AI is controlling
	
	private JFrame frame; // Gets the frame the AI is interacting with
	
	private boolean cancel; // Is this ai controller active or not. true if canceled/inactive
	
	private Team enemyTeam; // Enemy team
	
	// Create a new AI controller for the specified team
	public AIController(Team team, Team enemyTeam, JFrame frame) {
		this.team = team;
		this.frame = frame;
		this.enemyTeam = enemyTeam;
		cancel = false;
	}
	
	// Gets the team the AI Controller is controlling
	public Team getTeam() {
		return team;
	}
	
	// Gets the enemey team, the team the AI is versing
	public Team getEnemyTeam() {
		return enemyTeam;
	}
	
	// Get the frame the AI is responsible for
	public JFrame getAIFrame() {
		return frame;
	}
	
	public void runMove() {
		Main.getBoardController().checkForGameFinished();
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	if(team.getCommanderLogic().getMoveCount() > team.getCommanderLogic().getMaxMoves()) {
		        			Main.getBoardController().setNextPlayerToMove();
		        			return;
		        		}
		            	
		            	if(cancel) return; // If the AI was canceled, don't run AI code
		            	if(!getAIFrame().isActive() || !getAIFrame().isDisplayable()) return; // If the frame is over, cancel it
		            	if(Main.getBoardController().hasGameEnded()) return; // Cancel if the game is over
		            	// Run the AI
		            	System.out.println("Running move AI Move");
		            	preformAIMove();
		            }
		            	
		        }, Main.getAIController().getSpeed() // How fast it should run
		);
	}
	
	// Preforms AI movement based on all requirements, this is called from runAIHandler() ONLY
	private void preformAIMove() {
		if(Main.getBoardController().getCurrentPlayerToMove().equals(team.getTeamType())) {
			new AI(this);
			runMove();
		}
		
	}
	
	public BoardController getBoardController() {
		return Main.getBoardController();
	}
	
	
	// Cancels the AI from working
	public void cancelAI() {
		cancel = true;
	}
	
}