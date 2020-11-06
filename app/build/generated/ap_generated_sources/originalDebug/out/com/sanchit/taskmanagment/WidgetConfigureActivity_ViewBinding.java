// Generated code from Butter Knife. Do not modify!
package com.sanchit.taskmanagment;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WidgetConfigureActivity_ViewBinding implements Unbinder {
  private WidgetConfigureActivity target;

  @UiThread
  public WidgetConfigureActivity_ViewBinding(WidgetConfigureActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WidgetConfigureActivity_ViewBinding(WidgetConfigureActivity target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.no_listsTV = Utils.findRequiredViewAsType(source, R.id.no_listsTV, "field 'no_listsTV'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WidgetConfigureActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.toolbar = null;
    target.no_listsTV = null;
  }
}
