package com.HVEPYC.hvepyc_onhover_googlemap_implementation;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class testingUseInFragment extends Fragment{

    ArrayList<LatLngDistanceKeeper> LatLngs = new ArrayList<>();
    private ImageView existingImageView;
    private TextView existingTextView;
    private int NewImageViewID;
    private int NewTextViewID;
    private static int CIRCLE_RADIUS = 50;

    public testingUseInFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testing_use_in, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        //loading them initially
        this.existingImageView = view.findViewById(R.id.markerDesc1);
        this.existingTextView = view.findViewById(R.id.markerDesc2);

        //Making them invisible:
        existingImageView.setVisibility(View.GONE);
        existingTextView.setVisibility(View.GONE);

    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {

            //Drawing a circle on a certain coordinate
            LatLng london = new LatLng(51.510032748574915, -0.11810765544119338);

            // Position the map's camera on London (for Demonstration Purposes)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london, 15));

            Button button = getView().findViewById(R.id.dropPin);

            //Trying to make a popup show when hovering over a circle
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Finds location at center of screen
                    LatLng location = googleMap.getCameraPosition().target;
                    //draws circle there
                    Circle circle = googleMap.addCircle(new CircleOptions().center(location).radius(CIRCLE_RADIUS).fillColor(Color.argb(50, 68, 21, 249)).strokeWidth(5));

                    //Creates an ImageView and a TextView for its own description.
                    HashMap<ImageView, Integer> a = returnNewImageView();
                    HashMap<TextView, Integer> b = returnNewTextView();

                    HashMap.Entry<ImageView, Integer> entry = a.entrySet().iterator().next();
                    ImageView IVforthisLatLong = entry.getKey();
                    int IDofIV = entry.getValue();

                    HashMap.Entry<TextView, Integer> entry2 = b.entrySet().iterator().next();
                    TextView TVforthisLatLong = entry2.getKey();
                    int IDofTV = entry2.getValue();

                    //Add these to the revamped LatLngDistanceKeeper object
                    //Saves it to an arraylist for runtime reference
                    LatLngs.add(new LatLngDistanceKeeper(location, IVforthisLatLong, IDofIV, TVforthisLatLong, IDofTV));
                }
            });

            /**Code for making screen elements appear or disappear depending on whether marker is
             hovering on the given circle.
             An implementation to make up for the lack of an OnHover method for GoogleMap Circles.
             **/
            googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    LatLng location = googleMap.getCameraPosition().target;


                    if (LatLngs.size()>0) {
                        //Keep updating all distance values between all spheres for everything

                        for (LatLngDistanceKeeper i: LatLngs) {
                            //First sets distance
                            double dist = SphericalUtil.computeDistanceBetween(location, i.getLatLng());
                            i.setDistance(dist);

                            //Then checks if a circle is in range (range being radius of the circle)
                            if (i.getDistance() < CIRCLE_RADIUS) {

                                ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
//                                System.out.println("You are in range"+i.returnCounter());
                                if (doesImageViewWithThisIDExist(i.getIVID()) == false && doesTextViewWithThisIDExist(i.getTVID()) == false) {
                                    ImageView newIV = i.getIV();
                                    constraintLayout.addView(newIV);
                                    newIV.bringToFront();
                                    newIV.setVisibility(View.VISIBLE);
                                    TextView newTV = i.getTV();
                                    newTV.setText(i.getStoredText());
                                    constraintLayout.addView(newTV);
                                    newTV.bringToFront();
                                    newTV.setVisibility(View.VISIBLE);
                                }
                                else {
                                    ImageView existingIV = returnImageViewWithThisID(i.getIVID());
                                    TextView existingTV = returnTextViewWithThisID(i.getTVID());
                                    existingIV.setVisibility(View.VISIBLE);
                                    existingTV.setVisibility(View.VISIBLE);
                                }
                            }
                            else {

                                if (doesImageViewWithThisIDExist(i.getIVID()) && doesTextViewWithThisIDExist(i.getTVID())) {
                                    ImageView existingIV = returnImageViewWithThisID(i.getIVID());
                                    TextView existingTV = returnTextViewWithThisID(i.getTVID());
                                    existingIV.setVisibility(View.GONE);
                                    existingTV.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                }
            });
        }
    };

    private HashMap<ImageView,Integer> returnNewImageView() {

        //For imageview first:
        ImageView newImageView = new ImageView(requireContext());
        int newID = View.generateViewId();
        newImageView.setId(newID);
        newImageView.setImageResource(com.google.android.gms.base.R.drawable.common_google_signin_btn_text_light_normal_background);
        ConstraintLayout.LayoutParams existingImageLayoutParams = (ConstraintLayout.LayoutParams) existingImageView.getLayoutParams();
        ConstraintLayout.LayoutParams newImageLayoutParams = new ConstraintLayout.LayoutParams(existingImageLayoutParams.width, existingImageLayoutParams.height);

        //Copying all old layout params over
        newImageLayoutParams.width = existingImageLayoutParams.width;
        newImageLayoutParams.height = existingImageLayoutParams.height;
        newImageLayoutParams.bottomMargin = existingImageLayoutParams.bottomMargin;
        newImageLayoutParams.bottomToBottom = existingImageLayoutParams.bottomToBottom;
        newImageLayoutParams.endToEnd = existingImageLayoutParams.endToEnd;
        newImageLayoutParams.horizontalBias = existingImageLayoutParams.horizontalBias;
        newImageLayoutParams.startToStart = existingImageLayoutParams.startToStart;
        newImageView.setLayoutParams(newImageLayoutParams);
        newImageView.setVisibility(View.GONE);

        HashMap<ImageView, Integer> returnval = new HashMap<>();
        returnval.put(newImageView, newID);
        return returnval;
    }

    private HashMap<TextView, Integer> returnNewTextView() {
        //For textview first:
        TextView newTextView = new TextView(requireContext());
        newTextView.setText("Some default Value");
        int anotherNewID = View.generateViewId();
        newTextView.setId(anotherNewID);
        ConstraintLayout.LayoutParams existingTextLayoutParams = (ConstraintLayout.LayoutParams) existingTextView.getLayoutParams();
        ConstraintLayout.LayoutParams newTextLayoutParams = new ConstraintLayout.LayoutParams(existingTextLayoutParams.width, existingTextLayoutParams.height);

        //Copying all old layout params over
        newTextLayoutParams.width = existingTextLayoutParams.width;
        newTextLayoutParams.height = existingTextLayoutParams.height;
        newTextLayoutParams.bottomMargin = existingTextLayoutParams.bottomMargin;
        newTextLayoutParams.topToBottom = existingTextLayoutParams.topToBottom;
        newTextLayoutParams.startToStart = existingTextLayoutParams.startToStart;
        newTextLayoutParams.endToEnd = existingTextLayoutParams.endToEnd;
        newTextLayoutParams.bottomToBottom = existingTextLayoutParams.bottomToBottom;
        newTextLayoutParams.horizontalBias = existingTextLayoutParams.horizontalBias;
        newTextLayoutParams.verticalBias = existingTextLayoutParams.verticalBias;

        //Setting layoutparams for the new TextView
        newTextView.setLayoutParams(newTextLayoutParams);
        newTextView.setVisibility(View.GONE);
        newTextView.setGravity(Gravity.CENTER);

        HashMap<TextView, Integer> returnval = new HashMap<>();
        returnval.put(newTextView, anotherNewID);
        return returnval;
    }

    private boolean doesImageViewWithThisIDExist(int id) {
        boolean returnval = false;
        ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
        for (int i=0; i<constraintLayout.getChildCount(); i++) {
            View childView = constraintLayout.getChildAt(i);
            if (childView.getId() == id && childView instanceof ImageView) {
                returnval = true;
                break;
            }
        }
        return returnval;
    }

    private boolean doesTextViewWithThisIDExist(int id) {
        boolean returnval = false;
        ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
        for (int i=0; i<constraintLayout.getChildCount(); i++) {
            View childView = constraintLayout.getChildAt(i);
            if (childView.getId() == id && childView instanceof TextView) {
                returnval = true;
                break;
            }
        }
        return returnval;
    }

    private ImageView returnImageViewWithThisID(int id) {
        ImageView returnval = null;
        ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
        for (int i=0; i<constraintLayout.getChildCount(); i++) {
            View childView = constraintLayout.getChildAt(i);
            if (childView.getId() == id && childView instanceof ImageView) {
                returnval = (ImageView) childView;
                break;
            }
        }
        return returnval;
    }

    private TextView returnTextViewWithThisID(int id) {
        TextView returnval = null;
        ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
        for (int i=0; i<constraintLayout.getChildCount(); i++) {
            View childView = constraintLayout.getChildAt(i);
            if (childView.getId() == id && childView instanceof TextView) {
                returnval = (TextView) childView;
                break;
            }
        }
        return returnval;
    }

    private void addImageAndText(String newText) {
        if (existingTextView != null && existingImageView != null) {

            ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);

            //For imageview first:
            ImageView newImageView = new ImageView(requireContext());
            int newID = View.generateViewId();
            newImageView.setId(newID);
            this.NewImageViewID = newID;
            newImageView.setImageResource(com.google.android.gms.base.R.drawable.common_google_signin_btn_text_light_normal_background);
            ConstraintLayout.LayoutParams existingImageLayoutParams = (ConstraintLayout.LayoutParams) existingImageView.getLayoutParams();
            ConstraintLayout.LayoutParams newImageLayoutParams = new ConstraintLayout.LayoutParams(existingImageLayoutParams.width, existingImageLayoutParams.height);

            //Copying all old layout params over
            newImageLayoutParams.width = existingImageLayoutParams.width;
            newImageLayoutParams.height = existingImageLayoutParams.height;
            newImageLayoutParams.bottomMargin = existingImageLayoutParams.bottomMargin;
            newImageLayoutParams.bottomToBottom = existingImageLayoutParams.bottomToBottom;
            newImageLayoutParams.endToEnd = existingImageLayoutParams.endToEnd;
            newImageLayoutParams.horizontalBias = existingImageLayoutParams.horizontalBias;
            newImageLayoutParams.startToStart = existingImageLayoutParams.startToStart;

            //setting layoutparams for the new imageView
            newImageView.setLayoutParams(newImageLayoutParams);
            constraintLayout.addView(newImageView);
            newImageView.bringToFront();


            //For textview first:
            TextView newTextView = new TextView(requireContext());
            newTextView.setText(newText);
            int anotherNewID = View.generateViewId();
            newTextView.setId(anotherNewID);
            this.NewTextViewID = anotherNewID;
            ConstraintLayout.LayoutParams existingTextLayoutParams = (ConstraintLayout.LayoutParams) existingTextView.getLayoutParams();
            ConstraintLayout.LayoutParams newTextLayoutParams = new ConstraintLayout.LayoutParams(existingTextLayoutParams.width, existingTextLayoutParams.height);

            //Copying all old layout params over
            newTextLayoutParams.width = existingTextLayoutParams.width;
            newTextLayoutParams.height = existingTextLayoutParams.height;
            newTextLayoutParams.bottomMargin = existingTextLayoutParams.bottomMargin;
            newTextLayoutParams.topToBottom = existingTextLayoutParams.topToBottom;
            newTextLayoutParams.startToStart = existingTextLayoutParams.startToStart;
            newTextLayoutParams.endToEnd = existingTextLayoutParams.endToEnd;
            newTextLayoutParams.bottomToBottom = existingTextLayoutParams.bottomToBottom;
            newTextLayoutParams.horizontalBias = existingTextLayoutParams.horizontalBias;
            newTextLayoutParams.verticalBias = existingTextLayoutParams.verticalBias;

            //Setting layoutparams for the new TextView
            newTextView.setLayoutParams(newTextLayoutParams);
            constraintLayout.addView(newTextView);
            newTextView.bringToFront();
        }
    }

    private void removeImageAndText() {
        //Check if they both exist first:
        if (doesDescImageViewExist() && doesDescTextViewExist()) {
            ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
            int textviewID = this.NewTextViewID;
            int imageviewID = this.NewImageViewID;
            TextView oldtextview = constraintLayout.findViewById(textviewID);
            ImageView oldimageview = constraintLayout.findViewById(imageviewID);
            if (oldtextview != null && oldimageview != null) {
                constraintLayout.removeView(oldtextview);
                constraintLayout.removeView(oldimageview);
            }
        }
    }

    //To check whether the ImageView and the TextView exist:
    private boolean doesDescImageViewExist() {
        boolean returnval = false;
        ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
        for (int i=0; i<constraintLayout.getChildCount(); i++) {
            View childView = constraintLayout.getChildAt(i);
            if (childView.getId() == this.NewImageViewID && childView instanceof ImageView) {
                returnval = true;
                break;
            }
        }
        return returnval;
    }

    private boolean doesDescTextViewExist() {
        boolean returnval = false;
        ConstraintLayout constraintLayout = requireView().findViewById(R.id.theRealConstraintLayout);
        for (int i=0; i<constraintLayout.getChildCount(); i++) {
            View childView = constraintLayout.getChildAt(i);
            if (childView.getId() == this.NewTextViewID && childView instanceof TextView) {
                returnval = true;
                break;
            }
        }
        return returnval;
    }
}

//References used here:
//https://stackoverflow.com/a/51024906