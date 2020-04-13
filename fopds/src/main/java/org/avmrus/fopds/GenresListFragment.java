package org.avmrus.fopds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GenresListFragment extends ListFragment {

    private ArrayList<Book> booksList;
    private ArrayList<String> genresList;
    private ArrayList<String> genresBlacklist;

    public void setBooksList(ArrayList<Book> list) {
        this.booksList = list;
        genresList = extractGenres(list);
        genresBlacklist = Settings.getInstance().restoreArrayList("genresblacklist");
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
        registerForContextMenu(this.getListView());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Settings.getInstance().storeArrayList("genresblacklist", genresBlacklist);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_genreslist_context, menu);
        menu.findItem(R.id.menu_genreslist_item_remove).setEnabled(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_genreslist_item_add:
                addGenreToBlacklist(genresList.get(info.position));
                break;
            case R.id.menu_genreslist_item_remove:
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void addGenreToBlacklist(String genre) {
        this.genresBlacklist.add(genre);
    }

}
