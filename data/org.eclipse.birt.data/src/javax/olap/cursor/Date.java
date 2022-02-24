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
/*
 * Java(TM) OLAP Interface
 */

package javax.olap.cursor;

public interface Date extends javax.olap.query.querycoremodel.NamedObject {

	public void date(long date) throws javax.olap.OLAPException;

	public void setTime(long date) throws javax.olap.OLAPException;

	public javax.olap.cursor.Date valueOf(java.lang.String s) throws javax.olap.OLAPException;

	public java.lang.String toString();

}
