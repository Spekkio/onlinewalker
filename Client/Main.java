import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

class Main
{

    public static void main(String[] argv)
    {
	JFrame win = new JFrame();
	win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	win.setContentPane(new GrafikYta());
	//win.setContentPane(new ChatBox());
	win.setSize(500,500);
	win.setPreferredSize(new Dimension(500,500));
	win.setVisible(true);

    }
}
