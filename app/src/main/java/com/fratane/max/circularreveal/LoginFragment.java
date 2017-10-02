package com.fratane.max.circularreveal;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private CircularReveal circularReveal;


    public LoginFragment() {
        // Required empty public constructor
    }

    private CircularReveal getInstance(View rootView){
        if (circularReveal != null){
            return circularReveal;
        }

        View centerView = rootView.findViewById(R.id.progressBar);
        View reveal = rootView.findViewById(R.id.circularReveal);

        if (centerView == null){
            return null;
        }

        int centerY = reveal.getHeight() / 2;
        int centerX = reveal.getWidth() / 2;

        circularReveal = new CircularReveal(reveal, centerX, centerY);
        circularReveal.setExpandDur(700);
        circularReveal.setBackgroundColor(R.color.colorAccent);

        circularReveal.setCircularRevealListener(new CircularReveal.CircularRevealListener() {
            @Override
            public void onAnimationEnd(int animState) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return circularReveal;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(getBtnLoginClick(view));

        return view;
    }

    private View.OnClickListener getBtnLoginClick(final View rootView){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(rootView)) {
                    Task task = new Task(rootView);
                    task.execute();
                }else{
                    View login = rootView.findViewById(R.id.login);
                    View password = rootView.findViewById(R.id.password);
                }
            }
        };
    }

    private void tryLogin(View rootView){
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        Button btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
        }

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CircularReveal circularReveal = getInstance(rootView);

            if (circularReveal != null){
                circularReveal.expand();
            }
        }
    }

    private class Task extends AsyncTask<Void, Void, Void> {
        private View rootView;
        private View progressBar;
        private View btnLogin;

        public Task(View rootView){
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {
            progressBar = rootView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            btnLogin = rootView.findViewById(R.id.btnLogin);

            fadeAnimation(btnLogin, true);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);

            CircularReveal circular = getInstance(rootView);

            if (circular != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    circular.expand();
                }
            }

            //fadeAnimation(btnLogin, false);
        }
    }

    private boolean isValid(View rootView){
        EditText login = (EditText) rootView.findViewById(R.id.login);
        EditText password = (EditText) rootView.findViewById(R.id.password);

        if (login.getText().toString().isEmpty()){
            shakeView(login);
            return false;
        }

        if (password.getText().toString().isEmpty()){
            shakeView(password);
            return false;
        }

        return true;
    }

    private void shakeView(View view){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        view.startAnimation(animation);
    }

    @Override
    public void onResume() {
        super.onResume();

        View rootView = getView();

        if (rootView == null){
            return;
        }

        View btnLogin = rootView.findViewById(R.id.btnLogin);
        btnLogin.setAlpha(1f);
    }

    private void fadeAnimation(final View v, boolean isFadeOut){
        ObjectAnimator fadeOut = isFadeOut? ObjectAnimator.ofFloat(v, "alpha",  1f, 0f) :
                ObjectAnimator.ofFloat(v, "alpha",  0f, 1f);
        fadeOut.setDuration(500);
        fadeOut.start();
    }
}
