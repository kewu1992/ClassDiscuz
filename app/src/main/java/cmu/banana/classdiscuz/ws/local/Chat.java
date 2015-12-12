package cmu.banana.classdiscuz.ws.local;

import com.quickblox.chat.model.QBChatMessage;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

/**
 * Reference: ApplicationSingleton.java from http://quickblox.com/developers/Android#Download_Android_SDK
 */
public interface Chat {

    void sendMessage(QBChatMessage message) throws XMPPException, SmackException.NotConnectedException;

    void release() throws XMPPException;
}
