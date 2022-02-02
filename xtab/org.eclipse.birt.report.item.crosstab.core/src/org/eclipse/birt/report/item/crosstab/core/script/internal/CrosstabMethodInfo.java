/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstab;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCell;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCellInstance;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabInstance;
import org.eclipse.birt.report.model.api.scripts.MethodInfo;

/**
 * CrosstabMethodInfo
 */
public class CrosstabMethodInfo extends MethodInfo {

	private List<CrosstabArgumentInfoList> argumentInfos;

	CrosstabMethodInfo(Method method) {
		super(method);

		initArgumentList(method.getParameterTypes());
	}

	private void initArgumentList(Class<?>[] argumentList) {
		if (argumentInfos == null) {
			argumentInfos = new ArrayList<CrosstabArgumentInfoList>();
		}

		String[] argNames = populateArgNames(argumentList);

		CrosstabArgumentInfoList argumentInfoList = new CrosstabArgumentInfoList(argumentList, argNames);
		argumentInfos.add(argumentInfoList);
	}

	private String[] populateArgNames(Class<?>[] types) {
		if (types != null) {
			String[] names = new String[types.length];

			for (int i = 0; i < names.length; i++) {
				names[i] = getArgName(types[i]);
			}

			return names;
		}

		return null;
	}

	private String getArgName(Class<?> type) {
		if (type == IReportContext.class) {
			return "reportContext"; //$NON-NLS-1$
		}
		if (type == ICrosstab.class) {
			return "crosstab"; //$NON-NLS-1$
		}
		if (type == ICrosstabInstance.class) {
			return "crosstabInst"; //$NON-NLS-1$
		}
		if (type == ICrosstabCell.class) {
			return "cell"; //$NON-NLS-1$
		}
		if (type == ICrosstabCellInstance.class) {
			return "cellInst"; //$NON-NLS-1$
		}

		return null;
	}

	public Iterator argumentListIterator() {
		if (argumentInfos == null) {
			return Collections.EMPTY_LIST.iterator();
		}

		return argumentInfos.iterator();
	}

	public boolean isDeprecated() {
		String javaDoc = getJavaDoc();
		return javaDoc != null && javaDoc.indexOf("@deprecated") != -1; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getJavaDoc()
	 */
	public String getJavaDoc() {
		return javaDoc.get(getMethod().getName());
	}

	private final static Map<String, String> javaDoc = new HashMap<String, String>();

	static {
		javaDoc.put("onPrepareCrosstab", "/**\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ " * Called when crosstab is being prepared.\n" //$NON-NLS-1$
				+ " *\n" //$NON-NLS-1$
				+ " * @param crosstab\n" //$NON-NLS-1$
				+ " *            ICrosstab\n" //$NON-NLS-1$
				+ " * @param reportContext\n" //$NON-NLS-1$
				+ " *            IReportContext\n" //$NON-NLS-1$
				+ " */\n"); //$NON-NLS-1$

		javaDoc.put("onPrepareCell", "/**\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ " * Called when crosstab cell is being prepared.\n" //$NON-NLS-1$
				+ " *\n" //$NON-NLS-1$
				+ " * @param cell\n" //$NON-NLS-1$
				+ " *            ICrosstabCell\n" //$NON-NLS-1$
				+ " * @param reportContext\n" //$NON-NLS-1$
				+ " *            IReportContext\n" //$NON-NLS-1$
				+ " */\n"); //$NON-NLS-1$

		javaDoc.put("onCreateCrosstab", "/**\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ " * Called when crosstab is being created.\n" //$NON-NLS-1$
				+ " *\n" //$NON-NLS-1$
				+ " * @param crosstabInst\n" //$NON-NLS-1$
				+ " *            ICrosstabInstance\n" //$NON-NLS-1$
				+ " * @param reportContext\n" //$NON-NLS-1$
				+ " *            IReportContext\n" //$NON-NLS-1$
				+ " */\n"); //$NON-NLS-1$

		javaDoc.put("onCreateCell", "/**\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ " * Called when crosstab cell is being created.\n" //$NON-NLS-1$
				+ " *\n" //$NON-NLS-1$
				+ " * @param cellInst\n" //$NON-NLS-1$
				+ " *            ICrosstabCellInstance\n" //$NON-NLS-1$
				+ " * @param reportContext\n" //$NON-NLS-1$
				+ " *            IReportContext\n" //$NON-NLS-1$
				+ " */\n"); //$NON-NLS-1$

		javaDoc.put("onRenderCrosstab", "/**\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ " * Called when crosstab is being rendered.\n" //$NON-NLS-1$
				+ " *\n" //$NON-NLS-1$
				+ " * @param crosstabInst\n" //$NON-NLS-1$
				+ " *            ICrosstabInstance\n" //$NON-NLS-1$
				+ " * @param reportContext\n" //$NON-NLS-1$
				+ " *            IReportContext\n" //$NON-NLS-1$
				+ " */\n"); //$NON-NLS-1$

		javaDoc.put("onRenderCell", "/**\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ " * Called when crosstab cell is being rendered.\n" //$NON-NLS-1$
				+ " *\n" //$NON-NLS-1$
				+ " * @param cellInst\n" //$NON-NLS-1$
				+ " *            ICrosstabCellInstance\n" //$NON-NLS-1$
				+ " * @param reportContext\n" //$NON-NLS-1$
				+ " *            IReportContext\n" //$NON-NLS-1$
				+ " */\n"); //$NON-NLS-1$
	}
}
