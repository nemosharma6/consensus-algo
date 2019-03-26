import java.util.ArrayList;
import java.util.List;

/*
store the message and the nodes communicating through the message
 */
class Message {

    private List<Integer> msg;

    Message(List<Integer> l) {
        if (l == null)
            msg = null;
        else
            msg = new ArrayList<>(l);
    }

    List<Integer> getMsg() {
        return msg;
    }

}
