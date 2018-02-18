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

    public abstract int getNextChar() throws IOException;

    public abstract void token(String token);

    public abstract void token(Integer token);

    public abstract void end();

    boolean DoubleQuoteStarted = false;
    boolean SingleQuoteStarted = false;

    int _LastUTF8Char = 0;
    int _2ndLastUTF8Char = 0;
    int CurrectUTF8Char;

    ArrayList<Integer> Sentence = new ArrayList<>();
    ArrayList<String> Tokens;

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

    public void parse() throws IOException {
        this.start();

        while ((CurrectUTF8Char = this.getNextChar()) != -1) {

            if (DoubleQuoteStarted || TokenLib.isDoubleQuote(CurrectUTF8Char)) {
                /**
                 * Resolving strings
                 */
                if (DoubleQuoteStarted == false && TokenLib.isDoubleQuote(CurrectUTF8Char)) {
                    //clear last chars as 'this is the start of string'
                    this.flash();
                    DoubleQuoteStarted = true;
                } else if (DoubleQuoteStarted == true && TokenLib.isDoubleQuote(CurrectUTF8Char)
                        && !TokenLib.isEscape(_LastUTF8Char)) {
                    //End of string
                    this.flash();
                    DoubleQuoteStarted = false;
                } else {
                    //String not ended
                    Sentence.add(CurrectUTF8Char);
                }
            } else if (SingleQuoteStarted || TokenLib.isSingleQuote(CurrectUTF8Char)) {
                /**
                 * Resolving chars
                 */
                if (SingleQuoteStarted == false && TokenLib.isSingleQuote(CurrectUTF8Char)) {
                    //clear last chars as 'this is the start of string'
                    this.flash();
                    SingleQuoteStarted = true;
                } else if (SingleQuoteStarted == true && TokenLib.isSingleQuote(CurrectUTF8Char)
                        && !TokenLib.isEscape(_LastUTF8Char)) {
                    //End of string
                    this.flash();
                    SingleQuoteStarted = false;
                } else {
                    //String not ended
                    Sentence.add(CurrectUTF8Char);
                }
            } else if (!SingleQuoteStarted && (TokenLib.isSpace(CurrectUTF8Char) || TokenLib.isNewline(CurrectUTF8Char))) {
                this.flash();
            } else if (TokenLib.isSymbol(CurrectUTF8Char)) {
                //If any suymbol found flush everything
                //reset Sentence
                this.flash();
            } else {
                Sentence.add(CurrectUTF8Char);
                //log("0x" + Integer.toHexString(CurrectUTF8Char).toUpperCase() + ", ");
            }

            //Update last chars
            this._2ndLastUTF8Char = this._LastUTF8Char;
            this._LastUTF8Char = this.CurrectUTF8Char;

        }
        this.end();
        //log("\n");
    }

    /**
     * Flushes Sentence, Empty Sentence
     */
    private void flash() {
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
        dump(Sentense);
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
