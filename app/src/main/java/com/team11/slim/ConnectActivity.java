package com.team11.slim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class ConnectActivity extends Activity
{
    public final static String ADDRESS_MESSAGE = "com.team11.slim.ADDRESS_MESSAGE";
    public final static String PORT_MESSAGE = "com.team11.slim.PORT_MESSAGE";
    public final static String USERNAME_MESSAGE = "com.team11.slim.USERNAME_MESSAGE";

    // UI references.
    private EditText mServerAddress;
    private EditText mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // Set up the connect form
        mServerAddress = (EditText) findViewById(R.id.edit_text_server_address);
        mUserName = (EditText )findViewById(R.id.edit_text_user_name);
        mUserName.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == EditorInfo.IME_NULL)
                {
                    attemptLogin(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void attemptLogin( View v )
    {
        // Reset errors
        mServerAddress.setError(null);
        mUserName.setError(null);

        // Store values at the time of the login attempt
        String serverAddress = mServerAddress.getText().toString();
        String userName = mUserName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid user name
        if (TextUtils.isEmpty(userName))
        {
            mUserName.setError(getString(R.string.connect_field_required));
            focusView = mUserName;
            cancel = true;
        }

        // Check for a valid server address
        if (TextUtils.isEmpty(serverAddress))
        {
            mServerAddress.setError(getString(R.string.connect_field_required));
            focusView = mServerAddress;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt the connection and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(ADDRESS_MESSAGE, serverAddress);
            intent.putExtra(USERNAME_MESSAGE, userName);
            intent.putExtra(PORT_MESSAGE, 5555);
            startActivity(intent);
            finish();
        }
    }

    public void textFieldResponder(View v) {
        //Reset the error messages if you click the text views
        mServerAddress.setError(null);
        mUserName.setError(null);
    }
}



