package HW_10_1.HW_10_1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class LoadManager implements Runnable{
	
	private Document doc;
	private String hashtext;
	private String link;
	private String data_from_site;
	private Elements elements;
	
	public LoadManager(String link){
		this.link = link;
	}

	@Override
	public void run() {
	
		try {
			doc = Jsoup.connect(link).get();		
		} catch (IOException e2) {
			e2.printStackTrace();
		}
    	elements = doc.select("body");
    	elements.forEach(element -> data_from_site = element.text());

		hashtext = Hasher.getHashMD5(data_from_site);
		System.out.println(hashtext);	
		App.writeFile(hashtext);

	}
}


public class App {

	public static String path_to_links;
	public static String path_to_file;
	
	private static Path p_write;
	private static Path p_read;
	
    @SuppressWarnings("resource")
	public static void main( String[] args ) throws NoSuchAlgorithmException, InterruptedException, IOException    {
    		
    	Scanner input = new Scanner(System.in);
    		
    	do{
    		System.out.print("Input path to link: ");
    		path_to_links = input.nextLine();
    		if(path_to_links.equalsIgnoreCase("Exit")){
    			System.exit(0);
    		}
        	
    		System.out.print("Input path to writing file: ");
    		path_to_file = input.nextLine();
    		if(path_to_links.equalsIgnoreCase("Exit")){
    			System.exit(0);
    		}
    		
    		if(isExist()){
    			startExecuting();
    		}else{
    			System.out.println("File not exist");
    		}
    	}while(true);   	
    }
    
    
    public static Boolean isExist(){
    	
    	try{
        	p_write = Paths.get(App.path_to_file);
        	p_read = Paths.get(path_to_links);
    	}catch(IllegalArgumentException iae){
    		System.out.println("Incorrect symbol");
    	}

		if(Files.exists(p_write, LinkOption.NOFOLLOW_LINKS) && Files.exists(p_read, LinkOption.NOFOLLOW_LINKS)){
			return true;
    	}
		return false;
    }

    
    public static void startExecuting() throws IOException{

        Files.write(Paths.get(path_to_file), "".getBytes());

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for(String s : Files.readAllLines(p_read)){
        	executor.submit(new LoadManager(s));
        }
        
        executor.shutdown();
    }

    
	public static synchronized void writeFile(String s){

    	try {   		
			Files.write(p_write, s.getBytes(), StandardOpenOption.APPEND);
			Files.write(p_write, System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

//	"C:\\workspace\\HW_10_1\\links.txt"
//	"C:\\workspace\\HW_10_1\\md5_1.txt"

//106ca789b016c0e176d9ebf191b3302a
//8a795b9d0082f3bed0b106cfc9fba87b
//5485eae5da4775f6b994f4fc90667663
//4117edddf77579467c25c5877e970f9a
//d8a4db2ce036ce1202c95c14a4978468
//3aaad38e923e95c0e15916b9edf74322
//8c2a826c87675e995a566854efe55c99
