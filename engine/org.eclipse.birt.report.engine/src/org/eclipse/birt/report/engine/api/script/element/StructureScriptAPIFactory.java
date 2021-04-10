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

package org.eclipse.birt.report.engine.api.script.element;

/**
 * Structure Factory for script API.
 * 
 * @deprecated replaced by methods in {@link IReportDesign} .
 * 
 */

public class StructureScriptAPIFactory {

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	public static IHideRule createHideRule() {
		return ScriptAPIFactory.getInstance().createHideRule();
	}

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @return instance
	 */

	public static IFilterCondition createFilterCondition() {
		return ScriptAPIFactory.getInstance().createFilterCondition();
	}

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @return instance
	 */

	public static IDataBinding createDataBinding() {
		return ScriptAPIFactory.getInstance().createDataBinding();
	}

	/**
	 * Create <code>IHighLightRule</code>
	 * 
	 * @return instance
	 */

	public static IHighlightRule createHighLightRule() {
		return ScriptAPIFactory.getInstance().createHighLightRule();
	}

	/**
	 * Create <code>ISortCondition</code>
	 * 
	 * @return instance
	 */

	public static ISortCondition createSortCondition() {
		return ScriptAPIFactory.getInstance().createSortCondition();
	}

}
