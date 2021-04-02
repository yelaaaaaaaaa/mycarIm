package com.yryc.imkit.core.slim.viewinjector;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.yryc.imkit.core.slim.SlimViewHolder;


/**
 * Created by linshuaibin on 22/12/2016.
 */
public class DefaultViewInjector implements IViewInjector<com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector> {

    private SlimViewHolder viewHolder;

    public DefaultViewInjector(SlimViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    @Override
    public final <T extends View> T findViewById(int id) {
        return (T) viewHolder.id(id);
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector tag(int id, Object object) {
        findViewById(id).setTag(object);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector text(int id, int res) {
        TextView view = findViewById(id);
        view.setText(res);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector text(int id, CharSequence charSequence) {
        TextView view = findViewById(id);
        view.setText(charSequence);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector typeface(int id, Typeface typeface, int style) {
        TextView view = findViewById(id);
        view.setTypeface(typeface, style);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector typeface(int id, Typeface typeface) {
        TextView view = findViewById(id);
        view.setTypeface(typeface);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector textColor(int id, int color) {
        TextView view = findViewById(id);
        view.setTextColor(color);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector textSize(int id, int sp) {
        TextView view = findViewById(id);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector alpha(int id, float alpha) {
        View view = findViewById(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        }
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector image(int id, int res) {
        ImageView view = findViewById(id);
        view.setImageResource(res);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector image(int id, Drawable drawable) {
        ImageView view = findViewById(id);
        view.setImageDrawable(drawable);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector background(int id, int res) {
        View view = findViewById(id);
        view.setBackgroundResource(res);
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector background(int id, Drawable drawable) {
        View view = findViewById(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector visible(int id) {
        findViewById(id).setVisibility(View.VISIBLE);
        return this;
    }


    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector invisible(int id) {
        findViewById(id).setVisibility(View.INVISIBLE);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector gone(int id) {
        findViewById(id).setVisibility(View.GONE);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector visibility(int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return this;
    }

    @Override
    public <V extends View> com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector with(int id, Action<V> action) {
        action.action((V) findViewById(id));
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector clicked(int id, View.OnClickListener listener) {
        findViewById(id).setOnClickListener(listener);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector longClicked(int id, View.OnLongClickListener listener) {
        findViewById(id).setOnLongClickListener(listener);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector enable(int id, boolean enable) {
        findViewById(id).setEnabled(enable);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector enable(int id) {
        findViewById(id).setEnabled(true);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector disable(int id) {
        findViewById(id).setEnabled(false);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector checked(int id, boolean checked) {
        Checkable view = findViewById(id);
        view.setChecked(checked);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector selected(int id, boolean selected) {
        findViewById(id).setSelected(selected);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector pressed(int id, boolean pressed) {
        findViewById(id).setPressed(pressed);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector adapter(int id, RecyclerView.Adapter adapter) {
        RecyclerView view = findViewById(id);
        view.setAdapter(adapter);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector adapter(int id, Adapter adapter) {
        AdapterView view = findViewById(id);
        view.setAdapter(adapter);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector layoutManager(int id, RecyclerView.LayoutManager layoutManager) {
        RecyclerView view = findViewById(id);
        view.setLayoutManager(layoutManager);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector addView(int id, View... views) {
        ViewGroup viewGroup = findViewById(id);
        for (View view : views) {
            viewGroup.addView(view);
        }
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector addView(int id, View view, ViewGroup.LayoutParams params) {
        ViewGroup viewGroup = findViewById(id);
        viewGroup.addView(view, params);
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector removeAllViews(int id) {
        ViewGroup viewGroup = findViewById(id);
        viewGroup.removeAllViews();
        return this;
    }

    @Override
    public com.yryc.imkit.core.slim.viewinjector.DefaultViewInjector removeView(int id, View view) {
        ViewGroup viewGroup = findViewById(id);
        viewGroup.removeView(view);
        return this;
    }
}
