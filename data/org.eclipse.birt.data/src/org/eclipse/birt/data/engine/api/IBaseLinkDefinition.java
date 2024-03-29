/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

import java.util.List;

public interface IBaseLinkDefinition {

	String getLeftDataSet();

	List<String> getLeftColumns();

	String getRightDataSet();

	List<String> getRightColumns();

	String getJoinType();

	void setJoinType(String joinType);

	/**
	 * Name of the join types. They should be the same as those in
	 * DesignChoiceConstants.java
	 */
	String JOIN_TYPE_INNER = "inner"; //$NON-NLS-1$
	String JOIN_TYPE_LEFT_OUT = "left-out"; //$NON-NLS-1$
	String JOIN_TYPE_RIGHT_OUT = "right-out"; //$NON-NLS-1$
	String JOIN_TYPE_FULL_OUT = "full-out"; //$NON-NLS-1$

}
