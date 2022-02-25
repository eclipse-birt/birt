/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContext;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;

/**
 *
 */

public class JSExpressionContext implements IExpressionContext {

	private IExpressionProvider provider;
	private Object contextObj;
	private Map<String, Object> extras = new HashMap<>();

	public JSExpressionContext(IExpressionProvider provider, Object contextObj) {
		this.provider = provider;
		this.contextObj = contextObj;
	}

	@Override
	public Object getContextObject() {
		return contextObj;
	}

	public IExpressionProvider getExpressionProvider() {
		return provider;
	}

	@Override
	public Object getExtra(String key) {
		return extras.get(key);
	}

	@Override
	public void putExtra(String key, Object value) {
		extras.put(key, value);
	}

}
