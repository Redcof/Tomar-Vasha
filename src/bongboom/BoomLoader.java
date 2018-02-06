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

/**
 *
 * @author Arijit
 */
public class BoomLoader {
    private String BoomFilePath;
    private BufferedReader buffer;   
    public static final short MAX_LINE = 52;
    
    public static final String TITLE_REGEX = "(\\([a-z]+\\))\\s*(\\([a-z]+\\))\\s*(\\(.+?\\))";
    public static final String KEYWORD_REGEX = "(\\([a-z]+\\))\\s*(\\(.+?\\))";
    public static final String NUMBER_KEYWORD = "";
    
    public BoomLoader(String BoomFilePath) throws FileNotFoundException {
        this.BoomFilePath = BoomFilePath;
        
        InputStream in = new FileInputStream(this.BoomFilePath);
        Reader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        // buffer for efficiency
        buffer = new BufferedReader(reader);
    }
    
    public boolean load() throws IOException{
        short ctr = 1;
        do{
            String line = buffer.readLine();
            ctr++;
        }while(ctr <= MAX_LINE);
        
        return true;
    }
}
