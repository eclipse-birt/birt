/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.api;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;

/**
 * 
 */

public interface IAggregationResultRow extends IComparableStructure {

	public abstract void setLevelMembers(Member[] levelMembers);

	public abstract Member[] getLevelMembers();

	public abstract void setAggregationValues(Object[] aggregationValues);

	public abstract Object[] getAggregationValues();

}
