package com.gn.syslog;

import com.gn.syslog.form.MainForm;
import com.gn.syslog.form.SettingForm;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

/**
 * Naive-GN
 * Created by guoning on 15/11/1.
 */
public class StartUpToolWindow implements ToolWindowFactory {

//    ConfigForm configForm = new ConfigForm();

//    SettingForm settingForm = new SettingForm();
    MainForm mainForm = new MainForm();

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        //获取上下文工厂
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //获取窗体上下文
        Content content = contentFactory.createContent(mainForm.getRootPanel(), "", false);
        //将窗体上下文加入到toolWindow
        toolWindow.getContentManager().addContent(content);
    }
}
