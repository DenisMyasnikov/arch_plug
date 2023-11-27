import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClassesRuleChecker {


    public static String checkRuleWithClass(ArrayList<String> rule, PsiClass aClass) {
        int i = 0;
        if (rule.get(i).contentEquals("classes")) {
            if (i < rule.size()) {
                switch (rule.get(i + 1)) {
                    case ("should"):
                        rule.remove(0);
                        return afterShould(rule, aClass);
                    case ("that"):
                        rule.remove(0);
                        return afterThat(rule, aClass);
                }
            }
        }
        return null;
    }

    private static String afterShould(ArrayList<String> rul, PsiClass aClass) {
        if (rul.size() > 1) {
            boolean shouldMeanTrue = true;
            if (rul.get(1).contains("bePrivate")) {
                rul.remove(0);
                return classBePrivate(aClass, rul);
            } else if (rul.get(1).contains("bePublic")) {
                rul.remove(0);
                return classBePublic(aClass, rul);
            } else if (rul.get(1).contains("beAnnotatedWith")) {
                rul.remove(0);
                return classBeAnnoted(rul, aClass, shouldMeanTrue);
            } else if (rul.get(1).contains("resideInAPackage")) {
                rul.remove(0);
                return residesInPackage(rul, aClass, shouldMeanTrue);
            } else if (rul.get(1).contains("implement")) {
                rul.remove(0);
                return implement(rul, aClass, shouldMeanTrue);
            } else if (rul.get(1).contains("haveSimpleNameContaining")) {
                rul.remove(0);
                return haveSimpleNameContaining(rul, aClass, shouldMeanTrue);
            }
        }
        return null;
    }

    private static String afterThat(ArrayList<String> rul, PsiClass aClass) {
        boolean thatMeanFalse = false;
        if (rul.size() > 1) {
            if (rul.get(1).contains("resideInAPackage")) {
                rul.remove(0);
                return residesInPackage(rul, aClass, thatMeanFalse);
            } else if (rul.get(1).contains("implement")) {
                rul.remove(0);
                return implement(rul, aClass, thatMeanFalse);
            } else if (rul.get(1).contains("haveSimpleNameContaining")) {
                rul.remove(0);
                return haveSimpleNameContaining(rul, aClass, thatMeanFalse);
            } else if (rul.get(1).contains("areAnnotatedWith")) {
                rul.remove(0);
                return classBeAnnoted(rul, aClass, thatMeanFalse);
            }
        }
        return null;
    }

    private static String haveSimpleNameContaining(ArrayList<String> rul, PsiClass aClass, boolean status) {
        String[] split = rul.get(0).split("\\(");
        String strName = split[1].split("\\)")[0];
        if (!status) {
            if (rul.size() > 1) {
                if (aClass.getName().contains(strName)) {
                    rul.remove(0);
                    return afterShould(rul, aClass);
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            if (!aClass.getName().contains(strName)) {
                return "Error";
            }
        }
        return null;
    }

    private static String implement(ArrayList<String> rul, PsiClass aClass, boolean status) {
        String[] split = rul.get(0).split("\\(");
        String strImpl = split[1].split("\\)")[0];
        if (!status) {
            if (rul.size() > 1) {
                PsiReferenceList implementsList = aClass.getImplementsList();
                PsiJavaCodeReferenceElement[] referenceElements = implementsList.getReferenceElements();
                for (PsiJavaCodeReferenceElement element : referenceElements) {
                    if (element.getText().contentEquals(strImpl)) {
                        rul.remove(0);
                        return afterShould(rul, aClass);
                    }
                }
                return null;
            }
            return null;
        } else {

            PsiReferenceList implementsList = aClass.getImplementsList();
            PsiJavaCodeReferenceElement[] referenceElements = implementsList.getReferenceElements();
            for (PsiJavaCodeReferenceElement element : referenceElements) {
                if (element.getText().contentEquals(strImpl)) {
                    return null;
                }
            }
            return "Error";

        }
    }

    private static String residesInPackage(ArrayList<String> rul, PsiClass aClass, boolean status) {
        String[] split = rul.get(0).split("\\(");
        String packages = split[1].split("\\)")[0];

        if (!status) {
            if (rul.size() > 1) {
                PsiFile containingFile = aClass.getContainingFile();
                if (containingFile instanceof PsiJavaFile) {
                    PsiJavaFile javaFile = (PsiJavaFile) containingFile;
                    if (javaFile.getPackageName().contains(packages)) {
                        switch (rul.get(1)) {
                            case ("should"):
                                rul.remove(0);
                                return afterShould(rul, aClass);
                            case ("or"):
                                break;
                            case ("and"):
                                break;
                        }
                    } else if (rul.get(1).contentEquals("or")) {

                    }
                }
            }
            return null;
        } else {
            PsiFile containingFile = aClass.getContainingFile();
            if (containingFile instanceof PsiJavaFile) {
                PsiJavaFile javaFile = (PsiJavaFile) containingFile;
                if (javaFile.getPackageName().contains(packages)) {
                    return "error";
                }
            }
        }
        return null;
    }

    private static String classBeAnnoted(ArrayList<String> rul, PsiClass aClass, boolean status) {
        String[] split = rul.get(0).split("\\(");
        String annotation = split[1].split("\\)")[0];
        PsiAnnotation[] annotations = aClass.getAnnotations();
        if (!status) {
            if (rul.size() > 1) {
                for (PsiAnnotation a : annotations) {
                    if (a.getNameReferenceElement().getText().contentEquals(annotation)) {
                        rul.remove(0);
                        return afterShould(rul, aClass);
                    }
                }
            }
            return null;
        } else {
            for (PsiAnnotation a : annotations) {

                if (a.getNameReferenceElement().getText().contentEquals(annotation)) {
                    rul.remove(0);
                    return null;
                }
            }
            return "Error";
        }
    }

    private static String classBePublic(PsiClass aClass, ArrayList<String> rul) {
        PsiModifierList modifierList = aClass.getModifierList();
        if (modifierList.hasModifierProperty("private")) {
            if (rul.size() > 1) {
                switch (rul.get(1)) {
                    case ("or"):
                        rul.remove(0);
                        break;
                }
            }
            return "error";
        } else if (rul.size() > 1) {
            switch (rul.get(0)) {
                case ("and"):
                    rul.remove(0);
                    break;
            }
        }
        return null;
    }

    private static String classBePrivate(PsiClass aClass, ArrayList<String> rul) {
        PsiModifierList modifierList = aClass.getModifierList();
        PsiReference @NotNull [] annotations = modifierList.getReferences();
        if (modifierList.hasModifierProperty("public")) {
            if (rul.size() > 1) {
                switch (rul.get(1)) {
                    case ("or"):
                        rul.remove(0);
                        break;
                }
            }
            return "error";
        } else if (rul.size() > 1) {
            switch (rul.get(0)) {
                case ("and"):
                    rul.remove(0);
                    break;
            }
        }
        return null;
    }
}

