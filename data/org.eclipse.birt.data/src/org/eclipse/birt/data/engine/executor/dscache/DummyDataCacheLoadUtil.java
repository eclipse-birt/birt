package org.eclipse.birt.data.engine.executor.dscache;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheObjectWithDummyData;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

public class DummyDataCacheLoadUtil implements ILoadUtil {
	private DataSetCacheObjectWithDummyData cacheObject;
	private ILoadUtil sourceLoadUtil;
	private int rowID;

	public DummyDataCacheLoadUtil(DataSetCacheObjectWithDummyData cacheObject, DataEngineSession session)
			throws DataException {
		this.cacheObject = cacheObject;
		this.sourceLoadUtil = CacheUtilFactory.createLoadUtil(cacheObject.getSourceDataSetCacheObject(), session);
		this.rowID = 0;
	}

	public IResultObject loadObject() throws DataException {
		IResultObject source = this.sourceLoadUtil.loadObject();
		if (source == null)
			return null;
		this.rowID++;
		IResultClass actualResultClass = this.cacheObject.getResultClass();
		Object[] actual = new Object[actualResultClass.getFieldCount()];
		for (int i = 1; i <= actual.length; i++) {
			String fieldName = actualResultClass.getFieldName(i);
			int index = source.getResultClass().getFieldIndex(fieldName);
			if (index != -1) {
				actual[i - 1] = source.getFieldValue(index);
			} else {
				actual[i - 1] = populateDataWithType(actualResultClass.getFieldValueClass(i),
						actualResultClass.getFieldLabel(i));
			}
		}
		return new ResultObject(actualResultClass, actual);
	}

	private Object populateDataWithType(Class dataType, String displayName) {
		if (dataType == Integer.class) {
			return 1234;
		} else if (dataType == Double.class) {
			return 1234.00;
		} else if (dataType == String.class) {
			return displayName == null ? "Lorem Ipsum" : displayName + "_" + rowID;
		} else if (dataType == BigDecimal.class) {
			return BigDecimal.valueOf(1234.56);
		} else if (dataType == java.sql.Date.class) {
			return new java.sql.Date(System.currentTimeMillis());
		} else if (dataType == Time.class) {
			return new java.sql.Time(System.currentTimeMillis());
		} else if (dataType == java.util.Date.class) {
			return new java.util.Date(System.currentTimeMillis());
		} else if (dataType == Timestamp.class) {
			return new java.sql.Timestamp(System.currentTimeMillis());
		} else if (dataType == IBlob.class) {
			return null;
		} else if (dataType == IClob.class) {
			return null;
		} else if (dataType == Boolean.class) {
			return false;
		} else if (dataType == Object.class) {
			return null;
		} else
			return null;
	}

	public IResultClass loadResultClass() throws DataException {
		return this.cacheObject.getResultClass();
	}

	public void close() throws DataException {
		this.sourceLoadUtil.close();
	}

}
