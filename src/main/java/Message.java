import java.util.ArrayList;
import java.util.List;

/*
store the message and the nodes communicating through the message
 */
class Message {

    private List<Integer> msg;
    private Integer level;

    Message(List<Integer> l, Integer level) {
        if (l == null) {
            msg = null;
            level = null;
        } else {
            msg = new ArrayList<>(l);
            this.level = level;
        }
    }

    List<Integer> getMsg() {
        return msg;
    }

    public Integer getLevel() {
        return level;
    }
}
