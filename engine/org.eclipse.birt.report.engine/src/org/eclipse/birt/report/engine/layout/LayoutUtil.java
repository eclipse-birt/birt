/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;

public class LayoutUtil
{
	
	public static MasterPageDesign getMasterPage( IReportContent report, String masterPage )
	{
		MasterPageDesign pageDesign = null;
		if ( masterPage != null && !"".equals( masterPage ) ) //$NON-NLS-1$
		{
			pageDesign = report.getDesign( ).findMasterPage( masterPage );
			if ( pageDesign != null )
			{
				return pageDesign;
			}
		}
		return getDefaultMasterPage( report );
	}
	
	public static  MasterPageDesign getDefaultMasterPage( IReportContent report )
	{
		PageSetupDesign pageSetup = report.getDesign( ).getPageSetup( );
		int pageCount = pageSetup.getMasterPageCount( );
		if ( pageCount > 0 )
		{
			MasterPageDesign pageDesign =  pageSetup.getMasterPage( 0 );
			return pageDesign;
		}
		return null;
	}

	public static boolean isRowHidden( Object rowContent, String format,
			boolean outputDisplayNone )
	{
		if ( rowContent != null && rowContent instanceof IRowContent )
		{
			IStyle style = ( (IRowContent) rowContent ).getStyle( );
			if ( !outputDisplayNone
					&& IStyle.NONE_VALUE.equals( style
							.getProperty( IStyle.STYLE_DISPLAY ) ) )
			{
				return true;
			}
			String formats = style.getVisibleFormat( );
			if ( formats != null
					&& ( formats.indexOf( format ) >= 0 || formats
							.indexOf( BIRTConstants.BIRT_ALL_VALUE ) >= 0 ) )
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isRepeatableBand(IBandContent band)
	{
		IContent parent = (IContent)band.getParent( );
		if(parent instanceof IGroupContent)
		{
			IGroupContent group = (IGroupContent)parent;
			if(band.getBandType( )==IBandContent.BAND_GROUP_HEADER)
			{
				return group.isHeaderRepeat( );
			}
		}
		else if(parent instanceof ITableContent)
		{
			ITableContent table = (ITableContent)parent;
			if(band.getBandType( )==IBandContent.BAND_HEADER)
			{
				return table.isHeaderRepeat( );
			}
		}
		else if(parent instanceof IListContent)
		{
			IListContent list = (IListContent)parent;
			if(band.getBandType( )==IBandContent.BAND_HEADER)
			{
				return list.isHeaderRepeat( );
			}
		}
		return false;
		
		
	}
	public static boolean isRepeatableRow( IRowContent row )
	{
		IContent parent = (IContent) row.getParent( );
		if ( parent != null && ( parent instanceof IBandContent ) )
		{
			IBandContent band = (IBandContent) parent;
			int type = band.getBandType( );
			if ( type == IBandContent.BAND_HEADER )
			{
				IContent pp = (IContent) band.getParent( );
				if ( pp != null && pp instanceof ITableContent )
				{
					ITableContent table = (ITableContent) band.getParent( );
					return table.isHeaderRepeat( );
				}
			}
			else if ( type == IBandContent.BAND_GROUP_HEADER )
			{
				IContent pp = (IContent) band.getParent( );
				if ( pp != null && pp instanceof IGroupContent )
				{
					IGroupContent group = (IGroupContent) band.getParent( );
					return group.isHeaderRepeat( );
				}
			}

		}
		return false;
	}
}
