import java.io.*;
import java.util.*;

public class Top_N_Count {
	//main hashmap which holds <unique word, frequency>
	static LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<String, Integer>();
	
	public static void main(String[] args) throws IOException {
		int count = 0;
		File f = null;
		
		if(args.length == 2) {
			try {
				//top N occurring
				count = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				System.out.println("Error: Please make the first argument an integer representing the "
						+ "\"N-value\" for the top N frequently occuring words.\n");
				System.exit(0);
			}

			f = new File(args[1]);
				
			boolean exists = f.exists();
			boolean isDir = f.isDirectory();
			boolean isFile = f.isFile();
			
			
			if(exists == false) {
				System.out.println("Error: Please make the second argument a valid file path to a text"
						+ " document or directory");
				System.exit(1);
			}
			//checks for single file
			else if (isFile){
				int counter = 0;
				LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
				
				occurrenceFrequency(f);
				result = mergeSort(wordCount);
				Iterator<String> resultIter = result.keySet().iterator();
				
				while(counter < count){
					if(resultIter.hasNext()){
						String tempKey = resultIter.next();
						System.out.println("Word \"" + tempKey + "\" occurs " + result.get(tempKey) + " times." );
						counter++;
					}		
				}
			}
			//checks for directory
			else if (isDir){
				int counter = 0;
				LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
				
				dirTraverse(f);
				result = mergeSort(wordCount);
				Iterator<String> resultIter = result.keySet().iterator();
				
				while(counter < count){
					if(resultIter.hasNext()){
						String tempKey = resultIter.next();
						System.out.println("Word \"" + tempKey + "\" occurs " + result.get(tempKey) + " times." );
						counter++;
					}		
				}
			}
			//if something else
			else {
				System.out.println("Error");
				System.exit(3);
			}
		}
		else {
 			System.out.println("Error: You have entered an incorrect number of parameters. Example: command-line"
 					+ " prompt: Top_N_Count 10 valid/filepath/file.txt");
 			System.exit(2);
		}
	}
	
	//searches through directories, recursively calls for sub-directories
	public static void dirTraverse(File f) throws IOException {
		File[] listOfFiles = f.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        occurrenceFrequency(listOfFiles[i]);
	      } else if (listOfFiles[i].isDirectory()) {
	    	  dirTraverse(listOfFiles[i]);
	      }
	    }
	}
	
	//puts unique words and their frequencies in global hashmap for single file
	public static Map<String, Integer> occurrenceFrequency(File f) throws IOException {
		
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(f));
		
		String sCurrentLine;

		while ((sCurrentLine = br.readLine()) != null) {
			//delimits, find individual words
			String delims = "[ ,.!?]+";
			String[] tokens = sCurrentLine.split(delims);
			
			for(int i = 0; i<tokens.length; i++){
				//all words to lowercase
				String token = tokens[i].toLowerCase();
				
				//checks if word has already occurred or is unique
				if(wordCount.containsKey(token) == false){
					wordCount.put(token, 1);
				}
				else {
					wordCount.put(token, wordCount.get(token) + 1);
				}
			}		
		}
		br.close();
		return wordCount;
	}
	
	//mergesort, takes final map and sorts it
	public static LinkedHashMap<String, Integer> mergeSort(LinkedHashMap<String, Integer> map) {
        if (map.size() < 2) {
            return map;
        }
        
        // rounds downwards
        int middle = map.size() / 2;
        int location = 0;

        LinkedHashMap<String, Integer> mapLeft = new LinkedHashMap<String, Integer>();
        LinkedHashMap<String, Integer> mapRight = new LinkedHashMap<String, Integer>();
        
        for (String key : map.keySet()) {
            if(location < middle){
            	mapLeft.put(key, map.get(key));
            }
            else {
            	mapRight.put(key, map.get(key));
            }
            location++;
        }
        
        // recursive call
        mapLeft = mergeSort(mapLeft);
        mapRight = mergeSort(mapRight);
        return merge(mapLeft, mapRight);
    }

	//merges the individual hashmaps together
    public static LinkedHashMap<String, Integer> merge(LinkedHashMap<String, Integer> mapLeft, LinkedHashMap<String, Integer> mapRight) {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        Iterator<String> keyLeftIter = mapLeft.keySet().iterator();
        Iterator<String> keyRightIter = mapRight.keySet().iterator();
        String keyLeft = "";
        String keyRight = "";
        Integer valueLeft = 0;
        Integer valueRight = 0;
        boolean moreLeft = true;
        boolean moreRight = true;
        
        if (keyLeftIter.hasNext()) {
        	keyLeft = keyLeftIter.next();
        	valueLeft = mapLeft.get(keyLeft);
        } else {
        	moreLeft = false;
        }
        if (keyRightIter.hasNext()) {
        	keyRight = keyRightIter.next();
        	valueRight = mapRight.get(keyRight);
        } else {
        	moreRight = false;
        }
  
        while (moreLeft && moreRight) {
        	if (valueLeft.compareTo(valueRight) > 0) {
                result.put(keyLeft, valueLeft);
                
                if (keyLeftIter.hasNext()){
                	keyLeft = keyLeftIter.next();
                	valueLeft = mapLeft.get(keyLeft);
                } else {
                	moreLeft = false;
                }
            } 
        	else {
                result.put(keyRight, valueRight);
                
                if (keyRightIter.hasNext()){
                	keyRight = keyRightIter.next();
                	valueRight = mapRight.get(keyRight);
                } else {
                	moreRight = false;
                }
            }
        }
        
        while (moreLeft) {
        	// moreRight better be false

        	result.put(keyLeft, valueLeft);
        
        	if (keyLeftIter.hasNext()){
        		keyLeft = keyLeftIter.next();
        		valueLeft = mapLeft.get(keyLeft);
        	} else {
        		moreLeft = false;
        	}
        }
        
        while (moreRight) {
        	// moreLeft better be false

        	result.put(keyRight, valueRight);
        
        	if (keyRightIter.hasNext()){
        		keyRight = keyRightIter.next();
        		valueRight = mapRight.get(keyRight);
        	} else {
        		moreRight = false;
        	}
        }
        return result;
    }
}
