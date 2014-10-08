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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.Locale;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.extension.ReportItemFactory;

import com.ibm.icu.util.ULocale;

/**
 * CrosstabItemFactory. This class provides factory method to create all
 * crosstab items, such as crosstab report item, dimension view, measure view,
 * level view, crosstab cell, header cell and aggregation cell.
 */

public class CrosstabItemFactory extends ReportItemFactory implements
		IMessages,
		ICrosstabConstants
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItemFactory#getMessages
	 * ()
	 */
	public IMessages getMessages( )
	{
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItemFactory#newReportItem
	 * (org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public IReportItem newReportItem( DesignElementHandle extendedItemHandle )
	{
		if ( extendedItemHandle == null )
			return null;
		String extensionName = extendedItemHandle.getStringProperty( ExtendedItemHandle.EXTENSION_NAME_PROP );
		if ( extensionName == null )
			return null;
		if ( CROSSTAB_EXTENSION_NAME.equals( extensionName ) )
			return new CrosstabReportItemHandle( extendedItemHandle );
		if ( CROSSTAB_VIEW_EXTENSION_NAME.equals( extensionName ) )
			return new CrosstabViewHandle( extendedItemHandle );
		if ( DIMENSION_VIEW_EXTENSION_NAME.equals( extensionName ) )
			return new DimensionViewHandle( extendedItemHandle );
		if ( LEVEL_VIEW_EXTENSION_NAME.equals( extensionName ) )
			return new LevelViewHandle( extendedItemHandle );
		if ( MEASURE_VIEW_EXTENSION_NAME.equals( extensionName ) )
			return new MeasureViewHandle( extendedItemHandle );
		if ( COMPUTED_MEASURE_VIEW_EXTENSION_NAME.equals( extensionName ) )
			return new ComputedMeasureViewHandle( extendedItemHandle );
		if ( CROSSTAB_CELL_EXTENSION_NAME.equals( extensionName ) )
			return new CrosstabCellHandle( extendedItemHandle );
		if ( AGGREGATION_CELL_EXTENSION_NAME.equals( extensionName ) )
			return new AggregationCellHandle( extendedItemHandle );
		// if ( HEADER_CELL_EXTENSION_NAME.equals( extensionName ) )
		// return new HeaderCellHandle( extendedItemHandle );
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IMessages#getMessage(java
	 * .lang.String, java.util.Locale)
	 */
	public String getMessage( String key, Locale locale )
	{
		return Messages.getString( key, ULocale.forLocale( locale ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IMessages#getMessage(java
	 * .lang.String, com.ibm.icu.util.ULocale)
	 */
	public String getMessage( String key, ULocale locale )
	{
		return Messages.getString( key, locale );
	}

	public IStyleDeclaration[] getFactoryStyles( String extensionName )
	{
		// we dont' return the factory styles now, it'll be handled by theme
		// if ( CROSSTAB_EXTENSION_NAME.equals( extensionName ) )
		// {
		// return new IStyleDeclaration[]{
		// new CrosstabFactoryStyle( CROSSTAB_SELECTOR ),
		// new CrosstabFactoryStyle( CROSSTAB_CELL_SELECTOR )
		// };
		// }

		return null;
	}

}
