package org.se.lab;

/**
 * auxiliary class to display the euro value in the desired way
 *
 * @author rene
 */
class Euro {
    private long euro;
    private long cent;

    public Euro(long cents) {
        if (cents < 0){
            throw new IllegalArgumentException("Negative cent value is not allowed!");
        }
        euro = cents / 100;
        cent = cents % 100;
    }

    @Override
    public String toString() {
        // added leading zero for the cents
        return "EUR " + euro + "." + (cent<10? "0" : "") + cent;
    }
}
