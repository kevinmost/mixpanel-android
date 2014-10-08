package com.mixpanel.android.viewcrawler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class TestView extends FrameLayout {
    public TestView(Context context) {
        super(context);

        mAllViews = new HashSet<View>();
        mAllViews.add(this);

        mSecondLayer = new HashSet<View>();
        mThirdLayer = new HashSet<View>();
        mFourthLayer = new HashSet<View>();

        setId(ROOT_ID);
        setTag(CRAZY_TAG);

        ViewGroup linear = new LinearLayout(getContext());
        linear.setId(LINEAR_ID);

        addView(linear);
        mAllViews.add(linear);

        mSecondLayer.add(linear);

        final TextView text1 = new TextView(getContext());
        text1.setId(TEXT_VIEW_ID);
        text1.setTag(CRAZY_TAG);

        linear.addView(text1);
        mAllViews.add(text1);
        mThirdLayer.add(text1);
        mTextView1 = text1;

        final TextView text2 = new TextView(getContext());
        text2.setTag(SIMPLE_TAG);

        linear.addView(text2);
        mAllViews.add(text2);
        mThirdLayer.add(text2);
        mTextView2 = text2;

        mButtonGroup = new LinearLayout(getContext());
        linear.addView(mButtonGroup);
        mAllViews.add(mButtonGroup);
        mThirdLayer.add(mButtonGroup);

        mAdHocButton1 = new AdHocButton1(getContext());
        mAdHocButton1.setTag(SIMPLE_TAG);
        mAdHocButton1.setText("{Hi!}");
        mButtonGroup.addView(mAdHocButton1);
        mAllViews.add(mAdHocButton1);
        mFourthLayer.add(mAdHocButton1);

        mAdHocButton2 = new AdHocButton2(getContext());
        mAdHocButton2.setText("Hello \" There");
        mButtonGroup.addView(mAdHocButton2);
        mAllViews.add(mAdHocButton2);
        mFourthLayer.add(mAdHocButton2);

        mAdHocButton3 = new AdHocButton3(getContext());
        mAdHocButton2.setText("Howdy: ]");
        mAdHocButton3.setId(BUTTON_ID);
        mButtonGroup.addView(mAdHocButton3);
        mAllViews.add(mAdHocButton3);
        mFourthLayer.add(mAdHocButton3);

        mButtonParentView = mButtonGroup;

        mViewsByHashcode = new HashMap<Integer, View>();
        for (View v:mAllViews) {
            mViewsByHashcode.put(v.hashCode(), v);
        }
    }

    public int viewCount() {
        return 1 + mSecondLayer.size() + mThirdLayer.size() + mFourthLayer.size();
    }

    public interface CustomPropButton {
        public CharSequence getCustomProperty();
        public void setCustomProperty(CharSequence s);
    }

    public static class AdHocButton1 extends Button implements CustomPropButton {
        public AdHocButton1(Context context) {
            super(context);
        }

        public CharSequence getCustomProperty() {
            return SIMPLE_TAG;
        }

        public void setCustomProperty(CharSequence s) {
            ; // OK
        }

        // This is a HACK- it's actually an override of a secret/public method of View.
        // It's added here so that we can test accessibilityDelegate tracking without
        // doing a lot of puzzling and unreliable functional tests.
        public boolean includeForAccessibility() {
            return true;
        }
    }

    public static class AdHocButton2 extends Button {
        public AdHocButton2(Context context) {
            super(context);
        }

        public void setCountingProperty(Object o) {
            countingPropertyValue = o;
            countingPropertyCount++;
        }

        public Object getCountingProperty() {
            return countingPropertyValue;
        }

        // This is a HACK- it's actually an override of a secret/public method of View.
        // It's added here so that we can test accessibilityDelegate tracking without
        // doing a lot of puzzling and unreliable functional tests.
        public boolean includeForAccessibility() {
            return true;
        }

        public Object countingPropertyValue = null;
        public int countingPropertyCount = 0;
    }

    public static class AdHocButton3 extends Button implements CustomPropButton {
        public AdHocButton3(Context context) {
            super(context);
        }

        public CharSequence getCustomProperty() {
            throw new RuntimeException("BANG!");
        }

        public void setCustomProperty(CharSequence s) {
            throw new RuntimeException("BANG!");
        }

        // This is a HACK- it's actually an override of a secret/public method of View.
        // It's added here so that we can test accessibilityDelegate tracking without
        // doing a lot of puzzling and unreliable functional tests.
        public boolean includeForAccessibility() {
            return true;
        }
    }

    public final Set<View> mAllViews;
    public final View mButtonParentView;
    public final ViewGroup mButtonGroup;
    public final TextView mTextView1;
    public final TextView mTextView2;
    public final AdHocButton1 mAdHocButton1;
    public final AdHocButton2 mAdHocButton2;
    public final AdHocButton3 mAdHocButton3;
    public final Set<View> mSecondLayer;
    public final Set<View> mThirdLayer;
    public final Set<View> mFourthLayer;
    public final Map<Integer, View> mViewsByHashcode;

    public static final int ROOT_ID = 1000;
    public static final int BUTTON_ID = 2000;
    public static final int TEXT_VIEW_ID = 3000;
    public static final int LINEAR_ID = 4000;
    public static final String SIMPLE_TAG = "this_is_a_simple_tag";
    public static final String CRAZY_TAG = "this is a long and \"CRAZY\" \\\"Tag";
    public static final String DOUBLE_QUOTED_CRAZY_TAG = "\"this is a long and \\\"CRAZY\\\" \\\\\\\"Tag\"";
    public static final String SINGLE_QUOTED_CRAZY_TAG = "'this is a long and \\\"CRAZY\\\" \\\\\\\"Tag'";
}