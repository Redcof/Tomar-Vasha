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
    private int WordCount = 0;
    private int Sequence[];

    public BongUTF8CharSequence(String Name, int[] Sequence, int wordCount) {
        this.Name = Name;
        this.Sequence = Sequence;
        this.WordCount = wordCount;
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

    protected HashMap<String, BongUTF8CharSequence> KEYWORDS;
    
    public BoomTokenLib()
    {
        KEYWORDS = new HashMap<>();
        //KEYWORDS.put("T_LESS_THAN_EQUAL", new BongUTF8CharSequence("T_WHILE", new int[]{0x9AF, 0x9A4, 0x9CB, 0x995, 0x9CD, 0x9B7, 0x9A}));
    }


    //public static final String SYMBOLSc = new String("/*-+`~!@#$%^&()_+|}{:?><,./;'\\][=-");
    private Integer[] SYMBOLS = {
        0x2F, 0x2A, 0x2D, 0x2B, 0x60,
        0x7E, 0x21, 0x40, 0x23, 0x24,
        0x25, 0x5E, 0x26, 0x28, 0x29,
        0x5F, 0x2B, 0x7C, 0x7D, 0x7B,
        0x3A, 0x3F, 0x3E, 0x3C, 0x2C,
        0x2E, 0x2F, 0x3B, 0x5C, 0x5D,
        0x5B, 0x3D, 0x2D, 0x27
    };
    private Integer DOUBLE_QUOTE = 0x22; //";

    private Integer SINGLE_QUOTE = 0x27; //'
    
    private Integer ESCAPE = 0x5C; //\
    
    

    protected Integer[] NUMBERS = {
        0x09E6, 0x09E7, 0x09E8, 0x09E9, 0x09EA,
        0x09EB, 0x09EC, 0x9ED, 0x09EE, 0x09EF
    };

    private Integer[] WHITE_SPACES = {
        0xD, 0xA, 0x20, 0x9
    //  \r   \n   \s    \t
    };

    public final boolean isSpace(Integer unicode) {
        return (unicode.intValue() == WHITE_SPACES[2] || unicode.intValue() == WHITE_SPACES[3]);
    }

    public final boolean isNewline(Integer unicode) {
        return (unicode.intValue() == WHITE_SPACES[0] || unicode.intValue() == WHITE_SPACES[1]);
    }

    public final boolean isWhiteSpace(Integer unicode) {
        return (isSpace(unicode) || isNewline(unicode));
    }

    public final boolean isNumber(Integer unicode) {
        boolean flag = false;
        for (Integer code : this.NUMBERS) {
            if (code.equals(unicode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public final boolean isNumber(ArrayList<Integer> sequence) {
        boolean flag = true;
        for (Integer code : sequence) {
            if (this.isNumber(code) == false) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public final boolean isSymbol(Integer unicode) {
        boolean flag = false;
        for (Integer code : this.SYMBOLS) {
            if (code.equals(unicode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public final boolean isDoubleQuote(Integer unicode) {
        return unicode.equals(this.DOUBLE_QUOTE);
    }

    public final boolean isSingleQuote(Integer unicode) {
        return unicode.equals(this.SINGLE_QUOTE);
    }
    
    public final boolean isEscape(Integer unicode) {
        return unicode.equals(this.ESCAPE);
    }
    

    public final String searchToken(ArrayList<Integer> sequence) {
        for (Map.Entry<String, BongUTF8CharSequence> entry : this.KEYWORDS.entrySet()) {
            BongUTF8CharSequence temp = entry.getValue();
            if (temp.checkSequence(sequence)) {
                return temp.getReplacement();
            }
        }
        if (this.isNumber(sequence)) {
            String number = new String();
            for (Integer code : sequence) {
                number += (char) code.intValue() - this.NUMBERS[0];
            }
            return number;
        } else {
            return null;
        }
    }
}
