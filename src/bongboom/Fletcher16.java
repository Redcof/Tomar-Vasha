/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bongboom;

import java.util.ArrayList;

/**
 *
 * @author Arijit
 */
public final class Fletcher16 {
    public static long generate(ArrayList<Integer> data) {
        long c0, c1;
        int i = 0;
        int len = data.size();

        for (c0 = c1 = 0; len >= 5802; len -= 5802) {
            for (i = 0; i < 5802; ++i) {
                c0 = c0 + data.get(i);
                c1 = c1 + c0;
            }
            c0 = (c0 % 255);
            c1 = (c1 % 255);
        }
        for (i = 0; i < len; ++i) {
            c0 = c0 + data.get(i);
            c1 = c1 + c0;
        }
        c0 = (c0 % 255);
        c1 = (c1 % 255);
        return (c1 << 8 | c0);
    }
}
