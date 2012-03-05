package org.futurepages.util;

import java.awt.Image;
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
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.SeekableStream;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import org.apache.commons.lang.NotImplementedException;

/**
 * Utilidades para manipula��o de JPEG - Utiliza o Leitor da biblioteca JAI
 * Ler arquivos que fogem da especifica��o padr�o do JPEG
 */
public class JPEGUtil2 {

	public static BufferedImage getBufferedImage(File file) throws IOException {
		SeekableStream seekableStream = new FileSeekableStream(file);
		ParameterBlock pb = new ParameterBlock();
		pb.add(seekableStream);
		return JAI.create("jpeg", pb).getAsBufferedImage();
	}

	public static BufferedImage getBufferedImage(byte[] bytes) throws IOException {
		SeekableStream seekableStream = new ByteArraySeekableStream(bytes);
		ParameterBlock pb = new ParameterBlock();
		pb.add(seekableStream);
		return JAI.create("jpeg", pb).getAsBufferedImage();
	}

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
	 * pathNewFile: endere�o real completo incluindo o nome do arquivo
	 */
	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, true, true, null);
		image.flush();
	}

	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile, int[] subimage) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, true, true, subimage);
		image.flush();
	}

	public static void resizeImagePriorHeight(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, false, true, null);
		image.flush();
	}

	private static void resize(BufferedImage image, int thumbW, int thumbH, int quality, String pathNewFile, boolean priorWidth, boolean stretchWhenSmaller, int[] subimage) throws FileNotFoundException, IOException {
		// Calculos necess�rios para manter as propo�oes da imagem, conhecido como "aspect ratio"

		if (subimage != null) {
			image = image.getSubimage(subimage[0], subimage[1], subimage[2], subimage[3]);
		}

		double thumbRatio = (double) thumbW / (double) thumbH;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (priorWidth) {
			if (thumbRatio < imageRatio) {
				thumbH = (int) (thumbW / imageRatio);
			} else {
				thumbW = (int) (thumbH * imageRatio);
			}
		} else {
			if (thumbRatio < imageRatio) {
				thumbW = (int) (thumbH * imageRatio);
			} else {
				thumbH = (int) (thumbW / imageRatio);
			}
		}

		if (thumbW >= imageWidth || thumbH >= imageHeight) {
			//quando imagem � menor que o resultado final, faz um esticamento para crescer at� o tamanho desejado.
			//quando imagem � menor que o resultado final, faz um resizer pobre
			if (stretchWhenSmaller) {
				poorResize(image, null, thumbW, thumbH, quality, pathNewFile);
			} else {
				poorResize(image, null, imageWidth, imageHeight, quality, pathNewFile);
			}
		} else {
			image = GraphicsUtilities.createThumbnail(image, thumbW, thumbH);

			createJPEG(image, quality, pathNewFile);
		}
	}

	public static void resizeImageByOneDimension(Color color, boolean byWidth, File file, int dimension, int quality, String pathNewFile, boolean stretchWhenSmaller) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);

		resizeByWidth(byWidth, color, image, dimension, quality, pathNewFile, stretchWhenSmaller);
		image.flush();
	}

	public static void resizeImageByOneDimension(Color colorSquare, boolean byWidth, byte[] bytesOfImageFile, int theDimension, int quality, String pathNewFile, boolean stretchWhenSmaller) throws MalformedURLException, FileNotFoundException, IOException {

		BufferedImage image = getBufferedImage(bytesOfImageFile);

		resizeByWidth(byWidth, colorSquare, image, theDimension, quality, pathNewFile, stretchWhenSmaller);
		image.flush();
	}

	/**
	 * Redimensiona imagens (criar thubmnails) - prioriza a largura
	 */
//    A) se altura diferente da largura (imageWidth <> imageHeight):
	// 1) se largura original maior ou igual � largura final (imageWidth >= newWidth):
	//1.1) se largura maior que altura (imageWidth > imageHeight):
	//					#### completa com a cor na altura.
	//1.2) se altura maior que a largura (imageWidth < imageHeight):
	//				    #### redimensiona (diminui) para preencher a largura com a cor.
	//2) se a largura original menor que a largura final (imageWidth < newWidth):
	//2.1) se largura maior que altura (imageWidth > imageHeight):
	//		            #### completa com a cor na altura para formar.
	//2.2) se altura maior que a largura:
	//		            #### redimensiona (aumenta) para preencher a largura com a cor at� form um quadrado ou at� que alcanse a largura desejada.
	//		            #### somente resize padr�o (sem esticar para crescer)
//    B) se altura diferente de largura (imageWidth == imageHeight)
	//		            #### somente resize padr�o (sem esticar para crescer)
	private static void resizeByWidth(boolean reallyByWidth, Color colorSquare, BufferedImage image, int theDimension, int quality, String pathNewFile, boolean stretchWhenSmaller) throws FileNotFoundException, IOException {
		// Calculos necess�rios para manter as propo�oes da imagem, conhecido como "aspect ratio"
		int oW = image.getWidth(null);
		int oH = image.getHeight(null);

		double imageRatio = 0;

		int thumbW = 0, thumbH = 0, oDim1 = 0, oDim2 = 0, dim1 = 0, dim2 = 0;

		if (reallyByWidth) {
			imageRatio = (double) oH / (double) oW;
			thumbW = theDimension;
			thumbH = (int) (thumbW * imageRatio);
			dim1 = thumbW;
			dim2 = thumbH;
			oDim1 = oW;
			oDim2 = oH;

		} else {
			imageRatio = (double) oW / (double) oH;
			thumbH = theDimension;
			thumbW = (int) (thumbH * imageRatio);
			dim1 = thumbH;
			dim2 = thumbW;
			oDim1 = oH;
			oDim2 = oW;
		}

		if (oW != oH) { // && (!(thumbW >= oW || thumbH >= oH))) { antes tinha esse AND, n�o lembro por que eu coloquei, mas quando tirei resolveu alguns problemas (Leandro).
			int pos1 = 0, pos2 = 0, posX = 0, posY = 0, canv1 = 0, canv2 = 0, canvW = 0, canvH = 0;
			if (colorSquare != null) {
				if (oDim1 >= dim1) { //se largura original maior ou igual � largura final (imageWidth >= newWidth):
					if (oDim1 > oDim2) { //se largura maior que altura (imageWidth > imageHeight): //	completa com a cor na altura.
						pos1 = 0;
						pos2 = ((dim1-dim2)/2);
						canv1 = dim1;
						canv2 = dim1;
					} else { //se altura maior que a largura (imageWidth < imageHeight): redimensiona (diminui) para preencher a largura com a cor.
						pos1 =  (dim1 - ((dim1*dim1)/dim2) ) /2;
						pos2 = 0;
						canv1 = dim1;
						canv2 = dim1;
						int tempDim2  = dim1;
						dim1 = (dim1*dim1)/dim2; //depois por conta das sobrescritas do valor. necess�rio ficar aqui.
						dim2 = tempDim2;
					}
				} else { //se a largura original menor que a largura final (oW < oH)
					if (oDim1 > oDim2) {//se largura maior que altura (imageWidth > imageHeight): completa com a cor na altura para formar.
						pos1 = 0;
						pos2 = ((oDim1-oDim2)/2);
						canv1 = oDim1;
						canv2 = oDim1;
						dim1 = oDim1;
						dim2 = oDim2;
					} else { // oDim2 >= oDim1  -> se altura maior que a largura: redimensiona (aumenta) para preencher a largura com a cor at� form um quadrado ou at� que alcanse a largura desejada.
						int oDim2x = (oDim2>theDimension)? theDimension : oDim2;
						pos1 = ((oDim2x-oDim1)/2);
						pos2 = 0;
						dim1 = oDim1;
						dim2 = oDim2;
						canv1 = oDim2x;
						canv2 = oDim2;
					}
				}
				if(reallyByWidth){
					thumbW = dim1;
					thumbH = dim2;
					canvW = canv1;
					canvH = canv2;
					posX  = pos1;
					posY  = pos2;
				}else{
					thumbH = dim1;
					thumbW = dim2;
					canvH = canv1;
					canvW = canv2;
					posY  = pos1;
					posX  = pos2;
				}

				BufferedImage thumbImage = GraphicsUtilities.createCompatibleImage(canvW, canvH);

				Graphics2D graphics2D = thumbImage.createGraphics();
				graphics2D.setBackground(colorSquare);
				graphics2D.setColor(colorSquare);
				graphics2D.fill(new Rectangle2D.Double(0, 0, canvW, canvH));
				graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				graphics2D.drawImage(image, posX, posY, thumbW, thumbH, null);
				image = thumbImage;
			} else {
				image = GraphicsUtilities.createThumbnail(image, thumbW, thumbH);
			}

			createJPEG(image, quality, pathNewFile);

		} else { //imageWidth == imageHeight
			resize(image, thumbW, thumbH, quality, pathNewFile, reallyByWidth, stretchWhenSmaller, null);
		}
	}

	//TODO - Refatorar no futuro. (leandro)
	private static void poorResize(Image image, Color colorSquare, int width, int height, int quality, String pathNewFile) throws FileNotFoundException, IOException {

		BufferedImage thumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics2D = thumbImage.createGraphics();

		graphics2D.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics2D.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		if(colorSquare!=null){
			//TODO - posicionar no quadrado branco.
			throw new NotImplementedException("N�o foi implementado ainda ColorSquare para poorResize");
		}else{
			graphics2D.drawImage(image, 0, 0, width, height, null);
		}

		createJPEG(thumbImage, quality, pathNewFile);
	}

	private static void createJPEG(BufferedImage image, int quality, String pathNewFile) throws FileNotFoundException, IOException {
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
	}
}
