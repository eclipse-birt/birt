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

package org.eclipse.birt.report.designer.internal.ui.dialogs.expression;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.MenuButton;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

public class ExpressionButton
{

	private MenuButton button;

	private SelectionAdapter listener = new SelectionAdapter( ) {

		public void widgetSelected( SelectionEvent e )
		{
			Widget widget = e.widget;
			if ( widget instanceof MenuItem )
			{
				String exprType = (String) widget.getData( );
				setExpressionType( exprType );
				provider.handleSelectionEvent( exprType );
				refresh( );
			}
			else if ( widget instanceof MenuButton )
			{
				provider.handleSelectionEvent( getExpressionType( ) );
			}
		}

	};

	public ExpressionButton( Composite parent, int style )
	{
		button = new MenuButton( parent, style );
		button.addSelectionListener( listener );

		menu = new Menu( parent.getShell( ), SWT.POP_UP );
		button.setDropDownMenu( menu );

		setExpressionButtonProvider( new ExpressionButtonProvider( ) );
		refresh( );
	}

	public void setEnabled( boolean enable )
	{
		button.setEnabled( enable );
	}

	public boolean isEnabled( )
	{
		return button.isEnabled( );
	}

	public MenuButton getControl( )
	{
		return button;
	}

	protected void setExpressionType( String exprType )
	{
		if ( helper != null && !exprType.equals( helper.getExpressionType( ) ) )
			helper.setExpressionType( exprType );
	}

	protected String getExpressionType( )
	{
		String type = null;
		if ( helper != null )
		{
			type = helper.getExpressionType( );
		}
		return type != null ? type : ExpressionType.JAVASCRIPT;
	}

	public String getExpression( )
	{
		if ( helper != null )
			return helper.getExpression( );
		return "";
	}

	public void setExpression( String expression )
	{
		if ( expression != null && helper != null )
			helper.setExpression( expression );
	}

	protected void openExpressionBuilder( )
	{
		ExpressionBuilder builder = new ExpressionBuilder( getExpression( ) );
		if ( helper != null )
			builder.setExpressionProvier( helper.getExpressionProvider( ) );
		if ( builder.open( ) == Window.OK )
		{
			if ( helper != null )
			{
				String oldExpression = getExpression( );
				String newExpression = builder.getResult( );
				helper.setExpression( newExpression );
				notifyExpressionChangeEvent( oldExpression, newExpression );
			}
		}
	}

	public void notifyExpressionChangeEvent( String oldExpression,
			String newExpression )
	{
		if ( helper != null )
			helper.notifyExpressionChangeEvent( oldExpression, newExpression );
	}

	private IExpressionHelper helper;

	public void setExpressionHelper( IExpressionHelper helper )
	{
		this.helper = helper;
	}

	public void refresh( )
	{
		button.setImage( provider.getImage( getExpressionType( ) ) );
		button.setToolTipText( provider.getTooltipText( getExpressionType( ) ) );
	}

	private IExpressionButtonProvider provider;

	private Menu menu;

	public void setExpressionButtonProvider( IExpressionButtonProvider provider )
	{
		if ( provider != null && provider != this.provider )
		{
			this.provider = provider;

			provider.setInput( this );

			for ( int i = 0; i < menu.getItemCount( ); i++ )
			{
				menu.getItem( i ).dispose( );
				i--;
			}

			String[] types = this.provider.getExpressionTypes( );
			for ( int i = 0; i < types.length; i++ )
			{
				MenuItem item = new MenuItem( menu, SWT.PUSH );
				item.setText( this.provider.getText( types[i] ) );
				item.setData( types[i] );
				item.setImage( this.provider.getImage( types[i] ) );
				item.addSelectionListener( listener );
			}
			refresh( );
		}
	}
}
