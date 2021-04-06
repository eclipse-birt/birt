/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.g2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.eclipse.birt.chart.device.DisplayAdapter;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.util.ChartTextLayout;
import org.eclipse.birt.chart.device.util.ChartTextMetrics;
import org.eclipse.birt.chart.device.util.ITextLayoutFactory;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * The base class of all display servers which bases on java.awt.Graphics2D.
 */

public class G2dDisplayServerBase extends DisplayAdapter implements ITextLayoutFactory {

	protected Graphics2D _g2d;
	protected int iDpiResolution = 0;
	private static Map<String, BufferedImage> imageCache = new HashMap<String, BufferedImage>();
	private static Map<Image, Size> sizeCache = new HashMap<Image, Size>();

	@Override
	public Object createFont(FontDefinition fd) {
		// final Map<? extends AttributedCharacterIterator.Attribute,?> fontAttribs =
		// new HashMap<? extends AttributedCharacterIterator.Attribute,Object>( );
		final Map<TextAttribute, Object> fontAttribs = ChartUtil.newHashMap();
		fontAttribs.put(TextAttribute.FAMILY, fd.getName());
		// Although the fonts is set in points, we need to apply the dpi ratio manually
		// java always assumes 72dpi for fonts, see this link:
		// http://java.sun.com/products/java-media/2D/reference/faqs/index.html#Q_Why_does_eg_a_10_pt_font_in_Ja

		fontAttribs.put(TextAttribute.SIZE, new Float(fd.getSize() * getDpiResolution() / 72d));
		if (fd.isItalic()) {
			fontAttribs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		}
		if (fd.isBold()) {
			fontAttribs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		}
		if (fd.isUnderline()) {
			fontAttribs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		}
		if (fd.isStrikethrough()) {
			fontAttribs.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		}
		return new Font(fontAttribs);
	}

	@Override
	public final Object getColor(ColorDefinition cd) {
		return new Color(cd.getRed(), cd.getGreen(), cd.getBlue(), cd.getTransparency());
	}

	@Override
	public int getDpiResolution() {
		if (iDpiResolution <= 0) {
			return super.getDpiResolution();
		}
		return iDpiResolution;
	}

	@Override
	public void setDpiResolution(int dpi) {
		iDpiResolution = dpi;
	}

	@Override
	public void setGraphicsContext(Object graphicContext) {
		_g2d = (Graphics2D) graphicContext;
	}

	@Override
	public ITextMetrics getTextMetrics(Label la, boolean autoReuse) {
		ChartTextMetrics tm = new ChartTextMetrics(this, la, autoReuse);
		return tm;
	}

	@Override
	public Object loadImage(URL url) throws ChartException {
		String sUrl = url.toString();
		BufferedImage image = imageCache.get(sUrl);

		if (image == null) {
			URL urlFound = findResource(url);
			try {
				image = ImageIO.read(urlFound);
				imageCache.put(sUrl, image);
			} catch (IOException e) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.IMAGE_LOADING, e);
			}
		}

		return image;
	}

	@Override
	public Size getSize(Object oImage) {
		Size size = null;
		if (oImage instanceof Image) {
			Image image = (Image) oImage;
			size = sizeCache.get(image);
			if (size == null) {
				int newWidth = image.getWidth((ImageObserver) getObserver());
				int newHeight = image.getHeight((ImageObserver) getObserver());
				size = SizeImpl.create(newWidth, newHeight);
				sizeCache.put(image, size);
			}
		}
		return size;
	}

	public ChartTextLayout createTextLayout(String value, Map<? extends Attribute, ?> fontAttributes,
			FontRenderContext frc) {
		return new ChartTextLayout(value, fontAttributes, frc);
	}

}
