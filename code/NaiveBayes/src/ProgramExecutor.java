public class ProgramExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(!args[0].equals("-f")){
			System.out.println("File Path argument is missing...Usage [-f filePath]");
		}
		Algorithm alg = new Algorithm(args[1]);
		alg.crossValidation();
	}
}
