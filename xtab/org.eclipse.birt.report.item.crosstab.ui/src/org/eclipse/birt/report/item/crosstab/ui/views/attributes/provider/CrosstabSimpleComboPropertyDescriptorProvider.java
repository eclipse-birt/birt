/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.CrosstabSimpleComboSection;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 */
public class CrosstabSimpleComboPropertyDescriptorProvider extends
		SimpleComboPropertyDescriptorProvider
{

	private static final String NONE = Messages.getString( "BindingPage.None" ); //$NON-NLS-1$

	public CrosstabSimpleComboPropertyDescriptorProvider( String property,
			String element )
	{
		super( property, element );
	}

	public String[] getItems( )
	{
		String[] items = null;
		items = super.getItems( );
		if ( items != null )
		{
			return items;
		}
		Object selecteObj = input;
		if ( input instanceof List )
		{
			selecteObj = ( (List) input ).get( 0 );
		}

		ExtendedItemHandle handle = (ExtendedItemHandle) selecteObj;
		if ( !handle.getExtensionName( ).equals( "Crosstab" ) ) //$NON-NLS-1$
		{
			return items;
		}

		String[] tmpItems = null;
		if ( IReportItemModel.CUBE_PROP.equals( getProperty( ) ) )
		{
			tmpItems = ChoiceSetFactory.getCubes( );
		}

		items = new String[tmpItems.length + 1];
		items[0] = Messages.getString( "ChoiceSetFactory.choice.None" ); //$NON-NLS-1$
		System.arraycopy( tmpItems, 0, items, 1, tmpItems.length );

		return items;
	}

	public boolean isSpecialProperty( )
	{
		if ( IReportItemModel.CUBE_PROP.equals( getProperty( ) ) )
		{
			return true;
		}
		else
			return super.isSpecialProperty( );

	}

	public String getDisplayName( )
	{
		if ( IReportItemModel.CUBE_PROP.equals( getProperty( ) ) )
		{
			return Messages.getString( "Element.ReportElement.Cube" ); //$NON-NLS-1$
		}
		else
		{
			return super.getDisplayName( );
		}
	}

	public void save( Object value ) throws SemanticException
	{
		int ret = 0;
		// If choose binding Cube as None
		if ( !NONE.equals( getCubeName( ) ) )
		{
			MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
					Messages.getString( "CrosstabDataBinding.title.ChangeCube" ),//$NON-NLS-1$
					null,
					Messages.getString( "CrosstabDataBinding.message.changeCube" ),//$NON-NLS-1$
					MessageDialog.INFORMATION,
					new String[]{
							org.eclipse.birt.report.designer.nls.Messages.getString( "AttributeView.dialg.Message.Yes" ),//$NON-NLS-1$
							org.eclipse.birt.report.designer.nls.Messages.getString( "AttributeView.dialg.Message.No" ),//$NON-NLS-1$
							org.eclipse.birt.report.designer.nls.Messages.getString( "AttributeView.dialg.Message.Cancel" )}, 0 );//$NON-NLS-1$

			ret = prefDialog.open( );
			switch ( ret )
			{
				// Clear binding info
				case 0 :
					resetCubeReference( value, true );
					break;
				// Doesn't clear binding info
				case 1 :
					resetCubeReference( value, false );
					break;
				// Cancel.
				case 2 :
					section.getSimpleComboControl( )
							.setStringValue( getCubeName( ) );
					break;
			}
		}
		else
		{
			resetCubeReference( value, false );
		}
		// super.save( value );
	}

	private String getCubeName( )
	{
		String cubeName;
		if ( getExtendedItemHandle( ).getCube( ) == null )
		{
			cubeName = NONE;
		}
		else
		{
			cubeName = getExtendedItemHandle( ).getCube( ).getQualifiedName( );
		}
		if ( StringUtil.isBlank( cubeName ) )
		{
			cubeName = NONE;
		}
		return cubeName;
	}

	private void resetCubeReference( Object value, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			CubeHandle cubeHandle = null;
			if ( value != null )
			{
				cubeHandle = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findCube( value.toString( ) );
			}
			getExtendedItemHandle( ).setCube( cubeHandle );
			if ( clearHistory )
			{
				getExtendedItemHandle( ).getColumnBindings( ).clearValue( );
				getExtendedItemHandle( ).getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
						.clearValue( );
			}
			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		load( );
	}

	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	private ExtendedItemHandle getExtendedItemHandle( )
	{
		return (ExtendedItemHandle) DEUtil.getInputFirstElement( input );
	}

	private void startTrans( String name )
	{
		getActionStack( ).startTrans( name );
	}

	private void commit( )
	{
		getActionStack( ).commit( );
	}

	private void rollback( )
	{
		getActionStack( ).rollback( );
	}

	private CrosstabSimpleComboSection section;

	public void setCrosstabSimpleComboSection(
			CrosstabSimpleComboSection section )
	{
		this.section = section;
	}
}
