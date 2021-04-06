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
package org.eclipse.birt.data.engine.olap.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor;
import org.eclipse.birt.data.engine.olap.cursor.RowDataAccessor;
import org.eclipse.birt.data.engine.olap.cursor.RowDataAccessorService;
import org.eclipse.birt.data.engine.olap.cursor.SubRowDataAccessor;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtDimensionView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;

/**
 * 
 *
 */
public class EdgeAxis implements IEdgeAxis {

	private DimensionAxis[] dimensionAxis;
	private IAggregationResultSet rs;
	private IRowDataAccessor dataAccessor;
	private boolean isCalculatedMember = false;
	private RowDataAccessorService service;

	/**
	 * 
	 * @param resultSet
	 * @param view
	 * @param isCalculatedMember
	 * @throws IOException
	 */
	public EdgeAxis(IAggregationResultSet resultSet, BirtEdgeView view, boolean isCalculatedMember) throws IOException {
		this(resultSet, view, null, isCalculatedMember);
	}

	/**
	 * 
	 * @param resultSet
	 * @param view
	 * @param isCalculatedMember
	 * @param isMirrored
	 * @throws IOException
	 */
	public EdgeAxis(IAggregationResultSet resultSet, BirtEdgeView view, List sortList, boolean isCalculatedMember)
			throws IOException {
		this.rs = resultSet;
		this.isCalculatedMember = isCalculatedMember;

		populateDimensionAxis(resultSet, view);
		service = new RowDataAccessorService(dimensionAxis, view);
		this.dataAccessor = new RowDataAccessor(service, rs);

		for (int i = 0; i < this.dimensionAxis.length; i++) {
			this.dimensionAxis[i].setEdgeInfo(dataAccessor);
		}
	}

	/**
	 * 
	 * @param parent
	 * @param view
	 * @param sortList
	 * @param isPage
	 * @param startingLevelIndex
	 * @throws IOException
	 */
	public EdgeAxis(IRowDataAccessor parent, BirtEdgeView view, List sortList, boolean isPage, int startingLevelIndex)
			throws IOException {
		this(parent.getAggregationResultSet(), view, sortList, isPage);
		this.dataAccessor = new SubRowDataAccessor(parent.getRowDataAccessorService(), parent, startingLevelIndex);
		for (int i = 0; i < this.dimensionAxis.length; i++) {
			this.dimensionAxis[i].setEdgeInfo(dataAccessor);
		}
	}

	/**
	 * 
	 * @param rs
	 * @param view
	 * @param isCalculatedMember
	 * @throws OLAPException
	 * @throws IOException
	 */
	private void populateDimensionAxis(IAggregationResultSet rs, BirtEdgeView view) throws IOException {

		List dimensionAxisList = new ArrayList();
		int index = -1, levelIndex = -1;
		if (!isCalculatedMember) {
			levelIndex = index = 0;
			for (int i = 0; i < view.getDimensionViews().size(); i++) {
				BirtDimensionView dv = (BirtDimensionView) (view.getDimensionViews().get(i));
				Iterator levelIter = dv.getMemberSelection().iterator();

				while (levelIter.hasNext()) {
					ILevelDefinition level = (ILevelDefinition) levelIter.next();
					DimensionAxis axis = new DimensionAxis(this, rs, index, levelIndex);
					axis.setLevelDefinition(level);
					index++;
					levelIndex++;
					dimensionAxisList.add(axis);
				}
			}
		} else if (isCalculatedMember) {
			DimensionAxis axis = new DimensionAxis(this, rs, index, 0);
			dimensionAxisList.add(axis);
		}
		this.dimensionAxis = new DimensionAxis[dimensionAxisList.size()];
		for (int i = 0; i < dimensionAxisList.size(); i++) {
			this.dimensionAxis[i] = (DimensionAxis) dimensionAxisList.get(i);
		}
	}

	/**
	 * PopulateEdgeInfo operation should be done before move up/down along the edge
	 * cursor.
	 * 
	 * @throws OLAPException
	 * @throws IOException
	 */
	public void populateEdgeInfo(boolean isPage) throws OLAPException {
		if (this.dataAccessor != null)
			try {
				this.dataAccessor.initialize(isPage);
			} catch (IOException e) {
				throw new OLAPException(e.getLocalizedMessage());
			}
	}

	/**
	 * 
	 * @return
	 */
	public IRowDataAccessor getRowDataAccessor() {
		return this.dataAccessor;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public DimensionAxis getDimensionAxis(int index) {
		return dimensionAxis[index];
	}

	/**
	 * 
	 * @return
	 */
	public DimensionAxis[] getAllDimensionAxis() {
		return this.dimensionAxis;
	}

	/**
	 * 
	 * @return
	 */
	public IAggregationResultSet getQueryResultSet() {
		return rs;
	}
}
