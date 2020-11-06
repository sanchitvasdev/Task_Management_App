// Generated code from Butter Knife. Do not modify!
package com.sanchit.taskmanagment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ResetPasswordActivity_ViewBinding implements Unbinder {
  private ResetPasswordActivity target;

  @UiThread
  public ResetPasswordActivity_ViewBinding(ResetPasswordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ResetPasswordActivity_ViewBinding(ResetPasswordActivity target, View source) {
    this.target = target;

    target.inputEmail = Utils.findRequiredViewAsType(source, R.id.email, "field 'inputEmail'", EditText.class);
    target.btnReset = Utils.findRequiredViewAsType(source, R.id.btn_reset_password, "field 'btnReset'", Button.class);
    target.btnBack = Utils.findRequiredViewAsType(source, R.id.btn_back, "field 'btnBack'", Button.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ResetPasswordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inputEmail = null;
    target.btnReset = null;
    target.btnBack = null;
    target.progressBar = null;
  }
}
