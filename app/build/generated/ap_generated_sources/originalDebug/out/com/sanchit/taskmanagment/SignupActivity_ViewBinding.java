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

public class SignupActivity_ViewBinding implements Unbinder {
  private SignupActivity target;

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target, View source) {
    this.target = target;

    target.inputEmail = Utils.findRequiredViewAsType(source, R.id.email, "field 'inputEmail'", EditText.class);
    target.inputName = Utils.findRequiredViewAsType(source, R.id.name, "field 'inputName'", EditText.class);
    target.inputPassword = Utils.findRequiredViewAsType(source, R.id.password, "field 'inputPassword'", EditText.class);
    target.btnSignIn = Utils.findRequiredViewAsType(source, R.id.sign_in_button, "field 'btnSignIn'", Button.class);
    target.btnSignUp = Utils.findRequiredViewAsType(source, R.id.sign_up_button, "field 'btnSignUp'", Button.class);
    target.btnReset = Utils.findRequiredViewAsType(source, R.id.btn_reset_password, "field 'btnReset'", Button.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignupActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inputEmail = null;
    target.inputName = null;
    target.inputPassword = null;
    target.btnSignIn = null;
    target.btnSignUp = null;
    target.btnReset = null;
    target.progressBar = null;
  }
}
