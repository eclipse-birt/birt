/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.tests.engine.EngineCase;


public class HTMLCompleteImageHandlerTest extends EngineCase {

	/**
	 * @param name
	 */
	public HTMLCompleteImageHandlerTest(String name) {
		super(name);
	}
	
	/**
	 * Test suite()
	 * @return
	 */
	public static Test suite(){
		return new TestSuite(HTMLCompleteImageHandlerTest.class);
	}
	
	/**
	 * Test OnDesignImage() method
	 *
	 */
	public void testOnDesignImage(){
		try{
			//Get embedded image byte array
/*			String plug_path=EngineCase.PLUGIN_PATH;
			String file_path=EngineCase.RESOURCE_BUNDLE.getString("CASE_INPUT")
						+System.getProperty("file.separator")+"EmbedImage.txt";
			String path=plug_path+System.getProperty("file.separator")+file_path;
*/			
			String path=getBaseFolder()+System.getProperty("file.separator")
			+ INPUT_FOLDER+System.getProperty("file.separator")
			+"EmbedImage.txt";

			File imageFile=new File(path);
			long size=imageFile.length();
			InputStream is = new BufferedInputStream(new FileInputStream(imageFile));		    
			byte[] imageBytes=new byte[(int)size];
			is.read(imageBytes);
			assertNotNull(imageBytes);
			
			//Test onDesignImage()
			
			HTMLRenderContext context=new HTMLRenderContext();
			HTMLCompleteImageHandler imageHandler=new HTMLCompleteImageHandler();
			Image image=new Image(imageBytes,"image1");
			RenderOptionBase option=new RenderOptionBase();
			image.setRenderOption(option);
			File f=null;
			int count=0;
			String fPath=System.getProperty("user.dir");
			do{
				count++;
				String fp=fPath+System.getProperty("file.separator")+"design"+String.valueOf(count);
				f = new File(fp); //$NON-NLS-1$
				if(f.exists()){
					f.delete();
					continue;
				}else{
					break;
				}
			}while(true);
			
			String str=imageHandler.onDesignImage(image,context);
			String strGet="./design1";
			assertEquals("",str,strGet);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test onURLImage() method
	 *
	 */
	public void testOnURLImage(){
		String url="http://image";
		Image image=new Image(url);
		RenderOptionBase option=new RenderOptionBase();
		image.setRenderOption(option);
		HTMLRenderContext context=new HTMLRenderContext();
		HTMLCompleteImageHandler handler=new HTMLCompleteImageHandler();
		String urlGet=handler.onURLImage(image,context);
		assertEquals("OnURLImage() fail",url,urlGet);
	}
	
	/**
	 * Test onCustomeImage() method
	 *
	 */
	public void testOnCustomImage(){
		try{
			//Get embedded image byte array
/*			String plug_path=EngineCase.PLUGIN_PATH;
			String file_path=EngineCase.RESOURCE_BUNDLE.getString("CASE_INPUT")
						+System.getProperty("file.separator")+"EmbedImage.txt";
			String path=plug_path+System.getProperty("file.separator")+file_path;
*/			
			String path=getBaseFolder()+System.getProperty("file.separator")
			+ INPUT_FOLDER+System.getProperty("file.separator")
			+"EmbedImage.txt";

			File imageFile=new File(path);
			long size=imageFile.length();
			InputStream is = new BufferedInputStream(new FileInputStream(imageFile));		    
			byte[] imageBytes=new byte[(int)size];
			is.read(imageBytes);
			
			//Test onDesignImage()
			
			HTMLRenderContext context=new HTMLRenderContext();
			HTMLCompleteImageHandler imageHandler=new HTMLCompleteImageHandler();
			Image image=new Image(imageBytes,"image1");
			RenderOptionBase option=new RenderOptionBase();
			image.setRenderOption(option);
			File f=null;
			int count=1;
			String fPath=System.getProperty("user.dir");
			do{
				count++;
				String fp=fPath+System.getProperty("file.separator")+"custom"+String.valueOf(count);
				f = new File(fp); //$NON-NLS-1$
				if(f.exists()){
					f.delete();
					continue;
				}else{
					break;
				}
			}while(true);
			
			String str=imageHandler.onCustomImage(image,context);
			String strGet="./custom2";
			assertEquals("",str,strGet);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test onDocImage() method
	 * Not implemented at 1.0.1,so return null.
	 */
	public void testOnDocImage(){
		String url="http://image";
		Image image=new Image(url);
		RenderOptionBase option=new RenderOptionBase();
		image.setRenderOption(option);
		HTMLRenderContext context=new HTMLRenderContext();
		HTMLCompleteImageHandler handler=new HTMLCompleteImageHandler();
		String urlGet=handler.onDocImage(image,context);
		assertNull("OnDocImage() fail",urlGet);
	}
}
