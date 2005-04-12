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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.Hide;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludeLibrary;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.InputParameter;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.OutputParameter;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

/**
 * Provides the factory method to create empty structures.
 */

public class StructureFactory
{

	/**
	 * Creates an empty data set cached meta-data structure.
	 * 
	 * @return an empty data set cached meta-data structure.
	 */

	public static CachedMetaData createCachedMetaData( )
	{
		return new CachedMetaData( );
	}

	/**
	 * Creates an empty computed column structure.
	 * 
	 * @return an empty computed column structure
	 */

	public static ComputedColumn createComputedColumn( )
	{
		return new ComputedColumn( );
	}

	/**
	 * Creates an empty action structure.
	 * 
	 * @return an empty action structure.
	 */

	public static Action createAction( )
	{
		return new Action( );
	}

	/**
	 * Creates an empty config variable structure.
	 * 
	 * @return an empty config variable structure
	 */

	public static ConfigVariable createConfigVar( )
	{
		return new ConfigVariable( );
	}

	/**
	 * Creates an empty custom color structure.
	 * 
	 * @return an empty custom color structure
	 */

	public static CustomColor createCustomColor( )
	{
		return new CustomColor( );
	}

	/**
	 * Creates an empty data-set parameter structure.
	 * 
	 * @return an empty data-set parameter structure
	 */

	public static DataSetParameter createDataSetParameter( )
	{
		return new DataSetParameter( );
	}

	/**
	 * Creates an empty output parameter structure.
	 * 
	 * @return an empty output parameter structure
	 * 
	 * @deprecated by the method {@link #createDataSetParameter()}
	 */

	public static OutputParameter createOutputParameter( )
	{
		OutputParameter param = new OutputParameter( );
		param.setIsOutput( true );
		return param;
	}

	/**
	 * Creates an empty input parameter structure.
	 * 
	 * @return an empty input parameter structure
	 * 
	 * @deprecated by the method {@link #createDataSetParameter()}
	 */

	public static InputParameter createInputParameter( )
	{
		InputParameter param = new InputParameter( );
		param.setIsInput( true );
		return param;
	}

	/**
	 * Creates an empty embedded image structure.
	 * 
	 * @return an empty embedded image structure
	 */

	public static EmbeddedImage createEmbeddedImage( )
	{
		return new EmbeddedImage( );
	}

	/**
	 * Creates an empty filter condition structure.
	 * 
	 * @return an empty filter condition structure
	 */

	public static FilterCondition createFilterCond( )
	{
		return new FilterCondition( );
	}

	/**
	 * Creates an empty visibility rule structure.
	 * 
	 * @return an empty visibility rule structure
	 */

	public static Hide createHide( )
	{
		return new Hide( );
	}

	/**
	 * Creates an empty include script structure.
	 * 
	 * @return an empty include script structure
	 */

	public static IncludeScript createIncludeScript( )
	{
		return new IncludeScript( );
	}

	/**
	 * Creates an empty include library structure.
	 * 
	 * @return an empty include library structure
	 */

	public static IncludeLibrary createIncludeLibrary( )
	{
		return new IncludeLibrary( );
	}

	/**
	 * Creates an empty parameter binding structure.
	 * 
	 * @return an empty parameter binding structure
	 */

	public static ParamBinding createParamBinding( )
	{
		return new ParamBinding( );
	}

	/**
	 * Creates an empty property mask structure.
	 * 
	 * @return an empty property mask structure
	 */

	public static PropertyMask createPropertyMask( )
	{
		return new PropertyMask( );
	}

	/**
	 * Creates an empty result set column structure.
	 * 
	 * @return an empty result set column structure
	 */

	public static ResultSetColumn createResultSetColumn( )
	{
		return new ResultSetColumn( );
	}

	/**
	 * Creates an empty search key structure.
	 * 
	 * @return an empty search key structure
	 */

	public static SearchKey createSearchKey( )
	{
		return new SearchKey( );
	}

	/**
	 * Creates an empty selection choice structure.
	 * 
	 * @return an empty selection choice structure
	 */

	public static SelectionChoice createSelectionChoice( )
	{
		return new SelectionChoice( );
	}

	/**
	 * Creates an empty sort key structure.
	 * 
	 * @return an empty sort key structure
	 */

	public static SortKey createSortKey( )
	{
		return new SortKey( );
	}

	/**
	 * Creates an empty column hint structure.
	 * 
	 * @return an empty column hint structure
	 */

	public static ColumnHint createColumnHint( )
	{
		return new ColumnHint( );
	}

	/**
	 * Creates an empty highlight rule structure.
	 * 
	 * @return an empty highlight rule structure
	 */

	public static HighlightRule createHighlightRule( )
	{
		return new HighlightRule( );
	}

	/**
	 * Creates an empty map rule structure.
	 * 
	 * @return an empty map rule structure
	 */

	public static MapRule createMapRule( )
	{
		return new MapRule( );
	}

	/**
	 * Creates an empty extended property structure.
	 * 
	 * @return an empty extended property structure
	 */

	public static ExtendedProperty createExtendedProperty( )
	{
		return new ExtendedProperty( );
	}

}