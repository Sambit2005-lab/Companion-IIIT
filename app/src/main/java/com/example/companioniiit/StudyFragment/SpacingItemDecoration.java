package com.example.companioniiit.StudyFragment;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpaceWidth;

    public SpacingItemDecoration(int horizontalSpaceWidth) {
        this.horizontalSpaceWidth = horizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = horizontalSpaceWidth;

        // If you want spacing at the left of the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = horizontalSpaceWidth;
        }
    }
}
