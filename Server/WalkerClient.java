import java.net.*;
import java.io.*;
import java.util.zip.*;


class InputThreadClient extends Thread
{
    Socket c;
    BufferedReader reader;

    public InputThreadClient(Socket c)
    {
	this.c = c;
	try{
	    reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
	}
	catch(IOException e){
	}
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
		System.out.println(line);
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

class WalkerClient
{

    public static void sendMessage(Socket s, String nick, String msg) throws IOException
    {
	PrintStream ps = new PrintStream(s.getOutputStream());
	ps.println("A<"+nick+">"+" "+msg);
    }

    public WalkerClient(String server, String nickname)
    {
    }

    public static void main(String[] args)
    {

	int port=60000;
	String hostname = new String("148.160.188.252");
	Socket s;
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
		}

	    InputThreadClient it = new InputThreadClient(s);
	    it.start();

	    BufferedReader tgb = new BufferedReader(new InputStreamReader(System.in));

	    String reader = new String();
	    while(!reader.equals("quit"))
		{
		    reader=tgb.readLine();
		    //reader = args[0];
		    //ps.println(reader);
		    sendMessage(s,args[0],reader);
		}

	    s.close();
	}
	catch(UnknownHostException e){
	    System.out.println(e.getMessage());
	}
	catch(IOException e){
	    System.out.println(e.getMessage());
	}

    }


}
