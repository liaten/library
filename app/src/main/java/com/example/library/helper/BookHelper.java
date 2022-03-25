package com.example.library.helper;

import android.os.AsyncTask;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookHelper extends AsyncTask<String, Void, String> {

    private static final short MAX_LENGTH_AUTHOR = 24;
    private static final short MAX_LENGTH_TITLE = 20;
    private short length = 0;
    private final TextView textView;

    public BookHelper(@Nullable TextView textView, @NonNull String authorOrTitle) {
        this.textView = textView;
        setLength(authorOrTitle);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (textView != null) {
            textView.setText("");
        }
    }

    @Nullable
    @Override
    protected String doInBackground(@NonNull String... strings) {

        StringBuilder result = new StringBuilder();
        if (isAuthor(strings)) {
            result = setAuthor(result, strings);
        } else {
            result = setTitle(result, strings);
        }

        if (stringBuilderIsLongerThanMaxLength(result)) {
            result = cropString(result);
        }

        return result.toString();
    }

    protected void onPostExecute(@Nullable String result) {
        if (textView != null) {
            textView.setText(result);
        }
    }

    private void setLength(String authorOrTitle) {
        switch (authorOrTitle) {
            case "author":
                length = MAX_LENGTH_AUTHOR;
                break;
            case "title":
                length = MAX_LENGTH_TITLE;
                break;
        }
    }

    private StringBuilder cropString(StringBuilder s) {
        s.setLength(length);
        if (lastCharOfStringIsSpace(s)) {
            s.setLength(length - 1);
        }
        s.append("...");
        return s;
    }

    private boolean stringBuilderIsLongerThanMaxLength(StringBuilder s) {
        return s.length() > length;
    }

    private boolean lastCharOfStringIsSpace(StringBuilder s) {
        return s.toString().charAt(length - 1) == ' ';
    }

    /**
     * @param strings more than one string?
     * @return >1 - author, 1 - title
     */
    private boolean isAuthor(String... strings) {
        return strings.length > 1;
    }

    private StringBuilder setAuthor(StringBuilder stringBuilder, String... strings) {
        String surname = strings[0];
        String name = strings[1];
        return stringBuilder.append(name).append(" ").append(surname);
    }

    private StringBuilder setTitle(StringBuilder stringBuilder, String... strings) {
        String title = strings[0];
        return stringBuilder.append(title);
    }
}