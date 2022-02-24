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

package org.eclipse.birt.chart.device.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.TextRendererAdapter;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Provides convenience methods for rendering rotated text with configurable
 * attributes on a SWING graphics context.
 */
final class SwingTextRenderer extends TextRendererAdapter {

	/**
	 * The constructor.
	 */
	SwingTextRenderer(IDisplayServer sxs) {
		super(sxs);
	}

	/**
	 * This method renders the 'shadow' at an offset from the text 'rotated
	 * rectangle' subsequently rendered.
	 * 
	 * @param ipr
	 * @param iLabelPosition The position of the label w.r.t. the location specified
	 *                       by 'lo'
	 * @param lo             The location (specified as a 2d point) where the text
	 *                       is to be rendered
	 * @param la             The chart model structure containing the encapsulated
	 *                       text (and attributes) to be rendered
	 */
	public final void renderShadowAtLocation(IPrimitiveRenderer idr, int iLabelPosition, Location lo, Label la)
			throws ChartException {
		if (!ChartUtil.isShadowDefined(la)) {
			return;
		}

		final ColorDefinition cdShadow = la.getShadowColor();
		if (cdShadow == null) {
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"SwingTextMetrics.exception.undefined.shadow.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}

		final Graphics2D g2d = (Graphics2D) ((IDeviceRenderer) idr).getGraphicsContext();
		g2d.setFont((java.awt.Font) _sxs.createFont(la.getCaption().getFont()));
		computeTextAntialiasing(g2d, la.getCaption().getFont());

		switch (iLabelPosition & POSITION_MASK) {
		case ABOVE:
			showTopValue(idr, lo, la, iLabelPosition, true);
			break;

		case BELOW:
			showBottomValue(idr, lo, la, iLabelPosition, true);
			break;

		case LEFT:
			showLeftValue(idr, lo, la, iLabelPosition, true);
			break;

		case RIGHT:
			showRightValue(idr, lo, la, iLabelPosition, true);
			break;
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param iLabelPosition IConstants. LEFT, RIGHT, ABOVE or BELOW
	 * @param lo             POINT WHERE THE CORNER OF THE ROTATED RECTANGLE (OR
	 *                       EDGE CENTERED) IS RENDERED
	 * @param la
	 * @throws ChartException
	 */
	public final void renderTextAtLocation(IPrimitiveRenderer ipr, int iLabelPosition, Location lo, Label la)
			throws ChartException {
		final ColorDefinition cdText = la.getCaption().getColor();
		if (cdText == null) {
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"SwingTextMetrics.exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}

		final Graphics2D g2d = (Graphics2D) ((IDeviceRenderer) ipr).getGraphicsContext();
		g2d.setFont((java.awt.Font) _sxs.createFont(la.getCaption().getFont()));
		computeTextAntialiasing(g2d, la.getCaption().getFont());

		switch (iLabelPosition & POSITION_MASK) {
		case ABOVE:
			if (ChartUtil.isShadowDefined(la)) {
				showTopValue(ipr, lo, la, iLabelPosition, true);
			}
			showTopValue(ipr, lo, la, iLabelPosition, false);
			break;

		case BELOW:
			if (ChartUtil.isShadowDefined(la)) {
				showBottomValue(ipr, lo, la, iLabelPosition, true);
			}
			showBottomValue(ipr, lo, la, iLabelPosition, false);
			break;

		case LEFT:
			if (ChartUtil.isShadowDefined(la)) {
				showLeftValue(ipr, lo, la, iLabelPosition, true);
			}
			showLeftValue(ipr, lo, la, iLabelPosition, false);
			break;

		case RIGHT:
			if (ChartUtil.isShadowDefined(la)) {
				showRightValue(ipr, lo, la, iLabelPosition, true);
			}
			showRightValue(ipr, lo, la, iLabelPosition, false);
			break;

		case INSIDE:
			if (ChartUtil.isShadowDefined(la)) {
				showCenterValue(ipr, lo, la, true);
			}
			showCenterValue(ipr, lo, la, false);
			break;
		}

	}

	/**
	 * 
	 * @param idr
	 * @param boBlock
	 * @param taBlock
	 * @param la
	 */
	public final void renderTextInBlock(IDeviceRenderer idr, Bounds boBlock, TextAlignment taBlock, Label la)
			throws ChartException {
		Text t = la.getCaption();
		String sText = t.getValue();
		FontDefinition fd = t.getFont();
		ColorDefinition cdText = t.getColor();
		if (cdText == null) {
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"SwingTextMetrics.exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}
		IDisplayServer xs = idr.getDisplayServer();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		g2d.setFont((java.awt.Font) xs.createFont(fd));
		computeTextAntialiasing(g2d, la.getCaption().getFont());

		la.getCaption().setValue(sText);
		BoundingBox bb = idr.getChartComputation().computeBox(xs, ABOVE, la, 0, 0);

		if (taBlock == null) {
			taBlock = goFactory.createTextAlignment();
			taBlock.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
			taBlock.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		}
		HorizontalAlignment haBlock = taBlock.getHorizontalAlignment();
		VerticalAlignment vaBlock = taBlock.getVerticalAlignment();

		switch (haBlock.getValue()) {
		case HorizontalAlignment.CENTER:
			bb.setLeft(boBlock.getLeft() + (boBlock.getWidth() - bb.getWidth()) / 2);
			break;
		case HorizontalAlignment.LEFT:
			bb.setLeft(boBlock.getLeft());
			break;
		case HorizontalAlignment.RIGHT:
			bb.setLeft(boBlock.getLeft() + boBlock.getWidth() - bb.getWidth());
			break;
		}

		switch (vaBlock.getValue()) {
		case VerticalAlignment.TOP:
			bb.setTop(boBlock.getTop());
			break;
		case VerticalAlignment.CENTER:
			bb.setTop(boBlock.getTop() + (boBlock.getHeight() - bb.getHeight()) / 2);
			break;
		case VerticalAlignment.BOTTOM:
			bb.setTop(boBlock.getTop() + boBlock.getHeight() - bb.getHeight());
			break;
		}

		bb.setLeft(bb.getLeft() + bb.getHotPoint());
		if (ChartUtil.isShadowDefined(la)) {
			showTopValue(idr, goFactory.createLocation(bb.getLeft(), bb.getTop() + bb.getHeight()), la, 0, true);
		}
		showTopValue(idr, goFactory.createLocation(bb.getLeft(), bb.getTop() + bb.getHeight()), la, 0, false);
	}

	private final void showLeftValue(IPrimitiveRenderer ipr, Location lo, Label la, int iLabelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) ipr;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		FontDefinition fd = la.getCaption().getFont();
		double dAngleInDegrees = fd.getRotation();
		if (bShadow) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = la.getShadowColor() != null;
		}
		Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());
		Color clrBackground = null;
		if (la.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
		}
		final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
		final double dSineTheta = (Math.sin(dAngleInRadians));
		final double dCosTheta = (Math.cos(dAngleInRadians));

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		AffineTransform afTransform = g2d.getTransform();
		// Tune text position if needed. Location instance may be changed
		lo = adjustTextPosition(iLabelPosition, lo, itm, dAngleInDegrees);
		double dX = lo.getX(), dY = lo.getY();
		try {
			final double dFW = itm.getFullWidth();
			final double dH = itm.getHeight();
			final double dD = itm.getDescent();
			final double dFH = itm.getFullHeight();
			double dXOffset = 0, dW = 0;
			final int iLC = itm.getLineCount();
			final Insets ins = la.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			// Swing is not friendly to empty string, check and skip for this
			// case
			final boolean bEmptyText = "".equals(la.getCaption().getValue()); //$NON-NLS-1$
			TextLayout tl;

			final HorizontalAlignment ha = la.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = ha.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = ha.getValue() == HorizontalAlignment.CENTER;

			double dRotateX = (dX - dFW);
			double dRotateY = (dY + dH / 2);
			dX -= dFW;
			dY += dH / 2;

			if (dAngleInDegrees == 0) {
				double dYHalfOffset = (dFH + dH) / 2d;
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dYHalfOffset + ins.getTop() + dH * (i + 1) - dD));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(ipr, la.getOutline(), r2d);
				}
			}

			// DRAW POSITIVE ANGLE (> 0)
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dFW - dFW * dCosTheta;
				double dDeltaY = dFW * dSineTheta + dH / 2 - dH * dCosTheta / 2.0;
				dX += dDeltaX;
				dY -= dDeltaY;

				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							int index = iLC - i - 1;
							tl = ((SwingTextMetrics) itm).getLayout(index);
							if (bRightAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - (dH * i)) - ins.getBottom()));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY -
				// dDeltaY );
			}

			// DRAW NEGATIVE ANGLE (< 0)
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dDeltaX = dFW - dFW * dCosTheta - dH * dSineTheta;
				double dDeltaY = +dFW * dSineTheta - dH / 2 + dH * dCosTheta / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) + (dH * i)) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY -
				// dDeltaY );
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dDeltaX = dFW;
				double dDeltaY = (dFW - dH) / 2;
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFH, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}

							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - (dH * (iLC - i - 1))) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dDeltaX = dFW - dH;
				double dDeltaY = (dFW + dH) / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) + (dH * i)) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(itm);
		}
	}

	private final void showRightValue(IPrimitiveRenderer ipr, Location lo, Label la, int iLabelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) ipr;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		FontDefinition fd = la.getCaption().getFont();
		double dAngleInDegrees = fd.getRotation();
		if (bShadow) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = la.getShadowColor() != null;
		}
		Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());
		Color clrBackground = null;
		if (la.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
		}
		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		AffineTransform afTransform = g2d.getTransform();
		// Tune text position if needed. Location instance may be changed
		lo = adjustTextPosition(iLabelPosition, lo, itm, dAngleInDegrees);
		double dX = lo.getX(), dY = lo.getY();

		// dX += 2;
		dY += 1;

		try {
			final double dFW = itm.getFullWidth();
			final double dH = itm.getHeight();
			final double dD = itm.getDescent();
			final double dFH = itm.getFullHeight();
			double dXOffset = 0, dW = 0;
			final int iLC = itm.getLineCount();
			final Insets ins = la.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			// Swing is not friendly to empty string, check and skip for this
			// case
			final boolean bEmptyText = "".equals(la.getCaption().getValue()); //$NON-NLS-1$
			TextLayout tl;

			final HorizontalAlignment ha = la.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = ha.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = ha.getValue() == HorizontalAlignment.CENTER;

			double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			int iRotateX = (int) dX;
			int iRotateY = (int) (dY + dH / 2);
			dY += dH / 2;
			// double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
			double dCosTheta = Math.abs(Math.cos(dAngleInRadians));

			// HORIZONTAL TEXT
			if (dAngleInDegrees == 0) {
				double dYHalfOffset = (dFH + dH) / 2d;
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dYHalfOffset + ins.getTop() + dH * (i + 1) - dD)
							// (float)(((dY - dD) - ((iLC - i) * dH - (iLC + 1)
							// *
							// dH/2))
							// + ins.getTop())
							);
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
			}

			// DRAW POSITIVE ANGLE (> 0)
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dH * Math.sin(dAngleInRadians);
				double dDeltaY = dH / 2 - dH * dCosTheta / 2;
				dX -= dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, iRotateX - dDeltaX, iRotateY + dDeltaY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, (dY - dH), dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) ((dY - dD + dH * i) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, iRotateX - dDeltaX, iRotateY +
				// dDeltaY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW NEGATIVE ANGLE (< 0)
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dDeltaY = -dH / 2 + dH * dCosTheta / 2;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, iRotateX, iRotateY + dDeltaY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) ((dY - dD - dH * (iLC - i - 1)) - ins.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, iRotateX, iRotateY + dDeltaY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dDeltaX = dH;
				double dDeltaY = (dFW - dH) / 2;
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) ((dY - dD + dH * i) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dDeltaX = 0;
				double dDeltaY = (dFW + dH) / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) ((dY - dD + dH * i) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(itm);
		}
	}

	private final void showBottomValue(IPrimitiveRenderer ipr, Location lo, Label la, int iLabelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) ipr;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		FontDefinition fd = la.getCaption().getFont();
		// Color clrShadow = bShadow ? (Color)
		// _sxs.getColor(la.getShadowColor()) : null;
		double dAngleInDegrees = fd.getRotation();
		Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());
		Color clrBackground = null;
		if (la.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
		}
		double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		AffineTransform afTransform = g2d.getTransform();
		// Tune text position if needed. Location instance may be changed
		lo = adjustTextPosition(iLabelPosition, lo, itm, dAngleInDegrees);
		double dX = lo.getX(), dY = lo.getY();
		try {
			final double dFW = itm.getFullWidth();
			final double dH = itm.getHeight();
			final double dD = itm.getDescent();
			final double dFH = itm.getFullHeight();
			double dXOffset = 0, dW = 0;
			final int iLC = itm.getLineCount();
			final Insets ins = la.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			// Swing is not friendly to empty string, check and skip for this
			// case
			final boolean bEmptyText = "".equals(la.getCaption().getValue()); //$NON-NLS-1$
			TextLayout tl;

			final HorizontalAlignment ha = la.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = ha.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = ha.getValue() == HorizontalAlignment.CENTER;

			dX -= dFW / 2;
			dY += dH;

			double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
			double dCosTheta = Math.abs(Math.cos(dAngleInRadians));

			// HORIZONTAL TEXT
			if (dAngleInDegrees == 0) {
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dD + dH * i + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW IT AT A POSITIVE ANGLE
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {

				double dDeltaX = dFW * dCosTheta - dH * dSineTheta / 2.0 - dFW / 2.0;
				double dDeltaY = dH * dCosTheta + dFW * dSineTheta - dH;

				dX -= dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dD + dH * i + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}

			// DRAW IT AT A NEGATIVE ANGLE
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				dX += dFW / 2 + dH * dSineTheta / 2.0;
				g2d.rotate(dAngleInRadians, dX, dY - dH);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dD + dH * i + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY - dH );
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dYHalfOffset = (dFH + dH) / 2.0;
				double dDeltaX = (dFW + dH) / 2;
				double dDeltaY = (dFW - dH);
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - ((iLC - i) * dH - (iLC + 1) * dH / 2)) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				dX += dFW / 2;
				dY -= dH;

				double dYHalfOffset = (dFH + dH) / 2d;
				double dDeltaX = dYHalfOffset - dFH / 2d;
				dX -= dDeltaX;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) ((dY - dD) - dYHalfOffset + dH * (i + 1) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(itm);
		}
	}

	private final void showTopValue(IPrimitiveRenderer ipr, Location lo, Label la, int iLabelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) ipr;
		IChartComputation cComp = idr.getChartComputation();
		final Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		final FontDefinition fd = la.getCaption().getFont();
		// final Color clrShadow = bShadow ? (Color)
		// _sxs.getColor(la.getShadowColor()) : null;
		final double dAngleInDegrees = fd.getRotation();
		final Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());
		Color clrBackground = null;
		if (la.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
		}
		double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		AffineTransform afTransform = g2d.getTransform();
		// Tune text position if needed. Location instance may be changed
		lo = adjustTextPosition(iLabelPosition, lo, itm, dAngleInDegrees);
		double dX = lo.getX(), dY = lo.getY();
		try {
			final double dFW = itm.getFullWidth();
			final double dH = itm.getHeight();
			final double dD = itm.getDescent();
			final double dFH = itm.getFullHeight();
			double dXOffset = 0, dW = 0;
			final int iLC = itm.getLineCount();
			final Insets ins = goFactory.scaleInsets(la.getInsets(), _sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			// Swing is not friendly to empty string, check and skip for this
			// case
			final boolean bEmptyText = "".equals(la.getCaption().getValue()); //$NON-NLS-1$
			TextLayout tl;

			final HorizontalAlignment ha = la.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = ha.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = ha.getValue() == HorizontalAlignment.CENTER;
			double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
			double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
			dX -= dFW / 2;

			// HORIZONTAL TEXT
			if (dAngleInDegrees == 0) {
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, (dY - dFH), dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							// tl = new TextLayout(itm.getLine(iLC - i - 1),
							// g2d.getFont(), g2d.getFontRenderContext());
							int index = iLC - i - 1;
							tl = ((SwingTextMetrics) itm).getLayout(index);

							if (bRightAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dD - dH * i - ins.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW IT AT A POSITIVE ANGLE
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dFW / 2 + dH * dSineTheta / 2.0;
				;

				dX += dDeltaX;
				g2d.rotate(dAngleInRadians, dX, dY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, (dY - dFH), dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							tl = new TextLayout(itm.getLine(iLC - i - 1), g2d.getFont().getAttributes(),
									g2d.getFontRenderContext());
							if (bRightAligned) {
								dW = tl.getBounds().getWidth();
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = tl.getBounds().getWidth();
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dD - dH * i - ins.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW IT AT A NEGATIVE ANGLE
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {

				dX += -dFW * dCosTheta - dH * dSineTheta / 2.0 + dFW / 2.0;
				dY -= dFW * dSineTheta;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							int index = iLC - i - 1;
							tl = ((SwingTextMetrics) itm).getLayout(index);
							if (bRightAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dD - dH * i - ins.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				/*
				 * final RotatedRectangle rr = computePolygon(IConstants.ABOVE, la, lo.getX(),
				 * lo.getY()); g2d.setColor(Color.blue); g2d.draw(rr); final BoundingBox bb =
				 * computeBox(IConstants.ABOVE, la, lo.getX(), lo.getY()); renderBox(g2d, bb,
				 * Color.black, null);
				 */
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dYHalfOffset = (dFH + dH) / 2.0;
				double dDeltaX = (dFW + dH) / 2;
				dX += dDeltaX;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - ((itm.getLineCount() - i) * dH - (iLC + 1) * dH / 2))
											+ ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dYHalfOffset = (dFH + dH) / 2.0;
				double dDeltaX = (dFW - dH) / 2;
				double dDeltaY = dFW;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - ((itm.getLineCount() - i) * dH - (iLC + 1) * dH / 2))
											+ ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(itm);
		}
	}

	/**
	 * 
	 * @param g2d
	 * @param f
	 * @param dX
	 * @param dY
	 * @param sText
	 * @param iAngleInDegrees
	 */
	private final void showCenterValue(IPrimitiveRenderer ipr, Location lo, Label la, boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) ipr;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		double dX = lo.getX(), dY = lo.getY();
		FontDefinition fd = la.getCaption().getFont();
		double dAngleInDegrees = fd.getRotation();
		if (bShadow) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = la.getShadowColor() != null;
		}
		Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());
		Color clrBackground = null;
		if (la.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
		}
		final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
		final double dSineTheta = (Math.sin(dAngleInRadians));
		final double dCosTheta = (Math.cos(dAngleInRadians));

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		AffineTransform afTransform = g2d.getTransform();

		try {
			final double dFW = itm.getFullWidth();
			final double dH = itm.getHeight();
			final double dD = itm.getDescent();
			final double dFH = itm.getFullHeight();
			double dXOffset = 0, dW = 0;
			final int iLC = itm.getLineCount();
			final Insets ins = la.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			// Swing is not friendly to empty string, check and skip for this
			// case
			final boolean bEmptyText = "".equals(la.getCaption().getValue()); //$NON-NLS-1$
			TextLayout tl;

			final HorizontalAlignment ha = la.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = ha.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = ha.getValue() == HorizontalAlignment.CENTER;

			double dRotateX = dX;
			double dRotateY = dY;
			dX -= dFW / 2;
			dY += dH / 2;

			if (dAngleInDegrees == 0) {
				double dYHalfOffset = (dFH + dH) / 2d;
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(la.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFW, (dY - dYHalfOffset) + shadowness + dFH),
									(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (dY - dYHalfOffset + ins.getTop() + dH * (i + 1) - dD));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(ipr, la.getOutline(), r2d);
				}
			}

			// DRAW POSITIVE ANGLE (> 0)
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dFW - dFW * dCosTheta;
				double dDeltaY = dFW * dSineTheta + dH / 2;
				dX += dDeltaX;
				dY -= dDeltaY;

				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFH, dFW, dFH);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < itm.getLineCount(); i++) {
							int index = iLC - i - 1;
							tl = ((SwingTextMetrics) itm).getLayout(index);
							if (bRightAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(index);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - (dH * i)) - ins.getBottom()));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY -
				// dDeltaY );
			}

			// DRAW NEGATIVE ANGLE (< 0)
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dDeltaX = dFW - dFW * dCosTheta - dH * dSineTheta;
				double dDeltaY = dFW * dSineTheta + dH / 2 - dH * dCosTheta;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) + (dH * i)) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY -
				// dDeltaY );
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dDeltaX = dFW;
				double dDeltaY = (dFW - dH) / 2;
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFH, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}

							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) - (dH * (iLC - i - 1))) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dDeltaX = dFW - dH;
				double dDeltaY = (dFW + dH) / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFH) + shadowness),
							(Color) _sxs.getColor(la.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFW, (dY - dFH) + shadowness + dFH),
							(Color) _sxs.getColor(la.getShadowColor().translucent())));
					g2d.fill(new Rectangle2D.Double(dX + shadowness, (dY - dFH) + shadowness, dFW, dFH));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dH, dFW, dFH);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < iLC; i++) {
							tl = ((SwingTextMetrics) itm).getLayout(i);
							if (bRightAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
							} else if (bCenterAligned) {
								dW = itm.getWidth(i);
								dXOffset = -ins.getLeft() + (dFW - dW) / 2;
							}
							tl.draw(g2d, (float) (dX + dXOffset + ins.getLeft()),
									(float) (((dY - dD) + (dH * i)) + ins.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(ipr, la.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(itm);
		}
	}

	// private static final void renderBox(Graphics2D g2d, BoundingBox bb, Color
	// cFG, Color cBG)
	// {
	// if (cBG != null)
	// {
	// g2d.setColor(cBG);
	// g2d.fillRect((int) bb.getLeft(), (int) bb.getTop(), (int) bb.getWidth(),
	// (int) bb.getHeight());
	// }
	// g2d.setColor(cFG);
	// g2d.drawRect((int) bb.getLeft(), (int) bb.getTop(), (int) bb.getWidth(),
	// (int) bb.getHeight());
	// }

	private final void renderOutline(IPrimitiveRenderer ipr, LineAttributes lia, Rectangle2D.Double r2d) {
		if (lia != null && lia.isVisible() && lia.getColor() != null) {
			Graphics2D g2d = (Graphics2D) ((IDeviceRenderer) ipr).getGraphicsContext();
			Stroke sPrevious = null;
			final ColorDefinition cd = lia.getColor();
			final Stroke sCurrent = ((SwingRendererImpl) ipr).getCachedStroke(lia);
			if (sCurrent != null) // SOME STROKE DEFINED?
			{
				sPrevious = g2d.getStroke();
				g2d.setStroke(sCurrent);
			}
			g2d.setColor((Color) _sxs.getColor(cd));
			g2d.draw(r2d);
			if (sPrevious != null) // RESTORE PREVIOUS STROKE
			{
				g2d.setStroke(sPrevious);
			}
		}
	}

	private void computeTextAntialiasing(Graphics2D g2d, FontDefinition font) {
		if (font.isBold() || (font.getRotation() % 90 != 0) || font.getSize() > 13) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}

	}

}
