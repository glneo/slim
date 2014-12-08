package com.team11.slim;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity
{
    private final String LOG_TAG = "MAIN_ACT";

    private EditText mNewMessageText;
    private SimpleAdapter mAdapter;
    private ArrayList<HashMap<String,String>> mList;
    private Client mClient;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        // get layout instances
        mNewMessageText = (EditText)findViewById(R.id.message);
        ImageButton mSendButton = (ImageButton) findViewById(R.id.send);
        if(mSendButton != null)
            mSendButton.setOnClickListener(new SendClickListener());
        ListView mListView = (ListView) findViewById(R.id.listView);

        // Each row in the list stores the user name and text
        mList = new ArrayList<HashMap<String,String>>();

        // Keys used in Hashmap
        String[] from = { "USER_NAME", "MESSAGE_TEXT" };

        // Ids of views in listview_layout
        int[] to = { R.id.name, R.id.text };

        // Instantiating an adapter to store each items
        // R.layout.drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.message_list_item, from, to);

        // set up the drawer's list view with items and click listener
        mListView.setAdapter(mAdapter);

        String mServerAddress = getIntent().getExtras().getString(ConnectActivity.ADDRESS_MESSAGE);
        int mPort = (Integer) getIntent().getExtras().get(ConnectActivity.PORT_MESSAGE);
        mUserName = getIntent().getExtras().getString(ConnectActivity.USERNAME_MESSAGE);

        mClient = new Client(getApplicationContext(), mServerAddress, mPort, mUserName)
        {
            @Override
            protected void onProgressUpdate(Message... values)
            {
                super.onProgressUpdate(values);
                if( values[0].type == MessageType.Text )
                    addItem( values[0].peer.name, values[0].messageText );
                // notify the adapter that the data set has changed. This means that new message received
                // from server was added to the list
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onPostExecute(final Void myVoid)
            {
                mClient = null;
                doDisconnect();
            }

            @Override
            protected void onCancelled()
            {
                mClient = null;
                doDisconnect();
            }
        };
        mClient.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addItem( String userName, String messageText )
    {
        HashMap<String, String> hm = new HashMap<String,String>();
        hm.put( "USER_NAME", userName );
        hm.put( "MESSAGE_TEXT", messageText );
        mList.add( hm );
        mAdapter.notifyDataSetChanged();
    }

    public void doDisconnect()
    {
        Intent intent = new Intent(this, ConnectActivity.class);
        startActivity(intent);
        finish();
    }

    private class SendClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(final View view)
        {
            String messageText = mNewMessageText.getText().toString();
            mNewMessageText.setText(""); // Clear message
            if(!messageText.equals(""))
            {
                final Message message = new Message(new Peer(mUserName, ""),
                                                MessageType.Text,
                                                messageText);

                AsyncTask<Message, Void, Boolean> sendTask = new AsyncTask<Message, Void, Boolean>()
                {
                    @Override
                    protected Boolean doInBackground(Message... messages)
                    {
                        for (Message message1 : messages) {
                            Log.d(LOG_TAG, "Sending message: " + message1.messageText);
                            if (!mClient.send(message1))
                                return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean)
                    {
                        super.onPostExecute(aBoolean);
                        if( aBoolean )
                            Toast.makeText(view.getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(view.getContext(), "Failed to Send Message", Toast.LENGTH_SHORT).show();
                    }
                };

                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                    sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message);
                } else {
                    sendTask.execute(message);
                }
            }
        }
    }
}
