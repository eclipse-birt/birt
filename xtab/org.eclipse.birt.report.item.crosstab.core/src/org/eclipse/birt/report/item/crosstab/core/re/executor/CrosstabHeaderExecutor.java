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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;

/**
 * CrosstabHeaderExecutor
 */
public class CrosstabHeaderExecutor extends BaseCrosstabExecutor
{

	private boolean hasMeasureHeader;
	private int currentGroupIndex;

	public CrosstabHeaderExecutor( BaseCrosstabExecutor parent )
	{
		super( parent );
	}

	public IContent execute( )
	{
		ITableBandContent content = context.getReportContent( )
				.createTableBandContent( );
		content.setBandType( ITableBandContent.BAND_HEADER );

		initializeContent( content, null );

		prepareChildren( );

		return content;
	}

	private void prepareChildren( )
	{
		currentGroupIndex = 0;
		hasMeasureHeader = hasMeasureHeader( COLUMN_AXIS_TYPE );
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor nextExecutor = null;

		if ( currentGroupIndex < columnGroups.size( ) )
		{
			EdgeGroup eg = (EdgeGroup) columnGroups.get( currentGroupIndex++ );

			DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
					eg.dimensionIndex );
			LevelViewHandle lv = dv.getLevel( eg.levelIndex );

			nextExecutor = new CrosstabHeaderRowExecutor( this, lv );
		}
		else if ( hasMeasureHeader )
		{
			nextExecutor = new CrosstabMeasureHeaderRowExecutor( this );
			hasMeasureHeader = false;
		}

		return nextExecutor;
	}

	public boolean hasNextChild( )
	{
		if ( currentGroupIndex < columnGroups.size( ) )
		{
			return true;
		}

		if ( hasMeasureHeader )
		{
			return true;
		}

		return false;
	}
}
