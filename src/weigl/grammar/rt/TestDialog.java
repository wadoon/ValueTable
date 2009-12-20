package weigl.grammar.rt;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JTree;

import weigl.grammar.rt.PTree;;

public class TestDialog {
	public static void showFrame(PTree ast) {
		JFrame frame = new JFrame("AST");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTree tree = new JTree(new AstJTreeAdapter(ast));
		frame.add(tree);
		frame.pack();
		frame.setVisible(true);
	}
}
