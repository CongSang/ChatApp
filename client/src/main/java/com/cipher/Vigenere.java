/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cipher;

/**
 *
 * @author Admin
 */
public class Vigenere {
    private static String key = "MYCHATAPP";
    
    public Vigenere() { 
    }
    
    public static String Encode(String text) {
        String result = new String();
        char[] alphabet = new Alphabet().getAlphabet();
        int textIndex = -1, keyIndex = -1, cipherIndex;
        int k = 0;
        
        for(int i = 0; i < text.length(); i++) {      
            if(Character.isSpaceChar(text.charAt(i))) {
                result += text.charAt(i);
                continue;
            }  
            if (k >= key.length()) 
                k = 0;

            for (int j = 0; j < 26; j++) {                  
                if (alphabet[j] == Character.toUpperCase(text.charAt(i))) {
                    textIndex = new String(alphabet).indexOf(alphabet[j]);
                }

                if (alphabet[j] == Character.toUpperCase(key.charAt(k))) {
                    keyIndex = new String(alphabet).indexOf(alphabet[j]);
                }
                
                if (textIndex != -1 && keyIndex != -1) {
                    cipherIndex = (textIndex + keyIndex) % 26;
                    textIndex = -1;
                    keyIndex = -1;
                    k++;

                    if (Character.isUpperCase(text.charAt(i)))
                        result += alphabet[cipherIndex];           
                    else
                        result += Character.toLowerCase(alphabet[cipherIndex]);
                    break;
                }
            }           
        }
        
        return result;
    }
    
    public static String Decode(String text) {
        String result = new String();
        char[] alphabet = new Alphabet().getAlphabet();
        int textIndex = -1, keyIndex = -1, plainIndex;
        int k = 0;
        
        for(int i = 0; i < text.length(); i++) {      
            if(Character.isSpaceChar(text.charAt(i))) {
                result += text.charAt(i);
                continue;
            }  
            if (k >= key.length()) 
                k = 0;

            for (int j = 0; j < 26; j++) {                  
                if (alphabet[j] == Character.toUpperCase(text.charAt(i))) {
                    textIndex = new String(alphabet).indexOf(alphabet[j]);
                }

                if (alphabet[j] == Character.toUpperCase(key.charAt(k))) {
                    keyIndex = new String(alphabet).indexOf(alphabet[j]);
                }
                if (textIndex != -1 && keyIndex != -1) {
                    plainIndex = (textIndex - keyIndex);
                    if (plainIndex < 0)
                        plainIndex = (plainIndex + 26) % 26;
                    textIndex = -1;
                    keyIndex = -1;
                    k++;
                    
                    if (Character.isUpperCase(text.charAt(i)))
                        result += alphabet[plainIndex];
                    else
                        result += Character.toLowerCase(alphabet[plainIndex]);
                    break;
                }
            }       
        }
        
        return result;
    }
    
}
