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

package org.eclipse.birt.report.model.util;

import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.NamePropertyType;

/**
 * The utility class for the library related operation.
 * 
 */

public class LibraryUtil {

	/**
	 * Checks whether the library with given name space/URL can be included in the
	 * given module.
	 * 
	 * @param module          the module to include library
	 * @param namespace       the library name space
	 * @param url             the URL of the library file
	 * @param outermostModule the root of the module, its host must be null
	 * @return the matched library
	 * 
	 * @throws LibraryException
	 */

	public static Library checkIncludeLibrary(Module module, String namespace, URL url, Module outermostModule)
			throws LibraryException {

		// check whether the namespace of the library is valid or not
		if (StringUtil.isBlank(namespace) || !NamePropertyType.isValidName(namespace)) {
			throw new LibraryException(module, new String[] { namespace },
					LibraryException.DESIGN_EXCEPTION_INVALID_LIBRARY_NAMESPACE);
		}

		Library foundLib = outermostModule.getLibraryWithNamespace(namespace);

		if (url != null) {
			if (foundLib != null) {
				String tmpPath = foundLib.getLocation();
				String foundPath = url.toExternalForm();

				// the case: the same name spaces but different library files.

				if (!foundPath.equalsIgnoreCase(tmpPath)) {
					throw new LibraryException(module, new String[] { namespace },
							LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE);
				}

				// the library has already been included.

				if (module.getLibraryWithNamespace(namespace, IAccessControl.DIRECTLY_INCLUDED_LEVEL) != null) {
					throw new LibraryException(module, new String[] { namespace },
							LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE);
				}
			} else {
				// the name space must be unique since foundLib == null at this
				// time.

				foundLib = outermostModule.getLibraryByLocation(url.toExternalForm());
			}

			if (module.getLibraryByLocation(url.toExternalForm(), IAccessControl.DIRECTLY_INCLUDED_LEVEL) != null) {
				throw new LibraryException(module, new String[] { url.toExternalForm() },
						LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED);
			}
		}

		// check the recursive libraries from top to bottom

		if (module instanceof Library) {
			Library library = (Library) module;

			if (url != null && library.isRecursiveFile(url.toExternalForm())
					|| library.isRecursiveNamespace(namespace)) {
				throw new LibraryException(module, new String[] { namespace },
						LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY);
			}
		}

		return foundLib;
	}

	/**
	 * Inserts load libraries into the given map.
	 * 
	 * @param reloadLibs the map contains reload libraries, the name space is key
	 *                   and the library instance is the value
	 * @param library    the given library
	 */

	public static void insertReloadLibs(Map<String, Library> reloadLibs, Library library) {
		if (reloadLibs == null || reloadLibs == Collections.EMPTY_MAP)
			return;

		Set<String> namespaces = reloadLibs.keySet();

		List<Library> tmpLibs = library.getAllLibraries();
		String namespace = library.getNamespace();

		if (!namespaces.contains(namespace))
			reloadLibs.put(namespace, library);

		for (int i = 0; i < tmpLibs.size(); i++) {
			Library tmpLib = tmpLibs.get(i);
			namespace = tmpLib.getNamespace();

			if (!namespaces.contains(namespace))
				reloadLibs.put(namespace, library);

			reloadLibs.put(namespace, tmpLib);
		}
	}

	/**
	 * Inserts a default theme to the library slot.
	 * 
	 * @param library the target library
	 * @param theme   the theme to insert
	 */

	public static void insertCompatibleThemeToLibrary(Library library, Theme theme) {
		assert library != null;
		assert theme != null;

		// The name should not be null if it is required. The parser state
		// should have already caught this case.

		String name = theme.getName();
		assert !StringUtil.isBlank(name) && ModelMessages.getMessage(IThemeModel.DEFAULT_THEME_NAME).equals(name);

		NameSpace ns = library.getNameHelper().getNameSpace(Module.THEME_NAME_SPACE);
		assert library.getNameHelper().canContain(Module.THEME_NAME_SPACE, name);

		ns.insert(theme);

		// Add the item to the container.
		library.add(theme, ILibraryModel.THEMES_SLOT);
	}

	/**
	 * Checks if report design contains the same library as the target library which
	 * user wants to export. If no exception throws , that stands for user can
	 * export report design to library. Comparing with the obsolute file name path,
	 * if file is the same , throw <code>SemanticException</code> which error code
	 * is include recursive error.
	 * 
	 * For example , the path of library is "C:\test\lib.xml" .The followings will
	 * throw semantic exception:
	 * 
	 * <ul>
	 * <li>design file and library in the same folder:</li>
	 * <li><list-property name="libraries"> <structure>
	 * <property name="fileName">lib.xml</property>
	 * <property name="namespace">lib</property> </structure> </list-property></li>
	 * </ul>
	 * <ul>
	 * <li>folder of design file is "C:\design"</li>
	 * <li><list-property name="libraries"> <structure>
	 * <property name="fileName">..\test\lib.xml</property>
	 * <property name="namespace">lib</property> </structure> </list-property></li>
	 * </ul>
	 * 
	 * @param designToExport      handle of the report design to export
	 * @param targetLibraryHandle handle of target library
	 * @return if contains the same absolute file path , return true; else return
	 *         false.
	 * @throws SemanticException if absolute file path is the same between library
	 *                           included in report design and library.
	 */

	public static boolean hasLibrary(ReportDesignHandle designToExport, LibraryHandle targetLibraryHandle) {
		String reportLocation = targetLibraryHandle.getModule().getLocation();

		List<Library> libList = designToExport.getModule().getAllLibraries();

		for (Iterator<Library> libIter = libList.iterator(); libIter.hasNext();) {
			Library library = libIter.next();
			String libLocation = library.getRoot().getLocation();

			if (reportLocation.equals(libLocation)) {
				return true;
			}
		}
		return false;
	}

}
