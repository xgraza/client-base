/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class EventBus
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);

    private final Map<Class<?>, List<Subscription>> eventSubscriberMap = new ConcurrentHashMap<>();
    private final Set<Object> subscribedListenerSet = new HashSet<>();

    public boolean post(final Object event)
    {
        final List<Subscription> subscriptions = eventSubscriberMap.get(event.getClass());
        if (subscriptions == null || subscriptions.isEmpty())
        {
            return false;
        }

        boolean canceled = false;
        for (final Subscription subscription : subscriptions)
        {
            if (!subscription.getMetadata().receiveCanceled() && canceled)
            {
                continue;
            }
            try
            {
                subscription.invokeEvent(event);
            } catch (final Throwable throwable)
            {
                LOGGER.error("Failed to post {}#{}({}):",
                        subscription.getParent().getClass().getName(),
                        subscription.getMethod().getName(),
                        event.getClass().getSimpleName());
                LOGGER.error("Threw exception", throwable);
            }
            if (event instanceof Cancelable cancelable && cancelable.isCanceled())
            {
                canceled = true;
            }
        }

        return canceled;
    }

    public void register(final Object object)
    {
        if (!subscribedListenerSet.add(object))
        {
            return;
        }
        final Method[] methods = object.getClass().getDeclaredMethods();
        for (final Method method : methods)
        {
            if (!void.class.isAssignableFrom(method.getReturnType())
                    || method.getParameterCount() != 1
                    || !method.isAnnotationPresent(Subscribe.class)
                    || !method.trySetAccessible())
            {
                continue;
            }
            registerListener(method.getParameterTypes()[0],
                    new Subscription(object, method, method.getDeclaredAnnotation(Subscribe.class)));
        }
    }

    public void deregister(final Object object)
    {
        if (!subscribedListenerSet.remove(object))
        {
            return;
        }
        for (final List<Subscription> subscriptions : eventSubscriberMap.values())
        {
            for (Subscription subscription : subscriptions)
            {
                if (!subscription.getParent().equals(object))
                {
                    continue;
                }
                subscriptions.remove(subscription);
            }
        }
    }

    private void registerListener(Class<?> eventClass, Subscription subscription)
    {
        eventSubscriberMap.computeIfAbsent(
                eventClass, (x) -> new ArrayList<>()).add(subscription);
    }
}
