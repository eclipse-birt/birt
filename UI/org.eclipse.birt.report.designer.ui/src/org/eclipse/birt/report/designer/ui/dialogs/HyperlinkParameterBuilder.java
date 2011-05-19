/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class HyperlinkParameterBuilder extends BaseDialog
{

	public static final String HYPERLINK_PARAMETER = "HyperlinkParameter"; //$NON-NLS-1$
	public static final String TARGET_REPORT = "TargetReport"; //$NON-NLS-1$
	public static final String PARAMETER_HANDLE = "ParameterHandle"; //$NON-NLS-1$
	public static final String PARAMETER_VALUE = "ParameterValue"; //$NON-NLS-1$
	private String[] items;
	private Combo paramChooser;
	private HyperlinkBuilder hyperlinkBuilder;
	private Composite valueControl;
	private Label valueLabel;
	private Composite container;

	public void setHyperlinkBuilder( HyperlinkBuilder hyperlinkBuilder )
	{
		this.hyperlinkBuilder = hyperlinkBuilder;
	}

	protected HyperlinkParameterBuilder( String title )
	{
		super( title );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );

		container = new Composite( composite, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.minimumHeight = 80;
		container.setLayoutData( gd );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		layout.verticalSpacing = 10;
		container.setLayout( layout );

		new Label( container, SWT.NONE ).setText( Messages.getString("HyperlinkParameterBuilder.Label.Parameter") ); //$NON-NLS-1$
		paramChooser = new Combo( container, SWT.BORDER | SWT.READ_ONLY );

		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 250;
		paramChooser.setLayoutData( gd );

		paramChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				updateValueControl( );
			}

		} );

		valueLabel = new Label( container, SWT.NONE );
		valueLabel.setText( Messages.getString("HyperlinkParameterBuilder.Label.Value") ); //$NON-NLS-1$
		gd = new GridData( );
		gd.exclude = true;
		valueLabel.setLayoutData( gd );
		valueLabel.setVisible( false );
		// UIUtil.bindHelp( parent, IHelpContextIds.EXPRESSION_EDITOR_ID );

		populateComboBoxItems( );

		return composite;
	}

	protected void updateValueControl( )
	{
		if ( hyperlinkBuilder != null )
		{
			Object object = hyperlinkBuilder.getParameter( paramChooser.getText( ) );
			if ( valueControl != null && !valueControl.isDisposed( ) )
			{
				valueControl.dispose( );
			}

			if ( object instanceof ScalarParameterHandle || object == null )
			{
				GridData gd = (GridData) valueLabel.getLayoutData( );
				gd.exclude = false;
				valueLabel.setLayoutData( gd );
				valueLabel.setVisible( true );

				valueControl = new Composite( container, SWT.NONE );
				gd = new GridData( GridData.FILL_HORIZONTAL );
				valueControl.setLayoutData( gd );

				GridLayout layout = new GridLayout( );
				layout.marginWidth = layout.marginHeight = 0;
				layout.numColumns = 2;
				valueControl.setLayout( layout );

				text = new Text( valueControl, SWT.BORDER );
				text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

				ExpressionButtonUtil.createExpressionButton( valueControl,
						text,
						hyperlinkBuilder.getExpressionProvider( ),
						handle == null ? null : handle.getElementHandle( ) );
				if ( paramBinding != null )
				{
					ExpressionButtonUtil.initExpressionButtonControl( text,
							hyperlinkBuilder.getParamBindingExpression( paramBinding ) );
				}
			}
			else
			{
				valueEditor = createValueEditor( container, object );
				if ( valueEditor == null )
				{
					GridData gd = (GridData) valueLabel.getLayoutData( );
					gd.exclude = true;
					valueLabel.setLayoutData( gd );
					valueLabel.setVisible( false );
				}
				else
				{
					GridData gd = (GridData) valueLabel.getLayoutData( );
					gd.exclude = false;
					valueLabel.setLayoutData( gd );
					valueLabel.setVisible( true );
					valueEditor.getControl( )
							.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
					valueEditor.setProperty( PARAMETER_HANDLE, object );
					valueEditor.setProperty( TARGET_REPORT,
							hyperlinkBuilder.getTargetReportFile( ) );
					valueEditor.setProperty( PARAMETER_VALUE,
							paramBinding == null ? null
									: hyperlinkBuilder.getParamBindingExpression( paramBinding ) );
					valueEditor.update( true );
					valueControl = (Composite) valueEditor.getControl( );
				}
			}

			container.layout( );
		}
	}

	private IDialogHelper createValueEditor( Composite parent, Object parameter )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( parameter,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null )
				{
					final IDialogHelper helper = helperProvider.createHelper( this,
							HYPERLINK_PARAMETER );
					if ( helper != null )
					{
						helper.createContent( parent );
						return helper;
					}
				}
			}
		}
		return null;
	}

	protected void okPressed( )
	{
		if ( paramBinding != null )
		{
			List<Expression> expressions = new ArrayList<Expression>( );
			if ( text != null && !text.isDisposed( ) )
			{
				expressions.add( ExpressionButtonUtil.getExpression( text ) );
				paramBinding.setExpression( expressions );
			}
			else if ( valueEditor != null
					&& !valueEditor.getControl( ).isDisposed( ) )
			{
				valueEditor.update( false );
				expressions.add( (Expression) valueEditor.getProperty( PARAMETER_VALUE ) );
				paramBinding.setExpression( expressions );
			}
		}
		else
		{
			ParamBinding paramBinding = StructureFactory.createParamBinding( );
			paramBinding.setParamName( paramChooser.getText( ) );
			List<Expression> expressions = new ArrayList<Expression>( );
			if ( text != null && !text.isDisposed( ) )
			{
				expressions.add( ExpressionButtonUtil.getExpression( text ) );
				paramBinding.setExpression( expressions );
			}
			else if ( valueEditor != null
					&& !valueEditor.getControl( ).isDisposed( ) )
			{
				valueEditor.update( false );
				expressions.add( (Expression) valueEditor.getProperty( PARAMETER_VALUE ) );
				paramBinding.setExpression( expressions );
			}
			setResult( paramBinding );
		}
		super.okPressed( );
	}

	private ParamBinding paramBinding;

	public void setParamBinding( ParamBinding paramBinding )
	{
		this.paramBinding = paramBinding;
		if ( paramBinding != null )
		{
			this.items = new String[]{
				paramBinding.getParamName( )
			};
		}
	}

	public void setItems( String[] items )
	{
		this.items = items;
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems( )
	{
		if ( paramChooser != null && items != null )
		{
			paramChooser.removeAll( );
			for ( int i = 0; i < items.length; i++ )
				paramChooser.add( items[i], i );
			if ( items.length > 0 )
			{
				paramChooser.select( 0 );
				updateValueControl( );
			}
			if ( paramBinding != null )
				paramChooser.setEnabled( false );
		}
	}

	private ActionHandle handle;
	private Text text;
	private IDialogHelper valueEditor;

	public void setActionHandle( ActionHandle handle )
	{
		this.handle = handle;
	}
}
