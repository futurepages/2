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
import javax.imageio.ImageIO;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

/**
 * Utilidades para manipulação de JPEG
 */
public class JPEGUtil {

	/**
	 * Retorna a largura em pixels da imagem.
	 * @param file
	 * @return
	 * @throws java.net.MalformedURLException
	 */
	public static int getWidth(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		return image.getWidth(null);
	}

	/**
	 * Retorna a largura em pixels da imagem.
	 */
	public static int getHeight(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		return image.getHeight(null);
	}

	/**
	 * Redimensiona arquivo (File) e retorna a imagem redimensionada (pathNewFile)
	 * File: Arquivo de entrada
	 * width, height: largura e altura da nova imagem
	 * pathNewFile: endereço real completo incluindo o nome do arquivo
	 */
	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = ImageIO.read(file);
		resize(image, width, height, quality, pathNewFile, true);
		image.flush();
	}

	public static void resizeImagePriorHeight(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = ImageIO.read(file);
		resize(image, width, height, quality, pathNewFile, false);
		image.flush();
	}

	/**
	 * Redimensiona imagens (criar thubmnails) - prioriza a largura
	 */
	private static void resize(BufferedImage image, int width, int height, int quality, String pathNewFile, boolean priorWidth) throws FileNotFoundException, IOException {
		// Calculos necessários para manter as propoçoes da imagem, conhecido como "aspect ratio"
		double thumbRatio = (double) width / (double) height;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (priorWidth) {
			if (thumbRatio < imageRatio) {
				height = (int) (width / imageRatio);
			} else {
				width = (int) (height * imageRatio);
			}
		}else{
			if (thumbRatio < imageRatio) {
				width = (int) (height * imageRatio);
			} else {
				height = (int) (width / imageRatio);
			}
		}

		// Fim do cálculo
		
		image = GraphicsUtilities.createThumbnail(image,  width, height);

		FileOutputStream fos = new FileOutputStream(pathNewFile);
		BufferedOutputStream out = new BufferedOutputStream(fos);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
		quality = Math.max(0, Math.min(quality, 100));
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(image);

		image.flush();
		out.flush();
		fos.flush();
		fos.close();
		out.close();
//		System.gc();
	}
}
