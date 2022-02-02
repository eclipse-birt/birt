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
package org.eclipse.birt.data.engine.impl.document;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;

public interface IRDGroupUtil {
	public void setCacheProvider(CacheProvider cacheProvider);

	public void next(boolean hasNext) throws DataException;

	public int getCurrentGroupIndex(int groupLevel) throws DataException;

	public void move() throws DataException;

	public int getEndingGroupLevel() throws DataException;

	public int getStartingGroupLevel() throws DataException;

	public void last(int groupLevel) throws DataException;

	public void close() throws DataException;

	public int[] getGroupStartAndEndIndex(int groupIndex) throws DataException;

	public List[] getGroups() throws DataException;
}
