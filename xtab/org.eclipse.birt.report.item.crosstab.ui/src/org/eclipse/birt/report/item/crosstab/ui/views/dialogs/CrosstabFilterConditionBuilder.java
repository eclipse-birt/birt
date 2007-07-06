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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.widget.PopupSelectionList;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget.ExpressionValueCellEditor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class CrosstabFilterConditionBuilder extends FilterConditionBuilder
{

	private transient boolean refreshItems = true;
	protected Combo comboGroupLevel;
	protected List groupLevelList;
	protected List groupLevelNameList;
	protected FilterConditionElementHandle inputHandle;
	protected LevelViewHandle levelViewHandle;
	protected Group group;
	protected Table memberValueTable;
	protected TableViewer dynamicViewer;
	protected ExpressionValue expressionValue1, expressionValue2;

	protected String[] columns = new String[]{
			" ",
			Messages.getString( "SelColumnMemberValue.Column.Level" ),
			Messages.getString( "SelColumnMemberValue.Column.Value" )
	};

	private static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	protected MemberValueHandle memberValueHandle;
	protected List referencedLevelList;

	public void setInput( FilterConditionElementHandle input,
			LevelViewHandle levelViewHandle )
	{
		this.inputHandle = input;
		this.levelViewHandle = levelViewHandle;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		super.setDesignHandle( handle );
		if ( editor != null )
		{
			editor.setExpressionProvider( new ExpressionProvider( handle ) );
		}

	}

	/**
	 * @param title
	 */
	public CrosstabFilterConditionBuilder( String title, String message )
	{
		this( UIUtil.getDefaultShell( ), title, message );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public CrosstabFilterConditionBuilder( Shell parentShell, String title,
			String message )
	{
		super( parentShell, title, message );
	}

	protected void createFilterConditionContent( Composite innerParent )
	{
		UIUtil.bindHelp( innerParent,
				IHelpContextIds.XTAB_FILTER_CONDITION_BUILDER );
		
		Composite groupLevelParent = new Composite( innerParent, SWT.NONE );
		groupLevelParent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 2, false );
		groupLevelParent.setLayout( glayout );

		Label lbGroupLevel = new Label( groupLevelParent, SWT.NONE );
		lbGroupLevel.setText( Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.GroupLevel" ) ); //$NON-NLS-1$

		comboGroupLevel = new Combo( groupLevelParent, SWT.READ_ONLY
				| SWT.BORDER );
		GridData gdata = new GridData( );
		gdata.widthHint = 150;
		comboGroupLevel.setLayoutData( gdata );

		getLevels( );
		String groupLeveNames[] = (String[]) groupLevelNameList.toArray( new String[groupLevelNameList.size( )] );
		comboGroupLevel.setItems( groupLeveNames );
		comboGroupLevel.addListener( SWT.Selection, ComboGroupLeveModify );

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

		expression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateMemberValues( );
				expressionValue1.setNeedRefresh( true );
				expressionValue2.setNeedRefresh( true );
				updateButtons( );
			}
		} );

		Button expBuilder = new Button( condition, SWT.PUSH );
		// expBuilder.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		expBuilder.setLayoutData( gdata );
		UIUtil.setExpressionButtonImage( expBuilder );
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

		expressionValue1 = new ExpressionValue( condition, SWT.NONE, null );
		value1 = expressionValue1.getValueText( );
		valBuilder1 = expressionValue1.getPopupButton( );

		createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );
		createDummy( condition, 3 );

		expressionValue2 = new ExpressionValue( condition, SWT.NONE, null );
		value2 = expressionValue2.getValueText( );
		valBuilder2 = expressionValue2.getPopupButton( );
		value2.setVisible( false );
		valBuilder2.setVisible( false );

		if ( operator.getItemCount( ) > 0 )
		{
			operator.select( 0 );
		}

		createMemberValuesGroup( innerParent );
		syncViewProperties( );

	}

	protected Listener ComboGroupLeveModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			updateMemberValues( );
			updateBindings( );
		}
	};

	protected Listener ComboModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			Assert.isLegal( e.widget instanceof Combo );
			Combo combo = (Combo) e.widget;
			String newValue = combo.getText( );
			String value = null;
			if ( getResultBindingName( newValue ) != null )
			{
				value = ExpressionUtil.createJSDataExpression( getResultBindingName( newValue ) );
			}
			if ( value != null )
				newValue = value;
			combo.setText( newValue );
			updateMemberValues( );
			updateButtons( );
		}
	};

	protected void createMemberValuesGroup( Composite content )
	{
		group = new Group( content, SWT.NONE );
		group.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.SelColumnMemberValue" ) ); //$NON-NLS-1$
		group.setLayout( new GridLayout( ) );

		memberValueTable = new Table( group, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION );
		memberValueTable.setLinesVisible( true );
		memberValueTable.setHeaderVisible( true );
		memberValueTable.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		gd.horizontalSpan = 3;
		group.setLayoutData( gd );

		dynamicViewer = new TableViewer( memberValueTable );

		TableColumn column = new TableColumn( memberValueTable, SWT.LEFT );
		column.setText( columns[0] );
		column.setWidth( 15 );

		TableColumn column1 = new TableColumn( memberValueTable, SWT.LEFT );
		column1.setResizable( columns[1] != null );
		if ( columns[1] != null )
		{
			column1.setText( columns[1] );
		}
		column1.setWidth( 200 );

		TableColumn column2 = new TableColumn( memberValueTable, SWT.LEFT );
		column2.setResizable( columns[2] != null );
		if ( columns[2] != null )
		{
			column2.setText( columns[2] );
		}
		column2.setWidth( 200 );

		dynamicViewer.setColumnProperties( columns );
		editor = new ExpressionValueCellEditor( dynamicViewer.getTable( ),
				SWT.READ_ONLY );
		TextCellEditor textEditor = new TextCellEditor( dynamicViewer.getTable( ),
				SWT.READ_ONLY );
		TextCellEditor textEditor2 = new TextCellEditor( dynamicViewer.getTable( ),
				SWT.READ_ONLY );
		CellEditor[] cellEditors = new CellEditor[]{
				textEditor, textEditor2, editor
		};
		if ( designHandle != null )
		{
			editor.setExpressionProvider( new ExpressionProvider( designHandle ) );
			editor.setReportElement( (ExtendedItemHandle) designHandle );
		}

		dynamicViewer.setCellEditors( cellEditors );

		dynamicViewer.setContentProvider( contentProvider );
		dynamicViewer.setLabelProvider( labelProvider );
		dynamicViewer.setCellModifier( cellModifier );
		dynamicViewer.addSelectionChangedListener( selectionChangeListener );
	}

	private ISelectionChangedListener selectionChangeListener = new ISelectionChangedListener( ) {

		public void selectionChanged( SelectionChangedEvent event )
		{
			// TODO Auto-generated method stub
			ISelection selection = event.getSelection( );
			if ( selection instanceof StructuredSelection )
			{
				Object obj = ( (StructuredSelection) selection ).getFirstElement( );
				if ( obj != null
						&& obj instanceof MemberValueHandle
						&& editor != null )
				{
					editor.setMemberValue( (MemberValueHandle) obj );
				}
			}

		}
	};

	private String[] valueItems = new String[0];
	private static final String dummyChoice = "dummy"; //$NON-NLS-1$
	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputObj )
		{
			if ( !( inputObj instanceof List ) )
			{
				return new Object[0];
			}
			return ( (List) inputObj ).toArray( );
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( columnIndex == 0 )
			{
				if ( element == dummyChoice )
					return Messages.getString( "LevelPropertyDialog.MSG.CreateNew" );
				else
				{
					if ( element instanceof RuleHandle )
					{
						return ( (RuleHandle) element ).getDisplayExpression( );
					}
					return "";
				}
			}
			else if ( columnIndex == 1 )
			{
				return ( (MemberValueHandle) element ).getLevel( ).getName( );
			}
			else if ( columnIndex == 2 )
			{
				String value = ( (MemberValueHandle) element ).getValue( );
				return value == null ? "" : value;
			}
			return "";
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
			if ( Arrays.asList( columns ).indexOf( property ) == 2 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		public Object getValue( Object element, String property )
		{
			if ( Arrays.asList( columns ).indexOf( property ) != 2 )
			{
				return "";
			}
			String value = ( (MemberValueHandle) element ).getValue( );
			return value == null ? "" : value;
		}

		public void modify( Object element, String property, Object value )
		{
			if ( Arrays.asList( columns ).indexOf( property ) != 2 )
			{
				return;
			}
			TableItem item = (TableItem) element;
			MemberValueHandle memberValue = (MemberValueHandle) item.getData( );
			try
			{
				( (MemberValueHandle) memberValue ).setValue( (String) value );
			}
			catch ( SemanticException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}

			dynamicViewer.refresh( );
		}
	};
	private ExpressionValueCellEditor editor;

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties( )
	{
		if ( inputHandle == null )
		{
			if ( comboGroupLevel.getItemCount( ) == 0 )
			{
				comboGroupLevel.add( DEUtil.resolveNull( null ) );
			}
			comboGroupLevel.select( 0 );
			updateMemberValues( );
			updateBindings( );
		}
		else
		{
			getLevels( );

			int levelIndex = groupLevelList.indexOf( levelViewHandle );
			if ( levelIndex >= 0 )
			{
				comboGroupLevel.select( levelIndex );
				updateBindings( );
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

		private transient boolean needRefreshList = true;
		Text valueText;
		Button btnPopup;
		List valueList = new ArrayList( );

		Text getValueText( )
		{
			return valueText;
		}

		Button getPopupButton( )
		{
			return btnPopup;
		}

		public void setNeedRefresh( boolean bool )
		{
			needRefreshList = bool;
		}

		private List getSelectedValueList( )
		{
			if ( needRefreshList == false )
			{
				return valueList;
			}
			CubeHandle cube = null;
			CrosstabReportItemHandle crosstab = null;
			if ( designHandle instanceof ExtendedItemHandle )
			{
				try
				{
					Object obj = ( (ExtendedItemHandle) designHandle ).getReportItem( );
					if ( obj instanceof CrosstabReportItemHandle )
					{
						crosstab = (CrosstabReportItemHandle) obj;
					}

					crosstab = (CrosstabReportItemHandle) ( (ExtendedItemHandle) designHandle ).getReportItem( );
					cube = crosstab.getCube( );
				}
				catch ( ExtendedElementException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace( );
				}

			}
			if ( cube == null
					|| ( !( cube instanceof TabularCubeHandle ) )
					|| expression.getText( ).length( ) == 0 )
			{
				return new ArrayList( );
			}
			Iterator iter = null;

			// get cubeQueryDefn
			ICubeQueryDefinition cubeQueryDefn = null;
			DataRequestSession session = null;
			try
			{
				session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
				cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab );
				iter = session.getCubeQueryUtil( )
						.getMemberValueIterator( (TabularCubeHandle) cube,
								expression.getText( ),
								cubeQueryDefn );
			}
			catch ( Exception e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}
			valueList = new ArrayList( );
			int count = 0;
			int MAX_COUNT = CrosstabPlugin.getDefault( )
					.getPluginPreferences( )
					.getInt( CrosstabPlugin.PREFERENCE_FILTER_LIMIT );
			while ( iter != null && iter.hasNext( ) )
			{
				Object obj = iter.next( );
				if ( obj != null )
				{
					if ( valueList.indexOf( obj ) < 0 )
					{
						valueList.add( obj );
						if ( ++count >= MAX_COUNT )
						{
							break;
						}
					}


				}

			}
			// // test begin
			// for(int i =0; i < 5; i ++)
			// {
			// valueList.add( new Integer(i) );
			// }
			// // test end
			needRefreshList = false;
			return valueList;
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
			btnPopup = new Button( composite, SWT.ARROW | SWT.DOWN );
			btnPopup.addSelectionListener( new SelectionListener( ) {

				public void widgetSelected( SelectionEvent e )
				{
					refreshList( );
					Rectangle textBounds = valueText.getBounds( );
					Point pt = valueText.toDisplay( textBounds.x, textBounds.y );
					Rectangle rect = new Rectangle( pt.x,
							pt.y,
							valueText.getParent( ).getBounds( ).width,
							textBounds.height );

					PopupSelectionList popup = new PopupSelectionList( valueText.getParent( )
							.getShell( ) );
					popup.setItems( popupItems );
					String value = popup.open( rect );
					int selectionIndex = popup.getSelectionIndex( );

					if ( value != null )
					{
						String newValue = null;
						if ( value.equals( ( actions[0] ) ) )
						{

							List selectValueList = getSelectedValueList( );
							if ( selectValueList == null
									|| selectValueList.size( ) == 0 )
							{
								MessageDialog.openInformation( null,
										Messages.getString( "SelectValueDialog.selectValue" ),
										Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) );

							}
							else
							{
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

						}
						else if ( value.equals( actions[1] ) )
						{
							ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
									.getDisplay( )
									.getActiveShell( ),
									valueText.getText( ) );

							dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );

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

				public void widgetDefaultSelected( SelectionEvent e )
				{
					// TODO Auto-generated method stub

				}

			} );

		}

		private void refreshList( )
		{
			if ( refreshItems )
			{
				ArrayList finalItems = new ArrayList( 10 );
				for ( int n = 0; n < actions.length; n++ )
				{
					finalItems.add( actions[n] );
				}

				if ( currentItem != null )
				{
					// addParamterItems( finalItems );
				}
				popupItems = (String[]) finalItems.toArray( EMPTY_ARRAY );
			}
			refreshItems = false;
		}

		private class ExpressionLayout extends Layout
		{

			public void layout( Composite editor, boolean force )
			{
				Rectangle bounds = editor.getClientArea( );
				Point size = btnPopup.computeSize( SWT.DEFAULT,
						SWT.DEFAULT,
						force );
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
				Point buttonSize = btnPopup.computeSize( SWT.DEFAULT,
						SWT.DEFAULT,
						force );
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
				if(levelHandle.getCubeLevel( ) != null)
				{
					groupLevelNameList.add( levelHandle.getCubeLevel( )
							.getFullName( ) );				
				}

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
				FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
						.newFilterConditionElement( );
				filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				filter.setProperty( IFilterConditionElementModel.VALUE1_PROP,
						DEUtil.resolveNull( value1.getText( ) ) );
				filter.setProperty( IFilterConditionElementModel.VALUE2_PROP,
						DEUtil.resolveNull( value2.getText( ) ) );

				// set test expression for new map rule
				filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

				// test code -- begin --
				// MemberValueHandle parent = memberValueHandle;
				// while(true)
				// {
				// if(parent != null)
				// {
				// parent.getCubeLevelName( );
				// parent.getValue( );
				// MemberValueHandle child = getChildMemberValue(parent);
				// parent = child;
				// }else
				// {
				// break;
				// }
				//					
				// }
				// test code -- end --

				if ( referencedLevelList != null
						&& referencedLevelList.size( ) > 0 )
				{
					filter.add( FilterConditionElementHandle.MEMBER_PROP,
							memberValueHandle );
				}

				level.getModelHandle( ).add( ILevelViewConstants.FILTER_PROP,
						filter );
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

					if ( inputHandle.getMember( ) != null )
					{
						inputHandle.drop( FilterConditionElementHandle.MEMBER_PROP,
								0 );
					}

					// test code -- begin --
					// MemberValueHandle parent = memberValueHandle;
					// while(true)
					// {
					// if(parent != null)
					// {
					// parent.getCubeLevelName( );
					// parent.getValue( );
					// MemberValueHandle child = getChildMemberValue(parent);
					// parent = child;
					// }else
					// {
					// break;
					// }
					//						
					// }
					// test code -- end --

					if ( referencedLevelList != null
							&& referencedLevelList.size( ) > 0 )
					{
						inputHandle.add( FilterConditionElementHandle.MEMBER_PROP,
								memberValueHandle );
					}

				}
				else
				{
					FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
							.newFilterConditionElement( );
					filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
							DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
					filter.setProperty( IFilterConditionElementModel.VALUE1_PROP,
							DEUtil.resolveNull( value1.getText( ) ) );
					filter.setProperty( IFilterConditionElementModel.VALUE2_PROP,
							DEUtil.resolveNull( value2.getText( ) ) );
					filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

					// test code -- begin --
					// MemberValueHandle parent = memberValueHandle;
					// while(true)
					// {
					// if(parent != null)
					// {
					// parent.getCubeLevelName( );
					// parent.getValue( );
					// MemberValueHandle child = getChildMemberValue(parent);
					// parent = child;
					// }else
					// {
					// break;
					// }
					//						
					// }
					// test code -- end --

					if ( referencedLevelList != null
							&& referencedLevelList.size( ) > 0 )
					{
						filter.add( FilterConditionElementHandle.MEMBER_PROP,
								memberValueHandle );
					}

					levelViewHandle.getModelHandle( )
							.drop( ILevelViewConstants.FILTER_PROP, inputHandle );
					level.getModelHandle( )
							.add( ILevelViewConstants.FILTER_PROP, filter );

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

	private List getReferableBindings( LevelViewHandle level )
	{
		List retList = new ArrayList();
		
		if(level.getCubeLevel( ) == null)
		{
			return retList;
		}
		
		// get targetLevel
		DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle( level.getCubeLevel( ) );
		String targetLevel = ExpressionUtil.createJSDimensionExpression( dimensionHandle.getName( ),
				level.getCubeLevel( ).getName( ) );

		// get cubeQueryDefn
		ICubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery( level.getCrosstab( ) );
			retList = session.getCubeQueryUtil( )
					.getReferableBindings( targetLevel, cubeQueryDefn, false );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		return retList;
	}

	private void updateBindings( )
	{
		String exprText = expression.getText( );

		LevelViewHandle level = null;
		if ( comboGroupLevel.getSelectionIndex( ) != -1
				&& groupLevelList != null
				&& groupLevelList.size( ) > 0 )
		{
			level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
		}

		if ( level == null )
		{
			expression.setItems( new String[]{
				DEUtil.resolveNull( null )
			} );
			return;
		}

		expression.removeAll( );
		List bindingList = getReferableBindings( level );
		for ( int i = 0; i < bindingList.size( ); i++ )
		{
			try
			{
				expression.add( ( (IBinding) ( bindingList.get( i ) ) ).getBindingName( ) );
			}
			catch ( DataException e )
			{
				e.printStackTrace( );
			}
		}

		if ( expression.getItemCount( ) == 0 )
		{
			expression.add( DEUtil.resolveNull( null ) );
		}
		expression.setText( exprText );
	}

	private MemberValueHandle getChildMemberValue( MemberValueHandle memberValue )
	{
		if ( memberValue.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) != 1
				|| memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
						0 ) == null )
		{
			return null;
		}
		return (MemberValueHandle) memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
				0 );
	}

	private void dropChildMemberValue( MemberValueHandle memberValue )
	{
		MemberValueHandle child = getChildMemberValue( memberValue );
		if ( child == null )
			return;
		try
		{
			memberValue.drop( IMemberValueModel.MEMBER_VALUES_PROP, 0 );
		}
		catch ( SemanticException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}

	private MemberValueHandle updateMemberValuesFromLevelList(
			List referenceLevels, MemberValueHandle memberValue )
	{
		int count = referenceLevels.size( );
		MemberValueHandle lastMemberValue = memberValue;

		int hasCount = 0;
		while ( true )
		{
			hasCount++;
			LevelHandle tempLevel = getLevelHandle( (ILevelDefinition) referenceLevels.get( hasCount - 1 ) );
			if ( lastMemberValue.getLevel( ) != tempLevel )
			{
				try
				{
					lastMemberValue.setLevel( tempLevel );
					dropChildMemberValue( lastMemberValue );
				}
				catch ( SemanticException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace( );
				}
				break;
			}

			if ( getChildMemberValue( lastMemberValue ) == null )
			{
				break;
			}
			if ( hasCount >= count )
			{
				dropChildMemberValue( lastMemberValue );
				break;
			}
			lastMemberValue = getChildMemberValue( lastMemberValue );

		}

		for ( int i = hasCount; i < count; i++ )
		{
			MemberValueHandle newValue = DesignElementFactory.getInstance( )
					.newMemberValue( );
			LevelHandle tempLevel = getLevelHandle( (ILevelDefinition) referenceLevels.get( i ) );
			try
			{
				newValue.setLevel( tempLevel );
				lastMemberValue.add( IMemberValueModel.MEMBER_VALUES_PROP,
						newValue );
			}
			catch ( SemanticException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}

			lastMemberValue = newValue;
		}

		return memberValue;
	}

	private LevelHandle getLevelHandle( ILevelDefinition levelDef )
	{
		LevelHandle levelHandle = null;
		String levelName = levelDef.getName( );
		String dimensionName = levelDef.getHierarchy( )
				.getDimension( )
				.getName( );
		ExtendedItemHandle extHandle = (ExtendedItemHandle) designHandle;
		CrosstabReportItemHandle crosstab = null;
		try
		{
			crosstab = (CrosstabReportItemHandle) extHandle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		DimensionViewHandle dimension = crosstab.getDimension( dimensionName );
		// LevelViewHandle level = getLevel(dimension, levelName );
		LevelViewHandle level = dimension.findLevel( levelName );
		levelHandle = level.getCubeLevel( );
		return levelHandle;
	}

	private void updateMemberValues( )
	{
		if ( comboGroupLevel.getSelectionIndex( ) < 0
				|| expression.getText( ).length( ) == 0 )
		{
			memberValueTable.setEnabled( false );
			return;
		}
		LevelViewHandle level = null;
		if ( comboGroupLevel.getSelectionIndex( ) != -1
				&& groupLevelList != null
				&& groupLevelList.size( ) > 0 )
		{
			level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
		}
		if ( level == null )
		{
			memberValueTable.setEnabled( false );
			return;
		}
		
		// fix bug 191080 to update Member value Label.
		if(level.getAxisType( ) == ICrosstabConstants.COLUMN_AXIS_TYPE)
		{
			group.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.SelColumnMemberValue" ) ); //$NON-NLS-1$
		}else
		{
			group.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.SelRowMemberValue" ) );
		}
		
		referencedLevelList = getReferencedLevels( level, expression.getText( ) );
		if ( referencedLevelList == null || referencedLevelList.size( ) == 0 )
		{
			memberValueTable.setEnabled( false );
			return;
		}

		editor.setReferencedLevelList( referencedLevelList );

		// for ( int i = 0; i < levelList.size( ); i++ )
		// {
		// LevelDefiniton levelDefn = (LevelDefiniton)levelList.get( i );
		// String name = levelDefn.getName( );
		// }

		memberValueTable.setEnabled( true );
		memberValueHandle = null;
		if ( level == levelViewHandle )
		{
			memberValueHandle = inputHandle.getMember( );
		}

		if ( memberValueHandle == null )
		{
			memberValueHandle = DesignElementFactory.getInstance( )
					.newMemberValue( );
		}
		memberValueHandle = updateMemberValuesFromLevelList( referencedLevelList,
				memberValueHandle );
		List memList = getMemberValueList( memberValueHandle );
		dynamicViewer.setInput( memList );
	}

	
	private List getReferencedLevels( LevelViewHandle level, String bindingExpr )
	{
		List retList = new ArrayList();;

		if(level.getCubeLevel( ) == null)
		{
			return retList;
		}
		// get targetLevel
		DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle( level.getCubeLevel( ) );
		String targetLevel = ExpressionUtil.createJSDimensionExpression( dimensionHandle.getName( ),
				level.getCubeLevel( ).getName( ) );

		// get cubeQueryDefn
		ICubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery( level.getCrosstab( ) );
			retList = session.getCubeQueryUtil( )
					.getReferencedLevels( targetLevel,
							bindingExpr,
							cubeQueryDefn );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		return retList;
	}

	private List getMemberValueList( MemberValueHandle parent )
	{
		List list = new ArrayList( );
		if ( parent == null )
		{
			return list;
		}

		MemberValueHandle memberValue = parent;

		while ( true )
		{
			list.add( memberValue );
			if ( memberValue.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) != 1
					|| memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
							0 ) == null )
			{
				break;
			}
			memberValue = (MemberValueHandle) memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
					0 );
		}
		return list;
	}

	private String getResultBindingName( String name )
	{
		String[] items = expression.getItems( );
		if ( items.length == 0 )
		{
			return null;
		}
		for ( int i = 0; i < items.length; i++ )
		{
			String dataExpr = ExpressionUtil.createJSDataExpression( items[i] );
			if ( dataExpr.equals( name ) || items[i].equals( name ) )
			{
				return items[i];
			}
		}
		return null;
	}

}
