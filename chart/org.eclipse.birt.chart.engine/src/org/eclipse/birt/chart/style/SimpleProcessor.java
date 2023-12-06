/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.style;

import java.util.Iterator;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;

/**
 * A default implementation for IStyleProcessor.
 */
public final class SimpleProcessor extends BaseStyleProcessor {

	private static final SimpleStyle defaultStyle;

	private static SimpleProcessor instance;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	static {
		TextAlignment ta = goFactory.createTextAlignment();
		FontDefinition font = goFactory.createFontDefinition("SansSerif", //$NON-NLS-1$
				12, false, false, false, false, false, 0, ta);

		defaultStyle = new SimpleStyle(font, goFactory.BLACK(), null, null, null);
	}

	/**
	 * The access entry point.
	 *
	 * @return Return the access entry point
	 */
	synchronized public static SimpleProcessor instance() {
		if (instance == null) {
			instance = new SimpleProcessor();
		}

		return instance;
	}

	/**
	 * The constructor.
	 */
	private SimpleProcessor() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse.birt.chart.
	 * model.attribute.StyledComponent)
	 */
	@Override
	public IStyle getStyle(Chart model, StyledComponent name) {
		if (model != null && model.getStyles().size() > 0) {
			for (Iterator<StyleMap> itr = model.getStyles().iterator(); itr.hasNext();) {
				StyleMap sm = itr.next();

				if (sm.getComponentName().equals(name)) {
					Style ss = sm.getStyle();

					SimpleStyle rt = new SimpleStyle(defaultStyle);

					rt.setFont(goFactory.copyOf(ss.getFont()));

					if (ss.getColor() != null) {
						rt.setColor(goFactory.copyOf(ss.getColor()));
					}
					if (ss.getBackgroundColor() != null) {
						rt.setBackgroundColor(goFactory.copyOf(ss.getBackgroundColor()));
					}
					if (ss.getBackgroundImage() != null) {
						rt.setBackgroundImage(goFactory.copyOf(ss.getBackgroundImage()));
					}
					if (ss.getPadding() != null) {
						rt.setPadding(goFactory.copyOf(ss.getPadding()));
					}

					return rt;
				}
			}
		}

		// Always return the default value.
		return defaultStyle.copy();
	}
}
