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
    private int level;
    private List<Integer> levelValues;
    private int decision;

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

        for (int i = 0; i <= links.size(); i++)
            values.add(-1);

        values.set(id, val);

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        Log.write(name, " edges " + sb.toString());
        level = 0;
        levelValues = new ArrayList<>();

        for (int i = 0; i <= links.size(); i++)
            levelValues.add(0);

        decision = 0;

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

            for (Integer i : linkMap.keySet()) {

                if (skip.contains(i)) {
                    Log.write(name, "Skipping " + i + " message");
                    linkMap.get(i).sendMsg(id, new Message(null, null));
                    continue;
                }

                Message msg = new Message(values, level);
                linkMap.get(i).sendMsg(id, msg);
            }
        } else {

            int newLevel = Integer.MAX_VALUE;
            for (Integer i : linkMap.keySet()) {

                Message msg = linkMap.get(i).readMsg(id);
                if (msg.getMsg() == null) {
                    Log.write(name, "No message received from node-" + i + " in round " + round);
                    continue;
                }

                List<Integer> msgVal = msg.getMsg();
                for (int j = 0; j < msgVal.size(); j++) {
                    if (j != id && values.get(j) == -1 && msgVal.get(j) != -1)
                        values.set(j, msgVal.get(j));
                }

                Integer le = msg.getLevel();
                levelValues.set(i, le);
            }

            for (Integer lev : levelValues)
                newLevel = Math.min(newLevel, lev);

            level = newLevel + 1;
            levelValues.set(id, level);

            for (Integer i : linkMap.keySet()) {
                if (!skip.contains(i)) {
                    Message msg = new Message(values, level);
                    linkMap.get(i).sendMsg(id, msg);
                } else
                    linkMap.get(i).sendMsg(id, new Message(null, null));
            }

            if (round == ParseInput.rounds - 1) {
                if (level >= Master.k && makeDecision() == 1)
                    decision = 1;
            }
        }
    }

    int makeDecision() {
        for (Integer i : values)
            if (i != 1)
                return 0;
        return 1;
    }

    int getDecision() {
        return decision;
    }

    String formatList(List<Integer> list){
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            sb.append(i);
            sb.append(",");
        }

        if (list.size() != 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    String getValues() {
        return formatList(values);
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getLevel() {
        return level;
    }

    String getLevelValues(){
        return formatList(levelValues);
    }
}
