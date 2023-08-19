package com.example.taskmanager_prma_r21207;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Date;

public class LocalStorage {

    private static final String SHARED_PREFERENCES_NAME = "notes";

    public static void saveNoteLocally(Context context, Note note) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", note.getTitle());
        editor.putString("content", note.getContent());
        editor.putLong("timestamp", note.getTimestamp().getTime());
        editor.apply();
    }

    public static Note getLocallySavedNote(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String title = sharedPreferences.getString("title", "");
        String content = sharedPreferences.getString("content", "");
        long timestamp = sharedPreferences.getLong("timestamp", 0);
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTimestamp(new Date(timestamp));
        return note;
    }

    public static void clearLocallySavedNote(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
