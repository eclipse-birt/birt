/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IDataItem;
import org.eclipse.birt.report.model.api.simpleapi.IDataSet;
import org.eclipse.birt.report.model.api.simpleapi.IDataSource;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IDynamicText;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IGrid;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IImage;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.api.simpleapi.IList;
import org.eclipse.birt.report.model.api.simpleapi.IMasterPage;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportElement;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.api.simpleapi.ITextItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

/**
 * ReportDesign
 */
public class ReportDesign extends ScriptableObject implements IReportDesign {

	/**
	 *
	 */
	private static final long serialVersionUID = 5768246404361271845L;

	/**
	 * The class name in JavaScript.
	 */

	public static final String CLASS_NAME = "ReportDesign"; //$NON-NLS-1$

	final private InternalReportDesign report;

	/**
	 * Constructor.
	 *
	 * @param report
	 */

	public ReportDesign(ReportDesignHandle report) {
		this.report = new InternalReportDesign(report);
		initFunctions();
	}

	/**
	 * Gets master page script instance.
	 *
	 * @param name
	 * @return master page script instance
	 */

	@Override
	public IMasterPage getMasterPage(String name) {
		return report.getMasterPage(name);
	}

	@Override
	public IDataSet getDataSet(String name) {
		return report.getDataSet(name);
	}

	@Override
	public IDataSource getDataSource(String name) {
		return report.getDataSource(name);
	}

	@Override
	public IReportElement getReportElement(String name) {
		return report.getReportElement(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.simpleapi.IReportDesign#
	 * getReportElementByID(long)
	 */
	@Override
	public IReportElement getReportElementByID(long id) {
		return report.getReportElementByID(id);
	}

	@Override
	public IDataItem getDataItem(String name) {
		return report.getDataItem(name);
	}

	@Override
	public IGrid getGrid(String name) {
		return report.getGrid(name);
	}

	@Override
	public IImage getImage(String name) {
		return report.getImage(name);
	}

	@Override
	public ILabel getLabel(String name) {
		return report.getLabel(name);
	}

	@Override
	public IList getList(String name) {
		return report.getList(name);
	}

	@Override
	public ITable getTable(String name) {
		return report.getTable(name);
	}

	@Override
	public IDynamicText getDynamicText(String name) {
		return report.getDynamicText(name);
	}

	@Override
	public ITextItem getTextItem(String name) {
		return report.getTextItem(name);
	}

	@Override
	public void setDisplayNameKey(String displayNameKey) throws SemanticException {
		report.setProperty(IDesignElementModel.DISPLAY_NAME_ID_PROP, displayNameKey);
	}

	@Override
	public String getDisplayNameKey() {
		return report.getDisplayNameKey();
	}

	@Override
	public void setDisplayName(String displayName) throws SemanticException {
		report.setProperty(IDesignElementModel.DISPLAY_NAME_PROP, displayName);

	}

	@Override
	public String getDisplayName() {
		return report.getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#save()
	 */

	@Override
	public void save() throws IOException {
		report.save();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#saveAs(java
	 * .lang.String)
	 */

	@Override
	public void saveAs(String newName) throws IOException {
		report.saveAs(newName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#getTheme()
	 */
	@Override
	public String getTheme() {
		return report.getTheme();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#setTheme(java
	 * .lang.String)
	 */

	@Override
	public void setTheme(String theme) throws SemanticException {
		report.setProperty(IModuleModel.THEME_PROP, theme);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getParent()
	 */
	@Override
	public IDesignElement getParent() {
		return report.getParent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getNamedExpression
	 * (java.lang.String)
	 */
	@Override
	public String getNamedExpression(String name) {
		return report.getNamedExpression(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getQualifiedName
	 * ()
	 */
	@Override
	public String getQualifiedName() {
		return report.getQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getReport()
	 */
	@Override
	public IReportDesign getReport() {
		return report;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getStyle()
	 */
	@Override
	public IStyle getStyle() {
		return report.getStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getUserProperty
	 * (java.lang.String)
	 */
	@Override
	public Object getUserProperty(String name) {
		return report.getUserProperty(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setNamedExpression
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setNamedExpression(String name, String exp) throws SemanticException {
		report.setNamedExpression(name, exp);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setUserProperty
	 * (java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void setUserProperty(String name, Object value, String type) throws SemanticException {
		report.setUserProperty(name, value, type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setUserProperty
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setUserProperty(String name, String value) throws SemanticException {
		report.setUserProperty(name, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	private void initFunctions() {
		Method[] tmpMethods = this.getClass().getDeclaredMethods();
		HashMap<String, Method> methods = new LinkedHashMap<>();
		for (int i = 0; i < tmpMethods.length; i++) {
			Method tmpMethod = tmpMethods[i];
			String methodName = tmpMethod.getName();
			// must handle special case with long parameter or polymiorphism
			if ("getReportElementByID".equals(methodName) //$NON-NLS-1$
					|| "setUserProperty".equals(methodName)) {
				continue;
			}
			if ((tmpMethod.getModifiers() & Modifier.PUBLIC) != 0) {
				methods.put(methodName, tmpMethod);
			}
		}

		Context.enter();
		try {
			for (final Entry<String, Method> entry : methods.entrySet()) {
				this.defineProperty(entry.getKey(), new BaseFunction() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
						Object[] convertedArgs = JavascriptEvalUtil.convertToJavaObjects(args);
						try {
							Method method = entry.getValue();
							return method.invoke(ReportDesign.this, convertedArgs);
						} catch (Exception e) {
							throw new WrappedException(e);
						}
					}

				}, DONTENUM);
			}
		} finally {
			Context.exit();
		}

		this.defineProperty("getReportElementByID", //$NON-NLS-1$
				new Function_getReportElementByID(), DONTENUM);
		this.defineProperty("setUserProperty", //$NON-NLS-1$
				new Function_setUserProperty(), DONTENUM);
	}

	private class Function_getReportElementByID extends BaseFunction {

		private static final long serialVersionUID = 1L;

		@Override
		public Object call(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) {
			Object[] convertedArgs = JavascriptEvalUtil.convertToJavaObjects(args);

			return report.getReportElementByID((Integer) convertedArgs[0]);
		}
	}

	private class Function_setUserProperty extends BaseFunction {

		private static final long serialVersionUID = 1L;

		@Override
		public Object call(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) {
			Object[] convertedArgs = JavascriptEvalUtil.convertToJavaObjects(args);

			try {
				if (convertedArgs.length == 2) {
					report.setUserProperty((String) convertedArgs[0], (String) convertedArgs[1]);
				} else if (convertedArgs.length == 3) {
					report.setUserProperty((String) convertedArgs[0], convertedArgs[1], (String) convertedArgs[2]);
				}

			} catch (SemanticException e) {
				throw new WrappedException(e);
			}

			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.simpleapi.IDesignElement#
	 * getUserPropertyExpression(java.lang.String)
	 */
	@Override
	public Object getUserPropertyExpression(String name) {
		return report.getUserPropertyExpression(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.simpleapi.IReportDesign#
	 * createFilterCondition()
	 */
	@Override
	public IFilterCondition createFilterCondition() {
		return report.createFilterCondition();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createHideRule
	 * ()
	 */
	@Override
	public IHideRule createHideRule() {
		return report.createHideRule();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createHighLightRule
	 * ()
	 */
	@Override
	public IHighlightRule createHighLightRule() {
		return report.createHighLightRule();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createSortCondition
	 * ()
	 */
	@Override
	public ISortCondition createSortCondition() {
		return report.createSortCondition();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createAction()
	 */
	@Override
	public IAction createAction() {
		return report.createAction();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createDataBinding
	 * ()
	 */
	@Override
	public IDataBinding createDataBinding() {
		return report.createDataBinding();
	}
}
