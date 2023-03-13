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

package org.eclipse.birt.report.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.IVersionInfo;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.command.ContentElementInfo;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.strategy.CopyForPastePolicy;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ExtensionPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.xml.sax.SAXException;

import com.ibm.icu.text.CollationKey;
import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * The utility class which provides many static methods used in Model.
 */

public class ModelUtil extends ModelUtilBase {
	public final static int MAP_CAPACITY_LOW = 6;

	public final static int MAP_CAPACITY_MEDIUM = 12;

	/**
	 * Returns the wrapped value that is visible to API level. For example, element
	 * reference value is returned as string; element value is returned as
	 * DesignElementHandle.
	 *
	 * @param module the root
	 * @param defn   the property definition
	 * @param value  the property value
	 *
	 * @return value of a property as a API level object
	 */

	public static Object wrapPropertyValue(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Expression) {
			return defn.getStringValue(module, value);
		}

		if (value instanceof ReferenceValue) {
			return ReferenceValueUtil.needTheNamespacePrefix((ReferenceValue) value, module);
		}

		if (value instanceof List && defn != null && defn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			List valueList = (List) value;
			List<String> names = new ArrayList<>();
			for (int i = 0; i < valueList.size(); i++) {
				ElementRefValue item = (ElementRefValue) valueList.get(i);
				names.add(ReferenceValueUtil.needTheNamespacePrefix(item, module));
			}
			return names;
		}

		// convert design element to handles

		if (defn != null && defn.isElementType()) {
			if (value instanceof DesignElement) {
				return ((DesignElement) value).getHandle(module);
			} else if (value instanceof List) {
				List items = (List) value;
				List handles = new ArrayList();
				for (int i = 0; i < items.size(); i++) {
					DesignElement item = (DesignElement) items.get(i);
					handles.add(item.getHandle(module));
				}
				return handles;
			}
		}

		// to avoid the address reference and change the value directly not by
		// the Model command, we wrap the list value
		if (value instanceof List) {
			List retValue = new ArrayList((List) value);
			return retValue;
		}

		return value;
	}

	/**
	 * Duplicates the properties from source element to destination element. Source
	 * and the destination element should be of the same type. The following
	 * properties will be duplicated:
	 * <ul>
	 * <li>Properties set on element itself
	 * <li>Inherited from style or element's selector style
	 * <li>Inherited from parent
	 * </ul>
	 * <p>
	 * This method is <strong>ONLY</strong> for exporting properties.
	 *
	 * @param source              handle of the source element
	 * @param destination         handle of the destination element
	 * @param onlyFactoryProperty indicate whether only factory property values are
	 *                            duplicated.
	 * @param removeNameSpace     indicate whether the name space of the extended
	 *                            item property should be removed.
	 */
	public static void duplicateProperties(DesignElementHandle source, DesignElementHandle destination,
			boolean onlyFactoryProperty, boolean removeNameSpace) {
		duplicateProperties(source, destination, onlyFactoryProperty, removeNameSpace, false);
	}

	/**
	 * Duplicate properties with specified search strategy instead of
	 *
	 * @param source              handle of the source element
	 * @param destination         handle of the destination element
	 * @param onlyFactoryProperty indicate whether only factory property values are
	 *                            duplicated.
	 * @param removeNameSpace     indicate whether the name space of the extended
	 *                            item property should be removed.
	 * @param strategy            the property search strategy, note this only works
	 *                            when onlyFactoryProperty is true.
	 */
	public static void duplicateProperties(DesignElementHandle source, DesignElementHandle destination,
			boolean onlyFactoryProperty, boolean removeNameSpace, boolean duplicateForExport) {
		assert source != null;
		assert destination != null;

		if (!((source instanceof ReportDesignHandle) && (destination instanceof ModuleHandle))) {
			assert destination.getDefn().getName().equalsIgnoreCase(source.getDefn().getName());
		}

		if (source.getDefn().allowsUserProperties()) {
			PropertyHandle propHandle = source.getPropertyHandle(IDesignElementModel.USER_PROPERTIES_PROP);

			Object value = source.getElement().getUserProperties();

			Object valueToSet = null;
			if (propHandle != null) {
				valueToSet = ModelUtil.copyValue(propHandle.getDefn(), value);
			}

			if (valueToSet != null) {
				Iterator<Object> iter = ((List) valueToSet).iterator();
				while (iter.hasNext()) {
					UserPropertyDefn userPropDefn = (UserPropertyDefn) iter.next();
					destination.getElement().addUserPropertyDefn(userPropDefn);
				}
			}
		}

		if (source.getElement() instanceof IExtendableElement) {
			duplicateExtensionIdentifier(source.getElement(), destination.getElement(), source.getModule());
		}

		Iterator<PropertyHandle> iter = source.getPropertyIterator();

		while (iter.hasNext()) {
			PropertyHandle propHandle = iter.next();

			String propName = propHandle.getDefn().getName();

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if (needSkipProperty(destination, propName)) {
				continue;
			}

			ElementPropertyDefn propDefn = destination.getElement().getPropertyDefn(propName);

			if (propDefn == null || propDefn.getTypeCode() == IPropertyType.ELEMENT_TYPE) {
				continue;
			}

			Object value = null;

			// the special case for the toc, pageBreakAfter and pageBreakBefore
			// properties on the group element

			// for toc the default value is the group expression.
			if (propHandle.getElement() instanceof GroupElement && (IGroupElementModel.TOC_PROP.equals(propName)
					|| IStyleModel.PAGE_BREAK_AFTER_PROP.equals(propName)
					|| IStyleModel.PAGE_BREAK_BEFORE_PROP.equals(propName)
					|| IStyleModel.PAGE_BREAK_INSIDE_PROP.equals(propName))) {
				value = propHandle.getElement().getLocalProperty(propHandle.getModule(), propDefn);
			} else if (onlyFactoryProperty) {
				value = propHandle.getElement().getFactoryProperty(propHandle.getModule(), propDefn,
						duplicateForExport);
			} else if (IModuleModel.IMAGES_PROP.equals(propName)) {
				// Copy the embedded images
				Iterator<Object> images = source.getPropertyHandle(IModuleModel.IMAGES_PROP).iterator();
				while (images.hasNext()) {
					StructureHandle image = (StructureHandle) images.next();
					try {
						ElementExportUtil.exportStructure(image, (LibraryHandle) destination, false);
					} catch (SemanticException e) {
						assert false;
					}
				}
				continue;
			} else {
				value = propHandle.getElement().getStrategy().getPropertyExceptRomDefault(propHandle.getModule(),
						propHandle.getElement(), propDefn);
			}

			if (propDefn.isEncryptable()) {
				String encryption = propHandle.getElement().getEncryptionID(propDefn);
				Object valueToSet = EncryptionUtil.encrypt(propDefn, encryption,
						ModelUtil.copyValue(propHandle.getDefn(), value));
				destination.getElement().setProperty(propName, valueToSet);
				destination.getElement().setEncryptionHelper(propDefn, encryption);
			} else {
				Object valueToSet = ModelUtil.copyValue(propDefn, value);

				if (removeNameSpace && value instanceof ReferenceValue) {
					((ReferenceValue) valueToSet).setLibraryNamespace(null);
				}

				destination.getElement().setProperty(propName, valueToSet);

				if (valueToSet != null && propDefn.getTypeCode() == IPropertyType.CONTENT_ELEMENT_TYPE) {
					if (propDefn.isList()) {
						List<Object> values = (List<Object>) valueToSet;
						for (int i = 0; i < values.size(); i++) {
							DesignElement item = (DesignElement) values.get(i);
							item.setContainer(destination.getElement(), propName);
						}
					} else {
						((DesignElement) valueToSet).setContainer(destination.getElement(), propName);
					}
				}
			}
		}
	}

	/**
	 * Duplicates the extension identifier. The extension identifier must be set
	 * before copy other property values. If the identifier is not set first,
	 * extension property definitions cannot be found. Hence, duplicating property
	 * values cannot be right.
	 * <p>
	 * The extension identifier is:
	 * <ul>
	 * <li>EXTENSION_ID_PROP for Oda elements.
	 * <li>EXTENSION_NAME_PROP for extension elements like chart.
	 * </ul>
	 *
	 * @param source       the source element
	 * @param destination  the destination element
	 * @param sourceModule the root module of the source
	 */

	static void duplicateExtensionIdentifier(DesignElement source, DesignElement destination, Module sourceModule) {

		// for the special oda cases, the extension id must be set before
		// copy properties. Otherwise, destination cannot find its ODA
		// properties.

		if (source instanceof IOdaExtendableElementModel) {
			String extensionId = (String) source.getProperty(sourceModule,
					IOdaExtendableElementModel.EXTENSION_ID_PROP);

			destination.setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionId);
		} else

		if (source instanceof IExtendedItemModel) {
			String extensionId = (String) source.getProperty(sourceModule, IExtendedItemModel.EXTENSION_NAME_PROP);

			destination.setProperty(IExtendedItemModel.EXTENSION_NAME_PROP, extensionId);
		} else {
			assert false;
		}

	}

	/**
	 * Clone the structure list, a list value contains a list of
	 * <code>IStructure</code>.
	 *
	 * @param list The structure list to be cloned.
	 * @return The cloned structure list.
	 */

	public static ArrayList cloneStructList(List list) {
		if (list == null) {
			return null;
		}

		ArrayList returnList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object item = list.get(i);
			if (item instanceof IStructure) {
				returnList.add(((IStructure) item).copy());
			} else {
				assert false;
			}
		}
		return returnList;
	}

	/**
	 * Clones the value.
	 * <ul>
	 * <li>If the value is of simple type, like integer, or string, the original
	 * value will be returned.
	 * <li>If the value is strcuture list, the cloned structure list will be cloned.
	 * <li>If the value is structure, the cloned structure will be cloned.
	 * <li>If the value is element/strucuture reference value, the element/structure
	 * name will be returned.
	 * </ul>
	 *
	 * @param propDefn definition of property
	 * @param value    value to clone
	 * @return new value
	 */

	public static Object copyValue(IPropertyDefn propDefn, Object value) {
		return copyValue(propDefn, value, CopyForPastePolicy.getInstance());
	}

	/**
	 * Clones the value.
	 * <ul>
	 * <li>If the value is of simple type, like integer, or string, the original
	 * value will be returned.
	 * <li>If the value is strcuture list, the cloned structure list will be cloned.
	 * <li>If the value is structure, the cloned structure will be cloned.
	 * <li>If the value is element/strucuture reference value, the element/structure
	 * name will be returned.
	 * </ul>
	 *
	 * @param propDefn definition of property
	 * @param value    value to clone
	 * @param policy   how to copy the element-related values
	 * @return new value
	 */

	public static Object copyValue(IPropertyDefn propDefn, Object value, CopyPolicy policy) {

		if (value == null || propDefn == null) {
			return null;
		}

		if (propDefn.allowExpression() && value instanceof Expression) {
			return new Expression(((Expression) value).getExpression(), ((Expression) value).getUserDefinedType());
		}

		switch (propDefn.getTypeCode()) {
		case IPropertyType.STRUCT_TYPE:

			if (propDefn.isList()) {
				return ModelUtil.cloneStructList((List) value);
			}

			return ((Structure) value).copy();

		case IPropertyType.ELEMENT_REF_TYPE:
		case IPropertyType.STRUCT_REF_TYPE:

			ReferenceValue refValue = (ReferenceValue) value;
			return refValue.copy();

		case IPropertyType.LIST_TYPE:
			return clonePropertyList((List) value);
		case IPropertyType.ELEMENT_TYPE:
		case IPropertyType.CONTENT_ELEMENT_TYPE:
			if (propDefn.isList()) {
				return cloneElementList((List) value, policy);
			}
			return getCopy((DesignElement) value, policy);
		}

		return value;
	}

	/**
	 * Copies a list of simple property values.
	 *
	 * @param value the original value to copy
	 * @return the cloned list of simple property values
	 */

	private static List clonePropertyList(List value) {
		if (value == null) {
			return null;
		}

		List returnList = new ArrayList();
		for (int i = 0; i < value.size(); i++) {
			Object item = value.get(i);
			if (item instanceof ElementRefValue) {
				returnList.add(((ElementRefValue) item).copy());
			} else if (item instanceof Expression) {
				returnList.add(
						new Expression(((Expression) item).getExpression(), ((Expression) item).getUserDefinedType()));
			} else {
				returnList.add(item);
			}
		}
		return returnList;
	}

	/**
	 * Copies a list of design elements.
	 *
	 * @param value  the value to copy
	 * @param policy how to copy the element-related values
	 *
	 * @return the cloned list of design elements
	 */
	private static List cloneElementList(List value, CopyPolicy policy) {
		if (value == null) {
			return null;
		}
		ArrayList returnList = new ArrayList();
		for (int i = 0; i < value.size(); i++) {
			DesignElement item = (DesignElement) value.get(i);
			returnList.add(getCopy(item, policy));
		}
		return returnList;
	}

	/**
	 * Filtrates the table layout tasks.
	 *
	 * @param tasks the table layout tasks
	 * @return a list contained filtrated table layout tasks
	 */

	public static List<RecordTask> filterLayoutTasks(List<RecordTask> tasks) {
		List<RecordTask> retList = new ArrayList<>();
		Set<DesignElement> elements = new LinkedHashSet<>();

		for (int i = 0; i < tasks.size(); i++) {
			RecordTask task = tasks.get(i);

			if (task instanceof LayoutRecordTask) {
				DesignElement compoundElement = (DesignElement) ((LayoutRecordTask) task).getTarget();
				if (!elements.contains(compoundElement)) {
					retList.add(task);
					elements.add(compoundElement);
				}
			}
		}

		return retList;
	}

	/**
	 * Returns the first fatal exception from the given exception list. The fatal
	 * exception means the error should be forwarded to the outer-most host module
	 * and stops opening module.
	 *
	 * @param list the exception list
	 * @return the fatal exception, otherwise, return null.
	 */

	public static Exception getFirstFatalException(List<Exception> list) {
		Iterator<Exception> iter = list.iterator();
		while (iter.hasNext()) {
			Exception ex = iter.next();
			if (ex instanceof XMLParserException) {
				XMLParserException parserException = (XMLParserException) ex;
				if (parserException.getException() instanceof LibraryException) {
					String errorCode = ((LibraryException) parserException.getException()).getErrorCode();

					if (errorCode == LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY
							|| errorCode == LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE) {
						return parserException.getException();
					}
				}
			}
		}

		return null;
	}

	/**
	 * Checks whether the input stream has a compatible encoding signature with
	 * BIRT. Currently, BIRT only supports UTF-8 encoding.
	 *
	 * @param inputStream the input stream to check
	 * @param fileName    the design file name
	 * @return the signature from the UTF files.
	 * @throws IOException  if errors occur during opening the design file
	 * @throws SAXException if the stream has unexpected encoding signature
	 */

	public static String checkUTFSignature(InputStream inputStream, String fileName) throws IOException, SAXException {

		// This may fail if there are a lot of space characters before the end
		// of the encoding declaration

		String encoding = UnicodeUtil.checkUTFSignature(inputStream);

		if (encoding != null && !UnicodeUtil.SIGNATURE_UTF_8.equals(encoding)) {
			Exception cause = new DesignParserException(DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_ENCODING);
			Exception fileException = new DesignFileException(fileName, cause);

			throw new SAXException(fileException);
		}

		return encoding;
	}

	/**
	 *
	 * Performs property name sorting on a list of properties. Properties returned
	 * are sorted by their (locale-specific) display name. The name for sorting is
	 * assumed to be "groupName.displayName" in which "groupName" is the localized
	 * name of the property group, if any; and "displayName" is the localized name
	 * of the property. That is, properties without groups sort by their property
	 * display names. Properties with groups sort first by group name within the
	 * overall list, then by property name within the group. Sorting in English
	 * ignores case.
	 * <p>
	 * For example, if we have the groups "G" and "R", and the properties "alpha",
	 * "G.beta", "G.sigma", "iota", "R.delta", "R.epsilon" and "theta", the
	 * Properties returned is assumed to be sorted into that order.
	 *
	 * Sorts a list of <code>PropertyDefn</code> s by there localized name. Uses
	 * <code>Collator</code> to do the comparison, sorting in English ignores case.
	 *
	 * @param propDefns a list that contains PropertyDefns.
	 * @return the list of <code>PropertyDefn</code> s that is sorted by their
	 *         display name.
	 */

	public static List<IPropertyDefn> sortPropertiesByLocalizedName(List<IPropertyDefn> propDefns) {
		// Use the static factory method, getInstance, to obtain the appropriate
		// Collator object for the current
		// locale.

		// The Collator instance that performs locale-sensitive String
		// comparison.

		ULocale locale = ThreadResources.getLocale();
		Collator collator = Collator.getInstance(locale);

		// Sorting in English should ignore case.
		if (ULocale.ENGLISH.equals(locale)) {

			// Set Collator strength value as PRIMARY, only PRIMARY differences
			// are considered significant during comparison. The assignment of
			// strengths to language features is locale defendant. A common
			// example is for different base letters ("a" vs "b") to be
			// considered a PRIMARY difference.

			collator.setStrength(Collator.PRIMARY);
		}

		final Map<PropertyDefn, CollationKey> keysMap = new HashMap<>();
		for (int i = 0; i < propDefns.size(); i++) {
			PropertyDefn propDefn = (PropertyDefn) propDefns.get(i);

			// Transforms the String into a series of bits that can be compared
			// bitwise to other CollationKeys.
			// CollationKeys provide better performance than Collator.

			CollationKey key = collator.getCollationKey(propDefn.getDisplayName());
			keysMap.put(propDefn, key);
		}

		Collections.sort(propDefns, new Comparator<IPropertyDefn>() {

			@Override
			public int compare(IPropertyDefn o1, IPropertyDefn o2) {
				PropertyDefn p1 = (PropertyDefn) o1;
				PropertyDefn p2 = (PropertyDefn) o2;

				CollationKey key1 = keysMap.get(p1);
				CollationKey key2 = keysMap.get(p2);

				// Comparing two CollationKeys returns the relative order of the
				// Strings they represent. Using CollationKeys to compare
				// Strings is generally faster than using Collator.compare.

				return key1.compareTo(key2);
			}
		});

		return propDefns;
	}

	/**
	 * Sorts a list of element by their internal names.
	 *
	 * @param elements a list of <code>DesignElementHandle</code>
	 * @return a sorted list of element.
	 */

	public static List<DesignElementHandle> sortElementsByName(List<DesignElementHandle> elements) {
		List<DesignElementHandle> temp = new ArrayList<>(elements);
		Collections.sort(temp, new Comparator<DesignElementHandle>() {

			@Override
			public int compare(DesignElementHandle o1, DesignElementHandle o2) {
				String name1 = o1.getName();
				String name2 = o2.getName();

				if (null == name1) {
					if (null == name2) {
						return 0;
					}

					return -1;
				}

				// name1 != null

				if (null == name2) {
					return 1;
				}

				return name1.compareTo(name2);
			}

		});

		return temp;
	}

	/**
	 * Uses the new name space of the current module for reference property values
	 * of the given element. This method checks the <code>content</code> and nested
	 * elements in it.
	 *
	 * @param module    the module that <code>content</code> attaches.
	 * @param content   the element to revise
	 * @param nameSpace the new name space
	 */

	public static void reviseNameSpace(Module module, DesignElement content, String nameSpace) {
		Iterator<String> propNames = content.propertyWithLocalValueIterator();
		IElementDefn defn = content.getDefn();

		while (propNames.hasNext()) {
			String propName = propNames.next();

			ElementPropertyDefn propDefn = (ElementPropertyDefn) defn.getProperty(propName);
			revisePropertyNameSpace(module, content, propDefn, nameSpace);
		}

		Iterator<DesignElement> iter = new LevelContentIterator(module, content, 1);
		while (iter.hasNext()) {
			DesignElement item = iter.next();
			reviseNameSpace(module, item, nameSpace);
		}
	}

	/**
	 * Uses the new name space of the current module for reference property values
	 * of the given element. This method checks the <code>content</code> and nested
	 * elements in it.
	 *
	 * @param module    the module that <code>content</code> attaches.
	 * @param content   the element to revise
	 * @param propDefn
	 * @param nameSpace the new name space
	 */

	public static void revisePropertyNameSpace(Module module, DesignElement content, IElementPropertyDefn propDefn,
			String nameSpace) {
		if (propDefn == null || content == null || (propDefn.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE
				&& propDefn.getTypeCode() != IPropertyType.EXTENDS_TYPE)) {
			return;
		}

		Object value = content.getLocalProperty(module, (ElementPropertyDefn) propDefn);
		if (value == null) {
			return;
		}

		ReferenceValue refValue = (ReferenceValue) value;
		refValue.setLibraryNamespace(nameSpace);
	}

	/**
	 * Determines whether there is a child in the given element, which is kind of
	 * the given element definition.
	 *
	 * @param module
	 *
	 * @param element the element to find
	 * @param defn    the element definition type
	 * @return true if there is a child in the element whose type is the given
	 *         definition, otherwise false
	 */

	public static boolean containElement(Module module, DesignElement element, IElementDefn defn) {
		if (element == null || defn == null) {
			return false;
		}

		// Check contents.

		Iterator<DesignElement> iter = new ContentIterator(module, element);
		while (iter.hasNext()) {
			DesignElement e = iter.next();
			IElementDefn targetDefn = e.getDefn();
			if (targetDefn.isKindOf(defn)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether there is a child in the given element, which is kind of
	 * the given element definition.
	 *
	 * @param module
	 *
	 * @param element     the element to find
	 * @param elementName the element definition type
	 * @return true if there is a child in the element whose type is the given
	 *         definition, otherwise false
	 */

	public static boolean containElement(Module module, DesignElement element, String elementName) {
		IElementDefn defn = MetaDataDictionary.getInstance().getElement(elementName);
		return containElement(module, element, defn);
	}

	/**
	 * Gets the copy of the given element.
	 *
	 * @param element the element to copy
	 * @param policy  how to copy the element-related values
	 *
	 * @return the copy of the element
	 */

	public static DesignElement getCopy(DesignElement element, CopyPolicy policy) {
		if (element == null) {
			return null;
		}

		try {
			DesignElement copy = (DesignElement) element.doClone(policy);
			return copy;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * Gets the copy of the given element.
	 *
	 * @param element the element to copy
	 * @return the copy of the element
	 */

	public static DesignElement getCopy(DesignElement element) {
		return getCopy(element, CopyForPastePolicy.getInstance());
	}

	/**
	 * Returns externalized message. If There is no externalized message, return
	 * null.
	 *
	 * @param element    Design element.
	 * @param propIDName Name of resource key property
	 * @param locale     the locale
	 * @return externalized message if found, otherwise <code>null</code>
	 */

	public static String searchForExternalizedValue(DesignElement element, String propIDName, ULocale locale) {
		if (element == null) {
			return null;
		}

		IElementPropertyDefn defn = element.getPropertyDefn(propIDName);
		if (defn == null) {
			return null;
		}

		String textKey = (String) element.getProperty(element.getRoot(), propIDName);
		if (StringUtil.isBlank(textKey)) {
			return null;
		}

		DesignElement temp = element;
		while (temp != null) {
			String externalizedText = temp.getRoot().getMessage(textKey, locale);
			if (externalizedText != null) {
				return externalizedText;
			}

			// if this property can not inherit, return null

			if (!defn.canInherit()) {
				return null;
			}

			if (DesignElement.NO_BASE_ID != temp.getBaseId()) {
				temp = temp.getVirtualParent();
			} else {
				temp = temp.getExtendsElement();
			}
		}
		return null;
	}

	/**
	 * Returns externalized value.
	 *
	 * @param element    Design element.
	 * @param propIDName ID of property
	 * @param propName   Name of property
	 * @param locale     the locale
	 * @return externalized value.
	 */

	public static String getExternalizedValue(DesignElement element, String propIDName, String propName,
			ULocale locale) {
		if (element == null || element.getPropertyDefn(propName) == null
				|| element.getPropertyDefn(propIDName) == null) {
			return null;
		}
		String textKey = searchForExternalizedValue(element, propIDName, locale);
		if (!StringUtil.isBlank(textKey)) {
			return textKey;
		}

		// use static text.

		return element.getStringProperty(element.getRoot(), propName);
	}

	/**
	 * Returns externalized value.
	 *
	 * @param module     module
	 * @param structure  structure.
	 * @param propIDName ID of property
	 * @param propName   Name of property
	 * @param locale     the locale
	 * @return externalized value.
	 */

	public static String getExternalizedStructValue(DesignElement element, IStructure structure, String propIDName,
			String propName, ULocale locale) {
		if (structure == null) {
			return null;
		}

		String textKey = (String) structure.getProperty(element.getRoot(), propIDName);
		if (!StringUtil.isBlank(textKey)) {
			DesignElement temp = element;
			String externalizedText = null;

			while (temp != null) {
				externalizedText = temp.getRoot().getMessage(textKey, locale);
				if (!StringUtil.isBlank(externalizedText)) {
					return externalizedText;
				}

				if (DesignElement.NO_BASE_ID != temp.getBaseId()) {
					temp = temp.getVirtualParent();
				} else {
					temp = temp.getExtendsElement();
				}
			}
		}

		return (String) structure.getProperty(element.getRoot(), propName);
	}

	/**
	 * Returns a list whose entry is of <code>IVersionInfo</code> type. Each kind of
	 * automatical conversion information is stored in one instance of
	 * <code>IVersionInfo</code>. If the size of the return list is 0, there is no
	 * conversion information.
	 *
	 * @param version the design file version
	 * @return a list containing <code>IVersionInfo</code>
	 * @deprecated using checkVersion( String version, boolean
	 *             isSupportedUnknownVersion ) for replacing
	 */
	@Deprecated
	public static List<IVersionInfo> checkVersion(String version) {
		List<IVersionInfo> rtnList = new ArrayList<>();

		int versionNo = -1;

		try {
			versionNo = VersionUtil.parseVersion(version);
		} catch (IllegalArgumentException e) {

		}

		if (versionNo < 0 || versionNo > DesignSchemaConstants.REPORT_VERSION_NUMBER) {
			rtnList.add(new VersionInfo(version, VersionInfo.INVALID_VERSION));
		}

		if (versionNo <= VersionInfo.COLUMN_BINDING_FROM_VERSION
				&& DesignSchemaConstants.REPORT_VERSION_NUMBER > VersionInfo.COLUMN_BINDING_FROM_VERSION) {
			rtnList.add(new VersionInfo(version, VersionInfo.CONVERT_FOR_COLUMN_BINDING));
		}

		return rtnList;
	}

	/**
	 * Returns a list whose entry is of <code>IVersionInfo</code> type. Each kind of
	 * automatical conversion information is stored in one instance of
	 * <code>IVersionInfo</code>. If the size of the return list is 0, there is no
	 * conversion information.
	 *
	 * @param version                   the design file version
	 * @param isSupportedUnknownVersion whether support unknown version
	 * @return a list containing <code>IVersionInfo</code>
	 *
	 */
	public static List<IVersionInfo> checkVersion(String version, boolean isSupportedUnknownVersion) {
		List<IVersionInfo> rtnList = new ArrayList<>();

		int versionNo = -1;

		try {
			versionNo = VersionUtil.parseVersion(version);
		} catch (IllegalArgumentException e) {

		}

		if (versionNo < 0) {
			rtnList.add(new VersionInfo(version, VersionInfo.INVALID_VERSION));
		} else if (versionNo > DesignSchemaConstants.REPORT_VERSION_NUMBER) {
			if (isSupportedUnknownVersion) {
				rtnList.add(new VersionInfo(version, VersionInfo.LATER_VERSION));
			} else {
				rtnList.add(new VersionInfo(version, VersionInfo.INVALID_VERSION));
			}
		}

		if (versionNo <= VersionInfo.COLUMN_BINDING_FROM_VERSION
				&& DesignSchemaConstants.REPORT_VERSION_NUMBER > VersionInfo.COLUMN_BINDING_FROM_VERSION) {
			rtnList.add(new VersionInfo(version, VersionInfo.CONVERT_FOR_COLUMN_BINDING));
		}

		return rtnList;
	}

	/**
	 * Justifies whether the given element supports template transform
	 *
	 * @param element the element to check
	 * @return true if Model supports the template element for the given one,
	 *         otherwise false
	 */

	public static boolean isTemplateSupported(DesignElement element) {
		// all the data sets support template

		if (element instanceof DataSet) {
			return true;
		}

		// not all the report items support template, eg. auto text does not
		// support template

		if (element instanceof ReportItem) {
			IChoiceSet choiceSet = MetaDataDictionary.getInstance()
					.getChoiceSet(DesignChoiceConstants.CHOICE_TEMPLATE_ELEMENT_TYPE);
			assert choiceSet != null;
			IChoice[] choices = choiceSet.getChoices();
			for (int i = 0; i < choices.length; i++) {
				String name = choices[i].getName();
				MetaDataDictionary dd = MetaDataDictionary.getInstance();

				// if name is 'ExtendedItem', then all the extension from
				// ReportItem is supported
				if (DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_EXTENDED_ITEM.equals(name)) {
					if (element instanceof ExtendedItem
							&& element.getDefn().isKindOf(dd.getElement(ReportDesignConstants.REPORT_ITEM))) {
						return true;
					}
				} else {
					IElementDefn defn = MetaDataDictionary.getInstance().getElement(name);
					if (element.getDefn().isKindOf(defn)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Checks whether the compound element is valid if the element has no extends
	 * property value or if the current element is compound elements and extends
	 * value is unresovled.
	 *
	 * @param module  the root module of the element
	 * @param element the element to justify
	 *
	 * @return <code>true</code> if the compound element is valid. Otherwise
	 *         <code>false</code>.
	 *
	 * @deprecated
	 */

	@Deprecated
	public static boolean isValidReferenceForCompoundElement(Module module, DesignElement element) {
		ElementRefValue refValue = (ElementRefValue) element.getLocalProperty(module, IDesignElementModel.EXTENDS_PROP);
		if (refValue == null) {
			return true;
		}

		if (element.getDefn().isContainer() && !refValue.isResolved()) {
			return false;
		}

		// if any ancestor of this element loses extended element, return false

		DesignElement parent = element.getExtendsElement();
		while (parent != null) {
			if (!isValidReferenceForCompoundElement(parent.getRoot(), parent)) {
				return false;
			}
			parent = parent.getExtendsElement();
		}

		return true;
	}

	/**
	 * Get virtual parent or extended parent.
	 *
	 * @param element design element which wants to get its parent.
	 * @return parent of element.
	 */

	public static DesignElement getParent(DesignElement element) {
		DesignElement parent = null;
		if (element.isVirtualElement()) {
			parent = element.getVirtualParent();
		} else {
			parent = element.getExtendsElement();
		}
		return parent;
	}

	/**
	 * Checks whether the compound element is valid.
	 * <p>
	 * If the table/grid has no rows and columns, its layout is invalid.
	 *
	 * @param module  the root module of the element
	 * @param element the element to check
	 *
	 * @return <code>true</code> if the compound element is valid. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean isValidLayout(Module module, DesignElement element) {
		if (!(element instanceof ReportItem) || !element.getDefn().isContainer()) {
			return true;
		}

		if (element instanceof TableItem) {
			return LayoutUtil.isValidLayout((TableItem) element, module);
		}

		if (element instanceof GridItem) {
			return LayoutUtil.isValidLayout((GridItem) element, module);
		}

		return true;
	}

	/**
	 * Adds an element to the name space. If the module is null, or element is null,
	 * or element is not in the tree of module, then do nothing.
	 *
	 * @param module
	 * @param element
	 */

	public static void addElement2NameSpace(Module module, DesignElement element) {
		if (module == null || element == null || !element.isManagedByNameSpace()) {
			return;
		}

		NameExecutor executor = new NameExecutor(module, element);
		executor.makeUniqueName();
		if (element.getName() != null) {
			NameSpace namespace = executor.getNameSpace();
			if (namespace != null) {
				namespace.insert(element);
			}
		}
	}

	/**
	 * Checks whether these is reference between <code>reference</code> and
	 * <code>referred</code>.
	 *
	 * @param reference
	 * @param referred
	 *
	 * @return <code>true</code> if there is reference. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean isRecursiveReference(DesignElement reference, IReferencableElement referred) {
		if (reference == referred) {
			return true;
		}

		List<BackRef> backRefs = referred.getClientList();

		List<DesignElement> referenceElements = new ArrayList<>();
		for (int i = 0; i < backRefs.size(); i++) {
			BackRef backRef = backRefs.get(i);
			DesignElement tmpElement = backRef.getElement();

			if (tmpElement == reference) {
				return true;
			}

			if (tmpElement instanceof ReferenceableElement) {
				referenceElements.add(tmpElement);
			}
		}

		for (int i = 0; i < referenceElements.size(); i++) {
			if (isRecursiveReference(reference, (ReferenceableElement) referenceElements.get(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the URL object of the given string. If the input value is in URL
	 * format, return it. Otherwise, create the corresponding file object then
	 * return the url of the file object.
	 *
	 * @param filePath the file path
	 * @return the URL object or <code>null</code> if the <code>filePath</code>
	 *         cannot be parsed to the URL.
	 */

	public static URL getURLPresentation(String filePath) {
		return URIUtilImpl.getURLPresentation(filePath);
	}

	/**
	 * Returns the tag according to the simple property type. If the property type
	 * is structure or structure list, this method can not be used.
	 *
	 * <ul>
	 * <li>EXPRESSION_TAG, if the property is expression;
	 * <li>XML_PROPERTY_TAG, if the property is xml;
	 * <li>METHOD_TAG, if the property is method;
	 * <li>PROPERTY_TAG, if the property is string, number, and so on.
	 * </ul>
	 *
	 * @param prop the property definition
	 * @return the tag of this property
	 */

	public static String getTagByPropertyType(PropertyDefn prop) {
		assert prop != null;
		assert prop.getTypeCode() != IPropertyType.STRUCT_TYPE;

		switch (prop.getTypeCode()) {
		case IPropertyType.EXPRESSION_TYPE:
			return DesignSchemaConstants.EXPRESSION_TAG;

		case IPropertyType.XML_TYPE:
			return DesignSchemaConstants.XML_PROPERTY_TAG;

		case IPropertyType.SCRIPT_TYPE:
			return DesignSchemaConstants.METHOD_TAG;

		default:
			if (prop.isEncryptable()) {
				return DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG;
			}

			return DesignSchemaConstants.PROPERTY_TAG;
		}
	}

	/**
	 * Returns the target element for the notification event.
	 *
	 * @param content  the design element
	 * @param propDefn the property definition. The property that contains the
	 *                 <code>element</code>.
	 *
	 * @return the event target.
	 */

	public static ContentElementInfo getContentContainer(DesignElement content, PropertyDefn propDefn) {
		if (content == null) {
			return null;
		}
		Module root = content.getRoot();

		DesignElement tmpElement = content;
		PropertyDefn tmpPropDefn = propDefn;

		ContentElementInfo retTarget = new ContentElementInfo(true);
		while (tmpElement != null && tmpPropDefn != null) {
			DesignElement tmpContainer = tmpElement.getContainer();
			if (tmpContainer == null) {
				return null;
			}

			int index = -1;
			if (tmpPropDefn.isList()) {
				List tmplist = (List) tmpContainer.getLocalProperty(root, (ElementPropertyDefn) tmpPropDefn);
				if (tmplist != null) {
					index = tmplist.indexOf(tmpElement);
				}
			}

			retTarget.pushStep(tmpPropDefn, index);
			if (tmpPropDefn.getTypeCode() == IPropertyType.CONTENT_ELEMENT_TYPE
					&& !(tmpContainer instanceof ContentElement)) {
				retTarget.setTopElement(tmpContainer);
				return retTarget;
			}

			ContainerContext context = tmpElement.getContainerInfo();
			if (context == null) {
				break;
			}

			tmpElement = tmpElement.getContainer();
			context = tmpElement.getContainerInfo();

			if (context == null) {
				return null;
			}

			tmpContainer = tmpElement.getContainer();
			tmpPropDefn = tmpContainer.getPropertyDefn(context.getPropertyName());
		}

		return null;
	}

	/**
	 * Checks whether element definitions are compatible with others.
	 *
	 * @param element1 the element 1
	 * @param element2 the element 2
	 * @return <code>true</code> if definitions of two elements are same or both
	 *         elements are listing elements
	 */

	public static boolean isCompatibleDataBindingElements(DesignElement element1, DesignElement element2) {
		// if one list and the other table, it is OK.

		if (element1.getDefn() != element2.getDefn()
				&& !((element1 instanceof ListingElement) && (element2 instanceof ListingElement))) {
			return false;
		}

		return true;
	}

	/**
	 * Converts the <code>sheetException</code> to CssException.
	 *
	 * @param module         the module
	 * @param styleSheet
	 * @param fileName       the css file name
	 * @param sheetException the style sheet exception
	 *
	 * @return the CssException
	 */

	public static CssException convertSheetExceptionToCssException(Module module, IncludedCssStyleSheet styleSheet,
			String fileName, StyleSheetException sheetException) {
		String tmpErrorCode = sheetException.getErrorCode();
		if (StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND.equalsIgnoreCase(tmpErrorCode)) {
			tmpErrorCode = CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND;
		} else {
			tmpErrorCode = CssException.DESIGN_EXCEPTION_BADCSSFILE;
		}

		return new CssException(module, styleSheet, new String[] { fileName }, tmpErrorCode);
	}

	/**
	 * Duplicates the default hierarchy for the given target dimension by the
	 * position index where the default hierarchy set in source dimension resides in
	 * the source dimension.
	 *
	 * @param targetDimension
	 * @param sourceDimension
	 */
	public static void duplicateDefaultHierarchy(Dimension targetDimension, Dimension sourceDimension) {
		if (targetDimension == null || sourceDimension == null) {
			return;
		}

		DesignElement hierarchy = sourceDimension.getDefaultHierarchy(sourceDimension.getRoot());
		if (hierarchy != null) {
			int index = hierarchy.getIndex(sourceDimension.getRoot());
			assert index > -1;
			targetDimension.setDefaultHierarchy(index);
		}
	}

	/**
	 * Checks the elements have container or content relationship.
	 *
	 * @param firstElement  the design element
	 * @param secondElement the design element
	 * @return <code>true</code> if the elements have container or content
	 *         relationship; <code>false</code> otherwise.
	 */
	public static boolean checkContainerOrContent(DesignElement firstElement, DesignElement secondElement) {
		if (firstElement == null || secondElement == null || (firstElement == secondElement)) {
			return false;
		}

		if (firstElement.isContentOf(secondElement)) {
			return true;
		}
		if (secondElement.isContentOf(firstElement)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines two given values are equal or not.
	 *
	 * @param value1 value1
	 * @param value2 value2
	 * @return <code>true</code> if two values are equal. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean isEquals(Object value1, Object value2) {
		// may be same string or both null.

		if (value1 == value2) {
			return true;
		}

		if ((value1 != null && value2 == null) || (value1 == null && value2 != null)) {
			return false;
		}

		assert value1 != null && value2 != null;

		if (value1.getClass() != value2.getClass()) {
			return false;
		}

		if (!value1.equals(value2)) {
			return false;
		}

		return true;
	}

	/**
	 * Checks whether the property is extension property and this property has its
	 * own model.
	 *
	 * @param propDefn the property definition.
	 * @return if the property is extension property and this property has its own
	 *         model return <true>, otherwise return <false>.
	 */
	public static boolean isExtensionPropertyOwnModel(IPropertyDefn propDefn) {
		assert propDefn != null;

		return propDefn instanceof ExtensionPropertyDefn && ((ExtensionPropertyDefn) propDefn).hasOwnModel();
	}

	/**
	 * Constructs the expression list for the given list.
	 *
	 * @param values the list
	 * @return the expression list
	 */

	public static List getExpressionCompatibleList(List<Expression> values) {
		if (values == null) {
			return null;
		}

		List newList = new ArrayList();
		if (!values.isEmpty()) {
			for (int i = 0; i < values.size(); i++) {
				Expression tmpValue = values.get(i);
				if (tmpValue != null) {
					newList.add(tmpValue.getStringExpression());
				} else {
					newList.add(null);
				}
			}
		}

		return newList;
	}

	/**
	 * Checks if the element property can be inherited or is style property.
	 *
	 * @param defn the element property definition.
	 * @return <true> if the element property can be inherited or is style property,
	 *         else return <false>.
	 */
	public static boolean canInherit(ElementPropertyDefn defn) {
		assert defn != null;
		return defn.canInherit() || defn.isStyleProperty();
	}

	/**
	 * Checks if the property supports constant expression
	 *
	 * @param defn the property definition.
	 * @return true if the property supports constant expression, else return false.
	 */
	public static boolean supportConstantExpression(IPropertyDefn defn) {
		if (IReportItemModel.ALTTEXT_PROP.equals(defn.getName())) {
			return true;
		}
		return false;
	}

}
