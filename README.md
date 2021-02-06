# MuftPlacePicker  [![](https://jitpack.io/v/bullheadandplato/MuftPlacePicker.svg)](https://jitpack.io/#bullheadandplato/MuftPlacePicker)

OpenStreetMap Place Picker for Android. Based on OSMdroid

<div>
  <img src="https://github.com/bullheadandplato/MuftPlacePicker/raw/master/screens/search.png" width="300" height="600"/>
  <img src="https://github.com/bullheadandplato/MuftPlacePicker/raw/master/screens/map.png" width="300" height="600"/>
  <img src="https://github.com/bullheadandplato/MuftPlacePicker/raw/master/screens/dialog.png" width="300" height="600"/>
</div>

## How To Use
### STEP 1
Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:
```gradle
dependencies {
      implementation 'com.github.bullheadandplato:MuftPlacePicker:1.0.0'
}
```

### STEP 2
#### Show Place Picker
```
 PlacePicker.show(PlacePickerUiOptions.builder()
                                     .primaryColor(R.color.purple_700)
                                     .secondaryColor(R.color.white)
                                     .searchTextColor(R.color.black)
                                     .build(),
                             PlacePickerMapOptions.builder()
                                     .zoom(16)
                                     .build(),
                             place -> {
                                 placeTextView.setText(place.getDisplayName());
                             },
                             getSupportFragmentManager());
```
