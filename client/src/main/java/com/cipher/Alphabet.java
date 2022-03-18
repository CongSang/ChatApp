/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cipher;

/**
 *
 * @author Admin
 */
public class Alphabet {
    private static char[] alphabet = new char[26];

    public Alphabet() {
        for (int i = 0; i < 26; i++) {
            getAlphabet()[i] = Character.toString(65 + i).charAt(0);
        }
    }

    /**
     * @return the alphabet
     */
    public static char[] getAlphabet() {
        return alphabet;
    }
       
}
