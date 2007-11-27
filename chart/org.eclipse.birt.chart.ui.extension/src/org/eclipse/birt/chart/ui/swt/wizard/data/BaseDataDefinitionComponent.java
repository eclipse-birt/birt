/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.data;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DataTextDropListener;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

public class BaseDataDefinitionComponent extends DefaultSelectDataComponent
		implements
			SelectionListener,
			ModifyListener,
			FocusListener,
			KeyListener
{

	protected Composite cmpTop;

	private Combo cmbDefinition;
	private Text txtDefinition = null;

	private Button btnBuilder = null;

	private Button btnGroup = null;

	private Query query = null;

	protected SeriesDefinition seriesdefinition = null;

	protected ChartWizardContext context = null;

	private String sTitle = null;

	private String description = ""; //$NON-NLS-1$

	private String tooltipWhenBlank = Messages.getString( "BaseDataDefinitionComponent.Tooltip.InputValueExpression" ); //$NON-NLS-1$

	private boolean isQueryModified;

	private final String queryType;

	private int style = BUTTON_NONE;

	/** Indicates no button */
	public static final int BUTTON_NONE = 0;

	/** Indicates button for group sorting will be created */
	public static final int BUTTON_GROUP = 1;

	/** Indicates button for aggregation will be created */
	public static final int BUTTON_AGGREGATION = 2;

	/**
	 * 
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent( String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle )
	{
		this( BUTTON_NONE, queryType, seriesdefinition, query, context, sTitle );
	}

	/**
	 * 
	 * @param style
	 *            Specify buttons by using '|'. See {@link #BUTTON_GROUP},
	 *            {@link #BUTTON_NONE}, {@link #BUTTON_AGGREGATION}
	 * @param queryType
	 *            query type. See {@link ChartUIConstants#QUERY_CATEGORY},
	 *            {@link ChartUIConstants#QUERY_VALUE},
	 *            {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent( int style, String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle )
	{
		super( );
		assert query != null;
		this.query = query;
		this.queryType = queryType;
		this.seriesdefinition = seriesdefinition;
		this.context = context;
		this.sTitle = ( sTitle == null || sTitle.length( ) == 0 )
				? Messages.getString( "BaseDataDefinitionComponent.Text.SpecifyDataDefinition" ) //$NON-NLS-1$
				: sTitle;
		this.style = style;
	}

	public Composite createArea( Composite parent )
	{
		int numColumns = 2;
		if ( description != null && description.length( ) > 0 )
		{
			numColumns++;
		}
		if ( ( style & BUTTON_AGGREGATION ) == BUTTON_AGGREGATION )
		{
			numColumns++;
		}
		if ( ( style & BUTTON_GROUP ) == BUTTON_GROUP )
		{
			numColumns++;
		}

		cmpTop = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			glContent.numColumns = numColumns;
			glContent.marginHeight = 0;
			glContent.marginWidth = 0;
			glContent.horizontalSpacing = 2;
			cmpTop.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpTop.setLayoutData( gd );
		}

		if ( description != null && description.length( ) > 0 )
		{
			Label lblDesc = new Label( cmpTop, SWT.NONE );
			lblDesc.setText( description );
			lblDesc.setToolTipText( tooltipWhenBlank );
		}

		if ( ( style & BUTTON_AGGREGATION ) == BUTTON_AGGREGATION )
		{
			createAggregationItem( cmpTop );
		}

		String[] predefinedQuery = context.getPredefinedQuery( queryType );
		if ( predefinedQuery != null )
		{
			cmbDefinition = new Combo( cmpTop, SWT.NONE );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmbDefinition.setLayoutData( gd );
			cmbDefinition.setItems( predefinedQuery );
			cmbDefinition.setText( query.getDefinition( ) );
			cmbDefinition.addListener( SWT.Selection, new Listener( ) {

				public void handleEvent( Event event )
				{
					query.setDefinition( cmbDefinition.getText( ) );
					// If it's chart with axis, transposed chart when selecting
					// the non-first choice
					if ( context.getModel( ) instanceof ChartWithAxes )
					{
						( (ChartWithAxes) context.getModel( ) ).setTransposed( cmbDefinition.getSelectionIndex( ) > 0 );
					}
				}
			} );
			cmbDefinition.addModifyListener( this );
			cmbDefinition.addFocusListener( this );
			cmbDefinition.addKeyListener( this );
		}
		else
		{
			txtDefinition = new Text( cmpTop, SWT.BORDER | SWT.SINGLE );
			GridData gdTXTDefinition = new GridData( GridData.FILL_HORIZONTAL );
			txtDefinition.setLayoutData( gdTXTDefinition );
			if ( query != null && query.getDefinition( ) != null )
			{
				txtDefinition.setText( query.getDefinition( ) );
				txtDefinition.setToolTipText( getTooltipForDataText( query.getDefinition( ) ) );
			}
			txtDefinition.addModifyListener( this );
			txtDefinition.addFocusListener( this );
			txtDefinition.addKeyListener( this );
		}

		// Listener for handling dropping of custom table header
		Control dropControl = getInputControl( );
		DropTarget target = new DropTarget( dropControl, DND.DROP_COPY );
		Transfer[] types = new Transfer[]{
			SimpleTextTransfer.getInstance( )
		};
		target.setTransfer( types );
		// Add drop support
		target.addDropListener( new DataTextDropListener( dropControl ) );
		// Add color manager
		DataDefinitionTextManager.getInstance( )
				.addDataDefinitionText( dropControl, query );

		btnBuilder = new Button( cmpTop, SWT.PUSH );
		{
			GridData gdBTNBuilder = new GridData( );
			gdBTNBuilder.heightHint = 20;
			gdBTNBuilder.widthHint = 20;
			btnBuilder.setLayoutData( gdBTNBuilder );
			btnBuilder.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
			btnBuilder.addSelectionListener( this );
			btnBuilder.setToolTipText( Messages.getString( "DataDefinitionComposite.Tooltip.InvokeExpressionBuilder" ) ); //$NON-NLS-1$
			btnBuilder.getImage( ).setBackground( btnBuilder.getBackground( ) );
			btnBuilder.setEnabled( context.getUIServiceProvider( )
					.isInvokingSupported( ) );
			btnBuilder.setVisible( context.getUIServiceProvider( )
					.isEclipseModeSupported( ) );
		}

		if ( ( style & BUTTON_GROUP ) == BUTTON_GROUP )
		{
			btnGroup = new Button( cmpTop, SWT.PUSH );
			GridData gdBTNGroup = new GridData( );
			gdBTNGroup.heightHint = 20;
			gdBTNGroup.widthHint = 20;
			btnGroup.setLayoutData( gdBTNGroup );
			btnGroup.setImage( UIHelper.getImage( "icons/obj16/group.gif" ) ); //$NON-NLS-1$
			btnGroup.addSelectionListener( this );
			btnGroup.setToolTipText( Messages.getString( "BaseDataDefinitionComponent.Label.EditGroupSorting" ) ); //$NON-NLS-1$
		}

		// Updates color setting
		setColor( );

		return cmpTop;
	}

	public void selectArea( boolean selected, Object data )
	{
		if ( data instanceof Object[] )
		{
			Object[] array = (Object[]) data;
			seriesdefinition = (SeriesDefinition) array[0];
			query = (Query) array[1];
			setText( getInputControl( ), query.getDefinition( ) );
			DataDefinitionTextManager.getInstance( )
					.addDataDefinitionText( getInputControl( ), query );
		}
		setColor( );
	}

	private void setColor( )
	{
		if ( query != null )
		{
			Color cColor = ColorPalette.getInstance( )
					.getColor( query.getDefinition( ) );
			if ( getInputControl( ) != null )
			{
				ChartUIUtil.setBackgroundColor( getInputControl( ),
						true,
						cColor );
			}
		}
	}

	public void dispose( )
	{
		if ( getInputControl( ) != null )
		{
			DataDefinitionTextManager.getInstance( )
					.removeDataDefinitionText( getInputControl( ) );
		}
		super.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnBuilder ) )
		{
			handleBuilderAction( );
		}
		else if ( e.getSource( ).equals( btnGroup ) )
		{
			handleGroupAction( );
		}
	}

	/**
     * Handle grouping/sorting action.
	 */
	protected void handleGroupAction( )
	{
		SeriesDefinition sdBackup = (SeriesDefinition) EcoreUtil.copy( seriesdefinition );
		GroupSortingDialog groupDialog = createGroupSortingDialog( sdBackup );

		if ( groupDialog.open( ) == Window.OK )
		{
			if ( !sdBackup.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
			{
				seriesdefinition.eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				seriesdefinition.setSorting( sdBackup.getSorting( ) );
			}
			seriesdefinition.setGrouping( sdBackup.getGrouping( ) );
			seriesdefinition.getGrouping( )
					.eAdapters( )
					.addAll( seriesdefinition.eAdapters( ) );
		}
	}

	/**
     * Handle builder dialog action.
     */
	private void handleBuilderAction( )
	{
		try
		{
			String sExpr = context.getUIServiceProvider( )
					.invoke( IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS,
							getText( getInputControl( ) ),
							context.getExtendedItem( ),
							sTitle );
			setText( getInputControl( ), sExpr );
			query.setDefinition( sExpr );
		}
		catch ( ChartException e1 )
		{
			WizardBase.displayException( e1 );
		}
	}

	/**
	 * Create instance of <code>GroupSortingDialog</code> for base series or Y
	 * series.
	 * 
	 * @param sdBackup
	 * @return
	 */
	protected GroupSortingDialog createGroupSortingDialog(
			SeriesDefinition sdBackup )
	{
		return new GroupSortingDialog( cmpTop.getShell( ),
				context,
				sdBackup );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.getSource( ).equals( getInputControl( ) ) )
		{
			isQueryModified = true;
			// Reset tooltip
			getInputControl( ).setToolTipText( getTooltipForDataText( getText( getInputControl( ) ) ) );
		}
	}

	/**
	 * Sets the description in the left of data text box.
	 * 
	 * @param description
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	public void focusGained( FocusEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent e )
	{
		// Null event is fired by Drop Listener manually
		if ( e == null || e.widget.equals( getInputControl( ) ) )
		{
			saveQuery( );
		}
	}

	private void saveQuery( )
	{
		if ( isQueryModified )
		{
			Event e = new Event( );
			e.text = getText( getInputControl( ) );
			e.data = e.text;
			e.widget = getInputControl( );
			e.type = 0;
			fireEvent( e );

			if ( query != null )
			{
				query.setDefinition( getText( getInputControl( ) ) );
			}
			else
			{
				query = QueryImpl.create( getText( getInputControl( ) ) );
				query.eAdapters( ).addAll( seriesdefinition.eAdapters( ) );
				// Since the data query must be non-null, it's created in
				// ChartUIUtil.getDataQuery(), assume current null is a grouping
				// query
				seriesdefinition.setQuery( query );
			}

			// Refresh color from ColorPalette
			setColor( );
			getInputControl( ).getParent( ).layout( );
			isQueryModified = false;
		}
	}

	private String getTooltipForDataText( String queryText )
	{
		if ( queryText.trim( ).length( ) == 0 )
		{
			return tooltipWhenBlank;
		}
		return queryText;
	}

	public void keyPressed( KeyEvent e )
	{
		if ( e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR )
		{
			saveQuery( );
		}
	}

	public void keyReleased( KeyEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void setTooltipWhenBlank( String tootipWhenBlank )
	{
		this.tooltipWhenBlank = tootipWhenBlank;
	}

	private void createAggregationItem( Composite composite )
	{
		class AggregationItemAction extends Action
		{

			final String expression;

			AggregationItemAction( String text, String expression )
			{
				super( text, IAction.AS_CHECK_BOX );
				this.expression = expression;
			}

			public void run( )
			{
				SeriesGrouping currentGrouping = seriesdefinition.getGrouping( );
				if ( this.isChecked( ) )
				{
					currentGrouping.setEnabled( true );
					currentGrouping.setAggregateExpression( expression );

				}
				else
				{
					currentGrouping.setEnabled( false );
					currentGrouping.setAggregateExpression( "" ); //$NON-NLS-1$
				}
			}
		}

		ToolBar toolBar = new ToolBar( composite, SWT.FLAT | SWT.NO_FOCUS );
		ToolBarManager toolManager = new ToolBarManager( toolBar );

		class AggregationAction extends Action implements IMenuCreator
		{

			private Menu lastMenu;

			public AggregationAction( )
			{
				super( "", IAction.AS_DROP_DOWN_MENU ); //$NON-NLS-1$
				setImageDescriptor( ImageDescriptor.createFromURL( UIHelper.getURL( ChartUIConstants.IMAGE_SIGMA ) ) );
				setEnabled( getSDBase( ).getGrouping( ).isEnabled( ) );
			}

			public IMenuCreator getMenuCreator( )
			{
				return this;
			}

			public void dispose( )
			{
				if ( lastMenu != null )
				{
					lastMenu.dispose( );
					lastMenu = null;
				}
			}

			public Menu getMenu( Control parent )
			{
				if ( lastMenu != null )
				{
					lastMenu.dispose( );
				}
				lastMenu = new Menu( parent );
				createEntries( lastMenu );
				return lastMenu;
			}

			public Menu getMenu( Menu parent )
			{
				return null;
			}

			protected void addActionToMenu( Menu parent, IAction action )
			{
				ActionContributionItem item = new ActionContributionItem( action );
				item.fill( parent, -1 );
			}

			private SeriesDefinition getSDBase( )
			{
				return (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( context.getModel( ) )
						.get( 0 );
			}

			protected void createEntries( Menu menu )
			{
				SeriesDefinition sdBase = getSDBase( );
				if ( sdBase.getGrouping( ) == null
						|| !sdBase.getGrouping( ).isEnabled( )
						|| sdBase == seriesdefinition )
				{
					// If no base grouping or current series is base series,
					// disable the orthogonal grouping
					return;
				}

				try
				{
					int selectedIndex = -1;
					String[] aggNames = PluginSettings.instance( )
							.getRegisteredAggregateFunctionDisplayNames( );
					String[] aggData = PluginSettings.instance( )
							.getRegisteredAggregateFunctions( );

					SeriesGrouping grouping = seriesdefinition.getGrouping( );
					if ( grouping.isEnabled( )
							&& grouping.getAggregateExpression( ) != null )
					{
						int idx = getAggregateIndexByName( grouping.getAggregateExpression( ),
								aggData );
						if ( aggData.length > idx )
						{
							selectedIndex = idx;
						}
					}

					for ( int i = 0; i < aggNames.length; i++ )
					{
						IAction actionSum = new AggregationItemAction( aggNames[i],
								aggData[i] );
						if ( i == selectedIndex )
						{
							actionSum.setChecked( true );
						}
						addActionToMenu( menu, actionSum );
					}

				}
				catch ( ChartException e )
				{
					e.printStackTrace( );
				}
			}

			private int getAggregateIndexByName( String name, String[] names )
			{
				for ( int i = 0; i < names.length; i++ )
				{
					if ( name.equals( names[i] ) )
					{
						return i;
					}
				}

				return -1;
			}
		};

		toolManager.add( new AggregationAction( ) );
		toolManager.update( true );
	}

	private Control getInputControl( )
	{
		if ( txtDefinition != null )
		{
			return txtDefinition;
		}
		if ( cmbDefinition != null )
		{
			return cmbDefinition;
		}
		return null;
	}

	private String getText( Control control )
	{
		if ( control instanceof Text )
		{
			return ( (Text) control ).getText( );
		}
		if ( control instanceof Combo )
		{
			return ( (Combo) control ).getText( );
		}
		return ""; //$NON-NLS-1$
	}

	private void setText( Control control, String text )
	{
		if ( control instanceof Text )
		{
			( (Text) control ).setText( text );
		}
		else if ( control instanceof Combo )
		{
			( (Combo) control ).setText( text );
		}
	}
}
