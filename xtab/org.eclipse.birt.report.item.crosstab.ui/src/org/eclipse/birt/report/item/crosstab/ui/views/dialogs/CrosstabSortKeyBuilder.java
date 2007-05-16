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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.SortkeyBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class CrosstabSortKeyBuilder extends SortkeyBuilder
{

	protected SortElementHandle input;
	protected List groupLevelList;
	protected List groupLevelNameList;

	protected Combo textKey;
	protected Combo comboGroupLevel;

	protected LevelViewHandle levelViewHandle;

	public void setHandle( DesignElementHandle handle )
	{
		this.handle = handle;
	}

	public void setInput( SortElementHandle input, LevelViewHandle levelViewHandle )
	{
		this.input = input;
		this.levelViewHandle = levelViewHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog()
	 */
	protected boolean initDialog( )
	{
		if ( input == null )
		{
			textKey.setText( "" );
			comboGroupLevel.select( 0 );
			comboDirection.select( 0 );
			textKey.setFocus( );
			return true;
		}

		getLevels( );

		int levelIndex = groupLevelList.indexOf( levelViewHandle );
		if ( levelIndex >= 0 )
		{
			comboGroupLevel.select( levelIndex );
		}

		if ( input.getKey( ) != null && input.getKey( ).trim( ).length( ) != 0 )
		{
			textKey.setText( input.getKey( ).trim( ) );
		}

		if ( input.getDirection( ) != null
				&& input.getDirection( ).trim( ).length( ) != 0 )
		{
			String value = input.getDirection( ).trim( );
			IChoice choice = choiceSet.findChoice( value );
			if ( choice != null )
				value = choice.getDisplayName( );
			int index;
			index = comboDirection.indexOf( value );
			index = index < 0 ? 0 : index;
			comboDirection.select( index );
		}
		textKey.setFocus( );
		updateButtons( );
		return true;
	}

	/**
	 * Notifies that the ok button of this dialog has been pressed.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method sets
	 * this dialog's return code to <code>Window.OK</code> and closes the
	 * dialog. Subclasses may override.
	 * </p>
	 */
	protected void okPressed( )
	{
		LevelViewHandle level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
		String direction = comboDirection.getText( );
		IChoice choice = choiceSet.findChoiceByDisplayName( direction );
		if ( choice != null )
			direction = choice.getDisplayName( );
		int index;
		index = comboDirection.indexOf( direction );
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		stack.startTrans( getTitle( ) ); //$NON-NLS-1$ 
		try
		{
			if ( input == null )
			{
				
				SortElementHandle sortElement =DesignElementFactory.getInstance( ).newSortElement();
				sortElement.setKey( textKey.getText( ).trim( ) );
				if ( index >= 0 )
				{
					sortElement.setDirection( choice.getName( ) );
				}


				DesignElementHandle designElement = level.getModelHandle( );
				designElement.add( ILevelViewConstants.SORT_PROP, sortElement );

			}
			else
			// edit
			{
				if ( level == levelViewHandle )
				{
					input.setKey( textKey.getText( ).trim( ) );
					if ( index >= 0 )
					{
						input.setDirection( choice.getName( ) );
					}
				}
				else
				// The level is changed
				{
					SortElementHandle sortElement =DesignElementFactory.getInstance( ).newSortElement();
					sortElement.setKey( textKey.getText( ).trim( ) );
					if ( index >= 0 )
					{
						sortElement.setDirection( choice.getName( ) );
					}

//					PropertyHandle propertyHandle = levelViewHandle.getModelHandle( )
//							.getPropertyHandle( ILevelViewConstants.SORT_PROP );
//					propertyHandle.removeItem( input );
//
//					propertyHandle = level.getModelHandle( )
//							.getPropertyHandle( ILevelViewConstants.SORT_PROP );
					levelViewHandle.getModelHandle( ).drop( ILevelViewConstants.SORT_PROP, input );
					level.getModelHandle( ).add( ILevelViewConstants.SORT_PROP, sortElement );
				}

			}
			stack.commit( );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e,
					Messages.getString( "SortkeyBuilder.DialogTitle.Error.SetSortKey.Title" ),
					e.getLocalizedMessage( ) );
			stack.rollback( );
		}

		setReturnCode( OK );
		close( );
	}

	/**
	 * @param title
	 */
	public CrosstabSortKeyBuilder( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public CrosstabSortKeyBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	protected Composite createInputContents( Composite parent )
	{
		// Label lb = new Label( parent, SWT.NONE );
		// lb.setText( Messages.getString(
		// "SortkeyBuilder.DialogTitle.Label.Prompt" ) );

		Composite content = new Composite( parent, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 3, false );
		content.setLayout( glayout );
		Label groupLevel = new Label( content, SWT.NONE );
		groupLevel.setText( Messages.getString( "CrosstabSortkeyBuilder.DialogTitle.Label.GroupLevel" ) );
		comboGroupLevel = new Combo( content, SWT.READ_ONLY | SWT.BORDER );
		GridData gdata = new GridData( );
		gdata.widthHint = 200;
		comboGroupLevel.setLayoutData( gdata );

		getLevels( );
		String groupLeveNames[] = (String[]) groupLevelNameList.toArray( new String[groupLevelNameList.size( )] );
		comboGroupLevel.setItems( groupLeveNames );

		new Label( content, SWT.NONE );

		Label labelKey = new Label( content, SWT.NONE );
		labelKey.setText( Messages.getString( "SortkeyBuilder.DialogTitle.Label.Key" ) );
		textKey = new Combo( content, SWT.BORDER );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		textKey.setLayoutData( gdata );
		textKey.addListener( SWT.Selection, ComboKeyModify );
		textKey.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );
		if ( textKey.getItemCount( ) == 0 )
		{
			textKey.add( DEUtil.resolveNull( null ) );
		}
		
		Button btnExpression = new Button( content, SWT.NONE );
		btnExpression.setToolTipText( Messages.getString( "SortkeyBuilder.DialogTitle.Button.ExpressionBuilder" ) ); //$NON-NLS-1$
		setExpressionButtonImage( btnExpression );

		btnExpression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String oldValue = textKey.getText( );
				ExpressionBuilder dialog = new ExpressionBuilder( UIUtil.getDefaultShell( ),
						oldValue );
				dialog.setExpressionProvier( new ExpressionProvider( handle ) );
				if ( dialog.open( ) == Dialog.OK )
				{
					String newValue = dialog.getResult( );
					if ( !newValue.equals( oldValue ) )
					{
						textKey.setText( newValue );
					}
				}
				updateButtons( );
			}
		} );

		Label labelDirection = new Label( content, SWT.NONE );
		labelDirection.setText( Messages.getString( "SortkeyBuilder.DialogTitle.Label.Direction" ) );

		comboDirection = new Combo( content, SWT.READ_ONLY | SWT.BORDER );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		comboDirection.setLayoutData( gdata );
		String[] displayNames = ChoiceSetFactory.getDisplayNamefromChoiceSet( choiceSet );
		comboDirection.setItems( displayNames );

		return content;
	}

	protected boolean isConditionOK( )
	{
		if ( textKey.getText( ).trim( ).length( ) == 0
				|| comboGroupLevel.getText( ).trim( ).length( ) == 0 )
		{
			return false;
		}
		return true;
	}

	private List getGroupLevelNameList( )
	{
		if ( groupLevelNameList != null || groupLevelNameList.size( ) == 0)
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
		ExtendedItemHandle element = (ExtendedItemHandle) handle;
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
		if(crossTab == null)
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
		if(crossTabViewHandle == null)
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
}
