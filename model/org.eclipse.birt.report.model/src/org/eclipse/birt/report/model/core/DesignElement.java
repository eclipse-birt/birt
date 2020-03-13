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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CircularExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.SimpleListValidator;
import org.eclipse.birt.report.model.api.validators.StructureListValidator;
import org.eclipse.birt.report.model.api.validators.UnsupportedElementValidator;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.ITemplateParameterDefinitionModel;
import org.eclipse.birt.report.model.elements.strategy.CopyForPastePolicy;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.IContainerDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;
import org.eclipse.birt.report.model.validators.ValidationExecutor;
import org.eclipse.birt.report.model.validators.ValidationNode;

import com.ibm.icu.util.ULocale;

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
 * Every element is defined by an <em>element definition </em>. The relationship
 * between an element and element definition is much like that between a object
 * and a class in Java. The element definition defines characteristics of the
 * element, and is fixed by the development team. The element is defined by the
 * user and contains information needed for a particular report.
 * <p>
 * The {@link #getDefn}method provides access to the element definition for the
 * particular element. The returned {@link ElementDefn}object provides access to
 * the properties defined for the element as well as other information.
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
 * <li>The {@link #validatePropertyValue}method validates a property value using
 * the rules defined in the meta-data for the * property.</li>
 * <li>The {@link ElementDefn}class provides the meta-data for an element type.</li>
 * <li>The {@link ElementPropertyDefn}class provides meta-data for a property.
 * It has two subclasses:
 * {@link org.eclipse.birt.report.model.metadata.SystemPropertyDefn}for built-in
 * properties defined in the meta-data file, and {@link UserPropertyDefn}for
 * user-defined properties.</li>
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
 * named style. The derived class overrides the {@link #getStyle()}method to
 * return the style for use by the generic mechanisms.
 * <p>
 * An element that can have a style is called a <em>styled element</em>. The
 * style affects the styled elements in three distinct ways.
 * <ol>
 * <li>The element can include some or all of the properties defined for the
 * style element. The element defines only those that make sense for that
 * element. These <em>style properties</em> appear as if they are properties of
 * the element itself.</li>
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
 * <li>The {@link #getStyle()}method returns the associated style, if any, for
 * this element as a generic DesignElement.</li>
 * </ul>
 * 
 * <h3>Inheritance</h3>
 * 
 * User-defined elements can extend other elements, but style element is not
 * allowed to extend each other. The following is the terminology used for this
 * system.
 * <ul>
 * <li>If element E extends element D, then D is said to be the <em>parent </em>
 * element, and element E is the <em>derived </em> element.</li>
 * <li>We can also use the term <em>base </em> as a synonym for parent. That is,
 * D is the base element above.</li>
 * <li>Element D may in turn extend another element C.</li>
 * <li>We say that C and D are <em>ancestor </em> elements of E, and that D and
 * E are <em>descendents </em> of C.</li>
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
 * <dt><strong>Named style </strong></dt>
 * <dd>If an ancestor element explicitly names a style, then the derived element
 * will also use that style (unless the derived element specifies a different
 * style.)</dd>
 * 
 * <dt><strong>Element type </strong></dt>
 * <dd>Inheritance only works between elements of the same type. One could say
 * that a derived element inherits the element type of the parent element. More
 * precisely, both the parent and derived elements have a type, and the
 * semantics of the application require that the element types be the same. It
 * is an error to extend an element of a different type.</dd>
 * 
 * <dt><strong>User-defined properties </strong></dt>
 * <dd>The user can define a custom property. All derived elements also have
 * that property.</dd>
 * 
 * <dt><strong>Custom behavior </strong></dt>
 * <dd>The user can define custom behavior by associating the element with a
 * Java class. Derived elements inherit this behavior.</dd>
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
 * <li>The {@link #setExtendsName}method sets the name of the parent element. It
 * is used when reading a design file, or if it is necessary to store an
 * unresolved parent name.</li>
 * <li>The {@link #getExtendsElement}method gets the parent element.</li>
 * <li>The {@link #extendsRef}member stores the reference to the parent element.
 * </li>
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
 * <li>The {@link #addUserPropertyDefn}method defines a user-defined property.</li>
 * <li>The {@link #dropUserPropertyDefn}method removes a user-defined property.</li>
 * <li>The {@link #getUserPropertyDefn}method gets the definition of a
 * user-defined property.</li>
 * <li>The {@link UserPropertyDefn}class defines a user-defined property.</li>
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
 * The <em>local value</em> of a property for an element E indicates whether or
 * not the property is set on that specific element. The local value is
 * generally of interest only to low-level code. The XML design file shows only
 * the local property values for an element.
 * <p>
 * The <em>effective value </em> of a property provides the value computed using
 * the property search which includes the ancestor elements, styles, context,
 * system-defaults, and other information. The effective value is generally what
 * the user cares about, and is what appears in the property sheet and other UI.
 * <p>
 * Members and classes in the property value system include:
 * <p>
 * <ul>
 * <li>The {@link #propValues}member stores the property values set on this
 * element.</li>
 * <li>The {@link #setProperty(String, Object )}method sets a property value on
 * this element.</li>
 * <li>The {@link #clearProperty}method unsets a property value on this method.</li>
 * <li>The {@link #getLocalProperty(Module, String )}method gets the property
 * value, if any, set on this element.</li>
 * <li>The {@link #getProperty(Module, String )}method gets the effective value
 * of the property.</li>
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
 * reside in the {@link Module Module}.
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
 * </em>. (Note: the usage of the term container in this discussion is more
 * generic than the Container element in the report design. The Container
 * element is a container, but other elements are as well.)
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
 * <li>The {@link #listeners}member holds the list of subscribed listeners.</li>
 * <li>The {@link #addListener}and {@link #removeListener}methods to add and
 * remove listeners.</li>
 * <li>The {@link Listener}class to receive notifications.</li>
 * <li>A subclass of {@link NotificationEvent}notifies the listener of the type
 * of change, and information about the change.</li>
 * <li>The {@link org.eclipse.birt.report.model.activity.ActivityStack}class
 * triggers the notifications as it processes commands.</li>
 * <li>ActivityStack calls the {@link #sendEvent}method to send the notification
 * to the appropriate listeners.</li>
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
 * <dd>Elements can be found via navigation. One can start at the module and
 * work down though the containment hierarchy. Every valid element can be
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
 * that the design is released. IDs are <em>not </em> valid across sessions, and
 * are not persistent. The application must enable IDs by calling
 * {@link org.eclipse.birt.report.model.metadata.MetaDataDictionary#enableElementID}
 * . Call {@link #getID}to get the ID of an element. Call
 * {@link Module#getElementByID}to obtain and element given an element ID.</dd>
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
 * {@link #getHandle(Module module )}method.
 * 
 * <h3>Semantic State</h3>
 * 
 * Design elements maintain a wide variety of relationships to other parts of
 * the design. An element refers to its parent element or its shared style.
 * Expressions reference elements, parameters and data set columns. Many
 * elements reference data sources or data sets.
 * <p>
 * <em>Semantic processing</em> resolves name references to the actual elements
 * or other objects. Semantic processing is done incrementally for most
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

public abstract class DesignElement
		implements
			IDesignElement,
			IPropertySet,
			IDesignElementModel
{

	/**
	 * Constant indicate that the element has no virtual parent.
	 */

	public final static long NO_BASE_ID = -1;

	/**
	 * Constant indicate that the element has no id.
	 */

	public final static long NO_ID = 0;

	/**
	 * The max length the display label for every element. If the length exceeds
	 * this limit, the exceeding part will be shown as "...".
	 */
	private static final int MAX_DISPLAY_LABEL_LEN = 30;

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
	 * Listeners are the objects that want to be notified of events. Contents
	 * are of type Listener. Created only when needed.
	 */

	protected ArrayList<Listener> listeners = null;

	/**
	 * Values for non-intrinsic property values. The contents are of type
	 * Object.
	 */

	protected Map<String, Object> propValues = new HashMap<String, Object>(
			ModelUtil.MAP_CAPACITY_LOW );

	/**
	 * Definitions for user-defined properties. Contents are of type
	 * UserPropertyDefn.
	 */

	protected HashMap<String, UserPropertyDefn> userProperties = null;

	/**
	 * Class to store all container relation ship related information.
	 */

	protected ContainerContext containerInfo = null;

	/**
	 * The set of slots for the design element. If the design element has no
	 */

	protected ContainerSlot slots[] = null;

	/**
	 * Support for inheritance. Represents the resolved or unresolved element
	 * that this element extends.
	 */

	protected ElementRefValue extendsRef = null;

	/**
	 * Inverse of the extends relationship: parent-->derived. Very few elements
	 * are extended, so we create this list only when needed.
	 */

	protected ArrayList<DesignElement> derived = null;

	/**
	 * The unique ID assigned to this element when it is added to the design.
	 * Allows web applications to refer to the element by ID.
	 */

	protected long id = NO_ID;

	/**
	 * Cached element definition. Cached for speed since the definition cannot
	 * change.
	 */

	protected IElementDefn cachedDefn = null;

	/**
	 * Indicates whether the element is valid. The initial value is true.
	 */

	protected boolean isValid = true;

	/**
	 * API handle for this element.
	 */

	protected DesignElementHandle handle = null;

	/**
	 * The validation error list, each of which is the instance of
	 * <code>SemanticException</code>.
	 */

	protected List<SemanticException> errors;

	/**
	 * Support for id inheritance. If it is set, base id must be larger than
	 * <code>0</code>.
	 */

	protected long baseId = NO_BASE_ID;

	/**
	 * Map that stores pair of propName/encryptionID.
	 */
	protected Map<String, String> encryptionMap = null;

	/**
	 * Cached search strategy.
	 */

	protected PropertySearchStrategy cachedPropStrategy = null;

	/**
	 * Default constructor.
	 */

	public DesignElement( )
	{
		this( (String) null );
	}

	/**
	 * Constructs the design element with the name.
	 * 
	 * @param theName
	 *            initial element name
	 */

	public DesignElement( String theName )
	{
		name = StringUtil.trimString( theName );
		cachedDefn = MetaDataDictionary.getInstance( ).getElement(
				getElementName( ) );

		cachedPropStrategy = PropertySearchStrategy.getInstance( );
	}

	public DesignElement( IElementDefn elementDefn )
	{
		cachedDefn = elementDefn;
		cachedPropStrategy = PropertySearchStrategy.getInstance( );
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
			listeners = new ArrayList<Listener>( );
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
	 * <p>
	 * Part of: Notification system.
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
	 * <code>ReportDesign</code> or <code>Library</code>.
	 * 
	 * @return the root node of the design tree
	 */

	public Module getRoot( )
	{
		DesignElement element = this;

		while ( element.getContainer( ) != null )
			element = element.getContainer( );

		if ( element instanceof Module == false )
			return null;

		return (Module) element;
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
		if ( this instanceof Module || getContainer( ) != null )
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
	 * @param module
	 *            the root node of the design tree.
	 */

	public void broadcast( NotificationEvent ev, Module module )
	{

		// copy a temporary ArrayList and send to all direct listeners.
		// so, there is no concurrent problem if the user changes
		// listeners in elementChanged method.

		if ( listeners != null )
		{
			ArrayList<Listener> tmpListeners = new ArrayList<Listener>(
					listeners );

			Iterator<Listener> iter = tmpListeners.iterator( );
			while ( iter.hasNext( ) )
			{
				( iter.next( ) ).elementChanged( getHandle( module ), ev );
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

			Iterator<DesignElement> iter = derived.iterator( );
			while ( iter.hasNext( ) )
			{
				( iter.next( ) ).broadcast( ev, module );
			}
		}

	}

	/**
	 * Returns <code>true</code> if the element is within a child element which
	 * extends from another. Returns <code>false</code> otherwise. <br>
	 * <strong>Notice: </strong> <br>
	 * If the element is a virtual element, it must have defined a <code>
	 * baseId<code> which point to the ID of its virtual parent.
	 * 
	 * @return Returns <code>true</code> if the element is within a child
	 *         element which extends from another.
	 */

	public final boolean isVirtualElement( )
	{
		return baseId != NO_BASE_ID;
	}

	/**
	 * Resolve and returns the virtual parent element. If the element is not
	 * virtual element, <code>null</code> will be returned. Otherwise, find and
	 * returns the virtual parent using the <code>baseId</code>.
	 * 
	 * @return the virtual parent element.
	 */

	public final DesignElement getVirtualParent( )
	{
		if ( !isVirtualElement( ) )
			return null;

		// Find the out-most child element.

		DesignElement parent = null;
		DesignElement cur = this;

		while ( cur != null )
		{
			DesignElement extendsElement = cur.getExtendsElement( );
			if ( extendsElement != null )
			{
				parent = extendsElement;
				break;
			}

			cur = cur.getContainer( );
		}

		// The element is not in the tree.

		if ( parent == null )
			return null;

		assert parent.getContainer( ) instanceof Module;

		return parent.getRoot( ).getElementByID( baseId );

	}

	/**
	 * 
	 * @return
	 */
	public final DesignElement getDynamicVirtualParent( Module module )
	{
		if ( !isVirtualElement( ) )
			return null;

		// Find the out-most child element.

		DesignElement parent = null;
		DesignElement cur = this;

		while ( cur != null )
		{
			DesignElement extendsElement = cur
					.getDynamicExtendsElement( module );
			if ( extendsElement != null )
			{
				// an element can not define parent as extended element and
				// dynamic extends element both
				assert cur.getExtendsElement( ) == null;

				parent = extendsElement;
				break;
			}

			cur = cur.getContainer( );
		}

		// The element is not in the tree.

		if ( parent == null )
			return null;

		assert parent.getContainer( ) instanceof Module;

		return parent.getRoot( ).getElementByID( baseId );
	}

	/**
	 * 
	 * @return
	 */
	public DesignElement getDynamicExtendsElement( Module module )
	{
		return null;
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
	 * @param module
	 *            the module
	 * @param propName
	 *            name of the property to get. Can be a system-defined or
	 *            user-defined property name. Must be of the correct case.
	 * @return The property value, or null if no value is set.
	 */

	public Object getProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );

		// If the property is not found, then the value is null.

		if ( prop == null )
			return null;

		return getProperty( module, prop );
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
	 * @param module
	 *            the module
	 * @param prop
	 *            definition of the property to get
	 * @return The property value, or null if no value is set.
	 */

	public Object getProperty( Module module, ElementPropertyDefn prop )
	{
		Object value = cachedPropStrategy.getPropertyExceptRomDefault( module,
				this, prop );
		if ( value != null )
		{
			return value;
		}

		return prop.getDefault( );
	}

	/**
	 * Gets the search strategy for this element.
	 * 
	 * @return the search strategy for this element.
	 */

	public PropertySearchStrategy getStrategy( )
	{
		return cachedPropStrategy;
	}

	/**
	 * Returns the property value.
	 * 
	 * @param module
	 *            the module
	 * @param prop
	 *            the definition of property
	 * @return the property value, or null if no value is set.
	 */

	public Object getProperty( Module module, PropertyDefn prop )
	{
		return getProperty( module, (ElementPropertyDefn) prop );
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
	 * @param module
	 *            the module
	 * @param propName
	 *            name of the property to set. Must be valid. Can be a
	 *            system-defined or user-defined property name.
	 * @return the property value, or null if no value is set
	 */

	public Object getLocalProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;
		return getLocalProperty( module, prop );
	}

	/**
	 * Gets a property value given its definition. This version checks only this
	 * one object. That is, it gets the "local" property value. The property
	 * name must also be valid for this object.
	 * <p>
	 * Part of: Property value system.
	 * 
	 * @param module
	 *            the module
	 * @param prop
	 *            The property definition.
	 * @return The property value, or null if no value is set.
	 */

	public Object getLocalProperty( Module module, ElementPropertyDefn prop )
	{
		boolean isEncryptable = prop.isEncryptable( );
		if ( prop.isIntrinsic( ) )
		{
			// This is an intrinsic system-defined property.
			return isEncryptable
					? EncryptionUtil.decrypt( this, prop,
							getIntrinsicProperty( prop.getName( ) ) )
					: getIntrinsicProperty( prop.getName( ) );
		}

		switch ( prop.getTypeCode( ) )
		{
			case IPropertyType.ELEMENT_REF_TYPE :
				resolveElementReference( module, prop );
				break;
			case IPropertyType.STRUCT_REF_TYPE :
				resolveStructReference( module, prop );
				break;
			case IPropertyType.LIST_TYPE :
				if ( prop.getSubTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
					return resolveElementReferenceList( module, prop );

		}

		// Get the value of a non-intrinsic property.
		return isEncryptable ? EncryptionUtil.decrypt( this, prop, propValues
				.get( prop.getName( ) ) ) : propValues.get( prop.getName( ) );
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
		if ( NAME_PROP.equals( propName ) )
			return getName( );
		if ( EXTENDS_PROP.equals( propName ) )
		{
			if ( extendsRef != null && !extendsRef.isResolved( ) )
			{
				ReferenceValueUtil
						.resloveExtends( getRoot( ), this, extendsRef );
			}
			return extendsRef;
		}
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
	 * <li><strong>name </strong>-- The element name. The property value must be
	 * null or a String.
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

		if ( prop.getTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
		{
			ElementRefValue oldRef = (ElementRefValue) propValues
					.get( propName );
			ReferenceValueUtil.updateReference( this, oldRef,
					(ReferenceValue) value, prop );
		}

		// handle caching for structure references.

		if ( prop.getTypeCode( ) == IPropertyType.STRUCT_REF_TYPE )
		{
			StructRefValue oldRef = (StructRefValue) propValues.get( propName );
			ReferenceValueUtil.updateReference( this, oldRef,
					(StructRefValue) value, prop );
		}

		// establish the context if the value is a structure or structure list.

		StructureContextUtil.setStructureContext( prop, value, this );

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
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property
	 * 
	 * @return the mask of the given property
	 */

	public String getPropertyMask( Module module, String propName )
	{
		DesignElement e = this;

		do
		{
			List<? extends Object> masks = (List<? extends Object>) e
					.getLocalProperty( module,
							IDesignElementModel.PROPERTY_MASKS_PROP );

			if ( masks != null )
			{
				for ( int i = 0; i < masks.size( ); i++ )
				{
					PropertyMask mask = (PropertyMask) masks.get( i );
					if ( propName.equals( mask.getName( ) ) )
						return mask.getMask( );
				}
			}

			e = e.getExtendsElement( );

		} while ( e != null );

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.IPropertySet#setProperty(org.eclipse
	 * .birt.report.model.metadata.PropertyDefn, java.lang.Object)
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

	public IElementDefn getDefn( )
	{
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
		assert cachedDefn.allowsUserProperties( );
		assert userProp != null;
		String propName = userProp.getName( );
		assert getUserPropertyDefn( propName ) == null;
		assert cachedDefn.getProperty( propName ) == null;
		if ( userProperties == null )
			userProperties = new LinkedHashMap<String, UserPropertyDefn>(
					ModelUtil.MAP_CAPACITY_LOW );
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
		assert cachedDefn.allowsUserProperties( );
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
		return userProperties.get( propName );
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
			e = e.getExtendsElement( ) == null ? e.getVirtualParent( ) : e
					.getExtendsElement( );
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
		if ( !extendsRef.isResolved( ) )
		{
			ReferenceValueUtil.resloveExtends( getRoot( ), this, extendsRef );
		}
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

		return StringUtil.buildQualifiedReference( extendsRef
				.getLibraryNamespace( ), extendsRef.getName( ) );
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
	 * Gets the full name of this name. Generally, all the name is unique in the
	 * whole design tree, and therefore its full name is the same with
	 * name.However, some kinds of elements are not. Their name are unique
	 * partly, such as the level name is unique in the dimension container. And
	 * therefore, its full name is dimensionName/levelName.
	 * 
	 * @return the full name of this element
	 */
	public String getFullName( )
	{
		return getName( );
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
		return containerInfo == null ? null : containerInfo.container;
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
		if ( propName == null )
			return null;

		// Look for the property defined on this element.
		ElementPropertyDefn prop = (ElementPropertyDefn) cachedDefn
				.getProperty( propName );
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
	 * @param module
	 *            the module
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
	/*
	 * 
	 * public Object validatePropertyValue( Module module, String propName,
	 * Object value ) throws PropertyNameException, PropertyValueException { //
	 * If we can't find the property, then the property name // must be invalid.
	 * 
	 * ElementPropertyDefn prop = getPropertyDefn( propName ); if ( prop == null
	 * ) throw new PropertyNameException( this, propName );
	 * 
	 * // Validate the property value.
	 * 
	 * try { return prop.validateValue( module, this, value ); } catch (
	 * PropertyValueException e ) { // Fill in the context information for the
	 * exception.
	 * 
	 * e.setElement( this ); e.setPropertyName( propName ); throw e; } }
	 */

	/**
	 * Returns the shared style referenced by this element. This method will try
	 * to resolve the style element if the value is un-resolved.
	 * <p>
	 * Part of: Style system.
	 * 
	 * @param module
	 *            module
	 * @return the shared style, or null if this element does not explicitly
	 *         reference a shared style.
	 */

	public StyleElement getStyle( Module module )
	{
		return null;
	}

	/**
	 * Returns the shared style referenced by this element. This method will not
	 * try to resolve the style element if the value is un-resolved.
	 * <p>
	 * Part of: Style system.
	 * 
	 * @return the shared style, or null if this element does not explicitly
	 *         reference a shared style, or the value is un-resolved
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

	public void addDerived( DesignElement child )
	{
		if ( derived == null )
			derived = new ArrayList<DesignElement>( );
		assert child != null;
		assert child.getExtendsElement( ) == this;
		assert !derived.contains( child );
		assert child.cachedDefn == cachedDefn;
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

	public void dropDerived( DesignElement child )
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
		if ( base == oldExtends && base != null )
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

			assert base.cachedDefn == cachedDefn;

			// The name of the parent must be defined.

			assert base.getName( ) != null;

			String namespace = null;
			Module root = base.getRoot( );
			if ( root instanceof Library )
			{
				namespace = ( (Library) root ).getNamespace( );
			}

			extendsRef = new ElementRefValue( namespace, base );
			base.addDerived( this );
		}
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module
	 *            the module of this element
	 * 
	 * @return the data set element defined on this specific element
	 */

	public TemplateParameterDefinition getTemplateParameterElement(
			Module module )
	{
		ElementRefValue templateParam = (ElementRefValue) getProperty( module,
				REF_TEMPLATE_PARAMETER_PROP );
		if ( templateParam == null )
			return null;

		return (TemplateParameterDefinition) templateParam.getElement( );
	}

	/**
	 * Returns a list of the descendants of this element. The descendants
	 * include elements that directly extend from this one, along with elements
	 * that extend from those, and so on recursively.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @return The list of descendants.
	 */

	public List<DesignElement> getDescendents( )
	{
		ArrayList<DesignElement> list = new ArrayList<DesignElement>( );
		gatherDescendents( list );
		return list;
	}

	/**
	 * Builds up a list of the elements that extend from this one, directly or
	 * indirectly.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @param list
	 *            The list of descendants.
	 */

	public void gatherDescendents( ArrayList<DesignElement> list )
	{
		if ( derived == null )
			return;
		for ( int i = 0; i < derived.size( ); i++ )
		{
			DesignElement child = derived.get( i );
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
		if ( propValues.isEmpty( ) )
			return false;

		Iterator<String> iter = propValues.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String propName = iter.next( );
			ElementPropertyDefn propDefn = getPropertyDefn( propName );
			if ( ( propDefn.isListType( ) && !((List)propValues.get( propName)).isEmpty( ) ) )
			{
				return true;
			}
			else 
				if ( propDefn.getTypeCode( ) != IPropertyType.ELEMENT_TYPE )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns names of properties that have local values.
	 * 
	 * @return an iterator for property names.
	 */

	public Iterator<String> propertyWithLocalValueIterator( )
	{
		return propValues.keySet( ).iterator( );
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

	public List<UserPropertyDefn> getUserProperties( )
	{
		List<UserPropertyDefn> props = new ArrayList<UserPropertyDefn>( );
		DesignElement e = this;
		while ( e != null )
		{
			List<UserPropertyDefn> prop = e.getLocalUserProperties( );
			if ( prop != null )
			{
				props.addAll( prop );
			}
			e = e.getExtendsElement( ) == null ? e.getVirtualParent( ) : e
					.getExtendsElement( );
		}
		return props;
	}

	/**
	 * Returns a list of user properties that are defined only on this element.
	 * 
	 * @return The list of user properties.
	 */

	public List<UserPropertyDefn> getLocalUserProperties( )
	{
		if ( userProperties == null )
			return Collections.emptyList( );

		return new ArrayList<UserPropertyDefn>( userProperties.values( ) );
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
	 * the {@link #setProperty(String, Object )}method.
	 * <p>
	 * Part of: The name system.
	 * 
	 * @return Returns the displayName.
	 */

	public String getDisplayName( )
	{
		return (String) getProperty( null, DISPLAY_NAME_PROP );
	}

	/**
	 * Returns the optional message id used to localize the display name. Note:
	 * set the display name id by calling the
	 * {@link #setProperty(String, Object )}method.
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

	public void setID( long newID )
	{
		id = newID;
	}

	/**
	 * Returns the unique element ID. The ID will be valid only if element IDs
	 * were enabled in the MetaDataDictionary.
	 * 
	 * @return The unique ID. Returns 0 if element IDs are not enabled.
	 */

	public long getID( )
	{
		return this.id;
	}

	/**
	 * Determines if this element is contained in the given element. Checks up
	 * the containment hierarchy.
	 * 
	 * @param element
	 *            The potential container.
	 * @return True if this element is contained in the container either
	 *         directly or indirectly. False if this element is not contained.
	 *         If the given element is this element itself, return true.
	 */

	public boolean isContentOf( DesignElement element )
	{
		DesignElement e = this;
		while ( e != null )
		{
			if ( e == element )
				return true;
			e = e.getContainer( );
		}
		return false;
	}

	/**
	 * Returns the value of a property as a number (BigDecimal).
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property to get
	 * @return the property as a BigDecimal, or null if the value is not set or
	 *         cannot convert to a number
	 */

	public BigDecimal getNumberProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;

		Object value = getProperty( module, prop );
		return prop.getNumberValue( module, value );
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
	 * @param module
	 *            the module
	 * @param propName
	 *            The name of the property to get.
	 * @return The property value as a string.
	 */

	public String getStringProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;

		Object value = getProperty( module, prop );
		return prop.getStringValue( module, value );
	}

	/**
	 * Returns the value of the property in a localize format. Formats dates and
	 * numbers in the form according to the current locale. Converts choices and
	 * colors to their localized display names. Returns the localized display
	 * name for an element reference, etc.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the property name
	 * @return the localized display value of the property
	 */

	public String getDisplayProperty( Module module, String propName )
	{
		PropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return null;
		return getDisplayProperty( module, prop );
	}

	/**
	 * Returns the value of the property in a localize format. Formats dates and
	 * numbers in the form according to the current locale. Converts choices and
	 * colors to their localized display names. Returns the localized display
	 * name for an element reference, etc.
	 * 
	 * @param module
	 *            the module
	 * @param prop
	 *            the property definition
	 * @return the localized display value of the property
	 */

	public String getDisplayProperty( Module module, PropertyDefn prop )
	{
		Object value = getProperty( module, prop );
		if ( value == null )
			return null;
		return prop.getDisplayValue( module, value );
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

		String namespace = StringUtil.extractNamespace( name );
		name = StringUtil.extractName( name );

		extendsRef = new ElementRefValue( namespace, name );
	}

	/**
	 * Validates this element and its contents.
	 * 
	 * @param module
	 *            the module
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	public final List<SemanticException> validateWithContents( Module module )
	{
		ElementDefn elementDefn = (ElementDefn) cachedDefn;
		List<ValidationNode> validatorList = ValidationExecutor
				.getValidationNodes( this, elementDefn.getTriggerDefnSet( ),
						true );

		ValidationExecutor executor = module.getValidationExecutor( );
		errors = executor.perform( this, validatorList );

		List<SemanticException> list = new ArrayList<SemanticException>( errors );
		
		Iterator<ISlotDefn> slotIter = ( (ElementDefn) cachedDefn )
				.slotsIterator( );;
		while ( slotIter.hasNext( ) )
		{
			ISlotDefn slotDefn = slotIter.next( );
			int slotId = slotDefn.getSlotID( );
			Iterator<DesignElement> iter = getSlot( slotId ).iterator( );
			while ( iter.hasNext( ) )
			{
				list.addAll( iter.next( ).validateWithContents( module ) );
			}
		}

		// Besides elements in the slot, also need to validate elements that in
		// the property values in which elements can reside.

		List<IElementPropertyDefn> contentProps = cachedDefn.getContents( );
		for ( int i = 0; i < contentProps.size( ); i++ )
		{
			IElementPropertyDefn tmpContentProp = contentProps.get( i );
			Object tmpElements = getLocalProperty( module,
					(ElementPropertyDefn) tmpContentProp );

			if ( tmpElements == null )
				continue;

			if ( tmpElements instanceof DesignElement )
			{
				list.addAll( ( (DesignElement) tmpElements )
						.validateWithContents( module ) );
			}
			else if ( tmpElements instanceof List )
			{
				Iterator<DesignElement> iter = ( (List<DesignElement>) tmpElements )
						.iterator( );
				while ( iter.hasNext( ) )
				{
					list.addAll( iter.next( ).validateWithContents( module ) );
				}
			}
		}

		return list;
	}

	/**
	 * Validates only this element without its contents, and return error list.
	 * The derived class should override this method to define specific
	 * validation rules.
	 * 
	 * @param module
	 *            the module
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	public List<SemanticException> validate( Module module )
	{
		errors = new ArrayList<SemanticException>( );

		// Check whether this element is unsupported element.

		errors.addAll( UnsupportedElementValidator.getInstance( ).validate(
				module, this ) );

		// check property masks.

		List<? extends Object> propMasks = (List<Object>) getLocalProperty(
				module, PROPERTY_MASKS_PROP );

		if ( propMasks != null )
		{
			ListIterator<? extends Object> masks = propMasks.listIterator( );
			while ( masks.hasNext( ) )
			{
				PropertyMask mask = (PropertyMask) masks.next( );
				errors.addAll( mask.validate( module, this ) );
			}
		}

		return errors;
	}

	/**
	 * Checks all structures in the specific property whose type is structure
	 * list property type. This method is used for element semantic check. The
	 * error is kept in the report design's error list.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the structure list type
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	protected List<SemanticException> validateStructureList( Module module,
			String propName )
	{
		return StructureListValidator.getInstance( ).validate( module, this,
				propName );
	}

	/**
	 * Gets the element selector list.
	 * 
	 * @return the selector list of the element.
	 */
	public List<String> getElementSelectors( )
	{
		List<String> list = new ArrayList<String>( );

		String selector = ( (ElementDefn) cachedDefn ).getSelector( );
		if ( selector != null )
			list.add( selector );

		return list;
	}

	/**
	 * Checks all structures in the specific property whose type is structure
	 * list property type. This method is used in command. If any error is
	 * found, the exception will be thrown.
	 * 
	 * @param module
	 *            the module
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

	public void checkStructureList( Module module, PropertyDefn propDefn,
			List<Object> list, IStructure toAdd ) throws PropertyValueException
	{
		List<SemanticException> errorList = StructureListValidator
				.getInstance( ).validateForAdding( getHandle( module ),
						propDefn, list, toAdd );
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
	 *             throws <code>ExtendsException</code> if the parent element is
	 *             <code>NOT_FOUND</code>,<code>WRONG_TYPE</code>,
	 *             <code>SELF_EXTEND</code>,<code>CIRCULAR</code>.
	 */

	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		String extendsName = getExtendsName( );
		IElementDefn defn = cachedDefn;

		if ( parent == null )
		{
			throw new InvalidParentException( this, extendsName,
					InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND );
		}
		else if ( !defn.canExtend( ) )
		{
			throw new ExtendsForbiddenException( this, parent.getName( ),
					ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND );
		}
		else if ( parent.cachedDefn != defn )
		{
			throw new WrongTypeException( this, parent,
					WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE );
		}
		else if ( parent == this )
		{
			throw new CircularExtendsException( this, extendsName,
					CircularExtendsException.DESIGN_EXCEPTION_SELF_EXTEND );
		}
		else if ( parent.isKindOf( this ) )
		{
			throw new CircularExtendsException( this, parent,
					CircularExtendsException.DESIGN_EXCEPTION_CIRCULAR );
		}
	}

	/**
	 * Gets the value of a property as an integer.
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @param module
	 *            the module
	 * @return the property value as an integer. Returns 0 if the property is
	 *         not defined, or cannot convert to an integer.
	 */

	public int getIntProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return 0;

		Object value = getProperty( module, prop );
		return prop.getIntValue( module, value );
	}

	/**
	 * Gets the value of a property as a float (double).
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property to get
	 * @return the property value as a double. Returns 0 if the property is not
	 *         defined, or cannot convert to a double.
	 */

	public double getFloatProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return 0;

		Object value = getProperty( module, prop );
		return prop.getFloatValue( module, value );
	}

	/**
	 * Gets the value of a property as a boolean.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property to get
	 * @return the property value as a boolean. Returns false if the property is
	 *         not set, or not defined, or cannot convert to a boolean.
	 */

	public boolean getBooleanProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null )
			return false;

		Object value = getProperty( module, prop );
		return prop.getBooleanValue( module, value );
	}

	/**
	 * Gets the value of a property as a list.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property to get
	 * @return the value as an <code>ArrayList</code>, or null if the property
	 *         is not set or the value is not a list
	 */

	public List<Object> getListProperty( Module module, String propName )
	{
		Object value = getProperty( module, propName );
		if ( value == null )
			return null;
		if ( value instanceof ArrayList )
			return (ArrayList<Object>) value;
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

	public List<IElementPropertyDefn> getPropertyDefns( )
	{
		List<IElementPropertyDefn> list = cachedDefn.getProperties( );
		List<UserPropertyDefn> userProps = getUserProperties( );
		if ( userProps != null )
			list.addAll( userProps );
		return list;
	}

	/**
	 * Returns the internal name of the element. This name matches the name of
	 * the definition for the element.
	 * 
	 * @return the internal element type name for this element
	 */

	public abstract String getElementName( );

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
		if ( content == null || content.getContainer( ) != this )
			return NO_SLOT;
		return content.getContainerInfo( ).getSlotID( );
	}

	/**
	 * Checks whether the property of current element is editable.
	 * 
	 * @param module
	 * 
	 * @return <code>true</code> if the property of current element is editable.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canEdit( Module module )
	{
		if ( isRootIncludedByModule( )
				|| ( module != null && module.isReadOnly( ) ) )
			return false;
		return true;
	}

	/**
	 * Determines if this element can be dropped from its container.
	 * 
	 * @param module
	 * 
	 * @return <code>true</code> if it can be dropped. Returns
	 *         <code>false</code> otherwise.
	 */

	public boolean canDrop( Module module )
	{
		// if the root of element is included by report/library. Do not allow
		// drop; if module is read-only, forbid drop too
		if ( isRootIncludedByModule( )
				|| ( module != null && module.isReadOnly( ) ) )
			return false;

		// Can not change the structure of child element or a virtual element(
		// inside the child ).

		if ( isVirtualElement( ) )
			return false;

		// if the element is in the default slot of template parameter
		// definition, then the drop operation is forbidden

		if ( getContainer( ) instanceof TemplateParameterDefinition )
		{
			assert getContainerInfo( ).getSlotID( ) == ITemplateParameterDefinitionModel.DEFAULT_SLOT;
			return false;
		}

		// if this element is a template parameter definition and it is referred
		// by some template elements or report items or data sets, then it is
		// forbidden to drop

		DesignElement element = this;
		if ( element instanceof TemplateParameterDefinition )
		{
			List<BackRef> clients = ( (TemplateParameterDefinition) element )
					.getClientList( );
			if ( clients.size( ) != 0 )
				return false;
		}

		return true;
	}

	/**
	 * Checks whether the root of the current element is included by
	 * report/library.
	 * 
	 * @return <code>true</code> if the root of the current element is included
	 *         by report/library. Otherwise <code>false</code>.
	 */

	public boolean isRootIncludedByModule( )
	{
		Module tmpContainer = getRoot( );
		return tmpContainer == null ? false : tmpContainer.isReadOnly( );
	}

	/**
	 * Checks whether the <code>content</code> can be inserted to the slot
	 * <code>slotId</code> in another element <code>container</code>.
	 * 
	 * @param module
	 *            the module
	 * @param containerInfo
	 *            the container information
	 * @param content
	 *            the target element to be inserted
	 * @return a list containing exceptions.
	 */

	public List<SemanticException> checkContent( Module module,
			ContainerContext containerInfo, DesignElement content )
	{
		return new ArrayList<SemanticException>( );
	}

	/**
	 * Checks whether elements with the given element definition can be inserted
	 * to the slot <code>slotId</code> in another element <code>container</code>
	 * .
	 * 
	 * @param module
	 *            the module
	 * @param containerInfo
	 *            the container information
	 * @param defn
	 *            the element definition
	 * @return a list containing exceptions.
	 */

	public List<SemanticException> checkContent( Module module,
			ContainerContext containerInfo, IElementDefn defn )
	{
		return new ArrayList<SemanticException>( );
	}

	/**
	 * Gets the containerInfo of this element.
	 * 
	 * @return the container information
	 */
	public ContainerContext getContainerInfo( )
	{
		return this.containerInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#getObjectDefn()
	 */

	public IObjectDefn getObjectDefn( )
	{
		return cachedDefn;
	}

	/**
	 * Returns the value of an element reference property as an element. Returns
	 * null if either the property is unset, or the reference is unresolved.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the element reference property to get
	 * @return the element referenced by the property
	 */

	public DesignElement getReferenceProperty( Module module, String propName )
	{
		ElementRefValue ref = (ElementRefValue) getProperty( module, propName );
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
	 * Creates the Factory data structures for specialized property access. This
	 * method uses specialized property resolution rules:
	 * <p>
	 * <ul>
	 * <li>A property value is either a style property or a non-style property.</li>
	 * <li>A non-style property is set if this element, or any of its ancestor
	 * elements, provide a value. It is also considered set if the system
	 * provides a default value.</li>
	 * <li>A property value is considered set only if it is set in the private
	 * style of this element or an ancestor element; it is not considered set if
	 * it is inherited from a shared style.</li>
	 * </ul>
	 * 
	 * @param module
	 *            the module
	 * @param prop
	 *            definition of the property
	 * @return the value of the property according to the rules explained above
	 */

	public Object getFactoryProperty( Module module, ElementPropertyDefn prop )
	{
		// This class handles only non-style properties. See the
		// StyledElement class for the handling of style properties.

		assert !prop.isStyleProperty( );

		return cachedPropStrategy.getPropertyExceptRomDefault( module, this,
				prop );
	}
	
	/**
	 * Delegates {@link #getFactoryProperty(Module, ElementPropertyDefn)}.
	 * Derived classes can add special handling when {@code forExport} is set.
	 * 
	 * @param module
	 *            the module
	 * @param prop
	 *            definition of the property
	 * @param forExport
	 *            indicates whether the property is returned for export
	 * @return the value of the property according to the rules explained above
	 */
	public Object getFactoryProperty( Module module, ElementPropertyDefn prop,
			boolean forExport )
	{
		return getFactoryProperty( module, prop );
	}

	/**
	 * Creates the Factory data structures for specialized property access. This
	 * method uses specialized property resolution rules:
	 * <p>
	 * <ul>
	 * <li>A property value is either a style property or a non-style property.</li>
	 * <li>A non-style property is set if this element, or any of its ancestor
	 * elements, provide a value. It is also considered set if the system
	 * provides a default value.</li>
	 * <li>A property value is considered set only if it is set in the private
	 * style of this element or an ancestor element; it is not considered set if
	 * it is inherited from a shared style.</li>
	 * </ul>
	 * 
	 * @param module
	 *            module
	 * @param propName
	 *            name of the property
	 * @return the value of the property according to the rules explained above
	 */

	public Object getFactoryProperty( Module module, String propName )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );

		// If the property is not found, then the value is null.

		if ( prop == null )
			return null;

		return getFactoryProperty( module, prop );
	}

	/**
	 * Returns the list of elements that extend this one.
	 * 
	 * @return the list of elements. The list is always non-null.
	 */

	public List<DesignElement> getDerived( )
	{
		if ( derived != null )
			return new ArrayList<DesignElement>( derived );
		return new ArrayList<DesignElement>( );
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
	 * <li>The name of this kind of element, which is also defined in metadata</li>
	 * </ul>
	 * <p>
	 * User can also decide at which detail level the display label should be
	 * returned. The level could be one of the following 3 options:
	 * <ul>
	 * <li>USER_LABEL: Only the first 3 steps can be visited, if not found,
	 * return null</li>
	 * <li>SHORT_LABEL: All the above 5 steps can be visited. This will ensure
	 * there will be a return value</li>
	 * <li>FULL_LABEL: Besides the return value of SHORT_LABEL, this option says
	 * we need to return additional information. To get this, every child
	 * element needs to overwrite this method</li>
	 * </ul>
	 * 
	 * @param module
	 *            the module
	 * @param level
	 *            the description level.
	 * @return the display label of this element.
	 */

	public String getDisplayLabel( Module module, int level )
	{
		// search the externalized resource first
		ULocale locale = module == null ? ThreadResources.getLocale( ) : module
				.getLocale( );
		String displayLabel = ModelUtil.searchForExternalizedValue( this,
				DISPLAY_NAME_ID_PROP, locale );

		// if externalized resource not found, then check static display name

		if ( StringUtil.isBlank( displayLabel ) )
		{
			displayLabel = getDisplayName( );
		}

		// third, check the name to display

		if ( StringUtil.isBlank( displayLabel ) )
		{
			displayLabel = getNameForDisplayLabel( );
		}

		if ( level == USER_LABEL )
		{
			return displayLabel;
		}

		if ( !StringUtil.isBlank( displayLabel ) )
			return displayLabel;

		return getDefnDisplayName( module );
	}

	/**
	 * Returns the display name of the element definition. If the element
	 * definition display name is not defined, uses the element definition name.
	 * 
	 * @param module
	 *            the module
	 * 
	 * @return the display label of the element definition
	 */

	protected String getDefnDisplayName( Module module )
	{
		MetaDataDictionary dictionary = MetaDataDictionary.getInstance( );
		IElementDefn elementDefn = dictionary.getElement( getElementName( ) );
		String displayLabel = elementDefn.getDisplayName( );

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
	 * All descriptive text should be "elided" to a length of 30 characters. To
	 * elide the text, find the first white-space before the limit, truncate the
	 * string after that point, and append three dots "...".
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
	 * <code>{@link ReportDesignHandle#rename( DesignElementHandle )}</code> to
	 * change the element names.
	 * 
	 * @return Object the cloned design element.
	 * @throws CloneNotSupportedException
	 *             if clone is not supported.
	 * 
	 */

	final public Object clone( ) throws CloneNotSupportedException
	{
		return doClone( CopyForPastePolicy.getInstance( ) );
	}

	/**
	 * Generates a clone copy of this element. When a report element is cloned,
	 * the basic principle is just copying the property value into the clone,
	 * the other things, like container references, child list references,
	 * listener references will not be cloned; that is, the clone is isolated
	 * from the design tree until it is added into a target design tree. As for
	 * the extends reference to the parent, which is in library, the copy policy
	 * will be responsible for the specific dealing.
	 * 
	 * <p>
	 * When inserting the cloned element into the design tree, user needs to
	 * care about the element name confliction; that is, the client needs to
	 * call the method
	 * <code>{@link ReportDesignHandle#rename( DesignElementHandle )}</code> to
	 * change the element names.
	 * 
	 * @param policy
	 *            the policy for the clone action, maybe copy or copy for
	 *            template
	 * @return Object the cloned design element.
	 * @throws CloneNotSupportedException
	 *             if clone is not supported.
	 */

	public Object doClone( CopyPolicy policy )
			throws CloneNotSupportedException
	{
		// do the base clone, keep the reference to parent

		DesignElement element = (DesignElement) baseClone( );

		// do the specific work according the strategy instance

		if ( policy != null )
			policy.execute( this, element );

		// handle property value

		Iterator<String> iter = propValues.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String key = iter.next( );
			PropertyDefn propDefn = getPropertyDefn( key );

			// if the property is element type, then set-up the container
			// relationship

			if ( !propDefn.isElementType( ) )
				continue;

			Object value = propValues.get( propDefn.getName( ) );
			if ( value == null )
				continue;

			// set the cloned value

			Object clonedValue = ModelUtil.copyValue( propDefn, value, policy );
			if ( clonedValue == null )
				continue;
			element.propValues.put( key, clonedValue );

			if ( propDefn.isList( ) )
			{
				List<Object> values = (List<Object>) clonedValue;
				for ( int i = 0; i < values.size( ); i++ )
				{
					DesignElement item = (DesignElement) values.get( i );
					item.setContainer( element, key );
				}
			}
			else
			{
				( (DesignElement) clonedValue ).setContainer( element, key );
			}

		}

		// clone slots.
		int slotCount = cachedDefn.getSlotCount( );
		Iterator<ISlotDefn> iter1 = ( (ElementDefn) cachedDefn )
				.slotsIterator( );
		if ( slotCount > 0 )
		{
			element.slots = new ContainerSlot[slotCount];
			for ( int i = 0; i < slotCount; i++ )
			{
				SlotDefn slot = (SlotDefn) iter1.next( );
				element.slots[i] = slots[i].copy( element, slot.getSlotID( ),
						policy );
			}
		}

		return element;
	}

	/**
	 * Creates the slot with the definition.
	 */

	protected final void initSlots( )
	{
		int slotCount = cachedDefn.getSlotCount( );

		if ( slotCount == 0 )
			return;

		Iterator<ISlotDefn> iter1 = ( (ElementDefn) cachedDefn )
				.slotsIterator( );

		slots = new ContainerSlot[slotCount];
		for ( int i = 0; i < slotCount; i++ )
		{
			SlotDefn slot = (SlotDefn) iter1.next( );
			if ( slot.isMultipleCardinality( ) )
				slots[i] = new MultiElementSlot( );
			else
				slots[i] = new SingleElementSlot( );
		}
	}

	/**
	 * Returns the validation error list, each of which is the instance of
	 * <code>SemanticExcpetion</code>.
	 * 
	 * @return the validation error list.
	 */

	public List<SemanticException> getErrors( )
	{
		if ( errors == null )
			return new ArrayList<SemanticException>( );

		return errors;
	}

	/**
	 * Returns the identifier for locating element to user. This identifier
	 * string helps user locate this element in user interface. If this element
	 * has name defined, the name and element type are returned with the form
	 * "ElementType('Name')". Otherwise, locate the first container with name
	 * and generate the containment path with the form,
	 * "Container.Slot[Position]". If no container has name defined, "report" or
	 * container element type will be used as container's identifier, depending
	 * on whether this element is added into one report design.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>report.body[0] - The first element in body slot
	 * <li>table("myTable") - The table named myTable
	 * <li>table("myTable").detail[0].cells[3] - The forth cell of the first row
	 * in table
	 * </ul>
	 * <p>
	 * Note: the localized name is used for element type and slot name.
	 * 
	 * @return the identifier of this element
	 */

	public String getIdentifier( )
	{
		if ( getFullName( ) != null )
		{
			StringBuffer sb = new StringBuffer( );
			sb.append( cachedDefn.getDisplayName( ) );
			sb.append( "(\"" ); //$NON-NLS-1$
			sb.append( getFullName( ) );
			sb.append( "\")" ); //$NON-NLS-1$

			return sb.toString( );
		}

		if ( this instanceof ReportDesign )
			return "report"; //$NON-NLS-1$
		else if ( this instanceof Library )
			return "library"; //$NON-NLS-1$

		if ( getContainer( ) == null )
			return cachedDefn.getName( );

		IContainerDefn containerDefn = getContainerInfo( ).getContainerDefn( );

		StringBuffer sb = new StringBuffer( );
		sb.append( getContainer( ).getIdentifier( ) );
		sb.append( "." ); //$NON-NLS-1$
		sb.append( containerDefn.getDisplayName( ) );
		sb.append( "[" ); //$NON-NLS-1$
		sb.append( getContainerInfo( ).indexOf( this ) );
		sb.append( "]" ); //$NON-NLS-1$

		return sb.toString( );
	}

	/**
	 * Sets the element with the id reference.
	 * <p>
	 * Part of: Inheritance system.
	 * 
	 * @param baseId
	 *            The id reference element.
	 */

	public void setBaseId( long baseId )
	{
		this.baseId = baseId;
	}

	/**
	 * @return Returns the idRef.
	 */

	public long getBaseId( )
	{
		return baseId;
	}

	/**
	 * Clears local properties of the element.
	 */

	public void clearAllProperties( )
	{
		this.name = null;
		this.extendsRef = null;

		propValues.clear( );
	}

	/**
	 * Determines whether this element is managed by namespace. If this element
	 * is a pending node or the container is not managed by the namespace,
	 * return false. Otherwise true.
	 * 
	 * @return true if this element is managed by namespace, otherwise false
	 */

	public boolean isManagedByNameSpace( )
	{
		int nameOption = getDefn( ).getNameOption( );
		if ( nameOption == MetaDataConstants.NO_NAME )
		{
			return false;
		}

		ContainerContext infor = getContainerInfo( );
		if ( infor == null )
			return false;

		return getContainerInfo( ).isManagedByNameSpace( );
	}

	/**
	 * Determines if the current element can be transformed to a template
	 * element. False will be returned if the element can not be dropped or the
	 * container of the current element can not contain the template element.
	 * 
	 * @param module
	 *            the root module of the element
	 * 
	 * @return true if it can be transformed, otherwise false.
	 */

	public final boolean canTransformToTemplate( Module module )
	{
		// if this kind of element does not support template or this element can
		// not be dropped, return false;

		if ( !ModelUtil.isTemplateSupported( this ) || ( !canDrop( module ) ) )
			return false;

		// check the containment for the template elements

		IElementDefn templateReportItem = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.TEMPLATE_REPORT_ITEM );
		IElementDefn templateDataSet = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.TEMPLATE_DATA_SET );
		DesignElement container = getContainer( );
		if ( container != null )
		{
			ContainerContext containerInfor = getContainerInfo( );
			return containerInfor.canContain( module, templateReportItem )
					|| containerInfor.canContain( module, templateDataSet );
		}

		return true;
	}

	/**
	 * Checks whether this element is based on a template parameter definition
	 * or not.
	 * 
	 * @param module
	 *            the root module
	 * @return true if this element is based on a template parameter definition,
	 *         otherwise false
	 */

	public boolean isTemplateParameterValue( Module module )
	{
		return getTemplateParameterElement( module ) != null;
	}

	/**
	 * Checks all value items in the specific property whose type is list
	 * property type. This method is used in command. If any error is found, the
	 * exception will be thrown.
	 * 
	 * @param module
	 *            the module
	 * @param propDefn
	 *            the property definition of the list property
	 * @param list
	 *            the value list to check
	 * @param toAdd
	 *            the value item to add. This parameter maybe is
	 *            <code>null</code>.
	 * @throws PropertyValueException
	 *             if the structure list or the item to add has any semantic
	 *             error..
	 */

	public void checkSimpleList( Module module, PropertyDefn propDefn,
			List<Object> list, Object toAdd ) throws PropertyValueException
	{
		List<SemanticException> errorList = SimpleListValidator.getInstance( )
				.validateForAdding( getHandle( module ), propDefn, list, toAdd );
		if ( errorList.size( ) > 0 )
		{
			throw (PropertyValueException) errorList.get( 0 );
		}
	}

	/**
	 * The common logic for both clone and cloneForTemplate.
	 * 
	 * @return the clone element with reference to parent in library
	 * @throws CloneNotSupportedException
	 */

	protected Object baseClone( ) throws CloneNotSupportedException
	{
		DesignElement element = (DesignElement) super.clone( );

		// handle non-simple members and the element definition should not be
		// set null.
		element.containerInfo = null;
		element.listeners = null;
		element.derived = null;
		element.handle = null;
		element.propValues = new HashMap<String, Object>( );

		// handle encryption map
		if ( encryptionMap != null && !encryptionMap.isEmpty( ) )
		{
			element.encryptionMap = new HashMap<String, String>( );
			element.encryptionMap.putAll( encryptionMap );
		}

		// handle extends relationship
		if ( extendsRef != null )
			element.extendsRef = (ElementRefValue) this.extendsRef.copy( );

		// handle user property definitions
		Iterator<String> iter = null;
		if ( !isVirtualElement( ) && userProperties != null )
		{
			element.userProperties = new LinkedHashMap<String, UserPropertyDefn>(
					ModelUtil.MAP_CAPACITY_LOW );

			iter = userProperties.keySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				String key = iter.next( );
				UserPropertyDefn uDefn = userProperties.get( key );
				element.userProperties.put( key, (UserPropertyDefn) uDefn
						.copy( ) );
			}
		}

		// handle property value
		iter = propValues.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String key = iter.next( );
			ElementPropertyDefn propDefn = getPropertyDefn( key );
			Object value = propValues.get( key );
			if ( value == null )
				continue;

			// if the property is element type, then set-up the container
			// relationship

			if ( propDefn.isElementType( ) )
				continue;

			// set the cloned value
			Object clonedValue = ModelUtil.copyValue( propDefn, value );
			if ( clonedValue == null )
				continue;
			element.propValues.put( key, clonedValue );

			// if the property is structure type, then set-up the container
			// relationship

			if ( propDefn.getTypeCode( ) == IPropertyType.STRUCT_TYPE )
			{
				StructureContextUtil.setStructureContext( propDefn,
						clonedValue, element );
			}
		}

		return element;
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

	void setContainer( DesignElement obj, int slot )
	{
		containerInfo = new ContainerContext( obj, slot );
	}

	/**
	 * Caches the child-to-container relationship. The caller must have
	 * validated that the relationship is valid.
	 * <p>
	 * Part of: The containment system.
	 * 
	 * @param obj
	 *            the container to set
	 * @param propName
	 *            the property within the container where this element resides
	 */

	public void setContainer( DesignElement obj, String propName )
	{
		containerInfo = new ContainerContext( obj, propName );
	}

	/**
	 * Adds a content to this element.
	 * 
	 * @param content
	 *            the content to add
	 * @param slotID
	 *            the slot id to add
	 */
	public void add( DesignElement content, int slotID )
	{
		getSlot( slotID ).add( content );
		content.setContainer( this, slotID );
	}

	/**
	 * Adds a content to this element.
	 * 
	 * @param content
	 *            the content to add
	 * @param slotID
	 *            the slot id to add
	 * @param posn
	 */
	public void add( DesignElement content, int slotID, int posn )
	{
		getSlot( slotID ).insert( content, posn );
		content.setContainer( this, slotID );
	}

	/**
	 * Removes a content from the given slot.
	 * 
	 * @param content
	 *            the content to remove
	 * @param slotID
	 *            the slot id in which content resides
	 */
	public void remove( DesignElement content, int slotID )
	{
		getSlot( slotID ).remove( content );
		content.containerInfo = null;
	}

	/**
	 * 
	 * @param module
	 * @param content
	 * @param propName
	 */
	public void add( Module module, DesignElement content, String propName )
	{
		ElementPropertyDefn defn = getPropertyDefn( propName );
		if ( defn != null )
		{
			if ( defn.isList( ) )
			{
				List<DesignElement> values = (List<DesignElement>) getLocalProperty(
						module, propName );
				if ( values == null )
					values = new ArrayList<DesignElement>( );
				if ( !values.contains( content ) )
					values.add( content );
				setProperty( propName, values );
				content.setContainer( this, propName );
			}
			else
			{
				setProperty( defn, content );
				content.setContainer( this, propName );
			}
		}
	}

	/**
	 * Adds a content to this element.
	 * 
	 * @param module
	 * @param content
	 * @param propName
	 * @param posn
	 */
	public void add( Module module, DesignElement content, String propName,
			int posn )
	{
		ElementPropertyDefn defn = getPropertyDefn( propName );
		if ( defn != null )
		{
			if ( defn.isList( ) )
			{
				List<DesignElement> values = (List<DesignElement>) getLocalProperty(
						module, propName );
				if ( values == null )
					values = new ArrayList<DesignElement>( );
				if ( !values.contains( content ) )
					values.add( posn, content );
				setProperty( propName, values );
				content.setContainer( this, propName );
			}
			else
			{
				assert posn == 0;
				setProperty( defn, content );
				content.setContainer( this, propName );
			}
		}
	}

	/**
	 * Removes a content from the given property value.
	 * 
	 * @param module
	 *            the module of the content
	 * @param content
	 *            the content to remove
	 * @param propName
	 *            the property name where the content resides
	 */
	public void remove( Module module, DesignElement content, String propName )
	{
		ElementPropertyDefn defn = getPropertyDefn( propName );
		if ( defn != null )
		{
			// if the content does not belong to the same module, do nothing
			if (content.getRoot( ) != null && module != content.getRoot( )   )
			{
				return;
			}
			if ( defn.isList( ) )
			{
				List<DesignElement> values = (List<DesignElement>) getLocalProperty(
						module, propName );
				if ( values != null )
				{
					values.remove( content );
					content.containerInfo = null;
				}
			}
			else
			{
				clearProperty( propName );
				content.containerInfo = null;
			}
		}
	}

	/**
	 * Resolves a list of element reference.
	 * 
	 * @param module
	 *            the module information needed for the check, and records any
	 *            errors
	 * @param prop
	 *            the property whose type is element reference
	 * @return the list of element reference value and each reference value is
	 *         tried to resolve
	 */

	public List<ElementRefValue> resolveElementReferenceList( Module module,
			ElementPropertyDefn prop )
	{
		Object value = propValues.get( prop.getName( ) );

		assert value == null || value instanceof List;
		assert prop.getTypeCode( ) == IPropertyType.LIST_TYPE
				&& prop.getSubTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE;

		if ( value == null )
			return null;

		List<ElementRefValue> valueList = (List<ElementRefValue>) value;
		for ( int i = 0; i < valueList.size( ); i++ )
		{
			// try to resolve every
			ElementRefValue item = valueList.get( i );
			ReferenceValueUtil.resolveElementReference( module, this, prop,
					item );
		}
		return valueList;
	}

	/**
	 * Resolves a property element reference. The reference is the value of a
	 * property of type property element reference.
	 * 
	 * @param module
	 *            the module information needed for the check, and records any
	 *            errors
	 * @param element
	 *            design element
	 * @param prop
	 *            the property whose type is element reference
	 * @return the element reference value is always returned, which contains
	 *         the information of element resolution.
	 */

	public ElementRefValue resolveElementReference( Module module,
			ElementPropertyDefn prop )
	{
		Object value = propValues.get( prop.getName( ) );

		assert value == null || value instanceof ElementRefValue;
		assert prop.getTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE;

		if ( !( value instanceof ElementRefValue ) )
			return null;
		if ( module == null )
			return null;

		return ReferenceValueUtil.resolveElementReference( module, this, prop,
				(ElementRefValue) value );
	}

	/**
	 * Resolves a property structure reference. The reference is the value of a
	 * property of type property structure reference.
	 * 
	 * @param module
	 *            the module information needed for the check, and records any
	 *            errors
	 * @param prop
	 *            the property whose type is structure reference
	 * @return the resolved value if the resolve operation is successful,
	 *         otherwise the unresolved value
	 */

	public StructRefValue resolveStructReference( Module module,
			ElementPropertyDefn prop )
	{
		Object value = propValues.get( prop.getName( ) );

		if ( !( value instanceof StructRefValue ) )
			return null;
		if ( module == null )
			return null;

		return ReferenceValueUtil.resolveStructReference( module, this, prop,
				(StructRefValue) value );
	}

	/**
	 * Gets the position where this element resides in its container.
	 * 
	 * @param module
	 * 
	 * @return the index where this element resides in its container, otherwise
	 *         -1 if this element has no container
	 */
	public int getIndex( Module module )
	{
		ContainerContext containerContext = getContainerInfo( );
		return containerContext == null ? -1 : containerContext.indexOf(
				module, this );
	}

	/**
	 * Gets the local-set encryption id for the specialized property.
	 * 
	 * @param propDefn
	 * @return the local encryption id for the given property definition
	 */
	public String getLocalEncryptionID( ElementPropertyDefn propDefn )
	{
		if ( encryptionMap != null
				&& encryptionMap.get( propDefn.getName( ) ) != null )
		{
			String encryptionID = encryptionMap.get( propDefn.getName( ) );
			return encryptionID;
		}
		return null;
	}

	/**
	 * Gets the effective encryption id for the given property.
	 * 
	 * @param propDefn
	 * @return the effective encryption id
	 */
	public String getEncryptionID( ElementPropertyDefn propDefn )
	{
		if ( propDefn == null || !propDefn.isEncryptable( ) )
			return null;
		DesignElement e = this;
		while ( e != null )
		{
			String encryption = e.getLocalEncryptionID( propDefn );
			if ( encryption != null )
				return encryption;
			e = e.getExtendsElement( ) == null ? e.getVirtualParent( ) : e
					.getExtendsElement( );
		}
		return MetaDataDictionary.getInstance( ).getDefaultEncryptionHelperID( );
	}

	/**
	 * Justifies whether this element has set local value for the specialized
	 * property.
	 * 
	 * @param propDefn
	 * @return true if the element sets the local value, otherwise false
	 */
	protected boolean hasLocalValue( ElementPropertyDefn propDefn )
	{
		if ( propDefn == null )
			return false;
		if ( propDefn.isIntrinsic( ) )
			return getIntrinsicProperty( propDefn.getName( ) ) != null;
		return propValues.get( propDefn.getName( ) ) != null;
	}

	/**
	 * Sets the encryption id for the given property.
	 * 
	 * @param propName
	 * @param encryptionID
	 */
	public final void setEncryptionHelper( String propName, String encryptionID )
	{
		ElementPropertyDefn prop = getPropertyDefn( propName );
		if ( prop == null || !prop.isEncryptable( ) )
			return;
		setEncryptionHelper( prop, encryptionID );
	}

	/**
	 * Sets the encryption id for the given property.
	 * 
	 * @param propDefn
	 * @param encryptionID
	 */
	public void setEncryptionHelper( ElementPropertyDefn propDefn,
			String encryptionID )
	{
		if ( propDefn == null || !propDefn.isEncryptable( ) )
			return;
		String id = StringUtil.trimString( encryptionID );

		if ( encryptionMap == null )
			encryptionMap = new HashMap<String, String>( );
		if ( id == null )
			encryptionMap.remove( propDefn.getName( ) );
		else
			encryptionMap.put( propDefn.getName( ), id );

	}

	/**
	 * Checks whether the given element is contained by one of template
	 * parameter definition.
	 * 
	 * @return <code>true</code> if the element is in the template parameter
	 *         definition. Otherwise, <code>false</code>.
	 */
	public boolean isInTemplateParameterDefinitionSlot( )
	{
		return isInSlot( IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT );
	}

	/**
	 * Checks whether the given element is contained by the given slot.
	 * 
	 * @param slotID
	 *            the id of the slot
	 * 
	 * @return <code>true</code> if the element is in the given slot .
	 *         Otherwise, <code>false</code>.
	 */
	public boolean isInSlot( int slotID )
	{

		DesignElement tmpContainer = getContainer( );
		ContainerContext containerInfo = null;

		while ( tmpContainer != null && !( tmpContainer instanceof Module ) )
		{
			containerInfo = tmpContainer.getContainerInfo( );
			tmpContainer = tmpContainer.getContainer( );
		}

		int slot = containerInfo == null
				? IDesignElementModel.NO_SLOT
				: containerInfo.getSlotID( );

		if ( slotID == slot )
			return true;

		return false;
	}

	/**
	 * Gets the property search strategy for this element.
	 * 
	 * @return the property search strategy
	 */
	public PropertySearchStrategy getPropertySearchStrategy( )
	{
		return this.cachedPropStrategy;
	}

	/**
	 * Returns the slot index in the slots array. The slot ID may be discrete.
	 * 
	 * @param slotID
	 *            the slot id
	 * @return the index based on 0
	 */

	public int getSlotIndex( int slotID )
	{
		return slotID;
	}

	/**
	 * Returns the property definitions for this element that can hold other
	 * elements. Each one in the list is instance of <code>IPropertyDefn</code>.
	 * 
	 * @return the list of the property definition that can hold other elements
	 */

	public List<IElementPropertyDefn> getContents( )
	{
		return cachedDefn.getContents( );
	}

	/**
	 * Determines if this element acts as a container.
	 * 
	 * @return True if this element is a container, false otherwise.
	 */

	public final boolean isContainer( )
	{
		if ( cachedDefn.isContainer( ) )
			return true;

		if ( !getContents( ).isEmpty( ) )
			return true;

		return false;
	}

	public boolean canDynamicExtends( )
	{
		return false;
	}
}
