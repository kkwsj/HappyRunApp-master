package com.tsunami.run.happyrun.utils.weather;

import android.util.Log;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.tsunami.run.happyrun.app.App;
import com.tsunami.run.happyrun.utils.FileStreamUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SHEN XIAOMING on 2016/7/24.
 */
public class WeatherDataUtil {

    private static JSONObject dataAll;

    private static String city;

    private static int aqiRank = 0;

    public static int getAqiRank() {
        return aqiRank;
    }

    /**
     * 保存城市名
     * @param c
     */
    public static void setCity(String c) {
        city = c;
    }

    /**
     * 获取天气，直接同步方式
     */
    public static void requestWeather() {
        String httpUrl = "http://apis.baidu.com/heweather/weather/free";
        String httpArg = "city=" + city;
        BufferedReader reader;
        String result = "";
        StringBuilder sb = new StringBuilder();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey",  "c9fede3ca8b6f60bf44c5d0c4f24ec2c");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
//                sb.append("\r\n");
            }
            reader.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("dataUtil", "error");
            e.printStackTrace();
        }

        if (result.length() > 100) {    // 获取数据成功
            // 返回天气数据存入文件
            FileStreamUtil.save(App.getmAppContext(), result);
            // 保存返回天气数据
            setDataAll(result);
        }
    }

    /**
     * 获取天气情况
     *     异步方式，CallBack已封装好
     */
    public static void connectWeather() {
        Parameters para = new Parameters();
        para.put("city", city);
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {

                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("ApiStoreSDK", "onSuccess");

                        // 返回天气数据存入文件
                        FileStreamUtil.save(App.getmAppContext(), responseString);
                        // 保存返回天气数据
                        WeatherDataUtil.setDataAll(responseString);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("ApiStoreSDK", "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString, Exception e) {
                        Log.w("ApiStoreSDK", "onError, status: " + status);
                        Log.w("ApiStoreSDK", "errMsg: " + (e == null ? "" : e.getMessage()));
                        Log.e("ApiStoreSDK", getStackTrace(e));
                    }
                });
    }

    /**
     * ApiStoreSDK抛出异常获取路径
     * @param e
     * @return
     */
    static String getStackTrace(Throwable e) {
        if (e == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        str.append(e.getMessage()).append("\n");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            str.append(e.getStackTrace()[i]).append("\n");
        }
        return str.toString();
    }

    /**
     * 获取所有天气数据
     * @return  JSONObject值
     */
    public static JSONObject getDataAll() {
        return dataAll;
    }

    /**
     * 保存所有的天气数据
     * @param responseString    api返回值
     */
    public static void setDataAll(String responseString) {
        try {
            JSONObject HeWeather = new JSONObject(responseString);
            String HeWeather_str = HeWeather.getString("HeWeather data service 3.0");
            JSONArray outArray = new JSONArray(HeWeather_str);
            dataAll = outArray.getJSONObject(0); // 所有的天气数据
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取城市aqi
     * @return  空气质量指数
     */
    public static String getAqi() {
        try {
            if (dataAll.has("aqi")) {   // api中小城市无该项数据
                String aqi_str = dataAll.getString("aqi");
                JSONObject aqi = new JSONObject(aqi_str);
                String aqi_city_str = aqi.getString("city");
                JSONObject aqi_all = new JSONObject(aqi_city_str);
                String aqi_text = aqi_all.getString("aqi");
                int aqiNum = Integer.parseInt(aqi_text);
                if (aqiNum < 50) {
                    aqiRank = 0;
                } else if (aqiNum < 100) {
                    aqiRank = 1;
                } else if (aqiNum < 150) {
                    aqiRank = 2;
                }else if (aqiNum < 200) {
                    aqiRank = 3;
                } else if (aqiNum < 300) {
                    aqiRank = 4;
                } else {
                    aqiRank = 5;
                }
                return aqi_text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前城市
     * @return  当前城市
     */
    public static String getCity() {
        try {
            String basic_str = dataAll.getString("basic");
            JSONObject basic = new JSONObject(basic_str);
            String city = basic.getString("city");
            return city;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取城市aqi
     * @return  空气质量等级
     */
    public static String getAqiText() {
        try {
            if (dataAll.has("aqi")) {   // api中小城市无该项数据
                String aqi_str = dataAll.getString("aqi");
                JSONObject aqi = new JSONObject(aqi_str);
                String aqi_city_str = aqi.getString("city");
                JSONObject aqi_all = new JSONObject(aqi_city_str);
                String aqi_cond = aqi_all.getString("qlty");
                return aqi_cond;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前温度范围
     * @return  温度范围
     */
    public static String getTemp() {
        try {
            String daily_forecast_str = dataAll.getString("daily_forecast");
            JSONArray daily_forecast = new JSONArray(daily_forecast_str);
            JSONObject today = daily_forecast.getJSONObject(0);
            String tmp_str = today.getString("tmp");
            JSONObject tmp = new JSONObject(tmp_str);
            String min = tmp.getString("min");
            String max = tmp.getString("max");
            return min + "℃~" + max + "℃";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当天天气情况代码
     * @return  天气情况代码
     */
    public static String getCondCode() {
        try {
            String now_str = dataAll.getString("now");
            JSONObject now = new JSONObject(now_str);
            String cond_str = now.getString("cond");
            JSONObject cond = new JSONObject(cond_str);
            return cond.getString("code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当天天气情况
     * @return  天气情况
     */
    public static String getCond() {
        try {
            String now_str = dataAll.getString("now");
            JSONObject now = new JSONObject(now_str);
            String cond_str = now.getString("cond");
            JSONObject cond = new JSONObject(cond_str);
            String txt = cond.getString("txt");
            return txt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前温度
     * @return  当前温度
     */
    public static String getTmp() {
        try {
            String now_str = dataAll.getString("now");
            JSONObject now = new JSONObject(now_str);
            String tmp = now.getString("tmp") + "℃";
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取返回数据now字段
     * @return
     */
    public static String getNow() {
        try {
            String now_str = dataAll.getString("now");
            return now_str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取返回数据daily_forecast字段
     * @return
     */
    public static String getDailyForecast() {
        try {
            return dataAll.getString("daily_forecast");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取返回数据hourly_forecast字段
     * @return
     */
    public static String getHourlyForecast() {
        try {
            return dataAll.getString("hourly_forecast");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取返回数据suggestion字段
     * @return
     */
    public static String getSuggestion() {
        try {
            return dataAll.getString("suggestion");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取日出时间
     * @return
     */
    public static String getSunrise() {
        try {
            String daily_forecast_str = dataAll.getString("daily_forecast");
            JSONArray daily_forecast = new JSONArray(daily_forecast_str);
            JSONObject today= daily_forecast.getJSONObject(0);
            String sun_str = today.getString("astro");
            JSONObject sun = new JSONObject(sun_str);
            return sun.getString("sr");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取日落时间
     * @return
     */
    public static String getSunset() {
        try {
            String daily_forecast_str = dataAll.getString("daily_forecast");
            JSONArray daily_forecast = new JSONArray(daily_forecast_str);
            JSONObject today= daily_forecast.getJSONObject(0);
            String sun_str = today.getString("astro");
            JSONObject sun = new JSONObject(sun_str);
            return sun.getString("ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取api数据发布时间
     * @return
     */
    public static String getUpdateTime() {
        try {
            String basic_str = dataAll.getString("basic");
            JSONObject basic = new JSONObject(basic_str);
            String update_str = basic.getString("update");
            JSONObject update = new JSONObject(update_str);
            String loc = update.getString("loc");
            return loc.substring(11, 16) + "发布";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
