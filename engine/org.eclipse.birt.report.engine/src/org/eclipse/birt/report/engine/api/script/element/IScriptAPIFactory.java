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

package org.eclipse.birt.report.engine.api.script.element;

/**
 * Interface to create some structure instances.
 * 
 * @deprecated
 */
public interface IScriptAPIFactory {

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	public IHideRule createHideRule();

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @return instance
	 */

	public IFilterCondition createFilterCondition();

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @return instance
	 */

	public IDataBinding createDataBinding();

	/**
	 * Create <code>IHighLightRule</code>
	 * 
	 * @return instance
	 */

	public IHighlightRule createHighLightRule();

	/**
	 * Create <code>ISortCondition</code>
	 * 
	 * @return instance
	 */

	public ISortCondition createSortCondition();

}