import java.io.*;
import java.util.*;

public class Cheked {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        try (
                PrintStream p = new PrintStream("output.txt", "utf-8");
                Scanner s = new Scanner(new File("input.txt"), "utf-8");
                Scanner slovar = new Scanner(new File("words_alpha.txt"), "utf-8")
        ) {

            //read dictionary
            Set<String> sl = new HashSet<>();
            while (slovar.hasNextLine()) {
                sl.add(slovar.nextLine());
            }

            //create correctors
            List<WordCorrector> list = createCorrectors();

            Scanner file = s.useDelimiter("[^A-Za-z]+");
            while (file.hasNext()) {
                String word = file.next().toLowerCase();
                if (!isInDictionary(word, sl))
                    processWord(p, sl, word, list);
            }
        } catch (IOException ex) {
            System.out.println("Ошибка чтения или записи каких-то файлов: " + ex.getMessage());
        }
    }

    private static List<WordCorrector> createCorrectors() {
        List<WordCorrector> list = new ArrayList<>();

        list.add(new ChangeLetterCorrector());
        list.add(new DeliteLetterCorrector());
        list.add(w -> {
            Set<String> corrections = new HashSet<>();
            for (int i = 0; i < w.length(); i++)
                for (char c = 'a'; c <= 'z'; c++) {
                    String correctedWord = w.substring(0, i) + c + w.substring(i);
                    corrections.add(correctedWord);
                }
            return corrections;
        });
        list.add(new LetterChange());
        return list;
    }

    private static void processWord(PrintStream p, Set<String> sl, String word, List<WordCorrector> correctors) {
        p.print(word);
        p.print(": ");

        Set<String> str = new HashSet<>();

        for (WordCorrector cor : correctors) {
            for (String world : cor.proposeCorrections(word)) {
                str.add(world);
            }
        }

        for (String string : str) {
            if (isInDictionary(string, sl)) {
                p.print(string + " ");
            }
        }

        p.println();
    }

    private static boolean isInDictionary(String word, Set<String> sl) {
        return sl.contains(word);
    }
}
