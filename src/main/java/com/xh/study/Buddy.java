package com.xh.study;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Buddy {
    public static void main(String[] args) {
//        DynamicType.Unloaded<TestClass> unloaded = new ByteBuddy()
//                .subclass(TestClass.class).name(TestClass.class.getName()+"$XH")
//                .method(ElementMatchers.isToString())
//                .intercept(FixedValue.value("dadadadad"))
//                .make();
//        DynamicType.Unloaded unloaded = new ByteBuddy()
//                .subclass(TestClass.class)
//                .name(TestClass.class.getName() + "$XH")
//                .method(ElementMatchers.named("toString"))
//                .intercept(Advice.to(LoggerAdvisor.class))
//                .make();
//        DynamicType.Unloaded unloaded = new ByteBuddy()
//                .subclass(TestClass.class)
//                .name(TestClass.class.getName() + "$XH")
//                .method(ElementMatchers.named("toString"))
//                .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
//                    TestClass testClass = new TestClass();
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        System.out.println("开始");
//                        Object o = method.invoke(testClass, args);
//                        System.out.println("结束");
//                        return o;
//                    }
//                }))
//                .make();
//        DynamicType.Unloaded unloaded = new ByteBuddy()
//                .subclass(TestClass.class)
//                .name(TestClass.class.getName() + "$XH")
//                .method(ElementMatchers.named("toString"))
//                .intercept(MethodDelegation.to(new SingerAgentInterceptor()))
//                .make();

        try {
            TestClass  classClass = createByteBuddyDynamicProxy();
            classClass.toString();
            System.out.println(classClass);
//            System.out.println(classClass.newInstance().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        test();
    }


    public static void test() {
        System.out.println("test");
    }

    static class LoggerAdvisor {
        @Advice.OnMethodEnter
        public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
            System.out.println("onMethodEnter");
        }

        @Advice.OnMethodExit
        public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
            System.out.println("onMethodExit");
        }
    }

    private static TestClass createByteBuddyDynamicProxy() throws Exception {
        return (TestClass) new ByteBuddy().subclass(TestClass.class)
//                .implement(TestClass.class)
                .method(ElementMatchers.named("toString"))
                .intercept(MethodDelegation.to(new SingerAgentInterceptor(new TestClass())))
                .make()
                .load(TestClass.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public static class SingerAgentInterceptor {

        private Object delegate;

        public SingerAgentInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        /**
         * @param proxy 代理对象
         * @param method 代理方法
         * @param args 方法参数
         */
        public Object interceptor(@This Object proxy, @Origin Method method,
                                  @AllArguments Object[] args) throws Exception {
            System.out.println("bytebuddy delegate proxy before sing ");
            Object ret = method.invoke(delegate, args);
            System.out.println("bytebuddy delegate proxy after sing ");
            return ret;
        }
    }

    public static class SingerAgentInterceptor1 {

        public Object interceptor(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Exception {
            System.out.println("bytebuddy delegate proxy2 before sing ");
            Object ret = method.invoke(proxy, args);
            System.out.println("bytebuddy delegate proxy2 after sing ");
            return ret;
        }
    }
}
