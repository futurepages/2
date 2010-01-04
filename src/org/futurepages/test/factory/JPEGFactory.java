package org.futurepages.test.factory;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class JPEGFactory {

	private static JPEGFactory instance;

	private JPEGFactory() {}

	public static JPEGFactory getInstance() {
		if (instance == null) {
			instance = new JPEGFactory();
		}
		return instance;
	}
	
	/**
	 * Cria na repositorio configurado em futurepages.properties
	 * @return
	 * @throws ImageFormatException
	 * @throws IOException
	 */
	public File createJPEG() throws ImageFormatException, IOException{
		String fileName = StringFactory.getRandom(30)+".jpeg";
		
		return createJPEG(fileName);
	}
	
	public File createJPEG(String destinyImageFile) throws ImageFormatException, IOException{
		return createJPEG(300, 200, destinyImageFile);
	}
	
	public File createJPEG(int width, int height, String destinyImageFile) throws ImageFormatException, IOException{
		File file = new File(destinyImageFile);
		createJPEG(width, height, file);
		return file;
	}
	
	public void createJPEG(int width, int height, File destinyImageFile) throws ImageFormatException, IOException{
	  BufferedImage thumbImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
      
      Graphics2D graphics2D = thumbImage.createGraphics();
      graphics2D.drawImage(thumbImage, 0, 0, width, height, null);
      
      FileOutputStream fos = new FileOutputStream(destinyImageFile);
      BufferedOutputStream out = new BufferedOutputStream(fos);
      
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
      JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
      
      param.setQuality(0.0001f, false);
      encoder.setJPEGEncodeParam(param);
      encoder.encode(thumbImage);
      thumbImage.flush();
      out.flush();
      fos.flush();
      fos.close();
      out.close();
	}
	
}