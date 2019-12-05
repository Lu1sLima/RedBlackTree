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
 * Classe Pesquisa(onde as operações com as árvores são feitas).
 * @author Luís Lima
 * @author Adilson Medronha
 */
public class Pesquisa {
    public static RedBlackBST<Character, RedBlackBST> arvoreMacro = new RedBlackBST<>();
    public static RedBlackBST<String, String> arvoreMicro;
    public static String alfabeto = "abcdefghijklmnopqrstuvwxyz";


    public Pesquisa(){
    }

    /**
     * Esse método é utilizado apenas uma vez, que é antes de salvar todas as palavras no banco.
     * Após o salvamento das palavras, é só carrega-las do arquivo [data.txt].
    **/
    public static void carregaPalavras(){
        for(int i = 0; i < alfabeto.length(); i++){
            Path path1 = Paths.get("palavras"+Character.toUpperCase(alfabeto.charAt(i))+".txt");
            try(BufferedReader br = new BufferedReader(new FileReader(path1.toString()))){
                String linha = br.readLine();
                arvoreMicro = new RedBlackBST<>();
                while (linha !=null) {
                    linha = linha.toLowerCase();
                    String [] parts = linha.split(";");
                    arvoreMicro.add(parts[0], parts[1]);
                    
                    linha = br.readLine();
                    arvoreMacro.add(alfabeto.charAt(i), arvoreMicro);
                }
            } catch (IOException x) {
                System.err.format("Erro de E/S: %s%n", x);
            }
        }
    }


    /**
     * Método com a lógica para pesquisa na árvore.
     * Pesquisa primeiro na ÁRVORE MACRO [chave = prefixo da letra], pega o VALUE, que é uma ÁRVORE MICRO
     * e na ÁRVORE MICRO, pesquisa através da palavra passada por parâmetro, pegando o value(tradução).
     * @param palavra
     * @return traducao da palavra passada por parâmetro
     */

    public static String pesquisa(String palavra){
        Character prefix = Character.toLowerCase(palavra.charAt(0));
        System.out.println(arvoreMacro.get(prefix).get(palavra));
        return ""+arvoreMacro.get(prefix).get(palavra);
    }

    /**
     * Método com a lógica para salvar o objeto (árvore).
     */
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

    /**
     * Método que adiciona uma palavra com sua respectiva tradução na árvore.
     * @param palavra
     * @param traducao
     */
    public static void AdicionaPalavra(String palavra, String traducao){
        Character prefix = palavra.charAt(0);
        //vai na ÁRVORE MACRO, pega o value do prefixo (que é uma ÁRVORE MICRO), e adiciona a palavra e sua tradução na ÁRVORE MICRO.
        arvoreMacro.get(prefix).add(palavra, traducao);
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