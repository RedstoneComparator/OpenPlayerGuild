package cn.handyplus.guild.lib;

import cn.handyplus.guild.sql.SqlTemplates;
import java.util.Map;

public class Aa {
    public static String j;
    public static String k;
    public static String H;
    public static String l;
    public static String c;
    public static String K;
    public static String i;
    public static String B;
    public static String D;
    public static String L;
    public static String I;
    public static String m;
    public static String b;
    public static String f;
    public static String E;
    public static String C;
    public static String g;
    public static String G;
    public static String A;
    public static String e;
    public static String d;
    public static String M;
    public static String F;
    public static String mCqrWP;

    public static void J(Map<String, String> remoteValues, Map<Integer, String> remoteKeys) {
        initialize();
    }

    private Aa() {
    }

    public static void ppppPP(Map<String, String> remoteValues, Map<Integer, String> remoteKeys) {
        initialize();
    }

    public static void initialize() {
        j = SqlTemplates.AND;
        C = SqlTemplates.OR;
        d = SqlTemplates.IN;
        F = SqlTemplates.NOT_IN;
        K = SqlTemplates.NOT;
        H = SqlTemplates.LIKE;
        l = SqlTemplates.NOT_LIKE;
        m = SqlTemplates.EQUALS;
        c = SqlTemplates.NOT_EQUALS;
        e = SqlTemplates.GREATER_THAN;
        E = SqlTemplates.LESS_THAN;
        k = SqlTemplates.GREATER_OR_EQUAL;
        i = SqlTemplates.LESS_OR_EQUAL;
        I = SqlTemplates.IS_NULL;
        M = SqlTemplates.IS_NOT_NULL;
        A = SqlTemplates.GROUP_BY;
        g = SqlTemplates.LIMIT;
        L = SqlTemplates.OFFSET;
        mCqrWP = SqlTemplates.HAVING;
        D = SqlTemplates.ORDER_BY;
        b = SqlTemplates.EXISTS;
        f = SqlTemplates.BETWEEN;
        G = SqlTemplates.ASC;
        B = SqlTemplates.DESC;
    }

    public static boolean xxXXXx() {
        if (j == null || B == null) {
            return true;
        }
        return false;
    }

    static {
        initialize();
    }
}
