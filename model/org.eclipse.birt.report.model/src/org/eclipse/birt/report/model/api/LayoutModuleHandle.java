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

package org.eclipse.birt.report.model.api;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.command.CustomMsgCommand;
import org.eclipse.birt.report.model.command.LibraryCommand;
import org.eclipse.birt.report.model.command.ShiftLibraryCommand;
import org.eclipse.birt.report.model.command.ThemeCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.StyleSheetLoader;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Abstract module handle which provides the common functionalities of report
 * design and library.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Content Item</th>
 * <th width="40%">Description</th>
 * 
 * <tr>
 * <td>Code Modules</td>
 * <td>Global scripts that apply to the report as a whole.</td>
 * </tr>
 * 
 * <tr>
 * <td>Parameters</td>
 * <td>A list of Parameter elements that describe the data that the user can
 * enter when running the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sources</td>
 * <td>The connections used by the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sets</td>
 * <td>Data sets defined in the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Color Palette</td>
 * <td>A set of custom color names as part of the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Styles</td>
 * <td>User-defined styles used to format elements in the report. Each style
 * must have a unique name within the set of styles for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Page Setup</td>
 * <td>The layout of the master pages within the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Components</td>
 * <td>Reusable report items defined in this design. Report items can extend
 * these items. Defines a "private library" for this design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Translations</td>
 * <td>The list of externalized messages specifically for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Images</td>
 * <td>A list of images embedded in this report.</td>
 * </tr>
 * 
 * </table>
 */

abstract class LayoutModuleHandle extends ModuleHandle {

	/**
	 * Constructs one module handle with the given module element.
	 * 
	 * @param module module
	 */

	public LayoutModuleHandle(Module module) {
		super(module);

		initializeSlotHandles();
		cachePropertyHandles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#addConfigVariable(org.
	 * eclipse.birt.report.model.api.elements.structures.ConfigVariable)
	 */

	public void addConfigVariable(ConfigVariable configVar) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(CONFIG_VARS_PROP);

		if (configVar != null && StringUtil.isBlank(configVar.getName())) {
			throw new PropertyValueException(getElement(), propDefn, configVar,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE);
		}

		if (configVar != null && findConfigVariable(configVar.getName()) != null) {
			throw new PropertyValueException(getElement(), propDefn, configVar.getName(),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS);
		}

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.addItem(new StructureContext(getElement(), propDefn, null), configVar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#addImage(org.eclipse.birt
	 * .report.model.api.elements.structures.EmbeddedImage)
	 */
	public void addImage(EmbeddedImage image) throws SemanticException {
		ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, getElement());
		ElementPropertyDefn propDefn = module.getPropertyDefn(IMAGES_PROP);
		cmd.addItem(new StructureContext(module, propDefn, null), image);
	}

	/**
	 * Adds all the parameters under the given parameter group to a list.
	 * 
	 * @param list   the list to which the parameters are added.
	 * @param handle the handle to the parameter group.
	 */

	private void addParameters(ArrayList list, ParameterGroupHandle handle) {
		SlotHandle h = handle.getParameters();
		Iterator it = h.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#addTranslation(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public void addTranslation(String resourceKey, String locale, String text) throws CustomMsgException {
		CustomMsgCommand command = new CustomMsgCommand(getModule());
		command.addTranslation(resourceKey, locale, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#dropConfigVariable(java
	 * .lang.String)
	 */
	public void dropConfigVariable(String name) throws SemanticException {
		PropertyHandle propHandle = this.getPropertyHandle(CONFIG_VARS_PROP);

		int posn = findConfigVariablePos(name);
		if (posn < 0)
			throw new PropertyValueException(getElement(), propHandle.getPropertyDefn(), name,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		propHandle.removeItem(posn);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#dropImage(java.util.List)
	 */

	public void dropImage(List images) throws SemanticException {
		if (images == null)
			return;
		PropertyHandle propHandle = this.getPropertyHandle(IMAGES_PROP);
		propHandle.removeItems(images);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.ModuleHandle#dropImage(java.lang.String )
	 */

	public void dropImage(String name) throws SemanticException {
		PropertyHandle propHandle = this.getPropertyHandle(IMAGES_PROP);

		int pos = findImagePos(name);
		if (pos < 0)
			throw new PropertyValueException(getElement(), propHandle.getPropertyDefn(), name,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		propHandle.removeItem(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#dropTranslation(java.lang
	 * .String, java.lang.String)
	 */

	public void dropTranslation(String resourceKey, String locale) throws CustomMsgException {
		CustomMsgCommand command = new CustomMsgCommand(getModule());
		command.dropTranslation(resourceKey, locale);
	}

	/**
	 * Finds the position of the config variable with the given name.
	 * 
	 * @param name the config variable name
	 * @return the index ( from 0 ) of config variable with the given name. Return
	 *         -1, if not found.
	 * 
	 */

	private int findConfigVariablePos(String name) {
		List configVars = (List) module.getLocalProperty(module, CONFIG_VARS_PROP);
		if (configVars == null)
			return -1;

		int i = 0;
		for (Iterator iter = configVars.iterator(); iter.hasNext(); i++) {
			ConfigVariable var = (ConfigVariable) iter.next();

			if (var.getName().equals(name)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Finds the position of the image with the given name.
	 * 
	 * @param name the image name to find
	 * @return position of image with the specified name. Return -1, if not found.
	 */

	private int findImagePos(String name) {
		List images = (List) module.getLocalProperty(module, IMAGES_PROP);
		if (images == null || images.isEmpty())
			return -1;

		int i = 0;
		for (Iterator iter = images.iterator(); iter.hasNext(); i++) {
			EmbeddedImage image = (EmbeddedImage) iter.next();

			if (image.getName() != null && image.getName().equalsIgnoreCase(name)) {
				return i;
			}
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getComponents()
	 */
	public SlotHandle getComponents() {
		return getSlot(COMPONENT_SLOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getDataSets()
	 */
	public SlotHandle getDataSets() {
		return getSlot(DATA_SET_SLOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getDataSources()
	 */
	public SlotHandle getDataSources() {
		return getSlot(DATA_SOURCE_SLOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getFlattenParameters()
	 */
	public List getFlattenParameters() {
		ArrayList list = new ArrayList();
		SlotHandle slotHandle = getParameters();
		Iterator it = slotHandle.iterator();
		while (it.hasNext()) {
			DesignElementHandle h = (DesignElementHandle) it.next();
			list.add(h);
			if (h instanceof ParameterGroupHandle) {
				addParameters(list, (ParameterGroupHandle) h);
			}
		}
		DesignElementHandle.doSort(list);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getMasterPages()
	 */
	public SlotHandle getMasterPages() {
		return getSlot(PAGE_SLOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getParameters()
	 */
	public SlotHandle getParameters() {
		return getSlot(PARAMETER_SLOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getStyles()
	 */
	public SlotHandle getStyles() {
		return getSlot(IReportDesignModel.STYLE_SLOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#imagesIterator()
	 */

	public Iterator imagesIterator() {
		return getPropertyHandle(IMAGES_PROP).iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#replaceConfigVariable(
	 * org.eclipse.birt.report.model.api.elements.structures.ConfigVariable,
	 * org.eclipse.birt.report.model.api.elements.structures.ConfigVariable)
	 */
	public void replaceConfigVariable(ConfigVariable oldVar, ConfigVariable newVar) throws SemanticException {
		replaceObjectInList(CONFIG_VARS_PROP, oldVar, newVar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#replaceImage(org.eclipse
	 * .birt.report.model.api.elements.structures.EmbeddedImage,
	 * org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage)
	 */

	public void replaceImage(EmbeddedImage oldVar, EmbeddedImage newVar) throws SemanticException {
		replaceObjectInList(IMAGES_PROP, oldVar, newVar);
	}

	/**
	 * Replaces an old object in the structure list with the given new one.
	 * 
	 * @param propName the name of the property that holds a structure list
	 * @param oldVar   an existed object in the list
	 * @param newVar   a new object
	 * @throws SemanticException if the old object is not found or the name of new
	 *                           one is empty.
	 */

	private void replaceObjectInList(String propName, Object oldVar, Object newVar) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(propName);

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, getElement());
		cmd.replaceItem(new StructureContext(getElement(), propDefn, null), (Structure) oldVar, (Structure) newVar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getAllStyles()
	 */

	public List getAllStyles() {
		List elementList = module.getNameHelper().getElements(Module.STYLE_NAME_SPACE, IAccessControl.ARBITARY_LEVEL);

		return generateHandleList(elementList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getVisibleThemes(int)
	 */

	public List getVisibleThemes(int level) {
		List elementList = module.getNameHelper().getElements(Module.THEME_NAME_SPACE, level);
		List<DesignElement> elements = new ArrayList<DesignElement>();
		for (int i = 0; i < elementList.size(); i++) {
			DesignElement element = (DesignElement) elementList.get(i);
			if (element instanceof Theme)
				elements.add(element);
		}

		return generateHandleList(sortVisibleElements(elements, level));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.ModuleHandle#getVisibleReportItemThemes
	 * (int, java.lang.String)
	 */
	public List<ReportItemThemeHandle> getVisibleReportItemThemes(int level, String type) {
		if (!ReportItemTheme.isValidType(type))
			return Collections.emptyList();
		List elementList = module.getNameHelper().getElements(Module.THEME_NAME_SPACE, level);
		List<DesignElement> elements = new ArrayList<DesignElement>();
		for (int i = 0; i < elementList.size(); i++) {
			DesignElement element = (DesignElement) elementList.get(i);
			if (element instanceof ReportItemTheme) {
				ReportItemTheme theme = (ReportItemTheme) element;
				if (type.equals(theme.getType(module)))
					elements.add(element);
			}
		}

		return generateHandleList(sortVisibleElements(elements, level));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.ModuleHandle#
	 * getParametersAndParameterGroups()
	 */

	public List getParametersAndParameterGroups() {
		SlotHandle params = getSlot(PARAMETER_SLOT);

		List retList = new ArrayList();
		for (int i = 0; i < params.getCount(); i++) {
			retList.add(params.get(i));
		}

		return retList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getAllPages()
	 */

	public List getAllPages() {
		List elementList = module.getNameHelper().getNameSpace(Module.PAGE_NAME_SPACE).getElements();

		return generateHandleList(elementList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#shiftLibrary(org.eclipse
	 * .birt.report.model.api.LibraryHandle, int)
	 */

	public void shiftLibrary(LibraryHandle library, int toPosn) throws SemanticException {
		if (library == null)
			return;

		ShiftLibraryCommand command = new ShiftLibraryCommand(module);
		command.shiftLibrary((Library) library.getElement(), toPosn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.ModuleHandle#includeLibrariesIterator()
	 */
	public Iterator includeLibrariesIterator() {
		PropertyHandle propHandle = getPropertyHandle(LIBRARIES_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#includeLibrary(java.lang
	 * .String, java.lang.String)
	 */

	public void includeLibrary(String libraryFileName, String namespace) throws DesignFileException, SemanticException {
		LibraryCommand command = new LibraryCommand(module);
		command.addLibrary(libraryFileName, namespace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#dropLibrary(org.eclipse
	 * .birt.report.model.api.LibraryHandle)
	 */

	public void dropLibrary(LibraryHandle library) throws SemanticException {
		if (library == null)
			return;

		LibraryCommand command = new LibraryCommand(module);
		command.dropLibrary((Library) library.getElement());

		checkReportIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#reloadLibrary(org.eclipse
	 * .birt.report.model.api.LibraryHandle)
	 */

	public void reloadLibrary(LibraryHandle libraryToReload) throws SemanticException, DesignFileException {
		if (libraryToReload == null)
			return;

		Map reloadLibs = new HashMap();
		LibraryCommand command = new LibraryCommand(module);

		String location = libraryToReload.getLocation();
		if (location == null)
			location = libraryToReload.getFileName();

		command.reloadLibrary(location, reloadLibs);

		checkReportIfNecessary();
	}

	/**
	 * Reloads the library this module includes. <code>libraryToReload</code> must
	 * be directly/indirectly included in the module.
	 * 
	 * @param libraryToReload the library to reload
	 * @param reloadLibs      the map contains library files that has been reload
	 * 
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	private void reloadLibrary(Library libraryToReload, IncludedLibrary includedLib, Map reloadLibs)
			throws SemanticException, DesignFileException {
		if (libraryToReload == null)
			return;

		LibraryCommand command = new LibraryCommand(module);
		command.reloadLibrary((Library) libraryToReload, includedLib, reloadLibs);

		checkReportIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#reloadLibraries()
	 */

	public void reloadLibraries() throws SemanticException, DesignFileException {
		List libs = getListProperty(IModuleModel.LIBRARIES_PROP);
		if (libs == null || libs.isEmpty())
			return;

		List cachedList = new ArrayList();
		cachedList.addAll(libs);

		Map reloadLibs = new HashMap();

		for (int i = 0; i < cachedList.size(); i++) {
			IncludedLibrary lib = (IncludedLibrary) cachedList.get(i);
			Library includeLib = module.getLibraryWithNamespace(lib.getNamespace(),
					IAccessControl.DIRECTLY_INCLUDED_LEVEL);
			if (includeLib != null)
				reloadLibrary(includeLib, lib, reloadLibs);
			else {
				LibraryCommand cmd = new LibraryCommand(module);
				cmd.reloadLibrary(lib.getFileName(), lib.getNamespace());
			}

		}

		checkReportIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#reloadLibrary(java.lang
	 * .String)
	 */

	public void reloadLibrary(String reloadPath) throws SemanticException, DesignFileException {
		if (StringUtil.isEmpty(reloadPath))
			return;

		URL url = ModelUtil.getURLPresentation(reloadPath);
		String path = null;
		if (url != null)
			path = url.toExternalForm();

		if (path == null) {
			DesignParserException ex = new DesignParserException(new String[] { reloadPath },
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND);
			List exceptionList = new ArrayList();
			exceptionList.add(ex);
			throw new DesignFileException(path, exceptionList);
		}

		List<Library> libs = module.getLibrariesByLocation(path, IAccessControl.ARBITARY_LEVEL);

		Map<String, Library> reloadLibs = new HashMap();
		for (int i = 0; i < libs.size(); i++) {
			LibraryCommand command = new LibraryCommand(module);
			Library lib = libs.get(i);
			command.reloadLibrary(lib, null, reloadLibs);
		}

		checkReportIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.ModuleHandle#dropLibraryAndBreakExtends
	 * (org.eclipse.birt.report.model.api.LibraryHandle)
	 */

	public void dropLibraryAndBreakExtends(LibraryHandle library) throws SemanticException {
		if (library == null)
			return;

		LibraryCommand command = new LibraryCommand(module);
		command.dropLibraryAndBreakExtends((Library) library.getElement());

		checkReportIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#openCssStyleSheet(java
	 * .lang.String)
	 */
	public CssStyleSheetHandle openCssStyleSheet(String fileName) throws StyleSheetException {
		CssStyleSheet sheet = module.loadCss(fileName);
		return sheet.handle(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#openCssStyleSheet(java
	 * .io.InputStream)
	 */
	public CssStyleSheetHandle openCssStyleSheet(InputStream is) throws StyleSheetException {
		StyleSheetLoader loader = new StyleSheetLoader();
		return loader.load(module, is).handle(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#setThemeName(java.lang
	 * .String)
	 */
	public void setThemeName(String themeName) throws SemanticException {
		ThemeCommand command = new ThemeCommand(module, module);
		command.setTheme(themeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#setTheme(org.eclipse.birt
	 * .report.model.api.ThemeHandle)
	 */

	public void setTheme(ThemeHandle theme) throws SemanticException {
		ThemeCommand command = new ThemeCommand(module, module);
		command.setThemeElement(theme);
	}

	/**
	 * Returns all template parameter definition handles that this modules and the
	 * included modules contain.
	 * 
	 * @return all template parameter definition handles that this modules and the
	 *         included modules contain.
	 */

	List getAllTemplateParameterDefinitions() {
		List elementList = module.getNameHelper().getElements(Module.TEMPLATE_PARAMETER_NAME_SPACE,
				IAccessControl.NATIVE_LEVEL);

		return generateHandleList(elementList);
	}

}
