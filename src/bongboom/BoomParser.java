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

    int LastUTF8Char = 0;
    int CurrectUTF8Char;

    ArrayList<Integer> Sentense = new ArrayList<>();
    ArrayList<String> Tokens;

    private boolean log = true;

    private void log(String s) {
        if (log == true) {
            System.out.print(s);
        }
    }

    public void parse() throws IOException {

        this.start();

        while ((CurrectUTF8Char = this.getNextChar()) != -1) {
            // CurrectUTF8Char = Integer.toHexString(ch).toUpperCase();
            if (!DoubleQuoteStarted && !SingleQuoteStarted && (BoomTokenLib.isSpace(CurrectUTF8Char) || BoomTokenLib.isNewline(CurrectUTF8Char))) {

                this.flash();
            } else if (DoubleQuoteStarted || BoomTokenLib.isDoubleQuote(CurrectUTF8Char)) {
                if (DoubleQuoteStarted == false && BoomTokenLib.isDoubleQuote(CurrectUTF8Char)) {
                    //clear last chars as 'this is the start of string'
                    this.flash();
                    DoubleQuoteStarted = true;
                } else if (DoubleQuoteStarted == true && BoomTokenLib.isDoubleQuote(CurrectUTF8Char)
                        && !BoomTokenLib.isEscape(LastUTF8Char)) {
                    //End of string
                    this.flash();
                    DoubleQuoteStarted = false;
                } else {
                    Sentense.add(CurrectUTF8Char);
                }

            } else if (BoomTokenLib.isSymbol(CurrectUTF8Char)) {
                this.flash();
            } else {
                Sentense.add(CurrectUTF8Char);
                //log("0x" + Integer.toHexString(CurrectUTF8Char).toUpperCase() + ", ");
            }

            this.LastUTF8Char = this.CurrectUTF8Char;
        }
        this.end();
        //log("\n");
    }

    private void flash() {
        /**
         * Generate tokens
         */
        if (DoubleQuoteStarted == false) {
            Tokens = this.generateToken(Sentense);
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
            for (Integer tok : Sentense) {
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
        Sentense.clear();
    }

    public ArrayList<String> generateToken(ArrayList<Integer> Sentense) {
        ArrayList<String> Words = new ArrayList<>();
        if (Sentense.size() > 0) {
            String token = BoomTokenLib.searchToken(Sentense);
            if (token != null) {
                Words.add(token);
            } else {
                Words.add("ID_" + Fletcher16.generate(Sentense));
            }
        }
        return Words;
    }

}
