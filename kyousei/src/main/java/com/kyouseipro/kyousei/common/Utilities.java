package com.kyouseipro.kyousei.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import com.kyouseipro.kyousei.interfacies.IEnum;

public class Utilities {
    
    /**
     * 引数の文字列が Null または "" かどうかチェックする
     * @param str チェックする文字列
     * @return Null または "" であれば[True]を返す
     */
    public static boolean checkStringIsNullOrBlank(String str){
        boolean ans = false;
        if (str == null || str.strip().length() == 0){
            ans = true;
        }
        return ans;
    }

    /**
     * 引数の文字列が "" または空白の時は Null に変換する
     * @param str
     * @return
     */
    public static String changeStringIsBlankToNull(String str){
        if (str != null && str.strip().length() == 0){
            str = null;
        }
        return str;
    }

    public static String removeWhitespace(String str) {
        if (str != null) {
            if (str.strip().length() == 0) {
                str = null;
            }
        }
        return str;
    }


    /**
     * 郵便番号のチェック
     * @param code 調べたい文字列
     * @return 正しくなければNULLを返す
     */
    public static String checkPostalCode(String code) {
        if (code.length() < 7 || code.length() > 8) {
            return null;
        }

        String replaceCode = code.replace("-", "");
        if (!Utilities.checkStringNumerical(replaceCode)) {
            return null;
        }

        if (replaceCode.length() == 7) {
            return replaceCode.substring(0, 3) + "-" + replaceCode.substring(3, 7);
        }

        return code;
    }

    /**
     * 引数で受け取った文字列が数値かどうか正規表現でチェックするメソッド
     * @param text 調べたい文字列
     * @return 数値だけであれば[True]を返す
     */
    public static boolean checkStringNumerical(String text) {

        Pattern pattern = Pattern.compile("^[0-9]+$|-[0-9]+$");
        return pattern.matcher(text).matches();

    }
    
    /**
     * 第一引数に指定されたEnumの中から、第2引数のコード値と一致するものを取得する。
     *
     * @param target 取得したいEnumのクラス
     * @param code   検索するコード値
     * @param <E>    CodeInterfaceを実装したEnumクラス
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <E extends Enum & IEnum> E enumValueOf(Class<E> target, int num) {
        return Arrays.stream(target.getEnumConstants())
                .filter(data -> data.getNum() == num)
                .findFirst()
                .orElse(null);
    }

    // 月初日を返す
    public static String getFirstDate() { 
        Calendar cl = Calendar.getInstance();

        int yyyy = cl.get(Calendar.YEAR);
        int MM = cl.get(Calendar.MONTH);
        int dd = cl.getActualMinimum(Calendar.DAY_OF_MONTH);

        // cl.set(yyyy, MM, dd);
        
        // SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd");
        // return str.format(cl.getTime());
        // // return yyyy + "-" + MM + "-" + dd;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.of(yyyy, MM + 1, dd);
        return date.format(formatter);
    }
 
    // 月末日を返す
    public static String getLastDate() {
        Calendar cl = Calendar.getInstance();

        int yyyy = cl.get(Calendar.YEAR);
        int MM = cl.get(Calendar.MONTH);
        int dd = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // cl.set(yyyy, MM, dd);
        
        // SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd");
        // return str.format(cl.getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.of(yyyy, MM + 1, dd);
        return date.format(formatter);
        // return yyyy + "-" + MM + "-" + dd;
    }

    // /**
    //  * 汎用的な文字埋めメソッド
    //  * @param target 文字を埋める対象の文字列
    //  * @param length 桁数
    //  * @param padChar 埋める文字
    //  * @return
    //  */
    // public static String padLeft(String target, int length, char padChar) {
    //     int targetLength = target.length();
    //     if (targetLength >= length) {
    //         return target;
    //     }
    //     StringBuilder sb = new StringBuilder(length);
    //     for (int i = targetLength; i < length; i++) {
    //         sb.append(padChar);
    //     }
    //     sb.append(target);
    //     return sb.toString();
    // }


    // /**
    //  * 性別文字列取得 1:男 2:女 9:無回答
    //  * @param id
    //  * @return
    //  */
    // public static String getGenderString(int id) {
    //     String str = "";
    //     switch (id) {
    //         case 1:
    //             str = "男";
    //             break;
    //         case 2:
    //             str = "女";
    //             break;
    //         case 9:
    //             str = "無回答";
    //             break;
    //         default:
    //             break;
    //     }
    //     return str;
    // }

    // /**
    //  * 血液型文字列取得 1:A 2:B 3:O 4:AB 9:無回答
    //  * @param id
    //  * @return
    //  */
    // public static String getBloodTypeString(int id) {
    //     String str = "";
    //     switch (id) {
    //         case 1:
    //             str = "A";
    //             break;
    //         case 2:
    //             str = "B";
    //             break;
    //         case 3:
    //             str = "O";
    //             break;
    //         case 4:
    //             str = "AB";
    //             break;
    //         case 9:
    //             str = "無回答";
    //             break;
    //         default:
    //             break;
    //     }
    //     return str;
    // }

    // /**
    //  * 支払い状態文字列取得 0:未 1:済
    //  * @param id
    //  * @return
    //  */
    // public static String getPayStateString(int id) {
    //     String str = "";
    //     switch (id) {
    //         case 0:
    //             str = "未";
    //             break;
    //         case 1:
    //             str = "済";
    //             break;
    //         default:
    //             break;
    //     }
    //     return str;
    // }
}
