import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

class ParseInput {

    static int processCount;
    static int rounds;
    static int dropInterval;
    static int[] values;

    static void readFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            Log.write("ParseInput", "Input File not found");
        } else {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                processCount = Integer.parseInt(br.readLine());
                rounds = Integer.parseInt(br.readLine());
                dropInterval = Integer.parseInt(br.readLine());
                values = new int[processCount];
                String[] val = (br.readLine()).split(" ");
                for (int i = 0; i < val.length; i++)
                    values[i] = Integer.parseInt(val[i]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
