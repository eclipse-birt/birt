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
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Factory class which is used to return instances of IJointDataSetPopulator.
 */
public class JointDataSetPopulatorFactory {
	/**
	 * Return instance of IJointDataSetPopulator which is used for Cartesian join.
	 * 
	 * @param left
	 * @param right
	 * @param meta
	 * @param jcm
	 * @param joinType
	 * @return
	 * @throws DataException
	 */
	public static IDataSetPopulator getCartesianJointDataSetPopulator(IResultIterator left, IResultIterator right,
			JointResultMetadata meta, IJoinConditionMatcher jcm, int joinType, DataEngineSession session,
			int rowFetchLimit) throws DataException {
		IMatchResultObjectSeeker seeker = new CartesianResultObjectSeeker(jcm);
		return new BaseJointDataSetPopulator(left, right, meta, jcm, joinType, seeker, session, rowFetchLimit);
	}

	/**
	 * Return instance of IJointDataSetPopulator which is used for oridinary left,
	 * right, and inner join.
	 * 
	 * @param left
	 * @param right
	 * @param meta
	 * @param jcm
	 * @param joinType
	 * @param session
	 * @param rowFetchLimit
	 * @return
	 * @throws DataException
	 */
	public static IDataSetPopulator getBinaryTreeDataSetPopulator(IResultIterator left, IResultIterator right,
			JointResultMetadata meta, IJoinConditionMatcher jcm, int joinType, DataEngineSession session,
			int rowFetchLimit) throws DataException {
		return new BaseJointDataSetPopulator(left, right, meta, jcm, joinType, null, session, rowFetchLimit);
	}
}
