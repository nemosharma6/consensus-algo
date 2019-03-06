import java.util.concurrent.ArrayBlockingQueue;

public class Link {
    private int node1, node2;
    private ArrayBlockingQueue<Message> node1Writer;
    private ArrayBlockingQueue<Message> node2Writer;

    Link(int n1, int n2) {
        node1 = n1;
        node2 = n2;
        int msgCapacity = 1;
        node1Writer = new ArrayBlockingQueue<>(msgCapacity);
        node2Writer = new ArrayBlockingQueue<>(msgCapacity);
    }

    void sendMsg(int writer, Message msg) {

        try {
            if (writer == node1) node1Writer.put(msg);
            if (writer == node2) node2Writer.put(msg);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    Message readMsg(int reader) {

        if (reader == node1) return node2Writer.poll();
        else return node1Writer.poll();
    }

    int linkedTo(int id) {
        return (id == node1) ? node2 : node1;
    }

    @Override
    public String toString() {
        return "(" + node1 + "---" + node2 + ")";
    }

    int getNode1() {
        return node1;
    }

    int getNode2() {
        return node2;
    }
}
