package br.com.semdimapp.semdim.helper;

import android.util.Base64;

/**
 * Classe de codificação e decodificação do e-mail do usuario para base 64
 */
public class Base64Custom {

    /**
     * Codifica uma string para base 64
     * @param text String a ser codificada
     * @return String codificada
     */
    public static String encodeBase64(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT)
                .replaceAll("(\\n|\\r)","");
    }

    /**
     * Decodifica um texto em base 64 para string normal
     * @param encodedString texto em base 64
     * @return String decodificada
     */
    public static String decodeBase64(String encodedString){
        return new String(Base64.decode(encodedString, Base64.DEFAULT));
    }
}
