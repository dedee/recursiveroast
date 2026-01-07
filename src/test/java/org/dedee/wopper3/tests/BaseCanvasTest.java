package org.dedee.wopper3.tests;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.GeneralPath;
import java.io.Serial;

import org.dedee.wopper3.ui.BaseCanvas;

public class BaseCanvasTest extends BaseCanvas {
	@Serial
	private static final long serialVersionUID = 909157673777723799L;

	public BaseCanvasTest() {
	}

	@Override
	public void paint(GeneralPath gp) {
		gp.lineTo(10, 10);
		gp.lineTo(20, 10);
		gp.lineTo(30, 0);
		gp.lineTo(10, 40);
		gp.lineTo(-10, -40);
	}

	public static void main(String[] args) {
		Frame f = new Frame("TurtleGraphics");
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		BaseCanvasTest canvas = new BaseCanvasTest();
		f.add(canvas, BorderLayout.CENTER);
		f.setSize(200, 200);
		f.setVisible(true);
	}

	@Override
	public void paintLSystemText(Graphics2D g2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintRecursionLevel(Graphics2D g2) {
		// TODO Auto-generated method stub

	}

}
