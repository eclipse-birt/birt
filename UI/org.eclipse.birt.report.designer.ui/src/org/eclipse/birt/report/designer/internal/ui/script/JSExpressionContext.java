/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	private Map<String, Object> extras = new HashMap<String, Object>();

	public JSExpressionContext(IExpressionProvider provider, Object contextObj) {
		this.provider = provider;
		this.contextObj = contextObj;
	}

	public Object getContextObject() {
		return contextObj;
	}

	public IExpressionProvider getExpressionProvider() {
		return provider;
	}

	public Object getExtra(String key) {
		return extras.get(key);
	}

	public void putExtra(String key, Object value) {
		extras.put(key, value);
	}

}
