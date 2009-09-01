/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartWizardContext;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class ExprEditComposite extends Composite implements Listener
{

	/**
	 * Text modified event.
	 */
	public static final int TEXT_MODIFIED = 49;

	private IChartWizardContext fContext;
	private Text text;
	private Button btnExprDlg;
	private final String sTitle;
	private EObject eObj;
	private EAttribute eAttr;

	public ExprEditComposite( Composite parent, String sTitle,
			IChartWizardContext fContext )
	{
		super( parent, SWT.NONE );
		this.sTitle = sTitle;
		this.fContext = fContext;
		placeComponents( );
		initListeners( );
	}

	public void bindModel( EObject eObj, EAttribute eAttr )
	{
		this.eObj = eObj;
		this.eAttr = eAttr;
		load( );
	}

	private void load( )
	{
		if ( eObj != null && eAttr != null )
		{
			text.setText( (String) eObj.eGet( eAttr ) );
		}
	}

	private void save( )
	{
		if ( eObj != null && eAttr != null )
		{
			eObj.eSet( eAttr, text.getText( ) );
		}
	}

	private void placeComponents( )
	{
		GridData gd = new GridData( GridData.FILL_BOTH );
		this.setLayoutData( gd );
		GridLayout gl = new GridLayout( 2, false );
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.verticalSpacing = 0;
		this.setLayout( gl );

		text = new Text( this, SWT.BORDER );
		{
			gd = new GridData( GridData.FILL_BOTH );
			text.setLayoutData( gd );
		}

		btnExprDlg = new Button( this, SWT.PUSH );
		{
			gd = new GridData( );
			ChartUIUtil.setChartImageButtonSizeByPlatform( gd );
			btnExprDlg.setLayoutData( gd );
			btnExprDlg.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
			btnExprDlg.setToolTipText( Messages.getString("ExprEditComposite.InvokeExpressionBuilder") ); //$NON-NLS-1$
			btnExprDlg.setEnabled( fContext.getUIServiceProvider( )
					.isInvokingSupported( ) );
			btnExprDlg.setVisible( fContext.getUIServiceProvider( )
					.isEclipseModeSupported( ) );
		}
	}

	private void initListeners( )
	{
		text.addListener( SWT.Modify, this );
		text.addListener( SWT.FocusOut, this );
		text.addListener( SWT.KeyUp, this );
		btnExprDlg.addListener( SWT.Selection, this );
	}

	public void handleEvent( Event event )
	{
		if ( event.widget == text )
		{
			if ( event.type == SWT.Modify )
			{
				save( );
			}
			else if ( event.type == SWT.FocusOut )
			{
				fireEvent( );
			}
			else if ( event.type == SWT.KeyUp )
			{
				if ( event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR )
				{
					fireEvent( );
				}
			}
		}
		else if ( event.widget == btnExprDlg )
		{
			try
			{
				String sExpr = fContext.getUIServiceProvider( )
						.invoke( IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS,
								text.getText( ),
								fContext.getExtendedItem( ),
								sTitle );
				text.setText( sExpr );
				fireEvent( );
			}
			catch ( ChartException e )
			{
				WizardBase.displayException( e );
			}
		}

	}

	private void fireEvent( )
	{
		Event eventNew = new Event( );
		eventNew.widget = this;
		eventNew.type = TEXT_MODIFIED;
		this.notifyListeners( TEXT_MODIFIED, eventNew );
	}

	public String getText( )
	{
		return text.getText( );
	}

	public void setText( String sText )
	{
		text.setText( sText );
	}

}
