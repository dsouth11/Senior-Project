package com.project.Render;

import java.awt.Color;
import java.awt.Font;


import com.project.Main.Main;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DiceRollerGUI {

	private JPanel frmDiceRoller;
	
	private JLabel rollingLabel;
		
	public DiceRollerGUI() {
		initialize();
		guiPaint();
	}
	
	public JLabel getRollingLabel() {
		return rollingLabel;
	}
	
	public JPanel getFrame() {
		return frmDiceRoller;
	}

	
	private void initialize() {
		frmDiceRoller = new JPanel();
		JLabel paneLabel = new JLabel("Dice Roller");
		frmDiceRoller.add(paneLabel);
		//frmDiceRoller.setTitle("Dice Roller");
		//frmDiceRoller.setBounds(100, 100, 599, 260);
		//frmDiceRoller.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmDiceRoller.setBounds(1080, 310, 200, 300);
		frmDiceRoller.setBorder(BorderFactory.createLoweredBevelBorder());
		frmDiceRoller.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		
		frmDiceRoller.setLayout(new BoxLayout(frmDiceRoller, BoxLayout.Y_AXIS));
		
		rollingLabel = new JLabel("");
		rollingLabel.setForeground(Color.BLACK);
		rollingLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		rollingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//rollingLabel.setBounds(10, 11, 545, 199);
		frmDiceRoller.remove(rollingLabel);
		frmDiceRoller.add(rollingLabel);
		
		Main.getBoardController().getBoardObject().getFrame().add(frmDiceRoller);
	}
	
	public void guiPaint() {
		Main.getBoardController().getBoardObject().getFrame().remove(frmDiceRoller);
		frmDiceRoller.repaint();
		Main.getBoardController().getBoardObject().getFrame().add(frmDiceRoller);
	}
}