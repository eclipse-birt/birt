/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
	@Override
	public String escape(String s) {
		return s;
	}
}
