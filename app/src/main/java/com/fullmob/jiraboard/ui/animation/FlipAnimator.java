package com.fullmob.jiraboard.ui.animation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;

import com.fullmob.jiraboard.R;


/**
 * Created by Ravi Tamada on 22/02/17.
 * www.androidhive.info
 */

public class FlipAnimator {
    private static String TAG = FlipAnimator.class.getSimpleName();
    private static AnimatorSet leftIn, rightOut, leftOut, rightIn;

    /**
     * Performs flip animation on two views
     */
    public static void flipView(Context context, final View iconChecked, final View icon, boolean showChecked) {
        leftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        rightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
        leftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        rightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);

        final AnimatorSet showCheckedAnim = new AnimatorSet();
        final AnimatorSet showUnCheckedAnim = new AnimatorSet();

        leftIn.setTarget(iconChecked);
        rightOut.setTarget(icon);
//        showFrontAnim.playTogether(leftIn, rightOut);
        showCheckedAnim.playSequentially(rightOut, leftIn);

        leftOut.setTarget(iconChecked);
        rightIn.setTarget(icon);
//        showBackAnim.playTogether(rightIn, leftOut);
        showUnCheckedAnim.playSequentially(leftOut, rightIn);

        if (showChecked) {
            iconChecked.setAlpha(0f);
            showCheckedAnim.start();
        } else {
            icon.setAlpha(0f);
            showUnCheckedAnim.start();
        }
    }
}
