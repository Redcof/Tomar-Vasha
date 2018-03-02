/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bongboom;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Arijit
 */
public abstract class BoomParser {

    public abstract void start();

    public abstract String getNextLine() throws IOException;

    public abstract void token(String token);

    public abstract void token(Integer token);

    public abstract void end();

    boolean DoubleQuoteStarted = false;
    boolean SingleQuoteStarted = false;

    //int _LastUTF8Char = 0;
    //int _2ndLastUTF8Char = 0;
    int CurrectUTF8Char;

    ArrayList<Integer> Sentence = new ArrayList<>();
    ArrayList<String> Tokens;
    String SourceLine;
    int ColumnIndex = 0, SourceLineLength = 0, LineNumber = 0;
    
    private boolean log = true;

    private void log(String s) {
        if (log == true) {
            System.out.print(s);
        }
    }
    BoomTokenLib TokenLib;
    BoomInferJSONLoader Infer;

    public BoomParser() {
        TokenLib = new BoomTokenLib();
    }

    public BoomParser(BoomTokenLib tokenLib, BoomInferJSONLoader infer) {
        this.TokenLib = tokenLib;
        this.Infer = infer;
    }
    private boolean Parsing = false;
    
    public void parse() throws IOException, Exception {
        if (!Parsing) {
            Parsing = true;
            this.start();

            while (nextChar()) {
                if (TokenLib.isDoubleQuote(CurrectUTF8Char)) {
                    //clear last chars as 'this is the start of string'
                    this.flashSentense();
                    DoubleQuoteStarted = true;

                    //Block untill string parsed
                    this.parseStr();

                    //End of string
                    this.flashSentense();
                    DoubleQuoteStarted = false;
                } else if (TokenLib.isSingleQuote(CurrectUTF8Char)) {
                    /**
                     * Resolving chars
                     */
                    //clear last chars as 'this is the start of char'
                    this.flashSentense();
                    SingleQuoteStarted = true;

                    //Block untill character parsed
                    this.parseChar();

                    //End of character
                    this.flashSentense();
                    SingleQuoteStarted = false;
                } else if (!SingleQuoteStarted && (TokenLib.isSpace(CurrectUTF8Char) || TokenLib.isNewline(CurrectUTF8Char))) {
                    this.flashSentense();
                } else if (TokenLib.isSymbol(CurrectUTF8Char)) {
                    //If any suymbol found flush everything
                    //reset Sentence
                    this.flashSentense();
                } else {
                    Sentence.add(CurrectUTF8Char);
                    //log("0x" + Integer.toHexString(CurrectUTF8Char).toUpperCase() + ", ");
                }

            }
            this.end();
            //log("\n");
            Parsing = false;
        }
    }

    /**
     * Read next character symbol from file. Return true
     * on success. Else return return false;
     * @return boolean
     * @throws IOException 
     */
    boolean nextChar() throws IOException {
        //if the column index is equal to length
        //new line need to be read
        if(ColumnIndex == SourceLineLength)
        {
            SourceLine = this.getNextLine();
            if(SourceLine != null)
            {
                //add a newline at the end of string as newline omitted already
                SourceLine += "\n";
                //update length
                SourceLineLength = SourceLine.length();
                //increase line number
                LineNumber++;
            }
            else{
                //End of file
                return false;
            }
            //reset column index to zero
            ColumnIndex = 0;
        }
        return ((CurrectUTF8Char = this.SourceLine.charAt(ColumnIndex++)) != -1);
    }

    void parseStr() throws Exception {
        Sentence.add(CurrectUTF8Char);
        while (nextChar()) {
            Sentence.add(CurrectUTF8Char);
            if (CurrectUTF8Char == '\"') {
                //end of string
                break;
            }
            if(this.TokenLib.isNewline(CurrectUTF8Char))
            {
                throw new Exception("Unexpected new line when parsing string.");
            }
            if (CurrectUTF8Char == '\\') {
                //get the next char
                if (!nextChar()) {
                    //Not found; Break loop
                    throw new Exception("Unexpected EOF reatched when parsing string.");
                }
                Sentence.add(CurrectUTF8Char);

                switch (CurrectUTF8Char) {
                    /* Allowed escaped symbols */
                    case '\"':
                    case '/':
                    case '\\':
                    case 'b':
                    case 'f':
                    case 'r':
                    case 'n':
                    case 't':
                        break;
                    case 'u':
                        //get next char                        
                        for (int i = 0; i < 4 && nextChar(); i++) {
                            Sentence.add(CurrectUTF8Char);
                            /* If it isn't a hex character we have an error */
                            if (!((CurrectUTF8Char >= 48 && CurrectUTF8Char <= 57)
                                    /* 0-9 */
                                    || (CurrectUTF8Char >= 65 && CurrectUTF8Char <= 70)
                                    /* A-F */
                                    || (CurrectUTF8Char >= 97 && CurrectUTF8Char <= 102)/* a-f */)) {

                                // Error parsing string
                                throw new Exception("Unexpected symbol : " + (char) CurrectUTF8Char + " when parsing string.");
                            }
                        }
                        break;
                    /* Unexpected symbol */
                        
                    default:
                        // Error parsing string
                        char c = '\u0000';
                        throw new Exception("Unexpected symbol " + (char) CurrectUTF8Char + " when parsing string.");
                }
            }
        }

    }

    void parseChar() throws Exception {
        Sentence.add(CurrectUTF8Char);
        while (nextChar()) {
            Sentence.add(CurrectUTF8Char);
            if (CurrectUTF8Char == '\'') {
                if(Sentence.size() == 2){
                    throw new Exception("Unexpected single quote(') when parsing character.");
                }
                //end of char
                break;
            }
            if(this.TokenLib.isNewline(CurrectUTF8Char))
            {
                throw new Exception("Unexpected new line when parsing character.");
            }
            if (CurrectUTF8Char == '\\') {
                //get the next char
                if (!nextChar()) {
                    //Not found; Break loop
                    throw new Exception("Unexpected EOF reatched when parsing character.");
                }
                Sentence.add(CurrectUTF8Char);

                switch (CurrectUTF8Char) {
                    /* Allowed escaped symbols */
                    case '\"':
                    case '/':
                    case '\\':
                    case 'b':
                    case 'f':
                    case 'r':
                    case 'n':
                    case 't':
                        break;
                    case 'u':
                        //get next symbol                        
                        for (int i = 0; i < 4 && nextChar(); i++) {
                            Sentence.add(CurrectUTF8Char);
                            /* If it isn't a hex character we have an error */
                            if (!((CurrectUTF8Char >= 48 && CurrectUTF8Char <= 57)
                                    /* 0-9 */
                                    || (CurrectUTF8Char >= 65 && CurrectUTF8Char <= 70)
                                    /* A-F */
                                    || (CurrectUTF8Char >= 97 && CurrectUTF8Char <= 102)/* a-f */)) {

                                // Error parsing string
                                throw new Exception("Unexpected symbol : " + (char) CurrectUTF8Char + " when parsing character.");
                            }
                        }
                        break;
                    /* Unexpected symbol */
                        
                    default:
                        // Error parsing char
                        throw new Exception("Unexpected symbol " + (char) CurrectUTF8Char + " when parsing character.");
                }
            }
        }

    }

    /**
     * Flushes Sentence, Empty Sentence
     */
    private void flashSentense() {
        /**
         * Generate tokens
         */
        if (DoubleQuoteStarted == false && SingleQuoteStarted == false) {
            Tokens = this.generateToken(Sentence);
            /**
             * send tokens to host
             */
            for (String tok : Tokens) {
                this.token(tok);
            }
        } else {
            /**
             * send tokens to host
             */
            for (Integer tok : Sentence) {
                this.token(tok);
            }
        }
        /**
         * send the code to host
         */
        this.token(CurrectUTF8Char);
        /**
         * reset the word
         */
        Sentence.clear();
    }

    private void dump(ArrayList<Integer> Sentense) {
        int ctr = 0, len = Sentense.size();
        System.out.print("[");
        for (; ctr < len; ctr++) {
            System.out.print((char) Sentense.get(ctr).intValue());
        }
        System.out.print("]\n");
    }

    public synchronized ArrayList<String> generateToken(ArrayList<Integer> Sentense) {
        //dump(Sentense);
        ArrayList<String> Words = new ArrayList<>();
        if (Sentense.size() > 0) {
            String token = TokenLib.searchToken(Sentense);
            if (token != null) {
                Words.add(token);
            } else {
                String fl16 = "ID_" + Fletcher16.generate(Sentense);
                InferBundle ib = Infer.getFletcher16Value(fl16);
                if (ib == null) {
                    Words.add(fl16);
                } else {
                    Words.add(ib.getActual().toString());
                }
            }
        }
        return Words;
    }

}
