package com.xh.study;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class Interceptor {
    @RuntimeType
    public Object intercept(@Origin Class<?> clazz, @AllArguments Object[] allArguments, @Origin Method method,
                            @SuperCall Callable<?> zuper) throws Throwable {
        System.out.println("---[BEGIN] SimplePackageStaticMethodInterceptor");
        Object ret = null;
        try {
            System.out.println("    class name = " + clazz.getName());
            System.out.println("    method name = " + method.getName());
            ret = zuper.call();
        } catch (Throwable t) {
            throw t;
        } finally {
            System.out.println("---[END] SimplePackageStaticMethodInterceptor");
        }
        return ret;
    }
}
