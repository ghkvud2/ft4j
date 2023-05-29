# User Guide

## 1.0 Introduction

Full Text For Java (이하 ft4j)는 자바 객체를 문자열로 마샬링, 언마샬링할 수 있는 라이브러리입니다. 특히 바이트 기반의 고정 길이 문자열을 생성할 때 유용하게 사용할 수 있습니다. ft4j는 MIT 라이센스를 따릅니다.

## 2.0 Getting Started

ft4j를 사용하기 위해서, `ft4j.jar`를 어플리케이션 클래스패스에 추가하거나 메이븐 의존성을 추가합니다. ft4j는 JDK 1.8 버전 이상을 요구합니다.

```xml
<dependency>
    <groupId>com.github.ghkvud2</groupId>
    <artifactId>ft4j</artifactId>
    <version>1.0</version>
</dependency>
```

### 2.1 Marshall Simple Example

이 섹션에서는 ft4j를 사용해서 마샬링하는 간단한 예제를 살펴봅니다. 이름, 나이, 키 필드를 갖는 Person 객체가 있을 때, 해당 객체를 하나의 문자열로 변경해보겠습니다.

```java
class Person {

    @Message(length = 15)
    private String name;

    @IntValue(length = 5)
    private int age;

    @Decimal(length = 10, fractionalLength = 2)
    private double height;

}
```

객체를 문자열로 표시하기 위해서는 미리 정의된 어노테이션을 사용해야합니다. 어노테이션은 총 세 가지가 있습니다.

1. @Message : String 타입의 필드에 선언합니다. length는 해당 필드 값을 문자열로 나타냈을 때 최대 길이를 의미합니다 (바이트 기준).
2. @IntValue : short, int, long과 같은 정수형 타입의 필드에 선언합니다.
3. @Decimal : float, double과 같은 실수형 타입의 필드에 선언합니다.

> @IntValue와 @Decimal은 Integer, Double과 같은 Wrapper 클래스에는 선언할 수 없습니다.

MarshallerFactory클래스를 이용하여 실제로 마샬링을 수행하는 Marshaller를 생성해야 합니다. encoder메소드에 마샬링을 수행할 인코딩 타입을 지정할 수 있습니다. UTF-8과 EUC-KR을 지원합니다.

```java
Marshaller marshaller = MarshallerFactory.builder().encoder(EncoderType.UTF_8).build();

Person person = new Person("홍길동", 25, 175.6f);
String result = marshaller.marshall(person);
System.out.println(result);
```

```shell
홍길동      000250000175.60
```

```shell
name : [홍길동      ], 전체 15바이트
age  : [00025], 전체 5바이트
height : [0000175.60], 전체 10바이트
```

1. @Message는 필드 값을 왼쪽 정렬하고, 나머지 바이트를 SPACE로 채웁니다.

2. @IntValue은 필드 값을 오른쪽 정렬하고, 나머지 바이트를 ZERO로 채웁니다.

3. @Decimal은 필드 값을 오른쪽 정렬하고, 나머지 바이트를 ZERO로 채웁니다. 그리고 fractionalLength 속성을 이용하여 소수점 자릿수를 지정할 수 있습니다.

### 2.2 Unmarshall Simple Example

미구현

## 3.0 Core Concepts

### 3.1 Annotation

ft4j에서 사용할 수 있는 어노테이션 종류에 대해서 살펴봅니다.

#### 3.1.1 @Message

객체의 String 타입의 필드에 선언할 수 있는 어노테이션입니다. 주요 속성 값은 아래와 같습니다.

| 속성명          | 타입          | 필수  | 기본 값              | 설명                                                                    |
| ------------ | ----------- | --- | ----------------- | --------------------------------------------------------------------- |
| length       | int         | Y   |                   | 해당 필드의 바이트 수를 지정합니다.                                                  |
| defaultValue | String      | N   | -                 | 기본 값을 지정합니다. 필드 값이 있더라도 해당 값으로 대체됩니다.                                 |
| paddingByte  | PaddingByte | N   | PaddingByte.SPACE | length 속성 값이 필드 값의 바이트 수보다 더 클 때, 나머지 바이트를 채울 값을 지정합니다. 기본 값은 공백입니다.  |
| justify      | Justify     | N   | Justify.LEFT      | length 속성 값이 필드 값의 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다. 기본 값은 왼쪽 정렬입니다. |

**3.1.1.1 length**

String 타입의 name 필드를 갖는 User 클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 User 객체를 아래와 같이 생성합니다.

```java
class User {
    private String name;
    //constructor, getter, setter
}
```

```java
public class MessageTest {

    private Marshaller marshaller;
    private User user;

    @BeforeEach
    void setUp() {
        marshaller = MarshallerFactory.builder().encoder(EncoderType.UTF_8).build();
        user = new User("John");
    }

     // @Test Method......   
}
```

@Message 어노테이션의 length 값에 15를 설정한 뒤, User 객체의 필드 값을 "John"으로 설정하고 마샬링하면 총 15바이트 크기의 문자열이 생성됩니다. 다른 속성 값들은 모두 기본 값을 그대로 사용하기 때문에 필드 값은 왼쪽 정렬되고 나머지 바이트는 SPACE로 채워집니다.

```java
@Message(length = 15)
private String name;
```

```java
@Test
void length_test() {
    String result = marshaller.marshall(user);
    assertEquals("John           ", result);
    assertEquals(15, result.getBytes().length);
}
```

만약, 필드 값이 length의 길이보다 더 클 경우에는 필드 값을 length 값에 맞춰 자른 후 마샬링합니다. "John"은 4바이트인 반면, length의 값은 3이기 때문에 결과는 "Joh"입니다.

```java
@Message(length = 3)
private String name;
```

```java
@Test
void exceed_length_test() {
    String result = marshaller.marshall(user);
    assertEquals("Joh", result);
    assertEquals(3, result.getBytes().length);
}
```

**3.1.1.2 defaultValue**

defaultValue 속성 값을 "my name"로 설정하면, name 필드에 기존에 설정된 "John" 대신 "my name" 값으로 마샬링을 합니다.

```java
@Message(length = 15, defaultValue="my name")
private String name;
```

```java
@Test
void default_value_test() {
    String result = marshaller.marshall(user);
    assertEquals("my name        ", result);
    assertEquals(15, result.getBytes().length);
}
```

만약, defaultValue 속성 값이 length의 길이보다 더 클 경우에는 **DefaultValueExceedsLimitException가 발생합니다.**

```java
@Message(length = 3, defaultValue="my name")
private String name;
```

```java
@Test
void default_value_exceed_limit() {
    assertThrows(DefaultValueExceedsLimitException.class, () -> marshaller.marshall(user));
}
```

**3.1.1.3 paddingByte**

paddingByte 속성 값의 기본 값은 SPACE입니다. 나머지 바이트가 공백으로 채워진 것을 확인할 수 있습니다.

```java
@Message(length = 15)
private String name;
```

```java
@Test
void default_padding_test() {
    String result = marshaller.marshall(user);
    assertEquals("John           ", result);
    assertEquals(15, result.getBytes().length);
}
```

paddingByte 속성 값을 ZERO로 지정하면 나머지 바이트를 해당 값으로 채웁니다. PaddingByte의 종류는 다른 섹션을 참고하세요.

```java
@Message(length = 15, paddingByte = PaddingByte.ZERO)
private String name;
```

```java
@Test
void padding_test() {
    String result = marshaller.marshall(user);
    assertEquals("John00000000000", result);
    assertEquals(15, result.getBytes().length);
}
```

**3.1.1.4 justify**

justify 속성 값의 기본 값은 LEFT 입니다. 필드 값이 왼쪽 정렬된 것을 확인할 수 있습니다.

```java
@Message(length = 15)
private String name;
```

```java
@Test
void justify_left_test() {
    String result = marshaller.marshall(user);
    assertEquals("John           ", result);
    assertEquals(15, result.getBytes().length);
}
```

justify의 속성 값을 RIGHT로 지정하면 필드 값이 오른쪽 정렬된 것을 확인할 수 있습니다.

```java
@Message(length = 15, justify = Justify.RIGHT)
private String name;
```

```java
@Test
void justify_right_test() {
    String result = marshaller.marshall(user);
    assertEquals("           John", result);
    assertEquals(15, result.getBytes().length);
}
```

#### 3.1.2 @IntValue

객체의 short, int, long 타입의 필드에 선언할 수 있는 어노테이션입니다. 주요 속성 값은 아래와 같습니다.

| 속성명          | 타입          | 필수  | 기본 값             | 설명                                                                                                   |
| ------------ | ----------- | --- | ---------------- | ---------------------------------------------------------------------------------------------------- |
| length       | int         | Y   |                  | 해당 필드의 바이트 수를 지정합니다.                                                                                 |
| defaultValue | String      | N   | -                | 기본 값을 지정합니다. 필드 값이 있더라도 해당 값으로 대체됩니다.                                                                |
| paddingByte  | PaddingByte | N   | PaddingByte.ZERO | 필드 값의 바이트 수보다 length가 더 클 때, 나머지 바이트를 채우게될 바이트를 나타냅니다. 기본 값은 '0'입니다.                                 |
| justify      | Justify     | N   | Justify.RIGHT    | length 속성 값이 필드 값의 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다. 기본 값은 오른쪽 정렬입니다.                               |
| ignoreLimit  | boolean     | N   | false            | length 속성 값이 필드 값의 바이트 수보다 더 작을 때, 예외를 발생시킬지 여부를 결정합니다. 기본 값은 false이며, 이 경우 예외가 발생합니다. (아래 상세 내용 참고) |

> String 타입의 필드를 처리하는 @Message 어노테이션은 필드 값이 length 속성 값보다 더 작을 때, 예외를 발생시키지 않고 length 속성 값에 맞춰 바이트를 잘라서 문자열을 생성했습니다. 그러나 @IntValue 어노테이션이 선언될 수 있는 short, int, long 타입의 필드들은 length 길이에 맞춰 필드 값을 자르게되면 기존 필드 값과는 전혀 다른 의미의 값을 나타내게 됩니다.
> 
> 예를 들어, int형 타입의 필드 값에 100이라는 값이 설정되었고 위 테스트처럼 length 속성 값을 '2'로 설정했을 때를 생각해보겠습니다. @Message 어노테이션과 마찬가지로 length 속성 값에 맞춰 바이트를 자르게되면 100은 10이라는 값으로 변환될 것입니다. 이는 의도하지 않은 심각한 오류를 발생시킬 수 있으므로 @IntValue 어노테이션에는 이러한 경우에 기본적으로 예외를 발생시키도록 하였습니다.
> 
> 실생활의 예를 들어보면, 계좌이체를 하는 경우 입금한 사람의 이름이 잘리는 것과 의도치않게 입금 금액이 잘리는 경우, 둘 중 어떠한 상황이 더 심각한 오류가 발생한 상황일까요?

**3.1.2.1 length**

int 타입의 price 필드를 갖는 Product 클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 Product 객체를 아래와 같이 생성합니다.

```java
class Product {
    private int price;
    //constructor, getter, setter
}
```

```java
public class IntValueTest {

    private Marshaller marshaller;
    private Product product;

    @BeforeEach
    void setUp() {
        marshaller = MarshallerFactory.builder().encoder(EncoderType.UTF_8).build();
        product = new Product(100);
    }

     // @Test Method......   
}
```

@IntValue어노테이션의 length 값에 10를 설정한 뒤, Price 객체의 필드 값을 100으로 설정하고 마샬링하면 총 10바이트 크기의 문자열이 생성됩니다. 다른 속성 값들은 모두 기본 값으로 설정했기 때문에 필드 값은 오른쪽 정렬되고 나머지 바이트는 ZERO로 채워집니다.

```java
@IntValue(length = 10)
private int price;
```

```java
@Test
void length_test() {
    String result = marshaller.marshall(product);
    assertEquals("0000000100", result);
    assertEquals(10, result.getBytes().length);
}
```

만약, 필드 값이 length의 길이보다 더 클 경우에는 **FieldValueExceedsLimitException** 예외가 발생합니다.

```java
@IntValue(length = 2)
private String name;
```

```java
@Test
void field_value_exceed_length() {
    assertThrows(FieldValueExceedsLimitException.class, () -> marshaller.marshall(product));
}
```

ignoreLimit을  true로 설정할 경우, 예외가 발생하지 않습니다.

```java
@IntValue(length = 2, ignoreLimit = true)
private String name;
```

```java
@Test
void ignore_limit_test() {
    String result = marshaller.marshall(product);
    assertEquals("10", result);
    assertEquals(2, result.getBytes().length);
}
```

**3.1.2.1 defaultValue**

defaultValue 속성 값을 "200"로 설정하면, price 필드에 기존에 설정된 100 대신 "200" 값으로 마샬링을 합니다.

```java
@IntValue(length = 10, defaultValue = "200")
private int price;
```

```java
@Test
void default_value_test() {
    String result = marshaller.marshall(product);
    assertEquals("0000000200", result);
    assertEquals(10, result.getBytes().length);
}
```

만약, defaultValue 속성 값이 int 타입으로 변환할 수 없는 경우에는 NumberFormatException 예외가 발생합니다.

```java
@IntValue(length = 10, defaultValue = "ABC")
private String name;
```

```java
@Test
void convert_exception() {
    assertThrows(NumberFormatException.class, ()->marshaller.marshall(product));
}
```

**3.1.2.1 paddingByte**

paddingByte 속성 값의 기본 값은 ZERO입니다. 나머지 바이트가 0으로 채워진 것을 확인할 수 있습니다.

```java
@IntValue(length = 10)
private int price;
```

```java
@Test
void padding_test() {
    String result = marshaller.marshall(product);
    assertEquals("0000000100", result);
    assertEquals(10, result.getBytes().length);
}
```

paddingByte 속성 값을 SPACE로 지정하면 나머지 바이트를 해당 값으로 채웁니다.

```java
@IntValue(length = 10, paddingByte = PaddingByte.SPACE)
private int price;
```

```java
@Test
void padding_test() {
    String result = marshaller.marshall(user);
    assertEquals("       100", result);
    assertEquals(10, result.getBytes().length);
}
```

**3.1.2.1 justify**

justify 속성 값의 기본 값은 RIGHT입니다. 필드 값이 오른쪽 정렬된 것을 확인할 수 있습니다.

```java
@IntValue(length = 10)
private int price;
```

```java
@Test
void justify_test() {
    String result = marshaller.marshall(product);
    assertEquals("0000000100", result);
    assertEquals(10, result.getBytes().length);
}
```

justify의 속성 값을 LEFT로 지정하면 필드 값이 왼쪽 정렬된 것을 확인할 수 있습니다.

```java
@IntValue(length = 10, justify = Justify.LEFT)
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

왼쪽 정렬과 paddingByte를 SPACE로 설정할 수 도 있습니다.

```java
@IntValue(length = 10, justify = Justify.LEFT, paddingByte = PaddingByte.SPACE)
private int price;
```

```java
@Test
void justify_left_padding_test() {
    String result = marshaller.marshall(product);
    assertEquals("100       ", result);
    assertEquals(10, result.getBytes().length);
}
```

#### 3.1.3 @Decimal

객체의 float, double 타입의 필드에 선언할 수 있는 어노테이션입니다. 주요 속성 값은 아래와 같습니다.

| 속성명              | 타입          | 필수  | 기본 값             | 설명                                                                                     |
| ---------------- | ----------- | --- | ---------------- | -------------------------------------------------------------------------------------- |
| length           | int         | Y   |                  | 해당 필드의 바이트 수를 지정합니다.                                                                   |
| defaultValue     | String      | N   | -                | 기본 값을 지정합니다. 필드 값이 있더라도 해당 값으로 대체됩니다.                                                  |
| fractionalLength | int         | N   | 1                | 표현할 소수점 이하의 자릿수를 나타냅니다. length <= fractionalLength 인 경우, 소수점 이하는 생략됩니다. 기본 값은 1입니다.    |
| paddingByte      | PaddingByte | N   | PaddingByte.ZERO | 필드 값의 바이트 수보다 length가 더 클 때, 나머지 바이트를 채우게될 바이트를 나타냅니다. 기본 값은 '0'입니다.                   |
| justify          | Justify     | N   | Justify.RIGHT    | length 속성 값이 필드 값의 바이트 수보다 더 클 때, 필드 값의 정렬 방식을 지정합니다. 기본 값은 오른쪽 정렬입니다.                 |
| ignoreLimit      | boolean     | N   | false            | length 속성 값이 필드 값의 바이트 수보다 더 작을 때, 예외를 발생시킬지 여부를 결정합니다. 기본 값은 false이며, 이 경우 예외가 발생합니다. |

> ignoreLimit 속성은 @IntValue 속성에서 설명한 것과 같습니다.

**3.1.3.1 length**

double 타입의 rate필드를 갖는 Bank클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 Bank객체를 아래와 같이 생성합니다.

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

     // @Test Method......   
}
```

@Decimal어노테이션의 length 값에 10를 설정한 뒤, bank객체의 필드 값을 135.8345으로 설정하고 마샬링하면 총 10바이트 크기의 문자열이 생성됩니다. 다른 속성 값들은 모두 기본 값으로 설정했기 때문에 필드 값은 오른쪽 정렬되고 나머지 바이트는 ZERO로 채워집니다. fractionalLength의 기본 값은 1이므로 소수점 이하 첫째짜리까지만 표현됩니다.

```java
@Decimal(length = 10)
private double rate;
```

```java
@Test
void length_test() {
    String result = marshaller.marshall(bank);
    assertEquals("00000135.8", result);
    assertEquals(10, result.getBytes().length);
}
```

bank객체의 설정한 필드 값 135.8345을 소수점 이하 자릿수까지 모두 표현하고 싶다면, fractionalLength의 값을 설정해줘야합니다.

```java
@Decimal(length = 10, fractionalLength = 4)
private double rate;
```

```java
@Test
void fractionalLength_test() {
    String result = marshaller.marshall(bank);
    assertEquals("00135.8345", result);
    assertEquals(10, result.getBytes().length);        
}
```

```java
@Decimal(length = 10, fractionalLength = 5)
private double rate;
```

```java
@Test
void fractionalLength_test() {
    String result = marshaller.marshall(bank);
    assertEquals("0135.83450", result);
    assertEquals(10, result.getBytes().length);        
}
```

만약, 필드 값이 length의 길이보다 더 클 경우에는 **FieldValueExceedsLimitException** 예외가 발생합니다.

```java
@Decimal(length = 10)
private double rate;
```

```java
@Test
void field_value_exceed_length() {
    assertThrows(FieldValueExceedsLimitException.class, () -> marshaller.marshall(bank));
}
```

**3.1.3.1 defaultValue**

defaultValue 속성 값을 "100.3"로 설정하면, rate필드에 기존에 설정된 135.8345대신 "100.3" 값으로 마샬링을 합니다.

```java
@Decimal(length = 10, defaultValue = "100.3")
private double rate;
```

```java
@Test
void default_value_test() {
    String result = marshaller.marshall(bank);
    assertEquals("00000100.3", result);
    assertEquals(10, result.getBytes().length);
}
```

만약, defaultValue 속성 값이 double 타입으로 변환할 수 없는 경우에는 NumberFormatException 예외가 발생합니다.

```java
@Decimal(length = 10, defaultValue = "ABC")
private double rate;
```

```java
@Test
void convert_exception() {
    assertThrows(NumberFormatException.class, ()->marshaller.marshall(bank));
}
```

**3.1.3.1 paddingByte**

paddingByte 속성 값의 기본 값은 ZERO입니다. 나머지 바이트가 0으로 채워진 것을 확인할 수 있습니다.

```java
@Decimal(length = 10)
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

**3.1.3.1 justify**

justify 속성 값의 기본 값은 RIGHT입니다. 필드 값이 오른쪽 정렬된 것을 확인할 수 있습니다.

```java
@Decimal(length = 10)
private double rate;
```

```java
@Test
void justify_test() {
    String result = marshaller.marshall(bank);
    assertEquals("00000135.8", result);
    assertEquals(10, result.getBytes().length);
}
```

justify의 속성 값을 LEFT로 지정하면 필드 값이 왼쪽 정렬된 것을 확인할 수 있습니다.

```java
@Decimal(length = 10, justify = Justify.LEFT)
private double rate;
```

```java
@Test
void justify_left_test() {
    String result = marshaller.marshall(bank);
    assertEquals("135.800000", result);
    assertEquals(10, result.getBytes().length);
}
```

왼쪽 정렬과 paddingByte를 SPACE로 설정할 수 도 있습니다.

```java
@Decimal(length = 10, justify = Justify.LEFT, paddingByte = PaddingByte.SPACE)
private double rate;
```

```java
@Test
void justify_left_padding_test() {
    String result = marshaller.marshall(bank);
    assertEquals("135.8     ", result);
    assertEquals(10, result.getBytes().length);
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

**3.1.4.1 generator**

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

String 타입의 paymentDate필드를 갖는 Payment클래스를 아래와 같이 작성합니다. 테스트에 사용될 Marshaller객체와 Payment객체를 아래와 같이 생성합니다.

```java
class Payment {
    private String paymentDate;
    //constructor, getter, setter
}
```

```java
public class GeneratorTest {

    private Marshaller marshaller;
    private Bank bank;

    @BeforeEach
    void setUp() {
        marshaller = MarshallerFactory.builder().encoder(EncoderType.UTF_8).build();
        payment = new Payment();
    }

     // @Test Method......   
}
```

generator 속성 값에 위에서 생성한 FullDateTimeGenerator 클래스를 지정하면 필드 값을 현재 시간으로 대체합니다.

```java
@GeneratedValue(generator = FullDateTimeGenerator.class)
@Message(length = 17)
private String paymentDate;
```

```java
@Test
void generator_test() {
    String result = marshaller.marshall(payment);
    System.out.println(String.format("실행 결과 : [%s]", result));
}
```

```shell
실행 결과 : [20230515162424336]
```

@Message 어노테이션의 length 속성 값보다 generator가 생성한 문자열의 길이가 더 클 경우, GeneratedValueExceedsLimitException 예외가 발생합니다.

```java
@Test
void generated_value_exceeds_limit_test() {
    assertThrows(GeneratedValueExceedsLimitException.class, () -> marshaller.marshall(payment));
}
```

**3.1.4.2 key, cacheable**
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
@Message(length = 36)
private String uuid;

@Message(length = 5)
private String space;

@GeneratedValue(key = "id", cacheable = true)
@Message(length = 36)
private String cachedId;
```

```java
@Test
void cacheable_test() {
    String result = marshaller.marshall(payment);
    System.out.println(String.format("실행 결과 : [%s]", result));
}
```

같은 uuid가 출력된 것을 확인할 수 있습니다.

```shell
실행 결과 : [e251efd9-982f-4296-93f1-eaefa6cf7faa     e251efd9-982f-4296-93f1-eaefa6cf7faa]
```

notCachedId 필드에 cacheable 속성을 추가하지 않으면, uuid 필드와 다른 UUID가 생성되는 것을 확인할 수 있습니다.

```java
        @GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
        @Message(length = 36)
        private String uuid;

        @Message(length = 5)
        private String space;

        @GeneratedValue(generator = UUIDGenerator.class)
        @Message(length = 36)
        private String notCachedId;
```

```java
@Test
void not_cacheable_test() {
    String result = marshaller.marshall(payment);
    System.out.println(String.format("실행 결과 : [%s]", result));
}
```

```shell
실행 결과 : [07206f7c-48e4-48f2-bc93-210a2ee68941     1782df5e-bec9-4fb2-bd6a-43326f8c9f2c]
```

key 속성으로 전달된 키 값이 존재하지 않을 경우, generator에 지정된 클래스가 생성한 문자열로 키-값 형태로 저장하게 됩니다. 이때, generator 속성 값을 설정하지 않을 경우 CacheKeyNotFoundException 예외가 발생합니다. 아래 테스트에서는 uuid가 "id" 값으로 key를 설정했지만 캐시에는 어떠한 값도 저장되지 않은 상태이기 때문에 generator가 생성한 문자열을 캐시에 저장하려고 시도합니다. 그러나 generator 속성을 설정하지 않았기 때문에 예외가 발생합니다.

```java
@GeneratedValue(key = "id", cacheable = true)
@Message(length = 36)
private String uuid;

@Message(length = 5)
private String space;

@GeneratedValue(key = "id", cacheable = true)
@Message(length = 36)
private String cachedId;
```

```java
@Test
void key_not_found_test() {
    assertThrows(CacheKeyNotFoundException.class, () -> marshaller.marshall(payment));
}
```

cachedId 필드에 cacheable 속성을 true로 설정하고 key 속성 값을 지정하지 않을 경우, MissingCacheKeyException 예외가 발생합니다.

```java
@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
@Message(length = 36)
private String uuid;

@Message(length = 5)
private String space;

@GeneratedValue(cacheable = true)
@Message(length = 36)
private String cachedId;
```

```java
@Test
void missing_key_test() {
    assertThrows(MissingCacheKeyException.class, () -> marshaller.marshall(payment));
}
```

@GeneratedValue는 캐시를 `ThreadLocal` 에 저장합니다. 캐시의 생명주기는 Marshaller의 marshall() 메소드와 같습니다.

```java
@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
@Message(length = 36)
private String uuid;

@Message(length = 5)
private String space;

@GeneratedValue(key = "id", cacheable = true)
@Message(length = 36)
private String cachedId;
```

```java
@Test
void multithread_test() {

    for(int i = 0 ; i < 5; i++) {
        Runnable thread = () -> {
            String result = marshaller.marshall(payment);
            System.out.println(String.format("실행 결과 : [%s]", result));
        };
        thread.run();
    }
}
```

테스트 실행 결과를 보면 스레드끼리는 다른 UUID를 생성하는 것을 확인할 수 있습니다.

```shell
실행 결과 : [101c5c64-8dde-42ae-987f-306f9cb604d1     101c5c64-8dde-42ae-987f-306f9cb604d1]
실행 결과 : [d075edb3-9df8-40ad-bb3f-9e6a687a72c1     d075edb3-9df8-40ad-bb3f-9e6a687a72c1]
실행 결과 : [a6413529-8e1e-4bf7-b08d-0ec69a5c9619     a6413529-8e1e-4bf7-b08d-0ec69a5c9619]
실행 결과 : [d663789d-68f2-4ef1-be7c-b54d1b55fc38     d663789d-68f2-4ef1-be7c-b54d1b55fc38]
실행 결과 : [3e62096c-be66-4608-b02d-133d0336ab89     3e62096c-be66-4608-b02d-133d0336ab89]
```