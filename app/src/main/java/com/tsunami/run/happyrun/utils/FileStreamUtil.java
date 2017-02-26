package com.tsunami.run.happyrun.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by SHEN XIAOMING on 2016/8/13.
 */
public class FileStreamUtil {

    /**
     * 将数据存储到指定的文件中
     * @param context
     * @param data  文件名
     */
    public static void save(Context context, String data) {
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput("run", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件中读取数据
     * @param context
     * @return  字符串
     */
    public static String load(Context context) {
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
