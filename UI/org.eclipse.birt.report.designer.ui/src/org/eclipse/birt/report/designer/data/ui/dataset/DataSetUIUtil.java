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
package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

/**
 * The utility class.
 */
public final class DataSetUIUtil
{
	/**
	 * Save the column meta data to data set handle.
	 * 
	 * @param dataSetHandle
	 * @param items
	 * @throws SemanticException
	 */
	public static void updateColumnCache( DataSetHandle dataSetHandle ) throws SemanticException
	{
		DataSetItemModel[] items = DataSetManager.getCurrentInstance( ).getColumns( dataSetHandle, true);
		if ( items.length > 0 )
		{
			dataSetHandle.setCachedMetaData( StructureFactory.createCachedMetaData( ) );

			for ( int i = 0; i < items.length; i++ )
			{
				ResultSetColumn rsc = StructureFactory.createResultSetColumn( );
				rsc.setColumnName( ( items[i].getAlias( ) == null || items[i].getAlias( )
						.trim( )
						.length( ) == 0 ) ? items[i].getDataSetColumnName( )
						: items[i].getAlias( ) );
				rsc.setDataType( toModelDataType( items[i].getDataType( ) ) );
				rsc.setPosition( new Integer( items[i].getPosition( ) ) );

				dataSetHandle
						.getCachedMetaDataHandle( )
						.getResultSet( )
						.addItem( rsc );
			}
		}
		else
		{
			dataSetHandle
					.setCachedMetaData( null );
		}
	}
	
	/**
	 * Map oda data type to model data type.
	 * 
	 * @param modelDataType
	 * @return
	 */
	private static String toModelDataType( int modelDataType )
	{
		if ( modelDataType == DataType.INTEGER_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER ;
		else if ( modelDataType == DataType.STRING_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		else if ( modelDataType == DataType.DATE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
		else if ( modelDataType == DataType.DECIMAL_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
		else if ( modelDataType == DataType.DOUBLE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;
		
		return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
	}
}
