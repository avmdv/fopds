package org.avmrus.fopds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.avmrus.fopds.inet.InetGetter;
import org.avmrus.fopds.inet.Reply;

import java.util.ArrayList;

public class MainFragment extends Fragment implements View.OnClickListener {

    private boolean isConnected, isReady, isProcessed;
    private ArrayList<Book> booksList;
    private String title, status;
    private int booksCount;

    private TextView textViewTitle, textViewStatus;
    private Button buttonGet, buttonList, buttonCommit;

    public MainFragment() {
        super();
        this.booksList = new ArrayList<>();
    }

    private void setTitle(String text) {
        this.title = text;
        textViewTitle.setText(this.title);
    }

    private void setStatus(String text) {
        this.status = text;
        textViewStatus.setText(this.status);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        this.isConnected = false;
        this.isReady = false;
        this.isProcessed = false;
        this.title = getString(R.string.main_fragment_title);
        this.status = getString(R.string.main_fragment_status);
        this.booksCount = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        createChildViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initChildViews();
        if (!isConnected) {
            connect();
        }
    }

    private void createChildViews(View view) {
        textViewTitle = view.findViewById(R.id.fragment_main_title);
        textViewStatus = view.findViewById(R.id.fragment_main_status);
        buttonGet = view.findViewById(R.id.fragment_main_button_get);
        buttonList = view.findViewById(R.id.fragment_main_button_list);
        buttonCommit = view.findViewById(R.id.fragment_main_button_commit);
    }

    private void initChildViews() {
        textViewTitle.setText(this.title);
        textViewStatus.setText(this.status);
        buttonGet.setEnabled(isConnected);
        buttonGet.setOnClickListener(this);
        buttonList.setEnabled(isReady);
        buttonList.setOnClickListener(this);
//        buttonCommit.setEnabled(isProcessed);
//        buttonCommit.setOnClickListener(this);
        buttonCommit.setVisibility(View.GONE);
    }

    private void fetchBooksList(final int page) {
        String siteUrl = Settings.getInstance().getSiteUrl();
        Log.d(Settings.LOG_TAG, "fetching page " + page);
        String url = siteUrl + "/opds/new/" + page + "/new";
        InetGetter inetFetcher = new InetGetter();
        inetFetcher.getUrl(url, new Reply.UrlListener() {
            @Override
            public void onSuccess(byte[] reply) {
                parseResponse(reply, page);
            }
        }, new Reply.ErrorListener() {
            @Override
            public void onError(String error) {
                setStatus(error);
            }
        });
    }

    private void parseResponse(byte[] response, int page) {
        OpdsParser parser = new OpdsParser(response);
        ArrayList<Book> partList = parser.getBooksList();
        if (partList.size() > 0) {
            this.booksList.addAll(partList);
            page++;
            fetchBooksList(page);
            setStatus(getString(R.string.fetched_books) + " " + booksList.size() + "/" + booksCount);
        } else {
            isReady = true;
            buttonList.setEnabled(true);
        }
    }

    private void connect() {
        String url = Settings.getInstance().getSiteUrl() + "/opds/new";
        InetGetter fetcher = new InetGetter();
        fetcher.getUrl(url, new Reply.UrlListener() {
            @Override
            public void onSuccess(byte[] response) {
                onSuccessfulConnect(response);
            }
        }, new Reply.ErrorListener() {
            @Override
            public void onError(String error) {
                setStatus(error);
            }
        });

    }

    private void onSuccessfulConnect(byte[] data) {
        isConnected = true;
        OpdsParser opdsParser = new OpdsParser(data);
        setTitle(opdsParser.getTitle());
        booksCount = opdsParser.getBooksCount();
        setStatus(booksCount + " " + getString(R.string.main_fragment_status1));
        buttonGet.setEnabled(isConnected);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_main_button_get:
                onClickButtonGet();
                break;
            case R.id.fragment_main_button_list:
                onClickButtonList();
                break;
            case R.id.fragment_main_button_commit:
                onClickButtonCommit();
                break;
            default:
                break;
        }
    }

    private void onClickButtonCommit() {

    }

    private void onClickButtonGet() {
        booksList.clear();
        this.isReady = false;
        buttonList.setEnabled(false);
        fetchBooksList(0);

    }

    private void onClickButtonList() {
        GenresListFragment genresListFragment = (GenresListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("genresListFragment");
        if (genresListFragment == null) {
            genresListFragment = new GenresListFragment();
            genresListFragment.setBooksList(booksList);
            FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
            ftrans.addToBackStack(null);
            ftrans.replace(R.id.fragment_container, genresListFragment, "genresListFragment").commit();
        }
    }
}
