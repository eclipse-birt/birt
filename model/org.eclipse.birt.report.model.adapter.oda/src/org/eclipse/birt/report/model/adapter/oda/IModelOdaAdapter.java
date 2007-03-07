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

package org.eclipse.birt.report.model.adapter.oda;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;

/**
 * 
 *
 */

public interface IModelOdaAdapter
{

	/**
	 * Adapts the specified Model OdaDataSourceHandle to a Data Engine API
	 * DataSourceDesign object.
	 * 
	 * @param sourceHandle
	 *            the Model handle
	 * @return a new <code>DataSourceDesign</code>
	 */

	DataSourceDesign createDataSourceDesign( OdaDataSourceHandle sourceHandle );

	/**
	 * Adapts the specified Model OdaDataSetHandle to a Data Engine API
	 * DataSetDesign object.
	 * 
	 * @param setHandle
	 *            the Model handle
	 * @return a new <code>DataSetDesign</code>
	 */

	DataSetDesign createDataSetDesign( OdaDataSetHandle setHandle );

	/**
	 * Adapts the Data Engine API DataSetDesign object to the specified Model
	 * OdaDataSetHandle.
	 * 
	 * @param setDesign
	 *            the ODA dataSet design. <b>User should make sure
	 *            <code>setDesign</code> only contains driver-defined
	 *            parameter.It's very important! </b>
	 * @param module
	 *            the module where the Model handle resides.
	 * @return a new <code>OdaDataSourceHandle</code>
	 * @throws SemanticException
	 *             if any value in <code>sourceDesign</code> is invalid
	 *             according ROM.
	 * @throws IllegalStateException
	 *             if <code>setDesign</code> is not valid.
	 */

	OdaDataSetHandle createDataSetHandle( DataSetDesign setDesign,
			ModuleHandle module ) throws SemanticException,
			IllegalStateException;

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param setHandle
	 *            the Model handle
	 * @param setDesign
	 *            the ODA data source design
	 */

	void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign );

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param setHandle
	 *            the Model handle
	 * @param setDesign
	 *            the ODA data source design
	 * @param propertyName
	 *            the property name
	 */

	void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign, String propertyName );

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param sourceHandle
	 *            the Model handle
	 * @param sourceDesign
	 *            the ODA data source design
	 */

	void updateDataSourceDesign( OdaDataSourceHandle sourceHandle,
			DataSourceDesign sourceDesign );

	/**
	 * Adapts the Data Engine API DataSourceDesign object to the specified Model
	 * OdaDataSourceHandle.
	 * 
	 * @param sourceDesign
	 *            the ODA dataSource design.
	 * @param module
	 *            the module where the Model handle resides.
	 * @return a new <code>OdaDataSourceHandle</code>
	 * @throws SemanticException
	 *             if any value in <code>sourceDesign</code> is invalid
	 *             according ROM.
	 * @throws IllegalStateException
	 *             if <code>sourceDesign</code> is not valid.
	 */

	OdaDataSourceHandle createDataSourceHandle( DataSourceDesign sourceDesign,
			ModuleHandle module ) throws SemanticException,
			IllegalStateException;

	/**
	 * Updates values of <code>sourceHandle</code> with the given
	 * <code>sourceDesign</code>.
	 * 
	 * @param sourceDesign
	 *            the ODA data source design
	 * @param sourceHandle
	 *            the Model handle
	 * @throws SemanticException
	 *             if any of <code>sourceDesign</code> property values is not
	 *             valid.
	 */

	void updateDataSourceHandle( DataSourceDesign sourceDesign,
			OdaDataSourceHandle sourceHandle ) throws SemanticException;

	/**
	 * Updates values of <code>DataSetHandle</code> with the given
	 * <code>sourceDesign</code>.
	 * 
	 * @param setDesign
	 *            the ODA data source design
	 * @param setHandle
	 *            the Model handle
	 * @param isSourceChanged
	 *            <code>true</code> if the data set of the given design has
	 *            been changed. Otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if any of <code>sourceDesign</code> property values is not
	 *             valid.
	 */

	void updateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, boolean isSourceChanged )
			throws SemanticException;

	/**
	 * Creates a ODA DesignerState object with the given OdaDataSet.
	 * 
	 * @param setHandle
	 *            the ODA DataSet.
	 * @return the oda DesignerState object.
	 */

	DesignerState newOdaDesignerState( OdaDataSetHandle setHandle );

	/**
	 * Creates a ROM DesignerState object with the given ODA DataSet design.
	 * 
	 * @param designerState
	 *            the ODA designer state.
	 * @param setHandle
	 *            the ODA DataSet.
	 * @throws SemanticException
	 *             if ROM Designer state value is locked.
	 */

	void updateROMDesignerState( DesignerState designerState,
			OdaDataSetHandle setHandle ) throws SemanticException;

	/**
	 * Creates a ODA DesignerState object with the given OdaDataSource.
	 * 
	 * @param sourceHandle
	 *            the ODA DataSource.
	 * @return the oda DesignerState object.
	 */

	DesignerState newOdaDesignerState( OdaDataSourceHandle sourceHandle );

	/**
	 * Creates a ROM DesignerState object with the given ODA DataSet design.
	 * 
	 * @param designerState
	 *            the ODA designer state.
	 * @param sourceHandle
	 *            the ODA DataSource.
	 * @throws SemanticException
	 *             if ROM Designer state value is locked.
	 */

	void updateROMDesignerState( DesignerState designerState,
			OdaDataSourceHandle sourceHandle ) throws SemanticException;

	/**
	 * Check whether two data source design is equal.
	 * 
	 * @param designFromHandle
	 *            the data source design created from data source handle
	 * @param design
	 *            the data source design
	 * @return <code>true</code> if two data source designs are equal.
	 *         Otherwise <code>false</code>.
	 */

	boolean isEqualDataSourceDesign( DataSourceDesign designFromHandle,
			DataSourceDesign design );

}