package org.avmrus.fopds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GenresListFragment extends ListFragment {

    private ArrayList<Book> booksList;
    private ArrayList<String> genresList;

    public void setBooksList(ArrayList<Book> list) {
        this.booksList = list;
        genresList = extractGenres(list);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, genresList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        BooksListFragment booksListFragment = (BooksListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("booksListFragment");
        if (booksListFragment == null) {
            booksListFragment = new BooksListFragment();
            booksListFragment.setCategory(booksList, genresList.get(position));
            FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
            ftrans.addToBackStack(null);
            ftrans.replace(R.id.fragment_container, booksListFragment, "booksListFragment").commit();
        }
    }

    private ArrayList<String> extractGenres(ArrayList<Book> booksList) {
        ArrayList<String> genresList = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();
        for (Book book : booksList) {
            for (String category : book.getCategories()) {
                hashSet.add(category);
            }
        }
        for (String category : hashSet) {
            genresList.add(category);
        }
        Collections.sort(genresList);
        return genresList;
    }

}
