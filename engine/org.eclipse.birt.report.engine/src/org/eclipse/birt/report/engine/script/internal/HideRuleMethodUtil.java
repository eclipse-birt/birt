/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.script.internal.element.HideRuleImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Static CRUD hiderule method.
 * 
 */
public class HideRuleMethodUtil {

	/**
	 * Gets all hide rules.
	 * 
	 * @param handle
	 * @return hide rules
	 */

	public static IHideRule[] getHideRules(DesignElementHandle handle) {
		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);
		Iterator iterator = propHandle.iterator();
		List rList = new ArrayList();
		int count = 0;

		while (iterator.hasNext()) {
			HideRuleHandle ruleHandle = (HideRuleHandle) iterator.next();
			HideRuleImpl rule = new HideRuleImpl(ruleHandle);
			rList.add(rule);
			++count;
		}
		return (IHideRule[]) rList.toArray(new IHideRule[count]);
	}

	/**
	 * Removes Hide Rule through format type.
	 * 
	 * @param handle
	 * @param rule
	 */

	public static void removeHideRule(DesignElementHandle handle, IHideRule rule) throws ScriptException {
		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);
		try {
			propHandle.removeItem(rule.getStructure());
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Add HideRule.
	 * 
	 * @param handle
	 * @param rule
	 * @throws ScriptException
	 */

	public static void addHideRule(DesignElementHandle handle, IHideRule rule) throws ScriptException {
		if (rule == null)
			return;

		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);
		try {
			propHandle.addItem(rule.getStructure());
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Removes Hide Rules.
	 * 
	 * @param handle
	 */

	public static void removeHideRules(DesignElementHandle handle) throws ScriptException {
		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);
		try {
			propHandle.clearValue();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}
}
