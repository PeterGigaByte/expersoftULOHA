import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        //services
        Service service = new Implementation();
        //súbor sa berie z resources
        Dictionary dictionary = service.readFileCreateDictionary("test.in");

        //result
        ArrayList<String> results = service.checkSynonyms(dictionary);

        //example output
        //súbor sa berie z resources
        ArrayList<String> expected = service.readOutputExample("example_big.out");


        //tests
        if(results.size() == expected.size()){
            System.out.println("Size is OK!");
            int failed=0;
            int ok=0;
            for (int i=0;i<expected.size();i++){
                System.out.println(results.get(i).equals(expected.get(i)));
                if(results.get(i).equals(expected.get(i))){
                    ok++;
                }else{
                    System.out.println("index: "+i+" result: "+results.get(i)+" expected: "+expected.get(i));
                    failed++;
                }
            }
            System.out.println("OK: "+ok);
            System.out.println("FAILED: "+failed);
        }else{
            System.out.println("Size is NOT SAME!");
            System.out.println("result size: "+results.size());
            System.out.println("excepted size: "+expected.size());
            for(String result:results){
                System.out.println(result);
            }
        }
        //vypis suboru
        service.exportFile(results,"test_my");


    }
}
