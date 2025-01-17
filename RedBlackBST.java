
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 *  The {@code BST} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides ordered methods for finding the <em>minimum</em>,
 *  <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  <p>
 *  This implementation uses a <em>left-leaning red-black BST</em>. 
 *  The <em>put</em>, <em>get</em>, <em>contains</em>, <em>remove</em>,
 *  <em>minimum</em>, <em>maximum</em>, <em>ceiling</em>, <em>floor</em>,
 *  <em>rank</em>, and <em>select</em> operations each take
 *  &Theta;(log <em>n</em>) time in the worst case, where <em>n</em> is the
 *  number of key-value pairs in the symbol table.
 *  The <em>size</em>, and <em>is-empty</em> operations take &Theta;(1) time.
 *  The <em>keys</em> methods take
 *  <em>O</em>(log <em>n</em> + <em>m</em>) time, where <em>m</em> is
 *  the number of keys returned by the iterator.
 *  Construction takes &Theta;(1) time.
 *  <p>
 *  For alternative implementations of the symbol table API, see {@link ST},
 *  {@link BinarySearchST}, {@link SequentialSearchST}, {@link BST},
 *  {@link SeparateChainingHashST}, {@link LinearProbingHashST}, and
 *  {@link AVLTreeST}.
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class RedBlackBST<Key extends Comparable<Key>, Value> implements Serializable{

    private static final boolean RED   = true;
    private static final boolean BLACK = false;
    List<Boolean> cores = new ArrayList<>();

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node implements Serializable{
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }

        public String getColor(){
            if(color){
                return "red";
            }
            return "black";
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public RedBlackBST() {
    }


    /**
     * 
     * Métodos solicitados:
     * Alguns já estavam implementados na árvore!
     */


    /**
     * Retorna a o valor da chave do pai da chave que passar.
     * Método adaptado da implementação do colega @Lucas Garcia
     * @param key the key
     * @return valor da chave do pai da key passada por parametro, se for root, retorna null
     * Notação: O(n)
     */

    public Key getParent(Key key){
        return getParent(root, key);
    }

    private Key getParent(Node x, Key key){
        Node pai = x;
        if(key != root.key){
            while (x != null) {
                int cmp = key.compareTo(x.key);
                if      (cmp < 0){
                    pai = x;
                    x = x.left;
                }
                else if (cmp > 0) {
                    pai = x;
                    x = x.right;
                }
                else              return pai.key;
            }
        }
        return null;
    }

    
    /**
     * Retorna uma cópia da árvore.
     * @return retorna uma árvore idêntica a atual.
     * Notação O(n);
     */
    public RedBlackBST<Key, Value> clone(){
        RedBlackBST<Key, Value> clone = new RedBlackBST<>();
        clone(root, clone);
        return clone;    
    }

    private void clone(Node n, RedBlackBST<Key, Value> clone){
        List<Node> aux = positionsCentralClone();
        aux.forEach(a -> {
            clone.add(a.key, a.val);
        });
    }


    /**
     * Retorna uma lista com todos os elementos da arvore. Os elementos
     * sao colocados na lista seguindo um caminhamento prefixado.
     * @return lista com os elementos da arvore na ordem prefixada
     * Notação O(n)
     */
    public List<Key> positionsPre() {
        List<Key> res = new ArrayList<>();
        positionsPreAux(root, res);
        return res;
    }
    private void positionsPreAux(Node n, List<Key> res) {
        if (n != null) {
            res.add(n.key); //Visita o nodo
            positionsPreAux(n.left, res); //Visita a subarvore esquerda
            positionsPreAux(n.right, res); //Visita a subarvore direita
        }
    }

    /**
     * Método auxiliar utilizado para testes
     * É um caminhamento préfixado, só que retorna a cor dos nodos.
     * Esse método foi utilizado para obter imagens da árvore.
     * @author Luís Lima, Adilson Medronha
     * Notação O(n)
     */

    private List<String> positionsPreNode() {
        List<String> res = new ArrayList<>();
        positionsPreNodeAux(root, res);
        return res;
    }
    private void positionsPreNodeAux(Node n, List<String> res) {
        if (n != null) {
            res.add(n.getColor()); //Visita o nodo
            positionsPreNodeAux(n.left, res); //Visita a subarvore esquerda
            positionsPreNodeAux(n.right, res); //Visita a subarvore direita
        }
    }

    
    /** 
     * Retorna uma lista com todos os elementos da arvore na ordem de 
     * caminhamento pos-fixada. Deve chamar um metodo auxiliar recursivo.
     * @return List<Key> lista com as chaves da arvore
     * Notação O(n)
     */
    public List<Key> positionsPos() {
        List<Key> lista = new ArrayList<>();
        positionsPosAux(root, lista);
        return lista;
    }

    private void positionsPosAux(Node n, List<Key> l){
        if(n != null){
            // percorrer subarvore da esquerda
            positionsPosAux(n.left, l);
            // percorrer subarvore da direita
            positionsPosAux(n.right, l);
            // visita raiz;
            l.add(n.key);
        }
    }


    /**
     * Retorna uma lista com todos os elementos da arvore em Ordem(crescente). Os elementos
     * sao colocados na lista seguindo um caminhamento central.
     * @return lista com as chaves da arvore na ordem central(ordenada)
     * Notação O(n)
     */
    public List<Key> positionsCentral() {
        List<Key> res = new ArrayList<>();
        positionsCentralAux(root, res);
        return res;
    }

    private void positionsCentralAux(Node n, List<Key> res) {
        if (n != null) {
            positionsCentralAux(n.left, res); //Visita a subarvore esquerda
            res.add(n.key); //Visita o nodo
            positionsCentralAux(n.right, res); //Visita a subarvore direita
        }
    }

    /**
     * Método utilizado como auxiliar para a criação do método Clone!
     * Retorna uma lista com os NODOS da arvore em Ordem(crescente). Os elementos
     * sao colocados na lista seguindo um caminhamento central.
     * @return lista com os elementos da arvore na ordem central(ordenada)
     * Notação O(n)
     */

    private List<Node> positionsCentralClone() {
        List<Node> res = new ArrayList<>();
        positionsCentralAuxClone(root, res);
        return res;
    }
    private void positionsCentralAuxClone(Node n, List<Node> res) {
        if (n != null) {
            positionsCentralAuxClone(n.left, res); //Visita a subarvore esquerda
            res.add(n); //Visita o nodo
            positionsCentralAuxClone(n.right, res); //Visita a subarvore direita
        }
    }

    /** 
     * Retorna uma lista com todos os elementos da arvore na ordem de 
     * caminhamento em largura. 
     * @return List<Key> com as chaves da arvore
     * Notação O(n)
     */  

    public List<Key> positionsWidth() {
        List<Key> res = new ArrayList<>();
        Queue<Node> fila = new Queue<>();
        fila.enqueue(root);
        while(!fila.isEmpty()) {
            Node n = fila.dequeue();
            res.add(n.key);
            if (n.left!=null)
                fila.enqueue(n.left);
            if (n.right!=null)
                fila.enqueue(n.right);
        }
        return res;
    }

    
    
    
    /**
     * Retorna o tamanho da árvore apartir de root.
     * @return tamanho da árvore.
     * Notação O(1) constante
     */
    public int size() {
        return size(root);
    }
    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    } 
    
    /**
     * Verifica  se a árvore está vazia
     * @return {@code true} se a árvore está vazia e {@code false} se tiver algum elemento.
     * Notação O(1) constante
     */
    public boolean isEmpty() {
        return root == null;
    }
 
     /**
      * Insere na árvore um nodo de chave-valor.
      * Reescreve o valor se o campo ja estiver preenchido.
      * Deleta uma key se o valor for null
      * Notação O(log(n))
      * @param key a chave
      * @param val o valor
      * @throws IllegalArgumentException se {@code key} é {@code null}
      */

     public void add(Key key, Value val) {
         if(root != null){
         }
         if (key == null) throw new IllegalArgumentException("first argument to add() is null");
         if (val == null) {
             delete(key);
             return;
         }
 
         root = add(root, key, val);
         root.color = BLACK;
     }
 
     private Node add(Node h, Key key, Value val) { 
         if (h == null) return new Node(key, val, RED, 1);
 
         int cmp = key.compareTo(h.key);
         if      (cmp < 0) h.left  = add(h.left,  key, val); 
         else if (cmp > 0) h.right = add(h.right, key, val); 
         else              h.val   = val;
 
         // mantendo a árvore com a característica de balanceamento à esquerda.
         if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
         if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
         if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
         h.size = size(h.left) + size(h.right) + 1;
 
         return h;
     }
    /**
     * Verifica se a árvore possui a determinada key.
     * @param key a chave
     * @return {@code true} se a árvore possui a chave {@code key} e
     *     {@code false} se não.
     * @throws IllegalArgumentException se {@code key} é {@code null}
     * Notação O(log(n))
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }
 
     /**
      * Retorna a altura da árvore.
      * @return altura da árvore apartir de root
      * Notação O(log(n)) constante
      */
     public int height() {
         return height(root);
     }
     private int height(Node x) {
         if (x == null) return -1;
         return 1 + Math.max(height(x.left), height(x.right));
     }


     /**
      * 
      * Métodos Auxiliares da arvore (Para que ela funcione)
      * Criados pelos autores
      */

    /***************************************************************************
     *  Node helper methods.
     ***************************************************************************/

    /** Verifica se o nodo é vermelho
     * Notação O(1) constante
     */
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
     }

   /***************************************************************************
    * Método de procura padrão de árvores binárias de pesquisa.
    ***************************************************************************/

    /**
     * Retorna o valor associado com a key passada por parâmetro.
     * Notação O(log(n))
     * @param key a chave
     * @return o valor associado àquela key.
     *     and {@code null} se a chave não está na árvore.
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }
    
    
/****************************************** FIM DOS MÉTODOS SOLICITADOS ********************************************/



   /***************************************************************************
    *  Remove da RedBlackTree
    ***************************************************************************/

    /**
     * Remove a chave (e o valor) de menor expressão da árvore.
     * Notação O(lon(n))
     * @throws NoSuchElementException se a redblack está vazia.
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
    }

    private Node deleteMin(Node h) { 
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }


    /**
     * Remove a chave de valor mais expressivo na árvore.
     * Notação O(lon(n))
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) { 
        if (isRed(h.left))
            h = rotateRight(h);

        if (h.right == null)
            return null;

        if (!isRed(h.right) && !isRed(h.right.left))
            h = moveRedRight(h);

        h.right = deleteMax(h.right);

        return balance(h);
    }

    /**
     * Remove a chave e o valor especificado.     
     * se a chave não for null e estiver na árvore.    
     *
     * @param  key the key
     * Notação O(log (n))
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) { 
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
    }

    /**
     * delete the key-value pair with the given key rooted at h
     */
    private Node delete(Node h, Key key) { 
        // assert get(h, key) != null;

        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        }
        else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }
    
    /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    /**
     * make a left-leaning link lean to the right
     * @param h
     * @return Node h
     * Notação O(1)
     */
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    /** Notação O(1)
     *  make a right-leaning link lean to the left
     * @return Node x
     */
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    /**
     * flip the colors of a node and its two children
     * Notação O(1)
     */
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    /**
     * Assuming that h is red and both h.left and h.left.left are black, make h.left or one of its children red.
     * @param h
     * @return h
     * Notacao O(1)
     */
    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) { 
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

/**
 * Assuming that h is red and both h.right and h.right.left are black, make h.right or one of its children red.
 * @param Node h
 * @return h
 * Notacao O(1)
 */
    private Node moveRedRight(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    /**
     *  Método auxiliar de balanceamento da árvore, testa a cor dos nodos e de seus filhos, e conforme a cor, opera rotações ou troca de cores.
     * @param h
     * @return Node
     * Notação O(1)
     */
    private Node balance(Node h) {
        // assert (h != null);

        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }



   /***************************************************************************
    *  Ordered symbol table methods.
    ***************************************************************************/

    /**
     * Notacao O(1)
     * Returns the smallest key in the symbol table.
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    } 

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) { 
        // assert x != null;
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 

    /**
     * Notacao O(1)
     * Returns the largest key in the symbol table.
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    } 

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) { 
        // assert x != null;
        if (x.right == null) return x; 
        else                 return max(x.right); 
    } 

    /**
     * Notação O(lon(n))
     * Return the key in the symbol table whose rank is {@code k}.
     * This is the (k+1)st smallest key in the symbol table. 
     *
     * @param  k the order statistic
     * @return the key in the symbol table of rank {@code k}
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *        <em>n</em>–1
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + k);
        }
        Node x = select(root, k);
        return x.key;
    }

    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        // assert x != null;
        // assert k >= 0 && k < size(x);
        int t = size(x.left); 
        if      (t > k) return select(x.left,  k); 
        else if (t < k) return select(x.right, k-t-1); 
        else            return x; 
    } 

    /** Notação O(lon(n))
     * Return the number of keys in the symbol table strictly less than {@code key}.
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    } 

    // number of keys less than key in the subtree rooted at x
    private int rank(Key key, Node x) {
        if (x == null) return 0; 
        int cmp = key.compareTo(x.key); 
        if      (cmp < 0) return rank(key, x.left); 
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right); 
        else              return size(x.left); 
    } 

   /***************************************************************************
    *  Range count and range search.
    ***************************************************************************/

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *  Notação O(log(n))
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(min(), max());
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between {@code lo} 
     *    (inclusive) and {@code hi} (inclusive) as an {@code Iterable}
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    } 

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) { 
        if (x == null) return; 
        int cmplo = lo.compareTo(x.key); 
        int cmphi = hi.compareTo(x.key); 
        if (cmplo < 0) keys(x.left, queue, lo, hi); 
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key); 
        if (cmphi > 0) keys(x.right, queue, lo, hi); 
    } 

    /**
     * Returns the number of keys in the symbol table in the given range.
     * Notação O(log(n))
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo} 
     *    (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }


   /***************************************************************************
    *  Check integrity of red-black tree data structure.
    ***************************************************************************/

    /** Notação O(log(n)) 
     * does this binary tree satisfy symmetric order?
     * Note: this test also ensures that data structure is a binary tree since order is strict
     * @return boolean
    **/
    private boolean isBST() {
        return isBST(root, null, null);
    }

    /**
    * is the tree rooted at x a BST with all keys strictly between min and max
    * (if min or max is null, treat as empty constraint)
    * Credit: Bob Dondero's elegant solution
    **/
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    } 

    /**
     * Notação O(n)
     * Checking if the rank is consistent
     * @return boolean
     */
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    /**
     * Testing if the three have red right links, and at most one (left) red links in a row on any path
     * Notação O(1) constante
     * @return boolean
     */
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == null) return true;
        if (isRed(x.right)) return false;
        if (x != root && isRed(x) && isRed(x.left))
            return false;
        return is23(x.left) && is23(x.right);
    } 
    /**
     * Check if all paths from root to leaf have same number of black edges.
     * Notação O(log(n))
     */
    private boolean isBalanced() { 
        int black = 0;     // number of black links on path from root to min
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    } 




}

/******************************************************************************
 *  Copyright 2002-2019, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/