> 자주 쓰이는 코틀린 관용구 [원 글](https://kotlinlang.org/docs/idioms.html)을 번역한 글입니다.

코틀린에서 무작위로 자주 사용되는 관용구의 모음입니다. 자신이 자주 쓰는 관용어가 있으면 pull request를 통해 기여하세요.

### DTO(POJO/POCO) 생성

```kotlin
data class Customer(val name: String, val email: String)
```

고객 클래스에 다음 기능을 제공합니다:

* getters (var의 경우에는 setter) 모든 프로퍼티

* equals()

* hashCode()

* toString()

* copy()

* component1(), component2(), ..., 모든 프로퍼티들 (데이타 클래스를 참고하세요.)

### 함수 매개변수의 기본값

```
fun foo(a: Int = 0, b: String = "") { ... }
```

### 리스트 필터링

```kotlin
val positives = list.filter { x -> x > 0 }
```

또는 더 짧게:

```kotlin
val positives = list.filter { it > 0 }
```

### 컬렉션에 요소가 있는지 확인

```
if ("john@example.com" in emailsList) { ... }

if ("jane@example.com" !in emailsList) { ... }
```

### 문자열 변수 삽입

```kotlin
println("Name $name")
```

### 인스턴스 확인

```
when (x) {
    is Foo -> ...
    is Bar -> ...
    else   -> ...
}
```

### 읽기 전용 리스트
```kotlin
val list = listOf("a", "b", "c")
```

### 읽기 전용 맵
```kotlin
val list = listMap("a", "b", "c")
```

### 맵 접근
```kotlin
println(map["key"])
map["key"] = value
```

### 맵 또는 페어 리스트 탐색
```kotlin
for ((k, v) in map) {
    println("$k -> $v")
}
```

k 및 v는 이름 및 나이와 같은 편리한 이름으로 바꿀 수 있습니다.

### 범위에 걸쳐 반복
```kotlin
for (i in 1..100) { ... }  // closed range: includes 100
for (i in 1 until 100) { ... } // half-open range: does not include 100
for (x in 2..10 step 2) { ... }
for (x in 10 downTo 1) { ... }
(1..10).forEach { ... }
```

### lazy 속성

```kotlin
val p: String by lazy {
    // compute the string
}
```

### 확장 함수

```kotlin
fun String.spaceToCamelCase() { ... }

"Convert this to camelcase".spaceToCamelCase()
```

### 싱글톤 패턴

```kotlin
object Resource {
    val name = "Name"
}
```

### 추상 클래스 인스턴스화

```kotlin
abstract class MyAbstractClass {
    abstract fun doSomething()
    abstract fun sleep()
}

fun main() {
    val myObject = object : MyAbstractClass() {
        override fun doSomething() {
            // ...
        }

        override fun sleep() { 
        	// ...
        }
    }
    myObject.doSomething()
}
```

### If-not-null 단축

```kotlin
val files = File("Test").listFiles()

println(files?.size) // size is printed if files is not null
```

### If-not-null-else 단축

```kotlin
val files = File("Test").listFiles()

println(files?.size ?: "empty") // if files is null, this prints "empty"

// To calculate the fallback value in a code block, use `run`
val filesSize = files?.size ?: run {
    return someSize
}
println(filesSize)
```

### null인 경우 명령문 실행

```kotlin
val values = ...
val email = values["email"] ?: throw IllegalStateException("Email is missing!")
```

### 비어 있을 수 있는 컬렉션의 첫 번째 항목 가져오기
```kotlin
val emails = ... // might be empty
val mainEmail = emails.firstOrNull() ?: ""
```

### null이 아닌 경우 실행

```kotlin
val value = ...

value?.let {
    ... // execute this block if not null
}
```

### null이 아닌 경우 nullable 값 매핑
```kotlin
val value = ...

val mapped = value?.let { transformValue(it) } ?: defaultValue
// defaultValue is returned if the value or the transform result is null.
```

### when 문에 반환
```kotlin
fun transform(color: String): Int {
    return when (color) {
        "Red" -> 0
        "Green" -> 1
        "Blue" -> 2
        else -> throw IllegalArgumentException("Invalid color param value")
    }
}
```
### try-catch expression
```kotlin
fun test() {
    val result = try {
        count()
    } catch (e: ArithmeticException) {
        throw IllegalStateException(e)
    }

    // Working with result
}
```
### if expression
```kotlin
val y = if (x == 1) {
    "one"
} else if (x == 2) {
    "two"
} else {
    "other"
}
```

### Unit을 반환하는 메소드의 빌더 스타일 사용법
```kotlin
fun arrayOfMinusOnes(size: Int): IntArray {
    return IntArray(size).apply { fill(-1) }
}
```

### 단일 표현식 함수
```kotlin
fun theAnswer() = 42
```
위 함수는 다음과 같습니다.

```kotlin
fun theAnswer(): Int {
    return 42
}
```

이것은 다른 관용구와 효과적으로 결합되어 코드가 더 짧아질 수 있습니다. 예를 들어 when 표현식은 다음과 같습니다.

```kotlin
fun transform(color: String): Int = when (color) {
    "Red" -> 0
    "Green" -> 1
    "Blue" -> 2
    else -> throw IllegalArgumentException("Invalid color param value")
}
```

### 개체 인스턴스에서 여러 메서드 호출(with)

```kotlin
class Turtle {
    fun penDown()
    fun penUp()
    fun turn(degrees: Double)
    fun forward(pixels: Double)
}

val myTurtle = Turtle()
with(myTurtle) { //draw a 100 pix square
    penDown()
    for (i in 1..4) {
        forward(100.0)
        turn(90.0)
    }
    penUp()
}
```
### 개체 속성 구성(apply)

```kotlin
val myRectangle = Rectangle().apply {
    length = 4
    breadth = 5
    color = 0xFAFAFA
}
```

### 자바 7의 try-with-resources
```kotlin
val stream = Files.newInputStream(Paths.get("/some/file.txt"))
stream.buffered().reader().use { reader ->
    println(reader.readText())
}
```

### 제네릭 유형 정보가 필요한 제네릭 함수
```kotlin
//  public final class Gson {
//     ...
//     public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
//     ...

inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T = this.fromJson(json, T::class.java)
```

### 널 입력 가능 Boolean
```kotlin
val b: Boolean? = ...
if (b == true) {
    ...
} else {
    // `b` is false or null
}
```

### 두 변수 스왑
```kotlin
var a = 1
var b = 2
a = b.also { b = a }
```

### 코드를 불완전한 것으로 표시(TODO)
Kotlin의 표준 라이브러리에는 항상 NotImplementedError를 발생시키는 TODO() 함수가 있습니다. 반환 유형은 Nothing이므로 예상 유형에 관계없이 사용할 수 있습니다. 이유 매개변수를 허용하는 오버로드도 있습니다.

```kotlin
fun calcTaxes(): BigDecimal = TODO("Waiting for feedback from accounting")
```


IntelliJ IDEA의 kotlin 플러그인은 TODO()의 의미를 이해하고 TODO 도구 창에 코드 포인터를 자동으로 추가합니다.
