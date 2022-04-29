# 꼬리 재귀 함수

코틀린은 꼬리 재귀 함수에 대한 최적화 컴파일을 지원한다.

다음은 단순한 이진 검색을 수행하는 함수이다.

```kotlin
tailrec fun binIndexOf(
	x: Int,
    array: IntArray,
	from: Int = 0,
	to: Int = array.size
): Int {
	if (from == to ) return -1
	val midIndex = (from + to - 1) / 2
	val mid = array[midIndex]
	return when {
		mid < x ->bindIndexOf(x, array, midIndex + 1, to)
		mid > x ->bindIndexOf(x, array, from, midInex)
		else ->midinex
	}
}
```

위의 함수는 이진검색이라는 아이디어를 명확하게 표시해준다. 하지만 재귀 버전은 비재귀 버전과 비교해 성능차원에서 부가비용이 발생하고 스택 오버플로 가능성 있다.

하지만 코틀린에서 tailrec을 붙이면 컴파일러가 자동으로 재귀 함수를 비 재귀적인 코드로 변환해준다.

그 결과 재귀함수의 간결함과 비재귀 루프의 성능만 취할 수 있다.


```kotlin

//컴파일 후 실제로 이렇게 동작한다.
tailrec fun binIndexOf(
	x: Int,
    array: IntArray,
	from: Int = 0,
	to: Int = array.size
): Int {
	var fromIndex = from
    var toInedx = to
    
    while (true){
    	if (index == toIndex) return -1
        val midIndex = (fromIndex + toIndex - 1) /2
        val mid = array[midIndex]
        
        when {
        	mix < x ->fromIndex = minIndex + 1
            mid > x ->toIndex = midIndex
            else -> return midIndex
        }
    }
}
```

![](https://velog.velcdn.com/images/roo333/post/dfff526d-1f6d-4d08-b006-567c48fc0606/image.png)


> 코틀린 BYTE 코드를 JAVA 파일로 디컴파일시 실제로는 위와 같이 코드가 작성된걸 확인 할 수 있다.


이런 변환을 적용하려면 함수가 재귀 호출 다음에 아무 동작도 수행하지 말아야 한다. 이말이 바로 꼬리 재귀라는 용어라는 뜻이다. 함수에 tailrec을 붙였는데 꼬리 재귀가 아니라는 것을 컴파일러가 알게되면 컴파일러는 경고를 표시하고 함수를 일반 재귀 함수로 컴파일한다.

다음 함수는 호출 동작 이후에 덧셈을 수행하기 때문에 꼬리 재귀 함수가 아니다.

```kotlin
tailrec fun sum(array: IntArray, from: Int = 0, to: int = array.size): Int {

// A function is marked as tail-recursive but no tail calls are found
	return if (from < to) return array[from] + sum(array, from + 1, to) else 0
}
```
