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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.GuiExtensionManager;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage.MessagePageGenerator;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage.ModelClassWrapper;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * AttributesBuilder provides methods to create attribute page Generator
 * correspond to the current selection. It keeps the current IPageGenerator
 * instance and re-uses it when selection changed & the selected type kept the
 * same.
 */
public class AttributesBuilder
{

	/**
	 * The current attribute page generator
	 */
	protected IPageGenerator pageGenerator;

	protected String selectedTabText;

	private static final String DEFAULT_NONE = Messages.getString( "AttributesBuilder.Label.None" ); //$NON-NLS-1$; 
	/**
	 * The type information of current selection.
	 */
	protected String typeInfo = DEFAULT_NONE;

	/**
	 * Gets attribute pages generator correspond to the current selection.
	 * 
	 * @param selection
	 *            The current selection.
	 */
	public IPageGenerator getPageGenerator( List selection )
	{
		if ( pageGenerator != null && pageGenerator instanceof TabPageGenerator )
		{
			selectedTabText = ( (TabPageGenerator) pageGenerator ).getSelectedTabText( );
		}
		Class pageGeneratorClass = TabPageGenerator.class;
		String oldTypeInfo = typeInfo;
		if ( isSameType( selection ) == true )
		{
			pageGeneratorClass = getGeneratorClass( selection.get( 0 ) );

			Object element = null;
			element = selection.get( 0 );
			Object adapter = ElementAdapterManager.getAdapter( element,
					IPageGenerator.class );
			if ( adapter instanceof IPageGenerator )
			{
				if ( element instanceof ExtendedItemHandle )
				{
					typeInfo = Messages.getFormattedString( "AttributesBuilder.Label.Generic", new String[]{GuiExtensionManager.getExtensionDisplayName( selection.get( 0 ) )} ); //$NON-NLS-1$
				}
				if ( DEFAULT_NONE.equals( typeInfo ) )
				{
					if ( element instanceof DesignElementHandle )
					{
						String displayName = ( (DesignElementHandle) element ).getDefn( )
								.getDisplayName( );
						if ( displayName != null && !displayName.equals( "" ) )
						{
							typeInfo = Messages.getFormattedString( "AttributesBuilder.Label.Generic", new String[]{displayName} );//$NON-NLS-1$
						}
					}
				}
				IPageGenerator ng = (IPageGenerator) adapter;

				boolean change = false;
				if ( pageGenerator == null
						|| ( ng != null && pageGenerator.getClass( ) != ng.getClass( ) ) )
				{
					change = true;
				}
				else if ( pageGenerator != null )
				{
					Object input = pageGenerator.getInput( );
					change = isChange( element, input, change, pageGenerator );
				}

				if ( change )
				{
					if ( pageGenerator != null
							&& pageGenerator.getControl( ) != null
							&& !pageGenerator.getControl( ).isDisposed( ) )
					{
						if ( pageGenerator instanceof AbstractPageGenerator )
						{
							( (AbstractPageGenerator) pageGenerator ).dispose( );
						}
						pageGenerator.getControl( ).dispose( );
					}
					pageGenerator = ng;
					if ( pageGenerator instanceof TabPageGenerator )
					{
						( (TabPageGenerator) pageGenerator ).setSelectedTabText( selectedTabText );
					}
				}
				return pageGenerator;
			}

		}
		try
		{
			if ( pageGenerator != null
					&& ( pageGenerator.getClass( ) == pageGeneratorClass ) )
			{
				if ( pageGenerator instanceof CategoryPageGenerator )
				{
					if ( typeInfo.equals( oldTypeInfo ) )
					{
						return pageGenerator;
					}
				}
				else
				{
					return pageGenerator;
				}
			}
			if ( pageGenerator != null
					&& pageGenerator.getControl( ) != null
					&& !pageGenerator.getControl( ).isDisposed( ) )
			{
				if ( pageGenerator instanceof AbstractPageGenerator )
				{
					( (AbstractPageGenerator) pageGenerator ).dispose( );
				}
				pageGenerator.getControl( ).dispose( );
			}
			pageGenerator = (IPageGenerator) pageGeneratorClass.newInstance( );
			if ( pageGenerator instanceof TabPageGenerator )
			{
				( (TabPageGenerator) pageGenerator ).setSelectedTabText( selectedTabText );
			}
		}
		catch ( Exception e )
		{
			ExceptionUtil.handle( e );
		}
		return pageGenerator;
	}

	public static boolean isChange( Object element, Object input, boolean change, IPageGenerator generator )
	{
		if ( input != null )
		{
			input = DEUtil.getInputFirstElement( input );
		}
		if ( input == null )
		{
			change = true;
		}
		else if ( element instanceof ExtendedItemHandle
				&& input instanceof ExtendedItemHandle )
		{
			if ( !( (ExtendedItemHandle) element ).getExtensionName( )
					.equals( ( (ExtendedItemHandle) input ).getExtensionName( ) ) )
			{
				change = true;
			}
		}
		else if ( element.getClass( ) != input.getClass( ) )
		{
			if ( generator != null && generator instanceof TabPageGenerator )
			{
				change = ( (TabPageGenerator) generator ).isChange( element );
			}
			else
			{
				change = true;
			}
		}
		return change;
	}

	/**
	 * Gets the IPageGenerator class corresponding to the given DE type.
	 * 
	 * @param type
	 *            The DE element type
	 * @return The class of the IPageGenerator
	 */
	protected Class getGeneratorClass( Object obj )
	{
		Class type = obj.getClass( );

		if ( type == TableHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Table" ); //$NON-NLS-1$
			return ListPageGenerator.class;
		}
		else if ( type == ImageHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Picture" ); //$NON-NLS-1$
			return TextPageGenerator.class;
		}
		else if ( type == TextItemHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Text" ); //$NON-NLS-1$
			return TextPageGenerator.class;
		}
		else if ( type == TextDataHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.TextData" ); //$NON-NLS-1$
			return TextPageGenerator.class;
		}
		else if ( type == ColumnHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Column" ); //$NON-NLS-1$
			return ColumnPageGenerator.class;
		}
		else if ( type == RowHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Row" ); //$NON-NLS-1$
			return ColumnPageGenerator.class;
		}
		else if ( type == GridHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Grid" ); //$NON-NLS-1$
			return GridPageGenerator.class;
		}
		else if ( type == DataItemHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Data" ); //$NON-NLS-1$
			return DataPageGenerator.class;
		}
		else if ( type == CellHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Cell" ); //$NON-NLS-1$
			return ColumnPageGenerator.class;
		}
		else if ( type == ListHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.List" ); //$NON-NLS-1$
			return ListPageGenerator.class;
		}
		else if ( CubeHandle.class.isAssignableFrom( type ) )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Cube" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == DynamicFilterParameterHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.DynamicFilterParameter" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == ModelClassWrapper.class )
		{
			// this is to populate the wrapped type info
			getGeneratorClass( ( (ModelClassWrapper) obj ).getElement( ) );

			return MessagePageGenerator.class;
		}
		else if ( type == LabelHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Label" ); //$NON-NLS-1$		
			return LabelPageGenerator.class;
		}
		else if ( type == SimpleMasterPageHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.MasterPage" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( DataSourceHandle.class.isAssignableFrom( type ) )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.DataSourceHandle" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( VariableElementHandle.class.isAssignableFrom( type ) )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.VariableElementHandle" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( DataSetHandle.class.isAssignableFrom( type ) )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.DataSetHandle" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == ScalarParameterHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.ScalarParameterHandle" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == TemplateReportItemHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.TemplateReportItemHandle" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == ReportDesignHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Report" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == LibraryHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.Library" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == AutoTextHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.AutoText" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == ParameterGroupHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.ParameterGroup" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( type == CascadingParameterGroupHandle.class )
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.CascadingParameterGroup" ); //$NON-NLS-1$
			return CategoryPageGenerator.class;
		}
		else if ( obj instanceof ModuleHandle )
		{
			typeInfo = Messages.getFormattedString( "AttributesBuilder.Label.Generic", new String[]{( (DesignElementHandle) obj ).getDefn( ) //$NON-NLS-1$
								.getDisplayName( )
					} );
			return CategoryPageGenerator.class;
		}
		else
		{
			typeInfo = Messages.getString( "AttributesBuilder.Label.None" ); //$NON-NLS-1$
			return TabPageGenerator.class;
		}
	}

	/**
	 * Gets the type information of current selection.
	 * 
	 * @return A String represent the type information.
	 */
	public String getTypeInfo( )
	{
		return typeInfo;
	}

	/**
	 * Judges if elements in the list are the same type.
	 * 
	 * @param selection
	 *            The current selection.
	 * @return If the elements in the list have the same type, return true else
	 *         return false. if the list is null or has no element then return
	 *         false.
	 */
	protected boolean isSameType( List selection )
	{
		if ( selection == null || selection.size( ) == 0 )
			return false;

		if ( selection.get( 0 ) == null )
		{
			return false;
		}

		if ( selection.get( 0 ) instanceof ExtendedItemHandle )
		{
			String extName = ( (ExtendedItemHandle) selection.get( 0 ) ).getExtensionName( );
			for ( int i = 1; i < selection.size( ); i++ )
			{
				if ( !( selection.get( i ) instanceof ExtendedItemHandle ) )
				{
					return false;
				}
				String extName2 = ( (ExtendedItemHandle) selection.get( i ) ).getExtensionName( );
				if ( !extName.equals( extName2 ) )
				{
					return false;
				}
			}
		}
		else
		{
			Class classObj = selection.get( 0 ).getClass( );
			for ( int i = 1; i < selection.size( ); i++ )
			{
				if ( selection.get( i ).getClass( ) != classObj )
				{
					return false;
				}

			}
		}

		return true;
	}
}
