/*******************************************************************************
 * Copyright (c) 2026 Eclipse contributors and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *  
 *******************************************************************************/
package org.eclipse.birt.report.viewer.jasper9.fragment;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * This class is not a real implementation, just a named wrapper to make it
 * possible to name the exact implementation for ServiceLoader
 */

public class ExpressionFactoryWrapper extends ExpressionFactory {

	@Override
	public ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType) {
		return null;
	}

	@Override
	public ValueExpression createValueExpression(Object instance, Class<?> expectedType) {
		return null;
	}

	@Override
	public MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType,
			Class<?>[] expectedParamTypes) {
		return null;
	}

	@Override
	public Object coerceToType(Object obj, Class<?> expectedType) {
		return null;
	}

}
