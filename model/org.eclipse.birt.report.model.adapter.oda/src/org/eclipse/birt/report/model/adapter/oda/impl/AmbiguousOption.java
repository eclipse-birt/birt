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

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousOption;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousResultSetNode;

/**
 * Implements {@link IAmbiguousOption} to give out all the ambiguous information
 * when comparing data set design and data set handle.
 * 
 */

class AmbiguousOption implements IAmbiguousOption {

	private List<IAmbiguousParameterNode> ambiguousParameters = null;
	private List<IAmbiguousResultSetNode> ambiguousColumns = null;

	/**
	 * Default constructor.
	 */
	AmbiguousOption() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousOption#
	 * getAmbiguousParameters()
	 */
	public List<IAmbiguousParameterNode> getAmbiguousParameters() {
		if (ambiguousParameters == null)
			return Collections.emptyList();
		return ambiguousParameters;
	}

	void setAmbiguousParameters(List<IAmbiguousParameterNode> parameters) {
		this.ambiguousParameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousOption#
	 * getAmbiguousResultSets()
	 */
	public List<IAmbiguousResultSetNode> getAmbiguousResultSets() {
		if (ambiguousColumns == null)
			return Collections.emptyList();
		return ambiguousColumns;
	}

	void setAmbiguousResultSets(List<IAmbiguousResultSetNode> resultSets) {
		this.ambiguousColumns = resultSets;
	}

}
