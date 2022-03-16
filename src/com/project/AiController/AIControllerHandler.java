package com.project.AiController;

import java.util.ArrayList;

import com.project.TeamController.TeamType;


// Object that handles the controllers for the different AI's
public class AIControllerHandler {

	private ArrayList<AIController> AIControllers; // All AI controllers
	
	private int speed; // The speed at which the AI will move or work
	
	// Default contructor that sets the default information on system start
	public AIControllerHandler(int speed) {
		if(speed >= 1) {
			this.speed = speed * 1000;
		}else {
			this.speed = 50;
		}
		
		AIControllers = new ArrayList<AIController>();
	}
	
	// How fast each AI will move/process information
	public int getSpeed() {
		return speed;
	}
	
	// Sets new speed the AI will move/process information
	public void setNewSpeed(int speed) {
		if(speed >= 1) {
			this.speed = speed * 1000;
		}else {
			this.speed = 10; 
		}
	}
	
	// Get all currently active AI controllers
	public ArrayList<AIController> getAIControllers() {
		return AIControllers;
	}
	
	// Clears and resets all AI controllers
	public void clear() {
		for(AIController controller : getAIControllers()) {
			controller.cancelAI();
		}
		AIControllers.clear();
	}
	
	// Added a new AI controller to the system
	public void addController(AIController controller) {
		AIControllers.add(controller);
	}
	
	public AIController getAIControllerForTeam(TeamType teamType) {
		if(AIControllers.isEmpty()) return null;
		
		for(AIController con : AIControllers) {
			if(con.getTeam().getTeamType().equals(teamType)) {
				return con;
			}
			
		}
		return null;
		
	}
}