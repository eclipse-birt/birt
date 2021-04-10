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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.swt.graphics.Image;

/**
 * The interface to customize operators bar,mediates between the expression
 * builder's model and the expression builder itself, and provide the image,
 * toolip and the text to display in the choice list and insert into source
 * editor for a given element.
 */

public interface IExpressionProvider {

	/** The operator separator */
	public static final Operator OPERATOR_SEPARATOR = new Operator("operatorSeparator", null); //$NON-NLS-1$

	/**
	 * Returns the operators which show on the operator bar.
	 * 
	 * @return an array of operators, or null there is no operator bar.
	 */

	public Operator[] getOperators();

	/**
	 * Returns the elements for category.
	 * 
	 * @return an array of elements for category.
	 */

	public Object[] getCategory();

	/**
	 * Returns the elements to show in the next list with the given input element.
	 * 
	 * @param parentElement the parent element
	 * 
	 * @return an array of child elements
	 */

	public Object[] getChildren(Object parentElement);

	/**
	 * Returns the image for the given element.
	 * 
	 * @param element the given element
	 * 
	 * @return Image or null if there is no image for the given element
	 */
	public Image getImage(Object element);

	/**
	 * Returns the display text for the given element.
	 * 
	 * @param element the given element
	 * 
	 * @return String or null if there is no display text for the given element
	 */
	public String getDisplayText(Object element);

	/**
	 * Returns the tooltip text for the given element.
	 * 
	 * @param element the given element
	 * 
	 * @return String or null if there is no tooltip text for the given element
	 */
	public String getTooltipText(Object element);

	/**
	 * Returns the text to insert into the source editor when the given element is
	 * double-clicked.
	 * 
	 * @param element the given element
	 * 
	 * @return String or null if there is no text to insert for the given element
	 */
	public String getInsertText(Object element);

	public boolean hasChildren(Object element);
}
