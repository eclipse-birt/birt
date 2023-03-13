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
@Deprecated
public interface IScriptAPIFactory {

	/**
	 * Create <code>IHideRule</code> instance
	 *
	 * @return IHideRule
	 */

	IHideRule createHideRule();

	/**
	 * Create <code>IFilterCondition</code>
	 *
	 * @return instance
	 */

	IFilterCondition createFilterCondition();

	/**
	 * Create <code>IDataBinding</code>
	 *
	 * @return instance
	 */

	IDataBinding createDataBinding();

	/**
	 * Create <code>IHighLightRule</code>
	 *
	 * @return instance
	 */

	IHighlightRule createHighLightRule();

	/**
	 * Create <code>ISortCondition</code>
	 *
	 * @return instance
	 */

	ISortCondition createSortCondition();

}
