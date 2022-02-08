/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.ui.util;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.birt.data.oda.pojo.querymodel.ClassColumnMappings;
import org.eclipse.birt.data.oda.pojo.querymodel.Column;
import org.eclipse.birt.data.oda.pojo.querymodel.ColumnReferenceNode;
import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.FieldSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.OneColumnMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.querymodel.ReferenceGraph;
import org.eclipse.birt.data.oda.pojo.querymodel.RelayReferenceNode;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.ui.Activator;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ColumnDefinition;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.OdaType;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryWriter;
import org.eclipse.birt.data.oda.pojo.util.URLParser;

/**
 * 
 */

public class Utils {

	private static Logger logger = Logger.getLogger(Utils.class.getName());

	private static final String METHOD_FLAG = ".+\\(.*\\)$"; //$NON-NLS-1$
	private static final String BEAN_NAME_REGEX = "^(get|is)[A-Z].*"; //$NON-NLS-1$

	private static final String CLASS_IMG_FLAG = "classImgFlag"; //$NON-NLS-1$
	private static final String FIELD_IMG_FLAG = "fieldImgFlag"; //$NON-NLS-1$
	private static final String METHOD_IMG_FLAG = "methodImgFlag"; //$NON-NLS-1$
	private static final String WARNING_IMG_FLAG = "warningImgFlag"; //$NON-NLS-1$

	// The icons DESIGNTIME_IMG_FLAG and RUNTIME_IMG_FLAG are discarded to use for
	// the TabItem
	// But still preserve the 2 icon files and the entries
	private static final String DESIGNTIME_IMG_FLAG = "DesignTimeImgFlag"; //$NON-NLS-1$
	private static final String RUNTIME_IMG_FLAG = "RunTimeImgFlag"; //$NON-NLS-1$

	private static final String FOLDER_ICON = "FolderIcon"; //$NON-NLS-1$
	private static final String JAR_ICON = "JarIcon"; //$NON-NLS-1$
	private static final String OK_DISABLE_ICON = "OKDisableIcon"; //$NON-NLS-1$
	private static final String FAIL_DISABLE_ICON = "FailDisableIcon"; //$NON-NLS-1$

	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(CLASS_IMG_FLAG, ImageDescriptor.createFromFile(Activator.class, "/icons/class_obj.gif"));//$NON-NLS-1$
		reg.put(FIELD_IMG_FLAG, ImageDescriptor.createFromFile(Activator.class, "/icons/field_public_obj.gif"));//$NON-NLS-1$
		reg.put(METHOD_IMG_FLAG, ImageDescriptor.createFromFile(Activator.class, "/icons/method_public_obj.gif"));//$NON-NLS-1$
		reg.put(WARNING_IMG_FLAG, ImageDescriptor.createFromFile(Activator.class, "/icons/warning_obj.gif"));//$NON-NLS-1$
		reg.put(DESIGNTIME_IMG_FLAG, ImageDescriptor.createFromFile(Activator.class, "/icons/icon_designtime.gif"));//$NON-NLS-1$
		reg.put(RUNTIME_IMG_FLAG, ImageDescriptor.createFromFile(Activator.class, "/icons/icon_runtime.gif"));//$NON-NLS-1$
		reg.put(FOLDER_ICON, ImageDescriptor.createFromFile(Activator.class, "/icons/folder_icon.gif"));//$NON-NLS-1$
		reg.put(JAR_ICON, ImageDescriptor.createFromFile(Activator.class, "/icons/jar_icon.gif"));//$NON-NLS-1$
		reg.put(OK_DISABLE_ICON, ImageDescriptor.createFromFile(Activator.class, "/icons/ok_tbl_disabled.gif"));//$NON-NLS-1$
		reg.put(FAIL_DISABLE_ICON, ImageDescriptor.createFromFile(Activator.class, "/icons/fail_tbl_disabled.gif"));//$NON-NLS-1$
	}

	private Utils() {
	};

	public static URLParser createURLParser(ResourceIdentifiers ri) {
		if (ri == null) {
			return new URLParser(null);
		}
		return new URLParser(DesignSessionUtil.createResourceIdentifiersContext(ri));
	}

	public static OdaType getSuggestOdaType(Member m) {
		if (m instanceof Method) {
			return getSuggerstOdaType(((Method) m).getReturnType());
		} else if (m instanceof Field) {
			return getSuggerstOdaType(((Field) m).getType());
		}
		assert false;
		return OdaType.String;
	}

	@SuppressWarnings("unchecked")
	private static OdaType getSuggerstOdaType(Class type) {
		// Primitive or its wrapper
		if (type == Boolean.TYPE || type == Boolean.class) {
			return OdaType.Boolean;
		}
		if (type == Character.TYPE || type == Character.class) {
			return OdaType.String;
		}
		if (type == Byte.TYPE || type == Byte.class) {
			return OdaType.Integer;
		}
		if (type == Short.TYPE || type == Short.class) {
			return OdaType.Integer;
		}
		if (type == Integer.TYPE || type == Integer.class) {
			return OdaType.Integer;
		}
		if (type == Long.TYPE || type == Long.class) {
			return OdaType.Double;
		}
		if (type == Float.TYPE || type == Float.class) {
			return OdaType.Double;
		}
		if (type == Double.TYPE || type == Double.class) {
			return OdaType.Double;
		}

		if (BigDecimal.class.isAssignableFrom(type)) {
			return OdaType.Decimal;
		}
		if (java.sql.Blob.class.isAssignableFrom(type)) {
			return OdaType.Blob;
		}
		if (java.sql.Clob.class.isAssignableFrom(type)) {
			return OdaType.String;
		}
		if (java.sql.Time.class.isAssignableFrom(type)) {
			return OdaType.Time;
		}
		if (java.sql.Timestamp.class.isAssignableFrom(type)) {
			return OdaType.Timestamp;
		}
		if (java.util.Date.class.isAssignableFrom(type)) {
			return OdaType.Date;
		}
		if (type == String.class) {
			return OdaType.String;
		}
		// For other types
		return OdaType.Object;
	}

	public static String getSuggestName(Member m) {
		String name = m.getName();
		if (m instanceof Method) {
			if (name.matches(BEAN_NAME_REGEX)) {
				if (name.startsWith("get")) //$NON-NLS-1$
				{
					return name.substring(3);
				}
				// starts with "is"
				return name.substring(2);
			} else {
				return upperFirstChar(name);
			}
		} else if (m instanceof Field) {
			return upperFirstChar(name);

		}
		assert false;
		return m.getName();
	}

	private static String upperFirstChar(String name) {
		assert name != null && name.length() > 0;
		if (Character.isLowerCase(name.charAt(0))) {
			// Upper case the first char if necessary
			return name.replaceFirst(String.valueOf(name.charAt(0)),
					String.valueOf(Character.toUpperCase(name.charAt(0))));
		}
		return name;
	}

	public static void updateColumnMappings(PojoQuery pq, ColumnDefinition[] cds) {
		assert pq != null && cds != null;
		pq.clearColumnMappings();
		for (int i = 0; i < cds.length; i++) {
			IMappingSource[] mss = cds[i].getMappingPath();
			ClassColumnMappings parent = null;
			for (int j = 0; j <= mss.length - 2; j++) {
				ClassColumnMappings ccm = new ClassColumnMappings(mss[j]);
				if (parent == null) {
					parent = addClassColumnMappings(pq, ccm);
				} else {
					parent = addClassColumnMappings(parent, ccm);
				}
			}
			Column c = new Column(cds[i].getName(), cds[i].getType().getName(), i + 1);
			assert mss.length >= 1;
			IColumnsMapping cm = new OneColumnMapping(mss[mss.length - 1], c);
			if (parent == null) {
				pq.addColumnsMapping(cm);
			} else {
				parent.addColumnsMapping(cm);
			}
		}
	}

	public static ColumnDefinition[] getColumnDefinitions(PojoQuery pq) throws OdaException {
		assert pq != null;
		ReferenceGraph rg = ReferenceGraph.create(pq);
		ColumnDefinition[] cds = new ColumnDefinition[rg.getColumnReferences().length];
		int index = 0;
		for (ColumnReferenceNode crn : rg.getColumnReferences()) {
			String name = crn.getColumn().getName();
			OdaType type = OdaType.getInstance(crn.getColumn().getOdaType());
			if (type == null) {
				logger.log(Level.WARNING, "Unkown Oda type: " + crn.getColumn().getOdaType()); //$NON-NLS-1$
				type = OdaType.String;
			}
			Stack<IMappingSource> mss = new Stack<IMappingSource>();
			mss.push(crn.getReference());
			RelayReferenceNode rrn = crn.getParent();
			while (rrn != null) {
				mss.push(rrn.getReference());
				rrn = rrn.getParent();
			}
			IMappingSource[] mappingPath = new IMappingSource[mss.size()];
			for (int i = 0; i < mappingPath.length; i++) {
				mappingPath[i] = mss.pop();
			}
			cds[index] = new ColumnDefinition(mappingPath, name, type);
			index++;
		}
		return cds;
	}

	private static ClassColumnMappings addClassColumnMappings(PojoQuery pq, ClassColumnMappings ccm) {
		for (IColumnsMapping cm : pq.getColumnsMappings()) {
			if (cm instanceof ClassColumnMappings) {
				if (cm.getSource().equals(ccm.getSource())) {
					return (ClassColumnMappings) cm; // already exists
				}
			}
		}
		pq.addColumnsMapping(ccm);
		return ccm;
	}

	private static ClassColumnMappings addClassColumnMappings(ClassColumnMappings source, ClassColumnMappings ccm) {
		for (IColumnsMapping cm : source.getColumnsMappings()) {
			if (cm instanceof ClassColumnMappings) {
				if (cm.getSource().equals(ccm.getSource())) {
					return (ClassColumnMappings) cm; // already exists
				}
			}
		}
		source.addColumnsMapping(ccm);
		return ccm;
	}

	public static void savePrivateProperty(DataSetDesign design, String name, String value) throws OdaException {
		if (design.getPrivateProperties() != null) {
			if (value.length() == 0 && design.getPrivateProperties().getProperty(name) == null) {
				// It seems an empty string and a null value are equal for Model
				return;
			}
			if (!value.equals(design.getPrivateProperties().getProperty(name))) {
				design.getPrivateProperties().setProperty(name, value);
			}
		} else {
			java.util.Properties p = new java.util.Properties();
			p.put(name, value);
			design.setPrivateProperties(DesignSessionUtil.createDataSetNonPublicProperties(
					design.getOdaExtensionDataSourceId(), design.getOdaExtensionDataSetId(), p));
		}
	}

	public static String getPrivateProperty(DataSetDesign design, String name) {
		if (design.getPrivateProperties() != null) {
			return design.getPrivateProperties().getProperty(name);
		}
		return null;
	}

	public static String getPublicProperty(DataSourceDesign ds, String name) {
		if (ds.getPublicProperties() != null) {
			return ds.getPublicProperties().getProperty(name);
		}
		return null;
	}

	public static String getPrivateProperty(DataSourceDesign ds, String name) {
		if (ds.getPrivateProperties() != null) {
			return ds.getPrivateProperties().getProperty(name);
		}
		return null;
	}

	public static IMappingSource[] getMappingSource(String mappingPath) throws OdaException {
		assert mappingPath != null;

		String[] mappingParts = splitBy(mappingPath, Constants.METHOD_OR_FIELD_SEPARATOR);
		if (mappingParts == null) {
			throw new OdaException(Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
					new Object[] { mappingPath }));
		}
		List<IMappingSource> sources = new ArrayList<IMappingSource>();
		for (String mappingPart : mappingParts) {
			String part = mappingPart.trim();
			if (part.equals("")) //$NON-NLS-1$
			{
				throw new OdaException(Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
						new Object[] { mappingPath }));
			}
			if (part.matches(METHOD_FLAG)) // Maybe a valid method
			{
				int last = part.lastIndexOf('(');
				String methodName = part.substring(0, last).trim();
				if (isValidIdentifier(methodName)) {
					String paramParts = part.substring(last + 1, part.length() - 1).trim();
					List<IMethodParameter> params = new ArrayList<IMethodParameter>();
					if (paramParts.length() > 0) // contain parameters
					{
						String[] ps = splitBy(paramParts, Constants.METHOD_PARAM_SEPARATOR);
						if (ps == null) {
							throw new OdaException(Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
									new Object[] { mappingPath }));
						}
						for (String p : ps) {
							String param = p.trim();
							if (param.length() == 0) {
								throw new OdaException(Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
										new Object[] { mappingPath }));
							}
							String[] nameOrValueAndType = splitBy(param, Constants.PARAM_TYPE_SEPARATOR);
							if (nameOrValueAndType == null
									|| !(nameOrValueAndType.length == 1 || nameOrValueAndType.length == 2)) {
								throw new OdaException(Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
										new Object[] { mappingPath }));
							}
							if (nameOrValueAndType.length == 1) {
								String type = nameOrValueAndType[0].trim();
								if (type.length() == 0) {
									throw new OdaException(
											Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
													new Object[] { mappingPath }));
								}
								params.add(new ConstantParameter(null, type));
							} else {
								// nameOrValueAndType.length == 2
								String nameOrValue = nameOrValueAndType[0].trim();
								String type = nameOrValueAndType[1].trim();
								if (nameOrValue.length() == 0 || type.length() == 0) {
									throw new OdaException(
											Messages.getFormattedString("DataSet.InvalidColumnMappingPath", //$NON-NLS-1$
													new Object[] { mappingPath }));
								}
								if (nameOrValue.startsWith(String.valueOf(Constants.CONSTANT_PARAM_VALUE_QUOTE))
										&& nameOrValue.endsWith(String.valueOf(Constants.CONSTANT_PARAM_VALUE_QUOTE))) {
									String value = nameOrValue.substring(1, nameOrValue.length() - 1);
									String regex = "\\Q" + Constants.CONSTANT_PARAM_VALUE_QUOTE_ESCAPE //$NON-NLS-1$
											+ Constants.CONSTANT_PARAM_VALUE_QUOTE + "\\E"; //$NON-NLS-1$

									// skip all escape chars
									value = value.replaceAll(regex,
											String.valueOf(Constants.CONSTANT_PARAM_VALUE_QUOTE));
									params.add(new ConstantParameter(value, type));
								} else {
									params.add(new VariableParameter(nameOrValue, type));
								}
							}
						}
					}
					sources.add(new MethodSource(methodName, params.toArray(new IMethodParameter[0])));
				} else {
					throw new OdaException(Messages.getFormattedString("DataSet.InvalidMethodName", //$NON-NLS-1$
							new Object[] { methodName }));
				}
			} else if (isValidIdentifier(part))//
			{
				sources.add(new FieldSource(part));
			} else {
				throw new OdaException(Messages.getFormattedString("DataSet.InvalidFieldName", //$NON-NLS-1$
						new Object[] { part }));
			}
		}
		return sources.toArray(new IMappingSource[0]);
	}

	public static boolean isEmptyString(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * we should consider the case that char <parameter>separator</parameter>
	 * included in a quoted string
	 * 
	 * @param s:         string to be split
	 * @param separator: can not be <code>CONSTANT_PARAM_VALUE_QUOTE</code> or
	 *                   <code>CONSTANT_PARAM_VALUE_QUOTE_ESCAPE</code>
	 * @return null if " numbers in <parameter>s</parameter> are not matched
	 */
	private static String[] splitBy(String s, char separator) throws OdaException {
		assert s != null;
		List<String> result = new ArrayList<String>();
		int startIndex = 0;
		int curIndex = 0;
		boolean isWaitingForEndQuote = false;
		while (curIndex < s.length()) {
			char c = s.charAt(curIndex);
			if (isWaitingForEndQuote) {
				if (c == Constants.CONSTANT_PARAM_VALUE_QUOTE
						&& s.charAt(curIndex - 1) != Constants.CONSTANT_PARAM_VALUE_QUOTE_ESCAPE) // not an escape case
				{
					// go to the end of a quoted string
					isWaitingForEndQuote = false;
				}
			} else {
				if (c == separator) {
					result.add(s.substring(startIndex, curIndex));
					startIndex = curIndex + 1;
				} else if (c == Constants.CONSTANT_PARAM_VALUE_QUOTE) {
					isWaitingForEndQuote = true;
				}
			}
			curIndex++;
		}
		if (isWaitingForEndQuote) // " numbers in s are not matched
		{
			return null;
		}
		result.add(s.substring(startIndex, curIndex));
		return result.toArray(new String[0]);
	}

	private static boolean isValidIdentifier(String s) {
		assert s.length() > 0;
		char[] chars = s.toCharArray();
		if (!Character.isJavaIdentifierStart(chars[0])) {
			return false;
		}
		for (int i = 1; i < chars.length; i++) {
			if (!Character.isJavaIdentifierPart(chars[i])) {
				return false;
			}
		}
		return true;
	}

	public static class FileComparator implements Comparator<File>, Serializable {
		private static final long serialVersionUID = 1L;

		public int compare(File o1, File o2) {
			if (o1.isDirectory() && o2.isDirectory()) {
				return o1.getName().compareTo(o2.getName());
			} else if (o1.isFile() && o2.isFile()) {
				return o1.getName().compareTo(o2.getName());
			} else if (o1.isDirectory() && !o2.isDirectory()) {
				return -1;
			} else {
				// o1 is not a directory but o2 is a directory
				return 1;
			}
		}
	}

	public static void savePojoQuery(PojoQuery pq, DataSetDesign design, Shell shell) {
		try {
			design.setQueryText(PojoQueryWriter.write(pq));
		} catch (OdaException e) {
			ExceptionHandler.showException(shell, Messages.getString("DataSet.FailedToSaveTitle"), //$NON-NLS-1$
					Messages.getString("DataSet.FailedToSaveMsg"), e); //$NON-NLS-1$
		}
	}

	public static boolean isColumnDefinitionsEqual(PojoQuery pq, ColumnDefinition[] cds) {
		try {
			return Arrays.equals(Utils.getColumnDefinitions(pq), cds);
		} catch (OdaException e) {
			logger.log(Level.WARNING, "Failed to get column definitions from pq", e); //$NON-NLS-1$
			return false;
		}
	}

	public static Image getClassFlagImg() {
		return JFaceResources.getImageRegistry().get(CLASS_IMG_FLAG);
	}

	public static Image getFieldFlagImg() {
		return JFaceResources.getImageRegistry().get(FIELD_IMG_FLAG);
	}

	public static Image getMethodFlagImg() {
		return JFaceResources.getImageRegistry().get(METHOD_IMG_FLAG);
	}

	public static Image getWarningFlagImg() {
		return JFaceResources.getImageRegistry().get(WARNING_IMG_FLAG);
	}

	public static Image getFolderIcon() {
		return JFaceResources.getImageRegistry().get(FOLDER_ICON);
	}

	public static Image getJarIcon() {
		return JFaceResources.getImageRegistry().get(JAR_ICON);
	}

	public static Image getOKIcon() {
		return JFaceResources.getImageRegistry().get(OK_DISABLE_ICON);
	}

	public static Image getFailIcon() {
		return JFaceResources.getImageRegistry().get(FAIL_DISABLE_ICON);
	}

}
