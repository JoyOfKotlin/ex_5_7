/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package ex_5_7

sealed class List<out A> {
    abstract fun isEmpty(): Boolean
    fun cons(a: @UnsafeVariance A): List<A> = Cons(a, this)
    fun setHead(a: @UnsafeVariance A): List<A> = when (this) {
        Nil -> throw IllegalStateException("setHead called on an empty list")
        is Cons -> tail.cons(a)
    }

    fun drop(n: Int): List<A> {
        tailrec fun drop(n: Int, list: List<A>): List<A> =
            if (n <= 0) list else when (list) {
                is Cons -> drop(n - 1, list.tail)
                is Nil -> list
            }
        return drop(n, this)
    }

    fun dropWhile(p: (A) -> Boolean): List<A> =
        dropWhile(this, p)

    fun concat(list: List<@UnsafeVariance A>): List<A> = concat(this, list)

    fun reverse (): List<A> =
        reverse(List.invoke(),this)

    fun init(): List<A> = reverse().drop(1).reverse()

    fun <B> foldRight(identity : B, f: (A,B)->B): B =  foldRight(this, identity,f)

    fun <B> foldLeft(identity: B, f: (B)-> (A)->B): B = foldLeft(identity, this, f)

    fun lengthRight(): Int= foldRight(0){_,x ->x+1 }

    internal object Nil : List<Nothing>() {
        override fun isEmpty() = true
        override fun toString(): String = "[NIL]"
    }

    internal class Cons<A>(internal val head: A, internal val tail: List<A>) : List<A>() {
        override fun isEmpty() = false
        override fun toString(): String = "[${toString("", this)}NIL]"
        private tailrec fun toString(acc: String, list: List<A>): String =
            when (list) {
                is Nil -> acc
                is Cons -> toString("$acc${list.head},", list.tail)
            }
    }

    companion object {
        operator fun <A> invoke(vararg az: A): List<A> =
            az.foldRight(Nil as List<A>) { a: A, list: List<A> -> Cons(a, list) }

        private tailrec fun <A> dropWhile(list: List<A>, p: (A) -> Boolean): List<A> =
            when (list) {
                Nil -> list
                is Cons -> if (p(list.head)) dropWhile(list.tail, p) else list
            }

        private fun <A> concat(list1: List<A>, list2: List<A>): List<A> =
            when (list1) {
                Nil -> list2
                is Cons -> concat(list1.tail, list2).cons(list1.head)
            }

        private fun <A> reverse(acc: List<A>, list: List<A>): List<A> =
            when (list){
                Nil-> acc
                is Cons -> reverse(acc.cons(list.head),list.tail)
            }

        fun <A,B>   foldRight(list: List<A>, identity : B, f: (A,B)->B) : B =
            when (list) {
                List.Nil ->identity
                is List.Cons -> f(list.head, foldRight(list.tail,identity,f))
            }

        tailrec fun <A,B> foldLeft(acc:B, list: List<A>, f:(B)->(A)->B): B =
            when (list) {
                List.Nil -> acc
                is List.Cons -> foldLeft(f(acc)(list.head),list.tail,f)
            }
    }
}

class App {
    val greeting: String get() {  return "ex_5_7"     }
}

fun sum(ints: List<Int>): Int = when (ints) {
    List.Nil -> 0
    is List.Cons -> ints.head + sum(ints.tail)
}

fun product (ints: List<Int>) : Int = when (ints) {
    List.Nil -> 1
    is List.Cons -> ints.head * product(ints.tail)
}



fun sumRight(list : List<Int>)  : Int =
//    foldRight(list,0) { x, y -> x + y }
    list.foldRight(0,){ x, y -> x + y }

fun productRight(list : List<Int>)  : Int =
//    foldRight(list,1) { x, y -> x * y }
    list.foldRight(1,){ x, y -> x * y }

fun main() {
    println(sum(List(1,2,3,4,5,6,7,8,9,10)))
    println(product(List(1,2,3,4,5,6,7,8,9,10)))
    val right_list=List(1,2,3,4,5,6,7,8,9,10)
    println(sumRight(right_list))
    println(productRight(right_list))
    println(right_list.lengthRight())
}