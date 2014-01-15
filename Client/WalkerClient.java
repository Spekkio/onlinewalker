import java.net.*;
import java.io.*;
import java.util.zip.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class InputThreadClient extends Thread
{
    Socket c;
    BufferedReader reader;
    PrintStream output;
    Callback func;

    public InputThreadClient(Socket c, PrintStream output, Callback func, BufferedReader reader)
    {
	this.func = func;
	this.output = output;
	this.c = c;
	this.reader = reader;
	/*
	try{
	    reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
	}
	catch(IOException e){
	}
	*/
    }

    public void run()
    {
	String line;
	int ch;
	try{
	while(true)
	    {
		ch = reader.read();
		if(ch==-1)
		    {
			throw new WantsToQuitException();
		    }

		line=reader.readLine();
		if(line.equals("DISCONNECT"))
		    {
			throw new WantsToQuitException();
		    }
		//System.out.println(line);
		func.function(line);
	    }
	}
	catch(IOException e){
	}
	catch(WantsToQuitException e){
	}

	try{
	    c.close();
	}
	catch(IOException e){
	}

    }

}

class WalkerClient extends Thread
{
    String nickname="NULL", hostname;
    Socket s;
    Boolean connected=false;
    Callback func;

    public void setNickname(String nick)
    {
	this.nickname = nick;
    }

    public void sendMessage(String msg) throws IOException
    {
	sendMessage(s,nickname,msg);
    }

    public void sendMessage(Socket s, String nick, String msg) throws IOException
    {
	if(connected)
	    {
		PrintStream ps = new PrintStream(s.getOutputStream());
		ps.println("A<"+nick+">"+" "+msg);
	    }
	//else throw new IOException();
    }

    public WalkerClient(String server, Callback func)
    {
	this.func = func;
	this.nickname = nickname;
	hostname = new String(server);
    }

    public void run()
    {
	int port=60000;
	PrintStream ps;
	BufferedReader read;
	GZIPInputStream in;

	try{
	    s=new Socket(hostname,port);
	    ps = new PrintStream(s.getOutputStream());

	    ps.println("Hej server!");

	    read = new BufferedReader(new InputStreamReader(s.getInputStream()));

	    if(read.readLine().equals("Välkommen."))
		{
		    System.out.println("Välkommen.");
		    connected=true;
		}
	    Boolean lookForID=true;
	    while(lookForID)
		{
		    String line = read.readLine();
		    Pattern pattern = Pattern.compile("You:[a-zA-Z0-9-]+");
		    Matcher match = pattern.matcher(line);

		    if(match.find())
			{
			    pattern = Pattern.compile(new String("[:]"));
			    String[] strings = pattern.split(line);
			    nickname = strings[1];
			    func.setMyID(nickname);
			    System.out.println("Your ID: "+nickname);
			    lookForID = false;
			    connected = true;
			}
		    else
			{
			    System.err.println("["+line+"] Jag fick inget ID!");
			    connected=false;
			}
		}
	    /*
	    System.out.println("#1"+read.readLine());
	    System.out.println("#2"+read.readLine());
	    System.out.println("#3"+read.readLine());
	    System.out.println("#4"+read.readLine());
	    */

	    InputThreadClient it = new InputThreadClient(s,System.out,func,read);
	    it.start();

	    BufferedReader tgb = new BufferedReader(new InputStreamReader(System.in));

	    //String reader = new String();
	    //while(!reader.equals("quit"))
	    //{
		    /*
		    reader=tgb.readLine();
		    //reader = args[0];
		    //ps.println(reader);
		    sendMessage(s,nickname,reader);
		    */
	    //}
	    it.join();

	    s.close();
	}
	catch(InterruptedException e){
	    System.out.println(e.getMessage());
	    connected=false;
	}
	catch(UnknownHostException e){
	    System.out.println(e.getMessage());
	    connected=false;
	}
	catch(IOException e){
	    System.out.println(e.getMessage());
	    connected=false;
	}
	connected=false;
    }

}
