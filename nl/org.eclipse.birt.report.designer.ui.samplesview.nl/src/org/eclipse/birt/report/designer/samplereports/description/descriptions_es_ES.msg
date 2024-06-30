###########################################################
#Solution reports descriptions

#Listing
ProductCatalog=Imprime el catálogo de productos de Classic Models, agrupados por categoría de producto. Proporciona el nombre de producto, coste y descripción. Muestra una agrupación de nivel y utiliza una cuadrícula dentro de una fila de tabla para estructurar la separación. Además, muestra la inclusión de imagen y uso de la etiqueta <valor de> en elemento de texto para incluir el contenido de una columna de base de datos. Finalmente, el informe utiliza estilos para simplificar el mantenimiento y lograr una apariencia coherente. 
TopSellingProducts=Muestra un diagrama circular de los ingresos por línea de productos. Muestra una lista de los productos más vendidos, ordenados por ingresos. Muestra el uso de un gráfico y ordena un conjunto de resultados. Además, muestra la inclusión de imagen y utiliza cuadrícula y tablas para organizar el contenido del informe. Finalmente, el informe utiliza estilos para simplificar el mantenimiento y lograr una apariencia coherente. 
TopNPercent=Este informe utiliza las funciones de filtro de "n" principales y porcentaje de principales añadidas a BIRT 2.0 y muestra un diagrama de barras con los principales N clientes por ingresos, seguido de una lista de los principales M% clientes por ingresos ordenados por nombre de cliente. Para ello es necesario repasar varias veces los datos, pero esto lo realiza el motor de informes de BIRT de manera no visible. N y M se trasladan al informe como parámetros para permitir a los usuarios de informes seleccionar el número y porcentaje en tiempo de ejecución. 
StaticCrosstab=En este ejemplo se muestra la compilación de un informe estático de tabla de referencias cruzadas con la función BIRT Total. El informe presenta los ingresos de ventas anuales por trimestre para cada una de las líneas de productos que vende actualmente Classic Models Inc. Además, se agregan totales en las dimensiones horizontal y vertical. La función BIRT Total puede aceptar un segundo parámetro que filtra los datos agregados. En este ejemplo, esta función se utiliza para mostrar sólo valores que pertenecen a una oferta de productos, como Trenes. El primer parámetro de la función Total es el valor que se va a agregar, y en este ejemplo se ha definido como la cantidad vendida multiplicada por cada precio. El segundo parámetro es el filtro. Por tanto, si el filtro se ha especificado como dataSetRow["Productos::PRODUCTLINE"]=="Motocicletas", sólo se agregaría la línea de producto Motocicletas. 

#Business Forms
SalesInvoice=Imprime una factura para el pedido seleccionado, incluidos detalles de cliente y factura y productos solicitados. Muestra el uso de un parámetro para seleccionar el pedido que hay que facturar y expresiones para varios campos calculados, incluidos descuento y total del pedido. Utiliza la expresión para compilar el string de dirección del cliente e ilustra la supresión de nulos en los campos de base de datos con la función de sustitución de javascript. También muestra la inclusión de imagen y un uso sofisticado de cuadrículas y tablas para organizar el contenido del informe. Finalmente, el informe utiliza estilos para simplificar el mantenimiento y lograr una apariencia coherente. 

###########################################################
#Reporting Feature Examples descriptions

#XML Data Source
XMLDS_After=Un informe sencillo que muestra el uso de fuentes de datos XML. 

#Libraries
Newsfeeds=En este ejemplo se muestra cómo se puede utilizar BIRT para compilar librerías de informes de componentes reutilizables. En este ejemplo, empezamos creando una librería. Utilizamos URI para Eclipse RSS Newsfeed como fuente de datos de un informe y lo almacenamos en la librería. A continuación, publicamos la librería en una carpeta de recursos. En este ejemplo se muestra la creación de elementos no visuales en una librería. Es importante destacar que también se pueden crear elementos visuales y almacenarlos en una librería. 
Newsfeeds_After=En este ejemplo se muestra cómo se puede utilizar BIRT para compilar librerías de informes de componentes reutilizables. En este ejemplo, empezamos creando una librería. Utilizamos URI para Eclipse RSS Newsfeed como fuente de datos de un informe y lo almacenamos en la librería. A continuación, publicamos la librería en una carpeta de recursos. En este ejemplo se muestra la creación de elementos no visuales en una librería. Es importante destacar que también se pueden crear elementos visuales y almacenarlos en una librería.

#Combination Chart
CustomerOrdersFinal=En este ejemplo se muestra cómo se puede crear un diagrama de combinación dentro de BIRT. Además, este ejemplo muestra cómo añadir detalles, usando marcadores internos y el Editor de interactividad de diagrama. El diagrama de combinación presenta la deuda pendiente de los 5 clientes principales en formato de diagrama de barras con una serie de líneas que representan cada límite de crédito de los clientes superpuesto. Las barras dentro del diagrama están vinculadas con detalles de pedidos para cada cliente. 

#Cascading
cascade=En este ejemplo se muestra cómo compilar un informe BIRT que utiliza un grupo de parámetros en cascada. Un grupo de parámetros en cascada permite vincular entre sí un grupo de parámetros en el que, al seleccionar un valor para el primer parámetro, se afecta a las opciones disponibles en los siguientes parámetros. Los parámetros en cascada pueden vincularse a uno o más conjuntos de datos. Los conjuntos de datos completan cada nivel de la cascada. Esto, combinado con parámetros de conjuntos de datos, ofrece muy buena flexibilidad para descartar datos devueltos de un informe.

#Drill to details
MasterDetailOneReport=En este ejemplo se ilustra la creación de un informe con información de datos principales contenidos dentro del mismo informe.
OrderMasterAfter=En este ejemplo se muestra la compilación de un informe principal y un informe detallado. La lista de informes principales solicita y permite al usuario del informe hacer clic en un número de pedido particular para acceder al informe detallado del pedido seleccionado. En este ejemplo se ilustra el uso de un parámetro sencillo para descartar resultados del informe detallado y la propiedad de hyperlink dentro del informe principal para pasar este parámetro al informe detallado. La propiedad de hyperlink dentro del informe principal se genera dinámicamente basándose en resultados obtenidos de una consulta.
OrderDetailAfter=El informe detallado al que se accede al hacer clic en el informe principal.

#Dynamic Report Parameter
ParameterToDataSetParameter=En este ejemplo se ilustra la creación de un link entre un parámetro de informe y un parámetro de conjunto de datos.

#Expressions
Expressions=En este ejemplo se muestra cómo compilar un informe BIRT que utiliza funciones.

#Grouping
CustomerListAfter_Grouping=En este ejemplo se muestra la compilación de un informe agrupado en BIRT. El informe de ejemplo muestra una lista de clientes y grupos por país. Las funciones clave de BIRT que se incluyen en esta demostración son agrupación, saltos de página, tabla de contenido y agregación de datos por grupo o como total. 

#Highlighting and Conditional Formatting
ProductListAfter=En este ejemplo se muestra cómo añadir formato condicional y de resaltado a un informe BIRT. Se desarrolla un informe de lista de productos que muestra todos los productos de la compañía. El informe de ejemplo utiliza colores de fila alternativos y resalta filas que representan un producto que tiene una cantidad baja. Este informe ilustra también la adición de un script al evento onCreate de una fila para modificar el estilo de fila a cursiva para productos con valores de código alto. 

#Mapping and Sorting 
CustomerListAfter_Mapping=En este ejemplo se muestra el uso de las funciones de Asignación y Orden de BIRT. Asignación permite a un elemento de informe BIRT mostrar un valor diferente basado en una expresión. Por ejemplo, si una consulta devuelve un campo numérico para mes (1-12), la función de asignación puede utilizarse para presentar Enero - Diciembre. En este ejemplo, utilizamos asignación para mostrar un rango de crédito para clientes de Classic Models. Orden se utiliza en muchas ocasiones en BIRT y es muy flexible. En este ejemplo, mostramos cómo añadir una expresión de ordenación a un elemento de tabla que está vinculado a un parámetro de informe. El parámetro permite introducir un string de búsqueda que, después, se utiliza en la expresión de ordenación para definir el orden para la tabla. 

#Parallel Report 
EmployeeAfter=En este ejemplo se muestra cómo compilar un informe con secciones paralelas. También se ilustra el uso de filtros de tabla para descartar los datos mostrados. El informe utiliza un elemento de cuadrícula para diseñar dos columnas. En cada columna, se añade un conjunto de tablas anidadas para recuperar información de empleados y agruparla por jerarquía de posición. Las tablas se filtran por código de oficina. 

#Query Modification
NullParameter=En este ejemplo se muestra cómo modificar la consulta basada en los parámetros de informe utilizando eventos de conjuntos de datos. Por ejemplo, la consulta se modifica si se proporciona un valor para el parámetro de informe. 
OrderDetailsAfter=En este ejemplo se muestra cómo modificar una consulta JDBC usando Enlace de propiedades o Script. Enlace de propiedades es una función dentro de BIRT que permite ejecutar la modificación de hora de propiedades públicas de Fuente de datos o Conjunto de datos. Esto puede resultar útil para añadir información sensible al contexto a cualquier elemento. Como ejemplo, la cláusula 'where' puede modificarse para recuperar el nombre de usuario de la sesión o se puede cambiar la URL de la base de datos entre desarrollo y producción basándose en un parámetro de informe. 

#Report Elements
ProductLinesAfter=En este ejemplo se muestra el uso de los elementos Etiqueta, Texto, Texto dinámico, Datos e Imagen dentro de un diseño de informe. También se identifican algunas funciones clave y diferencias entre los elementos. Este ejemplo ilustra también la recuperación de datos BLOB y CLOB de una fuente de datos. 

#Sub Report
OrdersAfter=En este ejemplo se muestra la compilación de un informe que utiliza tablas anidadas. La consulta vinculada a la tabla interna dispone de parámetros para devolver detalles del pedido de un número de pedido determinado de la tabla externa. En este ejemplo se muestra también la referencia a columnas de tabla externas dentro de una tabla anidada. Esto permite diseñar informes BIRT con información de datos principales contenidos dentro del mismo informe. Al procesar la tabla externa, se vuelve a ejecutar su consulta de tabla interior basada en información de la tabla exterior. 

#Sorting
SortTableByReportParameter=En este ejemplo se muestra la creación de un informe que utiliza tablas anidadas. La consulta vinculada a la tabla interna dispone de parámetros para devolver detalles del pedido de un número de pedido determinado de la tabla externa. En este ejemplo se muestra también la referencia a columnas de tabla externas dentro de una tabla anidada. Esto permite diseñar informes BIRT con información de datos principales contenidos dentro del mismo informe. Al procesar la tabla externa, se vuelve a ejecutar su consulta de tabla interior basada en información de la tabla exterior. 

#Cross tab
CrosstabSampleRevenue=En este ejemplo se muestra la creación de un informe simple con un elemento de informe de tabla de referencias cruzadas (basado en un cubo BIRT). Se crea una tabla de referencias cruzadas con línea de productos en el área de columna y año->trimestre en el área de fila.
###########################################################
#Contribute Samples
Contribute_Samples=Envíe una entrada de Bugzilla en https://bugs.eclipse.org/bugs/enter_bug.cgi?product=BIRTb&bug_severity=enhancement, adjunte el archivo de diseño de informe y la captura de pantalla de la salida de informe (en formato de imagen .png, resolución 800*600, inferior a 250 KB)

###########################################################
#Scripting

#Dynamic Map Rule
DynamicMapRule=En este ejemplo se ilustra la llamada a la API del motor de diseño dentro del script beforeFactory para agregar una regla de asignación a un elemento de informe.  

#Group Separator
GroupSeparator=En este ejemplo se ilustra la adición de un separador cada tres filas dentro de un grupo. Este recuento se restablece en un salto de página.  

#Retrieve Session Information
Application_Session_IntegrationBF=En este ejemplo se ilustra el uso de un JavaScript para recuperar información de sesión.

#Scripted Data Source
ScriptedDatasourceExampleAfter=Esta sección de Ejemplos se centra en los scripts dentro de BIRT. Se utilizan informes sencillos para ilustrar la funcionalidad de scripts. La mayoría de los ejemplos tendrá un informe de antes y después que puede descargarse. Además, el ejemplo se muestra en una presentación Flash. Para cargar el objeto java exportado y permitir que el informe lo utilice, reinicie BIRT después de abrir este informe de ejemplo.  

#Visibility Rule
Addvisrule=En este ejemplo se ilustra cómo agregar una regla de visibilidad a un script.  

#Chart
SimpleChartAPI=En este ejemplo se ilustran muchos de los métodos de JavaScript disponibles dentro del diagrama API simple.  Se les denominan según el controlador de evento beforeFactory del informe.

###########################################################
#Extending BIRT

#Aggreate function extension point
optim=Esta sección de Ejemplos se centra en Extender BIRT. Los ejemplos están enfocados a la implementación de puntos de extensión suministrados por BIRT, proporcionando al desarrollador de BIRT la capacidad de añadir o modificar el comportamiento predeterminado del diseñador o motor de tiempo de ejecución. Esto incluye Tipos de diagrama, funciones de agregación, Fuentes de datos y Elementos de informes. 


 
