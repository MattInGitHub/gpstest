package com.matt.gpstest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int mStackLevel = 1;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        initLoc();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        bundle = savedInstanceState;
        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.new_fragment);
        Button button1 = (Button)findViewById(R.id.delete_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addFragmentToStack();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showStack();
            }
        });

        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            Fragment newFragment = CountingFragment.newInstance(mStackLevel);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, newFragment).commit();
        } else {
            mStackLevel = savedInstanceState.getInt("level");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }

    void showStack() {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag("2");
        System.out.println("fragment=" +fragment);
        fm.popBackStackImmediate("2",0);
    }
    void addFragmentToStack() {
        mStackLevel++;

        // Instantiate a new fragment.
        Fragment newFragment = CountingFragment.newInstance(mStackLevel);

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        ft.replace(R.id.simple_fragment, newFragment);
        ft.addToBackStack(""+mStackLevel);
        ft.commit();
    }


    public static class CountingFragment extends android.app.Fragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static CountingFragment newInstance(int num) {
            CountingFragment f = new CountingFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_test, container, false);
            View tv = v.findViewById(R.id.text_test);
            ((TextView)tv).setText("Fragment #" + mNum);
            tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
            return v;
        }
    }
}
