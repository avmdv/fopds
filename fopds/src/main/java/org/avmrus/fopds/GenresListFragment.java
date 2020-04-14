package org.avmrus.fopds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GenresListFragment extends Fragment {

    private ArrayList<Book> booksList;
    private ArrayList<String> genresList;
    private ArrayList<String> genresBlacklist;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    public void setBooksList(ArrayList<Book> list) {
        this.booksList = list;
        genresList = extractGenres(list);
        genresBlacklist = Settings.getInstance().restoreArrayList("genresblacklist");
        this.adapter = new GenresListAdapter(genresList, genresBlacklist);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genres_list, null);
        listView = view.findViewById(R.id.fragment_genres_list);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BooksListFragment booksListFragment = (BooksListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("booksListFragment");
                if (booksListFragment == null) {
                    booksListFragment = new BooksListFragment();
                    booksListFragment.setCategory(booksList, genresList.get(position));
                    FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
                    ftrans.addToBackStack(null);
                    ftrans.replace(R.id.fragment_container, booksListFragment, "booksListFragment").commit();
                }
            }
        });
        return view;
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
        if (!genresBlacklist.contains(genre)) {
            this.genresBlacklist.add(genre);
            Collections.sort(genresBlacklist);
            if (Settings.getInstance().getGenresBlacklistState()) {
                ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_genreslist, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (Settings.getInstance().getGenresBlacklistState()) {
            menu.findItem(R.id.menu_genreslist_item_blacklist).setChecked(true);
        } else {
            menu.findItem(R.id.menu_genreslist_item_blacklist).setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_genreslist_item_blacklist:
                blackListGenres(item);
                break;
            case R.id.menu_genreslist_edit_blacklist:
                editBlackList();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void blackListGenres(MenuItem item) {
        if (item.isChecked()) {
            item.setChecked(false);
            Settings.getInstance().setGenresBlacklistState(false);
        } else {
            item.setChecked(true);
            Settings.getInstance().setGenresBlacklistState(true);
        }
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void editBlackList() {
        BlacklistEditorFragment blacklistEditorFragment = (BlacklistEditorFragment) getActivity().getSupportFragmentManager().findFragmentByTag("genresBlacklistEditorFragment");
        if (blacklistEditorFragment == null) {
            blacklistEditorFragment = new BlacklistEditorFragment();
            blacklistEditorFragment.setList(genresBlacklist);
            FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
            ftrans.addToBackStack(null);
            ftrans.replace(R.id.fragment_container, blacklistEditorFragment, "genresBlacklistEditorFragment").commit();
        }

    }


}

