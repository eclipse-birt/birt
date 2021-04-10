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

package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;

/**
 * 
 */

public class DimensionFactory {
	// TODO : to refactor to use different name between dimension name and hierarcy
	// name.
	public static IDimension createDimension(String name, IDocumentManager documentManager, IDatasetIterator iterator,
			ILevelDefn[] levelDefs, boolean isTime, StopSign stopSign) throws IOException, BirtException {
		Hierarchy hierarchy = new Hierarchy(documentManager, name, name);
		hierarchy.createAndSaveHierarchy(iterator, levelDefs, stopSign);
		return new Dimension(name, documentManager, hierarchy, isTime);
	}

	public static IDimension loadDimension(String name, IDocumentManager documentManager)
			throws DataException, IOException {
		return new Dimension(name, documentManager);
	}

	public static IDimension loadDimension(String name, IDocumentManager documentManager,
			Set<String> notAccessibleLevels) throws DataException, IOException {
		return new SecuredDimension(name, documentManager, notAccessibleLevels);
	}

	public static IDimension createTimeDimension(String name, IDatasetIterator iterator, String levelColumnName,
			int[] timeDimTypes) {
		return null;
	}

}
