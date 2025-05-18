package com.kyouseipro.kyousei.common;

import com.kyouseipro.kyousei.interfacies.IEnum;

public class Enums {

    /**
     * DBレコード状態
     * 
     * @return 0.初期値 1.完了 2.削除 3.新規 4.更新 5.確定 6.未定
     */
    public enum state implements IEnum {
        INITIAL(0, "初期値"),
        COMPLETE(1, "完了"),
        DELETE(2, "削除"),
        CREATE(3, "新規"),
        UPDATE(4, "更新"),
        COMFILM(5, "確定"),
        UNDECIDED(6, "未定");

        private int num;
        private String str;

        private state(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.state.class, num).getStr();
        }
    }

    /**
     * 会社カテゴリー
     * 
     * @return 1.自社 2.事業者 3.荷主 4.商社
     */
    public enum companyCategory implements IEnum {
        OWN(1, "自社"),
        PARTNER(2, "事業者"),
        SHIPPER(3, "荷主"),
        SUPPLIER(4, "商社");

        private int num;
        private String str;

        private companyCategory(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.companyCategory.class, num).getStr();
        }
    }

    /**
     * 従業員カテゴリー
     * 
     * @return 1.正社員 2.アルバイト 3.施工担当 4.営業担当
     */
    public enum employeeCategory implements IEnum {
        FULLTIME(1, "正社員"),
        PARTTIME(2, "アルバイト"),
        CONSTRUCT(3, "施工担当"),
        SALES(4, "営業担当");

        private int num;
        private String str;

        private employeeCategory(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.employeeCategory.class, num).getStr();
        }
    }

    /**
     * 支払い方法
     * 
     * @return 1.振り込み 2.日払い
     */
    public enum paymentMethod implements IEnum {
        TRANSFER(1, "振り込み"),
        DAILYPAY(2, "日払い");

        private int num;
        private String str;

        private paymentMethod(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.paymentMethod.class, num).getStr();
        }
    }

    // /**
    // * カテゴリークラス
    // * @return EMPLOYMENT_STATUS:雇用形態 COMPANY_CLASS:企業区分 ITEM_CLASS:分類アイテム
    // */
    // public enum categoryClass implements IEnum {
    // EMPLOYMENT_STATUS(1, "雇用形態"),
    // COMPANY_CLASS(2, "企業区分"),
    // ITEM_CLASS(3, "分類アイテム");

    // private final String text;

    // private categoryClass(final String text) {
    // this.text = text;
    // }
    // public String getString() {
    // return this.text;
    // }
    // }

    /**
     * リサイクル日付カテゴリー
     * 
     * @return 1.usedate 2.deliverydate 3.forwarddate 4.lossdate
     */
    public enum recycleDateCategory implements IEnum {        
        USE(1, "使用日"),
        DELIVERY(2, "引渡日"),
        FORWARD(3, "発送日"),
        LOSS(4, "ロス処理日"),
        INPUT(9, "入力日");

        private int num;
        private String str;

        private recycleDateCategory(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.recycleDateCategory.class, num).getStr();
        }
    }

    /**
     * 性別
     * 
     * @return 1:男 2:女 9:無回答
     */
    public enum gender implements IEnum {
        MAN(1, "男"),
        WOMAN(2, "女"),
        OTHERS(9, "無回答");

        private int num;
        private String str;

        private gender(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.gender.class, num).getStr();
        }
    }

    /**
     * 血液型
     *
     * @return 1:A 2:B 3:O 4:AB 9:無回答
     */
    public enum bloodType implements IEnum {
        A(1, "A型"),
        B(2, "B型"),
        O(3, "O型"),
        AB(4, "AB型"),
        OTHERS(9, "無回答");

        private int num;
        private String str;

        private bloodType(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.bloodType.class, num).getStr();
        }
    }

    /**
     * 状態
     *
     * @return 1:済 2:未
     */
    public enum situation implements IEnum {
        YET(2, "未"),
        DONE(1, "済");

        private int num;
        private String str;

        private situation(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.situation.class, num).getStr();
        }
    }

    /**
     * 曜日
     *
     * @return 0:日 1:月 2:火 3:水 4:木 5:金 6:土
     */
    public enum dayOfWeekToStr implements IEnum {
        SUNDAY(0, "日"),
        MONDY(1, "月"),
        TUESDAY(2, "火"),
        WEDNESDAY(3, "水"),
        THURSDAY(4, "木"),
        FRIDAY(5, "金"),
        SATURDSY(6, "土");

        private int num;
        private String str;

        private dayOfWeekToStr(int num, String str) {
            this.num = num;
            this.str = str;
        }

        @Override
        public int getNum() {
            return this.num;
        }

        @Override
        public String getStr() {
            return this.str;
        }

        public static String getStrByNum(int num) {
            return Utilities.enumValueOf(Enums.situation.class, num).getStr();
        }
    }
}