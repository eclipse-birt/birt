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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.ibm.icu.text.Collator;

/**
 * Tree viewer content provider adapter for resource browser.
 *
 */

public class CubeContentProvider implements ITreeContentProvider {

	private boolean[] useSorting;

	public CubeContentProvider() {
	}

	public CubeContentProvider(boolean[] useSorting) {
		this.useSorting = useSorting;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof DimensionHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((DimensionHandle) parentElement)
					.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
			if (hierarchy == null) {
				return new Object[0];
			} else if (hierarchy.getLevelCount() > 0) {
				return new Object[] { hierarchy.getLevel(0) };
			} else {
				VirtualField virtualLevel = new VirtualField(VirtualField.TYPE_LEVEL);
				virtualLevel.setModel(parentElement);
				return new Object[] { virtualLevel };
			}
		}
		if (parentElement instanceof CubeHandle) {
			CubeHandle handle = (CubeHandle) parentElement;
			return new Object[] { handle.getPropertyHandle(ICubeModel.DIMENSIONS_PROP),
					handle.getPropertyHandle(ICubeModel.MEASURE_GROUPS_PROP) };
		}
		if (parentElement instanceof PropertyHandle) {
			PropertyHandle property = (PropertyHandle) parentElement;
			String name = property.getPropertyDefn().getName();

			if (name.equals(ICubeModel.DIMENSIONS_PROP)) {
				CubeHandle cube = (CubeHandle) property.getElementHandle();
				VirtualField virtualDimsnion = new VirtualField(VirtualField.TYPE_DIMENSION);
				virtualDimsnion.setModel(parentElement);
				List dimensionList = new ArrayList();
				List<DimensionHandle> dimensions = new ArrayList<>();
				if (cube.getContentCount(CubeHandle.DIMENSIONS_PROP) > 0) {
					dimensions.addAll(cube.getContents(CubeHandle.DIMENSIONS_PROP));
				}

				if (useSorting != null && useSorting[0]) {
					// sort attribute list
					Collections.sort(dimensions, new Comparator<DimensionHandle>() {

						@Override
						public int compare(DimensionHandle o1, DimensionHandle o2) {
							return Collator.getInstance().compare(o1.getName(), o2.getName());
						}
					});
				}

				dimensionList.addAll(dimensions);
				dimensionList.add(0, virtualDimsnion);
				return dimensionList.toArray();

			} else if (name.equals(ICubeModel.MEASURE_GROUPS_PROP)) {
				CubeHandle cube = (CubeHandle) property.getElementHandle();
				VirtualField virtualMeasureGroup = new VirtualField(VirtualField.TYPE_MEASURE_GROUP);
				virtualMeasureGroup.setModel(parentElement);
				List measureGroupList = new ArrayList();
				List<MeasureGroupHandle> measures = new ArrayList<>();
				if (cube.getContentCount(CubeHandle.MEASURE_GROUPS_PROP) > 0) {
					measures.addAll(cube.getContents(CubeHandle.MEASURE_GROUPS_PROP));
				}
				if (useSorting != null && useSorting[0]) {
					// sort attribute list
					Collections.sort(measures, new Comparator<MeasureGroupHandle>() {

						@Override
						public int compare(MeasureGroupHandle o1, MeasureGroupHandle o2) {
							return Collator.getInstance().compare(o1.getName(), o2.getName());
						}
					});
				}
				measureGroupList.addAll(measures);
				measureGroupList.add(0, virtualMeasureGroup);
				return measureGroupList.toArray();
			}
		}
		if (parentElement instanceof LevelHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((LevelHandle) parentElement).getContainer();
			int pos = ((LevelHandle) parentElement).getIndex();
			if (hierarchy.getLevel(pos + 1) != null) {
				return new Object[] { hierarchy.getLevel(pos + 1) };
			}
		}
		if (parentElement instanceof MeasureGroupHandle) {
			Object[] measures = ((MeasureGroupHandle) parentElement).getContents(MeasureGroupHandle.MEASURES_PROP)
					.toArray();
			if (measures == null || measures.length == 0) {
				VirtualField virtualMeasure = new VirtualField(VirtualField.TYPE_MEASURE);
				virtualMeasure.setModel(parentElement);
				return new Object[] { virtualMeasure };
			} else {
				return measures;
			}
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object )
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof LevelHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((LevelHandle) element).getContainer();
			LevelHandle level = (LevelHandle) element;
			if (hierarchy == null) {
				return null;
			}
			if (level.getIndex() > 0) {
				return hierarchy.getLevel(level.getIndex() - 1);
			} else {
				return hierarchy.getContainer();
			}
		}
		if (element instanceof MeasureGroupHandle) {
			MeasureGroupHandle measures = (MeasureGroupHandle) element;
			CubeHandle cube = (CubeHandle) measures.getContainer();
			if (cube != null) {
				return cube.getPropertyHandle(ICubeModel.MEASURE_GROUPS_PROP);
			}
		}
		if (element instanceof DimensionHandle) {
			DimensionHandle dimension = (DimensionHandle) element;
			CubeHandle cube = (CubeHandle) dimension.getContainer();
			if (cube != null) {
				return cube.getPropertyHandle(ICubeModel.DIMENSIONS_PROP);
			}
		}
		if (element instanceof MeasureHandle) {
			return ((MeasureHandle) element).getContainer();
		}
		if (element instanceof PropertyHandle) {
			PropertyHandle property = (PropertyHandle) element;
			return property.getElementHandle();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Object[]) {
			return ((Object[]) element).length > 0;
		}
		if (element instanceof DimensionHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((DimensionHandle) element)
					.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
			if (hierarchy == null) {
				return false;
			}
			return true;
		}
		if (element instanceof LevelHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((LevelHandle) element).getContainer();
			int pos = ((LevelHandle) element).getIndex();
			return hierarchy.getLevel(pos + 1) != null;
		}
		if ((element instanceof MeasureGroupHandle) || (element instanceof CubeHandle)) {
			return true;
		}
		if (element instanceof PropertyHandle) {
			PropertyHandle property = (PropertyHandle) element;
			String name = property.getPropertyDefn().getName();
			if (name.equals(ICubeModel.DIMENSIONS_PROP)) {
				// CubeHandle cube = (CubeHandle) property.getElementHandle( );
				// List dimensionList = cube.getContents(
				// CubeHandle.DIMENSIONS_PROP );
				// if ( dimensionList == null || dimensionList.size( ) == 0 )
				// {
				// TabularDimensionHandle dimension =
				// DesignElementFactory.getInstance( )
				// .newTabularDimension( "Group" );
				// try
				// {
				// cube.add( CubeHandle.DIMENSIONS_PROP, dimension );
				// }
				// catch ( SemanticException e )
				// {
				// ExceptionHandler.handle( e );
				// }
				// }
				// return dimensionList != null && dimensionList.size( ) > 0;
				return true;
			} else if (name.equals(ICubeModel.MEASURE_GROUPS_PROP)) {
				// CubeHandle cube = (CubeHandle) property.getElementHandle( );
				// List measureList = cube.getContents(
				// CubeHandle.MEASURE_GROUPS_PROP );
				// if ( measureList == null || measureList.size( ) == 0 )
				// {
				// TabularMeasureGroupHandle measureGroup =
				// DesignElementFactory.getInstance( )
				// .newTabularMeasureGroup( "Summary Field" );
				// try
				// {
				// cube.add( CubeHandle.MEASURE_GROUPS_PROP, measureGroup );
				// }
				// catch ( SemanticException e )
				// {
				// ExceptionHandler.handle( e );
				// }
				// }
				// return measureList != null && measureList.size( ) > 0;
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
