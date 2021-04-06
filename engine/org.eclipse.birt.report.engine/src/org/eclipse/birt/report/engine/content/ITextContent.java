/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for the Text Content Text content contains several
 * paragraphs which shares the same style properties.
 * 
 * If the text contain serveral return characters and the display is inline, it
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
	public String getText();

	/**
	 * Set the value of the text of the Text Content.
	 * 
	 * @param text the text value.
	 */
	public void setText(String text);
}
