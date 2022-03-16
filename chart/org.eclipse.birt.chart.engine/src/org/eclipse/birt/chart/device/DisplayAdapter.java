/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import java.net.URL;
import java.util.Locale;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IResourceFinder;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.component.Label;

import com.ibm.icu.util.ULocale;

/**
 * A no-op adapter implementation for the
 * {@link org.eclipse.birt.chart.device.IDisplayServer}interface definition.
 */
public class DisplayAdapter implements IDisplayServer {

	/**
	 * An internal instance of the locale being used for processing
	 */
	private transient ULocale lcl = null;

	protected transient IResourceFinder resourceFinder = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#debug()
	 */
	@Override
	public void debug() {
		// DO NOTHING
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDisplayServer#logCreation(java.lang.Object)
	 */
	@Override
	public void logCreation(Object oMisc) {
		// DO NOTHING
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDisplayServer#createFont(org.eclipse.birt.
	 * chart.model.attribute.FontDefinition)
	 */
	@Override
	public Object createFont(FontDefinition fd) {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDisplayServer#getColor(org.eclipse.birt.chart.
	 * model.attribute.ColorDefinition)
	 */
	@Override
	public Object getColor(ColorDefinition cd) {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getDpiResolution()
	 */
	@Override
	public int getDpiResolution() {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
		return 96;
	}

	@Override
	public void setDpiResolution(int dpi) {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#loadImage(java.net.URL)
	 */
	@Override
	public Object loadImage(URL url) throws ChartException {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getSize(java.lang.Object)
	 */
	@Override
	public Size getSize(Object oImage) {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getObserver()
	 */
	@Override
	public Object getObserver() {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDisplayServer#getTextMetrics(org.eclipse.birt.
	 * chart.model.component.Label)
	 */
	@Override
	public ITextMetrics getTextMetrics(Label la) {
		return getTextMetrics(la, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getLocale()
	 */
	@Override
	public final Locale getLocale() {
		return getULocale().toLocale();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getULocale()
	 */
	@Override
	public final ULocale getULocale() {
		return (lcl == null) ? ULocale.getDefault() : lcl;
	}

	/**
	 * A convenience method provided to associate a locale with a display server
	 *
	 * @param lcl The locale to be set
	 */
	@Override
	public final void setLocale(ULocale lcl) {
		this.lcl = lcl;
	}

	@Override
	public void dispose() {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
	}

	@Override
	public void setGraphicsContext(Object graphicContext) {
		// NO-OP ADAPTER DEFAULT IMPLEMENTATION
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDisplayServer#setResourceFinder(org.eclipse
	 * .birt.chart.util.IResourceFinder)
	 */
	@Override
	public void setResourceFinder(IResourceFinder resourceFinder) {
		this.resourceFinder = resourceFinder;
	}

	protected URL findResource(URL urlOriginal) {
		if (resourceFinder != null) {
			URL urlFound = resourceFinder.findResource(urlOriginal.getPath());
			if (urlFound != null) {
				return urlFound;
			}
		}
		return urlOriginal;
	}

	@Override
	public ITextMetrics getTextMetrics(Label la, boolean autoReuse) {
		return null;
	}

}
