/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;

/**
 * This class serves as data communication tool between CachedResultSet and
 * IExpressionProcessor.
 */

class ComputedColumnsState implements IComputedColumnsState {
	//
	private ComputedColumnsStateSwitch ccsSwitch;

	/**
	 * 
	 * @param helper
	 */
	ComputedColumnsState(ComputedColumnHelper helper) {
		this.ccsSwitch = new ComputedColumnsStateSwitch(helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
	 * isValueAvailable(int)
	 */
	public boolean isValueAvailable(int index) {
		return this.ccsSwitch.getCurrentInstance().isValueAvailable(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getName
	 * (int)
	 */
	public String getName(int index) {
		return this.ccsSwitch.getCurrentInstance().getName(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
	 * getExpression(int)
	 */
	public IBaseExpression getExpression(int index) {
		return this.ccsSwitch.getCurrentInstance().getExpression(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
	 * setValueAvailable(int)
	 */
	public void setValueAvailable(int index) {
		this.ccsSwitch.getCurrentInstance().setValueAvailable(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
	 * getCount()
	 */
	public int getCount() {
		return this.ccsSwitch.getCurrentInstance().getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
	 * getComputedColumn(int)
	 */
	public IComputedColumn getComputedColumn(int index) {
		return this.ccsSwitch.getCurrentInstance().getComputedColumn(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
	 * setModel(int)
	 */
	public void setModel(int model) {
		this.ccsSwitch.setScopeState(model);
	}

	/**
	 * 
	 * @return
	 */
	public int getLastAccessedComputedColumnIndex() {
		return this.ccsSwitch.getCurrentInstance().getLastAccessedComputedColumnIndex();
	}

	/**
	 * 
	 * @param index
	 */
	public void setLastAccessedComputedColumnId(int index) {
		this.ccsSwitch.getCurrentInstance().setLastAccessedComputedColumnId(index);
	}

	/**
	 * 
	 *
	 */
	private static class ComputedColumnsStateSwitch {
		/**
		 * 
		 */
		private ComputedColumnsStateInstance dataSetCC;
		private ComputedColumnsStateInstance resultSetCC;
		private ComputedColumnsStateInstance allCC;
		private int scopeState;

		/**
		 * 
		 * @param helper
		 */
		ComputedColumnsStateSwitch(ComputedColumnHelper helper) {
			helper.setModel(TransformationConstants.DATA_SET_MODEL);
			this.dataSetCC = new ComputedColumnsStateInstance(helper.getComputedColumnList());

			helper.setModel(TransformationConstants.RESULT_SET_MODEL);
			this.resultSetCC = new ComputedColumnsStateInstance(helper.getComputedColumnList());

			helper.setModel(TransformationConstants.ALL_MODEL);
			this.allCC = new ComputedColumnsStateInstance(helper.getComputedColumnList());
		}

		/**
		 * 
		 * @param state
		 */
		void setScopeState(int state) {
			this.scopeState = state;
		}

		/**
		 * 
		 * @return
		 */
		ComputedColumnsStateInstance getCurrentInstance() {
			if (this.scopeState == TransformationConstants.DATA_SET_MODEL)
				return this.dataSetCC;
			else if (this.scopeState == TransformationConstants.RESULT_SET_MODEL)
				return this.resultSetCC;
			else if (this.scopeState == TransformationConstants.ALL_MODEL)
				return this.allCC;
			return null;
		}
	}

	/**
	 * 
	 *
	 */
	private static class ComputedColumnsStateInstance {
		//
		private List computedColumnsList;
		private boolean[] isValueAvailable;
		private int lastAccessedCCIndex;

		/**
		 * Constructor.
		 * 
		 * @param computedColumnList
		 */
		public ComputedColumnsStateInstance(List computedColumnList) {
			this.lastAccessedCCIndex = -1;
			this.computedColumnsList = new ArrayList();
			this.computedColumnsList.addAll(computedColumnList);
			this.isValueAvailable = new boolean[computedColumnList.size()];
			for (int i = 0; i < this.isValueAvailable.length; i++) {
				this.isValueAvailable[i] = false;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.executor.IComputedColumnState#isValueAvailable(
		 * int)
		 */
		public boolean isValueAvailable(int index) {
			return this.isValueAvailable[index];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.IComputedColumnState#getName(int)
		 */
		public String getName(int index) {
			return ((IComputedColumn) this.computedColumnsList.get(index)).getName();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.executor.IComputedColumnState#getExpression(int)
		 */
		public ScriptExpression getExpression(int index) {
			// TODO Auto-generated method stub
			return (ScriptExpression) ((IComputedColumn) this.computedColumnsList.get(index)).getExpression();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.executor.IComputedColumnState#setValueAvailable(
		 * int)
		 */
		public void setValueAvailable(int index) {
			this.isValueAvailable[index] = true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.IComputedColumnState#getCount()
		 */
		public int getCount() {
			return this.computedColumnsList.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.executor.IComputedColumnState#getComputedColumn(
		 * int)
		 */
		public IComputedColumn getComputedColumn(int index) {
			return (IComputedColumn) this.computedColumnsList.get(index);
		}

		/**
		 * 
		 * @return
		 */
		public int getLastAccessedComputedColumnIndex() {
			return this.lastAccessedCCIndex;
		}

		/**
		 * 
		 * @param index
		 */
		public void setLastAccessedComputedColumnId(int index) {
			this.lastAccessedCCIndex = index;
		}
	}
}
