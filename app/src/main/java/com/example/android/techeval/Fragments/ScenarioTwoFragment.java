package com.example.android.techeval.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.techeval.LocationInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.example.android.techeval.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This fragment demonstrates the Scenario 2 which has 3 points
 */
public class ScenarioTwoFragment extends Fragment
        implements OnMapReadyCallback {
    private final static String IS_JSON_LOADED = "isJsonLoaded";
    private final static String TRANSPORT_LIST_JSON = "transportListJson";
    private final static String TRANSPORT_SELECTED_INDEX = "transportSelectedIndex";

    List<LocationInfo> mTransportList;
    boolean mIsJsonLoaded = false;
    LocationInfo mSelectedTransport;
    int mSelectedIndex = -1;
    View mRootView;
    Spinner mPointOneSpinner;
    TextView mCarTimeTextView;
    TextView mTrainTimeTextView;
    Button mNavigateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_scenario_two, container, false);

        if (savedInstanceState != null) {
            // retrieve the transport information list from JSON string using Gson if the json exists in savedInstanceState
            mIsJsonLoaded = savedInstanceState.getBoolean(IS_JSON_LOADED);
            if (mIsJsonLoaded) {
                String transportListJson = savedInstanceState.getString(TRANSPORT_LIST_JSON);
                LocationInfo[] arrayTransport = null;
                arrayTransport = new Gson().fromJson(transportListJson, LocationInfo[].class);
                mTransportList = new ArrayList<LocationInfo>(Arrays.asList(arrayTransport));
                mSelectedIndex = savedInstanceState.getInt(TRANSPORT_SELECTED_INDEX);
            }
        }

        mCarTimeTextView = (TextView) mRootView.findViewById(R.id.tvCarTime);
        mTrainTimeTextView = (TextView) mRootView.findViewById(R.id.tvTrainTime);
        mNavigateButton = (Button) mRootView.findViewById(R.id.btnNavigate);
        mNavigateButton.setEnabled(false);

        setupSpinner();
        setupGoogleMap();

        mNavigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTransport != null) {

                    // start the Google map for navigation with the selected information.
                    LocationInfo.Location location = mSelectedTransport.getLocation();
                    if (!TextUtils.isEmpty(location.getLatitude())
                            && !TextUtils.isEmpty(location.getLatitude())) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location.getLatitude() + "," + location.getLongitude());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(getContext(), R.string.navigation_launch_error, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the transport information list as JSON string using Gson if the json was already loaded from the Remote server.
        outState.putBoolean(IS_JSON_LOADED, mIsJsonLoaded);
        if (mIsJsonLoaded) {
            Gson gson = new Gson();
            String transportListJson = gson.toJson(mTransportList);
            outState.putString(TRANSPORT_LIST_JSON, transportListJson);
            outState.putInt(TRANSPORT_SELECTED_INDEX, mSelectedIndex);
        }
    }

    /**
     * Setup the Spinner in point 1 using AsyncTask
     */
    private void setupSpinner() {
        mPointOneSpinner = (Spinner) mRootView.findViewById(R.id.spinnerTransport);
        updateTransportInfo(null);

        if (mIsJsonLoaded) {
            setupSpinnerAdapter(mSelectedIndex);

        } else {
            new JsonLoadTask().execute();
        }
    }

    /**
     * Load the Json data from the remote server asynchronously
     */
    private class JsonLoadTask extends AsyncTask<Void, Void, ArrayList<LocationInfo>> {

        @Override
        protected ArrayList<LocationInfo> doInBackground(Void... params) {

            ArrayList<LocationInfo> transportList = new ArrayList<LocationInfo>();
            LocationInfo[] arrayTransport = null;
            try {
                URL url = new URL(getString(R.string.locationJsonUrl));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader buffedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"), 8);

                // Convert the Json date to LocationInfo array.
                Gson gson = new Gson();
                arrayTransport = gson.fromJson(buffedReader, LocationInfo[].class);
                for (LocationInfo locationInfo : arrayTransport) {
                    transportList.add(locationInfo);
                }
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            return transportList;
        }

        @Override
        protected void onPostExecute(ArrayList<LocationInfo> result) {
            if (result == null) {
                return;
            }

            // Prevent unnecessary JsonLoadTask execution by keeping Transport information list
            mIsJsonLoaded = true;
            mTransportList = result;
            setupSpinnerAdapter(0);
        }
    }

    /**
     * Setup the Adapter for the spinner in point 1 to show the transport information
     */
    private void setupSpinnerAdapter(int selectedIndex) {

        List<String> nameList = new ArrayList<>();
        for (LocationInfo locationInfo : mTransportList) {
            nameList.add(locationInfo.getName());
        }

//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nameList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mRootView.getContext(), R.layout.item_spinner_location, nameList);
        mPointOneSpinner.setAdapter(adapter);

        mPointOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (mTransportList.size() > position) {
                    // show the selected information onto point 2's TextViews and Google map
                    mSelectedIndex = position;
                    mSelectedTransport = mTransportList.get(position);
                    updateTransportInfo(mSelectedTransport);

                    LocationInfo.Location location = mSelectedTransport.getLocation();
                    if (!TextUtils.isEmpty(location.getLatitude())
                            && !TextUtils.isEmpty(location.getLatitude())
                            && mGoogleMap != null) {
                        double latitude = Double.parseDouble(location.getLatitude());
                        double longitude = Double.parseDouble(location.getLongitude());

                        // Add a marker with name and move the camera
                        LatLng coordinates = new LatLng(latitude, longitude);
                        mGoogleMap.addMarker(new MarkerOptions().position(coordinates).title(mSelectedTransport.getName()));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                    }

                    if (mGoogleMap != null) {
                        mNavigateButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSelectedTransport = null;
                updateTransportInfo(null);
                mNavigateButton.setEnabled(false);
            }
        });

        mPointOneSpinner.setSelection(selectedIndex);
    }

    /**
     * Update the TextViews in the point 2
     */
    private void updateTransportInfo(@Nullable LocationInfo locationInfo) {
        if (locationInfo == null) {
            mCarTimeTextView.setVisibility(View.GONE);
            mTrainTimeTextView.setVisibility(View.GONE);
            return;
        }

        LocationInfo.FromCentral fromCentral = locationInfo.getFromcentral();
        if (!TextUtils.isEmpty(fromCentral.getCar())) {
            mCarTimeTextView.setText(getString(R.string.format_car_mins, fromCentral.getCar()));
            mCarTimeTextView.setVisibility(View.VISIBLE);
        } else {
            mCarTimeTextView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(fromCentral.getTrain())) {
            mTrainTimeTextView.setText(getString(R.string.format_train_mins, fromCentral.getTrain()));
            mTrainTimeTextView.setVisibility(View.VISIBLE);
        } else {
            mTrainTimeTextView.setVisibility(View.GONE);
        }
    }

    // region Google Map
    private GoogleMap mGoogleMap;

    private void setupGoogleMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng coordinates = new LatLng(-34, 151);  // set as Sydney
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mGoogleMap.setMinZoomPreference(12f);
        if (mSelectedTransport != null) {
            mNavigateButton.setEnabled(true);
        }
    }
// endregion
}
