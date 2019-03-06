import java.util.List;

/*
store the message and the nodes communicating through the message
 */
class Message {

    private List<Integer> msg;

    Message(List<Integer> l) {
        msg = l;
    }

    List<Integer> getMsg() {
        return msg;
    }

}
