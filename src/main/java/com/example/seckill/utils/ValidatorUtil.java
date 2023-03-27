package com.example.seckill.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern MOBILE_PATTERN=Pattern.compile("^1[3-9]\\d{9}$");
    public static boolean isMobile(String mobile){
        if(StringUtils.isBlank(mobile))return false;
        return MOBILE_PATTERN.matcher(mobile).matches();
    }
}
