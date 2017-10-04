package com.example.android.techeval.Fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.techeval.R;
import com.example.android.techeval.adapters.ItemRecyclerAdapter;
import com.example.android.techeval.adapters.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is divided into 5 regions to demonstrate the Scenario 1 which has 5 points
 */
public class ScenarioOneFragment extends Fragment {
    final static int NUM_OF_POINT_ONE_ITEM = 5;
    static final String POINT_ONE_INDEX = "pointOneIndex";
    static final String POINT_FIVE_INDEX = "pointFiveIndex";

    private int mPointOneIndex = -1;
    private int mPointFiveIndex = -1;

    View mRootView;
    TextView mPointFourTextView;
    View mPointFiveLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_scenario_one, container, false);
        mPointFourTextView = (TextView) mRootView.findViewById(R.id.tvPointFour);
        mPointFiveLayout = mRootView.findViewById(R.id.layoutPointFive);

        if (savedInstanceState != null) {
            // retrieve indexes after screen rotation
            mPointOneIndex = savedInstanceState.getInt(POINT_ONE_INDEX);
            mPointFiveIndex = savedInstanceState.getInt(POINT_FIVE_INDEX);
        }

        setupRecyclerView();
        setupViewPagerView();
        setupPointFiveButtons();

        if (mPointFiveIndex >= 0) {
            updatePointFiveRegion(mPointFiveIndex);
        }
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save indexes to keep selected items in the screen even after screen is rotated.
        outState.putInt(POINT_ONE_INDEX, mPointOneIndex);
        outState.putInt(POINT_FIVE_INDEX, mPointFiveIndex);
    }

    /**
     * Setup the RecyclerView in the point 1 region with adapter
     */
    private void setupRecyclerView() {
        RecyclerView pointOneRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rvPointOne);
        assert pointOneRecyclerView != null;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pointOneRecyclerView.setLayoutManager(layoutManager);

        ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(getContext(), NUM_OF_POINT_ONE_ITEM, getScreenWidth() / 3);
        adapter.setItemOnClickListener(new ItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, String itemName) {
                mPointOneIndex = position;
                updatePointFourRegion(position);
            }
        });
        pointOneRecyclerView.setAdapter(adapter);
        if (mPointOneIndex >= 0) {
            pointOneRecyclerView.scrollToPosition(mPointOneIndex);
            updatePointFourRegion(mPointOneIndex);
        }
    }

    /**
     * Setup the ViewPager in the point 2 region with adapter
     */
    private void setupViewPagerView() {

        ViewPager pointTwoPager = (ViewPager) mRootView.findViewById(R.id.viewpagerPointTwo);
        // Use RadioGroup with custom button drawables to implement carousel-indicators
        final RadioGroup radioGroup = (RadioGroup) mRootView.findViewById(R.id.radioGroupPointTwo);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(PageFragment.initialise(0));
        fragments.add(PageFragment.initialise(1));
        fragments.add(PageFragment.initialise(2));
        fragments.add(PageFragment.initialise(3));

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        pointTwoPager.setAdapter(pagerAdapter);
        pointTwoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Change the checked radio button
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.radioButton1);
                        break;

                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;

                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;

                    case 3:
                        radioGroup.check(R.id.radioButton4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Setup buttons in the point 5 region
     */
    private void setupPointFiveButtons() {

        final View pointFiveLayout = mRootView.findViewById(R.id.layoutPointFive);
        Button redButton = (Button) mRootView.findViewById(R.id.btnRed);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePointFiveRegion(0);
            }
        });

        Button blueButton = (Button) mRootView.findViewById(R.id.btnBlue);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePointFiveRegion(1);
            }
        });

        Button greenButton = (Button) mRootView.findViewById(R.id.btnGreen);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePointFiveRegion(2);
            }
        });

    }

    /**
     * Update the TextView in point 4 region depending on which item is selected from point 1.
     *
     * @param position selected item position of the RecyclerView in point 1
     */
    private void updatePointFourRegion(int position) {
        mPointFourTextView.setText(getContext().getString(R.string.format_point_one_item, position + 1));
    }


    /**
     * Update the background color of point 5 region
     *
     * @param index - 0: colorRed, 1: colorBlue, 2:colorGreen
     */
    private void updatePointFiveRegion(int index) {
        switch (index) {
            case 0:
                mPointFiveLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                break;

            case 1:
                mPointFiveLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                break;

            case 2:
                mPointFiveLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                break;
        }
        mPointFiveIndex = index;
    }

    /**
     * Get the device's screen width to calculate point 1's item
     *
     * @return the screen width of the device
     */
    int getScreenWidth() {
        Point size = new Point();
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        d.getSize(size);
        if (size.x < size.y) {
            return size.x;
        } else {
            return size.y;
        }
    }
}
