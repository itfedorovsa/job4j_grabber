package ru.job4j.grabber;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Grab interface
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public interface Grab {

    /**
     * Init scheduler
     *
     * @param parse     Parse implementation. Type {@link ru.job4j.grabber.Parse>}
     * @param store     Store implementation. Type {@link ru.job4j.grabber.Store>}
     * @param scheduler Scheduler implementation. Type {@link org.quartz.Scheduler>}
     * @throws SchedulerException SchedulerException
     */
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;

}
