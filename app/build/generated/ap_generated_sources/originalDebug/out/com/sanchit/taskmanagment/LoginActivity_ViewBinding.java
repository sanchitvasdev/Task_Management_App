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
import com.google.android.gms.common.SignInButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.inputEmail = Utils.findRequiredViewAsType(source, R.id.email, "field 'inputEmail'", EditText.class);
    target.signInButton = Utils.findRequiredViewAsType(source, R.id.sign_in_button_google, "field 'signInButton'", SignInButton.class);
    target.inputPassword = Utils.findRequiredViewAsType(source, R.id.password, "field 'inputPassword'", EditText.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    target.btnLogin = Utils.findRequiredViewAsType(source, R.id.btn_login, "field 'btnLogin'", Button.class);
    target.btnSignup = Utils.findRequiredViewAsType(source, R.id.btn_signup, "field 'btnSignup'", Button.class);
    target.btnReset = Utils.findRequiredViewAsType(source, R.id.btn_reset_password, "field 'btnReset'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inputEmail = null;
    target.signInButton = null;
    target.inputPassword = null;
    target.progressBar = null;
    target.btnLogin = null;
    target.btnSignup = null;
    target.btnReset = null;
  }
}
