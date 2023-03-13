/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * ExtensionClassJSObject
 */
public class ExtensionClassJSObject implements JSObjectMetaData {

	private IClassInfo classInfo;
	private JSMethod[] cacheMethods;
	private JSField[] cacheFields;

	public ExtensionClassJSObject(IClassInfo classInfo) {
		this.classInfo = classInfo;
	}

	@Override
	public String getName() {
		return classInfo.getName();
	}

	@Override
	public JSMethod[] getMethods() {
		if (cacheMethods == null) {
			List jsMethods = new ArrayList();
			List methods = classInfo.getMethods();

			for (int i = 0; i < methods.size(); i++) {
				jsMethods.add(new ExtensionClassMethod((IMethodInfo) methods.get(i)));
			}

			Collections.sort(jsMethods);

			cacheMethods = (JSMethod[]) jsMethods.toArray(new JSMethod[jsMethods.size()]);
		}

		return cacheMethods;
	}

	@Override
	public JSField[] getFields() {
		if (cacheFields == null) {
			List jsFields = new ArrayList();
			List members = classInfo.getMembers();

			for (int i = 0; i < members.size(); i++) {
				jsFields.add(new ExtensionClassField((IMemberInfo) members.get(i)));
			}

			Collections.sort(jsFields);

			cacheFields = (JSField[]) jsFields.toArray(new JSField[jsFields.size()]);
		}

		return cacheFields;
	}

	@Override
	public String toString() {
		return classInfo.getName();
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public int getVisibility() {
		return VISIBILITY_PUBLIC;
	}

	private Object getSimpleName(String name) {
		return name.substring(name.lastIndexOf(".") + 1); //$NON-NLS-1$
	}

	@Override
	public JSObjectMetaData getComponentType() {
		return null;
	}

	/**
	 * ExtensionClassMethod
	 */
	private class ExtensionClassMethod implements JSMethod, Comparable {

		private JSObjectMetaData returnType;
		private IMethodInfo method;
		private String displayText;

		public ExtensionClassMethod(IMethodInfo method) {
			this.method = method;

			IClassInfo typeInfo = method.getClassReturnType();
			if (typeInfo != null) {
				returnType = new ExtensionClassJSObject(typeInfo);
			}
		}

		@Override
		public String getName() {
			return method.getName();
		}

		@Override
		public JSObjectMetaData getReturn() {
			return returnType;
		}

		@Override
		public JSObjectMetaData[] getArguments() {
			// TODO impl real argument info, currently simply use argument type

			Iterator itr = method.argumentListIterator();

			if (itr != null && itr.hasNext()) {
				// only process first arguemnt list
				IArgumentInfoList ail = (IArgumentInfoList) itr.next();

				List<JSObjectMetaData> args = new ArrayList<>();

				for (Iterator aitr = ail.argumentsIterator(); aitr.hasNext();) {
					IArgumentInfo aif = (IArgumentInfo) aitr.next();

					args.add(new ExtensionClassJSObject(aif.getClassType()));
				}

				return args.toArray(new JSObjectMetaData[args.size()]);
			}
			return null;
		}

		@Override
		public String getDisplayText() {
			if (displayText == null) {
				StringBuilder strbuf = new StringBuilder(getName());
				strbuf.append("("); //$NON-NLS-1$
				Iterator argListItr = method.argumentListIterator();

				if (argListItr.hasNext()) {
					IArgumentInfoList ail = (IArgumentInfoList) argListItr.next();

					int i = 0;
					for (Iterator itr = ail.argumentsIterator(); itr.hasNext();) {
						if (i > 0) {
							strbuf.append(", "); //$NON-NLS-1$
						}

						IArgumentInfo ai = (IArgumentInfo) itr.next();
						strbuf.append(getSimpleName(ai.getType())).append(" ") //$NON-NLS-1$
								.append("arg") //$NON-NLS-1$
								.append(i + 1);

						i++;
					}
				}
				strbuf.append(") "); //$NON-NLS-1$
				strbuf.append(getSimpleName(method.getReturnType()));
				strbuf.append(" - "); //$NON-NLS-1$
				strbuf.append(getSimpleName(classInfo.getName()));
				displayText = strbuf.toString();
				strbuf = null;
			}
			return displayText;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public int getVisibility() {
			return VISIBILITY_PUBLIC;
		}

		@Override
		public int compareTo(Object obj) {
			if (obj instanceof ExtensionClassMethod && ((ExtensionClassMethod) obj).getName() != null) {
				return getName().compareToIgnoreCase(((ExtensionClassMethod) obj).getName());
			}
			return 0;
		}

	}

	/**
	 * ExtensionClassField
	 */
	private class ExtensionClassField implements JSField, Comparable {

		private JSObjectMetaData type;
		private String typeName;
		private IMemberInfo field;
		private String displayText;

		public ExtensionClassField(IMemberInfo field) {
			this.field = field;
			this.typeName = field.getDataType();

			IClassInfo typeInfo = field.getClassType();
			if (typeInfo != null) {
				type = new ExtensionClassJSObject(typeInfo);
			}
		}

		@Override
		public String getName() {
			return field.getName();
		}

		@Override
		public JSObjectMetaData getType() {
			return type;
		}

		@Override
		public String getDisplayText() {
			if (displayText == null) {
				StringBuilder strbuf = new StringBuilder(getName());
				strbuf.append(" "); //$NON-NLS-1$
				if (field != null && field.getDataType() != null) {
					strbuf.append(getSimpleName(field.getDataType()));
					strbuf.append(" - "); //$NON-NLS-1$
				} else if (typeName != null) {
					strbuf.append(getSimpleName(typeName));
					strbuf.append(" - "); //$NON-NLS-1$
				} else if (type != null) {
					strbuf.append(getSimpleName(type.getName()));
					strbuf.append(" - "); //$NON-NLS-1$
				}

				strbuf.append(getSimpleName(classInfo.getName()));

				displayText = strbuf.toString();
				strbuf = null;
			}
			return displayText;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public int getVisibility() {
			return VISIBILITY_PUBLIC;
		}

		@Override
		public int compareTo(Object obj) {
			if (obj instanceof ExtensionClassField && ((ExtensionClassField) obj).getName() != null) {
				return getName().compareToIgnoreCase(((ExtensionClassField) obj).getName());
			}
			return 0;
		}

	}

}
