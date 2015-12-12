package cmu.banana.classdiscuz.util;

/**
 * Convert the focus time to focus level
 */
public class FocusTranslate {
    public static int time2Level(int time){
        return time / 100 + 1;
    }
}
