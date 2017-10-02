package com.fratane.max.circularreveal;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Max_F on 01/10/2017.
 */

public class CircularReveal {
    /*
     * Auxilia na "ida" e "volta" do reveal.
     */
    private boolean canAnim;

    /*
     * Duração da animação de ida e volta.
     */
    private int expandDur;
    private int collapseDur;

    private int offset = 0;

    /*
     * View buildCardSala ser animada com o reveal.
     */
    private View view;

    /*
     * Centro de inicio da animação.
     */
    private int centerX;
    private int centerY;

    /*
     * Ida ou volta da animação.
     */
    public static final int COLLAPSE = 1;
    public static final int EXPAND = 2;

    private boolean duringAnim = false;

    private int backgroundColor = Color.CYAN;

    private boolean animationEnd = true;

    private boolean keepViewStateAfterAnim = false;

    /*
     * callback interface.
     */
    private CircularRevealListener circularRevealListener;

    public CircularReveal(View view, int centerX, int centerY){
        if(view == null){
            throw new IllegalArgumentException("Animated view can't be null!");
        }

        if(centerX < 0){
            throw new IllegalArgumentException("Invalid centerX: " + String.valueOf(centerX));
        }

        if(centerY < 0){
            throw new IllegalArgumentException("Invalid centerY: " + String.valueOf(centerY));
        }

        this.view = view;
        this.centerX = centerX;
        this.centerY = centerY;
        this.collapseDur = 400;
        this.expandDur = 500;
        this.canAnim = true;
    }

    public CircularReveal(){
        this.collapseDur = 400;
        this.expandDur = 500;
        this.canAnim = true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animate(){
        if(canAnim) {
            expand();
        }else {
            collapse();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void expand(){
        animate(EXPAND);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void collapse(){
        animate(COLLAPSE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animate(final int type){
        boolean collapse = type == COLLAPSE;

        if(duringAnim){
            return;
        }

        int max = Math.max(view.getWidth(), view.getHeight());

        int startRadius = collapse? max : 0;

        int endRadius = collapse? 0 : max;

        final Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        anim.setDuration(expandDur);
        if(offset > 0) {
            anim.setStartDelay(offset);
        }


        view.setVisibility(View.VISIBLE);

        if (!keepViewStateAfterAnim) {
            view.setAlpha(1f);
        }

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                duringAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                duringAnim = false;

                if(circularRevealListener != null){
                    circularRevealListener.onAnimationEnd(EXPAND);
                }

                if (type == EXPAND){
                    if (!keepViewStateAfterAnim){
                        fadeAnimation(view);
                    }
                }else{
                    view.setVisibility(View.INVISIBLE);
                }

                canAnim = !canAnim;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        anim.start();
    }

    public boolean isDuringAnim() {
        return duringAnim;
    }

    public void setCircularRevealListener(CircularRevealListener circularRevealListener) {
        this.circularRevealListener = circularRevealListener;
    }

    public void setExpandDur(int expandDur) {
        this.expandDur = expandDur;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setCollapseDur(int collapseDur) {
        this.collapseDur = collapseDur;
    }

    public void setKeepViewStateAfterAnim(boolean keepViewStateAfterAnim) {
        this.keepViewStateAfterAnim = keepViewStateAfterAnim;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        view.setBackgroundResource(backgroundColor);
    }

    public interface CircularRevealListener{
        /**
         * Called when animation ends.
         */
        void onAnimationEnd(int animState);
    }

    private void fadeAnimation(final View v){
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, .0f);
        fadeOut.setDuration(500);
        fadeOut.start();
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
