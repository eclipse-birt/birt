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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ColorPalette;
import org.eclipse.birt.chart.ui.swt.wizard.internal.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.wizard.internal.DataTextDropListener;
import org.eclipse.birt.chart.ui.swt.wizard.internal.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class BaseDataDefinitionComponent extends DefaultSelectDataComponent implements
		SelectionListener,
		ModifyListener,
		FocusListener,
		KeyListener
{

	private transient Composite cmpTop;

	private transient Text txtDefinition = null;

	private transient Button btnBuilder = null;

	private transient Button btnGroup = null;

	private transient Query query = null;

	private transient SeriesDefinition seriesdefinition = null;

	private transient ChartWizardContext context = null;

	private transient String sTitle = null;

	private transient String description = ""; //$NON-NLS-1$

	private transient String tooltipWhenBlank = Messages.getString( "BaseDataDefinitionComponent.Tooltip.InputValueExpression" ); //$NON-NLS-1$

	private transient boolean isQueryModified;

	private transient int style = BUTTON_NONE;

	/** Indicates no button */
	public static final int BUTTON_NONE = 0;

	/** Indicates button for group sorting will be created */
	public static final int BUTTON_GROUP = 1;

	public BaseDataDefinitionComponent( SeriesDefinition seriesdefinition,
			Query query, ChartWizardContext context, String sTitle )
	{
		this( BUTTON_NONE, seriesdefinition, query, context, sTitle );
	}

	/**
	 * 
	 * @param style
	 *            Specify buttons by using '|'. See  
	 *            {@link #BUTTON_GROUP}, {@link #BUTTON_NONE}
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent( int style,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle )
	{
		super( );
		assert query != null;
		this.query = query;
		this.seriesdefinition = seriesdefinition;
		this.context = context;
		this.sTitle = ( sTitle == null || sTitle.length( ) == 0 ) ? Messages.getString( "BaseDataDefinitionComponent.Text.SpecifyDataDefinition" ) //$NON-NLS-1$
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

		txtDefinition = new Text( cmpTop, SWT.BORDER | SWT.SINGLE );
		{
			GridData gdTXTDefinition = new GridData( GridData.FILL_HORIZONTAL );
			gdTXTDefinition.widthHint = 50;
			txtDefinition.setLayoutData( gdTXTDefinition );
			if ( query != null && query.getDefinition( ) != null )
			{
				txtDefinition.setText( query.getDefinition( ) );
				txtDefinition.setToolTipText( getTooltipForDataText( query.getDefinition( ) ) );
			}
			txtDefinition.addModifyListener( this );
			txtDefinition.addFocusListener( this );
			txtDefinition.addKeyListener( this );

			// Listener for handling dropping of custom table header
			DropTarget target = new DropTarget( txtDefinition, DND.DROP_COPY );
			Transfer[] types = new Transfer[]{
				SimpleTextTransfer.getInstance( )
			};
			target.setTransfer( types );
			// Add drop support
			target.addDropListener( new DataTextDropListener( txtDefinition ) );
			// Add color manager
			DataDefinitionTextManager.getInstance( )
					.addDataDefinitionText( txtDefinition, query );
		}

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
			txtDefinition.setText( query.getDefinition( ) );
			DataDefinitionTextManager.getInstance( )
					.addDataDefinitionText( txtDefinition, query );
		}
		setColor( );
	}

	private void setColor( )
	{
		if ( query != null )
		{
			Color cColor = ColorPalette.getInstance( )
					.getColor( query.getDefinition( ) );
			ChartUIUtil.setBackgroundColor( txtDefinition, true, cColor );
		}
	}

	public void dispose( )
	{
		DataDefinitionTextManager.getInstance( )
				.removeDataDefinitionText( txtDefinition );
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
			try
			{
				String sExpr = context.getUIServiceProvider( )
						.invoke( IUIServiceProvider.COMMAND_EXPRESSION,
								txtDefinition.getText( ),
								context.getExtendedItem( ),
								sTitle );
				txtDefinition.setText( sExpr );
				query.setDefinition( sExpr );
			}
			catch ( ChartException e1 )
			{
				WizardBase.displayException( e1 );
			}
		}
		else if ( e.getSource( ).equals( btnGroup ) )
		{
			SeriesDefinition sdBackup = (SeriesDefinition) EcoreUtil.copy( seriesdefinition );
			GroupSortingDialog groupDialog = new GroupSortingDialog( cmpTop.getShell( ),
					context,
					sdBackup );

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
		if ( e.getSource( ).equals( txtDefinition ) )
		{
			isQueryModified = true;
			// Reset tooltip
			txtDefinition.setToolTipText( getTooltipForDataText( txtDefinition.getText( ) ) );
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
		if ( e == null || e.widget.equals( txtDefinition ) )
		{
			saveQuery( );
		}
	}

	private void saveQuery( )
	{
		if ( isQueryModified )
		{
			Event e = new Event( );
			e.text = txtDefinition.getText( );
			e.data = txtDefinition.getText( );
			e.widget = txtDefinition;
			e.type = 0;
			fireEvent( e );

			if ( query != null )
			{
				query.setDefinition( txtDefinition.getText( ) );
			}
			else
			{
				query = QueryImpl.create( txtDefinition.getText( ) );
				query.eAdapters( ).addAll( seriesdefinition.eAdapters( ) );
				// Since the data query must be non-null, it's created in
				// ChartUIUtil.getDataQuery(), assume current null is a grouping
				// query
				seriesdefinition.setQuery( query );
			}

			// Refresh color from ColorPalette
			setColor( );
			txtDefinition.getParent( ).layout( );
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
}
