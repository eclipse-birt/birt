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

import java.util.HashMap;

import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.w3c.dom.Node;

/**
 * Provides the interfaces for the Text Content
 * 
 * 
 * @version $Revision$ $Date$
 */
public interface ITextContent extends IReportItemContent
{

	/**
	 * @return Returns the value.
	 */
	public String getValue( );

	/**
	 * Adds CSS2.0 Properties
	 * 
	 * @param node
	 * @param properties
	 */
	public void addCssStyle( Node node, HashMap properties );

	/**
	 * Gets the corresponding CSS properties of the specified node.
	 * 
	 * @param node
	 *            the specified node
	 * @return the hashmap-type instance containing the CSS properties that the
	 *         key is the property name and the value is the property value.
	 */
	public HashMap getCssStyle( Node node );

	/**
	 * @return The stringFormatted.
	 */
	public boolean isStringFormatted( );

	/**
	 * Sets the value of stringFormatted.
	 * 
	 * @param value
	 */
	public void setStringFormatted( boolean value );

	/**
	 * Adds the image content to the collection in term of the <tt>Node</tt>
	 * 
	 * @param node
	 *            the key in the collection
	 * @param img
	 *            the image content
	 */
	public void addImageContent( Node node, IImageItemContent img );

	/**
	 * Gets the image content according to the <tt>Node</tt>
	 * 
	 * @param node
	 *            the specified key
	 * @return the image content
	 */
	public IImageItemContent getImageContent( Node node );

	/**
	 * @param helpText
	 *            The helpText to set.
	 */
	public void setHelpText( String helpText );

	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText( );

	/**
	 * @return Returns the singleLine.
	 */
	public boolean isSingleLine( );

	/**
	 * Sets the value
	 * 
	 * @param value
	 *            the value set
	 */
	public void setValue( String value );

	/**
	 * Gets the action design
	 * 
	 * @return
	 */
	public ActionDesign getAction( );

	/**
	 * @return Returns the domTree.
	 */
	public Node getDomTree( );

	/**
	 * @param domTree
	 *            The domTree to set.
	 */
	public void setDomTree( Node domTree );
}