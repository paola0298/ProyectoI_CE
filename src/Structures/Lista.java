package Structures;

/**
 * Lista doblemente enlanzada
 *
 * @author HazelMartinez
 * @version 1.0
 * @since 22/03/02019
 * @param <T>
 */
    public class Lista<T> {
        private int size;
        private Nodo<T> head;

    /**
     *
     * @param valor
     */
    public void insertar(T valor){
        //This method adds a object in the list
            Nodo<T> nuevoNodo = new Nodo<T>();
            nuevoNodo.guardarDato(valor);
            if(this.head == null){
                this.head = nuevoNodo;
                head.nuevaReferencia(null);
            }else{
                Nodo<T> temp;
                temp = this.head;
                while(temp.siguiente() != null){
                    temp = temp.siguiente();
                    //System.out.println(this.head.obtenerDato());
                }
                nuevoNodo.setPrevious(temp);
                temp.nuevaReferencia(nuevoNodo);
                this.size++;
            }
        }

        public void eliminar() {
            Nodo<T> temp = new Nodo<T>();
            temp = this.head;
            this.head = head.siguiente();
            temp = null;
            size--;
        }

    /**
     *
     * @param player
     */
    public void deleteNode(T player){
        //This method deletes a player in the playersList
            Nodo<T> temp = new Nodo<T>();
            temp = this.head;
            Nodo<T> temp2;
            while(temp != player){
                if(temp.siguiente()==null){
                    return;
                }
                temp = temp.siguiente();
            }temp2 = temp.getPrevious();
            temp2.setNext(temp2.siguiente().siguiente());

        }

        public void print(){
            int i;
            i= 0;
            Nodo<T> temp;
            temp = this.head;
            while(temp.siguiente() != null) {
                //System.out.println("Valor actual: " + temp.obtenerDato());
                //System.out.println("Valor siguiente: " + temp.siguiente().obtenerDato());
                if(temp.getPrevious() != null) {
                    //System.out.println("Valor previo: " + temp.getPrevious().obtenerDato());
                }
                temp = temp.siguiente();
                i++;
            }
            //System.out.println("Valor penultimo: " + temp.getPrevious().obtenerDato());
            //System.out.println("Valor final: " + temp.obtenerDato());

        }
        public T retornarValor(int posicion) {
            int indice;
            indice = 0;
            Nodo<T> temp;
            temp = this.head;
            while (indice != posicion) {
                temp = temp.siguiente();
                indice++;
            }
            //System.out.println(temp.obtenerDato());
            return temp.obtenerDato();
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

