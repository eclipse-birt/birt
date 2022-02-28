/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.i18n.MessageConstants;

/**
 * Default implementation for writing images in a form that is used in a
 * web-application.
 */
public class HTMLServerImageHandler extends HTMLImageHandler {

	protected Logger log = Logger.getLogger(HTMLServerImageHandler.class.getName());

	private String handlerId;
	private int count = 0;

	private static HashMap map = new HashMap();

	/**
	 * dummy constructor
	 */
	public HTMLServerImageHandler() {
		String codePart = Integer.toHexString(this.hashCode());
		String timePart = Long.toHexString(System.currentTimeMillis());
		handlerId = codePart + timePart;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onDesignImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	@Deprecated
	@Override
	public String onDesignImage(IImage image, Object context) {
		return handleImage(image, context, "design", true); //$NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 *
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onDocImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	@Deprecated
	@Override
	public String onDocImage(IImage image, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onURLImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	@Deprecated
	@Override
	public String onURLImage(IImage image, Object context) {
		assert (image != null);
		String uri = image.getID();
		if (uri.startsWith("http:") || uri.startsWith("https:")) {
			return uri;
		}
		return handleImage(image, context, "uri", true); //$NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 *
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onCustomImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	@Deprecated
	@Override
	public String onCustomImage(IImage image, Object context) {
		return handleImage(image, context, "custom", false); //$NON-NLS-1$
	}

	/**
	 * returns a unique file name based on a directory and name prefix
	 *
	 * @param imageDir directory to store the image
	 * @param prefix   prefix for the file name
	 * @return a file name
	 */
	protected String createUniqueFileName(String imageDir, String prefix) {
		return createUniqueFileName(imageDir, prefix, null);
	}

	/**
	 * creates a unique tempoary file to store an image
	 *
	 * @param imageDir directory to put image into
	 * @param prefix   file name prefix
	 * @param postfix  file name postfix
	 * @return a Java File Object
	 */
	protected String createUniqueFileName(String imageDir, String prefix, String postfix) {
		File file = null;
		postfix = (postfix == null ? "" : postfix);
		String uniCount = null;
		do {
			uniCount = genUniqueCount();
			file = new File(imageDir + "/" + prefix + uniCount + postfix); //$NON-NLS-1$
		} while (file.exists());

		return prefix + uniCount + postfix;
	}

	/**
	 * Generating unique string which contains following parts
	 * <li>the hashcode of the image handler
	 * <li>creation time of the image handler
	 * <li>image count created by the image handler
	 *
	 * @return return the unique count for filename
	 */
	synchronized private String genUniqueCount() {
		count++;
		return handlerId + count;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onFileImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	@Deprecated
	@Override
	public String onFileImage(IImage image, Object context) {
		return handleImage(image, context, "file", true); //$NON-NLS-1$
	}

	/**
	 * handles an image report item and returns an image URL
	 *
	 * @param image   represents the image design information
	 * @param context context information
	 * @param prefix  image prefix in URL
	 * @param needMap whether image map is needed
	 * @return URL for the image
	 */
	protected String handleImage(IImage image, Object context, String prefix, boolean needMap) {
		String mapID = null;
		if (needMap) {
			mapID = getImageMapID(image);
			if (map.containsKey(mapID)) {
				synchronized (map) {
					return (String) map.get(mapID);
				}
			}
		}
		String ret = null;
		if (context != null && (context instanceof HTMLRenderContext)) {
			HTMLRenderContext myContext = (HTMLRenderContext) context;
			String imageURL = myContext.getBaseImageURL();
			String imageDir = myContext.getImageDirectory();
			if (imageURL == null || imageURL.length() == 0 || imageDir == null || imageDir.length() == 0) {
				log.log(Level.SEVERE, "imageURL or ImageDIR is not set!"); //$NON-NLS-1$
				return null;
			}

			String extension = image.getExtension();
			String fileName = createUniqueFileName(imageDir, prefix, extension);
			File file = new File(imageDir, fileName); // $NON-NLS-1$
			try {
				image.writeImage(file);
			} catch (IOException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			// servlet mode
			if (imageURL.indexOf("?") > 0) //$NON-NLS-1$
			{
				ret = imageURL + fileName;
			} else if (imageURL.endsWith("/")) //$NON-NLS-1$
			{
				ret = imageURL + fileName;
			} else {
				ret = imageURL + "/" + fileName; //$NON-NLS-1$
			}

			if (needMap) {
				synchronized (map) {
					map.put(mapID, ret);
				}
			}

		} else {
			ret = handleTempImage(image, prefix, needMap);
		}

		return ret;
	}

	/**
	 * Handle a temporary image .
	 *
	 * @param image   represents the image design information
	 * @param prefix  image prefix in URL
	 * @param needMap whether image map is needed
	 * @return
	 */
	protected String handleTempImage(final IImage image, final String prefix, boolean needMap) {
		try {
			String fileName = AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {

				@Override
				public String run() throws IOException {
					File tempFile = File.createTempFile(prefix, ".img");
					image.writeImage(tempFile);
					return tempFile.getAbsolutePath();
				}
			});

			if (needMap) {
				String mapID = getImageMapID(image);
				synchronized (map) {
					map.put(mapID, fileName);
				}
			}
			return fileName;
		} catch (PrivilegedActionException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "unknow.img";
	}

	/**
	 * returns the unique identifier for the image
	 *
	 * @param image the image object
	 * @return the image id
	 */
	protected String getImageMapID(IImage image) {
		if (image.getReportRunnable() != null) {
			return image.getReportRunnable().hashCode() + image.getID();
		}
		return image.getID();
	}

	/**
	 * get image
	 *
	 * @param out      the output stream of image
	 * @param imageDir the image directory
	 * @param imageID  id of image
	 * @throws IOException
	 */
	public void getImage(OutputStream out, String imageDir, String imageID) throws EngineException {
		// As imageID is created by handleImage(), we need check the imageID first to
		// avoid
		// user uses this API read arbitrary file in disk.
		if (imageID.indexOf("./") != -1 || imageID.indexOf(".\\") != -1) {
			throw new EngineException(MessageConstants.MISSING_IMAGE_FILE_ERROR); // $NON-NLS-1$
		}
		File imageFile = new File(imageDir, imageID);
		if (!imageFile.exists()) {
			throw new EngineException(MessageConstants.MISSING_IMAGE_FILE_ERROR); // $NON-NLS-1$
		}
		InputStream in = null;
		try {
			in = new FileInputStream(imageFile);
			byte[] buffer = new byte[1024];
			int size = 0;
			do {
				size = in.read(buffer);
				if (size > 0) {
					out.write(buffer, 0, size);
				}

			} while (size > 0);
			in.close();
		} catch (IOException ex) {
			throw new EngineException(MessageConstants.ERROR, ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException er) {

			}
		}

	}
}
