package com.tsunami.run.happyrun.utils.weather;

import com.tsunami.run.happyrun.R;

/**
 * Created by SHEN XIAOMING on 2016/6/28.
 */
public class WeatherIconUtil {

    static public int getWeatherIcon(String code) {
        switch (Integer.parseInt(code)) {
            case 100:
                return R.mipmap.icon100;
            case 101:
                return R.mipmap.icon101;
            case 102:
                return R.mipmap.icon102;
            case 103:
                return R.mipmap.icon103;
            case 104:
                return R.mipmap.icon104;
            case 200:
            case 203:
            case 204:
                return R.mipmap.icon200;
            case 201:
            case 202:
                return R.mipmap.icon201;
            case 205:
            case 206:
            case 207:
                return R.mipmap.icon205;
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
                return R.mipmap.icon208;
            case 300:
                return R.mipmap.icon300;
            case 301:
                return R.mipmap.icon301;
            case 302:
                return R.mipmap.icon302;
            case 303:
                return R.mipmap.icon303;
            case 304:
                return R.mipmap.icon304;
            case 305:
                return R.mipmap.icon305;
            case 306:
                return R.mipmap.icon306;
            case 307:
                return R.mipmap.icon307;
            case 308:
                return R.mipmap.icon308;
            case 309:
                return R.mipmap.icon309;
            case 310:
                return R.mipmap.icon310;
            case 311:
                return R.mipmap.icon311;
            case 312:
                return R.mipmap.icon312;
            case 313:
                return R.mipmap.icon313;
            case 400:
                return R.mipmap.icon400;
            case 402:
                return R.mipmap.icon402;
            case 403:
                return R.mipmap.icon403;
            case 404:
                return R.mipmap.icon404;
            case 405:
                return R.mipmap.icon405;
            case 406:
                return R.mipmap.icon406;
            case 407:
                return R.mipmap.icon407;
            case 500:
                return R.mipmap.icon500;
            case 501:
                return R.mipmap.icon501;
            case 502:
                return R.mipmap.icon502;
            case 503:
                return R.mipmap.icon503;
            case 504:
                return R.mipmap.icon504;
            case 507:
                return R.mipmap.icon507;
            case 508:
                return R.mipmap.icon508;
            case 900:
                return R.mipmap.icon900;
            case 901:
                return R.mipmap.icon901;
            default:
                return R.mipmap.icon999;
        }
    }
}
