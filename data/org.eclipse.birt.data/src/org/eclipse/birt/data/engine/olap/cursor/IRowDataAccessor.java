/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

public interface IRowDataAccessor {

	public void initialize(boolean isPage) throws IOException;

	public IAggregationResultSet getAggregationResultSet();

	public RowDataAccessorService getRowDataAccessorService();

	public boolean dim_next(int dimAxisIndex) throws OLAPException;

	public boolean dim_previous(int dimAxisIndex) throws OLAPException;

	public boolean dim_relative(int offset, int dimAxisIndex) throws OLAPException;

	public boolean dim_first(int dimAxisIndex);

	public boolean dim_last(int dimAxisIndex);

	public boolean dim_isBeforeFirst(int dimAxisIndex);

	public boolean dim_isAfterLast(int dimAxisIndex);

	public boolean dim_isFirst(int dimAxisIndex);

	public boolean dim_isLast(int dimAxisIndex);

	public void dim_afterLast(int dimAxisIndex);

	public void dim_beforeFirst(int dimAxisIndex);

	public void dim_setPosition(int dimAxisIndex, long position);

	public long dim_getPosition(int dimAxisIndex);

	public Object dim_getCurrentMember(int dimAxisIndex, int attr) throws OLAPException;

	public Object dim_getCurrentMember(int dimAxisIndex, String attrName) throws OLAPException;

	public void edge_afterLast();

	public void edge_beforeFirst();

	public boolean edge_first();

	public long getEdgePostion();

	public boolean edge_isAfterLast();

	public boolean edge_isBeforeFirst();

	public boolean edge_isFirst();

	public boolean edge_isLast();

	public boolean edge_last();

	public boolean edge_next() throws OLAPException;

	public boolean edge_previous() throws OLAPException;

	public boolean edge_relative(int arg0) throws OLAPException;

	public void edge_setPostion(long position) throws OLAPException;

	public int getEdgeStart(int dimAxisIndex);

	public int getEdgeEnd(int dimAxisIndex);

	public int getExtend(int dimAxisIndex);

	public void sychronizedWithPage(int position);

}
