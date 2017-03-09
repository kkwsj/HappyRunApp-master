package com.tsunami.run.happyrun.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.fragments.CardViewFragment;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class KnowledgeAboutRunActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.activity_knowledge_about_run);

        Toolbar toolbar = (Toolbar) findViewById(R.id.knowlegeAboutRunToolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // 2 添加的代码
            getSupportActionBar().setTitle("知识小课堂");
            /////////////////////////////////
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CardViewFragment.newInstance())
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runFunctionClick(View view) {
        //Toast.makeText(KnowledgeAboutRunActivity.this,"dsdsdsdsdsdsdsdfsd",Toast.LENGTH_SHORT).show();
        showRunFunctionKnowledgeDialog();
    }

    public void runRunTimeClick(View view) {
        //Toast.makeText(KnowledgeAboutRunActivity.this,"dsdsdsdsdsdsdsdfsd",Toast.LENGTH_SHORT).show();
        showRunTimeKnowledgeDialog();
    }

    public void runRunWarmUpClick(View view) {
        //Toast.makeText(KnowledgeAboutRunActivity.this,"dsdsdsdsdsdsdsdfsd",Toast.LENGTH_SHORT).show();
        showRunWarmUpKnowledgeDialog();
    }

    public void runRunClothesClick(View view) {
        //Toast.makeText(KnowledgeAboutRunActivity.this,"dsdsdsdsdsdsdsdfsd",Toast.LENGTH_SHORT).show();
        showRunClothesKnowledgeDialog();
    }

    public void runRunShoesClick(View view) {
        //Toast.makeText(KnowledgeAboutRunActivity.this,"dsdsdsdsdsdsdsdfsd",Toast.LENGTH_SHORT).show();
        showRunShoesKnowledgeDialog();
    }

    public void showRunShoesKnowledgeDialog(){
        LayoutInflater factory = LayoutInflater.from(KnowledgeAboutRunActivity.this);//提示框
        final View view = factory.inflate(R.layout.run_shoes_knowledge, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(KnowledgeAboutRunActivity.this)
                .setTitle("跑鞋的选择")//提示框标题
                .setView(view)
                .create().show();
    }


    public void showRunFunctionKnowledgeDialog(){
        LayoutInflater factory = LayoutInflater.from(KnowledgeAboutRunActivity.this);//提示框
        final View view = factory.inflate(R.layout.run_function_knowledge, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(KnowledgeAboutRunActivity.this)
                .setTitle("跑步的方法")//提示框标题
                .setView(view)
                .create().show();
    }

    public void showRunTimeKnowledgeDialog(){
        LayoutInflater factory = LayoutInflater.from(KnowledgeAboutRunActivity.this);//提示框
        final View view = factory.inflate(R.layout.run_time_knowledge, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(KnowledgeAboutRunActivity.this)
                .setTitle("地点、时间的选择")//提示框标题
                .setView(view)
                .create().show();
    }

    public void showRunWarmUpKnowledgeDialog(){
        LayoutInflater factory = LayoutInflater.from(KnowledgeAboutRunActivity.this);//提示框
        final View view = factory.inflate(R.layout.run_warm_up_knowledge, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(KnowledgeAboutRunActivity.this)
                .setTitle("热身活动")//提示框标题
                .setView(view)
                .create().show();
    }

    public void showRunClothesKnowledgeDialog(){
        LayoutInflater factory = LayoutInflater.from(KnowledgeAboutRunActivity.this);//提示框
        final View view = factory.inflate(R.layout.run_clothes_knowledge, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(KnowledgeAboutRunActivity.this)
                .setTitle("服装的选择")//提示框标题
                .setView(view)
                .create().show();
    }

}
