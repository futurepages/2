package org.futurepages.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodingUtil {
	
	public static String getSystemEncoding(){
		return System.getProperty("file.encoding");
	}

    public static String correctPath(String wrongPath) throws UnsupportedEncodingException{
        return URLDecoder.decode(wrongPath, EncodingUtil.getSystemEncoding());
    }

    public static String convertOutputString(String source, String charset) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(baos, charset);
        out.write(source);
        out.close();
        return baos.toString();
     }

    public static String convertInputString(String input, String charset)
        throws IOException
    {
        int CHAR_BUFFER_SIZE = 8096;
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        InputStreamReader in = new InputStreamReader(bais, charset);
        StringBuffer output = new StringBuffer();
        char buff[] = new char[CHAR_BUFFER_SIZE];
        int count = 0;
        while ((count = in.read(buff, 0, CHAR_BUFFER_SIZE)) > 0)
        {
            output.append(buff, 0, count);
        }
        in.close();
        bais.close();
        return output.toString();
     }

    public static String convert(String str, String fromCharset, String toCharset) throws IOException{
        if(str == null) return "";
        str = convertInputString(str, fromCharset);
        return convertOutputString(str, toCharset);
    }

    public static String decodeUrl(String strIn){
        try {
            String decodedUrl = URLDecoder.decode(strIn, "UTF-8");
			return decodedUrl.replaceAll("&frasl;", "/"); //bug - tomcat não reconhece barra.
        } catch (Exception ex) {
           return null;
        }
    }

    public static String encodeUrl(String strIn){
        try {
            return URLEncoder.encode(strIn, "UTF-8");
        } catch (Exception ex) {
           return "";
        }
    }

    public static String toISO(String str){
        try{
            return convert(str, "UTF-8", "ISO-8859-1");
        }
        catch(IOException ex){
            return "";
        }
    }

    public static String toUTF8(String str){
        try{
            return convert(str, "ISO-8859-1","UTF-8");
        }
        catch(IOException ex){
            ex.printStackTrace();
            return "";
        }
    }
}