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
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.ReadOnlyActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.AttributeEvent;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IAttributeListener;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.INameManager;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.namespace.IModuleNameSpace;
import org.eclipse.birt.report.model.core.namespace.ModuleNameScopeFactory;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.TranslationTable;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;
import org.eclipse.birt.report.model.util.StructureRefUtil;
import org.eclipse.birt.report.model.util.VersionControlMgr;
import org.eclipse.birt.report.model.validators.ValidationExecutor;
import org.eclipse.birt.report.model.writer.ModuleWriter;

import com.ibm.icu.util.ULocale;

/**
 * Core representation of the module element. The module contains:
 * <ul>
 * <li>the name spaces for various kinds of elements
 * <li>a set of slots defined
 * <li>the library list
 * </ul>
 * by the derived class.
 * <p>
 * The Design Engine may be used in the web environment in which it is necessary
 * to identify elements using a unique ID separate from their object pointer.
 * The root class maintains the object ID counter, as well as an id-to-element
 * map. Because the map is costly, it is enabled only if ID support is enabled
 * in the data dictionary.
 */

public abstract class Module extends DesignElement implements IModuleModel
{

	/**
	 * Identifier for the shared style name space.
	 */

	public static final int STYLE_NAME_SPACE = 0;

	/**
	 * Identifier of the name space that stores layout elements that appear in
	 * the Body slot.
	 */

	public static final int ELEMENT_NAME_SPACE = 1;

	/**
	 * Identifier for the parameter name space.
	 */

	public static final int PARAMETER_NAME_SPACE = 2;

	/**
	 * Identifier for the data source name space.
	 */

	public static final int DATA_SOURCE_NAME_SPACE = 3;

	/**
	 * Identifier for the data set name space.
	 */

	public static final int DATA_SET_NAME_SPACE = 4;

	/**
	 * Identifier for the master page name space.
	 */

	public static final int PAGE_NAME_SPACE = 5;

	/**
	 * Identifier for the theme name space.
	 */

	public static final int THEME_NAME_SPACE = 6;

	/**
	 * Identifier for the template parameter definition name space.
	 */

	public static final int TEMPLATE_PARAMETER_NAME_SPACE = 7;

	/**
	 * Number of defined name spaces.
	 */

	public static final int NAME_SPACE_COUNT = 8;

	/**
	 * The session that owns this module.
	 */

	protected DesignSession session;

	/**
	 * The number of the next element ID.
	 */

	protected long elementIDCounter = 1;

	/**
	 * The hash map for the id-to-element lookup.
	 */

	protected HashMap idMap = new HashMap( );

	/**
	 * The undo/redo stack for operations on this module.
	 */

	protected ActivityStack activityStack = new ActivityStack( );

	/**
	 * The save state used for dirty file detection. See
	 * {@link org.eclipse.birt.report.model.activity.ActivityStack}for details.
	 */

	protected int saveState = 0;

	/**
	 * The array of name spaces. The meanings of each name space is defined in
	 * {@link org.eclipse.birt.report.model.api.metadata.MetaDataConstants MetaDataConstants}.
	 */

	protected NameSpace nameSpaces[] = new NameSpace[NAME_SPACE_COUNT];

	/**
	 * The array of module name spaces. Each name space has the corresponding
	 * module name space.
	 */

	protected IModuleNameSpace moduleNameSpaces[] = new IModuleNameSpace[NAME_SPACE_COUNT];

	/**
	 * Internal table to store a bunch of user-defined messages. One message can
	 * be defined in several translations, one translation per locale.
	 */

	protected TranslationTable translations = new TranslationTable( );

	/**
	 * The property definition list of all the referencable structure list
	 * property. Each one in the list is instance of <code>IPropertyDefn</code>
	 */

	private HashMap referencableProperties = null;

	/**
	 * Accumulates errors and warnings during a batch operation. Each one is the
	 * instance of <code>Exception</code>.
	 */

	protected List allExceptions = new ArrayList( );

	/**
	 * The validation executor. It performs the semantic validation and sends
	 * validation event to listeners.
	 */

	protected ValidationExecutor validationExecutor = new ValidationExecutor(
			this );

	/**
	 * The listener list for validation event.
	 */

	private List validationListeners = null;

	/**
	 * The file name. Null means that the module has not yet been saved to a
	 * file.
	 */

	protected String fileName = null;

	/**
	 * The system id which is needed for resolving relative URIs. It can be a
	 * relative/absolute file directory or a network path such as HTTP, FTP,
	 * etc.
	 */

	protected URL systemId = null;

	/**
	 * The UTF signature.
	 */

	protected String signature = null;

	/**
	 * The default units for the design.
	 */
	protected String units = null;

	/**
	 * All libraries which are included in this module.
	 */

	private List libraries = null;

	/**
	 * The attribute listener list to handle the file name changed events.
	 */

	private List attributeListeners = null;

	/**
	 * Dispose listener list to handle the design disposal events.
	 */

	private List disposeListeners = null;

	/**
	 * The theme for the module.
	 */

	private ElementRefValue theme = null;

	/**
	 * The fatal exception found in the included modules, which will stop
	 * opening the outer-most module.
	 */

	protected Exception fatalException;

	/**
	 * The registered name manager of this module.
	 */

	protected INameManager nameManager = null;

	/**
	 * The registered API backward compatibility manager of this module.
	 */

	protected VersionControlMgr versionMgr = null;

	/**
	 * Default constructor.
	 * 
	 * @param theSession
	 *            the session of the report
	 */

	protected Module( DesignSession theSession )
	{
		session = theSession;

		for ( int i = 0; i < NAME_SPACE_COUNT; i++ )
		{
			nameSpaces[i] = new NameSpace( );
			moduleNameSpaces[i] = ModuleNameScopeFactory
					.createElementNameSpace( this, i );
		}

		// Put this element into the ID map.

		setID( getNextID( ) );
		addElementID( this );

		// initialize the name manager

		nameManager = new NameManager( this );
		versionMgr = new VersionControlMgr( );
	}

	/**
	 * Returns the design session that represents the designs that the user has
	 * open and user preferences.
	 * 
	 * @return the design session
	 */

	public DesignSession getSession( )
	{
		return session;
	}

	/**
	 * Finds a shared style in this module itself.
	 * 
	 * @param name
	 *            Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public StyleElement findNativeStyle( String name )
	{
		return (StyleElement) nameSpaces[STYLE_NAME_SPACE].getElement( name );
	}

	/**
	 * Finds a shared style in this module and its included modules.
	 * 
	 * @param name
	 *            Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public StyleElement findStyle( String name )
	{
		ElementRefValue refValue = moduleNameSpaces[STYLE_NAME_SPACE].resolve(
				name, null );
		return (StyleElement) refValue.getElement( );
	}

	/**
	 * Finds a named report item in this module and the included modules. This
	 * is for body elements in the element's name space.
	 * 
	 * @param name
	 *            The name of the element to find.
	 * @return The element, or null if the element is not found.
	 */

	public DesignElement findElement( String name )
	{
		return resolveNativeElement( name, ELEMENT_NAME_SPACE );
	}

	/**
	 * Finds a data source by name in this module and the included modules.
	 * 
	 * @param name
	 *            the name of the data source to find.
	 * @return the data source, or null if the data source is not found.
	 */

	public DesignElement findDataSource( String name )
	{
		return resolveElement( name, DATA_SOURCE_NAME_SPACE, null );
	}

	/**
	 * Finds a data set by name in this module and the included modules.
	 * 
	 * @param name
	 *            the name of the data set to find.
	 * @return the data set, or null if the data set is not found.
	 */

	public DesignElement findDataSet( String name )
	{
		return resolveElement( name, DATA_SET_NAME_SPACE, null );
	}

	/**
	 * Finds a master page by name in this module and the included modules.
	 * 
	 * @param name
	 *            the master page name.
	 * @return the master page, if found, otherwise null.
	 */

	public DesignElement findPage( String name )
	{
		return resolveNativeElement( name, PAGE_NAME_SPACE );
	}

	/**
	 * Finds a parameter by name in this module and the included modules.
	 * 
	 * @param name
	 *            The parameter name.
	 * @return The parameter, if found, otherwise null.
	 */

	public DesignElement findParameter( String name )
	{
		return resolveNativeElement( name, PARAMETER_NAME_SPACE );
	}

	/**
	 * Gets a name space.
	 * 
	 * @param ns
	 *            The name space ID.
	 * @return The name space.
	 */

	public NameSpace getNameSpace( int ns )
	{
		assert ns >= 0 && ns < NAME_SPACE_COUNT;
		return nameSpaces[ns];
	}

	/**
	 * Returns the module name space instance for the given name space in this
	 * module.
	 * 
	 * @param nameSpace
	 *            the name space ID
	 * @return the module name space for the given name space
	 */

	public IModuleNameSpace getModuleNameSpace( int nameSpace )
	{
		assert nameSpace >= 0 && nameSpace < NAME_SPACE_COUNT;
		return moduleNameSpaces[nameSpace];
	}

	/**
	 * Returns the next element ID and increments the ID. Used to assign an ID
	 * to new elements.
	 * 
	 * @return The ID to assign to the new element.
	 */

	public long getNextID( )
	{
		return elementIDCounter++;
	}

	/**
	 * Adds an element to the id-to-element map. Does nothing if IDs are not
	 * enabled. Should be called only from the
	 * {@link org.eclipse.birt.report.model.command.ContentCommand ContentCommand}.
	 * 
	 * @param element
	 *            The new element to add.
	 */

	public void addElementID( DesignElement element )
	{
		assert idMap != null;

		assert element.getID( ) > 0;
		Long idObj = new Long( element.getID( ) );
		assert !idMap.containsKey( idObj );
		idMap.put( idObj, element );

		// let elementIDCounter is the current max id

		long id = element.getID( );
		if ( this.elementIDCounter <= id )
		{
			this.elementIDCounter = id + 1;
		}
	}

	/**
	 * Drops an element from the id-to-element map. Does nothing if IDs are not
	 * enabled. Should be called only from the
	 * {@link org.eclipse.birt.report.model.command.ContentCommand ContentCommand}.
	 * 
	 * @param element
	 *            The old element to remove.
	 */

	public void dropElementID( DesignElement element )
	{
		if ( idMap == null )
			return;
		assert element.getID( ) > 0;
		Long idObj = new Long( element.getID( ) );
		assert idMap.containsKey( idObj );
		idMap.remove( idObj );
	}

	/**
	 * Looks up an element given an element ID. Returns null if no element
	 * exists with the given ID. Also returns null if IDs are not enabled. Note:
	 * this method will not find an element that has previously been deleted
	 * from the module, even if that element still exists in memory in
	 * anticipation of issuing an undo of the delete. That is, only valid
	 * elements can be found.
	 * 
	 * @param id
	 *            The id of the element to find.
	 * @return The element itself, or null if the element can't be found or if
	 *         IDs are not enabled.
	 */

	public DesignElement getElementByID( long id )
	{
		if ( idMap == null )
			return null;
		return (DesignElement) idMap.get( new Long( id ) );
	}

	/**
	 * Returns the undo/redo stack for this module.
	 * 
	 * @return The "command" stack.
	 */

	public ActivityStack getActivityStack( )
	{
		return activityStack;
	}

	/**
	 * Preprares to save this module. Sets the modification date.
	 */

	public void prepareToSave( )
	{
	}

	/**
	 * Records a successful save.
	 */

	public void onSave( )
	{
		saveState = activityStack.getCurrentTransNo( );
		nameManager.clear( );
	}

	/**
	 * Reports whether the module has changed since it was created, loaded from
	 * disk, or saved, whichever has occurred most recently.
	 * 
	 * @return true if the in-memory version of the module differs from that on
	 *         disk, false if the two representations are the same
	 */

	public boolean isDirty( )
	{
		return saveState != activityStack.getCurrentTransNo( );
	}

	/**
	 * Called when creating a new module.
	 */

	protected void onCreate( )
	{
		// Force an update when the module is created.
		// This value should be same as the transaction number
		// when ActivityStack is created.

		saveState = 0;
	}

	/**
	 * Close this module.
	 */

	public void close( )
	{
		isValid = false;

		if ( !isReadOnly( ) )
		{
			saveState = activityStack.getCurrentTransNo( );
			session.drop( this );
		}
	}

	/**
	 * This method is not supported in report design.
	 * 
	 * @return Object the cloned report design element.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object doClone( CopyPolicy policy )
			throws CloneNotSupportedException
	{
		Module module = (Module) super.doClone( policy );

		// clear some attributes

		module.activityStack = new ActivityStack( );
		module.allExceptions = null;
		module.attributeListeners = null;
		module.disposeListeners = null;
		module.elementIDCounter = 1;
		module.fatalException = null;
		module.idMap = new HashMap( );
		module.moduleNameSpaces = new IModuleNameSpace[NAME_SPACE_COUNT];
		module.nameSpaces = new NameSpace[NAME_SPACE_COUNT];
		module.nameManager = new NameManager( module );
		module.referencableProperties = null;
		module.saveState = 0;
		module.systemId = null;
		module.translations = (TranslationTable) translations.clone( );
		module.validationExecutor = new ValidationExecutor( module );
		module.validationListeners = null;
		assert module.getID( ) > NO_ID;
		assert module.getElementByID( module.getID( ) ) == null;
		module.addElementID( module );

		// clone module name space and name space

		for ( int i = 0; i < NAME_SPACE_COUNT; i++ )
		{
			module.nameSpaces[i] = new NameSpace( );
			module.moduleNameSpaces[i] = ModuleNameScopeFactory
					.createElementNameSpace( module, i );
		}

		// clone theme property

		if ( theme != null )
			module.theme = new ElementRefValue( theme.getLibraryNamespace( ),
					theme.getName( ) );
		else
			module.theme = null;

		// clone libraries

		if ( libraries != null )
		{
			for ( int i = 0; i < libraries.size( ); i++ )
			{
				module.libraries = new ArrayList( );
				Library lib = (Library) ( (Library) libraries.get( i ) )
						.doClone( policy );
				lib.setHost( module );
				module.libraries.add( lib );
			}
		}
		else
			module.libraries = null;

		// build name space and id map

		IElementDefn defn = module.getDefn( );
		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = module.getSlot( i );

			if ( slot == null )
				continue;

			for ( int pos = 0; pos < slot.getCount( ); pos++ )
			{
				DesignElement innerElement = slot.getContent( pos );
				buildNameSpaceAndIDMap( module, innerElement );
			}
		}

		// call semantic check

		module.semanticCheck( module );

		return module;
	}

	/**
	 * Builds up the namespace and id-map for the cloned module.
	 * 
	 * @param module
	 *            the cloned module to build
	 * @param element
	 *            the element in the module to add into the namespace and id-map
	 */

	private void buildNameSpaceAndIDMap( Module module, DesignElement element )
	{
		if ( module == null || element == null )
			return;
		assert !( element instanceof Module );

		if ( element instanceof TemplateElement )
		{
			TemplateParameterDefinition templateParam = element
					.getTemplateParameterElement( module );
			if ( templateParam != null && templateParam.getRoot( ) != module )
			{
				element.setProperty(
						TemplateElement.REF_TEMPLATE_PARAMETER_PROP,
						new ElementRefValue( null, templateParam.getName( ) ) );
			}
		}

		ElementDefn defn = (ElementDefn) element.getDefn( );

		assert module.getElementByID( element.getID( ) ) == null;
		assert element.getID( ) > NO_ID;
		module.addElementID( element );

		// The name should not be null if it is required. The parser state
		// should have already caught this case.

		String name = element.getName( );
		assert !StringUtil.isBlank( name )
				|| defn.getNameOption( ) != MetaDataConstants.REQUIRED_NAME;

		// Disallow duplicate names.

		assert element.getContainer( ) != null;
		int id = defn.getNameSpaceID( );
		if ( name != null && id != MetaDataConstants.NO_NAME_SPACE
				&& element.isManagedByNameSpace( ) )
		{
			NameSpace ns = module.getNameSpace( id );

			assert !ns.contains( name );
			ns.insert( element );
		}

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = element.getSlot( i );
			assert slot != null;

			for ( int pos = 0; pos < slot.getCount( ); pos++ )
			{
				DesignElement innerElement = slot.getContent( pos );
				buildNameSpaceAndIDMap( module, innerElement );
			}
		}
	}

	/**
	 * Adds a new Translation entry to the module. A report file can reference
	 * message IDs that are defined by the customers. One entry of
	 * <code>Translation</code> represents a translated message for a specific
	 * locale.
	 * <p>
	 * 
	 * @param translation
	 *            new entry of <code>Translation</code> that are to be added
	 *            to the module.
	 */

	public void addTranslation( Translation translation )
	{
		translations.add( translation );
	}

	/**
	 * Drops a Translation from the module.
	 * <p>
	 * 
	 * @param translation
	 *            the translation to be dropped from the module.
	 * 
	 * @return <code>true</code> if the report module contains the given
	 *         translation.
	 */

	public boolean dropTranslation( Translation translation )
	{
		return translations.remove( translation );
	}

	/**
	 * Finds a <code>Translation</code> by the message resource key and the
	 * locale.
	 * <p>
	 * 
	 * @param resourceKey
	 *            resourceKey of the user-defined message where the translation
	 *            is defined in.
	 * @param locale
	 *            locale for the translation. Locale is in java-defined format(
	 *            en, en-US, zh_CN, etc.)
	 * @return the <code>Translation</code> that matches. return null if the
	 *         translation is not found in the report.
	 */

	public Translation findTranslation( String resourceKey, String locale )
	{
		return translations.findTranslation( resourceKey, locale );
	}

	/**
	 * Returns if the specified translation is contained in the translation
	 * table.
	 * 
	 * @param trans
	 *            a given <code>Translation</code>
	 * @return <code>true</code> if the <code>Translation</code> is
	 *         contained in the translation talbe, return <code>false</code>
	 *         otherwise.
	 */

	public boolean contains( Translation trans )
	{
		return translations.contains( trans );
	}

	/**
	 * Returns the whole collection of translations defined for the report
	 * module.
	 * <p>
	 * 
	 * @return a list containing all the Translations.
	 */

	public List getTranslations( )
	{
		return translations.getTranslations( );
	}

	/**
	 * Returns the collection of translations defined for a specific message.
	 * The message is presented by its resourceKey.
	 * <p>
	 * 
	 * @param resourceKey
	 *            resource key for the message.
	 * @return a list containing all the Translations defined for the message.
	 */

	public List getTranslations( String resourceKey )
	{
		return translations.getTranslations( resourceKey );
	}

	/**
	 * Returns a string array containing all the resource keys defined for
	 * messages.
	 * <p>
	 * 
	 * @return a string array containing all the resource keys defined for
	 *         messages return null if there is no messages stored.
	 */

	public String[] getTranslationResourceKeys( )
	{
		return translations.getResourceKeys( );
	}

	/**
	 * Finds user defined messages for the current thread's locale.
	 * 
	 * @param resourceKey
	 *            Resource key of the user defined message.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>""</code> if resoueceKey is blank or the message is not
	 *         found.
	 * @see #getMessage(String, Locale)
	 */

	public String getMessage( String resourceKey )
	{
		return getMessage( resourceKey, ThreadResources.getLocale( ) );
	}

	/**
	 * Finds user-defined messages for the given locale.
	 * <p>
	 * First we look up in the report itself, then look into the referenced
	 * message file. Each search uses a reduced form of Java locale-driven
	 * search algorithm: Language&Country, language, default.
	 * 
	 * @param resourceKey
	 *            Resource key of the user defined message.
	 * @param locale
	 *            locale of message, if the input <code>locale</code> is
	 *            <code>null</code>, the locale for the current thread will
	 *            be used instead.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>""</code> if translation can not be found, or
	 *         <code>resourceKey</code> is blank or <code>null</code>.
	 */

	public String getMessage( String resourceKey, ULocale locale )
	{
		if ( StringUtil.isBlank( resourceKey ) )
			return null;

		if ( locale == null )
			locale = ThreadResources.getLocale( );

		// find it in the module itself.

		String msg = translations.getMessage( resourceKey, locale );
		if ( msg != null )
			return msg;

		// find it in the linked resource file.

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return null;

		// try the resource path first.

		msg = BundleHelper.getHelper( this, baseName ).getMessage( resourceKey,
				locale );

		return msg;
	}

	/**
	 * Return the folder in which the design file is located. The search depend
	 * on the {@link #getFileName()}.
	 * 
	 * @return the folder in which the design file is located. Return
	 *         <code>null</code> if the folder can not be found.
	 * @deprecated since BIRT 2.1. find the message in resource path.
	 */

	private File getModuleFolder( )
	{
		if ( systemId == null )
			return null;

		return new File( systemId.getFile( ) );
	}

	/**
	 * Finds a custom color by name.
	 * 
	 * @param colorName
	 *            the custom color name
	 * @return the custom defined color that matches, or <code>null</code> if
	 *         the color name was not found in the custom color palette.
	 */

	public CustomColor findColor( String colorName )
	{
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance( )
				.getStructure( CustomColor.CUSTOM_COLOR_STRUCT );
		return (CustomColor) StructureRefUtil.findStructure( this, defn,
				colorName );
	}

	/**
	 * Finds a config variable by name
	 * 
	 * @param variableName
	 *            the configure variable name
	 * @return the config variable that matches, or <code>null</code> if the
	 *         variable name was not found.
	 */

	public ConfigVariable findConfigVariabel( String variableName )
	{
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance( )
				.getStructure( ConfigVariable.CONFIG_VAR_STRUCT );
		return (ConfigVariable) StructureRefUtil.findStructure( this, defn,
				variableName );
	}

	/**
	 * Finds an embedded image by name.
	 * 
	 * @param imageName
	 *            the embedded image name
	 * @return the defined image that matches, or <code>null</code> if the
	 *         image name was not found in the embedded images.
	 */

	public EmbeddedImage findImage( String imageName )
	{
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance( )
				.getStructure( EmbeddedImage.EMBEDDED_IMAGE_STRUCT );

		return (EmbeddedImage) StructureRefUtil.findStructure( this, defn,
				imageName );
	}

	/**
	 * Gets the property definition whose detail type is of the given structure
	 * name.
	 * 
	 * @param structureName
	 *            the structure name to search
	 * @return the property definition whose detail type is of the given
	 *         structure name, otherwise null
	 */

	public ElementPropertyDefn getReferencablePropertyDefn( String structureName )
	{
		if ( referencableProperties == null )
		{
			referencableProperties = new HashMap( );
			referencableProperties.put( ConfigVariable.CONFIG_VAR_STRUCT,
					getPropertyDefn( CONFIG_VARS_PROP ) );
			referencableProperties.put( EmbeddedImage.EMBEDDED_IMAGE_STRUCT,
					getPropertyDefn( IMAGES_PROP ) );
			referencableProperties.put( CustomColor.CUSTOM_COLOR_STRUCT,
					getPropertyDefn( COLOR_PALETTE_PROP ) );

		}

		return (ElementPropertyDefn) referencableProperties.get( structureName );
	}

	/**
	 * Returns the list of errors accumulated during a batch operation. These
	 * errors can be serious errors or warnings. Each one is the instance of
	 * <code>ErrorDetail</code>.
	 * 
	 * @return the list of errors or warning
	 */

	public List getAllErrors( )
	{
		return ErrorDetail.convertExceptionList( allExceptions );
	}

	/**
	 * Returns the list of exceptions accumulated during a batch operation. Each
	 * one is the instance of <code>Exception</code>.
	 * 
	 * @return the list of exception
	 */

	public List getAllExceptions( )
	{
		return allExceptions;
	}

	/**
	 * Returns the validation executor.
	 * 
	 * @return the validation executor
	 */

	public ValidationExecutor getValidationExecutor( )
	{
		return validationExecutor;
	}

	/**
	 * Adds one validation listener. The duplicate listener will not be added.
	 * 
	 * @param listener
	 *            the validation listener to add
	 */

	public void addValidationListener( IValidationListener listener )
	{
		if ( validationListeners == null )
			validationListeners = new ArrayList( );

		if ( !validationListeners.contains( listener ) )
			validationListeners.add( listener );
	}

	/**
	 * Removes one validation listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener
	 *            the validation listener to remove
	 * @return <code>true</code> if <code>listener</code> is sucessfully
	 *         removed. Otherwise <code>false</code>.
	 * 
	 */

	public boolean removeValidationListener( IValidationListener listener )
	{
		if ( validationListeners == null )
			return false;
		return validationListeners.remove( listener );
	}

	/**
	 * Broadcasts the validation event to validation listeners.
	 * 
	 * @param element
	 *            the validated element
	 * @param event
	 *            the validation event
	 */

	public void broadcastValidationEvent( DesignElement element,
			ValidationEvent event )
	{
		if ( validationListeners != null )
		{
			Iterator iter = validationListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				IValidationListener listener = (IValidationListener) iter
						.next( );

				listener.elementValidated( element.getHandle( this ), event );
			}
		}
	}

	/**
	 * Makes a unique name for an element. There are several cases.
	 * <p>
	 * <dl>
	 * <dt>Blank name, name is optional</dt>
	 * <dd>Leave the name blank, for some elements the name is optional.</dd>
	 * 
	 * <dt>Blank name, name is required</dt>
	 * <dd>Create a default name of the form "NewTable" where "New" is
	 * localized, and "Table" is the localized element name for creating a new
	 * element.</dd>
	 * 
	 * <dt>Name already exists in the name space</dt>
	 * <dd>This can occur either for the name provided, or for the default name
	 * created above. Add a number suffix to make the name unique. Example:
	 * "MyName4".</dd>
	 * </dl>
	 * 
	 * @param element
	 *            element for which to create a unique name
	 */

	public void makeUniqueName( DesignElement element )
	{
		nameManager.makeUniqueName( element );
	}

	/**
	 * Gets the name manager of this module.
	 * 
	 * @return the name manager of this module
	 */

	public INameManager getNameManager( )
	{
		return nameManager;
	}

	/**
	 * Returns the file name of the module file.
	 * 
	 * @return the module file name. Returns null if the module has not yet been
	 *         saved to a file.
	 */

	public String getFileName( )
	{
		return fileName;
	}

	/**
	 * Sets the module file name. This method is only called by module reader,
	 * it's illegal to be called for other purpose.
	 * 
	 * @param newName
	 *            the new file name. It may contain relative/absolute path
	 *            information. But this name must include the file name with the
	 *            filename extension.
	 */

	public void setFileName( String newName )
	{
		fileName = newName;
	}

	/**
	 * Sets the UTF signature of this module file.
	 * 
	 * @param signature
	 *            the UTF signature of the module file.
	 */

	public void setUTFSignature( String signature )
	{
		this.signature = signature;
	}

	/**
	 * Gets the UTF signature of this module file.
	 * 
	 * @return the UTF signature of the module file.
	 */

	public String getUTFSignature( )
	{
		return signature;
	}

	/**
	 * Returns the number of slots of the module. For the library and the report
	 * design, this number is different.
	 * 
	 * @return the number of slots of the module
	 */

	protected abstract int getSlotCount( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot >= 0 && slot < getSlotCount( );
		return slots[slot];
	}

	/**
	 * Records a semantic error during build and similar batch operations. This
	 * implementation is preliminary.
	 * 
	 * @param ex
	 *            the exception to record
	 */

	public void semanticError( SemanticException ex )
	{
		if ( allExceptions == null )
			allExceptions = new ArrayList( );
		allExceptions.add( ex );
	}

	/**
	 * Returns a list containing all errors during parsing the module file.
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public List getErrorList( )
	{
		List allErrors = getAllErrors( );

		List list = ErrorDetail.getSemanticErrors( allErrors,
				DesignFileException.DESIGN_EXCEPTION_SEMANTIC_ERROR );
		list.addAll( ErrorDetail.getSemanticErrors( allErrors,
				DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR ) );
		return list;
	}

	/**
	 * Returns a list containing warnings during parsing the module file.
	 * 
	 * @return a list containing parsing warnings. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public List getWarningList( )
	{
		List allErrors = getAllErrors( );

		return ErrorDetail.getSemanticErrors( allErrors,
				DesignFileException.DESIGN_EXCEPTION_SEMANTIC_WARNING );
	}

	/**
	 * Performs a semantic check of this element, and all its contained
	 * elements. Records errors in the module context.
	 * <p>
	 * Checks the contents of this element.
	 * 
	 * @param module
	 *            the module information needed for the check, and records any
	 *            errors
	 */

	public final void semanticCheck( Module module )
	{
		allExceptions = validateWithContents( module );

		// delete all useless template parameter definition

		if ( module instanceof ReportDesign )
		{
			ContainerSlot slot = module
					.getSlot( ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT );
			assert slot != null;
			for ( int i = slot.getCount( ) - 1; i >= 0; i-- )
			{
				TemplateParameterDefinition templateParam = (TemplateParameterDefinition) slot
						.getContent( i );
				if ( templateParam.getClientList( ).isEmpty( ) )
				{
					slot.remove( templateParam );

					// Remove the element from the ID map if we are using
					// IDs.

					module.manageId( templateParam, false );

					// Clear the inverse relationship.

					templateParam.setContainer( null, DesignElement.NO_SLOT );
				}
			}
		}

		// clear the name manager

		this.nameManager.clear( );
	}

	/**
	 * Resolves element with the given element name and name space.
	 * <code>propDefn</code> gives the information that how to resolve the
	 * <code>elementName</code>.
	 * 
	 * @param elementName
	 *            name of the element
	 * @param nameSpace
	 *            name space
	 * @param propDefn
	 *            the property definition
	 * @return the resolved element if the name can be resolved, otherwise,
	 *         return null.
	 * 
	 * @see IModuleNameSpace#resolve(String, PropertyDefn)
	 */

	public DesignElement resolveElement( String elementName, int nameSpace,
			PropertyDefn propDefn )
	{
		ElementRefValue refValue = moduleNameSpaces[nameSpace].resolve(
				elementName, propDefn );
		return refValue.getElement( );
	}

	/**
	 * Resolves element with the given element name and name space.
	 * <code>propDefn</code> gives the information that how to resolve the
	 * <code>elementName</code>.
	 * 
	 * @param element
	 *            the element
	 * @param nameSpace
	 *            name space
	 * @param propDefn
	 *            the property definition
	 * @return the resolved element if the name can be resolved, otherwise,
	 *         return null.
	 * 
	 * @see IModuleNameSpace#resolve(String, PropertyDefn)
	 */

	public DesignElement resolveElement( DesignElement element, int nameSpace,
			PropertyDefn propDefn )
	{
		ElementRefValue refValue = moduleNameSpaces[nameSpace].resolve(
				element, propDefn );
		return refValue.getElement( );
	}

	/**
	 * Resolves element with the given element name and name space.
	 * 
	 * @param elementName
	 *            name of the element
	 * @param nameSpace
	 *            name space
	 * @return the resolved element if the name can be resolved, otherwise,
	 *         return null.
	 */

	private DesignElement resolveNativeElement( String elementName,
			int nameSpace )
	{
		NameSpace namespace = nameSpaces[nameSpace];
		return namespace.getElement( elementName );
	}

	/**
	 * Sets the exceltion list into this module.
	 * 
	 * @param allExceptions
	 *            exception list to set
	 */

	protected void setAllExceptions( List allExceptions )
	{
		this.allExceptions = allExceptions;
	}

	/**
	 * Returns the <code>URL</code> object if the file with
	 * <code>fileName</code> exists. This method takes the following search
	 * steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>Search file taking <code>fileName</code> as relative file name and
	 * basing "base" property of module;
	 * <li>Search file with the file locator (<code>IResourceLocator</code>)
	 * in session.
	 * </ul>
	 * 
	 * @param fileName
	 *            file name to search
	 * @param fileType
	 *            file type. The value should be one of:
	 *            <ul>
	 *            <li><code>IResourceLocator.IMAGE</code>
	 *            <li><code>IResourceLocator.LIBRARY</code>
	 *            </ul>
	 *            Any invalid value will be treated as
	 *            <code>IResourceLocator.IMAGE</code>.
	 * @return the <code>URL</code> object if the file with
	 *         <code>fileName</code> is found, or null otherwise.
	 */

	public URL findResource( String fileName, int fileType )
	{
		URL url = getSession( ).getResourceLocator( ).findResource(
				(ModuleHandle) getHandle( this ), fileName, fileType );
		return url;
	}

	/**
	 * Loads library with the given library file name. This file name can be
	 * absolute or relative. If the library doesn't exist or fatal error occurs
	 * when opening library, one invalid library will be added into the library
	 * list of this module.
	 * 
	 * @param libraryFileName
	 *            library file name
	 * @param namespace
	 *            library namespace
	 * @return the loaded library
	 * @throws DesignFileException
	 *             if the library file has fatal error.
	 */

	public Library loadLibrary( String libraryFileName, String namespace )
			throws DesignFileException
	{
		if ( libraries == null )
			libraries = new ArrayList( );

		URL url = findResource( libraryFileName, IResourceLocator.LIBRARY );
		if ( url == null )
		{
			DesignParserException ex = new DesignParserException(
					new String[]{libraryFileName},
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND );
			List exceptionList = new ArrayList( );
			exceptionList.add( ex );
			throw new DesignFileException( libraryFileName, exceptionList );
		}

		try
		{
			Library library = LibraryReader.getInstance( ).read( session, this,
					url.toString( ), namespace, url.openStream( ) );

			if ( StringUtil.isBlank( namespace ) )
			{
				library.setNamespace( StringUtil
						.extractFileName( libraryFileName ) );
			}
			return library;
		}
		catch ( IOException e )
		{
			DesignParserException ex = new DesignParserException(
					new String[]{libraryFileName},
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND );
			List exceptionList = new ArrayList( );
			exceptionList.add( ex );
			throw new DesignFileException( libraryFileName, exceptionList );
		}
	}

	/**
	 * Loads library with given library file name. This method will add library
	 * into this module even if the library file is not found or has fatal
	 * error.
	 * 
	 * @param includeLibrary
	 *            library file name
	 * @see #loadLibrary(String, String)
	 */

	public void loadLibrarySilently( IncludedLibrary includeLibrary )
	{
		try
		{
			Library library = loadLibrary( includeLibrary.getFileName( ),
					includeLibrary.getNamespace( ) );
			library.setReadOnly( );
			libraries.add( library );
		}
		catch ( DesignFileException e )
		{
			Exception fatalException = ModelUtil.getFirstFatalException( e
					.getExceptionList( ) );

			Library library = new Library( session, this );
			library.setFatalException( fatalException );
			library.setFileName( includeLibrary.getFileName( ) );
			library.setNamespace( includeLibrary.getNamespace( ) );
			library.setValid( false );
			library.setAllExceptions( e.getExceptionList( ) );
			libraries.add( library );
		}
	}

	/**
	 * Returns all libaries this module contains.
	 * 
	 * @return list of libraries.
	 */

	public List getAllLibraries( )
	{
		return getLibraries( IModuleNameSpace.ARBITARY_LEVEL );
	}

	/**
	 * Returns included libaries within the given depth.
	 * 
	 * @param level
	 *            the given depth
	 * @return list of libraries.
	 * 
	 * @see IModuleNameSpace
	 */

	public List getLibraries( int level )
	{
		if ( level <= IModuleNameSpace.NATIVE_LEVEL || libraries == null )
			return Collections.EMPTY_LIST;

		int newLevel = level - 1;

		// if the new level is less than 0, then no need to do the iterator.

		if ( newLevel == IModuleNameSpace.NATIVE_LEVEL )
			return Collections.unmodifiableList( libraries );

		List allLibraries = new ArrayList( libraries );
		Iterator iter = new ArrayList( allLibraries ).iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = (Library) iter.next( );
			allLibraries.addAll( library.getLibraries( newLevel ) );
		}

		return allLibraries;
	}

	/**
	 * Returns only libraries this module includes directly.
	 * 
	 * @return list of libraries.
	 */

	public List getLibraries( )
	{
		return getLibraries( IModuleNameSpace.DIRECTLY_INCLUDED_LEVEL );
	}

	/**
	 * Inserts the library to the given position.
	 * 
	 * @param library
	 *            the library to insert
	 * @param posn
	 *            at which the given library is inserted.
	 */

	public void insertLibrary( Library library, int posn )
	{
		if ( libraries == null )
			libraries = new ArrayList( );

		// The position is allowed to equal the list size.

		assert posn >= 0 && posn <= libraries.size( );

		libraries.add( posn, library );
	}

	/**
	 * Adds the given libray to library list.
	 * 
	 * @param library
	 *            the library to insert
	 */

	public void addLibrary( Library library )
	{
		if ( libraries == null )
			libraries = new ArrayList( );

		libraries.add( library );
	}

	/**
	 * Drops the given library from library list.
	 * 
	 * @param library
	 *            the library to drop
	 * @return the position of the library to drop
	 */

	public int dropLibrary( Library library )
	{
		assert libraries != null;
		assert libraries.contains( library );

		int posn = libraries.indexOf( library );
		libraries.remove( library );

		return posn;
	}

	/**
	 * Returns the module with the given namespace. This method checks the
	 * namespace in both directly and indirectly included libraries.
	 * 
	 * @param namespace
	 *            the module namespace
	 * @return the module with the given namespace
	 */

	public Library getLibraryWithNamespace( String namespace )
	{
		return getLibraryWithNamespace( namespace,
				IModuleNameSpace.ARBITARY_LEVEL );
	}

	/**
	 * Returns the module with the given namespace. This method checks the
	 * namespace in included libraries within the given depth.
	 * 
	 * @param namespace
	 *            the module namespace
	 * @param level
	 *            the depth of the library
	 * @return the module with the given namespace
	 * 
	 * @see IModuleNameSpace
	 */

	public Library getLibraryWithNamespace( String namespace, int level )
	{
		if ( libraries == null )
			return null;

		List list = getLibraries( level );

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = (Library) iter.next( );
			if ( library.getNamespace( ).equals( namespace ) )
				return library;
		}

		return null;
	}

	/**
	 * Returns whether this module is read-only.
	 * 
	 * @return true, if this module is read-only. Otherwise, return false.
	 */

	public boolean isReadOnly( )
	{
		return activityStack instanceof ReadOnlyActivityStack;
	}

	/**
	 * Adds one attribute listener. The duplicate listener will not be added.
	 * 
	 * @param listener
	 *            the attribute listener to add
	 */

	public void addAttributeListener( IAttributeListener listener )
	{
		if ( attributeListeners == null )
			attributeListeners = new ArrayList( );

		if ( !attributeListeners.contains( listener ) )
			attributeListeners.add( listener );
	}

	/**
	 * Removes one attribute listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener
	 *            the attribute listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully
	 *         removed. Otherwise <code>false</code>.
	 * 
	 */

	public boolean removeAttributeListener( IAttributeListener listener )
	{
		if ( attributeListeners == null )
			return false;
		return attributeListeners.remove( listener );
	}

	/**
	 * Broadcasts the file name event to the file name listeners.
	 * 
	 * @param event
	 *            the file name event
	 */

	public void broadcastFileNameEvent( AttributeEvent event )
	{
		if ( attributeListeners != null )
		{
			Iterator iter = attributeListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				IAttributeListener listener = (IAttributeListener) iter.next( );

				listener.fileNameChanged( (ModuleHandle) getHandle( this ),
						event );
			}
		}
	}

	/**
	 * Adds one dispose listener. The duplicate listener will not be added.
	 * 
	 * @param listener
	 *            the dispose listener to add
	 */

	public void addDisposeListener( IDisposeListener listener )
	{
		if ( disposeListeners == null )
			disposeListeners = new ArrayList( );

		if ( !disposeListeners.contains( listener ) )
			disposeListeners.add( listener );
	}

	/**
	 * Removes one dispose listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener
	 *            the dispose listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully
	 *         removed. Otherwise <code>false</code>.
	 * 
	 */

	public boolean removeDisposeListener( IDisposeListener listener )
	{
		if ( disposeListeners == null )
			return false;
		return disposeListeners.remove( listener );
	}

	/**
	 * Broadcasts the dispose event to the dispose listeners.
	 * 
	 * @param event
	 *            the dispose event
	 */

	public void broadcastDisposeEvent( DisposeEvent event )
	{
		if ( disposeListeners == null || disposeListeners.isEmpty( ) )
			return;

		List temp = new ArrayList( disposeListeners );
		Iterator iter = temp.iterator( );
		while ( iter.hasNext( ) )
		{
			IDisposeListener listener = (IDisposeListener) iter.next( );
			listener.moduleDisposed( (ModuleHandle) getHandle( this ), event );
		}
	}

	/**
	 * Return a list of user-defined message keys. The list contained resource
	 * keys defined in the report itself and the keys defined in the referenced
	 * message files for the current thread's locale. The list returned contains
	 * no duplicate keys.
	 * 
	 * @return a list of user-defined message keys.
	 */

	public List getMessageKeys( )
	{
		Set keys = new LinkedHashSet( );

		String[] transKeys = translations.getResourceKeys( );
		if ( transKeys != null )
		{
			for ( int i = 0; i < transKeys.length; i++ )
				keys.add( transKeys[i] );
		}

		// find from the referenced message files.
		// e.g: message

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return new ArrayList( keys );

		keys.addAll( BundleHelper.getHelper( this, baseName ).getMessageKeys(
				ThreadResources.getLocale( ) ) );

		return new ArrayList( keys );
	}

	/**
	 * Checks if the file with <code>fileName</code> exists. The search steps
	 * are described in {@link #findResource(String, int)}.
	 * 
	 * @param fileName
	 *            the file name to check
	 * @param fileType
	 *            the file type
	 * @return true if the file exists, false otherwise.
	 */

	public boolean isFileExist( String fileName, int fileType )
	{
		URL url = findResource( fileName, fileType );

		return url != null;
	}

	/**
	 * Gets a list containing all the include libraries.
	 * 
	 * @return a list containing all the include libraries. Return
	 *         <code>null</code> if there were no include libraries defined.
	 */

	public List getIncludedLibraries( )
	{
		return Collections.unmodifiableList( (List) getLocalProperty( this,
				LIBRARIES_PROP ) );
	}

	/**
	 * Gets the default units for the design.
	 * 
	 * @return the default units used in the design
	 */
	public String getUnits( )
	{
		if ( !StringUtil.isBlank( units ) )
			return units;
		return (String) getPropertyDefn( ReportDesign.UNITS_PROP ).getDefault( );
	}

	/**
	 * Gets a list containing all the include scripts.
	 * 
	 * @return a list containing all the include scripts. Return
	 *         <code>null</code> if there were no scripts defined.
	 */
	public List getIncludeScripts( )
	{
		return (ArrayList) getLocalProperty( this, INCLUDE_SCRIPTS_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( UNITS_PROP.equals( propName ) )
		{
			return units;
		}
		else if ( THEME_PROP.equals( propName ) )
		{
			return theme;
		}
		return super.getIntrinsicProperty( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( UNITS_PROP.equals( propName ) )
			units = (String) value;
		else if ( THEME_PROP.equals( propName ) )
		{
			updateReference( theme, (ElementRefValue) value,
					getPropertyDefn( THEME_PROP ) );

			theme = (ElementRefValue) value;
		}
		else
			super.setIntrinsicProperty( propName, value );
	}

	/**
	 * Returns the writer for this moudle.
	 * 
	 * @return the writer for this moudle.
	 */

	public abstract ModuleWriter getWriter( );

	/**
	 * Returns the module handle.
	 * 
	 * @return module handle
	 */

	public ModuleHandle getModuleHandle( )
	{
		return (ModuleHandle) getHandle( this );
	}

	/**
	 * Returns the fatal exception, which means some unrecoverable error is
	 * found in the included libraries.
	 * 
	 * @return the fatal exception
	 */

	public Exception getFatalException( )
	{
		return fatalException;
	}

	/**
	 * Sets the fatal exception.
	 * 
	 * @param fatalException
	 *            the fatal exception to set
	 */

	protected void setFatalException( Exception fatalException )
	{
		this.fatalException = fatalException;
	}

	/**
	 * Returns the system id of the module. It is the relative URI path of the
	 * module.
	 * 
	 * @return the system id of the module
	 */

	public URL getSystemId( )
	{
		return systemId;
	}

	/**
	 * Sets the system id of the module. It is the relative URI path of the
	 * module.
	 * 
	 * @param systemId
	 *            the system id of the module
	 * 
	 */

	public void setSystemId( URL systemId )
	{
		this.systemId = systemId;
	}

	/**
	 * Finds a theme in this module and its included modules.
	 * 
	 * @param name
	 *            Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public Theme findTheme( String name )
	{
		ElementRefValue refValue = moduleNameSpaces[THEME_NAME_SPACE].resolve(
				name, null );
		return (Theme) refValue.getElement( );
	}

	/**
	 * Returns the theme of the report design/library.
	 * 
	 * @return the theme of the report design/library
	 */

	public Theme getTheme( )
	{
		if ( theme == null )
			return null;

		return (Theme) theme.getElement( );
	}

	/**
	 * Gets the name of the referenced theme on this element.
	 * 
	 * @return theme name. null if the theme is not defined on the element.
	 */

	public String getThemeName( )
	{
		if ( theme == null )
			return null;
		return theme.getName( );
	}

	/**
	 * Returns the resolved theme of the report design/library.
	 * 
	 * @param module
	 *            the module to resolve the theme
	 * @return the resolved theme of the report design/library
	 */

	public Theme getTheme( Module module )
	{
		if ( theme == null )
			return null;

		if ( theme.isResolved( ) )
			return (Theme) theme.getElement( );

		IModuleNameSpace resolver = module
				.getModuleNameSpace( Module.THEME_NAME_SPACE );

		ElementRefValue refValue = resolver.resolve( ReferenceValueUtil
				.needTheNamespacePrefix( theme, this ), null );

		Theme target = null;
		if ( refValue.isResolved( ) )
		{
			target = (Theme) refValue.getElement( );

			theme.resolve( target );
			target.addClient( this, THEME_PROP );
		}

		return target;
	}

	/**
	 * Resets the element Id for the content element and its sub elements.
	 * 
	 * @param element
	 *            the element to add
	 * @param isAdd
	 *            whether to add or remove the element id
	 */

	public void manageId( DesignElement element, boolean isAdd )
	{
		if ( element == null )
			return;

		// if the element is hanging and not in the module, return

		if ( element.getRoot( ) != this )
			return;

		IElementDefn defn = element.getDefn( );
		if ( isAdd )
		{
			// the element has no id or a duplicate id, re-allocate another one

			if ( element.getID( ) <= NO_ID
					|| ( getElementByID( element.getID( ) ) != null && getElementByID( element
							.getID( ) ) != element ) )
			{
				element.setID( getNextID( ) );
				assert getElementByID( element.getID( ) ) == null;
			}

			if ( getElementByID( element.getID( ) ) == null )
				addElementID( element );
		}
		else
		{
			dropElementID( element );
		}

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = element.getSlot( i );

			if ( slot == null )
				continue;

			for ( int pos = 0; pos < slot.getCount( ); pos++ )
			{
				DesignElement innerElement = slot.getContent( pos );
				manageId( innerElement, isAdd );
			}
		}
	}

	/**
	 * Gets the location information of the module.
	 * 
	 * @return the location information of the module
	 */

	public String getLocation( )
	{
		assert systemId != null;

		if ( fileName == null )
			return systemId.toString( );
		return systemId + StringUtil.extractFileNameWithSuffix( fileName );
	}

	/**
	 * @param namespace
	 * @return the included library structure with the given namespace
	 */

	public IncludedLibrary findIncludedLibrary( String namespace )
	{
		List libs = getIncludedLibraries( );

		IncludedLibrary includedItem = null;
		for ( int i = 0; i < libs.size( ); i++ )
		{
			IncludedLibrary incluedLib = (IncludedLibrary) libs.get( i );
			if ( incluedLib.getNamespace( ).equalsIgnoreCase( namespace ) )
			{
				includedItem = incluedLib;
				break;
			}
		}
		return includedItem;
	}

	/**
	 * Gets the library with the given location path.
	 * 
	 * @param theLocation
	 *            the location path to find
	 * @return the library with the given location path if found, otherwise null
	 */

	public Library getLibraryByLocation( String theLocation )
	{
		// if the location path is null or empty, return null

		if ( StringUtil.isBlank( theLocation ) )
			return null;

		// look up the library with the location path in the included library
		// list

		List libraries = getLibraries( );
		for ( int i = 0; i < libraries.size( ); i++ )
		{
			Library library = (Library) libraries.get( i );
			if ( theLocation.equalsIgnoreCase( library.getLocation( ) ) )
				return library;
		}

		// the library with the given location path is not found, return null

		return null;
	}

	/**
	 * Finds a template parameter definition by name in this module and the
	 * included modules.
	 * 
	 * @param name
	 *            name of the template parameter definition to find
	 * @return the template parameter definition, if found, otherwise null.
	 */

	public TemplateParameterDefinition findTemplateParameterDefinition(
			String name )
	{
		return (TemplateParameterDefinition) resolveNativeElement( name,
				TEMPLATE_PARAMETER_NAME_SPACE );
	}

	/**
	 * Returns whether the namespace to check is duplicate in target module.
	 * This method helps to judge whether the library to check can be included
	 * in target module.
	 * 
	 * @param namespaceToCheck
	 *            the namespace to check
	 * @return true if the namespace to check is duplicate.
	 */

	public boolean isDuplicateNamespace( String namespaceToCheck )
	{
		Module rootHost = this;
		while ( rootHost instanceof Library
				&& ( (Library) rootHost ).getHost( ) != null )
			rootHost = ( (Library) rootHost ).getHost( );

		// List libraries = rootHost.getAllLibraries( );

		List libraries = rootHost
				.getLibraries( IModuleNameSpace.ARBITARY_LEVEL );
		Iterator iter = libraries.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = (Library) iter.next( );

			if ( library.getNamespace( ).equals( namespaceToCheck ) )
				return true;
		}

		return false;
	}

	/**
	 * Finds the property binding defined in this module, which has the same
	 * property name with the given property name and has the same element id of
	 * the given element.
	 * 
	 * @param element
	 *            the element to find
	 * @param propName
	 *            the property name to find
	 * @return the matched property binding defined in the module, otherwise
	 *         null
	 */

	public PropertyBinding findPropertyBinding( DesignElement element,
			String propName )
	{
		// if element or property name is null, return null

		if ( element == null || propName == null )
			return null;

		// if the property with the given name is not defined on the element,
		// return null

		if ( element.getPropertyDefn( propName ) == null )
			return null;

		// find the property binding in the list, match the property name and
		// element id

		List propertyBindings = getListProperty( this, PROPERTY_BINDINGS_PROP );
		if ( propertyBindings == null )
			return null;
		for ( int i = 0; i < propertyBindings.size( ); i++ )
		{
			PropertyBinding propBinding = (PropertyBinding) propertyBindings
					.get( i );
			BigDecimal id = propBinding.getID( );
			if ( id != null
					&& propName.equalsIgnoreCase( propBinding.getName( ) )
					&& getElementByID( id.longValue( ) ) == element )
				return propBinding;

		}
		return null;
	}

	/**
	 * Gets all the defined property bindings for the given element. Each one in
	 * the list is instance of <code>PropertyBinding</code>.
	 * 
	 * @param element
	 *            the element to find
	 * @return the property binding list defined for the element
	 */

	public List getPropertyBindings( DesignElement element )
	{
		if ( element == null )
			return Collections.EMPTY_LIST;

		List propertyBindings = getListProperty( this, PROPERTY_BINDINGS_PROP );
		if ( propertyBindings == null )
			return Collections.EMPTY_LIST;

		List result = new ArrayList( );
		for ( int i = 0; i < propertyBindings.size( ); i++ )
		{
			PropertyBinding propBinding = (PropertyBinding) propertyBindings
					.get( i );
			BigDecimal id = propBinding.getID( );
			if ( id != null && getElementByID( id.longValue( ) ) == element )
				result.add( propBinding );

		}
		return result;
	}

	/**
	 * Checks the name of the embedded image in this report. If duplicate, get a
	 * unique name and rename it.
	 * 
	 * @param image
	 *            the embedded image whose name is need to check
	 */

	public void rename( EmbeddedImage image )
	{
		if ( image == null )
			return;
		if ( StringUtil.isBlank( image.getName( ) ) )
			return;

		List images = getListProperty( this, Module.IMAGES_PROP );
		if ( images == null )
			return;

		// build the embedded image names

		List names = new ArrayList( );
		for ( int i = 0; i < images.size( ); i++ )
		{
			EmbeddedImage theImage = (EmbeddedImage) images.get( i );
			String name = theImage.getName( );
			assert !names.contains( name );
			names.add( name );
		}

		// the name of the image to add is not duplicate

		if ( !names.contains( image.getName( ) ) )
			return;

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String name = image.getName( );
		String baseName = image.getName( );
		while ( names.contains( name ) )
		{
			name = baseName + ++index;
		}
		image.setName( name.trim( ) );
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
	 * @param element
	 *            the element handle whose name is need to check.
	 */

	public void rename( DesignElement element )
	{
		if ( element == null )
			return;

		IElementDefn defn = element.getDefn( );

		if ( defn.getNameOption( ) == MetaDataConstants.REQUIRED_NAME
				|| element.getRoot( ) instanceof Library
				|| element.getName( ) != null )
			makeUniqueName( element );

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = element.getSlot( i );

			if ( slot != null )
			{
				for ( int pos = 0; pos < slot.getCount( ); pos++ )
				{
					DesignElement innerElement = slot.getContent( pos );
					rename( innerElement );
				}
			}
		}
	}

	/**
	 * Returns the version manager for the API compatibility.
	 * 
	 * @return the version manager
	 */

	public VersionControlMgr getVersionManager( )
	{
		return versionMgr;
	}
}