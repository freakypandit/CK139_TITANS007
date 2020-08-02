package com.thirstymission.stepcounter;


public class Accelerometer {
    public float X;
    public float Y;
    public float Z;
    public double R;


    public Accelerometer(float[] event) {
        X = event[0];
        Y = event[1];
        Z = event[2];
        R = Math.sqrt(X*X + Y*Y + Z*Z);
    }

    public Number toNumber() {
        Number number = R;
        return number;
    }
}
