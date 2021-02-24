package com.example.pit;

import android.view.View;

public interface IPit {
    // add new point to (0,0)
    public void addPoint();

    // add initial points
    public void addInitPoints();

    // return view
    View getView();
}
