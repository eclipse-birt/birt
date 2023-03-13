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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.OdaLevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularHierarchyModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularLevelModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.OdaLevel;
import org.eclipse.birt.report.model.elements.olap.TabularCube;
import org.eclipse.birt.report.model.elements.olap.TabularHierarchy;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Abstract level state for all OLAP level parser.
 */
abstract public class LevelState extends ReportElementState {

	/**
	 * Constructs level state with the design parser handler, the container element
	 * and the container property name of the report element.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public LevelState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */
	@Override
	public void end() throws SAXException {
		super.end();

		// to do the backward compatibility, we cached all level elements and
		// helps to resolve
		if (handler.versionNumber < VersionUtil.VERSION_3_2_13) {
			((ModuleNameHelper) handler.module.getNameHelper()).addCachedLevel(getElement());
		}

		if (handler.versionNumber < VersionUtil.VERSION_3_2_23
				&& getElement().getProperty(handler.module, ILevelModel.DATE_TIME_LEVEL_TYPE) != null) {
			boolean isFound = false;
			ElementPropertyDefn attributesPropertyDefn = getElement().getPropertyDefn(ILevelModel.ATTRIBUTES_PROP);
			List attrs = (List) getElement().getProperty(handler.module, attributesPropertyDefn);
			if (attrs != null) {
				for (int i = 0; i < attrs.size(); i++) {
					LevelAttribute attr = (LevelAttribute) attrs.get(i);
					if (LevelAttribute.DATE_TIME_ATTRIBUTE_NAME.equals(attr.getName())) {
						isFound = true;
						break;
					}
				}
			}

			if (!isFound) {
				Structure struct = null;
				if (getElement() instanceof TabularLevel) {
					LevelAttribute attribute = new LevelAttribute();
					attribute.setName(LevelAttribute.DATE_TIME_ATTRIBUTE_NAME);
					attribute.setDataType(getDataType((TabularLevel) this.getElement()));
					struct = attribute;
				} else if (getElement() instanceof OdaLevel) {
					OdaLevelAttribute attribute = new OdaLevelAttribute();
					attribute.setName(LevelAttribute.DATE_TIME_ATTRIBUTE_NAME);
					attribute.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME);
					struct = attribute;
				}
				if (attributesPropertyDefn != null && struct != null) {
					ArrayList list = new ArrayList();
					if (attrs != null) {
						list.addAll(attrs);
					}
					list.add(struct);
					getElement().setProperty(attributesPropertyDefn, list);
				}
			}
		}
	}

	/**
	 * Gets the data type of the level
	 *
	 * @return the data type
	 */
	private String getDataType(TabularLevel level) {
		String columnName = level.getStringProperty(handler.module, ITabularLevelModel.COLUMN_NAME_PROP);
		if (!StringUtil.isBlank(columnName)) {
			DesignElement container = getElement().getContainer();
			DataSet dataSet = null;
			if (container instanceof TabularHierarchy) {
				dataSet = (DataSet) container.getReferenceProperty(handler.module,
						ITabularHierarchyModel.DATA_SET_PROP);
			}
			if (dataSet == null && container != null) {
				container = container.getContainer();
				if (container instanceof Dimension) {
					container = container.getContainer();
				}
				if (container instanceof TabularCube) {
					dataSet = (DataSet) container.getReferenceProperty(handler.module, ITabularCubeModel.DATA_SET_PROP);
				}
			}
			if (dataSet != null) {
				CachedMetaData metaData = (CachedMetaData) dataSet.getProperty(handler.module,
						IDataSetModel.CACHED_METADATA_PROP);
				if (metaData != null) {
					List<ResultSetColumn> resultSet = (List<ResultSetColumn>) metaData.getProperty(handler.module,
							CachedMetaData.RESULT_SET_MEMBER);
					if (resultSet != null) {
						for (ResultSetColumn column : resultSet) {
							if (columnName
									.equals(column.getStringProperty(handler.module, ResultSetColumn.NAME_MEMBER))) {
								String dataType = column.getStringProperty(handler.module,
										ResultSetColumn.DATA_TYPE_MEMBER);
								if (dataType != null) {
									return dataType;
								}
								break;
							}
						}
					}
				}
			}
		}
		return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
	}

}
