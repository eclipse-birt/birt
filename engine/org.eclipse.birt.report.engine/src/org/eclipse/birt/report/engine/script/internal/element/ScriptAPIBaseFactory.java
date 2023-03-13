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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.engine.api.script.element.IFilterCondition;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IScriptAPIFactory;
import org.eclipse.birt.report.engine.api.script.element.ISortCondition;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

/**
 * Interface to create some structure instances.
 *
 * @deprecated
 */
@Deprecated
public class ScriptAPIBaseFactory implements IScriptAPIFactory {

	/**
	 * Create <code>IHideRule</code> instance
	 *
	 * @return IHideRule
	 */

	@Override
	public IHideRule createHideRule() {
		HideRule r = new HideRule();
		IHideRule rule = new HideRuleImpl(r);
		return rule;
	}

	/**
	 * Create <code>IFilterCondition</code>
	 *
	 * @return instance
	 */

	@Override
	public IFilterCondition createFilterCondition() {
		FilterCondition c = new FilterCondition();
		IFilterCondition condition = new FilterConditionImpl(c);
		return condition;
	}

	/**
	 * Create <code>IDataBinding</code>
	 *
	 * @return instance
	 */

	@Override
	public IDataBinding createDataBinding() {
		ComputedColumn c = new ComputedColumn();
		IDataBinding binding = new DataBindingImpl(c);
		return binding;
	}

	/**
	 * Create <code>IHighLightRule</code>
	 *
	 * @return instance
	 */

	@Override
	public IHighlightRule createHighLightRule() {
		HighlightRule h = new HighlightRule();
		IHighlightRule rule = new HighlightRuleImpl(h);
		return rule;
	}

	/**
	 * Create <code>ISortCondition</code>
	 *
	 * @return instance
	 */

	@Override
	public ISortCondition createSortCondition() {
		SortKey s = new SortKey();
		ISortCondition sort = new SortConditionImpl(s);
		return sort;
	}
}
