# xpath_downloader
配合xpath使用的图片下载器。

### 背景介绍

在Chrome使用xpath助手后得到想要的图片链接。RT`↓`

![](http://media.mtjmtj7.cn/images/2019-10-01_4a5e7bc6-0e15-4fa9-92b5-83e567ae642d.png)

此时不想再Python巴拉巴拉的一顿操作(因为不熟练...

正好为了以后方便实用所以打算用Java做成GUI

界面如下`↓`：

![](http://media.mtjmtj7.cn/images/2019-10-01_5a94f34d-76f5-4dcb-b9b4-30c19cdb4206.png)

### 需求

1. 复制得到的链接是 n行的，处理换行符
2. 以原有文件名保存文件，使用正则表达式分割链接
3. 选择文件夹，下载文件
4. 多线程下载加快下载速度

### 代码

```
package xpath_img_downloader;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
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

public class Client extends JFrame {

	private JPanel contentPane;
	public JTextArea textArea;
	public JButton button;
	public JButton button_1;
	String filepath;

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
		setBounds(100, 100, 549, 401);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 10, 513, 271);
		contentPane.add(textArea);
		
		button = new JButton("选择文件夹");
		button.addActionListener(new ButtonActionListener());
		button.setBounds(10, 311, 243, 41);
		contentPane.add(button);
		
		button_1 = new JButton("下载");
		button_1.addActionListener(new Button_1ActionListener());
		button_1.setBounds(280, 311, 243, 41);
		contentPane.add(button_1);
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
			else {
				for (int i = 0; i < urlArray.length; i++) {
					String a[] = urlArray[i].split("[/]");
//					downloader(urlArray[i], path, a[a.length-1]);
					new downloaderThread(urlArray[i], path, a[a.length-1]).start();
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
	public void downloader (String link, String path, String filename) {
		
		URL url = null;
        try {
            url = new URL("http:" + link);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	class downloaderThread extends Thread{
		String link;
		String path;
		String filename;
		public downloaderThread(String link, String path, String filename) {
			super();
			this.link = link;
			this.path = path;
			this.filename = filename;
		}
		@Override
		public void run() {
			downloader(link, path, filename);
		}
	}
}

```

## 2019-10-06更新

- 更多样化的链接设置，在使用xpath得到的链接分为  `//img.mtjmtj7...`,`/upload/...`

对于第一种情况，我可以直接在链接前面加http或者https；对于第二种情况是属于服务器上传图片，我们可以输入域名前缀。

- 进度条提示。