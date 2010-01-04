package org.futurepages.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe que possui m�todos est�ticos para tratamento de strings no controle
 * da seguran�a da aplica��o web
 *
 */
public class Security {
    
    /**
     * Classe com m�todos para gest�o de seguran�a
     */
    public Security() {
    }
    
    /**
     * Retorna o valor da string de entrada com codifica��o md5
     *
     */
    public static String md5(String senha){
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        sen = hash.toString(16);
        
        //corre��o da falta de zeros
        if(sen.length()<32){
            int numZeros = 32 - sen.length();
            StringBuffer zeros = new StringBuffer("");
            for(int i = 1 ; i<=numZeros;i++){
                zeros.append("0");
            }
            return (zeros + sen);
        }
        return sen;
    }
    
    /**
     * Filtra a string de entrada e retorna uma string vazia caso exista algum
     * caractere malicioso.
     */
    public static String filtered(String in){
        if(in.contains("'")){
            in = in.replaceAll("'","''");
        }
        if((in.contains("<"))||(in.contains(">"))||(in.contains("%"))||(in.contains(";"))){
            in = "";
        } 
        return in;
    }
    
    /**
     * Inser��o segura, previne inje��o de scripts
     */
    public static String filteredInsert(String in){
        if(in.contains("'"))  in = in.replaceAll("'","''");
        if(in.contains("<"))  in = in.replaceAll("<"," ");
        if(in.contains(">"))  in = in.replaceAll(">"," ");

        return in;
    }
}