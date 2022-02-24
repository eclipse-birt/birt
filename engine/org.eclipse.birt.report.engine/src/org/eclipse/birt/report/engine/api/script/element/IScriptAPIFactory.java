/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
