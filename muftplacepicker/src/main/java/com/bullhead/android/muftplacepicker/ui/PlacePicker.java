package com.bullhead.android.muftplacepicker.ui;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bullhead.android.muftplacepicker.PlacePickerMapOptions;
import com.bullhead.android.muftplacepicker.PlacePickerUiOptions;
import com.bullhead.android.muftplacepicker.R;
import com.google.android.material.appbar.AppBarLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import static android.content.Context.UI_MODE_SERVICE;

@SuppressWarnings({"WeakerAccess"})
public class PlacePicker extends DialogFragment {
    private static final String TAG = PlacePicker.class.getSimpleName();

    private Toolbar               toolbar;
    private PlacePickerUiOptions  options;
    private PlacePickerMapOptions mapOptions;
    private MapView               mapView;
    private IMapController        mapController;


    private Context context;
    private boolean nightMode;

    @Nullable
    private CloseListener closeListener;

    @NonNull
    public static PlacePicker show(@NonNull PlacePickerUiOptions options,
                                   @NonNull PlacePickerMapOptions mapOptions,
                                   @NonNull FragmentManager fm) {
        Bundle args = new Bundle();
        args.putSerializable("options", options);
        args.putSerializable("mapOptions", mapOptions);
        PlacePicker placePicker = new PlacePicker();
        placePicker.setArguments(args);
        placePicker.show(fm, "terms");
        return placePicker;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setOsmdroidBasePath(getContext().getCacheDir());
        Configuration.getInstance().setUserAgentValue("muft-place-picker");
        if (getArguments() != null) {
            this.mapOptions = (PlacePickerMapOptions) getArguments().getSerializable("mapOptions");
            this.options    = (PlacePickerUiOptions) getArguments().getSerializable("options");
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    private void fixMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapController = mapView.getController();
        mapController.setZoom(mapOptions.getZoom());
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setMultiTouchControls(true);
        mapController.setCenter(mapOptions.getCenter());
    }

    public void setCloseListener(@Nullable CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (closeListener != null) {
            closeListener.onClose();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width  = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            int color = ContextCompat
                    .getColor(context, options.getSecondaryColor());
            dialog.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(color));
            if (!nightMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dialog.getWindow().setStatusBarColor(color);
                    dialog.getWindow().setNavigationBarColor(color);
                    dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialog.getWindow().setStatusBarColor(color);
                    dialog.getWindow().getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                dialog.getWindow().setNavigationBarColor(color);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fullscreen,
                container, false);
        setupViews(view);
        setupToolbar();
        return view;
    }

    private void setupViews(@NonNull View view) {
        toolbar = view.findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = view.findViewById(R.id.baseAppBar);
        SearchView   searchView   = view.findViewById(R.id.searchView);
        searchView.setTextColor(ContextCompat.getColor(context, options.getSearchTextColor()));
        searchView.setBackgroundColor(ContextCompat.getColor(context, options.getSecondaryColor()));
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
        assert toolbar.getNavigationIcon() != null;
        toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, options.getPrimaryColor()));
        toolbar.setNavigationOnClickListener(view1 -> dismiss());
        toolbar.setBackgroundColor(ContextCompat.getColor(context, options.getSecondaryColor()));
        toolbar.setTitle(options.getSearchTextColor());
        toolbar.setTitleTextColor(ContextCompat.getColor(context, options.getPrimaryColor()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        assert uiModeManager != null;
        nightMode = uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        fixMap();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }


    public interface CloseListener {
        void onClose();
    }
}
