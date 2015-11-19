package com.gn.syslog.form;

import com.gn.syslog.run.Sender;
import com.gn.syslog.run.SenderBuilder;
import com.gn.syslog.run.UdpClientSocket;
import com.gn.syslog.util.IPUtil;
import com.gn.syslog.util.PersistenceUtil;
import org.jetbrains.annotations.NotNull;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogIF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

/**
 * Naive-GN
 * Created by guoning on 15/11/1.
 */
public class SettingForm {
    public JPanel rootPanel;
    public JTextField ip_port;
    public JTextField total;
    public JTextField kbps;
    public JButton startButton;
    public JPanel infoPanel;
    public JTextArea sysLogTemp;
    private JLabel msf;
    private JProgressBar bar;
    private JButton stopButton;
    private JRadioButton TCPRadioButton;
    private JRadioButton UDPRadioButton;
    private JRadioButton syslogRadioButton;
    private JTextArea msgArea;


    private SettionBean settionBean;

    private Boolean isRunning = false;

    private String logType = "tcp";


    private Thread syslogThread = null;
    private String control = "";

    public SettingForm() {
        settionBean = PersistenceUtil.build(SettionBean.class);
        if (settionBean == null) {
            settionBean = new SettionBean();
        }
        setData(settionBean);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (syslogThread == null || isModified(settionBean)) {
                    getData(settionBean);
                    PersistenceUtil.save(settionBean);
                    syslogThread = getThread();
                }
                if (isRunning == null || !isRunning) {
                    start();
                } else {
                    pause();
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        TCPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logType = "tcp";
            }
        });
        UDPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logType = "udp";
            }
        });
        syslogRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logType = "syslog";
            }
        });
    }


    /**
     * 开始
     */
    private void start() {
        synchronized (control) {
            isRunning = true;
            stopButton.setEnabled(true);
            TCPRadioButton.setEnabled(false);
            UDPRadioButton.setEnabled(false);
            syslogRadioButton.setEnabled(false);
            startButton.setText("暂停");
            msgArea.setText("");
            if (syslogThread.isAlive()) {
                control.notifyAll();
            } else {
                syslogThread.start();
            }
        }
    }

    private void pause() {
        synchronized (control) {
            isRunning = false;
            startButton.setText("继续");
        }
    }

    /**
     * 结束
     */
    private void stop() {
        synchronized (control) {
            isRunning = null;
            syslogThread = null;
        }
    }

    private void reset() {
        stopButton.setEnabled(false);
        TCPRadioButton.setEnabled(true);
        UDPRadioButton.setEnabled(true);
        syslogRadioButton.setEnabled(true);
        msf.setText("Naive-GN");
        bar.setValue(0);
        startButton.setText("开始/暂停");
    }


    @NotNull
    private Thread getThread() {

        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    msf.setText(settionBean.getIp() + ":" + settionBean.getPort());
                    Sender sender = SenderBuilder.build(settionBean.getIp(), settionBean.getPort(), logType);
                    long sleep = 1000 / settionBean.getKbps();
                    bar.setMaximum(settionBean.getTotal() == 0 ? Integer.MAX_VALUE : settionBean.getTotal());
                    bar.setMinimum(1);
                    int totle = bar.getMaximum();
                    int index = 0;
                    while (isRunning != null && totle > index) {
                        if (isRunning) {
                            String log = buildLog();
                            sender.send(log);
                            bar.setValue(++index);
                            msgArea.append(log + "\n");
                            Thread.sleep(sleep);
                        } else {
                            synchronized (control) {
                                control.wait();
                            }
                        }
                    }
                    sender.close();
                    reset();
                } catch (Exception e) {
                    msgArea.setText(e.getMessage());
                }

            }
        });

    }


    private String buildLog() {
        return sysLogTemp.getText().replaceAll("\\$\\{IP\\}", IPUtil.getRandomIp4())
                .replaceAll("\\$\\{PORT\\}", IPUtil.getRandonPort() + "")
                .replaceAll("\\$\\{INT\\}", IPUtil.getInt() + "")
                .replaceAll("\\$\\{TIME\\}", IPUtil.getTime());

    }


    public void setData(SettionBean data) {
        ip_port.setText(data.getIp_port());
        total.setText(data.getTotal() + "");
        kbps.setText(data.getKbps() + "");
        sysLogTemp.setText(data.getSysLogTemp());
    }

    public void getData(SettionBean data) {
        data.setIp_port(ip_port.getText());
        data.setTotal(Integer.parseInt(total.getText()));
        data.setKbps(Integer.parseInt(kbps.getText()));
        data.setSysLogTemp(sysLogTemp.getText());
    }

    public boolean isModified(SettionBean data) {
        if (ip_port.getText() != null ? !ip_port.getText().equals(data.getIp_port()) : data.getIp_port() != null)
            return true;
        if (total.getText() != null ? !total.getText().equals(data.getTotal() + "") : data.getTotal() >= 0)
            return true;
        if (kbps.getText() != null ? !kbps.getText().equals(data.getKbps() + "") : data.getKbps() > 0)
            return true;
        if (sysLogTemp.getText() != null ? !sysLogTemp.getText().equals(data.getSysLogTemp()) : data.getSysLogTemp() != null)
            return true;
        return false;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("SettingForm");
        frame.setContentPane(new SettingForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
