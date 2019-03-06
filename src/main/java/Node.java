import java.util.*;

/*
The idea is to keep on sending and receiving list of values for n rounds. In each round, update your list of values
based on the received values.
 */
public class Node implements Runnable {

    private Thread thread;
    private int id, round;
    private List<Integer> values;
    private Map<Integer, Link> linkMap;
    private String name;
    private Set<Integer> skip = new HashSet<>();

    Node(int id, List<Link> links, int val) {
        this.id = id;
        round = 0;
        linkMap = new HashMap<>();
        values = new ArrayList<>();
        name = "node-" + id;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Link l : links) {
            linkMap.put(l.linkedTo(id), l);
            sb.append(l.toString());
            sb.append(" ");
        }

        for (int i = 0; i <= links.size(); i++) {
            values.add(-1);
        }

        values.set(id, val);

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        Log.write(name, " edges " + sb.toString());
    }

    void setSkip(Set<Integer> skip) {
        this.skip = skip;
    }

    void start() {
        Log.write(name, " round :" + round);
        thread = new Thread(this, String.valueOf(id));
        thread.start();
    }

    void join() {
        try {
            thread.join();
            Log.write(name, " finish round :" + round);
            round++;
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {

        if (round == 0) {

            int ct = 0;
            for (Integer i : linkMap.keySet()) {
                ct++;
                if (skip.contains(ct)) {
                    Log.write(name,"Skipping " + ct + " message");
                    continue;
                }

                Message msg = new Message(values);
                linkMap.get(i).sendMsg(id, msg);
            }
        } else {

            for (Integer i : linkMap.keySet()) {
                Message msg = linkMap.get(i).readMsg(id);
                if (msg == null) {
                    Log.write(name, "No message received from node-" + i + " in round " + round);
                    continue;
                }

                List<Integer> msgVal = msg.getMsg();
                for (int j = 0; j < msgVal.size(); j++) {
                    if (j != id && values.get(j) == -1 && msgVal.get(j) != -1)
                        values.set(j, msgVal.get(j));
                }
            }

            int ct = 0;
            for (Integer i : linkMap.keySet()) {
                if (!skip.contains(ct)) {
                    Message msg = new Message(values);
                    linkMap.get(i).sendMsg(id, msg);
                }

                ct++;
            }
        }
    }

    String getValues() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : values) {
            sb.append(i);
            sb.append(",");
        }

        if (values.size() != 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }
}
