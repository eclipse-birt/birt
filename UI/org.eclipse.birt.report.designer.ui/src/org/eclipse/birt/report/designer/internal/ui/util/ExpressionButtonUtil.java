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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionHelper;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class ExpressionButtonUtil
{

	public static final String EXPR_BUTTON = "exprButton";//$NON-NLS-1$
	public static final String EXPR_TYPE = "exprType";//$NON-NLS-1$

	public static ExpressionButton createExpressionButton( Composite parent,
			final Control control, final IExpressionProvider provider )
	{
		return createExpressionButton( parent,
				control,
				provider,
				null,
				false,
				SWT.PUSH );
	}

	public static ExpressionButton createExpressionButton( Composite parent,
			final Control control, final IExpressionProvider provider,
			Listener listener )
	{
		return createExpressionButton( parent,
				control,
				provider,
				null,
				false,
				SWT.PUSH );
	}

	public static ExpressionButton createExpressionButton( Composite parent,
			Control control, IExpressionProvider provider, int style )
	{
		return createExpressionButton( parent,
				control,
				provider,
				null,
				false,
				SWT.PUSH );
	}

	public static ExpressionButton createExpressionButton( Composite parent,
			Control control, IExpressionProvider provider,
			boolean allowConstant, int style )
	{
		return createExpressionButton( parent,
				control,
				provider,
				null,
				allowConstant,
				SWT.PUSH );
	}

	public static ExpressionButton createExpressionButton( Composite parent,
			final Control control, final IExpressionProvider provider,
			final Listener listener, boolean allowConstant, int style )
	{

		final ExpressionButton button = UIUtil.createExpressionButton( parent,
				style,
				allowConstant );
		IExpressionHelper helper = new IExpressionHelper( ) {

			public String getExpression( )
			{
				if ( control instanceof Text )
				{
					return ( (Text) control ).getText( );
				}
				else if ( control instanceof Combo )
				{
					return ( (Combo) control ).getText( );
				}
				return "";
			}

			public void notifyExpressionChangeEvent( String oldExpression,
					String newExpression )
			{
				if ( listener != null )
				{
					Event event = new Event( );
					event.widget = button.getControl( );
					event.data = new String[]{
							oldExpression, newExpression
					};
					event.detail = SWT.Modify;
					listener.handleEvent( event );
				}
				control.setFocus( );
			}

			public void setExpression( String expression )
			{
				if ( control instanceof Text )
				{
					( (Text) control ).setText( DEUtil.resolveNull( expression ) );
				}
				else if ( control instanceof Combo )
				{
					( (Combo) control ).setText( DEUtil.resolveNull( expression ) );
				}
				else if ( control instanceof CCombo )
				{
					( (CCombo) control ).setText( DEUtil.resolveNull( expression ) );
				}
			}

			public IExpressionProvider getExpressionProvider( )
			{
				return provider;
			}

			public String getExpressionType( )
			{
				return (String) control.getData( EXPR_TYPE );
			}

			public void setExpressionType( String exprType )
			{
				control.setData( EXPR_TYPE, exprType );
			}

		};

		button.setExpressionHelper( helper );

		control.setData( EXPR_BUTTON, button );
		control.setData( ExpressionButtonUtil.EXPR_TYPE,
				UIUtil.getDefaultScriptType( ) );
		button.refresh( );

		return button;
	}

	public static boolean isSupportJavaScript( ExpressionButton button )
	{
		return button.isSupportType( ExpressionType.JAVASCRIPT );
	}

	public static void initJSExpressionButtonCombo( final Combo combo )
	{
		Object button = combo.getData( ExpressionButtonUtil.EXPR_BUTTON );
		if ( button instanceof ExpressionButton && combo instanceof Combo )
		{
			if ( !( (ExpressionButton) button ).isSupportType( ExpressionType.JAVASCRIPT ) )
			{
				combo.removeAll( );
			}

			combo.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					combo.setData( ExpressionButtonUtil.EXPR_TYPE,
							ExpressionType.JAVASCRIPT );
					Object button = combo.getData( ExpressionButtonUtil.EXPR_BUTTON );
					if ( button instanceof ExpressionButton )
					{
						( (ExpressionButton) button ).refresh( );
					}
				}

			} );
		}
	}

	public static void initExpressionButtonControl( Control control,
			Object element, String property )
	{
		ExpressionHandle value = null;

		if ( element instanceof DesignElementHandle )
		{
			value = ( (DesignElementHandle) element ).getExpressionProperty( property );
		}
		else if ( element instanceof StructureHandle )
		{
			value = ( (StructureHandle) element ).getExpressionProperty( property );
		}
		else if ( element instanceof Structure )
		{
			value = ( (StructureHandle) element ).getExpressionProperty( property );
		}

		String stringValue = value == null || value.getExpression( ) == null ? "" : (String) value.getExpression( ); //$NON-NLS-1$

		if ( control instanceof Text )
		{
			( (Text) control ).setText( stringValue );
		}
		else if ( control instanceof Combo )
		{
			( (Combo) control ).setText( stringValue );
		}
		else if ( control instanceof CCombo )
		{
			( (CCombo) control ).setText( stringValue );
		}

		control.setData( ExpressionButtonUtil.EXPR_TYPE, value == null
				|| value.getType( ) == null ? UIUtil.getDefaultScriptType( )
				: (String) value.getType( ) );

		Object button = control.getData( ExpressionButtonUtil.EXPR_BUTTON );
		if ( button instanceof ExpressionButton )
		{
			( (ExpressionButton) button ).refresh( );
		}
	}

	public static void saveExpressionButtonControl( Control control,
			Object element, String property ) throws SemanticException
	{
		Expression expression = null;
		if ( control instanceof Text )
		{
			expression = new Expression( ( (Text) control ).getText( ),
					(String) control.getData( ExpressionButtonUtil.EXPR_TYPE ) );
		}
		else if ( control instanceof Combo )
		{
			expression = new Expression( ( (Combo) control ).getText( ),
					(String) control.getData( ExpressionButtonUtil.EXPR_TYPE ) );
		}
		else if ( control instanceof CCombo )
		{
			expression = new Expression( ( (CCombo) control ).getText( ),
					(String) control.getData( ExpressionButtonUtil.EXPR_TYPE ) );
		}

		if ( expression == null )
			return;

		if ( element instanceof DesignElementHandle )
		{
			( (DesignElementHandle) element ).setExpressionProperty( property,
					expression );
		}
		else if ( element instanceof StructureHandle )
		{
			( (StructureHandle) element ).setExpressionProperty( property,
					expression );
		}
		else if ( element instanceof Structure )
		{
			( (Structure) element ).setExpressionProperty( property, expression );
		}
	}

	public static void initExpressionButtonControl( Control control,
			ExpressionHandle value )
	{

		String stringValue = value == null || value.getExpression( ) == null ? "" : (String) value.getExpression( ); //$NON-NLS-1$

		if ( control instanceof Text )
		{
			( (Text) control ).setText( stringValue );
		}
		else if ( control instanceof Combo )
		{
			( (Combo) control ).setText( stringValue );
		}
		else if ( control instanceof CCombo )
		{
			( (CCombo) control ).setText( stringValue );
		}

		control.setData( ExpressionButtonUtil.EXPR_TYPE, value == null
				|| value.getType( ) == null ? UIUtil.getDefaultScriptType( )
				: (String) value.getType( ) );

		Object button = control.getData( ExpressionButtonUtil.EXPR_BUTTON );
		if ( button instanceof ExpressionButton )
		{
			( (ExpressionButton) button ).refresh( );
		}
	}

	public static void initExpressionButtonControl( Control control,
			Expression value )
	{
		String stringValue = value == null || value.getExpression( ) == null ? "" : (String) value.getExpression( ); //$NON-NLS-1$

		if ( control instanceof Text )
		{
			( (Text) control ).setText( stringValue );
		}
		else if ( control instanceof Combo )
		{
			( (Combo) control ).setText( stringValue );
		}
		else if ( control instanceof CCombo )
		{
			( (CCombo) control ).setText( stringValue );
		}

		control.setData( ExpressionButtonUtil.EXPR_TYPE, value == null
				|| value.getType( ) == null ? UIUtil.getDefaultScriptType( )
				: (String) value.getType( ) );

		Object button = control.getData( ExpressionButtonUtil.EXPR_BUTTON );
		if ( button instanceof ExpressionButton )
		{
			( (ExpressionButton) button ).refresh( );
		}
	}

	public static Expression getExpression( Control control )
	{
		String text = null;
		if ( control instanceof Text )
		{
			text = ( (Text) control ).getText( );
		}
		else if ( control instanceof Combo )
		{
			text = ( (Combo) control ).getText( );
		}
		else if ( control instanceof CCombo )
		{
			text = ( (CCombo) control ).getText( );
		}

		return new Expression( text.trim( ).length( ) == 0 ? null : text.trim( ),
				(String) control.getData( EXPR_TYPE ) );
	}
}
