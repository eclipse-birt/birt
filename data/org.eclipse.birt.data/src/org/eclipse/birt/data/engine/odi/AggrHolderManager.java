/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.odi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.stream.DummyOutputStream;

/**
 *
 */

public class AggrHolderManager {

	private List<IAggrValueHolder> holders;
	private HashMap<String, IAggrValueHolder> aggrIndex;

	public AggrHolderManager() {
		this.holders = new ArrayList<>();
		this.aggrIndex = new HashMap<>();
	}

	public void addAggrValueHolder(IAggrValueHolder newHolder) throws DataException {
		this.holders.add(newHolder);
		Set<String> aggrNames = newHolder.getAggrNames();
		for (String aggrName : aggrNames) {
			this.aggrIndex.put(aggrName, newHolder);
		}
	}

	public void clear() throws DataException {
		this.holders.clear();
		this.aggrIndex.clear();
	}

	public Object getAggrValue(String aggrName) throws DataException {
		IAggrValueHolder holder = this.aggrIndex.get(aggrName);
		if (holder != null) {
			return holder.getAggrValue(aggrName);
		}

		throw new DataException(ResourceConstants.INVALID_BOUND_COLUMN_NAME, aggrName);
	}

	public void doSave(OutputStream aggrIndexStream, OutputStream aggrStream) throws DataException {
		try {
			DataOutputStream aggrIndexDos = new DataOutputStream(aggrIndexStream);
			DataOutputStream aggrDos = new DataOutputStream(aggrStream);

			// Firstly write the aggr size to index stream
			IOUtil.writeInt(aggrIndexStream, this.aggrIndex.size());
			for (IAggrValueHolder holder : this.holders) {
				for (String arrgBindingName : holder.getAggrNames()) {
					// Then write aggrBindingName to aggr stream
					IOUtil.writeString(aggrDos, arrgBindingName);

					// Then write aggr name to aggr stream
					IOUtil.writeString(aggrDos, holder.getAggrInfo(arrgBindingName).getAggregation().getName());
					// Then write the aggr group level to aggr stream
					IOUtil.writeInt(aggrDos, holder.getAggrInfo(arrgBindingName).getGroupLevel());
					// Then write the aggr value to aggr stream
					List values = holder.getAggrValues(arrgBindingName);
					assert values.size() > 0;
					IOUtil.writeInt(aggrStream, values.size());
					for (int i = 0; i < values.size(); i++) {
						IOUtil.writeObject(aggrDos, values.get(i));
					}

					// Finally write the offset to aggr index stream
					if (aggrStream instanceof DummyOutputStream) {
						IOUtil.writeLong(aggrIndexDos, ((DummyOutputStream) aggrStream).getOffset());
					} else {
						IOUtil.writeLong(aggrIndexDos, ((RAOutputStream) aggrStream).getOffset());
					}
				}
			}
			aggrIndexStream.close();
			aggrStream.close();
			aggrIndexDos.close();
			aggrDos.close();
		} catch (BirtException e) {
			throw DataException.wrap(e);
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public boolean isEmpty() {
		return this.aggrIndex.isEmpty();
	}

	public List<IAggrValueHolder> getAggrValueHolders() {
		return holders.subList(0, holders.size());
	}
}
