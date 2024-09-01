# Eclipse BIRT 
Introduction to use the specialized user properties of the PdfEmitter.

## Reason
The PdfEmitter provides a set of specialized user properties to optimize the pdf output according of the reporting based requirements.

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

**PdfEmitter.Conformance**

	Content    	define the PDF conformance of the created PDF document, e.g. PDF.A1A
	Location   	report
	Data type  	string
	Values     	PDF.Standard, PDF.X1A2001, PDF.X32002, PDF.A1A, PDF.A1B
	Default    	PDF.Standard
	Reference  	see PdfEmitter.IccColorType, PdfEmitter.IccProfileFile, PdfEmitter.PDFA.AddDocumentTitle, PdfEmitter.PDFA.FallbackFont, PdfEmitter.IncludeCidSet
	Since      	4.16
	Designer  	4.17


**PdfEmitter.UAConformance**

	Content    	define the PDF/UA conformance of the created PDF document, e.g. PDF.UA-1
	Location   	report
	Data type  	string
	Values     	PDF.Standard, PDF.UA-1
	Default    	PDF.Standard
	Since      	4.16
	Designer  	4.17

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
