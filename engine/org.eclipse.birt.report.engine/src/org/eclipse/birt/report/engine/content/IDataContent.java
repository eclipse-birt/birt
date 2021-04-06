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
 * Provides the interface for the Data Content.
 */
public interface IDataContent extends ITextContent {

	/**
	 * Get the help text of the data content.
	 * 
	 * @return the help text.
	 */
	String getHelpText();

	/**
	 * Get the help key of the data content.
	 * 
	 * @return the help key.
	 */
	String getHelpKey();

	/**
	 * Set the value of the data content.
	 * 
	 * @param value the value of the data content.
	 */
	void setValue(Object value);

	/**
	 * Get the value of the data content.
	 * 
	 * @return the value of the data content.
	 */
	Object getValue();

	/**
	 * Get the label text of the data content.
	 * 
	 * @return the label text of the data content.
	 */
	String getLabelText();

	/**
	 * Get the label key of the data content.
	 * 
	 * @return the label key of the data content.
	 */
	String getLabelKey();

	/**
	 * Set the label text of the data content.
	 * 
	 * @param text the label text of the data content.
	 */
	void setLabelText(String text);

	/**
	 * Set the label key of the data content.
	 * 
	 * @param key the label key of the data content.
	 */
	void setLabelKey(String key);
}
