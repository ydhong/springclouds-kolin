package com.umeet.conference.util
import java.lang.reflect.InvocationTargetException
import org.apache.commons.beanutils.BeanUtilsBean


object NullAwareBeanUtilsBean : BeanUtilsBean() {
    @Throws(IllegalAccessException::class, InvocationTargetException::class)
    override fun copyProperty(dest: Any, name: String, value: Any?) {
        if (value == null)
            return

        super.copyProperty(dest, name, value)
    }
}
