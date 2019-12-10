package com.alex.criminaintent;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;


public class ShowImage extends DialogFragment {
        private ImageView imageAttachment;
        private final static String PHOTO_PATH = "photo_path";
        private String imagePath;
        float dY;
        float sY;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_DialogWhenLarge);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("ClickableViewAccessibility")
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.show_image_fragment, null);
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                imagePath = bundle.getString(PHOTO_PATH);
                imageAttachment = view.findViewById(R.id.crime_photo_fullscreen);

                Bitmap bitmap =PictureUtils.getScaledBitmap(imagePath, getActivity());
                imageAttachment.setImageBitmap(bitmap);
            }

            /**
             * обработка перетаскивания изображения
             */
            sY = imageAttachment.getY();// переменная для возврата изображения к центру экрана
            imageAttachment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dY = (v.getY() - event.getRawY());
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            v.animate()
                                    .y(event.getRawY() + dY)
                                    .setDuration(0)
                                    .start();
                            return true;
                        case MotionEvent.ACTION_UP:
                            v.animate()
                                    .y(sY)
                                    .setDuration(300)
                                    .start();
                            return true;
                    }
                    return false;
                }
            });
            return view;
        }


        public static ShowImage showFileDetail(String fileId) {
            Bundle pathPhotoForFragment = new Bundle();
            pathPhotoForFragment.putString(PHOTO_PATH, fileId);
            ShowImage fragmentDialog = new ShowImage();
            fragmentDialog.setArguments(pathPhotoForFragment);
            return fragmentDialog;
        }
    }


