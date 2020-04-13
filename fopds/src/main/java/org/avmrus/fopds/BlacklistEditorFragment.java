package org.avmrus.fopds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BlacklistEditorFragment extends ListFragment {
    private ArrayList<String> blacklist;

    public void setList(ArrayList<String> list) {
        this.blacklist = list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, blacklist);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_genreslist_context, menu);
        menu.findItem(R.id.menu_genreslist_item_add).setEnabled(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_genreslist_item_add:
                break;
            case R.id.menu_genreslist_item_remove:
                removeGenreToBlacklist(info.position);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void removeGenreToBlacklist(int position) {
        blacklist.remove(position);
        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }

}
