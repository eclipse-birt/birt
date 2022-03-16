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

import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * Structure handle to deal with cached data set metadata informations. The
 * information includes output column information when it gets from databases,
 * input/output parameter definitions.
 */

public class CachedMetaDataHandle extends StructureHandle {

	/**
	 * Constructors a handle given an element handle that defines the property and a
	 * member context to the metadata property.
	 *
	 * @param element an element handle that defines the property
	 * @param ref     a member reference to the data-set meta-data property.
	 */

	public CachedMetaDataHandle(DesignElementHandle element, StructureContext ref) {
		super(element, ref);
	}

	/**
	 * Constructors a handle given an element handle that defines the property and a
	 * member reference to the metadata property.
	 *
	 * @param element an element handle that defines the property
	 * @param ref     a member reference to the data-set meta-data property
	 * @deprecated
	 */

	@Deprecated
	public CachedMetaDataHandle(DesignElementHandle element, MemberRef ref) {
		super(element, ref);
	}

	/**
	 * Get a handle to deal with the cached parameter list member.
	 *
	 * @return a handle to deal with the cached parameter list member.
	 */

	public MemberHandle getParameters() {
		return getMember(CachedMetaData.PARAMETERS_MEMBER);
	}

	/**
	 * Get a handle to deal with the cached result set list member.
	 *
	 * @return a handle to deal with the cached result set list member.
	 */

	public MemberHandle getResultSet() {
		return getMember(CachedMetaData.RESULT_SET_MEMBER);
	}
}
