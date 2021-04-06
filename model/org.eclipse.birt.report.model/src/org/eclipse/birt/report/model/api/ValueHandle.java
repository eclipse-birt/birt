/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * Abstract base class for value-based handles.
 */

public abstract class ValueHandle extends ElementDetailHandle {

	/**
	 * Constructs a value handle with the given element handle.
	 * 
	 * @param element a handle to a report element
	 */

	public ValueHandle(DesignElementHandle element) {
		super(element);
	}

	/**
	 * Gets the property definition. This is the definition of the property that
	 * contains the specific value. If the value is a structure or member, then this
	 * is the definition of the property that contains the list that contains the
	 * structure that contains the member.
	 * 
	 * @return the property definition
	 */

	public abstract IElementPropertyDefn getPropertyDefn();

	/**
	 * Returns a reference to the value. The reference is used to identify a list
	 * entry or member.
	 * 
	 * @return a reference to the value
	 * @deprecated removed since 2.5, replaced by {@link #getContext()}
	 */

	public MemberRef getReference() {
		return new MemberRef(getContext());
	}

	/**
	 * Returns the structure context to the value. The context is used to identify a
	 * list entry or member.
	 */
	public abstract StructureContext getContext();

}
