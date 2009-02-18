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
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormPage;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.SortingHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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

	// private static final String TAB_BINDING = Messages.getString(
	// "GroupDialog.Tab.Binding" ); //$NON-NLS-1$

	private static final String TAB_SORTING = Messages.getString( "GroupDialog.Tab.Sorting" ); //$NON-NLS-1$

	private static final String TAB_FILTER = Messages.getString( "GroupDialog.Tab.Filter" ); //$NON-NLS-1$	

	// private static final String GROUP_DLG_INCLUDE_FOOTER_LABEL =
	// Messages.getString( "GroupDialog.Label.IncludeFooter" ); //$NON-NLS-1$

	// private static final String GROUP_DLG_INCLUDE_HEADER_LABEL =
	// Messages.getString( "GroupDialog.Label.IncludeHeader" ); //$NON-NLS-1$;

	// private static final String GROUP_DLG_HEADER_FOOTER_LABEL =
	// Messages.getString( "GroupDialog.Label.HeaderFooter" ); //$NON-NLS-1$

	private static final String GROUP_DLG_INTERVAL_BASE_LABEL = Messages.getString( "GroupDialog.Label.IntervalBase" ); //$NON-NLS-1$

	private static final String GROUP_DLG_AREA_MSG = Messages.getString( "GroupDialog.Dialog.GroupDetail" ); //$NON-NLS-1$

	public static final String GROUP_DLG_TITLE_NEW = Messages.getString( "GroupDialog.Title.New" ); //$NON-NLS-1$

	public static final String GROUP_DLG_TITLE_EDIT = Messages.getString( "GroupDialog.Title.Edit" ); //$NON-NLS-1$

	public static final String GROUP_DLG_HIDE_DETAIL = Messages.getString( "GroupDialog.buttion.HideDetail" ); //$NON-NLS-1$
	private List columnList;

	private GroupHandle inputGroup;

	/**
	 * The name editor
	 */
	private Text nameEditor;

	/**
	 * The group key and interval type combo box.
	 */
	private Combo keyChooser, intervalType;

	private Combo tocStyleType;
	/** The editor to input interval range. */
	private Text intervalRange;

	/**
	 * The include check box and sorting direction radio box
	 */
	private Button ascending, descending;

	private Button intervalBaseButton;

	private Text intervalBaseText;

	private Button hideDetail;

	private Text tocEditor;

	private Text bookmarkEditor;

	final private static IChoice[] intervalChoicesAll = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_INTERVAL )
			.getChoices( );

	final private static IChoice sortByAscending = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_SORT_DIRECTION )
			.findChoice( DesignChoiceConstants.SORT_DIRECTION_ASC );

	final private static IChoice sortByDescending = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_SORT_DIRECTION )
			.findChoice( DesignChoiceConstants.SORT_DIRECTION_DESC );

	final private static String SORT_GROUP_TITLE = DEUtil.getPropertyDefn( ReportDesignConstants.TABLE_GROUP_ELEMENT,
			GroupHandle.SORT_DIRECTION_PROP )
			.getDisplayName( );

	final private static IChoice[] pagebreakBeforeChoicesAll = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PAGE_BREAK_BEFORE )
			.getChoices( );
	final private static IChoice[] pagebreakAfterChoicesAll = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER )
			.getChoices( );
	final private static IChoice[] pagebreakInsideChoicesAll = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PAGE_BREAK_INSIDE )
			.getChoices( );
	private String[] sytleChoicesAll;

	final private static IChoice[] intervalChoicesString = getIntervalChoicesString( );
	final private static IChoice[] intervalChoicesDate = getIntervalChoicesDate( );
	final private static IChoice[] intervalChoicesNumeric = getIntervalChoicesNumeric( );
	private static IChoice[] intervalChoices = intervalChoicesAll;
	private String previoiusKeyExpression = ""; //$NON-NLS-1$

	private Button repeatHeaderButton;

	private Combo pagebreakAfterCombo;

	private Combo pagebreakBeforeCombo;

	private Combo pagebreakInsideCombo;

	private Button exprButton;

	private Button tocExprButton,bookmarkExprButton;

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 */
	public GroupDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
		sytleChoicesAll = getAllStyleChoices( );
	}

	private String[] getAllStyleChoices( )
	{
		String[] styles = ChoiceSetFactory.getStyles( );
		styles = filterPreStyles( styles );
		return styles;
	}

	private static String[] filterPreStyles( String items[] )
	{
		List preStyles = DesignEngine.getMetaDataDictionary( )
				.getPredefinedStyles( );
		List preStyleNames = new ArrayList( );

		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			preStyleNames.add( ( (IPredefinedStyle) preStyles.get( i ) ).getName( ) );
		}

		List sytleNames = new ArrayList( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( preStyleNames.indexOf( items[i] ) == -1 )
			{
				sytleNames.add( items[i] );
			}
		}

		return (String[]) ( sytleNames.toArray( new String[]{} ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		// Assert.isNotNull( dataSetList );

		if ( sytleChoicesAll == null )
		{
			sytleChoicesAll = getAllStyleChoices( );
		}

		//		Composite topComposite = (Composite) super.createDialogArea( parent );

		ScrolledComposite sc = new ScrolledComposite( parent, SWT.H_SCROLL
				| SWT.V_SCROLL );
		sc.setLayout( new FillLayout( ) );
		sc.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		applyDialogFont( sc );

		Composite topComposite = new Composite( sc, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_MARGIN );
		layout.marginWidth = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_MARGIN );
		layout.verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		layout.horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		topComposite.setLayout( layout );

		createTitleArea( topComposite );

		Composite composite = new Composite( topComposite, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		composite.setLayout( new GridLayout( 2, true ) );
		createFieldArea( composite );
		createGroupArea( composite );
		createBookmarkArea( topComposite );
		createTOCArea( topComposite );
		createFilterSortingArea( topComposite );
		UIUtil.bindHelp( parent, IHelpContextIds.GROUP_DIALOG_ID );

		sc.setContent( topComposite );
		sc.setExpandHorizontal( true );
		sc.setExpandVertical( true );
		sc.setMinWidth(500);
		sc.setMinHeight(650);
		
		return sc;
	}

	private void createBookmarkArea( Composite parent )
	{
		Composite bookmakrComposite = new Composite( parent, SWT.NONE );
		bookmakrComposite.setLayout( new GridLayout( ) );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		bookmakrComposite.setLayoutData( gd );
		bookmakrComposite.setLayout( layout );

		new Label( bookmakrComposite, SWT.NONE ).setText( Messages.getString( "GroupDialog.Label.Bookmark" ) ); //$NON-NLS-1$
		bookmarkEditor = new Text( bookmakrComposite, SWT.SINGLE | SWT.BORDER );
		gd = new GridData( );
		gd.widthHint = 180;
		bookmarkEditor.setLayoutData( gd );
		
		bookmarkExprButton = new Button( bookmakrComposite, SWT.PUSH );
		UIUtil.setExpressionButtonImage( bookmarkExprButton );
		bookmarkExprButton.setToolTipText( Messages.getString( "GroupDialog.toolTipText.openExprButton" ) ); //$NON-NLS-1$
		bookmarkExprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				ExpressionBuilder expressionBuilder = new ExpressionBuilder( bookmarkEditor.getText( ) );
				expressionBuilder.setExpressionProvier( new ExpressionProvider( inputGroup ) );

				if ( expressionBuilder.open( ) == OK )
				{
					bookmarkEditor.setText( expressionBuilder.getResult( ).trim( ) );
				}
			}
		} );
	}

	private void createTOCArea( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( Messages.getString( "GroupDialog.Dialog.TOCGroup" ) ); //$NON-NLS-1$
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		group.setLayout( layout );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// Creates TOC expression name Label
		new Label( group, SWT.NONE ).setText( Messages.getString( "GroupDialog.Dialog.TOC" ) ); //$NON-NLS-1$

		// Creates TOC area
		Composite tocArea = new Composite( group, SWT.NONE );
		tocArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		tocArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		// Creates expression editor
		tocEditor = new Text( tocArea, SWT.SINGLE | SWT.BORDER );
		GridData gd = new GridData( );
		gd.widthHint = 200;
		tocEditor.setLayoutData( gd );
		tocEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				if ( tocStyleType == null )
					return;
				if ( tocEditor.getText( ).trim( ).equals( "" ) ) //$NON-NLS-1$
				{
					tocStyleType.setEnabled( false );
				}
				else
				{
					tocStyleType.setEnabled( true );
				}
			}

		} );

		tocExprButton = new Button( tocArea, SWT.PUSH );
		// exprButton.setText( "..." ); //$NON-NLS-1$
		UIUtil.setExpressionButtonImage( tocExprButton );
		tocExprButton.setToolTipText( Messages.getString( "GroupDialog.toolTipText.openExprButton" ) ); //$NON-NLS-1$
		tocExprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				ExpressionBuilder expressionBuilder = new ExpressionBuilder( tocEditor.getText( ) );
				expressionBuilder.setExpressionProvier( new ExpressionProvider( inputGroup ) );

				if ( expressionBuilder.open( ) == OK )
				{
					tocEditor.setText( expressionBuilder.getResult( ).trim( ) );
				}
			}
		} );

		new Label( group, SWT.NONE ).setText( Messages.getString( "GroupDialog.Dialog.TOCStyle" ) ); //$NON-NLS-1$

		tocStyleType = new Combo( group, SWT.DROP_DOWN | SWT.READ_ONLY );
		gd = new GridData( );
		gd.widthHint = 200;
		tocStyleType.setLayoutData( gd );
		tocStyleType.setItems( sytleChoicesAll );
	}

	/**
	 * Creates the title area
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createTitleArea( Composite parent )
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
				if ( !keyChooser.getText( ).trim( ).equals( "" ) ) //$NON-NLS-1$
				{
					resetInterval( );
				}
			}

		} );
		keyChooser.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( keyChooser.getSelectionIndex( ) != -1 )
				{
					tocEditor.setText( DEUtil.getExpression( columnList.get( keyChooser.getSelectionIndex( ) ) ) );
				}
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}
		} );

		keyChooser.addFocusListener( new FocusAdapter( ) {

			public void focusLost( FocusEvent e )
			{
				String key = UIUtil.convertToModelString( keyChooser.getText( ),
						true );
				if ( key != null )
				{
					String tocExp = null;
					if ( keyChooser.indexOf( key ) != -1 )
					{
						tocExp = DEUtil.getExpression( columnList.get( keyChooser.indexOf( key ) ) );
					}
					else
					{
						for ( int i = 0; i < columnList.size( ); i++ )
						{
							if ( key.equals( DEUtil.getExpression( columnList.get( i ) ) ) )
							{
								tocExp = DEUtil.getExpression( columnList.get( i ) );
								break;
							}
						}
					}
					if ( !tocEditor.getText( ).equals( tocExp ) )
					{
						if ( tocExp == null )
						{
							tocEditor.setText( "" ); //$NON-NLS-1$
						}
						else
						{
							tocEditor.setText( tocExp );
						}
						return;
					}
				}
			}
		} );

		exprButton = new Button( keyArea, SWT.PUSH );
		// exprButton.setText( "..." ); //$NON-NLS-1$
		UIUtil.setExpressionButtonImage( exprButton );
		exprButton.setToolTipText( Messages.getString( "GroupDialog.toolTipText.openExprButton" ) ); //$NON-NLS-1$
		exprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{

				ExpressionBuilder expressionBuilder = new ExpressionBuilder( getKeyExpression( ) );
				expressionBuilder.setExpressionProvier( new ExpressionProvider( inputGroup ) );

				if ( expressionBuilder.open( ) == OK )
				{
					setKeyExpression( expressionBuilder.getResult( ).trim( ) );
					keyChooser.setFocus( );
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

		intervalRange = new Text( intervalRangeArea, SWT.SINGLE | SWT.BORDER );
		intervalRange.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		intervalRange.addVerifyListener( new VerifyListener( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
			 */
			public void verifyText( VerifyEvent event )
			{
				if ( event.text.length( ) <= 0 )
				{
					return;
				}

				int beginIndex = Math.min( event.start, event.end );
				int endIndex = Math.max( event.start, event.end );
				String inputtedText = intervalRange.getText( );
				String newString = inputtedText.substring( 0, beginIndex );

				newString += event.text;
				newString += inputtedText.substring( endIndex );

				event.doit = false;

				try
				{
					double value = Double.parseDouble( newString );

					if ( value >= 0 )
					{
						event.doit = true;
					}
				}
				catch ( NumberFormatException e )
				{
					return;
				}
			}
		} );

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

		hideDetail = new Button( composite, SWT.CHECK );
		hideDetail.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		hideDetail.setText( GROUP_DLG_HIDE_DETAIL );
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

		pagebreakGroup.setText( Messages.getString( "GroupDialog.PageBreak" ) ); //$NON-NLS-1$
		pagebreakGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		pagebreakGroup.setLayout( layout );

		new Label( pagebreakGroup, SWT.NONE ).setText( Messages.getString( "GroupDialog.PageBreakBefore" ) ); //$NON-NLS-1$
		pagebreakBeforeCombo = new Combo( pagebreakGroup, SWT.READ_ONLY
				| SWT.DROP_DOWN );
		for ( int i = 0; i < pagebreakBeforeChoicesAll.length; i++ )
		{
			pagebreakBeforeCombo.add( pagebreakBeforeChoicesAll[i].getDisplayName( ) );
		}
		pagebreakBeforeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( pagebreakGroup, 1, true );
		pagebreakBeforeCombo.setData( pagebreakBeforeChoicesAll );

		new Label( pagebreakGroup, SWT.NONE ).setText( Messages.getString( "GroupDialog.PageBreakAfter" ) ); //$NON-NLS-1$
		pagebreakAfterCombo = new Combo( pagebreakGroup, SWT.READ_ONLY
				| SWT.DROP_DOWN );
		for ( int i = 0; i < pagebreakAfterChoicesAll.length; i++ )
		{
			pagebreakAfterCombo.add( pagebreakAfterChoicesAll[i].getDisplayName( ) );
		}
		pagebreakAfterCombo.setData( pagebreakAfterChoicesAll );
		pagebreakAfterCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( pagebreakGroup, 1, true );

		new Label( pagebreakGroup, SWT.NONE ).setText( Messages.getString( "GroupDialog.PageBreakInside" ) ); //$NON-NLS-1$
		pagebreakInsideCombo = new Combo( pagebreakGroup, SWT.READ_ONLY
				| SWT.DROP_DOWN );
		for ( int i = 0; i < pagebreakInsideChoicesAll.length; i++ )
		{
			pagebreakInsideCombo.add( pagebreakInsideChoicesAll[i].getDisplayName( ) );
		}
		pagebreakInsideCombo.setData( pagebreakInsideChoicesAll );
		pagebreakInsideCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( pagebreakGroup, 1, true );

		repeatHeaderButton = new Button( pagebreakGroup, SWT.CHECK );
		repeatHeaderButton.setText( Messages.getString( "GroupDialog.RepeatHeader" ) ); //$NON-NLS-1$
		GridData data = new GridData( );
		data.horizontalSpan = 3;
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
		group.setLayoutData( GridDataFactory.fillDefaults( )
				.grab( true, true )
				.minSize( 500, 250 )
				.create( ) );
		ArrayList list = new ArrayList( 1 );
		list.add( inputGroup );

		TabFolder tab = new TabFolder( group, SWT.TOP );
		tab.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		// TODO:remove databinding page by cnfree on 4.28.2006
		/*
		 * TabItem bindingItem = new TabItem( tab, SWT.NONE ); FormPage
		 * bindingPage = new DataSetColumnBindingsFormPage( tab, new
		 * DataSetColumnBindingsFormHandleProvider( ) ); bindingPage.setInput(
		 * list ); bindingItem.setText( TAB_BINDING ); bindingItem.setControl(
		 * bindingPage );
		 */

		TabItem filterItem = new TabItem( tab, SWT.NONE );
		FormPage filterPage = new FormPage( tab,
				FormPage.FULL_FUNCTION_HORIZONTAL,
				new FilterHandleProvider( ) {

					public int[] getColumnWidths( )
					{
						return new int[]{
								200, 100, 100, 100
						};
					}
				},
				true );
		filterPage.setInput( list );
		filterItem.setText( TAB_FILTER );
		filterItem.setControl( filterPage );
		checkReadOnlyControl( IGroupElementModel.FILTER_PROP, filterPage );

		TabItem sortItem = new TabItem( tab, SWT.NONE );
		FormPage sortPage = new FormPage( tab,
				FormPage.FULL_FUNCTION_HORIZONTAL,
				new SortingHandleProvider( ) {

					public int[] getColumnWidths( )
					{
						return new int[]{
								200, 100
						};
					}
				},
				true );
		sortPage.setInput( list );
		sortItem.setText( TAB_SORTING );
		sortItem.setControl( sortPage );
		checkReadOnlyControl( IGroupElementModel.SORT_PROP, sortPage );
	}

	private boolean checkReadOnlyControl( String property, Control control )
	{
		PropertyHandle handle = inputGroup.getPropertyHandle( property );
		if ( handle != null && handle.isReadOnly( ) )
		{
			if ( control instanceof Text )
				( (Text) control ).setEditable( false );
			else if ( control instanceof Combo )
				( (Combo) control ).setEnabled( false );
			else if ( control instanceof Button )
				( (Button) control ).setEnabled( false );
			else if ( control instanceof Composite )
			{
				disableControl( (Composite) control );
			}
			return handle.isReadOnly( );
		}
		return false;
	}

	private void disableControl( Composite container )
	{
		Control[] children = container.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			if ( children[i] instanceof Composite )
				disableControl( (Composite) children[i] );
			else if ( !( children[i] instanceof Label ) )
				children[i].setEnabled( false );
		}
		container.setEnabled( false );
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
			checkReadOnlyControl( IGroupElementModel.GROUP_NAME_PROP,
					nameEditor );
		}

		if ( inputGroup.getBookmark( ) != null )
		{
			bookmarkEditor.setText( inputGroup.getBookmark( ) );
			checkReadOnlyControl( IGroupElementModel.BOOKMARK_PROP,
					bookmarkEditor );
		}
		refreshColumnList( );

		setKeyExpression( inputGroup.getKeyExpr( ) );
		if ( checkReadOnlyControl( IGroupElementModel.KEY_EXPR_PROP, keyChooser ) )
			exprButton.setEnabled( false );
		else
			exprButton.setEnabled( true );

		PropertyHandle property = inputGroup.getPropertyHandle( GroupElement.INTERVAL_RANGE_PROP );
		String range = property == null ? null : property.getStringValue( );

		intervalRange.setText( range == null ? "" : range ); //$NON-NLS-1$
		checkReadOnlyControl( GroupElement.INTERVAL_RANGE_PROP, intervalRange );
		int index = getIntervalTypeIndex( inputGroup.getInterval( ) );
		intervalType.select( index );
		checkReadOnlyControl( IGroupElementModel.INTERVAL_PROP, intervalType );
		if ( index == 0 )
		{
			intervalRange.setEnabled( false );
			intervalBaseButton.setEnabled( false );
			intervalBaseText.setEnabled( false );
		}
		else
		{
			intervalRange.setEnabled( true );
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
		checkReadOnlyControl( IGroupElementModel.INTERVAL_BASE_PROP,
				intervalBaseButton );
		checkReadOnlyControl( IGroupElementModel.INTERVAL_BASE_PROP,
				intervalBaseText );

		if ( DesignChoiceConstants.SORT_DIRECTION_ASC.equals( inputGroup.getSortDirection( ) ) )
		{
			ascending.setSelection( true );
		}
		else
		{
			descending.setSelection( true );
		}
		checkReadOnlyControl( IGroupElementModel.SORT_DIRECTION_PROP, ascending );
		checkReadOnlyControl( IGroupElementModel.SORT_DIRECTION_PROP,
				descending );

		List list = new ArrayList( 1 );
		list.add( inputGroup );
		// filterPage.setInput( list );

		tocEditor.setText( UIUtil.convertToGUIString( inputGroup.getTocExpression( ) ) );
		if ( inputGroup.getTOC( ) != null )
		{
			index = getTocStyleIndex( inputGroup.getTOC( ).getStyleName( ) );
			if ( index >= 0 && index < tocStyleType.getItemCount( ) )
			{
				tocStyleType.select( index );
			}
			else
			{
				if ( inputGroup.getTOC( ).getStyleName( ) != null )
					tocStyleType.setText( inputGroup.getTOC( ).getStyleName( ) );
			}
			if ( tocStyleType.getText( ) == null
					|| tocStyleType.getText( ).trim( ).length( ) == 0 )
				tocStyleType.setEnabled( false );
		}
		if ( checkReadOnlyControl( IGroupElementModel.TOC_PROP, tocEditor ) )
		{
			tocExprButton.setEnabled( false );
			tocStyleType.setEnabled( false );
		}
		else
		{
			tocExprButton.setEnabled( true );
		}
		index = getPagebreakBeforeIndex( inputGroup.getPageBreakBefore( ) );
		if ( index < 0 || index >= pagebreakBeforeCombo.getItemCount( ) )
		{
			pagebreakBeforeCombo.setText( inputGroup.getPageBreakBefore( ) );
		}
		else
		{
			pagebreakBeforeCombo.select( index );
		}
		checkReadOnlyControl( IStyleModel.PAGE_BREAK_BEFORE_PROP,
				pagebreakBeforeCombo );

		index = getPagebreakAfterIndex( inputGroup.getPageBreakAfter( ) );

		if ( index < 0 || index >= pagebreakAfterCombo.getItemCount( ) )
		{
			pagebreakAfterCombo.setText( inputGroup.getPageBreakAfter( ) );
		}
		else
		{
			pagebreakAfterCombo.select( index );
		}
		checkReadOnlyControl( IStyleModel.PAGE_BREAK_AFTER_PROP,
				pagebreakAfterCombo );

		index = getPagebreakInsideIndex( inputGroup.getPageBreakInside( ) );

		if ( index < 0 || index >= pagebreakInsideCombo.getItemCount( ) )
		{
			pagebreakInsideCombo.setText( inputGroup.getPageBreakInside( ) );
		}
		else
		{
			pagebreakInsideCombo.select( index );
		}
		checkReadOnlyControl( IStyleModel.PAGE_BREAK_AFTER_PROP,
				pagebreakInsideCombo );

		if ( inputGroup.repeatHeader( ) )
		{
			repeatHeaderButton.setSelection( true );
		}
		checkReadOnlyControl( IGroupElementModel.REPEAT_HEADER_PROP,
				repeatHeaderButton );

		hideDetail.setSelection( inputGroup.hideDetail( ) );
		checkReadOnlyControl( IGroupElementModel.HIDE_DETAIL_PROP, hideDetail );
		return true;
	}

	private void refreshColumnList( )
	{
		String selected = keyChooser.getText( );
		keyChooser.removeAll( );
		columnList = DEUtil.getVisiableColumnBindingsList( inputGroup );
		columnList = DataUtil.getValidGroupKeyBindings( columnList );
		Iterator itor = columnList.iterator( );
		while ( itor.hasNext( ) )
		{
			keyChooser.add( ( (ComputedColumnHandle) itor.next( ) ).getName( ) );
		}
		int newIndex = keyChooser.indexOf( selected );
		if ( newIndex != -1 )
		{
			keyChooser.select( newIndex );
		}
		else
		{
			keyChooser.setText( selected );
		}
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

	private int getPagebreakInsideIndex( String pageBreakInside )
	{
		int index = 0;
		for ( int i = 0; i < pagebreakInsideChoicesAll.length; i++ )
		{
			if ( pagebreakInsideChoicesAll[i].getName( )
					.equals( pageBreakInside ) )
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

	private int getTocStyleIndex( String style )
	{
		int index = -1;
		if ( style == null || style.trim( ).length( ) == 0 )
		{
			return 0;
		}
		for ( int i = 0; i < sytleChoicesAll.length; i++ )
		{
			if ( sytleChoicesAll[i].equals( style ) )
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
	// public void setDataSetList( List dataSetList )
	// {
	// this.dataSetList = dataSetList;
	// }
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

			String bookmark = bookmarkEditor.getText( );
			if ( bookmark != null && !bookmark.equals( "" ) ) //$NON-NLS-1$
			{
				inputGroup.setBookmark( bookmark );
			}

			String newToc = UIUtil.convertToModelString( tocEditor.getText( ),
					true );
			if ( newToc == null
					|| !newToc.equals( inputGroup.getTocExpression( ) ) )
			{
				inputGroup.setTocExpression( newToc );
			}
			int index;
			if ( inputGroup.getTOC( ) != null )
			{

				index = tocStyleType.getSelectionIndex( );
				if ( index == 0 )
				{
					inputGroup.getTOC( ).setStyleName( null );
				}
				else
				{
					inputGroup.getTOC( ).setStyleName( tocStyleType.getText( ) );
				}

			}

			index = keyChooser.getSelectionIndex( );
			if ( index == -1
					&& UIUtil.convertToModelString( keyChooser.getText( ), true ) != null )
			{
				String groupKey = UIUtil.convertToModelString( keyChooser.getText( ),
						true );
				if ( keyChooser.indexOf( groupKey ) != -1 )
				{
					index = keyChooser.indexOf( groupKey );
				}
				else
				{
					for ( int i = 0; i < columnList.size( ); i++ )
					{
						if ( groupKey.equals( DEUtil.getExpression( columnList.get( i ) ) ) )
						{
							index = i;
							break;
						}
					}
				}
			}
			String oldKeyExpr = inputGroup.getKeyExpr( );
			String newKeyExpr = getKeyExpression( );
			inputGroup.setKeyExpr( newKeyExpr );
			if ( newKeyExpr != null
					&& newKeyExpr.length( ) != 0
					&& !newKeyExpr.equals( oldKeyExpr )
					&& index != -1 )
			{
				SlotHandle slotHandle = null;
				// SlotHandle headerHandle = null;
				if ( inputGroup instanceof ListGroupHandle )
				{
					slotHandle = inputGroup.getHeader( );
					// headerHandle = ( (ListHandle) inputGroup.getContainer( )
					// ).getHeader( );
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
					// headerHandle = ( (TableHandle) inputGroup.getContainer( )
					// ).getHeader( );
					// if ( headerHandle.getCount( ) != 0 )
					// {
					// headerHandle = ( (RowHandle) headerHandle.get( 0 )
					// ).getCells( );
					// if ( headerHandle.getCount( ) != 0 )
					// {
					// headerHandle = ( (CellHandle) headerHandle.get( 0 )
					// ).getContent( );
					// }
					// }
				}
				if ( slotHandle != null )
				{
					DataItemHandle dataItemHandle = inputGroup.getElementFactory( )
							.newDataItem( null );
					dataItemHandle.setResultSetColumn( ( (ComputedColumnHandle) columnList.get( index ) ).getName( ) );
					slotHandle.add( dataItemHandle );
				}
			}

			index = intervalType.getSelectionIndex( );
			inputGroup.setInterval( intervalChoices[index].getName( ) );
			if ( index != 0 )
			{
				inputGroup.setIntervalRange( intervalRange.getText( ) );
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

			inputGroup.setHideDetail( hideDetail.getSelection( ) );
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
			inputGroup.setPageBreakInside( pagebreakInsideChoicesAll[pagebreakInsideCombo.getSelectionIndex( )].getName( ) );
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

	private void setKeyExpression( String key )
	{
		keyChooser.deselectAll( );
		key = StringUtil.trimString( key );
		if ( StringUtil.isBlank( key ) )
		{
			keyChooser.setText( "" ); //$NON-NLS-1$
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

	/**
	 * Reset interval
	 * 
	 */
	private void resetInterval( )
	{
		String currentKeyExpression = keyChooser.getText( );
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

	private String getKeyExpression( )
	{
		String exp = null;
		String keyText = UIUtil.convertToModelString( keyChooser.getText( ),
				true );
		if ( keyChooser.getSelectionIndex( ) != -1 )
		{
			exp = DEUtil.getExpression( columnList.get( keyChooser.getSelectionIndex( ) ) );
		}
		else if ( keyText != null && keyChooser.indexOf( keyText ) != -1 )
		{
			exp = DEUtil.getExpression( columnList.get( keyChooser.indexOf( keyText ) ) );
		}
		else
		{
			exp = keyChooser.getText( ).trim( );
		}
		return exp;
	}

}