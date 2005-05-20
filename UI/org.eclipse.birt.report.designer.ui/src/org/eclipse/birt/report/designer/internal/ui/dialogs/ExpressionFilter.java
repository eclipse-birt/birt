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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;

/**
 * A filter is used by a structure to extract a subset of elements
 *  
 */

public abstract class ExpressionFilter
{

	/**
	 * Creates a new filter.
	 */
	public ExpressionFilter( )
	{
	}

	/**
	 * Filters the given elements for the given viewer. The input array is not
	 * modified.
	 * <p>
	 * The default implementation of this method calls <code>select</code> on
	 * each element in the array, and returns only those elements for which
	 * <code>select</code> returns <code>true</code>.
	 * </p>
	 * 
	 * @param parent
	 *            the parent element
	 * @param elements
	 *            the elements to filter
	 * @return the filtered elements
	 */

	public Object[] filter( Object parent, Object[] elements )
	{
		int size = elements.length;
		ArrayList out = new ArrayList( size );
		for ( int i = 0; i < size; ++i )
		{
			Object element = elements[i];
			if ( select( parent, element ) )
			{
				out.add( element );
			}
		}
		return out.toArray( );
	}

	/**
	 * Returns whether the given element makes it through this filter.
	 * 
	 * @param parentElement
	 *            the parent element
	 * @param element
	 *            the element
	 * @return <code>true</code> if element is included in the filtered set,
	 *         and <code>false</code> if excluded
	 */

	public abstract boolean select( Object parentElement, Object element );
}