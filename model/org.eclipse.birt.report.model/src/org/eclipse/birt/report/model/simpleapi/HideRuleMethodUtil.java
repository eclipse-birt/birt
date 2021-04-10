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

package org.eclipse.birt.report.model.simpleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
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

	public static void removeHideRule(DesignElementHandle handle, IHideRule rule) throws SemanticException {
		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);

		propHandle.removeItem(rule.getStructure());
	}

	/**
	 * Add HideRule.
	 * 
	 * @param handle
	 * @param rule
	 * @throws SemanticException
	 */

	public static void addHideRule(DesignElementHandle handle, IHideRule rule) throws SemanticException {
		if (rule == null)
			return;

		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);

		propHandle.addItem(rule.getStructure());
	}

	/**
	 * Removes Hide Rules.
	 * 
	 * @param handle
	 */

	public static void removeHideRules(DesignElementHandle handle) throws SemanticException {
		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);

		propHandle.clearValue();

	}
}
