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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.aggregation.AggregationUtil;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IIndexInfo;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.IContextExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;

import com.ibm.icu.text.Collator;

/**
 * Deals with tree part of expression builder. Adds some mouse and DND support
 * to tree and corresponding source viewer.
 */
public class ExpressionTreeSupport implements ISelectionChangedListener
{

	// Tree item icon images
	private static final Image IMAGE_FOLDER = ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJ_FOLDER );

	private static final Image IMAGE_OPERATOR = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_OPERATOR );

	private static final Image IMAGE_COLUMN = getIconImage( IReportGraphicConstants.ICON_DATA_COLUMN );

	private static final Image IMAGE_GOLBAL = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_GLOBAL );

	private static final Image IMAGE_METHOD = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_METHOD );

	private static final Image IMAGE_STATIC_METHOD = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD );

	private static final Image IMAGE_CONSTRUCTOR = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_CONSTRUCTOP );

	private static final Image IMAGE_MEMBER = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_MEMBER );

	private static final Image IMAGE_STATIC_MEMBER = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER );

	/** Arithmetic operators and their descriptions */
	private static final String[][] OPERATORS_ASSIGNMENT = new String[][]{
			{
					"=", Messages.getString( "ExpressionProvider.Operator.Assign" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"+=", Messages.getString( "ExpressionProvider.Operator.AddTo" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"-=", Messages.getString( "ExpressionProvider.Operator.SubFrom" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"*=", Messages.getString( "ExpressionProvider.Operator.MultTo" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"/=", Messages.getString( "ExpressionProvider.Operator.DividingFrom" ) //$NON-NLS-1$ //$NON-NLS-2$
			}
	};

	/** Comparison operators and their descriptions */
	private static final String[][] OPERATORS_COMPARISON = new String[][]{
			{
					"==", Messages.getString( "ExpressionProvider.Operator.Equals" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"<", Messages.getString( "ExpressionProvider.Operator.Less" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"<=",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.LessEqual" ) //$NON-NLS-1$ 
			},
			{
					"!=",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.NotEqual" ) //$NON-NLS-1$ 
			},
			{
					">", Messages.getString( "ExpressionProvider.Operator.Greater" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					">=",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.GreaterEquals" ) //$NON-NLS-1$
			}
	};

	/** Computational operators and their descriptions */
	private static final String[][] OPERATORS_COMPUTATIONAL = new String[][]{

			{
					"+", Messages.getString( "ExpressionProvider.Operator.Add" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"-", Messages.getString( "ExpressionProvider.Operator.Sub" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"*", Messages.getString( "ExpressionProvider.Operator.Mult" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"/",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.Divides" ) //$NON-NLS-1$ 
			},
			{
					"++X ",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.Inc" ) //$NON-NLS-1$ 
			},
			{
					"X++ ", Messages.getString( "ExpressionProvider.Operator.ReturnInc" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"--X ", Messages.getString( "ExpressionProvider.Operator.Dec" ) //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"X-- ", Messages.getString( "ExpressionProvider.Operator.ReturnDec" ) //$NON-NLS-1$ //$NON-NLS-2$
			}
	};

	/** Logical operators and their descriptions */
	private static final String[][] OPERATORS_LOGICAL = new String[][]{
			{
					"&&",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.And" ) //$NON-NLS-1$ 
			},
			{
					"||",//$NON-NLS-1$
					Messages.getString( "ExpressionProvider.Operator.Or" ) //$NON-NLS-1$ 
			}
	};

	private static final String TREE_ITEM_CONTEXT = Messages.getString( "ExpressionProvider.Category.Context" ); //$NON-NLS-1$

	private static final String TREE_ITEM_OPERATORS = Messages.getString( "ExpressionProvider.Category.Operators" ); //$NON-NLS-1$

	private static final String TREE_ITEM_BIRT_OBJECTS = Messages.getString( "ExpressionProvider.Category.BirtObjects" ); //$NON-NLS-1$ 

	private static final String TREE_ITEM_DATASETS = Messages.getString( "ExpressionProvider.Category.DataSets" ); //$NON-NLS-1$

	private static final String TREE_ITEM_PARAMETERS = Messages.getString( "ExpressionProvider.Category.Parameters" ); //$NON-NLS-1$

	private static final String TREE_ITEM_NATIVE_OBJECTS = Messages.getString( "ExpressionProvider.Category.NativeObjects" ); //$NON-NLS-1$

	private static final String TREE_ITEM_LOGICAL = Messages.getString( "ExpressionProvider.Operators.Logical" ); //$NON-NLS-1$

	private static final String TREE_ITEM_COMPUTATIONAL = Messages.getString( "ExpressionProvider.Operators.Computational" ); //$NON-NLS-1$

	private static final String TREE_ITEM_COMPARISON = Messages.getString( "ExpressionProvider.Operators.Comparison" ); //$NON-NLS-1$

	private static final String TREE_ITEM_ASSIGNMENT = Messages.getString( "ExpressionProvider.Operators.Assignment" ); //$NON-NLS-1$	

	/** Tool tip key of tree item data */
	protected static final String ITEM_DATA_KEY_TOOLTIP = "TOOL_TIP"; //$NON-NLS-1$
	/**
	 * Text key of tree item data, this data is the text string to be inserted
	 * into the text area
	 */
	protected static final String ITEM_DATA_KEY_TEXT = "TEXT"; //$NON-NLS-1$
	protected static final String ITEM_DATA_KEY_ENABLED = "ENABLED"; //$NON-NLS-1$

	private static final String OBJECTS_TYPE_NATIVE = "native";//$NON-NLS-1$
	private static final String OBJECTS_TYPE_BIRT = "birt";//$NON-NLS-1$

	/**
	 * TODO use model constant ?
	 */
	private static final String CLIENT_CONTEXT = "client"; //$NON-NLS-1$

	/**
	 * public tree name constants used to be identified when a filter is added.
	 */
	public static final String TREE_NAME_OPERATORS = "Operators"; //$NON-NLS-1$
	public static final String TREE_NAME_NATIVE_OBJECTS = "Native Objects"; //$NON-NLS-1$
	public static final String TREE_NAME_BIRT_OBJECTS = "Birt Objects"; //$NON-NLS-1$
	public static final String TREE_NAME_DATASETS = "DataSets"; //$NON-NLS-1$
	public static final String TREE_NAME_PARAMETERS = "Parameters"; //$NON-NLS-1$
	public static final String TREE_NAME_CONTEXT = "Context"; //$NON-NLS-1$

	private SourceViewer expressionViewer;
	private Tree tree;
	private DropTarget dropTarget;
	private DropTargetAdapter dropTargetAdapter;

	private Object currentEditObject;
	private String currentMethodName, currentContextName;
	private TreeItem contextItem, dataSetsItem, parametersItem,
			nativeObejctsItem, birtObjectsItem;
	private List<TreeItem> dynamicItems;

	/**
	 * Creates all expression trees in default order
	 * 
	 * @param dataSetList
	 *            list for DataSet tree
	 */
	public void createDefaultExpressionTree( List dataSetList )
	{
		createFilteredExpressionTree( dataSetList, null );
	}

	/**
	 * Creates selected expression trees with given filter list.
	 * 
	 * @param dataSetList
	 *            list for DataSet tree
	 * @param filterList
	 *            list of filters
	 */
	public void createFilteredExpressionTree( List dataSetList, List filterList )
	{
		// if ( filter( TREE_NAME_DATASETS, filterList ) )
		// {
		// createDataSetsTree( dataSetList );
		// }
		if ( filter( TREE_NAME_CONTEXT, filterList ) )
		{
			createContextCatagory( );
		}
		if ( filter( TREE_NAME_PARAMETERS, filterList ) )
		{
			createParamtersCategory( );
		}
		if ( filter( TREE_NAME_NATIVE_OBJECTS, filterList ) )
		{
			createNativeObjectsCategory( );
		}
		if ( filter( TREE_NAME_BIRT_OBJECTS, filterList ) )
		{
			createBirtObjectsCategory( );
		}
		if ( filter( TREE_NAME_OPERATORS, filterList ) )
		{
			createOperatorsCategory( );
		}

		if ( currentMethodName != null )
		{
			switchContext( );
		}
	}

	/**
	 * Filters the tree name, given the filter list.
	 * 
	 * @param treeName
	 *            the tree name to be filtered.
	 * @param filters
	 *            the filter list.
	 * @return true if the tree name passes the filter list.
	 */
	private boolean filter( String treeName, List filters )
	{
		if ( filters == null )
		{
			return true;
		}
		for ( Iterator iter = filters.iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );

			if ( obj instanceof ExpressionFilter )
			{
				if ( !( (ExpressionFilter) obj ).select( this, treeName ) )
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Create operators band.Must set Tree before execution.
	 * 
	 */
	protected void createOperatorsCategory( )
	{
		assert tree != null;
		TreeItem topItem = createTopTreeItem( tree, TREE_ITEM_OPERATORS );
		TreeItem subItem = createSubFolderItem( topItem, TREE_ITEM_ASSIGNMENT );
		createSubTreeItems( subItem, OPERATORS_ASSIGNMENT, IMAGE_OPERATOR );
		subItem = createSubFolderItem( topItem, TREE_ITEM_COMPARISON );
		createSubTreeItems( subItem, OPERATORS_COMPARISON, IMAGE_OPERATOR );
		subItem = createSubFolderItem( topItem, TREE_ITEM_COMPUTATIONAL );
		createSubTreeItems( subItem, OPERATORS_COMPUTATIONAL, IMAGE_OPERATOR );
		subItem = createSubFolderItem( topItem, TREE_ITEM_LOGICAL );
		createSubTreeItems( subItem, OPERATORS_LOGICAL, IMAGE_OPERATOR );
	}

	/**
	 * Create native object band.Must set Tree before execution.
	 * 
	 */
	protected void createNativeObjectsCategory( )
	{
		assert tree != null;
		nativeObejctsItem = createTopTreeItem( tree, TREE_ITEM_NATIVE_OBJECTS );
		createObjects( nativeObejctsItem, OBJECTS_TYPE_NATIVE );
	}

	/**
	 * Create parameters band. Must set Tree before execution.
	 * 
	 */
	protected void createParamtersCategory( )
	{
		assert tree != null;
		parametersItem = createTopTreeItem( tree, TREE_ITEM_PARAMETERS );
		buildParameterTree( );
	}

	private void buildParameterTree( )
	{
		for ( Iterator iterator = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getParameters( )
				.iterator( ); iterator.hasNext( ); )
		{
			ReportElementHandle handle = (ReportElementHandle) iterator.next( );
			if ( handle instanceof ParameterHandle )
			{
				createSubTreeItem( parametersItem,
						DEUtil.getDisplayLabel( handle, false ),
						ReportPlatformUIImages.getImage( handle ),
						DEUtil.getExpression( handle ),
						( (ParameterHandle) handle ).getHelpText( ),
						true );
			}
			else if ( handle instanceof ParameterGroupHandle )
			{
				TreeItem groupItem = createSubTreeItem( parametersItem,
						DEUtil.getDisplayLabel( handle, false ),
						ReportPlatformUIImages.getImage( handle ),
						true );
				for ( Iterator itor = ( (ParameterGroupHandle) handle ).getParameters( )
						.iterator( ); itor.hasNext( ); )
				{
					ParameterHandle parameter = (ParameterHandle) itor.next( );
					createSubTreeItem( groupItem,
							parameter.getDisplayLabel( ),
							ReportPlatformUIImages.getImage( handle ),
							DEUtil.getExpression( parameter ),
							parameter.getDisplayLabel( ),
							true );
				}
			}
		}
	}

	/**
	 * Create data sets band.Must set Tree before execution.
	 * 
	 */
	protected void createDataSetsCategory( List dataSetList )
	{
		assert tree != null;
		dataSetsItem = createTopTreeItem( tree, TREE_ITEM_DATASETS );
		buildDataSetsTree( dataSetList );
	}

	private void buildDataSetsTree( List dataSetList )
	{
		clearTreeItem( dataSetsItem );
		for ( Iterator iterator = dataSetList.iterator( ); iterator.hasNext( ); )
		{
			DataSetHandle handle = (DataSetHandle) iterator.next( );
			TreeItem dataSetItem = createSubTreeItem( dataSetsItem,
					DEUtil.getDisplayLabel( handle, false ),
					ReportPlatformUIImages.getImage( handle ),
					true );

			try
			{
				CachedMetaDataHandle cachedMetadata = DataSetUIUtil.getCachedMetaDataHandle( handle );
				for ( Iterator iter = cachedMetadata.getResultSet( ).iterator( ); iter.hasNext( ); )
				{
					ResultSetColumnHandle element = (ResultSetColumnHandle) iter.next( );
					createSubTreeItem( dataSetItem,
							element.getColumnName( ),
							IMAGE_COLUMN,
							DEUtil.getExpression( element ),
							element.getColumnName( ),
							true );
				}
			}
			catch ( SemanticException e )
			{
			}
		}
	}

	/**
	 * Creates birt object tree. Must set Tree before execution.
	 * 
	 */
	protected void createBirtObjectsCategory( )
	{
		assert tree != null;
		birtObjectsItem = createTopTreeItem( tree, TREE_ITEM_BIRT_OBJECTS );
		createObjects( birtObjectsItem, OBJECTS_TYPE_BIRT );
	}

	/**
	 * Creates a top tree item
	 * 
	 * @param parent
	 * @param text
	 * @return tree item
	 */
	private TreeItem createTopTreeItem( Tree parent, String text )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE );
		item.setText( text );
		item.setImage( IMAGE_FOLDER );
		item.setData( ITEM_DATA_KEY_TOOLTIP, "" );//$NON-NLS-1$
		return item;
	}

	private TreeItem createTopTreeItem( Tree parent, String text, int index )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE, index );
		item.setText( text );
		item.setImage( IMAGE_FOLDER );
		item.setData( ITEM_DATA_KEY_TOOLTIP, "" );//$NON-NLS-1$
		return item;
	}

	private TreeItem createSubTreeItem( TreeItem parent, String text,
			Image image, boolean isEnabled )
	{
		return createSubTreeItem( parent, text, image, null, text, isEnabled );
	}

	private TreeItem createSubTreeItem( TreeItem parent, String text,
			Image image, String textData, String toolTip, boolean isEnabled )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE );
		item.setText( text );
		if ( image != null )
		{
			item.setImage( image );
		}
		item.setData( ITEM_DATA_KEY_TOOLTIP, toolTip );
		item.setData( ITEM_DATA_KEY_TEXT, textData );
		item.setData( ITEM_DATA_KEY_ENABLED, Boolean.valueOf( isEnabled ) );
		return item;
	}

	private TreeItem createSubFolderItem( TreeItem parent, String text )
	{
		return createSubTreeItem( parent, text, IMAGE_FOLDER, true );
	}

	private TreeItem createSubFolderItem( TreeItem parent, IClassInfo classInfo )
	{
		return createSubTreeItem( parent,
				classInfo.getDisplayName( ),
				IMAGE_FOLDER,
				null,
				classInfo.getToolTip( ),
				true );
	}

	private TreeItem createSubFolderItem( TreeItem parent,
			IScriptFunctionCategory category )
	{
		String categoreName = getCategoryDisplayName( category );
		return createSubTreeItem( parent,
				categoreName,
				IMAGE_FOLDER,
				null,
				category.getDescription( ),
				true );
	}

	private String getCategoryDisplayName( IScriptFunctionCategory category )
	{
		return category.getName( ) == null ? Messages.getString( "ExpressionTreeSupport.Category.Global" ) //$NON-NLS-1$
				: category.getName( );
	}

	private void createSubTreeItems( TreeItem parent, String[][] texts,
			Image image )
	{
		for ( int i = 0; i < texts.length; i++ )
		{
			createSubTreeItem( parent,
					texts[i][0],
					image,
					texts[i][0],
					texts[i][1],
					true );
		}
	}

	/**
	 * Adds mouse track listener.Must set Tree before execution.
	 * 
	 */
	public void addMouseTrackListener( )
	{
		assert tree != null;
		tree.addMouseTrackListener( new MouseTrackAdapter( ) {

			public void mouseHover( MouseEvent event )
			{
				Widget widget = event.widget;
				if ( widget == tree )
				{
					Point pt = new Point( event.x, event.y );
					TreeItem item = tree.getItem( pt );
					if ( item == null )
						tree.setToolTipText( "" );//$NON-NLS-1$
					else
					{
						String text = (String) item.getData( ITEM_DATA_KEY_TOOLTIP );
						tree.setToolTipText( text );
					}
				}
			}
		} );
	}

	/**
	 * Add double click behaviour. Must set Tree before execution.
	 * 
	 */
	public void addMouseListener( )
	{
		assert tree != null;
		tree.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent event )
			{
				TreeItem[] selection = getTreeSelection( );
				if ( selection == null || selection.length <= 0 )
					return;
				TreeItem item = selection[0];
				if ( item != null )
				{
					Object obj = item.getData( ITEM_DATA_KEY_TEXT );
					Boolean isEnabled = (Boolean) item.getData( ITEM_DATA_KEY_ENABLED );
					if ( obj != null && isEnabled.booleanValue( ) )
					{
						String text = (String) obj;
						insertText( text );
					}
				}
			}
		} );
	}

	protected TreeItem[] getTreeSelection( )
	{
		return tree.getSelection( );
	}

	/**
	 * Adds drag support to tree..Must set tree before execution.
	 */
	public void addDragSupportToTree( )
	{
		assert tree != null;
		DragSource dragSource = new DragSource( tree, DND.DROP_COPY
				| DND.DROP_MOVE );

		dragSource.setTransfer( new Transfer[]{
			TextTransfer.getInstance( )
		} );
		dragSource.addDragListener( new DragSourceAdapter( ) {

			public void dragStart( DragSourceEvent event )
			{
				TreeItem[] selection = tree.getSelection( );
				if ( selection.length <= 0
						|| selection[0].getData( ITEM_DATA_KEY_TEXT ) == null
						|| !( (Boolean) selection[0].getData( ITEM_DATA_KEY_ENABLED ) ).booleanValue( ) )
				{
					event.doit = false;
					return;
				}
			}

			public void dragSetData( DragSourceEvent event )
			{
				if ( TextTransfer.getInstance( )
						.isSupportedType( event.dataType ) )
				{
					TreeItem[] selection = tree.getSelection( );
					if ( selection.length > 0 )
					{
						event.data = selection[0].getData( ITEM_DATA_KEY_TEXT );
					}
				}
			}
		} );
	}

	/**
	 * Insert a text string into the text area
	 * 
	 * @param text
	 */
	protected void insertText( String text )
	{
		StyledText textWidget = expressionViewer.getTextWidget( );
		if ( !textWidget.isEnabled( ) )
		{
			return;
		}
		int selectionStart = textWidget.getSelection( ).x;
		if ( text.equalsIgnoreCase( "x++" ) ) //$NON-NLS-1$
		{
			text = textWidget.getSelectionText( ) + "++";//$NON-NLS-1$
		}
		else if ( text.equalsIgnoreCase( "x--" ) )//$NON-NLS-1$
		{
			text = textWidget.getSelectionText( ) + "--";//$NON-NLS-1$
		}
		else if ( text.equalsIgnoreCase( "++x" ) )//$NON-NLS-1$
		{
			text = "++" + textWidget.getSelectionText( );//$NON-NLS-1$
		}
		else if ( text.equalsIgnoreCase( "--x" ) )//$NON-NLS-1$
		{
			text = "--" + textWidget.getSelectionText( );//$NON-NLS-1$
		}

		textWidget.insert( text );
		textWidget.setSelection( selectionStart + text.length( ) );
		textWidget.setFocus( );

		if ( text.endsWith( "()" ) ) //$NON-NLS-1$
		{
			textWidget.setCaretOffset( textWidget.getCaretOffset( ) - 1 ); // Move
		}
	}

	/**
	 * Adds drop support to viewer.Must set viewer before execution.
	 * 
	 */
	public void addDropSupportToViewer( )
	{
		assert expressionViewer != null;
		if ( dropTarget == null || dropTarget.isDisposed( ) )
		{
			final StyledText text = expressionViewer.getTextWidget( );

			// Doesn't add again if a drop target has been created in the
			// viewer.
			if ( text.getData( "DropTarget" ) != null ) //$NON-NLS-1$
			{
				return;
			}

			dropTarget = new DropTarget( text, DND.DROP_COPY | DND.DROP_DEFAULT );
			dropTarget.setTransfer( new Transfer[]{
				TextTransfer.getInstance( )
			} );
			dropTargetAdapter = new DropTargetAdapter( ) {

				public void dragEnter( DropTargetEvent event )
				{
					text.setFocus( );
					if ( event.detail == DND.DROP_DEFAULT )
						event.detail = DND.DROP_COPY;
					if ( event.detail != DND.DROP_COPY )
						event.detail = DND.DROP_NONE;
				}

				public void dragOver( DropTargetEvent event )
				{
					event.feedback = DND.FEEDBACK_SCROLL
							| DND.FEEDBACK_INSERT_BEFORE;
				}

				public void dragOperationChanged( DropTargetEvent event )
				{
					dragEnter( event );
				}

				public void drop( DropTargetEvent event )
				{
					if ( event.data instanceof String )
						insertText( (String) event.data );
				}
			};
			dropTarget.addDropListener( dropTargetAdapter );
		}
	}

	public void removeDropSupportToViewer( )
	{
		if ( dropTarget != null && !dropTarget.isDisposed( ) )
		{
			if ( dropTargetAdapter != null )
			{
				dropTarget.removeDropListener( dropTargetAdapter );
				dropTargetAdapter = null;
			}
			dropTarget.dispose( );
			dropTarget = null;
		}
	}

	/**
	 * Sets the tree model.
	 * 
	 * @param tree
	 */
	public void setTree( Tree tree )
	{
		this.tree = tree;
	}

	protected Tree getTree( )
	{
		return tree;
	}

	/**
	 * Sets the viewer to use.
	 * 
	 * @param expressionViewer
	 */
	public void setExpressionViewer( SourceViewer expressionViewer )
	{
		this.expressionViewer = expressionViewer;
	}

	protected SourceViewer getExpressionViewer( )
	{
		return expressionViewer;
	}

	/**
	 * Gets an icon image by the key in plugin.properties
	 * 
	 * @param id
	 * @return image
	 */
	private static Image getIconImage( String id )
	{
		return ReportPlatformUIImages.getImage( id );
	}

	private void createObjects( TreeItem topItem, String objectType )
	{
		for ( Iterator itor = DEUtil.getClasses( ).iterator( ); itor.hasNext( ); )
		{
			IClassInfo classInfo = (IClassInfo) itor.next( );
			if ( classInfo.isNative( )
					&& OBJECTS_TYPE_BIRT.equals( objectType )
					|| !classInfo.isNative( )
					&& OBJECTS_TYPE_NATIVE.equals( objectType )
					|| classInfo.getName( ).equals( "Total" ) ) //$NON-NLS-1$
			{
				continue;
			}
			TreeItem subItem = createSubFolderItem( topItem, classInfo );
			Image globalImage = null;
			if ( isGlobal( classInfo.getName( ) ) )
			{
				globalImage = IMAGE_GOLBAL;
			}

			ArrayList<Object> childrenList = new ArrayList<Object>( );
			IMemberInfo[] members = (IMemberInfo[]) DEUtil.getMembers( classInfo )
					.toArray( new IMemberInfo[0] );
			for ( int i = 0; i < members.length; i++ )
			{
				childrenList.add( new ILocalizableInfo[]{
						classInfo, members[i]
				} );
			}
			List methodList = new ArrayList( );
			methodList.addAll( DEUtil.getMethods( classInfo, true ) );
			methodList.addAll( AggregationUtil.getMethods( classInfo ) );

			IMethodInfo[] methods = (IMethodInfo[]) methodList.toArray( new IMethodInfo[0] );
			for ( int i = 0; i < methods.length; i++ )
			{
				IMethodInfo mi = (IMethodInfo) methods[i];
				processMethods( classInfo, mi, childrenList );
			}

			ILocalizableInfo[][] children = childrenList.toArray( new ILocalizableInfo[0][] );
			sortLocalizableInfo( children );

			for ( int i = 0; i < children.length; i++ )
			{
				Object obj = children[i];
				createSubTreeItem( subItem,
						getDisplayText( obj ),
						globalImage == null ? getImage( obj ) : globalImage,
						getInsertText( obj ),
						getTooltipText( obj ),
						true );
			}
		}

		if ( OBJECTS_TYPE_BIRT.equals( objectType ) )
		{
			try
			{
				IScriptFunctionCategory[] categorys = FunctionProvider.getCategories( );
				Arrays.sort( categorys,
						new Comparator<IScriptFunctionCategory>( ) {

							public int compare( IScriptFunctionCategory o1,
									IScriptFunctionCategory o2 )
							{
								return getCategoryDisplayName( o1 ).compareTo( getCategoryDisplayName( o2 ) );
							}
						} );
				if ( categorys != null )
				{
					for ( int i = 0; i < categorys.length; i++ )
					{
						TreeItem subItem = createSubFolderItem( topItem,
								categorys[i] );
						IScriptFunction[] functions = categorys[i].getFunctions( );
						Arrays.sort( functions,
								new Comparator<IScriptFunction>( ) {

									public int compare( IScriptFunction o1,
											IScriptFunction o2 )
									{
										return getFunctionDisplayText( o1 ).compareTo( getFunctionDisplayText( o2 ) );
									}
								} );
						if ( functions != null )
						{
							for ( int j = 0; j < functions.length; j++ )
							{
								Image image = null;

								if ( functions[j].isStatic( ) )
								{
									image = IMAGE_STATIC_METHOD;
								}
								else
								{
									image = IMAGE_METHOD;
								}
								createSubTreeItem( subItem,
										getFunctionDisplayText( functions[j] ),
										image,
										getFunctionExpression( categorys[i],
												functions[j] ),
										functions[j].getDescription( ),
										true );
							}
						}
					}
				}
			}
			catch ( BirtException e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}

	public void sortLocalizableInfo( ILocalizableInfo[][] infos )
	{
		Arrays.sort( infos, new Comparator<ILocalizableInfo[]>( ) {

			private int computeWeight( ILocalizableInfo obj )
			{
				if ( obj instanceof IMemberInfo )
				{
					return ( (IMemberInfo) obj ).isStatic( ) ? 0 : 2;
				}
				else if ( obj instanceof IMethodInfo )
				{
					if ( ( (IMethodInfo) obj ).isConstructor( ) )
					{
						return 3;
					}
					return ( (IMethodInfo) obj ).isStatic( ) ? 1 : 4;
				}

				return 4;
			}

			public int compare( ILocalizableInfo[] o1, ILocalizableInfo[] o2 )
			{
				ILocalizableInfo info1 = ( (ILocalizableInfo[]) o1 )[1];
				ILocalizableInfo info2 = ( (ILocalizableInfo[]) o2 )[1];

				int w1 = computeWeight( info1 );
				int w2 = computeWeight( info2 );

				if ( w1 != w2 )
				{
					return w1 < w2 ? -1 : 1;
				}
				return Collator.getInstance( ).compare( getDisplayText( o1 ),
						getDisplayText( o2 ) );
			}
		} );
	}

	public Image getImage( Object element )
	{
		if ( element instanceof ILocalizableInfo[] )
		{
			ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
			if ( info instanceof IMethodInfo )
			{
				if ( ( (IMethodInfo) info ).isStatic( ) )
				{
					return IMAGE_STATIC_METHOD;
				}
				else if ( ( (IMethodInfo) info ).isConstructor( ) )
				{
					return IMAGE_CONSTRUCTOR;
				}
				return IMAGE_METHOD;
			}
			if ( info instanceof IMemberInfo )
			{
				if ( ( (IMemberInfo) info ).isStatic( ) )
				{
					return IMAGE_STATIC_MEMBER;
				}
				return IMAGE_MEMBER;
			}
		}
		return null;
	}

	public String getInsertText( Object element )
	{
		if ( element instanceof VariableElementHandle )
		{
			return ( (VariableElementHandle) element ).getVariableName( );
		}
		if ( element instanceof ILocalizableInfo[] )
		{
			IClassInfo classInfo = (IClassInfo) ( (ILocalizableInfo[]) element )[0];
			ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
			StringBuffer insertText = new StringBuffer( );
			if ( info instanceof IMemberInfo )
			{
				IMemberInfo memberInfo = (IMemberInfo) info;
				if ( memberInfo.isStatic( ) )
				{
					insertText.append( classInfo.getName( ) + "." ); //$NON-NLS-1$
				}
				insertText.append( memberInfo.getName( ) );
			}
			else if ( info instanceof IMethodInfo )
			{
				IMethodInfo methodInfo = (IMethodInfo) info;
				if ( methodInfo.isStatic( ) )
				{
					insertText.append( classInfo.getName( ) + "." ); //$NON-NLS-1$
				}
				else if ( methodInfo.isConstructor( ) )
				{
					insertText.append( "new " ); //$NON-NLS-1$
				}
				insertText.append( methodInfo.getName( ) );
				insertText.append( "()" ); //$NON-NLS-1$
			}
			return insertText.toString( );
		}
		return null;
	}

	public String getTooltipText( Object element )
	{
		if ( element instanceof ILocalizableInfo[] )
		{
			ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
			String tooltip = null;
			if ( info instanceof IMemberInfo )
			{
				tooltip = ( (IMemberInfo) info ).getToolTip( );
			}
			else if ( info instanceof IMethodInfo )
			{
				tooltip = ( (IMethodInfo) info ).getToolTip( );
			}
			return tooltip == null ? "" : tooltip;
		}
		return null;
	}

	protected String getDisplayText( Object element )
	{
		if ( element instanceof ILocalizableInfo[] )
		{
			// including class info,method info and member info
			ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
			StringBuffer displayText = new StringBuffer( info.getName( ) );
			if ( info instanceof IMethodInfo )
			{
				IMethodInfo method = (IMethodInfo) info;
				displayText.append( "(" ); //$NON-NLS-1$

				int argIndex = ( ( (ILocalizableInfo[]) element ).length > 2 ) ? ( ( (IIndexInfo) ( (ILocalizableInfo[]) element )[2] ).getIndex( ) )
						: 0;
				int idx = -1;

				Iterator argumentListIter = method.argumentListIterator( );
				while ( argumentListIter.hasNext( ) )
				{
					IArgumentInfoList arguments = (IArgumentInfoList) argumentListIter.next( );

					idx++;

					if ( idx < argIndex )
					{
						continue;
					}

					boolean isFirst = true;

					for ( Iterator iter = arguments.argumentsIterator( ); iter.hasNext( ); )
					{
						IArgumentInfo argInfo = (IArgumentInfo) iter.next( );
						if ( !isFirst )
						{
							displayText.append( ", " ); //$NON-NLS-1$
						}
						isFirst = false;

						if ( argInfo.getType( ) != null
								&& argInfo.getType( ).length( ) > 0 )
						{
							displayText.append( argInfo.getType( ) + " " //$NON-NLS-1$
									+ argInfo.getName( ) );
						}
						else
							displayText.append( argInfo.getName( ) );
					}

					break;
				}

				displayText.append( ")" ); //$NON-NLS-1$

				if ( !method.isConstructor( ) )
				{
					displayText.append( " : " ); //$NON-NLS-1$
					String returnType = method.getReturnType( );
					if ( returnType == null || returnType.length( ) == 0 )
					{
						returnType = "void"; //$NON-NLS-1$
					}
					displayText.append( returnType );
				}

			}
			else if ( info instanceof IMemberInfo )
			{
				String dataType = ( (IMemberInfo) info ).getDataType( );
				if ( dataType != null && dataType.length( ) > 0 )
				{
					displayText.append( " : " ); //$NON-NLS-1$
					displayText.append( dataType );
				}
			}
			return displayText.toString( );
		}
		return null;
	}

	private boolean isGlobal( String name )
	{
		// TODO global validation is hard coded
		return name != null && name.startsWith( "Global" ); //$NON-NLS-1$
	}

	private String getFunctionDisplayText( IScriptFunction function )
	{
		String functionStart = function.isConstructor( ) ? "new " : ""; //$NON-NLS-1$//$NON-NLS-2$
		StringBuffer displayText = new StringBuffer( functionStart );
		displayText.append( function.getName( ) );
		IScriptFunctionArgument[] arguments = function.getArguments( );

		displayText.append( "(" ); //$NON-NLS-1$

		if ( arguments != null )
		{
			for ( int i = 0; i < arguments.length; i++ )
			{
				displayText.append( arguments[i].getName( ) );
				if ( i < arguments.length - 1 )
					displayText.append( ", " );//$NON-NLS-1$
			}
		}
		displayText.append( ")" ); //$NON-NLS-1$
		return displayText.toString( );
	}

	private String getFunctionExpression( IScriptFunctionCategory category,
			IScriptFunction function )
	{
		String functionStart = function.isConstructor( ) ? "new " : ""; //$NON-NLS-1$//$NON-NLS-2$
		StringBuffer textData = new StringBuffer( functionStart );
		if ( function.isStatic( ) )
		{
			if ( category.getName( ) != null )
			{
				textData.append( category.getName( ) + "." );//$NON-NLS-1$
			}
		}
		textData.append( function.getName( ) + "()" );
		return textData.toString( );
	}

	protected void createContextCatagory( )
	{
		assert tree != null;
		contextItem = createTopTreeItem( tree, TREE_ITEM_CONTEXT );
		createContextObjects( currentMethodName );
	}

	/**
	 * Creates context objects tree. Context ojects tree is used in JS editor
	 * palette, which displays current object method's arguments.
	 */
	protected void createContextObjects( String methodName )
	{
		if ( contextItem != null
				&& !contextItem.isDisposed( )
				&& currentEditObject != null
				&& methodName != null )
		{
			clearTreeItem( contextItem );
			DesignElementHandle handle = (DesignElementHandle) currentEditObject;
			List args = DEUtil.getDesignElementMethodArgumentsInfo( handle,
					methodName );
			for ( Iterator iter = args.iterator( ); iter.hasNext( ); )
			{
				String argName = ( (IArgumentInfo) iter.next( ) ).getName( );
				createSubTreeItem( contextItem,
						argName,
						IMAGE_METHOD,
						argName,
						"", //$NON-NLS-1$
						true );
			}
		}
	}

	public void setCurrentEditObject( Object obj )
	{
		this.currentEditObject = obj;
		clearTreeItem( contextItem );
		clearDynamicItems( );
	}

	private void clearTreeItem( TreeItem treeItem )
	{
		if ( treeItem == null || treeItem.isDisposed( ) )
		{
			return;
		}
		TreeItem[] items = treeItem.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i] != null && !items[i].isDisposed( ) )
			{
				items[i].dispose( );
			}
		}
	}

	private void clearDynamicItems( )
	{
		if ( dynamicItems != null )
		{
			for ( TreeItem ti : dynamicItems )
			{
				if ( ti != null && !ti.isDisposed( ) )
				{
					ti.dispose( );
				}
			}

			dynamicItems.clear( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 * 
	 * Listen to JS editor method change.
	 */
	public void selectionChanged( SelectionChangedEvent event )
	{
		ISelection selection = event.getSelection( );
		if ( selection != null )
		{
			Object[] sel = ( (IStructuredSelection) selection ).toArray( );
			if ( sel.length == 1 )
			{
				if ( sel[0] instanceof IPropertyDefn )
				{
					IPropertyDefn elePropDefn = (IPropertyDefn) sel[0];

					currentMethodName = elePropDefn.getName( );
					currentContextName = elePropDefn.getContext( );

					switchContext( );
				}
			}
		}
	}

	protected void switchContext( )
	{

		if ( tree == null || tree.isDisposed( ) )
		{
			return;
		}

		createContextObjects( currentMethodName );

		updateClientContext( );

		updateDynamicItems( );
	}

	private void updateClientContext( )
	{
		if ( CLIENT_CONTEXT.equals( currentContextName ) )
		{
			if ( parametersItem != null && !parametersItem.isDisposed( ) )
			{
				parametersItem.dispose( );
				parametersItem = null;
			}

			if ( birtObjectsItem != null && !birtObjectsItem.isDisposed( ) )
			{
				birtObjectsItem.dispose( );
				birtObjectsItem = null;
			}
		}
		else
		{
			if ( parametersItem == null || parametersItem.isDisposed( ) )
			{
				int idx = tree.indexOf( contextItem );

				if ( idx == -1 )
				{
					parametersItem = createTopTreeItem( tree,
							TREE_ITEM_PARAMETERS );
				}
				else
				{
					parametersItem = createTopTreeItem( tree,
							TREE_ITEM_PARAMETERS,
							idx + 1 );
				}
				buildParameterTree( );
			}

			if ( birtObjectsItem == null || birtObjectsItem.isDisposed( ) )
			{
				int idx = tree.indexOf( nativeObejctsItem );

				if ( idx == -1 )
				{
					birtObjectsItem = createTopTreeItem( tree,
							TREE_ITEM_BIRT_OBJECTS );
				}
				else
				{
					birtObjectsItem = createTopTreeItem( tree,
							TREE_ITEM_BIRT_OBJECTS,
							idx + 1 );
				}
				createObjects( birtObjectsItem, OBJECTS_TYPE_BIRT );
			}
		}
	}

	private void updateDynamicItems( )
	{
		clearDynamicItems( );

		Object[] adapters = ElementAdapterManager.getAdapters( currentEditObject,
				IContextExpressionProvider.class );

		if ( adapters == null )
		{
			return;
		}

		for ( Object adapt : adapters )
		{
			IContextExpressionProvider contextProvider = (IContextExpressionProvider) adapt;

			if ( contextProvider != null )
			{
				IExpressionProvider exprProvider = contextProvider.getExpressionProvider( currentMethodName );

				if ( exprProvider != null )
				{
					createDynamicCategory( exprProvider );
				}
			}
		}
	}

	private void createDynamicCategory( IExpressionProvider provider )
	{
		Object[] cats = provider.getCategory( );

		if ( cats != null )
		{
			for ( Object cat : cats )
			{
				TreeItem ti = createTopTreeItem( tree, cat, provider );

				if ( dynamicItems == null )
				{
					dynamicItems = new ArrayList<TreeItem>( );
				}

				dynamicItems.add( ti );

				if ( provider.hasChildren( cat ) )
				{
					createDynamicChildern( ti, cat, provider );
				}
			}
		}
	}

	private void createDynamicChildern( TreeItem parent, Object element,
			IExpressionProvider provider )
	{
		Object[] children = provider.getChildren( element );

		if ( children != null )
		{
			for ( Object child : children )
			{
				if ( provider.hasChildren( child ) )
				{
					TreeItem ti = createSubFolderItem( parent, child, provider );

					createDynamicChildern( ti, child, provider );
				}
				else
				{
					createSubTreeItem( parent, child, provider );
				}
			}
		}
	}

	private TreeItem createTopTreeItem( Tree tree, Object element,
			IExpressionProvider provider )
	{
		TreeItem item = new TreeItem( tree, SWT.NONE );
		item.setText( provider.getDisplayText( element ) );
		item.setImage( provider.getImage( element ) );
		item.setData( ITEM_DATA_KEY_TOOLTIP, provider.getTooltipText( element ) );
		return item;
	}

	private TreeItem createSubFolderItem( TreeItem parent, Object element,
			IExpressionProvider provider )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE );
		item.setText( provider.getDisplayText( element ) );
		item.setImage( provider.getImage( element ) );
		item.setData( ITEM_DATA_KEY_TOOLTIP, provider.getTooltipText( element ) );
		return item;
	}

	private TreeItem createSubTreeItem( TreeItem parent, Object element,
			IExpressionProvider provider )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE );
		item.setText( provider.getDisplayText( element ) );
		item.setImage( provider.getImage( element ) );
		item.setData( ITEM_DATA_KEY_TOOLTIP, provider.getTooltipText( element ) );
		item.setData( ITEM_DATA_KEY_TEXT, provider.getInsertText( element ) );
		item.setData( ITEM_DATA_KEY_ENABLED, Boolean.TRUE );
		return item;
	}

	public void updateParametersTree( )
	{
		if ( parametersItem != null && !parametersItem.isDisposed( ) )
		{
			clearTreeItem( parametersItem );
			buildParameterTree( );
		}
	}

	private void processMethods( IClassInfo classInfo, IMethodInfo mi,
			List childrenList )
	{
		Iterator alitr = mi.argumentListIterator( );

		int idx = 0;
		if ( alitr == null )
		{
			childrenList.add( new ILocalizableInfo[]{
					classInfo, mi
			} );
		}
		else
		{
			while ( alitr.hasNext( ) )
			{
				alitr.next( );

				childrenList.add( new ILocalizableInfo[]{
						classInfo, mi, new IIndexInfo( idx++ )
				} );
			}
		}
	}
}