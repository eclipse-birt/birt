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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Parses the "property" tag. The tag may give the property value of the element
 * or the member of the structure.
 */

class PropertyState extends AbstractPropertyState
{

	/* cached hash codes of string for comparision */

	private static final int GROUP_START_PROP = ListGroup.GROUP_START_PROP
			.toLowerCase( ).hashCode( );
	private static final int CHEET_SHEET = "cheetSheet".toLowerCase( ).hashCode( ); //$NON-NLS-1$
	private static final int FILTER_OPERATOR_MEMBER = FilterCondition.OPERATOR_MEMBER
			.toLowerCase( ).hashCode( );
	private static final int MAPRULE_OPERATOR_MEMBER = MapRule.OPERATOR_MEMBER
			.toLowerCase( ).hashCode( );
	private static final int DATE_TIME_FORMAT_STRUCT = DateTimeFormatValue.FORMAT_VALUE_STRUCT
			.toLowerCase( ).hashCode( );
	private static final int NUMBER_FORMAT_STRUCT = NumberFormatValue.FORMAT_VALUE_STRUCT
			.toLowerCase( ).hashCode( );
	private static final int STRING_FORMAT_STRUCT = StringFormatValue.FORMAT_VALUE_STRUCT
			.toLowerCase( ).hashCode( );
	private static final int PARAM_FORMAT_STRUCT = ParameterFormatValue.FORMAT_VALUE_STRUCT
			.toLowerCase( ).hashCode( );

	private static final int HEADER_HEIGHT = "headerHeight".toLowerCase( ).hashCode( ); //$NON-NLS-1$
	private static final int FOOTER_HEIGHT = "footerHeight".toLowerCase( ).hashCode( ); //$NON-NLS-1$

	private static final int PAGE_BREAK_BEFORE_PROP = IStyleModel.PAGE_BREAK_BEFORE_PROP
			.toLowerCase( ).hashCode( );
	private static final int PAGE_BREAK_AFTER_PROP = IStyleModel.PAGE_BREAK_AFTER_PROP
			.toLowerCase( ).hashCode( );
	private static final int PAGE_BREAK_INSIDE_PROP = IStyleModel.PAGE_BREAK_INSIDE_PROP
			.toLowerCase( ).hashCode( );

	private static final int THUMBNAIL_PROP = IReportDesignModel.THUMBNAIL_PROP
			.toLowerCase( ).hashCode( );
	private static final int DATA_MEMBER = EmbeddedImage.DATA_MEMBER
			.toLowerCase( ).hashCode( );
	private static final int CONTENT_AS_BLOB_MEMBER = OdaDesignerState.CONTENT_AS_BLOB_MEMBER
			.toLowerCase( ).hashCode( );

	private static final int ON_CREATE_METHOD = ICellModel.ON_CREATE_METHOD
			.toLowerCase( ).hashCode( );

	private static final int CACHED_ROW_COUNT_PROP = ISimpleDataSetModel.CACHED_ROW_COUNT_PROP
			.toLowerCase( ).hashCode( );

	private static final int CHOICE_VERTICAL_ALIGN = DesignChoiceConstants.CHOICE_VERTICAL_ALIGN
			.toLowerCase( ).hashCode( );
	private static final int DEFAULT_VALUE_PROP = ScalarParameter.DEFAULT_VALUE_PROP
			.toLowerCase( ).hashCode( );
	private static final int DATA_TYPE_MEMBER = DataSetParameter.DATA_TYPE_MEMBER
			.toLowerCase( ).hashCode( );

	protected PropertyDefn propDefn = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, )
	 */

	PropertyState( ModuleParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, String propName, IStructure
	 *      struct)
	 */

	PropertyState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element );

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/**
	 * Sets the name in attribute.
	 * 
	 * @param name
	 *            the value of the attribute name
	 */

	protected void setName( String name )
	{
		super.setName( name );

		if ( struct != null )
		{
			propDefn = (PropertyDefn) struct.getDefn( ).getMember( name );
		}
		else
		{
			propDefn = element.getPropertyDefn( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );
		doEnd( value );
	}

	/**
	 * @param value
	 */

	protected void doEnd( String value )
	{
		if ( struct != null )
		{
			setMember( struct, propDefn.getName( ), name, value );
			return;
		}

		if ( StyledElement.STYLE_PROP.equalsIgnoreCase( name ) )
		{
			// Ensure that the element can have a style.

			if ( !element.getDefn( ).hasStyle( ) )
			{
				DesignParserException e = new DesignParserException(
						new String[]{name},
						DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
				RecoverableError.dealUndefinedProperty( handler, e );
				return;
			}

			( (StyledElement) element ).setStyleName( value );
		}

		else
		{
			setProperty( name, value );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo()
	 */

	protected AbstractParseState generalJumpTo( )
	{

		if ( element instanceof ReportDesign && THUMBNAIL_PROP == nameValue )
		{
			Base64PropertyState state = new Base64PropertyState( handler,
					element, IReportDesignModel.CHARSET );
			state.setName( name );
			return state;
		}

		if ( struct instanceof EmbeddedImage && DATA_MEMBER == nameValue )
		{
			Base64PropertyState state = new Base64PropertyState( handler,
					element, propDefn, struct, EmbeddedImage.CHARSET );
			state.setName( name );
			return state;
		}

		if ( struct instanceof OdaDesignerState
				&& CONTENT_AS_BLOB_MEMBER == nameValue )
		{
			Base64PropertyState state = new Base64PropertyState( handler,
					element, propDefn, struct, OdaDesignerState.CHARSET );
			state.setName( name );
			return state;
		}

		if ( ON_CREATE_METHOD == nameValue )
		{
			CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(
					handler, element );
			state.setName( name );
			return state;
		}

		return super.generalJumpTo( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#versionConditionalJumpTo()
	 */

	protected AbstractParseState versionConditionalJumpTo( )
	{
		IPropertyDefn jmpDefn = null;

		if ( struct != null )
			jmpDefn = struct.getDefn( ).getMember( name );
		else
			jmpDefn = element.getPropertyDefn( name );

		if ( handler.versionNumber <= VersionUtil.VERSION_3_2_7 )
		{
			if ( element instanceof ListGroup && GROUP_START_PROP == nameValue )
			{
				CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
						handler, element, ListGroup.GROUP_START_PROP );
				state.setName( ListGroup.INTERVAL_BASE_PROP );
				return state;
			}

			if ( element instanceof ReportDesign && CHEET_SHEET == nameValue )
			{
				CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
						handler, element, "cheetSheet" ); //$NON-NLS-1$
				state.setName( ReportDesign.CHEAT_SHEET_PROP );
				return state;
			}

			int jmpDefnValue = -1;
			int jmpStructDefnValue = -1;
			if ( jmpDefn != null )
			{
				jmpDefnValue = jmpDefn.getName( ).toLowerCase( ).hashCode( );
				IStructureDefn structDefn = jmpDefn.getStructDefn( );
				if ( structDefn != null )
					jmpStructDefnValue = structDefn.getName( ).toLowerCase( )
							.hashCode( );
			}

			if ( FILTER_OPERATOR_MEMBER == jmpDefnValue
					|| MAPRULE_OPERATOR_MEMBER == jmpDefnValue )
			{
				CompatibleOperatorState state = new CompatibleOperatorState(
						handler, element, propDefn, struct );
				state.setName( name );
				return state;
			}

			if ( DATE_TIME_FORMAT_STRUCT == jmpStructDefnValue
					|| NUMBER_FORMAT_STRUCT == jmpStructDefnValue
					|| STRING_FORMAT_STRUCT == jmpStructDefnValue
					|| PARAM_FORMAT_STRUCT == jmpStructDefnValue )
			{
				CompatibleFormatPropertyState state = new CompatibleFormatPropertyState(
						handler, element, propDefn, struct );
				state.setName( name );
				state.createStructure( );
				return state;
			}

			if ( element instanceof GraphicMasterPage
					&& ( HEADER_HEIGHT == nameValue || FOOTER_HEIGHT == nameValue ) )
				return new CompatibleIgnorePropertyState( handler, element );

			if ( ( element instanceof ListingElement || element instanceof GroupElement ) )
			{
				// now 'pageBreakInterval' is supported on table/list

				if ( IListingElementModel.PAGE_BREAK_INTERVAL_PROP
						.equalsIgnoreCase( name )
						&& element instanceof GroupElement )
					return new CompatibleIgnorePropertyState( handler, element );

				if ( name.equalsIgnoreCase( "onStart" ) || name //$NON-NLS-1$
						.equalsIgnoreCase( "onFinish" ) ) //$NON-NLS-1$
					return new CompatibleIgnorePropertyState( handler, element );

				if ( "onRow".equalsIgnoreCase( name ) //$NON-NLS-1$
						&& !( element instanceof TableItem ) )
					return new CompatibleIgnorePropertyState( handler, element );

				if ( "onRow".equalsIgnoreCase( name ) )//$NON-NLS-1$
					return new CompatibleOnRowPropertyState( handler, element );
			}
			if ( element instanceof GroupElement )
			{
				if ( "onCreate".equalsIgnoreCase( name ) || //$NON-NLS-1$
						"onRender".equalsIgnoreCase( name ) ) //$NON-NLS-1$
					return new CompatibleIgnorePropertyState( handler, element );
			}

			if ( PAGE_BREAK_BEFORE_PROP == nameValue
					|| PAGE_BREAK_AFTER_PROP == nameValue
					|| PAGE_BREAK_INSIDE_PROP == nameValue )
			{
				CompatiblePageBreakPropState state = new CompatiblePageBreakPropState(
						handler, element );
				state.setName( name );
				return state;
			}
		}

		// Change 'cachedRowCount' to 'dataSetRowLimit' in DataSet element.

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_7
				&& element instanceof DataSet
				&& CACHED_ROW_COUNT_PROP == nameValue )
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, ISimpleDataSetModel.CACHED_ROW_COUNT_PROP );
			state.setName( ISimpleDataSetModel.DATA_SET_ROW_LIMIT );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_2
				&& CHOICE_VERTICAL_ALIGN == nameValue )
		{
			CompatibleVerticalAlignState state = new CompatibleVerticalAlignState(
					handler, element );
			state.setName( DesignChoiceConstants.CHOICE_VERTICAL_ALIGN );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_4
				&& ( element instanceof ScalarParameter )
				&& ( DEFAULT_VALUE_PROP == nameValue ) )
		{
			CompatiblePropertyTypeState state = new CompatiblePropertyTypeState(
					handler, element );
			state.setName( ScalarParameter.DEFAULT_VALUE_PROP );
			return state;
		}
		if ( handler.versionNumber <= VersionUtil.VERSION_3_2_0
				&& struct instanceof DataSetParameter
				&& "isNullable".equals( name ) ) //$NON-NLS-1$
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, propDefn, struct, "isNullable" ); //$NON-NLS-1$
			state.setName( DataSetParameter.ALLOW_NULL_MEMBER );
			return state;
		}

		if ( ON_CREATE_METHOD == nameValue
				&& handler.versionNumber < VersionUtil.VERSION_3_2_0 )
		{
			CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(
					handler, element );
			state.setName( name );
			return state;
		}

		if ( struct instanceof ComputedColumn
				&& "aggregrateOn".toLowerCase( ).hashCode( ) == nameValue //$NON-NLS-1$
				&& ( element instanceof ScalarParameter || element instanceof ReportItem )
				& handler.versionNumber <= VersionUtil.VERSION_3_2_2 )
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, propDefn, struct, "aggregrateOn" ); //$NON-NLS-1$
			state.setName( ComputedColumn.AGGREGATEON_MEMBER );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_6
				&& ( struct instanceof DataSetParameter || struct instanceof OdaDataSetParameter )
				&& DATA_TYPE_MEMBER == nameValue )
		{
			CompatibleColumnDataTypeState state = new CompatibleColumnDataTypeState(
					handler, element, propDefn, struct );
			state.setName( DataSetParameter.DATA_TYPE_MEMBER );
			return state;
		}

		return super.versionConditionalJumpTo( );
	}
}