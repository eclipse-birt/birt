/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import org.eclipse.birt.data.oda.pojo.ui.impl.models.TreeData;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ClassTreeContentProvider.ClassWrapper;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.birt.data.oda.pojo.util.ClassParser;

/**
 * 
 */

public class ClassTreeLabelProvider extends LabelProvider {

	public Image getImage(Object arg0) {
		if (arg0 instanceof TreeData) {
			Object obj = ((TreeData) arg0).getWrappedObject();

			if (obj instanceof ClassWrapper) {
				return Utils.getClassFlagImg();
			}
			if (obj instanceof Field) {
				return Utils.getFieldFlagImg();
			}
			if (obj instanceof Method) {
				return Utils.getMethodFlagImg();
			}
			if (obj instanceof String) {
				return Utils.getWarningFlagImg();
			}
		}
		return null;
	}

	public String getText(Object arg0) {
		if (arg0 instanceof TreeData) {
			Object obj = ((TreeData) arg0).getWrappedObject();

			if (obj instanceof ClassWrapper) {
				return ((ClassWrapper) obj).getWrappedClass().getName();
			}
			if (obj instanceof Field) {
				return ((Field) obj).getName();
			}
			if (obj instanceof Method) {
				return ((Method) obj).getName() + "(" //$NON-NLS-1$
						+ ClassParser.getParametersLabel((Method) obj) + ")"; //$NON-NLS-1$
			}
			if (obj instanceof String) {
				return (String) obj;
			}
		}
		return ""; //$NON-NLS-1$
	}
}
