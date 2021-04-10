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

package org.eclipse.birt.chart.device.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.TextAdapter;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.TextLayout;

/**
 * 
 */
public final class SwtTextMetrics extends TextAdapter {

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	private int iLineCount = 0;

	private double cachedWidth;

	private String[] oText = null;

	private double[] faWidth = null;

	private GC gc = null;

	private Label la = null;

	private final IDisplayServer ids;

	private Font font;

	private Insets ins;

	/**
	 * The constructor initializes a tiny image that provides a graphics context
	 * capable of performing computations in the absence of a visual component
	 * 
	 * @param _ids
	 * @param _la
	 * @param gc
	 * @param autoReuse
	 */
	public SwtTextMetrics(final IDisplayServer _ids, Label _la, GC gc, boolean autoReuse) {
		this.gc = gc;
		ids = _ids;
		la = _la;

		if (autoReuse) {
			reuse(la);
		}
	}

	public SwtTextMetrics(final IDisplayServer _ids, Label _la, GC gc) {
		this(_ids, _la, gc, true);
	}

	/**
	 * Allows reuse of the multi-line text element for computing bounds of a
	 * different font
	 * 
	 * @param fd
	 */
	public final void reuse(Label la, double forceWrappingSize) {
		cachedWidth = Double.NaN;

		String s = la.getCaption().getValue();

		if (s == null) {
			s = IConstants.NULL_STRING;
		} else {
			// trim leading and trailing spaces.
			s = s.trim();
		}
		String[] sa = splitOnBreaks(s, forceWrappingSize);
		if (sa == null) {
			iLineCount = 1;
			oText = new String[] { s };
		} else {
			iLineCount = sa.length;
			oText = sa;
		}

		ins = goFactory.scaleInsets(la.getInsets(), ids.getDpiResolution() / 72d);

		if (forceWrappingSize > 0) {
			// update label with new broken content.
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < oText.length; i++) {
				sb.append(oText[i]).append("\n"); //$NON-NLS-1$
			}

			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}

			la.getCaption().setValue(sb.toString());
		}
	}

	/**
	 * Disposal of the internal image
	 */
	public final void dispose() {
		disposeFont();
	}

	public void disposeFont() {
		if (font != null) {
			font.dispose();
			font = null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isDisposed() {
		return gc.isDisposed();
	}

	protected Font getFont() {
		if (null == font) {
			font = (Font) ids.createFont(la.getCaption().getFont());
		}
		return font;
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getHeight() {
		gc.setFont(getFont());
		final int iHeight = gc.getFontMetrics().getHeight();
		return iHeight;
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getDescent() {
		gc.setFont(getFont());
		final int iDescent = gc.getFontMetrics().getDescent();
		return iDescent;
	}

	/**
	 * 
	 * @return The width of the line containing the maximum width (if multiline
	 *         split by hard breaks) or the width of the single line of text
	 */
	private final double stringWidth() {
		if (!Double.isNaN(cachedWidth)) {
			return cachedWidth;
		}

		faWidth = new double[iLineCount];
		cachedWidth = 0;
		gc.setFont(getFont());
		double dWidth;
		if (iLineCount > 1) {
			String[] sa = oText;
			for (int i = 0; i < iLineCount; i++) {
				dWidth = gc.textExtent(sa[i]).x;
				faWidth[i] = dWidth;
				if (dWidth > cachedWidth) {
					cachedWidth = dWidth;
				}
			}
		} else {
			cachedWidth = gc.textExtent(oText[0]).x;
			faWidth[0] = cachedWidth;
		}
		return cachedWidth;
	}

	public final double getFullHeight() {

		return getHeight() * getLineCount() + ins.getTop() + ins.getBottom();
	}

	@Override
	public final double getFullHeight(double fontHeight) {

		return fontHeight * getLineCount() + ins.getTop() + ins.getBottom();
	}

	public final double getFullWidth() {

		return stringWidth() + ins.getLeft() + ins.getRight();
	}

	@Override
	public double getWidth(int iIndex) {
		if (faWidth == null) {
			stringWidth();
		}
		return faWidth[iIndex];
	}

	/**
	 * 
	 * @return The number of lines created due to the hard breaks inserted
	 */
	public final int getLineCount() {
		return iLineCount;
	}

	/**
	 * 
	 * @return The line requested for
	 */
	public final String getLine(int iIndex) {
		return (iLineCount > 1) ? oText[iIndex] : oText[0];
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private String[] splitOnBreaks(String s, double maxSize) {
		List<String> al = new ArrayList<String>();

		// check hard break first
		int i = 0, j;
		do {
			j = s.indexOf('\n', i);

			if (j == -1) {
				j = s.length();
			}
			String ss = s.substring(i, j);
			if (ss != null && ss.length() > 0) {
				al.add(ss);
			}

			i = j + 1;

		} while (j != -1 && j < s.length());

		// check wrapping
		if (maxSize > 0) {
			TextLayout tl = new TextLayout(((SwtDisplayServer) ids).getDevice());
			tl.setFont(getFont());
			tl.setWidth((int) maxSize);

			List<String> nal = new ArrayList<String>();

			for (Iterator<String> itr = al.iterator(); itr.hasNext();) {
				String ns = itr.next();

				tl.setText(ns);

				int[] offsets = tl.getLineOffsets();
				String ss;

				for (i = 1; i < offsets.length; i++) {
					ss = ns.substring(offsets[i - 1], offsets[i]);

					nal.add(ss);
				}
			}

			tl.dispose();

			al = nal;
		}

		final int n = al.size();
		if (n == 1 || n == 0) {
			return null;
		}

		final String[] sa = new String[n];
		for (i = 0; i < al.size(); i++) {
			sa[i] = al.get(i);
		}
		return sa;
	}
}