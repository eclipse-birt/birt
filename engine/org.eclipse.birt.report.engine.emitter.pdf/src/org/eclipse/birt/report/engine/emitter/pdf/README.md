# Eclipse BIRT 
Introduction to use the specialized user properties of the PdfEmitter.

## Reason
The PdfEmitter provides a set of specialized user properties to optimize the PDF output according of the reporting based requirements.

Each of the user properties starts with the master prefix "PdfEmitter".


### User properties of the PdfEmitter

The following list get an overview of all supported user properties, the content and the definition of use.

**PdfEmitter.AppendDocumentList**

	Content    	PDF documents which shall be added at the end of the produced PDF document,
              	the value must include the full filename in kind of JavaScript or list like [PDF1,PDF2,PDF...,PDFn]
	Location   	report
	Data type  	string
	Values     	file name with full path or list of file names
	Default    	empty (null)
	Reference  	see PdfEmitter.PrependDocumentList
	Designer  	4.17

**PdfEmitter.BackGroundImageSvgToRaster**

	Content    	PDF documents use SVG images directly.
              	If it is necessary to convert the images before to raster images this option can be set.
              	Afterwards the SVG images will be transformed to PNG images. 
	Location   	report
	Data type  	boolean
	Values     	true, SVG images has to be converted to raster images
	           	false, SVG images used directly
	Default    	false
	Reference  	-
	Since	    	4.19
	Designer  	-

**PdfEmitter.Conformance**

	Content    	define the PDF conformance of the created PDF document, e.g. PDF.A1A
	Location   	report
	Data type  	string
	Values     	PDF.Standard, PDF.X1A2001, PDF.X32002, PDF.A1A, PDF.A1B, PDF.A2A, PDF.A2B, PDF.A3A, PDF.A3B, PDF.A3U, PDF.A4F
	Default    	PDF.Standard
	Reference  	see PdfEmitter.IccColorType, PdfEmitter.IccProfileFile, PdfEmitter.PDFA.AddDocumentTitle, PdfEmitter.PDFA.FallbackFont, PdfEmitter.IncludeCidSet
	Since      	4.16
	Designer  	4.17


**PdfEmitter.UAConformance**

	Content    	define the PDF/UA conformance of the created PDF document, e.g. PDF.UA-1
	Location   	report
	Data type  	string
	Values     	none, PDF.UA-1, PDF.UA-2
	Default    	none
	Since      	4.18
	Designer  	4.18

**PdfEmitter.IccColorType**

	Content    	define the ICC color type of the PDF/A document
	Location   	report
	Data type  	string
	Values     	RGB, ICC color profile type
             	CMYK, ICC color profile type
	Default    	RGB (sRGB IEC61966-2.1)
	Reference  	see PdfEmitter.Conformance, PdfEmitter.IccProfileFile, PdfEmitter.PDFA.AddDocumentTitle, PdfEmitter.PDFA.FallbackFont
	Since      	4.16
	Designer  	4.17

**PdfEmitter.IccProfileFile**

	Content    	define the ICC color file
	Location   	report
	Data type  	string
	Values     	file name with fully qualified path
	Default    	empty (null)
	Since      	4.16
	Designer  	4.17

**PdfEmitter.IncludeCidSet**

	Content    	include the CIDSet stream of a font into the document
	Location   	report
	Data type  	boolean
	Values     	true, CIDSet will be included
	           	false, CIDSet won't be included
	Default    	true
	Reference  	see PdfEmitter.Conformance, PdfEmitter.PDFA.FallbackFont
	Since      	4.17
	Designer  	4.17

**PdfEmitter.PDFA.FallbackFont**

	Content    	fall back font to create the PDF/A document correctly,
             	this font will be used if a report font isn't embeddable to the PDF document
	Location   	report
	Data type  	boolean
	Values     	file name with fully qualified path of an embeddable font
	Default    	empty (null)
	Since      	4.16
	Designer  	4.17

**PdfEmitter.PDFA.AddDocumentTitle**

	Content    	add the report title as PDF title,
             	with the activated title setting the PDF document is not full PDF/A conform due to a openPDF-topic
	Location   	report
	Data type  	boolean
	Values     	true, add the report title like PDF title
	          	false, avoid the PDF title
	Default    	false
	Since      	4.16
	Designer  	4.17

**PdfEmitter.PrependDocumentList**

	Content    	PDF documents which shall be added at the beginning of the produced PDF document,
              	the value must include the full filename in kind of JavaScript or list like [PDF1,PDF2,PDF...,PDFn]
	Location   	report
	Data type  	string
	Values     	file name with full path or list of file names
	Default    	empty (null)
	Reference  	see PdfEmitter.AppendDocumentList
	Designer  	4.17

**PdfEmitter.Version**

	Content    	define the PDF version of the created document
	Location   	report
	Data type  	string
	Values     	version number: 1.3 - 1.7
	Default    	1.5
	Since      	4.16
	Designer  	4.17

**PdfEmitter.VerticalTab**

	Content    	Go down to an Y position this far from the top before starting the content.
					If we are already down this far, this will not go further down.
					This can be specified in cells of table or grids, or in text/data items.
					The distance is measured from the top of the page body.
					For example, a value of "20cm", when the master page has 1cm margin-top
					and a page header of 15mm, results in the content starting at 225mm from
					the top of the sheet.
					This results in the content to be shown *at least* this far down from the top.
	Location   	cell or text/data.
	Data type  	string
	Values     	An absolute length, e.g. "20cm"
	Default    	empty (null)
	Since      	4.19


## PDF font configuration

The configuration of additional fonts for pdf-output can be configured at special font-configuration files.  
All font-configuration files are located into the folder **org.eclipse.birt.report.engine.fonts_<version-no.>**.  
The setup can be done through cascading configurations through the different configuration levels:  
- Level 01: fontsConfig.xml
- Level 02: fontsConfig_win32.xml or fontsConfig_linux.xml
- Level 03: fontsConfig_pdf.xml

A detailed description is given at the top level configuration file fontsConfig.xml.

**Kerning and Ligatures**

	Content    	The font configuration allows to enable advanced font handling
					to handle complex fonts and supports kerning and ligatures.
					To enable the special handling an according configuration tag
					"kerning-and-ligatures" can be used.
					The tag contains the attribute "enabled" which handle the enabling.
	Location   	fontsConfig.xml, fontsConfig_win32.xml, fontsConfig_linux.xml, fontsConfig_pdf.xml
	Data type  	boolean
	Values     	true, complex font handling with kerning and ligatures is enabled
					false, complex font handling is disabled
	Default    	false
	Since      	4.19

**Glyph Substitution**

	Content    	configuration of the font glyph substitution for PDF output
	Parameter  	-Dbirt.pdf.glyph.substitution.enabled
	Location   	JVM
	Data type  	boolean
	Values     	true, glyph tution enabled
					false, glyph substitution disabled
	Default    	false
	Reference  	see PdfEmitter.GlyphSubstitution
	Since      	4.20

**PdfEmitter.GlyphSubstitution**

	Content    	configuration of the font glyph substitution for PDF output
					The user property works only if the global configuration of glyph substitution is disabled.
	Location   	report
	Data type  	boolean
	Values     	true, glyph tution enabled
					false, glyph substitution disabled
	Default    	false
	Reference  	see -Dbirt.pdf.glyph.substitution.enabled
	Since      	4.20
