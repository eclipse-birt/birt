/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.DataGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.elements.interfaces.IDataGroupModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;

/**
 * AbstractGroupStructureProvider
 */
public abstract class AbstractGroupStructureProvider implements IGroupStructureProvider {

	private String groupPropertyName;

	protected AbstractGroupStructureProvider(String groupPropertyName) {
		this.groupPropertyName = groupPropertyName;
	}

	@Override
	public List<GroupHandle> getGroups(DesignElementHandle handle) {
		if (handle != null) {
			PropertyHandle propHandle = handle.getPropertyHandle(groupPropertyName);

			if (propHandle != null) {
				List glist = propHandle.getContents();

				if (glist.size() > 0) {
					List<GroupHandle> clist = new ArrayList<GroupHandle>();

					ElementFactory factory = new ElementFactory(handle.getModule());

					for (Object dg : glist) {
						if (dg instanceof GroupHandle) {
							// add to list directly
							clist.add((GroupHandle) dg);
						} else if (dg instanceof DataGroupHandle) {
							// convert DataGroupHandle to GroupHandle
							GroupHandle cg = convertGroup(factory, (DataGroupHandle) dg);

							if (cg != null) {
								clist.add(cg);
							}
						} else {
							// we dont' know how to convert it
						}
					}

					return clist;
				}
			}
		}
		return Collections.emptyList();
	}

	private GroupHandle convertGroup(ElementFactory factory, DataGroupHandle dataGroup) {
		try {
			TableGroupHandle group = factory.newTableGroup();

			group.setName(dataGroup.getGroupName());
			group.setExpressionProperty(IGroupElementModel.KEY_EXPR_PROP,
					(Expression) dataGroup.getExpressionProperty(IDataGroupModel.KEY_EXPR_PROP).getValue());
			group.setInterval(dataGroup.getInterval());
			group.setIntervalBase(dataGroup.getIntervalBase());
			group.setIntervalRange(dataGroup.getIntervalRange());

			group.setSortDirection(dataGroup.getSortDirection());

			// TODO copy sort/filter definitions and other properties, so far
			// only the properties above are used, the main purpose is to
			// populate the value list from UI.

			return group;
		} catch (BirtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
