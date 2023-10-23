package com.xfarmer.common.util;

public class RegexUtils {
    /**
     * @param phoneNum 传入的参数仅仅是一个电话号码时，调用此方法
     * @return 如果匹配正确，return true , else return else
     */
    //如果传进来的是电话号码，则对电话号码进行正则匹配
    public static boolean regexPhoneNumber(String phoneNum) {
        //电话号码匹配结果
        return phoneNum.matches("1[3456789]\\d{9}");

    }

    /**
     * @param email 传入的参数仅仅是一个邮箱地址时，调用此方法
     * @return 如果匹配正确，return true , else return false
     */
    //如果传进来的是邮箱地址，则对邮箱进行正则匹配
    public static boolean regexEmailAddress(String email) {
        //邮箱匹配结果
        return email.matches("[a-zA-Z_0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,}){1,3}");
    }

    /**
     * @param phoneNum 传入的电话号码
     * @param email    传入的邮箱地址
     * @return 如果匹配正确，return true , else return false
     */
    public static boolean regexEmailAddressAndPhoneNum(String phoneNum, String email) {
        //电话号码匹配结果
        boolean isPhoneNumMatcher = phoneNum.matches("1[358]\\d{9}");
        //邮箱匹配结果
        boolean isEmailMatcher = email.matches("[a-zA-Z_0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,}){1,3}");
        //matcher value is true , 则 return true , else return false
        return isEmailMatcher && isPhoneNumMatcher;
    }

    /**
     * @param qqNum 传入的QQ
     * @return 如果匹配正确，return true， else return false
     */
    public static boolean regexQQNumber(String qqNum) {
        //QQ号匹配结果
        return qqNum.matches("[1-9]\\d{2,11}");
    }

}
