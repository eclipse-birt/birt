/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.widget.ExpressionCellEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class LevelPropertyDialog extends BaseDialog
{

	private Label infoLabel;
	final private static IChoice[] intervalChoicesAll = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_INTERVAL )
			.getChoices( );

	private static IChoice[] intervalChoices = intervalChoicesAll;
	private Text intervalRange;
	private Combo intervalType;
	private Button intervalBaseButton;
	private Text intervalBaseText;
	private Composite dynamicArea;
	private DataSetHandle dataset;

	public LevelPropertyDialog( )
	{
		super( Messages.getString( "LevelPropertyDialog.Title" ) );
	}

	protected Control createDialogArea( Composite parent )
	{
		createTitleArea( parent );

		Composite contents = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.verticalSpacing = 10;
		layout.marginWidth = 20;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = convertWidthInCharsToPixels( 80 );
		data.heightHint = 350;
		contents.setLayoutData( data );

		createInfoArea( contents );
		createChoiceArea( contents );

		dynamicArea = createDynamicArea( contents );
		staticArea = createStaticArea( contents );

		WidgetUtil.createGridPlaceholder( contents, 1, true );

		initLevelDialog( );

		parent.layout( );

		return contents;
	}

	private void initLevelDialog( )
	{
		if ( input != null )
		{
			infoLabel.setText( infoLabel.getText( ) + " " + input.getFullName( ) );
			expressionEditor.setExpressionProvider( new CubeExpressionProvider( input ) );
		}

		Iterator valuesIter = input.staticValuesIterator( );
		if ( ( valuesIter == null || !valuesIter.hasNext( ) )
				|| dynamicButton.getSelection( ) )
		{
			try
			{
				input.setLevelType( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC );
				refreshDynamicViewer( );
				// while ( attrIter.hasNext( ) )
				// {
				// dynamicAttributes.add( ( (LevelAttributeHandle)
				// attrIter.next( ) ).getName( ) );
				// }
				dataset = ( (TabularHierarchyHandle) input.getContainer( ) ).getDataSet( );
				if ( dataset != null )
					attributeItems = OlapUtil.getDataFieldNames( dataset );
				resetEditorItems( );

				dynamicButton.setSelection( true );
				if ( input.getName( ) != null )
					nameText.setText( input.getName( ) );
				PropertyHandle property = input.getPropertyHandle( GroupElement.INTERVAL_RANGE_PROP );
				String range = property == null ? null
						: property.getStringValue( );
				intervalRange.setText( range == null ? "" : range ); //$NON-NLS-1$
				int width = intervalRange.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
				( (GridData) intervalRange.getLayoutData( ) ).widthHint = width < 60 ? 60
						: width;
				int index = getIntervalTypeIndex( input.getInterval( ) );
				if ( index != -1 )
					intervalType.select( index );
				else
					intervalType.select( 0 );
				if ( index == 0 )
				{
					intervalRange.setEnabled( false );
					intervalBaseButton.setEnabled( false );
					intervalBaseText.setEnabled( false );
				}
				else
				{
					intervalRange.setEnabled( true );
					intervalBaseButton.setEnabled( true );
					intervalBaseButton.setSelection( input.getIntervalBase( ) != null );
					intervalBaseText.setEnabled( intervalBaseButton.getSelection( ) );
					if ( input.getIntervalBase( ) != null )
					{
						intervalBaseText.setText( input.getIntervalBase( ) );
					}
				}

				updateButtonStatus( dynamicButton );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}
		else
		{
			// while ( valuesIter.hasNext( ) )
			// {
			// RuleHandle value = ( (RuleHandle) valuesIter.next( ) );
			// staticAttributes.put( value.getDisplayExpression( ),
			// value.getRuleExpression( ) );
			// }
			refreshStaticViewer( );
			// dynamicViewer.setInput( dynamicAttributes );
			staticButton.setSelection( true );
			updateButtonStatus( staticButton );
		}
	}

	private void refreshDynamicViewer( )
	{
		Iterator attrIter = input.attributesIterator( );
		List attrList = new LinkedList( );
		while ( attrIter.hasNext( ) )
		{
			attrList.add( attrIter.next( ) );
		}
		dynamicViewer.setInput( attrList );
	}

	private void refreshStaticViewer( )
	{
		Iterator valuesIter = input.staticValuesIterator( );
		List valuesList = new LinkedList( );
		while ( valuesIter.hasNext( ) )
		{
			valuesList.add( valuesIter.next( ) );
		}
		staticViewer.setInput( valuesList );
	}

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

	protected void okPressed( )
	{
		if ( dynamicButton.getSelection( ) )
		{
			try
			{
				input.setLevelType( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC );
				if ( nameText.getText( ) != null
						&& !nameText.getText( ).trim( ).equals( "" ) )
				{
					input.setName( nameText.getText( ) );
				}
				int index = intervalType.getSelectionIndex( );
				input.setInterval( intervalChoices[index].getName( ) );
				if ( index != 0 )
				{
					input.setIntervalRange( intervalRange.getText( ) );
				}
				else
				{
					input.setProperty( GroupHandle.INTERVAL_RANGE_PROP, null );
				}
				if ( intervalBaseText.getEnabled( ) )
				{
					input.setIntervalBase( UIUtil.convertToModelString( intervalBaseText.getText( ),
							false ) );
				}
				else
				{
					input.setIntervalBase( null );
				}
				input.getPropertyHandle( ILevelModel.STATIC_VALUES_PROP )
						.clearValue( );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}

		}
		else if ( staticButton.getSelection( ) )
		{
			try
			{
				input.setLevelType( DesignChoiceConstants.LEVEL_TYPE_MIRRORED );
				input.setInterval( null );
				input.setIntervalRange( null );
				input.setProperty( GroupHandle.INTERVAL_RANGE_PROP, null );
				input.setIntervalBase( UIUtil.convertToModelString( intervalBaseText.getText( ),
						false ) );
				input.setIntervalBase( null );
				input.getPropertyHandle( ILevelModel.ATTRIBUTES_PROP )
						.clearValue( );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}

		}
		super.okPressed( );
	}
	private static final String dummyChoice = "dummy"; //$NON-NLS-1$

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object input )
		{
			if ( input instanceof List )
			{
				List list = (List) input;
				if ( !list.contains( dummyChoice ) )
					list.add( dummyChoice );
				// Object[] items = new Object[list.size( )+1];
				// list.toArray( items );
				// String[] elements = new String[items.length + 1];
				// System.arraycopy( items, 0, elements, 0, items.length );
				// Object[] elements = new Object[list.size( ) + 1];
				// elements[elements.length - 1] = dummyChoice;
				return list.toArray( );
			}
			// else if ( input instanceof Map )
			// {
			// Map map = (Map) input;
			// String[] items = new String[map.size( )];
			// map.keySet( ).toArray( items );
			// String[] elements = new String[items.length + 1];
			// System.arraycopy( items, 0, elements, 0, items.length );
			// elements[elements.length - 1] = dummyChoice;
			// return elements;
			// }
			return new Object[0];
		}
	};

	private ITableLabelProvider staticLabelProvider = new ITableLabelProvider( ) {

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
			else
			{
				if ( element == dummyChoice )
					return "";
				else
				{
					if ( element instanceof RuleHandle )
					{
						return ( (RuleHandle) element ).getRuleExpression( );
					}
					return "";
				}
			}
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

	private ITableLabelProvider dynamicLabelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( columnIndex == 1 )
			{
				if ( element == dummyChoice )
					return Messages.getString( "LevelPropertyDialog.MSG.CreateNew" );
				else
				{
					if ( element instanceof LevelAttributeHandle )
					{
						return ( (LevelAttributeHandle) element ).getName( );
					}
				}
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

	private ICellModifier dynamicCellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			return true;
		}

		public Object getValue( Object element, String property )
		{
			if ( element instanceof LevelAttributeHandle )
			{
				LevelAttributeHandle handle = (LevelAttributeHandle) element;
				resetEditorItems( handle.getName( ) );
				for ( int i = 0; i < editor.getItems( ).length; i++ )
					if ( handle.getName( ).equals( editor.getItems( )[i] ) )
						return new Integer( i );
			}
			if ( element instanceof String )
			{
				resetEditorItems( );
			}
			return new Integer( -1 );
		}

		public void modify( Object element, String property, Object value )
		{
			if ( element instanceof Item )
				element = ( (Item) element ).getData( );

			if ( ( (Integer) value ).intValue( ) > -1
					&& ( (Integer) value ).intValue( ) < editor.getItems( ).length )
			{
				if ( element instanceof LevelAttributeHandle )
				{
					LevelAttributeHandle handle = (LevelAttributeHandle) element;
					try
					{
						handle.setName( editor.getItems( )[( (Integer) value ).intValue( )] );
						if ( dataset != null )
						{
							ResultSetColumnHandle dataField = OlapUtil.getDataField( dataset,
									handle.getName( ) );
							handle.setDataType( dataField.getDataType( ) );
						}
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
				else
				{
					LevelAttribute attribute = StructureFactory.createLevelAttribute( );
					attribute.setName( editor.getItems( )[( (Integer) value ).intValue( )] );
					if ( dataset != null )
					{
						ResultSetColumnHandle dataField = OlapUtil.getDataField( dataset,
								attribute.getName( ) );
						attribute.setDataType( dataField.getDataType( ) );
					}
					try
					{
						input.getPropertyHandle( ILevelModel.ATTRIBUTES_PROP )
								.addItem( attribute );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
				refreshDynamicViewer( );
			}
		}
	};

	private ICellModifier staticCellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			if ( property.equals( Prop_Name ) )
			{
				return true;
			}
			else
			{
				if ( element != dummyChoice )
					return true;
				else
					return false;
			}
		}

		public Object getValue( Object element, String property )
		{
			if ( property.equals( Prop_Name ) )
			{
				if ( element != dummyChoice )
				{
					if ( element instanceof RuleHandle )
						return ( (RuleHandle) element ).getDisplayExpression( );
				}
				return "";
			}
			else
			{
				if ( element != dummyChoice )
				{
					if ( element instanceof RuleHandle )
					{
						String result = ( (RuleHandle) element ).getRuleExpression( );
						return result == null ? "" : result;
					}
				}
				return "";
			}
		}

		public void modify( Object element, String property, Object value )
		{
			if ( element instanceof Item )
			{
				element = ( (Item) element ).getData( );
			}
			if ( property.equals( Prop_Name )
					&& !( value.toString( ).trim( ).equals( "" ) || value.equals( dummyChoice ) ) )
			{
				if ( element instanceof RuleHandle )
				{
					( (RuleHandle) element ).setDisplayExpression( value.toString( ) );
				}
				else
				{
					Rule rule = StructureFactory.createRule( );
					rule.setProperty( Rule.DISPLAY_EXPRE_MEMBER,
							value.toString( ) );
					rule.setProperty( Rule.RULE_EXPRE_MEMBER, "" );
					try
					{
						input.getPropertyHandle( ILevelModel.STATIC_VALUES_PROP )
								.addItem( rule );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
				refreshStaticViewer( );
			}
			else if ( property.equals( prop_Expression ) )
			{
				if ( element != dummyChoice
						&& !( value.toString( ).trim( ).equals( "" ) ) )
				{
					if ( element instanceof RuleHandle )
					{
						( (RuleHandle) element ).setRuleExpression( value.toString( ) );
					}
					refreshStaticViewer( );
				}
			}

		}
	};

	private int dynamicSelectIndex;

	protected Composite createDynamicArea( Composite parent )
	{
		Composite contents = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		contents.setLayoutData( data );

		Group groupGroup = new Group( contents, SWT.NONE );
		layout = new GridLayout( );
		layout.numColumns = 2;
		groupGroup.setLayout( layout );

		groupGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Label nameLabel = new Label( groupGroup, SWT.NONE );
		nameLabel.setText( Messages.getString( "LevelPropertyDialog.Label.Name" ) );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		nameLabel.setLayoutData( data );

		nameText = new Text( groupGroup, SWT.BORDER );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		nameText.setLayoutData( data );

		// Creates intervalRange labels
		new Label( groupGroup, SWT.NONE ).setText( Messages.getString( "LevelPropertyDialog.Label.Interval" ) );
		new Label( groupGroup, SWT.NONE ).setText( Messages.getString( "LevelPropertyDialog.Label.Range" ) );

		intervalType = new Combo( groupGroup, SWT.READ_ONLY | SWT.DROP_DOWN );
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
				intervalBaseButton.setEnabled( intervalType.getSelectionIndex( ) != 0 );
				intervalBaseText.setEnabled( intervalBaseButton.getEnabled( )
						&& intervalBaseButton.getSelection( ) );
			}
		} );

		// Creates intervalRange range chooser

		intervalRange = new Text( groupGroup, SWT.SINGLE | SWT.BORDER );
		intervalRange.setLayoutData( new GridData( ) );
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

		intervalBaseButton = new Button( groupGroup, SWT.CHECK );
		intervalBaseButton.setText( Messages.getString( "LevelPropertyDialog.Button.IntervalBase" ) ); //$NON-NLS-1$
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		intervalBaseButton.setLayoutData( gd );
		intervalBaseButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				intervalBaseText.setEnabled( intervalBaseButton.getSelection( ) );
			}
		} );

		intervalBaseText = new Text( groupGroup, SWT.SINGLE | SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		intervalBaseText.setLayoutData( gd );

		dynamicTable = new Table( contents, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER );
		gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 100;
		dynamicTable.setLayoutData( gd );
		dynamicTable.setLinesVisible( true );
		dynamicTable.setHeaderVisible( true );

		dynamicTable.addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( e.keyCode == SWT.DEL )
				{
					int itemCount = dynamicTable.getItemCount( );
					if ( dynamicSelectIndex == itemCount )
					{
						return;
					}
					if ( dynamicSelectIndex == itemCount - 1 )
					{
						dynamicSelectIndex--;
					}
					try
					{
						handleDynamicDelEvent( );
					}
					catch ( Exception e1 )
					{
						WidgetUtil.processError( getShell( ), e1 );
					}
					refreshDynamicViewer( );
				}
			}
		} );

		dynamicViewer = new TableViewer( dynamicTable );
		String[] columns = new String[]{
				" ", Messages.getString( "LevelPropertyDialog.Label.Attribute" )
		};

		TableColumn column = new TableColumn( dynamicTable, SWT.LEFT );
		column.setText( columns[0] );
		column.setWidth( 15 );

		TableColumn column1 = new TableColumn( dynamicTable, SWT.LEFT );
		column1.setResizable( columns[1] != null );
		if ( columns[1] != null )
		{
			column1.setText( columns[1] );
		}
		column1.setWidth( 200 );

		dynamicViewer.setColumnProperties( columns );
		editor = new ComboBoxCellEditor( dynamicViewer.getTable( ),
				attributeItems,
				SWT.READ_ONLY );
		CellEditor[] cellEditors = new CellEditor[]{
				null, editor
		};
		dynamicViewer.setCellEditors( cellEditors );

		dynamicViewer.setContentProvider( contentProvider );
		dynamicViewer.setLabelProvider( dynamicLabelProvider );
		dynamicViewer.setCellModifier( dynamicCellModifier );

		return contents;
	}

	String[] attributeItems = new String[0];

	private void resetEditorItems( )
	{
		resetEditorItems( null );
	}

	private void resetEditorItems( String name )
	{
		List list = new ArrayList( );
		list.addAll( Arrays.asList( attributeItems ) );

		Iterator attrIter = input.attributesIterator( );
		while ( attrIter.hasNext( ) )
		{
			LevelAttributeHandle handle = (LevelAttributeHandle) attrIter.next( );
			list.remove( handle.getName( ) );
		}

		list.remove( input.getColumnName( ) );
		if ( name != null && !list.contains( name ) )
		{
			list.add( 0, name );
		}
		String[] temps = new String[list.size( )];
		list.toArray( temps );
		editor.setItems( temps );
	}

	protected void handleDynamicDelEvent( )
	{
		if ( dynamicViewer.getSelection( ) != null
				&& dynamicViewer.getSelection( ) instanceof StructuredSelection )
		{
			Object element = ( (StructuredSelection) dynamicViewer.getSelection( ) ).getFirstElement( );
			if ( element instanceof LevelAttributeHandle )
			{
				try
				{
					( (LevelAttributeHandle) element ).drop( );
				}
				catch ( PropertyValueException e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}

	}

	protected void handleStaticDelEvent( )
	{
		if ( staticViewer.getSelection( ) != null
				&& staticViewer.getSelection( ) instanceof StructuredSelection )
		{
			Object element = ( (StructuredSelection) staticViewer.getSelection( ) ).getFirstElement( );
			if ( element instanceof RuleHandle )
			{
				try
				{
					( (RuleHandle) element ).drop( );
				}
				catch ( PropertyValueException e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}

	}

	private Composite createInfoArea( Composite parent )
	{
		Composite contents = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		contents.setLayoutData( data );

		infoLabel = new Label( contents, SWT.NONE );
		infoLabel.setText( Messages.getString( "LevelPropertyDialog.Label.Info" ) );
		return contents;

	}

	private void createChoiceArea( Composite parent )
	{
		Composite contents = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		contents.setLayoutData( data );
		dynamicButton = new Button( contents, SWT.RADIO );
		dynamicButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				try
				{
					input.setLevelType( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC );
				}
				catch ( SemanticException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				initLevelDialog( );
				refreshDynamicViewer( );
				updateButtonStatus( dynamicButton );
			}

		} );
		staticButton = new Button( contents, SWT.RADIO );
		staticButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				try
				{
					input.setLevelType( DesignChoiceConstants.LEVEL_TYPE_MIRRORED );
				}
				catch ( SemanticException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				initLevelDialog( );
				refreshStaticViewer( );
				updateButtonStatus( staticButton );
			}

		} );
		dynamicButton.setText( Messages.getString( "LevelPropertyDialog.Button.Dynamic" ) );
		staticButton.setText( Messages.getString( "LevelPropertyDialog.Button.Static" ) );
	}

	protected void updateButtonStatus( Button button )
	{
		if ( button == dynamicButton )
		{
			staticButton.setSelection( false );
			dynamicButton.setSelection( true );
			setExcludeGridData( staticArea, true );
			setExcludeGridData( dynamicArea, false );
		}
		else if ( button == staticButton )
		{
			dynamicButton.setSelection( false );;
			staticButton.setSelection( true );
			setExcludeGridData( dynamicArea, true );
			setExcludeGridData( staticArea, false );
		}
		this.getShell( ).layout( );
	}

	private Composite createTitleArea( Composite parent )
	{
		Composite contents = new Composite( parent, SWT.NONE );
		contents.setLayout( new GridLayout( ) );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		contents.setLayoutData( data );

		int heightMargins = 3;
		int widthMargins = 8;
		final Composite titleArea = new Composite( contents, SWT.NONE );
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
		label.setText( Messages.getString( "LevelPropertyDialog.Label.Property" ) ); //$NON-NLS-1$
		UIUtil.bindHelp( parent, IHelpContextIds.PREFIX + "DimensionLevel_ID" );
		return titleArea;

	}

	private TabularLevelHandle input;
	private TableViewer dynamicViewer;
	private Text nameText;

	private ComboBoxCellEditor editor;
	private TableViewer staticViewer;
	private Composite staticArea;
	private Button dynamicButton;
	private Button staticButton;
	protected int staticSelectIndex;
	private static final String Prop_Name = "Name";
	private static final String prop_Expression = "Expression";
	private Table dynamicTable;
	private ExpressionCellEditor expressionEditor;

	protected Composite createStaticArea( Composite parent )
	{
		Group contents = new Group( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		contents.setLayoutData( data );

		final Table staticTable = new Table( contents, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		staticTable.setLayoutData( gd );
		staticTable.setLinesVisible( true );
		staticTable.setHeaderVisible( true );

		staticTable.addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( e.keyCode == SWT.DEL )
				{
					int itemCount = staticTable.getItemCount( );
					if ( staticSelectIndex == itemCount )
					{
						return;
					}
					if ( staticSelectIndex == itemCount - 1 )
					{
						staticSelectIndex--;
					}
					try
					{
						handleStaticDelEvent( );
					}
					catch ( Exception e1 )
					{
						WidgetUtil.processError( getShell( ), e1 );
					}
					refreshStaticViewer( );
				}
			}
		} );

		staticViewer = new TableViewer( staticTable );
		String[] columns = new String[]{
				Messages.getString( "LevelPropertyDialog.Label.Name" ),
				Messages.getString( "LevelPropertyDialog.Label.Expression" )
		};

		int[] widths = new int[]{
				150, 150
		};

		for ( int i = 0; i < columns.length; i++ )
		{
			TableColumn column = new TableColumn( staticTable, SWT.LEFT );
			column.setResizable( columns[i] != null );
			if ( columns[i] != null )
			{
				column.setText( columns[i] );
			}
			column.setWidth( widths[i] );
		}

		staticViewer.setColumnProperties( columns );

		expressionEditor = new ExpressionCellEditor( staticTable );
		CellEditor[] cellEditors = new CellEditor[]{
				new TextCellEditor( staticTable ), expressionEditor

		};
		staticViewer.setCellEditors( cellEditors );

		staticViewer.setContentProvider( contentProvider );
		staticViewer.setLabelProvider( staticLabelProvider );
		staticViewer.setCellModifier( staticCellModifier );

		return contents;

	}

	public void setInput( TabularLevelHandle level )
	{
		this.input = level;
	}

	public static void setExcludeGridData( Control control, boolean exclude )
	{
		Object obj = control.getLayoutData( );
		if ( obj == null )
			control.setLayoutData( new GridData( ) );
		else if ( !( obj instanceof GridData ) )
			return;
		GridData data = (GridData) control.getLayoutData( );
		if ( exclude )
		{
			data.heightHint = 0;
		}
		else
		{
			data.heightHint = -1;
		}
		control.setLayoutData( data );
		control.getParent( ).layout( );
		control.setVisible( !exclude );
	}
}
