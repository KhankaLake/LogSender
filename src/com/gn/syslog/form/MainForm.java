package com.gn.syslog.form;

import com.gn.syslog.run.Sender;
import com.gn.syslog.run.SenderBuilder;
import com.gn.syslog.util.IPUtil;
import com.gn.syslog.util.PersistenceUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Naive-GN
 * Created by guoning on 15/11/19.
 */
public class MainForm {
    private JPanel rootPanel;
    private JTextField host;
    private JTextField port;
    private JTextField total;
    private JTextField kbps;
    private JRadioButton udp;
    private JRadioButton tcp;
    private JRadioButton syslog;
    private JButton start;
    private JButton stop;
    private JTextArea template;
    private JTextArea msg;
    private JProgressBar progress;


    private Boolean isRunning = false;
    private FormBean formBean;
    private String lock = "";
    private String type = "udp";

    public MainForm() {

        formBean = PersistenceUtil.build(FormBean.class);
        if (formBean == null) {
            formBean = new FormBean();
        }
        setData(formBean);

        //开始
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    initStart();
                }
            }
        });
        //结束
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    initStop();
                }
            }
        });
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = ((JRadioButton) e.getSource()).getText().trim().toLowerCase();
            }
        };
        udp.addActionListener(listener);
        tcp.addActionListener(listener);
        syslog.addActionListener(listener);


    }

    /**
     * 结束
     */
    private void initStop() {
        synchronized (lock) {
            start.setEnabled(true);
            udp.setEnabled(true);
            tcp.setEnabled(true);
            syslog.setEnabled(true);
            stop.setEnabled(false);
            isRunning = false;
        }
        doStop();
    }

    private void doStop() {
        PersistenceUtil.save(formBean);
    }


    /**
     * 开始
     */
    private void initStart() {
        synchronized (lock) {
            start.setEnabled(false);
            udp.setEnabled(false);
            tcp.setEnabled(false);
            syslog.setEnabled(false);
            stop.setEnabled(true);
            msg.setText("");
            msg.setAutoscrolls(true);
            isRunning = true;
            if(isModified(formBean)){
                getData(formBean);
            }
        }
        doStart();
    }

    private void doStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Sender sender = SenderBuilder.build(formBean.getHost(), Integer.parseInt(formBean.getPort()), type);

                    long sleep = 1000 / Integer.parseInt(formBean.getKbps());
                    progress.setMaximum(Integer.parseInt(formBean.getTotal()) == 0 ? Integer.MAX_VALUE : Integer.parseInt(formBean.getTotal()));
                    progress.setMinimum(1);
                    int totle = progress.getMaximum();
                    int index = 0;
                    while (isRunning && totle > index) {
                        String log = buildLog();
                        sender.send(log);
                        progress.setValue(++index);
                        msg.insert(log + "\n",0);
                        Thread.sleep(sleep);
                    }
                    sender.close();
                    initStop();
                } catch (Exception e) {
                    msg.append(e.getMessage());
                }
            }
        }).start();
    }


    private String buildLog() {
        return formBean.getTemplate().replaceAll("\\$\\{IP\\}", IPUtil.getRandomIp4())
                .replaceAll("\\$\\{PORT\\}", IPUtil.getRandonPort() + "")
                .replaceAll("\\$\\{INT\\}", IPUtil.getInt() + "")
                .replaceAll("\\$\\{TIME\\}", IPUtil.getTime());

    }


    public void setData(FormBean data) {
        host.setText(data.getHost());
        port.setText(data.getPort());
        total.setText(data.getTotal());
        kbps.setText(data.getKbps());
        template.setText(data.getTemplate());
        msg.setText(data.getMsg());
    }

    public void getData(FormBean data) {
        data.setHost(host.getText());
        data.setPort(port.getText());
        data.setTotal(total.getText());
        data.setKbps(kbps.getText());
        data.setTemplate(template.getText());
        data.setMsg(msg.getText());
    }

    public boolean isModified(FormBean data) {
        if (host.getText() != null ? !host.getText().equals(data.getHost()) : data.getHost() != null) return true;
        if (port.getText() != null ? !port.getText().equals(data.getPort()) : data.getPort() != null) return true;
        if (total.getText() != null ? !total.getText().equals(data.getTotal()) : data.getTotal() != null) return true;
        if (kbps.getText() != null ? !kbps.getText().equals(data.getKbps()) : data.getKbps() != null) return true;
        if (template.getText() != null ? !template.getText().equals(data.getTemplate()) : data.getTemplate() != null)
            return true;
        if (msg.getText() != null ? !msg.getText().equals(data.getMsg()) : data.getMsg() != null) return true;
        return false;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
