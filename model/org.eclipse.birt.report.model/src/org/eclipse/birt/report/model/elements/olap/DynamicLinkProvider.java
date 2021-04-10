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

package org.eclipse.birt.report.model.elements.olap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.namespace.DimensionNameHelper;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.extension.ExtensibilityProvider;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ElementFactoryUtil;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Provides dynamic link element provider. This provider implements some base
 * method for cube dimension and some special cube element that defines dynamic
 * link.
 */

abstract public class DynamicLinkProvider extends ExtensibilityProvider {

	private final static int DIMENSION_SPACE_ID = 0;

	private final static int NON_DIMENSION_SPACE_ID = 1;

	protected LayoutInfor infor = null;

	/**
	 * Constructs dynamic provider with the element.
	 * 
	 * @param element the element that holds this provider
	 */

	public DynamicLinkProvider(DesignElement element) {
		super(element);
		cachedExtDefn = null;
	}

	protected abstract DesignElement getTargetElement(Module module);

	protected abstract boolean isValidTarget(DesignElement target);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.ExtensibilityProvider#checkExtends
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */

	public final void checkExtends(DesignElement parent) throws ExtendsException {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IExtendableElement#getExtDefn()
	 */

	public final ExtensionElementDefn getExtDefn() {
		return cachedExtDefn;
	}

	/**
	 * 
	 * @param module
	 * @param prop
	 * @return
	 */
	public final Object getLayoutProperty(Module module, ElementPropertyDefn prop) {
		return infor == null ? null : infor.layoutProps.get(prop.getName());
	}

	public final void setLayoutProperty(ElementPropertyDefn prop, Object value) {
		if (infor == null)
			infor = new LayoutInfor();
		infor.layoutProps.put(prop.getName(), value);
	}

	public final void updateLayout(Module module) {
		// before clear the last information, we first unresolved all the
		// reference(both element reference and structure reference) related
		// with the elements in the layout structure
		clearReferences(module);

		// clear the late one
		infor = null;
		if (element instanceof Dimension) {
			((Dimension) element).nameHelper = new DimensionNameHelper((Dimension) element);
		}

		DesignElement target = getTargetElement(module);
		if (target != null && isValidTarget(target)) {
			infor = new LayoutInfor();

			duplicateStructure(target, element, module);
		}
	}

	private void clearReferences(Module module) {
		ContentIterator iter = new ContentIterator(module, element);
		while (iter.hasNext()) {
			DesignElement content = iter.next();
			if (!(content instanceof ReferenceableElement))
				continue;

			ReferenceableElement referred = (ReferenceableElement) content;
			if (!referred.hasReferences())
				continue;

			List<BackRef> clientList = referred.getClientList();
			for (BackRef clientRef : clientList) {
				Structure struct = clientRef.getStructure();
				String propName = clientRef.getPropertyName();
				if (struct != null) {
					ElementRefValue refValue = (ElementRefValue) struct.getLocalProperty(module, propName);
					refValue.unresolved(refValue.getName());
				} else {
					DesignElement client = clientRef.getElement();
					assert client != null;
					ElementRefValue refValue = (ElementRefValue) client.getLocalProperty(module, propName);
					refValue.unresolved(refValue.getName());
				}
			}
		}
	}

	protected boolean duplicateStructure(DesignElement source, DesignElement target, Module targetModule) {
		ElementDefn defn = (ElementDefn) source.getDefn();

		// copy top level properties
		List<IElementPropertyDefn> properties = defn.getContents();
		for (int i = 0; i < properties.size(); i++) {
			IElementPropertyDefn propDefn = properties.get(i);
			duplicateStructure(new ContainerContext(source, propDefn.getName()),
					new ContainerContext(target, propDefn.getName()), targetModule);
		}

		// do some special handle for cube and dimension

		if (target instanceof TabularCube) {
			TabularCube targetCube = (TabularCube) target;
			Cube sourceCube = (Cube) source;
			Module sourceRoot = sourceCube.getRoot();

			DesignElement group = source.getReferenceProperty(sourceRoot, ICubeModel.DEFAULT_MEASURE_GROUP_PROP);
			if (group != null) {
				int index = group.getIndex(sourceCube.getRoot());
				assert index > -1;
				targetCube.setDefaultMeasureGroup(index);
			}
		} else if (target instanceof Dimension) {
			Dimension targetDimension = (Dimension) target;
			Dimension sourceDimension = (Dimension) source;
			ModelUtil.duplicateDefaultHierarchy(targetDimension, sourceDimension);
		}

		return true;
	}

	private void duplicateStructure(ContainerContext sourceInfor, ContainerContext targetInfor, Module targetModule) {

		// clear the slot contents of the this element.
		targetInfor.clearContents();

		for (int j = 0; j < sourceInfor.getContentCount(null); j++) {
			DesignElement sourceContent = sourceInfor.getContent(null, j);

			// create an element of the same type

			DesignElement targetContent = ElementFactoryUtil.newElement(sourceContent.getElementName(),
					sourceContent.getName());

			if (targetContent != null) {
				// set up the element id and base id
				long id = sourceContent.getID();
				assert id > 0;
				targetContent.setID(sourceContent.getID());
				targetContent.setBaseId(sourceContent.getID());

				// setup the containment relationship
				targetInfor.add(targetModule, targetContent);
				addLocalNames(targetContent);

				// recusively duplicates the slots of the content
				duplicateStructure(sourceContent, targetContent, targetModule);

			}
		}
	}

	protected final void addLocalNames(DesignElement targetContent) {
		if (targetContent instanceof Dimension) {
			infor.localNameSpaces[DIMENSION_SPACE_ID].insert(targetContent);
		} else if (targetContent instanceof Level) {
			Dimension container = (Dimension) targetContent.getContainer().getContainer();
			assert container != null;
			container.getNameHelper().getNameSpace(Dimension.LEVEL_NAME_SPACE).insert(targetContent);
		} else {
			infor.localNameSpaces[NON_DIMENSION_SPACE_ID].insert(targetContent);
		}
	}

	public final DesignElement findLocalElement(String name, IElementDefn type) {
		if (StringUtil.isBlank(name) || type == null || infor == null)
			return null;

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		if (type.isKindOf(dd.getElement(ReportDesignConstants.DIMENSION_ELEMENT)))
			return infor.localNameSpaces[DIMENSION_SPACE_ID].getElement(name);

		DesignElement element = infor.localNameSpaces[NON_DIMENSION_SPACE_ID].getElement(name);
		if (element != null && element.getDefn().isKindOf(type))
			return element;
		return null;
	}

	public static class LayoutInfor {

		/**
		 * The map to store the layout property values. When the referred the tabular
		 * cube is resolved, this map will be set to store the values, such as
		 * 'dimensions' and 'measureGroups' and so on. Every time the referred cube is
		 * changed, this map will be updated.
		 */
		public Map<String, Object> layoutProps = null;

		public NameSpace[] localNameSpaces = null;

		public LayoutInfor() {
			this.layoutProps = new HashMap<String, Object>(ModelUtil.MAP_CAPACITY_LOW);
			this.localNameSpaces = new NameSpace[2];

			localNameSpaces[0] = new NameSpace();
			localNameSpaces[1] = new NameSpace();
		}
	}

}