/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for the Text Content Text content contains several
 * paragraphs which shares the same style properties.
 *
 * If the text contain several return characters and the display is inline, it
 * is treated as "INLINE-BLOCK" otherwise it is "INLINE".
 *
 * If the display is "block", it is "BLOCK" always.
 */
public interface ITextContent extends IContent {

	/**
	 * Get the string value of the Text Content.
	 *
	 * @return Returns the text value.
	 */
	String getText();

	/**
	 * Set the value of the text of the Text Content.
	 *
	 * @param text the text value.
	 */
	void setText(String text);

}
