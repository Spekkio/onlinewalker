import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

class Character
{
    public enum Direction { UP,DOWN,LEFT,RIGHT };
    Direction drawType;
    Image front, back, left, right;
    Image wfront, wback, wleft, wright;
    Image todraw;
    int x,y,nyx,nyy;

    private ArrayList<Position> recordBuffer;
    private int posIter;

    boolean walk;
    Timer walkerTimer;
    ImageObserver obs;
    String id;

    public void recordPosition(int nx, int ny,Direction ndrawType)
    {
	Position a = new Position(nx,ny);
	a.drawType = ndrawType;
	recordBuffer.add(a);
    }

    public void recordPosition()
    {
	Position a = new Position(x,y);
	a.drawType = drawType;
	recordBuffer.add(a);
    }

    public Position getNextRecord()
    {
	Position ret;
	int size = recordBuffer.size();
	if((posIter<size)&&(!recordBuffer.isEmpty()))
	    {
		ret = recordBuffer.get(posIter);
		posIter++;
	    }
	else
	    {
		if(!recordBuffer.isEmpty())
		    {
			ret = recordBuffer.get(size-1);
		    }
		else
		    {
			ret = new Position(x,y);
			ret.drawType = drawType;
		    }
	    }
	return ret;
    }

    public String printRecord()
    {
	String ret = new String();
	Position p;
	for(int i=0;i<recordBuffer.size();i++)
	    {
		p = recordBuffer.get(i);
		ret += p.getX()+" "+p.getY()+" "+p.drawType+":";
	    }
	return ret;
    }

    public void clearRecord()
    {
	posIter=0;
	recordBuffer.clear();
    }

    public String getID()
    {
	return this.id;
    }

    public Boolean equals(Character comp)
    {
	return comp.getID().equals(comp.getID());
    }

    public boolean walking()
    {
	if((nyx!=0) || (nyy!=0))
	    {
		return true;
	    }
	return false;
    }

    public Character(String id)
    {
	setID(id);
	System.out.println("New Chr ID : "+id);
	recordBuffer = new ArrayList<Position>();
	posIter=0;

	obs = new ImageObserver()
	    {
		public boolean imageUpdate(Image img,
				    int infoflags,
				    int x,
				    int y,
				    int width,
				    int height)
		{
		    System.out.println("=========================");
		    System.out.println("img: "+img);
		    System.out.println("infoflags: "+infoflags);
		    System.out.println("x: "+x);
		    System.out.println("y: "+y);
		    System.out.println("width: "+width);
		    System.out.println("height: "+height);
		    System.out.println("=========================");
		    return false;
		}

	    };


	try{
	    File file;
	    Graphics t;

	    front = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_down2.gif"));
	    wfront = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_down4.gif"));

	    back = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_up2.gif"));
	    wback = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_up4.gif"));

	    left = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_right2.gif"));
	    wleft = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_right4.gif"));

	    right = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_left2.gif"));
	    wright = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("bilder/Spekkio_run_left4.gif"));

	}
	catch(FileNotFoundException e){
	    System.err.println(e.getMessage());
	}
	catch(IOException e){
	    System.err.println(e.getMessage());
	}

	drawType = Direction.DOWN;
	walk = true;

	ActionListener walker = new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    walk = !walk;
		}
	    };

	walkerTimer = new Timer(300,walker);
	walkerTimer.start();

	x=200;
	y=200;

    }

    public void drawCharacter(Graphics g)
    {

	switch(drawType)
	    {
	    case UP:
		if(walking() && walk)
		    {
			todraw = wback;
		    }
		else
		    {
			todraw = back;
		    }
		break;

	    case DOWN:
		if(walking() && walk)
		    {
			todraw = wfront;
		    }
		else
		    {
			todraw = front;
		    }
		break;

	    case LEFT:
		if(walking() && walk)
		    {
			todraw = wleft;
		    }
		else
		    {
			todraw = left;
		    }
		break;

	    case RIGHT:
		if(walking() && walk)
		    {
			todraw = wright;
		    }
		else
		    {
			todraw = right;
		    }
		break;

	    default: break;
	    }

	g.drawImage(todraw,x,y,obs);
	//g.setBackground(Color.white);
	g.setColor(Color.white);
	g.fillRoundRect(x-100,y-50, 300, 50, 15, 15);
	g.setColor(Color.black);
	g.drawString(getID(),x-90,y-30);
	//g.setBackground(Color.black);
    }

    public void setDirection(Direction d)
    {
	drawType = d;
    }

    public void setDirection(String d)
    {
	drawType = Direction.valueOf(d);
    }

    public void setID(String id)
    {
	this.id = id;
    }

    public void setPosition(int x, int y)
    {
	this.x = x;
	this.y = y;
    }

    public void moveCharacter(int nx, int ny)
    {
	if(nx!=0){nyx = nx;}
	if(ny!=0){nyy = ny;}
	if(nx>0)
	    {
		setDirection(Direction.RIGHT);
	    }
	if(nx<0)
	    {
		setDirection(Direction.LEFT);
	    }
	if(ny<0)
	    {
		setDirection(Direction.UP);
	    }
	if(ny>0)
	    {
		setDirection(Direction.DOWN);
	    }
    }

    public void updateCharacter()
    {
	x += nyx;
	y += nyy;

	recordPosition();

	if(x<0)
	    {
		x=0;
	    }

	if(y<0)
	    {
		y=0;
	    }

	if(x>450)
	    {
		x=450;
	    }

	if(y>400)
	    {
		y=400;
	    }
    }

    public void stopCharacter(int nx, int ny)
    {
	if(nx==1)
	    {
		nyx = 0;
	    }
	if(ny==1)
	    {
		nyy = 0;
	    }
	moveCharacter(nyx,nyy);
    }
}
