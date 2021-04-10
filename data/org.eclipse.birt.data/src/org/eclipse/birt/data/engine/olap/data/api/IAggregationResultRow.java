/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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