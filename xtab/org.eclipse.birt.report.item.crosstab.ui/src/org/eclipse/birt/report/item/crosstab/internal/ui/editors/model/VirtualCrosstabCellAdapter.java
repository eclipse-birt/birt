/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Virtual cell adapter ,when the four area(Left conner, row area, column area,
 * measure area) has no children.
 */
public class VirtualCrosstabCellAdapter extends CrosstabCellAdapter implements IVirtualValidator {

	private CrosstabReportItemHandle crosstab;
	public static final int IMMACULATE_TYPE = -1;
	public static final int ROW_TYPE = ICrosstabConstants.ROW_AXIS_TYPE;
	public static final int COLUMN_TYPE = ICrosstabConstants.COLUMN_AXIS_TYPE;
	public static final int MEASURE_TYPE = ROW_TYPE + COLUMN_TYPE + 1;

	private int type = IMMACULATE_TYPE;

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public VirtualCrosstabCellAdapter(CrosstabCellHandle handle) {
		this(handle, IMMACULATE_TYPE);
	}

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param type
	 */
	public VirtualCrosstabCellAdapter(CrosstabCellHandle handle, int type) {
		super(null);
		if (handle != null) {
			throw new RuntimeException("Don't need create the Virtual adapter");//$NON-NLS-1$
		}

		this.type = type;
	}

	/*
	 * Virtual adapter has no children. (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.core.model.schematic.crosstab.
	 * CrosstabCellAdapter#getModelList()
	 */
	public final List getModelList() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Sets the type
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.util.IVirtualValidator#handleValidate
	 * (java.lang.Object)
	 */
	public boolean handleValidate(Object obj) {
		if (obj instanceof Object[]) {
			Object[] objects = (Object[]) obj;
			int len = objects.length;
			if (len == 0) {
				return false;
			}
			if (len == 1) {
				return handleValidate(objects[0]);
			} else {
				boolean isValidate = false;
				for (int i = 0; i < len; i++) {
					Object temp = objects[i];
					if (temp instanceof LevelAttributeHandle)
						temp = ((LevelAttributeHandle) temp).getElementHandle();
					if ((crosstab.getCube() == CrosstabAdaptUtil.getCubeHandle((DesignElementHandle) temp)
							|| crosstab.getCube() == null)) {
						isValidate = handleValidate(temp);
					} else {
						return false;
					}
				}
				return isValidate;
			}

		}
		// TODO there may be judge the dimension handle parent
		if (getType() == ICrosstabConstants.ROW_AXIS_TYPE || getType() == ICrosstabConstants.COLUMN_AXIS_TYPE) {
			if (obj instanceof DimensionHandle && CrosstabUtil.canContain(crosstab, (DimensionHandle) obj)) {
				return true;
			}
			if (obj instanceof LevelHandle) {
				return handleValidate(CrosstabAdaptUtil.getDimensionHandle((LevelHandle) obj));
			}

			if (obj instanceof LevelAttributeHandle) {
				LevelAttributeHandle lah = (LevelAttributeHandle) obj;
				LevelHandle lh = (LevelHandle) lah.getElementHandle();
				if (handleValidate(CrosstabAdaptUtil.getDimensionHandle(lh)))
					return true;

				if (getCrosstabCellHandle() != null
						&& getCrosstabCellHandle().getContainer() instanceof LevelViewHandle) {
					LevelViewHandle lvh = (LevelViewHandle) getCrosstabCellHandle().getContainer();
					if (lvh.getCubeLevel() == lh)
						return true;
				}
			}
		}
		if (getType() == MEASURE_TYPE) {
			if (obj instanceof MeasureHandle && CrosstabUtil.canContain(crosstab, (MeasureHandle) obj)) {
				return true;
			}
			if (obj instanceof MeasureGroupHandle && CrosstabUtil.canContain(crosstab, (MeasureGroupHandle) obj)
					&& (crosstab.getCube() == CrosstabAdaptUtil.getCubeHandle((DesignElementHandle) obj)
							|| crosstab.getCube() == null)) {
				return true;
			}
		}
		return false;
	}

	public void setCrosstabReportItemHandle(CrosstabReportItemHandle crosstab) {
		this.crosstab = crosstab;
	}

	public CrosstabReportItemHandle getCrosstabReportItemHandle() {
		return this.crosstab;
	}
}
