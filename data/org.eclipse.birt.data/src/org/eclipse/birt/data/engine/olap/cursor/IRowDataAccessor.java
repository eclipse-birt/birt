/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

public interface IRowDataAccessor {

	void initialize(boolean isPage) throws IOException;

	IAggregationResultSet getAggregationResultSet();

	RowDataAccessorService getRowDataAccessorService();

	boolean dim_next(int dimAxisIndex) throws OLAPException;

	boolean dim_previous(int dimAxisIndex) throws OLAPException;

	boolean dim_relative(int offset, int dimAxisIndex) throws OLAPException;

	boolean dim_first(int dimAxisIndex);

	boolean dim_last(int dimAxisIndex);

	boolean dim_isBeforeFirst(int dimAxisIndex);

	boolean dim_isAfterLast(int dimAxisIndex);

	boolean dim_isFirst(int dimAxisIndex);

	boolean dim_isLast(int dimAxisIndex);

	void dim_afterLast(int dimAxisIndex);

	void dim_beforeFirst(int dimAxisIndex);

	void dim_setPosition(int dimAxisIndex, long position);

	long dim_getPosition(int dimAxisIndex);

	Object dim_getCurrentMember(int dimAxisIndex, int attr) throws OLAPException;

	Object dim_getCurrentMember(int dimAxisIndex, String attrName) throws OLAPException;

	void edge_afterLast();

	void edge_beforeFirst();

	boolean edge_first();

	long getEdgePostion();

	boolean edge_isAfterLast();

	boolean edge_isBeforeFirst();

	boolean edge_isFirst();

	boolean edge_isLast();

	boolean edge_last();

	boolean edge_next() throws OLAPException;

	boolean edge_previous() throws OLAPException;

	boolean edge_relative(int arg0) throws OLAPException;

	void edge_setPostion(long position) throws OLAPException;

	int getEdgeStart(int dimAxisIndex);

	int getEdgeEnd(int dimAxisIndex);

	int getExtend(int dimAxisIndex);

	void sychronizedWithPage(int position);

}
