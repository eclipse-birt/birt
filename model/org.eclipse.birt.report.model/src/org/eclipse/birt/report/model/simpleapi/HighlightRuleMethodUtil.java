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

package org.eclipse.birt.report.model.simpleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Static CRUD HighlightRule method.
 * 
 */

public class HighlightRuleMethodUtil {

	/**
	 * Adds hidelight rule.
	 * 
	 * @param handle
	 * @param rule
	 */

	public static void addHighlightRule(DesignElementHandle handle, IHighlightRule rule) throws SemanticException {
		PropertyHandle propHandle = handle.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);

		propHandle.addItem(rule.getStructure());
	}

	/**
	 * Gets all highlightRules.
	 * 
	 * @param handle
	 * @return
	 */

	public static IHighlightRule[] getHighlightRules(DesignElementHandle handle) {
		PropertyHandle propHandle = handle.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);
		Iterator iterator = propHandle.iterator();
		List rList = new ArrayList();
		int count = 0;

		while (iterator.hasNext()) {
			HighlightRuleHandle ruleHandle = (HighlightRuleHandle) iterator.next();
			HighlightRuleImpl rule = new HighlightRuleImpl(ruleHandle);
			rList.add(rule);
			++count;
		}
		return (IHighlightRule[]) rList.toArray(new IHighlightRule[count]);
	}

	/**
	 * Removes highlight rule.
	 * 
	 * @param handle
	 * @param rule
	 * @throws SemanticException
	 */

	public static void removeHighlightRule(DesignElementHandle handle, IHighlightRule rule) throws SemanticException {
		PropertyHandle propHandle = handle.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);

		propHandle.removeItem(rule.getStructure());
	}

	/**
	 * Removes all highlight rules.
	 * 
	 * @param handle
	 * @throws SemanticException
	 */

	public static void removeHighlightRules(DesignElementHandle handle) throws SemanticException {
		PropertyHandle propHandle = handle.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);

		propHandle.clearValue();
	}
}
