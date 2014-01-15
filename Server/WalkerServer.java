import java.net.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.UUID;

class NoRealClientException extends Exception
{
    private static final long serialVersionUID = 42L;
}

class WantsToQuitException extends Exception
{
    private static final long serialVersionUID = 42L;
}

class InputThread extends Thread
{

    InputStream input;
    PrintStream ps;
    BufferedReader reader;
    String id;
    Socket c;
    ArrayList<Socket> cs;
    ArrayList<String> id_n;

    public String getID()
    {
	return this.id;
    }

    public void sendMessage(Socket s, String str) throws IOException
    {
	PrintStream output = new PrintStream(s.getOutputStream());
	output.println("."+str);
    }

    public void sendToAll(String msg)
    {
	PrintStream out;
	for(int i=0;i<cs.size();i++)
	    {
		try{
		    if(!cs.get(i).equals(c))
			{
			    out = new PrintStream(cs.get(i).getOutputStream());
			    out.println("."+msg);
			}
		}
		catch(IOException e){
		    System.err.println("Kunde inte skicka till: "+cs.get(i));
		}
	    }
    }

    public void finalize()
    {
	System.out.println(id+": InputThread to be deleted.");
    }

    public InputThread(Socket c, ArrayList<Socket> cs,ArrayList<String> idn, PrintStream ps,String id) throws IOException, NoRealClientException
    {
	this.id_n = idn;
	this.cs = cs;
	this.c = c;
	this.ps = ps;
	this.id = id;
	input = c.getInputStream();
	reader = new BufferedReader(new InputStreamReader(input));

	String detect = reader.readLine();
	if(detect.equals("Hej server!"))
	    {
		//System.out.println(detect);
	    }
	else
	    {
		throw new NoRealClientException();
	    }
    }

    public void run()
    {
	String line = new String();
	int ch;

	try{
	    while(true)
		{

		    ch = input.read();
		    if(ch==-1)
			{
			    throw new WantsToQuitException();
			}

		    line = reader.readLine();
		    if(line.equals("DISCONNECT"))
			{
			    throw new WantsToQuitException();
			}
		    sendToAll(line);

		    //ps.println(line);

		    /*
		    ch = input.read();
		    if(ch==-1)
			{
			    throw new WantsToQuitException();
			}
		    ps.write(ch);
		    */
		}
	}
	catch(IOException e){
	    sendToAll("<"+id+"> Disconnecting");
	    System.out.println("IOException: ["+e.getMessage()+"] "+this.c);
	}
	catch(WantsToQuitException e){
	    sendToAll("<"+id+"> Disconnecting");
	    System.out.println("Disconnecting: "+this.c);
	}
    }

}


class Connection extends Thread
{
    InputThread it;
    Socket c;
    ArrayList<Socket> sockets;
    ArrayList<String> id_n;

    public void sendMessage(Socket s, String str) throws IOException
    {
	PrintStream output = new PrintStream(s.getOutputStream());
	output.println("."+str);
    }

    public void sendToAll(String msg, Socket not)
    {
	PrintStream out;
	for(int i=0;i<sockets.size();i++)
	    {
		try{
		    if(!sockets.get(i).equals(not))
			{
			    out = new PrintStream(sockets.get(i).getOutputStream());
			    out.println("."+msg);
			}
		}
		catch(IOException e){
		    System.err.println("Kunde inte skicka till: "+sockets.get(i));
		}
	    }
    }

    public Connection(Socket c, ArrayList<Socket> sockets, ArrayList<String> idn, InputThread it)
    {
	this.it = it;
	this.id_n = idn;
	this.c = c;
	this.sockets = sockets;
	//System.out.println("Ny Anslutning skapad #: "+sockets.indexOf(c));

	/*Skicka ID till klienten*/
	try{
	sendMessage(c,"You:"+it.id);

	for(int i=0;i<id_n.size();i++)
	    {
		if(!id_n.get(i).equals(it.id))
		    {
			System.out.println("Sending: "+id_n.get(i));
			try{
			    sendMessage(c,"<"+id_n.get(i)+"> Joined");
			}
			catch(IOException err){
			}
		    }
	    }

	}
	catch(IOException err){
	}
	sendToAll("<"+it.getID()+"> Joined",c); // DEFAULT JOIN MESSAGE???
    }

    public void run()
    {
	try{
	    it.join();
	}
	catch(InterruptedException e) {
	    System.out.println("Interrupted Connection(): "+e.getMessage());
	}

	try{
	    if(!sockets.remove(c))
		{
		    System.err.println("Indexfel: "+c);
		}
	    if(!id_n.remove(it.id))
		{
		    System.err.println("Indexfel: "+it.id);
		}
	    c.close();
	}
	catch(IOException e) {
	    System.err.println("IOException Connection(): "+e.getMessage());
	}

    }

}

class Commands extends Thread
{

    BufferedReader reader;

    public Commands()
    {
	reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run()
    {

    }

}

class Listener extends Thread
{
    int port = 60000;
    ServerSocket s;
    Socket c;
    ArrayList<Socket> cs;
    ArrayList<String> idn;

    public void sendToAll(String msg)
    {
	PrintStream out;
	for(int i=0;i<cs.size();i++)
	    {
		try{
		    sendMessage(cs.get(i),msg);
		}
		catch(IOException e){
		    System.err.println("Kunde inte skicka till: "+cs.get(i));
		}
	    }
    }

    public void sendMessage(Socket s, String str) throws IOException
    {
	PrintStream output = new PrintStream(s.getOutputStream());
	output.println("."+str);
    }

    public Listener() throws IOException
    {
	s = new ServerSocket(port);
	System.out.println(s);
	cs = new ArrayList<Socket>();
	idn = new ArrayList<String>();
    }

    public void run()
    {
	while(true) {
	    try{
		String id = UUID.randomUUID().toString();
		c = s.accept();
		cs.add(c);
		idn.add(id);
		System.out.println(c);

		/*Data som ska skickas*/
		PrintStream output = new PrintStream(c.getOutputStream());
		output.println("Välkommen.");

		/*Data som kommer in*/
		InputThread in = new InputThread(c,cs,idn,new PrintStream(c.getOutputStream()),id);
		in.start();

		Connection con = new Connection(c, cs, idn, in);
		con.start();
	    }
	    catch(IOException e) {
		System.out.println(e.getMessage());
	    }
	    catch(NoRealClientException e) {
		System.out.println("Klienten kunde inte godkännas.");
	    }
	}
    }
}

class WalkerServer
{
    public static void main(String[] args)
    {
	WalkerServer ws = new WalkerServer();
	int port = 60000;
	ServerSocket s;
	Socket c;
	String line;
	BufferedReader reader;
	Listener listen=null;
	reader = new BufferedReader(new InputStreamReader(System.in));
	try{
	    listen = new Listener();
	    listen.start();
	    while(true)
		{
		    try{
			while(true)
			    {
				System.out.print("Command: ");
				line=reader.readLine();
				if(line.equals("d"))
				    {
					throw new WantsToQuitException();
				    }
				if(line.equals("h"))
				    {
					System.out.println("d: skicka disconnect till alla anslutna");
				    }
			    }
		    }
		    catch(IOException e){
		    }
		    catch(WantsToQuitException e){
			listen.sendToAll("DISCONNECT");
		    }

		}

	}
	catch(IOException e){
	    System.err.println(e.getMessage());
	}
    }
}
