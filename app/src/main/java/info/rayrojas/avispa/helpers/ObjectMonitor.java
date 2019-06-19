package info.rayrojas.avispa.helpers;

import java.util.Observable;

import info.rayrojas.avispa.models.Channel;

public class ObjectMonitor extends Observable {
    Channel wachedValue;
    public ObjectMonitor(Channel o){
        this.wachedValue = o;
    }
    public void setWachedValue(Channel o) {
        if ( o!= null && !o.getName().equals(this.wachedValue.getName())) {
            this.wachedValue = o;
            setChanged();
            notifyObservers(o);
        } else {
            setChanged();
            // trigger notification
            notifyObservers(o);
        }
    }

}
