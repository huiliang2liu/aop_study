package com.xh.study;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

public class Client3 {

    public static void main(String[] args) throws Exception {
        Singable proxy = createByteBuddyDynamicProxy();
        proxy.sing();
        System.out.println(proxy.toString());
    }

    private static Singable createByteBuddyDynamicProxy() throws Exception {
        return (Singable) new ByteBuddy().subclass(Singer.class)
                .implement(Singable.class)
                .method(ElementMatchers.named("sing"))
                .intercept(MethodDelegation.to(new SingerAgentInterceptor()))
                .make()
                .load(Client3.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public static class SingerAgentInterceptor {

        public Object interceptor(@This Object proxy, @Origin Method method,
                                  @SuperMethod Method superMethod,
                                  @AllArguments Object[] args) throws Exception {
            System.out.println("bytebuddy delegate proxy2 before sing ");
            Object ret = superMethod.invoke(proxy, args);
            System.out.println("bytebuddy delegate proxy2 after sing ");
            return ret;
        }
    }

}