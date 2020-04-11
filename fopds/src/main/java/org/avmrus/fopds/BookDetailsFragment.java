package org.avmrus.fopds;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.avmrus.fopds.inet.InetDownloader;
import org.avmrus.fopds.inet.InetGetter;
import org.avmrus.fopds.inet.InetPriority;
import org.avmrus.fopds.inet.Reply;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BookDetailsFragment extends Fragment {
    private Book book;
    private Bitmap cover;
    private String downloadFormat;
    private Spinner availableFormats;

    private TextView textviewStatus;

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, null);
        initChildViews(view);
        return view;
    }

    private void initChildViews(View view) {
        textviewStatus = view.findViewById(R.id.tvStatus);
        initAuthor(view);
        initTitle(view);
        initContent(view);
        initCover(view);
        initAvailableFormats(view);
        ImageButton bookDownload = view.findViewById(R.id.bookDownload);
        bookDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBook();
            }
        });
    }

    private void initAuthor(View view) {
        TextView textViewAuthor = view.findViewById(R.id.bookAuthor);
        if (book.getAuthors().size() > 0) {
            textViewAuthor.setText(book.getAuthors().get(0).getShortName());
        }
    }

    private void initTitle(View view) {
        TextView textViewTitle = view.findViewById(R.id.bookTitle);
        textViewTitle.setText(book.getTitle());
    }

    private void initContent(View view) {
        TextView textViewContent = view.findViewById(R.id.bookContent);
        textViewContent.setText(book.getContent());
    }

    private void initCover(View view) {
        ImageView imageViewCover = view.findViewById(R.id.bookCover);
        if (book.getCoverUrl() != null) {
            fetchCover(book.getCoverUrl(), imageViewCover);
        }
        imageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cover != null) {
                    CoverFragment coverFragment = (CoverFragment) getActivity().getSupportFragmentManager().findFragmentByTag("coverFragment");
                    if (coverFragment == null) {
                        coverFragment = new CoverFragment();
                        coverFragment.setCover(cover);
                        FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
                        ftrans.addToBackStack(null);
                        ftrans.replace(R.id.fragmentContainer, coverFragment, "coverFragment").commit();

                    }
                }
            }
        });
    }

    private void initAvailableFormats(View view) {
        downloadFormat = Settings.getInstance().getPreferredFormat();
        availableFormats = view.findViewById(R.id.spinner);
        ArrayList<String> data = new ArrayList<>();
        if (book.getDownloadUrls().size() > 0) {
            for (int i = 0; i < book.getDownloadUrls().size(); i++) {
                data.add(book.getDownloadUrls().get(i).getDisplayType());
            }
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.getInstance().getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availableFormats.setAdapter(adapter);
        availableFormats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                downloadFormat = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchCover(String urlCover, final ImageView imageView) {
        String url = Settings.getInstance().getSiteUrl() + book.getCoverUrl();
        InetGetter inetGetter = new InetGetter();
        inetGetter.getImage(InetPriority.PRIORITY_HIGH, url, new Reply.ImageListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                cover = bitmap;
                imageView.setImageBitmap(bitmap);
            }
        }, new Reply.ErrorListener() {
            @Override
            public void onError(String error) {

            }
        });
    }

    private void downloadBook() {
        DownloadRecord record = getSelectedDownloadRecord(downloadFormat);
        String libraryPath = Settings.getInstance().getLibraryPath();
        String siteUrl = Settings.getInstance().getSiteUrl();
        String url = siteUrl + record.getUrl();
        final String path = Environment.getExternalStorageDirectory() + "/" + libraryPath + "/" + book.getFirstAuthor().getName();
//        final String filename = book.getTitle() + "." + record.getExtension();


        final InetDownloader inetDownloader = new InetDownloader();
        inetDownloader.setListeners(new Reply.UrlListener() {
            @Override
            public void onSuccess(byte[] reply) {
                try {
                    String filename = path + "/" + inetDownloader.getFilename();
                    Log.d(Settings.LOG_TAG, "filename= " + filename);
                    File directory = new File(path);
                    directory.mkdirs();
                    FileOutputStream fileOutputStream = new FileOutputStream(filename);
                    fileOutputStream.write(reply);
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Reply.ProgressListener() {
            @Override
            public void onProgress(int progress) {
                textviewStatus.setText(progress + "%");
            }
        }, new Reply.ErrorListener() {
            @Override
            public void onError(String error) {
                textviewStatus.setText(error);
            }
        });
        inetDownloader.execute(url);
    }

    private DownloadRecord getSelectedDownloadRecord(String format) {
        DownloadRecord selectedRecord = null;
        for (DownloadRecord record : book.getDownloadUrls()) {
            if (record.getDisplayType().equalsIgnoreCase(format)) {
                selectedRecord = record;
                break;
            }
        }
        return selectedRecord;
    }
}

