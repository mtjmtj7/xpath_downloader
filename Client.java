package xpath_img_downloader;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.border.TitledBorder;
import javax.swing.JProgressBar;
import java.awt.Toolkit;

public class Client extends JFrame {

	private JPanel contentPane;
	public JButton button;
	public JButton button_1;
	String filepath;
	public JScrollPane scrollPane;
	public JTextArea textArea;
	public JPanel panel;
	public JRadioButton rdbtnHttp;
	public JRadioButton rdbtnHttps;
	public JRadioButton radioButton;
	public JTextField textField;
	ButtonGroup group;
	public JRadioButton radioButtonNull;
	public JProgressBar progressBar;
	int number = 0;
	public JPanel panel_1;
	public JLabel label;
	public JLabel lb_success;
	public JLabel lb_defeat;
	public JLabel label_2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setTitle("xpath图片下载器_mtjmtj7");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
			
		button = new JButton("选择文件夹");
		button.addActionListener(new ButtonActionListener());
		button.setBounds(10, 400, 243, 41);
		contentPane.add(button);
		
		button_1 = new JButton("下载");
		button_1.addActionListener(new Button_1ActionListener());
		button_1.setBounds(280, 400, 243, 41);
		contentPane.add(button_1);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 513, 246);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u94FE\u63A5\u5934", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 266, 513, 56);
		contentPane.add(panel);
		panel.setLayout(null);
		
		rdbtnHttp = new JRadioButton("HTTP");
		rdbtnHttp.setBounds(6, 23, 71, 23);
		panel.add(rdbtnHttp);
		
		rdbtnHttps = new JRadioButton("HTTPS");
		rdbtnHttps.setBounds(79, 23, 71, 23);
		panel.add(rdbtnHttps);
		
		radioButton = new JRadioButton("自定义");
		radioButton.setBounds(266, 23, 79, 23);
		panel.add(radioButton);
		
		radioButtonNull = new JRadioButton("无");
		radioButtonNull.setBounds(169, 23, 71, 23);
		panel.add(radioButtonNull);
		
		group =new ButtonGroup();
		group.add(rdbtnHttp);
		group.add(rdbtnHttps);
		group.add(radioButton);
		group.add(radioButtonNull);
		
		textField = new JTextField();
		textField.setBounds(351, 23, 138, 23);
		panel.add(textField);
		textField.setColumns(10);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 332, 513, 14);
		contentPane.add(progressBar);
		
		panel_1 = new JPanel();
		panel_1.setBounds(10, 353, 514, 37);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		label = new JLabel("下载成功");
		label.setBounds(10, 0, 56, 37);
		panel_1.add(label);
		
		lb_success = new JLabel("0");
		lb_success.setBounds(67, 11, 54, 15);
		panel_1.add(lb_success);
		
		lb_defeat = new JLabel("0");
		lb_defeat.setBounds(203, 11, 54, 15);
		panel_1.add(lb_defeat);
		
		label_2 = new JLabel("下载失败");
		label_2.setBounds(146, 0, 56, 37);
		panel_1.add(label_2);

	}
	//下载
	private class Button_1ActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String urls = textArea.getText();
			String urlArray[] = urls.split("[\\n]");
			String path = filepath;
			if(path == null) {
				JOptionPane.showMessageDialog(contentPane, "请先选择文件夹！");
			}
			else if(group.isSelected(null)) {
				JOptionPane.showMessageDialog(contentPane, "请选择链接头！");
			}
			else {
				String header = "";
				if(rdbtnHttp.isSelected()) header = "http";
				else if(rdbtnHttps.isSelected()) header = "https";
				else if (radioButton.isSelected())	header = textField.getText();
				else header = "";
				number = urlArray.length;
				progressBar.setMaximum(number);
				for (int i = 0; i < number; i++) {
					String a[] = urlArray[i].split("[/]");
//					downloader(urlArray[i], path, a[a.length-1]);
					new downloaderThread(header, urlArray[i], path, a[a.length-1]).start();
				}
				
			}
		}
	}
	//选择文件夹
	private class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser jChooser = new JFileChooser();
			jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int res = jChooser.showSaveDialog(contentPane);
			if(res == JFileChooser.APPROVE_OPTION){
				filepath = jChooser.getSelectedFile().getAbsolutePath();
			}
		}
	}
	//下载器
	public boolean downloader (String header, String link, String path, String filename) {
		
		URL url = null;
        try {
        	String s = "";
        	if(header == "" || header.equals("") || header == null) s=link;
        	else s = header + ":" + link;
            url = new URL(s);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }
            File f = new File(path+"/"+filename);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
 
            byte[] buffer = new byte[1024];
            int length;
 
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
	}
	//下载线程
	class downloaderThread extends Thread{
		String header;
		String link;
		String path;
		String filename;
		public downloaderThread(String header, String link, String path, String filename) {
			super();
			this.header = header;
			this.link = link;
			this.path = path;
			this.filename = filename;
		}
		@Override
		public void run() {
			boolean val = downloader(header, link, path, filename);
			//下载成功计数
			if(val)  downSuccess();
			else  downDefeat();
			//进度条
			setProValue();
		}
	}
	//下载成功计数器
	public synchronized void downSuccess(){
		int curr=new Integer(lb_success.getText());
		curr++;
		lb_success.setText(String.valueOf(curr));
	}
	//下载失败计数器
	public synchronized void downDefeat(){
		int curr=new Integer(lb_defeat.getText());
		curr++;
		lb_defeat.setText(String.valueOf(curr));
	}
	//进度条
	public synchronized void setProValue(){
		int curr = new Integer(lb_success.getText()) + new Integer(lb_defeat.getText());
		progressBar.setValue(curr);
		if(curr == progressBar.getMaximum()) {
			JOptionPane.showMessageDialog(contentPane, "下载完成！");
			//进度条清零
			progressBar.setValue(0);
			//清空链接面板
			textArea.setText("");
			//自动打开文件夹
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("rundll32 url.dll FileProtocolHandler "+ filepath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
