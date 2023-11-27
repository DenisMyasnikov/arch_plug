import com.intellij.codeInspection.*;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class ArchInspection extends AbstractBaseJavaLocalInspectionTool {

    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitIdentifier(PsiIdentifier identifier) {
                long m = System.currentTimeMillis();
                super.visitIdentifier(identifier);
                PsiElement parent = identifier.getParent();
                if (parent instanceof PsiClass){
                    PsiClass aClass = (PsiClass) parent;
                    ListOfRulesSingleton instance = ListOfRulesSingleton.getInstance(null);
                    ArrayList<String[]> rules = instance.getRules();
                    for (String [] str: rules){
                        ArrayList<String> rule = new ArrayList(Arrays.asList(str));
                        String s = ClassesRuleChecker.checkRuleWithClass(rule, aClass);
                        if (s != null)
                        {
                            String message = new String();
                            for (int i = 0; i < str.length; i++) {
                                message = message + str[i]+ " ";
                            }
                            holder.registerProblem(identifier, message);
                        }
                    }
                    long n = System.currentTimeMillis()-m;
                    long sec = TimeUnit.MILLISECONDS.toSeconds(n);
                }
            }
        };
    }
}
