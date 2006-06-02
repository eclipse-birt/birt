
package org.eclipse.birt.report.model.api.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents the utility class to help export element and structure to library
 * file. The element or structure to export must comply with the following rule:
 * <ul>
 * <li>Element must have name defined.
 * <li>The name property of strucure must have value.
 * <li>The element or structure must be contained in design file.
 * </ul>
 * 
 * Any violation will throw <code>IllegalArgumentException</code>.
 */

public class ElementExportUtil
{

	/**
	 * Exports the given element into one library file. If the library file is
	 * not found, new library file will be created and saved as the given file
	 * name.
	 * 
	 * @param elementToExport
	 *            handle of the element to export.
	 * @param libraryFileName
	 *            file name of the target library
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws DesignFileException
	 *             if error encountered when open library file with the given
	 *             file name.
	 * @throws SemanticException
	 *             if error encountered when element name is duplicate in the
	 *             target library.
	 * @throws IOException
	 *             if error encountered when writing file.
	 * @throws IllegalArgumentException
	 *             if the element to export is not in design file, or the
	 *             element doesn't has name.
	 */

	public static void exportElement( DesignElementHandle elementToExport,
			String libraryFileName, boolean canOverride )
			throws DesignFileException, SemanticException, IOException
	{
		ElementExporter.checkElementToExport( elementToExport );

		DesignSession session = elementToExport.getModule( ).getSession( );
		LibraryHandle libraryHandle = openOrCreateLibrary( session,
				libraryFileName );
		assert libraryHandle != null;

		exportElement( elementToExport, libraryHandle, canOverride );

		libraryHandle.save( );
		libraryHandle.close( );
	}

	/**
	 * Exports the given element list into one library file. If the library file
	 * is not found, new library file will be created and saved as the given
	 * file name.
	 * 
	 * @param elementsToExport
	 *            list of the element to export, each of which is the instance
	 *            of <code>DesignElementHandle</code>.
	 * @param libraryFileName
	 *            file name of the target library
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws DesignFileException
	 *             if error encountered when open library file with the given
	 *             file name.
	 * @throws SemanticException
	 *             if error encountered when element name is duplicate in the
	 *             target library.
	 * @throws IOException
	 *             if error encountered when writing file.
	 * @throws IllegalArgumentException
	 *             if the element to export is not in design file.
	 */

	public static void exportElements( List elementsToExport,
			String libraryFileName, boolean canOverride )
			throws DesignFileException, SemanticException, IOException
	{
		DesignSession session = null;

		// Check the elements to export

		Iterator iter = elementsToExport.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElementHandle elementToExport = (DesignElementHandle) iter
					.next( );

			ElementExporter.checkElementToExport( elementToExport );
			if ( session == null )
				session = elementToExport.getModule( ).getSession( );
		}

		// That means the list of elements to export is empty.
		if ( session == null )
			return;

		LibraryHandle libraryHandle = openOrCreateLibrary( session,
				libraryFileName );
		assert libraryHandle != null;

		// Export all elements in the given list

		iter = elementsToExport.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElementHandle elementToExport = (DesignElementHandle) iter
					.next( );

			exportElement( elementToExport, libraryHandle, canOverride );
		}

		libraryHandle.save( );
		libraryHandle.close( );
	}

	/**
	 * Exports the given element into one library.
	 * 
	 * @param elementToExport
	 *            handle of the element to export.
	 * @param targetLibraryHandle
	 *            handle of target library
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws SemanticException
	 *             if error encountered when element name is duplicate in the
	 *             target library.
	 * @throws IllegalArgumentException
	 *             if the element to export is not in design file.
	 */

	public static void exportElement( DesignElementHandle elementToExport,
			LibraryHandle targetLibraryHandle, boolean canOverride )
			throws SemanticException
	{
		ElementExporter.checkElementToExport( elementToExport );
		ElementExporter exporter = new ElementExporter( targetLibraryHandle );
		exporter.exportElement( elementToExport, canOverride );
	}

	/**
	 * Exports the given structure into one library. The allowed structures are
	 * <code>EmbeddedImage</code>,<code>CustomColor</code> and
	 * <code>ConfigVariable</code>. If the library file is not found, new
	 * library file will be created and saved as the given file name.
	 * 
	 * @param structToExport
	 *            handle of the structure to export.
	 * @param libraryFileName
	 *            file name of the target library
	 * @param canOverride
	 *            indicates whether the structure with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws DesignFileException
	 *             if error encountered when open library file with the given
	 *             file name.
	 * @throws SemanticException
	 *             if error encountered when adding new structure to library or
	 *             value setting.
	 * @throws IOException
	 *             if error encountered when writing file.
	 * @throws IllegalArgumentException
	 *             if the element to export is not in design file.
	 */

	public static void exportStructure( StructureHandle structToExport,
			String libraryFileName, boolean canOverride )
			throws DesignFileException, SemanticException, IOException
	{
		ElementExporter.checkStructureToExport( structToExport );
		DesignSession session = structToExport.getElementHandle( ).getModule( )
				.getSession( );
		LibraryHandle libraryHandle = openOrCreateLibrary( session,
				libraryFileName );
		assert libraryHandle != null;

		exportStructure( structToExport, libraryHandle, canOverride );

		libraryHandle.save( );
		libraryHandle.close( );
	}

	/**
	 * Exports the given structure into one library. The allowed structures are
	 * <code>EmbeddedImage</code>,<code>CustomColor</code> and
	 * <code>ConfigVariable</code>. If the library file is not found, new
	 * library file will be created and saved as the given file name.
	 * 
	 * @param structsToExport
	 *            list of the structure to export, each of which is the instance
	 *            of <code>StructureHandle</code>.
	 * @param libraryFileName
	 *            file name of the target library
	 * @param canOverride
	 *            indicates whether the structure with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws DesignFileException
	 *             if error encountered when open library file with the given
	 *             file name.
	 * @throws SemanticException
	 *             if error encountered when adding new structure to library or
	 *             value setting.
	 * @throws IOException
	 *             if error encountered when writing file.
	 * @throws IllegalArgumentException
	 *             if the element to export is not in design file.
	 */

	public static void exportStructures( List structsToExport,
			String libraryFileName, boolean canOverride )
			throws DesignFileException, SemanticException, IOException
	{
		DesignSession session = null;

		// Check the structs to export

		Iterator iter = structsToExport.iterator( );
		while ( iter.hasNext( ) )
		{
			StructureHandle structToExport = (StructureHandle) iter.next( );

			ElementExporter.checkStructureToExport( structToExport );
			if ( session == null )
				session = structToExport.getElementHandle( ).getModule( )
						.getSession( );
		}

		// That means the list is empty.

		if ( session == null )
			return;

		LibraryHandle libraryHandle = openOrCreateLibrary( session,
				libraryFileName );
		assert libraryHandle != null;

		// Export all elements in the given list

		iter = structsToExport.iterator( );
		while ( iter.hasNext( ) )
		{
			StructureHandle structToExport = (StructureHandle) iter.next( );

			exportStructure( structToExport, libraryHandle, canOverride );
		}

		libraryHandle.save( );
		libraryHandle.close( );
	}

	/**
	 * Exports the given structure into one library. The allowed structures are
	 * <code>EmbeddedImage</code>,<code>CustomColor</code> and
	 * <code>ConfigVariable</code>.
	 * 
	 * @param structToExport
	 *            handle of the structure to export.
	 * @param targetLibraryHandle
	 *            handle of target library
	 * @param canOverride
	 *            indicates whether the structure with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws SemanticException
	 *             if error encountered when adding new structure to library or
	 *             value setting.
	 */

	public static void exportStructure( StructureHandle structToExport,
			LibraryHandle targetLibraryHandle, boolean canOverride )
			throws SemanticException
	{
		ElementExporter.checkStructureToExport( structToExport );

		ElementExporter exporter = new ElementExporter( targetLibraryHandle );

		exporter.exportStructure( structToExport, canOverride );
	}

	/**
	 * Export the report design to a library file specified by the file name. If
	 * the library file is not found, new library file will be created and saved
	 * as the given name.
	 * 
	 * @param designToExport
	 *            the design which need to be exported
	 * @param libraryFileName
	 *            target library file name
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * @param genDefaultName
	 *            if true, a default name will be generated if an element
	 *            doesn't has a name. if false, an exception will be throwed
	 *            indicate that the element to export must has a name
	 * @throws DesignFileException
	 *             if the library file has fatal errors which can not be opened.
	 * @throws SemanticException
	 *             if error encountered when element name is duplicated in the
	 *             target library.
	 * 
	 * @throws IOException
	 *             if error encountered when writing file.
	 */

	public static void exportDesign( ReportDesignHandle designToExport,
			String libraryFileName, boolean canOverride, boolean genDefaultName )
			throws DesignFileException, SemanticException, IOException
	{
		DesignSession session = designToExport.getModule( ).getSession( );
		LibraryHandle libraryHandle = openOrCreateLibrary( session,
				libraryFileName );
		assert libraryHandle != null;

		exportDesign( designToExport, libraryHandle, canOverride,
				genDefaultName );
		libraryHandle.save( );
		libraryHandle.close( );
	}

	/**
	 * Exports one design to library.
	 * 
	 * @param designToExport
	 *            handle of the report design to export
	 * @param targetLibraryHandle
	 *            handle of target library
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * @param genDefaultName
	 *            if true, a default name will be generated if an element
	 *            doesn't has a name. if false, an exception will be throwed
	 *            indicate that the element to export must has a name
	 * @throws SemanticException
	 *             if error encountered when element name is duplicate in the
	 *             target library.
	 */

	public static void exportDesign( ReportDesignHandle designToExport,
			LibraryHandle targetLibraryHandle, boolean canOverride,
			boolean genDefaultName ) throws SemanticException
	{

		if( ModelUtil.hasLibrary( designToExport, targetLibraryHandle ))
		{
			throw new SemanticException(
					designToExport.getElement( ),
					LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY );
		}
		ElementExporter exporter = new ElementExporter( targetLibraryHandle );
		exporter.exportDesign( designToExport, canOverride, genDefaultName );
	}

	/**
	 * Opens the library with given library file name. If the library is not
	 * found, create it.
	 * 
	 * @param session
	 *            the session
	 * @param libraryFileName
	 *            file name of the library to open
	 * @return the opened library handle
	 * @throws DesignFileException
	 *             if error encountered when open library file with the given
	 *             file name.
	 */

	private static LibraryHandle openOrCreateLibrary( DesignSession session,
			String libraryFileName ) throws DesignFileException
	{

		try
		{
			Library library = session.openLibrary( libraryFileName );
			return library.handle( );
		}
		catch ( DesignFileException e )
		{
			if ( DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR == e
					.getErrorCode( ) )
			{
				List errorList = e.getErrorList( );

				// FILE_NOT_FOUND error is always the first one.

				ErrorDetail error = ( (ErrorDetail) errorList.get( 0 ) );
				if ( DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND == error
						.getErrorCode( ) )
				{
					Library library = session.createLibrary( );
					LibraryHandle libraryHandle = library.handle( );
					libraryHandle.setFileName( libraryFileName );
					return libraryHandle;
				}
			}

			throw e;
		}
	}

}
