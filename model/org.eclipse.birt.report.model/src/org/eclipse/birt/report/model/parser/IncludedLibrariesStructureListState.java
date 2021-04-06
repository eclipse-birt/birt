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

package org.eclipse.birt.report.model.parser;

import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.LibraryUtil;
import org.xml.sax.SAXException;

/**
 * Parses the simple structure list for "includeLibraires" property, each of
 * which has only one member. So it also can be considered as String List.
 */

public class IncludedLibrariesStructureListState extends CompatibleListPropertyState {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(IncludedLibrariesStructureListState.class.getName());

	/**
	 * Default constructor.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the element
	 */

	IncludedLibrariesStructureListState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */
	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.STRUCTURE_TAG == tagValue)
			return new IncludedLibraryStructureState(handler, element, propDefn);

		return super.startElement(tagName);
	}

	private static class IncludedLibraryStructureState extends CompatibleStructureState {

		IncludedLibraryStructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn) {
			super(theHandler, element, propDefn);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			super.end();

			IncludedLibrary includeLibrary = (IncludedLibrary) struct;

			// Use file name without path and suffix as default name space.

			if (StringUtil.isBlank(includeLibrary.getNamespace())) {
				String fileName = StringUtil.extractFileName(includeLibrary.getFileName());
				includeLibrary.setNamespace(fileName);
			}

			URL url = handler.module.findResource(includeLibrary.getFileName(), IResourceLocator.LIBRARY);

			String namespace = includeLibrary.getNamespace();

			// need to find the outermost module so that to make sure only one
			// instance is reload for the same library file.

			Module outermostModule = handler.module.findOutermostModule();

			Library foundLib = null;
			try {
				foundLib = LibraryUtil.checkIncludeLibrary(handler.module, namespace, url, outermostModule);
			} catch (LibraryException ex) {
				if (LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED.equalsIgnoreCase(ex.getErrorCode()))
					handler.getErrorHandler().semanticWarning(ex);
				else
					handler.getErrorHandler().semanticError(ex);
				logger.log(Level.WARNING, ex.getMessage(), ex);

				return;
			}

			Map<String, Library> reloadLibs = handler.reloadLibs;

			// get the reload library if applicable.
			foundLib = reloadLibs.get(namespace);

			handler.module.loadLibrarySilently(includeLibrary, foundLib, reloadLibs, url);
		}
	}

}
