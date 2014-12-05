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
    private final String mAddress;
    private final int mPort;
    private final String mUserName;
    private Socket mSocket;

    private BufferedReader input;
    private BufferedWriter output;

    Client(String address, int port, String userName)
    {
        mAddress = address;
        mPort = port;
        mUserName = userName;
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
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            mSocket = new Socket(mAddress, mPort);
            output = new BufferedWriter( new OutputStreamWriter( mSocket.getOutputStream() ) );
            output.write( mUserName );
            output.write( '\n' );
            output.flush();
            input = new BufferedReader( new InputStreamReader( mSocket.getInputStream() ) );
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
				if( mSocket != null )
					mSocket.close();
			}
			catch (IOException e)
			{
                e.printStackTrace();
			}
		}
        return null;
    }
}