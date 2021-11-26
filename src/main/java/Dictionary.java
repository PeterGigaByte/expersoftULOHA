import java.util.ArrayList;

public class Dictionary {
    private Integer numberOfTests;
    private Integer actualWord;
    private ArrayList<Integer> wordsSizes = new ArrayList<>();
    private ArrayList<Word> wordsDictionary = new ArrayList<>();
    private ArrayList<String> x1 = new ArrayList<>();
    private ArrayList<String> x2 = new ArrayList<>();
    public Integer getNumberOfTests() {
        return numberOfTests;
    }

    public void setNumberOfTests(Integer numberOfTests) {
        this.numberOfTests = numberOfTests;
    }

    public boolean alreadyExistWord(Word word){
        String name = word.getName();
        return wordsDictionary.stream().map(Word::getName).anyMatch(name::equals);
    }
    public Word alreadyExistWord(String name){
        return wordsDictionary.stream().filter(obj -> obj.getName().equals(name)).findFirst().orElseGet(() -> new Word(name));
    }
    public void addNewWordToDictionary(Word word){
        if(word!=null && !alreadyExistWord( word)) {
            wordsDictionary.add(word);
        }
    }


    public void addStringX1(String word){
        if(word!=null) {
            x1.add(word);
        }
    }
    public void addStringX2(String word){
        if(word!=null) {
            x2.add(word);
        }
    }
    public void setupForNextTest(){
        wordsDictionary.clear();

    }

    public void setNumberOfWords() {
    }

    public Integer getActualWord() {
        return actualWord;
    }

    public void setActualWord(Integer actualWord) {
        this.actualWord = actualWord;
    }
    public void addWordsSize(Integer size){
        if(size!=null){
            this.wordsSizes.add(size);
        }
    }

    public ArrayList<Integer> getWordsSizes() {
        return wordsSizes;
    }

    public ArrayList<String> getX1() {
        return x1;
    }



    public ArrayList<String> getX2() {
        return x2;
    }

    public ArrayList<Word> getWordsDictionary() {
        return wordsDictionary;
    }
}
