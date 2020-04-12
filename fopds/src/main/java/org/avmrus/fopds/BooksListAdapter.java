package org.avmrus.fopds;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.avmrus.fopds.inet.InetGetter;
import org.avmrus.fopds.inet.InetPriority;
import org.avmrus.fopds.inet.Reply;

import java.util.List;

public class BooksListAdapter extends ArrayAdapter<Book> {

    private List<Book> booksList;
    private String genre;
    private TextView textViewAuthor, textViewTitle;
    private ImageView imageViewCover;

    public BooksListAdapter(List<Book> booksList, String category) {
        super(Settings.getInstance().getContext(), 0, booksList);
        this.booksList = booksList;
        this.genre = category;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(Settings.getInstance().getContext()).inflate(R.layout.item_books_list, parent, false);
        }
        LinearLayout listLayout = listItem.findViewById(R.id.item_bookslist_item);
        createChildViews(listItem);
        Book book = booksList.get(position);
        if (isBookOfGenre(book, genre)) {
            initChildViews(book);
            listLayout.setVisibility(View.VISIBLE);
        } else {
            listLayout.setVisibility(View.GONE);
        }
        return listItem;
    }

    private Boolean isBookOfGenre(Book book, String genre) {
        Boolean result = false;
        for (String category : book.getCategories()) {
            if (category.equalsIgnoreCase(genre)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void createChildViews(View view) {
        this.textViewAuthor = view.findViewById(R.id.item_bookslist_author);
        this.textViewTitle = view.findViewById(R.id.item_bookslist_title);
        this.imageViewCover = view.findViewById(R.id.item_bookslist_cover);
    }

    private void initChildViews(final Book book) {
        if (isBookOfGenre(book, genre)) {
            textViewTitle.setText(book.getTitle());
            if (book.getAuthors().size() > 0) {
                textViewAuthor.setText(book.getFirstAuthor().getShortName());
            }
            if (book.getCover() != null) {
                imageViewCover.setImageBitmap(book.getCover());
            } else {
                if (book.getCoverUrl() != null) {
                    String url = Settings.getInstance().getSiteUrl() + book.getCoverUrl();
                    InetGetter inetGetter = new InetGetter();
                    inetGetter.getImage(InetPriority.PRIORITY_NORMAL, url, new Reply.ImageListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            book.setCover(bitmap);
                            imageViewCover.setImageBitmap(bitmap);
                            notifyDataSetChanged();
                        }
                    }, new Reply.ErrorListener() {
                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }

        }
    }
}
