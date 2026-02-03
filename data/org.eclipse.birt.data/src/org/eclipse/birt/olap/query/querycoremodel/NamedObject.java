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

package org.eclipse.birt.olap.query.querycoremodel;

public interface NamedObject {

	java.lang.String getName() throws org.eclipse.birt.olap.OLAPException;

	void setName(java.lang.String value) throws org.eclipse.birt.olap.OLAPException;

	java.lang.String getId() throws org.eclipse.birt.olap.OLAPException;

	void setId(java.lang.String value) throws org.eclipse.birt.olap.OLAPException;

}
