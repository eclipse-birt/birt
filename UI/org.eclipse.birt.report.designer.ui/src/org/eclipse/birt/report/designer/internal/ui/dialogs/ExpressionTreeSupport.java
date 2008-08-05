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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
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
		String categoreName = category.getName( ) == null ? Messages.getString( "ExpressionTreeSupport.Category.Global" ) //$NON-NLS-1$
				: category.getName( );
		return createSubTreeItem( parent,
				categoreName,
				IMAGE_FOLDER,
				null,
				category.getDescription( ),
				true );
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

			// Add members
			for ( Iterator iterator = classInfo.getMembers( ).iterator( ); iterator.hasNext( ); )
			{
				IMemberInfo memberInfo = (IMemberInfo) iterator.next( );
				Image image = globalImage;
				if ( image == null )
				{
					if ( memberInfo.isStatic( ) )
					{
						image = IMAGE_STATIC_MEMBER;
					}
					else
					{
						image = IMAGE_MEMBER;
					}
				}
				createSubTreeItem( subItem,
						memberInfo.getDisplayName( ),
						image,
						getMemberTextData( classInfo.getName( ), memberInfo ),
						memberInfo.getToolTip( ),
						true );
			}

			// Add constructors and methods
			List<IMethodInfo> methodList = new ArrayList<IMethodInfo>( );
			methodList.add( classInfo.getConstructor( ) );
			methodList.addAll( classInfo.getMethods( ) );
			for ( Iterator<IMethodInfo> iterator = methodList.iterator( ); iterator.hasNext( ); )
			{
				IMethodInfo methodInfo = iterator.next( );
				if ( methodInfo == null )
				{
					// Constructor is null
					continue;
				}
				Image image = globalImage;
				if ( image == null )
				{
					if ( methodInfo.isStatic( ) )
					{
						image = IMAGE_STATIC_METHOD;
					}
					else
					{
						image = IMAGE_METHOD;
					}
				}
				// Split a method with more than one signature into several
				// entries
				List displayList = getMethodArgumentsList( classInfo.getName( ),
						methodInfo );
				for ( int i = 0; i < displayList.size( ); i++ )
				{
					String[] array = (String[]) displayList.get( i );
					createSubTreeItem( subItem,
							array[0],
							image,
							array[1],
							methodInfo.getToolTip( ),
							true );
				}
			}
		}

		if ( OBJECTS_TYPE_BIRT.equals( objectType ) )
		{
			try
			{
				IScriptFunctionCategory[] categorys = FunctionProvider.getCategories( );
				if ( categorys != null )
				{
					for ( int i = 0; i < categorys.length; i++ )
					{
						TreeItem subItem = createSubFolderItem( topItem,
								categorys[i] );
						IScriptFunction[] functions = categorys[i].getFunctions( );
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

	private boolean isGlobal( String name )
	{
		// TODO global validation is hard coded
		return name != null && name.startsWith( "Global" ); //$NON-NLS-1$
	}

	private List getMethodArgumentsList( String className, IMethodInfo info )
	{
		List list = new ArrayList( );
		boolean isClassNameAdded = !isGlobal( className ) && isStatic( info );
		String methodStart = info.isConstructor( ) ? "new " : ""; //$NON-NLS-1$//$NON-NLS-2$

		for ( Iterator it = info.argumentListIterator( ); it.hasNext( ); )
		{
			// Includes display text in tree view and expression in source
			// viewer
			String[] array = new String[2];

			StringBuffer displayText = new StringBuffer( methodStart );
			displayText.append( info.getDisplayName( ) );
			displayText.append( "(" );//$NON-NLS-1$

			StringBuffer expression = new StringBuffer( methodStart );
			if ( isClassNameAdded )
			{
				expression.append( className + "." );//$NON-NLS-1$
			}
			expression.append( info.getName( ) );
			expression.append( "(" );//$NON-NLS-1$	

			IArgumentInfoList arguments = (IArgumentInfoList) it.next( );
			boolean firstTime = true;
			for ( Iterator iterator = arguments.argumentsIterator( ); iterator.hasNext( ); )
			{
				IArgumentInfo argument = (IArgumentInfo) iterator.next( );
				if ( !firstTime )
				{
					displayText.append( ", " );//$NON-NLS-1$
				}
				firstTime = false;
				displayText.append( IArgumentInfo.OPTIONAL_ARGUMENT_NAME.equals( argument.getName( ) ) ? argument.getDisplayName( )
						: ( argument.getType( ) + " " + argument.getDisplayName( ) ) ); //$NON-NLS-1$
			}
			displayText.append( ")" );//$NON-NLS-1$
			expression.append( ")" );//$NON-NLS-1$
			array[0] = displayText.toString( );
			array[1] = expression.toString( );
			list.add( array );
		}
		return list;
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

	private String getMemberTextData( String className, ILocalizableInfo info )
	{
		StringBuffer textData = new StringBuffer( );
		if ( !isGlobal( className ) && isStatic( info ) )
		{
			textData.append( className + "." );//$NON-NLS-1$
		}
		textData.append( info.getName( ) );
		return textData.toString( );
	}

	private boolean isStatic( ILocalizableInfo info )
	{
		return info instanceof IMethodInfo
				&& ( (IMethodInfo) info ).isStatic( )
				|| info instanceof IMemberInfo
				&& ( (IMemberInfo) info ).isStatic( );
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
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 *      org.eclipse.jface.viewers.SelectionChangedEvent)
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
}