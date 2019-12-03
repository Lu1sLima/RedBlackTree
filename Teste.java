/**
 * Teste
 */
public class Teste {

    public static void main(String[] args) {
        RedBlackBST<Character, String> a = new RedBlackBST<>();

        a.put('a', "arco");
        a.put('b', "baleia");
        a.put('c', "carvalho");
        // a.put('d', "dado");
        // a.put('e', "elefante");
        // a.put('f', "furacao");
        // a.put('g', "guiana");
        // a.put('h', "hiato");
        // a.put('i', "igor");
        // a.put('j', "jarra");
        // a.put('k', "kaleb");
        // a.put('l', "luis");
        // a.put('m', "marte");
        // a.put('n', "navio");
        // a.put('o', "oculos");
        // a.put('p', "parede");
        // a.put('q', "queijo");
        // a.put('r', "raiz");
        // a.put('s', "salve");
        // a.put('t', "talvez");
        // a.put('u', "uivar");
        // a.put('v', "vazio");
        // a.put('w', "wesley");
        // a.put('x', "xarrua");
        // a.put('y', "yuri");
        // a.put('z', "zebra");

        System.out.println(a.positionsPreNode());


        // System.out.println("---------- [CAMINHAMENTOS] ----------");
        // System.out.println("[CENTRAL]: "+a.positionsCentral());
        // System.out.println("[PRE]: "+a.positionsPre());
        // System.out.println("[POS]: "+a.positionsPos());
        // System.out.println("[LARGURA]: "+a.positionsWidth());
        // System.out.println("---------- [OUTROS METODOS] ----------");
        // System.out.println("Pai do [a]: "+a.getParent('a'));
        // System.out.println("Pai do [p]: "+a.getParent('p'));
        // RedBlackBST<Character, String> b = a.clone();
        // System.out.println("---------- [Arvore CLONE] ----------");
        // System.out.println("[CENTRAL]: "+b.positionsCentral());
        // System.out.println("[PRE]: "+b.positionsPre());
        // System.out.println("[POS]: "+b.positionsPos());
        // System.out.println("[LARGURA]: "+b.positionsWidth());
    }
}