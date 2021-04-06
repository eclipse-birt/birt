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

package org.eclipse.birt.report.model.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.StyleSheetLoader;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.TranslationTable;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.util.LibraryUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

public abstract class LayoutModule extends Module {

	/**
	 * Internal table to store a bunch of user-defined messages. One message can be
	 * defined in several translations, one translation per locale.
	 */

	protected TranslationTable translations = new TranslationTable();

	/**
	 * The property definition list of all the referencable structure list property.
	 * Each one in the list is instance of <code>IPropertyDefn</code>
	 */

	private HashMap<String, IElementPropertyDefn> referencableProperties = null;

	/**
	 * The default units for the design.
	 */
	protected String units = null;

	/**
	 * All libraries which are included in this module.
	 */

	private List<Library> libraries = null;

	/**
	 * The theme for the module.
	 */

	private ElementRefValue theme = null;

	/**
	 * The fatal exception found in the included modules, which will stop opening
	 * the outer-most module.
	 */

	protected Exception fatalException;

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(LayoutModule.class.getName());

	/**
	 * Default constructor.
	 * 
	 * @param theSession the session of the report
	 */

	protected LayoutModule(DesignSessionImpl theSession) {
		super(theSession);

		// initialize name helper
		this.nameHelper = new ModuleNameHelper(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#findNativeStyle(java.lang.String )
	 */
	public StyleElement findNativeStyle(String name) {
		return (StyleElement) nameHelper.getNameSpace(STYLE_NAME_SPACE).getElement(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#findStyle(java.lang.String)
	 */

	public StyleElement findStyle(String name) {
		return (StyleElement) resolveElement(null, name, null,
				MetaDataDictionary.getInstance().getElement(ReportDesignConstants.STYLE_ELEMENT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#findElement(java.lang.String)
	 */
	public DesignElement findElement(String name) {
		return resolveNativeElement(name, ELEMENT_NAME_SPACE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#findPage(java.lang.String)
	 */

	public DesignElement findPage(String name) {
		return resolveNativeElement(name, PAGE_NAME_SPACE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getTranslationTable()
	 */
	protected TranslationTable getTranslationTable() {
		return translations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#addTranslation(org.eclipse.
	 * birt.report.model.elements.Translation)
	 */

	public void addTranslation(Translation translation) {
		translations.add(translation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#dropTranslation(org.eclipse
	 * .birt.report.model.elements.Translation)
	 */

	public boolean dropTranslation(Translation translation) {
		return translations.remove(translation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#findTranslation(java.lang.String ,
	 * java.lang.String)
	 */
	public Translation findTranslation(String resourceKey, String locale) {
		return translations.findTranslation(resourceKey, locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#contains(org.eclipse.birt.report
	 * .model.elements.Translation)
	 */

	public boolean contains(Translation trans) {
		return translations.contains(trans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getTranslations()
	 */

	public List<Translation> getTranslations() {
		return translations.getTranslations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#getTranslations(java.lang.String )
	 */
	public List<Translation> getTranslations(String resourceKey) {
		return translations.getTranslations(resourceKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getTranslationResourceKeys()
	 */

	public String[] getTranslationResourceKeys() {
		return translations.getResourceKeys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getReferencablePropertyDefn
	 * (java.lang.String)
	 */
	public ElementPropertyDefn getReferencablePropertyDefn(String structureName) {
		if (referencableProperties == null) {
			referencableProperties = new HashMap<String, IElementPropertyDefn>();
			referencableProperties.put(ConfigVariable.CONFIG_VAR_STRUCT, getPropertyDefn(CONFIG_VARS_PROP));
			referencableProperties.put(EmbeddedImage.EMBEDDED_IMAGE_STRUCT, getPropertyDefn(IMAGES_PROP));
			referencableProperties.put(CustomColor.CUSTOM_COLOR_STRUCT, getPropertyDefn(COLOR_PALETTE_PROP));

		}

		return (ElementPropertyDefn) referencableProperties.get(structureName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#semanticCheck(org.eclipse.birt
	 * .report.model.core.Module)
	 */
	public void semanticCheck(Module module) {
		super.semanticCheck(module);

		// delete all useless template parameter definition

		if (module instanceof ReportDesign) {
			ContainerSlot slot = module.getSlot(IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT);
			assert slot != null;
			for (int i = slot.getCount() - 1; i >= 0; i--) {
				TemplateParameterDefinition templateParam = (TemplateParameterDefinition) slot.getContent(i);
				if (templateParam.getClientList().isEmpty()) {

					// Remove the element from the ID map if we are usingIDs.

					NameSpace ns = nameHelper.getNameSpace(TEMPLATE_PARAMETER_NAME_SPACE);
					ns.remove(templateParam);

					module.manageId(templateParam, false);

					module.remove(templateParam, IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT);

				}
			}
		}

		// clear the name manager

		nameHelper.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#loadLibrary(java.lang.String,
	 * java.lang.String, java.util.Map, java.net.URL)
	 */
	public Library loadLibrary(String libraryFileName, String namespace, Map<String, Library> reloadLibs, URL url)
			throws DesignFileException {
		namespace = StringUtil.trimString(namespace);

		LayoutModule outermostModule = (LayoutModule) findOutermostModule();

		// find the corresponding library instance

		Library library = null;

		List<Library> libs = outermostModule.getLibrariesWithNamespace(namespace, IAccessControl.ARBITARY_LEVEL);
		if (!libs.isEmpty())
			library = libs.get(0);

		if (library != null && reloadLibs.get(library.getNamespace()) != null) {
			return library.contextClone(this);
		}

		if (url == null) {
			DesignParserException ex = new DesignParserException(new String[] { libraryFileName },
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND);
			List<Exception> exceptionList = new ArrayList<Exception>();
			exceptionList.add(ex);
			throw new DesignFileException(libraryFileName, exceptionList);
		}

		try {
			ModuleOption option = new ModuleOption();

			// pass the original options to the new reader.
			if (options != null && options.getOptions().size() > 0)
				option.setOptions(options.getOptions());
			option.setMarkLineNumber(false);

			library = LibraryReader.getInstance().read(session, this, url, namespace, url.openStream(), option,
					reloadLibs);
			library.setLocation(url);

			if (StringUtil.isBlank(namespace)) {
				library.setNamespace(StringUtil.extractFileName(libraryFileName));
			}
			return library;
		} catch (IOException e) {
			DesignParserException ex = new DesignParserException(new String[] { libraryFileName },
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND);
			List<Exception> exceptionList = new ArrayList<Exception>();
			exceptionList.add(ex);
			throw new DesignFileException(libraryFileName, exceptionList);
		}
	}

	/**
	 * Returns libraries with the given namespace. This method checks the name space
	 * in included libraries within the given depth.
	 * 
	 * @param namespace the library name space
	 * @param level     the depth of the library
	 * @return a list containing libraries
	 * 
	 * @see IModuleNameScope
	 */

	private List<Library> getLibrariesWithNamespace(String namespace, int level) {
		if (libraries == null)
			return Collections.emptyList();

		List<Library> list = getLibraries(level);
		List<Library> retList = new ArrayList<Library>();

		Iterator<Library> iter = list.iterator();
		while (iter.hasNext()) {
			Library library = iter.next();
			if (library.getNamespace().equals(namespace))
				retList.add(library);
		}

		return retList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#loadLibrarySilently(org.eclipse
	 * .birt.report.model.api.elements.structures.IncludedLibrary,
	 * org.eclipse.birt.report.model.elements.Library, java.util.Map, java.net.URL)
	 */
	public void loadLibrarySilently(IncludedLibrary includeLibrary, Library foundLib, Map<String, Library> reloadLibs,
			URL url) {
		if (foundLib != null && reloadLibs.get(includeLibrary.getNamespace()) != null) {
			Library cloned = foundLib.contextClone(this);
			addLibrary(cloned);
			return;
		}

		Library library = null;

		try {
			library = loadLibrary(includeLibrary.getFileName(), includeLibrary.getNamespace(), reloadLibs, url);
			library.setReadOnly();
		} catch (DesignFileException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			Exception fatalException = ModelUtil.getFirstFatalException(e.getExceptionList());

			library = new Library(session, this);
			library.setFatalException(fatalException);
			library.setFileName(includeLibrary.getFileName());
			library.setNamespace(includeLibrary.getNamespace());
			library.setID(library.getNextID());
			library.addElementID(library);
			library.setValid(false);
			library.setAllExceptions(e.getExceptionList());
		}

		addLibrary(library);

		LibraryUtil.insertReloadLibs(reloadLibs, library);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getLibraries(int)
	 */

	public List<Library> getLibraries(int level) {
		if (level <= IAccessControl.NATIVE_LEVEL || libraries == null)
			return Collections.emptyList();

		int newLevel = level - 1;

		// if the new level is less than 0, then no need to do the iterator.

		if (newLevel == IAccessControl.NATIVE_LEVEL)
			return Collections.unmodifiableList(libraries);

		List<Library> allLibraries = new ArrayList<Library>();

		allLibraries.addAll(libraries);

		for (int i = 0; i < libraries.size(); i++) {
			Library library = libraries.get(i);
			allLibraries.addAll(library.getLibraries(newLevel));
		}

		return allLibraries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#insertLibrary(org.eclipse.birt
	 * .report.model.elements.Library, int)
	 */

	public void insertLibrary(Library library, int posn) {
		if (libraries == null)
			libraries = new ArrayList<Library>();

		// The position is allowed to equal the list size.

		assert posn >= 0 && posn <= libraries.size();

		libraries.add(posn, library);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#addLibrary(org.eclipse.birt
	 * .report.model.elements.Library)
	 */

	public void addLibrary(Library library) {
		if (libraries == null)
			libraries = new ArrayList<Library>();

		libraries.add(library);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#dropLibrary(org.eclipse.birt
	 * .report.model.elements.Library)
	 */

	public int dropLibrary(Library library) {
		assert libraries != null;
		assert libraries.contains(library);

		int posn = libraries.indexOf(library);
		libraries.remove(library);

		return posn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getLibraryWithNamespace(java
	 * .lang.String, int)
	 */
	public Library getLibraryWithNamespace(String namespace, int level) {
		if (libraries == null)
			return null;

		List<Library> list = getLibraries(level);

		Iterator<Library> iter = list.iterator();
		while (iter.hasNext()) {
			Library library = iter.next();
			if (library.getNamespace().equals(namespace))
				return library;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getIncludedLibraries()
	 */

	public List<IncludedLibrary> getIncludedLibraries() {
		List<IncludedLibrary> libs = (List<IncludedLibrary>) getLocalProperty(this, LIBRARIES_PROP);
		if (libs == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(libs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getUnits()
	 */
	public String getUnits() {
		if (!StringUtil.isBlank(units))
			return units;
		String tempUnits = (String) getPropertyDefn(UNITS_PROP).getDefault();
		if (!StringUtil.isBlank(tempUnits))
			return tempUnits;
		// see bugzilla 191168.
		return getSession().getUnits();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty
	 * (java.lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (UNITS_PROP.equals(propName)) {
			return units;
		} else if (ISupportThemeElementConstants.THEME_PROP.equals(propName)) {
			return theme;
		}
		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty
	 * (java.lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (UNITS_PROP.equals(propName))
			units = (String) value;
		else if (ISupportThemeElementConstants.THEME_PROP.equals(propName)) {
			ReferenceValueUtil.updateReference(this, theme, (ReferenceValue) value,
					getPropertyDefn(ISupportThemeElementConstants.THEME_PROP));
			theme = (ElementRefValue) value;
		} else
			super.setIntrinsicProperty(propName, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getFatalException()
	 */

	public Exception getFatalException() {
		return fatalException;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#setFatalException(java.lang
	 * .Exception)
	 */

	protected void setFatalException(Exception fatalException) {
		this.fatalException = fatalException;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getTheme()
	 */

	public Theme getTheme() {
		if (theme == null)
			return null;

		return (Theme) theme.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getThemeName()
	 */

	public String getThemeName() {
		if (theme == null)
			return null;
		return theme.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#getTheme(org.eclipse.birt.report
	 * .model.core.Module)
	 */

	public Theme getTheme(Module module) {
		if (theme == null)
			return null;

		if (theme.isResolved())
			return (Theme) theme.getElement();

		ElementRefValue refValue = nameHelper.resolve(null, ReferenceValueUtil.needTheNamespacePrefix(theme, this),
				null, MetaDataDictionary.getInstance().getElement(ReportDesignConstants.THEME_ITEM));

		Theme target = null;
		if (refValue.isResolved()) {
			DesignElement focus = refValue.getElement();

			if (focus instanceof Theme) {
				target = (Theme) focus;
				theme.resolve(target);
				(target).addClient(this, ISupportThemeElementConstants.THEME_PROP);
			}
		}

		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getLibraryByLocation(java.lang
	 * .String, int)
	 */
	public Library getLibraryByLocation(String theLocation, int level) {
		// if the location path is null or empty, return null

		if (StringUtil.isBlank(theLocation))
			return null;

		// look up the library with the location path in the included library
		// list

		List<Library> libraries = getLibraries(level);
		for (int i = 0; i < libraries.size(); i++) {
			Library library = libraries.get(i);
			if (theLocation.equalsIgnoreCase(library.getLocation()))
				return library;
			// do some enhancements for FILE schema
			else if (isSameFile(theLocation, library))
				return library;
		}

		// the library with the given location path is not found, return null

		return null;
	}

	/**
	 * Tests whether the two given objects refer the same logic file in the file
	 * system. Return true if they define the same logic file even though the URL is
	 * not identical. Otherwise false.
	 * 
	 * @param theLocation
	 * @param library
	 * @return
	 */
	private boolean isSameFile(String theLocation, Library library) {
		assert theLocation != null;
		assert library != null;
		URL url_1 = null;
		try {
			url_1 = new URL(theLocation);

			// if the location is not file schema, do nothing and return false
			if (!URIUtil.FILE_SCHEMA.equalsIgnoreCase(url_1.getProtocol()))
				return false;

			URL url_2 = new URL(library.getLocation());
			if (!URIUtil.FILE_SCHEMA.equalsIgnoreCase(url_2.getProtocol()))
				return false;

			String file_name_1 = url_1.getFile();
			File file_1 = new File(file_name_1);
			String logicalPath_1 = file_1.getCanonicalPath();
			String file_name_2 = url_2.getFile();
			File file_2 = new File(file_name_2);
			String logicalPath_2 = file_2.getCanonicalPath();
			if (logicalPath_1 != null && logicalPath_1.equals(logicalPath_2))
				return true;

		} catch (MalformedURLException e) {
			return false;
		} catch (IOException ex) {
			return false;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#findTemplateParameterDefinition
	 * (java.lang.String)
	 */

	public TemplateParameterDefinition findTemplateParameterDefinition(String name) {
		return (TemplateParameterDefinition) resolveNativeElement(name, TEMPLATE_PARAMETER_NAME_SPACE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#isDuplicateNamespace(java.lang
	 * .String)
	 */
	public boolean isDuplicateNamespace(String namespaceToCheck) {
		Module rootHost = this;
		while (rootHost instanceof Library && ((Library) rootHost).getHost() != null)
			rootHost = ((Library) rootHost).getHost();

		// List libraries = rootHost.getAllLibraries( );

		List<Library> libraries = rootHost.getLibraries(IAccessControl.ARBITARY_LEVEL);
		Iterator<Library> iter = libraries.iterator();
		while (iter.hasNext()) {
			Library library = iter.next();

			if (library.getNamespace().equals(namespaceToCheck))
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#rename(org.eclipse.birt.report
	 * .model.api.elements.structures.EmbeddedImage)
	 */
	public void rename(EmbeddedImage image) {
		if (image == null)
			return;
		if (StringUtil.isBlank(image.getName()))
			return;

		List<Object> images = getListProperty(this, IMAGES_PROP);
		if (images == null)
			return;

		// build the embedded image names

		List<String> names = new ArrayList<String>();
		for (int i = 0; i < images.size(); i++) {
			EmbeddedImage theImage = (EmbeddedImage) images.get(i);
			String name = theImage.getName();
			assert !names.contains(name);
			names.add(name);
		}

		// the name of the image to add is not duplicate

		if (!names.contains(image.getName()))
			return;

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String name = image.getName();
		String baseName = image.getName();
		while (names.contains(name)) {
			name = baseName + ++index;
		}
		image.setName(name.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#loadCss(java.lang.String)
	 */

	public CssStyleSheet loadCss(String fileName) throws StyleSheetException {
		try {
			StyleSheetLoader loader = new StyleSheetLoader();
			CssStyleSheet sheet = loader.load(this, fileName);
			List<CssStyle> styles = sheet.getStyles();
			for (int i = 0; styles != null && i < styles.size(); ++i) {
				CssStyle style = styles.get(i);
				style.setCssStyleSheet(sheet);
			}
			return sheet;
		} catch (StyleSheetException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Module#loadCss(org.eclipse.birt.report
	 * .model.core.DesignElement, java.net.URL, java.lang.String)
	 */

	public CssStyleSheet loadCss(DesignElement container, URL url, String fileName) throws StyleSheetException {
		try {
			StyleSheetLoader loader = new StyleSheetLoader();
			CssStyleSheet sheet = loader.load(this, url, fileName);
			sheet.setContainer(container);
			List<CssStyle> styles = sheet.getStyles();
			for (int i = 0; styles != null && i < styles.size(); ++i) {
				CssStyle style = styles.get(i);
				style.setCssStyleSheet(sheet);
			}
			return sheet;
		} catch (StyleSheetException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#cacheValues()
	 */
	public void cacheValues() {
		super.cacheValues();
		List<Library> libs = getAllLibraries();
		for (int i = 0; i < libs.size(); i++) {
			Library lib = libs.get(i);
			lib.nameHelper.cacheValues();
		}
	}

	/**
	 * This method is not supported in report design.
	 * 
	 * @return Object the cloned report design element.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		LayoutModule module = (LayoutModule) super.doClone(policy);

		// clear some attributes

		module.fatalException = null;
		module.referencableProperties = null;
		module.translations = (TranslationTable) translations.clone();

		// clone theme property
		if (theme != null)
			module.theme = new ElementRefValue(theme.getLibraryNamespace(), theme.getName());
		else
			module.theme = null;

		// clone libraries

		if (libraries != null) {
			module.libraries = new ArrayList<Library>();
			for (int i = 0; i < libraries.size(); i++) {
				Library lib = (Library) libraries.get(i).doClone(policy);
				lib.setHost((LayoutModule) module);
				module.libraries.add(lib);
			}
		} else
			module.libraries = null;

		// call semantic check

		module.semanticCheck(module);

		return module;
	}

}
