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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.core.AttributeEvent;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IAttributeListener;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.strategy.DummyCopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.LineNumberInfo;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.URIUtilImpl;

import com.ibm.icu.util.ULocale;

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

public abstract class ModuleHandleImpl extends DesignElementHandle implements IModuleModel {

	/**
	 * The flag indicates that whether the initialization is finished.
	 */

	protected boolean isInitialized = false;

	/**
	 * Constructs one module handle with the given module element.
	 * 
	 * @param module module
	 */

	public ModuleHandleImpl(Module module) {
		super(module);

		initializeSlotHandles();
		cachePropertyHandles();
	}

	/**
	 * Adds a new config variable.
	 * 
	 * @param configVar the config variable
	 * @throws SemanticException if the name is empty or the same name exists.
	 * 
	 */

	public void addConfigVariable(ConfigVariable configVar) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Adds a new embedded image.
	 * 
	 * @param image the image to add
	 * @throws SemanticException if the name is empty, type is invalid, or the same
	 *                           name exists.
	 */

	public void addImage(EmbeddedImage image) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Checks the name of the embedded image in this report. If duplicate, get a
	 * unique name and rename it.
	 * 
	 * @param image the embedded image whose name is need to check
	 */

	public final void rename(EmbeddedImage image) {
		module.rename(image);
	}

	/**
	 * Adds a new translation to the design.
	 * 
	 * @param resourceKey resource key for the message
	 * @param locale      the string value of a locale for the translation. Locale
	 *                    should be in java-defined format( en, en-US, zh_CN, etc.)
	 * @param text        translated text for the locale
	 * 
	 * @throws CustomMsgException if the resource key is duplicate or missing, or
	 *                            locale is not a valid format.
	 * 
	 * @see #getTranslation(String, String)
	 */

	public void addTranslation(String resourceKey, String locale, String text) throws CustomMsgException {
		throw new IllegalOperationException();
	}

	/**
	 * Adds the validation listener, which implements
	 * <code>IValidationListener</code>. A listener receives notifications each time
	 * an element is validated.
	 * 
	 * @param listener the validation listener.
	 */

	public final void addValidationListener(IValidationListener listener) {
		module.addValidationListener(listener);
	}

	/**
	 * Checks this whole report. Only one <code>ValidationEvent</code> will be sent,
	 * which contains all error information of this check.
	 */

	public final void checkReport() {
		// validate the whole design

		module.semanticCheck(module);

		ValidationEvent event = new ValidationEvent(module, null, getErrorList());

		module.broadcastValidationEvent(module, event);
	}

	/**
	 * Closes the design. The report design handle is no longer valid after closing
	 * the design. This method will send notifications instance of
	 * <code>DisposeEvent</code> to all the dispose listeners registered in the
	 * module.
	 */

	public final void close() {
		module.close();
		DisposeEvent event = new DisposeEvent(module);
		module.broadcastDisposeEvent(event);
	}

	/**
	 * Returns the structures which are defined in report design and all included
	 * valid libraries. This method will filter the structure with duplicate name
	 * with the follow rule.
	 * 
	 * <ul>
	 * <li>The structure defined in design file overrides the one with the same name
	 * in library file.
	 * <li>The structure defined in preceding library overrides the one with the
	 * same name in following library file.
	 * <ul>
	 * 
	 * @param propName   name of the list property
	 * @param nameMember name of the name member
	 * @return the filtered structure list with the above rule.
	 */

	final List getFilteredStructureList(String propName, String nameMember) {
		List list = new ArrayList();

		PropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null)
			return Collections.emptyList();

		Set names = new HashSet();
		Iterator iter = propHandle.iterator();
		while (iter.hasNext()) {
			StructureHandle s = (StructureHandle) iter.next();
			String nameValue = (String) s.getProperty(nameMember);
			if (!names.contains(nameValue)) {
				list.add(s);
				names.add(nameValue);
			}
		}

		List theLibraries = getLibraries();
		int size = theLibraries.size();
		for (int i = 0; i < size; i++) {
			LibraryHandle library = (LibraryHandle) theLibraries.get(i);
			if (library.isValid()) {
				iter = library.getFilteredStructureList(propName, nameMember).iterator();
				while (iter.hasNext()) {
					StructureHandle s = (StructureHandle) iter.next();
					String nameValue = (String) s.getProperty(nameMember);
					if (!names.contains(nameValue)) {
						list.add(s);
						names.add(nameValue);
					}
				}
			}
		}

		return list;
	}

	/**
	 * Returns the structures which are defined in the current module and all
	 * included valid libraries. This method will collect all structures from this
	 * module file and each valid library.
	 * 
	 * @param propName name of the list property
	 * @return the structure list, each of which is the instance of <code>
	 *         StructureHandle</code>
	 */

	final List getStructureList(String propName) {
		List list = new ArrayList();

		List tempList = getNativeStructureList(propName);
		if (!tempList.isEmpty())
			list.addAll(tempList);

		List theLibraries = getLibraries();
		int size = theLibraries.size();
		for (int i = 0; i < size; i++) {
			LibraryHandle library = (LibraryHandle) theLibraries.get(i);
			tempList = library.getStructureList(propName);
			if (!tempList.isEmpty())
				list.addAll(tempList);
		}

		return list;
	}

	/**
	 * Returns the structures which are defined locally in the current module. This
	 * method will collect all structures from the current module file locally.
	 * 
	 * @param propName name of the list property
	 * @return the structure list, each of which is the instance of <code>
	 *         StructureHandle</code>
	 */

	protected final List getNativeStructureList(String propName) {
		List list = new ArrayList();

		PropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null)
			return Collections.emptyList();

		Iterator iter = propHandle.iterator();
		while (iter.hasNext()) {
			StructureHandle s = (StructureHandle) iter.next();
			list.add(s);
		}

		return list;
	}

	/**
	 * Returns the iterator over all configuration variables. Each one is the
	 * instance of <code>ConfigVariableHandle</code>.
	 * <p>
	 * Note: The configure variable in library file will be hidden if the one with
	 * the same name appears in design file.
	 * 
	 * @return the iterator over all configuration variables.
	 * @see ConfigVariableHandle
	 */

	public final Iterator configVariablesIterator() {
		return getFilteredStructureList(CONFIG_VARS_PROP, ConfigVariable.NAME_MEMBER).iterator();
	}

	/**
	 * Returns the iterator over all structures of color palette. Each one is the
	 * instance of <code>CustomColorHandle</code>
	 * 
	 * @return the iterator over all structures of color palette.
	 * @see CustomColorHandle
	 */

	public final Iterator customColorsIterator() {
		return getStructureList(COLOR_PALETTE_PROP).iterator();
	}

	/**
	 * Drops a config variable.
	 * 
	 * @param name config variable name
	 * @throws SemanticException if no config variable is found.
	 * @deprecated
	 */

	public void dropConfigVariable(String name) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Drops an embedded image handle list from the design. Each one in the list is
	 * the instance of <code>EmbeddedImageHandle</code>.
	 * 
	 * @param images the image handle list to remove
	 * @throws SemanticException if any image in the list is not found.
	 */

	public void dropImage(List images) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Drops an embedded image from the design.
	 * 
	 * @param name the image name
	 * @throws SemanticException if the image is not found.
	 * @deprecated
	 */

	public void dropImage(String name) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Drops a translation from the design.
	 * 
	 * @param resourceKey resource key of the message in which this translation
	 *                    saves.
	 * @param locale      the string value of the locale for a translation. Locale
	 *                    should be in java-defined format( en, en-US, zh_CN, etc.)
	 * @throws CustomMsgException if <code>resourceKey</code> is <code>null</code>.
	 * @see #getTranslation(String, String)
	 */

	public void dropTranslation(String resourceKey, String locale) throws CustomMsgException {
		throw new IllegalOperationException();
	}

	/**
	 * Finds a data set by name in this module and the included modules.
	 * 
	 * @param name name of the data set
	 * @return a handle to the data set, or <code>null</code> if the data set is not
	 *         found
	 */

	public final DataSetHandle findDataSet(String name) {
		DesignElement element = module.findDataSet(name);
		if (!(element instanceof DataSet))
			return null;

		return (DataSetHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a joint data set by name in this module and the included modules.
	 * 
	 * @param name name of the joint data set
	 * @return a handle to the joint data set, or <code>null</code> if the data set
	 *         is not found
	 */

	public final JointDataSetHandle findJointDataSet(String name) {
		DesignElement element = module.findDataSet(name);
		if (!(element instanceof JointDataSet))
			return null;

		return (JointDataSetHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a template data set by name in this module and the included modules.
	 * 
	 * @param name name of the data set
	 * @return a handle to the template data set, or <code>null</code> if the data
	 *         set is not found
	 */

	public final TemplateDataSetHandle findTemplateDataSet(String name) {
		DesignElement element = module.findDataSet(name);
		if (element == null)
			return null;
		return (TemplateDataSetHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a data source by name in this module and the included modules.
	 * 
	 * @param name name of the data source
	 * @return a handle to the data source, or <code>null</code> if the data source
	 *         is not found
	 */

	public final DataSourceHandle findDataSource(String name) {
		DesignElement element = module.findDataSource(name);
		if (element == null)
			return null;
		return (DataSourceHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a named element in the name space in this module and the included
	 * moduled.
	 * 
	 * @param name the name of the element to find
	 * @return a handle to the element, or <code>null</code> if the element was not
	 *         found.
	 */

	public final DesignElementHandle findElement(String name) {
		DesignElement element = module.findElement(name);
		if (element == null)
			return null;
		return element.getHandle(element.getRoot());
	}

	/**
	 * Finds a cube element by name in this module and the included modules.
	 * 
	 * @param name the element name
	 * @return the cube element handle, if found, otherwise null
	 */

	public final CubeHandle findCube(String name) {
		DesignElement element = module.findOLAPElement(name);
		if (element == null)
			return null;
		return element.getDefn()
				.isKindOf(MetaDataDictionary.getInstance().getElement(ReportDesignConstants.CUBE_ELEMENT))
						? (CubeHandle) element.getHandle(element.getRoot())
						: null;
	}

	/**
	 * Finds a cube element by name in this module and the included modules.
	 * 
	 * @param name the element name, name must be Dimension name + "/" + level name.
	 * @return the cube element handle, if found, otherwise null
	 */

	public final LevelHandle findLevel(String name) {
		DesignElement element = module.findLevel(name);
		if (element == null)
			return null;
		return (LevelHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a dimension element by name in this module and the included modules.
	 *
	 * @param name name of the dimension to find
	 * @return the dimension handle if found, otherwise null
	 */
	public final DimensionHandle findDimension(String name) {
		Dimension element = module.findDimension(name);
		if (element == null)
			return null;
		return (DimensionHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds the image with the given name.
	 * 
	 * @param name the image name
	 * @return embedded image with the given name. Return <code>null</code>, if not
	 *         found.
	 */

	public final EmbeddedImage findImage(String name) {
		return module.findImage(name);
	}

	/**
	 * Finds the config variable with the given name.
	 * 
	 * @param name the variable name
	 * @return the config variable with the given name. Return <code>null</code> ,
	 *         if not found.
	 */

	public final ConfigVariable findConfigVariable(String name) {
		return module.findConfigVariabel(name);
	}

	/**
	 * Finds the custom color with the given name.
	 * 
	 * @param name the color name
	 * @return the custom color with the given name. Return <code>null</code> if
	 *         it's not found.
	 */

	public final CustomColor findColor(String name) {
		return module.findColor(name);
	}

	/**
	 * Finds a master page by name in this module and the included modules.
	 * 
	 * @param name the name of the master page
	 * @return a handle to the master page, or <code>null</code> if the page is not
	 *         found
	 */

	public final MasterPageHandle findMasterPage(String name) {
		DesignElement element = module.findPage(name);
		if (element == null)
			return null;
		return (MasterPageHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a parameter by name in this module and the included modules.
	 * 
	 * @param name the name of the parameter
	 * @return a handle to the parameter, or <code>null</code> if the parameter is
	 *         not found
	 */

	public final ParameterHandle findParameter(String name) {
		DesignElement element = module.findParameter(name);
		if (element == null || !(element instanceof Parameter))
			return null;
		return (ParameterHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Finds a style by its name in this module. The style with the same name, which
	 * is defined the included module, will never be returned.
	 * 
	 * @param name name of the style
	 * @return a handle to the style, or <code>null</code> if the style is not found
	 */

	public final SharedStyleHandle findNativeStyle(String name) {
		StyleElement style = module.findNativeStyle(name);
		if (style == null)
			return null;
		return (SharedStyleHandle) style.getHandle(module);
	}

	/**
	 * Finds a style by its name in this module and the included modules.
	 * 
	 * @param name name of the style
	 * @return a handle to the style, or <code>null</code> if the style is not found
	 */

	public final SharedStyleHandle findStyle(String name) {
		StyleElement style = module.findStyle(name);
		if (style == null)
			return null;
		return (SharedStyleHandle) style.getHandle(style.getRoot());
	}

	/**
	 * Finds a theme by its name in this module and the included modules.
	 * 
	 * @param name name of the theme
	 * @return a handle to the theme, or <code>null</code> if the theme is not found
	 */

	public final ThemeHandle findTheme(String name) {
		Theme theme = module.findTheme(name);
		if (theme == null)
			return null;
		return (ThemeHandle) theme.getHandle(theme.getRoot());
	}

	/**
	 * Finds a report item theme by its name in this module and its included
	 * libraries.
	 * 
	 * @param name name of the report item theme
	 * @return a handle to the report item theme, or null if not found
	 */
	public final ReportItemThemeHandle findReportItemTheme(String name) {
		ReportItemTheme theme = module.findReportItemTheme(name);
		if (theme == null)
			return null;
		return (ReportItemThemeHandle) theme.getHandle(theme.getRoot());
	}

	/**
	 * Returns the name of the author of the design report.
	 * 
	 * @return the name of the author.
	 */

	public final String getAuthor() {
		return getStringProperty(AUTHOR_PROP);
	}

	/**
	 * Gets the subject of the module.
	 * 
	 * @return the subject of the module.
	 */
	public final String getSubject() {
		return getStringProperty(SUBJECT_PROP);
	}

	/**
	 * Sets the subject of the module.
	 * 
	 * @param subject the subject of the module.
	 * @throws SemanticException
	 */
	public final void setSubject(String subject) throws SemanticException {
		setStringProperty(SUBJECT_PROP, subject);
	}

	/**
	 * Gets comments property value.
	 * 
	 * @return the comments property value.
	 */
	public final String getComments() {
		return getStringProperty(COMMENTS_PROP);
	}

	/**
	 * Sets the comments value.
	 * 
	 * @param comments the comments.
	 * @throws SemanticException
	 */
	public final void setComments(String comments) throws SemanticException {
		setStringProperty(COMMENTS_PROP, comments);
	}

	/**
	 * Returns the command stack that manages undo/redo operations for the design.
	 * 
	 * @return a command stack
	 * 
	 * @see CommandStack
	 */

	public final CommandStack getCommandStack() {
		return module.getActivityStack();
	}

	/**
	 * Returns a slot handle to work with the top-level components within the
	 * report.
	 * 
	 * @return A handle for working with the components.
	 */

	public SlotHandle getComponents() {
		return null;
	}

	/**
	 * Returns the name of the tool that created the design.
	 * 
	 * @return the name of the tool
	 */

	public final String getCreatedBy() {
		return getStringProperty(CREATED_BY_PROP);
	}

	/**
	 * Returns a slot handle to work with the data sets within the report. Note that
	 * the order of the data sets within the slot is unimportant.
	 * 
	 * @return A handle for working with the data sets.
	 */

	public SlotHandle getDataSets() {
		return null;
	}

	/**
	 * Gets the slot handle to work with all cube elements within the report.
	 * 
	 * @return cube slot handle
	 */
	public abstract SlotHandle getCubes();

	/**
	 * Returns a slot handle to work with the data sources within the report. Note
	 * that the order of the data sources within the slot is unimportant.
	 * 
	 * @return A handle for working with the data sources.
	 */

	public SlotHandle getDataSources() {
		return null;
	}

	/**
	 * Returns the default units for the design. These are the units that are used
	 * for dimensions that don't explicitly specify units.
	 * 
	 * @return the default units for the design.
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 */

	public final String getDefaultUnits() {
		return module.getUnits();
	}

	/**
	 * Sets the default units for the design. These are the units that are used for
	 * dimensions that don't explicitly specify units.
	 * <p>
	 * 
	 * For a report design, it allows the following constants that defined in <code>
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}
	 * </code>:
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>
	 * UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * </ul>
	 * 
	 * @param units the default units for the design.
	 * @throws SemanticException if the input unit is not one of allowed.
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 */

	public final void setDefaultUnits(String units) throws SemanticException {
		setStringProperty(UNITS_PROP, units);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getElement()
	 */
	public final DesignElement getElement() {
		return module;
	}

	/**
	 * Finds the handle to an element by a given element ID. Returns <code>null
	 * </code> if the ID is not valid, or if this session does not use IDs.
	 * 
	 * @param id ID of the element to find
	 * @return A handle to the element, or <code>null</code> if the element was not
	 *         found or this session does not use IDs.
	 */

	public final DesignElementHandle getElementByID(long id) {
		DesignElement element = module.getElementByID(id);
		if (element == null)
			return null;
		return element.getHandle(module);
	}

	/**
	 * Returns a list containing errors during parsing the design file.
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public final List getErrorList() {
		return module.getErrorList();
	}

	/**
	 * Returns the file name of the design. This is the name of the file from which
	 * the design was read, or the name to which the design was last written.
	 * 
	 * @return the file name
	 */

	public final String getFileName() {
		return module.getFileName();
	}

	/**
	 * Returns the flatten Parameters/ParameterGroups of the design. This method put
	 * all Parameters and ParameterGroups into a list then return it. The return
	 * list is sorted by on the display name of the parameters.
	 * 
	 * @return the sorted, flatten parameters and parameter groups.
	 */

	public List getFlattenParameters() {
		return Collections.emptyList();
	}

	/**
	 * Returns an external file that provides help information for the report.
	 * 
	 * @return the name of an external file
	 */

	public final String getHelpGuide() {
		return getStringProperty(HELP_GUIDE_PROP);
	}

	/**
	 * Returns the script called when the report starts executing.
	 * 
	 * @return the script called when the report starts executing
	 */

	public final String getInitialize() {
		return getStringProperty(INITIALIZE_METHOD);
	}

	/**
	 * Returns a slot handle to work with the master pages within the report. Note
	 * that the order of the master pages within the slot is unimportant.
	 * 
	 * @return A handle for working with the master pages.
	 */

	public SlotHandle getMasterPages() {
		return null;
	}

	/**
	 * Finds user-defined messages for the current thread's locale.
	 * 
	 * @param resourceKey Resource key of the user-defined message.
	 * @return the corresponding locale-dependent messages. Return <code>null
	 *         </code> if resoueceKey is blank.
	 * @see #getMessage(String, Locale)
	 */

	public final String getMessage(String resourceKey) {
		return getModule().getMessage(resourceKey);
	}

	/**
	 * Finds user-defined messages for the given locale.
	 * <p>
	 * First we look up in the report itself, then look into the referenced message
	 * file. Each search uses a reduced form of Java locale-driven search algorithm:
	 * Language&Country, language, default.
	 * 
	 * @param resourceKey Resource key of the user defined message.
	 * @param locale      locale of message, if the input <code>locale</code> is
	 *                    <code>
	 *            null</code> , the locale for the current thread will be used
	 *                    instead.
	 * @return the corresponding locale-dependent messages. Return <code>null
	 *         </code> if resoueceKey is blank.
	 */

	public final String getMessage(String resourceKey, Locale locale) {
		return getModule().getMessage(resourceKey, ULocale.forLocale(locale));
	}

	/**
	 * Finds user-defined messages for the given locale.
	 * <p>
	 * First we look up in the report itself, then look into the referenced message
	 * file. Each search uses a reduced form of Java locale-driven search algorithm:
	 * Language&Country, language, default.
	 * 
	 * @param resourceKey Resource key of the user defined message.
	 * @param locale      locale of message, if the input <code>locale</code> is
	 *                    <code>
	 *            null</code> , the locale for the current thread will be used
	 *                    instead.
	 * @return the corresponding locale-dependent messages. Return <code>null
	 *         </code> if resoueceKey is blank.
	 */

	public final String getMessage(String resourceKey, ULocale locale) {
		return getModule().getMessage(resourceKey, locale);
	}

	/**
	 * Return a list of user-defined message keys. The list contained resource keys
	 * defined in the report itself and the keys defined in the referenced message
	 * files for the current thread's locale. The list returned contains no
	 * duplicate keys.
	 * 
	 * @return a list of user-defined message keys.
	 */

	public final List getMessageKeys() {
		return getModule().getMessageKeys();
	}

	/**
	 * Returns a slot handle to work with the top-level parameters and parameter
	 * groups within the report. The order that the items appear within the slot
	 * determines the order in which they appear in the "requester" UI.
	 * 
	 * @return A handle for working with the parameters and parameter groups.
	 */

	public SlotHandle getParameters() {
		return null;
	}

	/**
	 * Returns a cascading parameter group handle with the given group name
	 * 
	 * @param groupName name of the cascading parameter group.
	 * @return a handle to the cascading parameter group. Returns <code>null
	 *         </code> if the cascading group with the given name is not found.
	 */

	public final CascadingParameterGroupHandle findCascadingParameterGroup(String groupName) {
		DesignElement element = module.findParameter(groupName);
		if (element == null || !(element instanceof CascadingParameterGroup))
			return null;
		return (CascadingParameterGroupHandle) element.getHandle(element.getRoot());

	}

	/**
	 * Returns a slot handle to work with the styles within the report. Note that
	 * the order of the styles within the slot is unimportant.
	 * 
	 * @return A handle for working with the styles.
	 */

	public SlotHandle getStyles() {
		return null;
	}

	/**
	 * Gets a handle to deal with a translation. A translation is identified by its
	 * resourceKey and locale.
	 * 
	 * @param resourceKey the resource key
	 * @param locale      the locale information
	 * 
	 * @return corresponding <code>TranslationHandle</code>. Or return <code>
	 *         null</code> if the translation is not found in the design.
	 * 
	 * @see TranslationHandle
	 */

	public final TranslationHandle getTranslation(String resourceKey, String locale) {
		Translation translation = module.findTranslation(resourceKey, locale);

		if (translation != null)
			return translation.handle(getModule());

		return null;
	}

	/**
	 * Returns a string array containing all the resource keys of user-defined
	 * translations for the report.
	 * 
	 * @return a string array containing message resource keys, return <code>
	 *         null</code> if there is no messages defined in the design.
	 */

	public final String[] getTranslationKeys() {
		return module.getTranslationResourceKeys();
	}

	/**
	 * Gets a list of translation defined on the report. The content of the list is
	 * the corresponding <code>TranslationHandle</code>.
	 * 
	 * @return a list containing TranslationHandles defined on the report or
	 *         <code>null</code> if the design has no any translations.
	 * 
	 * @see TranslationHandle
	 */

	public final List getTranslations() {
		List translations = module.getTranslations();

		if (translations == null)
			return null;

		List translationHandles = new ArrayList();

		for (int i = 0; i < translations.size(); i++) {
			translationHandles.add(((Translation) translations.get(i)).handle(getModule()));
		}

		return translationHandles;
	}

	/**
	 * Returns a list containing warnings during parsing the design file.
	 * 
	 * @return a list containing parsing warnings. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public final List getWarningList() {
		return module.getWarningList();
	}

	/**
	 * Returns the iterator over all embedded images of this module instance. Each
	 * one is the instance of <code>EmbeddedImageHandle</code>
	 * 
	 * @return the iterator over all embedded images.
	 * 
	 * @see EmbeddedImageHandle
	 */

	public Iterator imagesIterator() {
		return Collections.emptyList().iterator();
	}

	/**
	 * Returns the list of embedded images, including the one from libraries. Each
	 * one is the instance of <code>EmbeddedImageHandle</code>
	 * 
	 * @return the list of embedded images.
	 * 
	 * @see EmbeddedImageHandle
	 */

	public final List getAllImages() {
		return getStructureList(IMAGES_PROP);
	}

	/**
	 * Determines if the design has changed since it was last read from, or written
	 * to, the file. The dirty state reflects the action of the command stack. If
	 * the user saves the design and then changes it, the design is dirty. If the
	 * user then undoes the change, the design is no longer dirty.
	 * 
	 * @return <code>true</code> if the design has changed since the last load or
	 *         save; <code>false</code> if it has not changed.
	 */

	public final boolean needsSave() {
		String version = module.getVersionManager().getVersion();
		if (version != null) {
			boolean isSupportedUnknownVersion = false;
			if (module.getOptions() != null) {
				isSupportedUnknownVersion = module.getOptions().isSupportedUnknownVersion();
			}
			List versionInfos = ModelUtil.checkVersion(version, isSupportedUnknownVersion);
			if (!versionInfos.isEmpty())
				return true;
		}
		return module.isDirty();
	}

	/**
	 * Calls to inform a save is successful. Must be called after a successful
	 * completion of a save done using <code>serialize</code>.
	 */

	public final void onSave() {
		module.onSave();
	}

	/**
	 * Removes a given validation listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener the listener to de-register
	 * @return <code>true</code> if <code>listener</code> is sucessfully removed.
	 *         Otherwise <code>false</code>.
	 */

	public final boolean removeValidationListener(IValidationListener listener) {
		return getModule().removeValidationListener(listener);
	}

	/**
	 * Checks the element name in name space of this report.
	 * 
	 * <ul>
	 * <li>If the element name is required and duplicate name is found in name
	 * space, rename the element with a new unique name.
	 * <li>If the element name is not required, clear the name.
	 * </ul>
	 * 
	 * @param elementHandle the element handle whose name is need to check.
	 */

	public final void rename(DesignElementHandle elementHandle) {
		rename((DesignElementHandle) null, elementHandle);
	}

	/**
	 * Checks element name is unique in container.
	 * 
	 * @param containerHandle container of element
	 * @param elementHandle   element handle
	 */

	public void rename(DesignElementHandle containerHandle, DesignElementHandle elementHandle) {
		if (elementHandle == null)
			return;

		if (containerHandle == null) {
			module.rename(elementHandle.getElement());
			return;
		}

		// Specially for case rename copied style in theme.

		if (elementHandle instanceof StyleHandle) {
			if (containerHandle instanceof ThemeHandle) {
				String name = ((ThemeHandle) containerHandle).makeUniqueStyleName(elementHandle.getName());
				elementHandle.getElement().setName(name);
			} else if (containerHandle instanceof ReportDesignHandle)
				module.rename(elementHandle.getElement());
			return;
		}

		// If both have root, let name helper decide unique name
		// Now only for rename level name in hierarchy.

		module.rename(containerHandle.getElement(), elementHandle.getElement());

	}

	/**
	 * Replaces the old config variable with the new one.
	 * 
	 * @param oldVar the old config variable
	 * @param newVar the new config variable
	 * @throws SemanticException if the old config variable is not found or the name
	 *                           of new one is empty.
	 * 
	 */

	public void replaceConfigVariable(ConfigVariable oldVar, ConfigVariable newVar) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Replaces the old embedded image with the new one.
	 * 
	 * @param oldVar the old embedded image
	 * @param newVar the new embedded image
	 * @throws SemanticException if the old image is not found or the name of new
	 *                           one is empty.
	 */

	public void replaceImage(EmbeddedImage oldVar, EmbeddedImage newVar) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Saves the module to an existing file name. Call this only when the file name
	 * has been set.
	 * 
	 * @throws IOException if the file cannot be saved on the storage
	 * 
	 * @see #saveAs(String)
	 */

	public final void save() throws IOException {
		String fileName = getFileName();
		if (fileName == null)
			return;
		module.prepareToSave();
		String resolvedFileName = URIUtil.getLocalPath(fileName);
		if (resolvedFileName != null)
			fileName = resolvedFileName;
		module.getWriter().write(new File(fileName));
		module.onSave();
	}

	/**
	 * Saves the design to the file name provided. The file name is saved in the
	 * design, and subsequent calls to <code>save( )</code> will save to this new
	 * name.
	 * 
	 * @param newName the new file name
	 * @throws IOException if the file cannot be saved
	 * 
	 * @see #save()
	 */

	public final void saveAs(String newName) throws IOException {
		setFileName(newName);
		save();
	}

	/**
	 * Writes the report design to the given output stream. The caller must call
	 * <code>onSave</code> if the save succeeds.
	 * 
	 * @param out the output stream to which the design is written.
	 * @throws IOException if the file cannot be written to the output stream
	 *                     successfully.
	 */

	public final void serialize(OutputStream out) throws IOException {
		assert out != null;

		module.prepareToSave();
		module.getWriter().write(out);
		module.onSave();
	}

	/**
	 * Sets the name of the author of the design report.
	 * 
	 * @param author the name of the author.
	 */

	public final void setAuthor(String author) {
		try {
			setStringProperty(AUTHOR_PROP, author);
		} catch (SemanticException e) {
			throw new IllegalOperationException();
		}
	}

	/**
	 * Returns the name of the tool that created the design.
	 * 
	 * @param toolName the name of the tool
	 */

	public final void setCreatedBy(String toolName) {
		try {
			setStringProperty(CREATED_BY_PROP, toolName);
		} catch (SemanticException e) {
			throw new IllegalOperationException();
		}
	}

	/**
	 * Sets the design file name. This method will send notifications instance of
	 * <code>AttributeEvent</code> to all the attribute listeners registered in the
	 * module.
	 * 
	 * @param newName the new file name. It may contain the relative/absolute path
	 *                information. This name must include the file name with the
	 *                filename extension.
	 */

	public final void setFileName(String newName) {
		module.setFileName(newName);

		if (!StringUtil.isBlank(newName)) {
			URL systemId = URIUtilImpl.getDirectory(newName);
			if (systemId != null)
				module.setSystemId(systemId);

			URL location = URIUtilImpl.getURLPresentation(newName);
			module.setLocation(location);
		}

		AttributeEvent event = new AttributeEvent(module, AttributeEvent.FILE_NAME_ATTRIBUTE);
		module.broadcastFileNameEvent(event);
	}

	/**
	 * Sets an external file that provides help information for the report.
	 * 
	 * @param helpGuide the name of an external file
	 */

	public final void setHelpGuide(String helpGuide) {
		try {
			setStringProperty(HELP_GUIDE_PROP, helpGuide);
		} catch (SemanticException e) {
			throw new IllegalOperationException();
		}
	}

	/**
	 * Sets the script called when the report starts executing.
	 * 
	 * @param value the script to set.
	 */

	public final void setInitialize(String value) {
		try {
			setStringProperty(INITIALIZE_METHOD, value);
		} catch (SemanticException e) {
			throw new IllegalOperationException();
		}
	}

	/**
	 * Returns all style element handles that this modules and the included modules
	 * contain.
	 * 
	 * @return all style element handles that this modules and the included modules
	 *         contain.
	 */

	public List getAllStyles() {
		return Collections.emptyList();
	}

	/**
	 * Returns theme handles according the input level.
	 * 
	 * @param level an <code>int</code> value, which should be the one defined in
	 *              <code>IVisibleLevelControl</code>.
	 * 
	 * @return theme handles according the input level
	 */

	public List getVisibleThemes(int level) {
		return Collections.emptyList();
	}

	/**
	 * Returns report item theme handles according the input level.
	 * 
	 * @param level an <code>int</code> value, which should be the one defined in
	 *              <code>IVisibleLevelControl</code>.
	 * 
	 * @return theme handles according the input level
	 */
	public List<ReportItemThemeHandle> getVisibleReportItemThemes(int level, String type) {
		return Collections.emptyList();
	}

	/**
	 * Returns parameters and parameter groups on the module. Those parameters
	 * included in the parameter groups are not included in the return list.
	 * 
	 * @return parameters and parameter groups
	 */

	public List getParametersAndParameterGroups() {
		return Collections.emptyList();
	}

	/**
	 * Returns all data source handles that this modules and the included modules
	 * contain.
	 * 
	 * @return all data source handles that this modules and the included modules
	 *         contain.
	 */

	public final List getAllDataSources() {
		List elementList = module.getNameHelper().getElements(Module.DATA_SOURCE_NAME_SPACE,
				IAccessControl.ARBITARY_LEVEL);
		return generateHandleList(elementList);
	}

	/**
	 * Returns data source handles that are visible to this modules.
	 * 
	 * @return data source handles that are visible to this modules.
	 */

	public final List getVisibleDataSources() {
		List elementList = module.getNameHelper().getElements(Module.DATA_SOURCE_NAME_SPACE,
				IAccessControl.NATIVE_LEVEL);
		return generateHandleList(sortVisibleElements(elementList, IAccessControl.NATIVE_LEVEL));
	}

	/**
	 * Returns all data set handles that this modules and the included modules
	 * contain.
	 * 
	 * @return all data set handles that this modules and the included modules
	 *         contain.
	 */

	public final List getAllDataSets() {
		List elementList = module.getNameHelper().getElements(Module.DATA_SET_NAME_SPACE,
				IAccessControl.ARBITARY_LEVEL);
		return generateHandleList(elementList);
	}

	/**
	 * Returns data set handles that are visible to this modules.
	 * 
	 * @return data set handles that are visible to this modules.
	 */

	public final List getVisibleDataSets() {
		List elementList = module.getNameHelper().getElements(Module.DATA_SET_NAME_SPACE, IAccessControl.NATIVE_LEVEL);
		return generateHandleList(sortVisibleElements(elementList, IAccessControl.NATIVE_LEVEL));
	}

	/**
	 * Returns all cube handles that this modules and the included modules contain.
	 * 
	 * @return all cube handles that this modules and the included modules contain.
	 */

	public final List getAllCubes() {
		List elementList = module.getNameHelper().getElements(Module.CUBE_NAME_SPACE, IAccessControl.ARBITARY_LEVEL);
		List cubeList = getCubeList(elementList);
		return generateHandleList(cubeList);
	}

	/**
	 * Gets all the cube elements from the given element list.
	 * 
	 * @param elements
	 * @return all cube elements
	 */
	private List getCubeList(List elements) {
		if (elements == null)
			return null;
		List cubes = new ArrayList();
		for (int i = 0; i < elements.size(); i++) {
			DesignElement element = (DesignElement) elements.get(i);
			if (element.getDefn()
					.isKindOf(MetaDataDictionary.getInstance().getElement(ReportDesignConstants.CUBE_ELEMENT)))
				cubes.add(element);
		}
		return cubes;
	}

	/**
	 * Returns cube handles that are visible to this modules.
	 * 
	 * @return cube handles that are visible to this modules.
	 */

	public final List getVisibleCubes() {
		List elementList = module.getNameHelper().getElements(Module.CUBE_NAME_SPACE, IAccessControl.NATIVE_LEVEL);
		List cubeList = getCubeList(elementList);
		return generateHandleList(sortVisibleElements(cubeList, IAccessControl.NATIVE_LEVEL));
	}

	/**
	 * Returns the embedded images which are defined on the module itself. The
	 * embedded images defined in the included libraries will not be returned by
	 * this method.
	 * 
	 * @return the local embedded image list.
	 */
	public final List getVisibleImages() {
		List images = getNativeStructureList(IModuleModel.IMAGES_PROP);
		return images;
	}

	/**
	 * Returns all page handles that this modules and the included modules contain.
	 * 
	 * @return all page handles that this modules and the included modules contain.
	 */

	public List getAllPages() {
		return Collections.emptyList();
	}

	/**
	 * Returns all parameter handles that this modules.
	 * 
	 * @return all parameter handles that this modules.
	 */

	public final List getAllParameters() {
		List elementList = module.getNameHelper().getNameSpace(Module.PARAMETER_NAME_SPACE).getElements();

		return generateHandleList(elementList);
	}

	/**
	 * Returns the libraries this report design includes directly or indirectly.
	 * Each in the returned list is the instance of <code>LibraryHandle</code>.
	 * 
	 * @return the libraries this report design includes directly or indirectly.
	 */

	public final List getAllLibraries() {
		return getLibraries(IAccessControl.ARBITARY_LEVEL);
	}

	/**
	 * Returns included libaries this report design includes directly or indirectly
	 * within the given depth.
	 * 
	 * @param level the given depth
	 * @return list of libraries.
	 */

	protected final List getLibraries(int level) {
		List libraries = module.getLibraries(level);
		List retLibs = new ArrayList();

		Iterator iter = libraries.iterator();
		while (iter.hasNext()) {
			Library library = (Library) iter.next();
			retLibs.add(library.handle());
		}
		return Collections.unmodifiableList(retLibs);
	}

	/**
	 * Returns the libraries this report design includes directly. Each in the
	 * returned list is the instance of <code>LibraryHandle</code>.
	 * 
	 * @return the libraries this report design includes directly.
	 */

	public final List getLibraries() {
		return getLibraries(IAccessControl.DIRECTLY_INCLUDED_LEVEL);
	}

	/**
	 * Returns the library handle with the given namespace.
	 * 
	 * @param namespace the library namespace
	 * @return the library handle with the given namespace
	 */

	public final LibraryHandle getLibrary(String namespace) {
		Module library = module.getLibraryWithNamespace(namespace, IAccessControl.DIRECTLY_INCLUDED_LEVEL);
		if (library == null)
			return null;

		return (LibraryHandle) library.getHandle(library);
	}

	/**
	 * Returns the library handle with the given file name. The filename can include
	 * directory information, either relative or absolute directory. And the file
	 * should be on the local disk.
	 * 
	 * @param fileName the library file name. The filename can include directory
	 *                 information, either relative or absolute directory. And the
	 *                 file should be on the local disk.
	 * @return the library handle with the given file name
	 */

	public final LibraryHandle findLibrary(String fileName) {
		URL url = module.findResource(fileName, IResourceLocator.LIBRARY);
		if (url == null)
			return null;

		Module library = module.getLibraryByLocation(url.toString());
		if (library == null)
			return null;

		return (LibraryHandle) library.getHandle(library);
	}

	/**
	 * Shifts the library to new position. This method might affect the style
	 * reference, because the library order is changed.
	 * 
	 * @param library the library to shift
	 * @param toPosn  the new position
	 * @throws SemanticException if error is encountered when shifting
	 */

	public void shiftLibrary(LibraryHandle library, int toPosn) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Returns whether this module is read-only.
	 * 
	 * @return true, if this module is read-only. Otherwise, return false.
	 */

	public final boolean isReadOnly() {
		return module.isReadOnly();
	}

	/**
	 * Returns the iterator over all included libraries. Each one is the instance of
	 * <code>IncludeLibraryHandle</code>
	 * 
	 * @return the iterator over all included libraries.
	 * @see IncludedLibraryHandle
	 */

	public Iterator includeLibrariesIterator() {
		return Collections.emptyList().iterator();
	}

	/**
	 * Includes one library with the given library file name. The new library will
	 * be appended to the library list.
	 * 
	 * @param libraryFileName library file name
	 * @param namespace       library namespace
	 * @throws DesignFileException if the library file is not found, or has fatal
	 *                             error.
	 * @throws SemanticException   if error is encountered when handling
	 *                             <code>IncludeLibrary
	 *             </code>      structure list.
	 */

	public void includeLibrary(String libraryFileName, String namespace) throws DesignFileException, SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Drops the given library from the included libraries of this design file.
	 * 
	 * @param library the library to drop
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>IncludeLibrary
	 *             </code>    structure list. Or it maybe because that the given
	 *                           library is not found in the design. Or that the
	 *                           library has descedents in the current module
	 */

	public void dropLibrary(LibraryHandle library) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Reloads the library with the given library file path. If the library already
	 * is included directly, reload it. If the library is not included, exception
	 * will be thrown.
	 * <p>
	 * Call this method cautiously ONLY on the condition that the library file is
	 * REALLY changed outside. After reload successfully, the command stack is
	 * cleared.
	 * 
	 * @param libraryToReload the library instance
	 * @throws SemanticException   if error is encountered when handling
	 *                             <code>IncludeLibrary
	 *             </code>      structure list. Or it maybe because that the
	 *                             given library is not found in the design. Or that
	 *                             the library has descedents in the current module
	 * @throws DesignFileException if the library file is not found, or has fatal
	 *                             error.
	 */

	public void reloadLibrary(LibraryHandle libraryToReload) throws SemanticException, DesignFileException {
		throw new IllegalOperationException();
	}

	/**
	 * Reloads all libraries this module included.
	 * <p>
	 * Call this method cautiously ONLY on the condition that the library file is
	 * REALLY changed outside. After reload successfully, the command stack is
	 * cleared.
	 * 
	 * {@link #reloadLibrary(LibraryHandle)}
	 * 
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	public void reloadLibraries() throws SemanticException, DesignFileException {
		throw new IllegalOperationException();
	}

	/**
	 * Reloads the library with the given library file path. If the library already
	 * is included directly or indirectly(that is, the reload path could be the path
	 * of grandson of this module), reload it. If the library is not included,
	 * exception will be thrown.
	 * <p>
	 * Call this method cautiously ONLY on the condition that the library file is
	 * REALLY changed outside. After reload successfully, the command stack is
	 * cleared.
	 * 
	 * @param reloadPath this is supposed to be an absolute path, not in url form.
	 * @throws SemanticException   if error is encountered when handling
	 *                             <code>IncludeLibrary
	 *             </code>      structure list. Or it maybe because that the
	 *                             given library is not found in the design. Or that
	 *                             the library has descedents in the current module
	 * @throws DesignFileException if the library file is not found, or has fatal
	 *                             error.
	 */

	public void reloadLibrary(String reloadPath) throws SemanticException, DesignFileException {
		throw new IllegalOperationException();
	}

	/**
	 * Drops the given library from the design and break all the parent/child
	 * relationships. All child element will be localized in the module.
	 * 
	 * @param library the given library to drop
	 * @throws SemanticException if errors occured when drop the library.It may be
	 *                           because that the library is not found in the design
	 *                           or that some elements can not be localized
	 *                           properly.
	 */

	public void dropLibraryAndBreakExtends(LibraryHandle library) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Adds one attribute listener. The duplicate listener will not be added.
	 * 
	 * @param listener the attribute listener to add
	 */

	public final void addAttributeListener(IAttributeListener listener) {
		module.addAttributeListener(listener);
	}

	/**
	 * Removes one attribute listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener the attribute listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully removed.
	 *         Otherwise <code>false</code>.
	 * 
	 */

	public final boolean removeAttributeListener(IAttributeListener listener) {
		return module.removeAttributeListener(listener);
	}

	/**
	 * Adds one dispose listener. The duplicate listener will not be added.
	 * 
	 * @param listener the dispose listener to add
	 */

	public final void addDisposeListener(IDisposeListener listener) {
		module.addDisposeListener(listener);
	}

	/**
	 * Adds one resource change listener. The duplicate listener will not be added.
	 * 
	 * @param listener the resource change listener to add
	 */

	public final void addResourceChangeListener(IResourceChangeListener listener) {
		module.addResourceChangeListener(listener);
	}

	/**
	 * Removes one dispose listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener the dispose listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully removed.
	 *         Otherwise <code>false</code>.
	 * 
	 */

	public final boolean removeDisposeListener(IDisposeListener listener) {
		return module.removeDisposeListener(listener);
	}

	/**
	 * Removes one resource change listener. If the listener not registered, then
	 * the request is silently ignored.
	 * 
	 * @param listener the resource change listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully removed.
	 *         Otherwise <code>false</code>.
	 * 
	 */

	public final boolean removeResourceChangeListener(IResourceChangeListener listener) {
		return module.removeResourceChangeListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#drop()
	 */

	public final void drop() throws SemanticException {
		throw new IllegalOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#dropAndClear()
	 */

	public final void dropAndClear() throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Get the base name of the customer-defined resource bundle.
	 * 
	 * @return the base name of the customer-defined resource bundle.
	 */

	public final String getIncludeResource() {
		return getStringProperty(INCLUDE_RESOURCE_PROP);
	}

	/**
	 * 
	 * @return
	 */
	public final List<String> getIncludeResources() {
		return getListProperty(INCLUDE_RESOURCE_PROP);
	}

	/**
	 * Set the base name of the customer-defined resource bundle. The name is a
	 * common base name, e.g: "myMessage" without the Language_Country suffix, then
	 * the message file family can be "myMessage_en.properties",
	 * "myMessage_zh_CN.properties" etc. The message file is stored in the same
	 * folder as the design file.
	 * 
	 * @param baseName common base name of the customer-defined resource bundle.
	 */

	public final void setIncludeResource(String baseName) {
		try {
			setProperty(INCLUDE_RESOURCE_PROP, baseName);
		} catch (SemanticException e) {
			throw new IllegalOperationException();
		}
	}

	/**
	 * Set the list of base name of the customer-defined resource bundles. The name
	 * is a common base name, e.g: "myMessage" without the Language_Country suffix,
	 * then the message file family can be "myMessage_en.properties",
	 * "myMessage_zh_CN.properties" etc. The message file is stored in the same
	 * folder as the design file.
	 * 
	 * @param baseNameList list of the base name
	 */
	public final void setIncludeResources(List<String> baseNameList) {
		try {
			setProperty(INCLUDE_RESOURCE_PROP, baseNameList);
		} catch (SemanticException e) {
			throw new IllegalOperationException();
		}
	}

	/**
	 * Returns the <code>URL</code> object if the file with <code>fileName
	 * </code> exists. This method takes the following search steps:
	 * 
	 * <ul>
	 * If file type is MESSAGEFILE ,
	 * <li>Search file with the file locator ( <code>IResourceLocator</code>) in
	 * session. And Now just deal with relative file name.
	 * 
	 * <ul>
	 * If file type is not MESSAGEFILE,
	 * <li>Search file taking <code>fileName
	 * </code> as absolute file name;
	 * <li>Search file taking <code>fileName
	 * </code> as relative file name and basing "base" property of report design;
	 * <li>Search file with the file locator (<code>IResourceLocator
	 * </code>) in session
	 * </ul>
	 * 
	 * @param fileName file name to search
	 * @param fileType file type. The value should be one of:
	 *                 <ul>
	 *                 <li><code>IResourceLocator.IMAGE</code>
	 *                 <li><code>
	 *            IResourceLocator.LIBRARY</code>
	 *                 <li><code>
	 *            IResourceLocator.MESSAGEFILE</code>
	 *                 </ul>
	 *                 Any invalid value will be treated as <code>
	 *            IResourceLocator.IMAGE</code>.
	 * @return the <code>URL</code> object if the file with <code>fileName
	 *         </code> is found, or null otherwise.
	 */

	public final URL findResource(String fileName, int fileType) {
		return module.findResource(fileName, fileType);
	}

	/**
	 * Returns the <code>URL</code> object if the file with <code>fileName
	 * </code> exists. This method takes the following search steps:
	 * 
	 * <ul>
	 * If file type is MESSAGEFILE ,
	 * <li>Search file with the file locator ( <code>IResourceLocator</code>) in
	 * session. And Now just deal with relative file name.
	 * 
	 * <ul>
	 * If file type is not MESSAGEFILE,
	 * <li>Search file taking <code>fileName
	 * </code> as absolute file name;
	 * <li>Search file taking <code>fileName
	 * </code> as relative file name and basing "base" property of report design;
	 * <li>Search file with the file locator (<code>IResourceLocator
	 * </code>) in session
	 * </ul>
	 * 
	 * @param fileName   file name to search
	 * @param fileType   file type. The value should be one of:
	 *                   <ul>
	 *                   <li><code>IResourceLocator.IMAGE</code>
	 *                   <li><code>
	 *            IResourceLocator.LIBRARY</code>
	 *                   <li><code>
	 *            IResourceLocator.MESSAGEFILE</code>
	 *                   </ul>
	 *                   Any invalid value will be treated as <code>
	 *            IResourceLocator.IMAGE</code>.
	 * @param appContext The map containing the user's information
	 * @return the <code>URL</code> object if the file with <code>fileName
	 *         </code> is found, or null otherwise.
	 */

	public final URL findResource(String fileName, int fileType, Map appContext) {
		return module.findResource(fileName, fileType, appContext);
	}

	/**
	 * Gets the result style sheet with given file name of an external CSS2
	 * resource.
	 * 
	 * @param fileName the file name of the external CSS resource
	 * @return the <code>CssStyleSheetHandle</code> if the external resource is
	 *         successfully loaded
	 * @throws StyleSheetException thrown if the resource is not found, or there are
	 *                             syntax errors in the resource
	 */

	public CssStyleSheetHandle openCssStyleSheet(String fileName) throws StyleSheetException {
		throw new IllegalOperationException();
	}

	/**
	 * Gets the result style sheet with given file name of an external CSS2
	 * resource.
	 * 
	 * @param is the input stream of the resource
	 * @return the <code>CssStyleSheetHandle</code> if the external resource is
	 *         successfully loaded
	 * @throws StyleSheetException thrown if the resource is not found, or there are
	 *                             syntax errors in the resource
	 */

	public CssStyleSheetHandle openCssStyleSheet(InputStream is) throws StyleSheetException {
		throw new IllegalOperationException();
	}

	/**
	 * Imports the selected styles in a <code>CssStyleSheetHandle</code> to the
	 * module. Each in the list is instance of <code>SharedStyleHandle</code> .If
	 * any style selected has a duplicate name with that of one style already
	 * existing in the report design, this method will rename it and then add it to
	 * the design.
	 * 
	 * @param stylesheet     the style sheet handle that contains all the selected
	 *                       styles
	 * @param selectedStyles the selected style list
	 * 
	 */

	public void importCssStyles(CssStyleSheetHandle stylesheet, List selectedStyles) {
		throw new IllegalOperationException();
	}

	/**
	 * Sets the theme to a report.
	 * 
	 * @param themeName the name of the theme
	 * @throws SemanticException
	 */

	public void setThemeName(String themeName) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Returns the refresh rate when viewing the report.
	 * 
	 * @return the refresh rate
	 */

	public final ThemeHandle getTheme() {
		Theme theme = module.getTheme(module);
		if (theme == null)
			return null;

		return (ThemeHandle) theme.getHandle(theme.getRoot());
	}

	/**
	 * Sets the theme to a report.
	 * 
	 * @param theme the theme instance
	 * @throws SemanticException
	 */

	public void setTheme(ThemeHandle theme) throws SemanticException {
		throw new IllegalOperationException();
	}

	/**
	 * Gets the location information of the module.
	 * 
	 * @return the location information of the module
	 */

	final String getLocation() {
		return module.getLocation();
	}

	/**
	 * Checks whether there is an included library in this module, which has the
	 * same absolute path as that of the given library.
	 * 
	 * @param library the library to check
	 * @return true if there is an included library in this module, which has the
	 *         same absolute path as that the given library, otherwise false
	 */

	public final boolean isInclude(LibraryHandle library) {
		return module.getLibraryByLocation(library.getLocation()) != null;
	}

	/**
	 * Finds a template parameter definition by its name in this module and the
	 * included modules.
	 * 
	 * @param name name of the template parameter definition
	 * @return a handle to the template parameter definition, or <code>null
	 *         </code> if the template parameter definition is not found
	 */

	final TemplateParameterDefinitionHandle findTemplateParameterDefinition(String name) {
		TemplateParameterDefinition templateParam = module.findTemplateParameterDefinition(name);
		if (templateParam == null)
			return null;
		return templateParam.handle(templateParam.getRoot());
	}

	/**
	 * Returns all template parameter definition handles that this modules and the
	 * included modules contain.
	 * 
	 * @return all template parameter definition handles that this modules and the
	 *         included modules contain.
	 */

	List getAllTemplateParameterDefinitions() {
		return Collections.emptyList();
	}

	/**
	 * Returns the static description for the module.
	 * 
	 * @return the static description to display
	 */

	public final String getDescription() {
		return getStringProperty(IModuleModel.DESCRIPTION_PROP);
	}

	/**
	 * Returns the localized description for the module. If the localized
	 * description for the description resource key is found, it will be returned.
	 * Otherwise, the static description will be returned.
	 * 
	 * @return the localized description for the module
	 */

	public final String getDisplayDescription() {
		return getExternalizedValue(IModuleModel.DESCRIPTION_ID_PROP, IModuleModel.DESCRIPTION_PROP);
	}

	/**
	 * Sets the description of the module. Sets the static description itself. If
	 * the module is to be externalized, then set the description ID separately.
	 * 
	 * @param description the new description for the module
	 * @throws SemanticException if the property is locked.
	 */

	public final void setDescription(String description) throws SemanticException {
		setStringProperty(IModuleModel.DESCRIPTION_PROP, description);
	}

	/**
	 * Returns the resource key of the static description of the module.
	 * 
	 * @return the resource key of the static description
	 */

	public final String getDescriptionKey() {
		return getStringProperty(IModuleModel.DESCRIPTION_ID_PROP);
	}

	/**
	 * Sets the resource key of the static description of the module.
	 * 
	 * @param resourceKey the resource key of the static description
	 * 
	 * @throws SemanticException if the resource key property is locked.
	 */

	public final void setDescriptionKey(String resourceKey) throws SemanticException {
		setStringProperty(IModuleModel.DESCRIPTION_ID_PROP, resourceKey);
	}

	/**
	 * Gets the title property value.
	 * 
	 * @return the title property value.
	 */
	public final String getTitle() {
		return getStringProperty(IModuleModel.TITLE_PROP);
	}

	/**
	 * Sets the title value.
	 * 
	 * @param title the title.
	 * @throws SemanticException
	 */
	public final void setTitle(String title) throws SemanticException {
		setStringProperty(IModuleModel.TITLE_PROP, title);
	}

	/**
	 * Gets the title key.
	 * 
	 * @return the title key.
	 */
	public final String getTitleKey() {
		return getStringProperty(IModuleModel.TITLE_ID_PROP);
	}

	/**
	 * Sets the title key.
	 * 
	 * @param titleKey the title key.
	 * @throws SemanticException
	 */
	public final void setTitleKey(String titleKey) throws SemanticException {
		setStringProperty(IModuleModel.TITLE_ID_PROP, titleKey);
	}

	/**
	 * Initializes the report design when it is just created.
	 * <p>
	 * Set the value to the properties on repot design element which need the
	 * initialize valuel.
	 * 
	 * All initialize operations will not go into the command stack and can not be
	 * undo redo.
	 * 
	 * @param properties the property name value pairs.Those properties in the map
	 *                   are which need to be initialized.
	 * @throws SemanticException SemamticException will throw out when the give
	 *                           properties map contians invlid property name or
	 *                           property value.
	 */

	public final void initializeModule(Map properties) throws SemanticException {
		// if this report deisgn has been initialized, return.
		if (isInitialized)
			return;

		Module root = (Module) getElement();

		// initialize the properties for the reprot design.
		Iterator itre = properties.entrySet().iterator();
		while (itre.hasNext()) {
			Entry entry = (Entry) itre.next();
			String name = (String) entry.getKey();
			try {
				Object value = PropertyValueValidationUtil.validateProperty(this, name, entry.getValue());
				root.setProperty(name, value);
			} catch (SemanticException e) {
				// Do Nothing
			}
		}

		isInitialized = true;

	}

	/**
	 * Returns the encoding of the design/library file. Currently, BIRT only support
	 * UnicodeUtil.SIGNATURE_UTF_8.
	 * 
	 * @return the encoding of the file
	 */

	public final String getFileEncoding() {
		return UnicodeUtil.SIGNATURE_UTF_8;
	}

	/**
	 * Gets symbolic name of this module if defined. This property is needed when
	 * search resources in fragments. Usually it should be the plug-in id of the
	 * host plug-in.
	 * 
	 * @return the symbolica name of this module
	 */

	public final String getSymbolicName() {
		// This method should be deleted.
		return null;
	}

	/**
	 * Sets symbolic name of this module. This property is needed when search
	 * resources in fragments. Usually it should be the plug-in id of the host
	 * plug-in.
	 * 
	 * @param symbolicName
	 * @throws SemanticException
	 */

	public final void setSymbolicName(String symbolicName) throws SemanticException {
		// This method should be deleted.
	}

	/**
	 * Returns the system id of the module. It is the URL path of the module.
	 * 
	 * @return the system id of the module
	 */

	public final URL getSystemId() {
		return module.getSystemId();
	}

	/**
	 * Removes special script lib.
	 * 
	 * @param scriptLib script lib
	 * @throws SemanticException
	 */

	public final void dropScriptLib(ScriptLib scriptLib) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(SCRIPTLIBS_PROP);

		if (scriptLib == null)
			return;

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.removeItem(new StructureContext(getElement(), propDefn, null), scriptLib);
	}

	/**
	 * Removes the given included script.
	 * 
	 * @param includeScript the included script
	 * @throws SemanticException
	 */

	public final void dropIncludeScript(IncludeScript includeScript) throws SemanticException {
		if (includeScript == null)
			return;

		ElementPropertyDefn propDefn = module.getPropertyDefn(INCLUDE_SCRIPTS_PROP);

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.removeItem(new StructureContext(getElement(), propDefn, null), includeScript);
	}

	/**
	 * Removes special script lib handle.
	 * 
	 * @param scriptLibHandle script lib handle
	 * @throws SemanticException
	 */

	public final void dropScriptLib(ScriptLibHandle scriptLibHandle) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(SCRIPTLIBS_PROP);

		if (scriptLibHandle == null)
			return;

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.removeItem(new StructureContext(getElement(), propDefn, null), scriptLibHandle.getStructure());
	}

	/**
	 * Removes all script libs.
	 * 
	 * @throws SemanticException
	 */

	public final void dropAllScriptLibs() throws SemanticException {
		List scriptLibs = getFilteredStructureList(SCRIPTLIBS_PROP, ScriptLib.SCRIPTLIB_NAME_MEMBER);
		if (scriptLibs == null)
			return;
		int count = scriptLibs.size();
		for (int i = count - 1; i >= 0; --i) {
			ScriptLibHandle scriptLibHandle = (ScriptLibHandle) scriptLibs.get(i);
			dropScriptLib(scriptLibHandle);
		}
	}

	/**
	 * Returns the iterator over all script libs. Each one is the instance of
	 * <code>ScriptLibHandle</code>.
	 * <p>
	 * 
	 * @return the iterator over script libs.
	 * @see ScriptLibHandle
	 */

	public final Iterator scriptLibsIterator() {
		return getFilteredStructureList(SCRIPTLIBS_PROP, ScriptLib.SCRIPTLIB_NAME_MEMBER).iterator();
	}

	/**
	 * Returns all script libs.
	 * 
	 * @return list which structure is <code>ScriptLibHandle</code>
	 */

	public final List getAllScriptLibs() {
		return getFilteredStructureList(SCRIPTLIBS_PROP, ScriptLib.SCRIPTLIB_NAME_MEMBER);
	}

	/**
	 * Gets script lib though name
	 * 
	 * @param name name of script lib
	 * @return script lib
	 */

	public final ScriptLib findScriptLib(String name) {
		List scriptLibs = getListProperty(SCRIPTLIBS_PROP);
		if (scriptLibs == null || scriptLibs.isEmpty())
			return null;

		for (int i = 0; i < scriptLibs.size(); ++i) {
			ScriptLib scriptLib = (ScriptLib) scriptLibs.get(i);
			if (scriptLib.getName().equals(name)) {
				return scriptLib;
			}
		}
		return null;
	}

	/**
	 * Shifts jar file from source position to destination position. For example, if
	 * a list has A, B, C scriptLib in order, when move A scriptLib to
	 * <code>newPosn</code> with the value 1, the sequence becomes B, A, C.
	 * 
	 * @param sourceIndex source position. The range is <code>sourceIndex &gt;= 0 &&
	 *            sourceIndex &lt; list.size()</code>
	 * @param destIndex   destination position.The range is
	 *                    <code> destIndex &gt;= 0 &&
	 *            destIndex &lt; list.size()</code>
	 * @throws SemanticException
	 */

	public final void shiftScriptLibs(int sourceIndex, int destIndex) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(SCRIPTLIBS_PROP);
		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.moveItem(new StructureContext(getElement(), propDefn, null), sourceIndex, destIndex);
	}

	/**
	 * Shifts included script from source position to destination position. For
	 * example, if a list has A, B, C scriptLib in order, when move Am includeScript
	 * to <code>newPosn</code> with the value 1, the sequence becomes B, A, C.
	 * 
	 * @param sourceIndex source position. The range is <code>sourceIndex &gt;= 0 &&
	 *            sourceIndex &lt; list.size()</code>
	 * @param destIndex   destination position.The range is
	 *                    <code> destIndex &gt;= 0 &&
	 *            destIndex &lt; list.size()</code>
	 * @throws SemanticException
	 */

	public final void shifIncludeScripts(int sourceIndex, int destIndex) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(INCLUDE_SCRIPTS_PROP);
		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.moveItem(new StructureContext(getElement(), propDefn, null), sourceIndex, destIndex);
	}

	/**
	 * Add script lib
	 * 
	 * @param scriptLib script lib
	 * @throws SemanticException
	 */

	public final void addScriptLib(ScriptLib scriptLib) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(SCRIPTLIBS_PROP);
		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.addItem(new StructureContext(getElement(), propDefn, null), scriptLib);
	}

	/**
	 * Adds include script.
	 * 
	 * @param includeScript the include script
	 * @throws SemanticException
	 */

	public final void addIncludeScript(IncludeScript includeScript) throws SemanticException {
		ElementPropertyDefn propDefn = module.getPropertyDefn(INCLUDE_SCRIPTS_PROP);
		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.addItem(new StructureContext(getElement(), propDefn, null), includeScript);
	}

	/**
	 * Sets the resource folder for this module.
	 * 
	 * @param resourceFolder the folder to set
	 */

	public final void setResourceFolder(String resourceFolder) {
		module.setResourceFolder(resourceFolder);
	}

	/**
	 * Gets the resource folder set in this module.
	 * 
	 * @return the resource folder set in this module
	 */

	public final String getResourceFolder() {
		return module.getResourceFolder();
	}

	/**
	 * Looks up line number of the element in xml source given an element ID.
	 * Returns 1 if no line number of the element exists with the given ID.
	 * 
	 * @param id The id of the element to find.
	 * @return The line number of the element given the element id, or 1 if the
	 *         element can't be found or if IDs are not enabled.
	 * @deprecated new method see {@link #getLineNo(Object)}
	 */

	public final int getLineNoByID(long id) {
		return module.getLineNoByID(id);
	}

	/**
	 * looks up line number of the element\property\structure, in xml source with
	 * given xPaht. Returns 1 if there is no corresponding
	 * element\property\structure.
	 * 
	 * @param obj The xPath of the element\property\structure, it should be unique
	 *            in an report file.
	 * @return The line number of the element\property\structure, or 1 if
	 *         corresponding item does not exist.
	 */

	public final int getLineNo(Object obj) {
		if (obj instanceof StructureHandle) {
			IStructure struct = ((StructureHandle) obj).getStructure();
			if (LineNumberInfo.isLineNumberSuppoerted(struct))
				return module.getLineNo(obj);
		} else if (obj instanceof DesignElementHandle) {
			return module.getLineNo(((DesignElementHandle) obj).getElement());
		} else if (obj instanceof PropertyHandle || obj instanceof SlotHandle) {
			return module.getLineNo(obj);
		}

		return 1;
	}

	/**
	 * Returns the version for the opened design file. If the report/library is
	 * newly created, the version is <code>null</code>. Only the opened/saved
	 * report/library have the version information.
	 * <p>
	 * Whenever the report/library is save, the version becomes <code>
	 * DesignSchemaConstants.REPORT_VERSION</code> . That is, the saved
	 * report/library always have the latest version.
	 * 
	 * @return the design file version number
	 */

	public final String getVersion() {
		String retVersion = module.getVersionManager().getVersion();
		return retVersion;
	}

	/**
	 * Returns the iterator over all included scripts. Each one is the instance of
	 * <code>IncludeScriptHandle</code>
	 * 
	 * @return the iterator over all included scripts.
	 * @see IncludeScriptHandle
	 */

	public final Iterator includeScriptsIterator() {
		PropertyHandle propHandle = getPropertyHandle(INCLUDE_SCRIPTS_PROP);
		return propHandle == null ? Collections.emptyList().iterator() : propHandle.iterator();
	}

	/**
	 * Gets all included scripts. Includes those defined in the libraries.
	 * 
	 * @return the list of included script. Each item is an instance of <code>
	 *         IncludeScriptHandle</code> .
	 */

	public final List getAllIncludeScripts() {
		return getFilteredStructureList(INCLUDE_SCRIPTS_PROP, IncludeScript.FILE_NAME_MEMBER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#copy()
	 */

	public final IDesignElement copy() {
		// for the design/library, should not call copy for paste policy since
		// don't expect localization for extends-related properties.

		try {
			return (IDesignElement) ((Module) getElement()).doClone(DummyCopyPolicy.getInstance());
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		return null;

	}

	/**
	 * Sorts visible elements. Check value in design handle and libraries and sort
	 * the sequence as list in slot handle.
	 * 
	 * @param nameSpaceList the list contains elements from name space
	 * @param level         level
	 * 
	 * @return the list contains sorted design elements.
	 */

	protected final List sortVisibleElements(List nameSpaceList, int level) {
		// Sort element in namespace

		List<ModuleHandleImpl> modules = new ArrayList<ModuleHandleImpl>();
		if (nameSpaceList.size() == 0)
			return modules;

		// Libraries
		modules = getVisibleModules(level);

		return checkVisibleElements(nameSpaceList, modules);
	}

	/**
	 * Gets the visible modules.
	 * 
	 * @param level
	 * @return
	 */
	protected List<ModuleHandleImpl> getVisibleModules(int level) {
		List<ModuleHandleImpl> modules = new ArrayList<ModuleHandleImpl>();
		modules.add(this);
		modules.addAll(getLibraries(level));
		return modules;
	}

	/**
	 * Checks visible elements
	 * 
	 * @param nameSpaceList the list contains elements from name space
	 * @param modules       the list contains design handle and library handle
	 * @param slotID        slot id
	 * @return the list contains sorted design elements.
	 */

	private List checkVisibleElements(List nameSpaceList, List modules) {
		assert modules != null;
		List resultList = new ArrayList();
		List<Module> moduleList = new ArrayList<Module>();
		for (int i = 0; i < modules.size(); i++) {
			ModuleHandleImpl moduleHandle = (ModuleHandleImpl) modules.get(i);
			moduleList.add(moduleHandle.getModule());
		}

		for (int i = 0; i < nameSpaceList.size(); ++i) {
			DesignElement content = (DesignElement) nameSpaceList.get(i);

			if (moduleList.contains(content.getRoot())) {
				resultList.add(content);
			}

		}
		return resultList;
	}

	/**
	 * Generates a list of element handles according to the given element list. Each
	 * content in the return list is generated use <code>element.getHandle(
	 * Module )</code>
	 * 
	 * @param elementList a list of elements.
	 * @return a list of element handles.
	 */

	protected List generateHandleList(List elementList) {
		List handleList = new ArrayList();

		Iterator iter = elementList.iterator();
		while (iter.hasNext()) {
			DesignElement element = (DesignElement) iter.next();

			Module root = element.getRoot();
			assert root != null;

			handleList.add(element.getHandle(root));
		}
		return handleList;
	}

	/**
	 * Gets all the shared dimensions defined or accessed by this module.
	 * 
	 * @return
	 */
	public List<DimensionHandle> getAllSharedDimensions() {
		return Collections.emptyList();
	}

	/**
	 * Checks the report if it is set in options.
	 */
	public void checkReportIfNecessary() {
		ModuleOption options = module.getOptions();
		if (options == null || options.useSemanticCheck())
			checkReport();
	}

	/**
	 * Sets options to the module.
	 *
	 * @param options
	 */
	public void setOptions(Map options) {
		module.setOptions(options);
	}

	/**
	 * Gets the options set in the module.
	 *
	 * @return
	 */
	public Map getOptions() {
		ModuleOption options = module.getOptions();
		if (options == null)
			return Collections.EMPTY_MAP;

		return options.getOptions();
	}
}
