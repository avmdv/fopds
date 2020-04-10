package org.avmrus.fopds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class BooksListFragment extends Fragment {

    private ArrayList<Book> booksList;
    private ListView listView;
    private String category;
    private BooksListAdapter adapter;

    public BooksListFragment() {
        super();
    }

    public void setCategory(ArrayList<Book> list, String genre) {
        this.booksList = list;
        this.category = genre;
        this.adapter = new BooksListAdapter(booksList, category);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_list, null);
        listView = view.findViewById(R.id.list);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
                bookDetailsFragment.setBook(booksList.get(position));
                FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
                ftrans.addToBackStack(null);
                ftrans.replace(R.id.fragmentContainer, bookDetailsFragment, "bookDetailsFragment").commit();
            }
        });
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}