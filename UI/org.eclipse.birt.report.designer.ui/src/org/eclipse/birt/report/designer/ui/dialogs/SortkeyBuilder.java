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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

public class SortkeyBuilder extends BaseDialog
{

	public static final String DLG_TITLE_NEW = Messages.getString( "SortkeyBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "SortkeyBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	protected SortKeyHandle input;

	protected DesignElementHandle handle;

	protected IChoiceSet choiceSet;

	protected Combo comboDirection;
	private Combo comboKey;

	private List columnList;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

	/**
	 * @param title
	 */
	public SortkeyBuilder( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public SortkeyBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );
		choiceSet = ChoiceSetFactory.getStructChoiceSet( SortKey.SORT_STRUCT,
				SortKey.DIRECTION_MEMBER );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.INSERT_EDIT_SORTKEY_DIALOG_ID );

		GridLayout glayout;
		Composite contents = new Composite( parent, SWT.NONE );
		contents.setLayout( new GridLayout( ) );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		createTitleArea( contents );

		Composite composite = new Composite( contents, SWT.NONE );
		glayout = new GridLayout( );
		glayout.marginHeight = 0;
		glayout.marginWidth = 0;
		glayout.verticalSpacing = 0;
		composite.setLayout( glayout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		applyDialogFont( composite );
		initializeDialogUnits( composite );

		Composite innerParent = (Composite) createDialogArea( composite );
		createButtonBar( composite );

		createInputContents( innerParent );

		Composite space = new Composite( innerParent, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 10;
		space.setLayoutData( gdata );

		Label lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		updateButtons( );

		return composite;
	}

	protected Composite createInputContents( Composite parent )
	{
		Label lb = new Label( parent, SWT.NONE );
		lb.setText( Messages.getString( "SortkeyBuilder.DialogTitle.Label.Prompt" ) );

		Composite content = new Composite( parent, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 3, false );
		content.setLayout( glayout );

		Label labelKey = new Label( content, SWT.NONE );
		labelKey.setText( Messages.getString( "SortkeyBuilder.DialogTitle.Label.Key" ) );
		comboKey = new Combo( content, SWT.BORDER );
		GridData gdata = new GridData( );
		gdata.widthHint = 200;
		comboKey.setLayoutData( gdata );
		comboKey.setItems( getDataSetColumns( ) );
		if(comboKey.getItemCount( ) == 0)
		{
			comboKey.add( DEUtil.resolveNull( null ) );
		}
		comboKey.addListener( SWT.Selection, ComboKeyModify );
		comboKey.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Button btnExpression = new Button( content, SWT.NONE );
//		btnExpression.setText( "..." ); //$NON-NLS-1$
//		gdata = new GridData( );
//		gdata.heightHint = 20;
//		gdata.widthHint = 20;		
//		btnExpression.setLayoutData( gdata );
		btnExpression.setToolTipText( Messages.getString( "SortkeyBuilder.DialogTitle.Button.ExpressionBuilder" ) ); //$NON-NLS-1$
		setExpressionButtonImage(btnExpression);
		
		btnExpression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String oldValue = comboKey.getText( );
				ExpressionBuilder dialog = new ExpressionBuilder( UIUtil.getDefaultShell( ),
						oldValue );
				dialog.setExpressionProvier( new ExpressionProvider( handle ) );
				if ( dialog.open( ) == Dialog.OK )
				{
					String newValue = dialog.getResult( );
					if ( !newValue.equals( oldValue ) )
					{
						comboKey.setText( newValue );
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

	protected Listener ComboKeyModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			Assert.isLegal( e.widget instanceof Combo );
			Combo combo = (Combo) e.widget;
			String newValue = combo.getText( );
			String value = DEUtil.getExpression( getResultSetColumn( newValue ) );
			if ( value != null )
				newValue = value;
			combo.setText( newValue );
			updateButtons( );
		}
	};

	private Object getResultSetColumn( String name )
	{
		if ( columnList.isEmpty( ) )
		{
			return null;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			ComputedColumnHandle column = (ComputedColumnHandle) columnList.get( i );
			if ( column.getName( ).equals( name ) )
			{
				return column;
			}
		}
		return null;
	}

	protected Composite createTitleArea( Composite parent )
	{
		int heightMargins = 3;
		int widthMargins = 8;
		final Composite titleArea = new Composite( parent, SWT.NONE );
		FormLayout layout = new FormLayout( );
		layout.marginHeight = heightMargins;
		layout.marginWidth = widthMargins;
		titleArea.setLayout( layout );

		Display display = parent.getDisplay( );
		Color background = JFaceColors.getBannerBackground( display );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20 + ( heightMargins * 2 );
		titleArea.setLayoutData( layoutData );
		titleArea.setBackground( background );

		titleArea.addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				e.gc.setForeground( titleArea.getDisplay( )
						.getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
				Rectangle bounds = titleArea.getClientArea( );
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle( bounds );
			}
		} );

		Label label = new Label( titleArea, SWT.NONE );
		label.setBackground( background );
		label.setFont( FontManager.getFont( label.getFont( ).toString( ),
				10,
				SWT.BOLD ) );
		label.setText( getTitle( ) ); //$NON-NLS-1$

		return titleArea;
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
			comboKey.setText( "" );
			comboDirection.select( 0 );
			return true;
		}

		if ( input.getKey( ) != null && input.getKey( ).trim( ).length( ) != 0 )
		{
			comboKey.setText( input.getKey( ).trim( ) );
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
		updateButtons( );
		return true;
	}

	protected String[] getDataSetColumns( )
	{
		if ( columnList.isEmpty( ) )
		{
			return EMPTY;
		}
		String[] values = new String[columnList.size( )];
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			values[i] = ( (ComputedColumnHandle) columnList.get( i ) ).getName( );
		}
		return values;
	}

	public void setHandle( DesignElementHandle handle )
	{
		this.handle = handle;
		inilializeColumnList( handle );
	}

	private void inilializeColumnList( DesignElementHandle handle )
	{
		columnList = DEUtil.getVisiableColumnBindingsList( handle );
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
				SortKey sortKey = StructureFactory.createSortKey( );
				sortKey.setKey( comboKey.getText( ).trim( ) );
				if ( index >= 0 )
				{
					sortKey.setDirection( choice.getName( ) );
				}

				PropertyHandle propertyHandle = handle.getPropertyHandle( ListingHandle.SORT_PROP );
				propertyHandle.addItem( sortKey );

			}
			else
			// edit
			{

				input.setKey( comboKey.getText( ).trim( ) );
				if ( index >= 0 )
				{
					input.setDirection( choice.getName( ) );
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
		super.okPressed( );
	}

	/**
	 * Sets the model input.
	 * 
	 * @param input
	 */
	public void setInput( Object input )
	{
		if ( input instanceof SortKeyHandle )
		{
			this.input = (SortKeyHandle) input;
		}
		else
		{
			this.input = null;
		}

	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons( )
	{
		getOkButton( ).setEnabled( isConditionOK( ) );
	}

	protected boolean isConditionOK( )
	{
		if ( comboKey.getText( ).trim( ).length( ) == 0 )
		{
			return false;
		}
		return true;
	}

	protected void setExpressionButtonImage(Button button)
	{
		String imageName;
		if(button.isEnabled())
		{
			imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS;
		}else
		{
			imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS;
		}
		Image image = ReportPlatformUIImages.getImage(imageName );
		
		GridData gd = new GridData();
		gd.widthHint = 20;
		gd.heightHint = 20;
		button.setLayoutData(gd);
		
		button.setImage(image);
		if(button.getImage() != null)
		{
			button.getImage().setBackground(button.getBackground());
		}
		
	}
}
