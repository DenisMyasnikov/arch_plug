import java.util.ArrayList;

public class ListOfRulesSingleton {
    private static volatile ListOfRulesSingleton instance;

    private ArrayList<String[]> rules;

    private ListOfRulesSingleton(ArrayList<String[]> rules) {
        this.rules = rules;
    }

    public static ListOfRulesSingleton getInstance(ArrayList<String[]> rules) {

        ListOfRulesSingleton localInstance = instance;
        if (localInstance == null) {
            synchronized (ListOfRulesSingleton.class) {
                localInstance = instance;
                if (localInstance == null && rules != null) {
                    instance = new ListOfRulesSingleton(rules);
                }
            }
        }
        return localInstance;
    }

    public void setRules(ArrayList<String[]> rules) {
        this.rules = rules;
    }

    public ArrayList<String[]> getRules() {
        return rules;
    }
}

