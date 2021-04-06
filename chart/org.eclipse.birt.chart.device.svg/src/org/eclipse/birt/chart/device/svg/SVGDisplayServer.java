/***********************************************************************
 * Copyright (c) 2005 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.net.URL;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.swing.SwingDisplayServer;
import org.eclipse.birt.chart.device.util.ChartTextLayout;
import org.eclipse.birt.chart.device.util.ChartTextMetrics;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Label;

/**
 * This class represents the SVG Displayer Server.
 */
public class SVGDisplayServer extends SwingDisplayServer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDisplayServer#loadImage(java.net.URL)
	 */
	public Object loadImage(URL url) throws ChartException {
		URL urlFound = findResource(url);
		Image image = (Image) super.loadImage(urlFound);
		return new SVGImage(image, urlFound);
	}

	public ITextMetrics getTextMetrics(Label la, boolean autoReuse) {
		ChartTextMetrics tm = new ChartTextMetrics(this, la, autoReuse);
		return tm;
	}

	@Override
	public ChartTextLayout createTextLayout(String value, Map<? extends Attribute, ?> fontAttributes,
			FontRenderContext frc) {
		return new SVGTextLayout(value, fontAttributes, frc);
	}
}