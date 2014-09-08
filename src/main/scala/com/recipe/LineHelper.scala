package com.recipe

trait LineHelper {  
 
    
     def mergeNonEmptyLines(lines:List[String]) = {
      lines.foldLeft(List():List[String])((p,q)=>{
        if(q.trim().length() > 0)  q.trim::p
        else p
        
      })
   }
}