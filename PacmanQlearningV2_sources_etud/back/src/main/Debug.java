package main;

public class Debug {

	 public final static boolean DEBUG = true;
	 
	 public static void err(String message) {
		 if(DEBUG) {
			 String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			 String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			 String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			 int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			 
			 System.err.println(className + "." + methodName + "():" + lineNumber + " : " + message);
		 }
	 }

	 public static void info(String message) {
		 if(DEBUG) {
			 String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			 String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			 String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			 int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			 
			 System.out.println(className + "." + methodName + "():" + lineNumber + " : " + message);
		 }
	 }
}