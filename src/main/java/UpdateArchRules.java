import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateArchRules extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ArchFileReader myFileReader = new ArchFileReader(e.getProject().getBasePath());
        if (myFileReader.getFileWithRules() != null){

            ArrayList<String[]> rules = new ArrayList<>();
            try {
                ArrayList<String> rulesFromFile = myFileReader.getRulesFromFile();
                for (String str: rulesFromFile) {
                    rules.add(str.split("\\."));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            ListOfRulesSingleton instance = ListOfRulesSingleton.getInstance(null);
            instance.setRules(rules);
            int i = 10;
        }
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}

