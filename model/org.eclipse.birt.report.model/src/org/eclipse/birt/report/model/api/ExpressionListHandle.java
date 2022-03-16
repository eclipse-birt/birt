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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ListPropertyType;

/**
 * Simplifies working with expression lists. An expression list contains
 * multiple expressions.
 *
 */

public class ExpressionListHandle extends ComplexValueHandle {

	/**
	 * Constructs an expression list handle for the structure member.
	 *
	 * @param element the design element handle
	 * @param context the memberRef for the member property
	 */

	protected ExpressionListHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
		assert context.getPropDefn().getType() instanceof ListPropertyType;
	}

	/**
	 * Constructs an expression list handle for the structure member.
	 *
	 * @param element the design element handle
	 * @param context the memberRef for the member property
	 * @deprecated
	 */

	@Deprecated
	protected ExpressionListHandle(DesignElementHandle element, MemberRef ref) {
		super(element, ref);
		assert ref.getContext() != null;
		assert ref.getPropDefn().getType() instanceof ListPropertyType;
	}

	/**
	 * Constructs an expression list handle for an element property.
	 *
	 * @param element     handle to the element that defined the property.
	 * @param thePropDefn definition of the expression property.
	 */

	protected ExpressionListHandle(DesignElementHandle element, ElementPropertyDefn thePropDefn) {
		super(element, thePropDefn);
		assert thePropDefn.getType() instanceof ListPropertyType;
	}

	/**
	 * Returns the value as an expression list. Each item in return list is an
	 * <code>Expression</code>.
	 *
	 * @return a list containing expressions
	 */

	public List<Expression> getListValue() {
		Object tmpValue = getValue();
		if (tmpValue == null) {
			return null;
		}

		List<Expression> retList = new ArrayList<>((List<Expression>) tmpValue);
		return retList;
	}

	/**
	 * Returns the value as an expression list. Each item in return list is an
	 * <code>Expression</code>.
	 *
	 * @param toSet a list containing expressions
	 * @throws SemanticException
	 */

	public void setListValue(List<Expression> toSet) throws SemanticException {
		setValue(toSet);
	}
}
