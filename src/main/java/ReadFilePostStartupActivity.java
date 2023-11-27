import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.ArrayList;


public class ReadFilePostStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ArchFileReader myFileReader = new ArchFileReader(project.getBasePath());
        if (myFileReader.getFileWithRules() != null){
            try {
                ArrayList<String[]> rules = new ArrayList<>();
                ArrayList<String> rulesFromFile = myFileReader.getRulesFromFile(); //Считывание правил
                for (String str: rulesFromFile) {
                    rules.add(str.split("\\."));
                }
                ListOfRulesSingleton.getInstance(rules);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

