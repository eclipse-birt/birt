/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.api.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.validators.StructureListValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.LibraryUtil;

/**
 * Represents the utility class to help export element and structure to library
 * file. The element or structure to export must comply with the following rule:
 * <ul>
 * <li>Element must have name defined.
 * <li>The name property of structure must have value.
 * <li>The element or structure must be contained in design file.
 * </ul>
 * 
 * Any violation will throw <code>IllegalArgumentException</code>.
 */

public class ElementExportUtilImpl {

	/**
	 * Exports the given element into one library file. If the library file is not
	 * found, new library file will be created and saved as the given file name.
	 * 
	 * @param elementToExport handle of the element to export.
	 * @param libraryFileName file name of the target library
	 * @param canOverride     indicates whether the element with the same name in
	 *                        target library will be overriden.
	 * 
	 * @throws DesignFileException      if error encountered when open library file
	 *                                  with the given file name.
	 * @throws SemanticException        if error encountered when element name is
	 *                                  duplicate in the target library.
	 * @throws IOException              if error encountered when writing file.
	 * @throws IllegalArgumentException if the element to export is not in design
	 *                                  file, or the element doesn't has name.
	 */

	public static void exportElement(DesignElementHandle elementToExport, String libraryFileName, boolean canOverride)
			throws DesignFileException, SemanticException, IOException {

		DesignSessionImpl session = elementToExport.getModule().getSession();
		LibraryHandle libraryHandle = openOrCreateLibrary(session, libraryFileName);
		assert libraryHandle != null;

		ElementExporter exporter = new ElementExporter(libraryHandle);
		exporter.checkElementToExport(elementToExport, true);

		exportElement(elementToExport, libraryHandle, canOverride);

		libraryHandle.save();
		libraryHandle.close();
	}

	/**
	 * Exports the given element list into one library file. If the library file is
	 * not found, new library file will be created and saved as the given file name.
	 * 
	 * @param elementsToExport list of the element to export, each of which is the
	 *                         instance of <code>DesignElementHandle</code>.
	 * @param libraryFileName  file name of the target library
	 * @param canOverride      indicates whether the element with the same name in
	 *                         target library will be overridden.
	 * 
	 * @throws DesignFileException      if error encountered when open library file
	 *                                  with the given file name.
	 * @throws SemanticException        if error encountered when element name is
	 *                                  duplicate in the target library.
	 * @throws IOException              if error encountered when writing file.
	 * @throws IllegalArgumentException if the element to export is not in design
	 *                                  file.
	 */

	public static void exportElements(List elementsToExport, String libraryFileName, boolean canOverride)
			throws DesignFileException, SemanticException, IOException {
		if (elementsToExport == null || elementsToExport.isEmpty())
			return;
		DesignSessionImpl session = ((DesignElementHandle) elementsToExport.get(0)).getModule().getSession();
		assert session != null;
		LibraryHandle libraryHandle = openOrCreateLibrary(session, libraryFileName);
		assert libraryHandle != null;

		ElementExporter exporter = new ElementExporter(libraryHandle);

		// Check the elements to export
		Iterator iter = elementsToExport.iterator();
		while (iter.hasNext()) {
			DesignElementHandle elementToExport = (DesignElementHandle) iter.next();

			exporter.checkElementToExport(elementToExport, true);
		}

		// Export all elements in the given list

		iter = elementsToExport.iterator();
		while (iter.hasNext()) {
			DesignElementHandle elementToExport = (DesignElementHandle) iter.next();

			exportElement(elementToExport, libraryHandle, canOverride);
		}

		libraryHandle.save();
		libraryHandle.close();
	}

	/**
	 * Exports the given element into one library.
	 * 
	 * @param elementToExport     handle of the element to export.
	 * @param targetLibraryHandle handle of target library
	 * @param canOverride         indicates whether the element with the same name
	 *                            in target library will be overridden.
	 * 
	 * @throws SemanticException        if error encountered when element name is
	 *                                  duplicate in the target library.
	 * @throws IllegalArgumentException if the element to export is not in design
	 *                                  file.
	 */

	public static void exportElement(DesignElementHandle elementToExport, LibraryHandle targetLibraryHandle,
			boolean canOverride) throws SemanticException {

		ElementExporter exporter = new ElementExporter(targetLibraryHandle);
		exporter.checkElementToExport(elementToExport, true);
		CommandStack stack = targetLibraryHandle.getCommandStack();
		try {
			stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.EXPORT_ELEMENT_TO_LIBRARY));
			exporter.exportElement(elementToExport, canOverride);
			stack.commit();
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
	}

	/**
	 * Exports the given style into selected theme.
	 * 
	 * @param styleToExport the style to be exported.
	 * @param themeHandle   the theme handle.
	 * @param canOverride   indicates whether the element with the same name in
	 *                      target library will be overriden.
	 * @throws SemanticException
	 */
	public static void exportStyle(StyleHandle styleToExport, ThemeHandle themeHandle, boolean canOverride)
			throws SemanticException {
		LibraryHandle libraryHandle = (LibraryHandle) themeHandle.getRoot();
		ElementExporter exporter = new ElementExporter(libraryHandle);
		exporter.checkElementToExport(styleToExport, true);
		exporter.exportStyle(styleToExport, themeHandle, canOverride);
	}

	/**
	 * Exports the given structure into one library. The allowed structures are
	 * <code>EmbeddedImage</code>,<code>CustomColor</code> and
	 * <code>ConfigVariable</code>. If the library file is not found, new library
	 * file will be created and saved as the given file name.
	 * 
	 * @param structToExport  handle of the structure to export.
	 * @param libraryFileName file name of the target library
	 * @param canOverride     indicates whether the structure with the same name in
	 *                        target library will be overriden.
	 * 
	 * @throws DesignFileException      if error encountered when open library file
	 *                                  with the given file name.
	 * @throws SemanticException        if error encountered when adding new
	 *                                  structure to library or value setting.
	 * @throws IOException              if error encountered when writing file.
	 * @throws IllegalArgumentException if the element to export is not in design
	 *                                  file.
	 */

	public static void exportStructure(StructureHandle structToExport, String libraryFileName, boolean canOverride)
			throws DesignFileException, SemanticException, IOException {
		ElementExporter.checkStructureToExport(structToExport, true);
		DesignSessionImpl session = structToExport.getElementHandle().getModule().getSession();
		LibraryHandle libraryHandle = openOrCreateLibrary(session, libraryFileName);
		assert libraryHandle != null;

		exportStructure(structToExport, libraryHandle, canOverride);

		libraryHandle.save();
		libraryHandle.close();
	}

	/**
	 * Exports the given structure into one library. The allowed structures are
	 * <code>EmbeddedImage</code>,<code>CustomColor</code> and
	 * <code>ConfigVariable</code>. If the library file is not found, new library
	 * file will be created and saved as the given file name.
	 * 
	 * @param structsToExport list of the structure to export, each of which is the
	 *                        instance of <code>StructureHandle</code>.
	 * @param libraryFileName file name of the target library
	 * @param canOverride     indicates whether the structure with the same name in
	 *                        target library will be overridden.
	 * 
	 * @throws DesignFileException      if error encountered when open library file
	 *                                  with the given file name.
	 * @throws SemanticException        if error encountered when adding new
	 *                                  structure to library or value setting.
	 * @throws IOException              if error encountered when writing file.
	 * @throws IllegalArgumentException if the element to export is not in design
	 *                                  file.
	 */

	public static void exportStructures(List structsToExport, String libraryFileName, boolean canOverride)
			throws DesignFileException, SemanticException, IOException {
		DesignSessionImpl session = null;

		// Check the structs to export

		Iterator iter = structsToExport.iterator();
		while (iter.hasNext()) {
			StructureHandle structToExport = (StructureHandle) iter.next();

			ElementExporter.checkStructureToExport(structToExport, true);
			if (session == null)
				session = structToExport.getElementHandle().getModule().getSession();
		}

		// That means the list is empty.

		if (session == null)
			return;

		LibraryHandle libraryHandle = openOrCreateLibrary(session, libraryFileName);
		assert libraryHandle != null;

		// Export all elements in the given list

		iter = structsToExport.iterator();
		while (iter.hasNext()) {
			StructureHandle structToExport = (StructureHandle) iter.next();

			exportStructure(structToExport, libraryHandle, canOverride);
		}

		libraryHandle.save();
		libraryHandle.close();
	}

	/**
	 * Exports the given structure into one library. The allowed structures are
	 * <code>EmbeddedImage</code>,<code>CustomColor</code> and
	 * <code>ConfigVariable</code>.
	 * 
	 * @param structToExport      handle of the structure to export.
	 * @param targetLibraryHandle handle of target library
	 * @param canOverride         indicates whether the structure with the same name
	 *                            in target library will be overriden.
	 * 
	 * @throws SemanticException if error encountered when adding new structure to
	 *                           library or value setting.
	 */

	public static void exportStructure(StructureHandle structToExport, LibraryHandle targetLibraryHandle,
			boolean canOverride) throws SemanticException {
		ElementExporter.checkStructureToExport(structToExport, true);

		ElementExporter exporter = new ElementExporter(targetLibraryHandle);

		exporter.exportStructure(structToExport, canOverride);
	}

	/**
	 * Export the report design to a library file specified by the file name. If the
	 * library file is not found, new library file will be created and saved as the
	 * given name.
	 * 
	 * @param designToExport  the design which need to be exported
	 * @param libraryFileName target library file name
	 * @param canOverride     indicates whether the element with the same name in
	 *                        target library will be overriden.
	 * @param genDefaultName  if true, a default name will be generated if an
	 *                        element doesn't has a name. if false, an exception
	 *                        will be throwed indicate that the element to export
	 *                        must has a name
	 * @throws DesignFileException if the library file has fatal errors which can
	 *                             not be opened.
	 * @throws SemanticException   if error encountered when element name is
	 *                             duplicated in the target library.
	 * 
	 * @throws IOException         if error encountered when writing file.
	 */

	public static void exportDesign(ReportDesignHandle designToExport, String libraryFileName, boolean canOverride,
			boolean genDefaultName) throws DesignFileException, SemanticException, IOException {
		DesignSessionImpl session = designToExport.getModule().getSession();
		LibraryHandle libraryHandle = openOrCreateLibrary(session, libraryFileName);
		assert libraryHandle != null;

		exportDesign(designToExport, libraryHandle, canOverride, genDefaultName);

		// provides backward compatibility of bound columns during writing the
		// design file.
		String designVersion = designToExport.getVersion();
		String libVersion = libraryHandle.getVersion();
		if (StringUtil.isBlank(libVersion)) {
			libraryHandle.getModule().getVersionManager().setVersion(designVersion);
		}
		libraryHandle.save();
		libraryHandle.close();
	}

	/**
	 * Exports one design to library.
	 * 
	 * @param designToExport      handle of the report design to export
	 * @param targetLibraryHandle handle of target library
	 * @param canOverride         indicates whether the element with the same name
	 *                            in target library will be overridden.
	 * @param genDefaultName      if true, a default name will be generated if an
	 *                            element doesn't has a name. if false, an exception
	 *                            will be thrown indicate that the element to export
	 *                            must has a name
	 * @throws SemanticException if error encountered when element name is duplicate
	 *                           in the target library.
	 */

	public static void exportDesign(ReportDesignHandle designToExport, LibraryHandle targetLibraryHandle,
			boolean canOverride, boolean genDefaultName) throws SemanticException {

		if (LibraryUtil.hasLibrary(designToExport, targetLibraryHandle)) {
			throw new SemanticException(designToExport.getElement(),
					LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY);
		}
		ElementExporter exporter = new ElementExporter(targetLibraryHandle);
		CommandStack stack = targetLibraryHandle.getCommandStack();
		try {

			stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.EXPORT_ELEMENT_TO_LIBRARY));
			exporter.exportDesign(designToExport, canOverride, genDefaultName);
			stack.commit();

		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
	}

	/**
	 * Opens the library with given library file name. If the library is not found,
	 * create it.
	 * 
	 * @param session         the session
	 * @param libraryFileName file name of the library to open
	 * @return the opened library handle
	 * @throws DesignFileException if error encountered when open library file with
	 *                             the given file name.
	 */

	private static LibraryHandle openOrCreateLibrary(DesignSessionImpl session, String libraryFileName)
			throws DesignFileException {

		try {
			Library library = session.openLibrary(libraryFileName);
			return library.handle();
		} catch (DesignFileException e) {
			if (DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR == e.getErrorCode()) {
				List errorList = e.getErrorList();

				// FILE_NOT_FOUND error is always the first one.

				ErrorDetail error = ((ErrorDetail) errorList.get(0));
				if (DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND == error.getErrorCode()) {
					Library library = session.createLibrary();
					LibraryHandle libraryHandle = library.handle();
					libraryHandle.setFileName(libraryFileName);
					return libraryHandle;
				}
			}

			throw e;
		}
	}

	/**
	 * Checks whether the given element can be exported into one library.
	 * 
	 * @param elementToExport     handle of the element to export.
	 * @param targetLibraryHandle handle of target library
	 * @param canOverride         indicates whether the element with the same name
	 *                            in target library will be overridden.
	 * @return <code>true</code> if the element can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */

	public static boolean canExport(DesignElementHandle elementToExport, LibraryHandle targetLibraryHandle,
			boolean canOverride) {
		if (!canExport(elementToExport))
			return false;

		if (targetLibraryHandle == null)
			return false;

		// check canExport in IReportItem for extended item
		if (elementToExport instanceof ExtendedItemHandle
				&& !checkExportedExtendedItem((ExtendedItemHandle) elementToExport)) {
			return false;
		}

		if (elementToExport instanceof ReportItemHandle || elementToExport instanceof CubeHandle
				|| elementToExport instanceof DataSourceHandle || elementToExport instanceof DataSetHandle
				|| elementToExport instanceof ParameterHandle || elementToExport instanceof ParameterGroupHandle
				|| elementToExport instanceof MasterPageHandle || elementToExport instanceof StyleHandle) {
			return checkExportElementByContext(elementToExport, targetLibraryHandle, canOverride);

		}

		return false;
	}

	/**
	 * Checks if extendedItem and cube's content element has the same name as the
	 * exported element and its content elements.
	 * 
	 * @param handle              the element handle.
	 * @param targetLibraryHandle the target library handle.
	 * @param canOverride         indicates whether the element with the same name
	 *                            in target library will be overridden.
	 * @return <true> if extendedItem and cube's content element has the same name
	 *         as the exported element and its content elements, otherwise return
	 *         <false>.
	 */
	protected final static boolean checkExportElementByContext(DesignElementHandle handle,
			ModuleHandle targetLibraryHandle, boolean canOverride) {
		if (!checkExportableElementByContext(handle.getElement(), targetLibraryHandle, canOverride))
			return false;

		ContentIterator iter = new ContentIterator(handle.getModule(), handle.getElement());

		while (iter.hasNext()) {
			DesignElement element = iter.next();
			if (!checkExportableElementByContext(element, targetLibraryHandle, canOverride))
				return false;
		}

		return true;
	}

	/**
	 * Checks if extendedItem and cube's content element has the same name as the
	 * exported element and its content elements.
	 * 
	 * @param element             the design element.
	 * @param targetLibraryHandle the target library handle.
	 * @return <true> if extendedItem and cube's content element has the same name
	 *         as the exported element and its content elements, otherwise return
	 *         <false>.
	 */
	private static boolean checkExportableElementByContext(DesignElement element, ModuleHandle targetLibraryHandle,
			boolean canOverride) {
		String name = element.getName();
		if (name == null)
			return true;

		// special handle about style exporting, for API compatibility for old
		// method canExport did not provide the theme handle to export
		if (element instanceof StyleElement) {
			DesignElement style = targetLibraryHandle.getModule().findStyle(name);
			if (style == null || canOverride)
				return true;
			return false;
		}

		NameExecutor executor = new NameExecutor(targetLibraryHandle.getModule(), element);
		if (!executor.hasNamespace()) {
			return true;
		}
		DesignElement duplicateElement = executor.getElement(name);
		if (duplicateElement == null)
			return true;

		DesignElement targetElement = ElementExporterImpl.getDropTarget(duplicateElement);
		if (targetElement == null)
			return false;

		if (canOverride) {
			if (ElementExporterImpl.canDropInContext(targetElement))
				return true;
			return false;
		}

		return false;
	}

	/**
	 * Checks whether the given element can be exported into the selected theme.
	 * 
	 * @param styleHandle handle of the style.
	 * @param themeHandle the theme handle.
	 * @param canOverride indicates whether the element with the same name in target
	 *                    library will be overridden.
	 * @return <code>true</code> if the style can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */
	public static boolean canExport(StyleHandle styleHandle, ThemeHandle themeHandle, boolean canOverride) {
		if (themeHandle == null)
			return false;
		if (!canExport(styleHandle))
			return false;

		if (canOverride)
			return true;

		return themeHandle.findStyle(styleHandle.getName()) == null;
	}

	/**
	 * Checks whether the given structure can be exported into one library.
	 * Currently, only allows structures such as <code>EmbeddedImage</code>,
	 * <code>CustomColor</code> and <code>ConfigVariable</code>.
	 * 
	 * @param structToExport      the handle of the structure to export.
	 * @param targetLibraryHandle the handle of target library
	 * @param canOverride         indicates whether the element with the same name
	 *                            in target library will be overridden.
	 * @return <code>true</code> if the element can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */

	public static boolean canExport(StructureHandle structToExport, LibraryHandle targetLibraryHandle,
			boolean canOverride) {
		if (!canExport(structToExport))
			return false;

		if (targetLibraryHandle == null)
			return false;

		IPropertyDefn propDefn = null;
		String structName = structToExport.getDefn().getName();
		if (EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equals(structName))
			propDefn = targetLibraryHandle.getPropertyDefn(IModuleModel.IMAGES_PROP);
		else if (CustomColor.CUSTOM_COLOR_STRUCT.equals(structName))
			propDefn = targetLibraryHandle.getPropertyDefn(IModuleModel.COLOR_PALETTE_PROP);
		else if (ConfigVariable.CONFIG_VAR_STRUCT.equals(structName))
			propDefn = targetLibraryHandle.getPropertyDefn(IModuleModel.CONFIG_VARS_PROP);
		else {
			return false;
		}

		if (canOverride)
			return true;

		List results = StructureListValidator.getInstance().validateForAdding(targetLibraryHandle, propDefn,
				targetLibraryHandle.getListProperty(propDefn.getName()), structToExport.getStructure());
		if (results.isEmpty())
			return true;

		return false;
	}

	/**
	 * Checks whether the given element can be exported into one library. Different
	 * from another <code>canExport</code>, this method only concerns meta-data
	 * level. That is, based on element definition and regardless of the design
	 * context.
	 * 
	 * @param elementToExport handle of the element to export.
	 * @return <code>true</code> if the element can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */

	public static boolean canExport(DesignElementHandle elementToExport) {

		return canExport(elementToExport, true);
	}

	/**
	 * Checks whether the given structure can be exported into one library.
	 * Currently, only allows structures such as <code>EmbeddedImage</code>,
	 * <code>CustomColor</code> and <code>ConfigVariable</code>.
	 * <p>
	 * Different from another <code>canExport</code>, this method only concerns
	 * meta-data level. That is, based on structure definition and regardless of the
	 * design context.
	 * 
	 * @param structToExport the handle of the structure to export.
	 * @return <code>true</code> if the structure can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */

	public static boolean canExport(StructureHandle structToExport) {
		return canExport(structToExport, true);
	}

	/**
	 * Checks whether the given element can be exported into one library. Different
	 * from another <code>canExport</code>, this method only concerns meta-data
	 * level. That is, based on element definition and regardless of the design
	 * context.
	 * 
	 * @param elementToExport handle of the element to export
	 * @param ignoreName      true if not consider the name of the element when
	 *                        determines whether the element can be export or not,
	 *                        false if must consider the element name to determine
	 * @return <code>true</code> if the element can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */

	public static boolean canExport(DesignElementHandle elementToExport, boolean ignoreName) {
		if (elementToExport == null)
			return false;

		ElementExporter exporter = new ElementExporter(new LibraryHandle(new Library(new DesignSession(null))));
		try {
			exporter.checkElementToExport(elementToExport, ignoreName);
		} catch (IllegalArgumentException e) {
			return false;
		}

		if (elementToExport instanceof CubeHandle) {
			return true;
		}

		if (elementToExport instanceof DataSourceHandle) {
			return true;
		}

		if (elementToExport instanceof DataSetHandle) {
			return true;
		}

		if (elementToExport instanceof ParameterHandle || elementToExport instanceof ParameterGroupHandle) {
			return true;
		}

		if (elementToExport instanceof ExtendedItemHandle
				&& !checkExportedExtendedItem((ExtendedItemHandle) elementToExport)) {
			return false;
		}

		if (elementToExport instanceof ReportItemHandle) {
			return true;
		}

		if (elementToExport instanceof MasterPageHandle) {
			return true;
		}

		if (elementToExport instanceof StyleHandle) {
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the given structure can be exported into one library.
	 * Currently, only allows structures such as <code>EmbeddedImage</code>,
	 * <code>CustomColor</code> and <code>ConfigVariable</code>.
	 * <p>
	 * Different from another <code>canExport</code>, this method only concerns
	 * meta-data level. That is, based on structure definition and regardless of the
	 * design context.
	 * 
	 * @param structToExport the handle of the structure to export
	 * @param ignoreName     true if not consider the key name of the structure when
	 *                       determines whether the structure can be export or not,
	 *                       false if must consider the name to determine
	 * @return <code>true</code> if the structure can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */

	public static boolean canExport(StructureHandle structToExport, boolean ignoreName) {
		if (structToExport == null)
			return false;

		try {
			ElementExporter.checkStructureToExport(structToExport, ignoreName);
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	/**
	 * Checks extended item can be exported or not.
	 * 
	 * @param elementToExport the extended item to export
	 * @return <code>true</code> if the element can be exported successfully.
	 *         Otherwise <code>false</code>.
	 */
	protected static boolean checkExportedExtendedItem(ExtendedItemHandle elementToExport) {
		try {
			IReportItem item = elementToExport.getReportItem();

			if (item != null && !item.canExport())
				return false;
		} catch (ExtendedElementException e) {
			return false;
		}
		return true;
	}
}
