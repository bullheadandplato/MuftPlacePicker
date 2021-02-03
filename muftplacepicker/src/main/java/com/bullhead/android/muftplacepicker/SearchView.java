package com.bullhead.android.muftplacepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.card.MaterialCardView;

public class SearchView extends LinearLayout implements TextWatcher {
    private MaterialCardView   searchView;
    private EditText           etSearch;
    private QueryListener      listener;
    private InputMethodManager inputMethodManager;


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
        searchView = findViewById(R.id.rootView);
        etSearch   = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(this);
        etSearch.requestFocus();
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
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

    public void setListener(@Nullable QueryListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = etSearch.getText().toString();
        if (listener != null) {
            listener.onQuerySubmitted(etSearch.getText().toString());
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setTextColor(int searchTextColor) {
        etSearch.setTextColor(searchTextColor);
        etSearch.setHintTextColor(ColorUtils.setAlphaComponent(searchTextColor, 100));
    }


    public interface QueryListener {
        void onQuerySubmitted(@Nullable String text);
    }
}
