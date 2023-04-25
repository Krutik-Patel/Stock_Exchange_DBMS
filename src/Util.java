class Thrie {
    private int stockUnits;
    private int accountID;
    private int chronoToken;
    public Thrie(int stockUnits, int accountID, int chronoToken) {
        this.stockUnits = stockUnits;
        this.accountID = accountID;
        this.chronoToken = chronoToken;
    }
    public int getStockUnits() { return stockUnits; }
    public int getaccountID() { return accountID; }
    public int getChronoToken() { return chronoToken; }
    public void setStockUnits(int stockUnits) { this.stockUnits = stockUnits; }
    public void setaccountID(int accountID) { this.accountID = accountID; }
    public void setChronoToken(int chronoToken) { this.chronoToken = chronoToken; }

}
