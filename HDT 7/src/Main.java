import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

// Clase que representa una asociación entre una clave (key) y un valor (value)
class Association<K, V> implements Comparable<Association<String, String>> {
    private K key;
    private V value;

    public Association(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(Association<String, String> o) {
        return 0;
    }
}

// Clase que representa un nodo en un árbol binario
class TreeNode<E> {
    private E data;
    private TreeNode<E> left;
    private TreeNode<E> right;

    public TreeNode(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public E getData() {
        return data;
    }

    public TreeNode<E> getLeft() {
        return left;
    }

    public TreeNode<E> getRight() {
        return right;
    }

    public void setLeft(TreeNode<E> left) {
        this.left = left;
    }

    public void setRight(TreeNode<E> right) {
        this.right = right;
    }
}

// Clase que representa un árbol binario de búsqueda
class BinaryTree<E extends Comparable<E>> {
    private TreeNode<E> root;

    public BinaryTree() {
        root = null;
    }

    public void insert(E data) {
        root = insertRecursive(root, data);
    }

    private TreeNode<E> insertRecursive(TreeNode<E> root, E data) {
        if (root == null) {
            root = new TreeNode<>(data);
            return root;
        }

        if (data.compareTo(root.getData()) < 0) {
            root.setLeft(insertRecursive(root.getLeft(), data));
        } else if (data.compareTo(root.getData()) > 0) {
            root.setRight(insertRecursive(root.getRight(), data));
        }

        return root;
    }

    public boolean search(E key) {
        return searchRecursive(root, key);
    }

    private boolean searchRecursive(TreeNode<E> root, E key) {
        if (root == null) {
            return false;
        }

        if (root.getData().compareTo(key) == 0) {
            return true;
        } else if (root.getData().compareTo(key) > 0) {
            return searchRecursive(root.getLeft(), key);
        } else {
            return searchRecursive(root.getRight(), key);
        }
    }

    public void inorderTraversal() {
        inorderTraversalRecursive(root);
    }

    private void inorderTraversalRecursive(TreeNode<E> root) {
        if (root != null) {
            inorderTraversalRecursive(root.getLeft());
            System.out.println(root.getData());
            inorderTraversalRecursive(root.getRight());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        File dictionaryFile = new File("diccionario.txt");
        File textFile = new File("texto.txt");

        // Verificar si los archivos existen, si no, crearlos con datos predeterminados
        if (!dictionaryFile.exists()) {
            createDefaultDictionary(dictionaryFile);
        }
        if (!textFile.exists()) {
            createDefaultTextFile(textFile);
        }

        BinaryTree<Association<String, String>> dictionary = buildDictionary(dictionaryFile);
        translateText(dictionary, textFile);
    }

    // Método para construir el diccionario desde el archivo diccionario.txt
    public static BinaryTree<Association<String, String>> buildDictionary(File file) {
        BinaryTree<Association<String, String>> dictionary = new BinaryTree<>();
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); // Leer la línea y eliminar espacios en blanco al principio y al final
                if (!line.isEmpty()) {
                    String[] parts = line.split(",\\s*"); // Dividir la línea en partes usando la coma seguida de cero o más espacios como delimitador
                    if (parts.length == 2) {
                        Association<String, String> association = new Association<>(parts[0], parts[1]);
                        dictionary.insert(association);
                    } else {
                        System.out.println("Formato incorrecto en la línea del diccionario: " + line);
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    // Método para crear el archivo diccionario.txt con datos predeterminados
    public static void createDefaultDictionary(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println("(house, casa)");
            writer.println("(dog, perro)");
            writer.println("(homework, tarea)");
            writer.println("(woman, mujer)");
            writer.println("(town, pueblo)");
            writer.println("(yes, si)");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para crear el archivo texto.txt con datos predeterminados
    public static void createDefaultTextFile(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println("The woman asked me to do my homework about my town.");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para traducir el texto desde el archivo texto.txt
    public static void translateText(BinaryTree<Association<String, String>> dictionary, File file) {
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String word = scanner.next();
                word = word.replaceAll("[^a-zA-Z]", ""); // Elimina caracteres no alfabéticos
                word = word.toLowerCase(); // Convertir a minúsculas para la comparación

                if (dictionary.search(new Association<>(word, ""))) {
                    System.out.print(word + " ");
                } else {
                    System.out.print("*" + word + "* ");
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
