/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.ui.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.CubeValueSelector;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.extension.IUseCubeQueryList;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * CrosstabUseCubeQueryList
 */

public class CrosstabUseCubeQueryList implements IUseCubeQueryList {

	@Override
	public List getQueryList(String expression, ExtendedItemHandle extendedItem) {
		// TODO Auto-generated method stub
		CrosstabReportItemHandle crosstab = null;
		CubeHandle cube = null;
		try {
			Object obj = ((ExtendedItemHandle) extendedItem).getReportItem();
			DesignElementHandle tmp = extendedItem;

			while (true) {
				if (obj == null || obj instanceof ReportDesignHandle) {
					break;
				} else if (obj instanceof CrosstabReportItemHandle) {
					crosstab = (CrosstabReportItemHandle) obj;
					cube = crosstab.getCube();
					break;
				} else if (tmp instanceof ExtendedItemHandle) {
					tmp = tmp.getContainer();
					if (tmp instanceof ExtendedItemHandle) {
						obj = ((ExtendedItemHandle) tmp).getReportItem();
					}
				}
			}

		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block

		}

		if (cube == null || expression.length() == 0) {
			return new ArrayList();
		}

		Iterator iter = null;

		// get cubeQueryDefn
		ICubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try {
			session = DataRequestSession.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
					extendedItem.getModuleHandle()));
			DataService.getInstance().registerSession(cube, session);
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery(crosstab);
			iter = CubeValueSelector.getMemberValueIterator(session, cube, expression, cubeQueryDefn);
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		List valueList = new ArrayList();
		int count = 0;
		int MAX_COUNT = PreferenceFactory.getInstance()
				.getPreferences(CrosstabPlugin.getDefault(), UIUtil.getCurrentProject())
				.getInt(CrosstabPlugin.PREFERENCE_FILTER_LIMIT);
		while (iter != null && iter.hasNext()) {
			Object obj = iter.next();
			if (obj != null) {
				if (valueList.indexOf(obj) < 0) {
					valueList.add(obj);
					if (++count >= MAX_COUNT) {
						break;
					}
				}

			}

		}

		if (session != null) {
			session.shutdown();
		}
		return valueList;

	}

}
