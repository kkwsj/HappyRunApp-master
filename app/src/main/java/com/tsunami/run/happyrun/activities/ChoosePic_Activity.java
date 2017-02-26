package com.tsunami.run.happyrun.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.fragments.me_fragment;
import com.tsunami.run.happyrun.views.RoundImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class ChoosePic_Activity extends Activity{

    public static final int TAKE_PHOTO = 1;

    public static final int CROP_PHOTO = 2;

    private Button takePhoto;

    private ImageView picture;

    private RoundImageView my_photo;

    private Uri imageUri;

    private Button chooseFromAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpic_layout);
        //setContentView(R.layout.page_me);
        takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        my_photo= (RoundImageView) findViewById(R.id.my_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                try {
                    if(outputImage.exists()) {
                        outputImage.delete();

                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setDataAndType(imageUri,"image/*");
                intent.putExtra("crop",true);
                intent.putExtra("scale",true);
                // intent.putExtra("return-data",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,CROP_PHOTO);
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if(resultCode == RESULT_OK) {
                    if(data != null) {
                        imageUri = data.getData();
                    }
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //picture.setImageBitmap(bitmap);
                        //my_photo.setImageBitmap(bitmap);


                        new me_fragment();
                        my_photo.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


}
