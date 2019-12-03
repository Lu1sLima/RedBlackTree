import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.control.TextField;

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
                    linha = linha.toLowerCase();
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
        Character prefix = Character.toLowerCase(palavra.charAt(0));
        System.out.println(arvoreMacro.get(prefix).get(palavra));
        return ""+arvoreMacro.get(prefix).get(palavra);
    }

    public static void salvar(){
        try(ObjectOutputStream pSalvos = new ObjectOutputStream(new FileOutputStream("data.txt"))){
            pSalvos.writeObject(arvoreMacro);
            pSalvos.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void carregar(){
        try(ObjectInputStream cSalvas = new ObjectInputStream(new FileInputStream("data.txt"))){
            arvoreMacro = (RedBlackBST)cSalvas.readObject();
        }
        catch(IOException  | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void AdicionaPalavra(String palavra, String traducao){
        Character prefix = palavra.charAt(0);
        arvoreMacro.get(prefix).put(palavra, traducao);
        //System.out.println(arvoreMacro.get(prefix).positionsCentral());
        salvar();
    }

    public static String[] pesquisaAleatoria(){
        int n = (int)(Math.random()*((25-0)));
        List<String> randomKey = arvoreMacro.get(alfabeto.charAt(n)).positionsCentral();
        int nKeys = (int)(Math.random()*(randomKey.size()-0));
        System.out.println(nKeys);

        String [] doubleWords = new String [2];
        doubleWords[0] = randomKey.get(nKeys);
        doubleWords[1] = ""+arvoreMacro.get(alfabeto.charAt(n)).get(randomKey.get(nKeys));
        return doubleWords;
    }
}