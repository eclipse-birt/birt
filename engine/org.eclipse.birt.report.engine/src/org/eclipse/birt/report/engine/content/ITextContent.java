/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 * Provides the interfaces for the Text Content
 * Text content contains several paragraphs which shares the same style
 * properties.
 * 
 * If the text contain serveral return characters and the display is inline, it
 * is treated as "INLINE-BLOCK" otherwise it is "INLINE".
 * 
 * If the display is "block", it is "BLOCK" always.
 * 
 * @version $Revision: 1.8 $ $Date: 2005/05/08 06:59:45 $
 
 */
public interface ITextContent extends IContent
{

	/**
	 * string content
	 * 
	 * @return Returns the value.
	 */
	public String getText( );

	public void setText( String text );
}
