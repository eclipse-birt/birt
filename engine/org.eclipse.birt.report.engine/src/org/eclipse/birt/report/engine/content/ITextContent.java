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

import org.w3c.dom.Node;

/**
 * Provides the interfaces for the Text Content
 * 
 * @version $Revision: 1.7 $ $Date: 2005/05/08 06:08:27 $
 */
public interface ITextContent extends IReportItemContent
{

	/**
	 * @return Returns the value.
	 */
	public String getValue( );


	/**
	 * Gets the corresponding CSS properties of the specified node.
	 * <p>
	 * The caller calls it only if the Text-Item or multi-line is involved
	 * 
	 * @param node
	 *            the specified node
	 * @return the hashmap-type instance containing the CSS properties that the
	 *         key is the property name and the value is the property value.
	 */
	public HashMap getCssStyle( Node node );

	/**
	 * Gets the entire CSS style set,key--XML DOM node and value--the same as
	 * the return value of the method <code>getCssStyle(Node)</code>
	 * <p>
	 * The caller calls it only if the Text-Item or multi-line is involved
	 * 
	 * @return hash map if exists, otherwise null
	 */
	public HashMap getCssStyleSet( );


	/**
	 * Gets the image content according to the <tt>Node</tt>
	 * 
	 * @param node
	 *            the specified key
	 * @return the image content
	 */
	public IImageItemContent getImageContent( Node node );

	/**
	 * @return Returns the singleLine.
	 */
	public boolean isSingleLine( );

	/**
	 * Walks on every node
	 * 
	 * @param visitor
	 *            the ITextNodeVistor instance
	 */
	public void accessDOMTree( ITextNodeVisitor visitor );

	/**
	 * Defines the interfaces of ITextNodeVisitor
	 */
	public interface ITextNodeVisitor
	{

		/**
		 * Starts the node
		 * 
		 * @param node
		 *            the Node instance
		 */
		void onNodeStart( Node node );

		/**
		 * Outputs the text of the node
		 * 
		 * @param text
		 *            the text of the node
		 * @param needEscape
		 *            the flag indicating weather or not escaping the content
		 */
		void onText( String text, boolean needEscape );

		/**
		 * Ends the node
		 * 
		 * @param node
		 *            the Node instance
		 */
		void onNodeEnd( Node node );
	}
}

