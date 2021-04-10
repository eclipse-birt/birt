/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.svg;

import org.eclipse.birt.chart.device.util.HTMLEncoderAdapter;

/**
 * This class is responsible to encode/decode special characters for SVG
 * document.
 * 
 * @since 2.6
 */

public class SVGEncoderAdapter extends HTMLEncoderAdapter {

	private static SVGEncoderAdapter instance;

	/**
	 * Returns instance of this class.
	 * 
	 * @return
	 */
	public static SVGEncoderAdapter getInstance() {
		if (instance == null) {
			instance = new SVGEncoderAdapter();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.util.ICharacterEncoderAdapter#escape(java.lang.
	 * String)
	 */
	public String escape(String s) {
		return s;
	}
}
