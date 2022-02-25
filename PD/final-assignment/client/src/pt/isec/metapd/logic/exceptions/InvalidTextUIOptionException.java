package pt.isec.metapd.logic.exceptions;

public class InvalidTextUIOptionException extends Exception{
    public InvalidTextUIOptionException(){
        super("##### InvalidTextUIOptionException ##### - The chosen option in the TextUI isn't correct.");
    }
}
