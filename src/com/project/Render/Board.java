package com.project.Render;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;

import com.project.BoardController.GameType;
import com.project.Main.Main;
import com.project.Render.DiceRollerGUI;

import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.event.ActionEvent;

// Board object, has all the information about the frame 

public class Board {

	private JFrame frmChess; // Chess board frame
	private JPanel btnArea;
	
	// Start chess board frame
	public Board() {
		initialize();
		frmChess.setVisible(true);
	}

	
	// Creates the board
	// Auto render piece sizes to bounds: x, y, 60, 60
	private void initialize() {
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(getClass().getResource("resources/standard-walnut-chess-board-21184254145_1024x1024.jpg")));
		lblNewLabel.setBounds(0, 0, 716, 723);
		
		//Adding in a new Panel for Buttons
		btnArea = new JPanel();
		btnArea.setLayout(new BoxLayout(btnArea, BoxLayout.Y_AXIS));
		
		frmChess = new JFrame();
		frmChess.setContentPane(lblNewLabel);
		btnArea.setBounds(1080, 10, 200, 300);
		btnArea.setBorder(BorderFactory.createLoweredBevelBorder());
		frmChess.setResizable(false);
		frmChess.setTitle("Chess");
		frmChess.setBounds(100, 100, 1300, 755);
		frmChess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChess.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		frmChess.setJMenuBar(menuBar);
		frmChess.add(btnArea);
		
		JLabel nMenu = new JLabel("Game Options");
		nMenu.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(nMenu);
		
		JLabel nMenu2 = new JLabel("This will restart the game");
		nMenu2.setFont(new Font("Arial", Font.ITALIC, 10));
		nMenu2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(nMenu2);
		
		JButton pvpBtn = new JButton("Player v Player");
		pvpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.createNewGame(GameType.PLAYER_VS_PLAYER);
			}
		});
		pvpBtn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(pvpBtn);
		
		JButton pvaiBtn = new JButton("Player v AI");
		pvaiBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.createNewGame(GameType.PLAYER_VS_AI);
			}
		});
		pvaiBtn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(pvaiBtn);
		
		JButton aivaiBtn = new JButton("AI v AI");
		aivaiBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.createNewGame(GameType.AI_VS_AI);
			}
		});
		aivaiBtn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(aivaiBtn);
		
		JLabel turnMgmt = new JLabel("Turn Management");
		turnMgmt.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(turnMgmt);
		
		JButton endTurn = new JButton("End Turn");
		endTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Skip the persons turn
				Main.getBoardController().getLogs().addLog(Main.getBoardController().getCurrentPlayerToMove().toString() + " their skipped turn.");
				Main.getBoardController().getNextMoveRenderer().clearCurrentRender();
				Main.getBoardController().setNextPlayerToMove();
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Successfully Skipped Turn!");
				Main.getBoardController().getTeam1().getCommanderLogic().reset();
				Main.getBoardController().getTeam2().getCommanderLogic().reset();
			}
		});
		endTurn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		btnArea.add(endTurn);
		
		//DiceRollerGUI diceStuff = new DiceRollerGUI();
		
		//JPanel diceArea = diceStuff.getFrame();
		//diceArea.setBounds(1080, 310, 200, 300);
		//diceArea.setBorder(BorderFactory.createLoweredBevelBorder());
		//diceArea.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		//frmChess.add(diceArea);
				
		JMenu mnNewMenu_3 = new JMenu("AI Process Speed");
		menuBar.add(mnNewMenu_3);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("5 seconds");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Changed AI speed to 5 seconds.");
				Main.getAIController().setNewSpeed(5);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("4 seconds");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Changed AI speed to 4 seconds.");
				Main.getAIController().setNewSpeed(4);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("3 seconds");
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Changed AI speed to 3 seconds.");
				Main.getAIController().setNewSpeed(3);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_7);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("2 seconds");
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Changed AI speed to 2 seconds.");
				Main.getAIController().setNewSpeed(2);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_8);
		
		JMenuItem mntmNewMenuItem_9 = new JMenuItem("1 second");
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Changed AI speed to 1 second.");
				Main.getAIController().setNewSpeed(1);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_9);
		
		JMenuItem mntmNewMenuItem_10 = new JMenuItem("Instant");
		mntmNewMenuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Changed AI speed to instant.");
				Main.getAIController().setNewSpeed(0);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_10);
		
		JMenu reloadBoard = new JMenu("Reload Board");
		menuBar.add(reloadBoard);
		
		JMenuItem reloadBoardRequester = new JMenuItem("Reload");
		reloadBoardRequester.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getBoardController().getBoardObject().getFrame().repaint();
				Main.getNotificationHandler().sendNotificationMessage("Chess Game", "Reloaded Board");
			}
		});
		reloadBoard.add(reloadBoardRequester);
		
		JMenu mnNewMenu_1 = new JMenu("Force AI Move");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Force AI Move If Stuck");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Main.getBoardController().getCurrentGameType().equals(GameType.AI_VS_AI) || Main.getBoardController().getCurrentGameType().equals(GameType.PLAYER_VS_AI)) {
					Main.getAIController().getAIControllerForTeam(Main.getBoardController().getCurrentPlayerToMove()).runMove();
					
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem);
	}
	
	public JFrame getFrame() {
		return frmChess;
	}
}
