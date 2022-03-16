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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IDimLevel;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferencableStyledElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.MemberValue;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.Measure;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.metadata.StructureDefn;

/**
 * This class implemented visitor pattern, to flatten the report design.
 */

class ReportDesignSerializerImpl extends ElementVisitor {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ReportDesignSerializerImpl.class.getName());

	/**
	 * The created report design.
	 */

	protected ReportDesign targetDesign = null;

	/**
	 * The source design tree for the visitor pattern.
	 */

	protected ReportDesign sourceDesign = null;

	/**
	 * The stack that contains element in the targetDesign. It is used to set up
	 * container/content relationship.
	 */

	private Stack<DesignElement> elements = new Stack<>();

	/**
	 * Elements are not directly in source design. Hence, it should be created with
	 * new names and added to the target design. The key is the original element
	 * that is read in library by directly or indirectly referred by design element.
	 * The value is the newly created and will be inserted to design.
	 */

	private Map<DesignElement, DesignElement> externalElements = new LinkedHashMap<>(
			ModelUtil.MAP_CAPACITY_MEDIUM);

	/**
	 * Structures are not directly in source design. Hence, it should be created
	 * with new names and added to the target design. Currently, only have cases
	 * with embedded images.
	 */

	private Map<IStructure, IStructure> externalStructs = new LinkedHashMap<>(
			ModelUtil.MAP_CAPACITY_LOW);

	/**
	 * Cubes that need to build the dimension condition. It stores newCube/oldCube
	 * pair.
	 */
	private Map<Cube, Cube> cubes = new LinkedHashMap<>(ModelUtil.MAP_CAPACITY_LOW);

	/**
	 * Dimensions that need to build the 'defaultHierarchy'. It stores
	 * newDimension/oldDimension pair.
	 */
	private Map<Dimension, Dimension> dimensions = new LinkedHashMap<>(ModelUtil.MAP_CAPACITY_LOW);

	/**
	 * The element is on process.
	 */

	protected DesignElement currentNewElement = null;

	/**
	 * Saves all property bindings.
	 */

	private List<PropertyBinding> propertyBindings = new ArrayList<>();

	/**
	 * Map to store the pairs of localization report item theme. Key is the
	 * qualified name of old ReportItemTheme instance in the source design while
	 * value is the new created local reportItemThem in the target design.
	 */
	protected Map<String, ReportItemTheme> reportItemThemes = new LinkedHashMap<>(
			ModelUtil.MAP_CAPACITY_MEDIUM);

	/**
	 * this map maintain the mapping of created ReportItemTheme and the created
	 * report items which should use the ReportItemTheme. This map is used to update
	 * the ReportItemTheme name in report item after referred ReportItemTheme is
	 * renamed.
	 */
	protected Map<ReportItemTheme, List<ReportItem>> ReportItemThemeMapping = new LinkedHashMap<>(
			ModelUtil.MAP_CAPACITY_MEDIUM);

	protected Map<ExtendedItem, StyleHandle[]> referencedStyleMap = new LinkedHashMap<>(
			ModelUtil.MAP_CAPACITY_LOW);

	/**
	 * Returns the newly created report design.
	 *
	 * @return the newly created report design.
	 */

	public ReportDesign getTarget() {
		return targetDesign;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDesignElement
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public void visitReportDesign(ReportDesign obj) {
		long startTime = 0;
		long endTime = 0;

		if (logger.isLoggable(java.util.logging.Level.FINE)) {
			startTime = Calendar.getInstance().getTimeInMillis();
		}

		sourceDesign = obj;
		localizeDesign(obj);

		visitSlots(obj, targetDesign, IReportDesignModel.SLOT_COUNT);

		List<IElementPropertyDefn> properties = obj.getContents();
		if (properties.size() > 0) {
			visitContainerProperties(obj, targetDesign, properties);
		}

		// visit external selectors
		localizeExternalSelectors();

		addExternalElements();
		addExternalStructures();

		// handle dimension conditions
		localizeDimensionConditions();

		// handle default hierarchy in dimension elements
		localizeDefaultHierarchy();

		// add property bindings to the design, do this after external elements
		// have been added to the target design: support empty list now
		if (propertyBindings != null && !propertyBindings.isEmpty()) {
			targetDesign.setProperty(IModuleModel.PROPERTY_BINDINGS_PROP, propertyBindings);
		}

		// add report item themes to design tree
		addThemes();
		addReportItemThemes();

		// add referenced style into report, also update the theme
		processAllReferencedStyle();

		// do some memory release
		release();

		// copy version number from source to the target

		targetDesign.getVersionManager().setVersion(sourceDesign.getVersionManager().getVersion());

		if (logger.isLoggable(java.util.logging.Level.FINE)) {
			endTime = Calendar.getInstance().getTimeInMillis();

			String filename = sourceDesign.getFileName();
			if (filename == null) {
				filename = ""; //$NON-NLS-1$
			}
			logger.fine("total time to flatten design " + filename //$NON-NLS-1$
					+ " in milliseconds " + (endTime - startTime)); //$NON-NLS-1$
		}
	}

	/**
	 * Releases some memory instantly.
	 */

	private void release() {
		elements = null;
		externalElements = null;
		externalStructs = null;
		currentNewElement = null;
		cubes = null;
		propertyBindings = null;
	}

	/**
	 * By calling this method directly/indirectly, the obj must in the source design
	 * directly. Must not be in the included libraries of source design.
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDesignElement(org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public void visitDesignElement(DesignElement obj) {
		DesignElement newElement = localize(obj);
		ElementDefn elementDefn = (ElementDefn) obj.getDefn();

		if (obj.isContainer()) {
			int slotCount = elementDefn.getSlotCount();
			if (slotCount > 0) {
				visitSlots(obj, newElement, slotCount);
			}
			List<IElementPropertyDefn> properties = obj.getContents();
			if (properties.size() > 0) {
				visitContainerProperties(obj, newElement, properties);
			}
		}

		currentNewElement = newElement;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.ElementVisitor#visitContentElement
	 * (org.eclipse.birt.report.model.elements.ContentElement)
	 */

	@Override
	public void visitExtendedItem(ExtendedItem obj) {
		super.visitExtendedItem(obj);

		assert (currentNewElement instanceof ExtendedItem);
		IReportItem extItem = obj.getExtendedElement();
		if (extItem != null) {
			StyleHandle[] styles = extItem.getReferencedStyle();
			if (styles != null && styles.length > 0) {
				this.referencedStyleMap.put((ExtendedItem) currentNewElement, styles);
			}
		}
	}

	private String insertStyle(StyleHandle styleHandle) {
		Style style = (Style) styleHandle.getElement();
		Style newStyle = null;
		try {
			newStyle = (Style) style.clone();
		} catch (CloneNotSupportedException e) {
			assert (false);
		}

		if (newStyle != null) {
			ContainerContext context = new ContainerContext(targetDesign, IReportDesignModel.STYLE_SLOT);
			addElement(targetDesign, context, newStyle);
			targetDesign.manageId(newStyle, true);
			return newStyle.getName();
		}
		return null;
	}

	private void processAllReferencedStyle() {
		if (!referencedStyleMap.isEmpty()) {
			Map<StyleHandle, String> processedStyles = new HashMap<>();
			Iterator<ExtendedItem> iter = referencedStyleMap.keySet().iterator();
			while (iter.hasNext()) {
				ExtendedItem item = iter.next();
				processExtendedItemReferencedStyle(item, referencedStyleMap.get(item), processedStyles);
			}
		}
	}

	private void processExtendedItemReferencedStyle(ExtendedItem extendedItem, StyleHandle[] styles,
			Map<StyleHandle, String> processedStyles) {

		HashMap<String, String> styleMap = new HashMap<>();
		// add style and get the style name maps.
		for (int i = 0; i < styles.length; i++) {
			if (!processedStyles.containsKey(styles[i])) {
				String originalName = styles[i].getName();
				String name = insertStyle(styles[i]);
				if (name != null) {
					if (originalName != null && !originalName.equals(name)) {
						styleMap.put(originalName, name);
					}
					processedStyles.put(styles[i], name);
				}
			} else {
				String originalName = styles[i].getName();
				String name = processedStyles.get(styles[i]);
				if (!name.equals(originalName)) {
					styleMap.put(originalName, name);
				}
			}
		}
		if (!styleMap.isEmpty()) {
			IReportItem item = extendedItem.getExtendedElement();
			if (item != null) {
				item.updateStyleReference(styleMap);
			} else {
				try {
					extendedItem.initializeReportItem(targetDesign);
					item = extendedItem.getExtendedElement();
					if (item != null) {
						item.updateStyleReference(styleMap);
					}
				} catch (ExtendedElementException e) {
					logger.log(java.util.logging.Level.WARNING, "can not load extended item.", e); //$NON-NLS-1$
				}

			}
		}
	}

	/**
	 * Adds structure values to the target. Currently only has cases with embedded
	 * image.
	 */

	private void addExternalStructures() {
		ElementPropertyDefn propDefn = targetDesign.getPropertyDefn(IModuleModel.IMAGES_PROP);

		Iterator<IStructure> embeddedImages = externalStructs.values().iterator();
		StructureContext context = new StructureContext(targetDesign, propDefn, null);
		while (embeddedImages.hasNext()) {
			EmbeddedImage image = (EmbeddedImage) embeddedImages.next();

			targetDesign.rename(image);
			context.add(image);
		}

	}

	/**
	 * Adds elements values to the target. These elements are not directly in source
	 * design. May resides in libraries of source design.
	 */

	private void addExternalElements() {
		List<DesignElement> tmpElements = new ArrayList<>(externalElements.keySet());
		List<DesignElement> processedElements = new ArrayList<>();
		int index = 0;

		// need to collect old names here for OLAP elements like measures,
		// dimensions, levels.

		Map<DesignElement, List<String>> tmpOLAPNames = new HashMap<>();
		for (int i = 0; i < tmpElements.size(); i++) {
			DesignElement tmpElement = tmpElements.get(i);

			// add method to check whether this element need to update binding
			// caused by the renaming action, for not all the cube need do this
			if (needUpdateBinding(tmpElement)) {
				tmpOLAPNames.put(tmpElement, collectOLAPNames(sourceDesign, tmpElement));
			}
		}

		while (processedElements.size() < externalElements.size()) {
			DesignElement originalElement = tmpElements.get(index++);
			addExternalElement(tmpElements, processedElements, originalElement);
		}

		// need to collect new names here for OLAP elements like measures,
		// dimensions, levels. And performs renaming procedure to make sure
		// column binding expression and aggregate on list are correct.

		Iterator<Entry<DesignElement, List<String>>> iter1 = tmpOLAPNames.entrySet().iterator();
		while (iter1.hasNext()) {
			Entry<DesignElement, List<String>> entry = iter1.next();
			Cube originalElement = (Cube) entry.getKey();
			Cube newCube = (Cube) externalElements.get(originalElement);

			List<String> newNames = collectOLAPNames(targetDesign, newCube);
			List<String> oldNames = entry.getValue();

			updateReferredOLAPColumnBinding(targetDesign, newCube, buildOLAPNameMap(oldNames, newNames));
		}

	}

	/**
	 * Justifies whether this element needs to update binding expression caused by
	 * the renaming action.
	 *
	 * @param element
	 * @return
	 */
	protected boolean needUpdateBinding(DesignElement element) {
		if (element instanceof Cube) {
			return true;
		}
		return false;
	}

	/**
	 * Setups the container relationship for each external element.
	 *
	 * @param elements          the elements to add
	 * @param processedElements elements that have been added
	 * @param originalElement   the corresponding element in the source design
	 */

	private void addExternalElement(List<DesignElement> elements, List<DesignElement> processedElements,
			DesignElement originalElement) {
		if (processedElements.contains(originalElement)) {
			return;
		}

		DesignElement originalContainer = originalElement.getContainer();
		if (elements.contains(originalContainer)) {
			addExternalElement(elements, processedElements, originalContainer);
		}

		processedElements.add(originalElement);

		DesignElement tmpElement = externalElements.get(originalElement);

		DesignElement tmpContainer = getTargetContainer(originalElement, tmpElement, processedElements);

		ContainerContext context = null;

		if (tmpContainer != null) {
			context = originalElement.getContainerInfo().createContext(tmpContainer);
		}

		if (context == null && originalContainer instanceof Theme) {
			int newId = IReportDesignModel.STYLE_SLOT;
			context = new ContainerContext(targetDesign, newId);
		}

		// if so, it can only be ReportDesign and Library case.

		if (context == null && originalContainer instanceof Library) {
			context = originalElement.getContainerInfo();
			if (!context.isROMSlot()) {
				String propName = context.getPropertyName();
				if (null != propName) {
					context = new ContainerContext(tmpContainer, context.getPropertyName());
				} else {
					context = new ContainerContext(tmpContainer, DesignElement.NO_SLOT);
				}
			} else {
				int newId = DesignElement.NO_SLOT;
				switch (context.getSlotID()) {
				case ILibraryModel.THEMES_SLOT:
					newId = IDesignElementModel.NO_SLOT;
					assert false;
					break;
				case ILibraryModel.CUBE_SLOT:
					newId = IReportDesignModel.CUBE_SLOT;
					break;
				default:
					newId = context.getSlotID();
				}
				context = new ContainerContext(tmpContainer, newId);
			}
		}

		assert context != null;

		if (context.contains(targetDesign, tmpElement)) {
			return;
		}

		String originalName = originalElement.getName();
		addElement(targetDesign, context, tmpElement);
		if (tmpElement instanceof OdaDataSource) {
			if (!tmpElement.getName().equals(originalName)) {
				OdaDataSource odaDataSource = (OdaDataSource) tmpElement;
				odaDataSource.setProperty(IOdaDataSourceModel.EXTERNAL_CONNECTION_NAME, originalName);
			}
		}
		targetDesign.manageId(tmpElement, true);

		// after the id is adjusted, the property binding can be added.

		localizePropertyBindings(originalElement, tmpElement);
	}

	/**
	 * Gathers names for OLAP elements such as dimensions, levels and measures. For
	 * levels, get their full names.
	 *
	 * @param module the module
	 * @param cube   the OLAP cube
	 * @return a list containing names in strings.
	 */

	private List<String> collectOLAPNames(Module module, DesignElement cube) {
		List<String> retMap = new ArrayList<>();

		LevelContentIterator iter = new LevelContentIterator(module, cube, 3);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			if (innerElement instanceof Dimension || innerElement instanceof Measure) {
				retMap.add(innerElement.getName());
			} else if (innerElement instanceof Level) {
				retMap.add(((Level) innerElement).getFullName());
			}
		}

		return retMap;
	}

	/**
	 * Setups name mapping between old/new OLAP element names.
	 *
	 * @param oldNames old names in strings
	 * @param newNames new names in strings
	 * @return a map. The key is the old name and the value is corresponding new
	 *         name.
	 */

	private Map<String, String> buildOLAPNameMap(List<String> oldNames, List<String> newNames) {
		Map<String, String> retMap = new HashMap<>();
		for (int i = 0; i < oldNames.size(); i++) {
			String oldName = oldNames.get(i);
			String newName = newNames.get(i);

			retMap.put(oldName, newName);
		}

		return retMap;
	}

	/**
	 * Updates OLAP elements names in column bindings by using the name map.
	 *
	 * @param module  the module
	 * @param cube    the cube used by other elements
	 * @param nameMap a map. The key is the old name and the value is corresponding
	 *                new name.
	 */

	private void updateReferredOLAPColumnBinding(Module module, Cube cube, Map<String, String> nameMap) {
		// update derived measure first
		updateDerivedMeasure(module, cube, nameMap);
		List<BackRef> clients = cube.getClientList();
		for (int i = 0; i < clients.size(); i++) {
			BackRef ref = clients.get(i);
			DesignElement client = ref.getElement();
			List<Object> columnBindings = (List) client.getLocalProperty(module,
					IReportItemModel.BOUND_DATA_COLUMNS_PROP);

			if (columnBindings == null || columnBindings.isEmpty()) {
				return;
			}

			for (int j = 0; j < columnBindings.size(); j++) {
				ComputedColumn binding = (ComputedColumn) columnBindings.get(j);

				updateBindingExpr(binding, nameMap);
				updateAggregateOnList(binding, nameMap);
				updateAggregationArguments(binding, nameMap);
				updateCalculationArguments(binding, nameMap);
				updateTimeDimension(binding, nameMap);
			}
		}
	}

	/**
	 * Updates the expression of column binding. The dimension and level names will
	 * be changed if necessary.
	 *
	 * @param binding the column binding
	 * @param nameMap the name map
	 */

	private void updateBindingExpr(ComputedColumn binding, Map<String, String> nameMap) {

		Expression objExpr = binding.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER);

		if (objExpr == null) {
			return;
		}

		Expression newExpr = getUpdatedExpression(objExpr, nameMap);
		if (newExpr != null) {
			binding.setExpressionProperty(ComputedColumn.EXPRESSION_MEMBER, newExpr);
		}
	}

	private Expression getUpdatedExpression(Expression old, Map<String, String> nameMap) {
		if (old == null) {
			return null;
		}

		String expr = old.getStringExpression();
		String type = old.getType();
		Map<String, String> updateMap = getUpdateBindingMap(expr, nameMap, type);

		if (updateMap != null && !updateMap.isEmpty()) {
			String newExpr = expr;
			for (Entry<String, String> entry : updateMap.entrySet()) {
				String oldName = entry.getKey();
				String newName = entry.getValue();
				newExpr = newExpr.replaceAll("(\\W)" + oldName + "(\\W)", //$NON-NLS-1$ //$NON-NLS-2$
						"$1" + newName + "$2"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return new Expression(newExpr, old.getUserDefinedType());
		}
		return null;
	}

	/**
	 * Updates the expression of column binding. The dimension and level names will
	 * be changed if necessary.
	 *
	 * @param expr    the expression of column binding
	 * @param nameMap the name map
	 * @param type    the type of the expression
	 * @return the updated expression, or null if it doesn't needs updating
	 */

	protected Map<String, String> getUpdateBindingMap(String expr, Map<String, String> nameMap, String type) {
		Map<String, String> updateMap = new HashMap<>();
		if (IExpressionType.JAVASCRIPT.equalsIgnoreCase(type)) {
			// for the measure expression case
			Set<String> measureNameSet = null;

			try {
				measureNameSet = ExpressionUtil.getAllReferencedMeasures(expr);
			} catch (CoreException e) {
				// Do nothing
			}

			if (measureNameSet != null && !measureNameSet.isEmpty()) {
				for (String measureName : measureNameSet) {
					String newName = nameMap.get(measureName);
					if (newName != null) {
						updateMap.put(measureName, newName);
					}
				}
			} else {
				Set<IDimLevel> tmpSet = null;
				try {
					tmpSet = ExpressionUtil.getReferencedDimLevel(expr);
				} catch (CoreException e) {
					// do nothing
					return null;
				}

				Iterator<IDimLevel> dimLevels = tmpSet.iterator();
				while (dimLevels.hasNext()) {
					IDimLevel tmpObj = dimLevels.next();

					String oldName = tmpObj.getDimensionName();
					String newName = nameMap.get(oldName);
					if (newName == null) {
						continue;
					}

					if (!newName.equals(oldName)) {
						updateMap.put(oldName, newName);
					}
				}
			}

		}
		return updateMap;
	}

	/**
	 * Updates aggregation on list of column binding. Each aggregation on refers to
	 * a level name.The level names will be changed if necessary.
	 *
	 * @param binding the column binding
	 * @param nameMap the name map
	 */

	private void updateAggregateOnList(ComputedColumn binding, Map<String, String> nameMap) {
		List<String> aggreOnList = binding.getAggregateOnList();
		for (int i = 0; i < aggreOnList.size(); i++) {
			String levelFullName = aggreOnList.get(i);
			String newName = nameMap.get(levelFullName);

			if (newName == null) {
				continue;
			}

			aggreOnList.set(i, newName);
		}
	}

	private void updateAggregationArguments(ComputedColumn binding, Map<String, String> nameMap) {
		@SuppressWarnings("unchecked")
		List<AggregationArgument> arguments = (List<AggregationArgument>) binding.getProperty(null,
				ComputedColumn.ARGUMENTS_MEMBER);
		if (arguments != null && !arguments.isEmpty()) {
			for (AggregationArgument aggArg : arguments) {
				Expression objExpr = aggArg.getExpressionProperty(AggregationArgument.VALUE_MEMBER);

				if (objExpr == null) {
					return;
				}

				Expression newExpr = getUpdatedExpression(objExpr, nameMap);
				if (newExpr != null) {
					aggArg.setExpressionProperty(AggregationArgument.VALUE_MEMBER, newExpr);
				}
			}
		}
	}

	private void updateCalculationArguments(ComputedColumn binding, Map<String, String> nameMap) {
		@SuppressWarnings("unchecked")
		List<CalculationArgument> calculationArgs = (List<CalculationArgument>) binding.getProperty(null,
				ComputedColumn.CALCULATION_ARGUMENTS_MEMBER);
		if (calculationArgs != null && !calculationArgs.isEmpty()) {
			for (CalculationArgument calArg : calculationArgs) {
				Expression objExpr = calArg.getExpressionProperty(CalculationArgument.VALUE_MEMBER);

				if (objExpr == null) {
					return;
				}

				Expression newExpr = getUpdatedExpression(objExpr, nameMap);
				if (newExpr != null) {
					calArg.setExpressionProperty(CalculationArgument.VALUE_MEMBER, newExpr);
				}

			}
		}
	}

	private void updateDerivedMeasure(Module module, Cube cube, Map<String, String> nameMap) {
		List<Measure> derivedMeasureList = new ArrayList<>();

		LevelContentIterator iter = new LevelContentIterator(module, cube, 3);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			if (innerElement instanceof Measure) {
				Measure measure = (Measure) innerElement;
				if (measure.getBooleanProperty(module, MeasureHandle.IS_CALCULATED_PROP)) {
					derivedMeasureList.add(measure);
				}
			}
		}
		if (!derivedMeasureList.isEmpty()) {
			for (Measure derivedMeasure : derivedMeasureList) {
				Expression expr = (Expression) derivedMeasure.getProperty(module,
						MeasureHandle.MEASURE_EXPRESSION_PROP);
				Expression newExpr = getUpdatedExpression(expr, nameMap);
				if (newExpr != null) {
					derivedMeasure.setProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, newExpr);
				}
			}
		}
	}

	private void updateTimeDimension(ComputedColumn binding, Map<String, String> nameMap) {
		String timeDimension = (String) binding.getProperty(null, ComputedColumn.TIME_DIMENSION_MEMBER);

		String newTimeDimension = nameMap.get(timeDimension);

		if (newTimeDimension != null) {
			binding.setProperty(ComputedColumn.TIME_DIMENSION_MEMBER, newTimeDimension);
		}
	}

	/**
	 * Adds an element to the context. This method will handle container
	 * relationship, element name and element id for the inserted content and all
	 * its children.
	 *
	 * @param module
	 * @param context
	 * @param content
	 */
	protected void addElement(Module module, ContainerContext context, DesignElement content) {
		assert context != null;
		assert content != null;

		// first construct the container relationship
		context.add(module, content);

		// manage element name: the inserted content and all its children
		if (context.isManagedByNameSpace()) {
			// for ReportItemTheme, only need make sure the unique name of theme
			// itself, need not check the styles in the theme: TODO: revise
			// logic of makeUniqueName of NameHelper to ensure the style in the
			// report item theme will not be added into style name context in
			// the report design, and then remove this patch
			if (content instanceof ReportItemTheme) {
				module.makeUniqueName(content);
			} else {
				module.rename(context.getElement(), content);
			}
		}
		addElement2NameSpace(content);

		ContentIterator iter = new ContentIterator(module, content);
		while (iter.hasNext()) {
			DesignElement child = iter.next();
			addElement2NameSpace(child);
		}

		if (content instanceof Cube) {
			Cube cube = (Cube) content;
			cube.updateLayout(targetDesign);
		} else if (content instanceof TabularDimension) {
			TabularDimension dimension = (TabularDimension) content;
			dimension.updateLayout(targetDesign);
		}
	}

	/**
	 * Adds an element to the name space. If the module is null, or element is null,
	 * or element is not in the tree of module, then do nothing.
	 *
	 * @param module
	 * @param element
	 */

	private void addElement2NameSpace(DesignElement element) {
		if (element == null || !element.isManagedByNameSpace()) {
			return;
		}

		if (element.getName() != null) {
			NameExecutor executor = new NameExecutor(targetDesign, element);
			NameSpace namespace = executor.getNameSpace();
			if (namespace != null) {
				if (namespace.contains(element.getName())) {
					throw new RuntimeException("element name is not unique"); //$NON-NLS-1$
				}
				namespace.insert(element);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitStyle(org.
	 * eclipse.birt.report.model.elements.Style)
	 */

	@Override
	public void visitStyle(Style obj) {
		visitDesignElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTheme(org.
	 * eclipse.birt.report.model.elements.Theme)
	 */

	@Override
	public void visitTheme(Theme obj) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitReportItem
	 * (org.eclipse.birt.report.model.elements.ReportItem)
	 */

	@Override
	public void visitReportItem(ReportItem obj) {
		visitStyledElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.ElementVisitor#visitScalarParameter
	 * (org.eclipse.birt.report.model.elements.ScalarParameter)
	 */

	@Override
	public void visitScalarParameter(ScalarParameter obj) {
		visitParameter(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitStyledElement
	 * (org.eclipse.birt.report.model.core.StyledElement)
	 */

	@Override
	public void visitStyledElement(StyledElement obj) {
		visitDesignElement(obj);

		localizeStyle((StyledElement) currentNewElement, obj);

		localizeReportItemThemeProperty((StyledElement) currentNewElement, obj);
	}

	/**
	 * Resolves style reference in the target element. This method will copy the
	 * resolved element reference from the source element to the target design.
	 *
	 * @param targetDesign the target design to add the copied style
	 * @param target       the target element to resolve the style
	 * @param source       the source element
	 */

	private void localizeStyle(StyledElement target, StyledElement source) {
		// to localize properties values. The algorithm must match with the
		// property search path. 1. for element private value; 2. for shared
		// style value; 3. for values defined on extends parent, 4. for selector

		Set<String> notEmptyProperties = new HashSet<>();

		// first step to localize private style properties.
		// handle values defined on virtual/extends parents.

		localizeSelfStyleProperties(target, source, notEmptyProperties);

		notEmptyProperties.clear();
	}

	private void localizeReportItemThemeProperty(StyledElement target, StyledElement source) {
		// update the report item theme reference
		if (source instanceof ReportItem) {
			ReportItem sourceItem = (ReportItem) source;
			ReportItem targetItem = (ReportItem) target;

			ReportItemTheme sourceTheme = (ReportItemTheme) sourceItem.getTheme(sourceDesign);
			if (sourceTheme != null) {

				ReportItemTheme targetTheme = reportItemThemes
						.get(sourceTheme.getHandle(sourceTheme.getRoot()).getQualifiedName());
				assert targetTheme != null;
				ElementRefValue themeRef = new ElementRefValue(null, targetTheme.getName());
				themeRef.resolve(targetTheme);
				targetItem.setProperty(IReportItemModel.THEME_PROP, themeRef);
				addReportItemThemeMap(targetItem, targetTheme);

				List<ReportItem> reportItems = ReportItemThemeMapping.get(targetTheme);
				{
					if (reportItems == null) {
						reportItems = new ArrayList<>();
						reportItems.add(targetItem);
						ReportItemThemeMapping.put(targetTheme, reportItems);
					} else {
						reportItems.add(targetItem);
					}
				}
			}

		}
	}

	private void addReportItemThemeMap(ReportItem targetItem, ReportItemTheme targetTheme) {
		List<ReportItem> reportItems = ReportItemThemeMapping.get(targetTheme);
		{
			if (reportItems == null) {
				reportItems = new ArrayList<>();
				reportItems.add(targetItem);
				ReportItemThemeMapping.put(targetTheme, reportItems);
			} else {
				reportItems.add(targetItem);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitorImpl#
	 * visitTabularDimension
	 * (org.eclipse.birt.report.model.elements.olap.TabularDimension)
	 */
	@Override
	public void visitTabularDimension(TabularDimension obj) {
		if (obj.getSharedDimension(sourceDesign) != null) {
			TabularDimension newElement = (TabularDimension) localize(obj);

			newElement.updateLayout(targetDesign);

			targetDesign.manageId(newElement, true);

			currentNewElement = newElement;
		} else {
			super.visitTabularDimension(obj);
		}
	}

	/**
	 * Copies values of selector in the library into the target design. If the
	 * selector doesn't exist, localize to the report design. Otherwise, merge
	 * values in external and local selectors.
	 */

	private void localizeExternalSelectors() {
		assert elements.isEmpty();

		elements.push(targetDesign);

		Theme theme = sourceDesign.getTheme(sourceDesign);
		if (theme == null) {
			elements.pop();
			return;
		}

		Module tmpRoot = theme.getRoot();
		List<StyleElement> styles = theme.getAllStyles();
		for (int i = 0; i < styles.size(); i++) {
			Style tmpStyle = (Style) styles.get(i);
			visitExternalSelector(tmpStyle, tmpRoot);
		}

		elements.pop();
	}

	/**
	 * Copies style values from source to the target if corresponding values of
	 * target are null. This method follows the same algorithm that is defined in
	 * PropertySearchAlgorithm.
	 * <ul>
	 * <li>localize private style properties.
	 * <li>localize style properties on extends/virtual parents.
	 * </ul>
	 *
	 * @param target             the target element
	 * @param source             the source element
	 * @param notEmptyProperties
	 */

	private void localizeSelfStyleProperties(StyledElement target, StyledElement source,
			Set<String> notEmptyProperties) {
		StyledElement tmpElement = source;

		while (tmpElement != null) {
			Module root = tmpElement.getRoot();
			localizePrivateStyleProperties(target, tmpElement, root, notEmptyProperties);

			Style style = (Style) tmpElement.getStyle(root);

			// handle only when the style is not local one but a library
			// resource

			if (style != null) {
				Module styleRoot = style.getRoot();
				if (styleRoot != sourceDesign) {
					localizePrivateStyleProperties(target, style, styleRoot, notEmptyProperties);
				} else {
					target.setStyleName(tmpElement.getStyleName());
				}
			}

			if (tmpElement.isVirtualElement()) {
				tmpElement = (StyledElement) tmpElement.getVirtualParent();
			} else {
				tmpElement = (StyledElement) tmpElement.getExtendsElement();
			}
		}

	}

	/**
	 * Copies local properties from <code>style</code> to the <code>target</code>.
	 *
	 * @param target       the target element
	 * @param style        the style element
	 * @param targetDesign the module of the target element
	 */

	private void localizePrivateStyleProperties(DesignElement target, DesignElement source, Module root,
			Set<String> notEmptyProperties) {
		if (!source.hasLocalPropertyValues()) {
			return;
		}

		// copy all the local values in the style

		IElementDefn defn = source.getDefn();
		Iterator<String> iter = source.propertyWithLocalValueIterator();

		while (iter.hasNext()) {
			ElementPropertyDefn prop = (ElementPropertyDefn) defn.getProperty(iter.next());

			// the property may be user-defined property. So, the value may be
			// null.

			if (prop == null || !prop.isStyleProperty()) {
				continue;
			}

			String propName = prop.getName();
			if (notEmptyProperties.contains(propName)) {
				continue;
			}

			ElementPropertyDefn targetProp = target.getPropertyDefn(propName);

			if (targetProp == null) {
				continue;
			}

			if (target.getLocalProperty(targetDesign, prop) != null) {
				notEmptyProperties.add(propName);
				continue;
			}

			Object value = source.getLocalProperty(root, prop);

			// only handle values that not set in the target element

			if (value == null) {
				continue;
			}

			switch (targetProp.getTypeCode()) {
			case IPropertyType.LIST_TYPE:
				target.setProperty(targetProp, ModelUtil.copyValue(targetProp, value));
				break;
			case IPropertyType.STRUCT_TYPE:
				handleStructureValue(target, targetProp, value);
				break;
			default:
				target.setProperty(targetProp, ModelUtil.copyValue(targetProp, value));
			}

			notEmptyProperties.add(propName);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitMasterPage
	 * (org.eclipse.birt.report.model.elements.MasterPage)
	 */

	@Override
	public void visitMasterPage(MasterPage obj) {
		visitStyledElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitGroup(org.
	 * eclipse.birt.report.model.elements.GroupElement)
	 */

	@Override
	public void visitGroup(GroupElement obj) {
		visitDesignElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.ElementVisitor#visitRow(org.eclipse
	 * .birt.report.model.elements.TableRow)
	 */

	@Override
	public void visitRow(TableRow obj) {
		visitStyledElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.ElementVisitor#visitCell(org.eclipse
	 * .birt.report.model.elements.Cell)
	 */

	@Override
	public void visitCell(Cell obj) {
		visitStyledElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitColumn(org
	 * .eclipse.birt.report.model.elements.TableColumn)
	 */

	@Override
	public void visitColumn(TableColumn obj) {
		visitStyledElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSet(org
	 * .eclipse.birt.report.model.elements.DataSet)
	 */

	@Override
	public void visitDataSet(DataSet obj) {
		visitDesignElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSource
	 * (org.eclipse.birt.report.model.elements.DataSource)
	 */

	@Override
	public void visitDataSource(DataSource obj) {
		visitDesignElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.elements.ElementVisitor#
	 * visitTemplateParameterDefinition
	 * (org.eclipse.birt.report.model.elements.TemplateParameterDefinition)
	 */

	@Override
	public void visitTemplateParameterDefinition(TemplateParameterDefinition obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the slots of the element.
	 *
	 * @param obj the element to traverse
	 */

	private void visitSlots(DesignElement obj, DesignElement newElement, int slotCount) {
		elements.push(newElement);
		for (int i = 0; i < slotCount; i++) {
			// report item theme slot has serialized in localizeReportItemThemes
			if (obj == sourceDesign && i == IReportDesignModel.THEMES_SLOT) {
				continue;
			}
			visitContents(sourceDesign, new ContainerContext(obj, i));
		}
		elements.pop();
	}

	/**
	 * Visits the slots of the element.
	 *
	 * @param obj the element to traverse
	 */

	private void visitExternalSlots(DesignElement obj, DesignElement newElement, int slotCount) {
		elements.push(newElement);
		for (int i = 0; i < slotCount; i++) {
			visitExternalContents(sourceDesign, new ContainerContext(obj, i));
		}
		elements.pop();
	}

	/**
	 * Visits the container properties of the given element.
	 *
	 * @param obj
	 * @param newElement
	 * @param properties
	 */
	private void visitContainerProperties(DesignElement obj, DesignElement newElement,
			List<IElementPropertyDefn> properties) {
		elements.push(newElement);
		for (int i = 0; i < properties.size(); i++) {
			PropertyDefn propDefn = (PropertyDefn) properties.get(i);
			visitContents(sourceDesign, new ContainerContext(obj, propDefn.getName()));
		}
		elements.pop();
	}

	/**
	 * Returns a newly created element by given elements. Do not copies values here.
	 * It only concerns names, container/content relationship.
	 *
	 * @param element the design element
	 * @return a newly created element
	 */

	private DesignElement createNewElement(DesignElement element) {
		ContainerContext sourceContainment = element.getContainerInfo();

		ContainerContext containment = null;
		DesignElement container = elements.peek();
		String containmentProp = sourceContainment.getPropertyName();
		if (containmentProp != null) {
			containment = new ContainerContext(container, containmentProp);
		} else {
			containment = new ContainerContext(container, sourceContainment.getSlotID());
		}
		DesignElement newElement = newElement(element.getDefn().getName(), element.getName(), containment).getElement();

		// if the element is an external element. do not add to the design now.
		// should be added in the end by addExternalElements.

		Set<DesignElement> externalOriginalElements = externalElements.keySet();
		

		// setup container relationship

		if (externalOriginalElements.contains(element) || (element instanceof ReportDesign)) {
			return newElement;
		}

		if (newElement instanceof GroupElement) {
			newElement.setProperty(GroupElement.GROUP_NAME_PROP,
					element.getLocalProperty(sourceDesign, GroupElement.GROUP_NAME_PROP));

		}

		newElement.setID(element.getID());
		addElement(targetDesign, containment, newElement);

		// in localizeDesign, we localize all the report item themes in the
		// included libraries but not add it to id-map, therefore we need NOT
		// manage element ID for it is unique as expected. Add it to id-map
		// directly
		if (!(newElement instanceof ContentElement)) {
			targetDesign.addElementID(newElement);
		}

		return newElement;
	}

	/**
	 * Creates a structure by the given structure. The given structure must be a
	 * structure that is not directly defined in the source design.
	 *
	 * @param struct the source structure
	 * @return the new structure
	 */

	private IStructure visitExternalStruct(IStructure struct) {
		IStructure newStrcut = struct.copy();
		cacheMapping(struct, newStrcut);
		return newStrcut;
	}

	/**
	 * Localizes the element, including "extends" relationship, and reference
	 * relationship.
	 * <p>
	 * By calling this method directly/indirectly, the obj must in the source design
	 * directly. Must not be in the included libraries of source design.
	 *
	 * @param element the element to localize
	 */

	protected DesignElement localize(DesignElement element) {
		DesignElement newElement = createNewElement(element);
		DesignElement extendsElement = element.getExtendsElement();
		if (extendsElement != null) {
			targetDesign.cacheFlattenElement(extendsElement, newElement);
		}
		localizePropertyValues(element, newElement);
		localizePropertyBindings(element, newElement);

		// a temporary fix to handle the linkedDataModel measure/dimension namespace
		// issue.
		if (newElement instanceof ExtendedItem) {
			if (((ExtendedItem) newElement).getProperty(newElement.getRoot(), IExtendedItemModel.EXTENSION_NAME_PROP)
					.equals("MeasureView")) {
				ElementRefValue value = (ElementRefValue) newElement.getProperty(newElement.getRoot(), "measure");
				if (value != null) {
					value.setLibraryNamespace(null);
					newElement.setProperty("measure", value);
				}
			}
			if (((ExtendedItem) newElement).getProperty(newElement.getRoot(), IExtendedItemModel.EXTENSION_NAME_PROP)
					.equals("DimensionView")) {
				ElementRefValue value = (ElementRefValue) newElement.getProperty(newElement.getRoot(), "dimension");
				if (value != null) {
					value.setLibraryNamespace(null);
					newElement.setProperty("dimension", value);
				}
			}
		}
		return newElement;
	}

	/**
	 * Copies property bindings from <code>element</code> to the cached list.
	 * <p>
	 * The id in the bindings is changed from <code>element</code> to
	 * <code>newElement</code>. The binding that is near to the report design takes
	 * the higher priority.
	 *
	 * @param element    the source element
	 * @param newElement the target element
	 */

	private void localizePropertyBindings(DesignElement element, DesignElement newElement) {
		// element maybe lies in library, so getRoot rather than sourceDesign
		DesignElementHandle tmpElementHandle = element.getHandle(element.getRoot());
		List<PropertyBinding> elementBindings = tmpElementHandle.getPropertyBindings();

		List<PropertyBinding> newList = new ArrayList<>();
		long newID = newElement.getID();

		for (int i = 0; i < elementBindings.size(); i++) {
			PropertyBinding propBinding = elementBindings.get(i);

			// use the copy one instead of the source

			PropertyBinding newBinding = (PropertyBinding) propBinding.copy();
			newBinding.setID(newID);
			newList.add(newBinding);
		}

		propertyBindings.addAll(newList);
	}

	/**
	 *
	 * @param source
	 * @return the localized design
	 */

	protected void localizeDesign(ReportDesign source) {
		targetDesign = new ReportDesign(source.getSession());

		targetDesign.setFileName(source.getFileName());
		targetDesign.setSystemId(source.getSystemId());

		targetDesign.setID(targetDesign.getNextID());
		targetDesign.addElementID(targetDesign);
		// handle module options
		ModuleOption options = source.getOptions();
		try {
			if (options != null) {
				targetDesign.setOptions(((ModuleOption) options.copy()));
			}
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		localizePropertyValues(source, targetDesign);

		localizeIncludeResourceValues(source, targetDesign);

		localizeScriptLibValues(source, targetDesign);

		localizeIncludeScriptValues(source, targetDesign);

		// css style sheet must be treated here. It is different from other
		// elements and property values.

		visitCssStyleSheets(source, targetDesign);

		// localize all the library reportItemTheme
		localizeReportItemThemes(source, targetDesign);
	}

	private void localizeReportItemThemes(ReportDesign source, ReportDesign target) {
		// first, export all the local report item theme elements in the report
		// design itself
		elements.push(target);
		visitContents(source, new ContainerContext(source, IReportDesignModel.THEMES_SLOT));
		elements.pop();

		// second, copy all the external report item themes in the libraries
		// whether it is referred or not
		List<Library> libraries = source.getLibraries();
		if (libraries == null || libraries.isEmpty()) {
			return;
		}

		for (Library lib : libraries) {
			List<DesignElement> themes = lib.getSlot(ILibraryModel.THEMES_SLOT).getContents();
			if (themes == null || themes.isEmpty()) {
				continue;
			}

			for (DesignElement theme : themes) {
				if (theme instanceof ReportItemTheme) {
					ReportItemTheme reportItemTheme = (ReportItemTheme) theme;
					localizeReportItemTheme(reportItemTheme);
				}
			}
		}
	}

	private void localizeReportItemTheme(ReportItemTheme sourceTheme) {
		assert sourceTheme != null;

		// make a copy of the source theme
		ReportItemTheme newTheme = null;
		try {
			newTheme = (ReportItemTheme) sourceTheme.FlattenClone();
			String newName = sourceTheme.getName();
			String namespace = sourceTheme.getRoot().getNamespace();
			if (namespace == null) {
				newName += "-report";
			} else {
				newName += "-" + namespace;
			}
			newTheme.setName(newName);
		} catch (CloneNotSupportedException e) {
			// do nothing
		}
		if (newTheme == null) {
			return;
		}

		reportItemThemes.put(sourceTheme.getHandle(sourceTheme.getRoot()).getQualifiedName(), newTheme);

		// do not add to tree, for element ID location issues
	}

	private void addReportItemThemes() {
		if (reportItemThemes == null || reportItemThemes.isEmpty()) {
			return;
		}
		Iterator<ReportItemTheme> themeIter = reportItemThemes.values().iterator();
		while (themeIter.hasNext()) {
			ReportItemTheme newTheme = themeIter.next();

			// for original design theme, do nothing
			if (newTheme.getContainer() != null) {
				continue;
			}

			// add external library theme
			ContainerContext context = new ContainerContext(targetDesign, IReportDesignModel.THEMES_SLOT);
			String originalName = newTheme.getName();
			addElement(targetDesign, context, newTheme);
			targetDesign.manageId(newTheme, true);
			if (!originalName.equals(newTheme.getName())) {
				// the theme is renamed, all referred item should be updated
				List<ReportItem> reportItems = this.ReportItemThemeMapping.get(newTheme);
				if (reportItems != null && !reportItems.isEmpty()) {
					for (ReportItem reportItem : reportItems) {
						ElementRefValue themeRef = new ElementRefValue(null, newTheme.getName());
						themeRef.resolve(newTheme);
						reportItem.setProperty(IReportItemModel.THEME_PROP, themeRef);
					}
				}
			}
		}
	}

	private void addThemes() {
		Theme sourceTheme = sourceDesign.getTheme();
		if (sourceTheme == null) {
			return;
		}
		String themeName = sourceTheme.getName();
		Theme newTheme = new Theme();
		String namespace = sourceTheme.getRoot().getNamespace();
		if (namespace != null) {
			newTheme.setName(themeName + "-" + namespace);
		}
		// add external library theme
		ContainerContext context = new ContainerContext(targetDesign, IReportDesignModel.THEMES_SLOT);
		addElement(targetDesign, context, newTheme);
		targetDesign.manageId(newTheme, true);
		ReportDesignHandle targetDesignHandle = (ReportDesignHandle) targetDesign.getHandle(targetDesign);
		try {
			targetDesignHandle.setThemeName(newTheme.getName());
		} catch (SemanticException ex) {
		}
	}

	/**
	 * Gets the script library names of root element.
	 *
	 * @param ScriptLibList the list of script library.
	 * @return the script library names of root element.
	 */

	private List<Object> getRootScriptLibsName(List<Object> scriptLibList) {
		List<Object> scriptLibsPath = new ArrayList<>();

		for (int i = 0; i < scriptLibList.size(); i++) {
			ScriptLib sourceScriptLib = (ScriptLib) scriptLibList.get(i);
			scriptLibsPath.add(sourceScriptLib.getName());
		}

		return scriptLibsPath;
	}

	/**
	 * Gets the script library of root element.
	 *
	 * @param root the root element
	 * @return the script library of root element.
	 */

	private List<Object> getRootScriptLibs(ReportDesign root) {
		Object obj = root.getProperty(root, IModuleModel.SCRIPTLIBS_PROP);
		if (obj == null) {
			return Collections.emptyList();
		}

		return (List<Object>) obj;
	}

	/**
	 * Flattens the scriptLibs of the libraries to the report design.
	 *
	 * @param source the source element
	 * @param target the target element
	 */

	private void localizeScriptLibValues(ReportDesign source, ReportDesign target) {

		List<Library> libs = source.getAllLibraries();

		List<Object> targetValueList = new ArrayList<>(getRootScriptLibs(source));
		List<Object> relativePathList = getRootScriptLibsName(targetValueList);

		ElementPropertyDefn propDefn = source.getPropertyDefn(IModuleModel.SCRIPTLIBS_PROP);

		for (int i = 0; i < libs.size(); i++) {
			Library lib = libs.get(i);

			Object obj = lib.getProperty(lib, propDefn);

			if (obj == null) {
				continue;
			}

			List<Object> sourceValueList = (List<Object>) obj;

			for (int j = 0; j < sourceValueList.size(); j++) {
				ScriptLib sourceScriptLib = (ScriptLib) sourceValueList.get(j);
				String sourceScriptLibPath = sourceScriptLib.getName();

				if (!relativePathList.contains(sourceScriptLibPath)) {

					ScriptLib targetScriptLib = new ScriptLib();
					targetScriptLib.setName(sourceScriptLibPath);

					targetScriptLib.setContext(new StructureContext(target,
							target.getPropertyDefn(IModuleModel.SCRIPTLIBS_PROP), targetScriptLib));

					relativePathList.add(sourceScriptLibPath);
					targetValueList.add(targetScriptLib);
				}
			}
		}

		if (!targetValueList.isEmpty()) {
			target.setProperty(propDefn, targetValueList);
		}
	}

	/**
	 * Flattens the included resources of the library to the report design.
	 *
	 * @param source the source element
	 * @param target the target element
	 */

	void localizeIncludeResourceValues(ReportDesign source, ReportDesign target) {
		localizeIncludeValues(source, target, IModuleModel.INCLUDE_RESOURCE_PROP);
	}

	/**
	 * Flattens the included scripts of the library to the report design.
	 *
	 * @param source the source element
	 * @param target the target element
	 */
	private void localizeIncludeScriptValues(ReportDesign source, ReportDesign target) {
		localizeIncludeValues(source, target, IModuleModel.INCLUDE_SCRIPTS_PROP);
	}

	/**
	 * Flattens the included values (resources and scripts) of the library to the
	 * report design.
	 *
	 * @param source   the source element
	 * @param target   the target element
	 * @param propName the property name of the value
	 */
	private void localizeIncludeValues(ReportDesign source, ReportDesign target, String propName) {
		ElementPropertyDefn propDefn = source.getPropertyDefn(propName);

		Object obj = source.getProperty(source, propDefn);
		List<Object> newValues = new ArrayList<>();
		if (obj != null) {
			newValues.addAll((List<Object>) obj);
		}

		for (Library lib : source.getAllLibraries()) {
			Object libObj = lib.getProperty(lib, propDefn);

			if (libObj == null) {
				continue;
			}

			for (Object value : (List<Object>) libObj) {
				if (!newValues.contains(value)) {
					newValues.add(value);
				}
			}
		}

		if (!newValues.isEmpty()) {
			target.setProperty(propDefn, newValues);
		}
	}

	// css style related methods.

	/**
	 * Localizes the css style sheets from the source to the target.
	 *
	 * @param source the source module
	 * @param target the target module
	 */

	private void visitCssStyleSheets(ReportDesign source, ReportDesign target) {
		List<CssStyleSheet> allSheet = new ArrayList<>();
		List<CssStyleSheet> sheets = source.getCsses();
		for (int i = 0; i < sheets.size(); i++) {
			CssStyleSheet sheet = sheets.get(i);
			CssStyleSheet newSheet = visitCssStyleSheet(sheet);
			newSheet.setContainer(target);
			target.addCss(newSheet);
			allSheet.add(newSheet);
		}
		Theme theme = source.getTheme(source.getRoot());
		if (theme != null) {
			sheets = theme.getCsses();
			for (int i = 0; i < sheets.size(); i++) {
				CssStyleSheet sheet = sheets.get(i);
				CssStyleSheet newSheet = visitCssStyleSheet(sheet);
				newSheet.setContainer(target);
				target.addCss(newSheet);
				allSheet.add(newSheet);
			}
		}

		if (!allSheet.isEmpty()) {
			ArrayList value = new ArrayList();
			for (CssStyleSheet sheet : sheets) {
				IncludedCssStyleSheet css = StructureFactory.createIncludedCssStyleSheet();
				css.setFileName(sheet.getFileName());
				css.setExternalCssURI(sheet.getExternalCssURI());
				css.setUseExternalCss(sheet.isUseExternalCss());
				value.add(css);
			}
			ElementPropertyDefn defn = target.getPropertyDefn(IReportDesignModel.CSSES_PROP);
			if (!value.isEmpty()) {
				target.setProperty(defn, ModelUtil.copyValue(defn, value));
			}
		}
	}

	/**
	 * Localizes a css style sheets.
	 *
	 */

	private CssStyleSheet visitCssStyleSheet(CssStyleSheet sheet) {
		CssStyleSheet newSheet = new CssStyleSheet();
		newSheet.setFileName(sheet.getFileName());
		newSheet.setExternalCssURI(sheet.getExternalCssURI());
		newSheet.setUseExternalCss(sheet.isUseExternalCss());
		List<CssStyle> styles = sheet.getStyles();
		for (int i = 0; i < styles.size(); i++) {
			CssStyle style = styles.get(i);
			CssStyle newStyle = visitCssStyle(style);
			newStyle.setCssStyleSheet(newSheet);

			newSheet.addStyle(newStyle);
		}
		return newSheet;
	}

	/**
	 * Localizes a css style.
	 *
	 */

	private CssStyle visitCssStyle(CssStyle style) {
		CssStyle newStyle = new CssStyle(style.getName());
		localizePrivateStyleProperties(newStyle, style, (Module) style.getRoot(), new HashSet<String>());

		return newStyle;
	}

	/**
	 * Creates am element by the given element. The given element must be the one
	 * that is not directly defined in the source design.
	 *
	 * @param struct the source element
	 * @return the new element
	 */

	private DesignElement visitExternalElement(DesignElement element) {
		if (element instanceof Theme) {
			return null;
		}

		// ElementFactory factory = new ElementFactory( targetDesign );
		// DesignElement newElement = factory.newElement(
		// element.getDefn( ).getName( ), element.getName( ) )
		// .getElement( );

		DesignElement newElement = ElementFactoryUtil.newElement(element.getDefn().getName(), element.getName());

		IElementDefn elementDefn = newElement.getDefn();
		if (elementDefn.isContainer()) {
			int slotCount = elementDefn.getSlotCount();
			if (slotCount > 0) {
				visitExternalSlots(element, newElement, slotCount);
			}
			List<IElementPropertyDefn> properties = elementDefn.getContents();
			if (properties.size() > 0) {
				visitExternalContainerProperties(element, newElement, properties);
			}
		}

		localizePropertyValues(element, newElement);
		cacheMapping(element, newElement);

		if (newElement instanceof Cube) {
			Cube cube = (Cube) newElement;
			cube.updateLayout(targetDesign);
		} else if (newElement instanceof TabularDimension) {
			TabularDimension dimension = (TabularDimension) newElement;
			dimension.updateLayout(targetDesign);
		}

		return newElement;
	}

	/**
	 * Creates am element by the given element. The given element must be the one
	 * that is not directly defined in the source design.
	 *
	 * @param struct the source element
	 * @return the new element
	 */

	private DesignElement visitExternalReferencableStyledElement(ReferencableStyledElement element,
			DesignElement sourceElement) {
		// for any report item, cannot simply add to the target design since
		// this can break the original layout.

		ReportItem tmpItem = (ReportItem) element;

		// element IDs in the design/library are
		// different. One assumption is that if the element is in the source
		// design, its ID will not be changed in the target design.

		// first, finds out corresponding extends child in the design;
		// second, figure out the virtual child matched the input element;
		// third, return the value

		// current the case is hostChart, and both chart must be in the one
		// crosstab.

		DesignElement derivedElement = findExtendsChild(tmpItem, sourceElement);

		if (derivedElement == null) {
			// error

			logger.log(java.util.logging.Level.WARNING, "Error occurs during resolves element references."); //$NON-NLS-1$
			return null;
		}

		DesignElement tmpItem1 = findMatchedVirtualChild(sourceDesign, derivedElement, tmpItem);

		if (tmpItem1 == null) {
			// error

			logger.log(java.util.logging.Level.WARNING, "Error occurs during resolves element references."); //$NON-NLS-1$
			return null;
		}

		tmpItem1 = targetDesign.getElementByID(tmpItem1.getID());

		if (tmpItem1 == null) {
			// error

			logger.log(java.util.logging.Level.WARNING, "Error occurs during resolves element references."); //$NON-NLS-1$
		}

		return tmpItem1;
	}

	/**
	 * Returns elements that extends the given element or the element contains the
	 * given element.
	 *
	 * @param element the given element
	 * @return a list containing elements.
	 */

	private DesignElement findExtendsChild(ReferencableStyledElement element, DesignElement sourceElement) {
		DesignElement tmpElement = element;

		DesignElement sourceContainer = sourceElement;
		while (sourceContainer != null) {
			if (sourceContainer.getExtendsElement() != null) {
				break;
			}

			sourceContainer = sourceContainer.getContainer();
		}

		while (tmpElement != null) {
			if (!(tmpElement instanceof ReferencableStyledElement)) {
				tmpElement = tmpElement.getContainer();
				continue;
			}

			List<DesignElement> clients = tmpElement.getDerived();

			for (int i = 0; i < clients.size(); i++) {
				DesignElement tmpDerived = clients.get(i);
				if (tmpDerived.getRoot() == sourceDesign && tmpDerived == sourceContainer) {
					return tmpDerived;
				}
			}

			tmpElement = tmpElement.getContainer();
		}

		return tmpElement;
	}

	/**
	 * Returns the element of which the virtual parent is the given element.
	 *
	 * @param module        the root
	 * @param container     the element
	 * @param virtualParent the virtual parent element
	 * @return the matched element or null
	 */

	private DesignElement findMatchedVirtualChild(Module module, DesignElement container, DesignElement virtualParent) {
		ContentIterator iter1 = new ContentIterator(module, container);

		while (iter1.hasNext()) {
			DesignElement tmpElement = iter1.next();
			if (tmpElement.getBaseId() == virtualParent.getID()) {
				return tmpElement;
			}
		}

		return null;
	}

	/**
	 * Visits the container properties of the given element.
	 *
	 * @param obj
	 * @param newElement
	 * @param properties
	 */

	private void visitExternalContainerProperties(DesignElement obj, DesignElement newElement,
			List<IElementPropertyDefn> properties) {
		elements.push(newElement);
		for (int i = 0; i < properties.size(); i++) {
			PropertyDefn propDefn = (PropertyDefn) properties.get(i);
			visitExternalContents(sourceDesign, new ContainerContext(obj, propDefn.getName()));
		}
		elements.pop();
	}

	/**
	 * Visits the contents of the given context. Allows a derived class to traverse
	 * downward though the design tree.
	 *
	 * @param module  the module where the contents reside
	 * @param context the container context where the contents reside
	 */

	private void visitExternalContents(Module module, ContainerContext context) {
		List<DesignElement> contents = context.getContents(module);
		Iterator<DesignElement> iter = contents.iterator();

		DesignElement tmpContainer = elements.peek();
		while (iter.hasNext()) {
			DesignElement tmpElement = iter.next();
			DesignElement cachedExternalElement = getCache(tmpElement);
			if (cachedExternalElement != null) {
				ContainerContext newContext = context.createContext(tmpContainer);
				addElement(module, newContext, cachedExternalElement);
			} else {
				visitExternalElement(tmpElement);
			}
		}
	}

	/**
	 * Creates am element by the given element. The given element must be the one
	 * that is not directly defined in the source design.
	 *
	 * @param element     the source element
	 * @param elementRoot the root of the element
	 */

	private void visitExternalSelector(Style element, Module elementRoot) {
		String tmpStyleName = element.getName().toLowerCase();

		if (MetaDataDictionary.getInstance().getPredefinedStyle(tmpStyleName) == null) {
			return;
		}

		Style sourceDesignStyle = (Style) targetDesign.findNativeStyle(tmpStyleName);

		if (sourceDesignStyle == null) {
			ElementFactory factory = new ElementFactory(targetDesign);
			DesignElement newElement = factory.newElement(element.getDefn().getName(), element.getName()).getElement();

			localizePropertyValues(element, newElement);
			cacheMapping(element, newElement);
			newElement.setName(tmpStyleName);

			return;
		}

		Iterator<String> iter1 = element.propertyWithLocalValueIterator();
		while (iter1.hasNext()) {
			String elem = iter1.next();

			if (sourceDesignStyle.getLocalProperty(targetDesign, elem) != null) {
				continue;
			}

			Object value = element.getLocalProperty(elementRoot, elem);
			sourceDesignStyle.setProperty(elem, value);
		}
	}

	/**
	 * Copies user property definitions from element to newElement.
	 *
	 * @param element    the source element
	 * @param newElement the target element
	 */

	private void localizeUserPropDefn(DesignElement element, DesignElement newElement) {
		Iterator<UserPropertyDefn> iter = null;
		DesignElement current = element;

		do {
			if (current.hasUserProperties()) {
				iter = current.getLocalUserProperties().iterator();
				while (iter.hasNext()) {
					UserPropertyDefn uDefn = iter.next();
					if (newElement.getLocalUserPropertyDefn(uDefn.getName()) != null) {
						continue;
					}
					newElement.addUserPropertyDefn((UserPropertyDefn) uDefn.copy());
				}
			}

			if (current.isVirtualElement()) {
				current = current.getVirtualParent();
			} else {
				current = current.getExtendsElement();
			}

		} while (current != null);

	}

	protected Iterator<IElementPropertyDefn> getLocalizablePropertyDefns(DesignElement element) {
		// get properties from ascendants.

		Iterator<IElementPropertyDefn> iter = element.getPropertyDefns().iterator();

		return iter;
	}

	/**
	 * Copies all values from element to newElement. Structure, element reference
	 * values, etc. are dumped as a new copy.
	 *
	 * @param element    the source element
	 * @param newElement the target element
	 */

	private void localizePropertyValues(DesignElement element, DesignElement newElement) {
		// copy user property definitions first, otherwise definition will
		// not be found when copying property values

		localizeUserPropDefn(element, newElement);

		Module root = element.getRoot();
		if (element instanceof IExtendableElement) {
			ModelUtil.duplicateExtensionIdentifier(element, newElement, root);
		}

		// get properties from ascendants.

		Iterator<IElementPropertyDefn> iter = getLocalizablePropertyDefns(element);
		while (iter.hasNext()) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();

			String propName = propDefn.getName();

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if (IDesignElementModel.EXTENDS_PROP.equals(propName)
					|| IDesignElementModel.USER_PROPERTIES_PROP.equals(propName)
					|| ISupportThemeElementConstants.THEME_PROP.equals(propName)
					|| IModuleModel.LIBRARIES_PROP.equals(propName)
					|| IModuleModel.PROPERTY_BINDINGS_PROP.equals(propName)) {
				continue;
			}

			// style properties are handled in styledElement.

			if (propDefn.isStyleProperty() && !(element instanceof Style)) {
				continue;
			}

			Object value = getLocalizablePropertyValue(root, element, propDefn);

			if (IStyledElementModel.STYLE_PROP.equals(propName)) {
				// handle unresolved style name. If using external css style,
				// then style name may be unresolved
				if (propDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
					if (value != null && !((ElementRefValue) value).isResolved() && element instanceof StyledElement) {
						((StyledElement) newElement).setStyleName(((ElementRefValue) value).getName());
					}
					continue;
				}
			}

			if (value == null) {
				continue;
			}

			switch (propDefn.getTypeCode()) {
			case IPropertyType.ELEMENT_REF_TYPE:
				if (newElement instanceof Dimension && IDimensionModel.DEFAULT_HIERARCHY_PROP.equals(propName)) {
					handleDefaultHierarchy((Dimension) newElement, (Dimension) element);
					break;
				}
				handleElementRefValue(newElement, element, propDefn, (ElementRefValue) value);
				break;
			case IPropertyType.STRUCT_REF_TYPE:
				handleStructureRefValue(newElement, propDefn, (StructRefValue) value);
				break;
			case IPropertyType.LIST_TYPE:
				if (propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
					handleElementRefValueList(newElement, propDefn, (List) value);
				} else if (newElement.getLocalProperty(null, propDefn) == null) {
					newElement.setProperty(propDefn, ModelUtil.copyValue(propDefn, value));
				}
				break;
			case IPropertyType.STRUCT_TYPE:

				// handle dimension conditions in cube: 1) if the cube
				// resides directly in the design, then handle it in special
				// method 2) if the cube is not in the design but referred
				// by some elements in the design, such as design x-tab
				// extends a library x-tab, and the library x-tab refers a
				// library cube; in this case, no need special handle for
				// dimension condition, so call handleStructureValue is ok.
				if (newElement instanceof Cube && ITabularCubeModel.DIMENSION_CONDITIONS_PROP.equals(propDefn.getName())
						&& element.getRoot() == sourceDesign) {
					handleDimensionConditions((Cube) newElement, (Cube) element);
				} else {
					handleStructureValue(newElement, propDefn, value);
				}
				break;
			case IPropertyType.ELEMENT_TYPE:
				break;
			case IPropertyType.CONTENT_ELEMENT_TYPE:
				handleContentElementValue(newElement, propDefn, value);
				break;
			default:
				if (newElement.getLocalProperty(null, propDefn) == null) {
					if (propDefn.isEncryptable()) {
						String encryption = element.getEncryptionID(propDefn);
						newElement.setEncryptionHelper(propDefn, encryption);
						value = EncryptionUtil.encrypt(propDefn, encryption, value);
						newElement.setProperty(propDefn, value);
					} else {
						newElement.setProperty(propDefn, value);
					}
				}
			}
		}
	}

	/**
	 * Gets the localizable property value for the given property definition.
	 * Generally, return value of <code>getPropertyFromSelf</code>.
	 *
	 * @param module
	 * @param element
	 * @param propDefn
	 * @return
	 */

	protected Object getLocalizablePropertyValue(Module module, DesignElement element, ElementPropertyDefn propDefn) {
		return element.getStrategy().getPropertyFromElement(module, element, propDefn);
	}

	/**
	 *
	 * @param newCube
	 * @param cube
	 */
	private void handleDimensionConditions(Cube newCube, Cube cube) {
		if (newCube.getLocalProperty(targetDesign, ITabularCubeModel.DIMENSION_CONDITIONS_PROP) != null) {
			return;
		}
		if (cubes.get(newCube) == null) {
			cubes.put(newCube, cube);
		}
	}

	/**
	 *
	 */
	private void localizeDimensionConditions() {
		if (cubes.isEmpty()) {
			return;
		}
		Iterator<Cube> iter = cubes.keySet().iterator();
		while (iter.hasNext()) {
			Cube newCube = iter.next();
			Cube srcCube = cubes.get(newCube);
			ElementPropertyDefn propDefn = srcCube.getPropertyDefn(ITabularCubeModel.DIMENSION_CONDITIONS_PROP);
			List<Object> dimensionConditionList = (List<Object>) srcCube.getProperty(sourceDesign, propDefn);
			StructureContext dimensionConditionsContext = new StructureContext(newCube, propDefn, null);

			// one by one handle the dimension condition
			if (dimensionConditionList != null) {
				for (int i = 0; i < dimensionConditionList.size(); i++) {
					DimensionCondition dimensionCond = (DimensionCondition) dimensionConditionList.get(i);
					DimensionCondition newDimensionCond = (DimensionCondition) dimensionCond.copy();
					dimensionConditionsContext.add(newDimensionCond);

					// handle hierarchy reference
					ElementRefValue hierarchyRef = (ElementRefValue) dimensionCond.getLocalProperty(sourceDesign,
							DimensionCondition.HIERARCHY_MEMBER);
					// if hierarchy is not resolved, do nothing
					if (hierarchyRef == null || !hierarchyRef.isResolved()) {
						continue;
					}

					DesignElement hierarchy = hierarchyRef.getElement();
					assert hierarchy != null;
					int hierarchyIndex = hierarchy.getIndex(sourceDesign);
					DesignElement dimension = hierarchy.getContainer();
					assert dimension != null;
					int dimensionIndex = dimension.getIndex(sourceDesign);
					// according to the hierarchy index, set the referred
					// hierarchy in the new dimension condition
					DesignElement newDimension = getContent(targetDesign, newCube, CubeHandle.DIMENSIONS_PROP,
							dimensionIndex);
					assert newDimension != null;
					DesignElement newHierarchy = getContent(targetDesign, newDimension,
							DimensionHandle.HIERARCHIES_PROP, hierarchyIndex);
					assert newHierarchy != null;
					newDimensionCond.setProperty(DimensionCondition.HIERARCHY_MEMBER,
							new ElementRefValue(null, newHierarchy));

					// handle all the join conditions
					List<Object> joinConditionList = (List) dimensionCond.getProperty(sourceDesign,
							DimensionCondition.JOIN_CONDITIONS_MEMBER);
					if (joinConditionList == null || joinConditionList.isEmpty()) {
						continue;
					}
					List<Object> newJoinConditionList = (List) newDimensionCond.getProperty(targetDesign,
							DimensionCondition.JOIN_CONDITIONS_MEMBER);
					for (int j = 0; j < joinConditionList.size(); j++) {
						DimensionJoinCondition joinCond = (DimensionJoinCondition) joinConditionList.get(j);
						DimensionJoinCondition newJoinCond = (DimensionJoinCondition) newJoinConditionList.get(j);
						ElementRefValue levelRef = (ElementRefValue) joinCond.getLocalProperty(sourceDesign,
								DimensionJoinCondition.LEVEL_MEMBER);
						if (levelRef == null || !levelRef.isResolved()) {
							continue;
						}
						DesignElement level = levelRef.getElement();
						assert level != null;
						int levelIndex = level.getIndex(sourceDesign);

						// if level is not in the hierarchy that referred by
						// dimension condition, then this dimension condition is
						// invalid, no need to do validation
						if (level.getContainer() != hierarchy) {
							continue;
						}

						// according to the level index and set the referred
						// level
						DesignElement newLevel = getContent(targetDesign, newHierarchy, HierarchyHandle.LEVELS_PROP,
								levelIndex);
						assert newLevel != null;
						newJoinCond.setProperty(DimensionJoinCondition.LEVEL_MEMBER,
								new ElementRefValue(null, newLevel));
					}
				}
			}
		}
	}

	/**
	 *
	 * @param newCube
	 * @param cube
	 */
	private void handleDefaultHierarchy(Dimension newDimension, Dimension dimension) {
		if (newDimension.getLocalProperty(targetDesign, IDimensionModel.DEFAULT_HIERARCHY_PROP) != null) {
			return;
		}
		if (dimensions.get(newDimension) == null) {
			dimensions.put(newDimension, dimension);
		}
	}

	/**
	 *
	 */
	private void localizeDefaultHierarchy() {
		if (dimensions.isEmpty()) {
			return;
		}
		Iterator<Dimension> iter = dimensions.keySet().iterator();
		while (iter.hasNext()) {
			Dimension newDimension = iter.next();
			Dimension srcDimension = dimensions.get(newDimension);

			// handle default hierarchy by the index
			ModelUtil.duplicateDefaultHierarchy(newDimension, srcDimension);
		}
	}

	private DesignElement getContent(Module module, DesignElement element, String propName, int index) {
		Object value = element.getProperty(module, propName);
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			List valueList = (List) value;
			if (index >= 0 && index < valueList.size()) {
				return (DesignElement) valueList.get(index);
			}
		} else if (value instanceof DesignElement && index == 0) {
			return (DesignElement) value;
		}

		return null;

	}

	/**
	 * Localize values if the property type is content element or content element
	 * list.
	 *
	 * @param newElement the target element
	 * @param propDefn   the property definition
	 * @param valueList  the original property value
	 */

	private void handleContentElementValue(DesignElement newElement, PropertyDefn propDefn, Object value) {
		elements.push(newElement);
		if (propDefn.isListType()) {
			List tmplist = (List) value;
			for (int i = 0; i < tmplist.size(); i++) {
				DesignElement tmpElement = (DesignElement) tmplist.get(i);
				tmpElement.apply(this);
			}

			elements.pop();
			return;
		}

		((DesignElement) value).apply(this);
		elements.pop();

	}

	/**
	 * Localize values if the property type is structure or structure list.
	 *
	 * @param newElement the target element
	 * @param propDefn   the property definition
	 * @param valueList  the original property value
	 */

	private void handleStructureValue(DesignElement newElement, PropertyDefn propDefn, Object valueList) {
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;
		if (propDefn.isList() && IModuleModel.IMAGES_PROP.equalsIgnoreCase(propDefn.getName())) {
			StructureContext context = new StructureContext(newElement, (ElementPropertyDefn) propDefn, null);

			localizeEmbeddedImage((List<Object>) valueList, context);
		} else {
			newElement.setProperty(propDefn, createNewStructureValue(propDefn, valueList));
		}
	}

	/**
	 * Returns the copy of <code>value</code>. Structure, structure list, element
	 * reference values, etc. are dumped as a new copy.
	 *
	 * @param propDefn the property/member definition
	 * @param value    the source value
	 * @return the copy of <code>value</code>
	 *
	 */

	private Object createNewStructureValue(PropertyDefn propDefn, Object value) {
		Object newValue = null;

		if (propDefn.isList()) {
			List sourceValue = (List) value;

			newValue = new ArrayList();

			for (int i = 0; i < sourceValue.size(); i++) {
				Structure newStruct = doCreateNewStructureValue((Structure) sourceValue.get(i));
				((List) newValue).add(newStruct);
			}
		} else {
			newValue = doCreateNewStructureValue((Structure) value);
		}

		return newValue;
	}

	/**
	 * Returns the copy of <code>value</code>. Structure, structure list, element
	 * reference values, etc. are dumped as a new copy.
	 *
	 * @param propDefn the property/member definition
	 * @param value    the source value
	 * @return the copy of <code>value</code>
	 *
	 */

	private Structure doCreateNewStructureValue(Structure struct) {
		Structure newStruct = (Structure) struct.copy();

		Iterator<IPropertyDefn> iter = struct.getObjectDefn().propertiesIterator();
		while (iter.hasNext()) {
			StructPropertyDefn memberDefn = (StructPropertyDefn) iter.next();
			Object value = struct.getLocalProperty(sourceDesign, memberDefn);

			if (value == null) {
				continue;
			}

			switch (memberDefn.getTypeCode()) {
			case IPropertyType.ELEMENT_REF_TYPE:
				handleElementRefValue(newStruct, memberDefn, (ElementRefValue) value);
				break;
			case IPropertyType.STRUCT_TYPE:
				newStruct.setProperty(memberDefn, createNewStructureValue(memberDefn, value));
				break;
			default:
				newStruct.setProperty(memberDefn, ModelUtil.copyValue(memberDefn, value));
			}
		}

		return newStruct;
	}

	/**
	 * Localize values if the property type is element reference value.
	 *
	 * @param newElement the target element
	 * @param propDefn   the property definition
	 * @param value      the original property value
	 */

	private void handleElementRefValue(Structure structure, PropertyDefn propDefn, ElementRefValue value) {
		DesignElement refElement = value.getElement();

		// handle only when the data set is not local but
		// library resource

		if (needExportReferredElement(refElement)) {
			DesignElement newRefEelement = getCache(refElement);
			if (newRefEelement == null) {
				newRefEelement = visitExternalElement(refElement);
			}

			assert newRefEelement != null;

			structure.setProperty(propDefn, new ElementRefValue(null, newRefEelement));
		} else {
			structure.setProperty(propDefn, new ElementRefValue(value.getLibraryNamespace(), value.getName()));
		}
	}

	/**
	 * Localize values if the property type is element reference list.
	 *
	 * @param newElement the target element
	 * @param propDefn   the property definition
	 * @param valueList  the original property value
	 */

	private void handleElementRefValueList(DesignElement newElement, PropertyDefn propDefn,
			List<ElementRefValue> valueList) {
		List<ElementRefValue> values = new ArrayList<>();
		for (int i = 0; i < valueList.size(); i++) {
			// try to resolve every

			ElementRefValue item = valueList.get(i);
			DesignElement refElement = item.getElement();
			if (needExportReferredElement(refElement)) {
				DesignElement newRefEelement = getCache(refElement);
				if (newRefEelement == null) {
					newRefEelement = visitExternalElement(refElement);
				}
				values.add(new ElementRefValue(null, newRefEelement));
			} else {
				values.add(new ElementRefValue(null, item.getName()));
			}
		}

		newElement.setProperty(propDefn, values);

	}

	/**
	 * Localize values if the property type is structure reference value.
	 *
	 * @param newElement the target element
	 * @param propDefn   the property definition
	 * @param value      the original property value
	 */

	private void handleStructureRefValue(DesignElement newElement, PropertyDefn propDefn, StructRefValue value) {
		if (!isLocalImage((value).getQualifiedReference())) {
			EmbeddedImage targetEmbeddedImage = (EmbeddedImage) value.getTargetStructure();

			EmbeddedImage newEmbeddedIamge = localizeExternalEmbeddedImage(targetEmbeddedImage);

			newElement.setProperty(propDefn, new StructRefValue(null, newEmbeddedIamge));
		} else {
			newElement.setProperty(propDefn, ModelUtil.copyValue(propDefn, value));
		}
	}

	/**
	 * Localize values if the property type is element reference value.
	 *
	 * @param newElement the target element
	 * @param propDefn   the property definition
	 * @param value      the original property value
	 */

	private void handleElementRefValue(DesignElement newElement, DesignElement sourceElement, PropertyDefn propDefn,
			ElementRefValue value) {
		DesignElement refElement = value.getElement();

		// handle only when the data set is not local but
		// library resource

		if (needExportReferredElement(refElement)) {
			DesignElement newRefElement = getCache(refElement);
			if (newRefElement == null) {
				// if the element is a referencable styled element such as:
				// Chart -> hostChart.

				if (refElement instanceof ReferencableStyledElement) {
					newRefElement = visitExternalReferencableStyledElement((ReferencableStyledElement) refElement,
							sourceElement);
				} else {
					newRefElement = visitExternalElement(refElement);
				}
			}

			// if it is theme, newRefElement can be null.

			if (newRefElement != null) {
				newElement.setProperty(propDefn, new ElementRefValue(null, newRefElement));
			} else {
				newElement.setProperty(propDefn, new ElementRefValue(null, refElement.getName()));
			}
		} else {
			setElementRefProperty(newElement, propDefn, value);
		}
	}

	protected void setElementRefProperty(DesignElement newElement, PropertyDefn propDefn, ElementRefValue value) {
		boolean isDynamicLinkerChildren = false;
		DesignElement e = value.getElement();
		while (e != null) {
			if (e instanceof Cube) {
				Cube containerCube = (Cube) e;
				if (containerCube.getDynamicExtendsElement(sourceDesign) != null) {
					isDynamicLinkerChildren = true;
					break;
				}
			}
			e = e.getContainer();
		}

		if (!isDynamicLinkerChildren) {
			newElement.setProperty(propDefn, new ElementRefValue(value.getLibraryNamespace(), value.getName()));
		} else {
			newElement.setProperty(propDefn, new ElementRefValue(null, value.getName()));
		}
	}

	/**
	 * Justifies the referred element to be exported to the target or not. If it
	 * comes from the other module rather than the source design and the module is
	 * library, then it should be exported to target design. otherwise false.
	 *
	 * @param element
	 * @return
	 */
	private boolean needExportReferredElement(DesignElement element) {
		if (element != null) {
			Module root = element.getRoot();

			// now, we only support export external element from library and it
			// is not virtual element; there are two kinds of virtual element:
			// first, content in the children element by "extends" property;
			// second, content in the children element by dynamic linker, such
			// as "internalRef" in Dimension and so on; whatever kind of virtual
			// element, we should not add it to externalelement map, for it can
			// not be generated without the root parent.
			if (root != sourceDesign && root instanceof Library && !isDynamicLinkerVirtualElement(root, element)) {
				return true;
			}
		}

		return false;
	}

	private boolean isDynamicLinkerVirtualElement(Module module, DesignElement element) {
		if (element.isVirtualElement()) {
			DesignElement focus = element;
			while (focus != null) {
				if (focus.getDynamicExtendsElement(module) != null) {
					return true;
				}
				focus = focus.getContainer();
			}
		}
		return false;
	}

	/**
	 * Localizes embedded images in sourceEmbeddedImage to the new list of
	 * targetEmbeddedImage.
	 *
	 * @param sourceEmbeddedImage the source images
	 * @param targetContext       the target context to add the embedded image
	 */

	private void localizeEmbeddedImage(List<Object> sourceEmbeddedImage, StructureContext targetContext) {

		List targetEmeddedImage = targetContext.getList(targetDesign);
		if (targetEmeddedImage == null) {
			targetEmeddedImage = Collections.emptyList();
		}
		for (int i = 0; i < sourceEmbeddedImage.size(); i++) {
			EmbeddedImage sourceImage = (EmbeddedImage) sourceEmbeddedImage.get(i);

			if (!targetEmeddedImage.contains(sourceImage)) {
				EmbeddedImage newEmeddedImage = (EmbeddedImage) sourceImage.copy();

				localizeEmbeddedImageValues(sourceImage, newEmeddedImage);

				targetContext.add(newEmeddedImage);
			}
		}
	}

	/**
	 * Localizs member values of embedded images in sourceEmbeddedImage to the
	 * targetEmbeddedImage.
	 *
	 * @param sourceEmbeddedImage the source images
	 * @param targetEmeddedImage  the target images
	 */

	private void localizeEmbeddedImageValues(EmbeddedImage sourceEmbeddedImage, EmbeddedImage targetEmeddedImage) {
		EmbeddedImage tmpEmeddedImage = sourceEmbeddedImage;
		while (tmpEmeddedImage != null
				&& (targetEmeddedImage.getData(null) == null || targetEmeddedImage.getType(null) == null)) {
			targetEmeddedImage.setData(tmpEmeddedImage.getData(sourceDesign));
			targetEmeddedImage.setType(tmpEmeddedImage.getType(sourceDesign));

			StructRefValue refValue = (StructRefValue) tmpEmeddedImage.getProperty(sourceDesign,
					ReferencableStructure.LIB_REFERENCE_MEMBER);
			if (refValue == null) {
				break;
			}

			tmpEmeddedImage = (EmbeddedImage) refValue.getTargetStructure();
		}

		targetEmeddedImage.setProperty(ReferencableStructure.LIB_REFERENCE_MEMBER, null);

	}

	/**
	 * Localizes an embedded image to the cached map when the sourceEmbeddedImage is
	 * not directly in the source design.
	 *
	 * @param sourceEmbeddedImage the source embedded image
	 * @return the new embedded image
	 */

	private EmbeddedImage localizeExternalEmbeddedImage(EmbeddedImage sourceEmbeddedImage) {
		EmbeddedImage newEmeddedImage = (EmbeddedImage) getCache(sourceEmbeddedImage);

		if (newEmeddedImage != null) {
			return newEmeddedImage;
		}

		newEmeddedImage = (EmbeddedImage) visitExternalStruct(sourceEmbeddedImage);
		localizeEmbeddedImageValues(sourceEmbeddedImage, newEmeddedImage);

		return newEmeddedImage;
	}

	private void cacheMapping(DesignElement sourceElement, DesignElement targetElement) {
		externalElements.put(sourceElement, targetElement);
		targetDesign.cacheFlattenElement(sourceElement, targetElement);
	}

	private void cacheMapping(IStructure sourceStruct, IStructure targetStruct) {
		externalStructs.put(sourceStruct, targetStruct);
	}

	private IStructure getCache(IStructure sourceStruct) {
		return externalStructs.get(sourceStruct);
	}

	private DesignElement getCache(DesignElement sourceElement) {
		return externalElements.get(sourceElement);
	}

	/**
	 * Determines whether the given name embedded image is a local one.
	 *
	 * @param imageName
	 * @param sourceDesign
	 * @return true if the embedded image is a local one, otherwise false
	 */

	private boolean isLocalImage(String imageName) {
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance()
				.getStructure(EmbeddedImage.EMBEDDED_IMAGE_STRUCT);

		if (StructureRefUtil.findNativeStructure(targetDesign, defn, imageName) != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the container for the target element.
	 *
	 * @param sourceElement the source element
	 * @param target        the target element
	 * @return the container of <code>target</code>
	 */

	private DesignElement getTargetContainer(DesignElement sourceElement, DesignElement target,
			List<DesignElement> processedElement) {
		DesignElement sourceContainer = sourceElement.getContainer();
		long containerId = sourceContainer.getID();

		DesignElement tmpContainer;

		tmpContainer = externalElements.get(sourceContainer);
		if (tmpContainer == null) {
			tmpContainer = targetDesign.getElementByID(containerId);
		}

		if (tmpContainer == null) {
			return null;
		}

		if (sourceContainer.getElementName().equalsIgnoreCase(tmpContainer.getElementName()) || (sourceContainer instanceof Module && tmpContainer == targetDesign)) {
			return tmpContainer;
		}

		if (sourceContainer instanceof Theme) {
			return targetDesign;
		}

		return tmpContainer;
	}

	/**
	 * Visits the member value.
	 *
	 * @param obj the member value to traverse
	 */

	@Override
	public void visitMemberValue(MemberValue obj) {
		visitDesignElement(obj);
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public DesignElementHandle newElement(String elementTypeName, String name) {

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getExtension(elementTypeName);

		// try extension first
		if (elemDefn != null) {
			return newExtensionElement(elementTypeName, name);
		}

		// try other system definitions
		elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getElement(elementTypeName);
		if (elemDefn != null) {
			DesignElement element = ElementFactoryUtil.newElementExceptExtendedItem(targetDesign, elementTypeName,
					name);
			if (element == null) {
				return null;
			}
			return element.getHandle(targetDesign);
		}
		return null;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 * @param targetContext   the contain context where the created element will be
	 *                        inserted
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public DesignElementHandle newElement(String elementTypeName, String name, ContainerContext targetContext) {

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getExtension(elementTypeName);

		// try extension first
		if (elemDefn != null) {
			return newExtensionElement(elementTypeName, name);
		}

		// try other system definitions
		elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getElement(elementTypeName);
		if (elemDefn != null) {
			DesignElement element = newElement(targetDesign, targetContext, elementTypeName, name);
			if (element == null) {
				return null;
			}
			return element.getHandle(targetDesign);
		}
		return null;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param module            the module to create an element
	 * @param targetContainment the container context where the created element will
	 *                          be inserted
	 * @param elementTypeName   the element type name
	 * @param name              the optional element name
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public static DesignElement newElement(Module module, ContainerContext targetContainment, String elementTypeName,
			String name) {

		DesignElement element = ElementFactoryUtil.newElement(elementTypeName, name);
		if (targetContainment.isManagedByNameSpace()) {
			module.rename(targetContainment.getElement(), element);
		}
		return element;
	}

	/**
	 * Creates an extension element specified by the extension type name.
	 *
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 *
	 * @return design element, <code>null</code> returned if the extension with the
	 *         given type name is not found
	 */

	protected DesignElementHandle newExtensionElement(String elementTypeName, String name) {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(elementTypeName);
		if (extDefn == null) {
			return null;
		}
		String extensionPoint = extDefn.getExtensionPoint();
		if (PeerExtensionLoader.EXTENSION_POINT.equalsIgnoreCase(extensionPoint)) {
			return newExtendedItem(name, elementTypeName);
		}

		return null;
	}

	/**
	 * Creates a new extended item.
	 *
	 * @param name          the optional extended item name. Can be
	 *                      <code>null</code>.
	 * @param extensionName the required extension name
	 * @return a handle to extended item, return <code>null</code> if the definition
	 *         with the given extension name is not found
	 */

	public ExtendedItemHandle newExtendedItem(String name, String extensionName) {
		try {
			return newExtendedItem(name, extensionName, null);
		} catch (ExtendsException e) {
			assert false;
			return null;
		}
	}

	/**
	 * Creates a new extended item which extends from a given parent.
	 *
	 * @param name          the optional extended item name. Can be
	 *                      <code>null</code>.
	 * @param extensionName the required extension name
	 * @param parent        a given parent element.
	 * @return a handle to extended item, return <code>null</code> if the definition
	 *         with the given extension name is not found
	 * @throws ExtendsException
	 */

	private ExtendedItemHandle newExtendedItem(String name, String extensionName, ExtendedItemHandle parent)
			throws ExtendsException {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(extensionName);
		if (extDefn == null) {
			return null;
		}

		if (parent != null) {
			assert ((ExtendedItem) parent.getElement()).getExtDefn() == extDefn;
		}

		if (!(extDefn instanceof PeerExtensionElementDefn)) {
			throw new IllegalOperationException("Only report item extension can be created through this method."); //$NON-NLS-1$
		}

		ExtendedItem element = new ExtendedItem(name);

		// init provider.

		element.setProperty(IExtendedItemModel.EXTENSION_NAME_PROP, extensionName);

		if (parent != null) {
			element.getHandle(targetDesign).setExtends(parent);
		}

		targetDesign.makeUniqueName(element);
		ExtendedItemHandle handle = element.handle(targetDesign);
		try {
			handle.loadExtendedElement();
		} catch (ExtendedElementException e) {
			// It's impossible to fail when deserializing.

			assert false;
		}
		return handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.ElementVisitor#visitContentElement
	 * (org.eclipse.birt.report.model.elements.ContentElement)
	 */

	@Override
	public void visitContentElement(ContentElement obj) {
		visitDesignElement(obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitorImpl#
	 * visitReportItemTheme (org.eclipse.birt.report.model.elements.ReportItemTheme)
	 */
	@Override
	public void visitReportItemTheme(ReportItemTheme obj) {
		visitDesignElement(obj);
		ReportItemTheme newTheme = (ReportItemTheme) currentNewElement;
		reportItemThemes.put(obj.getHandle(obj.getRoot()).getQualifiedName(), newTheme);

	}

}
