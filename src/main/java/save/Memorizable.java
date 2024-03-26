package save;

public interface Memorizable {
    /**
     * Method to store class data locally
     */
    void memorize();

    /**
     * Method to extract stored data and recreate the class
     *
     * @throws WindowInitException - exception occurred during window initialization
     */
    void dememorize() throws WindowInitException;
}
