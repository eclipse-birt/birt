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
	@Override
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
	@Override
	public void reuse(Label la, double forceWrappingSize) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getHeight()
	 */
	@Override
	public double getHeight() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getDescent()
	 */
	@Override
	public double getDescent() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getFullHeight()
	 */
	@Override
	public double getFullHeight() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getFullWidth()
	 */
	@Override
	public double getFullWidth() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getLineCount()
	 */
	@Override
	public int getLineCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getLine(int)
	 */
	@Override
	public String getLine(int iIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getLocale()
	 */
	@Override
	public final Locale getLocale() {
		return getULocale().toLocale();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getULocale()
	 */
	@Override
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

	@Override
	public double getFullHeight(double fontHeight) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.ITextMetrics#getWidth(int)
	 */
	@Override
	public double getWidth(int iIndex) {
		return 0d;
	}
}
