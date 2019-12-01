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
 * Pesquisa
 */
public class Pesquisa {
    public static RedBlackBST<Character, RedBlackBST> arvoreMacro = new RedBlackBST<>();
    public static RedBlackBST<String, String> arvoreMicro;
    public static String alfabeto = "abcdefghijklmnopqrstuvwxyz";


    public Pesquisa(){
        
    }

    public static void carregaPalavras(){
        for(int i = 0; i < alfabeto.length(); i++){
            Path path1 = Paths.get("palavras"+Character.toUpperCase(alfabeto.charAt(i))+".txt");
            try(BufferedReader br = new BufferedReader(new FileReader(path1.toString()))){
                String linha = br.readLine();
                arvoreMicro = new RedBlackBST<>();
                while (linha !=null) {
                    String [] parts = linha.split(";");
                    arvoreMicro.put(parts[0], parts[1]);
                    
                    linha = br.readLine();
                    arvoreMacro.put(alfabeto.charAt(i), arvoreMicro);
                }
            } catch (IOException x) {
                System.err.format("Erro de E/S: %s%n", x);
            }
        }
        
    }
    public static String pesquisa(String palavra){
        carregaPalavras();
        Character prefix = palavra.charAt(0);
        return ""+arvoreMacro.get(prefix).get(palavra);
    }
}