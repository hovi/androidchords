package mock


typealias JvmStatic = kotlin.jvm.JvmStatic

typealias JvmField = kotlin.jvm.JvmField

typealias JvmOverloads = kotlin.jvm.JvmOverloads

typealias Math = java.lang.Math

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.FILE)
annotation class JsModule(val import: String)