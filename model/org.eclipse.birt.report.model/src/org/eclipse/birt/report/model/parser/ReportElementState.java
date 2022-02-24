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

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.command.NameCommand;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.VariableElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ElementStructureUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base class for all report element parse states.
 * 
 */

public abstract class ReportElementState extends DesignParseState {

	/**
	 * The element that contains the one being parsed.
	 */

	protected DesignElement container = null;

	/**
	 * The slot within the container in which the element being parsed is to appear.
	 */

	protected int slotID = 0;

	/**
	 * Name of the property that is defined in the container and is of element type.
	 */
	protected String containmentPropName = null;

	/**
	 * Constructs the report element state with the design parser handler, the
	 * container element and the container slot of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public ReportElementState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler);
		container = theContainer;
		slotID = slot;
	}

	/**
	 * Constructs the report element state with the design parser handler, the
	 * container element and the container property name of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public ReportElementState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler);
		container = theContainer;
		containmentPropName = prop;
	}

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * 
	 * @param theHandler SAX handler for the design file parser
	 */
	public ReportElementState(ModuleParserHandler theHandler) {
		super(theHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public abstract DesignElement getElement();

	private boolean checkContainer(DesignElement container, int slotID, DesignElement content) {
		ElementDefn containerDefn = (ElementDefn) container.getDefn();

		// The following conditions should not occur in the parser --
		// they indicate parser design errors since they can be prevented
		// with the right syntax checks.
		assert containerDefn.isContainer();
		if (!StringUtil.isBlank(containmentPropName)) {
			PropertyDefn propDefn = (PropertyDefn) containerDefn.getProperty(containmentPropName);
			assert propDefn != null;

			// now not all the children is allowed to be inserted to the
			// container, if the container is ExtendedItem and child is not
			// allowed, we still do some special handle
			assert propDefn.canContain(content) || (container instanceof ExtendedItem);

			// Can not change the structure of an element if it is a child
			// element or it is within a child element.

			if (container.getExtendsElement() != null && propDefn.getTypeCode() != IPropertyType.CONTENT_ELEMENT_TYPE) {
				handler.getErrorHandler().semanticWarning(new ContentException(container, containmentPropName, content,
						ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN));
				return false;
			}

			// If this is a single-item slot, ensure that the slot is empty.

			if (!propDefn.isList()
					&& new ContainerContext(container, containmentPropName).getContentCount(handler.module) > 0) {
				handler.getErrorHandler().semanticError(new ContentException(container, containmentPropName,
						ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL));
				return false;
			}

			return true;
		}

		SlotDefn slotInfo = (SlotDefn) containerDefn.getSlot(slotID);
		assert slotInfo != null;
		assert slotInfo.canContain(content);

		// Can not change the structure of an element if it is a child
		// element
		// or it is within a child element.

		if (container.getExtendsElement() != null) {
			handler.getErrorHandler().semanticWarning(new ContentException(container, slotID, content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN));
			return false;
		}

		// If this is a single-item slot, ensure that the slot is empty.

		if (!slotInfo.isMultipleCardinality() && container.getSlot(slotID).getCount() > 0) {
			handler.getErrorHandler().semanticError(
					new ContentException(container, slotID, ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL));
			return false;
		}

		return true;

	}

	/**
	 * Adds an element to the given slot. Records a semantic error and returns false
	 * if an error occurs. (Does not throw an exception because we don't want to
	 * terminate the parser: we want to keep parsing to find other errors.)
	 * 
	 * @param container the container element
	 * @param slotID    the slot within the container
	 * @param content   the content element
	 * @return true if the add was successful, false if an error occurred and a
	 *         semantic error was logged
	 */

	protected boolean addToSlot(DesignElement container, int slotID, DesignElement content) {
		// check the container relationship
		if (!checkContainer(container, slotID, content))
			return false;

		// if container is ExtendedItem and content is not allowed to be
		// inserted, then do some special handle
		if (container instanceof ExtendedItem) {
			if (!StringUtil.isBlank(containmentPropName)) {
				PropertyDefn propDefn = (PropertyDefn) container.getDefn().getProperty(containmentPropName);
				assert propDefn != null;
				if (!propDefn.canContain(content)) {
					((ExtendedItem) container).getExtensibilityProvider().handleIllegalChildren(containmentPropName,
							content);
					return true;
				}
			}
		}

		Module module = handler.getModule();

		if (!addElementID(module, content))
			return false;

		// Add the item to the container.
		if (!StringUtil.isBlank(containmentPropName)) {
			container.add(handler.module, content, containmentPropName);
		} else {
			container.add(content, slotID);
		}

		// this action should be done after container relationship is set,
		// otherwise we may not find the name holder, such as add level name to
		// the container dimension;The name should not be null if it is
		// required. The parser state should have already caught this case.
		addToNamespace(content);
		return true;
	}

	/**
	 * Initializes a report element with "name" and "extends" property.
	 * 
	 * @param attrs        the SAX attributes object
	 * @param nameRequired true if this element requires a name, false if the name
	 *                     is optional.
	 * 
	 * @see #initSimpleElement(Attributes)
	 */

	protected void initElement(Attributes attrs, boolean nameRequired) {
		DesignElement element = getElement();

		String name = attrs.getValue(IDesignElementModel.NAME_PROP);

		initElementName(name, nameRequired);

		String extendsName = attrs.getValue(DesignSchemaConstants.EXTENDS_ATTRIB);
		if (!StringUtil.isBlank(extendsName) && element.getDefn().canExtend()) {
			element.setExtendsName(extendsName);
			resolveExtendsElement();
		} else {
			// If "extends" is set on an element that can not be extended,
			// exception will be thrown.

			if (!StringUtil.isBlank(attrs.getValue(DesignSchemaConstants.EXTENDS_ATTRIB)))
				handler.getErrorHandler().semanticError(
						new DesignParserException(DesignParserException.DESIGN_EXCEPTION_ILLEGAL_EXTENDS));
		}

		initSimpleElement(attrs);
	}

	/**
	 * Initializes a report element with "name" property.
	 * 
	 * @param attrs        the SAX attributes object
	 * @param nameRequired true if this element requires a name, false if the name
	 *                     is optional.
	 */
	protected void initElementName(String name, boolean nameRequired) {
		DesignElement element = getElement();

		// name is not defined on this element
		PropertyDefn propDefn = element.getPropertyDefn(IDesignElementModel.NAME_PROP);

		if (!StringUtil.isBlank(name) && propDefn == null) {

			// if name is not defined, then fire a waring and do nothing
			handler.getErrorHandler()
					.semanticWarning(new NameException(element, name, NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN));
		} else if (propDefn != null) {
			boolean isValidName = true;
			try {
				name = (String) propDefn.validateValue(this.handler.getModule(), element, name);
			} catch (PropertyValueException e) {
				handler.getErrorHandler()
						.semanticError(new NameException(element, name, NameException.DESIGN_EXCEPTION_INVALID_NAME));
				isValidName = false;
				element.setName(name);
			}
			if (isValidName) {
				if (StringUtil.isBlank(name)) {
					if (nameRequired) {
						// if version<3.2.8, add it to the list and allocate
						// a name in end-document; if the version < 3.2.12,
						// add it to the list.
						if (handler.versionNumber <= VersionUtil.VERSION_3_2_12 && element instanceof ExtendedItem) {
							handler.addUnnamedReportItem(element);
						}
					}
				} else {
					element.setName(name);
				}
			}
		}
	}

	/**
	 * Initializes the "id" property of the element and add it to the container.
	 * Some simple elements, such as rows, columns, cells, and groups will call this
	 * method in <code>parseAttrs</code>. These kinds of element has no names and no
	 * extends property definition while they have "id" property. Other complicate
	 * elements, such as report items, master pages, data sets, data souces, will
	 * call <code>initElement</code> in <code>parseAttrs</code> to initialize the
	 * name and extends properties. This method will handle the "id" property and
	 * add it to the container.
	 * 
	 * @param attrs the SAX attributes object
	 * @see #initElement(Attributes, boolean)
	 */

	protected final void initSimpleElement(Attributes attrs) {
		DesignElement element = getElement();
		if (handler.markLineNumber)
			handler.tempLineNumbers.put(element, Integer.valueOf(handler.getCurrentLineNo()));

		if (!(element instanceof ContentElement)) {
			// get the "id" of the element

			initElementID(attrs, element);
		}
		// read view action

		String viewAction = attrs.getValue(DesignSchemaConstants.VIEW_ACTION_ATTRIB);
		if (!StringUtil.isBlank(viewAction)) {
			setProperty(IDesignElementModel.VIEW_ACTION_PROP, viewAction);
		}

		addToSlot(container, slotID, element);
	}

	/**
	 * Resolves the reference of the extend. There is an assumption that the parent
	 * element always exists before his derived ones.
	 * 
	 */

	private void resolveExtendsElement() {
		DesignElement element = getElement();
		Module module = handler.getModule();
		ElementDefn defn = (ElementDefn) element.getDefn();

		// Resolve extends

		String extendsName = element.getExtendsName();
		if (StringUtil.isBlank(extendsName))
			return;

		DesignElement parent = module.resolveElement(element, extendsName,
				element.getPropertyDefn(IDesignElementModel.EXTENDS_PROP), defn);

		if (parent == null) {
			handler.getErrorHandler().semanticWarning(new InvalidParentException(element, extendsName,
					InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND));
			return;
		}

		try {
			element.checkExtends(parent);
		} catch (ExtendsException ex) {
			handler.getErrorHandler().semanticError(ex);
			return;
		}

		element.setExtendsElement(parent);
		ElementStructureUtil.duplicateStructure(parent, element, module);
	}

	/**
	 * Add the element name into the module namespace.
	 * 
	 * @param element the element.
	 */
	protected void addToNamespace(DesignElement content) {
		String name = content.getName();
		// check whether style name is valid for css2 spec
		if (content instanceof StyleElement && name != null) {
			// if the name is null. the tmpMatcher may be null;

			Matcher tmpMatcher = NameCommand.styleNamePattern.matcher(name);

			if (tmpMatcher != null && !tmpMatcher.matches()) {
				if (handler.versionNumber < VersionUtil.VERSION_3_2_19) {
					// fire semantic warning error
					handler.getErrorHandler().semanticWarning(
							new NameException(content, name, NameException.DESIGN_EXCEPTION_INVALID_NAME));

				} else {
					// needn't fire error, since old report may be serialized to report document,
					// and the version is the latest
					handler.getErrorHandler().semanticWarning(
							new NameException(content, name, NameException.DESIGN_EXCEPTION_INVALID_NAME));
					return;
				}
			}
		}

		name = content.getName();
		ElementDefn contentDefn = (ElementDefn) content.getDefn();
		assert content.getContainer() != null;
		boolean isManagedByNameSpace = content.isManagedByNameSpace();

		// Disallow duplicate names.

		Module module = handler.getModule();

		if (name == null && contentDefn.getNameOption() == MetaDataConstants.REQUIRED_NAME && isManagedByNameSpace) {
			// if element is extended-item and version less than 3.2.8, do
			// nothing and returns
			if ((content instanceof ExtendedItem && handler.versionNumber < VersionUtil.VERSION_3_2_8))
				return;

			// for the report old than 3.2.20, do not check if the name of the
			// variable element is null.
			if (content instanceof VariableElement && handler.versionNumber < VersionUtil.VERSION_3_2_20)
				return;

			handler.getErrorHandler()
					.semanticError(new NameException(content, null, NameException.DESIGN_EXCEPTION_NAME_REQUIRED));
			return;

		}

		// not add tabular dimension into namespace until the 'internalRef'
		// is parsed; handle the name in state end()
		if (content instanceof TabularDimension && !(container instanceof Module)) {
			return;
		}

		if (name != null && isManagedByNameSpace) {
			NameExecutor executor = new NameExecutor(module, content);
			String namespceId = executor.getNameSpaceId();
			NameSpace ns = executor.getNameSpace();
			DesignElement existedElement = executor.getElement(name);
			if (existedElement != null) {
				// if name is identically equal, then fire error
				if (name.equals(existedElement.getName()))

					handler.getErrorHandler()
							.semanticError(new NameException(content, name, NameException.DESIGN_EXCEPTION_DUPLICATE));
				else {
					// for some style name, we should do backward
					// compatibilities
					if (handler.versionNumber >= VersionUtil.VERSION_3_2_19 && content instanceof Style)
						handler.getErrorHandler().semanticError(
								new NameException(content, name, NameException.DESIGN_EXCEPTION_DUPLICATE));

					// name of parameter and parameter group is changed to
					// case-insensitive since version 3.2.21
					if (handler.versionNumber >= VersionUtil.VERSION_3_2_21
							&& (namespceId == Module.PARAMETER_NAME_SPACE)) {
						handler.getErrorHandler().semanticError(
								new NameException(content, name, NameException.DESIGN_EXCEPTION_DUPLICATE));
					}
				}
				return;
			}
			DesignElement parent = content.getExtendsElement();
			if (namespceId == ReportDesign.ELEMENT_NAME_SPACE && parent != null) {
				if (parent.getContainerInfo().getSlotID() != IModuleModel.COMPONENT_SLOT)
				// if ( !module.getSlot( Module.COMPONENT_SLOT ).contains(
				// parent ) )
				{
					handler.getErrorHandler()
							.semanticError(new ExtendsForbiddenException(content, content.getElementName(),
									ExtendsForbiddenException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT));
					return;
				}
			}
			ns.insert(content);
		}
	}

	/**
	 * Add the virtual elements name into the module namespace.
	 * 
	 * @param element the element contains virtual elements inside.
	 */

	private void addTheVirualElements2Map(DesignElement element) {
		Iterator contentIter = new ContentIterator(handler.module, element);
		Module module = handler.getModule();

		while (contentIter.hasNext()) {
			DesignElement virtualElement = (DesignElement) contentIter.next();
			if (virtualElement.getName() != null) {
				module.makeUniqueName(virtualElement);
				addToNamespace(virtualElement);
			}
		}
	}

	/**
	 * Parse the attribute of "extensionName" for extendable element.
	 * 
	 * @param attrs                 the SAX attributes object
	 * @param extensionNameRequired whether extension name is required
	 */

	protected void parseExtensionName(Attributes attrs, boolean extensionNameRequired) {
		DesignElement element = getElement();

		assert element instanceof IExtendableElement;

		String extensionName = getAttrib(attrs, DesignSchemaConstants.EXTENSION_NAME_ATTRIB);

		if (StringUtil.isBlank(extensionName)) {
			if (!extensionNameRequired)
				return;

			SemanticError e = new SemanticError(element, SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION);
			RecoverableError.dealMissingInvalidExtension(handler, e);
		} else {
			MetaDataDictionary dd = MetaDataDictionary.getInstance();
			ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getElement(extensionName);
			if (extDefn == null) {
				SemanticError e = new SemanticError(element, new String[] { extensionName },
						SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND);
				RecoverableError.dealMissingInvalidExtension(handler, e);
			}
		}

		setProperty(IExtendedItemModel.EXTENSION_NAME_PROP, extensionName);
	}

	/**
	 * handles the compatible issue for test expression of <code>HilightRule</code>
	 * and <code>MapRule</code> in design file. The property
	 * <code>highlightTestExpre</code> and <code>MapTestExpre</code> were existed on
	 * <code>Style</code> element. Because of the schema change, they were moved
	 * into <code>HilightRule</code> and <code>MapRule</code> structure as a member
	 * property, which was renamed to <code>TestExpression</code>.
	 * 
	 * 
	 */
	protected void makeTestExpressionCompatible() {

		DesignElement element = getElement();

		// check whether the value of "highlightTestExpre" is in tempt map.
		if (handler.tempValue.get(IStyleModel.HIGHLIGHT_RULES_PROP) != null) {
			List highlightRules = element.getListProperty(handler.getModule(), IStyleModel.HIGHLIGHT_RULES_PROP);
			if (highlightRules != null) {
				for (int i = 0; i < highlightRules.size(); i++) {
					HighlightRule highlightRule = (HighlightRule) highlightRules.get(i);
					highlightRule.setTestExpression((String) handler.tempValue.get(IStyleModel.HIGHLIGHT_RULES_PROP));
				}
				handler.tempValue.remove(IStyleModel.HIGHLIGHT_RULES_PROP);
			}
		}
		// check whether the value of "mapTestExpre" is in tempt map.
		if (handler.tempValue.get(IStyleModel.MAP_RULES_PROP) != null) {
			List mapRules = element.getListProperty(handler.getModule(), IStyleModel.MAP_RULES_PROP);
			if (mapRules != null) {

				for (int i = 0; i < mapRules.size(); i++) {
					MapRule mapRule = (MapRule) mapRules.get(i);
					mapRule.setTestExpression((String) handler.tempValue.get(IStyleModel.MAP_RULES_PROP));
				}
				handler.tempValue.remove(IStyleModel.MAP_RULES_PROP);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		ElementDefn defn = (ElementDefn) getElement().getDefn();

		if (DesignSchemaConstants.OVERRIDDEN_VALUES_TAG.equalsIgnoreCase(tagName)) {
			if ((defn.getSlotCount() > 0 || defn.getContents().size() > 0) && defn.canExtend()) {
				return new OverriddenValuesState((ModuleParserHandler) getHandler(), getElement());
			} else if (getElement().canDynamicExtends()) {
				// fire a semantic warning to non-allowed overridden values
				getHandler().getErrorHandler()
						.semanticWarning(new XMLParserException(XMLParserException.DESIGN_EXCEPTION_UNKNOWN_TAG));
				return new AnyElementState(getHandler());
			}
		}
		return super.startElement(tagName);
	}

	public void end() throws SAXException {
		super.end();
		// if the element is a container and has extends
		if (getElement().getExtendsElement() != null && getElement().getDefn().isContainer()) {
			addTheVirualElements2Map(getElement());
			if (!handler.unhandleIDElements.contains(getElement()))
				handler.unhandleIDElements.add(getElement());
		}

		// creates handles so that to make sure Model API is read-only safe in
		// multiple threads.

		getElement().getHandle(handler.module);
	}
}
