package com.michalporeba.golp;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Third implementation from Allen Holub's book of a Publisher class
 * Modified to use generics (which were added to the language in the
 * same year as when the book was first published)
 *
 * I'm intentionally not using the internally defined linked list but
 * rather I'm using existing types and ignore the need to unsubscribe
 * for now, to help me focus on the patterns used and java syntax.
 */
class Publisher {

    interface Distributor {
        void deliverTo(Object subscriber); //the Visitor pattern's "visit" method
    }

    private class Subscription {
        private final Object subscriber;
        public Subscription(Object subscriber) {
            this.subscriber = subscriber;
        }

        public void accept(Distributor distributor) { //the Visitor pattern's "visitor"
            distributor.deliverTo(subscriber);
        }
    }

    private boolean isUpToDate = false;
    private Collection<Subscription> subscribers = new LinkedList<>();
    private Collection<Subscription> subscribersCopy;

    synchronized public void subscribe(Object subscriber) {
        subscribers.add(new Subscription(subscriber));
        isUpToDate = false;
    }

    public void publish(Distributor distributor) {
        ensureSubscribersCopyIsUpToDate();
        for(Subscription s : subscribersCopy) {
            // visit all of the subscriptions
            s.accept(distributor);
        }
    }

    private void ensureSubscribersCopyIsUpToDate() {
        synchronized(this) {
            if (!isUpToDate) {
                // that is ugly, but for now that's the limit of my Java understanding
                subscribersCopy = (Collection)((LinkedList) subscribers).clone();
            }
        }
    }
}
