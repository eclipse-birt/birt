/*
 * Java(TM) OLAP Interface
 */

package javax.olap.cursor;

public interface CubeCursor extends javax.olap.cursor.RowDataAccessor, javax.olap.cursor.Cursor {

	public java.util.List getOrdinateEdge() throws javax.olap.OLAPException;

	public java.util.Collection getPageEdge() throws javax.olap.OLAPException;

	public void synchronizePages() throws javax.olap.OLAPException;

}
