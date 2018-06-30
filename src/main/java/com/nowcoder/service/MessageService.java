package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import com.sun.xml.internal.ws.assembler.MetroTubelineAssembler;
import org.omg.PortableServer.AdapterActivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
        }
    public List<Message> getConversationDetial(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetial(conversationId,offset,limit);
    }
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int delMessage(int messageId){
        return messageDAO.delMessage(messageId);
    }

}

