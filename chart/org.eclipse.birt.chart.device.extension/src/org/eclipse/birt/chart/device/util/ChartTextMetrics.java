/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.TextAdapter;
import org.eclipse.birt.chart.device.g2d.G2dDisplayServerBase;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;

/**
 * Provides metrics for a label containing either one or multiple lines of text.
 */
public final class ChartTextMetrics extends TextAdapter {

	private int iLineCount = 0;

	private Object oText = null;

	private Graphics2D g2d = null;

	private FontMetrics fm = null;

	private ChartTextLayout[] tla = null;

	private String[] fsa = null;

	private double[] faWidth = null;

	private transient Object bi = null;

	private Label la = null;

	private final IDisplayServer xs;

	private Insets ins = null;

	private ITextLayoutFactory textLayoutFactory;

	/**
	 * The constructor initializes a tiny image that provides a graphics context
	 * capable of performing computations in the absence of a visual component
	 * 
	 * @param _xs
	 * @param _la
	 * @param autoReuse
	 */
	public ChartTextMetrics(G2dDisplayServerBase _xs, Label _la, boolean autoReuse) {

		bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) ((BufferedImage) bi).getGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if (_xs.getDpiResolution() >= 192) {
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		}

		xs = _xs;
		la = _la;
		textLayoutFactory = _xs;

		computeTextAntialiasing();

		if (autoReuse) {
			reuse(la);
		}
	}

	public ChartTextMetrics(G2dDisplayServerBase _xs, Label _la) {
		this(_xs, _la, true);
	}

	public void setTextLayoutFactory(ITextLayoutFactory textLayoutFactory) {
		this.textLayoutFactory = textLayoutFactory;
	}

	/**
	 * Only antialias rotated, bold text, and font size > 13
	 * 
	 */
	private void computeTextAntialiasing() {
		FontDefinition font = la.getCaption().getFont();

		if (font.isBold() || (font.getRotation() % 90 != 0) || font.getSize() > 13) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}

	}

	/**
	 * Allows reuse of the multi-line text element for computing bounds of changed
	 * font/text content
	 * 
	 * @param fd
	 */
	public final void reuse(Label la, double forceWrappingSize) {
		final Font f = (Font) xs.createFont(la.getCaption().getFont());
		fm = g2d.getFontMetrics(f);
		final FontRenderContext frc = g2d.getFontRenderContext();

		String s = la.getCaption().getValue();
		if (s == null) {
			s = IConstants.NULL_STRING;
		} else {
			// trim leading and trailing spaces.
			s = s.trim();
		}

		if (s.length() == 0) // TextLayout DOESN'T LIKE EMPTY STRINGS
		{
			s = IConstants.ONE_SPACE;
		}
		String[] sa = splitOnBreaks(s, forceWrappingSize);
		if (sa == null) {
			iLineCount = 1;
			oText = s;
			tla = new ChartTextLayout[1];
			fsa = new String[1];
			tla[0] = textLayoutFactory.createTextLayout(s, f.getAttributes(), frc);
			fsa[0] = s;
		} else {
			iLineCount = sa.length;
			oText = sa;
			tla = new ChartTextLayout[iLineCount];
			fsa = new String[iLineCount];
			for (int i = 0; i < iLineCount; i++) {
				tla[i] = textLayoutFactory.createTextLayout(sa[i], f.getAttributes(), frc);
				fsa[i] = sa[i];
			}
		}
		ins = la.getInsets().scaledInstance(pointsToPixels());

		if (forceWrappingSize > 0) {
			// update label with new broken content.
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fsa.length; i++) {
				sb.append(fsa[i]).append("\n"); //$NON-NLS-1$
			}

			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}

			la.getCaption().setValue(sb.toString());
		}
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getHeight() {
		return fm.getHeight();
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getDescent() {
		return fm.getDescent();
	}

	/**
	 * 
	 * @param fm
	 * @return The width of the line containing the maximum width (if multiline
	 *         split by hard breaks) or the width of the single line of text
	 */
	private final double stringWidth() {
		faWidth = new double[iLineCount];
		Rectangle2D r2d;

		if (iLineCount > 1) {
			double dWidth, dMaxWidth = 0;
			for (int i = 0; i < iLineCount; i++) {
				r2d = tla[i].getBounds();
				dWidth = r2d.getWidth();
				faWidth[i] = Math.max(0, dWidth);

				if (dWidth > dMaxWidth) {
					dMaxWidth = dWidth;
				}
			}

			/**
			 * Fixed for java.awt.font.TextLine.getBounds() bug, when string is blank, e.g.
			 * " ", it will return an negative result.
			 */
			return Math.max(0, dMaxWidth);
		} else if (iLineCount == 1) {
			/**
			 * // double w = tla[0].getBounds( ).getWidth( );
			 * 
			 * Not use the textLayout.getBounds(), this is not correct when the string
			 * contains full pitch characters.
			 */
			double w = fm.getStringBounds(fsa[0], g2d).getWidth();

			/**
			 * Fixed for java.awt.font.TextLine.getBounds() bug, when string is blank, e.g.
			 * " ", it will return an negative result.
			 */
			w = Math.max(0, w);
			faWidth[0] = w;
			return w;
		}

		return 0;
	}

	final double pointsToPixels() {
		return (xs.getDpiResolution() / 72d);
	}

	public final double getFullHeight() {
		return getHeight() * getLineCount() + (ins.getTop() + ins.getBottom());
	}

	@Override
	public final double getFullHeight(double fontHeight) {

		return fontHeight * getLineCount() + ins.getTop() + ins.getBottom();
	}

	public final double getFullWidth() {
		return stringWidth() + (ins.getLeft() + ins.getRight());
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
		return (iLineCount > 1) ? ((String[]) oText)[iIndex] : (String) oText;
	}

	public final ChartTextLayout getLayout(int iIndex) {
		return (iLineCount > 1) ? tla[iIndex] : tla[0];
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
			List<String> nal = new ArrayList<String>();

			for (Iterator<String> itr = al.iterator(); itr.hasNext();) {
				String ns = itr.next();

				AttributedString as = new AttributedString(ns, fm.getFont().getAttributes());
				LineBreakMeasurer lbm = new LineBreakMeasurer(as.getIterator(), g2d.getFontRenderContext());

				while (lbm.getPosition() < ns.length()) {
					int next = lbm.nextOffset((float) maxSize);

					String ss = ns.substring(lbm.getPosition(), next);
					lbm.setPosition(next);

					nal.add(ss);
				}
			}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#dispose()
	 */
	public void dispose() {
		if (bi != null) {
			((BufferedImage) bi).flush();
			bi = null;
			g2d.dispose();
			g2d = null;
		}
	}
}
