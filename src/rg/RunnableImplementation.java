package rg;

import rg.out.WriterOfFiles;

public class RunnableImplementation implements Runnable {

 
    @Override
    public void run() {
        
    	
    	
     
        for (int i = 0; i < 10; i++) {
            
        	try {
        		WriteAnHTMLPage();
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
           
        }
    }
    
   public static void WriteAnHTMLPage(){
	   System.out.println("make an html page");
	   
	   
	   WriterOfFiles.writeAFile(rg.out.pageCreator.makeHTML());
	   
   }
    
}
