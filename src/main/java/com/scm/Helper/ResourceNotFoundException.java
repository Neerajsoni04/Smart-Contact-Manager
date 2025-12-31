package com.scm.Helper;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg){
        super(msg);
    }

    public ResourceNotFoundException(){
        super("User Not Found!!!");
    }
}
