/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;

import javax.olap.cursor.CubeCursor;

public interface ICubeResultSet extends IBaseResultSet {

	CubeCursor getCubeCursor();

	String getCellIndex();

	void skipTo(String cellIndex) throws BirtException;

	IBaseCubeQueryDefinition getCubeQuery();

}
