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

package org.eclipse.birt.report.model.core;

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
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.namespace.INameContainer;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.TranslationTable;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.LineNumberInfo;
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

public abstract class ModuleImpl extends DesignElement implements IModuleModel, INameContainer, ISupportThemeElement {

	/**
	 * Identifier for the shared style name space.
	 */
	public static final String STYLE_NAME_SPACE = "style";

	/**
	 * Identifier of the name space that stores layout elements that appear in the
	 * Body slot.
	 */
	public static final String ELEMENT_NAME_SPACE = "element";

	/**
	 * Identifier for the parameter name space.
	 */
	public static final String PARAMETER_NAME_SPACE = "parameter";

	/**
	 * Identifier for the data source name space.
	 */
	public static final String DATA_SOURCE_NAME_SPACE = "datasource";

	/**
	 * Identifier for the data set name space.
	 */
	public static final String DATA_SET_NAME_SPACE = "dataset";

	/**
	 * Identifier for the master page name space.
	 */
	public static final String PAGE_NAME_SPACE = "page";

	/**
	 * Identifier for the theme name space.
	 */
	public static final String THEME_NAME_SPACE = "theme";

	/**
	 * Identifier for the template parameter definition name space.
	 */
	public static final String TEMPLATE_PARAMETER_NAME_SPACE = "template";

	/**
	 * Identifier for the cube element name space.
	 */
	public static final String CUBE_NAME_SPACE = "cube";

	/**
	 * Identifier for the variable element name space.
	 */
	public static final String VARIABLE_ELEMENT_NAME_SPACE = "variable";

	/**
	 * Identifier for the dimension element name space.
	 */
	public static final String DIMENSION_NAME_SPACE = "dimension";

	/**
	 * The session that owns this module.
	 */
	protected DesignSessionImpl session;

	/**
	 * Name helper for this module. Generally, all the module will define its name
	 * helper, such as report design, library and data mart and so on.
	 */
	protected INameHelper nameHelper = null;

	/**
	 * The number of the next element ID.
	 */
	protected long elementIDCounter = 1;

	/**
	 * The hash map for the id-to-element lookup.
	 */
	protected HashMap<Long, DesignElement> idMap = new HashMap<>();

	/**
	 * Information member for line numbers.
	 */
	protected LineNumberInfo lineNoInfo = null;

	/**
	 * The undo/redo stack for operations on this module.
	 */

	protected ActivityStack activityStack = new ActivityStack(getModule());

	/**
	 * The save state used for dirty file detection. See
	 * {@link org.eclipse.birt.report.model.activity.ActivityStack}for details.
	 */

	protected int saveState = 0;

	/**
	 * The validation executor. It performs the semantic validation and sends
	 * validation event to listeners.
	 */

	protected ValidationExecutor validationExecutor = new ValidationExecutor(getModule());

	/**
	 * The listener list for validation event.
	 */

	protected List<IValidationListener> validationListeners = null;

	/**
	 * The file name. Null means that the module has not yet been saved to a file.
	 */
	protected String fileName = null;

	/**
	 * The UTF signature.
	 */
	protected String signature = null;

	/**
	 * Accumulates errors and warnings during a batch operation. Each one is the
	 * instance of <code>Exception</code>.
	 */
	protected List<Exception> allExceptions = new ArrayList<>();

	/**
	 * Dispose listener list to handle the design disposal events.
	 */
	protected List<IDisposeListener> disposeListeners = null;

	/**
	 * Resource change listener list to handle the resource change events.
	 */
	protected List<IResourceChangeListener> resourceChangeListeners = null;

	/**
	 * The system id which is needed for resolving relative URIs. It can be a
	 * relative/absolute file directory or a network path such as HTTP, FTP, etc.
	 */

	protected URL systemId = null;

	/**
	 * The absolute path for the design file in URL format. It can be a file
	 * directory or a network path such as HTTP, FTP, etc. It is <code>null</code>
	 * if the file cannot be found.
	 */

	protected URL location = null;

	/**
	 * Options set for this module.
	 */

	protected ModuleOption options = null;

	/**
	 * Status that justifies whether this module does some cache for performance
	 * improvement or not. It will be set to TRUE when calling
	 * {@link #cacheValues()}.
	 */
	protected boolean isCached = false;

	/**
	 * Caches the bundles. The key is file name, the value is the list of
	 * <code>CachedBundles</code>>.
	 */
	private CachedBundles cachedBundles = null;

	/**
	 * The registered API backward compatibility manager of this module.
	 */
	protected VersionControlMgr versionMgr = null;

	/**
	 * The attribute listener list to handle the file name changed events.
	 */
	protected List<IAttributeListener> attributeListeners = null;

	/**
	 * Default constructor.
	 *
	 * @param theSession the session of the report
	 */

	protected ModuleImpl(DesignSessionImpl theSession) {
		session = theSession;
		versionMgr = new VersionControlMgr();
	}

	/**
	 * Returns the design session that represents the designs that the user has open
	 * and user preferences.
	 *
	 * @return the design session
	 */

	public DesignSessionImpl getSession() {
		return session;
	}

	abstract protected Module getModule();

	/**
	 * Finds a data source by name in this module and the included modules.
	 *
	 * @param name the name of the data source to find.
	 * @return the data source, or null if the data source is not found.
	 */

	public final DesignElement findDataSource(String name) {
		return resolveElement(null, name, null,
				MetaDataDictionary.getInstance().getElement(ReportDesignConstants.DATA_SOURCE_ELEMENT));
	}

	/**
	 * Finds a data set by name in this module and the included modules.
	 *
	 * @param name the name of the data set to find.
	 * @return the data set, or null if the data set is not found.
	 */

	public final DesignElement findDataSet(String name) {
		return resolveElement(null, name, null,
				MetaDataDictionary.getInstance().getElement(ReportDesignConstants.DATA_SET_ELEMENT));
	}

	/**
	 * Finds a cube element by name in this module and the included modules.
	 * Dimensions have their own namespaces. DO NOT call this method.
	 *
	 * @param name the element name
	 * @return the cube element, if found, otherwise null
	 */

	public final DesignElement findOLAPElement(String name) {
		return resolveNativeElement(name, CUBE_NAME_SPACE);
	}

	/**
	 * Finds a OLAP dimension element by name in this module.
	 *
	 * @param name the element name
	 * @return the cube element, if found, otherwise null
	 */

	public final Dimension findDimension(String name) {
		return (Dimension) resolveNativeElement(name, DIMENSION_NAME_SPACE);
	}

	/**
	 * Finds a level element by name in this module and the included modules. The
	 * name must be the full name; otherwise this method can not find the level by
	 * short name.
	 *
	 * @param name
	 * @return the level with the given full name
	 */
	public final DesignElement findLevel(String name) {
		return resolveElement(null, name, null,
				MetaDataDictionary.getInstance().getElement(ReportDesignConstants.LEVEL_ELEMENT));
	}

	/**
	 * Returns the next element ID and increments the ID. Used to assign an ID to
	 * new elements.
	 *
	 * @return The ID to assign to the new element.
	 */

	public final long getNextID() {
		return elementIDCounter++;
	}

	/**
	 * Adds an element to the id-to-element map. Does nothing if IDs are not
	 * enabled. Should be called only from the
	 * {@link org.eclipse.birt.report.model.command.ContentCommand ContentCommand}.
	 *
	 * @param element The new element to add.
	 */

	public final void addElementID(DesignElement element) {
		assert idMap != null;

		assert element.getID() > 0;
		Long idObj = element.getID();
		assert !idMap.containsKey(idObj);
		idMap.put(idObj, element);

		// let elementIDCounter is the current max id

		long id = element.getID();
		if (this.elementIDCounter <= id) {
			this.elementIDCounter = id + 1;
		}
	}

	/**
	 * Adds an id number to the map.
	 *
	 * @param id the id to add
	 */

	public final void addElementID(long id) {
		if (this.elementIDCounter <= id) {
			this.elementIDCounter = id + 1;
		}
	}

	/**
	 * Drops an element from the id-to-element map. Does nothing if IDs are not
	 * enabled. Should be called only from the
	 * {@link org.eclipse.birt.report.model.command.ContentCommand ContentCommand}.
	 *
	 * @param element The old element to remove.
	 */

	public final void dropElementID(DesignElement element) {
		if (idMap == null) {
			return;
		}
		assert element.getID() > 0;
		Long idObj = element.getID();
		assert idMap.containsKey(idObj) && idMap.get(idObj) == element;
		idMap.remove(idObj);
	}

	/**
	 * Initializes the line number hash map.
	 */

	public final void initLineNoMap() {
		lineNoInfo = new LineNumberInfo(getModule());
	}

	/**
	 * Looks up an element given an element ID. Returns null if no element exists
	 * with the given ID. Also returns null if IDs are not enabled. Note: this
	 * method will not find an element that has previously been deleted from the
	 * module, even if that element still exists in memory in anticipation of
	 * issuing an undo of the delete. That is, only valid elements can be found.
	 *
	 * @param id The id of the element to find.
	 * @return The element itself, or null if the element can't be found or if IDs
	 *         are not enabled.
	 */

	public final DesignElement getElementByID(long id) {
		if (idMap == null) {
			return null;
		}
		return idMap.get(Long.valueOf(id));
	}

	/**
	 * Returns the line number.
	 *
	 * @param obj the obj to query the line number, it can be
	 *            <code>DesignElement</code>, or <code>EmbeddedImage</code>, or
	 *            <code>IncludedLibrary</code>
	 *
	 * @return the line number
	 */

	public final int getLineNo(Object obj) {
		return lineNoInfo.get(obj);
	}

	/**
	 * Looks up line number of the element in xml source given an element ID.
	 * Returns 1 if no line number of the element exists with the given ID.
	 *
	 * @param id The id of the element to find.
	 * @return The line number of the element given the element id, or 1 if the
	 *         element can't be found or if IDs are not enabled.
	 * @deprecated {@link #getLineNo(Object)}
	 */

	@Deprecated
	final public int getLineNoByID(long id) {
		if (lineNoInfo == null) {
			return 1;
		}

		return lineNoInfo.getElementLineNo(id);
	}

	/**
	 * Adds an object's line number info.
	 *
	 * @param obj    the object
	 * @param lineNo the line number
	 */

	public final void addLineNo(Object obj, Integer lineNo) {
		if (lineNoInfo == null) {
			return;
		}

		lineNoInfo.put(obj, lineNo);
	}

	/**
	 * Returns the undo/redo stack for this module.
	 *
	 * @return The "command" stack.
	 */

	public final ActivityStack getActivityStack() {
		return activityStack;
	}

	/**
	 * Prepares to save this module. Sets the modification date.
	 */

	public final void prepareToSave() {
		if (options != null) {
			String createdBy = (String) options.getProperty(ModuleOption.CREATED_BY_KEY);
			if (createdBy != null) {
				setProperty(ModuleImpl.CREATED_BY_PROP, createdBy);
			}
		}
	}

	/**
	 * Records a successful save.
	 */

	public final void onSave() {
		saveState = activityStack.getCurrentTransNo();
		nameHelper.clear();
	}

	/**
	 * Reports whether the module has changed since it was created, loaded from
	 * disk, or saved, whichever has occurred most recently.
	 *
	 * @return true if the in-memory version of the module differs from that on
	 *         disk, false if the two representations are the same
	 */

	public final boolean isDirty() {
		return saveState != activityStack.getCurrentTransNo();
	}

	/**
	 * Sets saveState mark.
	 *
	 * @param saveState save state mark
	 */

	public final void setSaveState(int saveState) {
		this.saveState = saveState;
	}

	/**
	 * Called when creating a new module.
	 */

	protected final void onCreate() {
		// Force an update when the module is created.
		// This value should be same as the transaction number
		// when ActivityStack is created.

		saveState = 0;
	}

	/**
	 * Close this module.
	 */

	public final void close() {
		isValid = false;

		if (!isReadOnly()) {
			saveState = activityStack.getCurrentTransNo();
			session.drop(getModule());
		}
	}

	/**
	 * Tidy unnecessary references or data.
	 *
	 * @since 4.7
	 */
	public void tidy() {
		if (options != null) {
			options.close();
		}
	}

	/**
	 * This method is not supported in report design.
	 *
	 * @return Object the cloned report design element.
	 *
	 * @see java.lang.Object#clone()
	 */

	@Override
	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		Module module = ((ModuleImpl) super.doClone(policy)).getModule();

		// clear some attributes

		module.activityStack = new ActivityStack(module);
		module.allExceptions = null;
		module.attributeListeners = null;
		module.disposeListeners = null;
		module.resourceChangeListeners = null;
		module.elementIDCounter = 1;
		module.idMap = new HashMap<>();
		module.lineNoInfo = null;
		module.nameHelper = new ModuleNameHelper(module);
		module.saveState = 0;
		module.validationExecutor = new ValidationExecutor(module);
		module.validationListeners = null;

		// set ModuleOption to null
		// handle module options
		if (options != null) {
			module.setOptions(((ModuleOption) options.copy()));
		}
		assert module.getID() > NO_ID;
		assert module.getElementByID(module.getID()) == null;
		module.addElementID(module);

		// disable the caching, if the original cache is able, we will
		// overwrite doClone in ReportDesign to do the caching
		module.isCached = false;

		// build name space and id map

		IElementDefn defn = module.getDefn();

		// slots
		List<DesignElement> unhandledElements = new ArrayList<>();
		List<TabularDimension> unhandledCubeDimensions = new ArrayList<>();

		Iterator<ISlotDefn> slots = ((ElementDefn) defn).slotsIterator();
		while (slots.hasNext()) {
			ISlotDefn iSlotDefn = slots.next();

			ContainerSlot slot = module.getSlot(iSlotDefn.getSlotID());

			if (slot == null) {
				continue;
			}

			for (int pos = 0; pos < slot.getCount(); pos++) {
				DesignElement innerElement = slot.getContent(pos);
				if (innerElement.canDynamicExtends()) {
					if (!unhandledElements.contains(innerElement)) {
						unhandledElements.add(innerElement);
					}
					continue;
				} else if (innerElement instanceof Cube) {
					Cube cube = (Cube) innerElement;
					List dimensions = cube.getListProperty(module, ICubeModel.DIMENSIONS_PROP);
					if (dimensions != null) {
						for (int i = 0; i < dimensions.size(); i++) {
							Dimension dimension = (Dimension) dimensions.get(i);
							if (dimension instanceof TabularDimension) {
								TabularDimension tabularDimension = (TabularDimension) dimension;
								if (tabularDimension.hasSharedDimension(module)) {
									unhandledCubeDimensions.add(tabularDimension);
								}
							}
						}
					}
				}
				buildNameSpaceAndIDMap(module, innerElement);
			}
		}

		// handle container properties
		List<IElementPropertyDefn> properties = defn.getContents();
		for (int i = 0; i < properties.size(); i++) {
			IElementPropertyDefn propDefn = properties.get(i);
			if (propDefn.isList()) {
				List<DesignElement> innerElements = (List<DesignElement>) module.getProperty(module,
						(ElementPropertyDefn) propDefn);
				if (innerElements != null && !innerElements.isEmpty()) {
					for (int j = 0; j < innerElements.size(); j++) {
						DesignElement innerElement = innerElements.get(j);
						buildNameSpaceAndIDMap(module, innerElement);
					}
				}
			} else {
				DesignElement innerElement = (DesignElement) module.getProperty(module, (ElementPropertyDefn) propDefn);
				buildNameSpaceAndIDMap(module, innerElement);
			}
		}

		// handle unhandledelement list
		for (DesignElement element : unhandledElements) {
			buildNameSpaceAndIDMap(module, element);
		}

		// handle cube dimension that refers shared dimension
		for (TabularDimension element : unhandledCubeDimensions) {
			element.updateLayout(module);
			buildNameSpaceAndIDMap(module, element);
		}

		return module;
	}

	/**
	 * Builds up the namespace and id-map for the cloned module.
	 *
	 * @param module  the cloned module to build
	 * @param element the element in the module to add into the namespace and id-map
	 */

	private void buildNameSpaceAndIDMap(Module module, DesignElement element) {
		if (module == null || element == null) {
			return;
		}
		assert !(element instanceof ModuleImpl);

		if (element instanceof TemplateElement) {
			TemplateParameterDefinition templateParam = element.getTemplateParameterElement(module);
			if (templateParam != null && templateParam.getRoot() != module) {
				element.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP,
						new ElementRefValue(null, templateParam.getFullName()));
			}
		}

		ElementDefn defn = (ElementDefn) element.getDefn();

		assert module.getElementByID(element.getID()) == null || element.isVirtualElement()
				|| element.canDynamicExtends();
		assert element.getID() > NO_ID;
		if (module.getElementByID(element.getID()) == null) {
			module.addElementID(element);
		} else if (element.canDynamicExtends()) {
			assert module.getElementByID(element.getID()) == element;
		} else {
			element.setID(module.getNextID());
			module.addElementID(element);
		}

		// The name should not be null if it is required. The parser state
		// should have already caught this case.

		String name = element.getName();
		assert !StringUtil.isBlank(name) || defn.getNameOption() != MetaDataConstants.REQUIRED_NAME;

		// Disallow duplicate names.

		assert element.getContainer() != null;
		if (name != null && element.isManagedByNameSpace()) {
			NameExecutor executor = new NameExecutor(module, element);
			if (executor.hasNamespace()) {
				// most element name resides in module, however not all; for
				// example, level resides in dimension. Therefore, we will get name
				// space where the element should reside
				NameSpace ns = executor.getNameSpace();
				assert !ns.contains(name);
				ns.insert(element);
			}
		}

		if (element.isContainer()) {
			Iterator<DesignElement> iter = new LevelContentIterator(module, element, 1);
			while (iter.hasNext()) {
				DesignElement innerElement = iter.next();
				if (innerElement instanceof ContentElement) {
					continue;
				}
				buildNameSpaceAndIDMap(module, innerElement);
			}
		}
	}

	/**
	 * Finds user defined messages for the current thread's locale.
	 *
	 * @param resourceKey Resource key of the user defined message.
	 * @return the corresponding locale-dependent messages. Return <code>""</code>
	 *         if resoueceKey is blank or the message is not found.
	 * @see #getMessage(String, Locale)
	 */

	public final String getMessage(String resourceKey) {
		return getMessage(resourceKey, getLocale());
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
	 *                    <code>null</code>, the locale for the current thread will
	 *                    be used instead.
	 * @return the corresponding locale-dependent messages. Return <code>""</code>
	 *         if translation can not be found, or <code>resourceKey</code> is blank
	 *         or <code>null</code>.
	 */

	public final String getMessage(String resourceKey, ULocale locale) {
		if (StringUtil.isBlank(resourceKey)) {
			return null;
		}

		if (locale == null) {
			locale = getLocale();
		}

		// find it in the module itself.

		TranslationTable table = getTranslationTable();
		String msg = null;

		if (table != null) {
			msg = table.getMessage(resourceKey, locale);
			if (msg != null) {
				return msg;
			}
		}

		// find it in the linked resource file.

		List<Object> baseNameList = getIncludedResources();
		if (baseNameList == null || baseNameList.size() == 0) {
			return null;
		}

		// try the resource path first.

		for (int i = 0; i < baseNameList.size(); i++) {
			String baseName = (String) baseNameList.get(i);
			msg = BundleHelper.getHelper(getModule(), baseName).getMessage(resourceKey, locale);
			if (msg != null) {
				return msg;
			}
		}

		return msg;
	}

	/**
	 * Finds all included resources in current module
	 *
	 * @return 4.7
	 */
	public List<Object> getIncludedResources() {
		return getListProperty(getModule(), INCLUDE_RESOURCE_PROP);
	}

	/**
	 * Returns the list of errors accumulated during a batch operation. These errors
	 * can be serious errors or warnings. Each one is the instance of
	 * <code>ErrorDetail</code>.
	 *
	 * @return the list of errors or warning
	 */

	public final List<ErrorDetail> getAllErrors() {
		return ErrorDetail.convertExceptionList(allExceptions);
	}

	/**
	 * Returns the list of exceptions accumulated during a batch operation. Each one
	 * is the instance of <code>Exception</code>.
	 *
	 * @return the list of exception
	 */

	public final List<Exception> getAllExceptions() {
		return allExceptions;
	}

	/**
	 * Returns the validation executor.
	 *
	 * @return the validation executor
	 */

	public final ValidationExecutor getValidationExecutor() {
		return validationExecutor;
	}

	/**
	 * Adds one validation listener. The duplicate listener will not be added.
	 *
	 * @param listener the validation listener to add
	 */

	public final void addValidationListener(IValidationListener listener) {
		if (validationListeners == null) {
			validationListeners = new ArrayList<>();
		}

		if (!validationListeners.contains(listener)) {
			validationListeners.add(listener);
		}
	}

	/**
	 * Removes one validation listener. If the listener not registered, then the
	 * request is silently ignored.
	 *
	 * @param listener the validation listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully removed.
	 *         Otherwise <code>false</code>.
	 *
	 */

	public final boolean removeValidationListener(IValidationListener listener) {
		if (validationListeners == null) {
			return false;
		}
		return validationListeners.remove(listener);
	}

	/**
	 * Broadcasts the validation event to validation listeners.
	 *
	 * @param element the validated element
	 * @param event   the validation event
	 */

	public final void broadcastValidationEvent(DesignElement element, ValidationEvent event) {
		if (validationListeners != null) {
			Iterator<IValidationListener> iter = validationListeners.iterator();
			while (iter.hasNext()) {
				IValidationListener listener = iter.next();

				listener.elementValidated(element.getHandle(getModule()), event);
			}
		}
	}

	/**
	 * Returns the file name of the module file.
	 *
	 * @return the module file name. Returns null if the module has not yet been
	 *         saved to a file.
	 */

	public final String getFileName() {
		return fileName;
	}

	/**
	 * Sets the module file name. This method is only called by module reader, it's
	 * illegal to be called for other purpose.
	 *
	 * @param newName the new file name. It may contain relative/absolute path
	 *                information. But this name must include the file name with the
	 *                filename extension.
	 */

	public final void setFileName(String newName) {
		fileName = newName;
	}

	/**
	 * Sets the UTF signature of this module file.
	 *
	 * @param signature the UTF signature of the module file.
	 */

	public final void setUTFSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * Gets the UTF signature of this module file.
	 *
	 * @return the UTF signature of the module file.
	 */

	public final String getUTFSignature() {
		return signature;
	}

	/**
	 * Returns the number of slots of the module. For the library and the report
	 * design, this number is different.
	 *
	 * @return the number of slots of the module
	 */

	protected abstract int getSlotCount();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	@Override
	public final ContainerSlot getSlot(int slot) {
		assert slot >= 0;

		int index = getSlotIndex(slot);
		if (index == -1) {
			return null;
		}

		return slots[index];
	}

	/**
	 * Records a semantic error during build and similar batch operations. This
	 * implementation is preliminary.
	 *
	 * @param ex the exception to record
	 */

	public final void semanticError(SemanticException ex) {
		if (allExceptions == null) {
			allExceptions = new ArrayList<>();
		}
		allExceptions.add(ex);
	}

	/**
	 * Returns a list containing all errors during parsing the module file.
	 *
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 *
	 * @see ErrorDetail
	 */

	public final List<ErrorDetail> getErrorList() {
		List<ErrorDetail> allErrors = getAllErrors();

		List<ErrorDetail> list = ErrorDetail.getSemanticErrors(allErrors,
				DesignFileException.DESIGN_EXCEPTION_SEMANTIC_ERROR);
		list.addAll(ErrorDetail.getSemanticErrors(allErrors, DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR));
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

	public final List<ErrorDetail> getWarningList() {
		List<ErrorDetail> allErrors = getAllErrors();

		return ErrorDetail.getSemanticErrors(allErrors, DesignFileException.DESIGN_EXCEPTION_SEMANTIC_WARNING);
	}

	/**
	 * Performs a semantic check of this element, and all its contained elements.
	 * Records errors in the module context.
	 * <p>
	 * Checks the contents of this element.
	 *
	 * @param module the module information needed for the check, and records any
	 *               errors
	 */

	public void semanticCheck(Module module) {
		allExceptions = new ArrayList<>();
		allExceptions.addAll(validateWithContents(module));
	}

	/**
	 * Resolves element with the given element name and name space.
	 * <code>propDefn</code> gives the information that how to resolve the
	 * <code>elementName</code>.
	 *
	 * @param elementName name of the element
	 * @param elementDefn the definition of the target element
	 * @param propDefn    the property definition
	 * @return the resolved element if the name can be resolved, otherwise, return
	 *         null.
	 *
	 * @see IModuleNameScope#resolve(String, PropertyDefn)
	 */

	public final DesignElement resolveElement(DesignElement element, String elementName, PropertyDefn propDefn,
			IElementDefn elementDefn) {
		ElementRefValue refValue = nameHelper.resolve(element, elementName, propDefn, elementDefn);
		return refValue == null ? null : refValue.getElement();
	}

	/**
	 * Resolves element with the given element name and name space.
	 * <code>propDefn</code> gives the information that how to resolve the
	 * <code>elementName</code>.
	 *
	 * @param element     the element
	 * @param elementDefn the definition of the target element
	 * @param propDefn    the property definition
	 * @return the resolved element if the name can be resolved, otherwise, return
	 *         null.
	 *
	 * @see IModuleNameScope#resolve(String, PropertyDefn)
	 */

	public final DesignElement resolveElement(DesignElement focus, DesignElement element, PropertyDefn propDefn,
			IElementDefn elementDefn) {
		ElementRefValue refValue = nameHelper.resolve(focus, element, propDefn, elementDefn);
		return refValue == null ? null : refValue.getElement();
	}

	/**
	 * Resolves element with the given element name and name space.
	 *
	 * @param elementName name of the element
	 * @param nameSpace   name space
	 * @return the resolved element if the name can be resolved, otherwise, return
	 *         null.
	 */

	protected final DesignElement resolveNativeElement(String elementName, String nameSpace) {
		NameSpace namespace = nameHelper.getNameSpace(nameSpace);
		return namespace.getElement(elementName);
	}

	/**
	 * Sets the exception list into this module.
	 *
	 * @param allExceptions exception list to set
	 */

	protected final void setAllExceptions(List<Exception> allExceptions) {
		this.allExceptions = allExceptions;
	}

	/**
	 * Returns the <code>URL</code> object if the file with <code>fileName</code>
	 * exists. This method takes the following search steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>Search file taking <code>fileName</code> as relative file name and basing
	 * "base" property of module;
	 * <li>Search file with the file locator (<code>
	 * IResourceLocator</code>) in session.
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
	 *                 Any invalid value will be treated as
	 *                 <code>IResourceLocator.IMAGE</code>.
	 * @return the <code>URL</code> object if the file with <code>fileName</code> is
	 *         found, or null otherwise.
	 */

	public final URL findResource(String fileName, int fileType) {
		ModuleOption options = getOptions();

		URL url = getSession().getResourceLocator().findResource((ModuleHandle) getHandle(getModule()), fileName,
				fileType, options == null ? null : options.getOptions());
		return url;
	}

	/**
	 * Returns the <code>URL</code> object if the file with <code>fileName</code>
	 * exists. This method takes the following search steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>Search file taking <code>fileName</code> as relative file name and basing
	 * "base" property of module;
	 * <li>Search file with the file locator (<code>
	 * IResourceLocator</code>) in session.
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
	 *                   Any invalid value will be treated as
	 *                   <code>IResourceLocator.IMAGE</code>.
	 * @param appContext The map containing the user's information
	 * @return the <code>URL</code> object if the file with <code>fileName</code> is
	 *         found, or null otherwise.
	 */

	public final URL findResource(String fileName, int fileType, Map appContext) {
		URL url = getSession().getResourceLocator().findResource((ModuleHandle) getHandle(getModule()), fileName,
				fileType, appContext);
		return url;
	}

	/**
	 * Returns whether this module is read-only.
	 *
	 * @return true, if this module is read-only. Otherwise, return false.
	 */

	public final boolean isReadOnly() {
		return activityStack instanceof ReadOnlyActivityStack;
	}

	/**
	 * Adds one dispose listener. The duplicate listener will not be added.
	 *
	 * @param listener the dispose listener to add
	 */

	public final void addDisposeListener(IDisposeListener listener) {
		if (disposeListeners == null) {
			disposeListeners = new ArrayList<>();
		}

		if (!disposeListeners.contains(listener)) {
			disposeListeners.add(listener);
		}
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
		if (disposeListeners == null) {
			return false;
		}
		return disposeListeners.remove(listener);
	}

	/**
	 * Adds one resource change listener. The duplicate listener will not be added.
	 *
	 * @param listener the resource change listener to add
	 */

	public final void addResourceChangeListener(IResourceChangeListener listener) {
		if (resourceChangeListeners == null) {
			resourceChangeListeners = new ArrayList<>();
		}

		if (!resourceChangeListeners.contains(listener)) {
			resourceChangeListeners.add(listener);
		}
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
		if (resourceChangeListeners == null) {
			return false;
		}
		return resourceChangeListeners.remove(listener);
	}

	/**
	 * Broadcasts the dispose event to the dispose listeners.
	 *
	 * @param event the dispose event
	 */

	public final void broadcastDisposeEvent(DisposeEvent event) {
		if (disposeListeners == null || disposeListeners.isEmpty()) {
			return;
		}

		List<IDisposeListener> temp = new ArrayList<>(disposeListeners);
		Iterator<IDisposeListener> iter = temp.iterator();
		while (iter.hasNext()) {
			IDisposeListener listener = iter.next();
			listener.moduleDisposed((ModuleHandle) getHandle(getModule()), event);
		}
	}

	/**
	 * Broadcasts the resource change event to the resource change listeners.
	 *
	 * @param event the dispose event
	 */

	public final void broadcastResourceChangeEvent(ResourceChangeEvent event) {
		if (resourceChangeListeners == null || resourceChangeListeners.isEmpty()) {
			return;
		}

		List<IResourceChangeListener> temp = new ArrayList<>(resourceChangeListeners);
		Iterator<IResourceChangeListener> iter = temp.iterator();
		while (iter.hasNext()) {
			IResourceChangeListener listener = iter.next();
			listener.resourceChanged((ModuleHandle) getHandle(getModule()), event);
		}
	}

	/**
	 * Return a list of user-defined message keys. The list contained resource keys
	 * defined in the report itself and the keys defined in the referenced message
	 * files for the current thread's locale. The list returned contains no
	 * duplicate keys.
	 *
	 * @return a list of user-defined message keys.
	 */

	public final List<String> getMessageKeys() {
		Set<String> keys = new LinkedHashSet<>();

		String[] transKeys = getTranslationResourceKeys();
		if (transKeys != null) {
			for (int i = 0; i < transKeys.length; i++) {
				keys.add(transKeys[i]);
			}
		}

		// find from the referenced message files.
		// e.g: message

		List<Object> baseNameList = getListProperty(getModule(), INCLUDE_RESOURCE_PROP);
		if (baseNameList == null || baseNameList.size() == 0) {
			return new ArrayList<>(keys);
		}

		for (int i = 0; i < baseNameList.size(); i++) {
			String baseName = (String) baseNameList.get(i);
			keys.addAll(BundleHelper.getHelper(getModule(), baseName).getMessageKeys(getLocale()));
		}

		return new ArrayList<>(keys);
	}

	/**
	 * Checks if the file with <code>fileName</code> exists. The search steps are
	 * described in {@link #findResource(String, int)}.
	 *
	 * @param fileName the file name to check
	 * @param fileType the file type
	 * @return true if the file exists, false otherwise.
	 */

	public final boolean isFileExist(String fileName, int fileType) {
		URL url = findResource(fileName, fileType);

		return url != null;
	}

	/**
	 * Returns the writer for this module.
	 *
	 * @return the writer for this module.
	 */

	public abstract ModuleWriter getWriter();

	/**
	 * Returns the module handle.
	 *
	 * @return module handle
	 */

	public final ModuleHandle getModuleHandle() {
		return (ModuleHandle) getHandle(getModule());
	}

	/**
	 * Returns the system id of the module. It is the relative URI path of the
	 * module.
	 *
	 * @return the system id of the module
	 */

	public final URL getSystemId() {
		return systemId;
	}

	/**
	 * Sets the system id of the module. It is the relative URI path of the module.
	 *
	 * @param systemId the system id of the module
	 *
	 */

	public final void setSystemId(URL systemId) {
		this.systemId = systemId;
	}

	/**
	 * Resets the element Id for the content element and its sub elements.
	 *
	 * @param element the element to add
	 * @param isAdd   whether to add or remove the element id
	 */

	public final void manageId(DesignElement element, boolean isAdd) {
		

		// if the element is hanging and not in the module, return

		if ((element == null) || (element instanceof ContentElement) || (element.getRoot() != getModule())) {
			return;
		}
		if (isAdd) {
			// the element has no id or a duplicate id, re-allocate another one

			if (element.getID() <= NO_ID
					|| (getElementByID(element.getID()) != null && getElementByID(element.getID()) != element)) {
				element.setID(getNextID());
			}

			if (getElementByID(element.getID()) == null) {
				addElementID(element);
			}
		} else {
			dropElementID(element);
		}

		Iterator<DesignElement> iter = new LevelContentIterator(getModule(), element, 1);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			manageId(innerElement, isAdd);
		}
	}

	/**
	 * Gets the location information of the module.
	 *
	 * @return the location information of the module
	 */

	public final String getLocation() {
		if (location == null) {
			return null;
		}

		return location.toExternalForm();
	}

	/**
	 * Sets the location information of the module.
	 *
	 * @param location the location information of the module
	 */

	public final void setLocation(URL location) {
		this.location = location;
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
	 * @param element the element handle whose name is need to check.
	 */

	@Override
	public void rename(DesignElement element) {
		new NameExecutor(getModule(), element).rename();
	}

	/**
	 * Recursively changes the element name in the context of the container.
	 *
	 * <ul>
	 * <li>If the element name is required and duplicated name is found rename the
	 * element with a new unique name.
	 * <li>If the element name is not required, clear the name.
	 * </ul>
	 *
	 * @param container the container of the element
	 * @param element   the element handle whose name is need to check.
	 */

	public final void rename(DesignElement container, DesignElement element) {
		NameExecutor executor = new NameExecutor(getModule(), container, element);
		executor.makeUniqueName();

		LevelContentIterator iter = new LevelContentIterator(getModule(), element, 1);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			rename(element, innerElement);
		}
	}

	@Override
	public void makeUniqueName(DesignElement element) {
		new NameExecutor(getModule(), element).makeUniqueName();
	}

	/**
	 * Gets the options set in the module or any one of its host.
	 *
	 * @return the options
	 */

	public ModuleOption getOptions() {
		return this.options;
	}

	/**
	 * Sets the options in this module.
	 *
	 * @param options the options to set
	 */

	public final void setOptions(ModuleOption options) {
		this.options = options;
	}

	/**
	 * Sets the resource folder for this session.
	 *
	 * @param resourceFolder the folder to set
	 */

	public final void setResourceFolder(String resourceFolder) {
		if (options == null) {
			options = new ModuleOption();
		}
		options.setResourceFolder(resourceFolder);
	}

	/**
	 * Gets the resource folder set in this session.
	 *
	 * @return the resource folder set in this session
	 */

	public final String getResourceFolder() {
		ModuleOption effectOptions = getOptions();
		if (effectOptions == null) {
			return null;
		}
		return effectOptions.getResourceFolder();
	}

	/**
	 * Sets the module is read-only one. That means any operation on it will throw
	 * runtime exception.
	 */

	public final void setReadOnly() {
		activityStack = new ReadOnlyActivityStack(getModule());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameContainer#getNameHelper ()
	 */
	@Override
	public final INameHelper getNameHelper() {
		return this.nameHelper;
	}

	/**
	 * Determines whether the module has cached values.
	 *
	 * @return <code>true</code> if values have been cached. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isCached() {
		return isCached;
	}

	/**
	 * Sets cache status of the module. The value is set to TRUE when calling {
	 * {@link ReportDesignHandle#cacheValues()}.
	 *
	 * @param isCached
	 */
	public final void setIsCached(boolean isCached) {
		this.isCached = isCached;
	}

	/**
	 * Caches values for the element. The caller must be the report design.
	 */

	public void cacheValues() {
		nameHelper.cacheValues();
	}

	/**
	 * Gets all the design elements that resides in the id-map. All the element in
	 * the returned list resides in the design tree and has unique id.
	 *
	 * @return
	 */
	public final List<DesignElement> getAllElements() {
		List<DesignElement> elements = new ArrayList<>(idMap.values());
		return elements;
	}

	/**
	 * Caches the propertyResourceBundle list.
	 *
	 * @param baseName   the file name
	 * @param bundleList the propertyResouceBundle list
	 */

	public final CachedBundles getResourceBundle() {
		if (getOptions() == null || (getOptions() != null && getOptions().useSemanticCheck())) {
			return null;
		}

		if (cachedBundles == null) {
			cachedBundles = new CachedBundles();
		}

		return cachedBundles;
	}

	/**
	 * Returns the version manager for the API compatibility.
	 *
	 * @return the version manager
	 */

	public final VersionControlMgr getVersionManager() {
		return versionMgr;
	}

	/**
	 * Broadcasts the file name event to the file name listeners.
	 *
	 * @param event the file name event
	 */

	public final void broadcastFileNameEvent(AttributeEvent event) {
		if (attributeListeners != null) {
			Iterator<IAttributeListener> iter = attributeListeners.iterator();
			while (iter.hasNext()) {
				IAttributeListener listener = iter.next();

				listener.fileNameChanged((ModuleHandle) getHandle(getModule()), event);
			}
		}
	}

	/**
	 * Adds one attribute listener. The duplicate listener will not be added.
	 *
	 * @param listener the attribute listener to add
	 */

	public final void addAttributeListener(IAttributeListener listener) {
		if (attributeListeners == null) {
			attributeListeners = new ArrayList<>();
		}

		if (!attributeListeners.contains(listener)) {
			attributeListeners.add(listener);
		}
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
		if (attributeListeners == null) {
			return false;
		}
		return attributeListeners.remove(listener);
	}

	/**
	 * Finds a custom color by name.
	 *
	 * @param colorName the custom color name
	 * @return the custom defined color that matches, or <code>null</code> if the
	 *         color name was not found in the custom color palette.
	 */

	public final CustomColor findColor(String colorName) {
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance()
				.getStructure(CustomColor.CUSTOM_COLOR_STRUCT);
		return (CustomColor) StructureRefUtil.findStructure(getModule(), defn, colorName);
	}

	/**
	 * Finds a config variable by name
	 *
	 * @param variableName the configure variable name
	 * @return the config variable that matches, or <code>null</code> if the
	 *         variable name was not found.
	 */

	public final ConfigVariable findConfigVariabel(String variableName) {
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance()
				.getStructure(ConfigVariable.CONFIG_VAR_STRUCT);
		return (ConfigVariable) StructureRefUtil.findStructure(getModule(), defn, variableName);
	}

	/**
	 * Finds an embedded image by name.
	 *
	 * @param imageName the embedded image name
	 * @return the defined image that matches, or <code>null</code> if the image
	 *         name was not found in the embedded images.
	 */

	public final EmbeddedImage findImage(String imageName) {
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance()
				.getStructure(EmbeddedImage.EMBEDDED_IMAGE_STRUCT);

		return (EmbeddedImage) StructureRefUtil.findStructure(getModule(), defn, imageName);
	}

	/**
	 * Returns all libraries this module contains.
	 *
	 * @return list of libraries.
	 */

	public final List<Library> getAllLibraries() {
		return getLibraries(IAccessControl.ARBITARY_LEVEL);
	}

	// ////////////////////////////////////////////////////////////
	// belows are some empty implementation for unique method calls
	/**
	 * Finds a shared style in this module itself.
	 *
	 * @param name Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public StyleElement findNativeStyle(String name) {
		return null;
	}

	/**
	 * Finds a shared style in this module and its included modules.
	 *
	 * @param name Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public StyleElement findStyle(String name) {
		return null;
	}

	/**
	 * Finds a named report item in this module and the included modules. This is
	 * for body elements in the element's name space.
	 *
	 * @param name The name of the element to find.
	 * @return The element, or null if the element is not found.
	 */

	public DesignElement findElement(String name) {
		return null;
	}

	/**
	 * Finds a master page by name in this module and the included modules.
	 *
	 * @param name the master page name.
	 * @return the master page, if found, otherwise null.
	 */

	public DesignElement findPage(String name) {
		return null;
	}

	/**
	 * Finds a parameter by name in this module and the included modules.
	 *
	 * @param name The parameter name.
	 * @return The parameter, if found, otherwise null.
	 */

	public final DesignElement findParameter(String name) {
		return resolveNativeElement(name, PARAMETER_NAME_SPACE);
	}

	/**
	 * Adds a new Translation entry to the module. A report file can reference
	 * message IDs that are defined by the customers. One entry of
	 * <code>Translation</code> represents a translated message for a specific
	 * locale.
	 * <p>
	 *
	 * @param translation new entry of <code>Translation</code> that are to be added
	 *                    to the module.
	 */

	public void addTranslation(Translation translation) {
		// do nothing
	}

	/**
	 * Drops a Translation from the module.
	 * <p>
	 *
	 * @param translation the translation to be dropped from the module.
	 *
	 * @return <code>true</code> if the report module contains the given
	 *         translation.
	 */

	public boolean dropTranslation(Translation translation) {
		return false;
	}

	/**
	 * Gets the translation table of this module.
	 *
	 * @return
	 */
	protected TranslationTable getTranslationTable() {
		return null;
	}

	/**
	 * Finds a <code>Translation</code> by the message resource key and the locale.
	 * <p>
	 *
	 * @param resourceKey resourceKey of the user-defined message where the
	 *                    translation is defined in.
	 * @param locale      locale for the translation. Locale is in java-defined
	 *                    format( en, en-US, zh_CN, etc.)
	 * @return the <code>Translation</code> that matches. return null if the
	 *         translation is not found in the report.
	 */

	public Translation findTranslation(String resourceKey, String locale) {
		return null;
	}

	/**
	 * Returns if the specified translation is contained in the translation table.
	 *
	 * @param trans a given <code>Translation</code>
	 * @return <code>true</code> if the <code>Translation</code> is contained in the
	 *         translation table, return <code>false</code> otherwise.
	 */

	public boolean contains(Translation trans) {
		return false;
	}

	/**
	 * Returns the whole collection of translations defined for the report module.
	 * <p>
	 *
	 * @return a list containing all the Translations.
	 */

	public List<Translation> getTranslations() {
		return Collections.emptyList();
	}

	/**
	 * Returns the collection of translations defined for a specific message. The
	 * message is presented by its resourceKey.
	 * <p>
	 *
	 * @param resourceKey resource key for the message.
	 * @return a list containing all the Translations defined for the message.
	 */

	public List<Translation> getTranslations(String resourceKey) {
		return Collections.emptyList();
	}

	/**
	 * Returns a string array containing all the resource keys defined for messages.
	 * <p>
	 *
	 * @return a string array containing all the resource keys defined for messages
	 *         return null if there is no messages stored.
	 */

	public String[] getTranslationResourceKeys() {
		return null;
	}

	/**
	 * Gets the property definition whose detail type is of the given structure
	 * name.
	 *
	 * @param structureName the structure name to search
	 * @return the property definition whose detail type is of the given structure
	 *         name, otherwise null
	 */

	public ElementPropertyDefn getReferencablePropertyDefn(String structureName) {
		return null;
	}

	/**
	 * Returns the root module that contains this library. The return value can be
	 * report or library.
	 *
	 * @return the root module
	 */

	public Module findOutermostModule() {
		return getModule();
	}

	/**
	 * Loads library with the given library file name. This file name can be
	 * absolute or relative. If the library doesn't exist or fatal error occurs when
	 * opening library, one invalid library will be added into the library list of
	 * this module.
	 *
	 * @param libraryFileName library file name
	 * @param namespace       library namespace
	 * @param reloadLibs
	 * @param url             the found library URL
	 * @return the loaded library
	 * @throws DesignFileException if the library file has fatal error.
	 */

	public Library loadLibrary(String libraryFileName, String namespace, Map<String, Library> reloadLibs, URL url)
			throws DesignFileException {
		// do nothing
		return null;
	}

	/**
	 * Loads library with given library file name. This method will add library into
	 * this module even if the library file is not found or has fatal error.
	 *
	 * @param includeLibrary library file name
	 * @param foundLib       the matched library
	 * @param reloadLibs     the map contains reload libraries
	 * @param url            the found library URL
	 * @see #loadLibrary(String, String)
	 */

	public void loadLibrarySilently(IncludedLibrary includeLibrary, Library foundLib, Map<String, Library> reloadLibs,
			URL url) {
		// do nothing
	}

	/**
	 * Returns included libraries within the given depth. Uses the Breadth-First
	 * Search Algorithm.
	 *
	 * @param level the given depth
	 * @return list of libraries.
	 *
	 * @see IModuleNameScope
	 */

	public List<Library> getLibraries(int level) {
		return Collections.emptyList();
	}

	/**
	 * Returns only libraries this module includes directly.
	 *
	 * @return list of libraries.
	 */

	public final List<Library> getLibraries() {
		return getLibraries(IAccessControl.DIRECTLY_INCLUDED_LEVEL);
	}

	/**
	 * Inserts the library to the given position.
	 *
	 * @param library the library to insert
	 * @param posn    at which the given library is inserted.
	 */

	public void insertLibrary(Library library, int posn) {
		// do nothing
	}

	/**
	 * Adds the given library to library list.
	 *
	 * @param library the library to insert
	 */

	public void addLibrary(Library library) {
		// do nothing
	}

	/**
	 * Drops the given library from library list.
	 *
	 * @param library the library to drop
	 * @return the position of the library to drop
	 */

	public int dropLibrary(Library library) {
		// do nothing
		return -1;
	}

	/**
	 * Returns the module with the given namespace. This method checks the namespace
	 * in both directly and indirectly included libraries.
	 *
	 * @param namespace the module namespace
	 * @return the module with the given namespace
	 */

	public final Library getLibraryWithNamespace(String namespace) {
		return getLibraryWithNamespace(namespace, IAccessControl.ARBITARY_LEVEL);
	}

	/**
	 * Returns the module with the given namespace. This method checks the namespace
	 * in included libraries within the given depth.
	 *
	 * @param namespace the module namespace
	 * @param level     the depth of the library
	 * @return the module with the given namespace
	 *
	 * @see IModuleNameScope
	 */

	public Library getLibraryWithNamespace(String namespace, int level) {
		return null;
	}

	/**
	 * Gets a list containing all the include libraries.
	 *
	 * @return a list containing all the include libraries. Return <code>null</code>
	 *         if there were no include libraries defined.
	 */

	public List<IncludedLibrary> getIncludedLibraries() {
		return Collections.emptyList();
	}

	/**
	 * Gets the default units for the design.
	 *
	 * @return the default units used in the design
	 */
	public String getUnits() {
		return null;
	}

	/**
	 * Gets a list containing all the include scripts.
	 *
	 * @return a list containing all the include scripts. Return <code>null</code>
	 *         if there were no scripts defined.
	 */
	public final List<IncludeScript> getIncludeScripts() {
		return (ArrayList<IncludeScript>) getLocalProperty(getModule(), INCLUDE_SCRIPTS_PROP);
	}

	/**
	 * Returns the fatal exception, which means some unrecoverable error is found in
	 * the included libraries.
	 *
	 * @return the fatal exception
	 */

	public Exception getFatalException() {
		return null;
	}

	/**
	 * Sets the fatal exception.
	 *
	 * @param fatalException the fatal exception to set
	 */

	protected void setFatalException(Exception fatalException) {
		// do nothing
	}

	/**
	 * Finds a theme in this module and its included modules.
	 *
	 * @param name Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public final Theme findTheme(String name) {
		return (Theme) resolveElement(null, name, null,
				MetaDataDictionary.getInstance().getElement(ReportDesignConstants.THEME_ITEM));
	}

	/**
	 * Finds a theme in this module and its included modules.
	 *
	 * @param name Name of the style to find.
	 * @return The style, or null if the style is not found.
	 */

	public final ReportItemTheme findReportItemTheme(String name) {
		return (ReportItemTheme) resolveElement(null, name, null,
				MetaDataDictionary.getInstance().getElement(ReportDesignConstants.REPORT_ITEM_THEME_ELEMENT));
	}

	/**
	 * Returns the theme of the report design/library.
	 *
	 * @return the theme of the report design/library
	 */

	@Override
	public Theme getTheme() {
		return null;
	}

	/**
	 * Gets the name of the referenced theme on this element.
	 *
	 * @return theme name. null if the theme is not defined on the element.
	 */

	@Override
	public String getThemeName() {
		return null;
	}

	/**
	 * Returns the resolved theme of the report design/library.
	 *
	 * @param module the module to resolve the theme
	 * @return the resolved theme of the report design/library
	 */

	@Override
	public Theme getTheme(Module module) {
		return null;
	}

	/**
	 * @param namespace
	 * @return the included library structure with the given namespace
	 */

	public final IncludedLibrary findIncludedLibrary(String namespace) {
		List<IncludedLibrary> libs = getIncludedLibraries();
		if (libs == null) {
			return null;
		}

		IncludedLibrary includedItem = null;
		for (int i = 0; i < libs.size(); i++) {
			IncludedLibrary incluedLib = libs.get(i);
			String tmpNameSpace = incluedLib.getNamespace();
			assert tmpNameSpace != null;
			if (tmpNameSpace.equalsIgnoreCase(namespace)) {
				includedItem = incluedLib;
				break;
			}
		}
		return includedItem;
	}

	/**
	 * Gets the library with the given location path in native level.
	 *
	 * @param theLocation the location path to find
	 * @return the library with the given location path if found, otherwise null
	 */

	public final Library getLibraryByLocation(String theLocation) {
		return getLibraryByLocation(theLocation, IAccessControl.DIRECTLY_INCLUDED_LEVEL);
	}

	/**
	 * Gets the library with the given location path in given level.
	 *
	 * @param theLocation the location path to find
	 * @param level       the depth of the library
	 * @return the library with the given location path if found, otherwise null
	 */

	public Library getLibraryByLocation(String theLocation, int level) {
		return null;
	}

	/**
	 * Gets the library with the given location path in given level.
	 *
	 * @param theLocation the location path to find
	 * @param level       the depth of the library
	 * @return the library with the given location path if found, otherwise null
	 */

	public final List<Library> getLibrariesByLocation(String theLocation, int level) {
		// if the location path is null or empty, return null

		if (StringUtil.isBlank(theLocation)) {
			return Collections.emptyList();
		}

		// look up the library with the location path in the included library
		// list

		List<Library> retList = new ArrayList<>();
		List<Library> libraries = getLibraries(level);
		for (int i = 0; i < libraries.size(); i++) {
			Library library = libraries.get(i);
			if (theLocation.equalsIgnoreCase(library.getLocation())) {
				retList.add(library);
			}
		}

		// the library with the given location path is not found, return null

		return retList;
	}

	/**
	 * Finds a template parameter definition by name in this module and the included
	 * modules.
	 *
	 * @param name name of the template parameter definition to find
	 * @return the template parameter definition, if found, otherwise null.
	 */

	public TemplateParameterDefinition findTemplateParameterDefinition(String name) {
		return null;
	}

	/**
	 * Returns whether the namespace to check is duplicate in target module. This
	 * method helps to judge whether the library to check can be included in target
	 * module.
	 *
	 * @param namespaceToCheck the namespace to check
	 * @return true if the namespace to check is duplicate.
	 */

	public boolean isDuplicateNamespace(String namespaceToCheck) {
		return false;
	}

	/**
	 * Finds the property binding defined in this module, which has the same
	 * property name with the given property name and has the same element id of the
	 * given element.
	 *
	 * @param element  the element to find
	 * @param propName the property name to find
	 * @return the matched property binding defined in the module, otherwise null
	 */

	public final PropertyBinding findPropertyBinding(DesignElement element, String propName) {
		// if element or property name is null, return null

		

		// if the property with the given name is not defined on the element,
		// return null

		if (element == null || propName == null || (element.getPropertyDefn(propName) == null)) {
			return null;
		}

		// find the property binding in the list, match the property name and
		// element id

		List<Object> propertyBindings = getListProperty(getModule(), PROPERTY_BINDINGS_PROP);
		if (propertyBindings == null) {
			return null;
		}
		for (int i = 0; i < propertyBindings.size(); i++) {
			PropertyBinding propBinding = (PropertyBinding) propertyBindings.get(i);
			BigDecimal id = propBinding.getID();
			if (id != null && propName.equalsIgnoreCase(propBinding.getName())
					&& getElementByID(id.longValue()) == element) {
				return propBinding;
			}

		}
		return null;
	}

	/**
	 * Gets all the defined property bindings for the given element. Each one in the
	 * list is instance of <code>PropertyBinding</code>.
	 *
	 * @param element the element to find
	 * @return the property binding list defined for the element
	 */

	public final List<PropertyBinding> getPropertyBindings(DesignElement element) {
		if (element == null) {
			return Collections.emptyList();
		}

		List<Object> propertyBindings = getListProperty(getModule(), PROPERTY_BINDINGS_PROP);
		if (propertyBindings == null) {
			return Collections.emptyList();
		}

		List<PropertyBinding> result = new ArrayList<>();
		for (int i = 0; i < propertyBindings.size(); i++) {
			PropertyBinding propBinding = (PropertyBinding) propertyBindings.get(i);
			BigDecimal id = propBinding.getID();
			if (id != null && getElementByID(id.longValue()) == element) {
				result.add(propBinding);
			}

		}
		return result;
	}

	/**
	 * Checks the name of the embedded image in this report. If duplicate, get a
	 * unique name and rename it.
	 *
	 * @param image the embedded image whose name is need to check
	 */

	public void rename(EmbeddedImage image) {
		// do nothing
	}

	/**
	 * Returns the module namespace. Only included library has a non-empty
	 * namespace.
	 *
	 * @return the module namespace
	 */

	public String getNamespace() {
		return null;
	}

	/**
	 * Loads css with the given css file name. This file name can be absolute or
	 * relative. If the css doesn't exist or fatal error occurs when opening css,.
	 *
	 * @param fileName css file name
	 * @return the loaded css
	 * @throws StyleSheetException
	 */

	public CssStyleSheet loadCss(String fileName) throws StyleSheetException {
		// do nothing
		return null;
	}

	/**
	 * Loads css with the given css file name. This file name can be absolute or
	 * relative. If the css doesn't exist or fatal error occurs when opening css,.
	 *
	 * @param container report design/theme
	 * @param url       the url where the style sheet resides
	 * @param fileName  css file name
	 * @return the loaded css
	 * @throws StyleSheetException
	 */

	public CssStyleSheet loadCss(DesignElement container, URL url, String fileName) throws StyleSheetException {
		// do nothing
		return null;
	}

	/**
	 *
	 * @return
	 */
	public ULocale getLocale() {
		// first, read it from module option
		ModuleOption option = getOptions();
		if (option != null) {
			ULocale optionLocale = option.getLocale();
			if (optionLocale != null) {
				return optionLocale;
			}
		}

		// second, read it from session
		ULocale sessionLocale = session.getLocale();
		if (sessionLocale != null) {
			return sessionLocale;
		}

		return ThreadResources.getLocale();
	}

	/**
	 *
	 * @param source
	 */
	public void updateCacheForDrop(DataSource source) {
		// do nothing by default
	}

	/**
	 *
	 * @param source
	 * @param oldName
	 * @param newName
	 */
	public void updateCacheForRename(DataSource source, String oldName, String newName) {
		// do nothing by default
	}

	/**
	 * Sets the appContext to module options.
	 *
	 * @param appContext
	 */
	public void setOptions(Map appContext) {
		if (appContext == null || appContext.isEmpty()) {
			return;
		}
		if (options == null) {
			options = new ModuleOption();
		}

		options.setOptions(appContext);
	}
}
