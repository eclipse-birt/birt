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

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * CrosstabReportItemQuery
 */
public class CrosstabReportItemQuery extends ReportItemQueryBase
{

	private static Logger logger = Logger.getLogger( CrosstabReportItemQuery.class.getName( ) );

	private CrosstabReportItemHandle crosstabItem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.ReportItemQueryBase#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle modelHandle )
	{
		super.setModelObject( modelHandle );

		try
		{
			crosstabItem = (CrosstabReportItemHandle) modelHandle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, "Load crosstab item failed" ); //$NON-NLS-1$
			crosstabItem = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.ReportItemQueryBase#createReportQueries(org.eclipse.birt.data.engine.api.IDataQueryDefinition)
	 */
	public IDataQueryDefinition[] createReportQueries(
			IDataQueryDefinition parent ) throws BirtException
	{
		if ( crosstabItem == null )
		{
			throw new CrosstabException( modelHandle == null ? null
					: modelHandle.getElement( ),
					"Invalid crosstab model for query building" ); //$NON-NLS-1$
		}

		IDataQueryDefinition cubeQuery = CrosstabQueryHelper.buildQuery( crosstabItem,
				parent );

		return new IDataQueryDefinition[]{
			cubeQuery
		};
	}

}
