package com.yryc.imkit.core.slim;

import android.support.v7.widget.RecyclerView;

/**
 * Created by linshuaibin on 22/12/2016.
 */

abstract class AbstractSlimAdapter extends RecyclerView.Adapter<SlimViewHolder> {

    @Override
    public final void onBindViewHolder(SlimViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    protected abstract Object getItem(int position);

}
