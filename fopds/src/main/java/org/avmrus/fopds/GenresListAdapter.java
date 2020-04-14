package org.avmrus.fopds;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GenresListAdapter extends ArrayAdapter<String> {
    private ArrayList<String> genresList;
    private ArrayList<String> genresBlacklist;

    public GenresListAdapter(ArrayList<String> list, ArrayList<String> blacklist) {
        super(Settings.getInstance().getContext(), 0, list);
        this.genresList = list;
        this.genresBlacklist = blacklist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(Settings.getInstance().getContext()).inflate(R.layout.item_genres_list, parent, false);
        }
        LinearLayout listLayout = listItem.findViewById(R.id.item_genreslist_item);
        TextView textviewGenre = listItem.findViewById(R.id.item_genreslist_genre);
        textviewGenre.setText(genresList.get(position));
        if (Settings.getInstance().getBlacklist() && isGenreBlacklisted(genresList.get(position))) {
            listLayout.setVisibility(View.GONE);
        } else {
            listLayout.setVisibility(View.VISIBLE);
        }
        return listItem;
    }

    private boolean isGenreBlacklisted(String genre) {
        boolean result = false;
        if (this.genresBlacklist.contains(genre)) {
            result = true;
        }
        return result;
    }

}
