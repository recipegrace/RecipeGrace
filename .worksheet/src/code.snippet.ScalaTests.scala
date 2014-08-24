package code.snippet

object ScalaTests {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(85); 
  println("Welcome to the Scala worksheet");$skip(22); 
  val str = "(hello)";System.out.println("""str  : String = """ + $show(str ));$skip(40); val res$0 = 
  
    str.substring(1, str.length()-1);System.out.println("""res0: String = """ + $show(res$0))}
 
}
