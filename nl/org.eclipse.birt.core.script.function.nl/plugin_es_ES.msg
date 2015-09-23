#Properties file for org.eclipse.birt.core.script.bre
Bundle-Name.0 = Complemento de la función

dataType.String=String

dataType.Any=Cualquiera

dataType.DateTime=DateTime
dataType.Integer=Entero

dataType.Double=Doble

dataType.Number=Número

dataType.Boolean=Booleano

dataType.DoubleArray=Doble[]

FunctionDesc.charLength=Devuelve el número de caracteres del string.

FunctionDesc.concat=Devuelve un string concatenado por todos los strings indicados.

FunctionDesc.indexOf=Busca el destino en el origen del string y devuelve el índice de la primera coincidencia del patrón. \nLa búsqueda comienza en el inicio de posición. Todos los valores del índice se basan en 0.\nSi se omite el inicio, se asume un valor de 0. \nLa búsqueda de string distingue entre mayúsculas y minúsculas.

FunctionDesc.left=Devuelve el primer número n de caracteres del string str. \nSi n es 0, se devolverá un string vacío. \nSi n es mayor que la longitud de str, se devolverá un string completo. \nSi se omite n, se utiliza un valor de 1.

FunctionDesc.right=Devuelve el último número n de caracteres de string str. \nSi n es 0, se devolverá un string vacío. Si n es mayor que la longitud de str, se devolverá un string completo. \nSi se omite n, se utiliza un valor de 1.

FunctionDesc.search=Similar a la función indexOf, excepto que: \n(1) la comparación de string no distingue entre mayúsculas y minúsculas \n(2) el string del patrón puede contener caracteres comodín: \n* coincide con cualquier secuencia de caracteres (incluidos los vacíos); \n? coincide con cualquier carácter individual.

FunctionDesc.toLower=Convierte todos los caracteres del origen del string a letra minúscula.

FunctionDesc.toUpper=Convierte todos los caracteres del origen del string a letra mayúscula.

FunctionDesc.trim=Elimina los caracteres en blanco iniciales y finales (espacio, Tabulación, etc.).\nAdemás, todos los caracteres en blanco consecutivos se consolidan en uno.

FunctionDesc.trimLeft=Elimina todos los caracteres iniciales en blanco. Los caracteres finales en blanco y los caracteres en blanco entre las palabras no se eliminan.

FunctionDesc.trimRight=Elimina todos los caracteres finales en blanco. Los caracteres iniciales en blanco y los caracteres en blanco entre las palabras no se eliminan.

FunctionDesc.anyOf=el origen se encuentra en el intervalo de destino

FunctionDesc.between=TRUE si el origen se encuentra entre target1 y target2. FALSE de lo contrario

FunctionDesc.notBetween=TRUE si el origen no se encuentra entre target1 y target2. FALSE de lo contrario

FunctionDesc.equalTo=el origen es igual que el destino

FunctionDesc.greaterThan=el origen es mayor que el destino

FunctionDesc.lessThan=el origen es menor que el destino

FunctionDesc.greaterOrEqual=el origen es mayor o igual que el destino

FunctionDesc.lessOrEqual=el origen es menor o igual que el destino

FunctionDesc.notEqual=el origen no es igual que el destino

FunctionDesc.like=el origen coincide con el destino del patrón de SQL

FunctionDesc.notLike=el origen no coincide con el destino del patrón de SQL

FunctionDesc.match=el origen coincide con el destino del patrón de Java

FunctionDesc.compareString=comparar los dos strings a través de las condiciones especificadas

FunctionDesc.add=Suma de los dos números n1 y n2.

FunctionDesc.substract=n1 se resta a n2

FunctionDesc.multiple=n1 se multiplica por n2. Esta función sólo se utiliza para la compatibilidad retroactiva de la función "multiplicar".

FunctionDesc.multiply=n1 se multiplica por n2

FunctionDesc.divide=el dividendo se divide por el divisor

FunctionDesc.round=Redondea un número al número especificado de dígitos. \ndec es un entero y puede ser negativo. \nEl valor predeterminado para dec es 0 cuando se omite

FunctionDesc.roundUp=Redondea un número hacia arriba, distinto de 0, hasta el número especificado de dígitos. \nEl valor predeterminado para dec es 0.

FunctionDesc.roundDown=Redondea un número hacia abajo, distinto de 0, hasta el número especificado de dígitos. \nEl valor predeterminado para dec es 0.

FunctionDesc.ceiling=Redondea un número hacia arriba, distinto de 0, al múltiplo de valor más cercano.

FunctionDesc.mod=Resto que se obtiene al dividir el número entre el divisor. El resultado tiene el mismo signo que el divisor.

FunctionDesc.safeDivide=Si el resultado del dividendo dividido por el divisor no es cero o infinito, devolverá el resultado. \nDe lo contrario devuelve el valor si es cero.

FunctionDesc.year=Número de año de 4 dígitos del valor de fecha/hora indicado

FunctionDesc.quarter=Número de trimestre (1 al 4) del valor de fecha/hora indicado

FunctionDesc.month=Mes del valor de fecha/hora. La opción es un valor entero:\n1 (predeterminado): devolver número de mes (1-12);\n2: devolver nombre de mes completo según configuración regional de usuario (por ejemplo, Enero a Diciembre para la configuración regional en español);\n3: devolver nombre de mes abreviado según configuración regional de usuario (por ejemplo, Ene a Dic para la configuración regional en español)

FunctionDesc.week=Número de semana del año (1 a 52) del valor d de fecha/hora.

FunctionDesc.day=Número de día del mes (1 a 31).

FunctionDesc.weekDay=Día de la semana. La opción es un valor entero\n1: devolver un número 1 (Domingo) a 7 (Sábado);\n2: devolver un número 1 (Lunes) a 7 (Domingo);\n3: devolver un número 0 (Lunes) a 6 (Domingo);\n4: devolver el nombre del día de la semana según la configuración regional del usuario (por ejemplo, Domingo a Sábado para español);\n5: devolver el nombre del día de la semana abreviado según la configuración regional del usuario (por ejemplo, Dom a Sáb para español)

FunctionDesc.today=Devuelve un valor de fecha y hora que es la medianoche de la fecha actual.

FunctionDesc.now=Devuelve el valor de fecha y hora actual.

FunctionDesc.diffYear=Devuelve la diferencia en número de años.

FunctionDesc.diffMonth=Devuelve la diferencia en número de meses.

FunctionDesc.diffQuarter=Devuelve la diferencia en número de trimestres.

FunctionDesc.diffWeek=Devuelve la diferencia en número de semanas.

FunctionDesc.diffDay=Devuelve la diferencia en número de días

FunctionDesc.diffHour=Devuelve la diferencia en número de horas.

FunctionDesc.diffMinute=Devuelve la diferencia en número de minutos.

FunctionDesc.diffSecond=Devuelve la diferencia en número de segundos.

FunctionDesc.addYear=Añadir n año(s) a la fecha

FunctionDesc.addMonth=Añadir n mes(es) a la fecha

FunctionDesc.addQuarter=Añadir n trimestre(s) a la fecha

FunctionDesc.addWeek=Añadir n semana(s) a la fecha

FunctionDesc.addDay=Añadir n día(s) a la fecha

FunctionDesc.addHour=Añadir n hora(s) a la fecha

FunctionDesc.addMinute=Añadir n minuto(s) a la fecha

FunctionDesc.addSecond=Añadir n segundo(s) a la fecha

FunctionDesc.firstDayOfYear=Devuelve el primer día del año en el que cae la fecha.

FunctionDesc.firstDayOfQuarter=Devuelve el primer día del trimestre en el que cae la fecha.

FunctionDesc.firstDayOfMonth=Devuelve el primer día del mes en el que cae la fecha.

FunctionDesc.firstDayOfWeek=Devuelve el primer día de la semana en el que cae la fecha.

FunctionDesc.ddb=Depreciación de un activo durante un único período concreto mediante el método de balance por doble disminución

FunctionDesc.fv=el valor futuro de una anualidad basado en pagos periódicos y constantes y en un tipo de interés invariable.

FunctionDesc.ipmt=El pago de intereses correspondientes a un período concreto de una anualidad, \nbasado en pagos periódicos y constantes y en un tipo de interés invariable.

FunctionDesc.irr=Tasa interna de devolución para una serie de flujos de efectivo, pagos y recibos periódicos de una matriz existente

FunctionDesc.mirr=Tasa de devolución modificada para una serie de flujos de efectivo periódicos (pagos y recibos) de una matriz existente.

FunctionDesc.nper=Número de períodos de una anualidad basado en pagos periódicos y constantes y en un tipo de interés invariable.

FunctionDesc.npv=Valor neto actual de una serie distinta de flujos de efectivo periódicos, positivos y negativos, con un tipo de interés concreto.

FunctionDesc.percent=Procesa el porcentaje de los dos números

FunctionDesc.pmt=El pago de una anualidad basado en pagos periódicos y constantes y en un tipo de interés invariable.

FunctionDesc.ppmt=El pago principal de un período concreto de una anualidad, basado en un tipo de interés y un plan de pago constante.

FunctionDesc.pv=Valor actual de una anualidad basado en pagos periódicos y constantes que se van a realizar en el futuro y en un tipo de interés invariable.

FunctionDesc.rate=Tipo de interés por período de una anualidad.

FunctionDesc.sln=Depreciación por método directo de un activo durante un único período.

FunctionDesc.syd=Depreciación en dígitos de una suma de años de un activo durante un período específico Remark

FunctionDesc.addDate=Devuelve una fecha que se obtiene de la suma de años, meses y días hasta la fecha de inicio.

FunctionDesc.addTime=Devuelve una fecha que se obtiene de la suma de horas, minutos y segundos hasta la fecha de inicio.

FunctionDesc.subDate=Devuelve una fecha que se obtiene de la resta de años, meses y días desde la fecha de inicio.

FunctionDesc.subTime=Devuelve una fecha que se obtiene de la resta de horas, minutos y segundos desde la fecha de inicio.

FunctionDesc.days=El número de días entre dos fechas

FunctionDesc.hours=El número de horas entre dos fechas

FunctionDesc.minutes=El número de minutos entre dos fechas

FunctionDesc.months=El número de meses entre dos fechas

FunctionDesc.seconds=El número de segundos entre dos fechas

FunctionDesc.years=El número de años entre dos fechas

FunctionDesc.date= Devuelve un objeto de fecha inicializado con el año, mes, días, horas, minutos y segundos proporcionados. \n(año, mes y día son necesarios; horas minutos y segundos son opcionales)

FunctionDesc.duration.year=Obtiene el valor del campo YEARS como un valor entero,* o 0 si no está presente.\nEl valor lexicalDuration es la presentación del string de duración, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S)

FunctionDesc.duration.month=Obtiene el valor del campo MONTHS como un valor entero,* o 0 si no está presente.\nEl valor lexicalDuration es la presentación del string de duración, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S)

FunctionDesc.duration.day=Obtiene el valor del campo DAYS como un valor entero,* o 0 si no está presente.\nEl valor lexicalDuration es la presentación del string de duración, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S)

FunctionDesc.duration.hour=Obtiene el valor del campo HOURS como un valor entero,* o 0 si no está presente.\nEl valor lexicalDuration es la presentación del string de duración, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S)

FunctionDesc.duration.minute=Obtiene el valor del campo MINUTES como un valor entero,* o 0 si no está presente.\nEl valor lexicalDuration es la presentación del string de duración, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S)

FunctionDesc.duration.second=Obtiene el valor del campo SECONDS como un valor entero,* o 0 si no está presente.\nEl valor lexicalDuration es la presentación del string de duración, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S)

FunctionDesc.duration.timeInMills=Devuelve la longitud de la duración en milisegundos.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )\nEl argumento startDate se utiliza para desambiguar esta varianza

FunctionDesc.duration.isLongerThan=Comprueba si este valor lexicalDuration1 es estrictamente más largo que otro valor lexicalDuration2.\nPor ejemplo, "P1D"(un día) > "PT12H"(12 horas) y "P2Y" (dos años) > "P23M" (23 meses)

FunctionDesc.duration.isShorterThan=Comprueba si el valor lexicalDuration1 es estrictamente más corto que otro valor lexicalDuration2.\nPor ejemplo,"PT12H"(12 horas) < "P1D"(un día).

FunctionDesc.duration.add=Procesa una nueva duración cuyo valor es lexicalDuration1+lexicalDuration2.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )

FunctionDesc.duration.substract=Procesa una nueva duración cuyo valor es lexicalDuration1-lexicalDuration2.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )

FunctionDesc.duration.multiply=Procesa una nueva duración cuyo valor es "factor" veces más largo que el valor de lexicalDuration.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )\nFactor is the times longer of new Duration to create

FunctionDesc.duration.negate=Devuelve un nuevo objeto Duration cuyo valor es -lexicalDuration.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )

FunctionDesc.duration.getSign=Devuelve el signo de esta duración en -1,0 ó 1.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )

FunctionDesc.duration.compare=La relación de orden parcial entre lexicalDuration1 y lexicalDuration2.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )

FunctionDesc.duration.addTo=Suma esta duración a un objeto Date.\nEl valor lexicalDuration es la presentación del string, "PnYnMnDTnHnMnS"(por ejemplo, P5Y2M15DT4H40M20S )\nEl argumento startDate se utiliza para desambiguar esta varianza

FunctionDesc.format=Obtiene el string con formato del objeto y el patrón especificados.
