package kyousei.kyousei.common;

import kyousei.kyousei.interfaceis.IEnum;

public class Enums {
    /**
     * DBレコード状態
     * 
     * @return 0.未定(UNDECIDED) 1.確定(COMFILM) 2.削除(DELETE) 3.新規(CREATE) 4.更新(UPDATE) 5.完了(COMPLETE)
     */
    public enum state implements IEnum {
        UNDECIDED(0, "未定"),
        COMFILM(1, "確定"),
        DELETE(2, "削除"),
        CREATE(3, "新規"),
        UPDATE(4, "更新"),
        COMPLETE(5, "完了");

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
     * @return 1.自社(OWN) 2.施工会社(PARTNER) 3.取引先(CLIENT) 4.仕入先(SUPPLIER) 5.サービス(SERVICE)
     */
    public enum companyCategory implements IEnum {
        OWN(1, "自社"),
        PARTNER(2, "施工会社"),
        CLIENT(3, "取引先"),
        SUPPLIER(4, "仕入先"),
        SERVICE(5, "サービス");

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
     * 支払いカテゴリー
     * 
     * @return 1.未払い 2.支払い済
     */
    public enum cashState implements IEnum {
        UNPAID(1, "未払い"),
        PAID(2, "支払い済");
        private int num;
        private String str;

        private cashState(int num, String str) {
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
     * 性別
     * 
     * @return 1:男(MAN) 2:女(WOMAN) 9:無回答(OTHERS)
     */
    public enum gender implements IEnum {
        NULL(0, ""),
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
     * @return 1:A型(A) 2:B型(B) 3:O型(O) 4:AB型(AB) 9:無回答(OTHERS)
     */
    public enum bloodType implements IEnum {
        NULL(0, ""),
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
            return Utilities.enumValueOf(Enums.dayOfWeekToStr.class, num).getStr();
        }
    }
}