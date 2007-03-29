/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * The factory class to create scriptable objects.
 * 
 */

public interface ISimpleElementFactory
{

	final int MULTI_ROW_ITEM = 0;

	final int SIMPLE_ROW_ITEM = 1;

	/**
	 * Creates the scriptable object for the corresponding element handles.
	 * 
	 * @param handle
	 *            the element handle
	 * @param type
	 *            the expected data row type. Can be <code>MULTI_ROW_ITEM</code>
	 *            or <code>EMPTY_ROW_ITEM</code>
	 * @return the scriptable object
	 */

	public IReportItem wrapExtensionElement( ExtendedItemHandle handle, int type );

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	public IHideRule createHideRule( );

	/**
	 * Creates the filter structure.
	 * 
	 * @return the filter
	 */

	public IFilterCondition createFilterCondition( );

	/**
	 * Creates the data biinding structure.
	 * 
	 * @return the data binding
	 */

	public IDataBinding createDataBinding( );

	/**
	 * Creates the highlight rule structure.
	 * 
	 * @return the highlight rule
	 */

	public IHighlightRule createHighLightRule( );

	/**
	 * Creates the sort structure.
	 * 
	 * @return the sort
	 */

	public ISortCondition createSortCondition( );

}
