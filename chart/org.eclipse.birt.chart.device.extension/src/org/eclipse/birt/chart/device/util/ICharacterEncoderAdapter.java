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

package org.eclipse.birt.chart.device.util;

/**
 * This interface defines method to encode/decode special characters for HTML,
 * javascript and other cases.
 * 
 * @since 2.6
 */

public interface ICharacterEncoderAdapter {
	/**
	 * Escapes special characters for HTML.
	 * 
	 * @param s the content of HTML.
	 * @return
	 */
	public String escape(String s);

	/**
	 * Transforms a string to JS string constants.
	 * 
	 * @param s
	 * @return
	 */
	public String transformToJsConstants(String s);
}
