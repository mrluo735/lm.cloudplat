/**
 * @title StringUtilTest.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月27日上午9:46:10
 * @version v1.0
 */
package lm.com.framework;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

import lm.com.framework.mail.SmtpHelper;

/**
 * @author Administrator
 *
 */
public class EmailTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		Properties props = new Properties();      
		props.setProperty("mail.debug", "false");
        props.setProperty("mail.store.protocol", "pop3");      
        props.setProperty("mail.pop3.host", "pop.126.com");      
        Session session = Session.getDefaultInstance(props);      
        Store store = session.getStore("pop3");      
        store.connect("mrluo735", "");

        //通过POP3协议获得的Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        int messageCount = folder.getMessageCount();      
        System.out.println("INBOX:" + messageCount);        
        Message[] messages = folder.getMessages(); 
        for(Message message : messages){
        	MimeMessage mimeMessage = (MimeMessage) message;
        	System.out.println("messageNumber:" + message.getMessageNumber());
        	System.out.println("messageID:" + mimeMessage.getMessageID());
        }
        
//		Map<String, String> to = new HashMap<>();
//		to.put("mrcloud001@126.com", "mrcloud001");
//		Map<String, String> cc = new HashMap<>();
//		cc.put("mrcloud002@126.com", "mrcloud002");
//		
//		SmtpHelper smtpHelper = new SmtpHelper();
//		smtpHelper.setHost("smtp.126.com");
//		smtpHelper.setPort(25);
//		smtpHelper.setFrom("mrluo735@126.com");
//		smtpHelper.setFromName("mrluo735");
//		smtpHelper.setPassword("");
//		smtpHelper.connect();
//		smtpHelper.send("JavaMail", "这是我的第一封邮件", false, to, cc, null, null, 3);
		return;/*
		Properties props = new Properties();  
        // 开启debug调试  
        props.setProperty("mail.debug", "true");  
        // 发送服务器需要身份验证  
        props.setProperty("mail.smtp.auth", "true");  
        // 设置邮件服务器主机名  
        props.setProperty("mail.host", "smtp.126.com");  
        // 发送邮件协议名称  
        props.setProperty("mail.transport.protocol", "smtp");  
          
        // 设置环境信息  
        Session session = Session.getInstance(props);  
          
        // 创建邮件对象  
        Message msg = new MimeMessage(session);  
        msg.setSubject("JavaMail");  
        // 设置邮件内容  
        msg.setText("这是我的第一封邮件");
        // 设置发件人  
        msg.setFrom(new InternetAddress("mrluo735@126.com"));  
        msg.addRecipient(RecipientType.TO, new InternetAddress("mrcloud001@126.com", "mrcloud001"));
          
        Transport transport = session.getTransport();  
        // 连接邮件服务器  
        transport.connect("mrluo735", "");  
        // 发送邮件
        //Address[] addresss = new Address[] { new InternetAddress("mrcloud001@126.com", "mrcloud001") };
        transport.sendMessage(msg, msg.getAllRecipients());  
        // 关闭连接  
        transport.close();  */
	}
}
