package rg.out;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriterOfFiles {

	private static final String FILENAME = "//Users/john/angular_tut/page.html";
	private static final String BASE_FOLDER = "//Users/john/angular_tut/";
	public static void writeAFile( String content, String target_folder, String document_name) {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			//String content = "This is the content to write into file\n";
			fw = new FileWriter(BASE_FOLDER+target_folder+"/"+document_name);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {

				if (bw != null){
					bw.close();
				}
				if (fw != null){
					fw.close();
				}
			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}