package me.imdanix.drop;

import java.util.Collection;

public class RestrictList<T> {
    private final Collection<T> col;
    private final boolean blacklist;

    public RestrictList(Collection<T> col, boolean blacklist) {
        this.col = col;
        this.blacklist = blacklist;
    }

    public boolean isRestricted(T t) {
        return col.contains(t) == blacklist;
    }
}
