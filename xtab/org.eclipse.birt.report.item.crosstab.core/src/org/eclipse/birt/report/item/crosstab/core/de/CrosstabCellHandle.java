/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.command.PropertyRecord;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * DimensionViewHandle.
 */
public class CrosstabCellHandle extends AbstractCrosstabItemHandle
		implements ICrosstabCellConstants, ICrosstabConstants {

	/**
	 *
	 * @param handle
	 */
	protected CrosstabCellHandle(DesignElementHandle handle) {
		super(handle);
	}

	/**
	 * Gets the content slot handle of crosstab cell.
	 *
	 * @return the content slot handle
	 */
	PropertyHandle getContentProperty() {
		return handle.getPropertyHandle(CONTENT_PROP);
	}

	/**
	 * Returns an unmodifiable list of model handles for contents in this cell.
	 *
	 * @return the model handles of the cell content
	 */
	public List getContents() {
		return Collections.unmodifiableList(getContentProperty().getContents());
	}

	/**
	 * Adds content to the last position for cell contents.
	 *
	 * @param content
	 * @throws SemanticException
	 */
	public void addContent(DesignElementHandle content) throws SemanticException {
		PropertyHandle ph = getContentProperty();

		if (ph != null) {
			ph.add(content);
		}
	}

	/**
	 * Adds content to given position for cell contents.
	 *
	 * @param content
	 * @param newPos
	 * @throws SemanticException
	 */
	public void addContent(DesignElementHandle content, int newPos) throws SemanticException {
		PropertyHandle ph = getContentProperty();

		if (ph != null) {
			ph.add(content, newPos);
		}
	}

	/**
	 * Gets the dimension value handle for the cell width.
	 *
	 * @return cell width dimension value handle
	 */
	public DimensionHandle getWidth() {
		return handle.getDimensionProperty(IInternalReportItemModel.WIDTH_PROP);
	}

	/**
	 * Gets the dimension value handle for the cell height.
	 *
	 * @return cell height dimension value handle
	 */
	public DimensionHandle getHeight() {
		return handle.getDimensionProperty(IInternalReportItemModel.HEIGHT_PROP);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getPredefinedStyles ()
	 */
	@Override
	public List<String> getPredefinedStyles() {
		AbstractCrosstabItemHandle container = getContainer();
		if (container == null) {
			return Collections.emptyList();
		}

		List<String> styles = new ArrayList<>();

		if (container instanceof MeasureViewHandle) {
			// only cells in measure detail and aggregations are looked as
			// "x-tab-detail-cell"
			String propName = handle.getContainerPropertyHandle().getDefn().getName();
			if (IMeasureViewConstants.DETAIL_PROP.equals(propName)
					|| IMeasureViewConstants.AGGREGATIONS_PROP.equals(propName)) {
				styles.add(CROSSTAB_DETAIL_SELECTOR);
			} else {
				// measure header cell is looked as x-tab-header-cell
				styles.add(CROSSTAB_HEADER_SELECTOR);
			}
		} else if (container instanceof LevelViewHandle) {
			// if this cell lies in level view, then determine it is row level
			// or column level
			LevelViewHandle levelView = (LevelViewHandle) container;
			int axisType = levelView.getAxisType();
			switch (axisType) {
			case ICrosstabConstants.ROW_AXIS_TYPE:
				styles.add(CROSSTAB_ROW_HEADER_SELECTOR);
				break;
			case ICrosstabConstants.COLUMN_AXIS_TYPE:
				styles.add(CROSSTAB_COLUMN_HEADER_SELECTOR);
				break;
			default:
				break;
			}

			styles.add(CROSSTAB_HEADER_SELECTOR);

		} else {
			// all other cells in x-tab is looked as "x-tab-header-cell"
			styles.add(CROSSTAB_HEADER_SELECTOR);
		}

		// all cells apply to the "crosstab-cell" selector
		styles.add(CROSSTAB_CELL_SELECTOR);

		return styles;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.ReportItem#getProperty(java
	 * .lang.String)
	 */
	@Override
	public Object getProperty(String propName) {
		IPropertyDefn defn = handle.getPropertyDefn(propName);
		if (defn == null) {
			return null;
		}
		assert ((ElementPropertyDefn) defn).isStyleProperty();

		FactoryPropertyHandle factoryHandle = handle.getFactoryPropertyHandle(propName);
		Object value = factoryHandle == null ? null : factoryHandle.getValue();
		if (value != null) {
			return value;
		}

		DesignElementHandle crosstab = getCrosstabHandle();
		return crosstab == null ? null : crosstab.getProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.ReportItem#setProperty(java
	 * .lang.String, java.lang.Object)
	 */
	@Override
	public void setProperty(String propName, Object value) {
		ElementPropertyDefn defn = (ElementPropertyDefn) handle.getPropertyDefn(propName);
		if (defn != null && defn.isStyleProperty() && defn.canInherit()) {
			PropertyRecord record = new PropertyRecord(handle.getElement(), defn, value);
			record.setEventTarget(null);

			getCommandStack().execute(record);

		}
	}

	/**
	 * Set the cross table cell width based on unit string without locale handling
	 *
	 * @param width cross table cell width
	 *
	 * @throws SemanticException
	 */
	public void setWidth(String width) throws SemanticException {
		handle.setProperty(IInternalReportItemModel.WIDTH_PROP, StringUtil.parse(width));
	}

	/**
	 * Set the cross table cell width based on dimension value
	 *
	 * @param width cross table cell width
	 *
	 * @throws SemanticException
	 */
	public void setWidth(DimensionValue width) throws SemanticException {
		handle.setProperty(IInternalReportItemModel.WIDTH_PROP, width);
	}

	/**
	 * Set the cross table cell height based on unit string without locale handling
	 *
	 * @param height cross table cell height
	 *
	 * @throws SemanticException
	 */
	public void setHeight(String height) throws SemanticException {
		handle.setProperty(IInternalReportItemModel.HEIGHT_PROP, StringUtil.parse(height));
	}

	/**
	 * Set the cross table cell height based on dimension value
	 *
	 * @param height cross table cell height
	 *
	 * @throws SemanticException
	 */
	public void setHeight(DimensionValue height) throws SemanticException {
		handle.setProperty(IInternalReportItemModel.HEIGHT_PROP, height);
	}

}
