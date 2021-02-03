package com.bullhead.android.muftplacepicker.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bullhead.android.muftplacepicker.R;
import com.bullhead.android.muftplacepicker.api.ApiProvider;
import com.bullhead.android.muftplacepicker.ui.search.SearchAdapter;
import com.google.android.material.card.MaterialCardView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchView extends LinearLayout {
    private MaterialCardView   searchView;
    private EditText           etSearch;
    private ProgressBar        progressBar;
    private RecyclerView       recyclerView;
    private InputMethodManager inputMethodManager;
    private Disposable         disposable;


    public SearchView(Context context) {
        super(context);
        init(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.search_view, this, true);
        searchView   = findViewById(R.id.rootView);
        etSearch     = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar  = findViewById(R.id.progressBar);
        etSearch.requestFocus();
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            search();
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        });
        if (attrs != null) {
            TypedArray a = context
                    .obtainStyledAttributes(attrs, R.styleable.SearchView);
            if (a.hasValue(R.styleable.SearchView_searchBackgroundColor)) {
                searchView.setCardBackgroundColor(a
                        .getColorStateList(R.styleable.SearchView_searchBackgroundColor));
            }
            if (a.hasValue(R.styleable.SearchView_searchElevation)) {
                searchView.setCardElevation(a.getDimension(R.styleable.SearchView_searchElevation, 5f));
            }
            if (a.hasValue(R.styleable.SearchView_searchCornerRadius)) {
                searchView.setRadius(a.getDimension(R.styleable.SearchView_searchCornerRadius, 5f));
            }
            a.recycle();
        }
    }

    @NonNull
    public MaterialCardView getCardView() {
        return searchView;
    }

    public void focusSearch() {
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> {
                    etSearch.requestFocus();
                    inputMethodManager.toggleSoftInputFromWindow(etSearch.getWindowToken(),
                            InputMethodManager.SHOW_IMPLICIT, 0);
                }, 200);

    }

    private void search() {
        toggleProgress(false);
        String text = etSearch.getText().toString();
        if (disposable != null) {
            disposable.dispose();
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        toggleProgress(true);
        disposable = ApiProvider.getInstance()
                .search(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((event, error) -> toggleProgress(false))
                .subscribe(places -> {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new SearchAdapter(places));
                    recyclerView.setVisibility(VISIBLE);
                }, error -> {
                    Log.e("TAG", "onTextChanged: " + error.getLocalizedMessage());
                });
    }

    private void toggleProgress(boolean show) {
        if (show) {
            recyclerView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }


    public void setTextColor(int searchTextColor) {
        etSearch.setTextColor(searchTextColor);
        etSearch.setHintTextColor(ColorUtils.setAlphaComponent(searchTextColor, 100));
    }

    @Override
    public void setBackgroundColor(int color) {
        searchView.setCardBackgroundColor(color);
    }

    public void setProgressColor(int color) {
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(color));
    }
}
