
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
	public IDimension getDimesion();

	/**
	 * Return the levels contained in this iterator.
	 * 
	 * @return
	 */
	public ILevel[] getLevels();

	/**
	 * Get level index by name.
	 * 
	 * @param levelName
	 * @return
	 */
	public int getLevelIndex(String levelName);

	/**
	 * there may be multi key in one level, so that the returned int should be an
	 * array.
	 * 
	 * @param levelName
	 * @return
	 */
	public int[] getLevelKeyDataType(String levelName);

	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeIndex(String levelName, String attributeName);

	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeDataType(String levelName, String attributeName);

	/**
	 * random access seeking.
	 * 
	 * @param index
	 */
	public void seek(int index);

	/**
	 * 
	 * @param dimPosition
	 * @return
	 */
	public boolean locate(int dimPosition) throws BirtException, IOException;

	/**
	 * 
	 * @return
	 */
	public int length();

	/**
	 * Each member has its index associated with it in Dimesion. The index will
	 * never be changed no matter when the filter is running against the dimesion.
	 * 
	 * @since 2.1
	 * @return dimension position range of current row
	 * @throws BirtException if error occurs in Data Engine
	 * @throws IOException
	 */
	public int getDimesionPosition() throws BirtException, IOException;

	// TODO should refactor to getLevelKeyValues
	/**
	 * 
	 * @param levelIndex
	 * @return
	 * @throws IOException
	 */
	public Object[] getLevelKeyValue(int levelIndex) throws IOException;

	/**
	 * 
	 * @param levelIndex
	 * @return
	 * @throws IOException
	 */
	public Member getLevelMember(int levelIndex) throws IOException;

	/**
	 * 
	 * @param levelIndex
	 * @param attributeIndex
	 * @return
	 * @throws IOException
	 */
	public Object getLevelAttribute(int levelIndex, int attributeIndex) throws IOException;

	/**
	 * Closes this result and any associated secondary result iterator(s), providing
	 * a hint that the consumer is done with this result, whose resources can be
	 * safely released as appropriate.
	 * 
	 * @throws BirtException
	 * @throws IOException
	 */
	public void close() throws BirtException, IOException;

	/**
	 * 
	 * @param sortDef
	 * @throws BirtException
	 */
	public IDimensionResultIterator filter(IDimensionFilterDefn filterDef) throws BirtException;

	/**
	 * 
	 * @param sortDef
	 * @throws BirtException
	 */
	public void sort(IDimensionSortDefn sortDef) throws BirtException;

}
