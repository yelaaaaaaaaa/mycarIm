package com.yryc.imkit.widget.gallery.view;

import android.content.Context;
import android.graphics.Canvas;

import com.bumptech.glide.Glide;
import com.yryc.imkit.widget.gallery.model.GalleryPhotoModel;

import uk.co.senab.photoview.PhotoView;

public class GalleryPhotoView extends PhotoView {

    private GalleryPhotoModel photoModel;

    public GalleryPhotoView(Context context, GalleryPhotoModel photoModel) {
        super(context);
        this.photoModel = photoModel;
    }

    public void startGlide() {
        Glide.with(getContext()).load(photoModel.photoSource).into(GalleryPhotoView.this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
