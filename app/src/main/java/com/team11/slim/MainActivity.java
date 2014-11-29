package com.team11.slim;

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

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity
{
    private final String LOG_TAG = "MAIN_ACT";

    private EditText mNewMessageText;
    private ImageButton mSendButton;
    private SimpleAdapter mAdapter;
    private ArrayList<HashMap<String,String>> mList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        // get layout instances
        mNewMessageText = (EditText)findViewById(R.id.message);
        mSendButton = (ImageButton)findViewById(R.id.send);
        if(mSendButton != null)
            mSendButton.setOnClickListener( new SendClickListener() );
        mListView = (ListView)findViewById(R.id.listView);

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

    private class SendClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            addItem( "TESTUSER", "Hello, World!" );
        }
    }
}
