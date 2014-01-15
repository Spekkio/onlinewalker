import java.awt.*;
import java.applet.*;
import javax.swing.*;

class AppletMain extends Applet
{

    public AppletMain()
    {
    }

  public void start(){
     System.out.println("Applet starting.");
  }

  public void stop(){
     System.out.println("Applet stopping.");
  }

  public void destroy(){
     System.out.println("Destroy method called.");
  }


    public void init()
    {
	JFrame win = new JFrame();
	win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	win.setContentPane(new GrafikYta());
	//win.setContentPane(new ChatBox());
	win.setSize(500,500);
	win.setVisible(true);
    }
    /*
    public void paint(Graphics g)
    {
    }
    */

}
