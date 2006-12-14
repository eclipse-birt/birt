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

import java.util.List;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.metadata.ODAExtensionElementDefn;
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

		if ( IStyledElementModel.STYLE_PROP.equalsIgnoreCase( name ) )
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
		IPropertyDefn jmpDefn = null;
		if ( struct != null )
			jmpDefn = struct.getDefn( ).getMember( name );
		else
			jmpDefn = element.getPropertyDefn( name );

		if ( element instanceof ListGroup
				&& IGroupElementModel.GROUP_START_PROP.equalsIgnoreCase( name ) )
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, IGroupElementModel.GROUP_START_PROP );
			state.setName( IGroupElementModel.INTERVAL_BASE_PROP );
			return state;
		}

		if ( element instanceof ReportDesign
				&& "cheetSheet".equalsIgnoreCase( name ) ) //$NON-NLS-1$
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, "cheetSheet" ); //$NON-NLS-1$
			state.setName( IReportDesignModel.CHEAT_SHEET_PROP );
			return state;
		}

		if ( element instanceof Module && "msgBaseName".equalsIgnoreCase( name ) ) //$NON-NLS-1$
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, "msgBaseName" ); //$NON-NLS-1$
			state.setName( IModuleModel.INCLUDE_RESOURCE_PROP );
			return state;
		}

		if ( jmpDefn != null
				&& ( FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase( jmpDefn
						.getName( ) ) || StyleRule.OPERATOR_MEMBER
						.equalsIgnoreCase( jmpDefn.getName( ) ) ) )
		{
			CompatibleOperatorState state = new CompatibleOperatorState(
					handler, element, propDefn, struct );
			state.setName( name );
			return state;
		}

		if ( ( jmpDefn != null ) && ( jmpDefn.getStructDefn( ) != null ) )
		{
			if ( DateTimeFormatValue.FORMAT_VALUE_STRUCT
					.equalsIgnoreCase( jmpDefn.getStructDefn( ).getName( ) )
					|| NumberFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) )
					|| StringFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) )
					|| ParameterFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) ) )
			{
				CompatibleFormatPropertyState state = new CompatibleFormatPropertyState(
						handler, element, propDefn, struct );
				state.setName( name );
				state.createStructure( );
				return state;
			}
		}

		if ( ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT
				.equalsIgnoreCase( element.getDefn( ).getName( ) )
				&& ( name.equalsIgnoreCase( "headerHeight" ) || name //$NON-NLS-1$
						.equalsIgnoreCase( "footerHeight" ) ) ) //$NON-NLS-1$
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

		if ( IStyleModel.PAGE_BREAK_BEFORE_PROP.equalsIgnoreCase( name )
				|| IStyleModel.PAGE_BREAK_AFTER_PROP.equalsIgnoreCase( name )
				|| IStyleModel.PAGE_BREAK_INSIDE_PROP.equalsIgnoreCase( name ) )
		{
			CompatiblePageBreakPropState state = new CompatiblePageBreakPropState(
					handler, element );
			state.setName( name );
			return state;
		}

		if ( element instanceof ReportDesign
				&& IReportDesignModel.THUMBNAIL_PROP.equalsIgnoreCase( name ) )
		{
			Base64PropertyState state = new Base64PropertyState( handler,
					element, IReportDesignModel.CHARSET );
			state.setName( name );
			return state;
		}

		if ( struct instanceof EmbeddedImage
				&& EmbeddedImage.DATA_MEMBER.equalsIgnoreCase( name ) )
		{
			Base64PropertyState state = new Base64PropertyState( handler,
					element, propDefn, struct, EmbeddedImage.CHARSET );
			state.setName( name );
			return state;
		}

		if ( struct instanceof OdaDesignerState
				&& OdaDesignerState.CONTENT_AS_BLOB_MEMBER
						.equalsIgnoreCase( name ) )
		{
			Base64PropertyState state = new Base64PropertyState( handler,
					element, propDefn, struct, OdaDesignerState.CHARSET );
			state.setName( name );
			return state;
		}
		if ( ( ICellModel.ON_CREATE_METHOD.equalsIgnoreCase( name ) || ITableRowModel.ON_CREATE_METHOD
				.equalsIgnoreCase( name ) ) )
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

		if ( handler.versionNumber <= VersionUtil.VERSION_3_2_10
				&& propDefn == null
				&& element instanceof IOdaExtendableElementModel )
		{
			ODAExtensionElementDefn elementDefn = null;

			if ( element instanceof OdaDataSet )
				elementDefn = (ODAExtensionElementDefn) ( (OdaDataSet) element )
						.getExtDefn( );
			else if ( element instanceof OdaDataSource )
				elementDefn = (ODAExtensionElementDefn) ( (OdaDataSource) element )
						.getExtDefn( );

			if ( elementDefn != null )
			{
				List privatePropDefns = elementDefn.getODAPrivateDriverPropertyNames( );
				if ( privatePropDefns.contains( name ) )
				{
					CompatibleODAPrivatePropertyState state = new CompatibleODAPrivatePropertyState(
							handler, element );
					state.setName( name );
					return state;
				}
			}
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_10 )
		{
			if ( element instanceof ReportItem )
			{
				if ( IReportItemModel.TOC_PROP.equalsIgnoreCase( name ) )
				{
					CompatibleTOCPropertyState state = new CompatibleTOCPropertyState(
							handler, element );
					state.setName( IReportItemModel.TOC_PROP );
					return state;
				}
			}
			if ( element instanceof GroupElement )
			{
				if ( IGroupElementModel.TOC_PROP.equalsIgnoreCase( name ) )
				{
					CompatibleTOCPropertyState state = new CompatibleTOCPropertyState(
							handler, element );
					state.setName( IGroupElementModel.TOC_PROP );
					return state;
				}
			}
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_2
				&& ( DesignChoiceConstants.CHOICE_VERTICAL_ALIGN.equals( name ) ) )
		{
			CompatibleVerticalAlignState state = new CompatibleVerticalAlignState(
					handler, element );
			state.setName( DesignChoiceConstants.CHOICE_VERTICAL_ALIGN );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_4
				&& ( element instanceof ScalarParameter )
				&& ( IScalarParameterModel.DEFAULT_VALUE_PROP
						.equalsIgnoreCase( name ) ) )
		{
			CompatiblePropertyTypeState state = new CompatiblePropertyTypeState(
					handler, element );
			state.setName( IScalarParameterModel.DEFAULT_VALUE_PROP );
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

		if ( ( IReportItemModel.ON_CREATE_METHOD.equalsIgnoreCase( name ) )
				&& handler.versionNumber < VersionUtil.VERSION_3_2_0 )
		{
			CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(
					handler, element );
			state.setName( name );
			return state;
		}

		if ( struct instanceof ComputedColumn
				&& "aggregrateOn".equalsIgnoreCase( name ) //$NON-NLS-1$
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
				&& DataSetParameter.DATA_TYPE_MEMBER.equalsIgnoreCase( name ) )
		{
			CompatibleColumnDataTypeState state = new CompatibleColumnDataTypeState(
					handler, element, propDefn, struct );
			state.setName( DataSetParameter.DATA_TYPE_MEMBER );
			return state;
		}

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_9
				&& element instanceof ScalarParameter
				&& IScalarParameterModel.MUCH_MATCH_PROP
						.equalsIgnoreCase( name ) )
		{
			CompatibleMustMatchState state = new CompatibleMustMatchState(
					handler, element );
			state.setName( IScalarParameterModel.MUCH_MATCH_PROP );
			return state;
		}

		return super.versionConditionalJumpTo( );
	}

}