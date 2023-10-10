package nikolaou.hydro;

import java.lang.Math;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public final class PressurizedPipes {

    public final static double g = 9.81;
    public final static double mWater = 1e-3;
    public final static double vWater = 1.1e-6;
    public final static double rWater = 1000;

    public static double darcyWeisbach(double V, double R, double f){
        //Returns the slope of energy height for every type of pipe using the Darcy-Weisbach equation.
        return (f * V * V) / (4 * R * 2 * g);
    }

    public static double darcyWeisbackCW(double V, double D, double ks){
        //Returns the slope of energy line for circular water pipes using the Darcy-Weisback and Colebrook-White.
        return darcyWeisbach(V, D/4, colebrookWhite(reynoldsNumber(V, D), D, ks));
    }

    public static double colebrookWhite(double Re, double D, double ks){
        //Returns the f coefficient for the Darcy-Weisbach equation using the Colebrook-White equation.
        double currentF;
        double newF = 0.02;
        int count = 0;
        do {
            currentF = newF;
            newF = Math.pow(1/(-2 * Math.log10(((ks/D)/3.71) + 2.51 / (Re * Math.sqrt(currentF)))), 2);
            count++;
        } while((count < 5 || Math.abs(currentF - newF) > 1e-6) && count < 100);
        return newF;
    }

    public static double energyLosses(double L, double ks, double Q, double D){
        //Returns the energyLoses of a pipe using the Darcy-Weisbach, Colebrook-White equation.
        double V = 4 * Q / (Math.PI * D * D);
        return L * darcyWeisbackCW(V, D, ks);
    }

    public static double discharge(double L, double ks, double hf, double D){
        //Returns the discharge of a pipe using the Darcy-Weisbach, Colebrook-White equation.
        double Ref = Math.sqrt(2 * g * hf / L) * Math.pow(D , 1.5) / vWater;
        double f = Math.pow(1/(-2 * Math.log10(((ks/D)/3.71) + 2.51 / Ref)), 2);
        double V = Math.sqrt(2 * g * hf * D / (L * f));
        return V * Math.PI * D * D / 4;
    }

    public static double diameter(double L, double ks, double hf, double Q){
        //Returns the diameter of a pipe given the Darcy-Weisback and Colebrook-White equation.
        double currentF;
        double newF = 0.02;
        int count = 0;
        double D;
        do {
            currentF = newF;
            D = Math.pow(currentF * 8 * L * Q * Q / (hf * Math.PI * Math.PI * g), 0.2);
            double Re = 4 * Q / (vWater * Math.PI * D);
            newF = Math.pow(1/(-2 * Math.log10(((ks/D)/3.71) + 2.51 / (Re * Math.sqrt(currentF)))), 2);
            count++;
        } while((count < 5 || Math.abs(currentF - newF) > 1e-6) && count < 100);
        return D;
    }

    public static double reynoldsNumber(double V, double L, double v){
        //Returns the Reynolds number for any fluid in any flow.
        return V * L / v ;
    }

    public static double reynoldsNumber(double V, double L){
        //Returns the Reynolds number of water in a flow.
       return reynoldsNumber(V, L, vWater);
    }
}
