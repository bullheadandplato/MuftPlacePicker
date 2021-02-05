package com.bullhead.android.muftplacepicker.ui;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bullhead.android.muftplacepicker.PlacePickerMapOptions;
import com.bullhead.android.muftplacepicker.PlacePickerUiOptions;
import com.bullhead.android.muftplacepicker.R;
import com.bullhead.android.muftplacepicker.api.ApiProvider;
import com.bullhead.android.muftplacepicker.databinding.DialogFullscreenBinding;
import com.bullhead.android.muftplacepicker.domain.Place;
import com.bullhead.android.muftplacepicker.ui.search.SearchAdapter;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.UI_MODE_SERVICE;
import static android.view.View.VISIBLE;

@SuppressWarnings({"WeakerAccess"})
public class PlacePicker extends DialogFragment {
    private static final String TAG = PlacePicker.class.getSimpleName();

    private PlacePickerUiOptions    options;
    private PlacePickerMapOptions   mapOptions;
    private IMapController          mapController;
    private Consumer<Place>         selectListener;
    private Marker                  tapMarker;
    private Disposable              disposable;
    private DialogFullscreenBinding binding;

    @NonNull
    public static PlacePicker show(@NonNull PlacePickerUiOptions options,
                                   @NonNull PlacePickerMapOptions mapOptions,
                                   @Nullable Consumer<Place> listener,
                                   @NonNull FragmentManager fm) {
        Bundle args = new Bundle();
        args.putSerializable("options", options);
        args.putSerializable("mapOptions", mapOptions);
        PlacePicker placePicker = new PlacePicker();
        placePicker.setArguments(args);
        placePicker.show(fm, "terms");
        placePicker.selectListener = listener;
        return placePicker;
    }

    private Context context;
    private boolean nightMode;

    public void setSelectListener(@Nullable Consumer<Place> selectListener) {
        this.selectListener = selectListener;
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
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapController = binding.mapView.getController();
        mapController.setZoom(mapOptions.getZoom());
        binding.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        binding.mapView.setMultiTouchControls(true);
        mapController.setCenter(mapOptions.getCenter());
        final MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                updateMarker(p);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        binding.mapView.getOverlays().add(new MapEventsOverlay(mReceive));
    }

    private void updateMarker(GeoPoint position) {
        if (tapMarker == null) {
            tapMarker = new Marker(binding.mapView);
            tapMarker.setDefaultIcon();
            binding.mapView.getOverlays().add(tapMarker);
        }
        tapMarker.setPosition(position);
        binding.mapView.invalidate();
        loadPlace(position);
    }

    private void loadPlace(@NonNull GeoPoint point) {
        if (disposable != null) {
            disposable.dispose();
        }
        toggleProgress(true);
        disposable = ApiProvider.getInstance().reverse(point.getLatitude(), point.getLongitude())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((ev, e) -> toggleProgress(false))
                .subscribe(place -> {
                    binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    SearchAdapter adapter = new SearchAdapter(new ArrayList<Place>() {{
                        add(place);
                    }});
                    adapter.setListener((item, position) -> {
                        showSelectDialog(item);
                    });
                    binding.rv.setAdapter(adapter);
                    binding.rv.setVisibility(VISIBLE);
                }, error -> {

                });
    }

    private void toggleProgress(boolean show) {
        if (show) {
            binding.rv.setVisibility(View.GONE);
            binding.pb.setVisibility(VISIBLE);
        } else {
            binding.pb.setVisibility(View.GONE);
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
        binding = DialogFullscreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void showSelectDialog(Place place) {
        new SelectPlaceDialog(context, place, place1 -> {
            if (selectListener != null) {
                selectListener.accept(place1);
            }
            dismiss();
        }).show();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
        assert binding.toolbar.getNavigationIcon() != null;
        binding.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, options.getPrimaryColor()));
        binding.toolbar.setNavigationOnClickListener(view1 -> dismiss());
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(context, options.getSecondaryColor()));
        binding.toolbar.setTitle(options.getSearchTextColor());
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(context, options.getPrimaryColor()));
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
        binding.searchView.setTextColor(ContextCompat.getColor(context, options.getSearchTextColor()));
        binding.searchView.setBackgroundColor(ContextCompat.getColor(context, options.getSecondaryColor()));
        binding.searchView.setProgressColor(ContextCompat.getColor(context, options.getPrimaryColor()));
        binding.searchView.setPlaceSelectListener(this::showSelectDialog);
        binding.pb.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat
                .getColor(context, options.getPrimaryColor())));
        setupToolbar();
        fixMap();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onDestroyView() {
        if (disposable != null) {
            disposable.dispose();
        }
        binding = null;
        super.onDestroyView();
    }
}

