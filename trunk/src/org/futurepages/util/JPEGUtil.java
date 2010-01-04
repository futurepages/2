package org.futurepages.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/**
 * Utilidades para manipulação de JPEG
 */
public class JPEGUtil{

    /**
     * Retorna a largura em pixels da imagem.
     * @param file
     * @return
     * @throws java.net.MalformedURLException
     */
    public static int getWidth(File file) throws MalformedURLException{
        Image image = new ImageIcon(file.toURI().toURL()).getImage();
        return image.getWidth(null);
    }

     /**
      * Retorna a largura em pixels da imagem.
      */
    public static int getHeight(File file) throws MalformedURLException{
        Image image = new ImageIcon(file.toURI().toURL()).getImage();
        return image.getHeight(null);
    }
    
    /**
     * Redimensiona arquivo (File) e retorna a imagem redimensionada (pathNewFile)
     * File: Arquivo de entrada
     * width, height: largura e altura da nova imagem
     * pathNewFile: endereço real completo incluindo o nome do arquivo
     */
     public static void resizeImage(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException{
            Image image = new ImageIcon(file.toURI().toURL()).getImage();
            resize(image, width, height, quality , pathNewFile);
            image.flush();
    }
    
    /**
     * Redimensiona imagens (criar thubmnails)
     */
    private static void resize(Image image, int width, int height, int quality, String pathNewFile) throws FileNotFoundException, IOException {
        // Calculos necessários para manter as propoçoes da imagem, conhecido como "aspect ratio"
        double thumbRatio = (double) width / (double) height;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        
        double imageRatio = (double) imageWidth / (double) imageHeight;
        
        if (thumbRatio < imageRatio) {
            height = (int) (width / imageRatio);
        } else {
            width = (int) (height * imageRatio);
        }
        // Fim do cálculo
        
        BufferedImage thumbImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = thumbImage.createGraphics();

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.dispose();
        
        FileOutputStream fos = new FileOutputStream(pathNewFile);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
        quality = Math.max(0, Math.min(quality, 100));
        param.setQuality((float) quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(thumbImage);
        thumbImage.flush();
        out.flush();
        fos.flush();
        fos.close();
        out.close();
    }
}