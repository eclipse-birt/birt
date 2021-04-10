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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;

/**
 * Implements the interface {@link IAmbiguousParameterNode}. It defines the oda
 * data set parameter handle and a list of <code>IAmbiguousAttribute</code>.
 * 
 */
class AmbiguousParameterNode implements IAmbiguousParameterNode {

	private final OdaDataSetParameterHandle paramHandle;
	private final List<IAmbiguousAttribute> attributes;

	AmbiguousParameterNode(OdaDataSetParameterHandle paramHandle, List<IAmbiguousAttribute> attributes) {
		if (paramHandle == null)
			throw new IllegalArgumentException(
					"The oda data set parameter can not be null when creating AmbiguousParameterNode!"); //$NON-NLS-1$
		this.paramHandle = paramHandle;
		if (attributes == null)
			this.attributes = Collections.emptyList();
		else
			this.attributes = attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode#
	 * getOdaDataSetParameterHandle()
	 */
	public OdaDataSetParameterHandle getOdaDataSetParameterHandle() {
		return this.paramHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode#
	 * getAmbiguousAttributes()
	 */
	public List<IAmbiguousAttribute> getAmbiguousAttributes() {
		return this.attributes;
	}
}
