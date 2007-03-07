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

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstancts;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * Tree viewer label provider adapter for resource browser.
 */

public class CubeLabelProvider extends LabelProvider
{

	private static final Image IMG_DATASOURCE = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SOURCE );

	private static final Image IMG_DATASET = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SET );

	private static final Image IMG_DATAFIELD = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_DATA_COLUMN );
	
	private static final Image IMG_CUBE = UIHelper.getImage( BuilderConstancts.IMAGE_CUBE );

	private static final Image IMG_DIMENSION = UIHelper.getImage( BuilderConstancts.IMAGE_DIMENSION );

	private static final Image IMG_MEASURE = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_DATA_COLUMN );
	
	private static final Image IMG_MEASUREGROUP = UIHelper.getImage( BuilderConstancts.IMAGE_MEASUREGROUP );
	
	private static final Image IMG_LEVEL= UIHelper.getImage( BuilderConstancts.IMAGE_LEVEL );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		if ( element instanceof DataSourceHandle )
		{
			return IMG_DATASOURCE;
		}
		else if ( element instanceof DataSetHandle )
		{
			return IMG_DATASET;
		}
		else if ( element instanceof ResultSetColumnHandle )
		{
			return IMG_DATAFIELD;
		}
		else if ( element instanceof DimensionHandle )
		{
			return IMG_DIMENSION;
		}
		else if ( element instanceof LevelHandle )
		{
			return IMG_LEVEL;
		}
		else if ( element instanceof CubeHandle )
		{
			return IMG_CUBE;
		}
		else if ( element instanceof MeasureHandle )
		{
			return IMG_MEASURE;
		}
		else if ( element instanceof MeasureGroupHandle )
		{
			return IMG_MEASUREGROUP;
		}
		else if ( element instanceof String )
		{
			return IMG_DATAFIELD;
		}
		return super.getImage( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText( Object element )
	{
		if ( element instanceof DataSetHandle )
		{
			return ( (DataSetHandle) element ).getName( );
		}
		else if ( element instanceof ResultSetColumnHandle )
		{
			return ( (ResultSetColumnHandle) element ).getColumnName( );
		}
		else if ( element instanceof DimensionHandle )
		{
			return ( (DimensionHandle) element ).getName( ) + "(Dimension)";
		}
		else if ( element instanceof LevelHandle )
		{
			return ( (LevelHandle) element ).getName( );
		}
		else if ( element instanceof CubeHandle )
		{
			return ( (CubeHandle) element ).getName( );
		}
		else if ( element instanceof MeasureGroupHandle )
		{
			MeasureGroupHandle measures = (MeasureGroupHandle) element;
			if ( measures.getName( ) != null
					&& !measures.getName( ).trim( ).equals( "" ) )
				return measures.getName( ) + "(Measures)";
			else
				return "MEASURES" + "(Measures)";
		}
		else if ( element instanceof MeasureHandle )
		{
			return ( (MeasureHandle) element ).getName( );
		}
		else if ( element instanceof String )
		{
			return (String) element;
		}
		return super.getText( element );
	}

	/**
	 * 
	 * @return the absolute path of resource folder
	 */
	public String getToolTip( Object element )
	{
		return getText( element );
	}

}
