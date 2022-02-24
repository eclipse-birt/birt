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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * The structure context. It is used when establishes back reference.
 * 
 */

public class StructureContext {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(StructureContext.class.getName());

	protected final ContainerInfo containerInfo;

	/**
	 * The structure object that this context refers. The structure must set this
	 * context instance as its container context.
	 */
	protected final Structure struct;

	private StructureContext(ContainerInfo info, Structure struct) {
		this.containerInfo = info;
		this.struct = struct;
	}

	/**
	 * Constructs the structure context.
	 * 
	 * @param element         the design element
	 * @param elementPropDefn the property definition
	 * @param obj             the structure that is the value of the propDefn
	 */

	public StructureContext(DesignElement element, ElementPropertyDefn elementPropDefn, Structure obj) {
		this.containerInfo = new ElementContainerInfo(element, elementPropDefn);

		this.struct = obj;

	}

	/**
	 * Constructs the structure context.
	 * 
	 * @param struct   the structure
	 * @param propDefn the member definition
	 * @param obj      the structure that is the value of the propDefn
	 */

	public StructureContext(IStructure struct, PropertyDefn propDefn, Structure obj) {
		this.containerInfo = new StructureContainerInfo(struct, propDefn);

		this.struct = obj;
	}

	/**
	 * Adds the structure to the context.
	 * 
	 * @param struct the structure
	 */

	public void add(Structure struct) {
		add(-1, struct);
	}

	/**
	 * Adds the structure to the context with the given position.
	 * 
	 * @param index  the position
	 * @param struct the structure
	 */

	public void add(int index, Structure struct) {
		Object values = getLocalValue();

		if (containerInfo.getPropDefn().isList()) {
			if (values == null) {
				values = new ArrayList<Object>();
				containerInfo.setValue(values);
			}

			if (index == -1)
				index = ((List<Object>) values).size();

			((List<Object>) values).add(index, struct);
		} else {
			// the property value is the structure, not the structure list.

			assert values == null;

			containerInfo.setValue(struct);
		}

		// create a new context of this copy and then cache the structure member
		// in it, we can not directly set this context instance as the context
		// of the inserted structure
		StructureContext context = new StructureContext(containerInfo.getCopy(), struct);
		struct.setContext(context);
	}

	public StructureContext cacheStructure(Structure struct) {
		return new StructureContext(containerInfo.getCopy(), struct);
	}

	/**
	 * Gets the structure at the position of this context. If the value of this
	 * context is a structure list, return the item in the specified index; if the
	 * value is structure and given position is 0, then return the structure
	 * directly; otherwise return null.
	 * 
	 * @param module
	 * @param posn
	 * @return
	 */
	public Structure getStructureAt(Module module, int posn) {
		Object value = getValue(module);
		if (value instanceof List) {
			List listValue = (List) value;
			if (posn < 0 || posn >= listValue.size())
				return null;
			Object item = listValue.get(posn);
			return item instanceof Structure ? (Structure) item : null;
		} else if (value instanceof Structure) {
			return posn == 0 ? (Structure) value : null;
		}
		return null;
	}

	/**
	 * Removes the structure from the context.
	 * 
	 * @param struct the structure
	 */

	public void remove(Structure struct) {
		Object values = getLocalValue();

		assert values != null;

		if (containerInfo.getPropDefn().isList()) {
			List<Object> list = (List<Object>) values;
			int index = list.indexOf(struct);
			assert index != -1;

			list.remove(index);
		} else {
			// the property value is the structure, not the structure list.

			assert values == struct;

			containerInfo.setValue(null);
		}

		struct.setContext(null);
	}

	/**
	 * Clears the local value set in this structure context.
	 */
	public void clearValue() {
		containerInfo.setValue(null);
	}

	/**
	 * Removes the structure from the context.
	 * 
	 * @param index the position
	 */

	public void remove(int index) {
		Object values = getLocalValue();

		assert values != null;

		Structure struct = null;

		if (containerInfo.getPropDefn().isList()) {
			List list = (List) values;

			struct = (Structure) list.get(index);
			list.remove(index);
		} else {
			assert false;
		}

		assert struct != null;
		struct.setContext(null);
	}

	/**
	 * @return the dataContainer
	 */

	public Object getValueContainer() {
		return containerInfo.getContainer();
	}

	/**
	 * @return the elementPropName
	 */

	public PropertyDefn getPropDefn() {
		return containerInfo.getPropDefn();
	}

	/**
	 * Gets the structure definition of this context.If the property definition of
	 * this context is structure type, then return the detail structure definition
	 * of it; otherwise return null.
	 * 
	 * @return the structure definition of this context
	 */
	public IStructureDefn getStructDefn() {
		PropertyDefn propDefn = getPropDefn();
		if (propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE)
			return propDefn.getStructDefn();
		return null;
	}

	/**
	 * Returns the index in this list of the first occurrence of the structure that
	 * this context refers, or -1 if this list does not contain this element.
	 * 
	 * @param module
	 * @return the 0-based index where the structure resides in the list
	 */
	public int getIndex(Module module) {
		Structure struct = getStructure();
		if (struct == null)
			return -1;
		Object value = getValue(module);
		if (value instanceof Structure) {
			assert struct == value;
			return 0;
		} else if (value instanceof List) {
			return ((List) value).indexOf(struct);

		}
		return -1;

	}

	/**
	 * @return the elementPropName
	 */

	public ElementPropertyDefn getElementProp() {
		ElementContainerInfo elementContainerInfo = getElementContainerInfo();
		return elementContainerInfo == null ? null : (ElementPropertyDefn) elementContainerInfo.getPropDefn();
	}

	/**
	 * Returns the element that contains this context.
	 * 
	 * @return the element where the top level structure resides
	 */

	public DesignElement getElement() {
		ElementContainerInfo elementContainerInfo = getElementContainerInfo();
		return elementContainerInfo == null ? null : (DesignElement) elementContainerInfo.getContainer();
	}

	/**
	 * Gets the top level element container information of this structure context if
	 * found, otherwise return null.
	 * 
	 * @return the element container information if found, otherwise null
	 */
	private ElementContainerInfo getElementContainerInfo() {
		StructureContext tmpContext = this;
		while (tmpContext != null && tmpContext.containerInfo.getType() != ContainerInfo.ELEMENT_CONTAINER_TYPE) {
			tmpContext = ((Structure) tmpContext.getValueContainer()).getContext();
		}

		if (tmpContext != null) {
			assert tmpContext.containerInfo instanceof ElementContainerInfo;
			return (ElementContainerInfo) tmpContext.containerInfo;
		}

		return null;
	}

	/**
	 * Returns the local value of the context.
	 * 
	 * @param root the module
	 * @return the value
	 */

	public Object getLocalValue(Module root) {
		return containerInfo.getLocalValue(root);
	}

	/**
	 * Returns the local value of the context.
	 * 
	 * @param root the module
	 * @return the value
	 */

	private Object getLocalValue() {
		return containerInfo.getLocalValue();
	}

	/**
	 * 
	 * @param module
	 * @return
	 */
	public Object getValue(Module module) {
		DesignElement element = getElement();
		Module root = null;
		if (element != null) {
			root = element.getRoot();
			if (root == null)
				root = module;
		}
		return containerInfo.getValue(root);
	}

	/**
	 * Gets the focus structure of this structure context. If the cached structure
	 * is not null, it means that it is just the focus one to return; Otherwise it
	 * means this context refers to the member of the focus structure, therefore in
	 * such case, return the container that is just the focus one.
	 * 
	 * @return
	 */
	public Structure getStructure() {
		if (struct != null)
			return struct;

		Object container = containerInfo.getContainer();
		if (container instanceof Structure)
			return (Structure) container;

		return null;
	}

	/**
	 * Returns the nearest list pointed to by this context.
	 * 
	 * @return the list of structures
	 */

	public List getList(Module module) {
		if (struct != null) {
			Object value = containerInfo.getValue(module);
			return value instanceof List ? (List) value : null;
		}

		// else structure is not cache
		PropertyDefn propDefn = getPropDefn();
		if (propDefn.isListType()) {
			return (List) getValue(module);
		}

		Object container = containerInfo.getContainer();
		if (container instanceof DesignElement) {
			return null;
		} else if (container instanceof Structure) {
			Structure struct = (Structure) container;
			StructureContext context = struct.getContext();
			if (context != null) {
				Object value = context.containerInfo.getValue(module);
				return value instanceof List ? (List) value : null;
			}
		}
		return null;

	}

	/**
	 * Indicates whether this member reference points to a list.
	 * 
	 * @return true if points to a list.
	 */

	public boolean isListRef() {
		PropertyDefn propDefn = getPropDefn();
		return propDefn.isListType();
	}

	/**
	 * Gets the parent context of this context if exists.
	 * 
	 * @return
	 */
	public StructureContext getParentContext() {
		Object container = getValueContainer();
		if (container instanceof DesignElement)
			return null;
		return ((Structure) container).getContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof StructureContext))
			return false;
		StructureContext context = (StructureContext) obj;
		if (containerInfo.equals(context.containerInfo)
				&& ((struct == null && context.struct == null) || (struct != null && struct.equals(context.struct))))
			return true;
		return false;
	}

	abstract class ContainerInfo {

		/**
		 * The type that indicates that the structure resides in the element property.
		 */

		protected static final int ELEMENT_CONTAINER_TYPE = 0;

		/**
		 * The type that indicates that the structure resides in the structure member.
		 */

		protected static final int STRUCTURE_CONTAINER_TYPE = 1;

		/**
		 * 
		 * @return
		 */
		abstract int getType();

		/**
		 * Gets the value of this container information.
		 * 
		 * @return
		 */
		abstract Object getValue(Module module);

		/**
		 * 
		 * @return
		 */
		abstract Object getContainer();

		/**
		 * 
		 * @return
		 */
		abstract PropertyDefn getPropDefn();

		abstract Object getLocalValue();

		/**
		 * Gets the local value of this container information.
		 * 
		 * @param module
		 * @return
		 */
		abstract Object getLocalValue(Module module);

		abstract void setValue(Object value);

		/**
		 * Gets the copied info from the given container information.
		 * 
		 * @param sourceInfo
		 * @return
		 */
		abstract ContainerInfo getCopy();
	}

	class ElementContainerInfo extends ContainerInfo {

		protected final DesignElement container;
		protected final ElementPropertyDefn propDefn;

		ElementContainerInfo(DesignElement element, ElementPropertyDefn propDefn) {
			if (propDefn == null) {
				throw new IllegalArgumentException();
			}

			container = element;
			this.propDefn = propDefn;

			IPropertyDefn tmpPropDefn = container.getPropertyDefn(this.propDefn.getName());

			if (tmpPropDefn == null) {
				logger.warning("cannot get property definition " + propDefn //$NON-NLS-1$
						+ " for element " + container.getName()); //$NON-NLS-1$

				throw new IllegalArgumentException();
			}

			if (this.propDefn != tmpPropDefn) {
				logger.warning("property definitions: " //$NON-NLS-1$
						+ this.propDefn.getName() + " and " //$NON-NLS-1$
						+ tmpPropDefn.getName() + " are different. "); //$NON-NLS-1$

				throw new IllegalArgumentException("property definitions: " //$NON-NLS-1$
						+ this.propDefn.getName() + " and " //$NON-NLS-1$
						+ tmpPropDefn.getName() + " are different. "); //$NON-NLS-1$
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getType()
		 */
		int getType() {
			return ELEMENT_CONTAINER_TYPE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getContainer()
		 */
		Object getContainer() {
			return container;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getPropDefn()
		 */
		PropertyDefn getPropDefn() {
			return propDefn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getLocalValue()
		 */
		Object getLocalValue() {
			return getLocalValue(container.getRoot());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getLocalValue(org.eclipse.birt.report.model.core.Module)
		 */
		Object getLocalValue(Module module) {
			return container.getLocalProperty(module, propDefn);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getValue(org.eclipse.birt.report.model.core.Module)
		 */
		Object getValue(Module module) {
			return container.getProperty(module, propDefn);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #setValue(java.lang.Object)
		 */
		void setValue(Object value) {
			container.setProperty(propDefn, value);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getCopy()
		 */
		ContainerInfo getCopy() {
			return new ElementContainerInfo(container, propDefn);
		}
	}

	class StructureContainerInfo extends ContainerInfo {

		protected final Structure container;
		protected final PropertyDefn propDefn;

		StructureContainerInfo(IStructure struct, PropertyDefn propDefn) {
			if (struct instanceof Structure)
				container = (Structure) struct;
			else
				throw new IllegalArgumentException();
			this.propDefn = propDefn;
			if (propDefn == null || !propDefn.equals(container.getMemberDefn(propDefn.getName())))
				throw new IllegalArgumentException();
		}

		int getType() {
			return STRUCTURE_CONTAINER_TYPE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getContainer()
		 */
		Object getContainer() {
			return container;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getPropDefn()
		 */
		PropertyDefn getPropDefn() {
			return propDefn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getValue(org.eclipse.birt.report.model.core.Module)
		 */
		Object getValue(Module module) {
			return container.getProperty(module, propDefn);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getLocalValue()
		 */
		Object getLocalValue() {
			return getLocalValue(null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getLocalValue(org.eclipse.birt.report.model.core.Module)
		 */
		Object getLocalValue(Module module) {
			return container.getLocalProperty(module, propDefn);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #setValue(java.lang.Object)
		 */
		void setValue(Object value) {
			container.setProperty(propDefn, value);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.StructureContext.ContainerInfo
		 * #getCopy()
		 */
		ContainerInfo getCopy() {
			return new StructureContainerInfo(container, propDefn);
		}
	}
}
