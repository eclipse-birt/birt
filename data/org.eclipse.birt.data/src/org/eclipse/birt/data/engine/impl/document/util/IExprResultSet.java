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

package org.eclipse.birt.data.engine.impl.document.util;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Read the expression value from report document.
 */
public interface IExprResultSet
{

	/**
	 * @return
	 * @throws DataException
	 */
	public boolean next( ) throws DataException;

	/**
	 * @param name
	 * @return
	 * @throws DataException
	 */
	public Object getValue( String name ) throws DataException;

	/**
	 * @param rowIndex
	 */
	public void moveTo( int rowIndex ) throws DataException;

	/**
	 * @return
	 */
	public int getCurrentId( );

	/**
	 * @return
	 */
	public int getCurrentIndex( );

	/**
	 * @return
	 * @throws DataException
	 */
	public int getStartingGroupLevel( ) throws DataException;

	/**
	 * @return
	 * @throws DataException
	 */
	public int getEndingGroupLevel( ) throws DataException;

	/**
	 * @param groupLevel
	 * @throws DataException
	 */
	public void skipToEnd( int groupLevel ) throws DataException;

	/**
	 * @throws DataException
	 */
	public void close( ) throws DataException;

}