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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.eclipse.birt.report.data.adapter.api.LinkedDataSetUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.data.ui.aggregation.AggregationUtil;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IIndexInfo;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.expressions.IExpressionFilterSupport;
import org.eclipse.birt.report.designer.ui.expressions.ISortableExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

/**
 * The default implementation of IExpressionProvider. <br>
 * <p>
 * NOTE: This class should not be extended for those expression provider user
 * extensions. Instead, they should extend
 * <code>org.eclipse.birt.report.designer.ui.expressions.AbstractExpressionProvider</code>
 * 
 * @see org.eclipse.birt.report.designer.ui.expressions.AbstractExpressionProvider
 */
public class ExpressionProvider implements ISortableExpressionProvider, IExpressionFilterSupport {

	/**
	 * Expression
	 */
	private static class Expression {

		/**
		 * The tooltip of the operator
		 */
		public String tooltip;
		/**
		 * The symbol of the operator
		 */
		public String symbol;

		/**
		 * The text to insert into the source viewer
		 */
		public String insertString;

		Expression(String symbol, String insertString, String tooltip) {
			assert (symbol != null);
			this.symbol = symbol;
			if (insertString == null) {
				this.insertString = symbol;
			} else {
				this.insertString = insertString;
			}
			this.tooltip = tooltip;
		}
	}

	/** Arithmetic operators and their descriptions */
	protected static final Operator[] OPERATORS_ASSIGNMENT = new Operator[] {
			new Operator("=", Messages.getString("ExpressionProvider.Operator.Assign")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("+=", Messages.getString("ExpressionProvider.Operator.AddTo")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("-=", Messages.getString("ExpressionProvider.Operator.SubFrom")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("*=", Messages.getString("ExpressionProvider.Operator.MultTo")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("/=", Messages.getString("ExpressionProvider.Operator.DividingFrom")), //$NON-NLS-1$ //$NON-NLS-2$
	};

	/** Comparison operators and their descriptions */
	protected static Operator[] OPERATORS_COMPARISON = new Operator[] {
			new Operator("==", Messages.getString("ExpressionProvider.Operator.Equals")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("<", Messages.getString("ExpressionProvider.Operator.Less")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("<=", Messages.getString("ExpressionProvider.Operator.LessEqual")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("!=", Messages.getString("ExpressionProvider.Operator.NotEqual")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator(">", Messages.getString("ExpressionProvider.Operator.Greater")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator(">=", Messages.getString("ExpressionProvider.Operator.GreaterEquals")), //$NON-NLS-1$ //$NON-NLS-2$

	};

	/** Computational operators and their descriptions */
	protected static final Operator[] OPERATORS_COMPUTATIONAL = new Operator[] {
			new Operator("+", Messages.getString("ExpressionProvider.Operator.Add")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("-", Messages.getString("ExpressionProvider.Operator.Sub")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("*", Messages.getString("ExpressionProvider.Operator.Mult")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("/", Messages.getString("ExpressionProvider.Operator.Divides")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("++ ", Messages.getString("ExpressionProvider.Operator.Inc")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("-- ", Messages.getString("ExpressionProvider.Operator.Dec")), //$NON-NLS-1$ //$NON-NLS-2$
	};

	/** Logical operators and their descriptions */
	protected static final Operator[] OPERATORS_LOGICAL = new Operator[] {
			new Operator("&&", Messages.getString("ExpressionProvider.Operator.And")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("||", Messages.getString("ExpressionProvider.Operator.Or")), //$NON-NLS-1$ //$NON-NLS-2$

	};

	/** Default operators on the operator bar */
	protected static final Operator[] OPERATORS_ON_BAR = new Operator[] { OPERATORS_COMPUTATIONAL[0],
			OPERATORS_COMPUTATIONAL[1], OPERATORS_COMPUTATIONAL[2], OPERATORS_COMPUTATIONAL[3], OPERATOR_SEPARATOR,
			new Operator("!", Messages.getString("ExpressionProvider.Operator.Not")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("=", Messages.getString("ExpressionProvider.Operator.Equals")), //$NON-NLS-1$ //$NON-NLS-2$
			OPERATORS_COMPARISON[1], OPERATORS_COMPARISON[4], OPERATOR_SEPARATOR,
			new Operator("&", Messages.getString("ExpressionProvider.Operator.BitAnd")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("|", Messages.getString("ExpressionProvider.Operator.BitOr")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator("(", Messages.getString("ExpressionProvider.Operator.LeftBracket")), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator(")", Messages.getString("ExpressionProvider.Operator.RightBracket")),//$NON-NLS-1$ //$NON-NLS-2$
	};

	protected static final String DISPLAY_TEXT_ASSIGNMENT = Messages
			.getString("ExpressionProvider.Operators.Assignment"); //$NON-NLS-1$
	protected static final String DISPLAY_TEXT_COMPARISON = Messages
			.getString("ExpressionProvider.Operators.Comparison"); //$NON-NLS-1$
	protected static final String DISPLAY_TEXT_COMPUTATIONAL = Messages
			.getString("ExpressionProvider.Operators.Computational"); //$NON-NLS-1$
	protected static final String DISPLAY_TEXT_LOGICAL = Messages.getString("ExpressionProvider.Operators.Logical"); //$NON-NLS-1$

	protected static final Image IMAGE_OPERATOR = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_OPERATOR);
	protected static final Image IMAGE_COLUMN = getIconImage(IReportGraphicConstants.ICON_DATA_COLUMN);
	protected static final Image IMAGE_CONSTRUCTOR = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_CONSTRUCTOP);
	protected static final Image IMAGE_METHOD = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_METHOD);
	protected static final Image IMAGE_STATIC_METHOD = getIconImage(
			IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD);
	protected static final Image IMAGE_MEMBER = getIconImage(IReportGraphicConstants.ICON_EXPRESSION_MEMBER);
	protected static final Image IMAGE_STATIC_MEMBER = getIconImage(
			IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER);
	protected static final Image IMAGE_LEVEL_ATTRI = getIconImage(IReportGraphicConstants.ICON_LEVEL_ATTRI);

	public static final String DATASETS = Messages.getString("ExpressionProvider.Category.DataSets"); //$NON-NLS-1$
	public static final String OPERATORS = Messages.getString("ExpressionProvider.Category.Operators"); //$NON-NLS-1$
	public static final String COLUMN_BINDINGS = Messages.getString("ExpressionProvider.Category.ColumnBinding"); //$NON-NLS-1$
	public static final String CURRENT_CUBE = Messages.getString("ExpressionProvider.Category.DataCubes"); //$NON-NLS-1$
	public static final String AVAILABLE_MEASURES = Messages.getString("ExpressionProvider.Category.AvailableMeasures"); //$NON-NLS-1$
	public static final String MEASURE = "Measure";

	// public static final String DATASETS = Messages.getString(
	// "ExpressionProvider.Category.DataSets" ); //$NON-NLS-1$
	public static final String PARAMETERS = Messages.getString("ExpressionProvider.Category.Parameters"); //$NON-NLS-1$
	public static final String VARIABLES = Messages.getString("ExpressionProvider.Variables"); //$NON-NLS-1$
	public static final String VARIABLES_REPORT = Messages.getString("ExpressionProvider.Variables.Report"); //$NON-NLS-1$
	public static final String VARIABLES_PAGE = Messages.getString("ExpressionProvider.Variables.Page"); //$NON-NLS-1$
	public static final String NATIVE_OBJECTS = Messages.getString("ExpressionProvider.Category.NativeObjects");//$NON-NLS-1$
	public static final String BIRT_OBJECTS = Messages.getString("ExpressionProvider.Category.BirtObjects");//$NON-NLS-1$

	protected static final String ALL = Messages.getString("ExpressionProvider.Label.All"); //$NON-NLS-1$

	private static final String TOOLTIP_BINDING_PREFIX = Messages.getString("ExpressionProvider.Tooltip.ColumnBinding"); //$NON-NLS-1$

	protected DesignElementHandle elementHandle;

	protected IExpressionProvider adapterProvider;

	protected AlphabeticallyComparator innerComparator = new AlphabeticallyComparator();

	protected List<ExpressionFilter> filterList;

	private boolean includeSelf;

	/**
	 * Create a new expression provider with the current module
	 */
	public ExpressionProvider() {
		this(null);
	}

	/**
	 * Create a new expression provider with the given element
	 * 
	 * @param handle the handle of the element
	 */
	public ExpressionProvider(DesignElementHandle handle) {
		this(handle, true);
	}

	/**
	 * Create a new expression provider with the given element
	 * 
	 * @param handle the handle of the element
	 */
	public ExpressionProvider(DesignElementHandle handle, boolean includeSelf) {
		if (handle == null) {
			elementHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		} else {
			elementHandle = handle;
		}
		this.includeSelf = includeSelf;

		initAdapterProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getOperators
	 * ()
	 */
	public Operator[] getOperators() {
		if (adapterProvider != null) {
			Operator[] cats = adapterProvider.getOperators();

			if (cats != null) {
				Operator[] rt = new Operator[OPERATORS_ON_BAR.length + cats.length];

				System.arraycopy(OPERATORS_ON_BAR, 0, rt, 0, OPERATORS_ON_BAR.length);
				System.arraycopy(cats, 0, rt, OPERATORS_ON_BAR.length, cats.length);

				return rt;
			}
		}

		return OPERATORS_ON_BAR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getCategory
	 * ()
	 */
	public Object[] getCategory() {
		Object[] category = getCategoryList().toArray();
		if (filterList != null && !filterList.isEmpty()) {
			for (Iterator<ExpressionFilter> iter = filterList.iterator(); iter.hasNext();) {
				ExpressionFilter ft = iter.next();

				category = ft.filter(ExpressionFilter.CATEGORY, category);
			}
		}
		return category;
	}

	protected List<Object> getCategoryList() {
		ArrayList<Object> categoryList = new ArrayList<Object>(5);
		if (getChildren(COLUMN_BINDINGS).length > 0) {
			categoryList.add(COLUMN_BINDINGS);
		}
		if (elementHandle instanceof ReportItemHandle && ((ReportItemHandle) elementHandle).getCube() != null
				&& !LinkedDataSetUtil.bindToLinkedDataSet((ReportItemHandle) elementHandle)) {
			categoryList.add(CURRENT_CUBE);
		}
		if (elementHandle.getModuleHandle().getParameters() != null
				&& elementHandle.getModuleHandle().getParameters().getCount() != 0) {
			categoryList.add(PARAMETERS);
		}
		categoryList.add(NATIVE_OBJECTS);
		categoryList.add(BIRT_OBJECTS);
		categoryList.add(OPERATORS);
		if (elementHandle.getModuleHandle() instanceof ReportDesignHandle
				&& ((ReportDesignHandle) elementHandle.getModuleHandle()).getPageVariables().size() > 0) {
			for (VariableElementHandle veh : ((ReportDesignHandle) elementHandle.getModuleHandle())
					.getPageVariables()) {
				if (DesignChoiceConstants.VARIABLE_TYPE_REPORT.equals(veh.getType())) {
					categoryList.add(VARIABLES);
					break;
				}
			}
		}

		if (adapterProvider != null) {
			Object[] cats = adapterProvider.getCategory();

			if (cats != null) {
				categoryList.addAll(Arrays.asList(cats));
			}
		}

		return categoryList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.expressions.ISortableExpressionProvider
	 * #getSortedChildren(java.lang.Object)
	 */
	public Object[] getSortedChildren(Object parentElement) {
		return sortChildren(parentElement, getChildren(parentElement));
	}

	protected Object[] sortElements(Object[] children) {
		TreeMap<String, Object> map = new TreeMap<String, Object>(innerComparator);

		for (Object obj : children) {
			map.put(getDisplayText(obj), obj);
		}

		return map.values().toArray(new Object[map.size()]);
	}

	protected int innerCompare(Object o1, Object o2) {
		return innerComparator.compare(getDisplayText(o1), getDisplayText(o2));
	}

	protected Object[] sortChildren(Object parent, Object[] children) {
		if (children == null) {
			return null;
		}

		if (parent instanceof String) {
			if (ALL.equals(parent)) {
				// sort for all parameters
				return sortElements(children);
			} else {
				// children for category, return original list
				return children;
			}
		} else if (parent instanceof IClassInfo) {
			Object[] result = new Object[children.length];
			System.arraycopy(children, 0, result, 0, children.length);

			Arrays.sort(result, new Comparator<Object>() {

				private int computeWeight(Object obj) {
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

				public int compare(Object o1, Object o2) {
					if (o1 instanceof ILocalizableInfo[] && o2 instanceof ILocalizableInfo[]) {
						ILocalizableInfo info1 = ((ILocalizableInfo[]) o1)[1];
						ILocalizableInfo info2 = ((ILocalizableInfo[]) o2)[1];

						int w1 = computeWeight(info1);
						int w2 = computeWeight(info2);

						if (w1 != w2) {
							return w1 < w2 ? -1 : 1;
						}
					}
					return innerCompare(o1, o2);
				}
			});

			return result;
		} else if (parent instanceof ReportItemHandle || parent instanceof GroupHandle) {
			Object[] result = new Object[children.length];
			System.arraycopy(children, 0, result, 0, children.length);

			Arrays.sort(result, new Comparator<Object>() {

				private int computeWeight(Object obj) {
					if (obj instanceof ComputedColumnHandle) {
						return 0;
					} else if (obj instanceof InheritedComputedColumnHandle) {
						return 1;
					} else if (obj instanceof Expression) {
						return 2;
					}

					return 3;
				}

				public int compare(Object o1, Object o2) {
					int w1 = computeWeight(o1);
					int w2 = computeWeight(o2);

					if (w1 == w2) {
						return innerCompare(o1, o2);
					}

					return w1 < w2 ? -1 : 1;
				}
			});

			return result;
		}

		return sortElements(children);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getChildren
	 * (java.lang.Object)
	 */
	public Object[] getChildren(Object parent) {
		Object[] children = getChildrenList(parent).toArray();
		if (filterList != null && !filterList.isEmpty()) {
			for (Iterator<ExpressionFilter> iter = filterList.iterator(); iter.hasNext();) {
				ExpressionFilter ft = iter.next();

				children = ft.filter(parent, children);
			}
		}
		return children;
	}

	protected List<Object> getChildrenList(Object parent) {
		ArrayList<Object> childrenList = new ArrayList<Object>();

		if (parent instanceof IAdaptable) {
			parent = DNDUtil.unwrapToModel(parent);
		}
		if (parent instanceof Object[]) {
			Object[] array = (Object[]) parent;
			if (array instanceof Operator[]) {
				return Arrays.asList(array);
			}
			for (int i = 0; i < array.length; i++) {
				Object[] children = getChildren(array[i]);
				childrenList.addAll(Arrays.asList(children));
			}
		} else if (parent instanceof String) {
			if (PARAMETERS.equals(parent)) {
				childrenList.add(ALL);
				for (Iterator iter = elementHandle.getModuleHandle().getParameters().iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof ParameterGroupHandle) {
						childrenList.add(obj);
					}
				}
			} else if (ALL.equals(parent)) {
				for (Iterator iter = elementHandle.getModuleHandle().getParameters().iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof ParameterHandle) {
						childrenList.add(obj);
					}
				}
			}
			if (VARIABLES.equals(parent)) {
				childrenList.add(VARIABLES_REPORT);
				// childrenList.add( VARIABLES_PAGE );
			}
			if (VARIABLES_REPORT.equals(parent)) {
				for (VariableElementHandle variable : ((ReportDesignHandle) elementHandle.getModuleHandle())
						.getPageVariables()) {
					if (DesignChoiceConstants.VARIABLE_TYPE_REPORT.equals(variable.getType()))
						childrenList.add(variable);
				}
			}
			// if ( VARIABLES_PAGE.equals( parent ) )
			// {
			// for ( VariableElementHandle variable : ( (ReportDesignHandle)
			// elementHandle.getModuleHandle( ) ).getPageVariables( ) )
			// {
			// if ( DesignChoiceConstants.VARIABLE_TYPE_PAGE.equals(
			// variable.getType( ) ) )
			// childrenList.add( variable );
			// }
			// }
			else {
				if (COLUMN_BINDINGS.equals(parent)) {
					childrenList.addAll(getAllBindingElementHandles());
				} else if (CURRENT_CUBE.equals(parent)) {
					CubeHandle cube = getCube(elementHandle);
					if (cube != null) {
						Object nodeProviderAdapter = ElementAdapterManager.getAdapter(cube, INodeProvider.class);
						if (nodeProviderAdapter != null) {
							return Arrays.asList(((INodeProvider) nodeProviderAdapter).getChildren(cube));
						}
					}
				} else if (BIRT_OBJECTS.equals(parent)) {
					childrenList.addAll(getClassList(false));
					try {
						IScriptFunctionCategory[] categorys = FunctionProvider.getCategories();
						for (int i = 0; i < categorys.length; i++) {
							if (categorys[i].isVisible())
								childrenList.add(categorys[i]);
						}
					} catch (BirtException e) {
						ExceptionHandler.handle(e);
					}
				} else if (NATIVE_OBJECTS.equals(parent)) {
					childrenList.addAll(getClassList(true));
				} else if (OPERATORS.equals(parent)) {
					childrenList.add(OPERATORS_ASSIGNMENT);
					childrenList.add(OPERATORS_COMPARISON);
					childrenList.add(OPERATORS_COMPUTATIONAL);
					childrenList.add(OPERATORS_LOGICAL);
					childrenList.add(0, childrenList.toArray());
				}
			}
		} else if (parent instanceof IClassInfo) {
			IClassInfo classInfo = (IClassInfo) parent;
			for (Iterator iter = DEUtil.getMembers(classInfo).iterator(); iter.hasNext();) {
				childrenList.add(new ILocalizableInfo[] { classInfo, (IMemberInfo) iter.next() });
			}
			for (Iterator iter = DEUtil.getMethods(classInfo, true).iterator(); iter.hasNext();) {
				IMethodInfo mi = (IMethodInfo) iter.next();

				processMethods(classInfo, mi, childrenList);
			}

			for (Iterator iter = AggregationUtil.getMethods(classInfo).iterator(); iter.hasNext();) {
				IMethodInfo mi = (IMethodInfo) iter.next();

				processMethods(classInfo, mi, childrenList);
			}
		} else if (parent instanceof IScriptFunctionCategory) {
			try {
				IScriptFunction[] functions = ((IScriptFunctionCategory) parent).getFunctions();
				for (int i = 0; i < functions.length; i++) {
					if (functions[i].isVisible())
						childrenList.add(functions[i]);
				}
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
		} else if (parent instanceof ParameterGroupHandle) {
			childrenList.addAll(((ParameterGroupHandle) parent).getParameters().getContents());
		} else if (parent instanceof ReportItemHandle || parent instanceof GroupHandle) {
			if (parent instanceof ReportItemHandle) {
				List<String> nameList = new ArrayList<String>();
				Iterator<ComputedColumnHandle> iter = getColumnBindings((ReportItemHandle) parent);
				while (iter.hasNext()) {
					ComputedColumnHandle column = iter.next();
					childrenList.add(column);
					nameList.add(column.getName());
				}
				if (parent == elementHandle) {
					ReportItemHandle root = DEUtil.getBindingRoot(elementHandle);
					DesignElementHandle container = elementHandle;
					while (root != null && container != null && container != root) {
						container = container.getContainer();
						if (!(container instanceof ReportItemHandle))
							continue;
						Iterator<ComputedColumnHandle> iter1 = getColumnBindings((ReportItemHandle) container);
						while (iter1.hasNext()) {
							ComputedColumnHandle column = iter1.next();
							if (!nameList.contains(column.getName())) {
								childrenList.add(new InheritedComputedColumnHandle(column));
								nameList.add(column.getName());
							}
						}
					}
				}
			}

			// add hard code row count expression here
			if (DEUtil.enableRowNum(parent)) {
				Expression rowNum = new Expression(Messages.getString("ExpressionProvider.Expression.RowNumName"), //$NON-NLS-1$
						DEUtil.getRowNumExpression(elementHandle, (ReportItemHandle) parent), // $NON-NLS-1$
						Messages.getString("ExpressionProvider.Expression.RowNumTooltip"));//$NON-NLS-1$
				childrenList.add(rowNum);
			}
			// add edit option
			addEditBindingsItem(childrenList, Messages.getString("ExpressionProvider.EditBindings"), parent); //$NON-NLS-1$
		} else {
			Object nodeProviderAdapter = ElementAdapterManager.getAdapter(parent, INodeProvider.class);
			if (nodeProviderAdapter != null) {
				List children = new ArrayList();
				children.addAll(Arrays.asList(((INodeProvider) nodeProviderAdapter).getChildren(parent)));
//				if ( parent instanceof MeasureGroupHandle )
//				{
//					for ( int i = 0; i < children.size( ); i++ )
//					{
//						Object measure = children.get( i );
//						if ( measure instanceof TabularMeasureHandle
//								&& ( (TabularMeasureHandle) measure ).isCalculated( ) )
//						{
//							children.remove( i );
//							i--;
//						}
//					}
//				}
				return children;
			}
		}

		if (adapterProvider != null) {
			Object[] cats = adapterProvider.getChildren(parent);

			if (cats != null) {
				childrenList.addAll(Arrays.asList(cats));
			}
		}

		return childrenList;
	}

	private CubeHandle getCube(Object input) {
		if (input instanceof CubeHandle) {
			return (CubeHandle) input;
		} else if (input instanceof ReportItemHandle && ((ReportItemHandle) input).getCube() != null) {
			return ((ReportItemHandle) input).getCube();
		} else
			return getCubeHandle(input);
	}

	private CubeHandle getCubeHandle(Object input) {
		Object parent = null;
		if (input instanceof LevelHandle) {
			parent = ((LevelHandle) input).getContainer().getContainer().getContainer();
		} else if (input instanceof HierarchyHandle) {
			parent = ((HierarchyHandle) input).getContainer().getContainer();
		} else if (input instanceof DimensionHandle) {
			parent = ((DimensionHandle) input).getContainer();
		} else if (input instanceof MeasureHandle) {
			parent = ((MeasureHandle) input).getContainer().getContainer();
		} else if (input instanceof MeasureGroupHandle) {
			parent = ((MeasureGroupHandle) input).getContainer();
		}
		if (parent instanceof CubeHandle)
			return (CubeHandle) parent;
		return null;
	}

	/**
	 * Add 'Edit Bindings' item into children items list, if current context doesn't
	 * allow editing column, subclass will override this method.
	 * 
	 * @param childrenList
	 * @param itemText
	 * @param parent
	 * @since 2.6
	 */
	protected void addEditBindingsItem(ArrayList<Object> childrenList, String itemText, Object parent) {
		childrenList.add(new Object[] { itemText, parent });
	}

	/**
	 * Check overload method signature
	 * 
	 * @param classInfo
	 * @param mi
	 * @param childrenList
	 */
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
	 * Returns all element handles related to available bindings.
	 * 
	 * @return
	 * @since 2.3
	 */
	protected List<DesignElementHandle> getAllBindingElementHandles() {
		ArrayList<DesignElementHandle> childrenList = new ArrayList();
		List bindingList = getAllColumnBindingList();
		// The list is from top to bottom,reverse it
		Collections.reverse(bindingList);
		for (Iterator iter = bindingList.iterator(); iter.hasNext();) {
			ComputedColumnHandle handle = (ComputedColumnHandle) iter.next();
			if (!childrenList.contains(handle.getElementHandle())) {
				childrenList.add(handle.getElementHandle());
			}
		}
		return childrenList;
	}

	/**
	 * Returns all column bindings of current element handle.
	 * 
	 * @return
	 * @since 2.3
	 */
	protected List getAllColumnBindingList() {
		List bindingList = DEUtil.getAllColumnBindingList(elementHandle, includeSelf);
		return bindingList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#
	 * getDisplayText(java.lang.Object)
	 */
	public String getDisplayText(Object element) {
		Object nodeProviderAdapter = ElementAdapterManager.getAdapter(element, INodeProvider.class);
		if (nodeProviderAdapter != null) {
			return ((INodeProvider) nodeProviderAdapter).getNodeDisplayName(element);
		}

		if (element instanceof Object[]) {
			if (element instanceof Operator[]) {
				if (element == OPERATORS_ASSIGNMENT) {
					return DISPLAY_TEXT_ASSIGNMENT;
				} else if (element == OPERATORS_COMPARISON) {
					return DISPLAY_TEXT_COMPARISON;
				} else if (element == OPERATORS_COMPUTATIONAL) {
					return DISPLAY_TEXT_COMPUTATIONAL;
				} else if (element == OPERATORS_LOGICAL) {
					return DISPLAY_TEXT_LOGICAL;
				}
			} else if (element instanceof ILocalizableInfo[]) {
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

							displayText.append(argInfo.getDisplayName());
							if (argInfo.getType() != null)
								displayText.append(":" + argInfo.getType());
						}

						break;
					}

					displayText.append(")"); //$NON-NLS-1$

					if (!method.isConstructor()) {
						displayText.append(" : "); //$NON-NLS-1$
						String returnType = method.getReturnType();
						if (returnType == null) {
							returnType = "void"; //$NON-NLS-1$
						}
						displayText.append(returnType);
					}

				} else if (info instanceof IMemberInfo) {
					displayText.append(" : "); //$NON-NLS-1$
					displayText.append(((IMemberInfo) info).getDataType());
				}
				return displayText.toString();
			}
			Object[] inputArray = (Object[]) element;
			if (inputArray.length == 2 && inputArray[1] instanceof ReportItemHandle) {
				return inputArray[0].toString();
			} else {
				return ALL;
			}
		} else if (element instanceof String) {
			return (String) element;
		} else if (element instanceof Operator) {
			return ((Operator) element).symbol;
		} else if (element instanceof DesignElementHandle) {
			return DEUtil.getDisplayLabel(element, false);
		} else if (element instanceof ComputedColumnHandle) {
			return ((ComputedColumnHandle) element).getName();
		} else if (element instanceof InheritedComputedColumnHandle) {
			return ((InheritedComputedColumnHandle) element).getName();
		} else if (element instanceof Expression) {
			return ((Expression) element).symbol;
		} else if (element instanceof LevelAttributeHandle) {
			return ((LevelAttributeHandle) element).getName();
		} else if (element instanceof IScriptFunctionCategory) {
			String categoreName = ((IScriptFunctionCategory) element).getName() == null
					? Messages.getString("ExpressionTreeSupport.Category.Global") //$NON-NLS-1$
					: ((IScriptFunctionCategory) element).getName();
			return categoreName;
		} else if (element instanceof IScriptFunction) {
			IScriptFunction function = (IScriptFunction) element;
			StringBuffer displayText = new StringBuffer(function.getName());
			IScriptFunctionArgument[] arguments = function.getArguments();

			displayText.append("("); //$NON-NLS-1$

			if (arguments != null) {
				for (int i = 0; i < arguments.length; i++) {
					displayText.append(arguments[i].getName());
					if (arguments[i].getDataTypeName() != null)
						displayText.append(":" + arguments[i].getDataTypeName().trim());
					if (i < arguments.length - 1)
						displayText.append(", ");//$NON-NLS-1$
				}
			}
			displayText.append(")"); //$NON-NLS-1$
			if (!function.isConstructor()) {
				displayText.append(" : "); //$NON-NLS-1$
				String returnType = function.getDataTypeName();
				if (returnType == null) {
					returnType = "void"; //$NON-NLS-1$
				}
				displayText.append(returnType);
			}
			return displayText.toString();
		}

		if (adapterProvider != null) {
			String txt = adapterProvider.getDisplayText(element);

			if (txt != null) {
				return txt;
			}
		}

		return element.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#
	 * getTooltipText(java.lang.Object)
	 */
	public String getTooltipText(Object element) {
		if (element instanceof Operator) {
			return ((Operator) element).tooltip;
		} else if (element instanceof Expression) {
			return ((Expression) element).tooltip;
		} else if (element instanceof ILocalizableInfo[]) {
			return ((ILocalizableInfo[]) element)[1].getToolTip();
		} else if (element instanceof IScriptFunction) {
			return ((IScriptFunction) element).getDescription();
		} else if (element instanceof IScriptFunctionCategory) {
			return ((IScriptFunctionCategory) element).getDescription();
		} else if (element instanceof ComputedColumnHandle) {
			return TOOLTIP_BINDING_PREFIX + DataUtil.getAggregationExpression((ComputedColumnHandle) element);
		} else if (element instanceof InheritedComputedColumnHandle) {
			return TOOLTIP_BINDING_PREFIX + ((InheritedComputedColumnHandle) element).getHandle().getExpression();
		} else if (element instanceof DataSetViewData) {
			return ((DataSetViewData) element).getDisplayName();
		}
		if (adapterProvider != null) {
			String txt = adapterProvider.getTooltipText(element);

			if (txt != null) {
				return txt;
			}
		}

		return getDisplayText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getImage
	 * (java.lang.Object)
	 */
	public Image getImage(Object element) {
		Object nodeProviderAdapter = ElementAdapterManager.getAdapter(element, INodeProvider.class);
		if (nodeProviderAdapter != null) {
			return ((INodeProvider) nodeProviderAdapter).getNodeIcon(element);
		}

		if (element instanceof Operator) {
			return IMAGE_OPERATOR;
		} else if (element instanceof ILocalizableInfo[]) {
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
		} else if (element instanceof IScriptFunction) {
			if (((IScriptFunction) element).isStatic())
				return IMAGE_STATIC_METHOD;
			else if (((IScriptFunction) element).isConstructor()) {
				return IMAGE_CONSTRUCTOR;
			} else
				return IMAGE_METHOD;
		} else if (element instanceof ComputedColumnHandle || element instanceof ResultSetColumnHandle
				|| element instanceof DataSetItemModel || element instanceof Expression) {
			return IMAGE_COLUMN;
		} else if (element instanceof InheritedComputedColumnHandle) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_INHERIT_COLUMN);
		} else if (element instanceof DesignElementHandle) {
			return ReportPlatformUIImages.getImage(element);
		} else if (element instanceof LevelAttributeHandle) {
			return IMAGE_LEVEL_ATTRI;
		}

		if (adapterProvider != null) {
			return adapterProvider.getImage(element);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getInsertText
	 * (java.lang.Object)
	 */
	public String getInsertText(Object element) {
		if (element instanceof Operator) {
			return ((Operator) element).insertString;
		} else if (element instanceof Expression) {
			return ((Expression) element).insertString;
		} else if (element instanceof ILocalizableInfo[]) {
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
		} else if (element instanceof IScriptFunction) {
			IScriptFunction function = (IScriptFunction) element;
			StringBuffer insertText = new StringBuffer();
			if (function.isStatic()) {
				if (function.getCategory().getName() != null) {
					insertText.append(function.getCategory().getName() + "."); //$NON-NLS-1$
				}
			} else if (function.isConstructor()) {
				insertText.append("new "); //$NON-NLS-1$
			}
			insertText.append(function.getName());
			insertText.append("()"); //$NON-NLS-1$
			return insertText.toString();
		} else if (element instanceof ParameterHandle) {
			return DEUtil.getExpression(element);
		} else if (element instanceof VariableElementHandle) {
			return DEUtil.getExpression(element);
		} else if (element instanceof ComputedColumnHandle) {
			return DEUtil.getBindingexpression(elementHandle, (ComputedColumnHandle) element);
		} else if (element instanceof InheritedComputedColumnHandle) {
			return DEUtil.getBindingexpression(elementHandle, ((InheritedComputedColumnHandle) element).getHandle(),
					false);
		} else if (element instanceof LevelHandle || element instanceof MeasureHandle
				|| element instanceof LevelAttributeHandle) {
			return DEUtil.getExpression(element);
		} else if (element instanceof String) {
			return (String) element;
		}

		if (adapterProvider != null) {
			return adapterProvider.getInsertText(element);
		}

		return null;
	}

	private static Image getIconImage(String id) {
		return ReportPlatformUIImages.getImage(id);
	}

	protected List getClassList(boolean isNative) {
		List list = DEUtil.getClasses();
		ArrayList resultList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			IClassInfo classInfo = (IClassInfo) iter.next();
			if (classInfo.isNative() == isNative && !classInfo.getName().equals("Total")) //$NON-NLS-1$
			{
				resultList.add(classInfo);
			}
		}
		return resultList;
	}

	/**
	 * Adds a filter for this expression builder.
	 * 
	 * @param filter the filter to add
	 */
	public void addFilter(ExpressionFilter filter) {
		if (filter == null) {
			return;
		}

		if (filterList == null) {
			filterList = new ArrayList<ExpressionFilter>();
		}

		if (!filterList.contains(filter)) {
			filterList.add(filter);
		}
	}

	/**
	 * Adds a filter list for this expression builder.
	 * 
	 * @param list the list of the filter to add
	 */
	public void addFilterList(List<ExpressionFilter> list) {
		// allows for null.
		if (list == null || list.isEmpty()) {
			return;
		}
		for (Iterator<ExpressionFilter> iter = list.iterator(); iter.hasNext();) {
			ExpressionFilter ft = iter.next();

			addFilter(ft);
		}
	}

	/**
	 * Removes a filter from the filter list.
	 * 
	 * @param filter the filter to remove
	 */
	public void removeFilter(ExpressionFilter filter) {
		if (filterList == null) {
			return;
		}
		filterList.remove(filter);
	}

	/**
	 * Clears the filter list.
	 */
	public void clearFilters() {
		if (filterList == null) {
			return;
		}
		filterList.clear();
	}

	public void setFilters(List<ExpressionFilter> filters) {
		this.filterList = filters;
	}

	public List<ExpressionFilter> getFilters() {
		return filterList;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof IAdaptable) {
			element = DNDUtil.unwrapToModel(element);
		}
		if (element instanceof PropertyHandle || element instanceof TabularMeasureGroupHandle
				|| element instanceof DimensionHandle || element instanceof LevelHandle)
			return getChildrenList(element).size() > 0;

		if (adapterProvider != null) {
			return adapterProvider.hasChildren(element);
		}

		return false;
	}

	protected void initAdapterProvider() {
		adapterProvider = null;

		Object adapter = ElementAdapterManager.getAdapter(this, IExpressionProvider.class);

		if (adapter instanceof IExpressionProvider && adapter != this) {
			adapterProvider = (IExpressionProvider) adapter;
		}
	}

	/**
	 * Returns the column binding iterator of selected report item.
	 * 
	 * @param handle report item
	 * @return iterator of ColumnBindingHandle
	 */
	protected Iterator<ComputedColumnHandle> getColumnBindings(ReportItemHandle handle) {
		return handle.columnBindingsIterator();
	}
}