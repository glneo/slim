package com.team11.slim;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class Client extends AsyncTask<Void,Message,Void>
{
    private final String mAddress;
    private final int mPort;
    private final String mUserName;
    private Socket mSocket;

    private MessageDataSource dbSource;

    private BufferedReader input;
    private BufferedWriter output;

    Client(Context context, String address, int port, String userName)
    {
        mAddress = address;
        mPort = port;
        mUserName = userName;
        dbSource = new MessageDataSource(context);
    }

    public boolean send( Message message )
    {
        try
        {
            if( output != null )
            {
                output.write((new Gson()).toJson(message));
                output.write('\n');
                output.flush();
                return true;
            }
            else
                return false;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        List<Message> messages = dbSource.getAllMessages();
        for (Message message : messages)
            publishProgress( message );
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
                dbSource.addMessage( inputFromServer );
                publishProgress( inputFromServer );
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