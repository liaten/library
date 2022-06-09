package com.example.library.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.other.BooksExtendedList;
import com.example.library.helper.FragmentHelper;

public class SearchFragment extends Fragment {
    private Button FindBookButton;
    private String link = "";
    private EditText BookNameEditText,AuthorEditText,ThemeEditText;
    View.OnClickListener findBookListener = view -> {
        link = "https://liaten.ru/db/search_book.php?limited=n&recsPerPage=12";
        String author = AuthorEditText.getText().toString();
        String bookName = BookNameEditText.getText().toString();
        String theme = ThemeEditText.getText().toString();
        if(!author.equals("")){
            link += "&author=" + author;
        }
        if(!bookName.equals("")){
            link += "&bookName=" + bookName;
        }
        if(!theme.equals("")){
            link += "&theme=" + theme;
        }
        link += "&page=1";
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        "Результаты поиска",link)
        );
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
    }

    private void setViews() {
        View v = requireView();
        FindBookButton = v.findViewById(R.id.find_book);
        BookNameEditText = v.findViewById(R.id.book_name);
        AuthorEditText = v.findViewById(R.id.author);
        ThemeEditText = v.findViewById(R.id.theme);
    }

    private void setOnClickListeners() {
        FindBookButton.setOnClickListener(findBookListener);
    }
}
