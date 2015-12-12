package cmu.banana.classdiscuz.exception;

/**
 * Exception when happened in sign up activity
 */
public class SignUpException extends Exception{
    private int errorno;
    private String errormsg;

    public SignUpException(int errorno, String errormsg) {
        super();
        this.errorno = errorno;
        this.errormsg = errormsg;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String erromsg) {
        this.errormsg = erromsg;
    }

    public int getErrorno() {
        return errorno;
    }

    public void setErrorno(int errorno) {
        this.errorno = errorno;
    }


}
