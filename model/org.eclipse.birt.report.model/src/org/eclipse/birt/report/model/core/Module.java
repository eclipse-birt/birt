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
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.ReadOnlyActivityStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.AttributeEvent;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IAttributeListener;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.namespace.INameContainer;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.StyleSheetLoader;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.TranslationTable;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.LibraryUtil;
import org.eclipse.birt.report.model.util.LineNumberInfo;
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

public abstract class Module extends DesignElement
		implements
			IModuleModel,
			INameContainer
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
	 * Identifier for the cube element name space.
	 */

	public static final int CUBE_NAME_SPACE = 8;

	/**
	 * Number of defined name spaces.
	 */

	public static final int NAME_SPACE_COUNT = 9;

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

	protected HashMap<Long, DesignElement> idMap = new HashMap<Long, DesignElement>( );

	/**
	 * The undo/redo stack for operations on this module.
	 */

	protected ActivityStack activityStack = new ActivityStack( this );

	/**
	 * The save state used for dirty file detection. See
	 * {@link org.eclipse.birt.report.model.activity.ActivityStack}for details.
	 */

	protected int saveState = 0;

	/**
	 * Internal table to store a bunch of user-defined messages. One message can
	 * be defined in several translations, one translation per locale.
	 */

	protected TranslationTable translations = new TranslationTable( );

	/**
	 * The property definition list of all the referencable structure list
	 * property. Each one in the list is instance of <code>IPropertyDefn</code>
	 */

	private HashMap<String, IElementPropertyDefn> referencableProperties = null;

	/**
	 * Accumulates errors and warnings during a batch operation. Each one is the
	 * instance of <code>Exception</code>.
	 */

	protected List<Exception> allExceptions = new ArrayList<Exception>( );

	/**
	 * The validation executor. It performs the semantic validation and sends
	 * validation event to listeners.
	 */

	protected ValidationExecutor validationExecutor = new ValidationExecutor(
			this );

	/**
	 * The listener list for validation event.
	 */

	private List<IValidationListener> validationListeners = null;

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
	 * The absolute path for the design file in URL format. It can be a file
	 * directory or a network path such as HTTP, FTP, etc. It is
	 * <code>null</code> if the file cannot be found.
	 */

	private URL location = null;

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

	private List<Library> libraries = null;

	/**
	 * The attribute listener list to handle the file name changed events.
	 */

	private List<IAttributeListener> attributeListeners = null;

	/**
	 * Dispose listener list to handle the design disposal events.
	 */

	private List<IDisposeListener> disposeListeners = null;

	/**
	 * Resource change listener list to handle the resource change events.
	 */

	private List<IResourceChangeListener> resourceChangeListeners = null;

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
	 * The registered API backward compatibility manager of this module.
	 */

	protected VersionControlMgr versionMgr = null;

	/**
	 * Options set for this module.
	 */

	protected ModuleOption options = null;

	/**
	 * Information member for line numbers.
	 */

	protected LineNumberInfo lineNoInfo = null;

	/**
	 * 
	 */
	protected INameHelper nameHelper = null;

	/**
	 * Caches the bundles. The key is file name, the value is the list of
	 * <code>CachedBundles</code>>.
	 */

	private CachedBundles cachedBundles = null;

	private boolean isCached = false;

	/**
	 * Default constructor.
	 * 
	 * @param theSession
	 *            the session of the report
	 */

	protected Module( DesignSession theSession )
	{
		session = theSession;

		// initialize name helper
		this.nameHelper = new ModuleNameHelper( this );

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
		return (StyleElement) nameHelper.getNameSpace( STYLE_NAME_SPACE )
				.getElement( name );
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
		return (StyleElement) resolveElement( name, null, MetaDataDictionary
				.getInstance( )
				.getElement( ReportDesignConstants.STYLE_ELEMENT ) );
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
		return resolveElement( name, null, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.DATA_SOURCE_ELEMENT ) );
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
		return resolveElement( name, null, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.DATA_SET_ELEMENT ) );
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
	 * Finds a cube element by name in this module and the included modules.
	 * 
	 * @param name
	 *            the element name
	 * @return the cube element, if found, otherwise null
	 */

	public DesignElement findOLAPElement( String name )
	{
		return resolveNativeElement( name, CUBE_NAME_SPACE );
	}

	/**
	 * Finds a level element by name in this module and the included modules.
	 * The name must be the full name; otherwise this method can not find the
	 * level by short name.
	 * 
	 * @param name
	 * @return the level with the given full name
	 */
	public DesignElement findLevel( String name )
	{
		return resolveElement( name, null, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LEVEL_ELEMENT ) );
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
	 * {@link org.eclipse.birt.report.model.command.ContentCommand
	 * ContentCommand}.
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
	 * Adds an id number to the map.
	 * 
	 * @param id
	 *            the id to add
	 */

	public void addElementID( long id )
	{
		if ( this.elementIDCounter <= id )
		{
			this.elementIDCounter = id + 1;
		}
	}

	/**
	 * Drops an element from the id-to-element map. Does nothing if IDs are not
	 * enabled. Should be called only from the
	 * {@link org.eclipse.birt.report.model.command.ContentCommand
	 * ContentCommand}.
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
	 * Initializes the line number hash map.
	 */

	public void initLineNoMap( )
	{
		lineNoInfo = new LineNumberInfo( );
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
		return idMap.get( new Long( id ) );
	}

	/**
	 * Returns the line number.
	 * 
	 * @param obj
	 *            the obj to query the line number, it can be
	 *            <code>DesignElement</code>, or <code>EmbeddedImage</code>, or
	 *            <code>IncludedLibrary</code>
	 * 
	 * @return the line number
	 */

	public int getLineNo( Object obj )
	{
		return lineNoInfo.get( obj );
	}

	/**
	 * Looks up line number of the element in xml source given an element ID.
	 * Returns 1 if no line number of the element exists with the given ID.
	 * 
	 * @param id
	 *            The id of the element to find.
	 * @return The line number of the element given the element id, or 1 if the
	 *         element can't be found or if IDs are not enabled.
	 * @deprecated {@link #getLineNo(Object)}
	 */

	final public int getLineNoByID( long id )
	{
		if ( lineNoInfo == null )
			return 1;

		return lineNoInfo.getElementLineNo( id );
	}

	/**
	 * Adds an object's line number info.
	 * 
	 * @param obj
	 *            the object
	 * @param lineNo
	 *            the line number
	 */

	public void addLineNo( Object obj, Integer lineNo )
	{
		if ( lineNoInfo == null )
			return;

		lineNoInfo.put( obj, lineNo );
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
	 * Prepares to save this module. Sets the modification date.
	 */

	public void prepareToSave( )
	{
		if ( options != null )
		{
			String createdBy = (String) options
					.getProperty( ModuleOption.CREATED_BY_KEY );
			if ( createdBy != null )
				setProperty( Module.CREATED_BY_PROP, createdBy );
		}
	}

	/**
	 * Records a successful save.
	 */

	public void onSave( )
	{
		saveState = activityStack.getCurrentTransNo( );
		nameHelper.clear( );
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
	 * Sets saveState mark.
	 * 
	 * @param saveState
	 *            save state mark
	 */

	public void setSaveState( int saveState )
	{
		this.saveState = saveState;
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

		module.activityStack = new ActivityStack( module );
		module.allExceptions = null;
		module.attributeListeners = null;
		module.disposeListeners = null;
		module.resourceChangeListeners = null;
		module.elementIDCounter = 1;
		module.fatalException = null;
		module.idMap = new HashMap<Long, DesignElement>( );
		module.lineNoInfo = null;
		module.nameHelper = new ModuleNameHelper( module );
		module.referencableProperties = null;
		module.saveState = 0;
		module.translations = (TranslationTable) translations.clone( );
		module.validationExecutor = new ValidationExecutor( module );
		module.validationListeners = null;
		assert module.getID( ) > NO_ID;
		assert module.getElementByID( module.getID( ) ) == null;
		module.addElementID( module );

		// disable the caching, if the original cache is able, we will
		// overwrite doClone in ReportDesign to do the caching
		module.isCached = false;

		// clone theme property
		if ( theme != null )
			module.theme = new ElementRefValue( theme.getLibraryNamespace( ),
					theme.getName( ) );
		else
			module.theme = null;

		// clone libraries

		if ( libraries != null )
		{
			module.libraries = new ArrayList<Library>( );
			for ( int i = 0; i < libraries.size( ); i++ )
			{
				Library lib = (Library) libraries.get( i ).doClone( policy );
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
				element
						.setProperty(
								IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP,
								new ElementRefValue( null, templateParam
										.getFullName( ) ) );
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
			// most element name resides in module, however not all; for
			// example, level resides in dimension. Therefore, we will get name
			// space where the element should reside
			NameSpace ns = new NameExecutor( element ).getNameSpace( module );

			assert !ns.contains( name );
			ns.insert( element );
		}

		if ( defn.isContainer( ) )
		{
			Iterator<DesignElement> iter = new LevelContentIterator( this,
					element, 1 );
			while ( iter.hasNext( ) )
			{
				DesignElement innerElement = iter.next( );
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
	 *            new entry of <code>Translation</code> that are to be added to
	 *            the module.
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
	 * @return <code>true</code> if the <code>Translation</code> is contained in
	 *         the translation table, return <code>false</code> otherwise.
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

	public List<Translation> getTranslations( )
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

	public List<Translation> getTranslations( String resourceKey )
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
	 *            <code>null</code>, the locale for the current thread will be
	 *            used instead.
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

		List<Object> baseNameList = getListProperty( this,
				INCLUDE_RESOURCE_PROP );
		if ( baseNameList == null || baseNameList.size( ) == 0 )
			return null;

		// try the resource path first.

		for ( int i = 0; i < baseNameList.size( ); i++ )
		{
			String baseName = (String) baseNameList.get( i );
			msg = BundleHelper.getHelper( this, baseName ).getMessage(
					resourceKey, locale );
			if ( msg != null )
			{
				return msg;
			}
		}

		return msg;
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
	 * @return the defined image that matches, or <code>null</code> if the image
	 *         name was not found in the embedded images.
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
			referencableProperties = new HashMap<String, IElementPropertyDefn>( );
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

	public List<ErrorDetail> getAllErrors( )
	{
		return ErrorDetail.convertExceptionList( allExceptions );
	}

	/**
	 * Returns the list of exceptions accumulated during a batch operation. Each
	 * one is the instance of <code>Exception</code>.
	 * 
	 * @return the list of exception
	 */

	public List<Exception> getAllExceptions( )
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
			validationListeners = new ArrayList<IValidationListener>( );

		if ( !validationListeners.contains( listener ) )
			validationListeners.add( listener );
	}

	/**
	 * Removes one validation listener. If the listener not registered, then the
	 * request is silently ignored.
	 * 
	 * @param listener
	 *            the validation listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully
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
			Iterator<IValidationListener> iter = validationListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				IValidationListener listener = iter.next( );

				listener.elementValidated( element.getHandle( this ), event );
			}
		}
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
			allExceptions = new ArrayList<Exception>( );
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

	public List<ErrorDetail> getErrorList( )
	{
		List<ErrorDetail> allErrors = getAllErrors( );

		List<ErrorDetail> list = ErrorDetail.getSemanticErrors( allErrors,
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

	public List<ErrorDetail> getWarningList( )
	{
		List<ErrorDetail> allErrors = getAllErrors( );

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
		allExceptions = new ArrayList<Exception>( );
		allExceptions.addAll( validateWithContents( module ) );

		// delete all useless template parameter definition

		if ( module instanceof ReportDesign )
		{
			ContainerSlot slot = module
					.getSlot( IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT );
			assert slot != null;
			for ( int i = slot.getCount( ) - 1; i >= 0; i-- )
			{
				TemplateParameterDefinition templateParam = (TemplateParameterDefinition) slot
						.getContent( i );
				if ( templateParam.getClientList( ).isEmpty( ) )
				{

					// Remove the element from the ID map if we are usingIDs.

					NameSpace ns = nameHelper
							.getNameSpace( TEMPLATE_PARAMETER_NAME_SPACE );
					ns.remove( templateParam );

					module.manageId( templateParam, false );

					module
							.remove(
									templateParam,
									IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT );

				}
			}
		}

		// clear the name manager

		nameHelper.clear( );
	}

	/**
	 * Resolves element with the given element name and name space.
	 * <code>propDefn</code> gives the information that how to resolve the
	 * <code>elementName</code>.
	 * 
	 * @param elementName
	 *            name of the element
	 * @param elementDefn
	 *            the definition of the target element
	 * @param propDefn
	 *            the property definition
	 * @return the resolved element if the name can be resolved, otherwise,
	 *         return null.
	 * 
	 * @see IModuleNameScope#resolve(String, PropertyDefn)
	 */

	public DesignElement resolveElement( String elementName,
			PropertyDefn propDefn, IElementDefn elementDefn )
	{
		ElementRefValue refValue = nameHelper.resolve( elementName, propDefn,
				elementDefn );
		return refValue == null ? null : refValue.getElement( );
	}

	/**
	 * Resolves element with the given element name and name space.
	 * <code>propDefn</code> gives the information that how to resolve the
	 * <code>elementName</code>.
	 * 
	 * @param element
	 *            the element
	 * @param elementDefn
	 *            the definition of the target element
	 * @param propDefn
	 *            the property definition
	 * @return the resolved element if the name can be resolved, otherwise,
	 *         return null.
	 * 
	 * @see IModuleNameScope#resolve(String, PropertyDefn)
	 */

	public DesignElement resolveElement( DesignElement element,
			PropertyDefn propDefn, IElementDefn elementDefn )
	{
		ElementRefValue refValue = nameHelper.resolve( element, propDefn,
				elementDefn );
		return refValue == null ? null : refValue.getElement( );
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
		NameSpace namespace = nameHelper.getNameSpace( nameSpace );
		return namespace.getElement( elementName );
	}

	/**
	 * Sets the exception list into this module.
	 * 
	 * @param allExceptions
	 *            exception list to set
	 */

	protected void setAllExceptions( List<Exception> allExceptions )
	{
		this.allExceptions = allExceptions;
	}

	/**
	 * Returns the <code>URL</code> object if the file with
	 * <code>fileName</code> exists. This method takes the following search
	 * steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>
	 * Search file taking <code>fileName</code> as relative file name and basing
	 * "base" property of module;
	 * <li>Search file with the file locator (<code>
	 * IResourceLocator</code>) in
	 * session.
	 * </ul>
	 * 
	 * @param fileName
	 *            file name to search
	 * @param fileType
	 *            file type. The value should be one of:
	 *            <ul>
	 *            <li><code>IResourceLocator.IMAGE</code>
	 *            <li><code>
	 *            IResourceLocator.LIBRARY</code>
	 *            <li><code>
	 *            IResourceLocator.MESSAGEFILE</code>
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
	 * Returns the <code>URL</code> object if the file with
	 * <code>fileName</code> exists. This method takes the following search
	 * steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>
	 * Search file taking <code>fileName</code> as relative file name and basing
	 * "base" property of module;
	 * <li>Search file with the file locator (<code>
	 * IResourceLocator</code>) in
	 * session.
	 * </ul>
	 * 
	 * @param fileName
	 *            file name to search
	 * @param fileType
	 *            file type. The value should be one of:
	 *            <ul>
	 *            <li><code>IResourceLocator.IMAGE</code>
	 *            <li><code>
	 *            IResourceLocator.LIBRARY</code>
	 *            <li><code>
	 *            IResourceLocator.MESSAGEFILE</code>
	 *            </ul>
	 *            Any invalid value will be treated as
	 *            <code>IResourceLocator.IMAGE</code>.
	 * @param appContext
	 *            The map containing the user's information
	 * @return the <code>URL</code> object if the file with
	 *         <code>fileName</code> is found, or null otherwise.
	 */

	public URL findResource( String fileName, int fileType, Map appContext )
	{
		URL url = getSession( ).getResourceLocator( ).findResource(
				(ModuleHandle) getHandle( this ), fileName, fileType,
				appContext );
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
	 * @param reloadLibs
	 * @param url
	 *            the found library URL
	 * @return the loaded library
	 * @throws DesignFileException
	 *             if the library file has fatal error.
	 */

	public Library loadLibrary( String libraryFileName, String namespace,
			Map<String, Library> reloadLibs, URL url )
			throws DesignFileException
	{
		Module outermostModule = findOutermostModule( );

		// find the corresponding library instance

		Library library = null;

		List<Library> libs = outermostModule.getLibrariesWithNamespace(
				namespace, IAccessControl.ARBITARY_LEVEL );
		if ( !libs.isEmpty( ) )
			library = libs.get( 0 );

		if ( library != null
				&& reloadLibs.get( library.getNamespace( ) ) != null )
		{
			return library.contextClone( this );
		}

		if ( url == null )
		{
			DesignParserException ex = new DesignParserException(
					new String[]{libraryFileName},
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND );
			List<Exception> exceptionList = new ArrayList<Exception>( );
			exceptionList.add( ex );
			throw new DesignFileException( libraryFileName, exceptionList );
		}

		try
		{
			library = LibraryReader.getInstance( ).read( session, this, url,
					namespace, url.openStream( ), null, reloadLibs );
			library.setLocation( url );

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
			List<Exception> exceptionList = new ArrayList<Exception>( );
			exceptionList.add( ex );
			throw new DesignFileException( libraryFileName, exceptionList );
		}
	}

	/**
	 * Returns libraries with the given namespace. This method checks the name
	 * space in included libraries within the given depth.
	 * 
	 * @param namespace
	 *            the library name space
	 * @param level
	 *            the depth of the library
	 * @return a list containing libraries
	 * 
	 * @see IModuleNameScope
	 */

	private List<Library> getLibrariesWithNamespace( String namespace, int level )
	{
		if ( libraries == null )
			return Collections.emptyList( );

		List<Library> list = getLibraries( level );
		List<Library> retList = new ArrayList<Library>( );

		Iterator<Library> iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = iter.next( );
			if ( library.getNamespace( ).equals( namespace ) )
				retList.add( library );
		}

		return retList;
	}

	/**
	 * Loads library with given library file name. This method will add library
	 * into this module even if the library file is not found or has fatal
	 * error.
	 * 
	 * @param includeLibrary
	 *            library file name
	 * @param foundLib
	 *            the matched library
	 * @param reloadLibs
	 *            the map contains reload libraries
	 * @param url
	 *            the found library URL
	 * @see #loadLibrary(String, String)
	 */

	public void loadLibrarySilently( IncludedLibrary includeLibrary,
			Library foundLib, Map<String, Library> reloadLibs, URL url )
	{
		if ( foundLib != null
				&& reloadLibs.get( includeLibrary.getNamespace( ) ) != null )
		{
			Library cloned = foundLib.contextClone( this );
			addLibrary( cloned );
			return;
		}

		Library library = null;

		try
		{
			library = loadLibrary( includeLibrary.getFileName( ),
					includeLibrary.getNamespace( ), reloadLibs, url );
			library.setReadOnly( );
		}
		catch ( DesignFileException e )
		{
			Exception fatalException = ModelUtil.getFirstFatalException( e
					.getExceptionList( ) );

			library = new Library( session, this );
			library.setFatalException( fatalException );
			library.setFileName( includeLibrary.getFileName( ) );
			library.setNamespace( includeLibrary.getNamespace( ) );
			library.setID( library.getNextID( ) );
			library.addElementID( library );
			library.setValid( false );
			library.setAllExceptions( e.getExceptionList( ) );
		}

		addLibrary( library );

		LibraryUtil.insertReloadLibs( reloadLibs, library );
	}

	/**
	 * Returns all libraries this module contains.
	 * 
	 * @return list of libraries.
	 */

	public List<Library> getAllLibraries( )
	{
		return getLibraries( IAccessControl.ARBITARY_LEVEL );
	}

	/**
	 * Returns included libraries within the given depth. Uses the Breadth-First
	 * Search Algorithm.
	 * 
	 * @param level
	 *            the given depth
	 * @return list of libraries.
	 * 
	 * @see IModuleNameScope
	 */

	public List<Library> getLibraries( int level )
	{
		if ( level <= IAccessControl.NATIVE_LEVEL || libraries == null )
			return Collections.emptyList( );

		int newLevel = level - 1;

		// if the new level is less than 0, then no need to do the iterator.

		if ( newLevel == IAccessControl.NATIVE_LEVEL )
			return Collections.unmodifiableList( libraries );

		List<Library> allLibraries = new ArrayList<Library>( );

		allLibraries.addAll( libraries );

		for ( int i = 0; i < libraries.size( ); i++ )
		{
			Library library = libraries.get( i );
			allLibraries.addAll( library.getLibraries( newLevel ) );
		}

		return allLibraries;
	}

	/**
	 * Returns only libraries this module includes directly.
	 * 
	 * @return list of libraries.
	 */

	public List<Library> getLibraries( )
	{
		return getLibraries( IAccessControl.DIRECTLY_INCLUDED_LEVEL );
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
			libraries = new ArrayList<Library>( );

		// The position is allowed to equal the list size.

		assert posn >= 0 && posn <= libraries.size( );

		libraries.add( posn, library );
	}

	/**
	 * Adds the given library to library list.
	 * 
	 * @param library
	 *            the library to insert
	 */

	public void addLibrary( Library library )
	{
		if ( libraries == null )
			libraries = new ArrayList<Library>( );

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
				IAccessControl.ARBITARY_LEVEL );
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
	 * @see IModuleNameScope
	 */

	public Library getLibraryWithNamespace( String namespace, int level )
	{
		if ( libraries == null )
			return null;

		List<Library> list = getLibraries( level );

		Iterator<Library> iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = iter.next( );
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
			attributeListeners = new ArrayList<IAttributeListener>( );

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
			Iterator<IAttributeListener> iter = attributeListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				IAttributeListener listener = iter.next( );

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
			disposeListeners = new ArrayList<IDisposeListener>( );

		if ( !disposeListeners.contains( listener ) )
			disposeListeners.add( listener );
	}

	/**
	 * Adds one resource change listener. The duplicate listener will not be
	 * added.
	 * 
	 * @param listener
	 *            the resource change listener to add
	 */

	public void addResourceChangeListener( IResourceChangeListener listener )
	{
		if ( resourceChangeListeners == null )
			resourceChangeListeners = new ArrayList<IResourceChangeListener>( );

		if ( !resourceChangeListeners.contains( listener ) )
			resourceChangeListeners.add( listener );
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
	 * Removes one resource change listener. If the listener not registered,
	 * then the request is silently ignored.
	 * 
	 * @param listener
	 *            the resource change listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully
	 *         removed. Otherwise <code>false</code>.
	 * 
	 */

	public boolean removeResourceChangeListener(
			IResourceChangeListener listener )
	{
		if ( resourceChangeListeners == null )
			return false;
		return resourceChangeListeners.remove( listener );
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

		List<IDisposeListener> temp = new ArrayList<IDisposeListener>(
				disposeListeners );
		Iterator<IDisposeListener> iter = temp.iterator( );
		while ( iter.hasNext( ) )
		{
			IDisposeListener listener = iter.next( );
			listener.moduleDisposed( (ModuleHandle) getHandle( this ), event );
		}
	}

	/**
	 * Broadcasts the resource change event to the resource change listeners.
	 * 
	 * @param event
	 *            the dispose event
	 */

	public void broadcastResourceChangeEvent( ResourceChangeEvent event )
	{
		if ( resourceChangeListeners == null
				|| resourceChangeListeners.isEmpty( ) )
			return;

		List<IResourceChangeListener> temp = new ArrayList<IResourceChangeListener>(
				resourceChangeListeners );
		Iterator<IResourceChangeListener> iter = temp.iterator( );
		while ( iter.hasNext( ) )
		{
			IResourceChangeListener listener = iter.next( );
			listener.resourceChanged( (ModuleHandle) getHandle( this ), event );
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

	public List<String> getMessageKeys( )
	{
		Set<String> keys = new LinkedHashSet<String>( );

		String[] transKeys = translations.getResourceKeys( );
		if ( transKeys != null )
		{
			for ( int i = 0; i < transKeys.length; i++ )
				keys.add( transKeys[i] );
		}

		// find from the referenced message files.
		// e.g: message

		List<Object> baseNameList = getListProperty( this,
				INCLUDE_RESOURCE_PROP );
		if ( baseNameList == null || baseNameList.size( ) == 0 )
			return new ArrayList<String>( keys );

		for ( int i = 0; i < baseNameList.size( ); i++ )
		{
			String baseName = (String) baseNameList.get( i );
			keys.addAll( BundleHelper.getHelper( this, baseName )
					.getMessageKeys( ThreadResources.getLocale( ) ) );
		}

		return new ArrayList<String>( keys );
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

	public List<IncludedLibrary> getIncludedLibraries( )
	{
		List<IncludedLibrary> libs = (List<IncludedLibrary>) getLocalProperty(
				this, LIBRARIES_PROP );
		if ( libs == null )
			return Collections.emptyList( );
		return Collections.unmodifiableList( libs );
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
		String tempUnits = (String) getPropertyDefn( UNITS_PROP ).getDefault( );
		if ( !StringUtil.isBlank( tempUnits ) )
			return tempUnits;
		// see bugzilla 191168.
		return getSession( ).getUnits( );
	}

	/**
	 * Gets a list containing all the include scripts.
	 * 
	 * @return a list containing all the include scripts. Return
	 *         <code>null</code> if there were no scripts defined.
	 */
	public List<IncludeScript> getIncludeScripts( )
	{
		return (ArrayList<IncludeScript>) getLocalProperty( this,
				INCLUDE_SCRIPTS_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty
	 * (java.lang.String)
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
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty
	 * (java.lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( UNITS_PROP.equals( propName ) )
			units = (String) value;
		else if ( THEME_PROP.equals( propName ) )
		{
			ReferenceValueUtil.updateReference( this, theme,
					(ReferenceValue) value, getPropertyDefn( THEME_PROP ) );
			theme = (ElementRefValue) value;
		}
		else
			super.setIntrinsicProperty( propName, value );
	}

	/**
	 * Returns the writer for this module.
	 * 
	 * @return the writer for this module.
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
		return (Theme) resolveElement( name, null, MetaDataDictionary
				.getInstance( ).getElement( ReportDesignConstants.THEME_ITEM ) );
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

		ElementRefValue refValue = nameHelper.resolve( ReferenceValueUtil
				.needTheNamespacePrefix( theme, this ), null,
				MetaDataDictionary.getInstance( ).getElement(
						ReportDesignConstants.THEME_ITEM ) );

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

		if ( element instanceof ContentElement )
			return;

		// if the element is hanging and not in the module, return

		if ( element.getRoot( ) != this )
			return;
		if ( isAdd )
		{
			// the element has no id or a duplicate id, re-allocate another one

			if ( element.getID( ) <= NO_ID
					|| ( getElementByID( element.getID( ) ) != null && getElementByID( element
							.getID( ) ) != element ) )
			{
				element.setID( getNextID( ) );
			}

			if ( getElementByID( element.getID( ) ) == null )
				addElementID( element );
		}
		else
		{
			dropElementID( element );
		}

		Iterator<DesignElement> iter = new LevelContentIterator( this, element,
				1 );
		while ( iter.hasNext( ) )
		{
			DesignElement innerElement = iter.next( );
			manageId( innerElement, isAdd );
		}
	}

	/**
	 * Gets the location information of the module.
	 * 
	 * @return the location information of the module
	 */

	public String getLocation( )
	{
		if ( location == null )
			return null;

		return location.toExternalForm( );
	}

	/**
	 * Sets the location information of the module.
	 * 
	 * @param location
	 *            the location information of the module
	 */

	public void setLocation( URL location )
	{
		this.location = location;
	}

	/**
	 * @param namespace
	 * @return the included library structure with the given namespace
	 */

	public IncludedLibrary findIncludedLibrary( String namespace )
	{
		List<IncludedLibrary> libs = getIncludedLibraries( );
		if ( libs == null )
			return null;

		IncludedLibrary includedItem = null;
		for ( int i = 0; i < libs.size( ); i++ )
		{
			IncludedLibrary incluedLib = libs.get( i );
			if ( incluedLib.getNamespace( ).equalsIgnoreCase( namespace ) )
			{
				includedItem = incluedLib;
				break;
			}
		}
		return includedItem;
	}

	/**
	 * Gets the library with the given location path in native level.
	 * 
	 * @param theLocation
	 *            the location path to find
	 * @return the library with the given location path if found, otherwise null
	 */

	public Library getLibraryByLocation( String theLocation )
	{
		return getLibraryByLocation( theLocation,
				IAccessControl.DIRECTLY_INCLUDED_LEVEL );
	}

	/**
	 * Gets the library with the given location path in given level.
	 * 
	 * @param theLocation
	 *            the location path to find
	 * @param level
	 *            the depth of the library
	 * @return the library with the given location path if found, otherwise null
	 */

	public Library getLibraryByLocation( String theLocation, int level )
	{
		// if the location path is null or empty, return null

		if ( StringUtil.isBlank( theLocation ) )
			return null;

		// look up the library with the location path in the included library
		// list

		List<Library> libraries = getLibraries( level );
		for ( int i = 0; i < libraries.size( ); i++ )
		{
			Library library = libraries.get( i );
			if ( theLocation.equalsIgnoreCase( library.getLocation( ) ) )
				return library;
		}

		// the library with the given location path is not found, return null

		return null;
	}

	/**
	 * Gets the library with the given location path in given level.
	 * 
	 * @param theLocation
	 *            the location path to find
	 * @param level
	 *            the depth of the library
	 * @return the library with the given location path if found, otherwise null
	 */

	public List<Library> getLibrariesByLocation( String theLocation, int level )
	{
		// if the location path is null or empty, return null

		if ( StringUtil.isBlank( theLocation ) )
			return Collections.emptyList( );

		// look up the library with the location path in the included library
		// list

		List<Library> retList = new ArrayList<Library>( );
		List<Library> libraries = getLibraries( level );
		for ( int i = 0; i < libraries.size( ); i++ )
		{
			Library library = libraries.get( i );
			if ( theLocation.equalsIgnoreCase( library.getLocation( ) ) )
				retList.add( library );
		}

		// the library with the given location path is not found, return null

		return retList;
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

		List<Library> libraries = rootHost
				.getLibraries( IAccessControl.ARBITARY_LEVEL );
		Iterator<Library> iter = libraries.iterator( );
		while ( iter.hasNext( ) )
		{
			Library library = iter.next( );

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

		List<Object> propertyBindings = getListProperty( this,
				PROPERTY_BINDINGS_PROP );
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

	public List<PropertyBinding> getPropertyBindings( DesignElement element )
	{
		if ( element == null )
			return Collections.emptyList( );

		List<Object> propertyBindings = getListProperty( this,
				PROPERTY_BINDINGS_PROP );
		if ( propertyBindings == null )
			return Collections.emptyList( );

		List<PropertyBinding> result = new ArrayList<PropertyBinding>( );
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

		List<Object> images = getListProperty( this, IMAGES_PROP );
		if ( images == null )
			return;

		// build the embedded image names

		List<String> names = new ArrayList<String>( );
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
		nameHelper.rename( element );
	}

	/**
	 * Recursively changes the element name in the context of the container.
	 * 
	 * <ul>
	 * <li>If the element name is required and duplicated name is found rename
	 * the element with a new unique name.
	 * <li>If the element name is not required, clear the name.
	 * </ul>
	 * 
	 * @param container
	 *            the container of the element
	 * @param element
	 *            the element handle whose name is need to check.
	 */

	public void rename( DesignElement container, DesignElement element )
	{
		NameExecutor executor = new NameExecutor( element );
		INameHelper nameHelper = executor.getNameHelper( this, container );
		if ( nameHelper != null )
		{
			nameHelper.makeUniqueName( element );
		}

		LevelContentIterator iter = new LevelContentIterator( this, element, 1 );
		while ( iter.hasNext( ) )
		{
			DesignElement innerElement = iter.next( );
			rename( element, innerElement );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameContainer#makeUniqueName
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void makeUniqueName( DesignElement element )
	{
		nameHelper.makeUniqueName( element );
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

	/**
	 * Gets the options set in the module or any one of its host.
	 * 
	 * @return the options
	 */

	abstract public ModuleOption getOptions( );

	/**
	 * Sets the options in this module.
	 * 
	 * @param options
	 *            the options to set
	 */

	public void setOptions( ModuleOption options )
	{
		this.options = options;
	}

	/**
	 * Sets the resource folder for this session.
	 * 
	 * @param resourceFolder
	 *            the folder to set
	 */

	public void setResourceFolder( String resourceFolder )
	{
		if ( options == null )
			options = new ModuleOption( );
		options.setResourceFolder( resourceFolder );
	}

	/**
	 * Gets the resource folder set in this session.
	 * 
	 * @return the resource folder set in this session
	 */

	public String getResourceFolder( )
	{
		ModuleOption effectOptions = getOptions( );
		if ( effectOptions == null )
			return null;
		return effectOptions.getResourceFolder( );
	}

	/**
	 * Sets the module is read-only one. That means any operation on it will
	 * throw runtime exception.
	 */

	public void setReadOnly( )
	{
		activityStack = new ReadOnlyActivityStack( this );
	}

	/**
	 * Returns the module namespace. Only included library has a non-empty
	 * namespace.
	 * 
	 * @return the module namespace
	 */

	public String getNamespace( )
	{
		return null;
	}

	/**
	 * Loads css with the given css file name. This file name can be absolute or
	 * relative. If the css doesn't exist or fatal error occurs when opening
	 * css,.
	 * 
	 * @param fileName
	 *            css file name
	 * @return the loaded css
	 * @throws StyleSheetException
	 */

	public CssStyleSheet loadCss( String fileName ) throws StyleSheetException
	{
		try
		{
			StyleSheetLoader loader = new StyleSheetLoader( );
			CssStyleSheet sheet = loader.load( this, fileName );
			List<CssStyle> styles = sheet.getStyles( );
			for ( int i = 0; styles != null && i < styles.size( ); ++i )
			{
				CssStyle style = styles.get( i );
				style.setCssStyleSheet( sheet );
			}
			return sheet;
		}
		catch ( StyleSheetException e )
		{
			throw e;
		}
	}

	/**
	 * Loads css with the given css file name. This file name can be absolute or
	 * relative. If the css doesn't exist or fatal error occurs when opening
	 * css,.
	 * 
	 * @param container
	 *            report design/theme
	 * @param url
	 *            the url where the style sheet resides
	 * @param fileName
	 *            css file name
	 * @return the loaded css
	 * @throws StyleSheetException
	 */

	public CssStyleSheet loadCss( DesignElement container, URL url,
			String fileName ) throws StyleSheetException
	{
		try
		{
			StyleSheetLoader loader = new StyleSheetLoader( );
			CssStyleSheet sheet = loader.load( this, url, fileName );
			sheet.setContainer( container );
			List<CssStyle> styles = sheet.getStyles( );
			for ( int i = 0; styles != null && i < styles.size( ); ++i )
			{
				CssStyle style = styles.get( i );
				style.setCssStyleSheet( sheet );
			}
			return sheet;
		}
		catch ( StyleSheetException e )
		{
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameContainer#getNameHelper
	 * ()
	 */
	public INameHelper getNameHelper( )
	{
		return this.nameHelper;
	}

	/**
	 * Returns the root module that contains this library. The return value can
	 * be report or library.
	 * 
	 * @return the root module
	 */

	public Module findOutermostModule( )
	{
		return this;
	}

	/**
	 * Determines whether the module has cached values.
	 * 
	 * @return <code>true</code> if values have been cached. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isCached( )
	{
		return isCached;
	}

	/**
	 * Sets cache status of the module. The value is set to TRUE when calling {
	 * {@link ReportDesignHandle#cacheValues()}.
	 * 
	 * @param isCached
	 */
	public void setIsCached( boolean isCached )
	{
		this.isCached = isCached;
	}

	/**
	 * Caches values for the element. The caller must be the report design.
	 */

	public void cacheValues( )
	{
		nameHelper.cacheValues( );
		List<Library> libs = getAllLibraries( );
		for ( int i = 0; i < libs.size( ); i++ )
		{
			Library lib = libs.get( i );
			lib.nameHelper.cacheValues( );
		}
	}

	/**
	 * Gets all the design elements that resides in the id-map. All the element
	 * in the returned list resides in the design tree and has unique id.
	 * 
	 * @return
	 */
	public List<DesignElement> getAllElements( )
	{
		List<DesignElement> elements = new ArrayList<DesignElement>( );
		elements.addAll( idMap.values( ) );
		return elements;
	}

	/**
	 * Caches the propertyResourceBundle list.
	 * 
	 * @param baseName
	 *            the file name
	 * @param bundleList
	 *            the propertyResouceBundle list
	 */

	public CachedBundles getResourceBundle( )
	{
		if ( getOptions( ) == null
				|| ( getOptions( ) != null && getOptions( ).useSemanticCheck( ) ) )
		{
			return null;
		}

		if ( cachedBundles == null )
			cachedBundles = new CachedBundles( );

		return cachedBundles;
	}
}
