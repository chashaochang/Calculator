/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.xiaobai.calculator.utils;

/**
 * Some helpful methods operating on strings.
 */

public class StringUtils {

    /**
     * Return a string with n copies of c.
     */
    public static String repeat(char c, int n) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            result.append(c);
        }
        return result.toString();
    }

    /**
     * Return a copy of the supplied string with commas added every three digits.
     * The substring indicated by the supplied range is assumed to contain only
     * a whole number, with no decimal point.
     * Inserting a digit separator every 3 digits appears to be
     * at least somewhat acceptable, though not necessarily preferred, everywhere.
     * The grouping separator in the result is NOT localized.
     */
    public static String addCommas(String s, int begin, int end) {
        // Resist the temptation to use Java's NumberFormat, which converts to long or double
        // and hence doesn't handle very large numbers.
        StringBuilder result = new StringBuilder();
        int current = begin;
        while (current < end && (s.charAt(current) == '-' || s.charAt(current) == ' ')) {
            ++current;
        }
        result.append(s, begin, current);
        while (current < end) {
            result.append(s.charAt(current));
            ++current;
            if ((end - current) % 3 == 0 && end != current) {
                result.append(',');
            }
        }
        return result.toString();
    }

    /**
     * Ignoring all occurrences of c in both strings, check whether old is a prefix of new.
     * If so, return the remaining subsequence of whole. If not, return null.
     */
    public static CharSequence getExtensionIgnoring(CharSequence whole, CharSequence prefix,
                                                    char c) {
        int wIndex = 0;
        int pIndex = 0;
        final int wLen = whole.length();
        final int pLen = prefix.length();
        while (true) {
            while (pIndex < pLen && prefix.charAt(pIndex) == c) {
                ++pIndex;
            }
            while (wIndex < wLen && whole.charAt(wIndex) == c) {
                ++wIndex;
            }
            if (pIndex == pLen) {
                break;
            }
            if (wIndex == wLen || whole.charAt(wIndex) != prefix.charAt(pIndex)) {
                return null;
            }
            ++pIndex;
            ++wIndex;
        }
        while (wIndex < wLen && whole.charAt(wIndex) == c) {
            ++wIndex;
        }
        return whole.subSequence(wIndex, wLen);
    }

    /**
     * 网上随便找了个算法
     * https://www.jb51.net/article/184613.htm
     * */
    public static String calc(String exp) {
        String result = "";
        if (exp == null || exp.equals("")) return result;
        //因为没有运算符所以不用运算
        if (!exp.contains(" ")) {
            return result;
        }
        //截取运算符前面的字符串
        String s1 = exp.substring(0, exp.indexOf(" "));
        //截取的运算符
        String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2);
        //截取运算符后面的字符串
        String s2 = exp.substring(exp.indexOf(" ") + 3);
        double cnt = 0;
        if (!s1.equals("") && !s2.equals("")) {
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                cnt = d1 + d2;
            }
            if (op.equals("-")) {
                cnt = d1 - d2;
            }
            if (op.equals("×")) {
                cnt = d1 * d2;
            }
            if (op.equals("÷")) {
                if (d2 == 0) cnt = 0;
                else cnt = d1 / d2;
            }
            if (!s1.contains(".") && !s2.contains(".") && !op.equals("÷")) {
                int res = (int) cnt;
                result = res + "";
            } else {
                result = cnt + "";
            }
        }
        //如果s1是空 s2不是空 就执行下一步
        else if (!s1.equals("") && s2.equals("")) {
            double d1 = Double.parseDouble(s1);
            if (op.equals("+")) {
                cnt = d1;
            }
            if (op.equals("-")) {
                cnt = d1;
            }
            if (op.equals("×")) {
                cnt = 0;
            }
            if (op.equals("÷")) {
                cnt = 0;
            }
            if (!s1.contains(".")) {
                int res = (int) cnt;
                result = (res + "");
            } else {
                result = (cnt + "");
            }
        }
        //如果s1是空 s2不是空 就执行下一步
        else if (s1.equals("") && !s2.equals("")) {
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                cnt = d2;
            }
            if (op.equals("-")) {
                cnt = 0 - d2;
            }
            if (op.equals("×")) {
                cnt = 0;
            }
            if (op.equals("÷")) {
                cnt = 0;
            }
            if (!s2.contains(".")) {
                int res = (int) cnt;
                result = (res + "");
            } else {
                result = (cnt + "");
            }
        } else {
            result = ("");
        }
        return result;
    }
}
