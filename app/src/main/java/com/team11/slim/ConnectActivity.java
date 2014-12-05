package com.team11.slim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
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
        else if (!isUserNameValid(userName))
        {
            mUserName.setError(getString(R.string.connect_user_name_invalid));
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
        else if (!isServerAddressValid(serverAddress))
        {
            mServerAddress.setError(getString(R.string.connect_server_address_invalid));
            focusView = mServerAddress;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
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
        }
    }

    private boolean isServerAddressValid(String email)
    {
        return true;
    }

    private boolean isUserNameValid(String password)
    {
        return true;
    }

    public void textFieldResponder(View v) {
        //Reset the error messages if you click the text views
        mServerAddress.setError(null);
        mUserName.setError(null);
    }
}



