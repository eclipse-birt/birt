/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.swt;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.RotatedRectangle;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.TextRendererAdapter;
import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.graphics.Transform;

/**
 * Contains useful methods for rendering text on an SWT graphics context
 */
final class SwtTextRenderer extends TextRendererAdapter {

	private static final PaletteData PALETTE_DATA = new PaletteData(0xFF0000, 0xFF00, 0xFF);

	private static final int TRANSPARENT_COLOR = 0x123456;

	private static final int SHADOW_THICKNESS = 3;

	/**
	 * The constructor.
	 */
	SwtTextRenderer(IDisplayServer sxs) {
		super(sxs);
	}

	Device getDevice() {
		return ((SwtDisplayServer) _sxs).getDevice();
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
	@Override
	public final void renderShadowAtLocation(IPrimitiveRenderer ipr, int iLabelPosition, // IConstants. LEFT, RIGHT,
																							// ABOVE or BELOW
			Location lo, // POINT WHERE THE CORNER OF THE ROTATED RECTANGLE
			// (OR EDGE CENTERED) IS RENDERED
			Label la) throws ChartException {
		if (!ChartUtil.isShadowDefined(la)) {
			return;
		}

		final ColorDefinition cdShadow = la.getShadowColor();
		if (cdShadow == null) {
			throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING,
					"SwtTextRenderer.exception.undefined.shadow.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}

		switch (iLabelPosition & POSITION_MASK) {
		case ABOVE:
			showTopValue(ipr, lo, la, false);
			break;

		case BELOW:
			showBottomValue(ipr, lo, la, false);
			break;

		case LEFT:
			showLeftValue(ipr, lo, la, false);
			break;

		case RIGHT:
			showRightValue(ipr, lo, la, false);
			break;
		}
	}

	/**
	 * @param ipr
	 * @param lo
	 * @param la
	 * @param b
	 */
	private void showRightValue(IPrimitiveRenderer ipr, Location lo, Label la, boolean b) {
		// TODO not used temporarily
	}

	/**
	 * @param ipr
	 * @param lo
	 * @param la
	 * @param b
	 */
	private void showLeftValue(IPrimitiveRenderer ipr, Location lo, Label la, boolean b) {
		// TODO not used temporarily
	}

	/**
	 * @param ipr
	 * @param lo
	 * @param la
	 * @param b
	 */
	private void showBottomValue(IPrimitiveRenderer ipr, Location lo, Label la, boolean b) {
		// TODO not used temporarily
	}

	/**
	 * @param ipr
	 * @param lo
	 * @param la
	 * @param b
	 */
	private void showTopValue(IPrimitiveRenderer ipr, Location lo, Label la, boolean b) {
		GC gc = (GC) ((IDeviceRenderer) ipr).getGraphicsContext();
		IChartComputation cComp = ((IDeviceRenderer) ipr).getChartComputation();
		double dX = lo.getX(), dY = lo.getY();
		final FontDefinition fd = la.getCaption().getFont();

		final int dAngleInDegrees = (int) fd.getRotation();

		final Color clrBackground = (Color) _sxs.getColor(la.getShadowColor());

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		final double dFW = itm.getFullWidth();
		final double dFH = itm.getFullHeight();
		cComp.recycleTextMetrics(itm);

		double scaledThickness = SHADOW_THICKNESS * _sxs.getDpiResolution() / 72d;

		gc.setBackground(clrBackground);

		R31Enhance.setAlpha(gc, la.getShadowColor());

		// HORIZONTAL TEXT
		if (dAngleInDegrees == 0) {
			gc.fillRectangle((int) (dX + scaledThickness), (int) (dY + scaledThickness), (int) dFW, (int) dFH);
		}
		// TEXT AT POSITIVE 90 (TOP RIGHT HIGHER THAN TOP LEFT CORNER)
		else if (dAngleInDegrees == 90) {
			gc.fillRectangle((int) (dX + scaledThickness), (int) (dY - dFW - scaledThickness), (int) dFH, (int) dFW);
		}
		// TEXT AT NEGATIVE 90 (TOP RIGHT LOWER THAN TOP LEFT CORNER)
		else if (dAngleInDegrees == -90) {
			gc.fillRectangle((int) (dX - dFH - scaledThickness), (int) (dY + scaledThickness), (int) dFH, (int) dFW);
		} else {
			Transform transform = new Transform(getDevice());
			transform.translate((float) dX, (float) dY);
			transform.rotate(-dAngleInDegrees);
			gc.setTransform(transform);
			gc.fillRectangle((int) scaledThickness, (int) scaledThickness, (int) dFW, (int) dFH);
			transform.dispose();
			gc.setTransform(null);
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param iLabelPosition
	 * @param lo
	 * @param la
	 * @throws ChartException
	 */
	@Override
	public final void renderTextAtLocation(IPrimitiveRenderer idr, int iLabelPosition, // IConstants. LEFT, RIGHT, ABOVE
			// or BELOW
			Location lo, // POINT WHERE THE CORNER OF THE ROTATED RECTANGLE
			// (OR
			// EDGE CENTERED) IS RENDERED
			Label la) throws ChartException {
		final GC gc = (GC) ((IDeviceRenderer) idr).getGraphicsContext();
		IChartComputation cComp = ((IDeviceRenderer) idr).getChartComputation();
		BoundingBox bb = cComp.computeBox(_sxs, iLabelPosition, la, 0, 0);

		switch (iLabelPosition & POSITION_MASK) {
		case ABOVE:
			bb.setTop(lo.getY() - bb.getHeight());
			bb.setLeft(lo.getX() - bb.getHotPoint());
			break;

		case BELOW:
			bb.setTop(lo.getY());
			bb.setLeft(lo.getX() - bb.getHotPoint());
			break;

		case LEFT:
			bb.setTop(lo.getY() - bb.getHotPoint());
			bb.setLeft(lo.getX() - bb.getWidth());
			break;

		case RIGHT:
			bb.setTop(lo.getY() - bb.getHotPoint());
			bb.setLeft(lo.getX());
			break;

		case INSIDE:
			bb.setTop(lo.getY() - bb.getHeight() / 2);
			bb.setLeft(lo.getX() - bb.getWidth() / 2);
			break;
		}

		// Adjust the position.
		adjustTextPosition(iLabelPosition, bb);

		// RENDER Shadow around the text label
		if (ChartUtil.isShadowDefined(la)) {
			ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
			try {
				final double dFH = itm.getFullHeight();

				Location tmpLoc = Methods.computeRotatedTopPoint(_sxs, bb, la, dFH);

				renderShadowAtLocation(idr, IConstants.ABOVE, tmpLoc, la);
			} catch (IllegalArgumentException uiex) {
				throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, uiex);
			} finally {
				cComp.recycleTextMetrics(itm);
			}
		}

		if (la.getCaption().getFont().getRotation() == 0 || R31Enhance.isR31Available()) {
			renderHorizontalText(cComp, gc, la, bb.getLeft(), bb.getTop());
		} else {
			final Image imgText = rotatedTextAsImage(cComp, la);
			gc.drawImage(imgText, (int) bb.getLeft(), (int) bb.getTop());
			imgText.dispose();
		}

		renderBorder(cComp, gc, la, iLabelPosition, lo);
	}

	/**
	 * 
	 * @param idr
	 * @param boBlock
	 * @param taBlock
	 * @param la
	 */
	@Override
	public final void renderTextInBlock(IDeviceRenderer idr, Bounds boBlock, TextAlignment taBlock, Label la)
			throws ChartException {
		IChartComputation cComp = idr.getChartComputation();
		Text t = la.getCaption();
		String sText = t.getValue();
		ColorDefinition cdText = t.getColor();
		if (cdText == null) {
			throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING,
					"SwtTextRenderer.exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle(_sxs.getULocale()));
		}
		final GC gc = (GC) idr.getGraphicsContext();
		la.getCaption().setValue(sText);
		BoundingBox bb = cComp.computeBox(_sxs, ABOVE, la, 0, 0);

		if (taBlock == null) {
			taBlock = AttributeFactory.eINSTANCE.createTextAlignment();
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

		// RENDER Shadow around the text label
		if (ChartUtil.isShadowDefined(la)) {
			final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);

			try {
				final double dFH = itm.getFullHeight();

				Location tmpLoc = Methods.computeRotatedTopPoint(_sxs, bb, la, dFH);

				renderShadowAtLocation(idr, IConstants.ABOVE, tmpLoc, la);
			} catch (IllegalArgumentException uiex) {
				throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, uiex);
			} finally {
				cComp.recycleTextMetrics(itm);
			}
		}

		if (la.getCaption().getFont().getRotation() == 0 || R31Enhance.isR31Available()) {
			renderHorizontalText(cComp, gc, la, bb.getLeft(), bb.getTop());
		} else {
			final Image imgText = rotatedTextAsImage(cComp, la);
			gc.drawImage(imgText, (int) bb.getLeft(), (int) bb.getTop());
			imgText.dispose();
		}

		renderBorder(cComp, gc, la, IConstants.ABOVE,
				goFactory.createLocation(bb.getLeft() + bb.getHotPoint(), bb.getTop() + bb.getHeight()));
	}

	private final void renderBorder(IChartComputation cComp, GC gc, Label la, int iLabelLocation, Location lo)
			throws ChartException {
		// RENDER THE OUTLINE/BORDER
		final LineAttributes lia = la.getOutline();
		if (lia != null && lia.isVisible() && lia.getColor() != null) {
			RotatedRectangle rr = cComp.computePolygon(_sxs, iLabelLocation, la, lo.getX(), lo.getY(), null);

			final int iOldLineStyle = gc.getLineStyle();
			final int iOldLineWidth = gc.getLineWidth();

			final Color cFG = (Color) _sxs.getColor(lia.getColor());
			gc.setForeground(cFG);

			R31Enhance.setAlpha(gc, lia.getColor());

			int iLineStyle = SWT.LINE_SOLID;
			switch (lia.getStyle().getValue()) {
			case (LineStyle.DOTTED):
				iLineStyle = SWT.LINE_DOT;
				break;
			case (LineStyle.DASH_DOTTED):
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case (LineStyle.DASHED):
				iLineStyle = SWT.LINE_DASH;
				break;
			}
			gc.setLineStyle(iLineStyle);
			gc.setLineWidth(lia.getThickness());

			gc.drawPolygon(rr.getSwtPoints());

			gc.setLineStyle(iOldLineStyle);
			gc.setLineWidth(iOldLineWidth);
			cFG.dispose();
		}
	}

	/**
	 * Use this optimized routine for rendering horizontal ONLY text
	 * 
	 * @param gc
	 * @param la
	 * @param lo
	 */
	private final void renderHorizontalText(IChartComputation cComp, GC gc, Label la, double dX, double dY) {
		final FontDefinition fd = la.getCaption().getFont();
		final Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		final double dFW = itm.getFullWidth();
		final double dH = itm.getHeight();
		final double dFH = itm.getFullHeight();
		double dXOffset = 0;
		double dW = 0;
		final Insets ins = goFactory.scaleInsets(la.getInsets(), _sxs.getDpiResolution() / 72d);

		final HorizontalAlignment ha = la.getCaption().getFont().getAlignment().getHorizontalAlignment();
		final boolean bRightAligned = ha.getValue() == HorizontalAlignment.RIGHT;
		final boolean bCenterAligned = ha.getValue() == HorizontalAlignment.CENTER;

		final Rectangle r;

		Transform tr = null;

		Transform trOld = new Transform(getDevice());
		gc.getTransform(trOld);

		// In Linux-Cairo environment, the reverse transformation may damage the
		// clipping area due to computing-precision lost, so we must record it
		// manually here.
		Region previousClipping = null;

		if (R31Enhance.isR31Available()) {
			r = new Rectangle(0, 0, (int) dFW, (int) dFH);
			tr = new Transform(getDevice());
			gc.getTransform(tr);

			if (la.getCaption().getFont().getRotation() != 0) {

				float rotate = (float) la.getCaption().getFont().getRotation();
				double dAngleInRadians = ((-rotate * Math.PI) / 180.0);
				double dSineTheta = Math.sin(dAngleInRadians);

				float tTx = (float) (dX - dFW / 2);
				float tTy = (float) (dY - dFH / 2);

				if (rotate > 0)
					tTy += dFW * Math.abs(dSineTheta);
				else
					tTx += dFH * Math.abs(dSineTheta);

				tr.translate((float) (dFW / 2), (float) (dFH / 2));
				tr.translate(tTx, tTy);
				tr.rotate(-rotate);
			} else {
				R31Enhance.translate(gc, tr, (float) dX, (float) dY);
			}

			// if we are using transformation, record previous clipping first.
			previousClipping = new Region();
			gc.getClipping(previousClipping);
			R31Enhance.setTransform(gc, tr);
		} else {
			r = new Rectangle((int) dX, (int) dY, (int) dFW, (int) dFH);
		}

		// RENDER THE BACKGROUND
		boolean bFullyTransparent = true;

		if (la.getBackground() != null) {
			bFullyTransparent = (((ColorDefinition) la.getBackground()).getTransparency() == 0);

			R31Enhance.setAlpha(gc, 255);
		}

		if (!bFullyTransparent) {
			final Color clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
			final Color clrPreviousBackground = gc.getBackground();

			if (((ColorDefinition) la.getBackground()).isSetTransparency()) {
				R31Enhance.setAlpha(gc, (ColorDefinition) la.getBackground());
			}

			gc.setBackground(clrBackground);
			gc.fillRectangle(r);
			clrBackground.dispose();
			gc.setBackground(clrPreviousBackground);
		}

		// RENDER THE TEXT
		gc.setForeground(clrText);

		R31Enhance.setAlpha(gc, la.getCaption().getColor());

		final Font f = (Font) _sxs.createFont(fd);
		gc.setFont(f);

		TextStyle style = new TextStyle(f, null, null);
		style.underline = fd.isUnderline();
		style.strikeout = fd.isStrikethrough();
		TextLayout layout = new TextLayout(gc.getDevice());

		if (R31Enhance.isR31Available()) {
			for (int i = 0; i < itm.getLineCount(); i++) {
				String oText = itm.getLine(i);
				dW = itm.getWidth(i);
				if (bRightAligned) {
					// TODO this seems to be zero as dFW=dW+insets (see itm.getFullwidth())
					// what about left align?
					dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
				} else if (bCenterAligned) {
					dXOffset = -ins.getLeft() + (dFW - dW) / 2;
				}

				int x = (int) (dXOffset + ins.getLeft());
				int y = (int) (dH * i + ins.getTop());
				layout.setText(oText);
				layout.setStyle(style, 0, oText.length());
				layout.draw(gc, x, y);
			}
		} else {
			for (int i = 0; i < itm.getLineCount(); i++) {
				String oText = itm.getLine(i);
				dW = itm.getWidth(i);
				if (bRightAligned) {
					dXOffset = -ins.getLeft() + dFW - dW - ins.getRight();
				} else if (bCenterAligned) {
					dXOffset = -ins.getLeft() + (dFW - dW) / 2;
				}

				int x = (int) (dX + dXOffset + ins.getLeft());
				int y = (int) (dY + dH * i + ins.getTop());
				layout.setText(oText);
				layout.setStyle(style, 0, oText.length());
				layout.draw(gc, x, y);
			}
		}

		// restore the transform and clipping
		R31Enhance.setTransform(gc, trOld);

		if (previousClipping != null) {
			gc.setClipping(previousClipping);
			previousClipping.dispose();
		}

		R31Enhance.disposeTransform(trOld);
		R31Enhance.disposeTransform(tr);

		layout.dispose();
		f.dispose();
		clrText.dispose();
		cComp.recycleTextMetrics(itm);
	}

	/**
	 * NOTE: Need a better algorithm for rendering smoother rotated text
	 * 
	 * @param la
	 * @return
	 */
	final Image rotatedTextAsImage(IChartComputation cComp, Label la) {
		double dX = 0, dY = 0;
		final FontDefinition fd = la.getCaption().getFont();
		final double dAngleInDegrees = fd.getRotation();
		final Color clrText = (Color) _sxs.getColor(la.getCaption().getColor());
		double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);

		final ITextMetrics itm = cComp.getTextMetrics(_sxs, la, 0);
		final double dFW = itm.getFullWidth();
		final double dH = itm.getHeight();
		final double dFH = itm.getFullHeight();
		final int iLC = itm.getLineCount();
		final Insets ins = goFactory.scaleInsets(la.getInsets(), _sxs.getDpiResolution() / 72d);
		dY += dFH;
		double dCosTheta = Math.cos(dAngleInRadians);
		double dSineTheta = Math.sin(dAngleInRadians);

		final double dFWt = Math.abs(dFW * dCosTheta + dFH * Math.abs(dSineTheta));
		final double dFHt = Math.abs(dFH * dCosTheta + dFW * Math.abs(dSineTheta));

		final int clipWs = (int) dFW;
		final int clipHs = (int) dFH;
		final int clipWt = (int) dFWt;
		final int clipHt = (int) dFHt;

		// RENDER THE TEXT ON AN OFFSCREEN CANVAS
		ImageData imgdtaS = new ImageData(clipWs, clipHs, 32, PALETTE_DATA);
		imgdtaS.transparentPixel = TRANSPARENT_COLOR;
		final Image imgSource = new Image(getDevice(), imgdtaS);
		for (int i = 0; i < clipHs; i++) {
			for (int j = 0; j < clipWs; j++) {
				imgdtaS.setPixel(j, i, imgdtaS.transparentPixel);
			}
		}

		final GC gc = new GC(imgSource);
		final Font f = (Font) _sxs.createFont(fd);

		final Rectangle r = new Rectangle((int) dX, (int) (dY - dFH), (int) dFW, (int) dFH);

		// RENDER THE BACKGROUND
		final boolean bFullyTransparent = (((ColorDefinition) la.getBackground()).getTransparency() == 0);
		if (!bFullyTransparent) {
			final Color clrBackground = (Color) _sxs.getColor((ColorDefinition) la.getBackground());
			gc.setBackground(clrBackground);
			gc.fillRectangle(r);
			clrBackground.dispose();
		} else {
			final Color cTransparent = new Color(getDevice(), 0x12, 0x34, 0x56);
			gc.setBackground(cTransparent);
			gc.fillRectangle(r);
			cTransparent.dispose();
		}

		// RENDER THE TEXT
		gc.setForeground(clrText);
		gc.setFont(f);
		for (int i = 0; i < itm.getLineCount(); i++) {
			gc.drawText(itm.getLine(iLC - i - 1), (int) (dX + ins.getLeft()), (int) (dY - dH - dH * i + ins.getTop()),
					false);
		}

		int lineWidth = 0;
		if (fd.isUnderline() || fd.isStrikethrough()) {
			lineWidth = (int) (fd.getSize() / 12);
			gc.setLineWidth(lineWidth);

			if (fd.isUnderline()) {
				gc.drawLine((int) (dX + ins.getLeft()), (int) (dY - dFH + dH - lineWidth),
						(int) (dX + ins.getLeft() + dFW), (int) (dY - dFH + dH - lineWidth));
			}
			if (fd.isStrikethrough()) {
				gc.drawLine((int) (dX + ins.getLeft()), (int) (dY - dFH + dH * 0.5 - lineWidth),
						(int) (dX + ins.getLeft() + dFW), (int) (dY - dFH + dH * 0.5 - lineWidth));
			}
		}

		clrText.dispose();
		cComp.recycleTextMetrics(itm);
		f.dispose();
		gc.dispose();

		imgdtaS = imgSource.getImageData();
		final ImageData imgdtaT = new ImageData(clipWt, clipHt, 32, PALETTE_DATA);
		imgdtaT.palette = imgdtaS.palette;
		imgdtaT.transparentPixel = imgdtaS.transparentPixel;
		for (int i = 0; i < clipHt; i++) {
			for (int j = 0; j < clipWt; j++) {
				imgdtaT.setPixel(j, i, imgdtaT.transparentPixel);
			}
		}
		final int neg = (dAngleInDegrees < 0) ? (int) (clipHs * dSineTheta) : 0;
		final int pos = (dAngleInDegrees > 0) ? (int) (clipWs * Math.abs(dSineTheta)) : 0;

		final int yMax = clipHt - pos;
		final int xMax = clipWt - neg;
		int xSrc = 0, ySrc = 0, xDest = 0, yDest = 0, x = 0, y = 0;
		double yDestCosTheta, yDestSineTheta;

		// PIXEL TRANSFER LOOP
		for (yDest = -pos; yDest < yMax; yDest++) {
			yDestCosTheta = yDest * dCosTheta;
			yDestSineTheta = yDest * dSineTheta;
			for (xDest = -neg; xDest < xMax; xDest++) {
				// CALC SRC CO-ORDINATES
				xSrc = (int) Math.round(xDest * dCosTheta + yDestSineTheta);
				ySrc = (int) Math.round(yDestCosTheta - xDest * dSineTheta);
				if (xSrc < 0 || xSrc >= clipWs || ySrc < 0 || ySrc >= clipHs) // OUT
				// OF
				// RANGE
				{
					continue;
				}

				// CALC DEST CO-ORDINATES
				x = xDest + neg;
				y = yDest + pos;
				if (x < 0 || x >= clipWt || y < 0 || y >= clipHt) // OUT OF
				// RANGE
				{
					continue;
				}

				// NOW THAT BOTH CO-ORDINATES ARE WITHIN RANGE, TRANSFER A PIXEL
				imgdtaT.setPixel(x, y, imgdtaS.getPixel(xSrc, ySrc));
			}
		}
		imgSource.dispose();

		// BUILD THE IMAGE USING THE NEWLY MANIPULATED BYTES
		return new Image(getDevice(), imgdtaT);
	}

	/**
	 * Adjusts the text by one half of width or height. Currently use HotPoint as
	 * adjustment.
	 * 
	 * @param iLabelPosition position state
	 * @param bb
	 */
	protected void adjustTextPosition(int iLabelPosition, BoundingBox bb) {
		if (iLabelPosition > POSITION_MASK) {
			if ((iLabelPosition & POSITION_MOVE_ABOVE) == POSITION_MOVE_ABOVE) {
				bb.setTop(bb.getTop() - bb.getHotPoint());
			} else if ((iLabelPosition & POSITION_MOVE_BELOW) == POSITION_MOVE_BELOW) {
				bb.setTop(bb.getTop() + bb.getHotPoint());
			} else if ((iLabelPosition & POSITION_MOVE_LEFT) == POSITION_MOVE_LEFT) {
				bb.setLeft(bb.getLeft() - bb.getHotPoint());
			} else if ((iLabelPosition & POSITION_MOVE_RIGHT) == POSITION_MOVE_RIGHT) {
				bb.setLeft(bb.getLeft() + bb.getHotPoint());
			}
		}
	}
}