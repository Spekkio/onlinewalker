import java.util.ArrayList;
import java.util.UUID;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class CharacterPool
{
    ArrayList<Character> characters;
    Character mainch;
    WalkerClient client;
    Pattern pattern;
    Matcher match;

    public void addCharacter(String id)
    {
	Character ny = new Character(id);
	characters.add(ny);
    }

    public void delCharacter(String id)
    {
	for(int i=0;i<characters.size();i++)
	    {
		if(characters.get(i).getID().equals(id))
		    {
			characters.remove(i);
		    }
	    }
    }

    public Character findCharacter(String id)
    {
	Character ch;
	if(characters!=null)
	    {
		for(int i=0;i<characters.size();i++)
		    {
			ch = characters.get(i);

			if(ch.getID().equals(id))
			    {
				return ch;
			    }
		    }
	    }
	return null;
    }

    public void setPosition(String ids,int x,int y)
    {
	Character ch;
	if(characters!=null)
	    {
	for(int i=0;i<characters.size();i++)
	    {
		ch = characters.get(i);

		if(ch.getID().equals(ids))
		    {
			ch.setPosition(x,y);
		    }
	    }
	    }
    }

    public CharacterPool()
    {
	Callback cb = new Callback()
	    {
		public void setMyID(String str)
		{
		    mainch.setID(str);
		}

		public void function(String str)
		{
		    String[] strings;
		    String[] nick;
		    
		    pattern = Pattern.compile("^[<][a-zA-Z0-9-]+[>].[POSITION]{1}?");
		    match = pattern.matcher(str);		   

		    if(match.find())
			{
			    //System.out.println(str);
			    /*
			    */

			    pattern = Pattern.compile(new String("[:]"));
			    strings = pattern.split(str);
			    pattern = Pattern.compile(new String("[<>]"));
			    nick = pattern.split(strings[0]);
			    Character ch = findCharacter(nick[1]);
			    int setPos_x=0, setPos_y=0, old_x=0, old_y=0;
			    if(ch!=null){
				ch.clearRecord();
				ch.walk = false;
				ch.nyy=0;
				for(int i=1;i<strings.length;i++)
				    {
					pattern = Pattern.compile(new String("[ ]"));
					nick = pattern.split(strings[i]);

					setPos_x = Integer.parseInt(nick[0]);
					setPos_y = Integer.parseInt(nick[1]);

					if((i>1)&&((old_x!=setPos_x)||(old_y!=setPos_y)))
					    {
						ch.walk = true;
						ch.nyy=1;
					    }

					old_x = setPos_x;
					old_y = setPos_y;

					ch.recordPosition(setPos_x,
							  setPos_y,
							  Character.Direction.valueOf(nick[2]));
				    }
			    }

			}

		    pattern = Pattern.compile("^[<][a-zA-Z0-9-]+[>] Joined$");
		    match = pattern.matcher(str);

		    if(match.find())
			{
			    pattern = Pattern.compile(new String("[<>]"));
			    strings = pattern.split(str);
			    //System.out.println("add: "+strings[1]);
			    addCharacter(strings[1]);
			}

		    pattern = Pattern.compile("Disconnect");
		    match = pattern.matcher(str);

		    if(match.find())
			{
			    pattern = Pattern.compile(new String("[<>]"));
			    strings = pattern.split(str);

			    System.out.println("del: "+strings[1]);
			    delCharacter(strings[1]);
			}


		}
	    };

	ActionListener timerListener = new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    try{
		    //client.sendMessage(mainch.x+","+mainch.y+","+mainch.drawType);
			client.sendMessage("POSITION:"+mainch.printRecord());
			mainch.clearRecord();
		    }
		    catch(IOException err){
			System.err.println("Kunde inte skicka position: "+err.getMessage());
		    }
		}
	    };

	Timer timer = new Timer(500,timerListener);

	mainch = new Character("NULL");

	client = new WalkerClient("127.0.0.1",cb);
	client.start();

	characters = new ArrayList<Character>();

	timer.start();
    }

    public void drawAllCharacters(Graphics g)
    {
	int i;
	for(i=0;i<characters.size();i++)
	    {
		characters.get(i).drawCharacter(g);
	    }
    }

    public void setAllNextPos()
    {
	int i;
	Position p;
	Character c;
	if(!characters.isEmpty())
	    {
		for(i=0;i<characters.size();i++)
		    {
			c=characters.get(i);
			p=c.getNextRecord();
			c.setPosition(p.getX(),p.getY());
			c.setDirection(p.drawType);
			//System.out.println(c.x+"/"+c.y+"/"+c.drawType+"/"+p.drawType);
		    }
	    }
    }

    public void updateAllCharacters()
    {
	int i;
	for(i=0;i<characters.size();i++)
	    {
		characters.get(i).updateCharacter();
	    }
    }


}
