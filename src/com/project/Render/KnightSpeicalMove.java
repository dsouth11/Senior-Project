package com.project.Render;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.project.BoardController.Location;
import com.project.ChessPieces.IChessPiece;
import com.project.ChessPieces.KnightPiece;
import com.project.ChessPieces.ProbabilityController;
import com.project.Main.Main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class KnightSpeicalMove {

	private JFrame frmKnightSpecialMove;
	
	private IChessPiece piece;
	
	private boolean alreadyRender = false;
	
	public KnightSpeicalMove(IChessPiece knight) {
		this.piece = knight;
		initialize();
		frmKnightSpecialMove.setVisible(true);
	}

	private void initialize() {
		frmKnightSpecialMove = new JFrame();
		frmKnightSpecialMove.setTitle("Knight Special Move");
		frmKnightSpecialMove.setBounds(100, 100, 380, 227);
		frmKnightSpecialMove.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmKnightSpecialMove.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Do Knight Special Move?");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(87, 11, 249, 34);
		frmKnightSpecialMove.getContentPane().add(lblNewLabel);
		
		JButton yesButton = new JButton("Yes");
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!alreadyRender) {
					System.out.println("Attempting special move");
					
					KnightPiece n = (KnightPiece)piece;
					ArrayList<Location> attacks = n.getAttackLocations();
					renderSpecialMove(attacks);
				}
				alreadyRender = true;
			}
		});
		yesButton.setBounds(25, 122, 138, 45);
		frmKnightSpecialMove.getContentPane().add(yesButton);
		
		JButton noButton = new JButton("No");
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(labels != null) {
					clearCurrentRender();
				}
				Main.getBoardController().getLogs().addLog(piece.getTeamType() + " " + piece.getTexture().getPieceTextureName() + " at location (" + piece.getLocation().getX() + ", " + piece.getLocation().getZ() + ") declined special move");
				frmKnightSpecialMove.dispose();
				Main.getBoardController().resetKnightSpecialMoveGUI();
				Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
			}
		});
		noButton.setBounds(204, 122, 138, 45);
		frmKnightSpecialMove.getContentPane().add(noButton);
		
		
		Main.getBoardController().setKnightSpecialMoveGUI(this);
	}
	
	private ArrayList<NextMoveObject> labels;
	
	private void renderSpecialMove(ArrayList<Location> moves) {
		labels = new ArrayList<NextMoveObject>();
		CenterPointManager center = new CenterPointManager();
		
		// Only a single move
		if(moves.size() == 1) {}
		
		for(Location location : moves) {
			boolean isPieceRendered = false;
			Location point = center.centerPointAlgorithm(location.getX(), location.getZ());
			JLabel pieceLabel = null;
			// Check if location has a piece at it currently
			IChessPiece pieceAt = Main.getBoardController().getPieceAtLocation(location);
			if(pieceAt != null) {
				isPieceRendered = true;
				pieceLabel = pieceAt.getTexture().getPieceLabel();
				
				Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
				
				// ADD THE ROLL INFORMATION
				
				
				ProbabilityController probability = new ProbabilityController();
				int[] values = probability.getProbablity(pieceAt, piece);
				
				String text = "";
				for(int x = 0; x != values.length; x++) {
					if(x != 0) {
						text = text + ", " + values[x];
					}else {
						text = text + values[x];
					}
				}
				
				pieceLabel.setBorder(border);
				
			}else {
				pieceLabel = new JLabel("");
				pieceLabel.setBounds(point.getX(), point.getZ(), center.getDimensionOfPiece(), center.getDimensionOfPiece());
				
				Border border = BorderFactory.createLineBorder(Color.BLUE, 3);
				pieceLabel.setBorder(border);
			}
			Main.getBoardController().getBoardObject().getFrame().repaint();
			
			MouseAdapter mouseListener = new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			    	if(Main.getBoardController().hasGameEnded()) {
			    		Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
			    		return;
			    	}
			    	
			    	// Move the object to here
			    	for(NextMoveObject obj : labels) {
			    		if(obj.getLabel().equals(e.getComponent())) {
			    			// Check if there is a piece there
			    			Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
			    			IChessPiece piece1 = Main.getBoardController().getPieceAtLocation(obj.getLocation());
			    			
			    			// TODO ADD FUZZY LOGIC HERE!
			    			if(piece1 != null) { // A piece is here
			    				DiceRollerGUI gui = new DiceRollerGUI();
			    				ProbabilityController prob = new ProbabilityController();
			    				gui.getFrame().repaint();
			    				int rand = prob.getRandomNumber(true);
			    				if(rand != 1) {
			    					rand = rand - 1;
			    				}
			    				
			    				int[] valuesNeeded = prob.getProbablity(piece, piece1);
			    				
			    				String temp = "";
			    				for(int x = 0; x != valuesNeeded.length; x++) {
			    					temp = temp + " " + valuesNeeded[x];
			    				}
			    				System.out.println("Probabilty needed:" + temp);
			    				
			    				try {
									Thread.sleep(2500);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
			    				
			    				boolean success = false;
			    				for(int x = 0; x != valuesNeeded.length; x++) {
			    					if(rand == valuesNeeded[x]) {
			    						success = true;
			    					}
			    				}
			    				
			    				Main.getBoardController().getLogs().addLog(piece.getTeamType() + " " + piece.getTexture().getPieceTextureName() + " at location (" + piece.getLocation().getX() + ", " + piece.getLocation().getZ() + ") performed special move");
			    				if(success) {
			    					Main.getBoardController().getLogs().addLog("Knight special move success!");
			    					gui.getRollingLabel().setForeground(Color.GREEN);
			    					gui.getRollingLabel().setText("Rolled a " + rand + " (Success!)");
			    					gui.guiPaint();
			    					gui.getRollingLabel().repaint();
			    					
			    					Main.getBoardController().movePieceOnBoard(piece, obj.getLocation());
				    				Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
				    				clearCurrentRender();
				    				
				    				piece1.destroyPiece();
			    					Main.getBoardController().checkForGameFinished();
			    					
			    					frmKnightSpecialMove.dispose();
			    					Main.getBoardController().resetKnightSpecialMoveGUI();
			    					
			    				}else {
			    					Main.getBoardController().getLogs().addLog("Knight special move failed!");
			    					gui.getRollingLabel().setForeground(Color.RED);
			    					gui.getRollingLabel().setText("Rolled a " + rand + " (Missed!)");
			    					gui.guiPaint();
			    					gui.getRollingLabel().repaint();
			    					Main.getBoardController().getLogs().addLog(piece.getTeamType().toString() + " " + piece.getTexture().getPieceTextureName() + " failed to attack opponent at location (" + obj.getLocation().getX() + ", " + obj.getLocation().getZ() + "), " + 
			    							", " + getKillProbablityToString(valuesNeeded) + ", rolled: " + rand);
			    					Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
			    					clearCurrentRender();
			    					
			    					frmKnightSpecialMove.dispose();
			    					Main.getBoardController().resetKnightSpecialMoveGUI();
			    				}
			    				Main.getBoardController().getBoardObject().getFrame().repaint();
			    				gui.guiPaint();
			    				gui.getFrame().repaint();
			    				
			    			}
			    			
			    			break;
			    		}
			    	}
			    }
			}; 
			
			pieceLabel.addMouseListener(mouseListener);
			
			labels.add(new NextMoveObject(piece, pieceLabel, location, isPieceRendered, mouseListener));
			if(!isPieceRendered) {
				Main.getBoardController().getBoardObject().getFrame().add(pieceLabel);
			}
			    Main.getBoardController().getBoardObject().getFrame().repaint();
		}
		
	}
	
	// Remove all current renders
	private void clearCurrentRender() {
		for(NextMoveObject obj : labels) {
			if(!obj.isPieceRendered()) {
				obj.getLabel().setBorder(null);
				Main.getBoardController().getBoardObject().getFrame().remove(obj.getLabel());
			}else {
				// It's a rendered piece, remove the border
				obj.getLabel().setBorder(null);
				obj.getLabel().removeMouseListener(obj.getMouseAdapter());
			}
		}
		labels.clear();
		Main.getBoardController().getBoardObject().getFrame().repaint();
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
