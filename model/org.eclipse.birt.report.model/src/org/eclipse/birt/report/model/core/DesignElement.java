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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.command.ExtendsException;
import org.eclipse.birt.report.model.command.PropertyNameException;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.SemanticError;
import org.eclipse.birt.report.model.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefPropertyType;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.ObjectDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.validators.StructureListValidator;

/**
 * Base class for all design elements in BIRT. This class provides a number of
 * basic services:
 * <p>
 * <ul>
 * <li>Meta-data</li>
 * <li>Styles</li>
 * <li>Inheritance</li>
 * <li>Property management</li>
 * <li>Element name</li>
 * <li>Containment</li>
 * <li>Change notification</li>
 * <li>Visitor</li>
 * <li>API handle</li>
 * <li>Semantic state</li>
 * </ul>
 * <p>
 * The descriptions that follow describe the major operations on this class. All
 * operations that change an element <strong>must </strong> be made though a
 * command. So, if you find a method <code>setMumble( )</code> that is of
 * interest, you must locate (or create) a <code>SetMumble</code> command to
 * perform this operation in a way that can be undone. Also, some of the
 * operations described here are incomplete, in that they handle a task only on
 * this one element. Many tasks must be done on several elements to be complete.
 * For example, if we call the <code>setExtends( )</code> method of element E,
 * we also must cache the inverse relationships in the parent element. This
 * higher-level semantic processing is handled by code above this class.
 * 
 * <h3>Meta-data</h3>
 * 
 * This class stores information that the user enters for a design element.
 * Every element is defined by an <em>element definition </em>. The
 * relationship between an element and element definition is much like that
 * between a object and a class in Java. The element definition defines
 * characteristics of the element, and is fixed by the development team. The
 * element is defined by the user and contains information needed for a
 * particular report.
 * <p>
 * The {@link #getDefn}method provides access to the element definition for the
 * particular element. The returned {@link ElementDefn}object provides access
 * to the properties defined for the element as well as other information.
 * <p>
 * This abstract base class provides operations common to all (or most)
 * elements. The application creates Java subclasses to represent the various
 * kinds of report elements. Those subclasses provide the behavior specific to
 * that element type, usually allocating space for any element slots and
 * providing a callback into the {@link ElementVisitor design visitor}class.
 * <p>
 * See the {@link ElementDefn}class for more information on metadata, including
 * the use of the metadata definition file.
 * <p>
 * Members and classes in the meta-data system include:
 * <p>
 * <ul>
 * <li>The {@link #getDefn}method gets the element meta-data.</li>
 * <li>The {@link #getPropertyDefn}method gets the meta-data for a system- or
 * user-defined property.</li>
 * <li>The {@link #validatePropertyValue}method validates a property value
 * using the rules defined in the meta-data for the * property.</li>
 * <li>The {@link ElementDefn}class provides the meta-data for an element
 * type.</li>
 * <li>The {@link ElementPropertyDefn}class provides meta-data for a property.
 * It has two subclasses:
 * {@link org.eclipse.birt.report.model.metadata.SystemPropertyDefn}for
 * built-in properties defined in the meta-data file, and
 * {@link UserPropertyDefn}for user-defined properties.</li>
 * <li>The {@link org.eclipse.birt.report.model.metadata.MetaDataDictionary}
 * singleton class provides information to the meta-data for all elements.</li>
 * </ul>
 * 
 * <h3>Styles</h3>
 * 
 * Many elements have an associated style. Style influence the set of system-
 * defined properties, as described below. They also affect the notification
 * system as described below.
 * <p>
 * This abstract base class provides style support in the handling of property
 * values, notifications and the like. Derived classes add logic to define a
 * named style. The derived class overrides the {@link #getStyle}method to
 * return the style for use by the generic mechanisms.
 * <p>
 * An element that can have a style is called a <em>styled element</em>. The
 * style affects the styled elements in three distinct ways.
 * <ol>
 * <li>The element can include some or all of the properties defined for the
 * style element. The element defines only those that make sense for that
 * element. These <em>style properties</em> appear as if they are properties
 * of the element itself.</li>
 * <li>The element can name a shared style from which to get properties.</li>
 * <li>If element does provide the value for a style property, and the element
 * does not explicitly reference a shared style, then the application will use a
 * <em>property search</em> to find a style implicitly associated with this
 * element. This is a search upward in the containment hierarchy to find either
 * 1) a shared style referenced by a container, or 2) an predefined style
 * associated with the slot or element. For example, a label in a Container in a
 * List's Header slot will use the predefined "list-header" style unless another
 * style is defined on the style or Container.</li>
 * </ol>
 * <p>
 * Members and classes in the style system include:
 * <p>
 * <ul>
 * <li>The {@link #getStyle}method returns the associated style, if any, for
 * this element as a generic DesignElement.</li>
 * </ul>
 * 
 * <h3>Inheritance</h3>
 * 
 * User-defined elements can extend other elements, but style element is not
 * allowed to extend each other. The following is the terminology used for this
 * system.
 * <ul>
 * <li>If element E extends element D, then D is said to be the
 * <em>parent </em> element, and element E is the <em>derived </em> element.
 * </li>
 * <li>We can also use the term <em>base </em> as a synonym for parent. That
 * is, D is the base element above.</li>
 * <li>Element D may in turn extend another element C.</li>
 * <li>We say that C and D are <em>ancestor </em> elements of E, and that D
 * and E are <em>descendents </em> of C.</li>
 * <li>In this simple hierarchy, element E is the <em>most derived </em>
 * element, while element C is the <em>most base </em> element.</li>
 * </ul>
 * <p>
 * Inheritance applies to the following:
 * <p>
 * <dl>
 * <dt><strong>Property values </strong></dt>
 * <dd>An element inherits any property values defined by any of its ancestors.
 * If two ancestors define values for the same property, then the inherited
 * value is that defined by the most derived ancestor. This rule is similar to
 * the method overriding rules in Java.</dd>
 * 
 * <dt><strong>Named style </strong> </strong></dt>
 * <dd>If an ancestor element explicitly names a style, then the derived
 * element will also use that style (unless the derived element specifies a
 * different style.)</dd>
 * 
 * <dt><strong>Element type </strong> </strong></dt>
 * <dd>Inheritance only works between elements of the same type. One could say
 * that a derived element inherits the element type of the parent element. More
 * precisely, both the parent and derived elements have a type, and the
 * semantics of the application require that the element types be the same. It
 * is an error to extend an element of a different type.</dd>
 * 
 * <dt><strong>User-defined properties </strong> </strong></dt>
 * <dd>The user can define a custom property. All derived elements also have
 * that property.</dd>
 * 
 * <dt><strong>Custom behavior </strong> </strong></dt>
 * <dd>The user can define custom behavior by associating the element with a
 * Java class. Derived elements inherit this behavior.</li>
 * </dl>
 * <p>
 * 
 * Inheritance does <em>not</em>, however, apply to the following:
 * <p>
 * <ul>
 * <li><strong>Name </strong>. A derived element does not inherit the name of
 * the parent component. If it did, then there would be two elements with the
 * same name, violating the rules about element name uniqueness.</li>
 * 
 * <li><strong>Parent element </strong>. A derived element necessarily has a
 * distinct value for the "extends" property than does its parent.</li>
 * </ul>
 * <p>
 * 
 * Members and classes in the inheritance system include:
 * <p>
 * <ul>
 * <li>The {@link #setExtendsElement}method sets the parent element.</li>
 * <li>The {@link #setExtendsName}method sets the name of the parent element.
 * It is used when reading a design file, or if it is necessary to store an
 * unresolved parent name.</li>
 * <li>The {@link #getExtendsElement}method gets the parent element.</li>
 * <li>The {@link #extendsRef}member stores the reference to the parent
 * element.</li>
 * </ul>
 * 
 * <h3>User-Defined Properties</h3>
 * 
 * As noted above, the user can define user-defined properties for an element.
 * User-defined properties have many of the same attributes as system-defined
 * properties. User-defined properties work virtually the same as system-defined
 * properties, including support for localization, for a property type, for
 * validating the property value, and more.
 * <p>
 * Members and classes in the user-defined property system include:
 * <p>
 * <ul>
 * <li>The {@link #addUserPropertyDefn}method defines a user-defined property.
 * </li>
 * <li>The {@link #dropUserPropertyDefn}method removes a user-defined
 * property.</li>
 * <li>The {@link #getUserPropertyDefn}method gets the definition of a
 * user-defined property.</li>
 * <li>The {@link UserPropertyDefn}class defines a user-defined property.
 * </li>
 * <li>The {@link #userProperties}member holds the user-defined property
 * definitions for this element.</li>
 * </ul>
 * <p>
 * See also the meta-data section above for the methods that validate property
 * values, and the property value section below for methods that work with
 * property values.
 * 
 * <h3>Property Values</h3>
 * 
 * See the {@link ElementPropertyDefn}class for a description of the details of
 * property values. Property values are of two key types: normal and intrinsic.
 * Normal properties are stored in the {@link #propValues}member on this class.
 * Intrinsic properties are stored in member variables.
 * <p>
 * Normal properties have two states: <em>set</em> and <em>unset</em>. A
 * property is set on element E if a value for that property appears in the
 * propValues list for element E; otherwise it is unset on element E.
 * <p>
 * Elements inherit property values from their parent elements. So, if property
 * P is unset on element E, E will inherit the value of P from its parent
 * element.
 * <p>
 * The <em>local value</em> of a property for an element E indicates whether
 * or not the property is set on that specific element. The local value is
 * generally of interest only to low-level code. The XML design file shows only
 * the local property values for an element.
 * <p>
 * The <em>effective value </em> of a property provides the value computed
 * using the property search which includes the ancestor elements, styles,
 * context, system-defaults, and other information. The effective value is
 * generally what the user cares about, and is what appears in the property
 * sheet and other UI.
 * <p>
 * Members and classes in the property value system include:
 * <p>
 * <ul>
 * <li>The {@link #propValues}member stores the property values set on this
 * element.</li>
 * <li>The {@link #setProperty( String, Object )}method sets a property value
 * on this element.</li>
 * <li>The {@link #clearProperty}method unsets a property value on this
 * method.</li>
 * <li>The {@link #getLocalProperty( ReportDesign, String )}method gets the
 * property value, if any, set on this element.</li>
 * <li>The {@link #getProperty( ReportDesign, String )}method gets the
 * effective value of the property.</li>
 * </ul>
 * 
 * <h3>Element Name</h3>
 * 
 * Elements can have a name. The semantics of the specific element type
 * determine if the element name is optional or required. In a few contexts, the
 * name may be ignored (not supported). The name uniquely identifies an element
 * and is locale-independent. (The display name, described below, is
 * localizable.) Elements with a name can be referenced in the design or in
 * code. Elements without a name can be referenced in code by navigating to the
 * element.
 * <p>
 * If an element has a name, then it resides in a name space. All name spaces
 * reside in the {@link RootElement root element}.
 * <p>
 * An element also has a <em>display name</em> that is shown to the user. The
 * display name is optional, and is represented by a system property value. If
 * not provided, then the user sees the name. The display name is localizable.
 * Localized names are important for elements used in templates when the users
 * of the templates are international.
 * <p>
 * Members and classes in the name system include:
 * <p>
 * <ul>
 * <li>The {@link #setName}method.</li>
 * <li>The {@link #getName}method.</li>
 * <li>The {@link NameSpace NameSpace}class.</li>
 * </ul>
 * 
 * <h3>Containment</h3>
 * 
 * The overall report design organizes design elements into a hierarchy. All
 * elements except the report design itself are <em>contained </em> within some
 * other element: the <em>container </em>. The contained element is the
 * <em>content
 * </em>. (Note: the usage of the term container in this
 * discussion is more generic than the Container element in the report design.
 * The Container element is a container, but other elements are as well.)
 * <p>
 * The details of the containment are specific to each element type. For
 * example, a Container element has an ordered list of content elements. The
 * design has a named list of styles, and an ordered list of sections. As a
 * result, the implementation of the container-to-content relationship must be
 * defined in a subclass of this class.
 * <p>
 * However, all content elements have at most one container. And, all elements
 * (except the root) have a container. So, the content-to-container relationship
 * is modelled in this element base class.
 * <p>
 * Containers have one or more <em>slots </em>. See the
 * {@link org.eclipse.birt.report.model.metadata.SlotDefn}class for a detailed
 * description of slots.
 * <p>
 * Members in the containment system include:
 * <p>
 * <ul>
 * <li>The {@link #container}member that caches a pointer to the container
 * element.</li>
 * <li>The {@link #getContainer}method to get the container element.</li>
 * <li>The {@link #setContainer}method to set the container element.</li>
 * <li>The {@link #getSlot}method returns a slot within a container.
 * <li>The {@link ContainerSlot},{@link SingleElementSlot}and
 * {@link MultiElementSlot}classes that represent slots.</li>
 * </ul>
 * 
 * <h3>Change Notification</h3>
 * 
 * Any given element may be "visualized" in multiple places in the user
 * interface. If the user changes the element, all the affected parts of the UI
 * must be updated. The notification system is responsible for these updates.
 * <p>
 * The notification system allows any number of {@link Listener}objects to
 * subscribe to receive change notifications for an element. Notifications are
 * sent for each change using subclasses of the {@link NotificationEvent}class.
 * See these two classes for more details.
 * <p>
 * Note: subscribing and unsubscribing listeners is the only mutator operation
 * that the application can (and should) do without the use of a command.
 * <p>
 * Members and classes in the notification system include:
 * <p>
 * <ul>
 * <li>The {@link #listeners}member holds the list of subscribed listeners.
 * </li>
 * <li>The {@link #addListener}and {@link #removeListener}methods to add and
 * remove listeners.</li>
 * <li>The {@link Listener}class to receive notifications.</li>
 * <li>A subclass of {@link NotificationEvent}notifies the listener of the
 * type of change, and information about the change.</li>
 * <li>The {@link org.eclipse.birt.report.model.activity.ActivityStack}class
 * triggers the notifications as it processes commands.</li>
 * <li>ActivityStack calls the {@link #sendEvent}method to send the
 * notification to the appropriate listeners.</li>
 * <li>The sendEvent( ) method in turn calls
 * {@link #broadcast(NotificationEvent)}to do the detailed work of sending an
 * event to all the listeners.</li>
 * </ul>
 * 
 * <h3>Element Identity</h3>
 * 
 * There are four ways to refer to an item:
 * <p>
 * <dl>
 * <dt><strong>By name </strong></dt>
 * <dd>Elements can have a name. Names are unique within a name space. However,
 * many elements are anonymous. Names are required only when the element will be
 * referenced. Some elements, such as styles, data sources, and data sets, exist
 * to be referenced and so require a name. Most other elements are seldom
 * referenced, and so the name is optional.</dd>
 * 
 * <dd><strong>By navigation </strong></dt>
 * <dd>Elements can be found via navigation. One can start at the root element
 * and work down though the containment hierarchy. Every valid element can be
 * reached via navigation. Indeed, the definition of element creation, from the
 * perspective of the design, is when the element is added to the containment
 * hierarchy. Similarly, the definition of deletion, again from the perspective
 * of the design, is when the element is dropped from the containment hierarchy.
 * </dd>
 * 
 * <dd><strong>By pointer </strong></dt>
 * <dd>The design tool itself references elements mostly using Java pointers.
 * That is, once the application finds an element of interest, it simply holds
 * onto it using a pointer.</dd>
 * 
 * <dd><strong>By ID </strong></dt>
 * <dd>The web designer needs a simple way to reference items across calls to
 * the server. None of the above is both simple and consistent. Element IDs
 * provide this service. IDs are valid only within a single design "session". A
 * session is the time between when a design is loaded into memory, and the time
 * that the design is released. IDs are <em>not </em> valid across sessions,
 * and are not persistent. The application must enable IDs by calling
 * {@link org.eclipse.birt.report.model.metadata.MetaDataDictionary#enableElementID}.
 * Call {@link #getID}to get the ID of an element. Call
 * {@link RootElement#getElementByID}to obtain and element given an element ID.
 * </dd>
 * </dl>
 * 
 * <h3>Element Visitor</h3>
 * 
 * A visitor class is a design pattern that is useful for implementing certain
 * kinds of algorithms that need to "touch" many different elements. One could
 * code the element by adding methods to each element, but doing so is
 * cumbersome, complex, and error-prone. Better is to define a <em>visitor</em>
 * class that has methods for each element. One simply <em>applies</em> the
 * visitor to an element, and the element then calls its specific "visit" method
 * in the visitor. For example, the design engine uses a visitor to write the
 * design to an XML file.
 * 
 * <h3>API Handles</h3>
 * 
 * This class, and its subclasses, are low-level classes that maintain the state
 * of the design. Property and slot methods are generic so that they work for
 * all elements. Modifications must be done through commands. The application,
 * however, wants to work with a simpler set of objects, and wants to work with
 * specific "getter" methods to get properties. The application also simply
 * wants to call a "setter" method to make a change.
 * <p>
 * The API handle package provides a set of "facade" classes that give this
 * high-level view. Therefore, the application will seldom work with this class
 * directly. Instead, it will work with the handle produced by calling the
 * {@link #getHandle}method.
 * 
 * <h3>Semantic State</h3>
 * 
 * Design elements maintain a wide variety of relationships to other parts of
 * the design. An element refers to its parent element or its shared style.
 * Expressions reference elements, parameters and data set columns. Many
 * elements reference data sources or data sets.
 * <p>
 * <em>Semantic processing</em> resolves name references to the actual
 * elements or other objects. Semantic processing is done incrementally for most
 * operations. However, batch processing is needed when reading a design file
 * and for certain other operations.
 * <p>
 * Semantic processing is done by doing a depth-first traversal of the design
 * tree. Errors are gathered on the design context.
 * <p>
 * The result of semantic processing may be that the design caches a reference
 * to another element as is done in this class for the parent element and the
 * shared style. In other cases, the reference is checked and then discarded.
 * <p>
 * When a reference is cached, the system must handle the case of a reference
 * that cannot be resolved. For example, an element might use a shared style
 * defined in a template, but that shared style has been deleted from the
 * template and can no longer be resolved. In this case, we cache the name until
 * the user loads the required elements, or changes the reference to an existing
 * style. Similar rules apply to the "extends" property.
 * 
 * @see ElementDefn
 * @see DesignElementHandle
 * @see ElementPropertyDefn
 */

public abstract class DesignElement implements IPropertySet
{

	/**
	 * Property name sufficed for any string property that can be localized. If
	 * the property name is "mumble", then the message ID for that property is
	 * "mumbleID".
	 */

	public static final String ID_SUFFIX = "ID"; //$NON-NLS-1$

	/**
	 * Name of the property that holds custom XML for an element.
	 */

	public static final String CUSTOM_XML_PROP = "customXml"; //$NON-NLS-1$

	/**
	 * Name of the property that holds comments about the element. Comments
	 * cannot be localized: they are for the use of the report developer.
	 */

	public static final String COMMENTS_PROP = "comments"; //$NON-NLS-1$

	/**
	 * Display name of the element. Can be localized.
	 */

	public static final String DISPLAY_NAME_PROP = "displayName"; //$NON-NLS-1$

	/**
	 * Message ID property for the display name.
	 */

	public static final String DISPLAY_NAME_ID_PROP = "displayNameID"; //$NON-NLS-1$

	/**
	 * Element name property. The element name is <em>intrinsic</em>: it is
	 * available as a property, but is stored as a field.
	 */

	public static final String NAME_PROP = "name"; //$NON-NLS-1$

	/**
	 * Name or reference to the element that this element extends. The extends
	 * property is <em>intrinsic</em>: it is available as a property, but is
	 * stored as a field.
	 */
	public static final String EXTENDS_PROP = "extends"; //$NON-NLS-1$

	/**
	 * Name of the property that holds masks of BIRT/user defined properties for
	 * the element.
	 */

	public static final String PROPERTY_MASKS_PROP = "propertyMask"; //$NON-NLS-1$

	/**
	 * Name of the user property definition.
	 */

	public static final String USER_PROPERTIES_PROP = "userProperties"; //$NON-NLS-1$

	/**
	 * Marker to indicate that the element is not in a slot.
	 */

	public static final int NO_SLOT = -1;

	/**
	 * Marker to indicate that at which level the user want to get the display
	 * label of this element. The display name or name of element.
	 */

	public static final int USER_LABEL = 0;

	/**
	 * The display name, name or metadata name of element
	 */

	public static final int SHORT_LABEL = 1;

	/**
	 * The short label pluses additional information.
	 */

	public static final int FULL_LABEL = 2;

	/**
	 * The max length the display label for every element. If the length exceeds
	 * this limit, the exceeding part will be shown as "..."
	 */
	private static final int MAX_DISPLAY_LABEL_LEN = 30;

	/**
	 * The element list each of which is not supported in release one.
	 */

	private static final String[] unSupportedElements = {
			ReportDesignConstants.PARAMETER_GROUP_ELEMENT,
			ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT,
			ReportDesignConstants.FREE_FORM_ITEM,
			ReportDesignConstants.LINE_ITEM,
			ReportDesignConstants.RECTANGLE_ITEM,
			ReportDesignConstants.EXTENDED_ITEM};

	/**
	 * Elements have an optional name. The name may be required by some element
	 * types. If so, the derived element class should enforce the use of the
	 * name. The name is non-localizable.
	 * <p>
	 * The name often occurs in a name space. Derived classes must enforce the
	 * name space rules. This class simply maintains the name itself.
	 */

	protected String name = null;

	/**
	 * Elements are structured in a hierarchy. The implementation of the
	 * container-to-child relationship must be defined in derived classes. This
	 * base class defines the child-to-container relationship.
	 */

	protected DesignElement container = null;

	/**
	 * Slot in the container in which this element resides.
	 */

	protected int containerSlotID = NO_SLOT;

	/**
	 * Listeners are the objects that want to be notified of events. Contents
	 * are of type Listener. Created only when needed.
	 */

	protected ArrayList listeners = null;

	/**
	 * Values for non-intrinsic property values. The contents are of type
	 * Object.
	 */

	protected HashMap propValues = new HashMap( );

	/**
	 * Definitions for user-defined properties. Contents are of type
	 * UserPropertyDefn.
	 */

	protected HashMap userProperties = null;

	/**
	 * Support for inheritance. Represents the resolved or unresolved element
	 * that this element extends.
	 */

	protected ElementRefValue extendsRef = null;

	/**
	 * Inverse of the extends relationship: parent-->derived. Very few elements
	 * are extended, so we create this list only when needed.
	 */

	protected ArrayList derived = null;

	/**
	 * The unique ID assigned to this element when it is added to the design.
	 * Allows web applications to refer to the element by ID.
	 */

	protected int id = 0;

	/**
	 * Cached element definition. Cached for speed since the definition cannot
	 * change.
	 */

	protected ElementDefn cachedDefn = null;

	/**
	 * Indicates whether the element is valid. The initial value is true.
	 */

	protected boolean isValid = true;

	/**
	 * API handle for this element.
	 */

	protected DesignElementHandle handle = null;

	/**
	 * Default constructor.
	 */

	public DesignElement( )
	{
	}

	/**
	 * Constructs the design element with the name.
	 * 
	 * @param theName
	 *            initial element name
	 */

	public DesignElement( String theName )
	{
		name = theName;
	}

	/**
	 * Registers a listener. A listener can be registered any number of times,
	 * but will receive each event only once.
	 * <p>
	 * Part of: Notification system.
	 * 
	 * @param obj
	 *            the listener to register
	 */

	public void addListener( Listener obj )
	{
		if ( listeners == null )
			listeners = new ArrayList( );
		if ( obj != null && !listeners.contains( obj ) )
			listeners.add( obj );
	}

	/**
	 * Removes a listener. The listener is removed from the list of listeners.
	 * If the item is not in the list, then the request is silently ignored.
	 * <p>
	 * Part of: Notification system.
	 * 
	 * @param obj
	 *            the listener to remove
	 */

	public void removeListener( Listener obj )
	{
		if ( listeners == null )
			return;
		int posn = listeners.indexOf( obj );
		if ( posn != -1 )
			listeners.remove( posn );
	}

	/**
	 * Removes all listeners on this element.
	 */

	public void clearListeners( )
	{
		if ( listeners != null )
			listeners.clear( );
		listeners = null;
	}

	/**
	 * Sends an event called by the notification framework. The default
	 * implementation sends the event to listeners of this one object. Derived
	 * classes override this to send it to additional associated objects
	 * depending on context. The subclass should set the event's delivery path
	 * to indicate these additional delivery routes.
	 * <p>
	 * Part of: Notification system.
	 * 
	 * @param ev
	 *            the event to send
	 */

	public void sendEvent( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.DIRECT );
		broadcast( ev );
	}

	/**
	 * Gets the root node of the design tree. This node must be a instance of
	 * <code>ReportDesign</code>.
	 * 
	 * @return the root node of the design tree
	 */

	protected ReportDesign getRoot( )
	{
		DesignElement element = this;

		while ( element.getContainer( ) != null )
			element = element.getContainer( );

		if ( element instanceof ReportDesign == false )
			return null;

		return (ReportDesign) element;
	}

	/**
	 * Implements to broadcast an event to all listeners of this design element.
	 * Implementations of sendEvent( ) call this to do the actual work of
	 * broadcasting.
	 * <p>
	 * Part of: Notification system.
	 * 
	 * @param ev
	 *            the event to send
	 */

	public final void broadcast( NotificationEvent ev )
	{
		broadcast( ev, getRoot( ) );
	}

	/**
	 * Implements to broadcast an event to all listeners of this design element
	 * on a design tree. Note subclasses should override this method to change
	 * the behavior of broadcast method.
	 * 
	 * Part of: Notification system.
	 * 
	 * @param ev
	 *            the event to send
	 * @param design
	 *            the root node of the design tree.
	 */

	protected void broadcast( NotificationEvent ev, ReportDesign design )
	{

		// copy a temporary ArrayList and send to all direct listeners.
		// so, there is no concurrent problem if the user changes
		// listeners in elementChanged method.

		if ( listeners != null )
		{
			ArrayList tmpListeners = new ArrayList( listeners );

			Iterator iter = tmpListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				( (Listener) iter.next( ) ).elementChanged(
						getHandle( design ), ev );
			}
		}

		// Forward to derived classes.

		if ( derived != null )
		{
			// The event with the ELEMENT_CLIENT path should not be forwarded
			// to the children of this client element.

			if ( ev.getDeliveryPath( ) == NotificationEvent.ELEMENT_CLIENT )
				return;

			// The event with the STYLE_CLIENT path should be forwarded to the
			// children of this client element. But the path should be changed
			// to
			// DESENDENT, because the style property changes are also considered
			// as the changes of this element.

			if ( ev.getDeliveryPath( ) != NotificationEvent.STYLE_CLIENT )
				ev.setDeliveryPath( NotificationEvent.DESCENDENT );

			Iterator iter = derived.iterator( );
			while ( iter.hasNext( ) )
			{
				( (DesignElement) iter.next( ) ).broadcast( ev, design );
			}
		}

	}

	/**
	 * Gets a property value given its internal name. This version does the full
	 * property search as defined by the given derived component. That is, it
	 * gets the "effective" property value. The name can be a built-in property
	 * name or a user-defined property name. However, if the name is not valid,
	 * the method will simply return a null property value.
	 * <p>
	 * The search won't search up the inheritance hierarchy if the property is
	 * defined to not inherit.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            name of the property to get. Can be a system-defined or
	 *            user-defined property name. Must be of the correct case.
	 * @return The property value, or null if no value is set.
	 */

	public Object getProperty( ReportDesign design, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );

		// If the property is not found, then the value is null.

		if ( prop == null )
			return null;

		return getProperty( design, prop );
	}

	/**
	 * Returns the property value from this element's parent. The search is only
	 * made in local properties, and local style of parents.
	 * 
	 * @param design
	 *            report design.
	 * @param prop
	 *            definition of the property to get.
	 * @return property value, or null if no value is set.
	 */

	protected Object getPropertyFromParent( ReportDesign design,
			ElementPropertyDefn prop )
	{
		Object value = null;

		DesignElement e = getExtendsElement( );
		while ( e != null )
		{
			// If we can find the value here, return it.

			value = e.getLocalProperty( design, prop );
			if ( value != null )
				return value;

			// Does the style provide the value of this property ?

			StyleElement style = e.getLocalStyle( );
			if ( style != null )
			{
				value = style.getLocalProperty( design, prop );
				if ( value != null )
					return value;
			}

			e = e.getExtendsElement( );
		}

		return value;
	}

	/**
	 * Gets a property value given its definition. This version does the
	 * property search as defined by the given derived component. That is, it
	 * gets the "effective" property value. The definition can be for a system
	 * or user-defined property.
	 * <p>
	 * The search won't search up the containment hierarchy. Meanwhile, ti won't
	 * the inheritance hierarchy if the non-style property is defined to not
	 * inherit. And style property won't be searched up the containment
	 * hierarchy if it'd defined to no inherit.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            definition of the property to get
	 * @return The property value, or null if no value is set.
	 */

	public Object getPropertyFromElement( ReportDesign design,
			ElementPropertyDefn prop )
	{
		Object value = null;

		// 1). If we can find the value here, return it.

		value = getLocalProperty( design, prop );
		if ( value != null )
			return value;

		// 2). Does the style provide the value of this property ?

		StyleElement style = getLocalStyle( );
		if ( style != null )
		{
			value = style.getLocalProperty( design, prop );
			if ( value != null )
				return value;
		}

		// Can we search the parent element and container element ?

		if ( !prop.canInherit( ) )
		{
			if ( prop.isStyleProperty( ) )
			{
				String selector = getDefn( ).getSelector( );
				value = getPropertyFromSelector( design, prop, selector );
				if ( value != null )
					return value;
			}

			return getDefaultValue( design, prop );
		}

		// 3). Does the parent provide the value of this property?

		value = getPropertyFromParent( design, prop );
		if ( value != null )
			return value;

		// 4). Check if this element predefined style provides
		// the property value

		String selector = getDefn( ).getSelector( );
		value = getPropertyFromSelector( design, prop, selector );
		if ( value != null )
			return value;

		// Is this property style property? or
		// Is this element is a style?

		if ( !prop.isStyleProperty( ) || isStyle( ) )
			return getDefaultValue( design, prop );

		return null;
	}

	/**
	 * Gets a property value given its definition. This version does the full
	 * property search as defined by the given derived component. That is, it
	 * gets the "effective" property value. The definition can be for a system
	 * or user-defined property.
	 * <p>
	 * The search won't search up the inheritance hierarchy if the non-style
	 * property is defined to not inherit. And style property won't be searched
	 * up the containment hierarchy if it'd defined to no inherit.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            definition of the property to get
	 * @return The property value, or null if no value is set.
	 */

	public Object getProperty( ReportDesign design, ElementPropertyDefn prop )
	{
		if ( prop.isIntrinsic( ) )
		{
			// This is an intrinsic system-defined property.

			return getIntrinsicProperty( prop.getName( ) );
		}

		// Repeat the search up the inheritance or style
		// hierarchy, starting with this element.

		DesignElement e = this;
		Object value;
		while ( e != null )
		{
			value = e.getPropertyFromElement( design, prop );
			if ( value != null )
				return value;

			if ( !prop.canInherit( ) || !prop.isStyleProperty( ) || isStyle( ) )
				return getDefaultValue( design, prop );

			// 5, 6, 7).Check if the container/slot predefined style provides
			// the property value

			if ( e.getContainer( ) != null )
			{
				// The predefined style of container/slot combination or slot
				// provides the property value.

				String[] selectors = e.getContainer( ).getSelectors(
						getContainerSlot( ) );
				for ( int i = 0; i < selectors.length; i++ )
				{
					value = e.getPropertyFromSelector( design, prop,
							selectors[i] );
					if ( value != null )
						return value;
				}
			}

			// 8). Try to get the value of this property from container
			// hierarchy.

			e = e.getContainer( );
		}

		// Still not found. Use the default.

		return getDefaultValue( design, prop );
	}

	/**
	 * Gets the default value of the specified property.
	 * 
	 * @param design
	 *            report design
	 * @param prop
	 *            definition of the property to get
	 * @return The default property value, or null if no default value is set.
	 */

	public Object getDefaultValue( ReportDesign design, ElementPropertyDefn prop )
	{
		if ( prop.isStyleProperty( ) )
		{
			// Does session define default value for this property ?

			Object value = design.session.getDefaultValue( prop.getName( ) );
			if ( value != null )
				return value;
		}

		return prop.getDefault( );
	}

	/**
	 * Gets selector array, which contains the predefined style of
	 * container/slot combination and slot.
	 * 
	 * @param slotID
	 *            slot id
	 * @return the selector array, which always contains two strings. The first
	 *         is the predefined style of container/slot combination, and the
	 *         second is that of slot.
	 */

	public String[] getSelectors( int slotID )
	{
		String[] selectors = {null, null};

		ElementDefn defn = getDefn( );
		SlotDefn slotDefn = defn.getSlot( slotID );
		String selector = defn.getSelector( );

		if ( slotDefn == null )
		{
			selectors[0] = selector;
			return selectors;
		}
		String slotSelector = slotDefn.getSelector( );
		if ( StringUtil.isBlank( slotSelector ) )
		{
			selectors[0] = selector;
			return selectors;
		}

		selectors[0] = selector + "-" + slotSelector; //$NON-NLS-1$
		selectors[1] = slotSelector;

		return selectors;
	}

	/**
	 * Returns property value with predefined style.
	 * 
	 * @param design
	 *            report design
	 * @param prop
	 *            definition of property to get
	 * @param selector
	 *            predefined style
	 * @return The property value, or null if no value is set.
	 */

	public Object getPropertyFromSelector( ReportDesign design,
			ElementPropertyDefn prop, String selector )
	{
		if ( design == null )
			return null;

		// Find the predefined style

		StyleElement style = design.findStyle( selector );
		if ( style != null )
		{
			Object value = style.getLocalProperty( design, prop );
			if ( value != null )
				return value;
		}

		return null;
	}

	public Object getProperty( ReportDesign design, PropertyDefn prop )
	{
		return getProperty( design, (ElementPropertyDefn) prop );
	}

	/**
	 * Returns whether this element is a style. While this information could be
	 * computed from meta-data, it is computed here for performance.
	 * 
	 * @return true if this is a style element, false otherwise.
	 */

	public boolean isStyle( )
	{
		return false;
	}

	/**
	 * Gets a property value given its internal name. This version checks only
	 * this one object. That is, it gets the "local" property value. The
	 * property name must also be valid for this object. The name can be a
	 * built-in property name, or a user-defined property name. The value is set
	 * without checking the name for validity.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            name of the property to set. Must be valid. Can be a
	 *            system-defined or user-defined property name.
	 * @return the property value, or null if no value is set
	 */

	public Object getLocalProperty( ReportDesign design, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;
		return getLocalProperty( design, prop );
	}

	/**
	 * Gets a property value given its definition. This version checks only this
	 * one object. That is, it gets the "local" property value. The property
	 * name must also be valid for this object.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            The property definition.
	 * @return The property value, or null if no value is set.
	 */

	public Object getLocalProperty( ReportDesign design,
			ElementPropertyDefn prop )
	{
		if ( prop.isIntrinsic( ) )
		{
			// This is an intrinsic system-defined property.

			return getIntrinsicProperty( prop.getName( ) );
		}

		// Get the value of a non-intrinsic property.

		Object value = propValues.get( prop.getName( ) );
		if ( value == null || design == null )
			return value;

		// Cache an element reference property if necessary.

		if ( prop.getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE )
		{
			resolveElementReference( design, prop, (ElementRefValue) value );
		}

		return value;
	}

	/**
	 * Returns the value of an intrinsic property. Derived classes should
	 * override this to handle their own intrinsics. Clients should call the
	 * getProperty( ) or getLocalProperty( ) methods instead of calling this
	 * method directly.
	 * 
	 * @param propName
	 *            name of the intrinsic
	 * @return the value of the requested property
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( propName.equals( NAME_PROP ) )
			return name;
		if ( propName.equals( EXTENDS_PROP ) )
			return extendsRef;
		assert false;
		return null;
	}

	/**
	 * Sets the value of an intrinsic property. This should be called only from
	 * a command or the design parser. The property name must be valid, and the
	 * value must be valid for that property.
	 * <p>
	 * This class has two intrinsic properties:
	 * <ul>
	 * <li><strong>name </strong>-- The element name. The property value must
	 * be null or a String.
	 * <li><strong>extends </strong>-- The element that this element extends.
	 * Must be a String (for an unresolved name), DesignElement (for a resolved
	 * name) or null. The caller should ensure canExtend of this element is
	 * true.
	 * </ul>
	 * 
	 * @param propName
	 *            The name of the intrinsic property.
	 * @param value
	 *            The value to set for the property.
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( propName.equals( NAME_PROP ) )
		{
			setName( (String) value );
		}
		else if ( propName.equals( EXTENDS_PROP ) )
		{
			assert getDefn( ).canExtend( );
			assert !( value instanceof ElementRefValue );

			if ( value == null || value instanceof DesignElement )
				setExtendsElement( (DesignElement) value );
			else
				setExtendsName( (String) value );
		}
		else
		{
			assert false;
		}
	}

	/**
	 * Sets the value of a property. The value must have already been validated,
	 * and must be of the correct type for the property. The property name must
	 * also be valid for this object. The name can represent a system or
	 * user-defined property. The value is set locally. If the value is null,
	 * then the property is "unset."
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param propName
	 *            The name of the property to set. Must be valid. Can be a
	 *            system-defined or user-defined property name.
	 * @param value
	 *            The value to set. Must be valid for the property.
	 */

	public void setProperty( String propName, Object value )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );

		// Properties should be set using a command, and the command should
		// have done all the required checks for property validity.

		assert prop != null;
		if ( prop != null )
			setProperty( prop, value );
	}

	/**
	 * Sets the value of a property. The value must have already been validated,
	 * and must be of the correct type for the property. The property must be
	 * valid for this object. The property can be a system or user-defined
	 * property. The value is set locally. If the value is null, then the
	 * property is "unset."
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param prop
	 *            The property definition. Must be valid. Can be a
	 *            system-defined or user-defined property.
	 * @param value
	 *            The value to set. Must be valid for the property.
	 */

	public void setProperty( ElementPropertyDefn prop, Object value )
	{
		assert prop != null;

		String propName = prop.getName( );

		// Intrinsic properties are set by calling a method to set
		// a member variable.

		if ( prop.isIntrinsic( ) )
		{
			setIntrinsicProperty( propName, value );
			return;
		}

		// Handle caching for element references.

		if ( prop.getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE )
		{
			ElementRefValue oldRef = (ElementRefValue) propValues
					.get( propName );
			updateReference( oldRef, (ElementRefValue) value, prop );
		}

		// Set or clear the property.

		if ( value == null )
			propValues.remove( propName );
		else
			propValues.put( propName, value );
	}

	/**
	 * Returns the mask of the given property. If the mask of one property has
	 * not been found, looks for its parent or any ancestor.
	 * <p>
	 * Note that this method is only for internal usage. DO NOT call this method
	 * outside the org.eclipse.birt.report.model package.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            the name of the property
	 * 
	 * @return the mask of the given property
	 */

	public String getPropertyMask( ReportDesign design, String propName )
	{
		DesignElement e = this;
		String value = null;

		boolean found = false;
		do
		{
			ArrayList masks = (ArrayList) e.getLocalProperty( design,
					DesignElement.PROPERTY_MASKS_PROP );

			if ( masks != null )
			{
				for ( int i = 0; i < masks.size( ); i++ )
				{
					PropertyMask mask = (PropertyMask) masks.get( i );
					if ( propName.equalsIgnoreCase( mask.getName( ) ) )
					{
						value = mask.getMask( );
						found = true;
						break;
					}
				}
			}

			e = e.getExtendsElement( );

		} while ( ( e != null ) && ( found == false ) );

		return value;
	}

	/**
	 * Implements to cache a back-pointer from a referenced element. This
	 * element has an element reference property that can point to another
	 * "referencable" element. To maintain semantic consistency, the referenced
	 * element maintains a list of "clients" that identifies the elements that
	 * refer to it. The client list is used when the target element changes
	 * names or is deleted. In these cases, the change automatically updates the
	 * clients as well.
	 * <p>
	 * References can be in two states: resovled and unresolved. An unresolved
	 * reference is just a name, but the system has not yet identified the
	 * target element, or if a target even exists. A resolved reference caches a
	 * pointer to the target element itself.
	 * 
	 * @param oldRef
	 *            the old reference, if any
	 * @param newRef
	 *            the new reference, if any
	 * @param prop
	 *            definition of the property
	 */

	protected void updateReference( ElementRefValue oldRef,
			ElementRefValue newRef, ElementPropertyDefn prop )
	{
		ReferenceableElement target;

		// Drop the old reference. Clear the back pointer from the referenced
		// element to this element.

		if ( oldRef != null )
		{
			target = oldRef.getTargetElement( );
			if ( target != null )
				target.dropClient( this );
		}

		// Add the new reference. Cache a back pointer from the referenced
		// element to this element. Include the property name so we know which
		// property to adjust it the target is deleted.

		if ( newRef != null )
		{
			target = newRef.getTargetElement( );
			if ( target != null )
				target.addClient( this, prop.getName( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#setProperty(org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public void setProperty( PropertyDefn prop, Object value )
	{
		setProperty( (ElementPropertyDefn) prop, value );
	}

	/**
	 * Clears the local value of the property. See <code>setProperty( )</code>
	 * for details. This is equivalent to calling
	 * <code>setProperty( propName, null )</code>.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param propName
	 *            The name of the property to set. Must be valid. Can be a
	 *            system-defined or user-defined property name.
	 */

	public void clearProperty( String propName )
	{
		setProperty( propName, null );
	}

	/**
	 * Returns the definition object for this element.
	 * <p>
	 * Part of: Meta data system.
	 * 
	 * @return The element definition. Will always be non-null in a valid build.
	 */

	public ElementDefn getDefn( )
	{
		if ( cachedDefn != null )
			return cachedDefn;

		// The find( ) method will throw an assertion if the meta data
		// information
		// cannot be found. This case is a programming error, and should never
		// occur in the field.

		cachedDefn = MetaDataDictionary.getInstance( ).getElement(
				getElementName( ) );
		assert cachedDefn != null;
		return cachedDefn;
	}

	/**
	 * Adds a user-defined property. The property must be unique within the
	 * inheritance hierarchy of this element. It cannot duplicate either another
	 * user-defined property or a system-defined property. These checks must
	 * have been enforced by the application prior to calling this method.
	 * <p>
	 * Part of: User-defined property system.
	 * 
	 * @param userProp
	 *            definition of the user-defined property
	 */

	public void addUserPropertyDefn( UserPropertyDefn userProp )
	{
		assert getDefn( ).allowsUserProperties( );
		assert userProp != null;
		String propName = userProp.getName( );
		assert getUserPropertyDefn( propName ) == null;
		assert getDefn( ).getProperty( propName ) == null;
		if ( userProperties == null )
			userProperties = new HashMap( );
		userProperties.put( propName, userProp );
	}

	/**
	 * Drops a user-defined property. The application must have previously
	 * removed all existing values for that property. The application must have
	 * previously validated that the user-defined property does exist, and is
	 * defined on this element.
	 * <p>
	 * Part of: User-defined property system.
	 * 
	 * @param prop
	 *            The user-defined property.
	 */

	public void dropUserPropertyDefn( UserPropertyDefn prop )
	{
		assert getDefn( ).allowsUserProperties( );
		assert userProperties != null;
		assert userProperties.get( prop.getName( ) ) == prop;
		userProperties.remove( prop.getName( ) );
	}

	/**
	 * Gets a user property definition defined on this class itself.
	 * <p>
	 * Part of: User-defined property system.
	 * 
	 * @param propName
	 *            The internal name of the user-defined property.
	 * @return The user-defined property definition, if any.
	 */

	public UserPropertyDefn getLocalUserPropertyDefn( String propName )
	{
		if ( userProperties == null )
			return null;
		return (UserPropertyDefn) userProperties.get( propName );
	}

	/**
	 * Gets the definition of a user-defined property defined in this element or
	 * somewhere up the inheritance chain.
	 * <p>
	 * Part of: User-defined property system.
	 * 
	 * @param propName
	 *            The internal name of the user-defined property.
	 * @return The property definition, if found, null otherwise.
	 */

	public UserPropertyDefn getUserPropertyDefn( String propName )
	{
		DesignElement e = this;
		while ( e != null )
		{
			UserPropertyDefn p = e.getLocalUserPropertyDefn( propName );
			if ( p != null )
				return p;
			e = e.getExtendsElement( );
		}
		return null;
	}

	/**
	 * Gets the cached parent element. There will be no parent if either 1) the
	 * element does not extend from another user-defined element, or 2) the
	 * extends element is undefined.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @return parent element
	 */

	public DesignElement getExtendsElement( )
	{
		if ( extendsRef == null )
			return null;
		return extendsRef.getElement( );
	}

	/**
	 * Gets the name of the element that this element extends.
	 * 
	 * @return The name of the extended element.
	 */

	public String getExtendsName( )
	{
		if ( extendsRef == null )
			return null;
		return extendsRef.getName( );
	}

	/**
	 * Returns the optional element name.
	 * <p>
	 * Part of: The name system.
	 * 
	 * @return Returns the name.
	 */

	public String getName( )
	{
		return name;
	}

	/**
	 * Sets the element name. The caller must have cleaned up any references to
	 * the old name, have changed any name spaces as needed, have verified the
	 * name, and so on.
	 * <p>
	 * Part of: The name system.
	 * 
	 * @param newName
	 *            The name to set.
	 */

	public void setName( String newName )
	{
		name = newName;
	}

	/**
	 * Returns the container element.
	 * <p>
	 * Part of: The containment system.
	 * 
	 * @return Returns the container.
	 */

	public DesignElement getContainer( )
	{
		return container;
	}

	/**
	 * Caches the child-to-container relationship. The caller must have
	 * validated that the relationship is valid.
	 * <p>
	 * Part of: The containment system.
	 * 
	 * @param obj
	 *            the container to set
	 * @param slot
	 *            the slot within the container where this element resides
	 */

	public void setContainer( DesignElement obj, int slot )
	{
		container = obj;
		containerSlotID = slot;
	}

	/**
	 * Gets the property data for either a system-defined or user-defined
	 * property.
	 * <p>
	 * Part of: Property system and user-defined property system
	 * 
	 * @param propName
	 *            The name of the property to lookup.
	 * @return The property definition, or null, if the property is undefined.
	 */

	public ElementPropertyDefn getPropertyDefn( String propName )
	{
		// Look for the property defined on this element.

		ElementPropertyDefn prop = getDefn( ).getProperty( propName );
		if ( prop == null )
			prop = getUserPropertyDefn( propName );
		return prop;
	}

	/**
	 * Validates that the given property name is either a system-defined or
	 * user-defined property.
	 * <p>
	 * Part of: Property system.
	 * 
	 * @param propName
	 *            The name of the property to validate.
	 * @throws PropertyNameException
	 *             If the property is undefined.
	 */

	void validatePropertyName( String propName ) throws PropertyNameException
	{
		if ( getPropertyDefn( propName ) == null )
			throw new PropertyNameException( this, propName );
	}

	/**
	 * Validates the given property value. Validation may map the value onto a
	 * new value that should be used for the actual property update.
	 * <p>
	 * Part of: Property system.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            The name of the property to validate.
	 * @param value
	 *            The value to validate
	 * @return The validated value.
	 * @throws PropertyNameException
	 *             If the property is undefined.
	 * @throws PropertyValueException
	 *             If the value is incorrect.
	 */

	public Object validatePropertyValue( ReportDesign design, String propName,
			Object value ) throws PropertyNameException, PropertyValueException
	{
		// If we can't find the property, then the property name
		// must be invalid.

		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			throw new PropertyNameException( this, propName );

		// Validate the property value.

		try
		{
			return prop.validateValue( design, value );
		}
		catch ( PropertyValueException e )
		{
			// Fill in the context information for the exception.

			e.setElement( this );
			e.setPropertyName( propName );
			throw e;
		}
	}

	/**
	 * Returns the shared style referenced by this element. Returns the
	 * reference for this element itself; does not search up the inheritance
	 * hierarchy.
	 * 
	 * @return the shared style, or null if this element does not explicitly
	 *         reference a shared style.
	 */

	public StyleElement getLocalStyle( )
	{
		return null;
	}

	/**
	 * Returns the style, if any, associated with this element or one of the
	 * elements that this element extends.
	 * <p>
	 * Part of: Style system.
	 * 
	 * @return the shared style, or null if this element does not explicitly
	 *         reference a shared style, or inherit such a reference
	 */

	public StyleElement getStyle( )
	{
		return null;
	}

	/**
	 * Caches an element that derives from this element.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @param child
	 *            The new derived element.
	 */

	protected void addDerived( DesignElement child )
	{
		if ( derived == null )
			derived = new ArrayList( );
		assert child != null;
		assert child.getExtendsElement( ) == this;
		assert !derived.contains( child );
		assert child.getDefn( ) == getDefn( );
		derived.add( child );
	}

	/**
	 * Removes a cached element that derives from this element.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @param child
	 *            The old derived element.
	 */

	protected void dropDerived( DesignElement child )
	{
		assert derived != null;
		assert child != null;
		assert child.getExtendsElement( ) == this;
		assert derived.contains( child );
		derived.remove( child );
	}

	/**
	 * Sets the parent element. This version should be used to implement a
	 * command to change the parent element. The caller must have validated that
	 * the new parent is valid for this element.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @param base
	 *            The new parent element, or null if the element is to have no
	 *            base element.
	 */

	public void setExtendsElement( DesignElement base )
	{
		DesignElement oldExtends = getExtendsElement( );
		if ( base == oldExtends )
			return;
		if ( oldExtends != null )
			oldExtends.dropDerived( this );
		if ( base == null )
		{
			extendsRef = null;
		}
		else
		{
			// The parent must be of the same type as this element.

			assert base.getDefn( ) == getDefn( );

			// The name of the parent must be defined.

			assert base.getName( ) != null;

			if ( extendsRef == null )
				extendsRef = new ElementRefValue( );
			extendsRef.resolve( base );
			base.addDerived( this );
		}
	}

	/**
	 * Returns a list of the descendents of this element. The descendents
	 * include elements that directly extend from this one, along with elements
	 * that extend from those, and so on recursively.
	 * 
	 * @return The list of descendents.
	 */

	public List getDescendents( )
	{
		ArrayList list = new ArrayList( );
		gatherDescendents( list );
		return list;
	}

	/**
	 * Builds up a list of the elements that extend from this one, directly or
	 * indirectly.
	 * 
	 * @param list
	 *            The list of descendents.
	 */

	public void gatherDescendents( ArrayList list )
	{
		if ( derived == null )
			return;
		for ( int i = 0; i < derived.size( ); i++ )
		{
			DesignElement child = (DesignElement) derived.get( i );
			list.add( child );
			child.gatherDescendents( list );
		}
	}

	/**
	 * Checks if the element has defined any user properties. Checks only this
	 * element, not its parents.
	 * 
	 * @return True if the element has user properties, false if not.
	 */

	public boolean hasUserProperties( )
	{
		return userProperties != null && !userProperties.isEmpty( );
	}

	/**
	 * Checks if this element has any locally-defined property values.
	 * 
	 * @return True if the element has property values, false if not.
	 */

	public boolean hasLocalPropertyValues( )
	{
		return !propValues.isEmpty( );
	}

	/**
	 * Returns the container interface for this element if this element is a
	 * container. Derived classes that represent containers must override this.
	 * <p>
	 * Part of: Containment system.
	 * 
	 * @param slot
	 *            the slot ID to get
	 * @return The container interface, or null if this element is not a
	 *         container.
	 */

	public ContainerSlot getSlot( int slot )
	{
		return null;
	}

	/**
	 * Returns a list of user properties defined in this element and somewhere
	 * up the inheritance chain.
	 * 
	 * @return The list of user properties.
	 */

	public List getUserProperties( )
	{
		List props = new ArrayList( );
		DesignElement e = this;
		while ( e != null )
		{
			List prop = e.getLocalUserProperties( );
			if ( prop != null )
			{
				props.addAll( prop );
			}
			e = e.getExtendsElement( );
		}
		return props;
	}

	/**
	 * Returns a list of user properties that are defined only on this element.
	 * 
	 * @return The list of user properties.
	 */

	public List getLocalUserProperties( )
	{
		if ( userProperties == null )
			return Collections.EMPTY_LIST;

		return new ArrayList( userProperties.values( ) );
	}

	/**
	 * Checks if this element derives from the given element. The check is true
	 * if this element derives from the target, or if the target is an ancestor
	 * of this element.
	 * 
	 * @param element
	 *            The potential ancestor.
	 * @return True if the given element is an ancestor of this element, false
	 *         otherwise.
	 */

	public boolean isKindOf( DesignElement element )
	{
		DesignElement e = this;
		while ( e != null )
		{
			if ( e == element )
				return true;
			e = e.getExtendsElement( );
		}
		return false;
	}

	/**
	 * Determines if any elements extend this element.
	 * 
	 * @return True if the element is extended, false if not.
	 */

	public boolean hasDerived( )
	{
		return derived != null && derived.size( ) > 0;
	}

	/**
	 * Returns the optional display name. Note: set the display name by calling
	 * the {@link #setProperty( String, Object )}method.
	 * <p>
	 * Part of: The name system.
	 * 
	 * @return Returns the displayName.
	 */

	public String getDisplayName( )
	{
		return (String) getLocalProperty( null, DISPLAY_NAME_PROP );
	}

	/**
	 * Returns the optional message id used to localize the display name. Note:
	 * set the display name id by calling the
	 * {@link #setProperty( String, Object )}method.
	 * <p>
	 * Part of: The name system.
	 * 
	 * @return Returns the displayNameID.
	 */

	public String getDisplayNameID( )
	{
		return (String) getLocalProperty( null, DISPLAY_NAME_ID_PROP );
	}

	/**
	 * Sets the unique ID for this element. The ID can be set only once, when
	 * the element is first added to the design.
	 * 
	 * @param newID
	 *            The id to set.
	 */

	public void setID( int newID )
	{
		assert id == 0;
		assert container == null;
		id = newID;
	}

	/**
	 * Returns the unique element ID. The ID will be valid only if element IDs
	 * were enabled in the MetaDataDictionary.
	 * 
	 * @return The unique ID. Returns 0 if element IDs are not enabled.
	 */

	public int getID( )
	{
		return id;
	}

	/**
	 * Determines if this element is contained in the given element. Checks up
	 * the containment hierarchy.
	 * 
	 * @param element
	 *            The potential container.
	 * @return True if this element is contained in the container either
	 *         directly or indirectly. False if this element is not contained.
	 */

	public boolean isContentOf( DesignElement element )
	{
		DesignElement e = this;
		while ( e != null )
		{
			if ( e == element )
				return true;
			e = e.container;
		}
		return false;
	}

	/**
	 * Returns the value of a property as a number (BigDecimal.)
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            the name of the property to get
	 * @return the property as a BigDecimal, or null if the value is not set or
	 *         cannot convert to a number
	 */

	public BigDecimal getNumberProperty( ReportDesign design, String propName )
	{
		Object value = getProperty( design, propName );
		if ( value == null )
			return null;
		if ( value instanceof BigDecimal )
			return (BigDecimal) value;

		ElementPropertyDefn prop = getPropertyDefn( propName );
		PropertyType type = MetaDataDictionary.getInstance( ).getPropertyType(
				prop.getTypeCode( ) );
		return type.toNumber( design, value );
	}

	/**
	 * Applies a design visitor. The derived element calls the corresponding
	 * visitMumble( ) method in the visitor. The visitor allows an algorithm to
	 * be implemented in a single visitor class, rather than all across the
	 * element inheritance hierarchy.
	 * 
	 * @param visitor
	 *            The visitor to apply.
	 */

	public abstract void apply( ElementVisitor visitor );

	/**
	 * Gets a property converted to a string value.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            The name of the property to get.
	 * @return The property value as a string.
	 */

	public String getStringProperty( ReportDesign design, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;
		Object value = getProperty( design, prop );
		if ( value == null )
			return null;
		return prop.getStringValue( design, value );
	}

	/**
	 * Returns the value of the property in a localize format. Formats dates and
	 * numbers in the form according to the current locale. Converts choices and
	 * colors to their localized display names. Returns the localized display
	 * name for an element reference, etc.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            the property name
	 * @return the localized display value of the property
	 */

	public String getDisplayProperty( ReportDesign design, String propName )
	{
		PropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;
		return getDisplayProperty( design, prop );
	}

	/**
	 * Returns the value of the property in a localize format. Formats dates and
	 * numbers in the form according to the current locale. Converts choices and
	 * colors to their localized display names. Returns the localized display
	 * name for an element reference, etc.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            the property definition
	 * @return the localized display value of the property
	 */

	public String getDisplayProperty( ReportDesign design, PropertyDefn prop )
	{
		Object value = getProperty( design, prop );
		if ( value == null )
			return null;
		return prop.getDisplayValue( design, value );
	}

	/**
	 * Sets an unresolved extends name. The resolved relationships is not
	 * cached; it must be resolved later.
	 * 
	 * @param name
	 *            the name of the new parent element, or null if this element
	 *            does not extend any other element
	 */

	public void setExtendsName( String name )
	{
		setExtendsElement( null );
		name = StringUtil.trimString( name );
		if ( name == null )
			return;
		if ( extendsRef == null )
			extendsRef = new ElementRefValue( );
		extendsRef.unresolved( name );
	}

	/**
	 * Validates this element and its contents.
	 * 
	 * @param design
	 *            the report design
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	public final List validateWithContents( ReportDesign design )
	{
		List list = validate( design );

		int count = getDefn( ).getSlotCount( );
		for ( int i = 0; i < count; i++ )
		{
			Iterator iter = getSlot( i ).iterator( );
			while ( iter.hasNext( ) )
			{
				list.addAll( ( (DesignElement) iter.next( ) )
						.validateWithContents( design ) );
			}
		}

		return list;
	}

	/**
	 * Validates only this element without its contents, and return error list.
	 * The derived class should override this method to define specific
	 * validation rules.
	 * 
	 * @param design
	 *            the report design
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	public List validate( ReportDesign design )
	{
		List list = new ArrayList( );

		// Check whether this element is unsupported element.

		String elementName = getElementName( );

		for ( int i = 0; i < unSupportedElements.length; i++ )
		{
			if ( unSupportedElements[i].equalsIgnoreCase( elementName ) )
			{
				list.add( new SemanticError( this,
						SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT,
						SemanticError.WARNING ) );
			}
		}

		// check property masks.

		ArrayList propMasks = (ArrayList) getLocalProperty( design,
				PROPERTY_MASKS_PROP );

		if ( propMasks != null )
		{
			ListIterator masks = propMasks.listIterator( );
			while ( masks.hasNext( ) )
			{
				PropertyMask mask = (PropertyMask) masks.next( );
				list.addAll( mask.validate( design, this ) );
			}
		}

		return list;
	}

	/**
	 * Checks all structures in the specific property whose type is structure
	 * list property type. This method is used for element semantic check. The
	 * error is kept in the report design's error list.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            the name of the structure list type
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	protected List validateStructureList( ReportDesign design, String propName )
	{
		return StructureListValidator.getInstance( ).validate(
				design, this, propName );
	}

	/**
	 * Checks all structures in the specific property whose type is structure
	 * list property type. This method is used in command. If any error is
	 * found, the exception will be thrown.
	 * 
	 * @param design
	 *            the report design
	 * @param propDefn
	 *            the property definition of the list property
	 * @param list
	 *            the structure list to check
	 * @param toAdd
	 *            the structure to add. This parameter maybe is
	 *            <code>null</code>.
	 * @throws PropertyValueException
	 *             if the structure list or the structure to add has any
	 *             semantic error..
	 */

	public void checkStructureList( ReportDesign design, PropertyDefn propDefn,
			List list, IStructure toAdd ) throws PropertyValueException
	{
		List errorList = StructureListValidator.getInstance( ).validateForAdding(
				getHandle( design ), propDefn, list, toAdd );
		if ( errorList.size( ) > 0 )
		{
			throw (PropertyValueException) errorList.get( 0 );
		}
	}


	/**
	 * Checks the parent element.
	 * 
	 * @param parent
	 *            the parent element
	 * @throws ExtendsException
	 *             throws <code>ExtendsException</code> if the parent element
	 *             is <code>NOT_FOUND</code>,<code>WRONG_TYPE</code>,
	 *             <code>SELF_EXTEND</code>,<code>CIRCULAR</code>.
	 */

	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		String extendsName = getExtendsName( );
		ElementDefn defn = getDefn( );

		if ( parent == null )
		{
			throw new ExtendsException( this, extendsName,
					ExtendsException.DESIGN_EXCEPTION_NOT_FOUND );
		}
		else if ( parent.getDefn( ) != defn )
		{
			throw new ExtendsException( this, parent,
					ExtendsException.DESIGN_EXCEPTION_WRONG_TYPE );
		}
		else if ( parent == this )
		{
			throw new ExtendsException( this, extendsName,
					ExtendsException.DESIGN_EXCEPTION_SELF_EXTEND );
		}
		else if ( parent.isKindOf( this ) )
		{
			throw new ExtendsException( this, parent,
					ExtendsException.DESIGN_EXCEPTION_CIRCULAR );
		}

	}

	/**
	 * Gets the value of a property as an integer.
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @param design
	 *            the report design
	 * @return the property value as an integer. Returns 0 if the property is
	 *         not set, or cannot convert to an integer.
	 */

	public int getIntProperty( ReportDesign design, String propName )
	{
		Object value = getProperty( design, propName );
		if ( value == null )
			return 0;
		if ( value instanceof Integer )
			return ( (Integer) value ).intValue( );

		ElementPropertyDefn prop = getPropertyDefn( propName );
		PropertyType type = MetaDataDictionary.getInstance( ).getPropertyType(
				prop.getTypeCode( ) );
		return type.toInteger( design, value );
	}

	/**
	 * Gets the value of a property as a float (double).
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @param design
	 *            the report design
	 * @return the property value as a double. Returns 0 if the property is not
	 *         set, or cannot convert to a double.
	 */

	public double getFloatProperty( ReportDesign design, String propName )
	{
		Object value = getProperty( design, propName );
		if ( value == null )
			return 0;
		if ( value instanceof Double )
			return ( (Double) value ).doubleValue( );

		ElementPropertyDefn prop = getPropertyDefn( propName );
		PropertyType type = MetaDataDictionary.getInstance( ).getPropertyType(
				prop.getTypeCode( ) );
		return type.toDouble( design, value );
	}

	/**
	 * Gets the value of a property as a boolean.
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @param design
	 *            the report design
	 * @return the property value as a boolean. Returns false if the property is
	 *         not set, or cannot convert to a boolean.
	 */

	public boolean getBooleanProperty( ReportDesign design, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		PropertyType type = MetaDataDictionary.getInstance( ).getPropertyType(
				prop.getTypeCode( ) );
		assert type instanceof BooleanPropertyType;
		Object value = getProperty( design, propName );
		return ( (BooleanPropertyType) type ).toBoolean( design, value );
	}

	/**
	 * Gets the value of a property as a list.
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @param design
	 *            the report design
	 * @return the value as an <code>ArrayList</code>, or null if the
	 *         property is not set or the value is not a list
	 */

	public List getListProperty( ReportDesign design, String propName )
	{
		Object value = getProperty( design, propName );
		if ( value == null )
			return null;
		if ( value instanceof ArrayList )
			return (ArrayList) value;
		return null;
	}

	/**
	 * Gets the set of property definitions available to this element. Includes
	 * all properties defined for this element, all user-defined properties
	 * defined on this element or its ancestors, and any style properties that
	 * this element supports.
	 * 
	 * @return a list of property definitions
	 */

	public List getPropertyDefns( )
	{
		List list = getDefn( ).getProperties( );
		DesignElement e = this;
		while ( e != null )
		{
			if ( e.userProperties != null )
				list.addAll( e.userProperties.values( ) );
			e = e.getExtendsElement( );
		}
		return list;
	}

	/**
	 * Returns the internal name of the element. This name matches the name of
	 * the definition for the element.
	 * 
	 * @return the internal element type name for this element
	 */

	public abstract String getElementName( );

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element.
	 */

	public abstract DesignElementHandle getHandle( ReportDesign design );

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	public boolean equals( Object obj )
	{
		if ( obj == null )
			return false;
		if ( obj == this )
			return true;
		if ( obj instanceof DesignElementHandle )
			return ( (DesignElementHandle) obj ).getElement( ) == this;
		return false;
	}

	/**
	 * Returns the slot that contains the given content element.
	 * 
	 * @param content
	 *            the element to find
	 * @return the number of the slot that contains the element, or -1 if this
	 *         element does not contain the content element.
	 */

	public int findSlotOf( DesignElement content )
	{
		ElementDefn defn = getDefn( );
		int count = defn.getSlotCount( );
		for ( int i = 0; i < count; i++ )
		{
			if ( getSlot( i ).contains( content ) )
				return i;
		}
		return -1;
	}

	/**
	 * Determines if the slot can contain a given element.
	 * 
	 * @param design
	 *            the report design
	 * @param slotId
	 *            the slot id
	 * @param element
	 *            the element to insert
	 * @return <code>true</code> if the current element can contain the
	 *         <code>element</code>, otherwise <code>false</code>.
	 */

	public final boolean canContain( ReportDesign design, int slotId,
			DesignElement element )
	{
		boolean retValue = canContainInRom( slotId, element.getDefn( ) );
		if ( !retValue )
			return retValue;

		// special cases check table header containment.

		DesignElement tmpContainer = this;

		while ( tmpContainer != null )
		{
			if ( tmpContainer instanceof ListingElement )
			{
				List errors = tmpContainer.checkContent( design, this, slotId,
						element );
				return errors.isEmpty( );
			}

			tmpContainer = tmpContainer.getContainer( );
		}

		return retValue;
	}

	/**
	 * Determines if the current element can contain an element with the
	 * definition of <code>elementType</code> on context containment.
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @param slotId
	 *            the slot id
	 * @param defn
	 *            the definition of the element
	 * @return <code>true</code> if the slot can contain the an element,
	 *         otherwise <code>false</code>.
	 */

	public final boolean canContain( ReportDesign design, int slotId,
			ElementDefn defn )
	{
		assert defn != null;

		boolean retValue = canContainInRom( slotId, defn );
		if ( !retValue )
			return retValue;

		DesignElement tmpContainer = this;

		// special cases check table header containment.

		while ( tmpContainer != null )
		{
			if ( tmpContainer instanceof ListingElement )
			{
				List errors = tmpContainer.checkContent( design, this, slotId,
						defn );
				return errors.isEmpty( );
			}

			tmpContainer = tmpContainer.getContainer( );
		}

		return retValue;
	}

	/**
	 * Checks whether a type of elements can reside in the given slot of the
	 * current element.
	 * 
	 * @param slotId
	 *            the slot id of the current element
	 * @param defn
	 *            the element definition
	 * 
	 * @return <code>true</code> if elements with the definition
	 *         <code>defn</code> can reside in the given slot. Otherwise
	 *         <code>false</code>.
	 */

	private boolean canContainInRom( int slotId, ElementDefn defn )
	{
		if ( slotId < 0 || slotId >= getDefn( ).getSlotCount( ) )
			return false;

		SlotDefn slotDefn = getDefn( ).getSlot( slotId );
		assert slotDefn != null;

		return slotDefn.canContain( defn );
	}

	/**
	 * Checks whether the <code>content</code> can be inserted to the slot
	 * <code>slotId</code> in another element <code>container</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param container
	 *            the container element
	 * @param slotId
	 *            the slot id of the container element
	 * @param content
	 *            the target element to be inserted
	 * @return <code>true</code> if this insertion is valid. Otherwise
	 *         <code>false</code>.
	 */

	protected List checkContent( ReportDesign design, DesignElement container,
			int slotId, DesignElement content )
	{
		return new ArrayList( );
	}

	/**
	 * Checks whether elements with the given element definition can be inserted
	 * to the slot <code>slotId</code> in another element
	 * <code>container</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param container
	 *            the container element
	 * @param slotId
	 *            the slot id of the container element
	 * @param defn
	 *            the element definition
	 * @return <code>true</code> if this insertion is valid. Otherwise
	 *         <code>false</code>.
	 */

	protected List checkContent( ReportDesign design, DesignElement container,
			int slotId, ElementDefn defn )
	{
		return new ArrayList( );
	}

	/**
	 * Returns the slot within the container element that holds this element.
	 * 
	 * @return the container's slot in which this element appears
	 */

	public int getContainerSlot( )
	{
		return containerSlotID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#getObjectDefn()
	 */

	public ObjectDefn getObjectDefn( )
	{
		return getDefn( );
	}

	/**
	 * Returns the value of an element reference property as an element. Returns
	 * null if either the property is unset, or the reference is unresolved.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            the name of the element reference property to get
	 * @return the element referenced by the property
	 */

	public DesignElement getReferenceProperty( ReportDesign design,
			String propName )
	{
		ElementRefValue ref = (ElementRefValue) getProperty( design, propName );
		if ( ref == null )
			return null;
		return ref.getElement( );
	}

	/**
	 * Returns whether this element has references. That is, whether any other
	 * elements reference this one using an element reference property. This
	 * query does not check for "extends" references.
	 * 
	 * @return whether other elements refer to this one
	 */

	public boolean hasReferences( )
	{
		return false;
	}

	/**
	 * Tests whether the element has a private style defined on.
	 * 
	 * @return <code>true</code> if has, otherwise <code>false</code>.
	 * 
	 * @deprecated only <code>StyledElement</code> has the private style.
	 *             Other elements not.
	 */

	public boolean hasPrivateStyle( )
	{
		Iterator it = propValues.keySet( ).iterator( );
		String propName;
		while ( it.hasNext( ) )
		{
			propName = (String) it.next( );
			if ( getPropertyDefn( propName ).isStyleProperty( ) )
			{
				return true;
			}
		}

		DesignElement parent = getExtendsElement( );
		if ( parent != null )
		{
			return parent.hasPrivateStyle( );
		}

		return false;
	}

	/**
	 * Creates the Factory data structures for specialized property access. This
	 * method uses specialized property resolution rules:
	 * <p>
	 * <ul>
	 * <li>A property value is either a style property or a non-style property.
	 * </li>
	 * <li>A non-style property is set if this element, or any of its ancestor
	 * elements, provide a value. It is also considered set if the system
	 * provides a default value.</li>
	 * <li>A property value is considered set only if it is set in the private
	 * style of this element or an ancestor element; it is not considered set if
	 * it is inherited from a shared style.</li>
	 * </ul>
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            definition of the property
	 * @return the value of the property according to the rules explained above
	 */

	public Object getFactoryProperty( ReportDesign design,
			ElementPropertyDefn prop )
	{
		// This class handles only non-style properties. See the
		// StyledElement class for the handling of style properties.

		assert !prop.isStyleProperty( );
		return getProperty( design, prop );
	}

	/**
	 * Creates the Factory data structures for specialized property access. This
	 * method uses specialized property resolution rules:
	 * <p>
	 * <ul>
	 * <li>A property value is either a style property or a non-style property.
	 * </li>
	 * <li>A non-style property is set if this element, or any of its ancestor
	 * elements, provide a value. It is also considered set if the system
	 * provides a default value.</li>
	 * <li>A property value is considered set only if it is set in the private
	 * style of this element or an ancestor element; it is not considered set if
	 * it is inherited from a shared style.</li>
	 * </ul>
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            name of the property
	 * @return the value of the property according to the rules explained above
	 */

	public Object getFactoryProperty( ReportDesign design, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );

		// If the property is not found, then the value is null.

		if ( prop == null )
			return null;

		return getFactoryProperty( design, prop );
	}

	/**
	 * Resolves a property element reference. The reference is the value of a
	 * property of type property element reference.
	 * 
	 * @param design
	 *            the report design information needed for the check, and
	 *            records any errors
	 * @param prop
	 *            the property whose type is element reference
	 * @param ref
	 *            reference value to resolve
	 */

	public void resolveElementReference( ReportDesign design,
			ElementPropertyDefn prop, ElementRefValue ref )
	{
		if ( ref.isResolved( ) )
			return;

		// The element exist and is not resolved. Try to resolve it.
		// If it is now resolved, cache the back pointer.
		// Note that this is a safe operation to do without the
		// use of the command stack. We are not changing the meaning
		// of the property: we are only changing the form: from name
		// to element pointer.

		ElementRefPropertyType refType = (ElementRefPropertyType) prop
				.getType( );
		refType.resolve( design, prop, ref );
		if ( ref.isResolved( ) )
			ref.getTargetElement( ).addClient( this, prop.getName( ) );
	}

	/**
	 * Attempts to resolve an element reference property. If the property is
	 * empty, or the reference is already resolved, return true. If the
	 * reference is not resolved, attempt to resolve it. If it cannot be
	 * resolved, return false.
	 * 
	 * @param design
	 *            the report design
	 * @param propName
	 *            the name of the property
	 * @return <code>true</code> if the property is resolved;
	 *         <code>false</code> otherwise.
	 */

	public boolean checkElementReference( ReportDesign design, String propName )
	{
		assert !StringUtil.isBlank( propName );

		// Is the value set?

		Object value = propValues.get( propName );
		if ( value == null )
			return true;

		// This must be an element reference property.

		ElementPropertyDefn prop = getPropertyDefn( propName );
		assert PropertyType.ELEMENT_REF_TYPE == prop.getTypeCode( );

		// Attempt to resolve the reference.

		ElementRefValue ref = (ElementRefValue) value;
		resolveElementReference( design, prop, ref );
		return ref.isResolved( );
	}

	/**
	 * Returns the list of elements that extend this one.
	 * 
	 * @return the list of elements. The list is always non-null.
	 */

	public List getDerived( )
	{
		if ( derived != null )
			return new ArrayList( derived );
		return new ArrayList( );
	}

	/**
	 * Returns the valid status of this element. Child elements need to
	 * overwrite this method to say in which condition they can be said valid or
	 * not.
	 * <p>
	 * for example, if a JDBCDataSource can not reach the server, then it's
	 * invalid.
	 * <p>
	 * UI needs this method to show different icon for each element in the user
	 * interface.
	 * 
	 * @return true if this element is valid, false otherwise.
	 */

	public boolean isValid( )
	{
		return isValid;
	}

	/**
	 * Sets whether the element is valid.
	 * 
	 * @param isValid
	 *            the valid to set
	 */

	public void setValid( boolean isValid )
	{
		this.isValid = isValid;
	}

	/**
	 * Returns the display label of this element. To get the display label of an
	 * element, the following step should be done:
	 * <ul>
	 * <li>The localized display name of this element if set</li>
	 * <li>The display property value of this element if set</li>
	 * <li>The name of element if set</li>
	 * <li>The localized display name of this kind of element, which is defined
	 * in metadata, if set</li>
	 * <li>The name of this kind of element, which is also defined in metadata
	 * </li>
	 * </ul>
	 * <p>
	 * User can also decide at which detail level the display label should be
	 * returned. The level could be one of the following 3 options:
	 * <ul>
	 * <li>USER_LABEL: Only the first 3 steps can be visited, if not found,
	 * return null</li>
	 * <li>SHORT_LABEL: All the above 5 steps can be visited. This will ensure
	 * there will be a return value</li>
	 * <li>FULL_LABEL: Besides the return value of SHORT_LABEL, this option
	 * says we need to return additional information. To get this, every child
	 * element needs to overwrite this method</li>
	 * </ul>
	 * 
	 * @param design
	 *            the report design instance
	 * @param level
	 *            the description level.
	 * @return the display label of this element.
	 */

	public String getDisplayLabel( ReportDesign design, int level )
	{
		String displayLabel = design.getMessage( getDisplayNameID( ) );
		if ( StringUtil.isBlank( displayLabel ) )
		{
			displayLabel = getDisplayName( );
		}

		if ( StringUtil.isBlank( displayLabel ) )
		{
			displayLabel = getNameForDisplayLabel( );
		}

		if ( level == USER_LABEL )
		{
			return displayLabel;
		}

		MetaDataDictionary dictionary = MetaDataDictionary.getInstance( );
		ElementDefn elementDefn = dictionary.getElement( getElementName( ) );
		if ( StringUtil.isBlank( displayLabel ) )
		{
			displayLabel = elementDefn.getDisplayName( );
		}

		if ( StringUtil.isBlank( displayLabel ) )
		{
			displayLabel = elementDefn.getName( );
		}
		return displayLabel;
	}

	/**
	 * Returns the name of this element for display label.
	 * 
	 * @return the name of this element for display label.
	 */

	protected String getNameForDisplayLabel( )
	{
		return getName( );
	}

	/**
	 * All descriptive text should be ¡°elided¡± to a length of 30 characters. To
	 * elide the text, find the first white-space before the limit, truncate the
	 * string after that point, and append three dots: ¡°¡­¡±
	 * <p>
	 * 
	 * @param displayLabel
	 *            the display label to be elided.
	 * @return the elided display label.
	 */
	protected String limitStringLength( String displayLabel )
	{
		if ( displayLabel == null )
			return null;
		if ( displayLabel.length( ) > MAX_DISPLAY_LABEL_LEN )
		{
			displayLabel = displayLabel.substring( 0, MAX_DISPLAY_LABEL_LEN );
			int pos = displayLabel.lastIndexOf( " " ); //$NON-NLS-1$
			if ( pos != -1 )
			{
				displayLabel = displayLabel.substring( 0, pos );
			}
			return displayLabel + "..."; //$NON-NLS-1$
		}
		return displayLabel;
	}

	/**
	 * Generates a clone copy of this element. When a report element is cloned,
	 * the basic principle is just copying the property value into the clone,
	 * the other things, like container references, child list references,
	 * listener references will not be cloned; that is, the clone is isolated
	 * from the design tree until it is added into a target design tree.
	 * 
	 * <p>
	 * When inserting the cloned element into the design tree, user needs to
	 * care about the element name confliction; that is, the client needs to
	 * call the method
	 * <code>{@link ReportDesignHandle#rename( DesignElement )}</code> to
	 * change the element names.
	 * 
	 * @return Object the cloned design element.
	 * @throws CloneNotSupportedException
	 *             if clone is not supported.
	 * 
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		DesignElement element = (DesignElement) super.clone( );

		element.container = null;
		element.listeners = null;
		element.derived = null;
		element.extendsRef = null;
		element.cachedDefn = null;
		element.handle = null;

		// System Properties

		Iterator it = propValues.keySet( ).iterator( );
		element.propValues = new HashMap( );
		while ( it.hasNext( ) )
		{
			String key = (String) it.next( );
			PropertyDefn propDefn = getPropertyDefn( key );

			if ( propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
			{
				if ( propDefn.isList( ) )
				{
					element.propValues
							.put( key, cloneStructList( (ArrayList) propValues
									.get( key ) ) );
				}
				else
				{
					element.propValues.put( key, ( (Structure) propValues
							.get( key ) ).copy( ) );
				}
			}
			else if ( propDefn.getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE )
			{

				// set cloned reference to be unresolved.

				ElementRefValue refValue = (ElementRefValue) propValues
						.get( key );
				ElementRefValue newRefValue = new ElementRefValue( );
				newRefValue.unresolved( refValue.getName( ) );
				element.propValues.put( key, newRefValue );

			}
			else
				element.propValues.put( key, propValues.get( key ) );
		}

		// User Properties
		if ( userProperties != null )
		{
			element.userProperties = new HashMap( );
			it = userProperties.keySet( ).iterator( );
			while ( it.hasNext( ) )
			{
				Object key = it.next( );
				UserPropertyDefn uDefn = (UserPropertyDefn) userProperties
						.get( key );
				element.userProperties.put( key, uDefn.copy( ) );
			}
		}

		return element;
	}

	/**
	 * Clone the structure list, a list value contains a list of structures.
	 * 
	 * @param list
	 *            The structure list to be cloned.
	 * @return The cloned structure list.
	 */

	public ArrayList cloneStructList( ArrayList list )
	{
		if ( list == null )
			return null;

		ArrayList returnList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object item = list.get( i );
			if ( item instanceof Structure )
			{
				returnList.add( ( (Structure) item ).copy( ) );
			}
			else
			{
				assert false;
			}
		}
		return returnList;
	}

}