public enum PhysicalStatus {
    STRONG(12), USUAL(6), WEAK(4);

    private double koeff;

    PhysicalStatus(double k) {
        koeff = k;
    }

    double getKoeff() { return koeff; }
}
