/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.util.FileUtil;

/**
 * Default implementation for writing images in a form that is compatible with a
 * web browser's "HTML Complete" save option, i.e., writes images to a
 * predefined folder.
 * 
 * ImageDirectory: absolute path save the image into that directy, return the
 * aboluste URL of that image.
 * 
 * ImageDirectory: null, treat it as "." ImageDirectory: relative relative to
 * the base folder.
 * 
 * BaseFolder: parent folder of the output file, save the file into image
 * directory and return the relative path (base on the base folder).
 * 
 * BaseFolder:null, use "." as the base folder and return the aboluste path,
 * 
 * 
 */
public class HTMLCompleteImageHandler extends HTMLImageHandler {

	protected Logger log = Logger.getLogger(HTMLCompleteImageHandler.class.getName());

	private static int count = 0;

	private static HashMap map = new HashMap();

	/**
	 * dummy constructor
	 */
	public HTMLCompleteImageHandler() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onDesignImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
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
	public String onDocImage(IImage image, Object context) {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onURLImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onURLImage(IImage image, Object context) {
		assert (image != null);
		return image.getID();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onCustomImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onCustomImage(IImage image, Object context) {
		return handleImage(image, context, "custom", false); //$NON-NLS-1$
	}

	/**
	 * Creates a unique temporary file to store an image
	 * 
	 * @param imageDir directory to put image into
	 * @param prefix   file name prefix
	 * @param postfix  file name suffix
	 * @return a Java File Object
	 */
	protected File createUniqueFile(String imageDir, String prefix, String postfix) {
		assert prefix != null;
		if (postfix == null) {
			postfix = "";
		}
		File file = null;
		do {
			count++;
			file = new File(imageDir + "/" + prefix + count + postfix); //$NON-NLS-1$
		} while (file.exists());

		return new File(imageDir, prefix + count + postfix); // $NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onFileImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
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
				return (String) map.get(mapID);
			}
		}

		String imageDirectory = null;
		if (context != null && (context instanceof HTMLRenderContext)) {
			HTMLRenderContext myContext = (HTMLRenderContext) context;
			imageDirectory = myContext.getImageDirectory();
		}
		if (imageDirectory == null) {
			IReportRunnable runnable = image.getReportRunnable();
			if (runnable != null) {
				IReportEngine engine = runnable.getReportEngine();
				if (engine != null) {
					EngineConfig config = engine.getConfig();
					if (config != null) {
						imageDirectory = config.getTempDir();
					}
				}
			}
		}
		if (imageDirectory == null) {
			imageDirectory = FileUtil.getJavaTmpDir();
		}
		if (imageDirectory == null) {
			imageDirectory = ".";
		}

		String outputFile = null;
		IRenderOption renderOption = image.getRenderOption();
		if (renderOption != null) {
			Map outputSetting = renderOption.getOutputSetting();
			if (outputSetting != null) {
				outputFile = (String) image.getRenderOption().getOutputSetting().get(RenderOptionBase.OUTPUT_FILE_NAME);
			}
		}

		boolean returnRelativePath = needRelativePath(outputFile, imageDirectory);
		String imageOutputDirectory = getImageOutputDirectory(outputFile, imageDirectory);
		File file = saveImage(image, prefix, imageOutputDirectory);
		String outputPath = getOutputPath(returnRelativePath, imageDirectory, file);
		if (needMap) {
			map.put(mapID, outputPath);
		}
		return outputPath;
	}

	/**
	 * Save image to the output directory.
	 * 
	 * @param image
	 * @param prefix
	 * @param imageOutputDirectory
	 * @return
	 */
	private File saveImage(IImage image, String prefix, String imageOutputDirectory) {
		File file;
		synchronized (HTMLCompleteImageHandler.class) {
			file = createUniqueFile(imageOutputDirectory, prefix, image.getExtension());
			try {
				image.writeImage(file);
			} catch (IOException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return file;
	}

	private String getTempFile() {
		return new File(".").getAbsolutePath();
	}

	/**
	 * 
	 * @param reportOutputFile
	 * @param imageDirectory
	 * @return
	 */
	private boolean needRelativePath(String reportOutputFile, String imageDirectory) {
		if (reportOutputFile == null) {
			return false;
		}
		if (!FileUtil.isRelativePath(imageDirectory)) {
			return false;
		}
		return true;
	}

	/**
	 * Get the output directory for the image.
	 * 
	 * @param reportOutputFile
	 * @param imageDirectory
	 * @return
	 */
	private String getImageOutputDirectory(String reportOutputFile, String imageDirectory) {
		if (!FileUtil.isRelativePath(imageDirectory)) {
			return imageDirectory;
		}

		String reportOutputDirectory;
		if (reportOutputFile == null) {
			reportOutputDirectory = getTempFile();
		} else {
			reportOutputDirectory = new File(reportOutputFile).getAbsoluteFile().getParent();
		}

		return reportOutputDirectory + File.separator + imageDirectory;
	}

	/**
	 * Get the output path.
	 * 
	 * @param needRelativePath
	 * @param imageDirectory
	 * @param outputFile
	 * @return
	 */
	private String getOutputPath(boolean needRelativePath, String imageDirectory, File outputFile) {
		String result = null;
		if (needRelativePath) {
			result = imageDirectory + "/" + outputFile.getName();
		} else {
			try {
				result = outputFile.toURL().toExternalForm();
			} catch (Exception ex) {
				result = outputFile.getAbsolutePath();
			}
		}
		return result;
	}

	/**
	 * returns the unique identifier for the image
	 * 
	 * @param image the image object
	 * @return the image id
	 */
	protected String getImageMapID(IImage image) {
		if (image.getReportRunnable() != null)
			return image.getReportRunnable().hashCode() + image.getID();
		return image.getID();
	}
}