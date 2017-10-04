package com.example.android.techeval.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.techeval.R;

/**
 * This fragment is shown on the ViewPager in Point 2
 */
public class PageFragment extends Fragment {
    final static String FRAGMENT_VALUE = "fragmentValue";
    int fragVal;

    /**
     * Create a PageFragment instance
     *
     * @param val will be used as page number
     * @return instance of PageFragment
     */
    public static PageFragment initialise(int val) {
        PageFragment fragment = new PageFragment();

        Bundle args = new Bundle();
        args.putInt(FRAGMENT_VALUE, val);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt(FRAGMENT_VALUE) : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
        TextView pageNameTextView = (TextView) rootView.findViewById(R.id.tvPageName);
        pageNameTextView.setText(getString(R.string.format_point_two_page, fragVal + 1));

        switch (fragVal) {
            case 0:
                rootView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGray));
                pageNameTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                break;

            case 1:
                rootView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                pageNameTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                break;

            case 2:
                rootView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                pageNameTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                break;

            case 3:
                rootView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                pageNameTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                break;

        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.format_page_pressed, fragVal + 1), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}