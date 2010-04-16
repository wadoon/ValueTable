package weigl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import weigl.gui.swingconsole.CommandListener;
import weigl.gui.swingconsole.ConsolePane;
import weigl.valtab.Parser;
import weigl.valtab.SyntaxTree;

public class Gui extends JApplet implements CommandListener {
	private int c;

	public void init() {
		setLayout(new BorderLayout());
		add(create());
	}

	public JComponent create() {
		ConsolePane console = new ConsolePane(
				"ValueTable -- Alexander Weigl -- weigla@fh-trier.de \n\n",
				this);
		console.setPrompt1("formular> ");
		c=console.getPrompt1().length();
		console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		console.setForeground(Color.GREEN);
		console.setBackground(Color.BLACK);
		console.setCaretColor(Color.green);
		console.getCaret().setBlinkRate(0);
		JScrollPane scroll = new JScrollPane(console);
		scroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scroll;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new Gui().create());
		frame.setSize(500, 250);
		frame.setVisible(true);
	}

	@Override
	public Reader commandEntered(String line) {
		String s;
		try {
			s = run(line);
		} catch (ParseException e) {
			s = generror(e);
		}
		return new StringReader(s);
	}

	private String generror(ParseException e) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < e.getErrorOffset() + c; i++)
			sb.append(' ');
		return sb.append("^\n").append(e.getMessage()).append("\n").toString();
	}

	public String run(String line) throws ParseException {
		// read a valid input
		Parser p = new Parser();
		p.run(line);
		SyntaxTree stx = new SyntaxTree(p.getParseTree());
		ValueTable vt = ValueTable.createTable(stx);
		return vt.toString() + "\n DNF:" + NormalForm.createDisjunct(vt)
				+ "\n KNF:" + NormalForm.createConjunct(vt) + "\n";
	}

}