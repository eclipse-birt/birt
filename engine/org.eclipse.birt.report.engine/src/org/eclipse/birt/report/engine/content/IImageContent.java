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

package org.eclipse.birt.report.engine.content;

/**
 * Image content in the report.
 * 
 */
public interface IImageContent extends IContent {

	public final static int IMAGE_FILE = 0;
	public final static int IMAGE_NAME = 1;
	public final static int IMAGE_EXPRESSION = 2;
	public final static int IMAGE_URL = 3;
	/**
	 * @deprecated replaced by IMAGE_URL
	 */
	public final static int IMAGE_URI = 3;

	/**
	 * @return Returns the altText.
	 */
	public String getAltText();

	public String getAltTextKey();

	public void setAltText(String altText);

	public void setAltTextKey(String key);

	void setHelpKey(String key);

	String getHelpKey();

	/**
	 * @return Returns the data.
	 */
	public byte[] getData();

	void setData(byte[] data);

	/**
	 * @return Returns the extension.
	 */
	public String getExtension();

	void setExtension(String extension);

	/**
	 * @return Returns the URI.
	 */
	public String getURI();

	void setURI(String uri);

	/**
	 * Returns the type of image source
	 */
	public int getImageSource();

	void setImageSource(int source);

	/**
	 * @return the image map (null means no image map)
	 */
	public Object getImageMap();

	public void setImageMap(Object map);

	/**
	 * get the MIMEType
	 */
	public String getMIMEType();

	public void setMIMEType(String mimeType);

	public int getResolution();

	public void setResolution(int resolution);
}