package com.prestigeww.hermes.Model;

public class Document {

    String docId;
    byte[] data;
    String messageId;

    Document(){

    }

    Document(String docId, byte[] data, String messageId){
        this.docId = docId;
        this.data = data;
        this.messageId = messageId;
    }

}
