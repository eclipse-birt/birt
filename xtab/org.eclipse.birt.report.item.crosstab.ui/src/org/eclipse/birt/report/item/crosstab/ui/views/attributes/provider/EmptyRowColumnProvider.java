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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.swt.widgets.Control;

public class EmptyRowColumnProvider extends AbstractDescriptorProvider {

	private static final String EMPTY_COLUMN_TEXT = Messages.getString("EmptyRowColumnProvider.ColumnView.Button.Text");
	private static final String EMPTY_ROW_TEXT = Messages.getString("EmptyRowColumnProvider.RowView.Button.Text");
	private int viewType;
	protected Object input;

	public EmptyRowColumnProvider(int viewType) {
		this.viewType = viewType;
	}

	public String getDisplayName() {
		if (viewType == ICrosstabConstants.ROW_AXIS_TYPE)
			return EMPTY_ROW_TEXT;
		else
			return EMPTY_COLUMN_TEXT;
	}

	public int getMaxLengthOfDisplayName(Control control) {
		return UIUtil.getMaxStringWidth(new String[] { EMPTY_COLUMN_TEXT, EMPTY_ROW_TEXT }, control);
	}

	public Object load() {
		try {
			ExtendedItemHandle crossTabHandle = (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
			CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) crossTabHandle.getReportItem();
			CrosstabViewHandle crossTabViewHandle = crossTab.getCrosstabView(viewType);
			if (crossTabViewHandle != null) {
				return crossTabViewHandle.getMirroredStartingLevel();
			}
		} catch (ExtendedElementException e) {
		}
		return null;
	}

	public void save(Object value) throws SemanticException {
		LevelHandle handle = getLevelHandle(value);
		try {
			ExtendedItemHandle crossTabHandle = (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
			CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) crossTabHandle.getReportItem();
			CrosstabViewHandle crossTabViewHandle = crossTab.getCrosstabView(viewType);
			if (crossTabViewHandle != null) {
				crossTabViewHandle.setMirroredStartingLevel(handle);
			}
		} catch (ExtendedElementException e) {
		}
	}

	private LevelHandle getLevelHandle(Object value) {
		if (value == null)
			return null;
		else {
			Iterator iter = getViewLevels().iterator();
			while (iter.hasNext()) {
				LevelHandle level = (LevelHandle) iter.next();
				if (value.equals(level.getName()))
					return level;
			}
		}
		return null;
	}

	public List getViewLevels() {
		List list = new ArrayList();
		try {
			ExtendedItemHandle crossTabHandle = (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
			CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) crossTabHandle.getReportItem();
			CrosstabViewHandle crossTabViewHandle = crossTab.getCrosstabView(viewType);
			if (crossTabViewHandle != null) {
				int dimensionCount = crossTabViewHandle.getDimensionCount();
				for (int i = 0; i < dimensionCount; i++) {
					DimensionViewHandle dimension = crossTabViewHandle.getDimension(i);
					int levelCount = dimension.getLevelCount();
					for (int j = 0; j < levelCount; j++) {
						list.add(dimension.getLevel(j).getCubeLevel());
					}
				}
			}
		} catch (ExtendedElementException e) {
		}
		if (list.size() > 0)
			list.remove(0);
		return list;
	}

	public void setInput(Object input) {
		this.input = input;
	}

}
