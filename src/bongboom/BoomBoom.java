/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template SrcFile, choose Tools | Templates
 * and open the template in the editor.
 */
package bongboom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arijit
 */
public class BoomBoom {

    static Reader buffer;
    static Writer fileWriter;
    static OutputStream op;
    static File SrcFile;
    static File JmidFile;
    static File baseInferJSONFile;
    static StringBuffer BoomFilePath;
    static BoomLoader languageLoader;
    static BoomInferJSONLoader InferJSONPkg;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        SrcFile = new File("src/res/hello.bong");
        JmidFile = new File("src/res/hello.jmid");
        baseInferJSONFile = new  File("src/bongboom/chars/base_infer.json");
        BoomFilePath = new StringBuffer("src/bongboom/chars/english.boom");
        
        
        
        Charset encoding = Charset.forName("UTF-8");
        
        languageLoader =  new BoomLoader(BoomFilePath, encoding) {
            @Override
            public void start() {
                
            }

            @Override
            public void completed() {
                try {
                    handleFile(BoomBoom.SrcFile, encoding);
                } catch (IOException ex) {
                    Logger.getLogger(BoomBoom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void error(int code) {
                
            }
        };
        
        new Thread(languageLoader).start();
        
    }

    private static void handleFile(File file, Charset encoding)
            throws IOException {
        try (InputStream in = new FileInputStream(file);
                Reader reader = new InputStreamReader(in, encoding);
                // buffer for efficiency
            Reader buffer = new BufferedReader(reader)) {
           InferJSONPkg = new BoomInferJSONLoader(baseInferJSONFile);
            BoomBoom.buffer = buffer;
            BoomParser bp = new BoomParser(languageLoader,InferJSONPkg) {
                @Override
                public void start() {                   
                    try {
                        op = new FileOutputStream(JmidFile,false);
                        Writer writer = new OutputStreamWriter(op, encoding);
                        fileWriter = new BufferedWriter(writer);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(BoomBoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                @Override
                public int getNextChar() throws IOException {
                    return BoomBoom.buffer.read();
                }

                @Override
                public void token(String token) {
                    //System.out.print(token+" ");
                    try {
                        fileWriter.write(token);
                    } catch (IOException ex) {
                        Logger.getLogger(BoomBoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                @Override
                public void token(Integer token) {
                    int tok = token.intValue();
                    //System.out.print((char)tok + " ");
                    try {
                        fileWriter.write(tok);
                    } catch (IOException ex) {
                        Logger.getLogger(BoomBoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void end() {
                    try {                        
                        fileWriter.close(); 
                        op.close();
                    } catch (IOException ex) {
                        Logger.getLogger(BoomBoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                
            };
            bp.parse();
            
            
        };
    }

    private static void handleCharacters(Reader reader)
            throws IOException {
        int r;
        while ((r = reader.read()) != -1) {
            int ch = r;
            if (Integer.toHexString(ch).toUpperCase().equals("D") || Integer.toHexString(ch).toUpperCase().equals("A")) {
                System.out.print("\n");
            } else {
                System.out.print("0x" + Integer.toHexString(ch).toUpperCase() + ", ");
            }
        }
        System.out.print("\n");
    }

    private static void showCharacters(Reader reader)
            throws IOException {
        int r;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            if (Integer.toHexString(ch).toUpperCase().equals("D") || Integer.toHexString(ch).toUpperCase().equals("A")) {
                System.out.print("\n");
            } else {
                System.out.print("0x" + Integer.toHexString(ch).toUpperCase() + ", ");
            }
        }
        System.out.print("\n");
    }
}
