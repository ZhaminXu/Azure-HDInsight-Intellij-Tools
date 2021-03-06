package com.microsoft.azure.hdinsight.projects;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by guizha on 8/21/2015.
 */
public class HDInsightActionsComponent implements ApplicationComponent {
    @Override
    public void initComponent() {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup projectPopupGroup = (DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_PROJECT_VIEW_POPUP);
        projectPopupGroup.add(actionManager.getAction("Actions.SubmitSparkAction"), Constraints.FIRST);
    }

    @Override
    public void disposeComponent() {
    // do nothing
    }

    @NotNull
    @Override
    public String getComponentName() {
       return this.getClass().getName();
    }
}
