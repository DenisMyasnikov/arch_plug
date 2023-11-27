import java.io.*;
import java.util.ArrayList;

public class ArchFileReader {
    private File fileWithRules = null;

    public ArchFileReader(String basePath) {
        File file = new File(basePath);
        if (file.exists()){
            String[] list = file.list();
            for (String pathTofile: list) {
                if (pathTofile.contains("ArchRule")){
                    fileWithRules = new File(basePath + "/" +pathTofile);
                    boolean exists = fileWithRules.exists();
                }
            }
        }
    }

    public ArrayList<String>getRulesFromFile() throws IOException {
        FileReader fileReader = new FileReader(fileWithRules);
        BufferedReader reader = new BufferedReader(fileReader);
        String lineWithRule = reader.readLine();
        ArrayList <String> ruleLines = new ArrayList<>();
        while (lineWithRule != null){
            ruleLines.add(lineWithRule);
            lineWithRule = reader.readLine();
        }
        return ruleLines;
    }

    public File getFileWithRules() {
        return fileWithRules;
    }
}

