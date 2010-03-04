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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.ComboProxy;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.EAttributeAccessor;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * ChartExpressionButtonUtil
 */

public class ChartExpressionButtonUtil
{

	static interface IExpressionDescriptor
	{
		void setExpressionType( String type );

		String getExpressionType( );

		String getDisplayText( );

		String getExpression( );

		String getTooltip( );
	}

	static class ExpressionDescriptor implements IExpressionDescriptor
	{

		protected final ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );

		protected ExpressionDescriptor( )
		{

		}

		protected ExpressionDescriptor( String encodedExpr )
		{
			exprCodec.decode( encodedExpr );
		}

		protected ExpressionDescriptor( String sExprText, String type )
		{
			exprCodec.setExpression( sExprText );
			exprCodec.setType( type );
		}

		public static IExpressionDescriptor getInstance( Object expr,
				boolean isCube )
		{
			if ( expr instanceof String[] )
			{
				return new ExpressionDescriptor( ( (String[]) expr )[0] );
			}
			else if ( isCube )
			{
				if ( expr instanceof String )
				{
					return new BindingExpressionDescriptor( (String) expr,
							(String) expr,
							isCube );
				}
			}
			else if ( expr instanceof ColumnBindingInfo )
			{
				return new BindingExpressionDescriptor( ( (ColumnBindingInfo) expr ).getName( ),
						( (ColumnBindingInfo) expr ).getTooltip( ),
						isCube );
			}
			else if ( expr instanceof String )
			{
				return new ExpressionDescriptor( (String) expr );
			}
			return null;
		}

		public static IExpressionDescriptor getInstance( String exprText,
				String exprType )
		{
			return new ExpressionDescriptor( exprText, exprType );
		}

		public String getExpression( )
		{
			return exprCodec.encode( );
		}

		public void setExpressionType( String type )
		{
			// not implemented
		}

		public String getDisplayText( )
		{
			return exprCodec.getExpression( );
		}

		public String getExpressionType( )
		{
			return exprCodec.getType( );
		}

		public String getTooltip( )
		{
			return exprCodec.getExpression( );
		}
	}

	private static class BindingExpressionDescriptor extends
			ExpressionDescriptor
	{

		private final String bindingName;
		private final String tooltip;
		private final boolean isCube;

		public BindingExpressionDescriptor( String bindingName, String tooltip,
				boolean isCube )
		{
			this.bindingName = bindingName;
			this.tooltip = tooltip;
			this.isCube = isCube;
			exprCodec.setBindingName( bindingName,
					isCube,
					UIUtil.getDefaultScriptType( ) );
		}

		public void setExpressionType( String type )
		{
			if ( !exprCodec.getType( ).equals( type ) )
			{
				exprCodec.setBindingName( bindingName, isCube, type );
			}
		}

		@Override
		public String getTooltip( )
		{
			return tooltip;
		}

	}

	public static IExpressionButton createExpressionButton( Composite parent,
			Control control, ExtendedItemHandle eih, IExpressionProvider ep )
	{
		boolean isCube = ChartCubeUtil.getBindingCube( eih ) != null;

		boolean isCombo = control instanceof Combo || control instanceof CCombo;

		ChartExpressionHelper eHelper = isCombo ? new ChartExpressionComboHelper( isCube )
				: new ChartExpressionHelper( isCube );

		return new ChartExpressionButton( parent,
				control,
				eih,
				ep,
				eHelper );
	}

	static class ChartExpressionButton implements IExpressionButton
	{

		// lastExpr is used to cache the expression being set by the last
		// invoking of setExpression ore setBindingName.
		private final ExpressionCodec lastExpr = ChartModelHelper.instance( )
				.createExpressionCodec( );

		protected final ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );

		protected final ExpressionButton eb;
		protected final ChartExpressionHelper eHelper;
		protected final Vector<Listener> listeners = new Vector<Listener>( );
		protected EAttributeAccessor<String> accessor;

		public ChartExpressionButton( Composite parent, Control control,
				ExtendedItemHandle eih, IExpressionProvider ep,
				ChartExpressionHelper eHelper )
		{
			this.eHelper = eHelper;
			eb = ExpressionButtonUtil.createExpressionButton( parent,
					control,
					ep,
					eih,
					new Listener( ) {

						public void handleEvent( Event event )
						{
							onChange( );
						}
					},
					false,
					SWT.PUSH,
					eHelper );
			ExpressionButtonUtil.initExpressionButtonControl( control,
					(Expression) null );
			eHelper.initialize( );

			ControlListener controlListener = new ControlListener( );
			control.addListener( SWT.FocusOut, controlListener );
			control.addListener( SWT.Selection, controlListener );
			control.addListener( SWT.KeyDown, controlListener );
		}

		private class ControlListener implements Listener
		{

			public void handleEvent( Event event )
			{
				switch ( event.type )
				{
					case SWT.KeyDown :
						if ( event.keyCode == SWT.CR
								&& event.keyCode == SWT.KEYPAD_CR )
						{
							onChange( );
						}
						break;
					case SWT.FocusOut :
					case SWT.Selection :
						onChange( );
						break;
				}
			}
		}

		private void save( )
		{
			if ( accessor != null )
			{
				String expr = eHelper.getExpression( ).length( ) == 0 ? null
						: getExpression( );
				accessor.save( expr );
			}
		}

		private void load( )
		{
			if ( accessor != null )
			{
				setExpression( accessor.load( ) );
			}
		}

		protected boolean hasChanged( )
		{
			String oldExpr = lastExpr.getExpression( );
			String newExpr = eHelper.getExpression( );
			String oldType = lastExpr.getType( );
			String newType = eHelper.getExpressionType( );

			if ( oldExpr == null )
			{
				return newType != null || !oldType.equals( newType );
			}

			return !oldExpr.equals( newExpr ) || !oldType.equals( newType );
		}

		private void onChange( )
		{
			if ( hasChanged( ) )
			{
				notifyChangeEvent( );
			}
		}

		protected void notifyChangeEvent( )
		{
			String newExpr = eHelper.getExpression( );
			String newType = eHelper.getExpressionType( );
			Event event = new Event( );
			event.widget = eb.getControl( );
			event.detail = SWT.Modify;
			String[] data = new String[4];
			data[0] = lastExpr.getExpression( );
			data[1] = newExpr;
			data[2] = lastExpr.getType( );
			data[3] = newType;
			event.data = data;

			for ( Listener listener : listeners )
			{
				listener.handleEvent( event );
			}

			save( );
		}

		public void addListener( Listener listener )
		{
			if ( listener != null )
			{
				listeners.add( listener );
			}
		}

		public String getExpression( )
		{
			exprCodec.setExpression( eHelper.getExpression( ) );
			exprCodec.setType( eHelper.getExpressionType( ) );
			return exprCodec.encode( );
		}

		public void setExpression( String expr )
		{
			setExpression( expr, false );
		}

		public String getDisplayExpression( )
		{
			return eHelper.getExpression( );
		}

		public boolean isEnabled( )
		{
			return eb.isEnabled( );
		}

		public void setEnabled( boolean bEnabled )
		{
			eb.setEnabled( bEnabled );
		}

		public void setAccessor( EAttributeAccessor<String> accessor )
		{
			this.accessor = accessor;
			load( );
		}

		public String getExpressionType( )
		{
			return eHelper.getExpressionType( );
		}

		public boolean isCube( )
		{
			return eHelper.isCube( );
		}

		public void setBindingName( String bindingName, boolean bNotifyEvents )
		{
			if ( bindingName != null && bindingName.length( ) > 0 )
			{
				exprCodec.setBindingName( bindingName,
						isCube( ),
						eHelper.getExpressionType( ) );
				eHelper.setExpression( exprCodec.getExpression( ) );
			}
			else
			{
				eHelper.setExpression( bindingName );
			}

			eb.refresh( );

			if ( bNotifyEvents )
			{
				notifyChangeEvent( );
			}

			lastExpr.setExpression( eHelper.getExpression( ) );
			lastExpr.setType( eHelper.getExpressionType( ) );
		}

		public void setExpression( String expr, boolean bNotifyEvents )
		{
			if ( expr != null && expr.length( ) > 0 )
			{
				exprCodec.decode( expr );
				eHelper.setExpressionType( exprCodec.getType( ) );
				eHelper.setExpression( exprCodec.getExpression( ) );
			}
			else
			{
				eHelper.setExpression( expr );
			}
			
			eb.refresh( );

			if (bNotifyEvents)
			{
				notifyChangeEvent( );
			}
			
			lastExpr.setExpression( eHelper.getExpression( ) );
			lastExpr.setType( eHelper.getExpressionType( ) );
		}

		public void setAssitField( IAssistField assistField )
		{
			eHelper.setAssitField( assistField );
		}

		public void setPredefinedQuery( Object[] predefinedQuery )
		{
			if ( predefinedQuery == null )
			{
				return;
			}
			boolean isCube = isCube( );

			Set<IExpressionDescriptor> set = new HashSet<IExpressionDescriptor>( );
			for ( Object obj : predefinedQuery )
			{
				set.add( ExpressionDescriptor.getInstance( obj, isCube ) );
			}

			eHelper.setPredefinedQuerys( set );
		}

	}

	/**
	 * ChartExpressionHelper
	 */
	static class ChartExpressionHelper extends ExpressionHelper
	{

		protected final boolean isCube;
		protected IAssistField assistField;
		protected Set<IExpressionDescriptor> predefinedQuerys = new HashSet<IExpressionDescriptor>( );

		public ChartExpressionHelper( boolean isCube )
		{
			this.isCube = isCube;
		}

		public boolean isCube( )
		{
			return isCube;
		}

		public void setAssitField( IAssistField assistField )
		{
			this.assistField = assistField;
			updateAssistFieldContents( );
		}

		public void setPredefinedQuerys( Collection<IExpressionDescriptor> exprs )
		{
			predefinedQuerys.clear( );
			predefinedQuerys.addAll( exprs );
			setPredefinedQueryType( getExpressionType( ) );
			updateAssistFieldContents( );
		}

		private void updateAssistFieldContents( )
		{
			if ( assistField != null )
			{
				List<String> list = new ArrayList<String>( );
				for ( IExpressionDescriptor desc : predefinedQuerys )
				{
					list.add( desc.getDisplayText( ) );
				}
				assistField.setContent( list.toArray( new String[list.size( )] ) );
			}
		}

		private void setPredefinedQueryType( String type )
		{
			for ( IExpressionDescriptor desc : predefinedQuerys )
			{
				desc.setExpressionType( type );
			}
		}

		@Override
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
		}

		@Override
		public String getExpression( )
		{
			if ( control.isDisposed( ) )
			{
				return ""; //$NON-NLS-1$
			}
			return ChartUIUtil.getText( control ).trim( );
		}

		@Override
		public void setExpression( String expression )
		{
			if ( control.isDisposed( ) )
			{
				return;
			}
			ChartUIUtil.setText( control, DEUtil.resolveNull( expression ) );
		}

		public void initialize( )
		{
			// do nothing
		}

		@Override
		public String getExpressionType( )
		{
			String type = super.getExpressionType( );
			return type != null ? type : UIUtil.getDefaultScriptType( );
		}

		@Override
		public void setExpressionType( String exprType )
		{
			super.setExpressionType( exprType );
			setPredefinedQueryType( getExpressionType( ) );
			updateAssistFieldContents( );
		}

	}

	static class ChartExpressionComboHelper extends ChartExpressionHelper
	{
		protected final boolean bCacheUserInput = true;

		ChartExpressionComboHelper( boolean isCube )
		{
			super( isCube );
		}

		@Override
		public void setExpressionType( String exprType )
		{
			if ( getExpressionType( ).equals( exprType ) )
			{
				return;
			}
			super.setExpressionType( exprType );

			ComboProxy cp = ComboProxy.getInstance( control );
			if ( cp != null )
			{
				String[] itemsOld = cp.getItems( );
				String userExpr = cp.getText( );
				cp.removeAll( );

				for ( String oldItem : itemsOld )
				{
					IExpressionDescriptor desc = (IExpressionDescriptor) cp.getData( oldItem );
					addComboItem( cp, desc );
				}

				cp.setText( userExpr );
			}

		}

		private void addComboItem( ComboProxy cp, IExpressionDescriptor desc )
		{
			String key = desc.getDisplayText( );
			cp.add( key );
			cp.setData( key, desc );
		}

		@Override
		public void setExpression( String expression )
		{
			if ( bCacheUserInput
					&& expression != null
					&& expression.length( ) > 0 )
			{
				ComboProxy cp = ComboProxy.getInstance( control );
				
				// if ( cp != null && !cp.contains( expression ) )
				// {
				// IExpressionDescriptor desc =
				// ExpressionDescriptor.getInstance( expression,
				// getExpressionType( ) );
				// addComboItem( cp, desc );
				// }

				if ( cp.getData( expression ) instanceof IExpressionDescriptor )
				{
					IExpressionDescriptor desc = (IExpressionDescriptor) cp.getData( expression );
					control.setToolTipText( desc.getTooltip( ) );
				}
			}
			super.setExpression( expression );
		}

		@Override
		public void initialize( )
		{
			control.addListener( SWT.Selection, new Listener( ) {

				public void handleEvent( Event event )
				{
					ComboProxy cp = ComboProxy.getInstance( control );
					if ( cp != null )
					{
						if ( cp.getSelectionIndex( ) >= 0 )
						{
							IExpressionDescriptor desc = (IExpressionDescriptor) cp.getData( cp.getText( ) );
							if ( desc != null )
							{
								setExpressionType( desc.getExpressionType( ) );
							}
							button.refresh( );
						}
					}
				}
			} );

		}

		@Override
		public void setPredefinedQuerys( Collection<IExpressionDescriptor> exprs )
		{
			super.setPredefinedQuerys( exprs );

			ComboProxy cp = ComboProxy.getInstance( control );
			if ( cp != null )
			{
				cp.removeAll( );
				for ( IExpressionDescriptor desc : predefinedQuerys )
				{
					addComboItem( cp, desc );
				}
			}
		}

	}
}
