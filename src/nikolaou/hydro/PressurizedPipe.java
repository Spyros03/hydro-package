package nikolaou.hydro;

public class PressurizedPipe {

    private double discharge;
    private double diameter;
    private double energyLosses;
    private final double length;
    private final double ks;
    private final double viscosity;

    public PressurizedPipe(double discharge, double diameter, double energyLosses, double length,
                           double ks, double viscosity) {
        this.discharge = discharge;
        this.diameter = diameter;
        this.energyLosses = energyLosses;
        this.length = length;
        this.ks = ks;
        this.viscosity = viscosity;
    }

    public PressurizedPipe(double discharge, double diameter, double energyLosses, double length){
        this(discharge, diameter, energyLosses, length, 0.001, PressurizedPipes.vWater);
    }

    public PressurizedPipe(String unknown, double data1, double data2, double length,
                           double ks, double viscosity) throws Exception{
        switch (unknown.toUpperCase()) {
            case "DISCHARGE" -> {
                this.discharge = -1;
                this.diameter = data1;
                this.energyLosses = data2;
            }
            case "DIAMETER" -> {
                this.discharge = data1;
                this.diameter = -1;
                this.energyLosses = data2;
            }
            case "ENERGYLOSSES" -> {
                this.discharge = data1;
                this.diameter = data2;
                this.energyLosses = -1;
            }
            default -> throw new Exception("Invalid unknown value. Try discharge, diameter or energylosses.");
        }
        this.length = length;
        this.ks = ks;
        this.viscosity = viscosity;
    }

    public PressurizedPipe(String unknown, double data1, double data2, double length) throws Exception{
        this(unknown, data1, data2, length, 0.001, PressurizedPipes.vWater);
    }

    public double getDischarge() {
        if (discharge < 0) {
            discharge = PressurizedPipes.discharge(length, ks, energyLosses, diameter);
        }
        return discharge;
    }

    public double getVelocity(){
        return PressurizedPipes.velocity(getDiameter(), getDischarge());
    }

    public double getReynolds(){
        return PressurizedPipes.reynoldsNumber(getVelocity(), getDiameter(), getViscosity());
    }

    public double getDiameter(){
        if (diameter < 0){
            diameter = PressurizedPipes.diameter(length, ks, energyLosses, discharge);
        }
        return diameter;
    }

    public double getEnergyLosses(){
        if (energyLosses < 0){
            energyLosses = PressurizedPipes.energyLosses(length, ks, discharge, diameter);
        }
        return energyLosses;
    }

    public double getLength(){
        return length;
    }

    public double getKs(){
        return ks;
    }

    public double getViscosity(){
        return viscosity;
    }
}
