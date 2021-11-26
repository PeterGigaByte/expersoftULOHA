import java.util.ArrayList;

public interface Service {
    Dictionary readFileCreateDictionary(String filename);
    Integer createWords(Dictionary dictionary, Integer testNumber);
    ArrayList<String> printOutput(Dictionary dictionary, Integer sizeIndex,ArrayList<String> synonyms);
    ArrayList<String> checkSynonyms(Dictionary dictionary);
    ArrayList<String> readOutputExample(String fileName);
    void exportFile(ArrayList<String> result,String filename);
}
