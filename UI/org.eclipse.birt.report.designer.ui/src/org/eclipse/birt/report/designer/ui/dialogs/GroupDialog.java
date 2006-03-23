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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SortingHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.Spinner;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * Group Properties Dialog
 */

public class GroupDialog extends BaseDialog
{

	private static final String GROUP_DLG_GROUP_FILTER_SORTING = Messages.getString( "GroupDialog.Label.FilterSorting" ); //$NON-NLS-1$

	private static final String GROUP_DLG_GROUP_RANGE_LABEL = Messages.getString( "GroupDialog.Label.Range" ); //$NON-NLS-1$

	private static final String GROUP_DLG_GROUP_INTERVAL_LABEL = Messages.getString( "GroupDialog.Label.Interval" ); //$NON-NLS-1$

	private static final String GROUP_DLG_GROUP_KEY_LABEL = Messages.getString( "GroupDialog.Label.GroupOn" ); //$NON-NLS-1$

	private static final String GROUP_DLG_GROUP_NAME_LABEL = Messages.getString( "GroupDialog.Label.Name" ); //$NON-NLS-1$

	private static final String TAB_SORTING = Messages.getString( "GroupDialog.Tab.Sorting" ); //$NON-NLS-1$

	private static final String TAB_FILTER = Messages.getString( "GroupDialog.Tab.Filter" ); //$NON-NLS-1$

	private static final String GROUP_DLG_INCLUDE_FOOTER_LABEL = Messages.getString( "GroupDialog.Label.IncludeFooter" ); //$NON-NLS-1$

	private static final String GROUP_DLG_INCLUDE_HEADER_LABEL = Messages.getString( "GroupDialog.Label.IncludeHeader" ); //$NON-NLS-1$;

	private static final String GROUP_DLG_HEADER_FOOTER_LABEL = Messages.getString( "GroupDialog.Label.HeaderFooter" ); //$NON-NLS-1$

	public static final String GROUP_DLG_INTERVAL_BASE_LABEL = Messages.getString( "GroupDialog.Label.IntervalBase" ); //$NON-NLS-1$

	private static final String GROUP_DLG_AREA_MSG = Messages.getString( "GroupDialog.Dialog.GroupDetail" ); //$NON-NLS-1$

	public static final String GROUP_DLG_TITLE_NEW = Messages.getString( "GroupDialog.Title.New" ); //$NON-NLS-1$

	public static final String GROUP_DLG_TITLE_EDIT = Messages.getString( "GroupDialog.Title.Edit" ); //$NON-NLS-1$

	private List dataSetList = null, columnList;

	private GroupHandle inputGroup;

	/**
	 * The name editor
	 */
	private Text nameEditor;

	/**
	 * The group key and interval type combo box.
	 */
	private Combo keyChooser, intervalType;

	/**
	 * The spinner to choose interval range;
	 */
	private Spinner intervalRange;

	private FormPage filterPage, sortPage;

	/**
	 * The include check box and sorting direction radio box
	 */
	private Button includeHeader, includeFooter, ascending, descending;

	private Button intervalBaseButton;

	private Text intervalBaseText;

	private Text tocEditor;

	final private static IChoice[] intervalChoicesAll = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_INTERVAL )
			.getChoices( );

	final private static IChoice sortByAscending = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_SORT_DIRECTION )
			.findChoice( DesignChoiceConstants.SORT_DIRECTION_ASC );

	final private static IChoice sortByDescending = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_SORT_DIRECTION )
			.findChoice( DesignChoiceConstants.SORT_DIRECTION_DESC );

	final private static String SORT_GROUP_TITLE = DEUtil.getPropertyDefn( ReportDesignConstants.TABLE_GROUP_ELEMENT,
			GroupHandle.SORT_DIRECTION_PROP )
			.getDisplayName( );

	final private static IChoice[] pagebreakBeforeChoicesAll = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PAGE_BREAK_BEFORE )
			.getChoices( );
	final private static IChoice[] pagebreakAfterChoicesAll = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER )
			.getChoices( );

	final private static IChoice[] intervalChoicesString = getIntervalChoicesString( );
	final private static IChoice[] intervalChoicesDate = getIntervalChoicesDate( );
	final private static IChoice[] intervalChoicesNumeric = getIntervalChoicesNumeric( );
	private static IChoice[] intervalChoices = intervalChoicesAll;
	private String previoiusKeyExpression = "";

	private Button repeatHeaderButton;

	private Combo pagebreakAfterCombo;

	private Combo pagebreakBeforeCombo;

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 */
	public GroupDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Assert.isNotNull( dataSetList );

		Composite topComposite = (Composite) super.createDialogArea( parent );
		createTitleArea( topComposite );

		Composite composite = new Composite( topComposite, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		composite.setLayout( new GridLayout( 2, true ) );
		createFieldArea( composite );
		createGroupArea( composite );
		createTOCArea( composite );

		createFilterSortingArea( topComposite );

		return topComposite;
	}

	private void createTOCArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 200;
		composite.setLayoutData( gd );

		// Creates TOC expression name Label
		new Label( composite, SWT.NONE ).setText( Messages.getString( "GroupDialog.Dialog.TOC" ) ); //$NON-NLS-1$

		// Creates TOC area
		Composite tocArea = new Composite( composite, SWT.NONE );
		tocArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		tocArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		// Creates expression editor
		tocEditor = new Text( tocArea, SWT.SINGLE | SWT.BORDER );
		tocEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Button exprButton = new Button( tocArea, SWT.PUSH );
		exprButton.setText( "..." ); //$NON-NLS-1$
		exprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				ExpressionBuilder expressionBuilder = new ExpressionBuilder( tocEditor.getText( ) );
				expressionBuilder.setExpressionProvier( new ExpressionProvider( dataSetList ) );

				if ( expressionBuilder.open( ) == OK )
				{
					tocEditor.setText( expressionBuilder.getResult( ) );
				}
			}
		} );
	}

	/**
	 * Creates the title area
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createTitleArea( Composite parent )
	{
		int margins = 2;
		final Composite titleArea = new Composite( parent, SWT.NONE );
		FormLayout layout = new FormLayout( );
		layout.marginHeight = margins;
		layout.marginWidth = margins;
		titleArea.setLayout( layout );

		Display display = parent.getDisplay( );
		Color background = JFaceColors.getBannerBackground( display );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20 + ( margins * 3 );
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
		label.setText( GROUP_DLG_AREA_MSG );
	}

	/**
	 * Creates the field area
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createFieldArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 200;
		composite.setLayoutData( gd );

		// Creates group name Label
		new Label( composite, SWT.NONE ).setText( GROUP_DLG_GROUP_NAME_LABEL );

		// Creates group name editor
		nameEditor = new Text( composite, SWT.SINGLE | SWT.BORDER );
		nameEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// Creates group key Label
		new Label( composite, SWT.NONE ).setText( GROUP_DLG_GROUP_KEY_LABEL );

		// Creates group key chooser
		Composite keyArea = new Composite( composite, SWT.NONE );
		keyArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		keyArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		keyChooser = new Combo( keyArea, SWT.DROP_DOWN );
		keyChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		keyChooser.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				if ( !keyChooser.getText( ).trim( ).equals( "" ) )
					resetInterval( );
			}

		} );
		Button exprButton = new Button( keyArea, SWT.PUSH );
		exprButton.setText( "..." ); //$NON-NLS-1$
		exprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{

				ExpressionBuilder expressionBuilder = new ExpressionBuilder( getKeyExpression( ) );
				expressionBuilder.setExpressionProvier( new ExpressionProvider( dataSetList ) );

				if ( expressionBuilder.open( ) == OK )
				{
					setKeyExpression( expressionBuilder.getResult( ).trim( ) );
				}
			}
		} );

		// Creates intervalRange area
		Composite intervalArea = new Composite( composite, SWT.NONE );
		intervalArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		intervalArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		Composite intervalTypeArea = new Composite( intervalArea, SWT.NONE );
		intervalTypeArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		intervalTypeArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 1,
				false ) );

		Composite intervalRangeArea = new Composite( intervalArea, SWT.NONE );
		intervalRangeArea.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		intervalRangeArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 1,
				false ) );

		// Creates intervalRange labels
		new Label( intervalTypeArea, SWT.NONE ).setText( GROUP_DLG_GROUP_INTERVAL_LABEL );
		new Label( intervalRangeArea, SWT.NONE ).setText( GROUP_DLG_GROUP_RANGE_LABEL );

		// Creates intervalRange type chooser
		intervalType = new Combo( intervalTypeArea, SWT.READ_ONLY
				| SWT.DROP_DOWN );
		intervalType.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		for ( int i = 0; i < intervalChoices.length; i++ )
		{
			intervalType.add( intervalChoices[i].getDisplayName( ) );
		}
		intervalType.setData( intervalChoices );

		intervalType.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				intervalRange.setEnabled( intervalType.getSelectionIndex( ) != 0 );
				intervalBaseButton.setEnabled( intervalType.getSelectionIndex( ) != 0
						&& ( getColumnType( ) != String.class ) );
				intervalBaseText.setEnabled( intervalBaseButton.getEnabled( )
						&& intervalBaseButton.getSelection( ) );
			}
		} );
		// Creates intervalRange range chooser
		intervalRange = new Spinner( intervalRangeArea, SWT.NONE );
		intervalRange.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		intervalRange.setMinimum( 0 );
		intervalRange.setMaximum( Integer.MAX_VALUE );
		intervalRange.setStep( 1 );

		// Creates interval base editor
		intervalBaseButton = new Button( composite, SWT.CHECK );
		intervalBaseButton.setText( GROUP_DLG_INTERVAL_BASE_LABEL ); //$NON-NLS-1$
		intervalBaseButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		intervalBaseButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				intervalBaseText.setEnabled( intervalBaseButton.getSelection( ) );
			}
		} );

		intervalBaseText = new Text( composite, SWT.SINGLE | SWT.BORDER );
		intervalBaseText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	/**
	 * Creates the group area
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createGroupArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		layoutData.verticalSpan = 2;
		composite.setLayoutData( layoutData );
		composite.setLayout( new GridLayout( ) );

		Group includeGroup = new Group( composite, SWT.NONE );

		includeGroup.setText( GROUP_DLG_HEADER_FOOTER_LABEL );
		includeGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		includeGroup.setLayout( new FillLayout( SWT.VERTICAL ) );

		Composite includeGroupComposite = new Composite( includeGroup, SWT.NONE );
		includeGroupComposite.setLayout( new GridLayout( ) );

		includeHeader = new Button( includeGroupComposite, SWT.CHECK );
		includeHeader.setText( GROUP_DLG_INCLUDE_HEADER_LABEL );
		includeFooter = new Button( includeGroupComposite, SWT.CHECK );
		includeFooter.setText( GROUP_DLG_INCLUDE_FOOTER_LABEL );

		if ( inputGroup instanceof ListGroupHandle )
		{
			includeGroup.setEnabled( false );
			includeHeader.setEnabled( false );
			includeFooter.setEnabled( false );
		}
		Group sortingGroup = new Group( composite, SWT.NONE );
		sortingGroup.setText( SORT_GROUP_TITLE );
		sortingGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sortingGroup.setLayout( new FillLayout( SWT.VERTICAL ) );

		Composite sortingGroupComposite = new Composite( sortingGroup, SWT.NONE );
		sortingGroupComposite.setLayout( new GridLayout( ) );

		ascending = new Button( sortingGroupComposite, SWT.RADIO );
		ascending.setText( sortByAscending.getDisplayName( ) );
		descending = new Button( sortingGroupComposite, SWT.RADIO );
		descending.setText( sortByDescending.getDisplayName( ) );

		Group pagebreakGroup = new Group( composite, SWT.NONE );
		// TODO i18n
		pagebreakGroup.setText( "Page break" );
		pagebreakGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		pagebreakGroup.setLayout( layout );
		// TODO i18n
		new Label( pagebreakGroup, SWT.NONE ).setText( "Before:" );
		pagebreakBeforeCombo = new Combo( pagebreakGroup, SWT.NONE );
		for ( int i = 0; i < pagebreakBeforeChoicesAll.length; i++ )
		{
			pagebreakBeforeCombo.add( pagebreakBeforeChoicesAll[i].getDisplayName( ) );
		}
		pagebreakBeforeCombo.setData( pagebreakBeforeChoicesAll );

		new Label( pagebreakGroup, SWT.NONE ).setText( "After:" );
		pagebreakAfterCombo = new Combo( pagebreakGroup, SWT.NONE );
		for ( int i = 0; i < pagebreakAfterChoicesAll.length; i++ )
		{
			pagebreakAfterCombo.add( pagebreakAfterChoicesAll[i].getDisplayName( ) );
		}
		pagebreakAfterCombo.setData( pagebreakAfterChoicesAll );

		repeatHeaderButton = new Button( pagebreakGroup, SWT.CHECK );
		repeatHeaderButton.setText( "Repeat Header" );
		GridData data = new GridData( );
		data.horizontalSpan = 2;
		repeatHeaderButton.setLayoutData( data );
	}

	/**
	 * Creates sorting and filter table area
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createFilterSortingArea( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( GROUP_DLG_GROUP_FILTER_SORTING );
		group.setLayout( new GridLayout( ) );
		group.setLayoutData( new GridData( 550, 250 ) );
		ArrayList list = new ArrayList( 1 );
		list.add( inputGroup );

		TabFolder tab = new TabFolder( group, SWT.TOP );
		tab.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		TabItem filterItem = new TabItem( tab, SWT.NONE );
		filterPage = new FormPage( tab,
				FormPage.NORMAL_FUNCTION,
				new FilterHandleProvider( ) {

					public int[] getColumnWidths( )
					{
						return new int[]{
								200, 100, 100, 100
						};
					}
				} );
		filterPage.setInput( list );
		filterItem.setText( TAB_FILTER );
		filterItem.setControl( filterPage );

		TabItem sortItem = new TabItem( tab, SWT.NONE );
		sortPage = new FormPage( tab,
				FormPage.NORMAL_FUNCTION,
				new SortingHandleProvider( ) {

					public int[] getColumnWidths( )
					{
						return new int[]{
								200, 100
						};
					}
				} );
		sortPage.setInput( list );
		sortItem.setText( TAB_SORTING );
		sortItem.setControl( sortPage );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog()
	 */
	protected boolean initDialog( )
	{
		if ( inputGroup.getName( ) != null )
		{
			nameEditor.setText( inputGroup.getName( ) );
		}
		columnList = DataSetManager.getCurrentInstance( )
				.getColumns( dataSetList );
		Iterator itor = columnList.iterator( );
		while ( itor.hasNext( ) )
		{
			keyChooser.add( ( (DataSetItemModel) itor.next( ) ).getDisplayName( ) );
		}
		setKeyExpression( inputGroup.getKeyExpr( ) );

		int index = getIntervalTypeIndex( inputGroup.getInterval( ) );
		intervalType.select( index );
		if ( index == 0 )
		{
			intervalRange.setEnabled( false );
			intervalBaseButton.setEnabled( false );
			intervalBaseText.setEnabled( false );
		}
		else
		{
			intervalRange.setEnabled( true );
			intervalRange.setSelection( inputGroup.getIntervalRange( ) );
			if ( getColumnType( ) == String.class )
			{
				intervalBaseButton.setEnabled( false );
				intervalBaseText.setEnabled( false );
			}
			else
			{
				intervalBaseButton.setEnabled( true );
				intervalBaseButton.setSelection( inputGroup.getIntervalBase( ) != null );
				intervalBaseText.setEnabled( intervalBaseButton.getSelection( ) );
				if ( inputGroup.getIntervalBase( ) != null )
				{
					intervalBaseText.setText( inputGroup.getIntervalBase( ) );
				}
			}
		}

		if ( inputGroup instanceof TableGroupHandle )
		{
			includeHeader.setSelection( inputGroup.hasHeader( ) );
			includeFooter.setSelection( inputGroup.hasFooter( ) );
		}
		else
		{
			includeHeader.setSelection( false );
			includeFooter.setSelection( false );
		}

		if ( DesignChoiceConstants.SORT_DIRECTION_ASC.equals( inputGroup.getSortDirection( ) ) )
		{
			ascending.setSelection( true );
		}
		else
		{
			descending.setSelection( true );
		}
		List list = new ArrayList( 1 );
		list.add( inputGroup );
		// filterPage.setInput( list );

		tocEditor.setText( UIUtil.convertToGUIString( inputGroup.getTocExpression( ) ) );

		index = getPagebreakBeforeIndex( inputGroup.getPageBreakBefore( ) );
		pagebreakBeforeCombo.select( index );

		index = getPagebreakAfterIndex( inputGroup.getPageBreakAfter( ) );
		pagebreakAfterCombo.select( index );

		if ( inputGroup.repeatHeader( ) )
		{
			repeatHeaderButton.setSelection( true );
		}

		return true;
	}

	private int getPagebreakAfterIndex( String pageBreakAfter )
	{
		int index = 0;
		for ( int i = 0; i < pagebreakAfterChoicesAll.length; i++ )
		{
			if ( pagebreakAfterChoicesAll[i].getName( ).equals( pageBreakAfter ) )
			{
				index = i;
				break;
			}
		}
		return index;
	}

	private int getPagebreakBeforeIndex( String pageBreakBefore )
	{
		int index = 0;
		for ( int i = 0; i < pagebreakBeforeChoicesAll.length; i++ )
		{
			if ( pagebreakBeforeChoicesAll[i].getName( )
					.equals( pageBreakBefore ) )
			{
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * Sets the model input.
	 * 
	 * @param input
	 */
	public void setInput( Object input )
	{
		Assert.isTrue( input instanceof GroupHandle );
		inputGroup = (GroupHandle) input;
	}

	/**
	 * Sets the dataset list to use.
	 * 
	 * @param dataSetList
	 */
	public void setDataSetList( List dataSetList )
	{
		this.dataSetList = dataSetList;
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
			inputGroup.setName( nameEditor.getText( ) );

			String newToc = UIUtil.convertToModelString( tocEditor.getText( ),
					true );
			if ( newToc != inputGroup.getTocExpression( ) )
			{
				if ( newToc == null
						|| !newToc.equals( inputGroup.getTocExpression( ) ) )
				{
					inputGroup.setTocExpression( newToc );
				}
			}

			int index = keyChooser.getSelectionIndex( );
			String oldKeyExpr = inputGroup.getKeyExpr( );
			String newKeyExpr = getKeyExpression( );

			inputGroup.setKeyExpr( newKeyExpr );
			if ( newKeyExpr != null && !newKeyExpr.equals( oldKeyExpr ) )
			{
				SlotHandle slotHandle = null;
				if ( inputGroup instanceof ListGroupHandle )
				{
					slotHandle = inputGroup.getHeader( );
				}
				else if ( inputGroup instanceof TableGroupHandle )
				{
					if ( inputGroup.getHeader( ).getCount( ) != 0 )
					{
						RowHandle rowHandle = ( (RowHandle) inputGroup.getHeader( )
								.get( 0 ) );
						CellHandle cellHandle = (CellHandle) rowHandle.getCells( )
								.get( 0 );
						slotHandle = cellHandle.getContent( );
					}
				}
				if ( slotHandle != null )
				{
					Object insertObject;
					if ( index == -1 )
					{
						insertObject = newKeyExpr.trim( );
					}
					else
					{
						insertObject = columnList.get( index );
					}
					DesignElementHandle dataItemHandle = InsertInLayoutUtil.performInsert( insertObject,
							slotHandle,
							inputGroup.getContainer( ) );
					slotHandle.add( dataItemHandle );
				}
			}

			index = intervalType.getSelectionIndex( );
			inputGroup.setInterval( intervalChoices[index].getName( ) );
			if ( index != 0 )
			{
				inputGroup.setIntervalRange( intervalRange.getSelection( ) );
			}
			else
			{
				inputGroup.setProperty( GroupHandle.INTERVAL_RANGE_PROP, null );
			}
			if ( intervalBaseText.getEnabled( ) )
			{
				inputGroup.setIntervalBase( UIUtil.convertToModelString( intervalBaseText.getText( ),
						false ) );
			}
			else
			{
				inputGroup.setIntervalBase( null );
			}

			if ( inputGroup instanceof TableGroupHandle )
			{
				if ( includeHeader.getSelection( ) != inputGroup.hasHeader( ) )
				{// the include header status changed
					if ( includeHeader.getSelection( ) )
					{// from unchecked to checked
						inputGroup.getHeader( ).add( createRow( ) );
					}
					else
					{// from checked to unchecked,clear the slot
						inputGroup.clearContents( GroupHandle.HEADER_SLOT );
					}
				}
				if ( includeFooter.getSelection( ) != inputGroup.hasFooter( ) )
				{// the include footer status changed
					if ( includeFooter.getSelection( ) )
					{// from unchecked to checked
						inputGroup.getFooter( ).add( createRow( ) );
					}
					else
					{// from checked to unchecked,clear the slot
						inputGroup.clearContents( GroupHandle.FOOTER_SLOT );
					}
				}
			}
			if ( ascending.getSelection( ) )
			{
				inputGroup.setSortDirection( DesignChoiceConstants.SORT_DIRECTION_ASC );
			}
			else
			{
				inputGroup.setSortDirection( DesignChoiceConstants.SORT_DIRECTION_DESC );
			}
			inputGroup.setPageBreakBefore( pagebreakBeforeChoicesAll[pagebreakBeforeCombo.getSelectionIndex( )].getName( ) );
			inputGroup.setPageBreakAfter( pagebreakAfterChoicesAll[pagebreakAfterCombo.getSelectionIndex( )].getName( ) );
			inputGroup.setRepeatHeader( repeatHeaderButton.getSelection( ) );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return;
		}
		setResult( inputGroup );
		super.okPressed( );
	}

	/**
	 * Returns the internal type index by its value.
	 * 
	 * @param interval
	 */
	private int getIntervalTypeIndex( String interval )
	{
		int index = 0;
		for ( int i = 0; i < intervalChoices.length; i++ )
		{
			if ( intervalChoices[i].getName( ).equals( interval ) )
			{
				index = i;
				break;
			}
		}
		return index;
	}

	private DesignElementHandle createRow( ) throws SemanticException
	{
		// DesignElementHandle handle = inputGroup.getElementFactory( )
		// .newTableRow( );
		DesignElementHandle handle = DesignElementFactory.getInstance( inputGroup.getModuleHandle( ) )
				.newTableRow( );
		TableHandle table = (TableHandle) inputGroup.getContainer( );
		for ( int i = 0; i < table.getColumnCount( ); i++ )
		{
			// handle.addElement( inputGroup.getElementFactory( ).newCell( ),
			// RowHandle.CONTENT_SLOT );
			handle.addElement( DesignElementFactory.getInstance( inputGroup.getModuleHandle( ) )
					.newCell( ),
					RowHandle.CONTENT_SLOT );
		}
		return handle;
	}

	private void setKeyExpression( String key )
	{
		keyChooser.deselectAll( );
		if ( StringUtil.isBlank( key ) )
		{
			keyChooser.setText( "" );

			return;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			if ( key.equals( DEUtil.getExpression( columnList.get( i ) ) ) )
			{
				keyChooser.select( i );
				return;
			}
		}
		keyChooser.setText( key );
	}

	private String getKeyExpression( )
	{
		String exp = null;
		if ( keyChooser.getSelectionIndex( ) != -1 )
		{
			exp = DEUtil.getExpression( columnList.get( keyChooser.getSelectionIndex( ) ) );
		}
		else
		{
			exp = keyChooser.getText( );
		}
		return exp;
	}

	/**
	 * Reset interval
	 * 
	 */
	private void resetInterval( )
	{
		String currentKeyExpression = getKeyExpression( ).trim( );
		if ( previoiusKeyExpression.equals( currentKeyExpression ) )
			return;

		if ( ExpressionUtility.isColumnExpression( currentKeyExpression ) )
			intervalChoices = getSubIntervalChoice( );
		else
			intervalChoices = intervalChoicesAll;

		resetIntervalType( intervalChoices );

		previoiusKeyExpression = currentKeyExpression;
	}

	/**
	 * Get subIntervalChoice, could be String,Date,Numeric or All
	 * 
	 * @return
	 */
	private IChoice[] getSubIntervalChoice( )
	{
		Class columnType = getColumnType( );

		if ( columnType == null )
			return intervalChoicesAll;

		if ( String.class.isAssignableFrom( columnType ) )
		{
			return intervalChoicesString;
		}
		else if ( Date.class.isAssignableFrom( columnType ) )
		{
			return intervalChoicesDate;
		}
		else if ( Number.class.isAssignableFrom( columnType ) )
		{
			return intervalChoicesNumeric;
		}
		else
		{
			return intervalChoicesAll;
		}
	}

	private static IChoice[] getIntervalChoicesString( )
	{
		String[] str = new String[]{
				DesignChoiceConstants.INTERVAL_NONE,
				DesignChoiceConstants.INTERVAL_PREFIX,
		};

		return getIntervalChoiceArray( str );
	}

	private static IChoice[] getIntervalChoicesDate( )
	{
		String[] str = new String[]{
				DesignChoiceConstants.INTERVAL_NONE,
				DesignChoiceConstants.INTERVAL_YEAR,
				DesignChoiceConstants.INTERVAL_QUARTER,
				DesignChoiceConstants.INTERVAL_MONTH,
				DesignChoiceConstants.INTERVAL_WEEK,
				DesignChoiceConstants.INTERVAL_DAY,
				DesignChoiceConstants.INTERVAL_HOUR,
				DesignChoiceConstants.INTERVAL_MINUTE,
				DesignChoiceConstants.INTERVAL_SECOND
		};

		return getIntervalChoiceArray( str );
	}

	private static IChoice[] getIntervalChoicesNumeric( )
	{
		String[] str = new String[]{
				DesignChoiceConstants.INTERVAL_NONE,
				DesignChoiceConstants.INTERVAL_INTERVAL,
		};

		return getIntervalChoiceArray( str );
	}

	/**
	 * Get intervalChoiceArray
	 * 
	 * @param str
	 * @return
	 */
	private static IChoice[] getIntervalChoiceArray( String[] str )
	{
		List choiceList = new ArrayList( );
		for ( int i = 0; i < intervalChoicesAll.length; i++ )
		{
			for ( int j = 0; j < str.length; j++ )
			{
				if ( intervalChoicesAll[i].getName( ).equals( str[j] ) )
				{
					choiceList.add( intervalChoicesAll[i] );
					break;
				}
			}
		}
		IChoice[] choice = new IChoice[choiceList.size( )];

		return (IChoice[]) choiceList.toArray( choice );
	}

	/**
	 * Get columnDataType
	 * 
	 * @return
	 */
	private Class getColumnType( )
	{
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			Object dataSetItemModel = columnList.get( i );

			if ( dataSetItemModel instanceof DataSetItemModel )
			{
				if ( ( (DataSetItemModel) dataSetItemModel ).getDataSetColumnName( )
						.equals( keyChooser.getText( ) ) )
					return DataType.getClass( ( (DataSetItemModel) dataSetItemModel ).getDataType( ) );
			}
		}

		return null;
	}

	/**
	 * Reset intervalType
	 * 
	 * @param choice
	 */
	private void resetIntervalType( IChoice[] choice )
	{
		intervalType.removeAll( );

		for ( int i = 0; i < choice.length; i++ )
		{
			intervalType.add( choice[i].getDisplayName( ) );
		}
		intervalType.setData( choice );
		intervalType.select( 0 );
		intervalType.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				intervalRange.setEnabled( intervalType.getSelectionIndex( ) != 0 );
				intervalBaseButton.setEnabled( intervalType.getSelectionIndex( ) != 0
						&& ( getColumnType( ) != String.class ) );
				intervalBaseText.setEnabled( intervalBaseButton.getEnabled( )
						&& intervalBaseButton.getSelection( ) );
			}
		} );

		enableIntervalRangeAndBase( false );
	}

	/**
	 * Enable the interval range and base right after refreshing the interval
	 * type
	 * 
	 */
	private void enableIntervalRangeAndBase( boolean bool )
	{
		intervalRange.setEnabled( bool );
		intervalBaseButton.setEnabled( bool );
		intervalBaseText.setEnabled( bool );
	}

}