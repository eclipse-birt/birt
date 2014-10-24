#Properties file for org.eclipse.birt.core.script.bre
Bundle-Name.0 = 함수 플러그인

dataType.String=문자열

dataType.Any=모두

dataType.DateTime=DateTime
dataType.Integer=정수

dataType.Double=이중 실선

dataType.Number=숫자

dataType.Boolean=부울

dataType.DoubleArray=Double[]

FunctionDesc.charLength=문자열의 문자 수를 반환합니다.

FunctionDesc.concat=지정된 모든 문자열에 의해 연결된 문자열을 반환합니다.

FunctionDesc.indexOf=문자열 원본에서 대상을 검색하고 첫 번째 항목 패턴의 인덱스를 반환합니다. \n시작 위치에서 검색을 시작합니다. 모든 인덱스 값은 0부터 시작합니다.\n시작 값이 누락된 경우 값이 0으로 나타납니다. \n문자열 검색은 대/소문자를 구분합니다.

FunctionDesc.left=문자열에서 처음 n개의 문자를 반환합니다. \nn이 0이면 빈 문자열이 반환됩니다. \nn이 문자열 길이보다 크면 전체 문자열이 반환됩니다. \nn이 누락된 경우 값 1이 사용됩니다.

FunctionDesc.right=문자열에서 마지막 n개의 문자를 반환합니다. \nn이 0이면 빈 문자열이 반환됩니다. n이 문자열 길이보다 크면 전체 문자열이 반환됩니다. \nn이 누락된 경우 값 1이 사용됩니다.

FunctionDesc.search=다음을 제외하고 indexOf 함수와 유사합니다. \n(1) 문자열 비교가 대/소문자를 구분하는 경우 \n(2) 패턴 문자열에 와일드카드 문자를 포함할 수 있는 경우: \n*는 여러 문자(공백 포함)를 표시함 \n?는 한 문자를 표시함

FunctionDesc.toLower=문자열 원본 내 모든 문자를 소문자로 변환합니다.

FunctionDesc.toUpper=문자열 원본 내 모든 문자를 대문자로 변환합니다.

FunctionDesc.trim=모든 선행 및 후행 공백 문자(공백, 탭 등)를 제거합니다.\n또한, 연속적인 모든 공백 문자는 하나로 연결됩니다.

FunctionDesc.trimLeft=모든 선행 공백을 제거합니다. 후행 공백 및 단어 사이의 공백은 제거되지 않습니다.

FunctionDesc.trimRight=모든 후행 공백을 제거합니다. 선행 공백 및 단어 사이의 공백은 제거되지 않습니다.

FunctionDesc.anyOf=원본이 대상 범위 내에 있음

FunctionDesc.between=원본이 target1과 target2 사이에 있으면 TRUE입니다. 그렇지 않은 경우 FALSE입니다.

FunctionDesc.notBetween=원본이 target1과 target2 사이에 있지 않으면 TRUE입니다. 그렇지 않은 경우 FALSE입니다.

FunctionDesc.equalTo=원본이 대상과 같음

FunctionDesc.greaterThan=원본이 대상보다 큼

FunctionDesc.lessThan=원본이 대상보다 작음

FunctionDesc.greaterOrEqual=원본이 대상보다 크거나 같음

FunctionDesc.lessOrEqual=원본이 대상보다 작거나 같음

FunctionDesc.notEqual=원본이 대상과 같지 않음

FunctionDesc.like=원본이 SQL 패턴 대상과 일치함

FunctionDesc.notLike=원본이 SQL 패턴 대상과 일치하지 않음

FunctionDesc.match=원본이 Javascript 패턴 대상과 일치함

FunctionDesc.compareString=지정된 조건별로 두 개의 문자열 비교

FunctionDesc.add=두 개의 숫자 n1과 n2의 합입니다.

FunctionDesc.substract=n1에서 n2를 뺀 결과

FunctionDesc.multiple=n1과 n2를 곱한 결과 이 함수는 "곱하기" 함수의 역호환에만 사용됩니다.

FunctionDesc.multiply=n1과 n2를 곱한 결과

FunctionDesc.divide=피제수는 제수로 나눔

FunctionDesc.round=수를 지정된 자릿수로 반올림합니다. \ndec는 음의 정수일 수 있습니다. \ndec의 기본값이 누락된 경우 0입니다.

FunctionDesc.roundUp=0에서 멀어지도록 수를 지정된 자릿수로 올림합니다. \ndec의 기본값은 0입니다.

FunctionDesc.roundDown=0에서 멀어지도록 수를 지정된 자릿수로 내림합니다. \ndec의 기본값은 0입니다.

FunctionDesc.ceiling=0에서 멀어지도록 수를 significance의 배수가 될 때까지 절대 값을 올림합니다.

FunctionDesc.mod=수를 제수로 나눈 나머지입니다. 결과는 제수의 부호와 같습니다.

FunctionDesc.safeDivide=피제수를 제수로 나눈 결과가 0이 아니거나 무한인 경우 결과가 반환됩니다. \n결과가 0이면 지정된 값이 반환됩니다.

FunctionDesc.year=지정된 날짜/시간 값에 대한 4자리 연도 번호입니다.

FunctionDesc.quarter=지정된 날짜/시간 값에 대한 분기 번호(1-4)입니다.

FunctionDesc.month=날짜/시간 값의 월 번호입니다. 옵션은 정수 값입니다.\n1(기본값): 월 번호(1-12)를 반환합니다.\n2: 사용자 로케일에 따른 전체 월 이름을 반환합니다(예: 영어 로케일의 경우 January-December).\n3: 사용자 로케일에 따른 축약형 월 이름을 반환합니다(예: 영어 로케일의 경우 Jan-Dec).

FunctionDesc.week=날짜/시간 값 d에 대한 주 번호(1-52)입니다.

FunctionDesc.day=날짜입니다(1-31).

FunctionDesc.weekDay=요일입니다. 옵션은 정수 값입니다.\n1: 1-7(Sunday-Saturday)을 반환합니다.\n2: 1-7(Monday-Sunday)을 반환합니다.\n3: 0-6(Monday-Sunday)을 반환합니다.\n4: 사용자 로케일에 따른 요일 이름을 반환합니다(예: 영어 로케일의 경우 Sunday-Saturday).\n5: 사용자 로케일에 따른 축약형 요일 이름을 반환합니다(예: 영어 로케일의 경우 Sun-Sat).

FunctionDesc.today=값이 현재 날짜의 자정인 타임스탬프를 반환합니다.

FunctionDesc.now=현재 타임스탬프를 반환합니다.

FunctionDesc.diffYear=연도 수의 차이를 반환합니다.

FunctionDesc.diffMonth=개월 수의 차이를 반환합니다.

FunctionDesc.diffQuarter=분기 수의 차이를 반환합니다.

FunctionDesc.diffWeek=주 수의 차이를 반환합니다.

FunctionDesc.diffDay=일 수의 차이를 반환합니다.

FunctionDesc.diffHour=시간 수의 차이를 반환합니다.

FunctionDesc.diffMinute=분 수의 차이를 반환합니다.

FunctionDesc.diffSecond=초 수의 차이를 반환합니다.

FunctionDesc.addYear=날짜에 n년 추가

FunctionDesc.addMonth=날짜에 n개월 추가

FunctionDesc.addQuarter=날짜에 n분기 추가

FunctionDesc.addWeek=날짜에 n주 추가

FunctionDesc.addDay=날짜에 n일 추가

FunctionDesc.addHour=날짜에 n시간 추가

FunctionDesc.addMinute=날짜에 n분 추가

FunctionDesc.addSecond=날짜에 n초 추가

FunctionDesc.firstDayOfYear=해당 날짜가 속한 한 해의 첫 날짜를 반환합니다.

FunctionDesc.firstDayOfQuarter=해당 날짜가 속한 한 분기의 첫 날짜를 반환합니다.

FunctionDesc.firstDayOfMonth=해당 날짜가 속한 한 달의 첫 날짜를 반환합니다.

FunctionDesc.firstDayOfWeek=해당 날짜가 속한 한 주의 첫 날짜를 반환합니다.

FunctionDesc.ddb=주어진 한 기간 동안 이중 체감법을 사용한 자산의 감가 상각액입니다.

FunctionDesc.fv=주기적이고 고정적인 지급과 고정적인 이자율에 근거한 연금의 예측값입니다.

FunctionDesc.ipmt=주기적이고 고정적인 지급과 고정적인 이자율에 근거한 \n주어진 기간 동안의 연금에 대한 이자 지급입니다.

FunctionDesc.irr=기존 배열에서 일련의 주기적 현금 흐름, 지급 및 수금에 대한 내부 수익률입니다.

FunctionDesc.mirr=기존 배열에서 일련의 주기적 현금 흐름(지급 및 수금)에 대한 수정된 수익률입니다.

FunctionDesc.nper=주기적이고 고정적인 지급과 고정적인 이자율에 근거한 연금에 대한 기간 수입니다.

FunctionDesc.npv=주어진 이자율에서 일련의 여러 양수 값 및 음수 값의 주기적 현금 흐름에 대한 순 현재 가치입니다.

FunctionDesc.percent=두 숫자의 백분율을 계산합니다.

FunctionDesc.pmt=주기적이고 고정적인 지급과 고정적인 이자율에 근거한 연금에 대한 지급입니다.

FunctionDesc.ppmt=이자율과 고정적인 지급 일정에 기반한 주어진 기간 동안의 연금에 대한 원금 지급입니다.

FunctionDesc.pv=미래에 지불할 주기적이고 고정적인 지급과 고정적인 이자율에 근거한 연금에 대한 현재 값입니다.

FunctionDesc.rate=연금에 대한 기간별 이자율입니다.

FunctionDesc.sln=한 기간 동안 정율법에 의한 자산의 감가 상각액입니다.

FunctionDesc.syd=주어진 기간 동안 연수 합계법에 의한 자산의 감가 상각액입니다.

FunctionDesc.addDate=시작 날짜에 연도, 개월 및 일 수를 추가한 날짜를 반환합니다.

FunctionDesc.addTime=시작 날짜에 시간, 분 및 초 수를 추가한 날짜를 반환합니다.

FunctionDesc.subDate=시작 날짜에서 연도, 개월 및 일 수를 뺀 날짜를 반환합니다.

FunctionDesc.subTime=시작 날짜에서 시간, 분 및 초 수를 뺀 날짜 날짜를 반환합니다.

FunctionDesc.days=두 날짜 사이의 날짜 수

FunctionDesc.hours=두 날짜 사이의 시간 수

FunctionDesc.minutes=두 날짜 사이의 분 수

FunctionDesc.months=두 날짜 사이의 개월 수

FunctionDesc.seconds=두 날짜 사이의 초 수

FunctionDesc.years=두 날짜 사이의 연도 수

FunctionDesc.date= 지정된 연도, 월, 일, 시간, 분, 초로 초기화된 날짜 개체를 반환합니다. \n(연도, 월, 일은 필수이고, 시간, 분, 초는 옵션입니다.)

FunctionDesc.duration.year=YEARS 필드에 값이 없는 경우 정수, * 또는 0을 입력합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 기간 문자열입니다.

FunctionDesc.duration.month=MONTHS 필드에 값이 없는 경우 정수, * 또는 0을 입력합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 기간 문자열입니다.

FunctionDesc.duration.day=DAYS 필드에 값이 없는 경우 정수, * 또는 0을 입력합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 기간 문자열입니다.

FunctionDesc.duration.hour=HOURS 필드에 값이 없는 경우 정수, * 또는 0을 입력합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 기간 문자열입니다.

FunctionDesc.duration.minute=MINUTES 필드에 값이 없는 경우 정수, * 또는 0을 입력합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 기간 문자열입니다.

FunctionDesc.duration.second=SECONDS 필드에 값이 없는 경우 정수, * 또는 0을 입력합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 기간 문자열입니다.

FunctionDesc.duration.timeInMills=기간의 길이를 밀리초 단위로 반환합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.\nstartDate 인수는 이 분산의 모호함을 해결하는 데 사용됩니다.

FunctionDesc.duration.isLongerThan=이 lexicalDuration1이 다른 lexicalDuration2보다 긴지 확인합니다.\n예: "P1D"(1일) > "PT12H"(12시간) 및 "P2Y"(2년) > "P23M"(23개월)

FunctionDesc.duration.isShorterThan=lexicalDuration1이 다른 lexicalDuration2보다 짧은지 확인합니다.\n예: "PT12H"(12시간) > "P1D"(1일)

FunctionDesc.duration.add=값이 lexicalDuration1+lexicalDuration2인 새 기간 값을 계산합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.

FunctionDesc.duration.substract=값이 lexicalDuration1-lexicalDuration2인 새 기간 값을 계산합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.

FunctionDesc.duration.multiply=시간 비율 값이 lexicalDuration 값보다 긴 새 기간을 계산합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.\nFactor is the times longer of new Duration to create

FunctionDesc.duration.negate=값이 -lexicalDuration인 새 기간 개체를 반환합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.

FunctionDesc.duration.getSign=이 기간의 부호 값을 -1, 0 또는 1로 반환합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.

FunctionDesc.duration.compare=lexicalDuration1 및 lexicalDuration2 사이의 부분적인 순서 관계입니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.

FunctionDesc.duration.addTo=날짜 개체에 이 기간을 추가합니다.\nlexicalDuration은 "PnYnMnDTnHnMnS"(예: P5Y2M15DT4H40M20S)로 표시되는 문자열입니다.\nThe given Date argument is used to be modified

FunctionDesc.format=지정된 개체 및 패턴으로부터 서식 있는 스트링을 가져옵니다.
