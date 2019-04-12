package Structures;

public class  LinkedList<T> {

    private int size;
    private Node<T> head;

    public LinkedList(){
        this.size = 0;
        this.head = null;
    }

    public Node<T> getHead() {
        return head;
    }

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

    public void addFirst(T value){
        Node<T> newElement = new Node<>(value);
        newElement.setNext(this.head);
        this.head = newElement;
        this.size++;
    }

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

    public boolean deleteFirst(){
        if (this.head != null){
            this.head = this.head.getNext();
            this.size--;
            return true;
        }
        return false;
    }

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

    public T get(int index){
        Node<T> tmp = this.head;
        for(int i=0; i<index  && tmp.getNext()!= null; i++){
            tmp = tmp.getNext();
        }
        return tmp.getValue();
    }

    public Node<T> acces_index(int index) {
        Node<T> tmp = this.head;
        for (int i = 0; i < index && tmp.getNext() != null; i++) {
            tmp = tmp.getNext();
        }
        return tmp;
    }

    public int getSize() {
        return size;
    }

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
