
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

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 *
 */

public interface IDimensionResultIterator {
	/**
	 * Get dimension object
	 *
	 * @return
	 */
	IDimension getDimesion();

	/**
	 * Return the levels contained in this iterator.
	 *
	 * @return
	 */
	ILevel[] getLevels();

	/**
	 * Get level index by name.
	 *
	 * @param levelName
	 * @return
	 */
	int getLevelIndex(String levelName);

	/**
	 * there may be multi key in one level, so that the returned int should be an
	 * array.
	 *
	 * @param levelName
	 * @return
	 */
	int[] getLevelKeyDataType(String levelName);

	/**
	 *
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	int getLevelAttributeIndex(String levelName, String attributeName);

	/**
	 *
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	int getLevelAttributeDataType(String levelName, String attributeName);

	/**
	 * random access seeking.
	 *
	 * @param index
	 */
	void seek(int index);

	/**
	 *
	 * @param dimPosition
	 * @return
	 */
	boolean locate(int dimPosition) throws BirtException, IOException;

	/**
	 *
	 * @return
	 */
	int length();

	/**
	 * Each member has its index associated with it in Dimesion. The index will
	 * never be changed no matter when the filter is running against the dimesion.
	 *
	 * @since 2.1
	 * @return dimension position range of current row
	 * @throws BirtException if error occurs in Data Engine
	 * @throws IOException
	 */
	int getDimesionPosition() throws BirtException, IOException;

	// TODO should refactor to getLevelKeyValues
	/**
	 *
	 * @param levelIndex
	 * @return
	 * @throws IOException
	 */
	Object[] getLevelKeyValue(int levelIndex) throws IOException;

	/**
	 *
	 * @param levelIndex
	 * @return
	 * @throws IOException
	 */
	Member getLevelMember(int levelIndex) throws IOException;

	/**
	 *
	 * @param levelIndex
	 * @param attributeIndex
	 * @return
	 * @throws IOException
	 */
	Object getLevelAttribute(int levelIndex, int attributeIndex) throws IOException;

	/**
	 * Closes this result and any associated secondary result iterator(s), providing
	 * a hint that the consumer is done with this result, whose resources can be
	 * safely released as appropriate.
	 *
	 * @throws BirtException
	 * @throws IOException
	 */
	void close() throws BirtException, IOException;

	/**
	 *
	 * @param sortDef
	 * @throws BirtException
	 */
	IDimensionResultIterator filter(IDimensionFilterDefn filterDef) throws BirtException;

	/**
	 *
	 * @param sortDef
	 * @throws BirtException
	 */
	void sort(IDimensionSortDefn sortDef) throws BirtException;

}
