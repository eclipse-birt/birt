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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Base class for property structures. Implements the two "boiler-plate" methods
 * from the IStructure interface.
 *
 */

public abstract class Structure implements IStructure {

	private StructureContext context;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Structure)) {
			return false;
		}

		Structure struct = (Structure) obj;
		if (struct.getDefn() != getDefn()) {
			return false;
		}

		Iterator<IPropertyDefn> iter = getDefn().propertiesIterator();
		while (iter.hasNext()) {
			PropertyDefn defn = (PropertyDefn) iter.next();
			Object value = getLocalProperty(null, defn);
			if (!Objects.equals(value, struct.getLocalProperty(null, defn))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets a copy of this structure.
	 *
	 * @return the copied structure.
	 *
	 */

	@Override
	public final IStructure copy() {
		try {
			Structure retValue = (Structure) clone();
			StructureContextUtil.setupStructureContext(retValue);
			retValue.context = null;
			return retValue;
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		return null;
	}

	/**
	 * Gets the structure definition by the name of this structure.
	 *
	 * @return structure definition.
	 */

	@Override
	public IStructureDefn getDefn() {
		return MetaDataDictionary.getInstance().getStructure(getStructName());
	}

	/**
	 * Gets the definition for the member with the given name. Return the definition
	 * if the member is found in this structure definition, otherwise return null.
	 *
	 * @param memberName name of the member to find
	 * @return the member definition if found, otherwise return null
	 */
	public IPropertyDefn getMemberDefn(String memberName) {
		return (PropertyDefn) getDefn().getMember(memberName);
	}

	/**
	 * Gets the object definition of this structure.
	 *
	 * @return the structure definition returned.
	 *
	 */

	@Override
	public IObjectDefn getObjectDefn() {
		return MetaDataDictionary.getInstance().getStructure(getStructName());
	}

	/**
	 * Gets the value of the member. If the value has not been set, the default
	 * value for the member will be returned.
	 *
	 * @param module     the module
	 * @param memberName the member name
	 * @return the member is defined on the structure and the value is set,
	 *         otherwise null.
	 */

	@Override
	public final Object getProperty(Module module, String memberName) {
		PropertyDefn prop = (PropertyDefn) getDefn().getMember(memberName);
		if (prop == null) {
			return null;
		}

		return getProperty(module, prop);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IPropertySet#getProperty(org.eclipse
	 * .birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	@Override
	public Object getProperty(Module module, PropertyDefn propDefn) {
		assert propDefn != null;
		Object value = getLocalProperty(module, propDefn);
		if (value == null) {
			return propDefn.getDefault();
		}

		if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			return ReferenceValueUtil.needTheNamespacePrefix(refValue, module);
		}

		return value;
	}

	/**
	 * Gets the locale value of a property.
	 *
	 * @param module   the module
	 *
	 * @param propDefn definition of the property to get
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 */

	@Override
	public Object getLocalProperty(Module module, PropertyDefn propDefn) {
		Object value = null;
		if (propDefn.isIntrinsic()) {
			value = getIntrinsicProperty(propDefn.getName());
		}

		if (propDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			return ReferenceValueUtil.resolveElementReference(this, module, (StructPropertyDefn) propDefn, value);
		}
		return value;
	}

	/**
	 * Gets the locale value of a property.
	 *
	 * @param module     the module
	 *
	 * @param memberName name of the member to get
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 */

	public Object getLocalProperty(Module module, String memberName) {
		PropertyDefn prop = (PropertyDefn) getDefn().getMember(memberName);
		if (prop == null) {
			return null;
		}

		return getLocalProperty(module, prop);
	}

	/**
	 * Sets the value of the member.
	 *
	 * @param propName the member name to set
	 * @param value    the value to set
	 */

	public final void setProperty(String propName, Object value) {
		PropertyDefn prop = (PropertyDefn) getDefn().getMember(propName);
		if (prop == null) {
			return;
		}

		setProperty(prop, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IPropertySet#setProperty(org.eclipse
	 * .birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	@Override
	public void setProperty(PropertyDefn prop, Object value) {
		Object updatedValue = getCompatibleValue(prop, value);

		updateReference(prop, updatedValue);
		setupContext(prop, updatedValue);

		if (prop.isIntrinsic()) {
			setIntrinsicProperty(prop.getName(), updatedValue);
		}

	}

	/**
	 * Updates back reference for element reference value.
	 *
	 * @param prop  the property
	 * @param value value of the property
	 */

	protected void updateReference(PropertyDefn prop, Object value) {
		if ((value instanceof ElementRefValue || value == null)
				&& prop.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			ElementRefValue oldRef = (ElementRefValue) getLocalProperty(null, prop.getName());
			doUpdateReference(oldRef, (ElementRefValue) value, prop);
		}
	}

	/**
	 * Sets up the context if the value to set is structure or structure list.
	 *
	 * @param prop
	 * @param value
	 */
	protected void setupContext(PropertyDefn prop, Object value) {
		if (prop.getTypeCode() == IPropertyType.STRUCT_TYPE) {
			if (prop.isList()) {
				if (value instanceof List) {
					List listValue = (List) value;
					for (Object item : listValue) {
						Structure struct = (Structure) item;
						struct.setContext(new StructureContext(this, prop, struct));
					}
				}
			} else if (value instanceof Structure) {
				((Structure) value).setContext(new StructureContext(this, prop, (Structure) value));
			}
		}
	}

	/**
	 * Implements to cache a back-pointer from a referenced element. This element
	 * has an element reference property that can point to another "referencable"
	 * element. To maintain semantic consistency, the referenced element maintains a
	 * list of "clients" that identifies the elements that refer to it. The client
	 * list is used when the target element changes names or is deleted. In these
	 * cases, the change automatically updates the clients as well.
	 * <p>
	 * References can be in two states: resovled and unresolved. An unresolved
	 * reference is just a name, but the system has not yet identified the target
	 * element, or if a target even exists. A resolved reference caches a pointer to
	 * the target element itself.
	 *
	 * @param oldRef the old reference, if any
	 * @param newRef the new reference, if any
	 * @param prop   definition of the property
	 */

	private void doUpdateReference(ElementRefValue oldRef, ElementRefValue newRef, PropertyDefn prop) {
		IReferencableElement target;

		String propName = prop.getName();

		// Drop the old reference. Clear the back pointer from the referenced
		// element to this element.

		if (oldRef != null) {
			target = oldRef.getTargetElement();
			if (target != null) {
				target.dropClient(this, propName);
			}
		}

		// Add the new reference. Cache a back pointer from the referenced
		// element to this element. Include the property name so we know which
		// property to adjust it the target is deleted.

		if (newRef != null) {
			target = newRef.getTargetElement();
			if (target != null) {
				target.addClient(this, propName);
			}
		}
	}

	/**
	 * Returns the value of a structure property represented as a member variable.
	 *
	 * @param propName name of the property
	 * @return the value of the property, or null if the property is not set
	 */

	protected abstract Object getIntrinsicProperty(String propName);

	/**
	 * Sets the value of of a structure property represented as a member variable.
	 *
	 * @param propName the name of the property to set
	 * @param value    the property value
	 */

	protected abstract void setIntrinsicProperty(String propName, Object value);

	/**
	 * Validate whether this structure is valid. The derived class should override
	 * this method if semantic check is needed. The each error is the instance of
	 * <code>PropertyValueException</code>.
	 *
	 * @param module  the module
	 * @param element the element contains this structure
	 * @return the semantic error list
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		return new ArrayList<>();
	}

	/**
	 * Gets the specific handle of this structure. This structure must be in the
	 * element's structure list. The structure handle is transient because the
	 * position in the structure list is kept. The position changes if any structure
	 * is added, or dropped. So this handle should not be kept.
	 *
	 * @param valueHandle the value handle of this structure list property this
	 *                    structure is in
	 * @param index       the position of this structure in structure list
	 * @return the handle of this structure. If this structure is not in the
	 *         <code>valueHandle</code>,<code>null</code> is returned.
	 */

	public StructureHandle getHandle(SimpleValueHandle valueHandle, int index) {
		if (valueHandle == null) {
			return null;
		}
		List valueList = valueHandle.getListValue();
		if (valueList == null || index < 0 || index >= valueList.size()) {
			return null;
		}

		return handle(valueHandle, index);
	}

	/**
	 * Creates the specific handle of this structure. This handle is always created.
	 *
	 * @param valueHandle the value handle of this structure list property this
	 *                    structure is in
	 * @param index       the position of this structure in structure list
	 * @return the handle of this structure.
	 */

	protected abstract StructureHandle handle(SimpleValueHandle valueHandle, int index);

	/**
	 * Gets the specific handle of this structure. This structure must be in the
	 * element's structure list. The structure handle is transient because the
	 * position in the structure list is kept. The position changes if any structure
	 * is added, or dropped. So this handle should not be kept.
	 *
	 * @param valueHandle the value handle of this structure list property this
	 *                    structure is in
	 * @return the handle of this structure. If this structure is not in the
	 *         <code>valueHandle</code>,<code>null</code> is returned.
	 */

	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		if (valueHandle == null || valueHandle.getListValue() == null) {
			return null;
		}

		int posn = valueHandle.getListValue().indexOf(this);
		if (posn == -1) {
			return null;
		}

		return handle(valueHandle, posn);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.core.IStructure#isReferencable()
	 */

	@Override
	public boolean isReferencable() {
		return false;
	}

	/**
	 * Gets the value of the referencable member.
	 *
	 * @return the value of the referencable member
	 */

	public String getReferencableProperty() {
		return null;
	}

	/**
	 * Gets cached member ref.
	 *
	 * @return cached member ref
	 */

	public StructureContext getContext() {
		return context;
	}

	/**
	 * Caches the context to the structure.
	 *
	 * @param context the context
	 */

	public void setContext(StructureContext context) {
		if (context != null) {
			assert context.getStructure() == this;
		}

		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.core.IStructure#isDesignTime()
	 */
	@Override
	public boolean isDesignTime() {
		return true;
	}

	/**
	 * @return the element where the top structure resides
	 */

	public DesignElement getElement() {
		if (context == null) {
			return null;
		}

		return context.getElement();
	}

	/**
	 * Get the string value.
	 *
	 * @param module     the root design/library
	 * @param memberName name of the member to get
	 * @return String value of the member, or <code>null</code> if the member is not
	 *         set or is not found.
	 */

	public String getStringProperty(Module module, String memberName) {
		Object tmpValue = getProperty(module, memberName);
		if (tmpValue instanceof Expression) {
			return ((Expression) tmpValue).getStringExpression();
		} else if (tmpValue instanceof String) {
			return (String) tmpValue;
		}

		return null;
	}

	/**
	 * Constructs the expression object for the given value.
	 *
	 * @param value the value to convert
	 * @return the expression object
	 */

	private static Expression convertObjectToExpression(Object value) {
		Expression expression = null;

		if (!(value instanceof Expression)) {
			expression = new Expression(value, null);
		}

		return expression;
	}

	/**
	 * Constructs the expression list for the given list.
	 *
	 * @param values the list
	 * @return the expression list
	 */

	private static List<Expression> convertListToExpressionList(List<String> values) {
		if (values == null) {
			return null;
		}

		List<Expression> newList = new ArrayList<>();
		if (!values.isEmpty()) {
			for (int i = 0; i < values.size(); i++) {
				Expression tmpValue = convertObjectToExpression(values.get(i));
				if (tmpValue != null) {
					newList.add(tmpValue);
				}
			}
		}

		return newList;
	}

	/**
	 * Sets the value of the member as an expression.
	 *
	 * @param memberName name of the member to set.
	 * @param value      the expression to set
	 * @throws SemanticException if the member name is not defined on the structure
	 *                           or the value is not valid for the member.
	 */

	public void setExpressionProperty(String memberName, Expression value) {
		setProperty(memberName, value);
	}

	/**
	 * Gets the value of the member as an expression.
	 *
	 * @param memberName name of the member to set.
	 * @return the expression
	 * @throws SemanticException if the member name is not defined on the structure
	 *                           or the value is not valid for the member.
	 */

	public Expression getExpressionProperty(String memberName) {
		return (Expression) getProperty(null, memberName);
	}

	/**
	 * Converts the given value to the expression value for the compatibility issue.
	 *
	 * @param propDefn the member definition
	 * @param value    the value
	 * @return the compatible value
	 */

	protected Object getCompatibleValue(PropertyDefn propDefn, Object value) {
		if (propDefn.allowExpression()) {
			// for the case that the given value is a string/integer, etc.

			if (value != null && !(value instanceof List<?>) && !(value instanceof Expression)) {
				return new Expression(value, null);
			}

			// for the case that the given value is a string/integer list

			if (value instanceof List<?>) {
				List tmpList = (List) value;
				if (!tmpList.isEmpty() && !(tmpList.get(0) instanceof Expression)) {
					return convertListToExpressionList(tmpList);
				}
			}
		}

		return value;
	}

	/**
	 * Checks if a string member is blank. If the member is blank, a smantic error
	 * is added in to the given error list tolding that the member value is
	 * required.
	 *
	 * @param value      the member
	 * @param memberName the name of the member
	 * @param element    the element contain's this join condition
	 * @param errorList  the error list
	 */

	protected void checkStringMember(String value, String memberName, DesignElement element, List errorList) {
		PropertyDefn propDefn = (PropertyDefn) getDefn().getMember(memberName);

		if (StringUtil.isBlank(value) && propDefn.isValueRequired()) {
			errorList.add(new PropertyValueException(element, propDefn, value,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}
	}
}
