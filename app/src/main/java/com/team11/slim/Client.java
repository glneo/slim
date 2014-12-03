package com.team11.slim;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void,String,Void>
{
    public String serverAddress = null;
    public int serverPort;
    public String myName = null;
    public Socket client = null;
    private BufferedReader input;
    private BufferedWriter output;

    Client( String serverAddress, int serverPort, String myName ) throws IOException
    {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.myName = myName;
    }

    public void disconect()
    {
        try
        {
            client.close();
        }
        catch (IOException e)
        {
            // cannot close socket!?
        }
    }

    public void send( Message message )
    {
        try
        {
            output.write((new Gson()).toJson(message));
            output.write( '\n' );
            output.flush();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            this.client = new Socket( serverAddress, serverPort );
            output = new BufferedWriter( new OutputStreamWriter( client.getOutputStream() ) );
            output.write( myName );
            output.write( '\n' );
            output.flush();
            input = new BufferedReader( new InputStreamReader(client.getInputStream() ) );
            while( !isCancelled() )
            {
                Message inputFromServer = (new Gson()).fromJson(input.readLine(), Message.class);
                publishProgress( inputFromServer.messageText );
            }
        }
        catch( IOException e )
        {
            // Find new host
            //parent.migrateHost();
            e.printStackTrace();
        }
		finally
		{
			try
			{
                if( input != null )
                    input.close();
                if( output != null )
                    output.close();
				if( client != null )
					client.close();
			}
			catch (IOException e)
			{
			}
		}
        return null;
    }
}