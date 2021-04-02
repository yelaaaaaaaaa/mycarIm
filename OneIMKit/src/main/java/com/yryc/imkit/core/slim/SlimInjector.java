package com.yryc.imkit.core.slim;

import com.yryc.imkit.core.slim.viewinjector.IViewInjector;

/**
 * Created by linshuaibin on 01/04/2017.
 */

public interface SlimInjector<T> {
    void onInject(T data, IViewInjector injector);
}
