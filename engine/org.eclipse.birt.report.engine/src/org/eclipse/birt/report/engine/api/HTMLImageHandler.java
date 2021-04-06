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

package org.eclipse.birt.report.engine.api;

import java.util.Map;

import org.eclipse.birt.report.engine.api.script.IReportContext;

public class HTMLImageHandler implements IHTMLImageHandler {

	/**
	 * @deprecated
	 */
	public String onCustomImage(IImage image, Object context) {
		return null;
	}

	public String onCustomImage(IImage image, IReportContext context) {
		return onCustomImage(image, getRenderContext(context));
	}

	/**
	 * @deprecated
	 */
	public String onDesignImage(IImage image, Object context) {
		return null;
	}

	public String onDesignImage(IImage image, IReportContext context) {
		return onDesignImage(image, getRenderContext(context));
	}

	/**
	 * @deprecated
	 */
	public String onDocImage(IImage image, Object context) {
		return null;
	}

	public String onDocImage(IImage image, IReportContext context) {
		return onDocImage(image, getRenderContext(context));
	}

	/**
	 * @deprecated
	 */
	public String onFileImage(IImage image, Object context) {
		return null;
	}

	public String onFileImage(IImage image, IReportContext context) {
		return onFileImage(image, getRenderContext(context));
	}

	/**
	 * @deprecated
	 */
	public String onURLImage(IImage image, Object context) {
		return null;
	}

	public String onURLImage(IImage image, IReportContext context) {
		return onURLImage(image, getRenderContext(context));
	}

	/**
	 * Get render context.
	 * 
	 * @param context
	 * @return
	 */
	protected Object getRenderContext(IReportContext context) {
		if (context == null) {
			return null;
		}
		Map appContext = context.getAppContext();
		if (appContext != null) {
			String renderContextKey = EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT;
			String format = context.getOutputFormat();
			if ("pdf".equalsIgnoreCase(format)) {
				renderContextKey = EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT;
			}
			return appContext.get(renderContextKey);
		}
		return null;
	}

	/**
	 * get the cached image.
	 * 
	 * @param id      cache key
	 * @param type    image type.
	 * @param context report context
	 * @return the cached image
	 */
	public CachedImage getCachedImage(String id, int type, IReportContext context) {
		return null;
	}

	/**
	 * add the image into cache.
	 * 
	 * @param id      cached key
	 * @param type    image type
	 * @param image   image object.
	 * @param context report context
	 * @return the cached image.
	 */
	public CachedImage addCachedImage(String id, int type, IImage image, IReportContext context) {
		return null;
	}
}
