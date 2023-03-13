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

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.OdaLevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularHierarchyModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularLevelModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.OdaLevel;
import org.eclipse.birt.report.model.elements.olap.TabularCube;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.olap.TabularHierarchy;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.elements.strategy.GroupPropSearchStrategy;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.CompatiblePropertyChangeTables;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Sets the value of a property. Works with both system and user properties.
 * Works with normal and intrinsic properties.
 *
 */

public class PropertyCommand extends AbstractPropertyCommand {

	/**
	 * Constructor.
	 *
	 * @param module the root of <code>obj</code>
	 * @param obj    the element to modify.
	 */

	public PropertyCommand(Module module, DesignElement obj) {
		super(module, obj);
	}

	/**
	 * Sets the value of a property.
	 *
	 * @param propName the internal name of the property to set.
	 * @param value    the new property value.
	 * @throws SemanticException if the property is not found.
	 */

	public void setProperty(String propName, Object value) throws SemanticException {
		checkAllowedOperation();
		propName = StringUtil.trimString(propName);

		// Ensure that the property is defined.

		ElementPropertyDefn prop = element.getPropertyDefn(propName);
		if (prop == null) {
			throw new PropertyNameException(element, propName);
		}

		setProperty(prop, value);
	}

	/**
	 * Sets the value of a property. If the mask of a property is "lock", throws one
	 * exception.
	 * <p>
	 * If the mask of this property has been set to "lock", no value will be set. To
	 * set the value of a property, the mask value must be "hide" or "change".
	 *
	 * @param prop  definition of the property to set
	 * @param value the new property value.
	 * @throws SemanticException if the value is invalid or the property mask is
	 *                           "lock".
	 */

	public void setProperty(ElementPropertyDefn prop, Object value) throws SemanticException {
		checkAllowedOperation();

		// if property is element type, do some special handle
		if (prop.isElementType()) {
			setElementTypeProperty(prop, value);
			return;
		}

		// Backward for TOC expression.

		String propName = prop.getName();
		if ((IReportItemModel.TOC_PROP.equals(propName) || IGroupElementModel.TOC_PROP.equals(propName))
				&& (value instanceof String)) {
			Structure oldValue = (Structure) element.getLocalProperty(module, prop);
			if (oldValue != null) {
				StructureContext ref = new StructureContext(oldValue,
						(PropertyDefn) oldValue.getDefn().getMember(TOC.TOC_EXPRESSION), null);
				setMember(ref, value);
				return;
			}

			value = StructureFactory.createTOC((String) value);
		}

		if (IExtendedItemModel.EXTENSION_NAME_PROP.equals(prop.getName()) && element instanceof IExtendedItemModel) {
			throw new PropertyValueException(element, IExtendedItemModel.EXTENSION_NAME_PROP, value,
					PropertyValueException.DESIGN_EXCEPTION_EXTENSION_SETTING_FORBIDDEN);
		}

		String mask = element.getPropertyMask(module, prop.getName());
		if (DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK.equalsIgnoreCase(mask)) {
			throw new PropertyValueException(element, prop, value,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED);
		}

		// Within child element, properties that can cause structure change are
		// not allowed to set.

		if (element.isVirtualElement() && element instanceof Cell) {
			propName = prop.getName();
			if (ICellModel.COL_SPAN_PROP.equalsIgnoreCase(propName)
					|| ICellModel.ROW_SPAN_PROP.equalsIgnoreCase(propName)
					|| ICellModel.DROP_PROP.equalsIgnoreCase(propName)
					|| ICellModel.COLUMN_PROP.equalsIgnoreCase(propName)) {
				throw new PropertyValueException(element, prop, value,
						PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN);
			}

		}

		if (element instanceof MasterPage) {

			// Height and width are not allowed to be set if master page size
			// type is a pre-defined type.

			propName = prop.getName();
			if (!((MasterPage) element).isCustomType(module) && (IMasterPageModel.WIDTH_PROP.equals(propName)
					|| IMasterPageModel.HEIGHT_PROP.equals(propName))) {
				throw new SemanticError(element, SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE);
			}
		}

		// handle the case that changes a property to an expression, or
		// expression to a property/expression.

		value = validateCompatibleObject(prop, value);

		// if this structure has context, that may mean that it has been added
		// to some element or structure container, then make a copy
		if (value instanceof Structure) {
			Structure struct = (Structure) value;
			if (struct.getContext() != null) {
				value = struct.copy();
			}
		}
		if (prop.getName().equalsIgnoreCase(ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP)) {
			checkSharedDimensionReference(prop, value);
		}
		Object inputValue = value;
		value = validateValue(prop, value);

		if (value instanceof ElementRefValue && prop.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			checkRecursiveElementReference(prop, (ElementRefValue) value);
			checkDataBindingReference(prop, (ElementRefValue) value);
			if (inputValue instanceof String) {
				checkSharedDimensionReference(prop, value);
			}
		}

		if (element instanceof GroupElement && IGroupElementModel.GROUP_NAME_PROP.equals(prop.getName())) {
			String name = (String) value;
			if (!NamePropertyType.isValidName(name)) {
				throw new NameException(element, name, NameException.DESIGN_EXCEPTION_INVALID_NAME);
			}
			if (!isGroupNameValidInContext(name)) {
				throw new NameException(element, name, NameException.DESIGN_EXCEPTION_DUPLICATE);
			}
		}

		// Set the property.

		if (prop.isIntrinsic()) {
			setIntrinsicProperty(prop, value);
			return;
		}
		if (IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals(prop.getName()) && value == null) {
			clearRefTemplateParameterProp(prop, null);
			return;
		}

		if (element instanceof ContentElement) {
			if (!((ContentElement) element).isLocal()) {
				ContentElementCommand attrCmd = new ContentElementCommand(
						((ContentElement) element).getValueContainer().getElement().getRoot(), element,
						((ContentElement) element).getValueContainer());

				attrCmd.doSetProperty(prop, value);
				return;
			}
		}

		if (element instanceof ReportItem
				&& (IReportItemModel.DATA_SET_PROP.equals(propName) || IReportItemModel.CUBE_PROP.equals(propName))) {
			DesignElement container = element.getContainer();
			DataSet dataSet = null;
			Cube cube = null;
			if (IReportItemModel.DATA_SET_PROP.equals(propName)) {
				ElementRefValue refValue = (ElementRefValue) value;
				if (refValue != null) {
					dataSet = (DataSet) refValue.getElement();
				}
			} else if (IReportItemModel.CUBE_PROP.equals(propName)) {
				ElementRefValue refValue = (ElementRefValue) value;
				if (refValue != null) {
					cube = (Cube) refValue.getElement();
				}
			}
			if (!ContainerContext.isValidContainerment(module, container, (ReportItem) element, dataSet, cube)) {
				throw new SemanticError(element, SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_DATA_OBJECT);
			}
		}

		if (element instanceof ReportItemTheme && IReportItemThemeModel.TYPE_PROP.equals(propName)) {
			if (!ReportItemTheme.isValidType((String) value)) {
				throw new PropertyValueException(element, IReportItemThemeModel.TYPE_PROP, value,
						PropertyValueException.DESIGN_EXCEPTION_NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE);
			}
		}

		doSetProperty(prop, value);
	}

	/**
	 *
	 * @param prop
	 * @param value
	 * @throws SemanticException
	 */
	private void setElementTypeProperty(ElementPropertyDefn prop, Object value) throws SemanticException {
		ContainerContext context = new ContainerContext(element, prop.getName());
		CommandStack stack = getActivityStack();
		PropertyRecord record = new PropertyRecord(element, prop, value);
		stack.startTrans(record.getLabel());
		ContentCommand cmd = new ContentCommand(module, context);

		List<DesignElement> contents = context.getContents(module);
		try {
			// clear all the original contents and add the new value content
			if (contents != null) {
				for (int i = contents.size() - 1; i >= 0; i--) {
					DesignElement content = contents.get(i);
					cmd.remove(content);
				}
			}
			// add the new content
			if (value instanceof DesignElement) {
				cmd.add((DesignElement) value);
			} else if (value instanceof DesignElementHandle) {
				cmd.add(((DesignElementHandle) value).getElement());
			} else if (value instanceof List) {
				contents = (List) value;
				for (int i = 0; i < contents.size(); i++) {
					Object item = contents.get(i);
					if (item instanceof DesignElement) {
						cmd.add((DesignElement) item);
					} else if (item instanceof DesignElementHandle) {
						cmd.add(((DesignElementHandle) item).getElement());
					}
				}
			}
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/**
	 * Remove template definition from module if the definition is no longer
	 * refferenced when setting
	 * <code>IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP</code> null.
	 *
	 * @param prop  should be
	 *              <code>IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP</code>.
	 * @param value should be null;
	 * @throws SemanticException if any semantic exception is thrown.
	 */

	private void clearRefTemplateParameterProp(ElementPropertyDefn prop, Object value) throws SemanticException {
		checkAllowedOperation();
		assert prop != null;
		assert IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals(prop.getName());

		ActivityStack stack = module.getActivityStack();
		PropertyRecord record = new PropertyRecord(element, prop.getName(), value);

		stack.startTrans(record.getLabel());

		try {
			ElementRefValue templateParam = (ElementRefValue) element.getProperty(module, prop);
			TemplateParameterDefinition definition = (TemplateParameterDefinition) templateParam.getElement();

			doSetProperty(prop, value);

			if (definition != null && !definition.hasReferences()) {
				ContentCommand cmd = new ContentCommand(definition.getRoot(), definition.getContainerInfo());
				cmd.remove(definition);
			}
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/**
	 * Private method to set property.
	 *
	 * @param prop  the definition of the property to set.
	 * @param value the new property value.
	 * @throws ExtendedElementException if the extension property is invalid
	 * @throws PropertyValueException   if the element is a template element and
	 *                                  users try to set the value of template
	 *                                  definition to "null" or a non-existing
	 *                                  element
	 */

	private void doSetProperty(ElementPropertyDefn prop, Object value) throws SemanticException {
		Object oldValue = element.getLocalProperty(module, prop);

		// Ignore duplicate values, even if the current value is not local.
		// This avoids making local copies if the user enters the existing
		// value, or if the UI gets a bit sloppy.

		// for extension element, getLocalProperty may be null. However, still
		// proceed command.

		if (oldValue == null && value == null) {
			return;
		}
		if (oldValue != null && value != null && oldValue.equals(value)) {
			return;
		}

		String propName = prop.getName();
		if (element instanceof ExtendedItem) {

			ExtendedItem extendedItem = ((ExtendedItem) element);

			// if useOwnModel is true, set property to extended item and return
			// directly.

			if (extendedItem.isExtensionModelProperty(propName)) {
				IReportItem extElement = ((ExtendedItem) element).getExtendedElement();

				if (extElement == null) {
					return;
				}

				extElement.checkProperty(propName, value);
				extElement.setProperty(propName, value);

				return;
			}
		}

		if (element instanceof Level && propName.equals(ILevelModel.DATE_TIME_LEVEL_TYPE) && value != null) {
			ActivityStack stack = getActivityStack();

			PropertyRecord record = new PropertyRecord(element, prop, value);
			record.setEventTarget(getEventTarget());

			stack.startTrans(record.getLabel());
			stack.execute(record);
			boolean isFound = false;
			ElementPropertyDefn attributesPropertyDefn = element.getPropertyDefn(ILevelModel.ATTRIBUTES_PROP);
			List attrs = (List) element.getProperty(module, attributesPropertyDefn);
			if (attrs != null) {
				for (int i = 0; i < attrs.size(); i++) {
					LevelAttribute attr = (LevelAttribute) attrs.get(i);
					if (LevelAttribute.DATE_TIME_ATTRIBUTE_NAME.equals(attr.getName())) {
						isFound = true;
						break;
					}
				}
			}

			if (!isFound) {

				ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, element);

				Structure struct = null;
				if (element instanceof TabularLevel) {
					LevelAttribute attibute = new LevelAttribute();
					attibute.setName(LevelAttribute.DATE_TIME_ATTRIBUTE_NAME);
					attibute.setDataType(getDataType((TabularLevel) element));
					struct = attibute;
				} else if (element instanceof OdaLevel) {
					OdaLevelAttribute attibute = new OdaLevelAttribute();
					attibute.setName(LevelAttribute.DATE_TIME_ATTRIBUTE_NAME);
					attibute.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME);
					struct = attibute;
				}
				if (attributesPropertyDefn != null && struct != null) {
					try {
						cmd.addItem(new StructureContext(element, attributesPropertyDefn, null), struct);
					} catch (SemanticException e) {

						assert false;
						stack.rollback();
					}
				}
			}

			stack.commit();

			return;
		}

		ActivityStack stack = getActivityStack();

		PropertyRecord record = new PropertyRecord(element, prop, value);
		stack.startTrans(record.getLabel());

		record.setEventTarget(getEventTarget());

		stack.execute(record);

		if (IReportItemModel.DATA_BINDING_REF_PROP.equalsIgnoreCase(propName)) {
			try {

				// if element is table/list, we must localize group structure in
				// some special cases firstly
				if (element instanceof ListingElement) {
					GroupElementCommand tmpCmd = new GroupElementCommand(module,
							new ContainerContext(element, IListingElementModel.GROUP_SLOT));

					tmpCmd.updateBindingRef((ElementRefValue) oldValue, (ElementRefValue) value);
				}

				// whether the element is table/list or not, we must localize
				// the binding-ref related data properties
				if (value == null || !((ElementRefValue) value).isResolved()) {
					if (oldValue != null && ((ElementRefValue) oldValue).isResolved()) {
						localizeProperties(((ElementRefValue) oldValue).getElement());
					}
				}

			} catch (SemanticException e) {
				stack.rollback();
				throw e;
			}
		} else if (element instanceof Style || element instanceof StyledElement) {

			String tmpPropName = null;
			if (IStyleModel.BACKGROUND_SIZE_WIDTH.equals(propName)) {
				tmpPropName = IStyleModel.BACKGROUND_SIZE_HEIGHT;

			} else if (IStyleModel.BACKGROUND_SIZE_HEIGHT.equals(propName)) {
				tmpPropName = IStyleModel.BACKGROUND_SIZE_WIDTH;

			}
			if (tmpPropName != null) {
				handleBackgroundSize(stack, tmpPropName, value);
			}

		}

		// if the dimension refers a shared dimension, then handle the name
		if (element instanceof TabularDimension
				&& ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equals(propName)) {
			try {
				NameCommand command = new NameCommand(module, element);
				command.checkDimension();
			} catch (SemanticException e) {
				stack.rollback();
				throw e;
			}
		}

		stack.commit();
	}

	/**
	 * Gets the data type of the level
	 *
	 * @return
	 */
	private String getDataType(TabularLevel level) {
		String columnName = level.getStringProperty(module, ITabularLevelModel.COLUMN_NAME_PROP);
		if (!StringUtil.isBlank(columnName)) {
			DesignElement container = element.getContainer();
			DataSet dataSet = null;
			if (container instanceof TabularHierarchy) {
				dataSet = (DataSet) container.getReferenceProperty(module, ITabularHierarchyModel.DATA_SET_PROP);
			}
			if (dataSet == null && container != null) {
				container = container.getContainer();
				if (container instanceof Dimension) {
					container = container.getContainer();
				}
				if (container instanceof TabularCube) {
					dataSet = (DataSet) container.getReferenceProperty(module, ITabularCubeModel.DATA_SET_PROP);
				}
			}
			if (dataSet != null) {
				CachedMetaData metaData = (CachedMetaData) dataSet.getProperty(module,
						IDataSetModel.CACHED_METADATA_PROP);
				if (metaData != null) {
					List<ResultSetColumn> resultSet = (List<ResultSetColumn>) metaData.getProperty(module,
							CachedMetaData.RESULT_SET_MEMBER);
					if (resultSet != null) {
						for (ResultSetColumn column : resultSet) {
							if (columnName.equals(column.getStringProperty(module, ResultSetColumn.NAME_MEMBER))) {
								String dataType = column.getStringProperty(module, ResultSetColumn.DATA_TYPE_MEMBER);
								if (dataType != null) {
									return dataType;
								}
								break;
							}
						}
					}
				}
			}
		}
		return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
	}

	/**
	 * Handles the back ground size.
	 *
	 * @param stack           the activity stack.
	 * @param anotherPropName another property name.
	 * @param value           the property value.
	 */
	private void handleBackgroundSize(ActivityStack stack, String anotherPropName, Object value) {
		ElementPropertyDefn anotherProp = element.getPropertyDefn(anotherPropName);
		assert anotherProp != null;

		// if the input value is contain or cover, the property value will
		// be set as the input value.
		Object anotherPropLocalValue = element.getLocalProperty(module, anotherProp);

		if (DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(value)
				|| DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(value)) {
			if (!value.equals(anotherPropLocalValue)) {
				PropertyRecord record = new PropertyRecord(element, anotherProp, value);
				stack.execute(record);
			}
		} else if (DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(anotherPropLocalValue)
				|| DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(anotherPropLocalValue)) {
			PropertyRecord record = new PropertyRecord(element, anotherProp, value);
			stack.execute(record);
		}
	}

	/**
	 * Localizes element properties including listing elements and its group
	 * properties.
	 *
	 * @param targetElement
	 * @throws SemanticException
	 */

	private void localizeProperties(DesignElement targetElement) throws SemanticException {
		ReportItem reportItem = (ReportItem) element;

		recoverReferredReportItem(reportItem, targetElement);

		if (!(reportItem instanceof ListingElement) || !(targetElement instanceof ListingElement) || !ModelUtil.isCompatibleDataBindingElements(element, targetElement)) {
			return;
		}

		ListingElement listing = (ListingElement) reportItem;
		List<DesignElement> listingGroups = listing.getGroups();

		ListingElement targetListing = (ListingElement) targetElement;
		List<DesignElement> targetGroups = targetListing.getGroups();

		int size = Math.min(listingGroups.size(), targetGroups.size());
		for (int i = 0; i < size; i++) {
			recoverReferredReportItem(listingGroups.get(i), targetGroups.get(i));
		}
	}

	/**
	 * Localizes element properties from <code>targetElement</code> to
	 * <code>source</code>.
	 *
	 */

	private void recoverReferredReportItem(DesignElement source, DesignElement targetElement) throws SemanticException {
		Iterator<String> propNames = null;

		if (targetElement instanceof ReportItem) {
			propNames = ReportItemPropSearchStrategy.getDataBindingProperties(targetElement).iterator();
		} else if (targetElement instanceof GroupElement) {
			propNames = GroupPropSearchStrategy.getDataBindingPropties().iterator();
		} else {
			assert false;
			return;
		}

		while (propNames.hasNext()) {
			String propName = propNames.next();
			ElementPropertyDefn propDefn = (ElementPropertyDefn) targetElement.getDefn().getProperty(propName);
			if (propDefn == null) {
				continue;
			}

			ElementPropertyDefn sourcePropDefn = (ElementPropertyDefn) source.getDefn().getProperty(propName);

			

			// the filter is the special case. See
			// IListingElementModel.FILTER_PROP and
			// IExtendedItemModel.FILTER_PROP. Same to sorter

			if ((sourcePropDefn == null) || propDefn.getTypeCode() != sourcePropDefn.getTypeCode()
					|| propDefn.getStructDefn() != sourcePropDefn.getStructDefn()
					|| propDefn.getTargetElementType() != sourcePropDefn.getTargetElementType()) {
				continue;
			}

			Object value = targetElement.getStrategy().getPropertyExceptRomDefault(module, targetElement, propDefn);

			// do some special handle for column bindings
			if (IReportItemModel.BOUND_DATA_COLUMNS_PROP.equals(propName)) {
				value = getValidColumnBindings(source, targetElement, (List<ComputedColumn>) value);
			}
			value = ModelUtil.copyValue(propDefn, value);

			// Set the list value on the element itself.

			PropertyRecord propRecord = new PropertyRecord(source, sourcePropDefn, value);
			getActivityStack().execute(propRecord);
		}
	}

	private List<ComputedColumn> getValidColumnBindings(DesignElement source, DesignElement target,
			List<ComputedColumn> value) {
		// if two elements are both listing element, then do nothing
		// if two element are the same type, then do nothing
		if ((source instanceof ListingElement && target instanceof ListingElement) || (source.getDefn() == target.getDefn())) {
			return value;
		}

		List<ComputedColumn> retValue = new ArrayList<>();
		if (value != null) {
			for (int i = 0; i < value.size(); i++) {
				ComputedColumn binding = value.get(i);
				if (binding.getAggregateOn() == null) {
					retValue.add(binding);
				}
			}
		}
		return retValue;
	}

	/**
	 * Private method to validate the value of a property.
	 *
	 * @param prop  definition of the property to validate
	 * @param value the value to validate
	 * @return the value to store for the property
	 * @throws PropertyValueException if the value is not valid
	 */

	private Object validateValue(ElementPropertyDefn prop, Object value) throws SemanticException {
		// clear the property doesn't needs validation.

		if (value == null) {
			return null;
		}

		Object input = value;

		// uses the name to resolve the element name

		if (value instanceof DesignElementHandle) {
			/*
			 * DesignElementHandle elementHandle = (DesignElementHandle) value; Module root
			 * = elementHandle.getModule( );
			 *
			 * input = ReferenceValueUtil.needTheNamespacePrefix( elementHandle .getElement(
			 * ), root, module );
			 */
			input = ((DesignElementHandle) value).getElement();
		}

		Object retValue = null;
		String propName = prop.getName();

		try {
			retValue = prop.validateValue(module, element, input);
		} catch (PropertyValueException ex) {
			ex.setElement(element);
			ex.setPropertyName(propName);
			throw ex;
		}

		if (!(retValue instanceof ElementRefValue)) {
			return retValue;
		}

		// if the return element and the input element is not same, throws
		// exception

		ElementRefValue refValue = (ElementRefValue) retValue;
		if (refValue.isResolved() && value instanceof DesignElementHandle
				&& refValue.getElement() != ((DesignElementHandle) value).getElement()) {
			throw new SemanticError(element, new String[] { propName, refValue.getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF);
		}

		return retValue;

	}

	/**
	 * Sets the value of an intrinsic property.
	 *
	 * @param prop  definition of the property to set
	 * @param value the property value to set.
	 * @throws SemanticException if failed to set property.
	 */

	private void setIntrinsicProperty(ElementPropertyDefn prop, Object value) throws SemanticException {
		checkAllowedOperation();
		String propName = prop.getName();

		if (IDesignElementModel.NAME_PROP.equals(propName) && prop.getTypeCode() == IPropertyType.NAME_TYPE) {
			String name = (String) value;

			NameCommand cmd = new NameCommand(module, element);
			cmd.setName(name);
		} else if (IDesignElementModel.EXTENDS_PROP.equals(propName)) {
			ExtendsCommand cmd = new ExtendsCommand(module, element);
			cmd.setExtendsRefValue((ElementRefValue) value);
		} else if (IStyledElementModel.STYLE_PROP.equals(propName)) {
			// the value must be a type of ElementRefValue or null

			StyleCommand cmd = new StyleCommand(module, element);
			cmd.setStyleRefValue((ElementRefValue) value);
		} else if (IModuleModel.UNITS_PROP.equals(propName)) {
			doSetProperty(prop, value);
		} else if (IExtendedItemModel.EXTENSION_NAME_PROP.equals(propName)) {
			doSetProperty(prop, value);
		} else if (ISupportThemeElementConstants.THEME_PROP.equals(propName)) {
			ThemeCommand cmd = new ThemeCommand(module, element);
			cmd.setThemeRefValue((ElementRefValue) value);
		} else {
			// Other intrinsics properties will be added here.
			doSetProperty(prop, value);
		}
	}

	/**
	 * Checks whether the name is valid in the context.
	 *
	 * @param name the new name
	 * @return <code>true</code> if the name is valid. Otherwise <code>false</code>.
	 */

	private boolean isGroupNameValidInContext(String groupName) {
		assert element instanceof GroupElement;

		if (groupName == null) {
			return true;
		}

		if (element.getContainer() != null) {
			DesignElement tmpContainer = element.getContainer();

			List<SemanticException> errors = GroupNameValidator.getInstance().validateForRenamingGroup(
					(ListingHandle) tmpContainer.getHandle(module), (GroupHandle) element.getHandle(module), groupName);

			if (!errors.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Clears the value of a property.
	 *
	 * @param propName the name of the property to clear.
	 * @throws SemanticException if failed to clear property.
	 */

	public void clearProperty(String propName) throws SemanticException {
		setProperty(propName, null);
	}

	/**
	 * Sets the value of the member of a structure.
	 *
	 * @param ref   reference to the member to set
	 * @param value new value of the member
	 * @throws SemanticException if the value is not valid
	 */

	public void setMember(StructureContext ref, Object value) throws SemanticException {
		checkAllowedOperation();
		PropertyDefn memberDefn = (PropertyDefn) ref.getPropDefn();
		PropertyDefn propDefn = ref.getElementProp();
		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		assert memberDefn != null;
		// if this structure has context, that may mean that it has been added
		// to some element or structure container, then make a copy
		if (value instanceof Structure) {
			Structure struct = (Structure) value;
			if (struct.getContext() != null) {
				value = struct.copy();
			}
		}
		value = memberDefn.validateValue(module, element, value);

		// if set the value to the name of a structure, must ensure this
		// would not create duplicates.

		if (memberDefn.getTypeCode() == IPropertyType.NAME_TYPE
				|| memberDefn.getTypeCode() == IPropertyType.MEMBER_KEY_TYPE) {
			checkItemName(ref, (String) value);
		}

		if (value instanceof ElementRefValue && memberDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			checkRecursiveElementReference(memberDefn, (ElementRefValue) value);
		}

		// Ignore duplicate values, even if the current value is not local.
		// This avoids making local copies if the user enters the existing
		// value, or if the UI gets a bit sloppy.

		Object oldValue = ref.getLocalValue(module);
		if (oldValue == null && value == null) {
			return;
		}
		if (oldValue != null && value != null && oldValue.equals(value)) {
			return;
		}

		// The values differ. Make the change.

		ActivityStack stack = getActivityStack();

		String label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_ITEM_MESSAGE);
		stack.startTrans(label);

		ref = makeLocalCompositeValue(ref);

		MemberRecord record = new MemberRecord(module, element, ref, value);

		record.setEventTarget(getEventTarget());
		stack.execute(record);

		Structure structure = ref.getStructure();
		List<SemanticException> semanticList = structure.validate(module, element);
		if (semanticList.size() > 0) {
			stack.rollback();
			throw semanticList.get(0);
		}

		stack.commit();

	}

	/**
	 * Check operation is allowed or not. Now if element is css style instance ,
	 * forbidden its operation.
	 *
	 */

	private void checkAllowedOperation() {
		// read-only for css style.

		if (element instanceof CssStyle) {
			throw new IllegalOperationException(CssException.DESIGN_EXCEPTION_READONLY);
		}
	}

	/**
	 * Checks data binding reference.
	 *
	 * @param propDefn the property/member definition
	 * @param the      element reference value
	 * @throws SemanticException
	 */
	private void checkDataBindingReference(PropertyDefn propDefn, ElementRefValue refValue) throws SemanticException {

		// if the element is the container or the content of the input
		// element throws exception
		if (IReportItemModel.DATA_BINDING_REF_PROP.equals(propDefn.getName()) && refValue.isResolved()
				&& ModelUtil.checkContainerOrContent(element, refValue.getElement())) {
			throw new SemanticError(element, new String[] { element.getName(), refValue.getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_DATA_BINDING_REF);
		}
	}

	/**
	 * Checks the shared dimension reference. The dimension referred by cube
	 * dimension must be shared dimension.
	 *
	 * @param propDefn the property/member definition
	 * @param value    element reference value
	 * @throws SemanticException
	 */

	private void checkSharedDimensionReference(PropertyDefn propDefn, Object value) throws SemanticException {
		if (!ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equalsIgnoreCase(propDefn.getName())) {
			return;
		}

		if (value instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) value;
			if (!(handle.getContainer() instanceof ModuleHandle)) {
				throw new SemanticError(element, new String[] { element.getName(), handle.getName() },
						SemanticError.DESIGN_EXCEPTION_INVALID_DATA_BINDING_REF);
			}
		} else if (value instanceof DesignElement) {
			DesignElement refElement = (DesignElement) value;
			if (!(refElement.getContainer() instanceof Module)) {
				throw new SemanticError(element, new String[] { element.getName(), refElement.getName() },
						SemanticError.DESIGN_EXCEPTION_INVALID_DATA_BINDING_REF);
			}
		} else if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			if (refValue.isResolved() && !(refValue.getElement().getContainer() instanceof Module)) {
				throw new SemanticError(element, new String[] { element.getName(), refValue.getName() },
						SemanticError.DESIGN_EXCEPTION_INVALID_DATA_BINDING_REF);
			}

		}

	}

	/**
	 * Sets the expression type for the property compatibility if the property has
	 * changed from the string/Integer/DimensionValue... to allowExpression = true.
	 *
	 * @return the value
	 */

	private Object validateCompatibleObject(ElementPropertyDefn propDefn, Object value) {

		// currently only consider the case for the
		// ScalarParameter.defaultValue: the literalString --> allowExpression =
		// true. And the property type is the list

		boolean isCompatible = false;

		if (propDefn.allowExpression()) {
			// for the case that the given value is a string/integer, etc.

			if (value != null && !(value instanceof List<?>) && !(value instanceof Expression)) {
				isCompatible = true;
			}

			// for the case that the given value is a string/integer list

			if (value instanceof List<?>) {
				List tmpList = (List) value;
				if (!tmpList.isEmpty() && !(tmpList.get(0) instanceof Expression)) {
					isCompatible = true;
				}
			}
		}

		if (!isCompatible) {
			return value;
		}

		String defaultType = CompatiblePropertyChangeTables.getDefaultExprType(element.getDefn().getName(),
				propDefn.getName(), Integer.MIN_VALUE);

		if (defaultType == null) {
			return value;
		}

		Object retValue = value;

		// for example: integer -> integer/expression, string ->
		// string/expression.

		int typeCode = propDefn.getTypeCode();

		switch (typeCode) {
		case IPropertyType.EXPRESSION_TYPE:

			// expression -> string/expression

			if (defaultType != null) {
				retValue = new Expression(value, ExpressionType.JAVASCRIPT);
			}
			break;

		case IPropertyType.STRING_TYPE:
		case IPropertyType.LITERAL_STRING_TYPE:

			// string/dimension -> string/dimension/expression

			retValue = doValidateCompatibleObject(propDefn, propDefn.getType(), value);

			break;
		case IPropertyType.LIST_TYPE:

			// no validation here, only fill type.

			if (value instanceof List) {
				PropertyType tmpSubType = propDefn.getSubType();

				List tmpList = (List) value;
				retValue = new ArrayList<Expression>();
				for (int i = 0; i < tmpList.size(); i++) {
					((List) retValue).add(doValidateCompatibleObject(propDefn, tmpSubType, tmpList.get(i)));
				}
			} else {
				PropertyType tmpSubType = propDefn.getSubType();

				Expression tmpExpr = doValidateCompatibleObject(propDefn, tmpSubType, value);

				retValue = new ArrayList<Expression>();
				((List) retValue).add(tmpExpr);
			}
			break;
		default:
			break;

		}
		return retValue;
	}

	/**
	 * @param propDefn
	 * @param subType
	 * @param value
	 * @return
	 */

	private Expression doValidateCompatibleObject(PropertyDefn propDefn, PropertyType subType, Object value) {

		String validatedValue = null;
		try {
			validatedValue = (String) subType.validateValue(module, element, propDefn, value);
		} catch (PropertyValueException e) {
			// ignore this exception. must be ROM error.
		}

		return new Expression(validatedValue, ExpressionType.CONSTANT);

	}
}
