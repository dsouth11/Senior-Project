package com.project.Render;

import java.awt.event.MouseAdapter;

import javax.swing.JLabel;

import com.project.BoardController.Location;
import com.project.ChessPieces.IChessPiece;

// Object that holds information of the label/texture of the next move.
// When you press on a piece, points of touching appear on the board. You press those to interact and move the piece.
public class NextMoveObject {

	private JLabel label; // The next move label itself (blue or green square)
	
	private Location coords; // The coords of the nextmove label
	
	private IChessPiece piece; // The piece that is about to move
	
	private boolean isPieceRendered; // True is there is a piece at the next moved render location
	
	private MouseAdapter adapter;
	
	// Default constructor for next moves
	public NextMoveObject(IChessPiece piece, JLabel label, Location location, boolean isPieceRendered, MouseAdapter adapter) {
		this.isPieceRendered = isPieceRendered;
		this.piece = piece;
		this.label = label;
		this.coords = location;
		this.adapter = adapter;
	}
	
	// Gets the label of the next move location
	public JLabel getLabel() {
		return label;
	}
	
	// Get the location the next move is pointing towards
	public Location getLocation() {
		return coords;
	}
	
	// Get the piece that the next move is designed for
	public IChessPiece getPiece() {
		return piece;
	}
	
	// Check if the object points to a piece at a location (this is the green ones)
	public boolean isPieceRendered() {
		return isPieceRendered;
	}
	
	// Gets the click event handler specified when the nextmove is created
	public MouseAdapter getMouseAdapter() {
		return adapter;
	}
}
