package code
package snippet

import config.UserSite
import model._
import scala.xml._
import net.liftweb._
import common._
import http.{ LiftScreen, S }
import util.FieldError
import util.Helpers._
import http.js.JsCmd
import http.js.JsCmds._
import net.liftmodules.extras.Gravatar
import net.liftmodules.mongoauth.model.ExtSession
import net.liftmodules.mongoauth.LoginRedirect

/*
 * Use for editing the currently logged in user only.
 */
sealed trait BaseCurrentUserScreen extends BaseScreen {
  object userVar extends ScreenVar(User.currentUser.openOr(User.createRecord))

  override def localSetup {
    Referer(UserSite.home.url)
  }
}

object PasswordScreen extends BaseCurrentUserScreen {

  override def validations = RegisterScreen.passwordsMustMatch _ :: super.validations
  val passwordField = password(S ? "Password", "",
    trim,
    valMinLen(RegisterScreen.pwdMinLength, "Password must be at least " + RegisterScreen.pwdMinLength + " characters"),
    valMaxLen(RegisterScreen.pwdMaxLength, "Password must be " + RegisterScreen.pwdMaxLength + " characters or less"),

    "class" -> "form-control")
  val confirmPasswordField = password(S ? "Password", "",
    trim,
    valMinLen(6, "Password too short"), "class" -> "form-control")
  def finish() {
    userVar.is.password(passwordField.is)
    userVar.is.password.hashIt
    userVar.is.save()
    S.notice("New password saved")
  }
}

object LoginScreen extends BaseCurrentUserScreen {
  val email = field(S ? "Email address", "",
    trim,
    valMinLen(2, "Name too short"),
    valMaxLen(40, "That's a long name"), "class" -> "form-control")
  val havePass = field("Do you have password?", true)

  val passWord = password(S ? "Password", "",
    trim,
     "class" -> "form-control")

  val savePassWord = field(S ? "Save password?", false)

  def finish() {

    User.findByEmail(email) match {
      case Full(user) => {
        if (!havePass) {
          User.sendLoginToken(user)
          User.loginCredentials.remove()
          S.notice("An email has been sent to you with instructions for accessing your account")
          Noop
        } else if (user.password.isMatch(passWord)) {
          //   logger.debug("pwd matched")
          User.logUserIn(user, true)
          if (savePassWord) User.createExtSession(user.id.get)
          else ExtSession.deleteExtCookie()
          RedirectTo(LoginRedirect.openOr("/"))
        } else {
          S.error("Invalid credentials")
          Noop
        }
      }

      case _ =>
        S.error("Invalid credentials")
        Noop
    }
  }
}

/*
 * Use for creating a new user.
 */
object RegisterScreen extends BaseCurrentUserScreen {

  override def validations = passwordsMustMatch _ :: super.validations
  val pwdMinLength = 6
  val pwdMaxLength = 34
  val userName = field(S ? "Username","",
    trim,
    valMinLen(6, "Name too short"), "class" -> "form-control")
  val email = field(S ? "Email address", "",
    trim,
    valMinLen(2, "Name too short"),
    valMaxLen(40, "That's a long name"), "class" -> "form-control")
  val passwordField = password(S ? "Password", "",
    trim,
    valMinLen(pwdMinLength, "Password must be at least " + pwdMinLength + " characters"),
    valMaxLen(pwdMaxLength, "Password must be " + pwdMaxLength + " characters or less"),
    "class" -> "form-control")
  val confirmPasswordField = password(S ? "Password", "",
    trim,
    valMinLen(6, "Password too short"), "class" -> "form-control")
  val rememberMe = field(S ? "Save password?", User.loginCredentials.is.isRememberMe)

  override def localSetup {
    Referer(UserSite.home.url)
  }
  def passwordsMustMatch(): Errors = {
    if (passwordField.is != confirmPasswordField.is)
      List(FieldError(confirmPasswordField, "Passwords must match"))
    else if( User.emailExists(email.is.toString())) {
       List(FieldError(email, "Email already exists, please go to the login page for the remainder email"))
    }
    else if( User.userNameExists(userName.is.toString())) {
       List(FieldError(userName, "username already exists, please change your username"))
    }
    else Nil   
  }

  def finish() { 
    val user = userVar.is
    user.username(userName.is)
    user.email(email.is)
    user.password(passwordField.is)
    user.password.hashIt
    user.save()
    User.logUserIn(user, true)
    if (rememberMe) User.createExtSession(user.id.get)
    S.notice("Thanks for signing up!")
  }
}
