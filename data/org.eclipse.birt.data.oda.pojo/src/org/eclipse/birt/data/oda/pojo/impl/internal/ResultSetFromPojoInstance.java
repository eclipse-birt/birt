/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.impl.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.querymodel.ColumnReferenceNode;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.querymodel.ReferenceNode;
import org.eclipse.birt.data.oda.pojo.querymodel.RelayReferenceNode;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Its cursor is moved at the first line just when it's created
 */
public class ResultSetFromPojoInstance {
	private boolean nextCalled;
	private PojoQuery query;
	private Object root;
	private ClassLoader pojoClassLoader;

	// values of the references from root object
	private Map<ReferenceNode, Object> referenceValues = new HashMap<ReferenceNode, Object>();

	private Stack<NextableRelayReferenceNode> nextableRelayReferences = new Stack<NextableRelayReferenceNode>();

	/**
	 * @param root: a POJO instance
	 * @param query
	 */
	public ResultSetFromPojoInstance(Object root, PojoQuery query, ClassLoader pojoClassLoader) throws OdaException {
		assert root != null && query != null;
		this.root = root;
		this.query = query;
		this.nextCalled = false;
		this.pojoClassLoader = pojoClassLoader;
		fetchReferenceValues(); // prepare the first row
	}

	/**
	 * @param index: 1-based
	 * @return
	 * @throws OdaException
	 */
	public Object getColumnValue(int index) throws OdaException {
		ReferenceNode rn = query.getReferenceGraph().getColumnReferenceNode(index);
		return referenceValues.get(rn);
	}

	/**
	 * Moves the cursor down one row from its current position.
	 * 
	 * @return true, if next data row exists
	 * @throws OdaException if data source error occurs
	 */
	public boolean next() throws OdaException {
		if (!nextCalled) // next() is first called
		{
			nextCalled = true;
			return true;
		}
		NextableRelayReferenceNode relayNodeWithNextValue = null;
		while (!nextableRelayReferences.isEmpty()) {
			if (nextableRelayReferences.peek().getNextable().next()) {
				relayNodeWithNextValue = nextableRelayReferences.peek();
				break;
			} else {
				nextableRelayReferences.pop();
			}
		}
		if (relayNodeWithNextValue != null) {
			Object nextValue = relayNodeWithNextValue.getNextable().getValue();
			if (referenceValues.containsKey(relayNodeWithNextValue.getRelayReferenceNode())) {
				referenceValues.put(relayNodeWithNextValue.getRelayReferenceNode(), nextValue);
			}
			// refetch its descendants values
			fetchDescendantsValues(relayNodeWithNextValue.getRelayReferenceNode(), nextValue);
			return true;
		}
		return false;
	}

	private void fetchReferenceValues() throws OdaException {
		for (ReferenceNode rn : query.getReferenceGraph().getRoots()) {
			if (rn instanceof ColumnReferenceNode) {
				fetchColumnReferenceNodeValue((ColumnReferenceNode) rn, root);
			} else if (rn instanceof RelayReferenceNode) {
				Object value = fetchRelayReferenceNodeValue((RelayReferenceNode) rn, root);
				fetchDescendantsValues((RelayReferenceNode) rn, value);
			} else {
				// should never goes here
				assert false;
			}
		}
	}

	private void fetchColumnReferenceNodeValue(ColumnReferenceNode crn, Object from) throws OdaException {
		IMappingSource ms = crn.getReference();
		referenceValues.put(crn,
				ms.fetchValue(from, pojoClassLoader, query.getConnection().getClassMethodFieldBuffer()));
	}

	private Object fetchRelayReferenceNodeValue(RelayReferenceNode rrn, Object from) throws OdaException {
		IMappingSource ms = rrn.getReference();
		Object value = ms.fetchValue(from, pojoClassLoader, query.getConnection().getClassMethodFieldBuffer());
		if (Nextable.isNextable(value)) {
			Nextable n = Nextable.createNextable(value);
			if (n.next()) // n contains elements
			{
				pushNextableRelayReferenceNode(
						new NextableRelayReferenceNode(new InnermostNextable(n), (RelayReferenceNode) rrn));
				value = n.getValue(); // the value of the first element
			} else {
				// n is empty
				value = null;
			}
			referenceValues.put(rrn, value);
		} else {
			referenceValues.put(rrn, value);
		}
		return value;
	}

	private void fetchDescendantsValues(RelayReferenceNode rrn, Object from) throws OdaException {
		for (ReferenceNode rn : rrn.getChildren()) {
			if (rn instanceof ColumnReferenceNode) {
				fetchColumnReferenceNodeValue((ColumnReferenceNode) rn, from);
			} else if (rn instanceof RelayReferenceNode) {
				Object value = fetchRelayReferenceNodeValue((RelayReferenceNode) rn, from);
				fetchDescendantsValues((RelayReferenceNode) rn, value);
			} else {
				// should never goes here
				assert false;
			}
		}
	}

	private void pushNextableRelayReferenceNode(NextableRelayReferenceNode nrrn) throws OdaException {
		if (nextableRelayReferences.isEmpty()) {
			nextableRelayReferences.push(nrrn);
			return;
		}
		NextableRelayReferenceNode top = nextableRelayReferences.peek();
		if (top.getRelayReferenceNode().isAAncestorFor(nrrn.getRelayReferenceNode())) {
			nextableRelayReferences.push(nrrn);
		} else {
			// All 1-to-n maps should be in a transitional line
			throw new OdaException(Messages.getString("ResultSetFromPojoInstance.Invalid1ToNMaps")); //$NON-NLS-1$
		}
	}

	private static class NextableRelayReferenceNode {
		private InnermostNextable nextable;
		private RelayReferenceNode relayReferenceNode;

		public NextableRelayReferenceNode(InnermostNextable nextable, RelayReferenceNode relayReferenceNode) {
			assert nextable != null && relayReferenceNode != null;
			this.nextable = nextable;
			this.relayReferenceNode = relayReferenceNode;
		}

		/**
		 * @return the nextable
		 */
		public Nextable getNextable() {
			return nextable;
		}

		/**
		 * @return the relayReferenceNode
		 */
		public RelayReferenceNode getRelayReferenceNode() {
			return relayReferenceNode;
		}

	}
}
