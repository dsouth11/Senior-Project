package com.project.Delegation;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

import com.project.ChessPieces.BishopPiece;
import com.project.ChessPieces.IChessPiece;
import com.project.Main.Main;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DelegationControl {

	private JFrame frmDelegation;
	
	private IChessPiece delegator;
	
	public JLabel information;
	
	
	public DelegationControl(IChessPiece delegator) {
		this.delegator = delegator;
		if(Main.getBoardController().getDelegationController() != null) {
			Main.getBoardController().getDelegationController().frmDelegation.dispose();
		}
		initialize();
		frmDelegation.setVisible(true);
		Main.getBoardController().delegationController = this;
	}

	private void initialize() {
		frmDelegation = new JFrame();
		frmDelegation.setTitle("Delegation Command Center");
		frmDelegation.setBounds(100, 100, 478, 349);
		frmDelegation.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDelegation.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Select Piece To Delegate");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.GREEN);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(10, 11, 413, 127);
		frmDelegation.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Cancelled delegation");
				DelegationControl controller = Main.getBoardController().getDelegationController();
				if(controller != null) {
					controller.frmDelegation.dispose();
					Main.getBoardController().delegationController = null;
				}
			}
		});
		btnNewButton.setBounds(174, 204, 89, 23);
		frmDelegation.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Status:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(38, 149, 72, 32);
		frmDelegation.getContentPane().add(lblNewLabel_1);
		
		information = new JLabel("null");
		information.setBounds(109, 155, 248, 25);
		frmDelegation.getContentPane().add(information);
	}
	
	public void delegationPerformed(IChessPiece piece) {
		BishopPiece bishop = (BishopPiece)delegator;
		
		boolean alreadyContains = bishop.getDelegatedPieces().contains(piece);
		
		if(alreadyContains) {
			Main.getBoardController().getLogs().addLog(Main.getBoardController().getCurrentPlayerToMove().toString() + " gave king back authority of " + piece.getTexture().getPieceTextureName() + " at location " + piece.getLocation().toString());
			bishop.removeDelegatedPiece(piece);
		}
		
		if(!alreadyContains) {
			bishop.addDelegatedPiece(piece);
			
			Main.getBoardController().getLogs().addLog(Main.getBoardController().getCurrentPlayerToMove().toString() + " team performed delegation of " + piece.getTexture().getPieceTextureName() + " at location " + piece.getLocation().toString());
		}
		
		frmDelegation.dispose();
		Main.getBoardController().delegationController = null;
	}
	
	public void setInformation(String info) {
		information.setText(info);
		frmDelegation.repaint();
	}
}