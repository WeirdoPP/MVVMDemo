package com.zll.mvvm.view.basic_knowledge;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 数据结构分析
 * @since 2018/09/11
 * <p>
 * Collections （操作集合的工具类）
 * 对于集合类的操作不得不提到工具类Collections，它提供了许多方便的方法，如求两个集合的差集、并集、拷贝、排序等等。
 * 由于大部分的集合接口实现类都是不同步的，可以使用Collections.synchronized*方法创建同步的集合类对象。
 * 如创建一个同步的List：
 * List synList = Collections.synchronizedList(new ArrayList());
 * 其实现原理就是重新封装new出来的对象，操作对象时用关键字synchronized同步。看源码很容易理解。
 * <p>
 * ArrayList、Vector是线性表，使用Object数组作为容器去存储数据的，添加了很多方法维护这个数组，
 * 使其容量可以动态增长，极大地提升了开发效率。它们明显的区别是ArrayList是非同步的，Vector是同步的。
 * 不用考虑多线程时应使用ArrayList来提升效率。
 * 由此可根据实际情况来选择使用ArrayList（非同步、非频繁删除时选择）、Vector（需同步时选择）、LinkedList（频繁在任意位置插入、删除时选择）。
 * <p>
 * Map（存储键值对，key唯一）
 * HashMap结构的实现原理是将put进来的key-value封装成一个Entry对象存储到一个Entry数组中，
 * 位置（数组下标）由key的哈希值与数组长度计算而来。如果数组当前下标已有值，则将数组当前下标的值指向新添加的Entry对象。
 * TreeMap是由Entry对象为节点组成的一颗红黑树，put到TreeMap的数据默认按key的自然顺序排序，new TreeMap时传入Comparator自定义排序。
 * <p>
 * Set（保证容器内元素唯一性）
 * 之所以先讲Map是因为Set结构其实就是维护一个Map来存储数据的，利用Map结构key值唯一性。
 * <p>
 * HashMap和Hashtable的区别
 * HashMap和Hashtable都实现了Map接口，但决定用哪一个之前先要弄清楚它们之间的分别。主要的区别有：线程安全性，同步(synchronization)，以及速度。
 * <p>
 * HashMap几乎可以等价于Hashtable，除了HashMap是非synchronized的，并可以接受null(HashMap可以接受为null的键值(key)和值(value)，而Hashtable则不行)。
 * HashMap是非synchronized，而Hashtable是synchronized，这意味着Hashtable是线程安全的，多个线程可以共享一个Hashtable；而如果没有正确的同步的话，多个线程是不能共享HashMap的。Java 5提供了ConcurrentHashMap，它是HashTable的替代，比HashTable的扩展性更好。
 * 另一个区别是HashMap的迭代器(Iterator)是fail-fast迭代器，而Hashtable的enumerator迭代器不是fail-fast的。所以当有其它线程改变了HashMap的结构（增加或者移除元素），将会抛出ConcurrentModificationException，但迭代器本身的remove()方法移除元素则不会抛出ConcurrentModificationException异常。但这并不是一个一定发生的行为，要看JVM。这条同样也是Enumeration和Iterator的区别。
 * 由于Hashtable是线程安全的也是synchronized，所以在单线程环境下它比HashMap要慢。如果你不需要同步，只需要单一线程，那么使用HashMap性能要好过Hashtable。
 * HashMap不能保证随着时间的推移Map中的元素次序是不变的。
 * 要注意的一些重要术语：
 * 1) sychronized意味着在一次仅有一个线程能够更改Hashtable。就是说任何线程要更新Hashtable时要首先获得同步锁，其它线程要等到同步锁被释放之后才能再次获得同步锁更新Hashtable。
 * <p>
 * 2) Fail-safe和iterator迭代器相关。如果某个集合对象创建了Iterator或者ListIterator，然后其它的线程试图“结构上”更改集合对象，将会抛出ConcurrentModificationException异常。但其它线程可以通过set()方法更改集合对象是允许的，因为这并没有从“结构上”更改集合。但是假如已经从结构上进行了更改，再调用set()方法，将会抛出IllegalArgumentException异常。
 * <p>
 * 3) 结构上的更改指的是删除或者插入一个元素，这样会影响到map的结构。
 * <p>
 * 我们能否让HashMap同步？
 * HashMap可以通过下面的语句进行同步：
 * Map m = Collections.synchronizeMap(hashMap);
 * <p>
 * 结论
 * Hashtable和HashMap有几个主要的不同：线程安全以及速度。仅在你需要完全的线程安全的时候使用Hashtable，而如果你使用Java 5或以上的话，请使用ConcurrentHashMap吧。
 * <p>
 * <p>
 * <p>
 * HashSet、TreeSet分别默认维护一个HashMap、TreeMap。
 * <p>
 * https://blog.csdn.net/qq_29631809/article/details/72599708
 */

public class DataStructureActivity {
}
