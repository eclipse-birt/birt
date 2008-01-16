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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ParameterBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

public class BindingGroupDescriptorProvider implements IDescriptorProvider
{

	public String getDisplayName( )
	{
		return null;
	}

	protected List getAvailableDataBindingReferenceList(ReportItemHandle element){
		return element.getAvailableDataSetBindingReferenceList( );
	}
	
	public Object load( )
	{
		ReportItemHandle element = getReportItemHandle( );
		int type = element.getDataBindingType( );
		List referenceList = getAvailableDataBindingReferenceList(element);
		references = new String[referenceList.size( ) + 1];
		references[0] = NONE;
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			references[i + 1] = ( (ReportItemHandle) referenceList.get( i ) ).getQualifiedName( );
		}
		Object value;
		switch ( type )
		{
			case ReportItemHandle.DATABINDING_TYPE_DATA :
				DataSetHandle dataset = element.getDataSet( );
				if ( dataset == null )
					value = NONE;
				else
					value = dataset.getQualifiedName( );
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
				ReportItemHandle reference = element.getDataBindingReference( );
				if ( reference == null )
					value = NONE;
				else
					value = reference.getQualifiedName( );
				break;
			default :
				value = NONE;
		}
		BindingInfo info = new BindingInfo( type, value );
		return info;
	}

	public void save( Object saveValue ) throws SemanticException
	{
		if ( saveValue instanceof BindingInfo )
		{
			BindingInfo info = (BindingInfo) saveValue;
			int type = info.getBindingType( );
			String value = info.getBindingValue( ).toString( );
			switch ( type )
			{
				case ReportItemHandle.DATABINDING_TYPE_DATA :
					if ( value.equals( NONE ) )
					{
						value = null;
					}
					int ret = 0;
					if ( !NONE.equals( ( (BindingInfo) load( ) ).getBindingValue( )
							.toString( ) )
							|| getReportItemHandle( ).getColumnBindings( )
									.iterator( )
									.hasNext( ) )
					{
						MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.changeDataSet" ),//$NON-NLS-1$
								MessageDialog.INFORMATION,
								new String[]{
										Messages.getString( "AttributeView.dialg.Message.Yes" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.No" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.Cancel" )}, 0 );//$NON-NLS-1$

						ret = prefDialog.open( );
					}

					switch ( ret )
					{
						// Clear binding info
						case 0 :
							resetDataSetReference( value, true );
							break;
						// Doesn't clear binding info
						case 1 :
							resetDataSetReference( value, false );
							break;
						// Cancel.
						case 2 :
							section.load( );
					}
					break;
				case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
					if ( value.equals( NONE ) )
					{
						value = null;
					}
					int ret1 = 0;
					if ( !NONE.equals( ( (BindingInfo) load( ) ).getBindingValue( )
							.toString( ) )
							|| getReportItemHandle( ).getColumnBindings( )
									.iterator( )
									.hasNext( ) )
					{
						MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.changeDataSet" ),//$NON-NLS-1$
								MessageDialog.INFORMATION,
								new String[]{
										Messages.getString( "AttributeView.dialg.Message.Yes" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.Cancel" )}, 0 );//$NON-NLS-1$

						ret1 = prefDialog.open( );
					}

					switch ( ret1 )
					{
						// Clear binding info
						case 0 :
							resetReference( value, true );
							break;
						// Cancel.
						case 1 :
							section.load( );
					}
			}
		}
	}
	private Object input;

	private String[] references;

	public String[] getReferences( )
	{
		return references;
	}

	public String[] getAvailableDatasetItems( )
	{
		String[] dataSets = ChoiceSetFactory.getDataSets( );
		String[] newList = new String[dataSets.length + 1];
		newList[0] = NONE;
		System.arraycopy( dataSets, 0, newList, 1, dataSets.length );
		return newList;
	}

	public void setInput( Object input )
	{
		this.input = input;
	}

	public Object getInput( )
	{
		return input;
	}

	public static class BindingInfo
	{

		private int bindingType;
		private Object bindingValue;

		public BindingInfo( int type, Object value )
		{
			this.bindingType = type;
			this.bindingValue = value;
		}

		public BindingInfo( )
		{
		}

		public int getBindingType( )
		{
			return bindingType;
		}

		public Object getBindingValue( )
		{
			return bindingValue;
		}

		public void setBindingType( int bindingType )
		{
			this.bindingType = bindingType;
		}

		public void setBindingValue( Object bindingValue )
		{
			this.bindingValue = bindingValue;
		}

	}

	public static final String NONE = Messages.getString( "BindingPage.None" );

	public boolean isEnable( )
	{
		if ( DEUtil.getInputSize( input ) != 1 )
		{
			return false;
		}
		return true;
	}

	private void resetDataSetReference( Object value, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			getReportItemHandle( ).setDataBindingReference( null );
			DataSetHandle dataSet = null;
			if ( value != null )
			{
				dataSet = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findDataSet( value.toString( ) );
			}
			if ( getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				getReportItemHandle( ).setDataBindingReference( null );
			}
			getReportItemHandle( ).setDataSet( dataSet );
			if ( clearHistory )
			{
				getReportItemHandle( ).getColumnBindings( ).clearValue( );
				getReportItemHandle( ).getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
						.clearValue( );
			}
			dataSetProvider.generateAllBindingColumns( );

			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		section.load( );
	}

	private void resetReference( Object value, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			ReportItemHandle element = null;
			if ( value != null )
			{
				element = (ReportItemHandle) SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findElement( value.toString( ) );
			}
			getReportItemHandle( ).setDataBindingReference( element );
			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		section.load( );
	}

	public ReportItemHandle getReportItemHandle( )
	{
		return (ReportItemHandle) DEUtil.getInputFirstElement( input );
	}

	public boolean isBindingReference( )
	{
		return getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
	}

	/**
	 * Gets the DE CommandStack instance
	 * 
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	private void startTrans( String name )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).startTrans( name );
		}
	}

	private void commit( )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).commit( );
		}
	}

	private void rollback( )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).rollback( );
		}
	}

	/**
	 * @return Returns the enableAutoCommit.
	 */
	public boolean isEnableAutoCommit( )
	{
		return enableAutoCommit;
	}

	/**
	 * @param enableAutoCommit
	 *            The enableAutoCommit to set.
	 */
	public void setEnableAutoCommit( boolean enableAutoCommit )
	{
		this.enableAutoCommit = enableAutoCommit;
	}

	private transient boolean enableAutoCommit = true;

	DataSetColumnBindingsFormHandleProvider dataSetProvider;

	public void setDependedProvider(
			DataSetColumnBindingsFormHandleProvider provider )
	{
		this.dataSetProvider = provider;
	}

	BindingGroupSection section;

	public void setRefrenceSection( BindingGroupSection section )
	{
		this.section = section;
	}

	public void bindingDialog( )
	{
		ParameterBindingDialog dialog = new ParameterBindingDialog( UIUtil.getDefaultShell( ),
				getReportItemHandle( ) );
		startTrans( "" ); //$NON-NLS-1$
		if ( dialog.open( ) == Window.OK )
		{
			commit( );
		}
		else
		{
			rollback( );
		}
	}

	public String getText( int key )
	{
		switch ( key )
		{
			case 0 :
				return Messages.getString( "BindingPage.Dataset.Label" );
			case 1 :
				return Messages.getString( "parameterBinding.title" );
			case 2 :
				return Messages.getString( "BindingPage.ReportItem.Label" );
		}
		return "";
	}
}
