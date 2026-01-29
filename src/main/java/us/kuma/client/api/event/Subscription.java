/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.event;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author xgraza
 * @since 1/26/26
 */
final class Subscription
{
    static final Map<Method, Consumer<Object>> invokableCache = new HashMap<>();
    static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    final Object parent;
    final Method method;
    final Subscribe metadata;

    final Consumer<Object> consumer;

    @SuppressWarnings("unchecked") Subscription(Object parent, Method method, Subscribe metadata)
    {
        this.parent = parent;
        this.method = method;
        this.metadata = metadata;


        if (!invokableCache.containsKey(method))
        {
            // this part isnt my code, this is from bush's event bus
            // this invocation method is faster than plain reflection
            MethodType methodType = MethodType.methodType(Consumer.class);
            CallSite callSite;
            try
            {
                callSite = LambdaMetafactory.metafactory(
                        LOOKUP,
                        "accept",
                        methodType.appendParameterTypes(parent.getClass()),
                        MethodType.methodType(void.class, Object.class),
                        LOOKUP.unreflect(method),
                        MethodType.methodType(void.class, method.getParameterTypes()[0])
                );
            } catch (final LambdaConversionException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            try
            {
                consumer = (Consumer<Object>) callSite.getTarget().invoke(parent);
            } catch (final Throwable e)
            {
                throw new RuntimeException(e);
            }
            invokableCache.put(method, consumer);
        } else
        {
            consumer = invokableCache.get(method);
        }
    }

    void invokeEvent(final Object event)
    {
        consumer.accept(event);
    }

    Object getParent()
    {
        return parent;
    }

    Method getMethod()
    {
        return method;
    }

    Subscribe getMetadata()
    {
        return metadata;
    }
}
