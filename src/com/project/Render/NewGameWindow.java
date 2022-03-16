package com.project.Render;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.project.BoardController.GameType;
import com.project.Main.Main;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewGameWindow {

	private JFrame frmNewGame;
	
	public NewGameWindow() {
		initialize();
		frmNewGame.setVisible(true);
	}
	
	private void initialize() {
		frmNewGame = new JFrame();
		frmNewGame.setTitle("New Game?");
		frmNewGame.setBounds(100, 100, 303, 269);
		frmNewGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNewGame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Would You Like To Restart The Game?");
		lblNewLabel.setBounds(51, 11, 239, 14);
		frmNewGame.getContentPane().add(lblNewLabel);
		
		JButton yesButton = new JButton("Yes");
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Restart the game
				if(!Main.getBoardController().getCurrentGameType().equals(GameType.SQL_MULTIPLAYER)) {
					Main.createNewGame(Main.getBoardController().getCurrentGameType());
					frmNewGame.dispose();
				}else {
					Main.getNotificationHandler().sendNotificationMessage("Mutliplayer Handler", "Sent restart game request...");
					frmNewGame.dispose();
				}
				
			}
		});
		yesButton.setForeground(Color.BLACK);
		yesButton.setBackground(Color.GREEN);
		yesButton.setBounds(27, 78, 110, 54);
		frmNewGame.getContentPane().add(yesButton);
		
		JButton noButton = new JButton("No");
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Don't restart the game
				frmNewGame.dispose();
				Main.createNewGame(GameType.PLAYER_VS_PLAYER);
			}
		});
		noButton.setForeground(Color.BLACK);
		noButton.setBackground(Color.RED);
		noButton.setBounds(147, 78, 110, 54);
		frmNewGame.getContentPane().add(noButton);
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getBoardController().getBoardObject().getFrame().dispose();
				frmNewGame.dispose();
				System.exit(0);
				return;
			}
		});
		quitButton.setForeground(Color.BLACK);
		quitButton.setBackground(Color.ORANGE);
		quitButton.setBounds(93, 143, 110, 54);
		frmNewGame.getContentPane().add(quitButton);
	}

}
