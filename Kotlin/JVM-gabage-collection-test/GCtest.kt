

internal object GCTest {
    var objList: MyList? = null
    var wait = 1000 // milliseconds
    var initSteps = 16 // 2 MB
    var testSteps = 1
    var objSize = 128 // 1/8 MB
    @JvmStatic
    fun main(arg: Array<String>) {
        if (arg.size > 0) initSteps = arg[0].toInt()
        if (arg.size > 1) testSteps = arg[1].toInt()
        objList = MyList()
        val m = Monitor()
        m.isDaemon = true
        m.start()
        myTest()
    }

    fun myTest() {
        for (m in 0 until initSteps) {
            mySleep(wait)
            objList!!.add(MyObject())
        }
        for (n in 0 until 10 * 8 * 8 / testSteps) {
            for (m in 0 until testSteps) {
                mySleep(wait)
                objList!!.add(MyObject())
            }
            for (m in 0 until testSteps + 5) {
                mySleep(wait)
                objList!!.removeTail()
                // objList.removeHead();
            }
        }
    }

    fun mySleep(t: Int) {
        try {
            Thread.sleep(t.toLong())
        } catch (e: InterruptedException) {
            println("Interreupted...")
        }
    }

    internal class MyObject {
        private var obj: LongArray? = null
        var next: MyObject? = null
        var prev: MyObject? = null

        init {
            count++
            obj = LongArray(objSize * 128)
        }

        protected fun finalize() {
            count--
        }

        companion object {
            var count: Long = 0
                private set
        }
    }

    internal class MyList {
        var head: MyObject? = null
        var tail: MyObject? = null
        fun add(o: MyObject) {
            // add the new object to the head;
            if (head == null) {
                head = o
                tail = o
            } else {
                o.prev = head
                head!!.next = o
                head = o
            }
            count++
        }

        fun removeTail() {
            if (tail != null) {
                if (tail!!.next == null) {
                    tail = null
                    head = null
                } else {
                    tail = tail!!.next
                    tail!!.prev = null
                }
                count--
            }
        }

        fun removeHead() {
            if (head != null) {
                if (head!!.prev == null) {
                    tail = null
                    head = null
                } else {
                    head = head!!.prev
                    head!!.next = null
                }
                count--
            }
        }

        companion object {
            var count: Long = 0
        }
    }

    internal class Monitor : Thread() {
        override fun run() {
            val rt = Runtime.getRuntime()
            println(
                "Time   Total   Free   Free   Total   Act.   Dead   Over")
            println(
                "sec.    Mem.   Mem.   Per.    Obj.   Obj.   Obj.   Head")
            val dt0 = System.currentTimeMillis() / 1000
            while (true) {
                val tm = rt.totalMemory() / 1024
                val fm = rt.freeMemory() / 1024
                val ratio = 100 * fm / tm
                val dt = System.currentTimeMillis() / 1000 - dt0
                val to = MyObject.count * objSize
                val ao = MyList.count * objSize
                println(dt
                    .toString() + "     " + tm + "     " + fm + "     " + ratio + "%"
                        + "     " + to + "     " + ao + "     " + (to - ao)
                        + "     " + (tm - fm - to))
                mySleep(wait)
            }
        }
    }
}