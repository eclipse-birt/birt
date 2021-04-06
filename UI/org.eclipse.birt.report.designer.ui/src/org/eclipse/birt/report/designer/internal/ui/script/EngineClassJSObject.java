/* Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

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
 * EngineClassJSObject
 */
class EngineClassJSObject implements JSObjectMetaData {

	private static JSMethod[] noMthods = new JSMethod[0];
	private static JSField[] noFields = new JSField[0];

	private IClassInfo classInfo;
	private boolean showPublic;

	public EngineClassJSObject(IClassInfo classInfo) {
		this.classInfo = classInfo;
		// if type is DataSet or DataSource
		// show all methods and fields
		showPublic = "DataSet".equals(classInfo.getName()) //$NON-NLS-1$
				|| "DataSource".equals(classInfo.getName()); //$NON-NLS-1$
	}

	public String getName() {
		if (classInfo == null)
			return ""; //$NON-NLS-1$
		return classInfo.getName();
	}

	// FIXME cache it.
	public JSMethod[] getMethods() {
		if (classInfo == null)
			return noMthods;
		List methods = classInfo.getMethods();
		List all = new ArrayList();
		for (Iterator iter = methods.iterator(); iter.hasNext();) {
			IMethodInfo method = (IMethodInfo) iter.next();
			if (showPublic || method.isStatic())
				all.add(new EngineClassMethod(method));
		}
		Collections.sort(all);
		return (JSMethod[]) all.toArray(new JSMethod[all.size()]);
	}

	public JSField[] getFields() {
		if (classInfo == null)
			return noFields;
		List members = classInfo.getMembers();
		List all = new ArrayList();
		for (Iterator iter = members.iterator(); iter.hasNext();) {
			IMemberInfo member = (IMemberInfo) iter.next();
			if (showPublic || member.isStatic())
				all.add(new EngineClassField(member));
		}
		Collections.sort(all);
		return (JSField[]) all.toArray(new JSField[all.size()]);
	}

	public String getDescription() {
		return null;
	}

	public int getVisibility() {
		return 0;
	}

	public JSObjectMetaData getComponentType() {
		return null;
	}

	/**
	 * EngineClassMethod
	 */
	class EngineClassMethod implements JSMethod, Comparable {

		private IMethodInfo method;
		private String displayText;

		public EngineClassMethod(IMethodInfo method) {
			this.method = method;
		}

		public String getName() {
			return method.getDisplayName();
		}

		public JSObjectMetaData getReturn() {
			JSObjectMetaData meta = JSSyntaxContext.getEnginJSObject(method.getReturnType());
			if (meta == null) {
				try {
					return JSSyntaxContext.getJavaClassMeta(method.getReturnType());
				} catch (ClassNotFoundException e) {
					return null;
				}
			}
			return meta;
		}

		public JSObjectMetaData[] getArguments() {
			// TODO impl real argument info, currently simply use argument type

			Iterator itr = method.argumentListIterator();

			if (itr != null && itr.hasNext()) {
				// only process first arguemnt list
				IArgumentInfoList ail = (IArgumentInfoList) itr.next();

				List<JSObjectMetaData> args = new ArrayList<JSObjectMetaData>();

				for (Iterator aitr = ail.argumentsIterator(); aitr.hasNext();) {
					IArgumentInfo aif = (IArgumentInfo) aitr.next();

					JSObjectMetaData meta = JSSyntaxContext.getEnginJSObject(aif.getType());
					if (meta == null) {
						try {
							meta = JSSyntaxContext.getJavaClassMeta(aif.getType());
						} catch (ClassNotFoundException e) {
						}
					}

					if (meta != null) {
						args.add(meta);
					}
				}

				return args.toArray(new JSObjectMetaData[args.size()]);
			}
			return null;
		}

		public String getDisplayText() {
			if (displayText == null) {
				StringBuffer strbuf = new StringBuffer(getName());
				strbuf.append("("); //$NON-NLS-1$
				for (Iterator iter = method.argumentListIterator(); iter.hasNext();) {
					IArgumentInfoList element = (IArgumentInfoList) iter.next();
					int i = 0;
					for (Iterator iterator = element.argumentsIterator(); iterator.hasNext();) {
						if (i++ > 0) {
							strbuf.append(", "); //$NON-NLS-1$
						}
						IArgumentInfo argument = (IArgumentInfo) iterator.next();
						if (argument.getType() != null) {
							strbuf.append(argument.getType()).append(" "); //$NON-NLS-1$
						}
						strbuf.append(argument.getName());
					}
					break;
				}
				strbuf.append(") "); //$NON-NLS-1$
				strbuf.append(classInfo.getName());
				displayText = strbuf.toString();
				strbuf = null;
			}
			return displayText;
		}

		public String getDescription() {
			return null;
		}

		public int getVisibility() {
			if (method.isStatic())
				return EngineClassJSObject.VISIBILITY_STATIC;
			return 0;
		}

		public int compareTo(Object obj) {
			if (obj instanceof EngineClassMethod && ((EngineClassMethod) obj).getName() != null) {
				return getName().compareToIgnoreCase(((EngineClassMethod) obj).getName());
			}
			return 0;
		}

	}

	/**
	 * EngineClassField
	 */
	class EngineClassField implements JSField, Comparable {

		private IMemberInfo member;
		private String displayText;

		public EngineClassField(IMemberInfo member) {
			this.member = member;
		}

		public String getName() {
			return member.getDisplayName();
		}

		public JSObjectMetaData getType() {
			JSObjectMetaData meta = JSSyntaxContext.getEnginJSObject(member.getDataType());
			if (meta == null)
				try {
					return JSSyntaxContext.getJavaClassMeta(member.getDataType());
				} catch (ClassNotFoundException e) {
					return null;
				}
			return meta;
		}

		public String getDisplayText() {
			if (displayText == null) {
				StringBuffer strbuf = new StringBuffer(getName());
				strbuf.append(" "); //$NON-NLS-1$
				strbuf.append(classInfo.getName());
				displayText = strbuf.toString();
				strbuf = null;
			}
			return displayText;
		}

		public String getDescription() {
			return null;
		}

		public int getVisibility() {
			return 0;
		}

		public int compareTo(Object obj) {
			if (obj instanceof EngineClassField && ((EngineClassField) obj).getName() != null) {
				return getName().compareToIgnoreCase(((EngineClassField) obj).getName());
			}
			return 0;
		}
	}

}
