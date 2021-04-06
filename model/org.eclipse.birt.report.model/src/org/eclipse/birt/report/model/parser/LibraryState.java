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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.LibraryUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * This class provides parser state for the top-level Library element.
 */

public class LibraryState extends ModuleState {

	/**
	 * Constructs the library state with the library file parser handler.
	 * 
	 * @param theHandler The library parser handler.
	 */

	public LibraryState(ModuleParserHandler theHandler) {
		super(theHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		if (handler.versionNumber >= VersionUtil.VERSION_3_2_16) {
			super.end();
			return;
		}

		Library library = (Library) getElement();

		Object themeObj = getElement().getLocalProperty(module, IModuleModel.THEME_PROP);
		if (themeObj != null)
			return;

		// do the compatibility work for the library without the theme

		Theme theme = ((Library) module).findNativeTheme(ModelMessages.getMessage(IThemeModel.DEFAULT_THEME_NAME));

		if (theme == null) {
			theme = new Theme(ModelMessages.getMessage(IThemeModel.DEFAULT_THEME_NAME));
			LibraryUtil.insertCompatibleThemeToLibrary(library, theme);

			handler.unhandleIDElements.add(theme);

			library.setProperty(IModuleModel.THEME_PROP, new ElementRefValue(null, theme));
		}

		super.end();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		if (handler.isReadOnlyModuleProperties) {
			return super.startElement(tagName);
		}

		if (tagName.equalsIgnoreCase(DesignSchemaConstants.TRANSLATIONS_TAG))
			return new TranslationsState();
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PARAMETERS_TAG))
			return new ParametersState(handler, getElement(), IModuleModel.PARAMETER_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.DATA_SOURCES_TAG))
			return new DataSourcesState(handler, getElement(), IModuleModel.DATA_SOURCE_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.DATA_SETS_TAG))
			return new DataSetsState(handler, getElement(), IModuleModel.DATA_SET_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.THEMES_TAG))
			return new ThemesState(handler, getElement(), ILibraryModel.THEMES_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STYLES_TAG)
				&& handler.versionNumber < VersionUtil.VERSION_3_0_0)
			return new CompatibleLibraryStylesState(handler, getElement(), ILibraryModel.THEMES_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PAGE_SETUP_TAG))
			return new PageSetupState(handler, getElement(), IModuleModel.PAGE_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.COMPONENTS_TAG))
			return new ComponentsState(handler, getElement(), IModuleModel.COMPONENT_SLOT);
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PROPERTY_TAG))
			return new PropertyState(handler, getElement());
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.CUBES_TAG))
			return new CubesState(handler, getElement(), ILibraryModel.CUBE_SLOT);
		return super.startElement(tagName);

	}
}
