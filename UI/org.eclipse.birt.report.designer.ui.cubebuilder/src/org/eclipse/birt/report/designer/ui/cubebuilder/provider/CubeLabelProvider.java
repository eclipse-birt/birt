/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

/**
 *
 * Tree viewer label provider adapter for resource browser.
 */

public class CubeLabelProvider extends LabelProvider {

	private static final Image IMG_DATASOURCE = ReportPlatformUIImages
			.getImage(IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SOURCE);

	private static final Image IMG_DATASET = ReportPlatformUIImages
			.getImage(IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SET);

	private static final Image IMG_DATAFIELD = ReportPlatformUIImages
			.getImage(IReportGraphicConstants.ICON_DATA_COLUMN);

	private static final Image IMG_DATAFIELD_USED = UIHelper.getImage(BuilderConstants.IMAGE_COLUMN_USED);

	private static final Image IMG_CUBE = UIHelper.getImage(BuilderConstants.IMAGE_CUBE);

	private static final Image IMG_DIMENSION = UIHelper.getImage(BuilderConstants.IMAGE_DIMENSION);

	private static final Image IMG_DIMENSION_FOLDER = UIHelper.getImage(BuilderConstants.IMAGE_DIMENSION_FOLDER);

	private static final Image IMG_MEASUREGROUP_FOLDER = UIHelper.getImage(BuilderConstants.IMAGE_MEASUREGROUP_FOLDER);

	private static final Image IMG_MEASURE = UIHelper.getImage(BuilderConstants.IMAGE_MEASUREGROUP);

	private static final Image IMG_MEASUREGROUP = UIHelper.getImage(BuilderConstants.IMAGE_MEASUREGROUP);

	private static final Image IMG_DERIVED_MEASURE = UIHelper.getImage(BuilderConstants.IMAGE_DERIVED_MEASURE);

	private static final Image IMG_LEVEL = UIHelper.getImage(BuilderConstants.IMAGE_LEVEL);

	private static final Image IMG_OTHER_DATASETS = UIHelper.getImage(BuilderConstants.IMAGE_OTHER_DATASETS);

	private TabularCubeHandle input;

	public void setInput(TabularCubeHandle input) {
		this.input = input;
	}

	private boolean isDataViewer = false;

	public void setProivderViewer(boolean isDataViewer) {
		this.isDataViewer = isDataViewer;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof DesignElementHandle && ((DesignElementHandle) element).getSemanticErrors().size() > 0) {
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		} else if (element instanceof DataSourceHandle) {
			return IMG_DATASOURCE;
		} else if (element instanceof DataSetHandle) {
			return IMG_DATASET;
		} else if (element instanceof VirtualField
				&& ((VirtualField) element).getType().equals(VirtualField.TYPE_OTHER_DATASETS)) {
			return IMG_OTHER_DATASETS;
		} else if (element instanceof VirtualField
				&& ((VirtualField) element).getType().equals(VirtualField.TYPE_SHARED_DIMENSIONS)) {
			return IMG_DIMENSION_FOLDER;
		} else if (element instanceof ResultSetColumnHandle) {
			Map<String, List<String>> columnMap = getColumnMap();
			ResultSetColumnHandle column = (ResultSetColumnHandle) element;
			String datasetName = ((DataSetHandle) column.getElementHandle()).getName();
			String columnName = column.getColumnName();
			if (columnMap.containsKey(datasetName) && columnMap.get(datasetName).contains(columnName)) {
				return IMG_DATAFIELD_USED;
			}
			return IMG_DATAFIELD;
		} else if (element instanceof DimensionHandle) {
			return IMG_DIMENSION;
		} else if (element instanceof LevelHandle) {
			if (isDataViewer) {
				List<DimensionHandle> dimensions = getSharedDimensionHandles();
				if (dimensions.contains(((LevelHandle) element).getContainer().getContainer())) {
					return IMG_DATAFIELD_USED;
				}
			}
			return IMG_LEVEL;
		} else if (element instanceof CubeHandle) {
			return IMG_CUBE;
		} else if (element instanceof MeasureHandle) {
			return ((MeasureHandle) element).isCalculated() ? IMG_DERIVED_MEASURE : IMG_MEASURE;
		} else if (element instanceof MeasureGroupHandle) {
			return IMG_MEASUREGROUP;
		} else if (element instanceof String) {
			return IMG_DATAFIELD;
		} else if (element instanceof PropertyHandle) {
			PropertyHandle model = (PropertyHandle) element;
			if (model.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP)) {
				return IMG_DIMENSION_FOLDER;
			}
			if (model.getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP)) {
				return IMG_MEASUREGROUP_FOLDER;
			}
		}
		return super.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof DataSetHandle) {
			if (input != null && input.getDataSet() != null && input.getDataSet() == element) {
				return ((DataSetHandle) element).getName() + " " //$NON-NLS-1$
						+ Messages.getString("GroupsPage.Primary.Dataset"); //$NON-NLS-1$
			} else {
				return ((DataSetHandle) element).getName();
			}
		}
		if (element instanceof VirtualField
				&& ((VirtualField) element).getType().equals(VirtualField.TYPE_OTHER_DATASETS)) {
			return Messages.getString("Cube.Other.Datasets"); //$NON-NLS-1$
		}
		if (element instanceof VirtualField
				&& ((VirtualField) element).getType().equals(VirtualField.TYPE_SHARED_DIMENSIONS)) {
			return Messages.getString("CubeLabelProvider.SharedDimensions"); //$NON-NLS-1$
		} else if (element instanceof ResultSetColumnHandle) {
			return OlapUtil.getDataFieldDisplayName((ResultSetColumnHandle) element);
		} else if (element instanceof DimensionHandle) {
			return ((DimensionHandle) element).getName();
		} else if (element instanceof LevelHandle) {
			return ((LevelHandle) element).getName();
		} else if (element instanceof CubeHandle) {
			return ((CubeHandle) element).getName();
		} else if (element instanceof MeasureGroupHandle) {
			return ((MeasureGroupHandle) element).getName();
		} else if (element instanceof MeasureHandle) {
			String name = ((MeasureHandle) element).getDisplayName();
			if (name == null || name.trim().length() == 0) {
				name = ((MeasureHandle) element).getName();
			}
			try {
				if (((MeasureHandle) element).isCalculated()) {
					return name;
				} else {
					return name + "(" //$NON-NLS-1$
							+ DataUtil.getAggregationManager()
									.getAggregation(DataAdapterUtil
											.adaptModelAggregationType(((MeasureHandle) element).getFunction()))
									.getDisplayName()
							+ ")"; //$NON-NLS-1$
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ""; //$NON-NLS-1$
			}
		} else if (element instanceof String) {
			return (String) element;
		} else if (element instanceof PropertyHandle) {
			PropertyHandle model = (PropertyHandle) element;
			if (model.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP)) {
				return Messages.getString("Cube.Groups"); //$NON-NLS-1$
			} else if (model.getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP)) {
				return Messages.getString("Cube.MeasureGroup"); //$NON-NLS-1$
			}
		}
		return super.getText(element);
	}

	/**
	 *
	 * @return the absolute path of resource folder
	 */
	public String getToolTip(Object element) {
		return getText(element);
	}

	private List<DimensionHandle> getSharedDimensionHandles() {

		List<DimensionHandle> dimensions = new ArrayList<>();
		List list = input.getContents(CubeHandle.DIMENSIONS_PROP);
		for (int i = 0; i < list.size(); i++) {
			DimensionHandle dimension = (DimensionHandle) list.get(i);
			if (dimension instanceof TabularDimensionHandle
					&& ((TabularDimensionHandle) dimension).getSharedDimension() != null) {
				dimension = ((TabularDimensionHandle) dimension).getSharedDimension();
				dimensions.add(dimension);
			}
		}
		return dimensions;
	}

	protected Map<String, List<String>> getColumnMap() {
		Map<String, List<String>> columnMap = new HashMap<>();

		if (input == null) {
			return columnMap;
		}

		List list = input.getContents(CubeHandle.DIMENSIONS_PROP);
		for (int i = 0; i < list.size(); i++) {
			DimensionHandle dimension = (DimensionHandle) list.get(i);
			if (dimension instanceof TabularDimensionHandle
					&& ((TabularDimensionHandle) dimension).getSharedDimension() != null) {
				dimension = ((TabularDimensionHandle) dimension).getSharedDimension();
			}
			TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
					.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
			if (hierarchy != null && hierarchy.getLevelCount() > 0) {
				String dataset = hierarchy.getDataSet() == null ? input.getDataSet().getName()
						: hierarchy.getDataSet().getName();
				List<String> columns = columnMap.get(dataset);
				if (columns == null) {
					columns = new ArrayList<>();
					columnMap.put(dataset, columns);
				}
				for (int j = 0; j < hierarchy.getLevelCount(); j++) {
					TabularLevelHandle level = (TabularLevelHandle) hierarchy.getLevel(j);
					columns.add(level.getColumnName());
				}
			}
		}

		list = input.getContents(CubeHandle.MEASURE_GROUPS_PROP);
		for (int i = 0; i < list.size(); i++) {
			MeasureGroupHandle measureGroup = (MeasureGroupHandle) list.get(i);
			Object[] measures = measureGroup.getContents(MeasureGroupHandle.MEASURES_PROP).toArray();
			if (measures != null) {
				String dataset = input.getDataSet().getName();
				List<String> columns = columnMap.get(dataset);
				if (columns == null) {
					columns = new ArrayList<>();
					columnMap.put(dataset, columns);
				}

				for (int j = 0; j < measures.length; j++) {
					TabularMeasureHandle measure = (TabularMeasureHandle) measures[j];
					try {
						String columnName = ExpressionUtil.getColumnName(measure.getMeasureExpression());
						if (columnName != null) {
							columns.add(columnName);
						}
					} catch (BirtException e) {
						// do nothing
					}
				}
			}
		}

		return columnMap;
	}
}
