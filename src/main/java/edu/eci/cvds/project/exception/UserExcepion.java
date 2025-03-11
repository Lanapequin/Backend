package edu.eci.cvds.project.exception;

public class UserExcepion extends Exception{
    public UserExcepion(String message){
        super(message);
    }

    public static class UserNotFoundException extends UserExcepion{
        public UserNotFoundException(String message){
            super(message);
        }
    }

    public static class UserIncorrectPasswordException extends UserExcepion{
        public UserIncorrectPasswordException(String message){
            super(message);
        }
    }
}