package eu.karelhovorka.zpevnik.util


import mock.*

object Preconditions {

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if `expression` is false
     */
    @JvmStatic
    fun checkArgument(expression: Boolean) {
        if (!expression) {
            throw IllegalArgumentException()
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     * string using [String.valueOf]
     * @throws IllegalArgumentException if `expression` is false
     */
    fun checkArgument(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalArgumentException(errorMessage.toString())
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression           a boolean expression
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     * message is formed by replacing each `%s` placeholder in the template with an
     * argument. These are matched by position - the first `%s` gets `errorMessageArgs[0]`, etc. Unmatched arguments will be appended to the formatted message in
     * square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message template. Arguments
     * are converted to strings using [String.valueOf].
     * @throws IllegalArgumentException if `expression` is false
     * @throws NullPointerException     if the check fails and either `errorMessageTemplate` or
     * `errorMessageArgs` is null (don't let this happen)
     */
    @JvmStatic
    fun checkArgument(
            expression: Boolean,
            errorMessageTemplate: String,
            vararg errorMessageArgs: Any) {
        if (!expression) {
            throw IllegalArgumentException(format(errorMessageTemplate, *errorMessageArgs))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(b: Boolean, errorMessageTemplate: String, p1: Char) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(b: Boolean, errorMessageTemplate: String, p1: Int) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(b: Boolean, errorMessageTemplate: String, p1: Long) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Char) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Int) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Long) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Int, p2: Char) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Int, p2: Int) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Int, p2: Long) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Int, p2: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Char) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Int) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Long) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Char) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Int) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Long) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean,
            errorMessageTemplate: String,
            p1: Any,
            p2: Any,
            p3: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3))
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     *
     *
     * See [.checkArgument] for details.
     */
    fun checkArgument(
            b: Boolean,
            errorMessageTemplate: String,
            p1: Any,
            p2: Any,
            p3: Any,
            p4: Any) {
        if (!b) {
            throw IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3, p4))
        }
    }

    fun checkState(expression: Boolean) {
        if (!expression) {
            throw IllegalStateException()
        }
    }

    fun checkState(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalStateException(errorMessage.toString())
        }
    }

    fun checkState(
            expression: Boolean,
            errorMessageTemplate: String,
            vararg errorMessageArgs: Any) {
        if (!expression) {
            throw IllegalStateException(format(errorMessageTemplate, *errorMessageArgs))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Char) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Int) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Long) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Char) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Char, p2: Int) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Long) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Char, p2: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Int, p2: Char) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Int, p2: Int) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Int, p2: Long) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Int, p2: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Char) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(b: Boolean, errorMessageTemplate: String, p1: Long, p2: Int) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Long) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Long, p2: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Char) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Int) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Long) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean, errorMessageTemplate: String, p1: Any, p2: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean,
            errorMessageTemplate: String,
            p1: Any,
            p2: Any,
            p3: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2, p3))
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     *
     *
     * See [.checkState] for details.
     */
    fun checkState(
            b: Boolean,
            errorMessageTemplate: String,
            p1: Any,
            p2: Any,
            p3: Any,
            p4: Any) {
        if (!b) {
            throw IllegalStateException(format(errorMessageTemplate, p1, p2, p3, p4))
        }
    }

    @JvmStatic
    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }

    @JvmStatic
    fun <T> checkNotNull(reference: T?, errorMessage: Any): T {
        if (reference == null) {
            throw NullPointerException(errorMessage.toString())
        }
        return reference
    }

    fun <T> checkNotNull(
            reference: T?, errorMessageTemplate: String, vararg errorMessageArgs: Any): T {
        if (reference == null) {
            // If either of these parameters is null, the right thing happens anyway
            throw NullPointerException(format(errorMessageTemplate, *errorMessageArgs))
        }
        return reference
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Char): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Int): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Long): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Char, p2: Char): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Char, p2: Int): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Char, p2: Long): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Char, p2: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Int, p2: Char): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Int, p2: Int): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Int, p2: Long): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Int, p2: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Long, p2: Char): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Long, p2: Int): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(obj: T?, errorMessageTemplate: String, p1: Long, p2: Long): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Long, p2: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Any, p2: Char): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Any, p2: Int): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Any, p2: Long): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?, errorMessageTemplate: String, p1: Any, p2: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?,
            errorMessageTemplate: String,
            p1: Any,
            p2: Any,
            p3: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2, p3))
        }
        return obj
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     *
     *
     * See [.checkNotNull] for details.
     */

    fun <T> checkNotNull(
            obj: T?,
            errorMessageTemplate: String,
            p1: Any,
            p2: Any,
            p3: Any,
            p4: Any): T {
        if (obj == null) {
            throw NullPointerException(format(errorMessageTemplate, p1, p2, p3, p4))
        }
        return obj
    }

    /**
     * Ensures that `index` specifies a valid *element* in an array, list or string of size
     * `size`. An element index may range from zero, inclusive, to `size`, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @param desc  the text to use to describe this index in an error message
     * @return the value of `index`
     * @throws IndexOutOfBoundsException if `index` is negative or is not less than `size`
     * @throws IllegalArgumentException  if `size` is negative
     */

    @JvmOverloads
    fun checkElementIndex(index: Int, size: Int, desc: String = "index"): Int {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException(badElementIndex(index, size, desc))
        }
        return index
    }

    private fun badElementIndex(index: Int, size: Int, desc: String): String {
        return if (index < 0) {
            format("%s (%s) must not be negative", desc, index)
        } else if (size < 0) {
            throw IllegalArgumentException("negative size: " + size)
        } else { // index >= size
            format("%s (%s) must be less than size (%s)", desc, index, size)
        }
    }

    /**
     * Ensures that `index` specifies a valid *position* in an array, list or string of
     * size `size`. A position index may range from zero to `size`, inclusive.
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size  the size of that array, list or string
     * @param desc  the text to use to describe this index in an error message
     * @return the value of `index`
     * @throws IndexOutOfBoundsException if `index` is negative or is greater than `size`
     * @throws IllegalArgumentException  if `size` is negative
     */

    @JvmOverloads
    fun checkPositionIndex(index: Int, size: Int, desc: String = "index"): Int {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw IndexOutOfBoundsException(badPositionIndex(index, size, desc))
        }
        return index
    }

    private fun badPositionIndex(index: Int, size: Int, desc: String): String {
        return if (index < 0) {
            format("%s (%s) must not be negative", desc, index)
        } else if (size < 0) {
            throw IllegalArgumentException("negative size: " + size)
        } else { // index > size
            format("%s (%s) must not be greater than size (%s)", desc, index, size)
        }
    }

    /**
     * Ensures that `start` and `end` specify a valid *positions* in an array, list
     * or string of size `size`, and are in order. A position index may range from zero to
     * `size`, inclusive.
     *
     * @param start a user-supplied index identifying a starting position in an array, list or string
     * @param end   a user-supplied index identifying a ending position in an array, list or string
     * @param size  the size of that array, list or string
     * @throws IndexOutOfBoundsException if either index is negative or is greater than `size`,
     * or if `end` is less than `start`
     * @throws IllegalArgumentException  if `size` is negative
     */
    fun checkPositionIndexes(start: Int, end: Int, size: Int) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (start < 0 || end < start || end > size) {
            throw IndexOutOfBoundsException(badPositionIndexes(start, end, size))
        }
    }

    private fun badPositionIndexes(start: Int, end: Int, size: Int): String {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index")
        }
        return if (end < 0 || end > size) {
            badPositionIndex(end, size, "end index")
        } else format("end index (%s) must not be less than start index (%s)", end, start)
        // end < start
    }

    /**
     * Substitutes each `%s` in `template` with an argument. These are matched by
     * position: the first `%s` gets `args[0]`, etc. If there are more arguments than
     * placeholders, the unmatched arguments will be appended to the end of the formatted message in
     * square braces.
     *
     * @param template a non-null string containing 0 or more `%s` placeholders.
     * @param args     the arguments to be substituted into the message template. Arguments are converted
     * to strings using [String.valueOf]. Arguments can be null.
     */
    // Note that this is somewhat-improperly used from Verify.java as well.
    internal fun format(template_: String, vararg args: Any): String {
        var template = template_
        template = template.toString() // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        val builder = StringBuilder(template.length + 16 * args.size)
        var templateStart = 0
        var i = 0
        while (i < args.size) {
            val placeholderStart = template.indexOf("%s", templateStart)
            if (placeholderStart == -1) {
                break
            }
            builder.append(template, templateStart, placeholderStart)
            builder.append(args[i++])
            templateStart = placeholderStart + 2
        }
        builder.append(template, templateStart, template.length)

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.size) {
            builder.append(" [")
            builder.append(args[i++])
            while (i < args.size) {
                builder.append(", ")
                builder.append(args[i++])
            }
            builder.append(']')
        }

        return builder.toString()
    }
}/*
   * All recent hotspots (as of 2009) *really* like to have the natural code
   *
   * if (guardExpression) {
   *    throw new BadException(messageExpression);
   * }
   *
   * refactored so that messageExpression is moved to a separate String-returning method.
   *
   * if (guardExpression) {
   *    throw new BadException(badMsg(...));
   * }
   *
   * The alternative natural refactorings into void or Exception-returning methods are much slower.
   * This is a big deal - we're talking factors of 2-8 in microbenchmarks, not just 10-20%. (This is
   * a hotspot optimizer bug, which should be fixed, but that's a separate, big project).
   *
   * The coding pattern above is heavily used in java.util, e.g. in ArrayList. There is a
   * RangeCheckMicroBenchmark in the JDK that was used to test this.
   *
   * But the methods in this class want to throw different exceptions, depending on the args, so it
   * appears that this pattern is not directly applicable. But we can use the ridiculous, devious
   * trick of throwing an exception in the middle of the construction of another exception. Hotspot
   * is fine with that.
   */
/**
 * Ensures that `index` specifies a valid *element* in an array, list or string of size
 * `size`. An element index may range from zero, inclusive, to `size`, exclusive.
 *
 * @param index a user-supplied index identifying an element of an array, list or string
 * @param size  the size of that array, list or string
 * @return the value of `index`
 * @throws IndexOutOfBoundsException if `index` is negative or is not less than `size`
 * @throws IllegalArgumentException  if `size` is negative
 */
/**
 * Ensures that `index` specifies a valid *position* in an array, list or string of
 * size `size`. A position index may range from zero to `size`, inclusive.
 *
 * @param index a user-supplied index identifying a position in an array, list or string
 * @param size  the size of that array, list or string
 * @return the value of `index`
 * @throws IndexOutOfBoundsException if `index` is negative or is greater than `size`
 * @throws IllegalArgumentException  if `size` is negative
 */