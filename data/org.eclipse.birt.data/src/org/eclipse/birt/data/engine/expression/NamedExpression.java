/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.data.engine.expression;

import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * An expression with name
 */
public class NamedExpression {
	String name;
	IBaseExpression expression;

	public NamedExpression(String name, IBaseExpression expression) {
		if (name == null) {
			throw new NullPointerException("name is null");
		}
		this.expression = expression;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IBaseExpression getExpression() {
		return expression;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedExpression other = (NamedExpression) obj;
		return name.equals(other.getName());
	}
}
