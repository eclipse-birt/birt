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

package org.eclipse.birt.report.designer.ui.parameters.node;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.parameters.IParameterGroup;
import org.eclipse.birt.report.designer.ui.parameters.IParameter;
import org.eclipse.birt.report.designer.ui.parameters.ParameterFactory;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;

/**
 * Builds parameter node tree.
 * 
 */

public class ParameterNodeFactory {

	/**
	 * Builds parameter tree.
	 * 
	 * @param task
	 * @return parameter tree.
	 */

	public static IParameterNode buildParamTree(IGetParameterDefinitionTask task) {
		ParameterFactory factory = new ParameterFactory(task);
		List children = factory.getRootChildren();

		CompositeParameterNode rootNode = new CompositeParameterNode();

		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();

			if (obj instanceof IParameter) {
				IParameter param = (IParameter) obj;
				LeafParameterNode leaf = new LeafParameterNode(param);
				rootNode.add(leaf);

			} else if (obj instanceof IParameterGroup) {
				IParameterGroup parameterGroup = (IParameterGroup) obj;
				CompositeParameterNode node = new CompositeParameterNode();

				List childList = parameterGroup.getChildren();
				rootNode.add(node);

				buildParamGroup(node, childList);
			}

		}
		return rootNode;
	}

	/**
	 * Builds parameter group to tree.
	 * 
	 * @param node
	 * @param childList
	 */
	private static void buildParamGroup(CompositeParameterNode node, List childList) {
		assert node != null;
		assert childList != null;

		Iterator iterator = childList.iterator();
		while (iterator.hasNext()) {
			IParameter param = (IParameter) iterator.next();
			LeafParameterNode leaf = new LeafParameterNode(param);
			node.add(leaf);
		}
	}

}
