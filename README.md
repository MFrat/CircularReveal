# Quickstart

``` java
    private CircularReveal getInstance(View rootView){
        if (circularReveal != null){
            return circularReveal;
        }

        View centerView = rootView.findViewById(R.id.someView1);
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
```

``` java
    private View.OnClickListener getBtnLoginClick(final View rootView){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInstance(rootView).expand();
            }
        };
    }
```


