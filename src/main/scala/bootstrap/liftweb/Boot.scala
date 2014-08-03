package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import mapper._
import code.model._
import net.liftmodules.FoBo
import scala.language.postfixOps
import sitemap._
import sitemap.Loc._
import sitemap._
import sitemap.Loc._
import net.liftmodules.mongoauth.Locs
import net.liftmodules.mongoauth.MongoAuth
import code.config.MongoConfig
import scala.util.Try
/**

 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

     MongoConfig.init() 
//    MongoAuth.authUserMeta.default.set(User)
 //   MongoAuth.loginTokenAfterUrl.default.set(Site.password.url)
 //   MongoAuth.siteName.default.set("LiftMongoTest")
//    MongoAuth.systemEmail.default.set(SystemUser.user.email.get)
//    MongoAuth.systemUsername.default.set(SystemUser.user.name.get)
    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    //Schemifier.schemify(true, Schemifier.infoF _, User)

    // where to search snippet
    LiftRules.addToPackages("code")



    LiftRules.setSiteMap(Site.sitemap)

    //Init the FoBo - Front-End Toolkit module, 
    //see http://liftweb.net/lift_modules for more info
    FoBo.InitParam.JQuery=FoBo.JQuery1102  
    FoBo.InitParam.ToolKit=FoBo.Bootstrap311 
    FoBo.init() 
    
    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.isLoggedIn)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) => 
      new Html5Properties(r.userAgent))    
      
    LiftRules.noticesAutoFadeOut.default.set( (notices: NoticeType.Value) => {
        notices match {
          case NoticeType.Notice => Full((8 seconds, 4 seconds))
          case _ => Empty
        }
     }
    )
    bootstrap.liftmodules.GoogleAnalytics.init 
    
   
  }
  
  object Site extends Locs {
    
    import scala.xml._
    val divider1   = Menu("divider1") / "divider1"
    val ddLabel1   = Menu.i("UserDDLabel") / "ddlabel1"
    val home       = Menu.i("Home") / "index" 
    
    val isAdmin = If(() => Try{User.currentUser.get.email.toString== "feroshjacob@gmail.com"}.getOrElse(false),
                  () => RedirectResponse("/login"))
 val loginToken = buildLoginTokenMenu
  val logout =buildLogoutMenu
   val listUsers = Menu.i("Users") / "admin" / "listusers" >> isAdmin 
    val password = Menu.i("Password") / "settings" / "password" >> RequireLoggedIn 
  val register =  Menu.i("Register") / "register" >> RequireNotLoggedIn
  val listRecipes =  Menu.i("Recipes") / "recipe" /"list" >> RequireNotLoggedIn
  
  

   val static     = Menu(Loc("Static", Link(List("static"), true, "/static/contactus"), S.loc("Contact Us" , scala.xml.Text("Contact Us")),LocGroup("lg2","topRight")))
  //  val twbs       = Menu(Loc("Bootstrap3", Link(List("bootstrap301"), true, "/bootstrap301/index"), S.loc("Bootstrap3" , scala.xml.Text("Bootstrap3")),LocGroup("lg2")))
     
    def sitemap = SiteMap(
        home          >> LocGroup("lg1"),
        static,
        
  //      twbs,
        ddLabel1      >> LocGroup("topRight") >> PlaceHolder submenus (
           // divider1  >> FoBo.TBLocInfo.Divider >> profileParamMenu
            password, register,listUsers,listRecipes,
             Menu.i("Login") / "login" >> RequireNotLoggedIn,
            logout
            )
         )
  }
  
}
