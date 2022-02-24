/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IIndexInfo;
import org.eclipse.birt.report.designer.internal.ui.views.memento.Memento;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoBuilder;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoElement;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.expressions.IContextExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;

import com.ibm.icu.text.Collator;

/**
 * Deals with tree part of expression builder. Adds some mouse and DND support
 * to tree and corresponding source viewer.
 */
public class ExpressionTreeSupport implements ISelectionChangedListener {

	// Tree item icon images
	private static final Image IMAGE_FOLDER = ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJ_FOLDER);

	private static final Image IMAGE_OPERATOR = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_OPERATOR);

	private static final Image IMAGE_GOLBAL = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_GLOBAL);

	private static final Image IMAGE_METHOD = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_METHOD);

	private static final Image IMAGE_STATIC_METHOD = getIconImage(
			IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD);

	private static final Image IMAGE_CONSTRUCTOR = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_CONSTRUCTOP);

	private static final Image IMAGE_MEMBER = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_MEMBER);

	private static final Image IMAGE_STATIC_MEMBER = getIconImage(
			IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER);

	/** Arithmetic operators and their descriptions */
	private static final String[][] OPERATORS_ASSIGNMENT = new String[][] {
			{ "=", Messages.getString("ExpressionProvider.Operator.Assign") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "+=", Messages.getString("ExpressionProvider.Operator.AddTo") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "-=", Messages.getString("ExpressionProvider.Operator.SubFrom") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "*=", Messages.getString("ExpressionProvider.Operator.MultTo") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "/=", Messages.getString("ExpressionProvider.Operator.DividingFrom") //$NON-NLS-1$ //$NON-NLS-2$
			} };

	/** Comparison operators and their descriptions */
	private static final String[][] OPERATORS_COMPARISON = new String[][] {
			{ "==", Messages.getString("ExpressionProvider.Operator.Equals") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "<", Messages.getString("ExpressionProvider.Operator.Less") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "<=", //$NON-NLS-1$
					Messages.getString("ExpressionProvider.Operator.LessEqual") //$NON-NLS-1$
			}, { "!=", //$NON-NLS-1$
					Messages.getString("ExpressionProvider.Operator.NotEqual") //$NON-NLS-1$
			}, { ">", Messages.getString("ExpressionProvider.Operator.Greater") //$NON-NLS-1$ //$NON-NLS-2$
			}, { ">=", //$NON-NLS-1$
					Messages.getString("ExpressionProvider.Operator.GreaterEquals") //$NON-NLS-1$
			} };

	/** Computational operators and their descriptions */
	private static final String[][] OPERATORS_COMPUTATIONAL = new String[][] {

			{ "+", Messages.getString("ExpressionProvider.Operator.Add") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "-", Messages.getString("ExpressionProvider.Operator.Sub") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "*", Messages.getString("ExpressionProvider.Operator.Mult") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "/", //$NON-NLS-1$
					Messages.getString("ExpressionProvider.Operator.Divides") //$NON-NLS-1$
			}, { "++X ", //$NON-NLS-1$
					Messages.getString("ExpressionProvider.Operator.Inc") //$NON-NLS-1$
			}, { "X++ ", Messages.getString("ExpressionProvider.Operator.ReturnInc") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "--X ", Messages.getString("ExpressionProvider.Operator.Dec") //$NON-NLS-1$ //$NON-NLS-2$
			}, { "X-- ", Messages.getString("ExpressionProvider.Operator.ReturnDec") //$NON-NLS-1$ //$NON-NLS-2$
			} };

	/** Logical operators and their descriptions */
	private static final String[][] OPERATORS_LOGICAL = new String[][] { { "&&", //$NON-NLS-1$
			Messages.getString("ExpressionProvider.Operator.And") //$NON-NLS-1$
			}, { "||", //$NON-NLS-1$
					Messages.getString("ExpressionProvider.Operator.Or") //$NON-NLS-1$
			} };

	private static final String TREE_ITEM_CONTEXT = Messages.getString("ExpressionProvider.Category.Context"); //$NON-NLS-1$

	private static final String TREE_ITEM_OPERATORS = Messages.getString("ExpressionProvider.Category.Operators"); //$NON-NLS-1$

	private static final String TREE_ITEM_BIRT_OBJECTS = Messages.getString("ExpressionProvider.Category.BirtObjects"); //$NON-NLS-1$

	private static final String TREE_ITEM_PARAMETERS = Messages.getString("ExpressionProvider.Category.Parameters"); //$NON-NLS-1$

	private static final String TREE_ITEM_NATIVE_OBJECTS = Messages
			.getString("ExpressionProvider.Category.NativeObjects"); //$NON-NLS-1$

	private static final String TREE_ITEM_VARIABLES = Messages.getString("ExpressionProvider.Category.Variables"); //$NON-NLS-1$

	private static final String TREE_ITEM_LOGICAL = Messages.getString("ExpressionProvider.Operators.Logical"); //$NON-NLS-1$

	private static final String TREE_ITEM_COMPUTATIONAL = Messages
			.getString("ExpressionProvider.Operators.Computational"); //$NON-NLS-1$

	private static final String TREE_ITEM_COMPARISON = Messages.getString("ExpressionProvider.Operators.Comparison"); //$NON-NLS-1$

	private static final String TREE_ITEM_ASSIGNMENT = Messages.getString("ExpressionProvider.Operators.Assignment"); //$NON-NLS-1$

	/** Tool tip key of tree item data */
	protected static final String ITEM_DATA_KEY_TOOLTIP = "TOOL_TIP"; //$NON-NLS-1$
	/**
	 * Text key of tree item data, this data is the text string to be inserted into
	 * the text area
	 */
	protected static final String ITEM_DATA_KEY_TEXT = "TEXT"; //$NON-NLS-1$
	protected static final String ITEM_DATA_KEY_ENABLED = "ENABLED"; //$NON-NLS-1$

	private static final String OBJECTS_TYPE_NATIVE = "native";//$NON-NLS-1$
	private static final String OBJECTS_TYPE_BIRT = "birt";//$NON-NLS-1$

	private SourceViewer expressionViewer;
	private Tree tree;
	private DropTarget dropTarget;
	private DropTargetAdapter dropTargetAdapter;

	private Object currentEditObject;
	private String currentMethodName;
	private TreeItem contextItem, parametersItem, nativeObejctsItem, birtObjectsItem, operatorsItem, variablesItem;
	private List<TreeItem> dynamicItems;

	private List staticFilters;

	private MementoBuilder builder = new MementoBuilder();

	private Memento viewerMemento;

	public ExpressionTreeSupport() {
		super();
	}

	/**
	 * Creates all expression trees in default order
	 */
	public void createDefaultExpressionTree() {
		createFilteredExpressionTree(null);
	}

	/**
	 * Creates selected expression trees with given filter list.
	 * 
	 * @param filterList list of filters
	 */
	public void createFilteredExpressionTree(List filterList) {
		this.staticFilters = filterList;

		createExpressionTree();
	}

	public String getElementType() {
		if (currentEditObject == null)
			return null;
		String displayName = ((DesignElementHandle) currentEditObject).getDefn().getDisplayName();

		if (displayName == null || "".equals(displayName))//$NON-NLS-1$
		{
			displayName = ((DesignElementHandle) currentEditObject).getDefn().getName();
		}
		return displayName;
	}

	private void createExpressionTree() {
		if (tree == null || tree.isDisposed()) {
			return;
		}

		List<IExpressionProvider> dynamicContextProviders = null;
		List<ExpressionFilter> dynamicFilters = null;

		if (currentEditObject != null && currentMethodName != null) {
			if (viewerMemento != null) {
				IMemento memento = viewerMemento.getChild(getElementType());
				if (memento == null) {
					Memento elementMemento = (Memento) viewerMemento.createChild(getElementType(),
							MementoElement.Type_Element);
					elementMemento.getMementoElement().setValue(getElementType());
				}
			}

			Object[] adapters = ElementAdapterManager.getAdapters(currentEditObject, IContextExpressionProvider.class);

			if (adapters != null) {
				for (Object adapt : adapters) {
					IContextExpressionProvider contextProvider = (IContextExpressionProvider) adapt;

					if (contextProvider != null) {
						IExpressionProvider exprProvider = contextProvider.getExpressionProvider(currentMethodName);

						if (exprProvider != null) {
							if (dynamicContextProviders == null) {
								dynamicContextProviders = new ArrayList<IExpressionProvider>();
							}

							dynamicContextProviders.add(exprProvider);
						}

						ExpressionFilter exprFilter = contextProvider.getExpressionFilter(currentMethodName);

						if (exprFilter != null) {
							if (dynamicFilters == null) {
								dynamicFilters = new ArrayList<ExpressionFilter>();
							}

							dynamicFilters.add(exprFilter);
						}
					}
				}
			}
		}

		List<ExpressionFilter> filters = null;

		if (staticFilters != null && dynamicFilters != null) {
			filters = new ArrayList<ExpressionFilter>();
			filters.addAll(staticFilters);
			filters.addAll(dynamicFilters);
		} else if (staticFilters != null) {
			filters = staticFilters;
		} else if (dynamicFilters != null) {
			filters = dynamicFilters;
		}

		// flag to check if some bulit-in categories need be removed
		boolean hasContext = false;
		boolean hasParameters = false;
		boolean hasVariables = false;
		boolean hasNativeObjects = false;
		boolean hasBirtObjects = false;
		boolean hasOperators = false;

		// check and create built-in categories
		if (filter(ExpressionFilter.CATEGORY, ExpressionFilter.CATEGORY_CONTEXT, filters)) {
			hasContext = true;
			clearSubTreeItem(contextItem);
			createContextCatagory();
		}
		if (filter(ExpressionFilter.CATEGORY, ExpressionFilter.CATEGORY_PARAMETERS, filters)) {
			hasParameters = true;
			if (parametersItem == null || parametersItem.isDisposed()) {
				// only create once
				createParamtersCategory();
			}
		}
		if (filter(ExpressionFilter.CATEGORY, ExpressionFilter.CATEGORY_VARIABLES, filters)) {
			hasVariables = true;
			createVariablesCategory();
		}

		if (filter(ExpressionFilter.CATEGORY, ExpressionFilter.CATEGORY_NATIVE_OBJECTS, filters)) {
			hasNativeObjects = true;
			if (nativeObejctsItem == null || nativeObejctsItem.isDisposed()) {
				// only create once
				createNativeObjectsCategory();
			}
		}
		if (filter(ExpressionFilter.CATEGORY, ExpressionFilter.CATEGORY_BIRT_OBJECTS, filters)) {
			hasBirtObjects = true;
			if (birtObjectsItem == null || birtObjectsItem.isDisposed()) {
				// only create once
				createBirtObjectsCategory();
			}
		}
		if (filter(ExpressionFilter.CATEGORY, ExpressionFilter.CATEGORY_OPERATORS, filters)) {
			hasOperators = true;
			if (operatorsItem == null || operatorsItem.isDisposed()) {
				// only create once
				createOperatorsCategory();
			}
		}

		if (!hasContext) {
			clearTreeItem(contextItem);
		}
		if (!hasParameters) {
			clearTreeItem(parametersItem);
		}
		if (!hasVariables) {
			clearTreeItem(variablesItem);
		}
		if (!hasNativeObjects) {
			clearTreeItem(nativeObejctsItem);
		}
		if (!hasBirtObjects) {
			clearTreeItem(birtObjectsItem);
		}
		if (!hasOperators) {
			clearTreeItem(operatorsItem);
		}

		clearDynamicItems();

		if (dynamicContextProviders != null) {
			updateDynamicItems(dynamicContextProviders);
		}

		restoreSelection();
	}

	protected void saveSelection(TreeItem selection) {
		Memento memento = ((Memento) viewerMemento.getChild(getElementType()));
		if (memento == null) {
			return;
		}

		MementoElement[] selectPath = createItemPath(selection);
		memento.getMementoElement().setAttribute(MementoElement.ATTRIBUTE_SELECTED, selectPath);
	}

	protected MementoElement[] createItemPath(TreeItem item) {
		MementoElement tempMemento = null;
		while (item.getParentItem() != null) {
			TreeItem parent = item.getParentItem();
			for (int i = 0; i < parent.getItemCount(); i++) {
				if (parent.getItem(i) == item) {
					MementoElement memento = new MementoElement(item.getText(), item.getText(),
							MementoElement.Type_Element);
					if (tempMemento != null)
						memento.addChild(tempMemento);
					tempMemento = memento;
					item = parent;
					break;
				}
			}
		}
		MementoElement memento = new MementoElement(item.getText(), item.getText(), MementoElement.Type_Element);
		if (tempMemento != null)
			memento.addChild(tempMemento);
		return getNodePath(memento);
	}

	public MementoElement[] getNodePath(MementoElement node) {
		ArrayList pathList = new ArrayList();
		MementoElement memento = node;
		pathList.add(node);// add root
		while (memento.getChildren().length > 0) {
			pathList.add(memento.getChild(0));
			memento = (MementoElement) memento.getChild(0);
		}
		MementoElement[] paths = new MementoElement[pathList.size()];
		pathList.toArray(paths);
		return paths;
	}

	private void restoreSelection() {
		Memento memento = ((Memento) viewerMemento.getChild(getElementType()));
		if (memento == null)
			return;

		expandTreeFromMemento(memento);
		Object obj = memento.getMementoElement().getAttribute(MementoElement.ATTRIBUTE_SELECTED);
		if (obj != null) {
			for (int i = 0; i < tree.getItemCount(); i++) {
				restoreSelectedMemento(tree.getItem(i), (MementoElement[]) obj);
			}
		}
	}

	private void restoreSelectedMemento(TreeItem root, MementoElement[] selectedPath) {
		if (selectedPath.length <= 0)
			return;

		for (int i = 0; i < selectedPath.length; i++) {
			MementoElement element = selectedPath[i];
			if (root.getText().equals(element.getValue())) {
				continue;
			}
			boolean flag = false;
			for (int j = 0; j < root.getItemCount(); j++) {
				if (root.getItem(j).getText().equals(element.getValue())) {
					root = root.getItem(j);
					flag = true;
					break;
				}
			}
			if (!flag)
				return;
		}
		tree.setSelection(root);
	}

	/**
	 * Filters the tree name, given the filter list.
	 * 
	 * @param treeName the tree name to be filtered.
	 * @param filters  the filter list.
	 * @return true if the tree name passes the filter list.
	 */
	private boolean filter(Object parent, Object child, List<ExpressionFilter> filters) {
		if (filters == null) {
			return true;
		}
		for (ExpressionFilter ft : filters) {
			if (ft != null && !ft.select(parent, child)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Create operators band.Must set Tree before execution.
	 * 
	 */
	protected void createOperatorsCategory() {
		assert tree != null;

		int idx = getIndex(birtObjectsItem, nativeObejctsItem, parametersItem, contextItem);

		operatorsItem = createTopTreeItem(tree, TREE_ITEM_OPERATORS, idx);

		TreeItem subItem = createSubFolderItem(operatorsItem, TREE_ITEM_ASSIGNMENT);
		createSubTreeItems(subItem, OPERATORS_ASSIGNMENT, IMAGE_OPERATOR);
		subItem = createSubFolderItem(operatorsItem, TREE_ITEM_COMPARISON);
		createSubTreeItems(subItem, OPERATORS_COMPARISON, IMAGE_OPERATOR);
		subItem = createSubFolderItem(operatorsItem, TREE_ITEM_COMPUTATIONAL);
		createSubTreeItems(subItem, OPERATORS_COMPUTATIONAL, IMAGE_OPERATOR);
		subItem = createSubFolderItem(operatorsItem, TREE_ITEM_LOGICAL);
		createSubTreeItems(subItem, OPERATORS_LOGICAL, IMAGE_OPERATOR);
	}

	/**
	 * Create native object band.Must set Tree before execution.
	 * 
	 */
	protected void createNativeObjectsCategory() {
		assert tree != null;

		int idx = getIndex(parametersItem, contextItem);

		nativeObejctsItem = createTopTreeItem(tree, TREE_ITEM_NATIVE_OBJECTS, idx);
		createObjects(nativeObejctsItem, OBJECTS_TYPE_NATIVE);
	}

	/**
	 * Create parameters band. Must set Tree before execution.
	 * 
	 */
	protected void createParamtersCategory() {
		assert tree != null;

		int idx = getIndex(contextItem);

		parametersItem = createTopTreeItem(tree, TREE_ITEM_PARAMETERS, idx);
		buildParameterTree();
	}

	private void buildParameterTree() {
		ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

		if (module == null) {
			return;
		}

		for (Iterator iterator = module.getParameters().iterator(); iterator.hasNext();) {
			ReportElementHandle handle = (ReportElementHandle) iterator.next();
			if (handle instanceof ParameterHandle) {
				createSubTreeItem(parametersItem, DEUtil.getDisplayLabel(handle, false),
						ReportPlatformUIImages.getImage(handle), DEUtil.getExpression(handle),
						((ParameterHandle) handle).getHelpText(), true);
			} else if (handle instanceof ParameterGroupHandle) {
				TreeItem groupItem = createSubTreeItem(parametersItem, DEUtil.getDisplayLabel(handle, false),
						ReportPlatformUIImages.getImage(handle), true);
				for (Iterator itor = ((ParameterGroupHandle) handle).getParameters().iterator(); itor.hasNext();) {
					ParameterHandle parameter = (ParameterHandle) itor.next();
					createSubTreeItem(groupItem, parameter.getDisplayLabel(), ReportPlatformUIImages.getImage(handle),
							DEUtil.getExpression(parameter), parameter.getDisplayLabel(), true);
				}
			}
		}
	}

	protected void createVariablesCategory() {
		if (variablesItem == null || variablesItem.isDisposed()) {
			int idx = getIndex(contextItem);
			variablesItem = createTopTreeItem(tree, TREE_ITEM_VARIABLES, idx);
		}
		buildVariableTree();
	}

	private void buildVariableTree() {
		clearSubTreeItem(variablesItem);

		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();

		if (handle instanceof ReportDesignHandle) {
			for (VariableElementHandle variableHandle : ((ReportDesignHandle) handle).getPageVariables()) {
				if (currentMethodName != null
						&& DesignChoiceConstants.VARIABLE_TYPE_PAGE.equals(variableHandle.getType())
						&& !(currentMethodName.equals("onPageStart") || currentMethodName.equals("onPageEnd")
								|| currentMethodName.equals("onRender")))
					continue;
				createSubTreeItem(variablesItem, DEUtil.getDisplayLabel(variableHandle, false),
						ReportPlatformUIImages.getImage(variableHandle), DEUtil.getExpression(variableHandle),
						variableHandle.getDisplayLabel(), true);
			}
		}

		restoreSelection();
	}

	/**
	 * Creates birt object tree. Must set Tree before execution.
	 * 
	 */
	protected void createBirtObjectsCategory() {
		assert tree != null;

		int idx = getIndex(nativeObejctsItem, parametersItem, contextItem);

		birtObjectsItem = createTopTreeItem(tree, TREE_ITEM_BIRT_OBJECTS, idx);
		createObjects(birtObjectsItem, OBJECTS_TYPE_BIRT);
	}

	// /**
	// * Creates a top tree item
	// *
	// * @param parent
	// * @param text
	// * @return tree item
	// */
	// private TreeItem createTopTreeItem( Tree parent, String text )
	// {
	// TreeItem item = new TreeItem( parent, SWT.NONE );
	// item.setText( text );
	// item.setImage( IMAGE_FOLDER );
	// item.setData( ITEM_DATA_KEY_TOOLTIP, "" );//$NON-NLS-1$
	// return item;
	// }

	private TreeItem createTopTreeItem(Tree parent, String text, int index) {
		TreeItem item = new TreeItem(parent, SWT.NONE, index);
		item.setText(text);
		item.setImage(IMAGE_FOLDER);
		item.setData(ITEM_DATA_KEY_TOOLTIP, "");//$NON-NLS-1$
		return item;
	}

	private TreeItem createSubTreeItem(TreeItem parent, String text, Image image, boolean isEnabled) {
		return createSubTreeItem(parent, text, image, null, text, isEnabled);
	}

	private TreeItem createSubTreeItem(TreeItem parent, String text, Image image, String textData, String toolTip,
			boolean isEnabled) {
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText(text);
		if (image != null) {
			item.setImage(image);
		}
		item.setData(ITEM_DATA_KEY_TOOLTIP, toolTip);
		item.setData(ITEM_DATA_KEY_TEXT, textData);
		item.setData(ITEM_DATA_KEY_ENABLED, Boolean.valueOf(isEnabled));
		return item;
	}

	private TreeItem createSubFolderItem(TreeItem parent, String text) {
		return createSubTreeItem(parent, text, IMAGE_FOLDER, true);
	}

	private TreeItem createSubFolderItem(TreeItem parent, IClassInfo classInfo) {
		return createSubTreeItem(parent, classInfo.getDisplayName(), IMAGE_FOLDER, null, classInfo.getToolTip(), true);
	}

	private TreeItem createSubFolderItem(TreeItem parent, IScriptFunctionCategory category) {
		String categoreName = getCategoryDisplayName(category);
		return createSubTreeItem(parent, categoreName, IMAGE_FOLDER, null, category.getDescription(), true);
	}

	private String getCategoryDisplayName(IScriptFunctionCategory category) {
		return category.getName() == null ? Messages.getString("ExpressionTreeSupport.Category.Global") //$NON-NLS-1$
				: category.getName();
	}

	private void createSubTreeItems(TreeItem parent, String[][] texts, Image image) {
		for (int i = 0; i < texts.length; i++) {
			createSubTreeItem(parent, texts[i][0], image, texts[i][0], texts[i][1], true);
		}
	}

	/**
	 * Adds mouse track listener.Must set Tree before execution.
	 * 
	 */
	public void addMouseTrackListener() {
		assert tree != null;
		tree.addMouseTrackListener(new MouseTrackAdapter() {

			public void mouseHover(MouseEvent event) {
				Widget widget = event.widget;
				if (widget == tree) {
					Point pt = new Point(event.x, event.y);
					TreeItem item = tree.getItem(pt);
					if (item == null)
						tree.setToolTipText("");//$NON-NLS-1$
					else {
						String text = (String) item.getData(ITEM_DATA_KEY_TOOLTIP);
						tree.setToolTipText(text);
					}
				}
			}
		});
	}

	/**
	 * Add double click behaviour. Must set Tree before execution.
	 * 
	 */
	public void addMouseListener() {
		assert tree != null;
		tree.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent event) {
				TreeItem[] selection = getTreeSelection();
				if (selection == null || selection.length <= 0)
					return;
				TreeItem item = selection[0];
				if (item != null) {
					Object obj = item.getData(ITEM_DATA_KEY_TEXT);
					Boolean isEnabled = (Boolean) item.getData(ITEM_DATA_KEY_ENABLED);
					if (obj != null && isEnabled.booleanValue()) {
						String text = (String) obj;
						insertText(text);
					}
				}
			}
		});
	}

	protected TreeItem[] getTreeSelection() {
		return tree.getSelection();
	}

	/**
	 * Adds drag support to tree..Must set tree before execution.
	 */
	public void addDragSupportToTree() {
		assert tree != null;
		DragSource dragSource = new DragSource(tree, DND.DROP_COPY | DND.DROP_MOVE);

		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {

			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = tree.getSelection();
				if (selection.length <= 0 || selection[0].getData(ITEM_DATA_KEY_TEXT) == null
						|| !((Boolean) selection[0].getData(ITEM_DATA_KEY_ENABLED)).booleanValue()) {
					event.doit = false;
					return;
				}
			}

			public void dragSetData(DragSourceEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					TreeItem[] selection = tree.getSelection();
					if (selection.length > 0) {
						event.data = selection[0].getData(ITEM_DATA_KEY_TEXT);
					}
				}
			}
		});
	}

	/**
	 * Insert a text string into the text area
	 * 
	 * @param text
	 */
	protected void insertText(String text) {
		StyledText textWidget = expressionViewer.getTextWidget();
		if (!textWidget.isEnabled()) {
			return;
		}
		int selectionStart = textWidget.getSelection().x;
		if (text.equalsIgnoreCase("x++")) //$NON-NLS-1$
		{
			text = textWidget.getSelectionText() + "++";//$NON-NLS-1$
		} else if (text.equalsIgnoreCase("x--"))//$NON-NLS-1$
		{
			text = textWidget.getSelectionText() + "--";//$NON-NLS-1$
		} else if (text.equalsIgnoreCase("++x"))//$NON-NLS-1$
		{
			text = "++" + textWidget.getSelectionText();//$NON-NLS-1$
		} else if (text.equalsIgnoreCase("--x"))//$NON-NLS-1$
		{
			text = "--" + textWidget.getSelectionText();//$NON-NLS-1$
		}

		textWidget.insert(text);
		textWidget.setSelection(selectionStart + text.length());
		textWidget.setFocus();

		if (text.endsWith("()")) //$NON-NLS-1$
		{
			textWidget.setCaretOffset(textWidget.getCaretOffset() - 1); // Move
		}
	}

	/**
	 * Adds drop support to viewer.Must set viewer before execution.
	 * 
	 */
	public void addDropSupportToViewer() {
		assert expressionViewer != null;
		if (dropTarget == null || dropTarget.isDisposed()) {
			final StyledText text = expressionViewer.getTextWidget();

			// Doesn't add again if a drop target has been created in the
			// viewer.
			if (text.getData("DropTarget") != null) //$NON-NLS-1$
			{
				return;
			}

			dropTarget = new DropTarget(text, DND.DROP_COPY | DND.DROP_DEFAULT);
			dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
			dropTargetAdapter = new DropTargetAdapter() {

				public void dragEnter(DropTargetEvent event) {
					text.setFocus();
					if (event.detail == DND.DROP_DEFAULT)
						event.detail = DND.DROP_COPY;
					if (event.detail != DND.DROP_COPY)
						event.detail = DND.DROP_NONE;
				}

				public void dragOver(DropTargetEvent event) {
					event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_INSERT_BEFORE;
				}

				public void dragOperationChanged(DropTargetEvent event) {
					dragEnter(event);
				}

				public void drop(DropTargetEvent event) {
					if (event.data instanceof String)
						insertText((String) event.data);
				}
			};
			dropTarget.addDropListener(dropTargetAdapter);
		}
	}

	public void removeDropSupportToViewer() {
		if (dropTarget != null && !dropTarget.isDisposed()) {
			if (dropTargetAdapter != null) {
				dropTarget.removeDropListener(dropTargetAdapter);
				dropTargetAdapter = null;
			}
			dropTarget.dispose();
			dropTarget = null;
		}
	}

	/**
	 * Sets the tree model.
	 * 
	 * @param tree
	 */
	public void setTree(final Tree tree) {
		this.tree = tree;

		if ((viewerMemento = (Memento) builder.getRootMemento().getChild("ExpressionTreeSupport")) == null) {
			viewerMemento = (Memento) builder.getRootMemento().createChild("ExpressionTreeSupport",
					MementoElement.Type_Viewer);
		}

		SelectionAdapter treeSelectionListener = new SelectionAdapter() {

			public void widgetDefaultSelected(SelectionEvent e) {
				saveSelection((TreeItem) e.item);
			}
		};

		tree.addSelectionListener(treeSelectionListener);

		tree.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent event) {
				// only activate if there is a cell editor
				Point pt = new Point(event.x, event.y);
				TreeItem item = tree.getItem(pt);
				if (item != null) {
					saveSelection(item);
				}
			}
		});

		tree.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (tree.getSelectionCount() > 0)
					saveSelection(tree.getSelection()[0]);
			}
		});

		TreeListener treeListener = new TreeListener() {

			public void treeCollapsed(TreeEvent e) {
				if (e.item instanceof TreeItem) {
					TreeItem item = (TreeItem) e.item;

					MementoElement[] path = createItemPath(item);
					removeNode(((Memento) viewerMemento.getChild(getElementType())), path);

					getTree().setSelection(item);
					saveSelection(item);
				}
			}

			public void treeExpanded(TreeEvent e) {
				if (e.item instanceof TreeItem) {
					TreeItem item = (TreeItem) e.item;

					MementoElement[] path = createItemPath(item);
					addNode(((Memento) viewerMemento.getChild(getElementType())), path);

					getTree().setSelection(item);
					saveSelection(item);
				}

			}

		};

		tree.addTreeListener(treeListener);
	}

	public boolean addNode(Memento element, MementoElement[] nodePath) {
		if (element == null) {
			return false;
		}

		if (nodePath != null && nodePath.length > 0) {
			MementoElement memento = element.getMementoElement();
			// if ( !memento.equals( nodePath[0] ) )
			// return false;
			for (int i = 0; i < nodePath.length; i++) {
				MementoElement child = getChild(memento, nodePath[i]);
				if (child != null)
					memento = child;
				else {
					memento.addChild(nodePath[i]);
					return true;
				}
			}
			return true;
		}
		return false;
	}

	public boolean removeNode(Memento element, MementoElement[] nodePath) {
		if (element == null) {
			return false;
		}

		if (nodePath != null && nodePath.length > 0) {
			MementoElement memento = element.getMementoElement();
			// if ( !memento.equals( nodePath[0] ) )
			// return false;
			for (int i = 0; i < nodePath.length; i++) {
				MementoElement child = getChild(memento, nodePath[i]);
				if (child != null)
					memento = child;
				else
					return false;
			}
			memento.getParent().removeChild(memento);
			return true;
		}
		return false;
	}

	private void expandTreeFromMemento(Memento memento) {
		if (tree.getItemCount() == 0 || memento == null)
			return;
		for (int i = 0; i < tree.getItemCount(); i++) {
			TreeItem root = tree.getItem(i);
			for (int j = 0; j < memento.getMementoElement().getChildren().length; j++) {
				MementoElement child = memento.getMementoElement().getChildren()[j];
				restoreExpandedMemento(root, child);
			}
		}
	}

	private void restoreExpandedMemento(TreeItem root, MementoElement memento) {
		if (memento.getKey().equals(root.getText())) {
			if (root.getItemCount() > 0) {
				if (!root.getExpanded())
					root.setExpanded(true);
				MementoElement[] children = memento.getChildren();
				for (int i = 0; i < children.length; i++) {
					MementoElement child = children[i];
					String key = child.getValue().toString();

					for (int j = 0; j < root.getItemCount(); j++) {
						TreeItem item = root.getItem(j);
						if (item.getText().equals(key)) {
							restoreExpandedMemento(item, child);
							break;
						}
					}
				}
			}
		}
	}

	private MementoElement getChild(MementoElement parent, MementoElement key) {
		MementoElement[] children = parent.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i].equals(key))
				return children[i];
		}
		return null;
	};

	protected Tree getTree() {
		return tree;
	}

	/**
	 * Sets the viewer to use.
	 * 
	 * @param expressionViewer
	 */
	public void setExpressionViewer(SourceViewer expressionViewer) {
		this.expressionViewer = expressionViewer;
	}

	protected SourceViewer getExpressionViewer() {
		return expressionViewer;
	}

	/**
	 * Gets an icon image by the key in plugin.properties
	 * 
	 * @param id
	 * @return image
	 */
	private static Image getIconImage(String id) {
		return ReportPlatformUIImages.getImage(id);
	}

	private void createObjects(TreeItem topItem, String objectType) {
		for (Iterator itor = DEUtil.getClasses().iterator(); itor.hasNext();) {
			IClassInfo classInfo = (IClassInfo) itor.next();
			if (classInfo.isNative() && OBJECTS_TYPE_BIRT.equals(objectType)
					|| !classInfo.isNative() && OBJECTS_TYPE_NATIVE.equals(objectType)
					|| classInfo.getName().equals("Total")) //$NON-NLS-1$
			{
				continue;
			}
			TreeItem subItem = createSubFolderItem(topItem, classInfo);
			Image globalImage = null;
			if (isGlobal(classInfo.getName())) {
				globalImage = IMAGE_GOLBAL;
			}

			ArrayList<Object> childrenList = new ArrayList<Object>();
			IMemberInfo[] members = (IMemberInfo[]) DEUtil.getMembers(classInfo).toArray(new IMemberInfo[0]);
			for (int i = 0; i < members.length; i++) {
				childrenList.add(new ILocalizableInfo[] { classInfo, members[i] });
			}
			List methodList = new ArrayList();
			methodList.addAll(DEUtil.getMethods(classInfo, true));
			methodList.addAll(AggregationUtil.getMethods(classInfo));

			IMethodInfo[] methods = (IMethodInfo[]) methodList.toArray(new IMethodInfo[0]);
			for (int i = 0; i < methods.length; i++) {
				IMethodInfo mi = methods[i];
				processMethods(classInfo, mi, childrenList);
			}

			ILocalizableInfo[][] children = childrenList.toArray(new ILocalizableInfo[0][]);
			sortLocalizableInfo(children);

			for (int i = 0; i < children.length; i++) {
				Object obj = children[i];
				createSubTreeItem(subItem, getDisplayText(obj), globalImage == null ? getImage(obj) : globalImage,
						getInsertText(obj), getTooltipText(obj), true);
			}
		}

		if (OBJECTS_TYPE_BIRT.equals(objectType)) {
			try {
				IScriptFunctionCategory[] categorys = FunctionProvider.getCategories();
				Arrays.sort(categorys, new Comparator<IScriptFunctionCategory>() {

					public int compare(IScriptFunctionCategory o1, IScriptFunctionCategory o2) {
						return getCategoryDisplayName(o1).compareTo(getCategoryDisplayName(o2));
					}
				});
				if (categorys != null) {
					for (int i = 0; i < categorys.length; i++) {
						TreeItem subItem = createSubFolderItem(topItem, categorys[i]);
						IScriptFunction[] functions = categorys[i].getFunctions();
						Arrays.sort(functions, new Comparator<IScriptFunction>() {

							public int compare(IScriptFunction o1, IScriptFunction o2) {
								return getFunctionDisplayText(o1).compareTo(getFunctionDisplayText(o2));
							}
						});
						if (functions != null) {
							for (int j = 0; j < functions.length; j++) {
								Image image = null;

								if (functions[j].isStatic()) {
									image = IMAGE_STATIC_METHOD;
								} else {
									image = IMAGE_METHOD;
								}
								createSubTreeItem(subItem, getFunctionDisplayText(functions[j]), image,
										getFunctionExpression(categorys[i], functions[j]),
										functions[j].getDescription(), true);
							}
						}
					}
				}
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	private void sortLocalizableInfo(ILocalizableInfo[][] infos) {
		Arrays.sort(infos, new Comparator<ILocalizableInfo[]>() {

			private int computeWeight(ILocalizableInfo obj) {
				if (obj instanceof IMemberInfo) {
					return ((IMemberInfo) obj).isStatic() ? 0 : 2;
				} else if (obj instanceof IMethodInfo) {
					if (((IMethodInfo) obj).isConstructor()) {
						return 3;
					}
					return ((IMethodInfo) obj).isStatic() ? 1 : 4;
				}

				return 4;
			}

			public int compare(ILocalizableInfo[] o1, ILocalizableInfo[] o2) {
				ILocalizableInfo info1 = o1[1];
				ILocalizableInfo info2 = o2[1];

				int w1 = computeWeight(info1);
				int w2 = computeWeight(info2);

				if (w1 != w2) {
					return w1 < w2 ? -1 : 1;
				}
				return Collator.getInstance().compare(getDisplayText(o1), getDisplayText(o2));
			}
		});
	}

	private Image getImage(Object element) {
		if (element instanceof ILocalizableInfo[]) {
			ILocalizableInfo info = ((ILocalizableInfo[]) element)[1];
			if (info instanceof IMethodInfo) {
				if (((IMethodInfo) info).isStatic()) {
					return IMAGE_STATIC_METHOD;
				} else if (((IMethodInfo) info).isConstructor()) {
					return IMAGE_CONSTRUCTOR;
				}
				return IMAGE_METHOD;
			}
			if (info instanceof IMemberInfo) {
				if (((IMemberInfo) info).isStatic()) {
					return IMAGE_STATIC_MEMBER;
				}
				return IMAGE_MEMBER;
			}
		}
		return null;
	}

	private String getInsertText(Object element) {
		if (element instanceof VariableElementHandle) {
			return ((VariableElementHandle) element).getVariableName();
		}
		if (element instanceof ILocalizableInfo[]) {
			IClassInfo classInfo = (IClassInfo) ((ILocalizableInfo[]) element)[0];
			ILocalizableInfo info = ((ILocalizableInfo[]) element)[1];
			StringBuffer insertText = new StringBuffer();
			if (info instanceof IMemberInfo) {
				IMemberInfo memberInfo = (IMemberInfo) info;
				if (memberInfo.isStatic()) {
					insertText.append(classInfo.getName() + "."); //$NON-NLS-1$
				}
				insertText.append(memberInfo.getName());
			} else if (info instanceof IMethodInfo) {
				IMethodInfo methodInfo = (IMethodInfo) info;
				if (methodInfo.isStatic()) {
					insertText.append(classInfo.getName() + "."); //$NON-NLS-1$
				} else if (methodInfo.isConstructor()) {
					insertText.append("new "); //$NON-NLS-1$
				}
				insertText.append(methodInfo.getName());
				insertText.append("()"); //$NON-NLS-1$
			}
			return insertText.toString();
		}
		return null;
	}

	private String getTooltipText(Object element) {
		if (element instanceof ILocalizableInfo[]) {
			ILocalizableInfo info = ((ILocalizableInfo[]) element)[1];
			String tooltip = null;
			if (info instanceof IMemberInfo) {
				tooltip = ((IMemberInfo) info).getToolTip();
			} else if (info instanceof IMethodInfo) {
				tooltip = ((IMethodInfo) info).getToolTip();
			}
			return tooltip == null ? "" : tooltip; //$NON-NLS-1$
		}
		return null;
	}

	protected String getDisplayText(Object element) {
		if (element instanceof ILocalizableInfo[]) {
			// including class info,method info and member info
			ILocalizableInfo info = ((ILocalizableInfo[]) element)[1];
			StringBuffer displayText = new StringBuffer(info.getName());
			if (info instanceof IMethodInfo) {
				IMethodInfo method = (IMethodInfo) info;
				displayText.append("("); //$NON-NLS-1$

				int argIndex = (((ILocalizableInfo[]) element).length > 2)
						? (((IIndexInfo) ((ILocalizableInfo[]) element)[2]).getIndex())
						: 0;
				int idx = -1;

				Iterator argumentListIter = method.argumentListIterator();
				while (argumentListIter.hasNext()) {
					IArgumentInfoList arguments = (IArgumentInfoList) argumentListIter.next();

					idx++;

					if (idx < argIndex) {
						continue;
					}

					boolean isFirst = true;

					for (Iterator iter = arguments.argumentsIterator(); iter.hasNext();) {
						IArgumentInfo argInfo = (IArgumentInfo) iter.next();
						if (!isFirst) {
							displayText.append(", "); //$NON-NLS-1$
						}
						isFirst = false;

						if (argInfo.getType() != null && argInfo.getType().length() > 0) {
							displayText.append(argInfo.getType() + " " //$NON-NLS-1$
									+ argInfo.getName());
						} else
							displayText.append(argInfo.getName());
					}

					break;
				}

				displayText.append(")"); //$NON-NLS-1$

				if (!method.isConstructor()) {
					displayText.append(" : "); //$NON-NLS-1$
					String returnType = method.getReturnType();
					if (returnType == null || returnType.length() == 0) {
						returnType = "void"; //$NON-NLS-1$
					}
					displayText.append(returnType);
				}

			} else if (info instanceof IMemberInfo) {
				String dataType = ((IMemberInfo) info).getDataType();
				if (dataType != null && dataType.length() > 0) {
					displayText.append(" : "); //$NON-NLS-1$
					displayText.append(dataType);
				}
			}
			return displayText.toString();
		}
		return null;
	}

	private boolean isGlobal(String name) {
		// TODO global validation is hard coded
		return name != null && name.startsWith("Global"); //$NON-NLS-1$
	}

	private String getFunctionDisplayText(IScriptFunction function) {
		String functionStart = function.isConstructor() ? "new " : ""; //$NON-NLS-1$//$NON-NLS-2$
		StringBuffer displayText = new StringBuffer(functionStart);
		displayText.append(function.getName());
		IScriptFunctionArgument[] arguments = function.getArguments();

		displayText.append("("); //$NON-NLS-1$

		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				displayText.append(arguments[i].getName());
				if (i < arguments.length - 1)
					displayText.append(", ");//$NON-NLS-1$
			}
		}
		displayText.append(")"); //$NON-NLS-1$
		return displayText.toString();
	}

	private String getFunctionExpression(IScriptFunctionCategory category, IScriptFunction function) {
		String functionStart = function.isConstructor() ? "new " : ""; //$NON-NLS-1$//$NON-NLS-2$
		StringBuffer textData = new StringBuffer(functionStart);
		if (function.isStatic()) {
			if (category.getName() != null) {
				textData.append(category.getName() + "."); //$NON-NLS-1$
			}
		}
		textData.append(function.getName() + "()"); //$NON-NLS-1$
		return textData.toString();
	}

	protected void createContextCatagory() {
		assert tree != null;

		if (contextItem == null || contextItem.isDisposed()) {
			contextItem = createTopTreeItem(tree, TREE_ITEM_CONTEXT, 0);
		}
		createContextObjects(currentMethodName);
	}

	/**
	 * Creates context objects tree. Context ojects tree is used in JS editor
	 * palette, which displays current object method's arguments.
	 */
	protected void createContextObjects(String methodName) {
		if (currentEditObject != null && methodName != null) {
			DesignElementHandle handle = (DesignElementHandle) currentEditObject;
			List args = DEUtil.getDesignElementMethodArgumentsInfo(handle, methodName);
			for (Iterator iter = args.iterator(); iter.hasNext();) {
				String argName = ((IArgumentInfo) iter.next()).getName();
				createSubTreeItem(contextItem, argName, IMAGE_METHOD, argName, "", //$NON-NLS-1$
						true);
			}
		}
	}

	public void setCurrentEditObject(Object obj) {
		this.currentEditObject = obj;
	}

	private void clearTreeItem(TreeItem treeItem) {
		if (treeItem == null || treeItem.isDisposed()) {
			return;
		}
		treeItem.dispose();
	}

	private void clearSubTreeItem(TreeItem treeItem) {
		if (treeItem == null || treeItem.isDisposed()) {
			return;
		}
		TreeItem[] items = treeItem.getItems();
		for (int i = 0; i < items.length; i++) {
			clearTreeItem(items[i]);
		}
	}

	private void clearDynamicItems() {
		if (dynamicItems != null) {
			for (TreeItem ti : dynamicItems) {
				clearTreeItem(ti);
			}

			dynamicItems.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 * 
	 * Listen to JS editor method change.
	 */

	private int eventFireNum = 0;
	private int execNum = 0;

	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection != null) {
			Object[] sel = ((IStructuredSelection) selection).toArray();
			if (sel.length == 1) {
				if (sel[0] instanceof IPropertyDefn) {
					final IPropertyDefn elePropDefn = (IPropertyDefn) sel[0];

					currentMethodName = elePropDefn.getName();

					eventFireNum++;

					Display.getDefault().timerExec(100, new Runnable() {

						public void run() {
							execNum++;

							if (elePropDefn.getName().equals(currentMethodName)) {
								if (execNum == eventFireNum) {
									createExpressionTree();
								}
							}
							if (execNum >= eventFireNum) {
								execNum = 0;
								eventFireNum = 0;
							}
						}
					});
				}
			}
		}
	}

	private void updateDynamicItems(List<IExpressionProvider> providers) {
		for (IExpressionProvider exprProvider : providers) {
			if (exprProvider != null) {
				createDynamicCategory(exprProvider);
			}
		}
	}

	private void createDynamicCategory(IExpressionProvider provider) {
		Object[] cats = provider.getCategory();

		if (cats != null) {
			for (Object cat : cats) {
				TreeItem ti = createTopTreeItem(tree, cat, provider);

				if (dynamicItems == null) {
					dynamicItems = new ArrayList<TreeItem>();
				}

				dynamicItems.add(ti);

				if (provider.hasChildren(cat)) {
					createDynamicChildern(ti, cat, provider);
				}
			}
		}
	}

	private void createDynamicChildern(TreeItem parent, Object element, IExpressionProvider provider) {
		Object[] children = provider.getChildren(element);

		if (children != null) {
			for (Object child : children) {
				if (provider.hasChildren(child)) {
					TreeItem ti = createSubFolderItem(parent, child, provider);

					createDynamicChildern(ti, child, provider);
				} else {
					createSubTreeItem(parent, child, provider);
				}
			}
		}
	}

	private TreeItem createTopTreeItem(Tree tree, Object element, IExpressionProvider provider) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(provider.getDisplayText(element));
		item.setImage(provider.getImage(element));
		item.setData(ITEM_DATA_KEY_TOOLTIP, provider.getTooltipText(element));
		return item;
	}

	private TreeItem createSubFolderItem(TreeItem parent, Object element, IExpressionProvider provider) {
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText(provider.getDisplayText(element));
		item.setImage(provider.getImage(element));
		item.setData(ITEM_DATA_KEY_TOOLTIP, provider.getTooltipText(element));
		return item;
	}

	private TreeItem createSubTreeItem(TreeItem parent, Object element, IExpressionProvider provider) {
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText(provider.getDisplayText(element));
		item.setImage(provider.getImage(element));
		item.setData(ITEM_DATA_KEY_TOOLTIP, provider.getTooltipText(element));
		item.setData(ITEM_DATA_KEY_TEXT, provider.getInsertText(element));
		item.setData(ITEM_DATA_KEY_ENABLED, Boolean.TRUE);
		return item;
	}

	public void updateParametersTree() {
		if (parametersItem != null && !parametersItem.isDisposed()) {
			clearSubTreeItem(parametersItem);
			buildParameterTree();
			restoreSelection();
		}
	}

	private void processMethods(IClassInfo classInfo, IMethodInfo mi, List childrenList) {
		Iterator alitr = mi.argumentListIterator();

		int idx = 0;
		if (alitr == null) {
			childrenList.add(new ILocalizableInfo[] { classInfo, mi });
		} else {
			while (alitr.hasNext()) {
				alitr.next();

				childrenList.add(new ILocalizableInfo[] { classInfo, mi, new IIndexInfo(idx++) });
			}
		}
	}

	/**
	 * Calculates the first available position according to the given item list.
	 */
	private int getIndex(TreeItem... items) {
		if (items != null) {
			for (TreeItem ti : items) {
				if (ti != null && !ti.isDisposed()) {
					return tree.indexOf(ti) + 1;
				}
			}
		}

		return 0;
	}
}
