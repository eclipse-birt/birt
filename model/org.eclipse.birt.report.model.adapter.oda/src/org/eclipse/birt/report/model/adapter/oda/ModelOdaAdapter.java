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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;

/**
 * An adapter class that converts between ROM OdaDataSourceHandle and ODA
 * DataSourceDesign.
 * 
 * @see OdaDataSourceHandle
 * @see DataSourceDesign
 */

public class ModelOdaAdapter implements IModelOdaAdapter
{

	private IModelOdaAdapter adapter;

	/**
	 * The logger for errors.
	 */

	protected static Logger errorLogger = Logger
			.getLogger( ModelOdaAdapter.class.getName( ) );

	/**
	 * Constructs a DesignEngine with the given platform config.
	 * 
	 * @param config
	 *            the platform config.
	 */

	public ModelOdaAdapter( )
	{
		try
		{
			Platform.startup( null );
		}
		catch ( BirtException e )
		{
			errorLogger.log( Level.INFO,
					"Error occurs while start the platform", e ); //$NON-NLS-1$
		}

		Object factory = Platform
				.createFactoryObject( IAdapterFactory.EXTENSION_MODEL_ADAPTER_ODA_FACTORY );
		if ( factory instanceof IAdapterFactory )
		{
			adapter = ( (IAdapterFactory) factory ).createModelOdaAdapter( );
		}
		if ( adapter == null )
		{
			errorLogger.log( Level.INFO,
					"Can not start the model adapter oda factory." ); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSourceDesign(org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DataSourceDesign createDataSourceDesign(
			OdaDataSourceHandle sourceHandle )
	{
		return adapter.createDataSourceDesign( sourceHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DataSetDesign createDataSetDesign( OdaDataSetHandle setHandle )
	{
		return adapter.createDataSetDesign( setHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSetHandle(org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 *      org.eclipse.birt.report.model.api.ModuleHandle)
	 */

	public OdaDataSetHandle createDataSetHandle( DataSetDesign setDesign,
			ModuleHandle module ) throws SemanticException,
			IllegalStateException
	{
		return adapter.createDataSetHandle( setDesign, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */

	public void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign )
	{
		adapter.updateDataSetDesign( setHandle, setDesign );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 *      java.lang.String)
	 */

	public void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign, String propertyName )
	{
		adapter.updateDataSetDesign( setHandle, setDesign, propertyName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSourceDesign(org.eclipse.birt.report.model.api.OdaDataSourceHandle,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSourceDesign)
	 */

	public void updateDataSourceDesign( OdaDataSourceHandle sourceHandle,
			DataSourceDesign sourceDesign )
	{
		adapter.updateDataSourceDesign( sourceHandle, sourceDesign );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSourceHandle(org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 *      org.eclipse.birt.report.model.api.ModuleHandle)
	 */

	public OdaDataSourceHandle createDataSourceHandle(
			DataSourceDesign sourceDesign, ModuleHandle module )
			throws SemanticException, IllegalStateException
	{
		return adapter.createDataSourceHandle( sourceDesign, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSourceHandle(org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 *      org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public void updateDataSourceHandle( DataSourceDesign sourceDesign,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
	{
		adapter.updateDataSourceHandle( sourceDesign, sourceHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSetHandle(org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 *      org.eclipse.birt.report.model.api.OdaDataSetHandle, boolean)
	 */

	public void updateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, boolean isSourceChanged )
			throws SemanticException
	{
		adapter.updateDataSetHandle( setDesign, setHandle, isSourceChanged );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#newOdaDesignerState(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DesignerState newOdaDesignerState( OdaDataSetHandle setHandle )
	{
		return adapter.newOdaDesignerState( setHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateROMDesignerState(org.eclipse.datatools.connectivity.oda.design.DesignerState,
	 *      org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public void updateROMDesignerState( DesignerState designerState,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		adapter.updateROMDesignerState( designerState, setHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#newOdaDesignerState(org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DesignerState newOdaDesignerState( OdaDataSourceHandle sourceHandle )
	{
		return adapter.newOdaDesignerState( sourceHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateROMDesignerState(org.eclipse.datatools.connectivity.oda.design.DesignerState,
	 *      org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public void updateROMDesignerState( DesignerState designerState,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
	{
		adapter.updateROMDesignerState( designerState, sourceHandle );
	}
}
