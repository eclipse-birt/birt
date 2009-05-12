/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.datasource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.data.ui.util.DataUIConstants;
import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Property page to define the data source property binding. This page include
 * an editor tab for ODA data source properties. The tab shows all data source
 * properties defined by the extension.
 */
public class PropertyBindingPage extends AbstractDescriptionPropertyPage
{

	private IDesignElementModel ds;

	/**
	 * the binding propreties's name list
	 */
	private List bindingName = new ArrayList( );
	
	/**
	 * the binding properties's display name list
	 */
	private List displayName = new ArrayList( );

	/**
	 * the binding properties's value list, this list contains all binding
	 * property
	 */
	private List bindingValue = new ArrayList( );

	/**
	 * the label list used in composite
	 */
	private List nameLabelList = new ArrayList( );
	/**
	 * the text list used in composite
	 */
	private List<Text> propertyTextList = new ArrayList( );
	/**
	 * the button list used in composite
	 */
	private List buttonList = new ArrayList( );

	// This is a temporary property for data set property binding
	private final String QUERYTEXT = "queryText"; //$NON-NLS-1$
	private final String PASSWORD = "odaPassword"; //$NON-NLS-1$
	private static Logger logger = Logger.getLogger( PropertyBindingPage.class.getName( ) );
	
	private ReportElementHandle handle;

	/**
	 * the content
	 */
	public Control createContents( Composite parent )
	{
		// property binding initialize
		initPropertyBinding( );

		int size = bindingName.size( );

		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( 3, false ) );
		GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL );

		composite.setLayoutData( gridData );

		Label nameLabel;
		Text propertyText = null;
		
		// according the binding properties's size, dynamically add the
		// label,text,button group list to composite
		for ( int i = 0; i < size; i++ )
		{
			nameLabel = new Label( composite, SWT.NONE );
			nameLabel.setText( (String) displayName.get( i ) + ":" ); //$NON-NLS-1$
			nameLabelList.add( nameLabel );

			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			if ( ( (String) bindingName.get( i ) ).equals( QUERYTEXT ) )
			{
				propertyText = new Text( composite, SWT.BORDER
						| SWT.V_SCROLL | SWT.H_SCROLL );
				data.heightHint = 100;
			}
			else if ( ( (String) bindingName.get( i ) ).equals( PASSWORD ) )
			{
				propertyText = new Text( composite, SWT.BORDER | SWT.PASSWORD );
			}
			else
				propertyText = new Text( composite, SWT.BORDER );
			propertyText.setLayoutData( data );
			propertyText.setText( (String) bindingValue.get( i ) == null ? "" //$NON-NLS-1$
					: (String) bindingValue.get( i ) );
			propertyTextList.add( propertyText );

			if ( ds instanceof OdaDataSourceHandle )
			{
				handle = (OdaDataSourceHandle)ds;
				Button buildButton = new Button( composite, SWT.NONE );
				UIUtil.setExpressionButtonImage( buildButton );
				buttonList.add( buildButton );
				// add button listener
				addListener( );
				
				OdaDataSourceHandle odsh = (OdaDataSourceHandle)ds;
				Utility.setSystemHelp( composite, 
						IHelpConstants.PREFIX + "Wizard_DataSourcePropertyBinding"
						+ "("+ odsh.getExtensionID( ).replace( '.', '_' ) + ")" //'.' char will interrupt help system
						+ "_ID");

			}
			else if ( ds instanceof OdaDataSetHandle )
			{
				handle = (OdaDataSetHandle) ds;
				OdaDataSourceHandle odsh = (OdaDataSourceHandle) ( ( (OdaDataSetHandle) ds ).getDataSource( ) );
				ExpressionButton button = createExpressionButton( composite, propertyText );
				propertyText.setData( DataUIConstants.EXPR_BUTTON,
						button );
				Expression expr = handle.getPropertyBindingExpression( (String) bindingName.get( i ) );
				propertyText.setText( expr == null
						|| expr.getStringExpression( ) == null ? ""
						: expr.getStringExpression( ) );
				if ( expr != null && expr.getType( ) != null )
					propertyText.setData( DataUIConstants.EXPR_TYPE,
							expr.getType( ) );
				
				button = (ExpressionButton) propertyText.getData( DataUIConstants.EXPR_BUTTON );
				if ( button != null )
					button.refresh( );

				Utility.setSystemHelp( composite, 
						IHelpConstants.PREFIX + "Wizard_DataSetPropertyBinding"
						+ "("+ odsh.getExtensionID( ).replace( '.', '_' ) + ")" //'.' char will interrupt help system
						+ "_ID");
			}
		}
		if ( size <= 0 )
			setEmptyPropertyMessages( composite );
		return composite;
	}
	
	private ExpressionButton createExpressionButton( Composite composite, final Text property )
	{
		ExpressionButton exprButton = UIUtil.createExpressionButton( composite, SWT.PUSH );
		if ( handle == null )
		{
			handle = DesignElementFactory.getInstance( getModuleHandle( ) )
					.newOdaDataSet( null );
		}
		
		IExpressionHelper helper = new IExpressionHelper( ) {

			public String getExpression( )
			{
				if ( property != null )
					return property.getText( );
				else
					return "";
			}

			public void setExpression( String expression )
			{
				if ( property != null )
					property.setText( expression );
			}

			public void notifyExpressionChangeEvent( String oldExpression,
					String newExpression )
			{

			}

			public IExpressionProvider getExpressionProvider( )
			{
				return new ExpressionProvider( handle );
			}

			public String getExpressionType( )
			{
				return (String) property.getData( DataUIConstants.EXPR_TYPE );
			}

			public void setExpressionType( String exprType )
			{
				property.setData( DataUIConstants.EXPR_TYPE, exprType );
			}

		};
		exprButton.setExpressionHelper( helper );
		
		buttonList.add( exprButton );		
		return exprButton;
	}

	/**
	 * add button selection listener to button list
	 * 
	 */
	private void addListener( )
	{
		for ( int i = 0; i < buttonList.size( ); i++ )
		{
			Button buildButton = (Button) buttonList.get( i );
			final Text text = (Text) propertyTextList.get( i );
			final String name = (String) bindingName.get( i );
			
			buildButton.setToolTipText( Messages.getFormattedString( "PropertyBindingPage.button.tooltip",
					new Object[]{
						displayName.get( i )
					} ) ); //$NON-NLS-1$

			buildButton.addSelectionListener( new SelectionListener( ) {

				// new value from expression builder dialog

				public void widgetSelected( SelectionEvent e )
				{
					String str = ""; //$NON-NLS-1$
					// if the label is odaPassword, the text should not be
					// displayed in builder
					if ( name.equals( PASSWORD ) )
						str = ""; //$NON-NLS-1$
					else
						str = text.getText( );
					ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							str );
					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						str = dialog.getResult( );
						text.setText( str );
					}
				}

				public void widgetDefaultSelected( SelectionEvent e )
				{
					// TODO Auto-generated method stub

				}
			} );
		}
	}

	/**
	 * initial the property binding. If the property binding has not defined, the
	 * default binding will be the meta data of the property's value
	 * 
	 */
	private void initPropertyBinding( )
	{
		ds = (IDesignElementModel) getContainer( ).getModel( );
		Iterator iterator = null;
		IElementDefn elementDefn = getElementDefn( );

		if ( elementDefn != null )
		{
			iterator = elementDefn.getProperties( ).iterator( );
		}
		if ( ds instanceof DataSetHandle
				&& ( (DataSetHandle) ds ).getPropertyHandle( QUERYTEXT )
						.isVisible( ) )
		{
			bindingName.add( QUERYTEXT );
			displayName.add( Messages.getString( "PropertyBindingPage.dataset.queryText" ) ); //$NON-NLS-1$
			bindingValue.add( ( (DataSetHandle) ds ).getPropertyBinding( QUERYTEXT ) == null
					? ""
					: ( (DataSetHandle) ds ).getPropertyBinding( QUERYTEXT ) );
		}
		if ( iterator != null )
		{
			while ( iterator.hasNext( ) )
			{
				IElementPropertyDefn propertyDefn = (IElementPropertyDefn) iterator.next( );
				if ( propertyDefn instanceof IPropertyDefn
						&& propertyDefn.getValueType( ) == IPropertyDefn.ODA_PROPERTY )
				{
					String name = propertyDefn.getName( );

					if ( elementDefn != null
							&& !elementDefn.isPropertyVisible( name ) )
						continue;
					bindingName.add( name );
					displayName.add( propertyDefn.getDisplayName( ) );

					if ( ds instanceof DataSetHandle )
					{
						bindingValue.add( ( (DataSetHandle) ds ).getPropertyBinding( name ) == null
								? ""
								: ( (DataSetHandle) ds ).getPropertyBinding( name ) );
					}
					else if ( ds instanceof DataSourceHandle )
					{
						bindingValue.add( ( (DataSourceHandle) ds ).getPropertyBinding( name ) == null
								? ""
								: ( (DataSourceHandle) ds ).getPropertyBinding( name ) );
					}
					else
					{
						bindingValue.add( "" );
					}
				}
			}
		}
	}

	/**
	 * get the elementDefn of datasourceHandle|datasetHandle
	 * @return
	 */
	private IElementDefn getElementDefn( )
	{
		IElementDefn elementDefn = null;
		if ( ds instanceof DataSourceHandle )
		{
			elementDefn = ( (DataSourceHandle) ds ).getDefn( );
		}
		else if ( ds instanceof DataSetHandle )
		{
			elementDefn = ( (DataSetHandle) ds ).getDefn( );
			

		}
		return elementDefn;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		for ( int i = 0; i < bindingName.size( ); i++ )
		{
			try
			{
				String value = null;
				Text propertyText = (Text) propertyTextList.get( i );
				if ( propertyText.isDisposed( )
						|| propertyText.getText( ) == null
						|| propertyText.getText( ).trim( ).length( ) == 0 )
					value = null;
				else
					value = propertyText.getText( ).trim( );
				
				Expression expr = new Expression( value,
						(String) propertyText.getData( DataUIConstants.EXPR_TYPE ) );

				if ( ds instanceof DataSourceHandle )
					( (DataSourceHandle) ds ).setPropertyBinding( (String) bindingName.get( i ),
							value );
				else if ( ds instanceof DataSetHandle )
					( (DataSetHandle) ds ).setPropertyBinding( (String) bindingName.get( i ),
							expr );
			}
			catch ( SemanticException e )
			{
				logger.log( Level.FINE, e.getMessage( ), e );
			}
		}
		return super.performOk( );
	}
	
	/**
	 * if the dataset/datasource has no public property, set message to show
	 * this.
	 * 
	 */
	private void setEmptyPropertyMessages( Composite composite )
	{
		Label messageLabel = new Label( composite, SWT.NONE );
		if ( ds instanceof DataSourceHandle )
		{
			messageLabel.setText( Messages.getString( "PropertyBindingPage.datasource.property.empty" ) ); //$NON-NLS-1$
		}
		else if ( ds instanceof DataSetHandle )
		{
			messageLabel.setText( Messages.getString( "PropertyBindingPage.dataset.property.empty" ) ); //$NON-NLS-1$
		}
	}
	
	/**
	 * activate the property binding page
	 */
	public void pageActivated( )
	{
		getContainer( ).setMessage( Messages.getString( "datasource.editor.property" ),//$NON-NLS-1$
				IMessageProvider.NONE );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.data.ui.property.AbstractPropertyPage#getToolTip()
	 */
	public String getToolTip( )
	{
		return Messages.getString( "PropertyBindingPage.property.tooltip" ); //$NON-NLS-1$
	}
	
	private ModuleHandle getModuleHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
	}

}
