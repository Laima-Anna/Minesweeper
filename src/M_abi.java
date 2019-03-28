//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.sql.Timestamp;

public class M_abi {
    public M_abi() {
    }




    public static String toStringJ(int[] var0, int var1, String var2) {
        String var3 = "";
        String var4 = "%" + var1 + "d";

        for (int var5 = 0; var5 < var0.length; ++var5) {
            if (var5 == 0) {
                var3 = var3 + String.format(var4, var0[var5]);
            } else {
                var3 = var3 + var2 + String.format(var4, var0[var5]);
            }
        }

        return var3;
    }



    public static void printM_ind(String var0, int[][] var1) {
        int var2 = -2147483648;
        int[][] var3 = var1;
        int var4 = var1.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            int[] var6 = var3[var5];
            int[] var7 = var6;
            int var8 = var6.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                int var10 = var7[var9];
                int var11 = ("" + var10).length();
                if (var11 > var2) {
                    var2 = var11;
                }
            }
        }

        int[] var12 = new int[var1[0].length];

        for (var4 = 0; var4 < var1[0].length; var12[var4] = var4++) {
        }

        System.out.println(var0 + "   " + toStringJ(var12, var2, ".") + ".");

        for (var4 = 0; var4 < var1.length; ++var4) {
            String var13;
            if (var4 < 10) {
                var13 = var0 + " " + var4 + ". ";
            } else {
                var13 = var0 + var4 + ". ";
            }

            System.out.print(var13 + toStringJ(var1[var4], var2, " "));
            System.out.println();
        }

    }

}
