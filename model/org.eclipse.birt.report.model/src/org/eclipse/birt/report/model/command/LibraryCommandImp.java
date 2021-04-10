/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.command;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.command.LibraryReloadedEvent;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.ElementStructureUtil;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.LibraryUtil;

/**
 * Represents the command for adding and dropping library from report design.
 * For each operation, should start a new command instead of using the existing
 * command.
 */

public class LibraryCommandImp extends AbstractElementCommand {

	/**
	 * The action to reload the library.
	 */

	private static final int RELOAD_ACTION = 1;

	/**
	 * The simple action like add/remove.
	 */

	private static final int SIMPLE_ACTION = 2;

	/**
	 * The included library structure is appended to the list.
	 */

	private static final int APPEND_POS = -1;

	/**
	 * Construct the command with the report design.
	 * 
	 * @param module the report design
	 */

	public LibraryCommandImp(Module module) {
		super(module, module);
	}

	/**
	 * Adds new library file to report design.
	 * 
	 * @param libraryFileName library file name
	 * @param namespace       library name space
	 * @throws DesignFileException if the library file is not found or has fatal
	 *                             errors.
	 * @throws SemanticException   if failed to add <code>IncludeLibrary</code>
	 *                             structure
	 */

	public void addLibrary(String libraryFileName, String namespace) throws DesignFileException, SemanticException {
		if (StringUtil.isBlank(namespace))
			namespace = StringUtil.extractFileName(libraryFileName);

		URL fileURL = module.findResource(libraryFileName, IResourceLocator.LIBRARY);

		Module outermostModule = module.findOutermostModule();

		Library foundLib = null;
		try {
			foundLib = LibraryUtil.checkIncludeLibrary(module, namespace, fileURL, outermostModule);
		} catch (LibraryException ex) {
			throw ex;
		}

		if (foundLib == null)
			foundLib = module.loadLibrary(libraryFileName, namespace, new HashMap<String, Library>(), fileURL);
		else
			foundLib = foundLib.contextClone(module);

		doAddLibrary(libraryFileName, foundLib);
	}

	/**
	 * Drop the given library from the design. And break all the parent/child
	 * relationships. All child element will be localized in the module.
	 * 
	 * @param library a given library to be dropped.
	 * @throws SemanticException
	 * 
	 */

	public void dropLibraryAndBreakExtends(Library library) throws SemanticException {
		// library not found.

		if (!module.getLibraries().contains(library)) {
			throw new LibraryException(library, new String[] { library.getNamespace() },
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND);
		}

		ActivityStack stack = getActivityStack();

		LibraryRecord record = new LibraryRecord(module, library, false);
		stack.startTrans(record.getLabel());
		try {
			for (int slotID = 0; slotID < library.getDefn().getSlotCount(); slotID++) {
				if (slotID == ILibraryModel.THEMES_SLOT)
					continue;

				for (Iterator<DesignElement> iter = library.getSlot(slotID).iterator(); iter.hasNext();) {
					DesignElement element = iter.next();
					List<DesignElement> derived = element.getDerived();
					for (int i = 0; i < derived.size(); i++) {
						DesignElement child = derived.get(i);
						if (child.getRoot() == getModule()) {
							ExtendsCommand command = new ExtendsCommand(getModule(), child);
							command.localizeElement();
						}
					}
				}
			}

			getActivityStack().execute(record);

			// Remove the include library structure.

			String libraryFileName = library.getFileName();
			assert libraryFileName != null;
			removeIncludeLibrary(libraryFileName, library.getNamespace());

		} catch (SemanticException ex) {
			stack.rollback();
			throw ex;
		}
		getActivityStack().commit();
	}

	/**
	 * Drops the given library.
	 * 
	 * @param library the library to drop
	 * @param inForce <code>true</code> if drop the library even the module has
	 *                element reference to this library. <code>false</code> if do
	 *                not drop library when the module has element reference to this
	 *                library.
	 * @throws SemanticException if failed to remove <code>IncludeLibrary</code>
	 *                           structure
	 */

	public void dropLibrary(Library library) throws SemanticException {
		// library not found.

		if (!module.getLibraries().contains(library)) {
			throw new LibraryException(library, new String[] { library.getNamespace() },
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND);
		}

		// library has dependents in the current module. And check the inForce
		// flag.

		dealAllElementDecendents(library, SIMPLE_ACTION);

		doDropLibrary(library);
	}

	/**
	 * Performs the action to drop the library from the module.
	 * 
	 * @param library the library to drop
	 * @param action  can be RELOAD or SIMPLE.
	 * @throws SemanticException if error occurs during insert an included library
	 */

	private void doDropLibrary(Library library) throws SemanticException {
		// Remove the include library structure.

		ActivityStack stack = getActivityStack();

		// Drop the library and update the client references.

		LibraryRecord record = new LibraryRecord(library.getHost(), library, false);

		stack.startTrans(record.getLabel());

		getActivityStack().execute(record);

		try {
			String libFileName = library.getFileName();
			assert libFileName != null;
			removeIncludeLibrary(libFileName, library.getNamespace());
		} catch (SemanticException ex) {
			stack.rollback();
			throw ex;
		}
		getActivityStack().commit();

	}

	/**
	 * Checks possible extends references for the given element. If extends
	 * reference is unresolve, virtual elements of extends children are removed. And
	 * local property values of virtual elements are returned.
	 * 
	 * @param parent the design element
	 * @return the map containing local values of virtual elements. Each key is the
	 *         id of extends child. Each value is another map of which the key is
	 *         the base id of virtual element and the value is property name/value
	 *         pair.
	 * @throws SemanticException if error occurs during removing virtual elements.
	 */

	private Map<Long, Map<Long, List<Object>>> dealElementDecendents(Module library, DesignElement parent,
			int actionCode) throws SemanticException {
		if (!parent.hasDerived())
			return Collections.emptyMap();

		List<DesignElement> allDescendents = new ArrayList<DesignElement>();
		getAllDescdents(parent, allDescendents);

		Map<Long, Map<Long, List<Object>>> overriddenValues = new HashMap<Long, Map<Long, List<Object>>>();

		for (int i = 0; i < allDescendents.size(); i++) {
			DesignElement child = allDescendents.get(i);
			Module tmpModule = child.getRoot();

			if (child.hasDerived())
				dealElementDecendents(tmpModule, child, actionCode);

			if (tmpModule != module)
				continue;

			if (actionCode == RELOAD_ACTION) {
				Map<Long, List<Object>> values = unresolveElementDescendent(module, child);
				overriddenValues.put(Long.valueOf(child.getID()), values);
			} else if (actionCode == SIMPLE_ACTION)
				throw new LibraryException(library, new String[] { child.getHandle(module).getDisplayLabel() },
						LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS);

		}

		return overriddenValues;
	}

	/**
	 * Reloads the library with the given file path. After reloading, activity stack
	 * is cleared.
	 * 
	 * @param toReloadLibrary the URL file path of the library file. The instance
	 *                        must be directly/indirectly included in the module.
	 * @param includedLib     the included library structure
	 * @param reloadLibs      the map contains reload libraries, the name space is
	 *                        key and the library instance is the value
	 * @throws DesignFileException if the file does no exist.
	 * @throws SemanticException   if the library is not included in the current
	 *                             module.
	 */

	public void reloadLibrary(Library toReloadLibrary, IncludedLibrary includedLib, Map<String, Library> reloadLibs)
			throws DesignFileException, SemanticException {
		String location = toReloadLibrary.getLocation();
		if (location == null)
			location = toReloadLibrary.getFileName();

		Library library = null;
		List<Library> libs = module.getLibrariesByLocation(location, IAccessControl.ARBITARY_LEVEL);
		for (int i = 0; i < libs.size(); i++) {
			if (toReloadLibrary == libs.get(i)) {
				library = toReloadLibrary;
				break;
			}
		}

		if (library == null)
			library = getLibraryByStruct(includedLib);

		if (library == null)
			throw new LibraryException(module, new String[] { toReloadLibrary.getNamespace() },
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND);

		library = findTopLevelLibrary(library);
		assert library != null;

		Module host = library.getHost();
		IncludedLibrary tmpIncludedLib = host.findIncludedLibrary(library.getNamespace());
		int removePosn = host.getIncludedLibraries().indexOf(tmpIncludedLib);

		Map<Long, Map<Long, List<Object>>> overriddenValues = null;
		ActivityStack activityStack = getActivityStack();
		activityStack.startSilentTrans(true);

		try {
			// must use content command to remove all virtual elements if
			// required. This can solve unresolving issues like DataSet, Style
			// references, as well as removing names from name space.

			overriddenValues = dealAllElementDecendents(library, RELOAD_ACTION);
			doDropLibrary(library);

			doReloadLibrary(library, tmpIncludedLib.getFileName(), overriddenValues, reloadLibs, removePosn);
		} catch (SemanticException e) {
			activityStack.rollback();
			throw e;
		} catch (DesignFileException e) {
			activityStack.rollback();
			throw e;
		}

		// send the library reloaded event first, and then commit transaction

		library = module.getLibraryWithNamespace(library.getNamespace(), IAccessControl.DIRECTLY_INCLUDED_LEVEL);

		doPostReloadAction(library);

	}

	/**
	 * Reloads libraries according to the given location.
	 * 
	 * @param location   the library location
	 * @param reloadLibs the map contains reload libraries, the name space is key
	 *                   and the library instance is the value
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void reloadLibrary(String location, Map<String, Library> reloadLibs)
			throws DesignFileException, SemanticException {
		List<Library> libs = module.getLibrariesByLocation(location, IAccessControl.ARBITARY_LEVEL);

		for (int i = 0; i < libs.size(); i++)
			reloadLibrary(libs.get(i), null, reloadLibs);
	}

	/**
	 * Returns the library that is directly included the outermost module.
	 * 
	 * @param lib the library file
	 * @return the library that is directly included the outermost module
	 */

	private Library findTopLevelLibrary(Library lib) {
		Library tmpLib = lib;

		// find the right library to reload.

		while (tmpLib != null) {
			if (tmpLib.getHost() == module)
				break;

			tmpLib = (Library) tmpLib.getHost();
		}

		return tmpLib;
	}

	/**
	 * Adds new library file to report design.
	 * 
	 * @param libraryFileName library file name
	 * @param namespace       library name space
	 * @throws DesignFileException if the library file is not found or has fatal
	 *                             errors.
	 * @throws SemanticException   if failed to add <code>IncludeLibrary</code>
	 *                             structure
	 */

	public void reloadLibrary(String libraryFileName, String namespace) throws DesignFileException, SemanticException {
		if (StringUtil.isBlank(namespace))
			namespace = StringUtil.extractFileName(libraryFileName);

		addLibrary(libraryFileName, namespace);

		// do post reload actions

		Library lib = module.getLibraryWithNamespace(namespace);
		doPostReloadAction(lib);
	}

	/**
	 * Does some post actions after library is reloaded. It includes sending out the
	 * LibraryReloadEvent, commit the stack and flush the stack and send out the
	 * ActivityStackEvent.
	 * 
	 * @param lib
	 * @throws BirtException
	 */

	protected void doPostReloadAction(Library lib) throws DesignFileException, SemanticException {
		LibraryReloadedEvent event = new LibraryReloadedEvent(module, lib);
		module.broadcast(event);

		// clear save state mark.

		ActivityStack activityStack = module.getActivityStack();
		activityStack.commit();

		// clear all common stack.

		activityStack.flush();

		module.setSaveState(0);
		activityStack.sendNotifcations(new ActivityStackEvent(activityStack, ActivityStackEvent.DONE));
	}

	/**
	 * Checks possible extends references to element in the given Library. If
	 * extends reference is unresolved, virtual elements are removed. And local
	 * property values of virtual elements are returned.
	 * 
	 * @param library the library instance
	 * @return the map containing local values of virtual elements. Each key is the
	 *         id of extends child. Each value is another map of which the key is
	 *         the base id of virtual element and the value is property name/value
	 *         pair.
	 * @throws LibraryException if there is any extends reference.
	 */

	private Map<Long, Map<Long, List<Object>>> dealAllElementDecendents(Library library, int actionCode)
			throws SemanticException {
		// library has decendents in the current module. And check the inForce
		// flag.

		Map<Long, Map<Long, List<Object>>> overriddenValues = new HashMap<Long, Map<Long, List<Object>>>();

		LevelContentIterator contentIter = new LevelContentIterator(library, library, 1);
		while (contentIter.hasNext()) {
			DesignElement tmpElement = contentIter.next();
			if (!tmpElement.getDefn().canExtend())
				continue;

			Map<Long, Map<Long, List<Object>>> values = dealElementDecendents(library, tmpElement, actionCode);
			if (actionCode == RELOAD_ACTION)
				overriddenValues.putAll(values);
		}

		return overriddenValues;
	}

	/**
	 * Performs the action to add the library to the module.
	 * 
	 * @param libraryFileName  the library path
	 * @param namespace        the library namespace
	 * @param action           can be RELOAD or SIMPLE.
	 * @param overriddenValues the overridden values.
	 * @throws SemanticException if the library file is invalid.
	 */

	private void doAddLibrary(String libraryFileName, Library foundLib) throws SemanticException, DesignFileException {
		Library library = foundLib;

		library.setReadOnly();
		ActivityStack activityStack = getActivityStack();

		LibraryRecord record = new LibraryRecord(module, library, true);
		activityStack.startTrans(record.getLabel());
		getActivityStack().execute(record);

		// Add includedLibraries

		String namespace = foundLib.getNamespace();

		if (module.findIncludedLibrary(namespace) == null)
			addLibraryStructure(libraryFileName, namespace, APPEND_POS);

		activityStack.commit();
	}

	/**
	 * Reloads the given library. During this step, the input library has been
	 * removed. It only for reloading operation.
	 * 
	 * @param toReload         the library to reload
	 * @param overriddenValues the overridden values
	 * @param reloadLibs       the map contains reload libraries, the name space is
	 *                         key and the library instance is the value
	 * @param removePosn       the position at which the library should be inserted
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	private void doReloadLibrary(Library toReload, String includedLibPath,
			Map<Long, Map<Long, List<Object>>> overriddenValues, Map<String, Library> reloadLibs, int removePosn)
			throws SemanticException, DesignFileException {
		String namespace = toReload.getNamespace();
		URL fileURL = module.findResource(includedLibPath, IResourceLocator.LIBRARY);

		// if the file cannot be found,add the included library structure only.

		if (fileURL == null) {
			if (module.findIncludedLibrary(namespace) == null)
				addLibraryStructure(includedLibPath, namespace, removePosn);

			// add an invalid library instance, same to the parser

			Library invalidLib = new Library(module.getSession(), module);
			invalidLib.setFileName(includedLibPath);
			invalidLib.setNamespace(namespace);
			invalidLib.setID(invalidLib.getNextID());
			invalidLib.addElementID(invalidLib);
			invalidLib.setValid(false);

			ActivityStack activityStack = getActivityStack();

			LibraryRecord record = new LibraryRecord(module, invalidLib, overriddenValues, removePosn);

			activityStack.startTrans(record.getLabel());
			activityStack.execute(record);
			activityStack.commit();

			return;
		}

		Library library = null;
		Library reloadLibrary = reloadLibs.get(toReload.getNamespace());

		if (reloadLibrary == null) {
			library = module.loadLibrary(includedLibPath, namespace, reloadLibs, fileURL);
			LibraryUtil.insertReloadLibs(reloadLibs, library);
		} else {
			library = reloadLibrary.contextClone(module);
		}

		library.setReadOnly();

		ActivityStack activityStack = getActivityStack();

		LibraryRecord record = new LibraryRecord(module, library, overriddenValues, removePosn);

		assert record != null;
		activityStack.startTrans(record.getLabel());
		getActivityStack().execute(record);

		// Add includedLibraries

		if (module.findIncludedLibrary(namespace) == null)
			addLibraryStructure(includedLibPath, namespace, removePosn);

		activityStack.commit();
	}

	/**
	 * Adds an include library structure in the module.
	 * 
	 * @param libraryFileName
	 * @param namespace
	 * @throws SemanticException
	 */

	private void addLibraryStructure(String libraryFileName, String namespace, int removePosn)
			throws SemanticException {
		// Add includedLibraries

		IncludedLibrary includeLibrary = StructureFactory.createIncludeLibrary();
		includeLibrary.setFileName(libraryFileName);
		includeLibrary.setNamespace(namespace);

		ElementPropertyDefn propDefn = module.getPropertyDefn(IModuleModel.LIBRARIES_PROP);
		ComplexPropertyCommand propCommand = new ComplexPropertyCommand(module, module);

		if (removePosn == APPEND_POS)
			propCommand.addItem(new StructureContext(module, propDefn, null), includeLibrary);
		else {
			propCommand.insertItem(new StructureContext(module, propDefn, null), includeLibrary, removePosn);
		}
	}

	/**
	 * Un-resolves extends reference for the given element. Besides the change on
	 * extends reference, all virtual elements in the given element are removed.
	 * Their element references are also cleared. Moreover, their names are removed
	 * from name spaces.
	 * 
	 * @param module the root of the element
	 * @param child  the design element
	 * @return the map containing local values of virtual elements. The key is the
	 *         base id of virtual element and the value is property name/value pair.
	 * @throws SemanticException if error occurs during removing virtual elements.
	 */

	private Map<Long, List<Object>> unresolveElementDescendent(Module module, DesignElement child)
			throws SemanticException {
		ElementRefValue value = (ElementRefValue) child.getLocalProperty(module, IDesignElementModel.EXTENDS_PROP);

		DesignElement parent = value.getElement();
		assert parent != null;

		// special case for extended item

		if (child instanceof ExtendedItem) {
			assert parent instanceof ExtendedItem;
			if (!((ExtendedItem) child).hasLocalPropertyValuesOnOwnModel())
				((ExtendedItem) child).getExtensibilityProvider().clearOwnModel();
		}

		// not layout structure involved.

		if (!child.getDefn().isContainer()) {
			// unresolves the extends child

			parent.dropDerived(child);
			value.unresolved(value.getName());

			return Collections.emptyMap();
		}

		Map<Long, List<Object>> overriddenValues = ElementStructureUtil.collectPropertyValues(module, child);

		// remove virtual elements in the element

		ActivityStack activityStack = getActivityStack();

		activityStack.startSilentTrans(null);

		LevelContentIterator contentIter = new LevelContentIterator(module, child, 1);
		while (contentIter.hasNext()) {
			DesignElement tmpElement = contentIter.next();
			ContentCommand command = new ContentCommand(module, tmpElement.getContainerInfo(), true, true);
			command.remove(tmpElement);
		}

		activityStack.commit();

		// unresolves the extends child

		parent.dropDerived(child);
		value.unresolved(value.getName());

		return overriddenValues;
	}

	/**
	 * Recursively collect all the descendant of the given element.
	 * 
	 * @param tmpElement a given element.
	 * @param results    the result list containing all the children.
	 */

	private void getAllDescdents(DesignElement tmpElement, List<DesignElement> results) {
		List<DesignElement> descends = tmpElement.getDerived();
		results.addAll(descends);

		for (int i = 0; i < descends.size(); i++) {
			getAllDescdents(descends.get(i), results);
		}
	}

	/**
	 * Drops the include library structure.
	 * 
	 * @param fileName  file name of the library.
	 * 
	 * @param namespace name space of the library.
	 * 
	 * @throws PropertyValueException
	 */

	private void removeIncludeLibrary(String fileName, String namespace) throws PropertyValueException {
		assert fileName != null;
		assert namespace != null;

		List<IncludedLibrary> includeLibraries = module.getIncludedLibraries();
		for (int i = 0; i < includeLibraries.size(); i++) {
			IncludedLibrary includeLibrary = includeLibraries.get(i);

			if (!namespace.equals(includeLibrary.getNamespace()))
				continue;

			if (!fileName.endsWith(StringUtil.extractFileNameWithSuffix(includeLibrary.getFileName())))
				continue;

			ElementPropertyDefn propDefn = module.getPropertyDefn(IModuleModel.LIBRARIES_PROP);
			ComplexPropertyCommand propCommand = new ComplexPropertyCommand(module, module);
			propCommand.removeItem(new StructureContext(module, propDefn, null), includeLibrary);
			break;
		}
	}

	/**
	 * Finds the library that matches the given the included library structure.
	 * 
	 * @param includedLib the included library structure
	 * @return the matched library instance
	 */

	private Library getLibraryByStruct(IncludedLibrary includedLib) {
		List<Object> includedLibs = module.getListProperty(module, IModuleModel.LIBRARIES_PROP);
		if (includedLibs == null)
			return null;

		int index = includedLibs.indexOf(includedLib);
		if (index == -1)
			return null;

		Library retLib = module.getLibraries().get(index);
		if (retLib.getNamespace().equalsIgnoreCase(includedLib.getNamespace()))
			return retLib;

		return null;
	}
}
