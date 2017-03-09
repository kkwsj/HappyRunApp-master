package com.tsunami.run.happyrun.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.activities.KnowledgeAboutRunActivity;
import com.tsunami.run.happyrun.activities.RunHistoryActivity;
import com.tsunami.run.happyrun.utils.AndroidUtil;
import com.tsunami.run.happyrun.views.RoundImageView;
import com.tsunami.run.happyrun.views.WheelView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by 2010330579 on 2016/3/26.
 */
public class me_fragment extends Fragment {


    private static final String[] YEARS = new String[]{"1984", "1985", "1986", "1987", "1988", "1989",
            "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999",
            "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
            "2010", "2011", "2012", "2013", "2014", "2015", "2016"};
    private String selectedYear = "1987";


    private RelativeLayout re_avatar;
    private RelativeLayout re_name;
    private RelativeLayout re_sex;
    private RelativeLayout re_age;
    private RelativeLayout re_settings;
    private RelativeLayout re_sign;
    private RelativeLayout re_knowledge;

    private ImageView iv_avatar;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_sign;
    private TextView tv_age;

    String nick;

    private Context context = this.context;

    public static final int CROP_PHOTO = 2;

    // 拍照按钮
    private Button takePhoto;

    // 个人中心圆形头像
    private RoundImageView my_photo;
    private Uri imageUri;

    // 从相册中选择图片按钮
    private Button chooseFromAlbum;

    private ImageView img;
    private Button btnUpload;
    //private HttpUtils httpUtils;
    private String URL = "http://192.168.1.119:8080/ServeNew/picCheck";

    private String[] items = {"拍照", "相册", "上传头像"};
    private String title = "选择照片";

    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
//        return User.getInstance().getUsername() + sdf.format(date) + ".png";
        // Log.e("Person"," " + User.getInstance().getUsername().length());
//        int userLength = User.getInstance().getUsername().length();
//        String strUser = String.valueOf(userLength);
//        if(userLength < 10) {
//            strUser = "0" + strUser;
//        }
//        return strUser + User.getInstance().getUsername() + ".png";
        return ".png";
    }

    File outputImage = new File(Environment.getExternalStorageDirectory(), "username.jpg");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.page_me, container, false);

        re_avatar = (RelativeLayout) rootView.findViewById(R.id.re_avatar);
        re_name = (RelativeLayout) rootView.findViewById(R.id.re_name);
        re_sex = (RelativeLayout) rootView.findViewById(R.id.re_sex);
        re_age = (RelativeLayout) rootView.findViewById(R.id.re_age);
        re_settings = (RelativeLayout) rootView.findViewById(R.id.re_settings);
        re_sign = (RelativeLayout) rootView.findViewById(R.id.re_sign);
        re_knowledge = (RelativeLayout) rootView.findViewById(R.id.run_knowledge);

        iv_avatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_sex = (TextView) rootView.findViewById(R.id.tv_sex);
        tv_sign = (TextView) rootView.findViewById(R.id.tv_sign);
        tv_age = (TextView) rootView.findViewById(R.id.tv_age);


        initView();

        return rootView;
    }


    private void initView() {
        re_avatar.setOnClickListener(new MyListener());
        re_name.setOnClickListener(new MyListener());
        re_sex.setOnClickListener(new MyListener());
        re_age.setOnClickListener(new MyListener());
        re_settings.setOnClickListener(new MyListener());
        re_sign.setOnClickListener(new MyListener());
        re_knowledge.setOnClickListener(new MyListener());
        tv_name.setText(nick);

    }


    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_avatar:
                    AlertDialog.Builder dialog = AndroidUtil.getListDialogBuilder(
                            getActivity(), items, title, dialogListener);
                    dialog.show();
                    break;

                case R.id.re_name:
                    showNameDialog(); //ZLF
                    break;
                case R.id.re_age:
                    showAgeDialog();
                    break;

                case R.id.re_sex:
                    showSexDialog();
                    break;


                case R.id.re_sign:
                    showSignDialog();
                    break;

                case R.id.re_settings:
                    Intent intent = new Intent(getActivity(), RunHistoryActivity.class);
                    startActivity(intent);
                    break;

                case R.id.run_knowledge:
                    intent = new Intent(getActivity(), KnowledgeAboutRunActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    }

    private android.content.DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    break;
                case 1:
                    startPick(dialog);
                    break;

                case 2:
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }

    private void showAgeDialog() {
        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(YEARS));
        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                selectedYear = item;
            }
        });

        new AlertDialog.Builder(getActivity())
                .setTitle("请选择年龄")
                .setView(outerView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "您的选择是" + selectedYear, Toast.LENGTH_SHORT).show();
                        tv_age.setText(selectedYear);
                    }
                })
                .setNegativeButton("cancel", null)
                .show();


    }


    private void showSexDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_nan = (TextView) window.findViewById(R.id.tv_content1);
        tv_nan.setText("男");
        tv_nan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
                tv_sex.setText("男");
                dlg.cancel();
            }
        });
        TextView tv_nv = (TextView) window.findViewById(R.id.tv_content2);
        tv_nv.setText("女");
        tv_nv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv_sex.setText("女");
                dlg.cancel();
            }
        });

    }

    private void showPhotoDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("拍照");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();

                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                // intent.putExtra("return-data",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CROP_PHOTO);
                dlg.cancel();
            }
        });
    }

    public void back(View view) {
        getActivity().finish();
    }

    public void showNameDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //事件
                                tv_name.setText(edit.getText());
                            }
                        }).setNegativeButton("取消", null).create().show();

    }

    public void showSignDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(getActivity())
                //   .setTitle("无数据,改变范围试试吧")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //事件
                                tv_sign.setText(edit.getText());
                            }
                        }).setNegativeButton("取消", null).create().show();
    }


    // 调用系统相机
    protected void startCamera(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
        startActivityForResult(intent, PHOTO_CARMERA);
    }

    // 调用系统相册
    protected void startPick(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CARMERA:
                startPhotoZoom(Uri.fromFile(tempFile), 300);
                startPhotoZoom(Uri.fromFile(outputImage), 300);
                break;
            case PHOTO_PICK:
                if (null != data) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case PHOTO_CUT:
                if (null != data) {
                    setPicToView(data);
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 调用系统裁剪
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }

    // 将裁剪后的图片显示在ImageView上
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            final Bitmap bmp = bundle.getParcelable("data");
            iv_avatar.setImageBitmap(bmp);

            Log.e("user photo", outputImage.getPath());

            saveCropPic(bmp);
            Log.i("MainActivity", tempFile.getAbsolutePath());

        }
    }

    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        FileOutputStream fis_output = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(tempFile);
            fis.write(baos.toByteArray());
            fis.flush();
            fis_output = new FileOutputStream(outputImage);
            fis_output.write(baos.toByteArray());
            fis_output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
