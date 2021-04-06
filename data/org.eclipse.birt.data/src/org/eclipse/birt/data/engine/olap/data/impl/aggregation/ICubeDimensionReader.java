
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 * 
 */

public interface ICubeDimensionReader {
	/**
	 * 
	 * @param dimIndex
	 * @param levelIndex
	 * @param dimPos
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public Member getLevelMember(int dimIndex, int levelIndex, int dimPos) throws IOException, DataException;

	/**
	 * 
	 * @param dimIndex
	 * @param levelIndex
	 * @param dimPos
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public Member[] getLevelMembers(int dimIndex, int endLevelIndex, int dimPos) throws IOException, DataException;

	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public IDimension getDimension(String dimensionName);

	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public int getDimensionIndex(String dimensionName);

	/**
	 * 
	 * @param dimensionName
	 * @param levelIndex
	 * @return
	 */
	public int getLevelIndex(String dimensionName, String levelIndex);

	/**
	 * 
	 * @param dimensionName
	 * @param levelIndex
	 * @return
	 */
	public int getlowestLevelIndex(String dimensionName);
}
