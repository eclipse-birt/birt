/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import java.util.Locale;

import org.eclipse.birt.chart.model.component.Label;

import com.ibm.icu.util.ULocale;

/**
 * A no-op adapter implementation for the
 * {@link org.eclipse.birt.chart.device.ITextMetrics}interface definition.
 */
public class TextAdapter implements ITextMetrics {

	private transient ULocale lcl = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.ITextMetrics#reuse(org.eclipse.birt.chart.model
	 * .component.Label)
	 */
	public void reuse(Label la) {
		reuse(la, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.ITextMetrics#reuse(org.eclipse.birt.chart.model
	 * .component.Label, double)
	 */
	public void reuse(Label la, double forceWrappingSize) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getHeight()
	 */
	public double getHeight() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getDescent()
	 */
	public double getDescent() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getFullHeight()
	 */
	public double getFullHeight() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getFullWidth()
	 */
	public double getFullWidth() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getLineCount()
	 */
	public int getLineCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getLine(int)
	 */
	public String getLine(int iIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getLocale()
	 */
	public final Locale getLocale() {
		return getULocale().toLocale();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getULocale()
	 */
	public final ULocale getULocale() {
		return (lcl == null) ? ULocale.getDefault() : lcl;
	}

	/**
	 * Sets current locale.
	 * 
	 * @param lcl
	 */
	public final void setLocale(ULocale lcl) {
		this.lcl = lcl;
	}

	public double getFullHeight(double fontHeight) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getWidth(int)
	 */
	public double getWidth(int iIndex) {
		return 0d;
	}
}