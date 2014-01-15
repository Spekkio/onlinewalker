import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

class GrafikYta extends JPanel
{
    Character im;
    Image grass,background;
    int updatems=50;
    int speed=150;
    Image bubbla;
    List<Image> grase;
    boolean typemode;
    String chat;
    int stringcounter;
    CharacterPool chp;


    public GrafikYta()
    {
	grase = new ArrayList<Image>();
	chp = new CharacterPool();

	try{
	    for(int i=1;i<10;i++)
		{
		    grass = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Natural-0"+i+".gif"));
		    grase.add(grass);
		}
	}
	catch(IOException e){
	    System.err.println(e.getMessage());
	}
	catch(IllegalArgumentException e){
	    System.err.println("Error loading grass: "+e.getMessage());
	}

	stringcounter=0;
	chat = new String();
	typemode=false;
	setBackground(Color.black);
	im = chp.mainch;

	try{
	bubbla = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/pratbubbla.png"));
	}
	catch(IOException a)
	    {
	    }

	KeyListener act = new KeyListener()
	    {
		public void keyTyped(KeyEvent e)
		{
		    if(!typemode && (e.getKeyChar()=='e'))
			{
			    //typemode=true;
			}
		    else 
			{
			}
		}
		public void keyPressed(KeyEvent e)
		{
		    int scale = (int)(speed*(updatems*0.001));
		    switch(e.getKeyCode())
			{
			case 38:
			    im.moveCharacter(0,-scale);
			    break;
			case 40:
			    im.moveCharacter(0,scale);
			    break;
			case 37:
			    im.moveCharacter(-scale,0);
			    break;
			case 39:
			    im.moveCharacter(scale,0);
			    break;
			default: break;
			}
		}
		public void keyReleased(KeyEvent e)
		{
		    switch(e.getKeyCode())
			{
			case 38:
			    im.stopCharacter(0,1);
			    break;
			case 40:
			    im.stopCharacter(0,1);
			    break;
			case 37:
			    im.stopCharacter(1,0);
			    break;
			case 39:
			    im.stopCharacter(1,0);
			    break;
			default: break;
			}
		}
	    };

	ActionListener timerListener = new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    repaint();
		}
	    };

	Timer timer = new Timer(updatems,timerListener);
	timer.start();
	background = createBackground();
	addKeyListener(act);
	setFocusable(true);
    }


    public Image createBackground()
    {
	int x,y;
	int width = 62;
	int height = 32;

	BufferedImage i = new BufferedImage(500, 500, 1);
	Graphics g = i.createGraphics();
	//i.setBackground(Color.white);

	for(x=-width/2;x<((500/width));x++)
	    {
		for(y=-height/2;y<((500/height));y++)
		    {
			    g.drawImage(grase.get((int)(Math.random()*8)),x*width,y*height,this);
			    g.drawImage(grase.get((int)(Math.random()*8)),x*width+width/2,y*height+height/2,this);
		    }
	    }
	return i;
    }


    protected void paintComponent(Graphics g)
    {
	super.paintComponent(g);

	g.drawImage(background,0,0,this);

	//chp.updateAllCharacters();
	chp.setAllNextPos();
	chp.drawAllCharacters(g);

	im.updateCharacter();
	im.drawCharacter(g);

	if(typemode)
	    {
		g.drawImage(bubbla,im.x+50,im.y-130,this);
		g.drawString(chat,im.x+120,im.y-100);
	    }

    }

}
