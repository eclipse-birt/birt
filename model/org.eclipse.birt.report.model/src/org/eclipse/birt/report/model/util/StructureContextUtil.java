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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;

/**
 *
 */

public class StructureContextUtil {

	/**
	 * Sets structure context.
	 * 
	 * @param propDefn the property define
	 * @param tmpValue the cloned value
	 * @param element  the design element.
	 */
	public static void setStructureContext(ElementPropertyDefn propDefn, Object tmpValue, DesignElement element) {
		assert propDefn != null;
		assert element != null;

		if (tmpValue == null)
			return;

		// only handle the case that this is a structure or structure list.

		if (propDefn.getTypeCode() != IPropertyType.STRUCT_TYPE) {
			return;
		}

		if (propDefn.isList()) {
			List values = (List) tmpValue;
			for (int i = 0; i < values.size(); i++) {
				Structure item = (Structure) values.get(i);
				item.setContext(new StructureContext(element, propDefn, item));
			}
		} else if (tmpValue instanceof Structure) {
			((Structure) tmpValue).setContext(new StructureContext(element, propDefn, (Structure) tmpValue));
		}
	}

	/**
	 * Establishes the structure context for the given structure or any nested
	 * structure.
	 * 
	 * @param struct the structure to setup
	 * 
	 */

	public static void setupStructureContext(Structure struct) {
		if (struct == null)
			return;
		Iterator<IPropertyDefn> members = struct.getDefn().propertiesIterator();
		while (members.hasNext()) {
			PropertyDefn member = (PropertyDefn) members.next();
			if (member.getTypeCode() != IPropertyType.STRUCT_TYPE)
				continue;

			Object tmpValue = struct.getLocalProperty(null, member);
			if (tmpValue == null)
				continue;
			if (tmpValue instanceof List) {
				List tmpList = (List) tmpValue;
				for (int i = 0; i < tmpList.size(); i++) {
					Structure child = (Structure) tmpList.get(i);
					child.setContext(new StructureContext(struct, member, child));
					setupStructureContext(child);
				}

				continue;
			}

			Structure child = (Structure) tmpValue;
			child.setContext(new StructureContext(struct, member, child));
			setupStructureContext(child);
		}

	}

	/**
	 * Creates a structure context for the member of the given structure handle.
	 * 
	 * @param structHandle
	 * @param memberName
	 * @return
	 */
	public static StructureContext createStructureContext(StructureHandle structHandle, String memberName) {
		if (structHandle == null)
			return null;

		Structure struct = (Structure) structHandle.getStructure();
		if (struct == null)
			return null;
		PropertyDefn propDefn = (PropertyDefn) struct.getMemberDefn(memberName);

		if (propDefn == null)
			return null;

		StructureContext context = new StructureContext(struct, propDefn, null);
		return context;
	}

	/**
	 * 
	 * @param target
	 * @param context
	 * @return
	 */
	public static StructureContext getLocalStructureContext(Module targetModule, DesignElement target,
			StructureContext context) {
		// return null if either target is null or context is null
		if (target == null || context == null)
			return null;

		// if the top element of the context is just the target itself, it means
		// the context is local and return it directly
		if (target == context.getElement())
			return context;

		ElementPropertyDefn propDefn = context.getElementProp();
		// for overridden property case, we should get definition locally
		propDefn = target.getPropertyDefn(propDefn.getName());

		// if target has no local value, return directly
		if (target.getLocalProperty(targetModule, propDefn) == null)
			return context;

		List<StructureContext> contextList = new ArrayList<StructureContext>();
		StructureContext tmpContext = context;
		while (tmpContext != null) {
			// every time, add the context to the top
			contextList.add(0, tmpContext);
			tmpContext = tmpContext.getParentContext();
		}

		assert contextList.size() > 0;

		Structure targetStruct = null;
		for (int i = 0; i < contextList.size(); i++) {
			tmpContext = contextList.get(i);
			StructureContext targetContext = null;

			if (i == 0) {
				targetContext = new StructureContext(target, propDefn, null);
			} else {
				assert targetStruct != null;
				targetContext = new StructureContext(targetStruct, tmpContext.getPropDefn(), null);
			}

			targetStruct = getTargetStructure(targetModule, targetContext, tmpContext);

			// cache structure
			if (targetStruct != null) {
				targetContext = targetContext.cacheStructure(targetStruct);
			}
			if (tmpContext == context)
				return targetContext;

		}

		assert false;
		return null;

	}

	private static Structure getTargetStructure(Module module, StructureContext targetContext,
			StructureContext sourceContext) {
		assert targetContext != null;
		assert sourceContext != null;
		assert targetContext.getPropDefn() == sourceContext.getPropDefn();

		if (sourceContext.getStructure() == null)
			return null;
		int index = sourceContext.getIndex(null);
		return targetContext.getStructureAt(module, index);
	}

	public static StructureContext getMemberContext(StructureHandle structHandle, StructPropertyDefn member) {
		if (structHandle == null || member == null)
			return null;

		StructureContext context = structHandle.getContext();
		DesignElement target = structHandle.getElement();
		ElementPropertyDefn propDefn = context.getElementProp();
		Module module = structHandle.getModule();
		Object localValue = target.getLocalProperty(module, propDefn);
		if (localValue == null) {
			Structure struct = (Structure) structHandle.getStructure();
			assert struct != null;
			return new StructureContext(struct, member, null);
		} else {
			StructureContext targetContext = getLocalStructureContext(module, target, context);
			assert targetContext != null;
			Structure struct = targetContext.getStructure();
			assert struct != null;
			return new StructureContext(struct, member, null);

		}
	}

	/**
	 * Determines whether the cached structure handle is valid or not. If the
	 * structure is not in the design tree any more, then the handle is invalid;
	 * otherwise valid.
	 * 
	 * @param structHandle
	 * @return
	 */
	public static boolean isValidStructureHandle(StructureHandle structHandle) {
		if (structHandle == null)
			return true;
		Structure struct = (Structure) structHandle.getStructure();
		if (struct == null)
			return false;

		StructureContext context = struct.getContext();

		// the local structure is dropped and therefore the context is null, at
		// this case the cached structure handle is invalid for the structure is
		// not in design tree
		if (context == null)
			return false;

		DesignElement hostElement = context.getElement();
		DesignElement content = struct.getElement();
		if (content == hostElement)
			return true;

		if (hostElement == null)
			return false;

		// if the two element are not the same, then justify the inheritance
		Object localValue = content.getLocalProperty(structHandle.getModule(), context.getElementProp());
		if (localValue != null)
			return false;

		return true;
	}

}
