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

package org.eclipse.birt.data.engine.impl.document.viewing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.DataSourceRuntime;
import org.eclipse.birt.data.engine.impl.IQueryContextVisitor;
import org.eclipse.birt.data.engine.impl.QueryExecutor;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.ULocale;

/**
 *
 */
public class NewInstanceHelper {

	/**
	 * No instance
	 */
	private NewInstanceHelper() {
	}

	/**
	 * @return
	 */
	public static IDataSource newDataSource() {
		return new IDataSource() {

			/*
			 * @see
			 * org.eclipse.birt.data.engine.odi.IDataSource#addProperty(java.lang.String,
			 * java.lang.String)
			 */
			@Override
			public void addProperty(String name, String value) throws DataException {
			}

			/*
			 * @see
			 * org.eclipse.birt.data.engine.odi.IDataSource#setAppContext(java.util.Map)
			 */
			@Override
			public void setAppContext(Map context) throws DataException {
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IDataSource#newQuery(java.lang.String,
			 * java.lang.String)
			 */
			@Override
			public IDataSourceQuery newQuery(String queryType, String queryText, boolean fromCache,
					IQueryContextVisitor qcv) throws DataException {
				return null;
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IDataSource#newCandidateQuery()
			 */
			@Override
			public ICandidateQuery newCandidateQuery(boolean fromCache) {
				return null;
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IDataSource#open()
			 */
			@Override
			public void open() throws DataException {
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IDataSource#canClose()
			 */
			@Override
			public boolean canClose() {
				return true;
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IDataSource#close()
			 */
			@Override
			public void close() {
			}
		};
	}

	/**
	 * @param dataEngine
	 * @return
	 */
	public static DataSourceRuntime newDataSourceRuntime(Scriptable queryScope) {
		return new DataSourceRuntime(newBaseDataSourceDesign(), queryScope, null) {

			/*
			 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
			 * getExtensionID()
			 */
			@Override
			public String getExtensionID() {
				return null;
			}

			/*
			 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
			 * getExtensionProperty(java.lang.String)
			 */
			@Override
			public String getExtensionProperty(String name) {
				return null;
			}

			/*
			 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
			 * setExtensionProperty(java.lang.String, java.lang.String)
			 */
			@Override
			public void setExtensionProperty(String name, String value) {

			}

			/*
			 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
			 * getAllExtensionProperties()
			 */
			@Override
			public Map getAllExtensionProperties() {
				return null;
			}
		};
	}

	/**
	 * @return
	 */
	private static IBaseDataSourceDesign newBaseDataSourceDesign() {
		return new IBaseDataSourceDesign() {

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getBeforeOpenScript() {
				return null;
			}

			@Override
			public String getAfterOpenScript() {
				return null;
			}

			@Override
			public String getBeforeCloseScript() {
				return null;
			}

			@Override
			public String getAfterCloseScript() {
				return null;
			}

			@Override
			public IBaseDataSourceEventHandler getEventHandler() {
				return null;
			}
		};
	}

	/**
	 * @return
	 */
	public static IBaseDataSetDesign newDataSetDesign() {
		return new BaseDataSetDesign();
	}

	/**
	 * @return
	 */
	public static IBaseDataSetDesign newIVDataSetDesign() {
		return new BaseDataSetDesign() {

			List computedColumns = new ArrayList();

			/*
			 * @see org.eclipse.birt.data.engine.impl.document.viewing.NewInstanceHelper.
			 * BaseDataSetDesign#getComputedColumns()
			 */
			@Override
			public List getComputedColumns() {
				return this.computedColumns;
			}
		};
	}

	/**
	 * @return
	 */
	public static IQuery newOdiQuery() {
		return new IQuery() {

			/*
			 * @see org.eclipse.birt.data.engine.odi.IQuery#setOrdering(java.util.List)
			 */
			@Override
			public void setOrdering(List sortSpecs) throws DataException {
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IQuery#setGrouping(java.util.List)
			 */
			@Override
			public void setGrouping(List groupSpecs) throws DataException {
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IQuery#setMaxRows(int)
			 */
			@Override
			public void setMaxRows(int maxRows) {
			}

			/*
			 * @see
			 * org.eclipse.birt.data.engine.odi.IQuery#addOnFetchEvent(org.eclipse.birt.data
			 * .engine.odi.IResultObjectEvent)
			 */
			@Override
			public void addOnFetchEvent(IResultObjectEvent event) {
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
			 */
			@Override
			public void close() {
			}

			/*
			 * @see
			 * org.eclipse.birt.data.engine.odi.IQuery#setExprProcessor(org.eclipse.birt.
			 * data.engine.executor.transformation.IExpressionProcessor)
			 */
			@Override
			public void setExprProcessor(IExpressionProcessor exprProcessor) {
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.IQuery#setDistinctValueFlag(boolean)
			 */
			@Override
			public void setDistinctValueFlag(boolean distinctValueFlag) {
			}

			@Override
			public void setRowFetchLimit(int limit) {
			}

			@Override
			public IBaseQueryDefinition getQueryDefinition() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setQueryDefinition(IBaseQueryDefinition query) {
				// TODO Auto-generated method stub

			}

		};
	}

	/**
	 * @return
	 */
	public static DataSetRuntime newDataSetRuntime(IBaseDataSetDesign dataSetDesign, QueryExecutor queryExecutor) {
		return new DataSetRuntime(dataSetDesign, queryExecutor, queryExecutor.getSession()) {
		};
	}

	/**
	 *
	 */
	private static class BaseDataSetDesign implements IBaseDataSetDesign {
		@Override
		public String getName() {
			return null;
		}

		/**
		 * @deprecated
		 */
		@Deprecated
		@Override
		public int getCacheRowCount() {
			return 0;
		}

		@Override
		public String getDataSourceName() {
			return null;
		}

		@Override
		public List getComputedColumns() {
			return null;
		}

		@Override
		public List getFilters() {
			return null;
		}

		@Override
		public List getParameters() {
			return null;
		}

		@Override
		public List getResultSetHints() {
			return null;
		}

		@Override
		public Collection getInputParamBindings() {
			return null;
		}

		@Override
		public String getBeforeOpenScript() {
			return null;
		}

		@Override
		public String getAfterOpenScript() {
			return null;
		}

		@Override
		public String getOnFetchScript() {
			return null;
		}

		@Override
		public String getBeforeCloseScript() {
			return null;
		}

		@Override
		public String getAfterCloseScript() {
			return null;
		}

		@Override
		public IBaseDataSetEventHandler getEventHandler() {
			return null;
		}

		@Override
		public boolean needDistinctValue() {
			return false;
		}

		@Override
		public int getRowFetchLimit() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setRowFetchLimit(int max) {
			// TODO Auto-generated method stub

		}

		@Override
		public ULocale getCompareLocale() {
			return null;
		}

		@Override
		public String getNullsOrdering() {
			return null;
		}

		@Override
		public List getSortHints() {
			return null;
		}
	}

	/**
	 * @return
	 */
	public static BaseQuery newBaseQuery() {
		return new BaseQuery() {

			/*
			 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
			 */
			@Override
			public void close() {
			}
		};
	}

}
