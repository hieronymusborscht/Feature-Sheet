package rg;

import rg.out.WriterOfFiles;

public class RunnableImplementation implements Runnable {

 
    @Override
    public void run() {
        
    	
    	
    	
    	
     
        for (int i = 0; i < 10; i++) {
        	try {
        		WriteAnHTMLPage("the content here", "shane", "page.html");
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
        
        
        
    }
    
   public static void WriteAnHTMLPage(String the_content, String the_folder, String the_doc){
	   System.out.println("make an html page");
	   
	   
	   WriterOfFiles.writeAFile(rg.out.pageCreator.makeHTML(the_content), the_folder, the_doc);
	   
   }
    
}
