import java.net.*;
import java.io.*;

public class ThreadTest {

  public static void main(String[] args) {

    int thePort;
    ServerSocket ss;
    Socket theConnection;
    
    try {
      thePort = Integer.parseInt("60000");
    }  
    catch (Exception e) {
      thePort = 0;
    }
    try{
      ss = new ServerSocket(thePort);
      System.out.println("Listening for connections on port " + ss.getLocalPort());


      while (true) {
        theConnection = ss.accept();
	Connection cn = new Connection(theConnection);
	cn.start();
      }
    }
    catch (IOException e) {
    
    }
  
  }

}

class Connection extends Thread {

    InputThread it;
    OutputThread ot;

    public void run()
    {
        try {
	    ot.join();
	    it.join();
	    System.out.println("Get here?");
        }
        catch (InterruptedException e) {
        }
    }

    public Connection(Socket theConnection) throws IOException
    {
        System.out.println("Connection established with " + theConnection);

	it = new InputThread(theConnection.getInputStream());
        it.start();
        ot = new OutputThread(theConnection.getOutputStream(), it);
        ot.start();
        // need to wait for ot and it to finish 
    }

}

class InputThread extends Thread {
  
  InputStream is;
  
   public InputThread(InputStream is) {
       this.is = is;
   }

    public void setInputStream(InputStream is)
    {
	this.is = is;
    }

   public void run()  {
   
     try {
       while (true && is!=null) {
         int i = is.read();
         if (i == -1) break;
         char c = (char) i;
         System.out.print(c);
       }
     }
     catch (IOException e) {
       System.err.println(e);
     }
   
   }

}

class OutputThread extends Thread
{
  
    PrintStream ps;
    BufferedReader is;
    InputThread it;
    
   public OutputThread(OutputStream os, InputThread it) {
     ps = new PrintStream(os);
     this.it = it;
     is = new BufferedReader(new InputStreamReader(System.in));
   }

   public void run() {

     String line;
     try {
       while (true) {
         line = is.readLine();
         if (line.equals("."))
	     {
		 System.out.println("-Wants to interrupt");
		 break;
	     }
         ps.println(line);
       }   
     }
     catch (IOException e) {
     
     }
      it.interrupt();
   }

}
