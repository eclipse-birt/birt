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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.MapHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.PopupSelectionList;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog for adding or editing map rule.
 */

public class MapRuleBuilder extends BaseDialog
{
	protected IExpressionProvider expressionProvider;	
	protected transient String bindingName = null;
	protected ReportElementHandle currentItem = null;
	
	protected String[] popupItems = null;

	protected static String[] EMPTY_ARRAY = new String[]{};
	
	public static final String DLG_TITLE_NEW = Messages.getString( "MapRuleBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "MapRuleBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	/**
	 * Usable operators for building map rule conditions.
	 */
	static final String[][] OPERATOR;

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( MapRule.STRUCTURE_NAME,
				MapRule.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( );
		OPERATOR = new String[chs.length][2];

		for ( int i = 0; i < chs.length; i++ )
		{
			OPERATOR[i][0] = chs[i].getDisplayName( );
			OPERATOR[i][1] = chs[i].getName( );
		}
	}

	/**
	 * Returns the operator value by its display name.
	 * 
	 * @param name
	 */
	public static String getValueForOperator( String name )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][0].equals( name ) )
			{
				return OPERATOR[i][1];
			}
		}

		return null;
	}

	/**
	 * Returns how many value fields this operator needs.
	 * 
	 * @param operatorValue
	 */
	public static int determineValueVisible( String operatorValue )
	{
		if ( DesignChoiceConstants.MAP_OPERATOR_ANY.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_FALSE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_TRUE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NULL.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_NULL.equals( operatorValue ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_LT.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_LE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_EQ.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_GE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_GT.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_LIKE.equals( operatorValue ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_BETWEEN.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN.equals( operatorValue ) )
		{
			return 2;
		}

		return 1;
	}

	/**
	 * Returns the operator display name by its value.
	 * 
	 * @param value
	 */
	public static String getNameForOperator( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return OPERATOR[i][0];
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the index for given operator value in the operator list.
	 * 
	 * @param value
	 */
	static int getIndexForOperatorValue( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return i;
			}
		}

		return 0;
	}

	private MapRuleHandle handle;

	private MapHandleProvider provider;

	private int handleCount;

	protected Combo expression, operator;

	private Text display;
	
	private ExpressionValue  value1, value2;

	private Label andLable;

	private Text resourceKeytext;

	private Button btnBrowse;

	private Button btnReset;

	protected DesignElementHandle designHandle;

	private static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "HighlightRuleBuilderDialog.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

	protected static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	private ParamBindingHandle[] bindingParams = null;

	/**
	 * Default constructor.
	 * 
	 * @param parentShell
	 *            Parent Shell
	 * @param title
	 *            Window Title
	 */
	public MapRuleBuilder( Shell parentShell, String title,
			MapHandleProvider provider )
	{
		super( parentShell, title );

		this.provider = provider;
	}

	protected List columnList;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

	private String[] getDataSetColumns( )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.INSERT_EDIT_MAP_RULE_DIALOG_ID );
		refreshList( );
		
		GridData gdata;
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

		Label lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "MapRuleBuilderDialog.text.Condition" ) ); //$NON-NLS-1$

		Composite condition = new Composite( innerParent, SWT.NONE );
		condition.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );

		expression = new Combo( condition, SWT.NONE );
		gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		expression.setItems( getDataSetColumns( ) );
		fillExpression( expression );
		expression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expression.getText( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					expression.setText( DEUtil.getColumnExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
				}
				else
				{
					String newValue = expression.getText( );
					String value = DEUtil.getExpression( getResultSetColumn( newValue ) );
					if ( value != null )
						newValue = value;
					expression.setText( newValue );
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
		UIUtil.setExpressionButtonImage( expBuilder );
		// gdata = new GridData( );
		// gdata.heightHint = 20;
		// gdata.widthHint = 20;
		// expBuilder.setLayoutData( gdata );
		expBuilder.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
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
		operator.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String value = getValueForOperator( operator.getText( ) );

				int vv = determineValueVisible( value );

				if ( vv == 0 )
				{
					value1.setVisible( false );
					value2.setVisible( false );
					andLable.setVisible( false );
				}
				else if ( vv == 1 )
				{
					value1.setVisible( true );
					value2.setVisible( false );
					andLable.setVisible( false );
				}
				else if ( vv == 2 )
				{
					value1.setVisible( true );
					value2.setVisible( true );
					andLable.setVisible( true );
				}
				updateButtons( );
			}
		} );

		GridData gd = new GridData( GridData.END);
		gd.widthHint = 130;
		gd.heightHint = 20;
		
		value1 = new ExpressionValue( condition,gd, expression );
		value1.addTextControlListener( SWT.Modify, textModifyListener );
		value1.addButtonControlListener( SWT.Selection, popBtnSelectionListener1 );

		createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "HighlightRuleBuilderDialog.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );

		createDummy( condition, 3 );

		value2 = new ExpressionValue( condition,gd, expression );
		value2.addTextControlListener( SWT.Modify, textModifyListener );
		value2.addButtonControlListener( SWT.Selection, popBtnSelectionListener2 );
		value2.setVisible( false );

		if ( operator.getItemCount( ) > 0 )
		{
			operator.select( 0 );
		}

		lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "MapRuleBuilderDialog.text.Display" ) ); //$NON-NLS-1$

		Composite format = new Composite( innerParent, SWT.NONE );
		format.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( );
		format.setLayout( glayout );

		display = new Text( format, SWT.BORDER );
		gdata = new GridData( );
		gdata.widthHint = 300;
		display.setLayoutData( gdata );

		Composite space = new Composite( innerParent, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 20;
		space.setLayoutData( gdata );

		createResourceKeyArea( innerParent );

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		if ( handle != null )
		{
			syncViewProperties( );
		}

		updateButtons( );

		return composite;
	}
	
	
	private Listener textModifyListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			updateButtons( );
		}
	};
	
	private Listener popBtnSelectionListener1 = new Listener( ) {

		public void handleEvent( Event event )
		{
			popBtnSelectionAction(value1);
		}

	};
	
	private Listener popBtnSelectionListener2 = new Listener( ) {

		public void handleEvent( Event event )
		{
			popBtnSelectionAction(value2);
		}

	};

	private void refreshList( )
	{
			ArrayList finalItems = new ArrayList( 10 );
			for ( int n = 0; n < actions.length; n++ )
			{
				finalItems.add( actions[n] );
			}

			popupItems = (String[]) finalItems.toArray( EMPTY_ARRAY );
	}
	
	private List getSelectValueList( ) throws BirtException
	{
		List selectValueList = new ArrayList( );
		ReportItemHandle reportItem = DEUtil.getBindingHolder( currentItem );
		if ( bindingName != null && reportItem != null )
		{

			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					reportItem.getModuleHandle( ) ) );
			selectValueList.addAll( session.getColumnValueSet( reportItem.getDataSet( ),
					reportItem.paramBindingsIterator( ),
					reportItem.columnBindingsIterator( ),
					bindingName ) );
			session.shutdown( );
		}
		else
		{
			ExceptionHandler.openErrorMessageBox( Messages.getString( "SelectValueDialog.errorRetrievinglist" ), Messages.getString( "SelectValueDialog.noExpressionSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return selectValueList;
	}
	
	protected void popBtnSelectionAction(ExpressionValue expressionValue)
	{
		Text valueText =  expressionValue.getTextControl( );
		Rectangle textBounds = valueText.getBounds( );
		Point pt = valueText.toDisplay( textBounds.x, textBounds.y );
		Rectangle rect = new Rectangle( pt.x, pt.y, valueText.getParent( )
				.getBounds( ).width, textBounds.height );

		PopupSelectionList popup = new PopupSelectionList( valueText.getParent( )
				.getShell( ) );
		popup.setItems( popupItems );
		String value = popup.open( rect );
		int selectionIndex = popup.getSelectionIndex( );

		for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
		{
			String columnName = ( (ComputedColumnHandle) ( iter.next( ) ) ).getName( );
			if ( DEUtil.getColumnExpression( columnName )
					.equals( expression.getText( ) ) )
			{
				bindingName = columnName;
				break;
			}
		}

		if ( value != null )
		{
			String newValue = null;
			if ( value.equals( ( actions[0] ) ) )
			{
				if ( bindingName != null )
				{
					try
					{
						List selectValueList = getSelectValueList( );
						SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
								.getDisplay( )
								.getActiveShell( ),
								Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
						dialog.setSelectedValueList( selectValueList );
						if ( bindingParams != null )
						{
							dialog.setBindingParams( bindingParams );
						}
						if ( dialog.open( ) == IDialogConstants.OK_ID )
						{
							newValue = dialog.getSelectedExprValue( );
						}
					}
					catch ( Exception ex )
					{
						MessageDialog.openError( null,
								Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
								Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
										+ "\n" //$NON-NLS-1$
										+ ex.getMessage( ) );
					}
				}
				else if ( designHandle instanceof TabularCubeHandle )
				{
					DataSetHandle dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
					String expressionString = expression.getText( );
					try
					{
						List selectValueList = SelectValueFetcher.getSelectValueList( expressionString,
								dataSet,
								false );
						SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
								.getDisplay( )
								.getActiveShell( ),
								Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
						dialog.setSelectedValueList( selectValueList );
						if ( dialog.open( ) == IDialogConstants.OK_ID )
						{
							newValue = dialog.getSelectedExprValue( );
						}

					}
					catch ( BirtException e1 )
					{
						MessageDialog.openError( null,
								Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
								Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
										+ "\n" //$NON-NLS-1$
										+ e1.getMessage( ) );
					}
				}
				else
				{
					MessageDialog.openInformation( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
				}
			}
			else if ( value.equals( actions[1] ) )
			{
				ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						valueText.getText( ) );

				if ( expressionProvider == null )
					dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
				else
					dialog.setExpressionProvier( expressionProvider );

				if ( dialog.open( ) == IDialogConstants.OK_ID )
				{
					newValue = dialog.getResult( );
				}
			}
			else if ( selectionIndex > 3 )
			{
				newValue = "params[\"" + value + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			if ( newValue != null )
			{
				valueText.setText( newValue );
			}
		}
	}
	
	private Composite createResourceKeyArea( Composite parent )
	{
		Composite resourceKeyArea = new Composite( parent, SWT.NONE );
		resourceKeyArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 4, false );
		resourceKeyArea.setLayout( glayout );

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		resourceKeyArea.setLayoutData( gd );

		Label lb = new Label( resourceKeyArea, SWT.NONE );
		lb.setText( Messages.getString( "MapRuleBuilder.Button.ResourceKey" ) ); //$NON-NLS-1$
		resourceKeytext = new Text( resourceKeyArea, SWT.BORDER | SWT.READ_ONLY );
		resourceKeytext.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		btnBrowse = new Button( resourceKeyArea, SWT.PUSH );
		btnBrowse.setLayoutData( new GridData( ) );
		btnBrowse.setText( "..." ); //$NON-NLS-1$

		btnBrowse.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleBrowserSelectedEvent( );
			}
		} );

		btnReset = new Button( resourceKeyArea, SWT.PUSH );
		btnReset.setLayoutData( new GridData( ) );
		btnReset.setText( Messages.getString( "MapRuleBuilder.Button.Reset" ) ); //$NON-NLS-1$

		btnReset.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleSelectedEvent( null );
			};

		} );
		checkResourceKey( );

		Label noteLabel = new Label( parent, SWT.NONE | SWT.WRAP );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 4;
		gd.widthHint = 350;
		noteLabel.setLayoutData( gd );
		noteLabel.setText( Messages.getString( "I18nPage.text.Note" ) ); //$NON-NLS-1$
		return resourceKeyArea;
	}

	protected void handleBrowserSelectedEvent( )
	{
		ResourceEditDialog dlg = new ResourceEditDialog( btnBrowse.getShell( ),
				Messages.getString( "ResourceKeyDescriptor.title.SelectKey" ) ); //$NON-NLS-1$

		dlg.setResourceURL( provider.getResourceURL( ) );

		if ( dlg.open( ) == Window.OK )
		{
			handleSelectedEvent( (String) dlg.getResult( ) );
		}
	}

	private void handleSelectedEvent( String newValue )
	{
		if ( "".equals( newValue ) )//$NON-NLS-1$
		{
			newValue = null;
		}

		resourceKeytext.setText( DEUtil.resolveNull( newValue ) );

	}

	private Composite createTitleArea( Composite parent )
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
		label.setText( getTitle( ) );

		return titleArea;
	}

	private Composite createDummy( Composite parent, int colSpan )
	{
		Composite dummy = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 22;
		gdata.horizontalSpan = colSpan;
		gdata.heightHint = 10;
		dummy.setLayoutData( gdata );

		return dummy;
	}

	private Text createText( Composite parent )
	{
		Text txt = new Text( parent, SWT.BORDER );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.widthHint = 100;
		txt.setLayoutData( gdata );

		return txt;
	}

	/*
	 * Update handle for the Map Rule builder
	 */
	public void updateHandle( MapRuleHandle handle, int handleCount )
	{
		this.handle = handle;
		this.handleCount = handleCount;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
		initializeProviderType( );
		inilializeColumnList( handle );
		initializeParamterBinding( handle );
	}

	public void setReportElement( ReportElementHandle reportItem )
	{
		currentItem = reportItem;
	}
	
	private void inilializeColumnList( DesignElementHandle handle )
	{
		if(handle instanceof ExtendedItemHandle)
		{
			columnList = new ArrayList();
		}
		else
		{
			columnList = DEUtil.getVisiableColumnBindingsList( handle );
		}
	}

	private void initializeParamterBinding( DesignElementHandle handle )
	{
		if ( handle instanceof ReportItemHandle )
		{
			ReportItemHandle inputHandle = (ReportItemHandle) handle;
			List list = new ArrayList( );
			for ( Iterator iterator = inputHandle.paramBindingsIterator( ); iterator.hasNext( ); )
			{
				ParamBindingHandle paramBindingHandle = (ParamBindingHandle) iterator.next( );
				list.add( paramBindingHandle );
			}
			bindingParams = new ParamBindingHandle[list.size( )];
			list.toArray( bindingParams );
		}
	}

	/*
	 * Return the hanle of Map Rule builder
	 */
	public MapRuleHandle getHandle( )
	{
		return handle;
	}

	private void fillExpression( Combo control )
	{
		String te = "";//$NON-NLS-1$

		if ( handle != null )
		{
			te = handle.getTestExpression( );
		}

		// String te = provider.getTestExpression( );

		if ( ( designHandle instanceof DataItemHandle )
				&& ( ( (DataItemHandle) designHandle ).getResultSetColumn( ) != null ) )
		{
			control.add( VALUE_OF_THIS_DATA_ITEM );
		}

		if ( control.getItemCount( ) == 0 )
		{
			control.add( DEUtil.resolveNull( null ) );
			control.select( control.getItemCount( ) - 1 );
		}
		
	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	private void updateButtons( )
	{
		enableInput( isExpressionOK( ) );

		getOkButton( ).setEnabled( isConditionOK( ) );
	}

	private void enableInput( boolean val )
	{
		operator.setEnabled( val );
		value1.setEnabled( val );
		value2.setEnabled( val );
		display.setEnabled( val );
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	private boolean isExpressionOK( )
	{
		if ( expression == null )
		{
			return false;
		}

		if ( expression.getText( ) == null
				|| expression.getText( ).length( ) == 0 )
		{
			return false;
		}

		return true;
	}

	/**
	 * Gets if the condition is available.
	 */
	private boolean isConditionOK( )
	{
		if ( expression == null )
		{
			return false;
		}

		if ( !isExpressionOK( ) )
		{
			return false;
		}

		return checkValues( );
	}

	/**
	 * Gets if the values of the condition is(are) available.
	 */
	private boolean checkValues( )
	{
		if ( value1.getVisible( ) )
		{
			if ( value1.getText( ) == null || value1.getText( ).length( ) == 0 )
			{
				return false;
			}
		}

		if ( value2.getVisible( ) )
		{
			if ( value2.getText( ) == null || value2.getText( ).length( ) == 0 )
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * SYNC the control value according to the handle.
	 */
	private void syncViewProperties( )
	{
		// expression.setText( DEUtil.resolveNull( provider.getTestExpression( )
		// ) );

		expression.setText( DEUtil.resolveNull( handle.getTestExpression( ) ) );

		operator.select( getIndexForOperatorValue( handle.getOperator( ) ) );

		value1.setText( DEUtil.resolveNull( handle.getValue1( ) ) );

		value2.setText( DEUtil.resolveNull( handle.getValue2( ) ) );

		int vv = determineValueVisible( handle.getOperator( ) );

		if ( vv == 0 )
		{
			value1.setVisible( false );
			value2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( vv == 1 )
		{
			value1.setVisible( true );
			value2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( vv == 2 )
		{
			value1.setVisible( true );
			value2.setVisible( true );
			andLable.setVisible( true );
		}

		display.setText( DEUtil.resolveNull( handle.getDisplay( ) ) );
		resourceKeytext.setText( DEUtil.resolveNull( handle.getDisplayKey( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		try
		{
			// provider.setTestExpression( DEUtil.resolveNull(
			// expression.getText( ) ) );

			if ( handle == null )
			{
				MapRule rule = StructureFactory.createMapRule( );

				rule.setProperty( HighlightRule.OPERATOR_MEMBER,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				if ( value1.isVisible( ) )
				{
					rule.setProperty( HighlightRule.VALUE1_MEMBER,
							DEUtil.resolveNull( value1.getText( ) ) );
				}
				if ( value2.isVisible( ) )
				{
					rule.setProperty( HighlightRule.VALUE2_MEMBER,
							DEUtil.resolveNull( value2.getText( ) ) );
				}

				rule.setProperty( MapRule.DISPLAY_MEMBER,
						DEUtil.resolveNull( display.getText( ) ) );

				// set test expression for new map rule
				rule.setTestExpression( DEUtil.resolveNull( expression.getText( ) ) );

				handle = provider.doAddItem( rule, handleCount );
			}
			else
			{
				handle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				if ( value1.isVisible( ) )
				{
					handle.setValue1( DEUtil.resolveNull( value1.getText( ) ) );
				}
				if ( value2.isVisible( ) )
				{
					handle.setValue2( DEUtil.resolveNull( value2.getText( ) ) );
				}
				handle.setDisplay( DEUtil.resolveNull( display.getText( ) ) );
				handle.setDisplayKey( DEUtil.resolveNull( resourceKeytext.getText( ) ) );
				handle.setTestExpression( DEUtil.resolveNull( expression.getText( ) ) );
			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		super.okPressed( );
	}

	private void editValue( Control control )
	{
		String initValue = null;
		if ( control instanceof Text )
		{
			initValue = ( (Text) control ).getText( );
		}
		else if ( control instanceof Combo )
		{
			initValue = ( (Combo) control ).getText( );
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
				initValue );

		if ( designHandle != null )
		{
			ExpressionProvider expressionProvider = new ExpressionProvider( designHandle );
			expressionProvider.addFilter( new ExpressionFilter( ) {

				public boolean select( Object parentElement, Object element )
				{
					if ( ExpressionFilter.CATEGORY.equals( parentElement )
							&& ExpressionProvider.CURRENT_CUBE.equals( element ) )
					{
						return false;
					}
					return true;
				}

			} );
			expressionBuilder.setExpressionProvier( expressionProvider );
		}

		if ( expressionBuilder.open( ) == OK )
		{
			String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
			if ( control instanceof Text )
			{
				( (Text) control ).setText( result );
			}
			else if ( control instanceof Combo )
			{
				( (Combo) control ).setText( result );
			}
		}
		updateButtons( );
	}

	private void checkResourceKey( )
	{
		checkResourceKey( null );
	}

	private void checkResourceKey( MapRuleHandle handle )
	{
		resourceKeytext.setEnabled( true );
		btnBrowse.setEnabled( true );
		btnReset.setEnabled( true );

		String baseName = provider.getBaseName( );
		if ( baseName == null )
		{
			btnBrowse.setEnabled( false );
		}
		else
		{
			URL resource = provider.getResourceURL( );
			String path = null;
			try
			{
				if ( resource != null )
				{
					path = FileLocator.resolve( resource ).getFile( );
				}

			}
			catch ( IOException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}
			if ( resource == null || path == null || !new File( path ).exists( ) )
			{
				btnBrowse.setEnabled( false );
			}
			else
			{
				btnBrowse.setEnabled( true );
			}
		}
		if ( handle != null )
		{
			resourceKeytext.setText( DEUtil.resolveNull( handle.getDisplayKey( ) ) );
		}

	}

	protected String getExpression( String resultSet )
	{
		if ( provider.getExpressionType( ) == MapHandleProvider.EXPRESSION_TYPE_ROW )
		{
			return DEUtil.getColumnExpression( resultSet );
		}
		else if ( provider.getExpressionType( ) == MapHandleProvider.EXPRESSION_TYPE_DATA )
		{
			return DEUtil.getDataExpression( resultSet );
		}

		return null;

	}

	private void initializeProviderType( )
	{
		if ( designHandle instanceof DataItemHandle )
		{
			DataItemHandle dataItem = (DataItemHandle) designHandle;
			if ( dataItem.getContainer( ) instanceof ExtendedItemHandle )
			{
				provider.setExpressionType( MapHandleProvider.EXPRESSION_TYPE_DATA );
			}
			else
			{
				provider.setExpressionType( MapHandleProvider.EXPRESSION_TYPE_ROW );
			}
		}
	}
}