/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

/**
 * Cached Image Created by the IHTMLImageHandler.
 * 
 * Before call the IHTMLImageHandler to generate the image, the report engine
 * will first ask the handler if there exits a cache for that image. If the
 * cache finded, the report engine will use that cache directly and won't
 * request the handle to create the new image instance any more.
 */
public class CachedImage {

	/**
	 * id of the image.
	 */
	String id;
	/**
	 * url of the image. It is the file path of the cached image, it is used as if
	 * it is defined in the report design directly.
	 */
	String url;
	/**
	 * mime type of the image
	 */
	String mimeType;
	/**
	 * image map of the image.
	 */
	String imageMap;
	/**
	 * image size
	 */
	ImageSize imageSize;

	/**
	 * create an empty cache.
	 */
	public CachedImage() {

	}

	/**
	 * create a instance of cached image.
	 * 
	 * @param id  id of the image.
	 * @param url url of the image
	 */
	public CachedImage(String id, String url) {
		this.id = id;
		this.url = url;
	}

	/**
	 * set the id of the cached image.
	 * 
	 * @param id id of the image.
	 */
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * get the id of the image.
	 * 
	 * @return id of the image.
	 */
	public String getID() {
		return id;
	}

	/**
	 * get the URL of the cached image.
	 * 
	 * The image content can be reterive from that URL.
	 * 
	 * @return url of the image.
	 */
	public String getURL() {
		return url;
	}

	/**
	 * set the image URL.
	 * 
	 * @param url url which refer to the image
	 */
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * get the image map of the image.
	 * 
	 * the image map represents in HTML format.
	 * 
	 * @return the image map of the image.
	 */
	public String getImageMap() {
		return imageMap;
	}

	/**
	 * set the image map of the image.
	 * 
	 * the image map is in HTML format.
	 * 
	 * @param imageMap image map in HTML format.
	 */
	public void setImageMap(String imageMap) {
		this.imageMap = imageMap;
	}

	/**
	 * get the mime type of the image.
	 * 
	 * @return the mime type.
	 */
	public String getMIMEType() {
		return mimeType;
	}

	/**
	 * set the mime type of the image.
	 * 
	 * @param mimeType the mime type of the image
	 */
	public void setMIMEType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * get the image size
	 */
	public ImageSize getImageSize() {
		return imageSize;
	}

	/**
	 * set the image size
	 */
	public void setImageSize(ImageSize size) {
		imageSize = size;
	}
}
