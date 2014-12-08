package com.team11.slim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageDataSource
{
    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {DataBaseHelper.COLUMN_ID,
            DataBaseHelper.COLUMN_NAME,
            DataBaseHelper.COLUMN_MESSAGE};

    public MessageDataSource(Context context)
    {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void addMessage(Message message)
    {
        if( message.type == MessageType.Text )
        {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.COLUMN_NAME, message.peer.name);
            values.put(DataBaseHelper.COLUMN_MESSAGE, message.messageText);
            database.insert(DataBaseHelper.TABLE_MESSAGES, null, values);
        }
    }

    public List<Message> getAllMessages()
    {
        List<Message> messages = new ArrayList<Message>();

        Cursor cursor = database.query(DataBaseHelper.TABLE_MESSAGES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Peer peer = new Peer(cursor.getString(1), "");
            Message message = new Message(peer, MessageType.Text, cursor.getString(2));
            messages.add(message);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return messages;
    }
}
