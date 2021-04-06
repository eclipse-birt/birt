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

package org.eclipse.birt.report.model.adapter.oda;

import java.util.List;

/**
 * Interface to give out all the ambiguous information when comparing data set
 * design and data set handle.
 * 
 */

public interface IAmbiguousOption {

	/**
	 * Gets all the ambiguous parameter nodes when comparing data set design and
	 * data set handle.
	 * 
	 * @return
	 */
	List<IAmbiguousParameterNode> getAmbiguousParameters();

	/**
	 * 
	 * @return
	 */
	List<IAmbiguousResultSetNode> getAmbiguousResultSets();
}
