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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.FilterByRow;

/**
 * The class used to populate DataSet data.
 * 
 */
class DataSetProcessUtil extends RowProcessUtil
{
	/**
	 * 
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 */
	private DataSetProcessUtil( ResultSetPopulator populator,
			ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper,
			FilterByRow filterByRow,
			PassStatusController psController,
			DataEngineSession session)
	{
		super( populator,
				iccState,
				computedColumnHelper,
				filterByRow,
				psController, session);
	}
	
	/**
	 * Populate the data set data of an IResultIterator instance.
	 * 
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 * @throws DataException
	 */
	public static void doPopulate( ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow,
			PassStatusController psController, DataEngineSession session ) throws DataException
	{
		DataSetProcessUtil instance = new DataSetProcessUtil( populator,
				iccState,
				computedColumnHelper,
				filterByRow,
				psController,
				session );
		instance.populateDataSet( );
	}
	
	/**
	 * 
	 * @throws DataException
	 */
	private void populateDataSet() throws DataException
	{
		List aggCCList = prepareComputedColumns(TransformationConstants.DATA_SET_MODEL );

		doDataSetFilter( );

		//Begin populate computed columns with aggregations.
		populateComputedColumns( aggCCList );
	}
	
	/**
	 * 
	 * @throws DataException
	 */
	private void doDataSetFilter( ) throws DataException
	{
		if(	!psController.needDoOperation( PassStatusController.DATA_SET_FILTERING ))
			return;
				
		boolean changeMaxRows = filterByRow.getFilterList( FilterByRow.QUERY_FILTER )
				.size( )
				+ filterByRow.getFilterList( FilterByRow.GROUP_FILTER ).size( ) > 0;

		applyFilters( FilterByRow.DATASET_FILTER,
				changeMaxRows );
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	private void populateComputedColumns( List aggCCList ) throws DataException
	{
		if ( !psController.needDoOperation( PassStatusController.DATA_SET_COMPUTED_COLUMN_POPULATING ) )
			return;
		// if no group pass has been made, made one.
		if ( !psController.needDoOperation( PassStatusController.DATA_SET_FILTERING ) )
		{
			PassUtil.pass( this.populator,
					new OdiResultSetWrapper( populator.getResultIterator( ) ),
					false, this.session );
		}
		computedColumnHelper.getComputedColumnList( ).clear( );
		computedColumnHelper.getComputedColumnList( ).addAll( aggCCList );
		computedColumnHelper.setModel( TransformationConstants.DATA_SET_MODEL );
		iccState.setModel( TransformationConstants.DATA_SET_MODEL );
		// If there are computed columns cached in iccState, then begin
		// multipass.
		if ( iccState.getCount( ) > 0 )
		{
			ComputedColumnCalculator.populateComputedColumns( this.populator,
					new OdiResultSetWrapper( this.populator.getResultIterator( ) ),
					iccState,
					computedColumnHelper, this.session );
		}
		computedColumnHelper.setModel( TransformationConstants.NONE_MODEL );
	}
}
