package Logic;

import org.apache.commons.lang3.RandomStringUtils;

public class Game {
    String idGame;

    public void create_ID (){
        this.idGame = RandomStringUtils.randomAlphanumeric(6);
    }
    public void tokens(){

    }






//
//    public Player access_by_id(String player_id){
//        Player element = null;
//        for (int i = 0; i <= players.getSize(); i++){
//            element = players.get(i);
//            if (element.getplayerId() != player_id){
//                System.out.println("IF");
//            } else{
//                System.out.println("ELSE");
//                break;
//            }
//
//        }
//        System.out.println(element);return element;
////
//    }
//    public boolean hasTop_Points(Player poss_winner){
//        for (int i = 0; i <= players.getSize(); i++){
//            Player element = players.get(i);
//            if (poss_winner.getScore() <= players.getCurrent().getScore()){
//                return false;
//
//            }else{
//                break;
//            }
//
//
//        }return true;
//
//    }
}
