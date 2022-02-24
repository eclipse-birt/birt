/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ExtensionPropertyDefinitionEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.validators.ValidationExecutor;
import org.eclipse.birt.report.model.validators.ValidationNode;

/**
 * Changes the value of a property. The caller must have previously validated
 * that the property name is valid, that the new value is valid and so on.
 */

class PropertyRecordImpl extends SimpleRecord {

	/**
	 * The design element, that is, a report element. All report element classes
	 * derives from DesignElement
	 */

	protected DesignElement element = null;

	/**
	 * The property definition. When create the PropertyEvent, the property name
	 * should be stored in it, so the listener can check it to, say, whether to
	 * refresh the UI or not.
	 */

	protected ElementPropertyDefn propDefn = null;

	/**
	 * The old value of the property
	 */

	protected Object oldValue = null;

	/**
	 * The new value of the property
	 */

	protected Object newValue = null;

	/**
	 * Constructor.
	 * 
	 * @param propertyOwner the report element that has the property
	 * @param name          the name of the property to change
	 * @param value         the new value
	 */

	public PropertyRecordImpl(DesignElement propertyOwner, String name, Object value) {
		assert propertyOwner != null;
		element = propertyOwner;
		assert name != null;
		propDefn = element.getPropertyDefn(name);
		assert propDefn != null;
		assert !propDefn.isIntrinsic();
		newValue = value;
		oldValue = copyLocalValue(propertyOwner.getLocalProperty(null, propDefn));

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { propDefn.getDisplayName() });
	}

	/**
	 * Constructor.
	 * 
	 * @param propertyOwner the element that has the property to set
	 * @param prop          the definition of the property to set
	 * @param value         the new value
	 */

	public PropertyRecordImpl(DesignElement propertyOwner, ElementPropertyDefn prop, Object value) {
		assert propertyOwner != null;
		propDefn = prop;
		assert propDefn != null;

		// The intrinsic property can also be set with property record. For
		// example,
		// the "unit" property in ReportDesign.

		element = propertyOwner;
		newValue = value;
		oldValue = copyLocalValue(propertyOwner.getLocalProperty(null, propDefn));

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { propDefn.getDisplayName() });
	}

	/**
	 * if the local value is a list, we should make a copy for the list to avoid the
	 * old value in this record to be changed by further operation on the element.
	 * 
	 * @param localValue
	 * @return copyed local value if it is a list.
	 */
	private Object copyLocalValue(Object localValue) {
		if (localValue instanceof List) {
			ArrayList<Object> newValue = new ArrayList<Object>();
			newValue.addAll((List) localValue);
			return newValue;
		}

		return localValue;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget ()
	 */

	public DesignElement getTarget() {
		if (eventTarget != null)
			return eventTarget.getElement();

		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		// Use the same notification for the done/redone and undone states.

		if (element instanceof ExtendedItem) {
			IReportItem extElement = ((ExtendedItem) element).getExtendedElement();
			// assert extElement != null;

			if (extElement != null && extElement.refreshPropertyDefinition()) {
				return new ExtensionPropertyDefinitionEvent(element);
			}
		}

		// if the element works like properties, return property event for the
		// top element.

		if (eventTarget != null) {
			return new PropertyEvent(eventTarget.getElement(), eventTarget.getPropName());
		}

		return new PropertyEvent(element, propDefn.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform
	 * (boolean)
	 */

	protected void perform(boolean undo) {
		Object value = null;
		Object tmpOldValue = null;

		if (!undo) {
			value = newValue;
			tmpOldValue = oldValue;
		} else {
			value = oldValue;
			tmpOldValue = newValue;
		}

		if (propDefn.getTypeCode() != IPropertyType.STRUCT_TYPE) {
			if (propDefn.isEncryptable() && value instanceof String) {
				String localEncryption = element.getLocalEncryptionID(propDefn);

				// if value is not null, must specify encryption; so if local
				// encryption is not set, we will search the inheritance and
				// default and get a non-empty encryption
				String encryption = localEncryption == null ? element.getEncryptionID(propDefn) : localEncryption;
				assert encryption != null;

				value = EncryptionUtil.encrypt(propDefn, encryption, value);
				element.setProperty(propDefn, value);
				if (localEncryption == null)
					element.setEncryptionHelper(propDefn, encryption);
				return;
			}

			element.setProperty(propDefn, value);

			// if the element is cube, need to update layout in some cases
			if (element instanceof Cube) {
				Cube cube = (Cube) element;
				if (cube.isBoundWithLayout(propDefn))
					cube.updateLayout(cube.getRoot());
			} else if (element instanceof TabularDimension) {
				TabularDimension dimension = (TabularDimension) element;
				if (ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equals(propDefn.getName())) {
					Module root = dimension.getRoot();
					dimension.updateLayout(root);
					if (root != null)
						root.manageId(dimension, true);
				}
			}
			return;
		}

		clearStructureContext(tmpOldValue);
		setupStructureContext(value);

		if (value == null || (value instanceof List && ((List) value).isEmpty()))
			element.setProperty(propDefn, value);
	}

	/**
	 * @param values
	 */

	private void setupStructureContext(Object values) {
		if (values == null)
			return;

		if (values instanceof List && ((List) values).isEmpty())
			return;

		StructureContext context = new StructureContext(element, propDefn, null);

		if (values instanceof Structure)
			context.add((Structure) values);
		else if (values instanceof List) {
			for (int i = 0; i < ((List) values).size(); i++)
				context.add((Structure) ((List) values).get(i));
		}
	}

	/**
	 * @param values
	 */

	private void clearStructureContext(Object values) {
		if (values == null)
			return;

		if (values instanceof List && ((List) values).isEmpty())
			return;

		StructureContext context = new StructureContext(element, propDefn, null);

		if (values instanceof Structure) {
			context.remove((Structure) values);
			adjustReferredClients((Structure) values);
		} else if (values instanceof List) {
			// always remove the first one

			List<Structure> structs = new ArrayList<Structure>();
			structs.addAll((List<Structure>) values);
			int count = structs.size();
			for (int i = 0; i < count; i++) {
				Structure struct = structs.get(i);
				context.remove(struct);
				adjustReferredClients(struct);
			}
		}
	}

	/**
	 * Clears references of elements that are referred by the to-be-deleted element,
	 * except for extends and style element references. Unlike the method
	 * {@link #adjustReferenceClients(ReferenceableElement,boolean)}, this method
	 * removes references from those elements that are referred.
	 * 
	 * @param element the element to be deleted
	 * 
	 */

	private void adjustReferredClients(Structure struct) {
		Iterator<IPropertyDefn> propDefns = struct.getDefn().propertiesIterator();

		while (propDefns.hasNext()) {
			PropertyDefn propDefn = (PropertyDefn) propDefns.next();

			if (propDefn.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE)
				continue;

			Object value = struct.getLocalProperty(element.getRoot(), propDefn);

			if (value == null || !((ElementRefValue) value).isResolved())
				continue;

			// since the structure is removed, change the property to unresolved
			// status

			struct.setProperty((PropertyDefn) propDefn, new ElementRefValue(
					((ElementRefValue) value).getLibraryNamespace(), ((ElementRefValue) value).getName()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.IValidatable#getValidators()
	 */
	public List<ValidationNode> getValidators() {
		return ValidationExecutor.getValidationNodes(element, propDefn.getTriggerDefnSet(), false);
	}

	/**
	 * Returns the definition of the property whose value is changed.
	 * 
	 * @return the property definition
	 */

	public ElementPropertyDefn getPropDefn() {
		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		List<RecordTask> retValue = new ArrayList<RecordTask>();
		retValue.addAll(super.getPostTasks());

		if (!(element instanceof Cell))
			return retValue;

		String propName = propDefn.getName();

		if (!ICellModel.COL_SPAN_PROP.equalsIgnoreCase(propName) && !ICellModel.ROW_SPAN_PROP.equalsIgnoreCase(propName)
				&& !ICellModel.COLUMN_PROP.equalsIgnoreCase(propName)
				&& !ICellModel.DROP_PROP.equalsIgnoreCase(propName))
			return retValue;

		ReportItem compoundElement = LayoutUtil.getCompoundContainer(element);
		if (compoundElement == null)
			return retValue;

		retValue.add(new LayoutRecordTask(compoundElement.getRoot(), compoundElement));
		return retValue;
	}
}
