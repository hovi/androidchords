package mock

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class JvmStatic

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
public annotation class JvmField

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
public annotation class JvmOverloads


object Math {
    fun min(a: Int, b: Int): Int {
        if (a < b) {
            return a
        }
        return b
    }
}

