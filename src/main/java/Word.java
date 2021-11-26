import java.util.ArrayList;

public class Word {
    private String name;
    private ArrayList<String> possibleSynonymous = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Word(String name) {
        this.name = name;
    }

    public void addNewSynonyme(String word){
        if(word!=null && !alreadyExistWord(word)){
            this.possibleSynonymous.add(word);
        }

    }
    public boolean alreadyExistWord(String word){
        return possibleSynonymous.contains(word);
    }

    public ArrayList<String> getPossibleSynonymous() {
        return possibleSynonymous;
    }
}
