package com.project.Logs;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.project.Main.Main;

public class Logs {

	private JTextArea area;
	
	private ArrayList<String> lines;
	
	public Logs() {
		lines = new ArrayList<String>();
		area = new JTextArea();
		area.setBounds(710, 10, 600, 720);
		area.setEditable(false);
		
		
		JScrollPane sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBounds(710, 10, 370, 670);
		
		Main.getBoardController().getBoardObject().getFrame().add(sp);
		
		addLog("-----------------------------------------Logger-----------------------------------------");
		
	}
	
	public void addLog(String log) {
		lines.add(log);
		
		String all = "";
		for(String a : lines) {
			if(all == "") {
				all = a;
			}else {
				all = all + "\n" + a;
			}
		}
		
		area.setText(all);
		Main.getBoardController().getBoardObject().getFrame().repaint();
	}
}