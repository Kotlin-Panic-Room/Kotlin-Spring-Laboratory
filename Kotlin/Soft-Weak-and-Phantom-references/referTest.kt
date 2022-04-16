import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*
import kotlin.test.*


//강한 참조는 가장 일반적인 참조 유형
// 객체의 참조를 가지고 있는 동안에는 해당 객체는 GC의 대상이 되지 않는다.
fun testStrongRef(){
    //arrage
    var instance: String? = "123"
    var reference: String? = instance
    //act
    instance = null
    System.gc()
    // asserts
    assertNotNull(reference)
}

//약한 참조는 GC가 발생하면 무조건 수거된다.
// 약한 참조가 사라지는 시점이 GC의 실행 주기와 일치하고,
//이를 이용해 짧은 주기에 자주 사용되는 객체를 캐시할 때 유용하다. (실제로 톰캣 컨테이너의 ConcurrentCache class에서 WeakHashMap을 사용한다고 한다)
fun testWeakRef(){
    //arrage
    var instance: String? = StringBuilder("123").toString()
    val reference = WeakReference(instance)
    // Act
    instance = null
    System.gc()
    // Asserts
    assertNull(reference.get())
}

fun testWeakRefMap(){
    // arrange
    val map = WeakHashMap<String, Boolean>()
    var instance: String? = StringBuilder("123").toString()
    map[instance] = true

    // Act
    instance = null;
    GcUtils.fullFinalization();

    // assets
    assertTrue(map.isEmpty())
}
// 강한 참조와는 다르게 GC에 의해 수거될 수도 있고, 수거되지 않을 수도 있다.
// 메모리에 충분한 여유가 있다면 GC가 수행되더라도 수거되지 않는다.
// 만약 Out of Memory의 시점에 가깝다면 수거될 확률이 높다.
fun testSoftRef(){
    // Arrange
    var instance: String? = StringBuilder("123").toString()
    val softReference = SoftReference(instance)
    instance = null
    assertNotNull(softReference)
    assertNotNull(softReference.get())

    // Act
    GcUtils.tryToAllocateAllAvailableMemory() // OutOfMemoryError exception 뜰 때까지 구동

    // Asserts
    assertNull(softReference.get())

}


fun main() {
    testStrongRef()
    testWeakRef()
    testWeakRefMap()
    testSoftRef()
}


