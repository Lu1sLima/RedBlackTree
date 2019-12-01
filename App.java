import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Scanner;
/**
 * App
 */
public class App {
    public RedBlackBST<Character, RedBlackBST> arvoreMacro = new RedBlackBST<>();
    public RedBlackBST<String, String> arvoreMicro;
    String alfabeto = "abcdefghijklmnopqrstuvwxyz";

    public App(){

    };
    public void carregaPalavras(){

        for(int i = 0; i < alfabeto.length(); i++){
            Path path1 = Paths.get("palavras"+Character.toUpperCase(alfabeto.charAt(i))+".txt");
            try(BufferedReader br = new BufferedReader(new FileReader(path1.toString()))){
                String linha = br.readLine();
                arvoreMicro = new RedBlackBST<>();
                while (linha !=null) {
                    String [] parts = linha.split(";");
                    String formatada = stripAccents(parts[1]);
                    String teste1 = formatada.replaceAll("[^\\p{ASCII}]", "");
                    arvoreMicro.put(parts[0], formatada);

                    linha = br.readLine();
                    arvoreMacro.put(alfabeto.charAt(i), arvoreMicro);
                }
            } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
            }
        }

    }

    public static String stripAccents(String input){
        return input == null ? null :
                Normalizer.normalize(input, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }


    public static void main(String[] args) {
        App p1 = new App();
        p1.carregaPalavras();
        System.out.println(p1.arvoreMacro.get('a').get("add"));

    }
}