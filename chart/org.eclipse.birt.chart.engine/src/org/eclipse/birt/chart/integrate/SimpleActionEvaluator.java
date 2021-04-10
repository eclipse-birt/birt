/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.integrate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.factory.ActionEvaluatorAdapter;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;

/**
 * Simple implementation for IActionEvaluator
 */

public class SimpleActionEvaluator extends ActionEvaluatorAdapter {

	public String[] getActionExpressions(Action action, StructureSource source) {
		if (action instanceof MultipleActions) {
			List<String> expList = new ArrayList<String>();
			for (Action subAction : ((MultipleActions) action).getActions()) {
				ActionType at = subAction.getType();
				if (at == ActionType.URL_REDIRECT_LITERAL && subAction.getValue() instanceof URLValue) {
					String sa = ((URLValue) subAction.getValue()).getBaseUrl();
					SimpleActionHandle handle = SimpleActionUtil.deserializeAction(sa);
					expList.add(handle.getURI());
				}
			}
			if (expList.size() > 0) {
				return (String[]) expList.toArray(new String[expList.size()]);
			}
		} else if (ActionType.URL_REDIRECT_LITERAL.equals(action.getType())) {
			List<String> expList = new ArrayList<String>();
			if (action.getValue() instanceof URLValue) {
				URLValue uv = (URLValue) action.getValue();

				String sa = uv.getBaseUrl();
				SimpleActionHandle handle = SimpleActionUtil.deserializeAction(sa);
				expList.add(handle.getURI());
			} else if (action.getValue() instanceof MultiURLValues) {
				for (URLValue uv : ((MultiURLValues) action.getValue()).getURLValues()) {
					String sa = uv.getBaseUrl();
					SimpleActionHandle handle = SimpleActionUtil.deserializeAction(sa);
					expList.add(handle.getURI());
				}
			}

			if (expList.size() > 0) {
				return (String[]) expList.toArray(new String[expList.size()]);
			}
		} else if (ActionType.SHOW_TOOLTIP_LITERAL.equals(action.getType())) {
			if (StructureType.SERIES.equals(source.getType())) {
				TooltipValue tv = (TooltipValue) action.getValue();

				String exp = tv.getText();
				if (exp != null && exp.trim().length() > 0) {
					return new String[] { exp };
				}
			}
		}

		return null;
	}
}
