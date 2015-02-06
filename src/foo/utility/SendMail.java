package foo.utility;
/*
 * Created on Feb 21, 2005
 *
 */

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.web.constants.MailType;

import foo.OnStartUpServlet;
import foo.SingletonFlag;

public class SendMail {
	private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

	private static final String SMTP_HOST_NAME = "smtp.126.com";
	private static final String SMTP_PORT = "465";
	private static final String emailMsgTxt = "State of HAC machine changed";
	private static final String emailSubjectTxt = "notify of HAC from default[网易126]";
	private static final String emailFromAddress = "fragiler@126.com";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static String[] sendTo = { "339633135@qq.com" };

/*	public static void main(String args[]) throws Exception {

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		new SendGmail().sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt, emailFromAddress);
		System.out.println("Sucessfully Sent mail to All Users");
	}*/
	
	private static String _emailSubjectTxt= (OnStartUpServlet.configMap
			.get("emailSubjectTxt") == null) ? emailSubjectTxt
			: OnStartUpServlet.configMap.get("emailSubjectTxt");
	private static String _emailMsgTxt = (OnStartUpServlet.configMap.get("emailMsgTxt") == null) ? emailMsgTxt
			: OnStartUpServlet.configMap.get("emailMsgTxt");
	private static String _emailFromAddress = (OnStartUpServlet.configMap.get("emailFromAddress")==null)?emailFromAddress:OnStartUpServlet.configMap.get("emailFromAddress");
	private static String[] _sendTo = (OnStartUpServlet.configMap.get("sendTo") == null) ? sendTo
			: OnStartUpServlet.configMap.get("sendTo").split(",");
	private static String _emailType =(OnStartUpServlet.configMap.get("emailType")==null)?emailSubjectTxt:OnStartUpServlet.configMap.get("emailType");
	private static String _name = OnStartUpServlet.configMap.get("authName");
	private static String _pwd = OnStartUpServlet.configMap.get("authPwd");

	private static String _SMTP_HOST_NAME =(OnStartUpServlet.configMap.get("SMTP_HOST_NAME")==null)?SMTP_HOST_NAME:OnStartUpServlet.configMap.get("SMTP_HOST_NAME");
	private static String _SMTP_PORT =(OnStartUpServlet.configMap.get("SMTP_PORT")==null)?SMTP_PORT:OnStartUpServlet.configMap.get("SMTP_PORT");
	private static String _SSL_FACTORY =(OnStartUpServlet.configMap.get("SSL_FACTORY")==null)?SSL_FACTORY:OnStartUpServlet.configMap.get("SSL_FACTORY");

	/**
	 * 邮件默认通知
	 * @throws Exception
	 */
	public void note() throws Exception {
//		sendTo = ((String)OnStartUpServlet.configMap.get("sendTo")).split(",");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		String state;
		String _emailDisplay;
		if(SingletonFlag.getInstance())
			state = "Open";
		else
			state = "Close";
		switch (MailType.getEnumByKey(_emailType)) {
		case GMAIL:
			_emailDisplay = MailType.GMAIL.getValue();
			_emailDisplay += _emailSubjectTxt + " from " + _emailDisplay;
			break;
		case NETEASE126:
			_emailDisplay = MailType.NETEASE126.getValue();
			_emailDisplay += _emailSubjectTxt + " from " + _emailDisplay;
			break;
		default:
			_emailDisplay = _emailSubjectTxt;
		}
		new SendMail().sendSSLMessage(_sendTo, _emailDisplay+":["+state+"]", _emailMsgTxt+":["+state+"]", _emailFromAddress);
		logger.debug("Sucessfully Sent mail to All Users");
	}

	public void sendSSLMessage(String recipients[], String subject, String message, String from)
			throws MessagingException {
		boolean debug = false;

		Properties props = new Properties();
		switch (MailType.getEnumByKey(_emailType)) {
		case GMAIL:
			_SMTP_HOST_NAME = "smtp.gmail.com";
			break;
		case NETEASE126:
			_SMTP_HOST_NAME = "smtp.126.com";
			break;
		}
		props.put("mail.smtp.host", _SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", _SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", _SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", _SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(_name, _pwd);
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}
}
