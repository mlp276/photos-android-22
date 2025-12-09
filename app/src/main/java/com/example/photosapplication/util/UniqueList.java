package com.example.photosapplication.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * UniqueList - extends the ArrayList object, but to only include unique elements
 * The adding methods of List are modified so that they are only added into the list
 * based on the comparison of the elements in the list and the element to be added
 * based on the equals(Object o) method
 */
public class UniqueList<E> extends ArrayList<E> {
    
    /**
     * Adds the element to the list, making sure it is a unique element
     * 
     * @param e the element to be added
     * @return a boolean value indicating a successful addition of the element
     */
    public boolean add(E e) {
        if (this.contains(e)) {
            return false;
        }
        return super.add(e);
    }

    /**
     * Adds the collection of elements to the list, making sure the elements in the collection
     * are unique to the list
     * 
     * @param c the collection of elements to be added
     * @return a boolean value indicating a successful addition of the collection of elements
     */
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (this.add(e)) {
                modified = true;
            }
        }
        return modified;
    }
}
