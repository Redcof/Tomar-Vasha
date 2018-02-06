/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bongboom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arijit
 */
class BongUTF8CharSequence {

    private String Name;
    private int Sequence[];

    public BongUTF8CharSequence(String Name, int[] Sequence) {
        this.Name = Name;
        this.Sequence = Sequence;

    }

    public String getName() {
        return Name;
    }

    public int[] getSequence() {
        return Sequence;
    }

    public boolean checkSequence(ArrayList<Integer> Sequence2) {
        boolean flag = true;
        int length1 = this.Sequence.length;
        int length2 = Sequence2.size();

        if (length1 == length2) {
            int ctr = 0;
            for (; ctr < length1; ctr++) {
                if (this.Sequence[ctr] != Sequence2.get(ctr)) {
                    flag = false;
                    break;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public boolean isKeyword() {
        return (this.Name.charAt(0) == 'T');
    }

    public String getReplacement() {
        if (this.Name.charAt(0) == 'T') {
            return this.Name.substring(2).toLowerCase();
        } else {
            return this.Name;
        }
    }

}

public class BoomTokenLib {

    public static final HashMap<String, BongUTF8CharSequence> TOKENS;

    static {
        TOKENS = new HashMap<>();

        TOKENS.put("K_LF", new BongUTF8CharSequence("K_LF", new int[]{0xD}));//\n
        TOKENS.put("K_CR", new BongUTF8CharSequence("K_CR", new int[]{0xA}));//\r

        TOKENS.put("K_SPACE", new BongUTF8CharSequence("K_SPACE", new int[]{0x20}));//space
        TOKENS.put("K_TAB", new BongUTF8CharSequence("K_TAB", new int[]{0x9}));//tab

        /**
         * keywords
         */
        TOKENS.put("T_ABSTRACT", new BongUTF8CharSequence("T_ABSTRACT", new int[]{0x985, 0x9B8, 0x9AE, 0x9CD, 0x9AA, 0x9C2, 0x9B0, 0x9CD, 0x9A3}));
        TOKENS.put("T_CONTINUE", new BongUTF8CharSequence("T_CONTINUE", new int[]{0x99A, 0x9B2, 0x993}));
        TOKENS.put("T_FOR", new BongUTF8CharSequence("T_FOR", new int[]{0x99A, 0x9BE, 0x9B2, 0x9BE, 0x993}));
        TOKENS.put("T_NEW", new BongUTF8CharSequence("T_NEW", new int[]{0x9A8, 0x9C1, 0x9A4, 0x9A8}));
        TOKENS.put("T_SWITCH", new BongUTF8CharSequence("T_SWITCH", new int[]{0x996, 0x9CB, 0x981, 0x99C, 0x993}));
        TOKENS.put("T_ASSERT", new BongUTF8CharSequence("T_ASSERT", new int[]{0x9AD, 0x9C1, 0x9B2, 0x9A7, 0x9B0, 0x993}));
        TOKENS.put("T_DEFAULT", new BongUTF8CharSequence("T_DEFAULT", new int[]{0x9B8, 0x9CD, 0x9AC, 0x9BE, 0x9AD, 0x9BE, 0x9AC, 0x9BF, 0x995}));
        TOKENS.put("T_GOTO", new BongUTF8CharSequence("T_GOTO", new int[]{0x9AF, 0x9BE, 0x993}));
        TOKENS.put("T_PACKAGE", new BongUTF8CharSequence("T_PACKAGE", new int[]{0x9AA, 0x9CD, 0x9AF, 0x9BE, 0x995}));
        TOKENS.put("T_SYNCHRONIZED", new BongUTF8CharSequence("T_SYNCHRONIZED", new int[]{0x9AA, 0x9B0, 0x9AA, 0x9B0}));
        TOKENS.put("T_BOOLEAN", new BongUTF8CharSequence("T_BOOLEAN", new int[]{0x9B9, 0x9CD, 0x9AF, 0x9BE, 0x981, 0x9A8, 0x9BE}));
        TOKENS.put("T_DO", new BongUTF8CharSequence("T_DO", new int[]{0x995, 0x9B0, 0x993}));
        TOKENS.put("T_IF", new BongUTF8CharSequence("T_IF", new int[]{0x9AF, 0x9A6, 0x9BF}));
        TOKENS.put("T_PRIVATE", new BongUTF8CharSequence("T_PRIVATE", new int[]{0x997, 0x9C1, 0x9AA, 0x9CD, 0x9A4}));
        TOKENS.put("T_THIS", new BongUTF8CharSequence("T_THIS", new int[]{0x98F, 0x99F, 0x9BE}));
        TOKENS.put("T_BREAK", new BongUTF8CharSequence("T_BREAK", new int[]{0x9AC, 0x9C7, 0x9B0, 0x993}));
        TOKENS.put("T_DOUBLE", new BongUTF8CharSequence("T_DOUBLE", new int[]{0x9A6, 0x9B6, 0x9AE, 0x9BF, 0x995, 0x9AC}));
        TOKENS.put("T_IMPLEMENTS", new BongUTF8CharSequence("T_IMPLEMENTS", new int[]{0x9A4, 0x9C8, 0x9B0, 0x9BF, 0x995, 0x9B0, 0x9C7}));
        TOKENS.put("T_PROTECTED", new BongUTF8CharSequence("T_PROTECTED", new int[]{0x985, 0x982, 0x9B6, 0x9C0, 0x9A6, 0x9BE, 0x9B0}));
        TOKENS.put("T_THROW", new BongUTF8CharSequence("T_THROW", new int[]{0x99B, 0x9CB, 0x981, 0x9DC, 0x993}));
        TOKENS.put("T_BYTE", new BongUTF8CharSequence("T_BYTE", new int[]{0x98F, 0x995, 0x995}));
        TOKENS.put("T_ELSE", new BongUTF8CharSequence("T_ELSE", new int[]{0x9A8, 0x9BE, 0x9B9, 0x9DF}));
        TOKENS.put("T_IMPORT", new BongUTF8CharSequence("T_IMPORT", new int[]{0x9A8, 0x9BE, 0x993}));
        TOKENS.put("T_PUBLIC", new BongUTF8CharSequence("T_PUBLIC", new int[]{0x9B8, 0x9AC, 0x9BE, 0x9B0}));
        TOKENS.put("T_THROWS", new BongUTF8CharSequence("T_THROWS", new int[]{0x99B, 0x9CB, 0x981, 0x9DC, 0x9C7, 0x98F}));
        TOKENS.put("T_CASE", new BongUTF8CharSequence("T_CASE", new int[]{0x998, 0x99F, 0x9A8, 0x9BE}));
        TOKENS.put("T_ENUM", new BongUTF8CharSequence("T_ENUM", new int[]{0x98F, 0x9A8, 0x9BE, 0x9AE}));
        TOKENS.put("T_INSTANCEOF", new BongUTF8CharSequence("T_INSTANCEOF", new int[]{0x98F, 0x99F, 0x9BE, 0x9B0, 0x9AE, 0x9A4}));
        TOKENS.put("T_RETURN", new BongUTF8CharSequence("T_RETURN", new int[]{0x9AB, 0x9C7, 0x9B0, 0x993}));
        TOKENS.put("T_TRANSIENT", new BongUTF8CharSequence("T_TRANSIENT", new int[]{0x985, 0x9B8, 0x9CD, 0x9A5, 0x9BE, 0x9AF, 0x9BC, 0x9C0}));
        TOKENS.put("T_CATCH", new BongUTF8CharSequence("T_CATCH", new int[]{0x9A7, 0x9B0, 0x993}));
        TOKENS.put("T_EXTENDS", new BongUTF8CharSequence("T_EXTENDS", new int[]{0x985, 0x9AC, 0x9BF, 0x995, 0x9B2}));
        TOKENS.put("T_INT", new BongUTF8CharSequence("T_INT", new int[]{0x9AA, 0x9C2, 0x9B0, 0x9CD, 0x9A3}));
        TOKENS.put("T_SHORT", new BongUTF8CharSequence("T_SHORT", new int[]{0x99B, 0x9CB, 0x99F, 0x9CB}));
        TOKENS.put("T_TRY", new BongUTF8CharSequence("T_TRY", new int[]{0x99A, 0x9C7, 0x9B7, 0x9CD, 0x99F, 0x9BE, 0x995, 0x9B0}));
        TOKENS.put("T_CHAR", new BongUTF8CharSequence("T_CHAR", new int[]{0x9AC, 0x9B0, 0x9CD, 0x9A3}));
        TOKENS.put("T_FINAL", new BongUTF8CharSequence("T_FINAL", new int[]{0x9B6, 0x9C7, 0x9B7, 0x9C7}));
        TOKENS.put("T_INTERFACE", new BongUTF8CharSequence("T_INTERFACE", new int[]{0x986, 0x9A6, 0x9B2}));
        TOKENS.put("T_STATIC", new BongUTF8CharSequence("T_STATIC", new int[]{0x9B8, 0x9CD, 0x9A5, 0x9BF, 0x9B0}));
        TOKENS.put("T_VOID", new BongUTF8CharSequence("T_VOID", new int[]{0x9AB, 0x9BE, 0x981, 0x995, 0x9BE}));
        TOKENS.put("T_CLASS", new BongUTF8CharSequence("T_CLASS", new int[]{0x9B6, 0x9CD, 0x9B0, 0x9C7, 0x9A3, 0x9C0}));
        TOKENS.put("T_FINALLY", new BongUTF8CharSequence("T_FINALLY", new int[]{0x985, 0x9AC, 0x9B6, 0x9C7, 0x9B7, 0x9C7}));
        TOKENS.put("T_LONG", new BongUTF8CharSequence("T_LONG", new int[]{0x9AA, 0x9C2, 0x9B0, 0x9CD, 0x9A3, 0x9AC}));
        TOKENS.put("T_STRICTFP", new BongUTF8CharSequence("T_STRICTFP", new int[]{0x9A6, 0x9B6, 0x9AE, 0x9BF, 0x995, 0x995}));
        TOKENS.put("T_VOLATILE", new BongUTF8CharSequence("T_VOLATILE", new int[]{0x989, 0x9A6, 0x9CD, 0x9AC, 0x9BE, 0x9AF, 0x9BC, 0x9C0}));
        TOKENS.put("T_CONST", new BongUTF8CharSequence("T_CONST", new int[]{0x9B8, 0x9CD, 0x9A5, 0x9BE, 0x9AF, 0x9BC, 0x9C0}));
        TOKENS.put("T_FLOAT", new BongUTF8CharSequence("T_FLOAT", new int[]{0x9A6, 0x9B6, 0x9AE, 0x9BF, 0x995}));
        TOKENS.put("T_NATIVE", new BongUTF8CharSequence("T_NATIVE", new int[]{0x9B8, 0x9B0, 0x9BE, 0x9B8, 0x9B0, 0x9BF}));
        TOKENS.put("T_SUPER", new BongUTF8CharSequence("T_SUPER", new int[]{0x995, 0x9B0, 0x9CD, 0x9A4, 0x9BE}));
        TOKENS.put("T_WHILE", new BongUTF8CharSequence("T_WHILE", new int[]{0x9AF, 0x9A4, 0x9CB, 0x995, 0x9CD, 0x9B7, 0x9A}));
    }

    //public static final String SYMBOLSc = new String("/*-+`~!@#$%^&()_+|}{:?><,./;'\\][=-");
    public static final Integer[] SYMBOLS = {
        0x2F, 0x2A, 0x2D, 0x2B, 0x60,
        0x7E, 0x21, 0x40, 0x23, 0x24,
        0x25, 0x5E, 0x26, 0x28, 0x29,
        0x5F, 0x2B, 0x7C, 0x7D, 0x7B,
        0x3A, 0x3F, 0x3E, 0x3C, 0x2C,
        0x2E, 0x2F, 0x3B, 0x5C, 0x5D,
        0x5B, 0x3D, 0x2D, 0x27
    };
    public static final Integer DOUBLE_QUOTE = 0x22; //";

    public static final Integer SINGLE_QUOTE = 0x27; //'
    
    public static final Integer ESCAPE = 0x5C; //'
    
    

    public static final Integer[] NUMBERS = {
        0x09E6, 0x09E7, 0x09E8, 0x09E9, 0x09EA,
        0x09EB, 0x09EC, 0x9ED, 0x09EE, 0x09EF
    };

    public static final Integer[] WHITE_SPACES = {
        0xD, 0xA, 0x20, 0x9
    //  \r   \n   \s    \t
    };

    public static boolean isSpace(Integer unicode) {
        return (unicode.intValue() == WHITE_SPACES[2] || unicode.intValue() == WHITE_SPACES[3]);
    }

    public static boolean isNewline(Integer unicode) {
        return (unicode.intValue() == WHITE_SPACES[0] || unicode.intValue() == WHITE_SPACES[1]);
    }

    public static boolean isWhiteSpace(Integer unicode) {
        return (isSpace(unicode) || isNewline(unicode));
    }

    public static boolean isNumber(Integer unicode) {
        boolean flag = false;
        for (Integer code : BoomTokenLib.NUMBERS) {
            if (code.equals(unicode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean isNumber(ArrayList<Integer> sequence) {
        boolean flag = true;
        for (Integer code : sequence) {
            if (BoomTokenLib.isNumber(code) == false) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isSymbol(Integer unicode) {
        boolean flag = false;
        for (Integer code : BoomTokenLib.SYMBOLS) {
            if (code.equals(unicode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean isDoubleQuote(Integer unicode) {
        return unicode.equals(BoomTokenLib.DOUBLE_QUOTE);
    }

    public static boolean isSingleQuote(Integer unicode) {
        return unicode.equals(BoomTokenLib.SINGLE_QUOTE);
    }
    
    public static boolean isEscape(Integer unicode) {
        return unicode.equals(BoomTokenLib.ESCAPE);
    }
    

    public static String searchToken(ArrayList<Integer> sequence) {
        for (Map.Entry<String, BongUTF8CharSequence> entry : BoomTokenLib.TOKENS.entrySet()) {
            BongUTF8CharSequence temp = entry.getValue();
            if (temp.checkSequence(sequence)) {
                return temp.getReplacement();
            }
        }
        if (BoomTokenLib.isNumber(sequence)) {
            String number = new String();
            for (Integer code : sequence) {
                number += (char) code.intValue() - BoomTokenLib.NUMBERS[0];
            }
            return number;
        } else {
            return null;
        }
    }
}
