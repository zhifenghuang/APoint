package com.blokbase.pos.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.math.BigDecimal;
import java.util.Locale;

public class Utils {

    private static void changeAppLanguage(Resources resources, Locale language) {
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(language);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static void changeAppLanguage(Context context, int language) {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        switch (language) {
            case 0:
                locale = Locale.ENGLISH;
                break;
            case 1:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
        }
        changeAppLanguage(context.getResources(), locale);
    }

    public static String removeZero(String number) {
        if (TextUtils.isEmpty(number)) {
            return "0";
        }
        if (number.indexOf(".") > 0) {
            number = number.replaceAll("0+?$", "");//去掉后面无用的零
            number = number.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        if (number.contains(".")) {
            String t = number.split("\\.")[1];
            return (t != null && t.length() > 4) ? String.format("%.4f", Double.parseDouble(number)) : number;
        } else {
            return number;
        }
    }


    public static String getNewValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "0";
        }
        value = new BigDecimal(value).divide(new BigDecimal("1000000000000000000"), 4, BigDecimal.ROUND_UP).toString();
        return Utils.removeZero(value);
    }
}
