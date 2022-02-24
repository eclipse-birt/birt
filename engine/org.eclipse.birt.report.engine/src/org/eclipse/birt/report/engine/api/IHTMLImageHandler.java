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

import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * Defines the image handler interface for use in HTML format
 */
public interface IHTMLImageHandler {
	/**
	 * handles a design image. The implementation supplies a URL and optionally
	 * stores the image.
	 * 
	 * @param image   the image definition object
	 * @param context the context for generating the URL
	 * @return the URL for the image
	 * @deprecated
	 */
	public abstract String onDesignImage(IImage image, Object context);

	public abstract String onDesignImage(IImage image, IReportContext context);

	/**
	 * handles a database image. The implementation supplies a URL and optionally
	 * stores the image.
	 * 
	 * @param image   the image definition object
	 * @param context the context for generating the URL
	 * @return the URL for the image
	 * @deprecated
	 */
	public abstract String onDocImage(IImage image, Object context);

	public abstract String onDocImage(IImage image, IReportContext context);

	/**
	 * handles a image specified as a on-disk URI. The implementation supplies a URL
	 * and optionally stores the image.
	 * 
	 * @param image   the image definition object
	 * @param context the context for generating the URL
	 * @return the URL for the image
	 * @deprecated
	 */
	public abstract String onFileImage(IImage image, Object context);

	public abstract String onFileImage(IImage image, IReportContext context);

	/**
	 * handles an image specified as an external URL. The implementation supplies a
	 * URL and optionally stores the image.
	 * 
	 * @param image   the image definition object
	 * @param context the context for generating the URL
	 * @return the URL for the image
	 * @deprecated
	 */
	public abstract String onURLImage(IImage image, Object context);

	public abstract String onURLImage(IImage image, IReportContext context);

	/**
	 * handles a custom image created for example, by chart extension. The
	 * implementation supplies a URL and optionally stores the image.
	 * 
	 * @param image   the image definition object
	 * @param context the context for generating the URL
	 * @return the URL for the image
	 * @deprecated
	 */
	public abstract String onCustomImage(IImage image, Object context);

	public abstract String onCustomImage(IImage image, IReportContext context);

	/**
	 * get the cached image for that id.
	 * 
	 * The CachedImage object contains: URL: the absolute file path of the image.
	 * MIMETYPE: the mimetype of the image IMAGEMAP: the image map of the image.
	 * 
	 * @param id      id of the image
	 * @param type    type of the image, one defined in the IImage
	 * @param context script context
	 * @return CachedImage object if find, otherwise, return null.
	 */
	public abstract CachedImage getCachedImage(String id, int type, IReportContext context);

	/**
	 * add the image into image cache, so it can be accessed through
	 * <code>getCachedImage</code>.
	 * 
	 * @param id      cache key
	 * @param type    image type
	 * @param image   image object
	 * @param context report context
	 * @return the cached image.
	 */
	public abstract CachedImage addCachedImage(String id, int type, IImage image, IReportContext context);
}
