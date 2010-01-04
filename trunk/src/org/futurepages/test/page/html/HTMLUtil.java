package org.futurepages.test.page.html;
import java.io.FileOutputStream;
import java.io.IOException;

public class HTMLUtil {

		public static void printInFile(String fileName, String fileContent) throws IOException {
			FileOutputStream file = new FileOutputStream(fileName);
			file.write(fileContent.getBytes());
			file.flush();
		}
		
		public static String capitalize(String s) {
			if (HTMLUtil.isStringVazia(s)) {
				return "";
			}
			
			if (isConector(s)) {
				return s.toLowerCase();
			}
			
			if ((s.length() > 1) && (s.substring(0, 2).equalsIgnoreCase("d'"))) {
				return "d'" + Character.toUpperCase(s.charAt(2)) + s.substring(3, s.length()).toLowerCase();
			}
			
			return s.substring(0, 1).toUpperCase().concat(s.substring(1, s.length()).toLowerCase());
		}
		
		private static boolean isConector(String palavra) {
			if (HTMLUtil.isStringVazia(palavra)) {
				return false;
			}
			return (palavra.equalsIgnoreCase("de") || palavra.equalsIgnoreCase("da") || palavra.equalsIgnoreCase("do") || palavra.equalsIgnoreCase("das") || palavra.equalsIgnoreCase("dos") || palavra.equalsIgnoreCase("o") || palavra.equalsIgnoreCase("e") || palavra.equalsIgnoreCase("a"));
		}
		
		public static boolean isStringVazia(String string) {
			return (string == null || string.trim().length() == 0);
		}
		
		public static String modifyFirstCharToLowerCase(String string) {
			return Character.toLowerCase(string.charAt(0)) + string.substring(1, string.length());
		}
		
		/**
		 * Valida o HTML, fechando as tag input e retirando caracteres inválidos para que o HTML possa ser parseado para XML.
		 * @param htmlSource
		 * @return
		 */
		public static String validateHTML(String htmlSource) {
			String textAuxiliar = htmlSource;
//			textAuxiliar.replace("=\"", "=").replace("", "=");
			int indexTagInput = htmlSource.indexOf("<input");
			int indexCloserInput = 0;
			String tagInput = "";
			
			while (indexTagInput != -1) {
				indexCloserInput = textAuxiliar.indexOf(">", indexTagInput);
				tagInput = textAuxiliar.substring(indexTagInput, indexCloserInput + 1);
				htmlSource = htmlSource.replace(tagInput, tagInput + "</input>");
				
				textAuxiliar = textAuxiliar.replaceFirst("<input", "/input");
				indexTagInput = textAuxiliar.indexOf("<input");
			}
			htmlSource = htmlSource.replaceAll("&nbsp;", "");
			htmlSource = htmlSource.replaceAll("<br>", "");
			htmlSource = htmlSource.replaceAll("</br>", "");
			htmlSource = htmlSource.replaceAll("&", ";");
			return htmlSource;
		}
		
	}

