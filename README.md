# User Guide

## 1.0 Introduction

Full Text For Java(이하 ft4j)는 바이트 배열을 자바 객체로 변환하는 라이브러리입니다. 오늘 날 서로 다른 시스템들은 json 포맷의 데이터를 송수신합니다. 하지만 일부 도메인(ex.금융권)에서는 고정길이 문자열  방식(이하. 전문 방식)으로 데이터 송수신이 이뤄집니다.

이러한 방식은 일련의 바이트 배열에 대해서 오프셋과 길이 정보를 토대로 어플리케이션에서 수동으로 자바 객체의 필드 혹은 변수로 변환하는 작업을 거칩니다.

> 문자열 길이 기반으로 marshall/unmarshall을 지원하는 라이브러리는 여러가지 있었습니다.  ft4j는 다른 라이브러리들의 가장 큰 차이점은 바이트 기반의 연산을 지원한다는 것입니다. 특히, 한글의 경우 인코딩 방식에 따라 2~3바이트로 변환되는데 ft4j는 인코딩 방식에 따라 한글 문자 연산을 지원합니다. 자세한 사항은 **3.2 인코딩**을 참고하세요

예를 들어 이름, 이메일, 주소로 이뤄진 유저 정보를 아래의 오프셋과 길이 기반의 전문으로 전송한다고 가정하겠습니다.

| 속성명 | 오프셋 | 길이  |
| --- | --- | --- |
| 이름  | 0   | 10  |
| 이메일 | 10  | 20  |
| 주소  | 30  | 20  |

총 50바이트의 유저 정보 전문을 자바 변수로 변환하기 위해서는 아래와 같은 일련의 방식을 거쳐야합니다. 

```java
@Test
void manual_parse_test() {

    String rawData = "John      test@gmail.com      Korea, Seoul        ";
    byte[] bytes = rawData.getBytes();

    int offset = 0;
    int length = 10;
    String name = new String(Arrays.copyOfRange(bytes, offset, offset + length));
    assertEquals("John      ", name);

    offset += length;
    length = 20;
    String email = new String(Arrays.copyOfRange(bytes, offset, offset + length));
    assertEquals("test@gmail.com      ", email);

    offset += length;
    length = 20;
    String address = new String(Arrays.copyOfRange(bytes, offset, offset + length));
    assertEquals("Korea, Seoul        ", address);
}
```

같은 결과를 나타낼 수 있는 다양한 방식이 있겠지만, 이러한 방식의 문제는 오프셋과 길이를 하드코딩 해야하고 바이트 값을 자바 변수의 타입에 맞게 변환해줘야 하는 추가적인 작업이 필요할 수 있기 때문에 전문이 복잡해질수록 유지보수가 어렵게됩니다.

## 2.0 Getting Started

ft4j를 사용하기 위해서, `ft4j.jar`를 어플리케이션 클래스패스에 추가하거나 의존성을 추가합니다. ft4j는 JDK 1.8 버전 이상을 요구합니다.

```xml
<dependency>
    <groupId>com.github.ghkvud2</groupId>
    <artifactId>ft4j</artifactId>
    <version>1.4</version>
</dependency>
```

```groovy
implementation group: 'io.github.ghkvud2', name: 'ft4j', version: '1.1'
```

### 2.0.1 Marshall Simple Example

ft4j를 사용해서 간단한 marshalling 예제를 살펴봅니다. 위에서 정의했던 전문에 대응하는 필드를 갖는 Person클래스를 선언하고 각 필드의 타입에 맞게 어노테이션을 필드에 선언합니다.

```java
class Person {

    @StringValue(order = 1, length = 10)
    private String name;

    @StringValue(order = 2, length = 20)
    private String email;

    @StringValue(order = 3, length = 20)
    private String address;

    //constructors, getters
}
```

```java
@Test
void ft4j_marshall_test() {

    MarshallManager marshaller = MarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] expected = "John      test@gmail.com      Korea, Seoul        ".getBytes(Charset.forName("utf-8"));   
    Person person = new Person("John", "test@gmail.com", "Korea, Seoul");
    byte[] result = marshaller.marshall(person);

    assertArrayEquals(expected, result);
}
```

테스트 결과를 보면, Person 객체의 필드 값들이 미리 정의된 길이에 맞춰 변환된 것을 확인할 수 있습니다.

> 테스트를 실행하면 Person 객체의 필드 값이 변환될 때, 부족한 길이만큼은 공백으로 채워진 것을 볼 수 있습니다. 해당 내용은 3.1.1 공통 속성을 참고하세요.

### 2.0.2 Unmarshall Simple Example

unmarshalling 예제를 살펴봅니다.

```java
@Test
void ft4j_unmarshall_test() {

    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] input = "John      test@gmail.com      Korea, Seoul        ".getBytes(Charset.forName("utf-8"));
    Person person = unMarshaller.unmarshall(input, Person.class);

    assertEquals("John", person.getName());
    assertEquals("test@gmail.com", person.getEmail());
    assertEquals("Korea, Seoul", person.getAddress());
}
```

> 테스트를 실행하면 rawData에서의 공백이 모두 제거된 값이 Person 객체의 필드에 저장된 것을 확인할 수 있습니다. 해당 내용은 3.1.1 공통 속성을 참고하세요.

## 3.0 Core Concept

### 3.1 매핑 어노테이션

객체의 필드에 선언할 수 있는 대표적인 어노테이션의 종류는 아래와 같습니다. 어노테이션 이름에서 알 수 있듯이 필드의 타입에 따라 선언할 어노테이션이 정해져있습니다. 만약, 올바르지 않은 어노테이션을 선언하면 예외가 발생합니다.

1. @StringValue

2. @FloatValue

3. @DoubleValue

4. @ShortValue

5. @IntValue

6. @LongValue
   
   #### 3.1.1 공통 속성

위 어노테이션들이 공통적으로 갖는 속성에 대해서 살펴보겠습니다.

| 번호  | 속성명          | 타입          | 필수여부 | 설명                                                                                                                                   |
| --- | ------------ | ----------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------ |
| 1   | order        | int         | Y    | 필드들의 marshall/unmarshall 순서를 지정합니다.                                                                                                  |
| 2   | length       | int         | Y    | 해당 필드의 바이트 수를 지정합니다.                                                                                                                 |
| 3   | defaultValue | String      | N    | marshall 연산시, 현재 필드 값에 상관없이 해당 값으로 대체합니다.                                                                                            |
| 4   | paddingByte  | PaddingByte | N    | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 나머지 바이트를 채울 값을 지정합니다. @StringValue와 같은 문자열 타입의 default 값은 SPACE고, 다른 숫자형 타입의 default 값은 ZERO입니다. |
| 5   | justify      | Justify     | N    | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다. @StringValue와 같은 문자열 타입의 default 값은 LEFT고, 다른 숫자형 타입의 default 값은 RIGHT입니다.   |

##### 3.1.1.1 order 속성

ft4j는 marshall/unmarshall 대상이 되는 객체 필드들의 순서를 order 속성으로 판단합니다. 객체의 필드 선언 순서는 영향을 끼치지 않습니다. OrderProp 클래스에 field1, field2 필드를 선언했습니다. 각 필드에 @StringValue 어노테이션을 선언하면서 order 속성 값을 아래와 같이 지정하면 field1에는 1234, field2에는 5678이 할당됩니다.

```java
class OrderProp {

    @StringValue(order = 1, length = 4)
    private String field1;

    @StringValue(order = 2, length = 4)
    private String field2;

    //getters
}
```

```java
@Test
void order_property() {
    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterTypeUTF_8).build();
    byte[] rawData = "12345678".getBytes();

    OrderProp orderProp = unMarshaller.unmarshall(rawData, OrderProp.class);
    assertEquals("1234", orderProp.getField1());
    assertEquals("5678", orderProp.getField2());
}
```

대신, 객체 필드의 선언 순서는 그대로 두고 field1과 field2의 order 속성 값을 바꾸면 field1에는 5678, field2에는 1234이 할당됩니다. 

```java
@StringValue(order = 2, length = 4)
private String field1;

@StringValue(order = 1, length = 4)
private String field2;
```

```java
@Test
void order_property() {
    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterTypeUTF_8).build();
    byte[] rawData = "12345678".getBytes();

    OrderProp orderProp = unMarshaller.unmarshall(rawData, OrderProp.class);
    assertEquals("5678", orderProp.getField1());
    assertEquals("1234", orderProp.getField2());
}
```

##### 3.1.1.2 length 속성

객체를 marshalling 할 때, length 속성 값에 맞춰 바이트 배열의 크기를 결정합니다. 반대로 unmarshalling 할 때에는 바이트 배열에서 length 속성 값에 지정된 값 만큼을 읽어들여 객체 필드 타입에 맞게 unmarshalling 합니다.

```java
class Person {
    @StringValue(order = 1, length = 10)
    private String name;

    //constructors, getter
}
```

Person 객체의 name 필드에 "John" 이라는 4바이트 문자열을 저장하고 marshalling을 수행하면, length 속성 값인 10만큼의 바이트 배열을 리턴합니다.

```java
@Test
void length_marshall_test() {

    MarshallManager marshaller = MarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] expected = "John      ".getBytes(Charset.forName("utf-8"));
    Person person = new Person("John");
    byte[] result = marshaller.marshall(person);

    assertArrayEquals(expected, result);
    assertEquals(10, result.length);
}
```

공백을 포함한 길이가 10인 "John      " 문자열을 바이트 배열로 변환하고 unmarshalling을 수행하면 Person 객체의 name 필드 값에 "John"이 저장된 것을 확인할 수 있습니다.

```java
@Test
void length_unmarshall_test() {

    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] input = "John      ".getBytes(Charset.forName("utf-8"));
    assertEquals(10, input.length);

    Person person = unMarshaller.unmarshall(input, Person.class);
    assertEquals("John", person.getName());
}
```

> Marshalling을 할 때에는 필드 값으로 바이트 배열을  채우고 난 뒤 비어있는 나머지 공간은 공백으로 채워지고, Unmarshalling을 할 때에는 공백을 모두 지우고 필드에 John 이라는 문자열만 저장됐습니다. 이 부분은 아래 paddingByte 속성 값에 대한 설명을 참고하세요.

##### 3.1.1.3 defaultValue 속성

객체의 필드 값을 설정하지 않아도 defaultValue 속성 값으로 대체하여 marshalling을 합니다. 반면, Unmarshalling을 할 때에는 defaultValue 속성 값보다 바이트 배열의 값이 우선합니다.

```java
class Person {
    @StringValue(order = 1, length = 10, defaultValue = "Peter")
    private String name;

    //constructors, getter
}
```

Person 객체를 생성할 때, name 필드에 어떤 값도 세팅하지 않더라도 marshalling한 결과인 바이트 배열에는 defaultValue 속성 값으로 설정한 "Peter" 문자열이 반영됩니다.

```java
@Test
void defaultValue_marshall_test() {

    MarshallManager marshaller = MarshallFactory.builder().converter(ConverterType.UTF_8).build();

    Person person = new Person();
    byte[] expected = "Peter     ".getBytes(Charset.forName("utf-8"));
    byte[] result = marshaller.marshall(person);

    assertArrayEquals(expected, result);
    assertEquals(10, result.length);
}
```

반면, Unmarshalling을 할 때에는 defaultValue 속성 값을 설정하더라도 바이트 배열의 값이 우선하여 "John" 문자열이 Person 객체의 name 필드에 세팅됩니다.

```java
@Test
void defaultValue_unmarshall_test() {

    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] input = "John      ".getBytes(Charset.forName("utf-8"));

    assertEquals(10, input.length);
    Person person = unMarshaller.unmarshall(input, Person.class);
    assertEquals("John", person.getName());
}
```

##### 3.1.1.4 paddingByte 속성

length 속성과 밀접한 연관이 있는 속성입니다. marshalling할 때 우선 length 속성만큼의 길이를 갖는 바이트 배열을 생성하고 필드 값을 할당합니다. 만약 필드 값을 모두 할당하고도 바이트 배열에 비어있는 공간이 있다면 paddingByte 속성 값으로 채우게 됩니다. 반대로 unmarshalling할 때, 바이트 배열의 paddingByte 속성 값에 해당하는 값은 제외하고 객체의 필드에 값을 할당합니다. @StringValue처럼 문자열 타입의 어노테이션은 default 값이 PaddingByte.SPACE이며 숫자 타입의 어노테이션은 default 값이 PaddingByte.ZERO입니다. 아래 @StringValue와 @IntValue 어노테이션 소스 코드를 보면 PaddingByte의 default 값을 확인할 수 있습니다.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringValue {

    int order();

    int length();

    String defaultValue() default "";

    PaddingByte paddingByte() default PaddingByte.SPACE;

    Justify justify() default Justify.LEFT;
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntValue {

    int order();

    int length();

    String defaultValue() default "";

    PaddingByte paddingByte() default PaddingByte.ZERO;

    Justify justify() default Justify.RIGHT;

    boolean ignoreLimit() default false;
}
```

`3.1.1.2 length 속성`에서 살펴봤던 @StringValue 테스트 코드에서 marshalling할 때 바이트 배열에 공백이 추가되고, unmarshalling 할 때 공백이 제거된 채로 객체의 필드에 값이 세팅된 이유가 바로 이 속성 때문입니다. 만약 paddingByte속성을 ZERO로 설정하면 어떻게 되는지 테스트해보겠습니다.

```java
class Person {
    @StringValue(order = 1, length = 10, paddingByte = PaddingByte.ZERO)
    private String name;

    //constructors, getter
}
```

```java
@Test
void length_marshall_test() {

    MarshallManager marshaller = MarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] expected = "John000000".getBytes(Charset.forName("utf-8")); 

    //바이트 배열에 John이라는 문자열을 채우고 나머지 빈 부분은 ZERO로 채움
       Person person = new Person("John");
    byte[] result = marshaller.marshall(person);

    assertArrayEquals(expected, result);
    assertEquals(10, result.length);
}
```

```java
@Test
void length_unmarshall_test() {

    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.UTF_8).build();
    byte[] input = "John      ".getBytes(Charset.forName("utf-8"));

    assertEquals(10, input.length);
    Person person = unMarshaller.unmarshall(input, Person.class);

    //객체 필드에 값을 세팅할 때, paddingByte값을 제거하고 할당하는데 현재 paddingByte 속성 값이 ZERO이므로
    //name 필드에 "John"이 아닌 "John      "이 할당됨
    assertEquals("John      ", person.getName());

    //input 바이트 배열에 빈 공간을 ZERO 채우고 unmarshalling을 수행하면 현재 paddingByte 속성 값이 ZERO이므로
    //name 필드에 ZERO를 제거한 "John" 문자열이 할당됨
    input = "John000000".getBytes(Charset.forName("utf-8"));
    person = unMarshaller.unmarshall(input, Person.class);
    assertEquals("John", person.getName());
}
```

> 금융권에서 일반적으로 사용하는 전문 방식은 문자열은 SPACE로 padding하고 왼쪽 정렬, 숫자형은 ZERO로 padding하고 오른쪽 정렬을 합니다. 정렬과 관련된 부분은 바로 아래 justify 속성에서 살펴봅니다. 

##### 3.1.1.5 justify 속성

marshalling할 때 객체의 필드 값을 바이트 배열의 어느 방향부터 채울지 결정하는 속성입니다.  unmarshalling 할 때는 바이트 배열의 어느 방향부터 값을 읽어 필드에 세팅할지 결정합니다. @StringValue는 default 값이 Justify.LEFT이며 숫자 타입의 어노테이션은 default 값이 Justify.RIGHT입니다.

```java
class Person {
    @StringValue(order = 1, length = 10, justify = Justify.RIGHT)
    private String name;

    //constructors, getter
}
```

```java
@Test
void justify_marshall_test() {
    MarshallManager marshaller = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
    Person person = new Person("John");

    byte[] expected = "      John".getBytes(Charset.forName("utf-8"));
    byte[] result = marshaller.marshall(person);

    assertArrayEquals(expected, result);
    assertEquals(10, result.length);
}
```

아래 예제는 중요한데, unmarshalling을 할 때는 paddingByte 속성과 밀접한 연관이 있습니다. Justify가 RIGHT로 설정되어있으면 반대 방향인 왼쪽부터 paddingByte에 속성 값에 해당하는 바이트가 아닌 값이 나올 때 까지 오프셋을 증가시킵니다. unmarshall 메소드의 일부를 보면 justify 값에 따라 오프셋 인덱스를 나타내는 start, 마지막 인덱스를 나타내는 end를 조정하는 코드가 있습니다.

```java
@Override
public int unmarshall(AnnotationFieldProperty property, int offset, byte[] bytes) {
    int limit = property.length();
    int start = offset;
    int end = offset + limit - 1;

    PaddingByte paddingByte = property.padding();
    Justify justify = property.justify();

    if (end >= bytes.length) {
        throw new FieldValueUnderFlowException("During unmarshalling, Field value bytes underflow.");
    }

    if (justify == Justify.LEFT) {

        //Justify.LEFT인 경우에는 오른쪽부터 시작하여 paddingByte에 해당하는 인덱스는 건너뜀
        while (start <= end && paddingByte.getValue() == bytes[end]) {
            end--;
        }
    } else if (justify == Justify.RIGHT) {

        //Justify.RIGHT인 경우에는 왼쪽부터 시작하여 paddingByte에 해당하는 인덱스는 건너뜀
        while (start <= end && paddingByte.getValue() == bytes[start]) {
            start++;
        }
    }
    //.....
}
```

아래 테스트 코드를 보면 Justify 속성 값이 RIGHT이고, paddingByte는 SPACE이므로 "      John"이라는 문자열에서 처음으로 SPACE가 아닌 J 문자가 나오는 인덱스부터 마지막 인덱스까지의 값을 객체 필드에 저장하게됩니다.

```java
@Test
void justify_unmarshall_test() {
    UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.UTF_8).build();

    byte[] input = "      John".getBytes(Charset.forName("utf-8"));
    assertEquals(10, input.length);

    Person person = unMarshaller.unmarshall(input, Person.class);
    assertEquals("John", person.getName());
}
```

---

#### 3.1.1 @StringValue

String 타입에 선언할 수 있는 어노테이션입니다. 주요 속성 값은 아래와 같습니다.

| 번호  | 속성명          | 타입          | 필수  | 기본 값              | 설명                                                        |
| --- | ------------ | ----------- | --- | ----------------- | --------------------------------------------------------- |
| 1   | order        | int         | Y   |                   | 필드들의 marshall/unmarshall 순서를 지정합니다.                       |
| 2   | length       | int         | Y   |                   | 해당 필드의 바이트 수를 지정합니다.                                      |
| 3   | defaultValue | String      | N   | Empty String      | marshall 연산시, 현재 필드 값에 상관없이 해당 값으로 대체합니다.                 |
| 4   | paddingByte  | PaddingByte | N   | PaddingByte.SPACE | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 나머지 바이트를 채울 값을 지정합니다. |
| 5   | justify      | Justify     | N   | Justify.LEFT      | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다.   |

##### 3.1.1.1 length

String 타입의 name 필드를 갖는 User 클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 User 객체를 아래와 같이 생성합니다.

```java
class User {
    private String name;
    //constructor, getter, setter
}
```

```java
public class StringValueTest {

    private MarshallManager marshallManager;
    private User user;
    private Charset charset;

    @BeforeEach
    void setUp() {
        marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
        user = new User("John");
        charset = Charset.forName("utf-8");
    }

     // @Test Method......   
}
```

@StringValue 어노테이션의 length 값에 15를 설정한 뒤, User 객체의 필드 값을 "John"으로 설정하고 마샬링하면 총 15바이트 크기의 문자열이 생성됩니다. 다른 속성 값들은 모두 기본 값을 그대로 사용하기 때문에 필드 값은 왼쪽 정렬되고 나머지 바이트는 SPACE로 채워집니다.

```java
@StringValue(order = 1, length = 15)
private String name;
```

```java
@Test
void length_test() {

    byte[] expected = "John           ".getBytes(charset);
    byte[] result = marshallManager.marshall(user);

    assertEquals(15, result.length);
    assertArrayEquals(expected, result);
}
```

만약, 필드 값이 length의 길이보다 더 클 경우에는 필드 값을 length 값에 맞춰 자른 후 마샬링합니다. "John"은 4바이트인 반면, length의 값은 3이기 때문에 결과는 "Joh"입니다.

```java
@StringValue(order = 1, length = 3)
private String name;
```

```java
@Test
void exceed_length_test() {
    byte[] expected = "Joh".getBytes(charset);
    byte[] result = marshallManager.marshall(user);

    assertEquals(3, result.length);
    assertArrayEquals(expected, result);
}
```

##### 3.1.1.2 defaultValue

defaultValue 속성 값을 "my name"로 설정하면, name 필드에 기존에 설정된 "John" 대신 "my name" 값으로 마샬링을 합니다.

```java
@StringValue(order = 1, length = 15, defaultValue="my name")
private String name;
```

```java
@Test
void default_value_test() {
    byte[] expected = "my name        ".getBytes(charset);
    byte[] result = marshallManager.marshall(user);

    assertEquals(15, result.length);
    assertArrayEquals(expected, result);
}
```

##### 3.1.1.3 paddingByte

paddingByte 속성 값의 기본 값은 SPACE이지만 해당 속성 값을 ZERO로 지정하면 나머지 바이트를 해당 값으로 채웁니다.

```java
@StringValue(order = 1, length = 15, paddingByte = PaddingByte.ZERO)
private String name;
```

```java
@Test
void padding_test() {
    byte[] expected = "John00000000000".getBytes(charset);
    byte[] result = marshallManager.marshall(user);

    assertEquals(15, result.length);
    assertArrayEquals(expected, result);
}
```

##### 3.1.1.4 justify

justify 속성 값의 기본 값은 LEFT 이지만 해당 속성 값을 RIGHT로 지정하면 필드 값이 오른쪽 정렬된 것을 확인할 수 있습니다.

```java
@StringValue(length = 15, justify = Justify.RIGHT)
private String name;
```

```java
@Test
void justify_right_test() {
    byte[] expected = "           John".getBytes(charset);
    byte[] result = marshallManager.marshall(user);

    assertEquals(15, result.length);
    assertArrayEquals(expected, result);
}
```

#### 3.1.2 @ShortValue, @IntValue, @LongValue

객체의 short, int, long 타입의 필드에 선언할 수 있는 어노테이션입니다. 주요 속성 값은 아래와 같습니다. 세 어노테이션의 사용법은 모두 같습니다.

| 번호  | 속성명          | 타입          | 필수  | 기본 값             | 설명                                                                                     |
| --- | ------------ | ----------- | --- | ---------------- | -------------------------------------------------------------------------------------- |
| 1   | order        | int         | Y   |                  | 필드들의 marshall/unmarshall 순서를 지정합니다.                                                    |
| 2   | length       | int         | Y   |                  | 해당 필드의 바이트 수를 지정합니다.                                                                   |
| 3   | defaultValue | String      | N   | Empty String     | marshall 연산시, 현재 필드 값에 상관없이 해당 값으로 대체합니다.                                              |
| 4   | paddingByte  | PaddingByte | N   | PaddingByte.ZERO | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 나머지 바이트를 채울 값을 지정합니다.                              |
| 5   | justify      | Justify     | N   | Justify.RIGHT    | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다.                                |
| 6   | ignoreLimit  | boolean     | N   | false            | length 속성 값이 필드 값의 바이트 수보다 더 작을 때, 예외를 발생시킬지 여부를 결정합니다. 기본 값은 false이며, 이 경우 예외가 발생합니다. |

> String 타입의 필드를 처리하는 @StringValue 어노테이션은 필드 값이 length 속성 값보다 더 작을 때, 예외를 발생시키지 않고 length 속성 값에 맞춰 바이트를 잘라서 문자열을 생성했습니다. 그러나 정수형 필드에 선언될 수 있는 어노테이션들은 length 길이에 맞춰 필드 값을 자르게되면 기존 필드 값과는 전혀 다른 의미의 값을 나타내게 됩니다.
> 
> 예를 들어, int형 타입의 필드 값에 100이라는 값이 설정되었고 위 테스트처럼 length 속성 값을 '2'로 설정했을 때를 생각해보겠습니다. length 속성 값에 맞춰 바이트를 자르게되면 100은 10이라는 값으로 변환될 것입니다. 이는 의도하지 않은 심각한 오류를 발생시킬 수 있으므로 @IntValue 어노테이션에는 이러한 경우에 기본적으로 예외를 발생시키도록 하였습니다.
> 
> 실생활의 예를 들어보면, 계좌이체를 하는 경우 입금한 사람의 이름이 잘리는 것과 의도치않게 입금 금액이 잘리는 경우, 둘 중 어떠한 상황이 더 심각한 오류가 발생한 상황일까요?

##### 3.1.2.1 length

int 타입의 price 필드를 갖는 Product 클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 Product 객체를 아래와 같이 생성합니다.

```java
class Product {
    private int price;
    //constructor, getter, setter
}
```

```java
public class IntValueTest {

    private MarshallManager marshallManager;
    private Product product;
    private Charset charset;

    @BeforeEach
    void setUp() {
        marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
        product = new Product(100);
        charset = Charset.forName("utf-8");
    }

     // @Test Method......   
}
```

@IntValue어노테이션의 length 값에 10를 설정한 뒤, Price 객체의 필드 값을 100으로 설정하고 마샬링하면 총 10바이트 크기의 문자열이 생성됩니다. 다른 속성 값들은 모두 기본 값으로 설정했기 때문에 필드 값은 오른쪽 정렬되고 나머지 바이트는 ZERO로 채워집니다.

```java
@IntValue(order = 1, length = 10)
private int price;
```

```java
@Test
void length_test() {
    byte[] expected = "0000000100".getBytes(charset);
    byte[] result = marshallManager.marshall(product);

    assertEquals(10, result.length);
    assertArrayEquals(expected, result);
}
```

만약, 필드 값이 length의 길이보다 더 클 경우에는 **FieldValueExceedsLimitException** 예외가 발생합니다.

```java
@IntValue(order = 1, length = 2)
private String name;
```

```java
@Test
void field_value_exceed_length() {
    assertThrows(FieldValueExceedsLimitException.class, () -> marshallManager.marshall(product));
}
```

##### 3.1.2.2 ignoreLimit

ignoreLimit을  true로 설정하면 예외가 발생하지 않고 필드 값의 길이를 어노테이션의 속성 값에 맞춰 자릅니다.

```java
@IntValue(length = 2, ignoreLimit = true)
private String name;
```

```java
@Test
void ignore_limit_test() {
    byte[] expected = "10".getBytes(charset);
    byte[] result = marshallManager.marshall(product);
    assertEquals(2, result.length);
    assertArrayEquals(expected, result);
}
```

##### 3.1.2.3 defaultValue

defaultValue 속성 값을 "200"로 설정하면, price 필드에 기존에 설정된 100 대신 "200" 값으로 마샬링을 합니다.

```java
@IntValue(order = 1, length = 10, defaultValue = "200")
private int price;
```

```java
@Test
void default_value_test() {
    byte[] expected = "0000000200".getBytes(charset);
    byte[] result = marshallManager.marshall(product);

    assertEquals(10, result.length);
    assertArrayEquals(expected, result);
}
```

만약, defaultValue 속성 값이 int 타입으로 변환할 수 없는 경우에는 **NumberFormatException** 예외가 발생합니다.

```java
@IntValue(order = 1, length = 10, defaultValue = "ABC")
private String name;
```

```java
@Test
void convert_exception() {
    assertThrows(NumberFormatException.class, ()->marshallManager.marshall(product));
}
```

##### 3.1.2.4 paddingByte

paddingByte 속성 값의 기본 값은 ZERO이지만 해당 속성 값을 SPACE로 지정하면 나머지 바이트를 해당 값으로 채웁니다.

```java
@IntValue(order = 1, length = 10, paddingByte = PaddingByte.SPACE)
private int price;
```

```java
@Test
void padding_test() {
    byte[] expected = "       100".getBytes(charset);
    byte[] result = marshallManager.marshall(product);

    assertEquals(10, result.length);
    assertArrayEquals(expected, result);
}
```

##### 3.1.2.5 justify

justify 속성 값의 기본 값은 RIGHT이지만 해당 속성 값을 LEFT로 지정하면 필드 값이 왼쪽 정렬된 것을 확인할 수 있습니다. 

```java
@IntValue(order = 1, length = 10, justify = Justify.LEFT)
private int price;
```

```java
@Test
void justify_left_test() {
    String result = marshaller.marshall(product);
    assertEquals("1000000000", result);
    assertEquals(10, result.getBytes().length);
}
```

단, 숫자형을 왼쪽 정렬하고 PaddingByte 속성 값을 ZERO로 두면 필드 값의 의미가 완전 달라진다는 것을 주의하세요. 왼쪽 정렬과 paddingByte를 SPACE로 설정하여 의도하지 않은 값을 생성하지 않도록 하세요.

```java
@IntValue(order = 1, length = 10, justify = Justify.LEFT, paddingByte = PaddingByte.SPACE)
private int price;
```

```java
@Test
void justify_left_space_padding_test() {
    byte[] expected = "100       ".getBytes(charset);
    byte[] result = marshallManager.marshall(product);

    assertEquals(10, result.length);
    assertArrayEquals(expected, result);
}
```

#### 3.1.3 @FloatValue, @DoubleValue

객체의 float, double 타입의 필드에 선언할 수 있는 어노테이션입니다. 주요 속성 값은 아래와 같습니다. 두 어노테이션의 사용법은 모두 같습니다.

| 번호  | 속성명              | 타입          | 필수  | 기본 값             | 설명                                                                                                                                                                                                             |
| --- | ---------------- | ----------- | --- | ---------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | order            | int         | Y   |                  | 필드들의 marshall/unmarshall 순서를 지정합니다.                                                                                                                                                                            |
| 2   | length           | int         | Y   |                  | 해당 필드의 바이트 수를 지정합니다.                                                                                                                                                                                           |
| 3   | defaultValue     | String      | N   | Empty String     | marshall 연산시, 현재 필드 값에 상관없이 해당 값으로 대체합니다.                                                                                                                                                                      |
| 4   | paddingByte      | PaddingByte | N   | PaddingByte.ZERO | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 나머지 바이트를 채울 값을 지정합니다.                                                                                                                                                      |
| 5   | justify          | Justify     | N   | Justify.RIGHT    | length 속성 값이 필드에 저장된 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다.                                                                                                                                                        |
| 6   | ignoreLimit      | boolean     | N   | false            | length 속성 값이 필드 값의 바이트 수보다 더 작을 때, 예외를 발생시킬지 여부를 결정합니다. 기본 값은 false이며, 이 경우 예외가 발생합니다.                                                                                                                         |
| 7   | fractionalLength | int         | N   | -1               | 소수점 이하를 표시합니다. 기본 값은 -1이며 소수점 자리수를 그대로 유지합니다. 0은 소수점 자리수를 표시하지 않습니다. 양수는 그 값만큼의 소수점 자리수를 유지합니다. 해당 속성 값으로 인해 확장되는 소수점 자리수와 정수부, 그리고 '.'을 포함하여 length 속성 값보다 작거나 같아야 합니다. 단, ignoreLimit을 true로 설정할 경우 주의해야합니다. |

##### 3.1.3.1 length

double 타입의 rate필드를 갖는 Bank클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 Bank객체를 아래와 같이 생성합니다. double의 경우 바이트 배열로 직접 비교하기 어렵기 때문에 테스트를 도와줄 convert 유틸 메소드를 선언합니다.

```java
class Bank {
    private double rate;
    //constructor, getter, setter
}
```

```java
public class DecimalTest {

    private Marshaller marshaller;
    private Bank bank;

    @BeforeEach
    void setUp() {
        marshaller = MarshallerFactory.builder().encoder(EncoderType.UTF_8).build();
        bank = new Bank(135.8345);
    }

    //테스트에 사용될 util 메소드
       public static String convert(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    // @Test Method......
}
```

@DoubleValue어노테이션의 length 값에 10를 설정한 뒤, bank객체의 필드 값을 135.8345으로 설정하고 마샬링하면 총 10바이트 크기의 문자열이 생성됩니다. 다른 속성 값들은 모두 기본 값으로 설정했기 때문에 필드 값은 오른쪽 정렬되고 나머지 바이트는 ZERO로 채워집니다. fractionalLength의 기본 값은 -1이므로 최초 할당된 값의 소수점 이하의 모든 소수를 표현합니다.

```java
@DoubleValue(order = 1, length = 10)
private double rate;
```

```java
@Test
void length_test() {
    String expected = "00135.8345"; // '.'도 length에 포함됩니다.
    byte[] result = marshallManager.marshall(bank);

    assertEquals(10, result.length);
    assertEquals(expected, convert(result, charset));
}
```

##### 3.1.3.2 ignoreLimit

정수 타입의 어노테이션과 마찬가지로 필드 값이 length의 길이보다 더 클 경우에는 **FieldValueExceedsLimitException** 예외가 발생합니다.  ignoreLimit을  true로 설정하면 예외가 발생하지 않고 필드 값의 길이를 어노테이션의 속성 값에 맞춰 자릅니다. 이때, length 속성 값을 맞추기 위해 소수점 이하 값들 부터 버립니다.

```java
@DoubleValue(order = 1, length = 5, ignoreLimit = true)
private double rate;
```

```java
@Test
void ignore_limit_test() {
    String expected = "135.8";
    byte[] result = marshallManager.marshall(bank);

    assertEquals(5, result.length);
    assertEquals(expected, convert(result, charset));
}
```

##### 3.1.3.3 fractionalLength

몇 번째 소수점까지 나타낼지 결정하는 속성입니다. 기본 값은 -1이며 최초 값의 소수점을 모두 표현합니다. 양수일 경우, 해당 값 만큼의 소수점 자리수를 표현하며 0인 경우에는 소수점을 표현하지 않습니다. 135.8345 값을 소수점 셋째 자리까지 표현하기 위해 fractionalLength 값을 3으로 설정합니다. 해당 속성 값 이하의 소수점은 버려집니다.

```java
@DoubleValue(order = 1, length = 10, fractionalLength = 3)
private double rate;
```

```java
@Test
void fractionalLength_test() {
    String expected = "000135.834";
    byte[] result = marshallManager.marshall(bank);

    assertEquals(10, result.length);
    assertEquals(expected, convert(result, charset));        
}
```

fractionalLength 속성 값을 0으로 설정한 경우 정수부만 표현합니다. 마찬가지로 소수점 이하는 모두 버려집니다.

```java
@DoubleValue(order = 1, length = 10, fractionalLength = 0)
private double rate;
```

```java
@Test
void fractionalLength_test() {
    String expected = "0000000135";
    byte[] result = marshallManager.marshall(bank);

    assertEquals(10, result.length);
    assertEquals(expected, convert(result, charset));
}
```

만약, 필드 값이 length의 길이보다 더 클 경우에는 **FieldValueExceedsLimitException** 예외가 발생합니다. 최초 '135.8345' 값은 정수부가 세 자리, 소수부가 네 자리이고, '.'을 포함하여 길이가 총 8이었습니다. 이때, fractionalLength 속성 값을 8로 설정하면 정수부와 소수점의 길이의 합이 이미 4이므로 소수부 길이를 8로 늘리게되면 length 속성 값을 초과하므로 예외가 발생합니다.

```java
@DoubleValue(order = 1, length = 10, fractionalLength = 8)
private double rate;
```

```java
@Test
void field_value_exceed_length() {
    assertThrows(FieldValueExceedsLimitException.class, () -> marshallManager.marshall(bank));
}
```

##### 3.1.3.4 defaultValue

defaultValue 속성 값을 "100.3"로 설정하면, rate필드에 기존에 설정된 135.8345대신 "100.3" 값으로 마샬링을 합니다.

```java
@DoubleValue(order = 1, length = 10, defaultValue = "100.3")
private double rate;
```

```java
@Test
void default_value_test() {
    String expected = "00000100.3";
    byte[] result = marshallManager.marshall(bank);

    assertEquals(10, result.length);
    assertEquals(expected, convert(result, charset));
}
```

만약, defaultValue 속성 값이 double 타입으로 변환할 수 없는 경우에는 **NumberFormatException** 예외가 발생합니다.

```java
@DoubleValue(order = 1, length = 10, defaultValue = "ABC")
private double rate;
```

```java
@Test
void convert_exception() {
    assertThrows(NumberFormatException.class, ()->marshallManager.marshall(bank));
}
```

defaultValue의 값이 length 속성 값을 초과하면 **DefaultValueExceedsLimitException**예외가 발생합니다.

```java
@DoubleValue(order = 1, length = 10, defaultValue = "123456.7891")
private double rate;
```

```java
@Test
void defaultValue_exceeds_length() {
    assertThrows(DefaultValueExceedsLimitException.class, ()->marshallManager.marshall(bank));
}
```

> 단, 마지막 소수점이 0인 경우 모두 무시되므로 defaultValue의 길이가 length 속성 값을 초과하더라도 해당 예외가 발생하지 않습니다. 이 부분은 의도와 다르게 동작하는 부분입니다. defaultValue 속성 값을 '1234.0000'로 설정하더라도 Java의 소수점 연산때문에 소수점이 모두 무시되는 문제를 인지하고 있습니다. 해당 부분은 다음 버전에서 수정되어야합니다.

##### 3.1.3.5 paddingByte

paddingByte 속성 값의 기본 값은 ZERO입니다. 나머지 바이트가 0으로 채워진 것을 확인할 수 있습니다.

```java
@DoubleValue(order = 1, length = 10)
private double rate;
```

```java
@Test
void paddingbyte_test() {
    String result = marshaller.marshall(bank);
    assertEquals("00000135.8", result);
    assertEquals(10, result.getBytes().length);
}
```

paddingByte 속성 값을 SPACE로 지정하면 나머지 바이트를 해당 값으로 채웁니다.

```java
@Decimal(length = 10, paddingByte = PaddingByte.SPACE)
private double rate;
```

```java
@Test
void padding_test() {
    String result = marshaller.marshall(bank);
    assertEquals("     135.8", result);
    assertEquals(10, result.getBytes().length);
}
```

##### 3.1.3.6 justify

justify 속성 값의 기본 값은 RIGHT이지만 해당 속성 값을 LEFT로 지정하면 필드 값이 왼쪽 정렬된 것을 확인할 수 있습니다. 

```java
@DoubleValue(order = 1, length = 10, justify = Justify.LEFT)
private double rate;
```

```java
@Test
void justify_left_test() {
    String expected = "135.834500";
    byte[] result = marshallManager.marshall(bank);

    assertEquals(10, result.length);
    assertEquals(expected, convert(result, charset));
}
```

단, 숫자형을 왼쪽 정렬하고 PaddingByte 속성 값을 ZERO로 두면 필드 값의 의미가 완전 달라진다는 것을 주의하세요. 왼쪽 정렬과 paddingByte를 SPACE로 설정하여 의도하지 않은 값을 생성하지 않도록 하세요.

```java
@DoubleValue(order = 1, length = 10, justify = Justify.LEFT, paddingByte = PaddingByte.SPACE)
private double rate;
```

```java
@Test
void justify_left_space_padding_test() {
    String expected = "135.8345  ";
    byte[] result = marshallManager.marshall(bank);

    assertEquals(10, result.length);
    assertEquals(expected, convert(result, charset));
}
```

#### 3.1.4 @GeneratedValue

객체의 default 값을 생성하는 Generator를 지정할 때 사용합니다. 주요 속성 값은 아래와 같습니다.

| 속성명       | 타입        | 필수  | 기본 값          | 설명                                                            |
| --------- | --------- | --- | ------------- | ------------------------------------------------------------- |
| generator | Generator | N   | NoopGenerator | Generator 인터페이스의 구현체를 지정합니다. 해당 구현 클래스가 생성하는 값으로 필드 값을 대체합니다. |
| cacheable | boolean   | N   | false         | 캐시 적용 여부를 나타냅니다. 기본 값은 false입니다.                              |
| key       | String    | N   | -             | 캐시 적용 여부가 true일 때, key 값으로 사용될 값을 지정합니다.                      |

@GeneratedValue는 defaultValue 속성으로 지정하기 힘든 복잡한 로직을 수행할 수 있습니다. generator 속성에 Generator 타입의 클래스를 지정하면 해당 클래스가 반환하는 문자열로 필드 값을 대체합니다.

##### 3.1.4.1 generator

```java
public interface Generator {
    String generate();
}
```

예를 들어, 현재 시간을 기본 값으로 사용하고 싶은 경우 아래와 같은 커스텀 클래스를 정의할 수 있습니다.

```java
public class FullDateTimeGenerator implements Generator {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Override
    public String generate() {
        return LocalDateTime.now().format(formatter);
    }

}
```

String 타입의 paymentDate필드를 갖는 Payment클래스를 아래와 같이 작성합니다. 

```java
class Payment {
    private String paymentDate;
    //constructor, getter, setter
}
```

```java
public class GeneratorTest {

    private Marshaller marshaller;
    private Payment payment;
    private Charset charset;

    @BeforeEach
    void setUp() {
        marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
        payment = new Payment();
        charset = Charset.forName("utf-8");
    }

     // @Test Method......   
}
```

generator 속성 값에 위에서 생성한 FullDateTimeGenerator 클래스를 지정하면 필드 값을 현재 시간으로 대체합니다.

```java
@GeneratedValue(generator = FullDateTimeGenerator.class)
@StringValue(order = 1, length = 17)
private String paymentDate;
```

```java
@Test
void generator_test() {
    byte[] result = marshallManager.marshall(payment);
    System.out.println("실행 결과 : " + new String(result, charset));
}
```

```shell
실행 결과 : 20230919011448965
```

@StringValue 어노테이션의 length 속성 값보다 generator가 생성한 문자열의 길이가 더 클 경우, GeneratedValueExceedsLimitException 예외가 발생합니다.  FullDateTimeGenerator의 generate() 메소드는 길이가 17인 문자열을 리턴하는 반면에, 길이를 16으로 변경한 뒤 테스트를 실행합니다.

```java
@GeneratedValue(generator = FullDateTimeGenerator.class)
@StringValue(order = 1, length = 16)
private String paymentDate;
```

```java
@Test
void generated_value_exceeds_limit_test() {
    assertThrows(GeneratedValueExceedsLimitException.class, () -> marshallManager.marshall(payment));
}
```

##### 3.1.4.2 key, cacheable

generator 속성에 지정된 클래스가 생성하는 문자열을 캐싱해두고 다른 필드에서도 캐싱된 값을 사용할 때 사용되는 속성입니다. cacheable 속성을 true로 설정하면, key 속성으로 전달된 값으로 캐시에서 데이터를 조회하고 그 값으로 필드 값을 대체합니다.

UUID를 생성하는 Generator 클래스를 작성합니다.

```java
public class UUIDGenerator implements Generator {

    @Override
    public String generate() {
        return LocalDateTime.now().format(formatter);
    }
}
```

Payment 클래스를 아래와 같이 수정합니다. 두 UUID를 구별하기 위해 중간에 space 필드를 추가했습니다.
uuid 필드는 generator 속성 값에 UUIDGenerator 클래스를 지정했습니다. 그리고 "id"라는 문자열로 캐싱하기 위해 cacheable을 true로 설정합니다. cachedId는 uuid필드에서 지정한 UUIDGenerator 클래스가 생성한 캐싱된 문자열을 사용할 것이므로 generator 속성 값을 지정하지 않아도 됩니다.

```java
@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
@StringValue(order = 1, length = 36)
private String uuid;

@StringValue(order = 2, length = 5)
private String space;

@GeneratedValue(key = "id", cacheable = true)
@StringValue(order = 3, length = 36)
private String cachedId;
```

```java
@Test
void cacheable_test() {
    byte[] result = marshallManager.marshall(payment);
    System.out.println("실행 결과 : " + new String(result, charset));
}
```

같은 uuid가 출력된 것을 확인할 수 있습니다.

```shell
실행 결과 : 0820f0b6-a9ce-494f-be2c-6727d95d6032     0820f0b6-a9ce-494f-be2c-6727d95d6032
```

notCachedId 필드에 cacheable 속성을 추가하지 않으면, uuid 필드와 다른 UUID가 생성되는 것을 확인할 수 있습니다.

```java
@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
@StringValue(order = 1, length = 36)
private String uuid;

@StringValue(order = 2, length = 5)
private String space;

@GeneratedValue(generator = UUIDGenerator.class)
@StringValue(order = 3, length = 36)
private String notCachedId;
```

```java
@Test
void not_cacheable_test() {
    byte[] result = marshallManager.marshall(payment);
    System.out.println("실행 결과 : " + new String(result, charset));
}
```

```shell
실행 결과 : 5404f460-246b-46bc-9f9c-d8f92608c628     1445454a-d13f-4528-aa0c-90a755a8ccba
```

@GeneratedValue는 cacheable 속성이 true일 때, 기본적으로 key 속성 값에 해당하는 키가 캐시에 존재하는지 확인합니다. 키가 존재하지 않는다면 generator 속성 값에 전달된 클래스를 이용해서 값을 생성하고, 해당 키-값 쌍을 캐시에 저장합니다. 만약, 캐시에 존재하지 않는 키를 key 속성 값으로 지정하면서 generator 속성을 지정하지 않는다면  **CacheKeyNotFoundException**예외가 발생합니다.

```java
// "id"에 해당하는 키가 존재하지 않으므로, 새로 생성해야하는데 generator 속성 값도 설정되지 않아 예외가 발생
@GeneratedValue(key = "id", cacheable = true)
@StringValue(length = 36)
private String uuid;

@StringValue(length = 5)
private String space;

@GeneratedValue(key = "id", cacheable = true)
@StringValue(length = 36)
private String cachedId;
```

```java
@Test
void key_not_found_test() {
    assertThrows(CacheKeyNotFoundException.class, () -> marshallManager.marshall(payment));
}
```

cachedId 필드처럼 cacheable 속성을 true로 설정하고 key 속성 값을 지정하지 않을 경우, **MissingCacheKeyException** 예외가 발생합니다.

```java
@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
@StringValue(length = 36)
private String uuid;

@StringValue(length = 5)
private String space;

@GeneratedValue(cacheable = true)
@StringValue(length = 36)
private String cachedId;
```

```java
@Test
void missing_key_test() {
    assertThrows(MissingCacheKeyException.class, () -> marshallManager.marshall(payment));
}
```

@GeneratedValue는 캐시를 `ThreadLocal` 에 저장합니다. 캐시의 생명주기는 Marshaller의 marshall() 메소드와 같습니다.

```java
@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
@StringValue(order = 1, length = 36)
private String uuid;

@StringValue(order = 2, length = 5)
private String space;

@GeneratedValue(key = "id", cacheable = true)
@StringValue(order = 3, length = 36)
private String cachedId;
```

```java
@Test
void multithread_test() {

    for(int i = 0 ; i < 5; i++) {
        Runnable thread = () -> {
            byte[] result = marshallManager.marshall(payment);
            System.out.println("실행 결과 : " + new String(result, charset));
        };
        thread.run();
    }    
}
```

테스트 실행 결과를 보면 스레드끼리는 다른 UUID를 생성하는 것을 확인할 수 있습니다.

```shell
실행 결과 : 9c6875c4-191d-4b64-863e-b9fb205ad7c1     9c6875c4-191d-4b64-863e-b9fb205ad7c1
실행 결과 : 96bcd849-39f1-4266-aefe-da6c014dcaa6     96bcd849-39f1-4266-aefe-da6c014dcaa6
실행 결과 : 145770a3-660d-4684-937e-1edf08d6b6ca     145770a3-660d-4684-937e-1edf08d6b6ca
실행 결과 : 1d471c42-be9c-4b08-8c34-75cd7592188a     1d471c42-be9c-4b08-8c34-75cd7592188a
실행 결과 : fa455e86-9a0e-4491-a02f-05e81a2b251f     fa455e86-9a0e-4491-a02f-05e81a2b251f
```

### 3.2 인코딩

ft4j는 EUC-KR과 UTF-8 인코딩 방식을 지원합니다. ft4j는 바이트 길이 기반으로 동작하기 때문에 어떤 인코딩 방식을 사용하는지에 따라서 결과 값이 달라집니다. 예를 들어, 한글은 EUC-KR 인코딩 방식에서는 2바이트로 표현되며 UTF-8 인코딩 방식으로는 3바이트로 표현됩니다.

아래 예시에서 MarshallManager, UnMarshallManager를 생성할 때 ConverterType을 전달하는 것을 볼 수 있습니다.

```java
public enum ConverterType {
    EUC_KR, UTF_8
}
```

```java
MarshallManager marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();

UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.EUC_KR).build();
```

#### 3.2.1 Marshall 테스트

ft4j는 바이트 배열을 length 속성 값에 맞춰 marshall/unmarshall을 수행하므로 일련의 바이트로 표현된 원본 데이터가 온전하지 않을 수 있습니다. ft4j는 연산의 결과로 이렇게 불완전한 데이터가 발생할 경우, 데이터를 공백으로 치환하는 로직을 포함하고 있습니다.

String 타입의 name필드를 갖는 User클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체를 utf-8 인코딩을 사용하여 생성합니다.

```java
class User {
    private String name;
    //constructor, getter, setter
}

public class EncodingTest {

    private MarshallManager marshallManager;

    @BeforeEach
    void setUp() {
        marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
    }

    // @Test Method......
}
```

@StringValue 어노테이션에 length 속성 값을 4로 설정하고 name 필드에 길이가 5인 "Smith" 문자열을 할당했습니다. 이때 name 필드 값을 length 속성 값만큼 잘라 바이트 배열로 marshalling 했습니다.

```java
@StringValue(order = 1, length = 4)
private String name;
```

```java
@Test
void utf8_english_test() {

    User user = new User("Smith");
    byte[] result = marshallManager.marshall(user);    
    byte[] expected = "Smit".getBytes(Charset.forName("utf-8"));

    assertEquals(4, result.length);
    assertArrayEquals(expected, result);
}
```

name 필드에 한글 문자열 "스미스"를 할당하면 어떻게되는지 보겠습니다. utf-8 인코딩 방식에서 한글은 3바이트를 차지합니다. "스미스"의 첫 번째 글자 "스"에 해당하는 바이트에 온전히 3바이트를 할당할 수 있습니다. 그러나 두 번째 글자 "미"에 나머지 1바이트만 할당하면 "미"라는 한글을 온전히 표현할 수 없습니다. 이때, ft4j는 나머지 1바이트에 대해서 공백으로 채웁니다.

```java
@Test
void utf8_kor_test() {
    User user = new User("스미스");
    byte[] result = marshallManager.marshall(user);

    byte[] expected = "스 ".getBytes(Charset.forName("utf-8"));
    assertEquals(4, result.length);
    assertArrayEquals(expected, result);
}
```

인코딩을 EUC-KR으로 변경하고 "스미스"라는 같은 문자열에 대해서 테스트를 진행해보겠습니다.

```java
@BeforeEach
void setUp() {
    marshallManager = MarshallFactory.builder().converter(ConverterType.EUC_KR).build();
}
```

EUC-KR 인코딩에서 한글은 2바이트를 차지합니다. length 속성 값이 4이므로 첫 두 글자 "스미"를 4바이트로 온전히 표현할 수 있습니다.

```java
@Test
void euckr_kor_test() {

    User user = new User("스미스");
    byte[] result = marshallManager.marshall(user);

    byte[] expected = "스미".getBytes(Charset.forName("EUC-KR"));

    assertEquals(4, result.length);
    assertArrayEquals(expected, result);
}
```


#### 3.2.2 Unmarshall 테스트
unmarshalling의 경우 어떻게 되는지 테스트 해보겠습니다. 테스트에 사용될 UnMarshaller객체를 utf-8 인코딩을 사용하여 생성합니다.

```java
public class EncodingTest {

	private UnMarshallManager unMarshallManager;

	@BeforeEach
	void setUp() {
		unMarshallManager = UnMarshallFactory.builder().converter(ConverterType.EUC_KR).build();
	}

    // @Test Method......
}
```

name필드에 @StringValue을 선언하고 length 속성 값을 4로 지정합니다. 

```java
@StringValue(order = 1, length = 4)
private String name;
```
"Smith" 문자열을 길이가 5인 바이트 배열로 변환한 후, unmarshlling하면 User 객체의 name 필드에는 길이가 4인 "Smit" 문자열이 저장됩니다.

```java
@Test
void utf8_english_test() {
	byte[] input = "Smith".getBytes(Charset.forName("utf-8"));
	User user = unMarshallManager.unmarshall(input, User.class);
	assertEquals("Smit", user.getName());
}
```

UTF-8인코딩에서 "스미스" 문자열은 9바이트입니다. name필드에는 총 4바이트만 할당할 수 있으므로 '스' 한 글자만 할당할 수 있습니다. marshalling과 달리 문자를 온전히 표현할 수 없다면 length 속성 값보다 작은 바이트가 필드에 할당 될 수 있습니다. 아래 테스트 코드에서는 '스' 문자 뒤에 공백이 붙지 않은 것에 주목하세요.

```java
@Test
void utf8_kor_test() {
	byte[] input = "스미스".getBytes(Charset.forName("utf-8"));
	User user = unMarshallManager.unmarshall(input, User.class);
	assertEquals("스", user.getName());
}
```

인코딩을 EUC-KR으로 변경하고 "스미스"라는 같은 문자열에 대해서 테스트를 진행해보겠습니다.

```java
@BeforeEach
void setUp() {
	unMarshallManager = UnMarshallFactory.builder().converter(ConverterType.EUC_KR).build();
}
```

EUC-KR인코딩에서 "스미스" 문자열은 6바이트입니다. name 필드에 4바이트인 "스미" 두 글자를 할당할 수 있습니다.

```java
@Test
void euckr_kor_test() {
	byte[] input = "스미스".getBytes(Charset.forName("euc-kr"));
	User user = unMarshallManager.unmarshall(input, User.class);
	assertEquals("스미", user.getName());
}
```

