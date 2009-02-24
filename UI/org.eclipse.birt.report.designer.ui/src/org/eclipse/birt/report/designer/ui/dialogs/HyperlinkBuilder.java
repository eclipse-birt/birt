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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.ComboBoxCellEditor;
import org.eclipse.birt.report.designer.ui.widget.ExpressionCellEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The builder for hyper link
 */

public class HyperlinkBuilder extends BaseDialog
{

	private static final String TITLE = Messages.getString( "HyperlinkBuilder.DialogTitle" ); //$NON-NLS-1$
	private static final String LABEL_SELECT_TYPE = Messages.getString( "HyperlinkBuilder.Label.SelectType" ); //$NON-NLS-1$
	private static final String LABEL_LOCATION = Messages.getString( "HyperlinkBuilder.Label.Location" ); //$NON-NLS-1$
	private static final String LABEL_TARGET = Messages.getString( "HyperlinkBuilder.Label.Target" ); //$NON-NLS-1$
	private static final String LABEL_TOOLTIP = Messages.getString( "HyperlinkBuilder.Label.Tooltip" ); //$NON-NLS-1$
	private static final String LABEL_BOOKMARK = Messages.getString( "HyperlinkBuilder.Label.Bookmark" ); //$NON-NLS-1$
	private static final String LABEL_LINKED_EXPRESSION = Messages.getString( "HyperlinkBuilder.Label.LinkedExpression" ); //$NON-NLS-1$
	private static final String LABEL_REPORT_PARAMETER = Messages.getString( "HyperlinkBuilder.Label.Parameters" ); //$NON-NLS-1$
	private static final String RADIO_NONE = Messages.getString( "HyperlinkBuilder.Radio.None" ); //$NON-NLS-1$
	private static final String RADIO_URI = Messages.getString( "HyperlinkBuilder.Radio.Uri" ); //$NON-NLS-1$
	private static final String RADIO_BOOKMARK = Messages.getString( "HyperlinkBuilder.Radio.Bookmark" ); //$NON-NLS-1$
	private static final String RADIO_DRILLTHROUGH = Messages.getString( "HyperlinkBuilder.Radio.DrillThrough" ); //$NON-NLS-1$

	private static final String COLUMN_PARAMETER = Messages.getString( "HyperlinkBuilder.Column.Parameters" ); //$NON-NLS-1$
	private static final String COLUMN_VALUE = Messages.getString( "HyperlinkBuilder.Column.Values" ); //$NON-NLS-1$
	private static final String COLUMN_REQUIRED = Messages.getString( "HyperlinkBuilder.ParameterRequired" ); //$NON-NLS-1$
	private static final String COLUMN_DATA_TYPE = Messages.getString( "HyperlinkBuilder.Column.DataType" ); //$NON-NLS-1$

	private static final Image IMAGE_OPEN_FILE = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_OPEN_FILE );

	private static final Image REQUIRED_ICON = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_DEFAULT );
	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );

	private static final String ERROR_MSG_REPORT_REQUIRED = Messages.getString( "HyperlinkBuilder.ErrorMessage.ReportReqired" ); //$NON-NLS-1$
	private static final String ERROR_MSG_INVALID_REPORT = Messages.getString( "HyperlinkBuilder.ErrorMessage.InvalidReport" ); //$NON-NLS-1$

	private static final String TOOLTIP_BROWSE_FILE = Messages.getString( "HyperlinkBuilder.BrowseForFile" ); //$NON-NLS-1$
	private static final String TOOLTIP_EXPRESSION = Messages.getString( "HyperlinkBuilder.OpenExpression" ); //$NON-NLS-1$
	private static final String[] STEPS = new String[]{
			"",//$NON-NLS-1$ 
			Messages.getString( "HyperlinkBuilder.Step.1" ),//$NON-NLS-1$ 
			Messages.getString( "HyperlinkBuilder.Step.2" ), //$NON-NLS-1$ 
			Messages.getString( "HyperlinkBuilder.Step.3" ), //$NON-NLS-1$ 
			Messages.getString( "HyperlinkBuilder.Step.4" ), //$NON-NLS-1$ 
			Messages.getString( "HyperlinkBuilder.Step.5" ), //$NON-NLS-1$ 
			Messages.getString( "HyperlinkBuilder.Step.6" ) //$NON-NLS-1$ 
	};

	private static final String REQUIED_MARK = "*"; //$NON-NLS-1$

	private static final IChoiceSet CHOICESET_TARGET = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_TARGET_NAMES_TYPE );

	private static final ParamBinding dummyParameterBinding = StructureFactory.createParamBinding( );

	private ActionHandle inputHandle;
	private List dataSetList;

	private Composite displayArea;

	private String selectedType;
	// Radios
	private Button noneRadio, uriRadio, bookmarkRadio, drillRadio;

	private Combo bookmarkChooser, targetChooser;

	private Text bookmarkEditor;

	/**
	 * Target report design location.
	 */
	private Text locationEditor;

	private CLabel messageLine;

	private TableViewer paramBindingTable;

	private ComboBoxCellEditor parameterChooser;

	private ArrayList<ParamBinding> bindingList = new ArrayList<ParamBinding>( );
	private ArrayList<ParameterHandle> parameterList = new ArrayList<ParameterHandle>( );

	private List<String> typeFilterList = new ArrayList<String>( 2 );
	private boolean bTargetEnabled = true;
	private boolean bTooltipEnabled = true;

	private Object targetReportHandle;

	private HashMap paramTypes = new HashMap( );

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			ArrayList list = ( (ArrayList) inputElement );
			ArrayList elementsList = (ArrayList) list.clone( );
			// if ( bindingList.size( ) != parameterList.size( ) )
			// {
			// To check if all parameters have been bound.
			elementsList.add( dummyParameterBinding );
			// }
			return elementsList.toArray( );
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			if ( columnIndex == 1 )
			{
				ParamBinding parameterBinding = ( (ParamBinding) element );
				boolean isRequired = isParameterRequired( parameterBinding.getParamName( ) );
				if ( isRequired )
				{
					return REQUIRED_ICON;
				}
			}
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			String text = null;
			ParamBinding parameterBinding = ( (ParamBinding) element );
			if ( parameterBinding != dummyParameterBinding )
			{
				if ( columnIndex == 0 )
				{
					text = parameterBinding.getParamName( );

				}
				else if ( columnIndex == 2 )
				{
					String name = parameterBinding.getParamName( );
					String dataType = (String) paramTypes.get( name );
					if ( dataType == null )
					{
						return ""; //$NON-NLS-1$
					}
					return getDisplayDataType( dataType );
				}
				else if ( columnIndex == 3 )
				{
					text = parameterBinding.getExpression( );
				}
			}
			if ( text == null )
			{
				text = ""; //$NON-NLS-1$
			}
			return text;
		}

		public void addListener( ILabelProviderListener listener )
		{
		}

		public void dispose( )
		{
		}

		public boolean isLabelProperty( Object element, String property )
		{
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
		}

	};

	private ICellModifier cellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			if ( element == dummyParameterBinding
					&& COLUMN_VALUE.equals( property ) )
			{
				return false;
			}
			return true;
		}

		public Object getValue( Object element, String property )
		{
			ParamBinding paramBinding = ( (ParamBinding) element );
			Object value = null;
			if ( COLUMN_VALUE.equals( property ) )
			{
				value = paramBinding.getExpression( );
				if ( value == null )
				{
					value = ""; //$NON-NLS-1$
				}
			}
			else if ( COLUMN_PARAMETER.equals( property ) )
			{
				value = paramBinding.getParamName( );
				if ( value == null )
				{
					value = ""; //$NON-NLS-1$
				}

				buildParameterChoices( (String) value );
				// int index = -1;
				// for ( int i = 0; i < parameterChooser.getItems( ).length; i++
				// )
				// {
				// if ( parameterChooser.getItems( )[i].equals( paramBinding.
				// getParamName( ) ) )
				// {
				// index = i;
				// break;
				// }
				// }

			}
			return value;
		}

		public void modify( Object element, String property, Object value )
		{
			if ( element instanceof Item )
			{
				element = ( (Item) element ).getData( );
			}
			ParamBinding paramBinding = ( (ParamBinding) element );
			if ( COLUMN_VALUE.equals( property ) )
			{
				paramBinding.setExpression( (String) value );
			}
			else if ( COLUMN_PARAMETER.equals( property ) )
			{
				// int index = -1;
				// for ( int i = 0; i < parameterChooser.getItems( ).length; i++
				// )
				// {
				// if ( parameterChooser.getItems( )[i].equals( value ) )
				// {
				// index = i;
				// break;
				// }
				// }

				if ( ( value != null ) && ( (String) value ).length( ) > 0 )
				{
					if ( paramBinding == dummyParameterBinding )
					{
						paramBinding = StructureFactory.createParamBinding( );
						bindingList.add( paramBinding );
					}
					paramBinding.setParamName( (String) value );
				}
			}
			paramBindingTable.refresh( );
			validateTables( );
		}
	};

	/**
	 * Drillthrough, target report design.
	 */
	private Button reportDesignButton;

	/**
	 * Drillthrough, target report document.
	 */
	private Button reportDocumentButton;

	/**
	 * Drillthrough, target report document location.
	 */
	private Text documentEditor;

	/**
	 * Drillthrough, targetBookmark.
	 */
	private Button targetBookmarkButton;

	/**
	 * Drillthrough, toc entity.
	 */
	private Button tocButton;

	private Button sameFrameButton;
	private Button newWindowButton;
	private Button wholePageButton;
	private Button parentFrameButton;

	private String[] supportedFormats;

	private Combo anchorChooser, targetFormatsChooser;
	private Button checkButton;
	private Group targetGroup;

	private boolean isIDE = false;
	private Text tooltipText;
	private ScrolledComposite scrollContent;

	public HyperlinkBuilder( Shell parentShell )
	{
		super( parentShell, TITLE );
	}

	public HyperlinkBuilder( )
	{
		this( UIUtil.getDefaultShell( ) );
	}

	public HyperlinkBuilder( Shell parentShell, boolean isIDE )
	{
		super( parentShell, TITLE );
		this.isIDE = isIDE;
	}

	public HyperlinkBuilder( boolean isIDE )
	{
		this( UIUtil.getDefaultShell( ) );
		this.isIDE = isIDE;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );

		createSelectionArea( composite );
		new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		scrollContent = new ScrolledComposite( composite, SWT.V_SCROLL );
		scrollContent.setAlwaysShowScrollBars( false );
		scrollContent.setExpandHorizontal( true );
		scrollContent.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		displayArea = new Composite( scrollContent, SWT.NONE );
		scrollContent.setContent( displayArea );

		Shell shell = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( )
				.getShell( );

		int height = shell.getBounds( ).height < 510 + 200 ? shell.getBounds( ).height - 200
				: 510;
		if ( !bTargetEnabled )
		{
			height -= 70;
		}
		if ( !bTooltipEnabled )
		{
			height -= 50;
		}
		scrollContent.setLayoutData( new GridData( 600, height ) );

		displayArea.setLayout( new GridLayout( 3, false ) );
		new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		UIUtil.bindHelp( parent, IHelpContextIds.HYPERLINK_BUILDER_ID );

		Point size = displayArea.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		displayArea.setSize( size );

		return composite;
	}

	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		composite.setLayout( new GridLayout( 2, false ) );

		new Label( composite, SWT.NONE ).setText( LABEL_SELECT_TYPE );

		if ( !typeFilterList.contains( DesignChoiceConstants.ACTION_LINK_TYPE_NONE ) )
		{
			noneRadio = new Button( composite, SWT.RADIO );
			noneRadio.setText( RADIO_NONE );
			addRadioListener( noneRadio,
					DesignChoiceConstants.ACTION_LINK_TYPE_NONE );
		}

		if ( !typeFilterList.contains( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK ) )
		{
			UIUtil.createBlankLabel( composite );

			uriRadio = new Button( composite, SWT.RADIO );
			uriRadio.setText( RADIO_URI );
			addRadioListener( uriRadio,
					DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK );
		}

		if ( !( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) instanceof LibraryHandle )
				&& !typeFilterList.contains( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK ) )
		{

			UIUtil.createBlankLabel( composite );

			bookmarkRadio = new Button( composite, SWT.RADIO );
			bookmarkRadio.setText( RADIO_BOOKMARK );
			addRadioListener( bookmarkRadio,
					DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK );
		}

		if ( !typeFilterList.contains( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH ) )
		{
			UIUtil.createBlankLabel( composite );

			drillRadio = new Button( composite, SWT.RADIO );
			drillRadio.setText( RADIO_DRILLTHROUGH );
			addRadioListener( drillRadio,
					DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
		}

	}

	/**
	 * Adds hyperlink type filter to disable one type
	 * 
	 * @param disabledType
	 *            hyperlink type string
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_NONE
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_HYPERLINK
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_BOOKMARK_LINK
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_DRILL_THROUGH
	 */
	public void addHyperlinkTypeFilter( String disabledType )
	{
		typeFilterList.add( disabledType );
	}

	public void setTargetEnabled( boolean bEnabled )
	{
		this.bTargetEnabled = bEnabled;
	}

	public void setTooltipEnabled( boolean bEnabled )
	{
		this.bTooltipEnabled = bEnabled;
	}

	private void switchTo( String type )
	{
		selectedType = type;
		clearArea( );
		closeTargetReport( );

		displayArea.setLayout( new GridLayout( 3, false ) );

		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( type ) )
		{
			switchToURI( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( type ) )
		{
			switchToBookmark( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( type ) )
		{
			switchToDrillthrough( );
		}

		initDisplayArea( );
		displayArea.layout( );

		Point size = displayArea.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		displayArea.setSize( size );
	}

	private void switchToURI( )
	{
		new Label( displayArea, SWT.NONE ).setText( REQUIED_MARK
				+ LABEL_LOCATION );
		locationEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
		locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		locationEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );
		Composite buttonArea = new Composite( displayArea, SWT.NONE );
		buttonArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, true ) );
		buttonArea.setLayoutData( new GridData( ) );
		createBrowerButton( buttonArea, locationEditor, true, false );
		createExpressionButton( buttonArea, locationEditor );
		createTargetBar( );
		createTooltipBar( );
	}

	private void createTooltipBar( )
	{
		if ( bTooltipEnabled )
		{
			new Label( displayArea, SWT.NONE ).setText( LABEL_TOOLTIP );
			tooltipText = new Text( displayArea, SWT.BORDER );
			tooltipText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			UIUtil.createBlankLabel( displayArea );
		}
	}

	private void switchToBookmark( )
	{
		createBookmarkBar( true );
		createTooltipBar( );
	}

	private void switchToDrillthrough( )
	{
		// new Label( displayArea, SWT.NONE ).setText( REQUIED_MARK +
		// LABEL_REPORT );
		// locationEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
		// locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
		// ) );
		// locationEditor.addModifyListener( new ModifyListener( ) {
		//
		// public void modifyText( ModifyEvent e )
		// {
		// closeReport( );
		// initParamterBindings( );
		// initBookmarkList( reportHandle );
		// updateButtons( );
		// }
		//
		// } );
		// createBrowerButton( displayArea, locationEditor, false, true );
		//
		// UIUtil.createBlankLabel( displayArea );
		// messageLine = new CLabel( displayArea, SWT.NONE );
		// GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		// gd.horizontalSpan = 2;
		// messageLine.setLayoutData( gd );
		//
		// createBindingTable( displayArea );
		// createBookmarkBar( false );
		// createTargetBar( );
		// createFormatBar( );

		displayArea.setLayout( new GridLayout( ) );
		// final ScrolledComposite scrolledContainer = new ScrolledComposite(
		// displayArea,
		// SWT.NONE );

		final Composite container = new Composite( displayArea, SWT.NONE );
		container.setLayout( new GridLayout( ) );
		// scrolledContainer.setContent( container );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		messageLine = new CLabel( container, SWT.NONE );
		messageLine.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createDrillthroughSelectTargetReport( container );
		createDrillthroughSelectTargetAnchor( container );
		createDrillthroughCreateLinkExpression( container );
		if ( bTargetEnabled )
		{
			createDrillthroughSelectShowTarget( container );
		}
		createDrillthroughSelectFormat( container );
		if ( bTooltipEnabled )
		{
			createDrillthroughTooltip( container );
		}

		container.setSize( container.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
	}

	private void createDrillthroughTooltip( Composite container )
	{
		Group formatsGroup = new Group( container, SWT.NONE );
		formatsGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatsGroup.setText( STEPS[6]
				+ Messages.getString( "HyperlinkBuilder.DrillThrough.Tooltip" ) ); //$NON-NLS-1$
		formatsGroup.setLayout( new GridLayout( 1, false ) );
		tooltipText = new Text( formatsGroup, SWT.BORDER );
		tooltipText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	private void createDrillthroughSelectTargetReport( Composite container )
	{
		targetGroup = new Group( container, SWT.NONE );
		targetGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		targetGroup.setText( STEPS[1]
				+ Messages.getString( "HyperlinkBuilder.DrillThrough.SelectTargetReport" ) ); //$NON-NLS-1$
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		targetGroup.setLayout( layout );

		reportDesignButton = new Button( targetGroup, SWT.RADIO );
		reportDesignButton.setText( Messages.getString( "HyperlinkBuilder.ReportDesignButton" ) ); //$NON-NLS-1$
		reportDesignButton.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				selectRadio( targetGroup, reportDesignButton );
				initTargetReport( locationEditor.getText( ) );
				initParamterBindings( );
				deSelectAnchor( );
				updateButtons( );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}
		} );

		locationEditor = new Text( targetGroup, SWT.BORDER | SWT.SINGLE );
		locationEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				closeTargetReport( );
				initTargetReport( locationEditor.getText( ) );
				initParamterBindings( );
				updateButtons( );
				deSelectAnchor( );
			}

		} );
		locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createBrowerButton( targetGroup, locationEditor, false, true );

		reportDocumentButton = new Button( targetGroup, SWT.RADIO );
		reportDocumentButton.setText( Messages.getString( "HyperlinkBuilder.ReportDocumentButton" ) ); //$NON-NLS-1$

		reportDocumentButton.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				selectRadio( targetGroup, reportDocumentButton );
				initTargetReport( documentEditor.getText( ) );
				initParamterBindings( );
				deSelectAnchor( );
				updateButtons( );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}
		} );

		documentEditor = new Text( targetGroup, SWT.BORDER | SWT.SINGLE );
		documentEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				closeTargetReport( );
				initTargetReport( documentEditor.getText( ) );
				initParamterBindings( );
				updateButtons( );
				deSelectAnchor( );
			}

		} );
		documentEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createBrowerButton( targetGroup,
				documentEditor,
				false,
				true,
				new String[]{
					"*.rptdocument"} ); //$NON-NLS-1$

		createBindingTable( targetGroup );

	}

	private void createDrillthroughSelectTargetAnchor( Composite container )
	{
		final Group group = new Group( container, SWT.NONE );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		group.setText( STEPS[2]
				+ Messages.getString( "HyperlinkBuilder.DrillThrough.SelectTargetAnchor" ) ); //$NON-NLS-1$
		group.setLayout( new GridLayout( ) );

		targetBookmarkButton = new Button( group, SWT.RADIO );
		targetBookmarkButton.setText( Messages.getString( "HyperlinkBuilder.DrillThroughTargetBookmark" ) ); //$NON-NLS-1$
		targetBookmarkButton.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				initAnchorChooser( targetReportHandle, false );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}
		} );

		tocButton = new Button( group, SWT.RADIO );
		tocButton.setText( Messages.getString( "HyperlinkBuilder.DrillThroughTargetToc" ) ); //$NON-NLS-1$

		tocButton.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				initAnchorChooser( targetReportHandle, true );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}
		} );

		anchorChooser = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		anchorChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		anchorChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( anchorChooser.getData( ) instanceof List )
				{
					List value = (List) anchorChooser.getData( );
					bookmarkEditor.setText( (String) value.get( anchorChooser.getSelectionIndex( ) ) );
				}
				else
				{
					bookmarkEditor.setText( anchorChooser.getText( ) );
				}
				updateButtons( );
			}

		} );
	}

	private void createDrillthroughCreateLinkExpression( Composite container )
	{
		Group group = new Group( container, SWT.NONE );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		group.setText( STEPS[3]
				+ Messages.getString( "HyperlinkBuilder.DrillThrough.CreateLinkExpr" ) ); //$NON-NLS-1$
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		group.setLayout( layout );
		new Label( group, SWT.NONE ).setText( Messages.getString( "HyperlinkBuilder.DrillThroughLinkExpression" ) ); //$NON-NLS-1$

		bookmarkEditor = new Text( group, SWT.BORDER | SWT.READ_ONLY );
		bookmarkEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createExpressionButton( group, bookmarkEditor );
	}

	private void createDrillthroughSelectShowTarget( Composite container )
	{
		Group group = new Group( container, SWT.NONE );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		group.setText( STEPS[4]
				+ Messages.getString( "HyperlinkBuilder.DrillThrough.ShowTargetReport" ) ); //$NON-NLS-1$
		group.setLayout( new GridLayout( 2, false ) );

		newWindowButton = new Button( group, SWT.RADIO );
		newWindowButton.setText( Messages.getString( "HyperlinkBuilder.DrillThroughNewWindow" ) ); //$NON-NLS-1$

		parentFrameButton = new Button( group, SWT.RADIO );
		parentFrameButton.setText( Messages.getString( "HyperlinkBuilder.DrillThroughParentFrame" ) ); //$NON-NLS-1$	

		sameFrameButton = new Button( group, SWT.RADIO );
		sameFrameButton.setText( Messages.getString( "HyperlinkBuilder.DrillThroughSameFrame" ) ); //$NON-NLS-1$

		wholePageButton = new Button( group, SWT.RADIO );
		wholePageButton.setText( Messages.getString( "HyperlinkBuilder.DrillThroughWholePage" ) ); //$NON-NLS-1$

	}

	private void createDrillthroughSelectFormat( Composite container )
	{
		ReportEngine engine = new ReportEngine( new EngineConfig( ) );
		supportedFormats = engine.getSupportedFormats( );

		Group formatsGroup = new Group( container, SWT.NONE );
		formatsGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatsGroup.setText( ( bTargetEnabled ? STEPS[5] : STEPS[4] )
				+ Messages.getString( "HyperlinkBuilder.DrillThrough.SelectFormat" ) ); //$NON-NLS-1$
		formatsGroup.setLayout( new GridLayout( 2, false ) );

		checkButton = new Button( formatsGroup, SWT.CHECK );
		checkButton.setText( Messages.getString( "HyperlinkBuilder.TargetReportFormat" ) ); //$NON-NLS-1$
		checkButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				targetFormatsChooser.setEnabled( ( (Button) e.widget ).getSelection( ) );
				if ( ( (Button) e.widget ).getSelection( ) == false
						&& targetFormatsChooser.getSelectionIndex( ) != -1 )
				{
					targetFormatsChooser.deselect( targetFormatsChooser.getSelectionIndex( ) );
				}
			}
		} );

		targetFormatsChooser = new Combo( formatsGroup, SWT.BORDER
				| SWT.READ_ONLY );
		targetFormatsChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		targetFormatsChooser.setItems( supportedFormats );
		targetFormatsChooser.setEnabled( false );
		// select format affects getting TOCTree from ReportDocument
		targetFormatsChooser.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( tocButton.getSelection( )
						&& targetReportHandle instanceof IReportDocument )
				{
					initAnchorChooser( targetReportHandle, true );
				}
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}
		} );
	}

	private void createBindingTable( Composite parent )
	{
		Label label = new Label( parent, SWT.NONE );
		label.setText( LABEL_REPORT_PARAMETER );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_END ) );
		paramBindingTable = new TableViewer( parent, SWT.BORDER
				| SWT.SINGLE
				| SWT.FULL_SELECTION );
		Table table = paramBindingTable.getTable( );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 2;
		gd.heightHint = 60;
		table.setLayoutData( gd );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		TableColumn parameterColumn = new TableColumn( table, SWT.LEFT );
		parameterColumn.setText( COLUMN_PARAMETER );
		parameterColumn.setResizable( true );
		parameterColumn.setWidth( 100 );

		parameterColumn = new TableColumn( table, SWT.CENTER );
		parameterColumn.setText( COLUMN_REQUIRED );
		parameterColumn.setResizable( true );
		parameterColumn.setWidth( 55 );

		TableColumn dataTypeColumn = new TableColumn( table, SWT.LEFT );
		dataTypeColumn.setText( COLUMN_DATA_TYPE );
		dataTypeColumn.setResizable( true );
		dataTypeColumn.setWidth( 65 );

		TableColumn valueColumn = new TableColumn( table, SWT.LEFT );
		valueColumn.setText( COLUMN_VALUE );
		valueColumn.setResizable( true );
		valueColumn.setWidth( 115 );

		table.addKeyListener( new KeyAdapter( ) {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt
			 *      .events.KeyEvent)
			 */
			public void keyReleased( KeyEvent e )
			{
				// If Delete pressed, delete the selected row
				if ( e.keyCode == SWT.DEL )
				{
					deleteRow( );
				}
			}

		} );

		paramBindingTable.setColumnProperties( new String[]{
				COLUMN_PARAMETER,
				COLUMN_REQUIRED,
				COLUMN_DATA_TYPE,
				COLUMN_VALUE
		} );

		parameterChooser = new ComboBoxCellEditor( table,
				new String[0],
				SWT.NONE );

		ExpressionCellEditor valueEditor = new ExpressionCellEditor( table );
		valueEditor.setExpressionProvider( getExpressionProvider( ) );
		paramBindingTable.setCellEditors( new CellEditor[]{
				parameterChooser, null, null, valueEditor
		} );
		paramBindingTable.setContentProvider( contentProvider );
		paramBindingTable.setLabelProvider( labelProvider );
		paramBindingTable.setCellModifier( cellModifier );
		paramBindingTable.setInput( bindingList );
	}

	private void clearArea( )
	{
		Control[] controls = displayArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}
	}

	private void createExpressionButton( Composite parent, final Text text )
	{
		Button button = new Button( parent, SWT.PUSH );
		// button.setLayoutData( new GridData( ) );
		// button.setText( "..." ); //$NON-NLS-1$
		UIUtil.setExpressionButtonImage( button );
		button.setToolTipText( TOOLTIP_EXPRESSION );
		button.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ExpressionBuilder builder = new ExpressionBuilder( text.getText( ) );
				configureExpressionBuilder( builder );
				if ( builder.open( ) == Dialog.OK )
				{
					text.setText( builder.getResult( ) );
					updateButtons( );
				}
			}

		} );

	}

	/**
	 * Configures the expression builder which is to be opened in the hyper-link
	 * builder
	 * 
	 * @param builder
	 *            Expression builder
	 */
	protected void configureExpressionBuilder( ExpressionBuilder builder )
	{
		builder.setExpressionProvier( getExpressionProvider( ) );
	}

	/**
	 * @return
	 */
	protected ExpressionProvider getExpressionProvider( )
	{
		return new ExpressionProvider( inputHandle.getElementHandle( ) );
	}

	private Button createBrowerButton( Composite parent, final Text text,
			final boolean needQuote, final boolean needFilter )
	{
		List extensionList = ReportPlugin.getDefault( )
				.getReportExtensionNameList( );
		String[] extensionNames = new String[extensionList.size( )];
		for ( int i = 0; i < extensionNames.length; i++ )
		{
			extensionNames[i] = "*." + extensionList.get( i ); //$NON-NLS-1$
		}
		return createBrowerButton( parent,
				text,
				needQuote,
				needFilter,
				extensionNames ); //$NON-NLS-1$
	}

	private Button createBrowerButton( Composite parent, final Text text,
			final boolean needQuote, final boolean needFilter,
			final String[] fileExt )
	{
		Button button = new Button( parent, SWT.PUSH );
		GridData gd = new GridData( );
		if ( !Platform.getOS( ).equals( Platform.OS_MACOSX ) )
		{
			gd.widthHint = 20;
			gd.heightHint = 20;
		}
		button.setLayoutData( gd );
		button.setImage( IMAGE_OPEN_FILE );
		button.setToolTipText( TOOLTIP_BROWSE_FILE );
		button.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String filename = null;
				if ( !isIDE || getProjectFolder( ) == null )
				{
					FileDialog dialog = new FileDialog( UIUtil.getDefaultShell( ) );
					if ( needFilter )
					{
						dialog.setFilterExtensions( fileExt );
					}
					filename = dialog.open( );
				}
				else
				{
					ProjectFileDialog dialog;
					if ( needFilter )
					{
						dialog = new ProjectFileDialog( getProjectFolder( ),
								fileExt );
					}
					else
						dialog = new ProjectFileDialog( getProjectFolder( ) );
					if ( dialog.open( ) == Window.OK )
					{
						filename = dialog.getPath( );
					}
				}
				try
				{

					if ( filename != null )
					{
						File file = new File( filename );
						if ( !( file.isFile( ) && file.exists( ) ) )
						{
							ExceptionHandler.openErrorMessageBox( Messages.getString( "HyperlinkBuilder.FileNameError.Title" ), //$NON-NLS-1$
									Messages.getString( "HyperlinkBuilder.FileNameError.Message" ) ); //$NON-NLS-1$
							return;
						}

						filename = file.toURL( ).toString( );

						// should check extensions in Linux enviroment
						if ( needFilter
								&& checkExtensions( fileExt, filename ) == false )
						{
							ExceptionHandler.openErrorMessageBox( Messages.getString( "HyperlinkBuilder.FileNameError.Title" ), //$NON-NLS-1$
									Messages.getString( "HyperlinkBuilder.FileNameError.Message" ) ); //$NON-NLS-1$
							return;
						}

						filename = URIUtil.getRelativePath( getBasePath( ),
								filename );

						filename = new Path( filename ).toString( );
						if ( needQuote )
						{
							filename = "\"" + filename + "\""; //$NON-NLS-1$ //$NON-NLS-2$
						}
						text.setText( filename );
					}

					updateButtons( );
				}
				catch ( Exception ex )
				{
					ExceptionHandler.handle( ex );
				}
			}

		} );
		return button;
	}

	private void createTargetBar( )
	{
		if ( bTargetEnabled )
		{
			new Label( displayArea, SWT.NONE ).setText( LABEL_TARGET );
			targetChooser = new Combo( displayArea, SWT.READ_ONLY | SWT.BORDER );
			targetChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			targetChooser.setItems( ChoiceSetFactory.getDisplayNamefromChoiceSet( CHOICESET_TARGET ) );
			UIUtil.createBlankLabel( displayArea );
		}
	}

	private void createBookmarkBar( boolean isRequired )
	{
		String label;
		if ( isRequired )
		{
			label = REQUIED_MARK + LABEL_BOOKMARK;
		}
		else
		{
			label = LABEL_BOOKMARK;
		}
		new Label( displayArea, SWT.NONE ).setText( label );
		bookmarkChooser = new Combo( displayArea, SWT.BORDER | SWT.READ_ONLY );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		bookmarkChooser.setLayoutData( gd );
		bookmarkChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				bookmarkEditor.setText( bookmarkChooser.getText( ) );
				updateButtons( );
			}

		} );

		new Label( displayArea, SWT.NONE ).setText( LABEL_LINKED_EXPRESSION );
		bookmarkEditor = new Text( displayArea, SWT.BORDER | SWT.READ_ONLY );
		bookmarkEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createExpressionButton( displayArea, bookmarkEditor );
	}

	protected void okPressed( )
	{
		try
		{
			// Remove original settings
			inputHandle.setToolTip( null );
			inputHandle.setURI( null );
			inputHandle.setTargetBookmark( null );
			inputHandle.setTargetBookmarkType( null );
			inputHandle.setTargetWindow( null );
			inputHandle.setTargetFileType( null );
			inputHandle.setReportName( null );
			inputHandle.setFormatType( null );
			inputHandle.getMember( Action.PARAM_BINDINGS_MEMBER )
					.setValue( null );

			if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
			{
				inputHandle.setURI( locationEditor.getText( ).trim( ) );
				if ( bTargetEnabled )
				{
					inputHandle.setTargetWindow( ChoiceSetFactory.getValueFromChoiceSet( targetChooser.getText( ),
							CHOICESET_TARGET ) );
				}
				saveTooltip( );
			}
			else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
			{
				inputHandle.setTargetBookmark( bookmarkEditor.getText( ).trim( ) );
				saveTooltip( );
			}
			else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
			{
				if ( reportDesignButton.getSelection( ) )
				{
					inputHandle.setTargetFileType( DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN );
					inputHandle.setReportName( locationEditor.getText( ).trim( ) );
					for ( Iterator iter = bindingList.iterator( ); iter.hasNext( ); )
					{
						inputHandle.addParamBinding( (ParamBinding) iter.next( ) );
					}
				}
				else if ( reportDocumentButton.getSelection( ) )
				{
					inputHandle.setTargetFileType( DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT );
					inputHandle.setReportName( documentEditor.getText( ).trim( ) );
					for ( Iterator iter = bindingList.iterator( ); iter.hasNext( ); )
					{
						inputHandle.addParamBinding( (ParamBinding) iter.next( ) );
					}
				}

				if ( !StringUtil.isBlank( bookmarkEditor.getText( ) )
						&& !bookmarkEditor.getText( ).equals( "---" ) ) //$NON-NLS-1$
				{
					inputHandle.setTargetBookmark( bookmarkEditor.getText( )
							.trim( ) );
				}

				if ( targetBookmarkButton.getSelection( ) )
				{
					inputHandle.setTargetBookmarkType( DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK );
				}
				else if ( tocButton.getSelection( ) )
				{
					inputHandle.setTargetBookmarkType( DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC );
				}

				if ( bTargetEnabled )
				{
					if ( sameFrameButton.getSelection( ) )
					{
						inputHandle.setTargetWindow( DesignChoiceConstants.TARGET_NAMES_TYPE_SELF );
					}
					else if ( newWindowButton.getSelection( ) )
					{
						inputHandle.setTargetWindow( DesignChoiceConstants.TARGET_NAMES_TYPE_BLANK );
					}
					else if ( wholePageButton.getSelection( ) )
					{
						inputHandle.setTargetWindow( DesignChoiceConstants.TARGET_NAMES_TYPE_TOP );
					}
					else if ( parentFrameButton.getSelection( ) )
					{
						inputHandle.setTargetWindow( DesignChoiceConstants.TARGET_NAMES_TYPE_PARENT );
					}
				}

				// for ( int i = 0; i < supportedFormats.length; i++ )
				// {
				// if ( ( (Button) formatCheckBtns.get( supportedFormats[i] )
				// ).getSelection( ) )
				// {
				// inputHandle.setFormatType( supportedFormats[i] );
				// }
				// }
				int index = targetFormatsChooser.getSelectionIndex( );
				if ( checkButton.getSelection( ) && index != -1 )
				{
					inputHandle.setFormatType( targetFormatsChooser.getItem( index ) );
				}
				else
				{
					inputHandle.setFormatType( null );
				}
				saveTooltip( );
			}
			inputHandle.setLinkType( selectedType );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		setResult( inputHandle );
		super.okPressed( );
	}

	private void saveTooltip( ) throws SemanticException
	{
		if ( bTooltipEnabled )
		{
			if ( tooltipText.getText( ).trim( ).length( ) == 0 )
				inputHandle.setToolTip( null );
			else
				inputHandle.setToolTip( tooltipText.getText( ).trim( ) );
		}
	}

	public boolean close( )
	{
		closeTargetReport( );
		return super.close( );
	}

	/**
	 * Set the action to edit.
	 * 
	 * @param input
	 *            the action to edit.
	 */
	public void setInput( ActionHandle input )
	{
		inputHandle = input;
	}

	protected boolean initDialog( )
	{
		dataSetList = DEUtil.getDataSetList( inputHandle.getElementHandle( ) );

		switchTo( inputHandle.getLinkType( ) );

		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
		{
			uriRadio.setSelection( true );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
		{
			bookmarkRadio.setSelection( true );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
		{
			drillRadio.setSelection( true );
		}
		else
		{
			noneRadio.setSelection( true );
		}

		this.getShell( ).addListener( SWT.Resize, new Listener( ) {

			public void handleEvent( Event event )
			{
				// TODO Auto-generated method stub
				GridData gd = (GridData) scrollContent.getLayoutData( );
				if ( gd.horizontalAlignment != SWT.FILL
						|| gd.verticalAlignment != SWT.FILL )
				{
					scrollContent.setLayoutData( new GridData( GridData.FILL_BOTH ) );
				}
				scrollContent.layout( false, true );
				displayArea.layout( false, true );
			}
		} );
		return super.initDialog( );
	}

	private void initDisplayArea( )
	{
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
		{
			if ( inputHandle.getURI( ) != null )
			{
				locationEditor.setText( inputHandle.getURI( ) );
			}
			if ( bTargetEnabled )
			{
				if ( inputHandle.getTargetWindow( ) != null )
				{
					targetChooser.setText( ChoiceSetFactory.getDisplayNameFromChoiceSet( inputHandle.getTargetWindow( ),
							CHOICESET_TARGET ) );
				}
				else
				{
					targetChooser.select( 0 );
				}
			}
			loadTooltip( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
		{
			if ( inputHandle.getTargetBookmark( ) != null )
			{
				bookmarkEditor.setText( inputHandle.getTargetBookmark( ) );
			}
			initBookmarkList( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ) );
			loadTooltip( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
		{
			// if ( inputHandle.getTargetBookmark( ) != null )
			// {
			// bookmarkEditor.setText( inputHandle.getTargetBookmark( ) );
			// }
			// if ( inputHandle.getReportName( ) != null )
			// {
			// locationEditor.setText( inputHandle.getReportName( ) );
			// }
			// else
			// {
			// initParamterBindings( );
			// initBookmarkList( null );
			// }
			// if ( inputHandle.getTargetWindow( ) != null )
			// {
			// targetChooser.setText(
			// ChoiceSetFactory.getDisplayNameFromChoiceSet(
			// inputHandle.getTargetWindow( ),
			// CHOICESET_TARGET ) );
			// }
			// else
			// {
			// targetChooser.select( 0 );
			// }
			// if ( inputHandle.getFormatType( ) != null )
			// {
			// formatChooser.setText(
			// ChoiceSetFactory.getDisplayNameFromChoiceSet(
			// inputHandle.getFormatType( ),
			// CHOICESET_FORMAT ) );
			// }
			// else
			// {
			// formatChooser.select( 0 );
			// }
			// TODO

			if ( DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT.equals( inputHandle.getTargetFileType( ) ) )
			{
				reportDocumentButton.setSelection( true );
				if ( inputHandle.getReportName( ) != null )
				{
					documentEditor.setText( inputHandle.getReportName( ) );
				}
				selectRadio( targetGroup, reportDocumentButton );
			}
			else
			{
				reportDesignButton.setSelection( true );
				if ( inputHandle.getReportName( ) != null )
				{
					locationEditor.setText( inputHandle.getReportName( ) );
				}
				selectRadio( targetGroup, reportDesignButton );
			}
			// edit mode, initail pre-setting
			if ( inputHandle.getReportName( ) != null )
			{
				initTargetReport( inputHandle.getReportName( ) );
			}

			if ( DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK.equals( inputHandle.getTargetBookmarkType( ) ) )
			{
				targetBookmarkButton.setSelection( true );
				initAnchorChooser( targetReportHandle, false );
			}
			else if ( DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC.equals( inputHandle.getTargetBookmarkType( ) ) )
			{
				tocButton.setSelection( true );
				initAnchorChooser( targetReportHandle, true );
			}
			if ( inputHandle.getTargetBookmark( ) != null )
			{
				bookmarkEditor.setText( inputHandle.getTargetBookmark( ) );
			}
			else
			{
				bookmarkEditor.setText( "---" ); //$NON-NLS-1$
			}

			if ( bTargetEnabled )
			{
				if ( DesignChoiceConstants.TARGET_NAMES_TYPE_BLANK.equals( inputHandle.getTargetWindow( ) ) )
				{
					newWindowButton.setSelection( true );
				}
				else if ( DesignChoiceConstants.TARGET_NAMES_TYPE_SELF.equals( inputHandle.getTargetWindow( ) ) )
				{
					sameFrameButton.setSelection( true );
				}
				else if ( DesignChoiceConstants.TARGET_NAMES_TYPE_TOP.equals( inputHandle.getTargetWindow( ) ) )
				{
					wholePageButton.setSelection( true );
				}
				else if ( DesignChoiceConstants.TARGET_NAMES_TYPE_PARENT.equals( inputHandle.getTargetWindow( ) ) )
				{
					parentFrameButton.setSelection( true );
				}
				else
				{
					newWindowButton.setSelection( true );
				}
			}

			if ( inputHandle.getFormatType( ) != null )
			{
				for ( int index = 0; index < supportedFormats.length; index++ )
				{
					if ( supportedFormats[index].equals( inputHandle.getFormatType( ) ) )
					{
						checkButton.setSelection( true );
						targetFormatsChooser.setEnabled( true );
						targetFormatsChooser.select( index );
						break;
					}
				}
			}
			loadTooltip( );
		}
		updateButtons( );
	}

	private void loadTooltip( )
	{
		if ( bTooltipEnabled )
		{
			if ( inputHandle.getToolTip( ) != null )
				tooltipText.setText( inputHandle.getToolTip( ) );
		}
	}

	private void initParamterBindings( )
	{
		if ( targetReportHandle != null )
		{
			bindingList.clear( );
			parameterList.clear( );

			String errorMessage = null;
			String newFilename = null;
			if ( reportDesignButton.getSelection( ) )
			{
				newFilename = locationEditor.getText( ).trim( );
			}
			else if ( reportDocumentButton.getSelection( ) )
			{
				newFilename = documentEditor.getText( ).trim( );
			}
			if ( newFilename == null || newFilename.length( ) == 0 )
			{
				errorMessage = ERROR_MSG_REPORT_REQUIRED;
			}
			else
			{
				ReportDesignHandle tmpReportDesign = null;
				if ( targetReportHandle instanceof IReportDocument )
				{
					tmpReportDesign = ( (IReportDocument) targetReportHandle ).getReportDesign( );
				}
				else if ( targetReportHandle instanceof ReportDesignHandle )
				{
					tmpReportDesign = (ReportDesignHandle) targetReportHandle;
				}

				if ( tmpReportDesign != null )
				{
					if ( targetReportHandle instanceof ReportDesignHandle )
					{
						paramTypes.clear( );
						for ( Iterator iter = ( (ReportDesignHandle) tmpReportDesign ).getAllParameters( )
								.iterator( ); iter.hasNext( ); )
						{
							Object obj = iter.next( );
							if ( obj instanceof ParameterHandle )
							{
								ParameterHandle param = (ParameterHandle) obj;
								parameterList.add( param );
								if ( param instanceof ScalarParameterHandle )
								{
									paramTypes.put( param.getName( ),
											( (ScalarParameterHandle) param ).getDataType( ) );
								}

							}
							// bug 147604
							// else if ( obj instanceof ParameterGroupHandle )
							// {
							// parameterList.addAll( ( (ParameterGroupHandle)
							// obj
							// ).getParameters( )
							// .getContents( ) );
							// }
						}
					}

					if ( newFilename.equals( inputHandle.getReportName( ) ) )
					{
						for ( Iterator iter = inputHandle.paramBindingsIterator( ); iter.hasNext( ); )
						{
							ParamBindingHandle handle = (ParamBindingHandle) iter.next( );
							bindingList.add( (ParamBinding) handle.getStructure( ) );
						}
					}
				}
			}
			if ( errorMessage != null )
			{
				messageLine.setText( errorMessage );
				messageLine.setImage( ERROR_ICON );
				paramBindingTable.getTable( ).setEnabled( false );
			}
			else
			{
				messageLine.setText( "" ); //$NON-NLS-1$
				messageLine.setImage( null );
				paramBindingTable.getTable( ).setEnabled( true );
			}

			paramBindingTable.refresh( );
			// paramBindingTable.getTable( )
			// .setEnabled( !parameterList.isEmpty( ) );

			updateButtons( );
		}
	}

	private void initBookmarkList( Object handle )
	{
		bookmarkChooser.removeAll( );
		if ( handle != null && handle instanceof ReportDesignHandle )
		{
			bookmarkChooser.setItems( (String[]) ( (ReportDesignHandle) handle ).getAllBookmarks( )
					.toArray( new String[0] ) );
			bookmarkChooser.setText( bookmarkEditor.getText( ) );
		}
		bookmarkChooser.setEnabled( bookmarkChooser.getItemCount( ) > 0 );
	}

	private void initAnchorChooser( Object handle, boolean isToc )
	{
		anchorChooser.removeAll( );
		if ( handle instanceof ReportDesignHandle )
		{
			if ( isToc )
			{
				List chooserItems = ( (ReportDesignHandle) handle ).getAllTocs( );
				chooserItems.add( 0, "---" ); //$NON-NLS-1$
				// anchorChooser.setItems( (String[]) ( (ReportDesignHandle)
				// handle ).getAllTocs( )
				// .toArray( new String[0] ) );
				anchorChooser.setItems( (String[]) chooserItems.toArray( new String[0] ) );
			}
			else
			{
				List chooserItems = ( (ReportDesignHandle) handle ).getAllBookmarks( );
				chooserItems.add( 0, "---" ); //$NON-NLS-1$
				// anchorChooser.setItems( (String[]) ( (ReportDesignHandle)
				// handle ).getAllBookmarks( )
				// .toArray( new String[0] ) );
				anchorChooser.setItems( (String[]) chooserItems.toArray( new String[0] ) );
			}
		}
		else if ( handle instanceof IReportDocument )
		{
			if ( isToc )
			{
				String format = "html"; //$NON-NLS-1$
				if ( targetFormatsChooser.getSelectionIndex( ) != -1 )
				{
					format = supportedFormats[targetFormatsChooser.getSelectionIndex( )];
				}
				ITOCTree tocTree = ( (IReportDocument) handle ).getTOCTree( format,
						SessionHandleAdapter.getInstance( )
								.getSessionHandle( )
								.getULocale( ) );
				TOCNode rootTocNode = tocTree.getRoot( );
				// TOCNode rootTocNode = ( (IReportDocument) handle ).findTOC(
				// null );
				List chooserItems = getAllTocDisplayString( rootTocNode );
				chooserItems.add( 0, "---" ); //$NON-NLS-1$
				// anchorChooser.setItems( (String[]) getAllTocDisplayString(
				// rootTocNode ).toArray( new String[0] ) );
				anchorChooser.setItems( (String[]) chooserItems.toArray( new String[0] ) );
			}
			else
			{
				anchorChooser.setItems( getDocumentBookmarks( (IReportDocument) handle ) );
			}
		}

		bookmarkEditor.setText( "" ); //$NON-NLS-1$

		String bookmark = inputHandle.getTargetBookmark( );
		String[] chooserValues = anchorChooser.getItems( );
		if ( bookmark != null && chooserValues != null )
		{
			for ( int i = 0; i < chooserValues.length; i++ )
			{
				if ( bookmark.equals( chooserValues[i] ) )
				{
					anchorChooser.select( i );
					bookmarkEditor.setText( anchorChooser.getText( ) );
					break;
				}
			}
		}

		anchorChooser.setEnabled( anchorChooser.getItemCount( ) > 0 );
	}

	private String[] getDocumentBookmarks( IReportDocument rdoc )
	{
		List bookmarks = rdoc.getBookmarks( );
		String[] bookmarkArray = new String[bookmarks.size( ) + 1];
		bookmarkArray[0] = "---"; //$NON-NLS-1$
		int i = 1;
		for ( Iterator iter = bookmarks.iterator( ); iter.hasNext( ); )
		{
			bookmarkArray[i] = "\"" + iter.next( ) + "\""; //$NON-NLS-1$//$NON-NLS-2$
			i++;
		}
		return bookmarkArray;
	}

	private List getAllTocDisplayString( TOCNode parent )
	{
		List tocList = new ArrayList( );
		if ( parent.getParent( ) != null )
		{
			tocList.add( "\"" + parent.getDisplayString( ) + "\"" ); //$NON-NLS-1$//$NON-NLS-2$
		}
		List childToc = parent.getChildren( );
		for ( Iterator iter = childToc.iterator( ); iter.hasNext( ); )
		{
			TOCNode node = (TOCNode) iter.next( );
			tocList.addAll( getAllTocDisplayString( node ) );
		}
		return tocList;
	}

	private void addRadioListener( Button radio, final String type )
	{
		radio.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( selectedType != type )
				{
					switchTo( type );
				}
			}
		} );
	}

	private void updateButtons( )
	{
		boolean okEnable = true;
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
		{
			okEnable = !StringUtil.isBlank( locationEditor.getText( ) );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
		{
			okEnable = !StringUtil.isBlank( bookmarkEditor.getText( ) );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
		{
			okEnable = targetReportHandle != null
					&& ( !StringUtil.isBlank( locationEditor.getText( ) ) || !StringUtil.isBlank( documentEditor.getText( ) ) )
					&& messageLine.getImage( ) == null;
		}
		getOkButton( ).setEnabled( okEnable );
	}

	private void validateTables( )
	{
		for ( int i = 0; i < bindingList.size( ); i++ )
		{
			for ( int j = i + 1; j < bindingList.size( ); j++ )
			{
				if ( bindingList.get( i )
						.getParamName( )
						.equals( bindingList.get( j ).getParamName( ) ) )
				{
					String errorMessage = Messages.getString( "HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName" ); //$NON-NLS-1$
					messageLine.setText( errorMessage );
					messageLine.setImage( ERROR_ICON );
					updateButtons( );
					return;
				}
			}
		}
		messageLine.setText( "" ); //$NON-NLS-1$
		messageLine.setImage( null );
		updateButtons( );
	}

	private void buildParameterChoices( String selectedParameter )
	{
		ArrayList avaliavleList = new ArrayList( );
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ParameterHandle parameter = (ParameterHandle) iter.next( );
			avaliavleList.add( parameter.getQualifiedName( ) );
		}
		for ( Iterator iter = bindingList.iterator( ); iter.hasNext( ); )
		{
			ParamBinding paramBinding = (ParamBinding) iter.next( );
			if ( !paramBinding.getParamName( ).equals( selectedParameter ) )
			{
				avaliavleList.remove( paramBinding.getParamName( ) );
			}
		}
		parameterChooser.setItems( (String[]) avaliavleList.toArray( new String[0] ) );
	}

	private void deleteRow( )
	{
		ParamBinding paramBinding = getSelectedBinding( );
		if ( paramBinding != null )
		{
			bindingList.remove( paramBinding );
			paramBindingTable.refresh( );
		}
	}

	private ParamBinding getSelectedBinding( )
	{
		IStructuredSelection selection = (IStructuredSelection) paramBindingTable.getSelection( );
		if ( selection.size( ) == 1 )
		{
			return (ParamBinding) selection.getFirstElement( );
		}
		return null;
	}

	private void closeTargetReport( )
	{
		if ( targetReportHandle instanceof ReportDesignHandle )
		{
			( (ReportDesignHandle) targetReportHandle ).close( );
		}
		else if ( targetReportHandle instanceof IReportDocument )
		{
			( (IReportDocument) targetReportHandle ).close( );
		}
		targetReportHandle = null;
	}

	private void initTargetReport( String newFilename )
	{
		closeTargetReport( );
		targetReportHandle = null;
		String errorMessage = null;
		if ( newFilename.endsWith( ".rptdocument" ) ) //$NON-NLS-1$
		{
			ReportEngine engine = new ReportEngine( new EngineConfig( ) );

			try
			{
				targetReportHandle = engine.openReportDocument( resolvePath( newFilename ) );
			}
			catch ( EngineException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				errorMessage = e.getMessage( );
			}
		}
		else
		{
			try
			{
				targetReportHandle = SessionHandleAdapter.getInstance( )
						.getSessionHandle( )
						.openDesign( newFilename );
			}
			catch ( DesignFileException e )
			{
				try
				{
					targetReportHandle = SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openDesign( resolvePath( newFilename ) );
				}
				catch ( DesignFileException e1 )
				{
					errorMessage = ERROR_MSG_INVALID_REPORT;
				}
			}
		}
		if ( errorMessage != null )
		{
			messageLine.setText( errorMessage );
			messageLine.setImage( ERROR_ICON );
		}
		else
		{
			messageLine.setText( "" ); //$NON-NLS-1$
			messageLine.setImage( null );
		}
	}

	private String resolvePath( String file_path )
	{
		String rootPath = null;
		if ( file_path.startsWith( "/" ) )
		{
			rootPath = getProjectFolder();
		}else{
			rootPath = getBasePath( );
		}
		return URIUtil.resolveAbsolutePath( rootPath, file_path );
	}

	private String getBasePath( )
	{
		String baseFile = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getFileName( );
		return new File( baseFile ).getParent( );
	}

	/**
	 * Set the action to edit with a serialized string
	 * 
	 * @param input
	 *            the serialized string
	 * @param handle
	 *            DesignElementHandle
	 * @throws DesignFileException
	 */
	public void setInputString( String input, DesignElementHandle handle )
			throws DesignFileException
	{
		setInput( ModuleUtil.deserializeAction( input, handle ) );
	}

	/**
	 * Returns the serialized result action.
	 * 
	 * @return the serialized result action
	 * @throws IOException
	 */
	public String getResultString( ) throws IOException
	{
		return ModuleUtil.serializeAction( (ActionHandle) getResult( ) );
	}

	/**
	 * Set radio members enable, and others radio's members disable.
	 * 
	 * @param container
	 * @param radio
	 * @param enable
	 */
	private void selectRadio( Composite container, Button radio )
	{
		Control[] children = container.getChildren( );
		boolean isChoiceChild = false;
		for ( int i = 0; i < children.length; i++ )
		{
			if ( children[i] instanceof Label )
			{
				continue;
			}
			// break if style is radio
			if ( ( children[i].getStyle( ) & SWT.RADIO ) != 0 )
			{
				if ( children[i] == radio )
				{
					isChoiceChild = true;
				}
				else
				{
					isChoiceChild = false;
				}
				continue;
			}
			if ( !isChoiceChild )
			{
				children[i].setEnabled( false );
			}
			else
			{
				children[i].setEnabled( true );
			}
		}
	}

	private void deSelectAnchor( )
	{
		targetBookmarkButton.setSelection( false );
		tocButton.setSelection( false );
		anchorChooser.removeAll( );
		bookmarkEditor.setText( "" ); //$NON-NLS-1$
	}

	protected boolean isParameterRequired( String paramName )
	{
		if ( paramName == null )
		{
			return false;
		}

		if ( parameterList != null )
		{
			for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
			{
				Object obj = iter.next( );
				if ( obj instanceof ScalarParameterHandle
						&& ( (ScalarParameterHandle) obj ).getName( )
								.equals( paramName ) )
				{
					return !( (ScalarParameterHandle) obj ).allowNull( )
							|| !( (ScalarParameterHandle) obj ).allowBlank( );
				}
			}
		}
		return false;
	}

	private boolean checkExtensions( String fileExt[], String fileName )
	{
		for ( int i = 0; i < fileExt.length; i++ )
		{
			String ext = fileExt[i].substring( fileExt[i].lastIndexOf( '.' ) );
			if ( fileName.toLowerCase( ).endsWith( ext.toLowerCase( ) ) )
			{
				return true;
			}
		}
		return false;
	}

	protected String getProjectFolder( )
	{
		return UIUtil.getProjectFolder( );
	}

	private String getDisplayDataType( String dataType )
	{
		final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
				.getElement( ReportDesignConstants.SCALAR_PARAMETER_ELEMENT )
				.getProperty( ScalarParameterHandle.DATA_TYPE_PROP )
				.getAllowedChoices( );

		IChoice choice = DATA_TYPE_CHOICE_SET.findChoice( dataType );
		if ( choice == null )
		{
			return ""; //$NON-NLS-1$
		}

		return choice.getDisplayName( );

	}

}
