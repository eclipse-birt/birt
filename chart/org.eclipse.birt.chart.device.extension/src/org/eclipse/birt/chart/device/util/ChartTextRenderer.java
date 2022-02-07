/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.device.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
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
import org.eclipse.birt.chart.device.g2d.G2dRendererBase;
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

public class ChartTextRenderer extends TextRendererAdapter {
	protected ITextLayoutFactory textLayoutFactory;

	/**
	 * 
	 */
	public ChartTextRenderer(IDisplayServer dispServer) {
		super(dispServer);
	}

	public void setTextLayoutFactory(ITextLayoutFactory textLayoutFactory) {
		this.textLayoutFactory = textLayoutFactory;
	}

	/**
	 * This method renders the 'shadow' at an offset from the text 'rotated
	 * rectangle' subsequently rendered.
	 * 
	 * @param renderer
	 * @param labelPosition The position of the label w.r.t. the location specified
	 *                      by 'location'
	 * @param location      The location (specified as a 2d point) where the text is
	 *                      to be rendered
	 * @param label         The chart model structure containing the encapsulated
	 *                      text (and attributes) to be rendered
	 */
	public final void renderShadowAtLocation(IPrimitiveRenderer renderer, int labelPosition, Location location,
			Label label) throws ChartException {
		final ColorDefinition cdShadow = label.getShadowColor();
		if (cdShadow == null) {
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"exception.undefined.shadow.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}

		final Graphics2D g2d = (Graphics2D) ((IDeviceRenderer) renderer).getGraphicsContext();
		g2d.setFont((java.awt.Font) _sxs.createFont(label.getCaption().getFont()));

		switch (labelPosition & POSITION_MASK) {
		case ABOVE:
			showTopValue(renderer, location, label, labelPosition, true);
			break;

		case BELOW:
			showBottomValue(renderer, location, label, labelPosition, true);
			break;

		case LEFT:
			showLeftValue(renderer, location, label, labelPosition, true);
			break;

		case RIGHT:
			showRightValue(renderer, location, label, labelPosition, true);
			break;
		}
	}

	/**
	 * 
	 * @param renderer
	 * @param labelPosition IConstants. LEFT, RIGHT, ABOVE or BELOW
	 * @param location      POINT WHERE THE CORNER OF THE ROTATED RECTANGLE (OR EDGE
	 *                      CENTERED) IS RENDERED
	 * @param label
	 * @throws ChartException
	 */
	public final void renderTextAtLocation(IPrimitiveRenderer renderer, int labelPosition, Location location,
			Label label) throws ChartException {
		final ColorDefinition colorDef = label.getCaption().getColor();
		if (colorDef == null) {
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}

		final Graphics2D g2d = (Graphics2D) ((IDeviceRenderer) renderer).getGraphicsContext();
		g2d.setFont((java.awt.Font) _sxs.createFont(label.getCaption().getFont()));

		switch (labelPosition & POSITION_MASK) {
		case ABOVE:
			if (ChartUtil.isShadowDefined(label)) {
				showTopValue(renderer, location, label, labelPosition, true);
			}
			showTopValue(renderer, location, label, labelPosition, false);
			break;

		case BELOW:
			if (ChartUtil.isShadowDefined(label)) {
				showBottomValue(renderer, location, label, labelPosition, true);
			}
			showBottomValue(renderer, location, label, labelPosition, false);
			break;

		case LEFT:
			if (ChartUtil.isShadowDefined(label)) {
				showLeftValue(renderer, location, label, labelPosition, true);
			}
			showLeftValue(renderer, location, label, labelPosition, false);
			break;

		case RIGHT:
			if (ChartUtil.isShadowDefined(label)) {
				showRightValue(renderer, location, label, labelPosition, true);
			}
			showRightValue(renderer, location, label, labelPosition, false);
			break;
		case INSIDE:
			if (ChartUtil.isShadowDefined(label)) {
				showCenterValue(renderer, location, label, true);
			}
			showCenterValue(renderer, location, label, false);
			break;

		}

	}

	/**
	 * 
	 * @param renderer
	 * @param boBlock
	 * @param taBlock
	 * @param label
	 */
	public final void renderTextInBlock(IDeviceRenderer renderer, Bounds boBlock, TextAlignment taBlock, Label label)
			throws ChartException {
		Text t = label.getCaption();
		String labelText = t.getValue();
		FontDefinition fontDef = t.getFont();
		ColorDefinition cdText = t.getColor();
		if (cdText == null) {
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}
		IDisplayServer dispServer = renderer.getDisplayServer();
		Graphics2D g2d = (Graphics2D) renderer.getGraphicsContext();
		g2d.setFont((java.awt.Font) dispServer.createFont(fontDef));

		label.getCaption().setValue(labelText);
		BoundingBox boundBox = renderer.getChartComputation().computeBox(dispServer, ABOVE, label, 0, 0);

		if (taBlock == null) {
			taBlock = goFactory.createTextAlignment();
			taBlock.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
			taBlock.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		}
		HorizontalAlignment haBlock = taBlock.getHorizontalAlignment();
		VerticalAlignment vaBlock = taBlock.getVerticalAlignment();

		switch (haBlock.getValue()) {
		case HorizontalAlignment.CENTER:
			boundBox.setLeft(boBlock.getLeft() + (boBlock.getWidth() - boundBox.getWidth()) / 2);
			break;
		case HorizontalAlignment.LEFT:
			boundBox.setLeft(boBlock.getLeft());
			break;
		case HorizontalAlignment.RIGHT:
			boundBox.setLeft(boBlock.getLeft() + boBlock.getWidth() - boundBox.getWidth());
			break;
		}

		switch (vaBlock.getValue()) {
		case VerticalAlignment.TOP:
			boundBox.setTop(boBlock.getTop());
			break;
		case VerticalAlignment.CENTER:
			boundBox.setTop(boBlock.getTop() + (boBlock.getHeight() - boundBox.getHeight()) / 2);
			break;
		case VerticalAlignment.BOTTOM:
			boundBox.setTop(boBlock.getTop() + boBlock.getHeight() - boundBox.getHeight());
			break;
		}

		boundBox.setLeft(boundBox.getLeft() + boundBox.getHotPoint());
		if (ChartUtil.isShadowDefined(label)) {
			showTopValue(renderer,
					goFactory.createLocation(boundBox.getLeft(), boundBox.getTop() + boundBox.getHeight()), label, 0,
					true);
		}
		showTopValue(renderer, goFactory.createLocation(boundBox.getLeft(), boundBox.getTop() + boundBox.getHeight()),
				label, 0, false);
	}

	private final void showLeftValue(IPrimitiveRenderer renderer, Location location, Label label, int labelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) renderer;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		FontDefinition fontDef = label.getCaption().getFont();
		double dAngleInDegrees = fontDef.getRotation();
		if (bShadow) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = label.getShadowColor() != null;
		}
		Color clrText = (Color) _sxs.getColor(label.getCaption().getColor());
		Color clrBackground = null;
		if (label.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) label.getBackground());
		}

		final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
		final double dSineTheta = (Math.sin(dAngleInRadians));
		final double dCosTheta = (Math.cos(dAngleInRadians));
		final ITextMetrics textMetrics = cComp.getTextMetrics(_sxs, label, 0);
		AffineTransform afTransform = new AffineTransform(g2d.getTransform());

		// Tune text position if needed. Location instance may be changed
		location = adjustTextPosition(labelPosition, location, textMetrics, dAngleInDegrees);
		double dX = location.getX(), dY = location.getY();

		try {
			final double dFullWidth = textMetrics.getFullWidth();
			final double dHeight = textMetrics.getHeight();
			final double dDescent = textMetrics.getDescent();
			final double dFullHeight = textMetrics.getFullHeight();
			double dXOffset = 0, dWidth = 0;
			final int lineCount = textMetrics.getLineCount();
			final Insets insets = label.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			final boolean bEmptyText = "".equals(label.getCaption().getValue()); //$NON-NLS-1$
			ChartTextLayout textLayout;
			double dYDiff;

			final HorizontalAlignment hAlign = label.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = hAlign.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = hAlign.getValue() == HorizontalAlignment.CENTER;

			double dRotateX = (dX - dFullWidth);
			double dRotateY = (dY + dHeight / 2);
			dX -= dFullWidth;
			dY += dHeight / 2;

			if (dAngleInDegrees == 0) {
				double dYHalfOffset = (dFullHeight + dHeight) / 2d;
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));

					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dYHalfOffset + insets.getTop() + dHeight * (i + 1) - dDescent));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(renderer, label.getOutline(), r2d);
				}
			}

			// DRAW POSITIVE ANGLE (> 0)
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dFullWidth - dFullWidth * dCosTheta;
				double dDeltaY = dFullWidth * dSineTheta + dHeight / 2;
				dX += dDeltaX;
				dY -= dDeltaY;

				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				dYDiff = dY - dFullHeight;

				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							int index = lineCount - i - 1;
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(index);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) - (dHeight * i)) - insets.getBottom()));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY
				// - dDeltaY );
			}

			// DRAW NEGATIVE ANGLE (< 0)
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dDeltaX = dFullWidth - dFullWidth * dCosTheta - dHeight * dSineTheta;
				double dDeltaY = dFullWidth * dSineTheta + dHeight / 2 - dHeight * dCosTheta;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				dYDiff = dY - dFullHeight;

				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) + (dHeight * i)) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY
				// - dDeltaY );
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dDeltaX = dFullWidth;
				double dDeltaY = (dFullWidth - dHeight) / 2;
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				dYDiff = dY - dFullHeight;

				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}

							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) - (dHeight * (lineCount - i - 1))) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dDeltaX = dFullWidth - dHeight;
				double dDeltaY = (dFullWidth + dHeight) / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				dYDiff = dY - dFullHeight;

				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) + (dHeight * i)) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(textMetrics);
		}
	}

	private final void showRightValue(IPrimitiveRenderer renderer, Location location, Label label, int labelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) renderer;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		FontDefinition fontDef = label.getCaption().getFont();
		double dAngleInDegrees = fontDef.getRotation();
		if (bShadow) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = label.getShadowColor() != null;
		}
		Color clrText = (Color) _sxs.getColor(label.getCaption().getColor());
		Color clrBackground = null;
		if (label.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) label.getBackground());
		}

		final ITextMetrics textMetrics = cComp.getTextMetrics(_sxs, label, 0);
		AffineTransform afTransform = new AffineTransform(g2d.getTransform());

		// Tune text position if needed. Location instance may be changed
		location = adjustTextPosition(labelPosition, location, textMetrics, dAngleInDegrees);
		double dX = location.getX(), dY = location.getY();

		// dX += 2;
		dY += 1;

		try {
			final double dFullWidth = textMetrics.getFullWidth();
			final double dHeight = textMetrics.getHeight();
			final double dDescent = textMetrics.getDescent();
			final double dFullHeight = textMetrics.getFullHeight();
			double dXOffset = 0, dWidth = 0;
			final int lineCount = textMetrics.getLineCount();
			final Insets insets = label.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			final boolean bEmptyText = "".equals(label.getCaption().getValue()); //$NON-NLS-1$
			ChartTextLayout textLayout;
			double dYDiff;

			final HorizontalAlignment hAlign = label.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = hAlign.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = hAlign.getValue() == HorizontalAlignment.CENTER;

			double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			int iRotateX = (int) dX;
			int iRotateY = (int) (dY + dHeight / 2);
			dY += dHeight / 2;

			// HORIZONTAL TEXT
			if (dAngleInDegrees == 0) {
				double dYHalfOffset = (dFullHeight + dHeight) / 2d;
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dYHalfOffset + insets.getTop() + dHeight * (i + 1) - dDescent)
							// (float)(((dY - dDescent) - ((lineCount - i) *
							// dHeight - (lineCount + 1)
							// *
							// dHeight/2))
							// + insets.getTop())
							);
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
			}

			// DRAW POSITIVE ANGLE (> 0)
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dHeight * Math.sin(dAngleInRadians);
				double dDeltaY = dHeight * Math.cos(dAngleInRadians) - dHeight / 2;
				dX -= dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, iRotateX - dDeltaX, iRotateY + dDeltaY);
				dYDiff = dY - dFullHeight;

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) ((dY - dDescent + dHeight * i) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, iRotateX - dDeltaX, iRotateY
				// + dDeltaY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW NEGATIVE ANGLE (< 0)
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dDeltaY = -dHeight / 2;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, iRotateX, iRotateY + dDeltaY);
				dYDiff = dY - dFullHeight;

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFullHeight, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) ((dY - dDescent - dHeight * (lineCount - i - 1)) - insets.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, iRotateX, iRotateY + dDeltaY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dDeltaX = dHeight;
				double dDeltaY = (dFullWidth - dHeight) / 2;
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				dYDiff = dY - dFullHeight;

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) ((dY - dDescent + dHeight * i) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dDeltaX = 0;
				double dDeltaY = (dFullWidth + dHeight) / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				dYDiff = dY - dFullHeight;

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, dYDiff + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, dY + shadowness),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d,
							new Rectangle2D.Double(dX + shadowness, dYDiff + shadowness, dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dYDiff, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) ((dY - dDescent + dHeight * i) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(textMetrics);
		}
	}

	private final void showBottomValue(IPrimitiveRenderer renderer, Location location, Label label, int labelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) renderer;
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		IChartComputation cComp = idr.getChartComputation();
		FontDefinition fontDef = label.getCaption().getFont();
		// Color clrShadow = bShadow ? (Color)
		// _sxs.getColor(label.getShadowColor()) : null;
		double dAngleInDegrees = fontDef.getRotation();
		Color clrText = (Color) _sxs.getColor(label.getCaption().getColor());
		Color clrBackground = null;
		if (label.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) label.getBackground());
		}

		double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
		final ITextMetrics textMetrics = cComp.getTextMetrics(_sxs, label, 0);
		AffineTransform afTransform = new AffineTransform(g2d.getTransform());

		// Tune text position if needed. Location instance may be changed
		location = adjustTextPosition(labelPosition, location, textMetrics, dAngleInDegrees);
		double dX = location.getX(), dY = location.getY();
		try {
			final double dFullWidth = textMetrics.getFullWidth();
			final double dHeight = textMetrics.getHeight();
			final double dDescent = textMetrics.getDescent();
			final double dFullHeight = textMetrics.getFullHeight();
			double dXOffset = 0, dWidth = 0;
			final int lineCount = textMetrics.getLineCount();
			final Insets insets = label.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			final boolean bEmptyText = "".equals(label.getCaption().getValue()); //$NON-NLS-1$
			ChartTextLayout textLayout;

			final HorizontalAlignment hAlignment = label.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = hAlignment.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = hAlignment.getValue() == HorizontalAlignment.CENTER;

			dX -= dFullWidth / 2;
			dY += dHeight;

			// HORIZONTAL TEXT
			if (dAngleInDegrees == 0) {
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, (dY - dHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dHeight, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dDescent + dHeight * i + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW IT AT A POSITIVE ANGLE
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
				double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
				double dDeltaX = dFullWidth * dCosTheta - dHeight * dSineTheta - dFullWidth / 2.0;
				double dDeltaY = dHeight * dCosTheta + dFullWidth * dSineTheta - dHeight;

				dX -= dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, (dY - dHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dHeight, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dDescent + dHeight * i + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}

			// DRAW IT AT A NEGATIVE ANGLE
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				dX += dFullWidth / 2;
				g2d.rotate(dAngleInRadians, dX, dY - dHeight);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, (dY - dHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dHeight, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dDescent + dHeight * i + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY - dHeight );
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dYHalfOffset = (dFullHeight + dHeight) / 2.0;
				double dDeltaX = (dFullWidth + dHeight) / 2;
				double dDeltaY = (dFullWidth - dHeight);
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent)
											- ((lineCount - i) * dHeight - (lineCount + 1) * dHeight / 2))
											+ insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				dX += dFullWidth / 2;
				dY -= dHeight;

				double dYHalfOffset = (dFullHeight + dHeight) / 2d;
				double dDeltaX = dYHalfOffset - dFullHeight / 2d;
				dX -= dDeltaX;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) ((dY - dDescent) - dYHalfOffset + dHeight * (i + 1) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(textMetrics);
		}
	}

	protected void fillShadow(Graphics2D g2d, Shape shape) {
		g2d.fill(shape);
	}

	private final void showTopValue(IPrimitiveRenderer renderer, Location location, Label label, int labelPosition,
			boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) renderer;
		IChartComputation cComp = idr.getChartComputation();
		final Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		final FontDefinition fontDef = label.getCaption().getFont();
		// final Color clrShadow = bShadow ? (Color)
		// _sxs.getColor(la.getShadowColor()) : null;
		final double dAngleInDegrees = fontDef.getRotation();
		final Color clrText = (Color) _sxs.getColor(label.getCaption().getColor());
		Color clrBackground = null;
		if (label.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) label.getBackground());
		}
		double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);

		// final ITextMetrics textMetrics = new ChartTextMetrics( _sxs, label );
		final ITextMetrics textMetrics = cComp.getTextMetrics(_sxs, label, 0);
		AffineTransform afTransform = new AffineTransform(g2d.getTransform());

		// Tune text position if needed. Location instance may be changed
		location = adjustTextPosition(labelPosition, location, textMetrics, dAngleInDegrees);
		double dX = location.getX(), dY = location.getY();
		try {
			final double dFullWidth = textMetrics.getFullWidth();
			final double dHeight = textMetrics.getHeight();
			final double dDescent = textMetrics.getDescent();
			final double dFullHeight = textMetrics.getFullHeight();
			double dXOffset = 0, dWidth = 0;
			final int lineCount = textMetrics.getLineCount();
			final Insets insets = label.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			final boolean bEmptyText = "".equals(label.getCaption().getValue()); //$NON-NLS-1$
			ChartTextLayout textLayout;

			final HorizontalAlignment hAlignment = label.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = hAlignment.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = hAlignment.getValue() == HorizontalAlignment.CENTER;

			dX -= dFullWidth / 2;

			// HORIZONTAL TEXT
			if (dAngleInDegrees == 0) {
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth,
									(dY - dFullHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, (dY - dFullHeight), dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							// textLayout = new
							// TextLayout(textMetrics.getLine(lineCount - i -
							// 1),
							// g2d.getFont(), g2d.getFontRenderContext());
							int index = lineCount - i - 1;
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(index);

							if (bRightAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dDescent - dHeight * i - insets.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW IT AT A POSITIVE ANGLE
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dFullWidth / 2;

				dX += dDeltaX;
				g2d.rotate(dAngleInRadians, dX, dY);

				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth,
									(dY - dFullHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, (dY - dFullHeight), dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							textLayout = textLayoutFactory.createTextLayout(textMetrics.getLine(lineCount - i - 1),
									g2d.getFont().getAttributes(), g2d.getFontRenderContext());
							if (bRightAligned) {
								dWidth = textLayout.getBounds().getWidth();
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textLayout.getBounds().getWidth();
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dDescent - dHeight * i - insets.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// DRAW IT AT A NEGATIVE ANGLE
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
				double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
				dX -= dFullWidth / 2 - (dFullWidth - dFullWidth * dCosTheta);
				dY -= dFullWidth * dSineTheta;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth,
									(dY - dFullHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFullHeight, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							int index = lineCount - i - 1;
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(index);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dDescent - dHeight * i - insets.getBottom()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				/*
				 * final RotatedRectangle rr = computePolygon(IConstants.ABOVE, label,
				 * location.getX(), location.getY()); g2d.setColor(Color.blue); g2d.draw(rr);
				 * final BoundingBox bb = computeBox(IConstants.ABOVE, label, location.getX(),
				 * location.getY()); renderBox(g2d, bb, Color.black, null);
				 */
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dYHalfOffset = (dFullHeight + dHeight) / 2.0;
				double dDeltaX = (dFullWidth + dHeight) / 2;
				dX += dDeltaX;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()), (float) (((dY - dDescent)
									- ((textMetrics.getLineCount() - i) * dHeight - (lineCount + 1) * dHeight / 2))
									+ insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dYHalfOffset = (dFullHeight + dHeight) / 2.0;
				double dDeltaX = (dFullWidth - dHeight) / 2;
				double dDeltaY = dFullWidth;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()), (float) (((dY - dDescent)
									- ((textMetrics.getLineCount() - i) * dHeight - (lineCount + 1) * dHeight / 2))
									+ insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}

				// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
				// g2d.rotate( -dAngleInRadians, dX, dY );
				// crossHairs(g2d, (int)dX, (int)dY);
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(textMetrics);
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
	private final void showCenterValue(IPrimitiveRenderer renderer, Location location, Label label, boolean bShadow) {
		IDeviceRenderer idr = (IDeviceRenderer) renderer;
		IChartComputation cComp = idr.getChartComputation();
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext();
		double dX = location.getX(), dY = location.getY();
		FontDefinition fontDef = label.getCaption().getFont();
		double dAngleInDegrees = fontDef.getRotation();
		if (bShadow) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = label.getShadowColor() != null;
		}
		Color clrText = (Color) _sxs.getColor(label.getCaption().getColor());
		Color clrBackground = null;
		if (label.getBackground() != null) {
			clrBackground = (Color) _sxs.getColor((ColorDefinition) label.getBackground());
		}

		final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
		final double dSineTheta = (Math.sin(dAngleInRadians));
		final double dCosTheta = (Math.cos(dAngleInRadians));
		final ITextMetrics textMetrics = cComp.getTextMetrics(_sxs, label, 0);
		AffineTransform afTransform = new AffineTransform(g2d.getTransform());

		try {
			final double dFullWidth = textMetrics.getFullWidth();
			final double dHeight = textMetrics.getHeight();
			final double dDescent = textMetrics.getDescent();
			final double dFullHeight = textMetrics.getFullHeight();
			double dXOffset = 0, dWidth = 0;
			final int lineCount = textMetrics.getLineCount();
			final Insets insets = label.getInsets().scaledInstance(_sxs.getDpiResolution() / 72d);
			final double shadowness = 3 * _sxs.getDpiResolution() / 72d;
			// Swing is not friendly to empty string, check and skip for this
			// case
			final boolean bEmptyText = "".equals(label.getCaption().getValue()); //$NON-NLS-1$
			ChartTextLayout textLayout;

			final HorizontalAlignment hAlign = label.getCaption().getFont().getAlignment().getHorizontalAlignment();
			final boolean bRightAligned = hAlign.getValue() == HorizontalAlignment.RIGHT;
			final boolean bCenterAligned = hAlign.getValue() == HorizontalAlignment.CENTER;

			double dRotateX = dX;
			double dRotateY = dY;
			dX -= dFullWidth / 2;
			dY += dHeight / 2;

			if (dAngleInDegrees == 0) {
				double dYHalfOffset = (dFullHeight + dHeight) / 2d;
				if (bShadow) // RENDER THE SHADOW
				{
					g2d.setPaint(
							new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness),
									(Color) _sxs.getColor(label.getShadowColor()),
									new Point2D.Double(dX + shadowness + dFullWidth,
											(dY - dYHalfOffset) + shadowness + dFullHeight),
									(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dYHalfOffset) + shadowness,
							dFullWidth, dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dYHalfOffset, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (dY - dYHalfOffset + insets.getTop() + dHeight * (i + 1) - dDescent));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(renderer, label.getOutline(), r2d);
				}
			}

			// DRAW POSITIVE ANGLE (> 0)
			else if (dAngleInDegrees > 0 && dAngleInDegrees < 90) {
				double dDeltaX = dFullWidth - dFullWidth * dCosTheta;
				double dDeltaY = dFullWidth * dSineTheta + dHeight / 2;
				dX += dDeltaX;
				dY -= dDeltaY;

				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth,
									(dY - dFullHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFullHeight, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < textMetrics.getLineCount(); i++) {
							int index = lineCount - i - 1;
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(index);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(index);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) - (dHeight * i)) - insets.getBottom()));
						}
					}

					// RENDER THE OUTLINE
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY
				// - dDeltaY );
			}

			// DRAW NEGATIVE ANGLE (< 0)
			else if (dAngleInDegrees < 0 && dAngleInDegrees > -90) {
				double dDeltaX = dFullWidth - dFullWidth * dCosTheta - dHeight * dSineTheta;
				double dDeltaY = dFullWidth * dSineTheta + dHeight / 2 - dHeight * dCosTheta;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY);
				if (bShadow) {
					// RENDER THE SHADOW
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth, (dY - dHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dHeight, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) + (dHeight * i)) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY
				// - dDeltaY );
			}

			// VERTICALLY UP
			else if (dAngleInDegrees == 90) {
				double dDeltaX = dFullWidth;
				double dDeltaY = (dFullWidth - dHeight) / 2;
				dX += dDeltaX;
				dY += dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth,
									(dY - dFullHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dFullHeight, dFullWidth,
							dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}

							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) - (dHeight * (lineCount - i - 1))) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}

			// VERTICALLY DOWN
			else if (dAngleInDegrees == -90) {
				double dDeltaX = dFullWidth - dHeight;
				double dDeltaY = (dFullWidth + dHeight) / 2;
				dX += dDeltaX;
				dY -= dDeltaY;
				g2d.rotate(dAngleInRadians, dX, dY);
				if (bShadow) {
					g2d.setPaint(new GradientPaint(new Point2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness),
							(Color) _sxs.getColor(label.getShadowColor()),
							new Point2D.Double(dX + shadowness + dFullWidth,
									(dY - dFullHeight) + shadowness + dFullHeight),
							(Color) _sxs.getColor(label.getShadowColor().translucent())));
					fillShadow(g2d, new Rectangle2D.Double(dX + shadowness, (dY - dFullHeight) + shadowness, dFullWidth,
							dFullHeight));
				} else {
					final Rectangle2D.Double r2d = new Rectangle2D.Double(dX, dY - dHeight, dFullWidth, dFullHeight);

					// RENDER THE BACKGROUND FILL
					if (clrBackground != null) {
						g2d.setColor(clrBackground);
						g2d.fill(r2d);
					}

					// RENDER THE TEXT
					if (!bEmptyText) {
						g2d.setColor(clrText);
						for (int i = 0; i < lineCount; i++) {
							textLayout = ((ChartTextMetrics) textMetrics).getLayout(i);
							if (bRightAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + dFullWidth - dWidth - insets.getRight();
							} else if (bCenterAligned) {
								dWidth = textMetrics.getWidth(i);
								dXOffset = -insets.getLeft() + (dFullWidth - dWidth) / 2;
							}
							textLayout.draw(g2d, (float) (dX + dXOffset + insets.getLeft()),
									(float) (((dY - dDescent) + (dHeight * i)) + insets.getTop()));
						}
					}

					// RENDER THE OUTLINE/BORDER
					renderOutline(renderer, label.getOutline(), r2d);
				}
				// g2d.rotate( -dAngleInRadians, dX, dY );
			}
		} finally {
			g2d.setTransform(afTransform);
			cComp.recycleTextMetrics(textMetrics);
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

	protected void renderOutline(IPrimitiveRenderer renderer, LineAttributes lineAttribs, Rectangle2D.Double rect) {
		if (lineAttribs != null && lineAttribs.isVisible() && lineAttribs.getColor() != null) {
			Graphics2D g2d = (Graphics2D) ((IDeviceRenderer) renderer).getGraphicsContext();
			Stroke sPrevious = null;
			final ColorDefinition cd = lineAttribs.getColor();
			final Stroke sCurrent = ((G2dRendererBase) renderer).getCachedStroke(lineAttribs);
			if (sCurrent != null) // SOME STROKE DEFINED?
			{
				sPrevious = g2d.getStroke();
				g2d.setStroke(sCurrent);
			}
			g2d.setColor((Color) _sxs.getColor(cd));
			g2d.draw(rect);
			// g2d.setNoFillColor( g2d.getCurrentElement( ) );
			if (sPrevious != null) // RESTORE PREVIOUS STROKE
			{
				g2d.setStroke(sPrevious);
			}
		}
	}

}
