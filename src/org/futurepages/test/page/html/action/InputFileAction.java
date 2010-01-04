package org.futurepages.test.page.html.action;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.futurepages.test.page.FutureSelenium;

public class InputFileAction extends UIElement implements Action {

	public InputFileAction() {
		super(FutureSelenium.getInstance());
	}

	public InputFileAction(FutureSelenium futureSelenium) {
		super(futureSelenium);
	}

	/**
	 * @param name
	 *            nome identificado do Input
	 * @param args
	 *            argumentos ao input args[0]: caminho do arquivo
	 */
	public void act(String name, Object... args) {
		if (args != null && args.length > 0) {
			
			Number positionLeft = selenium.getElementPositionLeft(name); 
			Number positionTop = selenium.getElementPositionTop(name); 
			
			String valor = args[0].toString();
//			selenium.type("", "","");
//		    selenium.focus("upload_field");
//
//		    //Type using Robot
//		    selenium.typeKeysUsingRobot("c:\\some_xml_doc.xml");
//
//		    //Submit the Form
//		    selenium.submit("form_name");
			
			
			
			
//			new FileChooserThread(name,valor ).start();
//			selenium.click(name);
//			selenium.clickAt("file", positionLeft + "," + positionTop); 
		}
	}

//	public class FileChooserThread extends Thread { 
//		public FileChooserThread(String name, String fileName) { 
//			super(new FileRunner(name, fileName)); 
//		} 
//	}
//
//	class FileRunner implements Runnable { 
//
//		private String inputName; 
//		private String fileName; 
//
//		public FileRunner(String name, String fileName) { 
//			this.inputName = name; 
//			this.fileName = fileName; 
//		} 
//
//		public void run() {
//			chooseFile(inputName, fileName);
//		}
//
//		private void chooseFile(String name, String fileName) {
//			try { 
//				System.out.println("InputFileAction.chooseFile() antes do click....");
//				selenium.click(name);
//				System.out.println("InputFileAction.chooseFile() depois do click....");
//
//				Thread.sleep(1000); 
//				System.out.println("InputFileAction.chooseFile() depois do sleep....");
//				Robot robot = new Robot(); 
//				System.out.print("InputFileAction.chooseFile() file:");
//				for (char c : fileName.toCharArray()){
//					System.out.print(c);
//					if (c == ':') { 
//						robot.keyPress(KeyEvent.VK_SHIFT); 
//						robot.keyPress(KeyEvent.VK_SEMICOLON); 
//						robot.keyRelease(KeyEvent.VK_SHIFT); 
//					} else if (c == '/') { 
//						robot.keyPress(KeyEvent.VK_BACK_SLASH); 
//					} else { 
//						robot.keyPress(KeyStroke.getKeyStroke( Character.toUpperCase(c), 0).getKeyCode()); 
//					} 
//				} 
//				robot.keyPress(KeyEvent.VK_ENTER); 
//			} catch (Exception e) { 
//				throw new RuntimeException(e);
//			}
//		}
//	}
}