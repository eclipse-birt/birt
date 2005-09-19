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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.ReadOnlyActivityStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.AttributeEvent;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IAttributeListener;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludeLibrary;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.namespace.IModuleNameSpace;
import org.eclipse.birt.report.model.core.namespace.ModuleNameSpaceFactory;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.TranslationTable;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.validators.ValidationExecutor;
import org.eclipse.birt.report.model.writer.ModuleWriter;

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
	 * Number of defined name spaces.
	 */

	public static final int NAME_SPACE_COUNT = 6;

	/**
	 * The session that owns this module.
	 */

	protected DesignSession session;

	/**
	 * The number of the next element ID.
	 */

	protected int elementIDCounter = 1;

	/**
	 * The hash map for the id-to-element lookup. Created only if ID support has
	 * been enabled in the
	 * {@link org.eclipse.birt.report.model.metadata.MetaDataDictionary MetaDataDictionary}
	 * class.
	 */

	protected HashMap idMap = null;

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
	 * The set of slots for the module.
	 */

	protected ContainerSlot slots[] = null;

	/**
	 * Internal table to store a bunch of user-defined messages. One message can
	 * be defined in several translations, one translation per locale.
	 */

	protected TranslationTable translations = new TranslationTable( );

	/**
	 * The property definition list of all the referencable structure list
	 * property. Each one in the list is instance of <code>IPropertyDefn</code>
	 */

	private List referencableProperties = null;

	/**
	 * Accumulates errors and warnings during a batch operation.
	 */

	protected List allErrors = new ArrayList( );

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
	 * Default constructor.
	 * 
	 * @param theSession
	 *            the session of the report
	 */

	public Module( DesignSession theSession )
	{
		session = theSession;

		for ( int i = 0; i < NAME_SPACE_COUNT; i++ )
		{
			nameSpaces[i] = new NameSpace( );
			moduleNameSpaces[i] = ModuleNameSpaceFactory
					.createElementNameSpace( this, i );
		}

		// Create the id map for id-to-element lookup if IDs were enabled
		// in the data dictionary.

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		if ( dd.useID( ) )
		{
			idMap = new HashMap( );

			// Put this element into the ID map.

			setID( getNextID( ) );
			addElementID( this );
		}
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
		return (StyleElement) resolveElement( name, STYLE_NAME_SPACE );
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
		return resolveElement( name, ELEMENT_NAME_SPACE );
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
		return resolveElement( name, DATA_SOURCE_NAME_SPACE );
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
		return resolveElement( name, DATA_SET_NAME_SPACE );
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
		return resolveElement( name, PAGE_NAME_SPACE );
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
		return resolveElement( name, PARAMETER_NAME_SPACE );
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

	public int getNextID( )
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
		if ( idMap == null )
			return;
		assert element.getID( ) > 0;
		Integer idObj = new Integer( element.getID( ) );
		assert !idMap.containsKey( idObj );
		idMap.put( idObj, element );
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
		Integer idObj = new Integer( element.getID( ) );
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

	public DesignElement getElementByID( int id )
	{
		if ( idMap == null )
			return null;
		return (DesignElement) idMap.get( new Integer( id ) );
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

	public Object clone( ) throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException( );
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

	public String getMessage( String resourceKey, Locale locale )
	{
		if ( StringUtil.isBlank( resourceKey ) )
			return ""; //$NON-NLS-1$

		if ( locale == null )
			locale = ThreadResources.getLocale( );

		// find it in the module itself.

		String msg = translations.getMessage( resourceKey, locale );
		if ( msg != null )
			return msg;

		// find it in the linked resource file.

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return ""; //$NON-NLS-1$

		File msgFolder = getModuleFolder( );
		if ( msgFolder == null )
			return ""; //$NON-NLS-1$

		return BundleHelper.getHelper( msgFolder, baseName ).getMessage(
				resourceKey, locale );
	}

	/**
	 * Return the folder in which the design file is located. The search depend
	 * on the {@link #getFileName()}.
	 * 
	 * @return the folder in which the design file is located. Return
	 *         <code>null</code> if the folder can not be found.
	 */

	private File getModuleFolder( )
	{
		String designPath = getFileName( );
		File designFile = new File( designPath );
		if ( !designFile.exists( ) )
			return null;

		if ( designFile.isFile( ) )
			return designFile.getParentFile( );

		return null;
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
		String namespace = StringUtil.extractNamespace( colorName );
		String name = StringUtil.extractName( colorName );

		Module moduleToSearch = this;
		if ( namespace != null )
			moduleToSearch = getLibraryWithNamespace( namespace );

		if ( moduleToSearch != null )
		{
			ArrayList list = (ArrayList) moduleToSearch.getLocalProperty(
					moduleToSearch, COLOR_PALETTE_PROP );
			if ( list != null )
			{
				for ( int i = 0; i < list.size( ); i++ )
				{
					CustomColor color = (CustomColor) list.get( i );
					if ( color.getName( ).equals( name ) )
						return color;
				}
			}
		}

		return null;
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
		ArrayList list = (ArrayList) getLocalProperty( this, CONFIG_VARS_PROP );
		if ( list != null )
		{
			for ( int i = 0; i < list.size( ); i++ )
			{
				ConfigVariable variable = (ConfigVariable) list.get( i );
				if ( variable.getName( ).equals( variableName ) )
					return variable;
			}
		}

		List theLibraries = getLibraries( );
		int size = theLibraries.size( );
		for ( int i = 0; i < size; i++ )
		{
			Library library = (Library) theLibraries.get( i );
			if ( library.isValid( ) )
			{
				ConfigVariable variable = library
						.findConfigVariabel( variableName );
				if ( variable != null )
					return variable;
			}
		}

		return null;
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
		EmbeddedImage image = findImage( imageName, this );
		if ( image != null )
			return image;

		String namespace = StringUtil.extractNamespace( imageName );
		String name = StringUtil.extractName( imageName );

		if ( namespace == null )
			return null;

		Module moduleToSearch = getLibraryWithNamespace( namespace );

		return findImage( name, moduleToSearch );
	}

	/**
	 * Finds an embedded image by name in the given module.
	 * 
	 * @param imageName
	 *            the name of the image to find
	 * @param moduleToSearch
	 *            the module in which embedded image is searched
	 * @return image if found, otherwise, return null.
	 */

	private EmbeddedImage findImage( String imageName, Module moduleToSearch )
	{
		if ( moduleToSearch == null )
			return null;

		List list = (List) moduleToSearch.getLocalProperty( moduleToSearch,
				IMAGES_PROP );
		if ( list != null )
		{
			for ( int i = 0; i < list.size( ); i++ )
			{
				EmbeddedImage image = (EmbeddedImage) list.get( i );
				if ( image.getName( ) != null
						&& image.getName( ).equals( imageName ) )
					return image;
			}
		}

		return null;
	}

	/**
	 * Gets the property definition list of the structure list type and its
	 * structure can be referred by other elements. Each one in the list is
	 * instance of <code>IElementPropertyDefn</code>.
	 * 
	 * @return the property definition list of the structure list type and its
	 *         structure can be referred by other elements
	 */

	public List getReferencablePropertyDefns( )
	{
		if ( referencableProperties == null )
			referencableProperties = new ArrayList( );
		if ( referencableProperties.size( ) > 0 )
			return referencableProperties;
		referencableProperties.add( getPropertyDefn( CONFIG_VARS_PROP ) );
		referencableProperties.add( getPropertyDefn( COLOR_PALETTE_PROP ) );
		referencableProperties.add( getPropertyDefn( IMAGES_PROP ) );
		return referencableProperties;
	}

	/**
	 * Returns the list of errors accumulated during a batch operation. These
	 * errors can be serious errors or warnings.
	 * 
	 * @return the list of errors or warning
	 */

	public List getAllErrors( )
	{
		return allErrors;
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
		ElementDefn eDefn = (ElementDefn) element.getDefn( );
		String name = StringUtil.trimString( element.getName( ) );

		// Some elements can have a blank name.

		if ( eDefn.getNameOption( ) != MetaDataConstants.REQUIRED_NAME
				&& name == null )
			return;

		// If the element already has a unique name, us it.

		NameSpace nameSpace = getNameSpace( eDefn.getNameSpaceID( ) );
		if ( name != null && !nameSpace.contains( name ) )
			return;

		// If the element has no name, create it as "New<new name>" where
		// "<new name>" is the new element display name for the element. Both
		// "New" and the new element display name are localized to the user's
		// locale.

		if ( name == null )
		{
			// When creating a new report element which requires a name, the
			// default name will be "New" followed by the element name, such as
			// "New Label"; also, if "NewLabel" already exists, then a number
			// will be appended, such as "NewLabel1", etc.

			name = ModelMessages
					.getMessage( MessageConstants.NAME_PREFIX_NEW_MESSAGE );

			name += ModelMessages.getMessage( "New." //$NON-NLS-1$
					+ element.getDefn( ).getName( ) );
			name = name.trim( );
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		while ( nameSpace.contains( name ) )
		{
			name = baseName + ++index; //$NON-NLS-1$
		}
		element.setName( name.trim( ) );
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
	 *            the new file name
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
	 * Privates method to create the slots.
	 */

	protected void initSlots( )
	{
		slots = new ContainerSlot[getSlotCount( )];
		for ( int i = 0; i < slots.length; i++ )
			slots[i] = new MultiElementSlot( );
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
		if ( allErrors == null )
			allErrors = new ArrayList( );
		allErrors.add( ex );
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
		List exceptionList = validateWithContents( module );
		allErrors = ErrorDetail.convertExceptionList( exceptionList );
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

	public DesignElement resolveElement( String elementName, int nameSpace )
	{
		ElementRefValue refValue = moduleNameSpaces[nameSpace]
				.resolve( elementName );
		return refValue.getElement( );
	}

	/**
	 * Sets the error list into this module.
	 * 
	 * @param allErrors
	 *            error list to set
	 */

	public void setAllErrors( List allErrors )
	{
		this.allErrors = allErrors;
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
		try
		{
			File f = new File( fileName );
			if ( f.isAbsolute( ) )
				return f.exists( ) ? f.toURL( ) : null;

			String base = getStringProperty( this, BASE_PROP );
			if ( base != null )
			{
				f = new File( base, fileName );
				if ( f.exists( ) && f.isFile( ) )
					return f.toURL( );
			}
		}
		catch ( MalformedURLException e )
		{
			return null;
		}

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
			Library library = LibraryReader.getInstance( ).read( session,
					url.toString( ), url.openStream( ) );
			library.setNamespace( namespace );
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

	public void loadLibrarySilently( IncludeLibrary includeLibrary )
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
			Library library = new Library( );
			library.setFileName( includeLibrary.getFileName( ) );
			library.setNamespace( includeLibrary.getNamespace( ) );
			library.setValid( false );
			library.setAllErrors( e.getErrorList( ) );
			libraries.add( library );
		}
	}

	/**
	 * Returns all libaries this module contains.
	 * 
	 * @return list of libraries.
	 */

	public List getLibraries( )
	{
		if ( libraries != null )
			return new ArrayList( libraries );

		return Collections.EMPTY_LIST;
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
	 * Returns the module with the given namespace.
	 * 
	 * @param namespace
	 *            the module namespace
	 * @return the module with the given namespace
	 */

	public Library getLibraryWithNamespace( String namespace )
	{
		if ( libraries == null )
			return null;

		Iterator iter = libraries.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = (Library) iter.next( );

			if ( library.getNamespace( ).equals( namespace ) )
				return library;
		}

		return null;
	}

	/**
	 * Updates the style reference using the element in the given library list.
	 * 
	 * @param librariesToUpdate
	 *            library list
	 */

	public void updateStyleClients( List librariesToUpdate )
	{
		int size = librariesToUpdate.size( );
		for ( int i = 0; i < size; i++ )
		{
			updateStyleClients( (Library) libraries.get( i ) );
		}
	}

	/**
	 * Updates the element reference which refers to the given library.
	 * 
	 * @param library
	 *            the library whose element references are updated.
	 */

	public void updateStyleClients( Library library )
	{
		ContainerSlot slot = library.getSlot( Library.STYLE_SLOT );
		Iterator iter = slot.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			assert element instanceof ReferenceableElement;

			ReferenceableElement referenceableElement = (ReferenceableElement) element;

			updateClientReferences( referenceableElement );
		}
	}

	/**
	 * Updates the element reference which refers to the given referenceable
	 * element.
	 * 
	 * @param referred
	 *            the element whose element references are updated
	 */

	private void updateClientReferences( ReferenceableElement referred )
	{
		List clients = referred.getClientList( );
		Iterator iter = clients.iterator( );
		while ( iter.hasNext( ) )
		{
			BackRef ref = (BackRef) iter.next( );
			DesignElement client = ref.element;

			ElementRefValue value = (ElementRefValue) client.getLocalProperty(
					this, ref.propName );
			value.unresolved( value.getName( ) );

			referred.dropClient( client );

			client.resolveElementReference( this, client
					.getPropertyDefn( ref.propName ) );
		}
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
		if ( disposeListeners != null )
		{
			Iterator iter = disposeListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				IDisposeListener listener = (IDisposeListener) iter.next( );

				listener.moduleDisposed( (ModuleHandle) getHandle( this ),
						event );
			}
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

		File msgFolder = getModuleFolder( );
		if ( msgFolder == null )
			return new ArrayList( keys );

		Collection msgKeys = BundleHelper.getHelper( msgFolder, baseName )
				.getMessageKeys( ThreadResources.getLocale( ) );
		keys.addAll( msgKeys );

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

	public List getIncludeLibraries( )
	{
		return new ArrayList( (List) getLocalProperty( this,
				INCLUDE_LIBRARIES_PROP ) );
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
		if ( propName.equals( UNITS_PROP ) )
		{
			return units;
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
		if ( propName.equals( UNITS_PROP ) )
			units = (String) value;
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
}