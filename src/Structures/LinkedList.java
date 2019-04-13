package Structures;

public class  LinkedList<T> {

    private int size;
    private Node<T> head;

    /**
     * Constructor de la lista enlazada
     */
    public LinkedList(){
        this.size = 0;
        this.head = null;
    }

    /**
     * Agrega un elemento al final de la lista
     * @param value Valor a agregar
     */
    public void addLast(T value){
        Node<T> newElement = new Node<>(value);

        if (this.head==null){
            this.head = newElement;
        } else {
           Node<T> temp = this.head;
           while(temp.getNext()!=null)
               temp = temp.getNext();
           temp.setNext(newElement);
        }
        this.size++;
    }

    /**Agrega un elemento al inicio de la lista
     * @param value  Valor a agregar
     */
    public void addFirst(T value){
        Node<T> newElement = new Node<>(value);
        newElement.setNext(this.head);
        this.head = newElement;
        this.size++;
    }

    /**Elimina el último elemento de la lista
     * @return  true si se pudo eliminar, false en caso contrario
     */
    public boolean deleteLast(){
        if (this.head!=null){
            Node<T> temp = this.head;
            while(temp.getNext().getNext()!=null){
                temp = temp.getNext();
            }
            temp.setNext(null);
            this.size--;
            return true;
        }
        return false;
    }

    /**
     *  Elimina el primer elemento de la lista
     * @return  true si se pudo eliminar, false en caso contrario
     */
    public boolean deleteFirst(){
        if (this.head != null){
            this.head = this.head.getNext();
            this.size--;
            return true;
        }
        return false;
    }

    /**
     * Elimina en elemeno específico de la lista
     * @param element  elemento a eliminar
     * @return true si se pudo eliminar, false en caso contrario
     */
    public boolean remove(T element){
        if (element == this.head.getValue()){
            deleteFirst();
        } else {
            Node<T> temp = this.head.getNext();
            Node<T> prev = this.head;

            while(temp!= null){
                if (temp.getValue() == element){
                    prev.setNext(temp.getNext());
                    this.size--;
                    return true;
                } else
                    prev = temp;
                    temp = temp.getNext();
            }
        }
        return false;
    }

    /**
     * Método que imprime la lista
     * @return Retorna un string con los elementos de la lista
     */
    @Override
    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        Node<T> temp = this.head;
        if (temp == null){
            buffer.append("]");
            return buffer.toString();
        }else {
            int i = 0;
            while (temp != null) {
                buffer.append(temp.getValue());
                if(i!=size-1)
                    buffer.append(", ");
                else
                    buffer.append("]");
                temp = temp.getNext();
                i++;
            }
        }
        return buffer.toString();
    }

    /**
     * Se obtiene el valor a partir de una posición dada
     * @param index  Índice para buscar el elemento
     * @return elemento
     */
    public T get(int index){
        Node<T> tmp = this.head;
        for(int i=0; i<index  && tmp.getNext()!= null; i++){
            tmp = tmp.getNext();
        }
        return tmp.getValue();
    }

    /**
     * Método que retorna un nodo en una posición dada
     * @param index  posición donde se encuentra el nodo
     * @return el nodo
     */
    public Node<T> acces_index(int index) {
        Node<T> tmp = this.head;
        for (int i = 0; i < index && tmp.getNext() != null; i++) {
            tmp = tmp.getNext();
        }
        return tmp;
    }

    /**
     * @return retorna el tamño de la lista
     */
    public int getSize() {
        return size;
    }

    /**
     * @param element Elemento actual
     * @return Nodo que se encuentra a la par
     */
    public T nextOf(T element) {
        Node<T> temp = head;
        for (int i=0; i<size; i++) {
            if (temp.getValue() == element) {
                if (temp.getNext() == null) {
                    return head.getValue();
                } else {
                    return temp.getNext().getValue();
                }
            }
            temp = temp.getNext();
        }
        return null;
    }


    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);


        System.out.println(list);

        System.out.println(list.nextOf(1));
        System.out.println(list.nextOf(2));
        System.out.println(list.nextOf(3));
        System.out.println(list.nextOf(4));

        System.out.println(list);

    }
}
