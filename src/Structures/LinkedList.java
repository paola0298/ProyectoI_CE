package Structures;

public class LinkedList<T> {

    private int size;
    private Node<T> head;

    public Node<T> getHead() {
        return head;
    }

    public LinkedList(){
        this.size = 0;
        this.head = null;
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

    public boolean deleteElement(T element){
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

    public void printList(){
        Node<T> temp = this.head;
        while (temp!=null){
            System.out.println(temp.getValue());
            temp = temp.getNext();
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.addFirst(100);
        list.addFirst(200);
        list.deleteElement(100);
        list.printList();
    }
}
