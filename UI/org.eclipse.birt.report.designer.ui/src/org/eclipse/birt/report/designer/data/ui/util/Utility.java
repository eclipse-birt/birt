/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 *
 * Utility class to get some useful objects from designer.ui. For future work to
 * break away data.ui from designer.ui
 */
public class Utility {
	/**
	 * get image descriptor
	 *
	 * @param imageDescriptor
	 * @return
	 */
	public static ImageDescriptor getImageDescriptor(String imageDescriptor) {
		return ReportPlatformUIImages.getImageDescriptor(imageDescriptor);
	}

	/**
	 * get report module handle
	 *
	 * @return
	 */
	public static ModuleHandle getReportModuleHandle() {
		return HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle();
	}

	/**
	 * get command stack
	 *
	 * @return
	 */
	public static CommandStack getCommandStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	/**
	 * get unique data set name
	 *
	 * @param baseName
	 * @return
	 */
	public static String getUniqueDataSetName(String baseName) {
		String finalName = baseName;
		int n = 1;

		while (HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().checkDataSetName(finalName)) {
			finalName = baseName + n++;
		}
		return finalName;
	}

	/**
	 * get unique data source name
	 *
	 * @param baseName
	 * @return
	 */
	public static String getUniqueDataSourceName(String baseName) {
		String finalName = baseName;
		int n = 1;

		while (HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().checkDataSourceName(finalName)) {
			finalName = baseName + n++;
		}
		return finalName;
	}

	/**
	 * check whether the data source name exist
	 *
	 * @param name
	 * @return
	 */
	public static boolean checkDataSourceName(String name) {
		return HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().checkDataSourceName(name);
	}

	/**
	 * check whether the data set name exist
	 *
	 * @param name
	 * @return
	 */
	public static boolean checkDataSetName(String name) {
		return HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().checkDataSetName(name);
	}

	/**
	 * get visible data source list
	 *
	 * @return
	 */
	public static List getVisibleDataSources() {
		return getReportModuleHandle().getVisibleDataSources();
	}

	/**
	 * get all available report parameter list
	 *
	 * @return
	 */
	public static List getAllParameters() {
		List parameterList = SessionHandleAdapter.getInstance().getReportDesignHandle().getAllParameters();
		return parameterList;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static ScalarParameterHandle getScalarParameter(String name) {
		if (name == null) {
			return null;
		}
		List parameterList = getAllParameters();
		Object parameterObject = null;
		for (int i = 0; i < parameterList.size(); i++) {
			parameterObject = parameterList.get(i);
			if (parameterObject instanceof ScalarParameterHandle) {
				if (name.equals(((ScalarParameterHandle) parameterObject).getQualifiedName())) {
					return (ScalarParameterHandle) parameterObject;
				}
			}
		}
		return null;
	}

	public static DynamicFilterParameterHandle getDynamicFilterParameter(String name) {
		if (name == null) {
			return null;
		}
		List parameterList = getAllParameters();
		Object parameterObject = null;
		for (int i = 0; i < parameterList.size(); i++) {
			parameterObject = parameterList.get(i);
			if (parameterObject instanceof DynamicFilterParameterHandle) {
				if (name.equals(((DynamicFilterParameterHandle) parameterObject).getQualifiedName())) {
					return (DynamicFilterParameterHandle) parameterObject;
				}
			}
		}
		return null;
	}

	/**
	 * get all data source list
	 *
	 * @return
	 */
	public static List getDataSources() {
		return getReportModuleHandle().getDataSources().getContents();
	}

	/**
	 * get visible data set list
	 *
	 * @return
	 */
	public static List getVisibleDataSets() {
		return getReportModuleHandle().getVisibleDataSets();
	}

	/**
	 * find the dataSet according to the given name
	 *
	 * @param name
	 * @return
	 */
	public static DataSetHandle findDataSet(String name) {
		return getReportModuleHandle().findDataSet(name);
	}

	/**
	 * get design element factory
	 *
	 * @return
	 */
	public static DesignElementFactory getDesignElementFactory() {
		DesignElementFactory factory = DesignElementFactory.getInstance(HandleAdapterFactory.getInstance()
				.getReportDesignHandleAdapter().getModuleHandle().getDataSets().getElementHandle().getModuleHandle());
		return factory;
	}

	/**
	 * new oda data set handle
	 *
	 * @param name
	 * @param type
	 * @return
	 */
	public static OdaDataSetHandle newOdaDataSet(String name, String type) {
		return getDesignElementFactory().newOdaDataSet(name, type);
	}

	/**
	 * new joint data set handle
	 *
	 * @param name
	 * @return
	 */
	public static JointDataSetHandle newJointDataSet(String name) {
		return getDesignElementFactory().newJointDataSet(name);
	}

	/**
	 * new script data set handle
	 *
	 * @param name
	 * @return
	 */
	public static ScriptDataSetHandle newScriptDataSet(String name) {
		return getDesignElementFactory().newScriptDataSet(name);
	}

	/**
	 * new oda data source handle
	 *
	 * @param name
	 * @param type
	 * @return
	 */
	public static OdaDataSourceHandle newOdaDataSource(String name, String type) {
		return getDesignElementFactory().newOdaDataSource(name, type);
	}

	/**
	 * new script data source handle
	 *
	 * @param name
	 * @return
	 */
	public static ScriptDataSourceHandle newScriptDataSource(String name) {
		return getDesignElementFactory().newScriptDataSource(name);
	}

	/**
	 * set script editor page active The id must be same with
	 * ReportScriptFormPage.ID
	 */
	public static void setScriptActivityEditor() {
		final String SCRIPT_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.script"; //$NON-NLS-1$
		if (UIUtil.getActiveReportEditor().findPage(SCRIPT_EDITOR_ID) != null) // $NON-NLS-1$
		{
			UIUtil.getActiveReportEditor().setActivePage(SCRIPT_EDITOR_ID);// $NON-NLS-1$
		}
	}

	/**
	 *
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getProperty(Object obj, String propertyName)
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object value = null;
		PropertyDescriptor descriptor = getPropertyDescriptor(obj, propertyName);
		if (descriptor != null) {
			Method method = descriptor.getReadMethod();
			if (method != null) {
				value = method.invoke(obj, null);
			}
		}

		return value;
	}

	/**
	 *
	 * @param obj
	 * @param propertyName
	 * @param value
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setProperty(Object obj, String propertyName, Object value)
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		PropertyDescriptor descriptor = getPropertyDescriptor(obj, propertyName);
		if (descriptor != null) {
			Method method = descriptor.getWriteMethod();
			if (method != null) {
				method.invoke(obj, new Object[] { value });
			}
		}
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static String getNonNullString(String value) {
		return value == null ? "" : value; //$NON-NLS-1$
	}

	/**
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	public static int findIndex(String[] source, String target) {
		int index = 0;
		for (int i = 0; i < source.length; i++) {
			if (source[i].equals(target)) {
				index = i;
				break;
			}
		}

		return index;
	}

	/**
	 *
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws IntrospectionException
	 */
	private static PropertyDescriptor getPropertyDescriptor(Object obj, String propertyName)
			throws IntrospectionException {
		PropertyDescriptor[] descriptors = getPropertyDescriptors(obj);
		for (int n = 0; n < descriptors.length; n++) {
			if (descriptors[n].getName().equals(propertyName)) {
				return descriptors[n];
			}
		}
		return null;
	}

	/**
	 *
	 * @param obj
	 * @return
	 * @throws IntrospectionException
	 */
	private static PropertyDescriptor[] getPropertyDescriptors(Object obj) throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
		return info.getPropertyDescriptors();
	}

	/**
	 * Convert the give string to GUI style, which cannot be null
	 *
	 * @param string the string to convert
	 * @return the string, or an empty string for null
	 */
	public static String convertToGUIString(String string) {
		if (string == null) {
			string = ""; //$NON-NLS-1$
		}
		return string;
	}

	/**
	 * Convert the give string to Model style
	 *
	 * @param string the string to convert
	 * @param trim   specify if the string needs to be trimmed
	 * @return the string, or null for an empty string
	 */
	public static String convertToModelString(String string, boolean trim) {
		if (string == null) {
			return null;
		}
		if (trim) {
			string = string.trim();
		}
		if (string.length() == 0) {
			string = null; // $NON-NLS-1$
		}
		return string;
	}

	/**
	 * Creates a new grid layout without margins by default
	 *
	 * @return the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin() {
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		return layout;
	}

	public static String getExpression(Object element) {
		if (element instanceof DataSetViewData) {
			String colName = ((DataSetViewData) element).getAlias();

			if (colName == null || colName.trim().length() == 0) {
				colName = ((DataSetViewData) element).getName();
			}
			return getColumnExpression(colName);
		}
		return null;
	}

	/**
	 * Create a row expression base on a binding column name.
	 *
	 * @param columnName the column name
	 * @return the expression, or null if the column name is blank.
	 */
	public static String getColumnExpression(String columnName) {
		Assert.isNotNull(columnName);
		if (StringUtil.isBlank(columnName)) {
			return null;
		}
		return ExpressionUtil.createJSRowExpression(columnName);// $NON-NLS-1$
	}

	/**
	 * Escapes \ and " following standard of Javascript
	 *
	 * @param str
	 * @return new string after escape special character
	 */
	public static String escape(String str) {
		String[][] chars = { { "\\\\", "\"", "\'", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				}, { "\\\\\\\\", "\\\\\"", "\\\\\'", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				} };
		String result = str;
		for (int i = 0; i < chars[0].length; i++) {
			result = result.replaceAll(chars[0][i], chars[1][i]);
		}
		return result;
	}

	/**
	 *
	 * @param control
	 * @param contextId
	 */
	public static void setSystemHelp(Control control, String contextId) {
		UIUtil.bindHelp(control, contextId);
	}

	/**
	 *
	 * @param e
	 */
	public static void log(Exception e) {
		ILog log = new ReportPlugin().getLog();
		log.log(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, e.getMessage(), e));
	}
}
