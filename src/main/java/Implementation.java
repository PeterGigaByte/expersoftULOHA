import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Implementation implements Service {

    @Override
    public Dictionary readFileCreateDictionary(String fileName) {
        //Nový slovník
        Dictionary dictionary = new Dictionary();
        //Načítanie dát
        try (Stream<String> stream = Files.lines(Paths.get(getClass().getResource(fileName).toURI()))) {
            stream.forEach(line->{
                //číslo - pomocná premenná pre určovanie pozície
                String number;
                //počet testov
                if(NumberUtils.isNumber(line)  && dictionary.getNumberOfTests()==null ){
                    dictionary.setNumberOfTests(Integer.parseInt(line));
                }
                //počet slov na načítanie
                else if(NumberUtils.isNumber(number = line.replaceAll("[\\s|\0]", ""))){
                    Integer size = Integer.parseInt(number);
                    dictionary.setNumberOfWords();
                    dictionary.setActualWord(0);
                    dictionary.addWordsSize(size);

                }
                //načítavanie samotných slov
                else if(line.length()>0  && line.matches("^[a-zA-Z ]*$") ){
                    String[] words = line.split(" ");
                    if(words.length==2 &&  words[0].length()<=20 && words[1].length()<=20 && words[1].length() > 0 && words[0].length() > 0){
                        dictionary.addStringX1(words[0].toLowerCase());
                        dictionary.addStringX2(words[1].toLowerCase());
                    }
                    dictionary.setActualWord(dictionary.getActualWord()+1);
                }
            });
        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
        return dictionary;
    }

    @Override
    public Integer createWords(Dictionary dictionary, Integer testNumber) {
        //Počet testov
        Integer numberOfTests = dictionary.getNumberOfTests();
        //overenie či je číslo v správnom tvare <0,100>
        if(testNumber>numberOfTests || testNumber<=0 || numberOfTests>100){
            //nesprávne číslo
            return null;
        }
        int wordsCountTestIndex = (testNumber*2)-2;
        //Veľkosť - dĺžka/počet slov, s ktorými chceme pracovať
        Integer size = dictionary.getWordsSizes().get(wordsCountTestIndex);

        //Pozícia prvého indexu slova, s ktorým chceme pracovať
        Integer firstTestWordIndex = getWordIndex(wordsCountTestIndex,dictionary.getWordsSizes());

        if(wrongParameters(size)){
            return null;
        }
        //Nájdeme si pozície v poli x1 a x2, ktoré sme si vypočítali a tu sa nachádzajú naše slová, s ktorými chceme pracovať
        //Prehľadávame
        for(int i = firstTestWordIndex; i<size+firstTestWordIndex;i++){
            //Vytvorí prvé slovo ak ešte neexistuje
            Word word1 = dictionary.alreadyExistWord( dictionary.getX1().get(i));
            //Vytvorí druhé slovo ak ešte neexistuje
            Word word2 = dictionary.alreadyExistWord( dictionary.getX2().get(i));
            //Pomocný ArrayList pre nájdenie vzťahov
            ArrayList<String> relationShipWord1 = new ArrayList<>();
            //Ak sa názvy slov nerovnajú tak pridaj vzťah medzi nimi ako prvé dva vzťahy v našom pomocnom ArrayListe
            if(!word1.getName().equals(word2.getName())){
                relationShipWord1.add(word1.getName());
                relationShipWord1.add(word2.getName());
            }
            //Ak sa rovnajú pridaj iba raz tento názov do vzťahov
            else{
                relationShipWord1.add(word1.getName());
            }
            //Ak už prvé slovo už existuje tak sa načítaj všetky jeho vzťahy unikátne
            if(dictionary.alreadyExistWord(word1)){
                for(Word temp:dictionary.getWordsDictionary()){
                    if(temp.getPossibleSynonymous().contains(word1.getName())){
                        relationShipWord1.add(temp.getName());
                    }
                }
            }
            //Ak už druhé slovo už existuje tak sa načítaj všetky jeho vzťahy unikátne
            if(dictionary.alreadyExistWord(word2)){
                for(Word temp:dictionary.getWordsDictionary()){
                    if(temp.getPossibleSynonymous().contains(word1.getName()) && !relationShipWord1.contains(temp.getName())){
                        relationShipWord1.add(temp.getName());
                    }
                }
            }
            //Pridaj unikátne vzťahy, ktoré sme našli do slov, s ktorými aktuálne pracujeme
            for (String temp:relationShipWord1){
                if(!word1.getPossibleSynonymous().contains(temp)){
                    word1.addNewSynonyme(temp);
                }
                if(!word2.getPossibleSynonymous().contains(temp)){
                    word2.addNewSynonyme(temp);
                }
            }
            //Pridaj unikátne vzťahy do všetkých slov, s ktorými majú tieto dve slová vzťah
            for(Word temp:dictionary.getWordsDictionary()){
                for(String temp1:temp.getPossibleSynonymous()){
                    if(relationShipWord1.contains(temp1)||relationShipWord1.contains(temp.getName())){
                        for(String relationship:relationShipWord1){
                            if(!temp.getPossibleSynonymous().contains(relationship)){
                                temp.addNewSynonyme(relationship);
                            }
                        }
                        //tento break je veľmi dôležitý
                        break;
                    }
                }
            }

            if(!word1.getName().equals(word2.getName())) {
                //Pridanie nových slov do slovníka ak sa názvy nerovnajú
                dictionary.addNewWordToDictionary(word1);
                dictionary.addNewWordToDictionary(word2);
            }else if(!dictionary.alreadyExistWord(word1)){
                //Pridanie nových slov do slovníka ak sa názvy rovnajú a slovo 1 neexistuje ešte v slovníku
                dictionary.addNewWordToDictionary(word1);
            }


        }
        //Vrátime hodnotu kde sme skončili a teda kde budeme ďalej pokračovať
        return wordsCountTestIndex+1;
    }

    @Override
    public ArrayList<String> printOutput(Dictionary dictionary, Integer sizeIndex,ArrayList<String> synonyms) {
        //Veľkosť - dĺžka/počet slov, s ktorými chceme pracovať
        Integer size = dictionary.getWordsSizes().get(sizeIndex);

        //Pozícia prvého indexu slova, s ktorým chceme pracovať
        Integer firstWordIndex = getWordIndex(sizeIndex,dictionary.getWordsSizes());
        if(wrongParameters(size)){
            return null;
        }

        //Prechádzanie slov - hľadanie vzťahov medzi nimi a pridávanie do ArraListu
        for(int i = firstWordIndex; i<size+firstWordIndex;i++){
            Word word1 = dictionary.alreadyExistWord( dictionary.getX1().get(i));
            Word word2 = dictionary.alreadyExistWord( dictionary.getX2().get(i));
            if(word1 !=null && word2 != null) {
                //Ak slová majú medzi sebou vzťah alebo sa rovnajú sú synonymá
                if (word1.alreadyExistWord(word2.getName()) || word2.alreadyExistWord(word1.getName()) || word1.getName().equals(word2.getName())) {
                    synonyms.add("synonyms");
                }
                //Inak niesu
                else {
                    synonyms.add("different");
                }
            }
        }
        return synonyms;
    }

    @Override
    public ArrayList<String> checkSynonyms(Dictionary dictionary) {
        ArrayList<String> synonyms = new ArrayList<>();
        //prechádzanie testov
        for(int i = 1; i<=dictionary.getNumberOfTests();i++){
            //vytvorenie slov pre testovanie a ich vzťahov
            Integer queriesIndex = createWords(dictionary,i);
            //testovanie a tvorba vystupu
            synonyms = printOutput(dictionary,queriesIndex,synonyms);
            //vyčistenie, pripravenie na ďalší test
            dictionary.setupForNextTest();
        }
        return synonyms;
    }

    @Override
    public ArrayList<String> readOutputExample(String fileName) {
        //čítanie príkladného outputu - slúži na testovanie
        ArrayList<String> expectedOutput = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(getClass().getResource(fileName).toURI()))) {
            stream.forEach(expectedOutput::add);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return expectedOutput;
    }

    @Override
    public void exportFile(ArrayList<String> result,String filename) {
        try {
            FileWriter writer = new FileWriter("src/main/resources/"+filename+".out");
            for(String str: result) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer getWordIndex(Integer wordsCountIndex, ArrayList<Integer> sizes){
        //čítanie príkladného outputu - slúži na testovanie
        Integer firstWordIndex = 0;
        if(wordsCountIndex < sizes.size() && wordsCountIndex >= 0) {
            for (int i = 0; i != wordsCountIndex; i++) {
                firstWordIndex += sizes.get(i);
            }
        }
        return firstWordIndex;
    }
    private boolean wrongParameters(Integer param1){
        //nesprávne číslo
        return param1 > 100 || param1 < 0;
    }

}
