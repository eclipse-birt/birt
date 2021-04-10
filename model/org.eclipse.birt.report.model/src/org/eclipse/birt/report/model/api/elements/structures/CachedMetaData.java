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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.PropertyStructure;

/**
 * Structure used to cache data set information that include output column
 * information when it gets from databases, input/output parameter definitions.
 */

public class CachedMetaData extends PropertyStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public final static String CACHED_METADATA_STRUCT = "CachedMetaData"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameters definitions.
	 */

	public static final String PARAMETERS_MEMBER = "parameters"; //$NON-NLS-1$

	/**
	 * Member name of the cached result set(output columns).
	 */

	public final static String RESULT_SET_MEMBER = "resultSet"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getHandle(org.eclipse.birt
	 * .report.model.api.SimpleValueHandle)
	 */

	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		return new CachedMetaDataHandle(valueHandle.getElementHandle(), getContext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return CACHED_METADATA_STRUCT;
	}

}