/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bongboom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Arijit
 */
public abstract class BoomLoader implements Runnable {

    private StringBuffer BoomFilePath;
    private StringBuffer LanguageTitleInEng;
    private StringBuffer LanguageTitleInLanguage;
    private BufferedReader buffer;
    
    private HashMap<String, BongUTF8CharSequence> KEYWORDS;
    private ArrayList<Integer> NUMBERS;
    
    public static final short MAX_LINE = 52;
    public static final short TITLE_LINE = 1;
    public static final short NUMBER_LINE = 52;

    /**
     * (\(\s*(title)\s*\))\s*(\(([a-z]+)\))\s*(\((.+)?\)) 
     * Group 2: `title` 
     * Group 4: language name in English 
     * Group 6: language name in same
     */
    public static final StringBuffer TITLE_REGEX = new StringBuffer("(\\([a-z]+\\))\\s*(\\([a-z]+\\))\\s*(\\(.+?\\))");
    /**
     * (\(\s*([a-z]+)\s*\))\s*(\(\s*(.+)\s*?\)) 
     * Group 2: keyword from java 
     * Group 4: equivalent keyword from language
     */
    public static final StringBuffer KEYWORD_REGEX = new StringBuffer("(\\(\\s*([a-z]+)\\s*\\))\\s*(\\(\\s*(.+)\\s*?\\))");
    /**
     * \(\s*(numbers)\s*\)\s*\(([^\n\r]+)?\) 
     * Group 1: `numbers` 
     * Group 2: `0,1, 2,3 ,4, 5,6,7 , 8, 9` then do Split-Trim
     */
    public static final StringBuffer NUMBERS_REGEX = new StringBuffer("\\(\\s*(numbers)\\s*\\)\\s*\\(([^\\n\\r]+)?\\)");

    public abstract void start();

    public abstract void completed();

    public abstract void error(int code);

    public BoomLoader(StringBuffer BoomFilePath, Charset encoding) throws FileNotFoundException {
        this.BoomFilePath = BoomFilePath;

        InputStream in = new FileInputStream(this.BoomFilePath.toString());
        Reader reader = new InputStreamReader(in, encoding);
        // buffer for efficiency
        buffer = new BufferedReader(reader);
    }
    boolean LoadCompleted = false;

    public synchronized boolean getStatus() {
        return LoadCompleted;
    }

    public synchronized void load() {
        StringBuilder line;
        Matcher matcher;
        this.start();
        short ctr = TITLE_LINE;
        try {
            do {
                line = new StringBuilder(buffer.readLine());
                if (ctr > TITLE_LINE && ctr < NUMBER_LINE) {
                    Pattern p = Pattern.compile(KEYWORD_REGEX.toString());
                    
                    do{
                        matcher = p.matcher(line);
                        matcher.group(2);//JAVA keyword
                        matcher.group(4);//Eqv. Lang. Chars          
                        ctr++;
                    }while(ctr < NUMBER_LINE);

                } else if (ctr == NUMBER_LINE) {
                   matcher = Pattern.compile(NUMBERS_REGEX.toString()).matcher(line);
                   String numbers = matcher.group(2);
                   String numbersa_arr[] = numbers.trim().split(",");
                   this.NUMBERS = new ArrayList<>();
                   for(String num : numbersa_arr){
                       NUMBERS.add((int)num.trim().charAt(0));
                   }
                   ctr++;
                } else if (ctr == TITLE_LINE) {
                   matcher = Pattern.compile(TITLE_REGEX.toString()).matcher(line);
                   LanguageTitleInEng = new StringBuffer(matcher.group(4));
                   LanguageTitleInLanguage = new StringBuffer(matcher.group(6));
                   ctr++;
                }
                
            } while (ctr <= MAX_LINE);
        } catch (IOException ex) {
            this.error(404);
            LoadCompleted = true;
            return;
        }
        LoadCompleted = true;
        this.completed();
    }

    @Override
    public void run() {
        this.load();
    }
}
