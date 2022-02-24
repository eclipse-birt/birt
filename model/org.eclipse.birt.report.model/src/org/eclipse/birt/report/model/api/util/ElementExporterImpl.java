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

package org.eclipse.birt.report.model.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.command.GroupElementCommand;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.namespace.AbstractNameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Hierarchy;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.Measure;
import org.eclipse.birt.report.model.elements.olap.MeasureGroup;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ElementFactoryUtil;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Exports elements or structures to library. This class contains the handle for
 * target library and encapsulates the main logicas for exporting.
 */

class ElementExporterImpl {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ElementExporterImpl.class.getName());

	protected ModuleHandle targetModuleHandle;

	/**
	 * Records element has property binding. key is element in design handle. value
	 * is element in library handle.
	 */

	protected Map<DesignElementHandle, DesignElementHandle> propBindingMap = new HashMap<DesignElementHandle, DesignElementHandle>();

	/**
	 * Constructs the exporter with the handle of target library.
	 * 
	 * @param libraryHandle handle of the target library
	 */

	ElementExporterImpl(LibraryHandle libraryHandle) {
		this.targetModuleHandle = libraryHandle;
	}

	/**
	 * Constructs the exporter with the handle of the target of the design.
	 * 
	 * @param designHandle
	 */
	ElementExporterImpl(ReportDesignHandle designHandle) {
		this.targetModuleHandle = designHandle;
	}

	/**
	 * Default constructor.
	 */
	ElementExporterImpl() {

	}

	/**
	 * Checks whether the given element is suitable for exporting.
	 * <ul>
	 * <li>The element must be in design file.
	 * <li>The element must have name.
	 * </ul>
	 * 
	 * @param elementToExport handle of the element to export
	 * @param ignoreName      true if not consider the name of the element when
	 *                        determines whether the element can be export or not,
	 *                        false if must consider the element name to determine
	 */

	void checkElementToExport(DesignElementHandle elementToExport, boolean ignoreName) {
		ModuleHandle root = elementToExport.getRoot();
		if (!isSupportedExporting(root)) {
			throw new IllegalArgumentException("The element to export must be in design file."); //$NON-NLS-1$
		}

		if (StringUtil.isBlank(elementToExport.getName()) && !ignoreName) {
			throw new IllegalArgumentException("The element must have name defined."); //$NON-NLS-1$
		}
	}

	protected boolean isSupportedExporting(ModuleHandle rootToExport) {
		if (targetModuleHandle == null)
			return true;
		if (targetModuleHandle instanceof LibraryHandle) {
			return rootToExport instanceof ReportDesignHandle;
		}

		return false;
	}

	/**
	 * Checks whether the given structure is suitable for exporting.
	 * <ul>
	 * <li>The structure must be in design file.
	 * <li>The structure must have name property value.
	 * <li>The structure must be one of <code>EmbeddedImage</code>,
	 * <code>CustomColor</code> and <code>ConfigVariable</code>.
	 * </ul>
	 * 
	 * @param structToExport handle of the structure to export
	 * @param ignoreName     true if not consider the key name of the structure when
	 *                       determines whether the structure can be export or not,
	 *                       false if must consider the name to determine
	 */

	static void checkStructureToExport(StructureHandle structToExport, boolean ignoreName) {
		String memberName = null;
		String propName = null;

		// Check whether the structure is allowed to export.

		String structName = structToExport.getDefn().getName();
		if (EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equals(structName)) {
			propName = IModuleModel.IMAGES_PROP;
			memberName = EmbeddedImage.NAME_MEMBER;
		} else if (CustomColor.CUSTOM_COLOR_STRUCT.equals(structName)) {
			propName = IModuleModel.COLOR_PALETTE_PROP;
			memberName = CustomColor.NAME_MEMBER;
		} else if (ConfigVariable.CONFIG_VAR_STRUCT.equals(structName)) {
			propName = IModuleModel.CONFIG_VARS_PROP;
			memberName = ConfigVariable.NAME_MEMBER;
		} else {
			throw new IllegalArgumentException("The structure \"" //$NON-NLS-1$
					+ structName + "\" is not allowed to export."); //$NON-NLS-1$
		}

		// Check whether the name property value is defined.

		Object value = structToExport.getMember(memberName).getValue();
		if (StringUtil.isBlank((String) value) && !ignoreName) {
			throw new IllegalArgumentException("The structure \"" //$NON-NLS-1$
					+ structName + "\" must have member \"" + memberName + "\" defined."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Check whether this structure is in design file.

		boolean found = false;
		ModuleHandle moduleHandle = structToExport.getElementHandle().getModuleHandle();
		PropertyHandle propertyHandle = moduleHandle.getPropertyHandle(propName);

		List list = propertyHandle.getListValue();
		if (list != null) {
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Structure struct = (Structure) iter.next();
				if (struct == structToExport.getStructure()) {
					found = true;
					break;
				}
			}
		}

		if (!found) {
			throw new IllegalArgumentException("The structure to export must be in design file."); //$NON-NLS-1$
		}
	}

	private void checkOperation() {
		// only library handle supports these structures
		if (!(targetModuleHandle instanceof LibraryHandle)) {
			throw new IllegalOperationException("Only library handle supports this exporting operation!"); //$NON-NLS-1$
		}
	}

	/**
	 * Exports the given element.
	 * 
	 * @param structToExport handle of the structure to export.
	 * @param canOverride    indicates whether the structure with the same name in
	 *                       target library will be overriden.
	 * 
	 * @throws SemanticException if error encountered when adding this structure to
	 *                           target library or duplicating member value from the
	 *                           given structure.
	 */

	protected void exportStructure(StructureHandle structToExport, boolean canOverride) throws SemanticException {
		// only library handle supports these structures
		checkOperation();

		String structName = structToExport.getDefn().getName();
		String nameMemberName = null;

		Structure newStruct = null;
		PropertyHandle newPropertyHandle = null;

		if (EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equals(structName)) {
			nameMemberName = EmbeddedImage.NAME_MEMBER;
			newStruct = StructureFactory.createEmbeddedImage();
			newPropertyHandle = targetModuleHandle.getPropertyHandle(IModuleModel.IMAGES_PROP);
		} else if (CustomColor.CUSTOM_COLOR_STRUCT.equals(structName)) {
			nameMemberName = CustomColor.NAME_MEMBER;
			newStruct = StructureFactory.createCustomColor();
			newPropertyHandle = targetModuleHandle.getPropertyHandle(IModuleModel.COLOR_PALETTE_PROP);
		} else if (ConfigVariable.CONFIG_VAR_STRUCT.equals(structName)) {
			nameMemberName = ConfigVariable.NAME_MEMBER;
			newStruct = StructureFactory.createConfigVar();
			newPropertyHandle = targetModuleHandle.getPropertyHandle(IModuleModel.CONFIG_VARS_PROP);
		} else {
			throw new IllegalArgumentException("The structure \"" //$NON-NLS-1$
					+ structName + "\" is not allowed to export."); //$NON-NLS-1$
		}

		assert newStruct != null;
		assert newPropertyHandle != null;

		if (canOverride) {
			Object nameValue = structToExport.getMember(nameMemberName).getValue();
			if (nameValue != null) {
				Iterator iter = newPropertyHandle.iterator();
				while (iter.hasNext()) {
					StructureHandle structureHandle = (StructureHandle) iter.next();
					Object value = structureHandle.getMember(nameMemberName).getValue();

					if (nameValue.equals(value)) {
						IStructure struct = structureHandle.getStructure();
						newPropertyHandle.removeItem(struct);
						break;
					}
				}
			}
		}

		Iterator iter = structToExport.getDefn().propertiesIterator();
		while (iter.hasNext()) {
			PropertyDefn memberDefn = (PropertyDefn) iter.next();
			String memberName = memberDefn.getName();

			if (ReferencableStructure.LIB_REFERENCE_MEMBER.equals(memberName))
				continue;

			Object value = structToExport.getMember(memberName).getValue();
			Object valueToSet = ModelUtil.copyValue(memberDefn, value);

			newStruct.setProperty(memberName, valueToSet);
		}

		newPropertyHandle.addItem(newStruct);
	}

	/**
	 * Finds and Drops the duplicated element in library.
	 * 
	 * @param handle the element handle.
	 * @throws SemanticException
	 */
	protected void findAndDropDuplicatedElement(DesignElementHandle handle) throws SemanticException {
		if (handle.getName() != null && dropDuplicatedElement(handle.getElement()))
			return;

		ContentIterator iter = new ContentIterator(handle.getModule(), handle.getElement());

		while (iter.hasNext()) {
			DesignElement element = iter.next();
			if (element.getName() == null)
				continue;
			dropDuplicatedElement(element);
		}
	}

	/**
	 * Drops the duplicated element in library.
	 * 
	 * @param handle the design element
	 * @return true if the duplicated element is dropped, otherwise false.
	 * @throws SemanticException
	 */
	protected final boolean dropDuplicatedElement(DesignElement element) throws SemanticException {

		Module targetModule = targetModuleHandle.getModule();
		NameExecutor executor = new NameExecutor(targetModule, element);
		if (!executor.hasNamespace()) {
			return true;
		}

		DesignElement duplicateElement = executor.getElement(element.getName());

		if (duplicateElement == null) {
			if (isOLAPElement(element)) {
				AbstractNameHelper nameHelper = (AbstractNameHelper) executor.getNameHelper();
				String namespaceId = executor.getNameSpaceId();
				NameSpace nameSpace = nameHelper.getCachedNameSpace(namespaceId);
				duplicateElement = nameSpace.getElement(element.getName());
			} else
				return false;
		}

		DesignElement targetElement = getDropTarget(duplicateElement);
		if (targetElement == null)
			return false;

		// for OLAP element, rename it
		if (isOLAPElement(targetElement)) {
			executor.makeUniqueName();

			// handle default hierarchy property
			if (element instanceof Hierarchy) {
				Dimension targetDimension = (Dimension) element.getContainer();
				if (targetElement.getName().equals(
						targetDimension.getStringProperty(targetModule, IDimensionModel.DEFAULT_HIERARCHY_PROP))) {

					targetDimension.setDefaultHierarchy((Hierarchy) element);
				}

			}
			return false;
		}

		// check this element with duplicate name can be dropped or not
		if (!canDropInContext(targetElement)) {
			throw new SemanticException(element, new String[] { element.getName() },
					SemanticException.DESIGN_EXCEPTION__EXPORT_ELEMENT_FAIL);
		}

		targetElement.getHandle(targetModuleHandle.getModule()).drop();
		return true;
	}

	private boolean isOLAPElement(DesignElement element) {
		if (element instanceof Dimension || element instanceof Hierarchy || element instanceof Level
				|| element instanceof MeasureGroup || element instanceof Measure)
			return true;
		return false;
	}

	/**
	 * Checks if the element can be dropped according to the element context.
	 * 
	 * @param element the design element.
	 * @return <true> if the element locates in <code>Cube</code> or the element is
	 *         an extended item and locates in <code>ExtendedItem</code> ,otherwise
	 *         return false.
	 */
	static DesignElement getDropTarget(DesignElement element) {
		if (element == null)
			return null;

		String nameSpaceID = ((ElementDefn) element.getDefn()).getNameSpaceID();
		if (!(Module.CUBE_NAME_SPACE.equals(nameSpaceID) || Module.DIMENSION_NAME_SPACE.equals(nameSpaceID)
				|| Module.ELEMENT_NAME_SPACE.equals(nameSpaceID)))
			return element;

		DesignElement container = element.getContainer();
		while (container != null) {
			/*
			 * // checks element locates in cube if ( container instanceof Cube ) return
			 * container;
			 */
			if (container instanceof ExtendedItem) {
				// element locates in ExtendedItem, if the element is
				// ElementItem type this element can not be dropped, otherwise
				// it can
				if (element instanceof ExtendedItem) {
					ExtendedItem item = (ExtendedItem) container;
					Object dataset = item.getProperty(item.getRoot(), IReportItemModel.DATA_SET_PROP);
					Object cube = item.getProperty(item.getRoot(), IReportItemModel.CUBE_PROP);
					if (dataset != null || cube != null)
						return item;
				}
			}

			container = container.getContainer();
		}

		return element;
	}

	/**
	 * Checks if the element can be dropped according to the element context.
	 * 
	 * @param element the design element.
	 * @return <true> if the element locates in <code>Cube</code> or the element is
	 *         an extended item and locates in <code>ExtendedItem</code> ,otherwise
	 *         return false.
	 */
	static boolean canDropInContext(DesignElement element) {
		assert element != null;
		DesignElement container = element.getContainer();
		while (container != null) {
			if (container instanceof ExtendedItem) {
				// element locates in ExtendedItem, if the element is
				// ElementItem type this element can not be dropped, otherwise
				// it can
				if (element instanceof ExtendedItem)
					return false;

				return true;
			}

			container = container.getContainer();
		}
		return true;
	}

	/**
	 * Exports the given element.
	 * 
	 * @param elementToExport handle of the element to export.
	 * @param canOverride     indicates whether the element with the same name in
	 *                        target library will be overridden.
	 * @return the handle of the element exported
	 * @throws SemanticException if error encountered when adding this element to
	 *                           target library or duplicating property value from
	 *                           the given element.
	 */

	protected DesignElementHandle exportElement(DesignElementHandle elementToExport, boolean canOverride)
			throws SemanticException {
		if (elementToExport instanceof StyleHandle)
			return exportStyle((StyleHandle) elementToExport, canOverride);

		int slotID = getExportSlotID(elementToExport);

		// if the element only exist in the report design such as template
		// element definition, do not export it; MUST not use slotID >=
		// slotCount, for there is specified slot id cases in meta-data, like
		// datamart
		if (targetModuleHandle.getDefn().getSlot(slotID) == null)
			return null;

		DesignElementHandle newElementHandle = duplicateElement(elementToExport, false);

		// if canOverride, we must firstly drop the elements whose name are
		// duplicate with the exported element and its contents
		if (canOverride) {
			findAndDropDuplicatedElement(newElementHandle);
		}

		SlotHandle slotHandle = targetModuleHandle.getSlot(slotID);
		addToSlot(slotHandle, newElementHandle);

		if (propBindingMap.keySet().contains(elementToExport)) {
			propBindingMap.put(elementToExport, newElementHandle);
		}
		return newElementHandle;
	}

	/**
	 * Gets the slot id where the element resides in the target module.
	 * 
	 * @param elementToExport
	 * @return
	 */
	protected int getExportSlotID(DesignElementHandle elementToExport) {
		if (elementToExport == null)
			return DesignElement.NO_SLOT;
		int slotID = getTopContainerSlot(elementToExport.getElement());
		// The element in body slot should be added into components slot.
		if (slotID == IReportDesignModel.BODY_SLOT)
			slotID = IModuleModel.COMPONENT_SLOT;
		else if (slotID == IModuleModel.PAGE_SLOT
				&& elementToExport.getContainer() != elementToExport.getModuleHandle())
			slotID = IModuleModel.COMPONENT_SLOT;
		else if (slotID == IReportDesignModel.CUBE_SLOT)
			slotID = ILibraryModel.CUBE_SLOT;

		return slotID;
	}

	/**
	 * Export the given style to the target library.
	 * 
	 * @param elementToExport the style to export
	 * @param canOverride     <code>true</code> indicates the element with the same
	 *                        name in target library will be overriden. Otherwise
	 *                        <code>false</code> .
	 * @return the handle of the style exported
	 * @throws SemanticException
	 */

	protected DesignElementHandle exportStyle(StyleHandle elementToExport, boolean canOverride)
			throws SemanticException {
		checkOperation();

		SlotHandle themes = ((LibraryHandle) targetModuleHandle).getThemes();
		String defaultThemeName = ModelMessages.getMessage(IThemeModel.DEFAULT_THEME_NAME);

		// find the default theme

		NameSpace nameSpace = targetModuleHandle.getModule().getNameHelper().getNameSpace(Module.THEME_NAME_SPACE);

		Theme theme = (Theme) nameSpace.getElement(defaultThemeName);
		ThemeHandle themeHandle = null;

		// if no default theme, create it in the themes slot.

		if (theme == null) {
			themeHandle = targetModuleHandle.getElementFactory().newTheme(defaultThemeName);
			themes.add(themeHandle);
		} else
			themeHandle = (ThemeHandle) theme.getHandle(targetModuleHandle.getModule());

		return exportStyle(elementToExport, themeHandle, canOverride);
	}

	/**
	 * Export the given style to the target library.
	 * 
	 * @param elementToExport the style to export
	 * @param theme           the theme where the style exports.
	 * @param canOverride     <code>true</code> indicates the element with the same
	 *                        name in target library will be overriden. Otherwise
	 *                        <code>false</code> .
	 * @return the handle of the style exported
	 * @throws SemanticException
	 */

	protected DesignElementHandle exportStyle(StyleHandle elementToExport, ThemeHandle theme, boolean canOverride)
			throws SemanticException {
		assert theme != null;

		if (canOverride) {
			StyleHandle style = theme.findStyle(elementToExport.getName());
			if (style != null)
				style.drop();
		}

		DesignElementHandle newElementHandle = duplicateElement(elementToExport, false);
		addToSlot(theme.getStyles(), newElementHandle);
		return newElementHandle;
	}

	/**
	 * Change property binding's 'id' property. Let its' value related to the new
	 * data set element.
	 * 
	 * @param contentHandle
	 * @param refMap
	 */

	private void changePropertyBindingID(ReportDesignHandle designToExport) {
		List propertyBindings = targetModuleHandle.getListProperty(ReportDesignHandle.PROPERTY_BINDINGS_PROP);
		if (propertyBindings == null)
			return;

		Iterator iterator = propertyBindings.iterator();
		while (iterator.hasNext()) {
			PropertyBinding struct = (PropertyBinding) iterator.next();
			long id = struct.getID().longValue();
			DesignElementHandle tempHandle = designToExport.getElementByID(id);

			DesignElementHandle tempCopyInLibHandle = (DesignElementHandle) propBindingMap.get(tempHandle);
			if (tempCopyInLibHandle != null)
				struct.setID(tempCopyInLibHandle.getID());
		}
	}

	/**
	 * 
	 * Initialize property binding map. Each key item is design element handle which
	 * has property binding.
	 * 
	 * @param designToExport
	 */

	private void initPropBindingList(ReportDesignHandle designToExport) {
		List propertyBindings = designToExport.getListProperty(ReportDesignHandle.PROPERTY_BINDINGS_PROP);
		for (int i = 0; propertyBindings != null && i < propertyBindings.size(); ++i) {
			PropertyBinding struct = (PropertyBinding) propertyBindings.get(i);
			long id = struct.getID().longValue();
			DesignElementHandle tempHandle = designToExport.getElementByID(id);
			if (tempHandle != null && !propBindingMap.keySet().contains(tempHandle))
				propBindingMap.put(tempHandle, null);
		}
	}

	/**
	 * Exports the given design. The following rules are applied on exporting.
	 * <ul>
	 * <li>Only properties supported by library are exported.
	 * <li>Only top-level element with name are exported.
	 * </ul>
	 * 
	 * @param designToExport handle of the report design to export.
	 * @param canOverride    indicates whether the element with the same name in
	 *                       target library will be overriden.
	 * @param genDefaultName if true, a default name will be generated if an element
	 *                       doesn't has a name. if false, an exception will be
	 *                       throwed indicate that the element to export must has a
	 *                       name
	 * @throws SemanticException if error encountered when adding this element to
	 *                           target library or duplicating property value from
	 *                           the given element.
	 */

	void exportDesign(ReportDesignHandle designToExport, boolean canOverride, boolean genDefaultName)
			throws SemanticException {
		ModelUtil.duplicateProperties(designToExport, targetModuleHandle, false, false);

		initPropBindingList(designToExport);

		// Copy the contents in design file.

		int slotCount = designToExport.getDefn().getSlotCount();
		for (int i = 0; i < slotCount; i++) {
			SlotHandle sourceSlotHandle = designToExport.getSlot(i);
			Iterator iter = sourceSlotHandle.iterator();

			// First export element which has name.

			List noNameList = new ArrayList();
			while (iter.hasNext()) {
				DesignElementHandle contentHandle = (DesignElementHandle) iter.next();

				if (StringUtil.isBlank(contentHandle.getName())) {
					noNameList.add(contentHandle);
				} else {
					exportElement(contentHandle, canOverride);
				}
			}

			// Second export element which has no name.

			iter = noNameList.iterator();
			while (iter.hasNext()) {
				DesignElementHandle contentHandle = (DesignElementHandle) iter.next();
				if (!genDefaultName) {
					String typeName = contentHandle.getDefn().getDisplayName();
					String location = contentHandle.getElement().getIdentifier();

					throw new IllegalArgumentException("The element [type=\"" + typeName + "\"," //$NON-NLS-1$//$NON-NLS-2$
							+ "location=\"" + location + "\"] must have name defined."); //$NON-NLS-1$ //$NON-NLS-2$
				}

				exportElement(contentHandle, canOverride);
			}
		}

		// specially change property binding id.
		changePropertyBindingID(designToExport);
	}

	protected final int getTopContainerSlot(DesignElement element) {
		int slotID = element.getContainerInfo().getSlotID();

		DesignElement container = element.getContainer();
		while (!(container instanceof Module)) {
			slotID = container.getContainerInfo().getSlotID();
			container = container.getContainer();
			assert container != null;
		}

		return slotID;
	}

	/**
	 * @param elementHandle
	 * @return
	 */

	protected DesignElementHandle createNewElement(DesignElementHandle elementHandle) {
		String elementName = elementHandle.getDefn().getName();
		String name = elementHandle.getName();

		return ElementFactoryUtil.newElement(targetModuleHandle.getModule(), elementName, name, false);
	}

	/**
	 * Duplicates the given element in target module, including properties and
	 * contents.
	 * 
	 * @param elementHandle       handle of the element to duplicate
	 * @param onlyFactoryProperty indicate whether only factory property values are
	 *                            duplicated.
	 * 
	 * @return the handle of the duplicated element
	 * @throws SemanticException if error encountered when setting property or
	 *                           adding content into slot.
	 */

	protected final DesignElementHandle duplicateElement(DesignElementHandle elementHandle, boolean onlyFactoryProperty)
			throws SemanticException {
		DesignElementHandle newElementHandle = createNewElement(elementHandle);
		if (newElementHandle == null) {
			logger.severe("Cannot create the element instance for " //$NON-NLS-1$
					+ elementHandle.getDefn().getName());
			assert false;

			return null;
		}

		// Copy all properties from the original one to new element.
		ModelUtil.duplicateProperties(elementHandle, newElementHandle, onlyFactoryProperty, true, true);

		// if 'theme' property is defined, then clear it; otherwise do nothing
		if (newElementHandle.getElement() instanceof ISupportThemeElement) {
			PropertyHandle propHandle = newElementHandle.getPropertyHandle(ISupportThemeElementConstants.THEME_PROP);
			if (propHandle != null)
				propHandle.clearValue();
		}

		// Duplicate all contents in the original element to new one.

		duplicateSlots(elementHandle, newElementHandle);

		if (!onlyFactoryProperty && newElementHandle.getName() == null) {
			targetModuleHandle.getModule().makeUniqueName(newElementHandle.getElement());
		}

		return newElementHandle;
	}

	/**
	 * Duplicates the content elements from source element to destination element.
	 * 
	 * @param source      handle of the element to duplicate
	 * @param destination handle of of the destination element
	 * @throws SemanticException if error encountered when adding contents into
	 *                           slot.
	 */

	private void duplicateSlots(DesignElementHandle source, DesignElementHandle destination) throws SemanticException {
		int slotCount = source.getDefn().getSlotCount();

		// duplicate slot
		for (int i = 0; i < slotCount; i++) {
			SlotHandle sourceSlotHandle = source.getSlot(i);
			SlotHandle destinationSlotHandle = destination.getSlot(i);

			Iterator iter = sourceSlotHandle.iterator();
			while (iter.hasNext()) {
				DesignElementHandle contentHandle = (DesignElementHandle) iter.next();

				DesignElementHandle newContentHandle = duplicateElement(contentHandle, true);

				// table/list must have special handle for group slot if they
				// have binding-ref
				if (source instanceof ListingHandle && i == ListingHandle.GROUP_SLOT) {
					Object bindingRef = source.getProperty(ListingHandle.DATA_BINDING_REF_PROP);

					// if binding-ref is set whether is resolved or not, we must
					// call GroupContentCommand to localize the groups rather
					// than the content command to add group for content command
					// will throw out exception
					if (bindingRef != null) {
						GroupElementCommand cmd = new GroupElementCommand(destination.getModule(),
								new ContainerContext(destination.getElement(), i));
						cmd.setupSharedDataGroups(source.getElement());
					} else
						addToSlot(destinationSlotHandle, newContentHandle);

				} else
					addToSlot(destinationSlotHandle, newContentHandle);
			}
		}

		// duplicate container properties
		List props = source.getElement().getDefn().getContents();
		for (int i = 0; i < props.size(); i++) {

			IPropertyDefn propDefn = (IPropertyDefn) props.get(i);

			if (propDefn.getTypeCode() != IPropertyType.ELEMENT_TYPE)
				continue;

			String propName = propDefn.getName();
			Object value = source.getProperty(propName);

			if (value == null)
				continue;

			if (propDefn.isList()) {
				for (int j = 0; j < ((List) value).size(); j++) {
					DesignElementHandle contentHandle = (DesignElementHandle) ((ArrayList) value).get(j);

					DesignElementHandle newContentHandle = duplicateElement(contentHandle, true);

					destination.add(propName, newContentHandle);
				}
			} else {
				DesignElementHandle newContentHandle = duplicateElement(((DesignElementHandle) value), true);
				destination.add(propName, newContentHandle);
			}
		}
	}

	private void addToSlot(SlotHandle slotHandle, DesignElementHandle contentHandle) throws SemanticException {
		slotHandle.add(contentHandle);
	}
}
