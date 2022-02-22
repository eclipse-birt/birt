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
	void setCacheProvider(CacheProvider cacheProvider);

	void next(boolean hasNext) throws DataException;

	int getCurrentGroupIndex(int groupLevel) throws DataException;

	void move() throws DataException;

	int getEndingGroupLevel() throws DataException;

	int getStartingGroupLevel() throws DataException;

	void last(int groupLevel) throws DataException;

	void close() throws DataException;

	int[] getGroupStartAndEndIndex(int groupIndex) throws DataException;

	List[] getGroups() throws DataException;
}
