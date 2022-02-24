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

package org.eclipse.birt.report.designer.ui.preview.parameter.node;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.preview.parameter.IParamGroup;
import org.eclipse.birt.report.designer.ui.preview.parameter.IParameter;
import org.eclipse.birt.report.designer.ui.preview.parameter.ParameterFactory;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;

/**
 * Builds parameter node tree.
 * 
 */

public class ParamNodeFactory {

	/**
	 * Builds parameter tree.
	 * 
	 * @param task
	 * @return parameter tree.
	 */

	public static IParamNode buildParamTree(IGetParameterDefinitionTask task) {
		ParameterFactory factory = new ParameterFactory(task);
		List children = factory.getRootChildren();

		CompositeNode rootNode = new CompositeNode();

		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();

			if (obj instanceof IParameter) {
				IParameter param = (IParameter) obj;
				ParamLeaf leaf = new ParamLeaf(param);
				rootNode.add(leaf);

			} else if (obj instanceof IParamGroup) {
				IParamGroup paramGroup = (IParamGroup) obj;
				CompositeNode node = new CompositeNode();

				List childList = paramGroup.getChildren();
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
	private static void buildParamGroup(CompositeNode node, List childList) {
		assert node != null;
		assert childList != null;

		Iterator iterator = childList.iterator();
		while (iterator.hasNext()) {
			IParameter param = (IParameter) iterator.next();
			ParamLeaf leaf = new ParamLeaf(param);
			node.add(leaf);
		}
	}

}
