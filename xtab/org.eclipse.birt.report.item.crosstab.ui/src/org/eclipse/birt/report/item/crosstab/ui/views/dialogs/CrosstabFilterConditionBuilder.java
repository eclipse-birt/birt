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

package org.eclipse.birt.report.item.crosstab.ui.views.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class CrosstabFilterConditionBuilder extends FilterConditionBuilder
{

	protected Combo comboGroupLevel;
	protected List groupLevelList;
	protected List groupLevelNameList;
	protected FilterConditionElementHandle inputHandle;
	protected LevelViewHandle levelViewHandle;

	public void setInput( FilterConditionElementHandle input,
			LevelViewHandle levelViewHandle )
	{
		this.inputHandle = input;
		this.levelViewHandle = levelViewHandle;
	}

	/**
	 * @param title
	 */
	public CrosstabFilterConditionBuilder( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public CrosstabFilterConditionBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	protected void createFilterConditionContent( Composite innerParent )
	{
		
		Composite groupLevelParent = new Composite( innerParent, SWT.NONE );
		groupLevelParent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 2, false );
		groupLevelParent.setLayout( glayout );
		
		Label lbGroupLevel = new Label( groupLevelParent, SWT.NONE );
		lbGroupLevel.setText( Messages.getString( "FilterConditionBuilder.text.GroupLevel" ) ); //$NON-NLS-1$

		comboGroupLevel = new Combo( groupLevelParent, SWT.READ_ONLY | SWT.BORDER );
		GridData gdata = new GridData( );
		gdata.widthHint = 150;
		comboGroupLevel.setLayoutData( gdata );

		getLevels( );
		String groupLeveNames[] = (String[]) groupLevelNameList.toArray( new String[groupLevelNameList.size( )] );
		comboGroupLevel.setItems( groupLeveNames );

		createDummy( innerParent, 1 );

		Composite condition = new Composite( innerParent, SWT.NONE );
		condition.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );

		Label lb = new Label( condition, SWT.NONE );
		lb.setText( Messages.getString( "FilterConditionBuilder.text.Condition" ) ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.horizontalSpan = 4;
		lb.setLayoutData( gdata );
		
		expression = new Combo( condition, SWT.NONE );
		gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		expression.addListener( SWT.Selection, ComboModify );
		// expression.setItems( getDataSetColumns( ) );
		if ( expression.getItemCount( ) == 0 )
		{
			expression.add( DEUtil.resolveNull( null ) );
		}
		expression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expression.getText( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					expression.setText( DEUtil.getColumnExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
				}
				updateButtons( );
			}
		} );
		expression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Button expBuilder = new Button( condition, SWT.PUSH );
		// expBuilder.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		expBuilder.setLayoutData( gdata );
		setExpressionButtonImage( expBuilder );
		expBuilder.setToolTipText( Messages.getString( "FilterConditionBuilder.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		expBuilder.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editValue( expression );
			}
		} );

		operator = new Combo( condition, SWT.READ_ONLY );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( OpoertorSelection );

		ExpressionValue expressionValue1 = new ExpressionValue( condition,
				SWT.NONE,
				null );
		value1 = expressionValue1.getValueText( );
		valBuilder1 = expressionValue1.getPopupButton( );

		createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );

		createDummy( condition, 3 );

		ExpressionValue expressionValue2 = new ExpressionValue( condition,
				SWT.NONE,
				null );
		value2 = expressionValue2.getValueText( );
		valBuilder2 = expressionValue2.getPopupButton( );
		value2.setVisible( false );
		valBuilder2.setVisible( false );

		if ( operator.getItemCount( ) > 0 )
		{
			operator.select( 0 );
		}

		Composite space = new Composite( innerParent, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 15;
		space.setLayoutData( gdata );

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		syncViewProperties( );

	}

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties( )
	{
		if ( inputHandle == null )
		{
			comboGroupLevel.select( 0 );
		}
		else
		{
			getLevels( );

			int levelIndex = groupLevelList.indexOf( levelViewHandle );
			if ( levelIndex >= 0 )
			{
				comboGroupLevel.select( levelIndex );
			}
			
			expression.setText( DEUtil.resolveNull( inputHandle.getExpr( ) ) );
			operator.select( getIndexForOperatorValue( inputHandle.getOperator( ) ) );
			value1.setText( DEUtil.resolveNull( inputHandle.getValue1( ) ) );
			value2.setText( DEUtil.resolveNull( inputHandle.getValue2( ) ) );
			int vv = determineValueVisible( inputHandle.getOperator( ) );

			if ( vv == 0 )
			{
				value1.setVisible( false );
				valBuilder1.setVisible( false );
				value2.setVisible( false );
				valBuilder2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 1 )
			{
				value1.setVisible( true );
				valBuilder1.setVisible( true );
				value2.setVisible( false );
				valBuilder2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 2 )
			{
				value1.setVisible( true );
				valBuilder1.setVisible( true );
				value2.setVisible( true );
				valBuilder2.setVisible( true );
				andLable.setVisible( true );
			}
		}

	}

	private Text createText( Composite parent )
	{
		Text txt = new Text( parent, SWT.BORDER );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.widthHint = 100;
		txt.setLayoutData( gdata );

		return txt;
	}

	private class ExpressionValue
	{

		Text valueText;
		Button btnPopup;

		Text getValueText( )
		{
			return valueText;
		}

		Button getPopupButton( )
		{
			return btnPopup;
		}

		ExpressionValue( Composite parent, int style, final Combo expressionText )
		{
			Composite composite = new Composite( parent, SWT.NONE );
			composite.setLayout( new ExpressionLayout( ) );
			GridData gdata = new GridData( GridData.END );
			gdata.widthHint = 120;
			gdata.heightHint = 20;
			composite.setLayoutData( gdata );
			// GridLayout layout = new GridLayout(2,false);
			// composite.setLayout(layout);
			valueText = createText( composite );
			valueText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					updateButtons( );
				}
			} );

			btnPopup = new Button( composite, SWT.PUSH );
			gdata = new GridData( );
			gdata.heightHint = 20;
			gdata.widthHint = 20;
			btnPopup.setLayoutData( gdata );
			setExpressionButtonImage( btnPopup );
			btnPopup.setToolTipText( Messages.getString( "FilterConditionBuilder.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
			btnPopup.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					editValue( valueText );
				}
			} );
		}

		private class ExpressionLayout extends Layout
		{

			public void layout( Composite editor, boolean force )
			{
				Rectangle bounds = editor.getClientArea( );
				Point size = btnPopup.computeSize( 20, 20, force );
				valueText.setBounds( 0, 0, bounds.width - size.x, bounds.height );
				btnPopup.setBounds( bounds.width - size.x,
						0,
						size.x,
						bounds.height );
			}

			public Point computeSize( Composite editor, int wHint, int hHint,
					boolean force )
			{
				if ( wHint != SWT.DEFAULT && hHint != SWT.DEFAULT )
					return new Point( wHint, hHint );
				Point contentsSize = valueText.computeSize( SWT.DEFAULT,
						SWT.DEFAULT,
						force );
				Point buttonSize = btnPopup.computeSize( 20, 20, force );
				// Just return the button width to ensure the button is not
				// clipped
				// if the label is long.
				// The label will just use whatever extra width there is
				Point result = new Point( buttonSize.x,
						Math.max( contentsSize.y, buttonSize.y ) );
				return result;
			}
		}

	}

	private List getGroupLevelNameList( )
	{
		if ( groupLevelNameList != null || groupLevelNameList.size( ) == 0 )
		{
			return groupLevelNameList;
		}
		getLevels( );
		return groupLevelNameList;
	}

	private List getLevels( )
	{
		if ( groupLevelList != null )
		{
			return groupLevelList;
		}
		groupLevelList = new ArrayList( );
		groupLevelNameList = new ArrayList( );
		ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
		CrosstabReportItemHandle crossTab = null;
		try
		{
			crossTab = (CrosstabReportItemHandle) element.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		if ( crossTab == null )
		{
			return groupLevelList;
		}
		if ( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE )
					.getModelHandle( );
			getLevel( (ExtendedItemHandle) elementHandle );
		}

		if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE )
					.getModelHandle( );
			getLevel( (ExtendedItemHandle) elementHandle );
		}

		return groupLevelList;
	}

	private void getLevel( ExtendedItemHandle handle )
	{
		CrosstabViewHandle crossTabViewHandle = null;
		try
		{
			crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		if ( crossTabViewHandle == null )
		{
			return;
		}
		int dimensionCount = crossTabViewHandle.getDimensionCount( );
		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dimension = crossTabViewHandle.getDimension( i );
			int levelCount = dimension.getLevelCount( );
			for ( int j = 0; j < levelCount; j++ )
			{
				LevelViewHandle levelHandle = dimension.getLevel( j );
				groupLevelList.add( levelHandle );
				groupLevelNameList.add( levelHandle.getCubeLevel( ).getFullName( ) );
			}
		}

	}

	/**
	 * Gets if the condition is available.
	 */
	protected boolean isConditionOK( )
	{
		if ( comboGroupLevel.getText( ) != null
				&& comboGroupLevel.getText( ).length( ) == 0 )
		{
			return false;
		}

		return super.isConditionOK( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		LevelViewHandle level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );

		try
		{
			if ( inputHandle == null )
			{
				FilterConditionElementHandle filter = DesignElementFactory.getInstance().newFilterConditionElement( );
				filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				filter.setProperty( IFilterConditionElementModel.VALUE1_PROP,
						DEUtil.resolveNull( value1.getText( ) ) );
				filter.setProperty( IFilterConditionElementModel.VALUE2_PROP,
						DEUtil.resolveNull( value2.getText( ) ) );

				// set test expression for new map rule
				filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

//				PropertyHandle propertyHandle = level.getModelHandle( )
//						.getPropertyHandle( ILevelViewConstants.FILTER_PROP );
//				propertyHandle.addItem( filter );
				level.getModelHandle( ).add( ILevelViewConstants.FILTER_PROP, filter );
			}
			else
			{
				// will update later;
				if ( level == levelViewHandle ) // unchanged
				{
					inputHandle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
					if ( value1.getVisible( ) )
					{
						inputHandle.setValue1( DEUtil.resolveNull( value1.getText( ) ) );
					}
					else
					{
						inputHandle.setValue1( "" );
					}

					if ( value2.getVisible( ) )
					{
						inputHandle.setValue2( DEUtil.resolveNull( value2.getText( ) ) );
					}
					else
					{
						inputHandle.setValue2( "" );
					}
					inputHandle.setExpr( DEUtil.resolveNull( expression.getText( ) ) );
				}
				else
				{
					FilterConditionElementHandle filter = DesignElementFactory.getInstance().newFilterConditionElement( );
					filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
							DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
					filter.setProperty( IFilterConditionElementModel.VALUE1_PROP,
							DEUtil.resolveNull( value1.getText( ) ) );
					filter.setProperty( IFilterConditionElementModel.VALUE2_PROP,
							DEUtil.resolveNull( value2.getText( ) ) );
					filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

//					PropertyHandle propertyHandle = levelViewHandle.getModelHandle( )
//							.getPropertyHandle( ILevelViewConstants.FILTER_PROP );
//					propertyHandle.removeItem( inputHandle );
//
//					propertyHandle = level.getModelHandle( )
//							.getPropertyHandle( ILevelViewConstants.FILTER_PROP );
//					propertyHandle.addItem( filter );
					
					levelViewHandle.getModelHandle( ).drop( ILevelViewConstants.FILTER_PROP, inputHandle );
					level.getModelHandle( ).add( ILevelViewConstants.FILTER_PROP, filter );

				}

			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		setReturnCode( OK );
		close( );
	}

}
