package com.project.ChessPieces;

import java.util.ArrayList;

import com.project.BoardController.Location;
import com.project.Render.PiecesTexture;
import com.project.TeamController.TeamType;

// Interface that defines the chess pieces
public interface IChessPiece {

	public boolean hasMovedAlready(); // This is to check if the piece has moved once already
	
	public void setHasMovedOnce(); // Set the piece to have moved once already
	
	public boolean isAlive(); // Set to true when the piece is still alive, false if it has died
	
	public void destroyPiece(); // Removes the piece from the board
	
	public Location getLocation(); // Gets the current location of the piece (0,0) -> (7,7)
	
	public void setLocation(int x, int z); // Sets the location of the piece (0,0) -> (7,7)
	
	public void setLocation(Location location); // Sets the location of the piece with a location object. (0,0) -> (7,7)

	public PiecesTexture getTexture(); // Returns the texture information of the piece, has piece image information and file data
	
	public ArrayList<Location> getPossibleMoves(); // Algorithms that determine where the piece can move next
	
	public TeamType getTeamType();
	
	public Location getStartLocation();
	
}